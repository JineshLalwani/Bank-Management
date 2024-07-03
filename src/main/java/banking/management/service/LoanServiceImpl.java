package banking.management.service;

import banking.management.dto.LoanAccountDTO;
import banking.management.model.Account;
import banking.management.model.Loan;
import banking.management.model.LoanStatus;
import banking.management.repository.AccountRepository;
import banking.management.repository.LoanRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountRepository accountRepository;

    private static final Logger logger = Logger.getLogger(LoanServiceImpl.class.getName());

    @Override
    @Transactional
    public LoanAccountDTO createLoan(LoanAccountDTO loanAccountDTO) {
        Loan savedLoan = null;
        try {
            logger.info("Creating a new loan...");

            Loan loan = new Loan();
            loan.setLoanType(loanAccountDTO.getLoanType());
            loan.setLoanAmount(loanAccountDTO.getLoanAmount());
            loan.setRemainingAmount(loanAccountDTO.getRemainingAmount());
            if(loanAccountDTO.getRemainingAmount()>0)
            {
                loan.setLoanStatus(LoanStatus.NOT_PAID);
            }
            else {
                loan.setLoanStatus(LoanStatus.PAID);
            }
            loan.setMonthlyEMI(loanAccountDTO.getMonthlyEMI());
            loan.setDateOpened(LocalDate.now());

            Optional<Account> account1 = accountRepository.findByAccountId(loanAccountDTO.getAccountId());
            Account account2 = account1.orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + loanAccountDTO.getAccountId()));
            loan.setAccount(account2);
            List<Loan> loans = account2.getLoans();
            loans.add(loan);
            long remainingLoanAmount = loanAccountDTO.getRemainingAmount();
            account2.setCurrentBalance(account2.getCurrentBalance() + (int) remainingLoanAmount);

            savedLoan = loanRepository.save(loan);
            logger.info("Loan created successfully with ID: " + savedLoan.getLoanId());
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "EntityNotFoundException: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while creating the loan: " + e.getMessage(), e);
            throw new RuntimeException("Unexpected error occurred while creating the loan", e);
        }
        return mapToDTO(savedLoan);
    }

    @Override
    public LoanAccountDTO getLoanById(Long loanId) {
        try {
            logger.info("Retrieving loan with ID: " + loanId);
            Loan loan = loanRepository.findById(loanId)
                    .orElseThrow(() -> new EntityNotFoundException("Loan not found with ID: " + loanId));
            logger.info("Loan retrieved successfully for ID: " + loanId);
            return mapToDTO(loan);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "EntityNotFoundException: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while retrieving the loan: " + e.getMessage(), e);
            throw new RuntimeException("Unexpected error occurred while retrieving the loan", e);
        }
    }

    @Override
    @Transactional
    public LoanAccountDTO updateLoan(Long loanId, LoanAccountDTO loanAccountDTO) {
        try {
            logger.info("Updating loan with ID: " + loanId);
            Loan loan = loanRepository.findById(loanId)
                    .orElseThrow(() -> new EntityNotFoundException("Loan not found with ID: " + loanId));

            long remainAmount=loan.getRemainingAmount();
            loan.setLoanType(loanAccountDTO.getLoanType());
            loan.setLoanAmount(loanAccountDTO.getLoanAmount());
            loan.setRemainingAmount(loanAccountDTO.getRemainingAmount());
            if(loanAccountDTO.getRemainingAmount()>0)
            {
                loan.setLoanStatus(LoanStatus.NOT_PAID);
            }
            else {
                loan.setLoanStatus(LoanStatus.PAID);
            }
            loan.setMonthlyEMI(loanAccountDTO.getMonthlyEMI());
            loan.setDateOpened(loan.getDateOpened());

            Optional<Account> account1 = accountRepository.findByAccountId(loanAccountDTO.getAccountId());
            Account account2 = account1.orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + loanAccountDTO.getAccountId()));
            loan.setAccount(account2);
            List<Loan> loans = account2.getLoans();
            loans.add(loan);
            account2.setLoans(loans);
            long remainingLoanAmount = loanAccountDTO.getRemainingAmount();
            account2.setCurrentBalance(account2.getCurrentBalance() - (int) remainAmount + (int) remainingLoanAmount);

            Loan updatedLoan = loanRepository.save(loan);
            logger.info("Loan updated successfully with ID: " + updatedLoan.getLoanId());
            return mapToDTO(updatedLoan);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "EntityNotFoundException: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while updating the loan: " + e.getMessage(), e);
            throw new RuntimeException("Unexpected error occurred while updating the loan", e);

        }
    }

    @Override
    @Transactional
    public void deleteLoan(Long loanId) {
        try {
            logger.info("Deleting loan with ID: " + loanId);
            if (!loanRepository.existsById(loanId)) {
                throw new EntityNotFoundException("Loan not found with ID: " + loanId);
            }
            loanRepository.deleteById(loanId);
            logger.info("Loan deleted successfully with ID: " + loanId);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "EntityNotFoundException: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while deleting the loan: " + e.getMessage(), e);
            throw new RuntimeException("Unexpected error occurred while deleting the loan", e);
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void deductMonthlyEMI() {
        try {
            logger.info("Deducting monthly EMI for all loans...");

            List<Loan> loans = loanRepository.findAll();
            for (Loan loan : loans) {
                float monthlyEMI = loan.getMonthlyEMI();
                long remainingAmount = loan.getRemainingAmount();
                if(remainingAmount <= 0) {
                    loan.setLoanStatus(LoanStatus.PAID);
                    loanRepository.save(loan);
                    continue;
                }
                remainingAmount -= (long) monthlyEMI;
                loan.setRemainingAmount(remainingAmount);
                Optional<Account> account1 = accountRepository.findByAccountId(loan.getAccount().getAccountId());
                Account account2 = account1.orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + loan.getAccount().getAccountId()));
                account2.setCurrentBalance(account2.getCurrentBalance() - (int) monthlyEMI);
                loanRepository.save(loan);

                logger.info("EMI deducted for loan with ID: " + loan.getLoanId());
            }

            logger.info("Monthly EMI deduction process completed.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurred during monthly EMI deduction: " + e.getMessage(), e);
        }
    }

    private LoanAccountDTO mapToDTO(Loan loan) {
        LoanAccountDTO dto = new LoanAccountDTO();
        dto.setLoanId(loan.getLoanId());
        dto.setAccountId(loan.getAccount().getAccountId());
        dto.setLoanType(loan.getLoanType());
        dto.setLoanAmount(loan.getLoanAmount());
        dto.setLoanStatus(loan.getLoanStatus());
        dto.setRemainingAmount(loan.getRemainingAmount());
        dto.setMonthlyEMI(loan.getMonthlyEMI());
        return dto;
    }
}
