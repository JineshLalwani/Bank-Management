package banking.management.service;

import banking.management.dto.AccountDetailsDTO;
import banking.management.model.Account;
import banking.management.model.Details;
import banking.management.model.Loan;
import banking.management.model.User;
import banking.management.repository.AccountRepository;
import banking.management.repository.DetailsRepository;
import banking.management.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = Logger.getLogger(AccountServiceImpl.class.getName());

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DetailsRepository detailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public AccountDetailsDTO createAccount(AccountDetailsDTO accountDetailsDTO) {
        Account savedAccount = null;
        try {
            logger.info("Creating a new account...");

            Account account = new Account();
            account.setAccountType(accountDetailsDTO.getAccountType());
            account.setCurrentBalance(accountDetailsDTO.getCurrentBalance());
            account.setAccountStatus(accountDetailsDTO.getAccountStatus());
            account.setMonthlyInterest(accountDetailsDTO.getMonthlyInterest());
            account.setDateOpened(LocalDate.now());

            Optional<Details> optionalDetails = detailsRepository.findByBankIfsc(accountDetailsDTO.getBankIfsc());
            Details details = optionalDetails.orElseThrow(() -> new EntityNotFoundException("Details not found for IFSC: " + accountDetailsDTO.getBankIfsc()));
            account.setDetails(details);

            List<Long> userIds = accountDetailsDTO.getUserId();
            if (userIds != null && !userIds.isEmpty()) {
                List<User> users = userRepository.findByUserIdIn(userIds);
                account.setUsers(users);
                List<Account> accountList = new ArrayList<>();
                accountList.add(account);
                for (User userIndividual : users) {
                    userIndividual.setAccounts(accountList);
                }
            }

            savedAccount = accountRepository.save(account);
            logger.info("Account created successfully with ID: " + savedAccount.getAccountId());
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "EntityNotFoundException: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while creating the account: " + e.getMessage(), e);
            throw new RuntimeException("Unexpected error occurred while creating the account", e);
        }
        return mapToDTO(savedAccount);
    }

    @Override
    public AccountDetailsDTO getAccountById(Long accountId) {
        try {
            logger.info("Retrieving account with ID: " + accountId);
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + accountId));
            logger.info("Account retrieved successfully for ID: " + accountId);
            return mapToDTO(account);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "EntityNotFoundException: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while retrieving the account: " + e.getMessage(), e);
            throw new RuntimeException("Unexpected error occurred while retrieving the account", e);
        }
    }

    @Override
    @Transactional
    public AccountDetailsDTO updateAccount(Long accountId, AccountDetailsDTO accountDetailsDTO) {
        try {
            logger.info("Updating account with ID: " + accountId);
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + accountId));

            account.setAccountType(accountDetailsDTO.getAccountType());
            account.setCurrentBalance(accountDetailsDTO.getCurrentBalance());
            account.setAccountStatus(accountDetailsDTO.getAccountStatus());
            account.setMonthlyInterest(accountDetailsDTO.getMonthlyInterest());
            account.setDateOpened(LocalDate.now());

            Optional<Details> optionalDetails = detailsRepository.findByBankIfsc(accountDetailsDTO.getBankIfsc());
            Details details = optionalDetails.orElseThrow(() -> new EntityNotFoundException("Details not found for IFSC: " + accountDetailsDTO.getBankIfsc()));
            account.setDetails(details);

            List<Long> userIds = accountDetailsDTO.getUserId();
            if (userIds != null && !userIds.isEmpty()) {
                List<User> users = userRepository.findByUserIdIn(userIds);
                account.setUsers(users);
                List<Account> accountList = new ArrayList<>();
                accountList.add(account);
                for (User userIndividual : users) {
                    userIndividual.setAccounts(accountList);
                }
            }

            Account updatedAccount = accountRepository.save(account);
            logger.info("Account updated successfully with ID: " + updatedAccount.getAccountId());
            return mapToDTO(updatedAccount);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "EntityNotFoundException: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while updating the account: " + e.getMessage(), e);
            throw new RuntimeException("Unexpected error occurred while updating the account", e);
        }
    }

    @Override
    @Transactional
    public void deleteAccount(Long accountId) {
        try {
            logger.info("Deleting account with ID: " + accountId);
            if (!accountRepository.existsById(accountId)) {
                throw new EntityNotFoundException("Account not found with ID: " + accountId);
            }
            accountRepository.deleteById(accountId);
            logger.info("Account deleted successfully with ID: " + accountId);
        } catch (EntityNotFoundException e) {
            logger.log(Level.SEVERE, "EntityNotFoundException: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error occurred while deleting the account: " + e.getMessage(), e);
            throw new RuntimeException("Unexpected error occurred while deleting the account", e);
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void addingMonthlyInterest() {
        try {
            logger.info("Adding monthly Interest for all accounts...");

            List<Account> accounts = accountRepository.findAll();
            for (Account account : accounts) {
                float monthlyInterest = account.getMonthlyInterest();
                int addingAmount = account.getCurrentBalance()*(int)monthlyInterest/100;
                account.setCurrentBalance(account.getCurrentBalance()+addingAmount);

                accountRepository.save(account);

                logger.info("Monthly Interest added for account with ID: " + account.getAccountId());
            }

            logger.info("Monthly Interest adding process completed.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurred during Monthly Interest addition: " + e.getMessage(), e);
        }
    }

    private AccountDetailsDTO mapToDTO(Account account) {
        AccountDetailsDTO dto = new AccountDetailsDTO();
        dto.setAccountId(account.getAccountId());
        dto.setAccountType(account.getAccountType());
        dto.setCurrentBalance(account.getCurrentBalance());
        dto.setAccountStatus(account.getAccountStatus());
        dto.setMonthlyInterest(account.getMonthlyInterest());
        dto.setBankIfsc(account.getDetails().getBankIfsc());
        List<Long> userIds = account.getUsers().stream()
                .map(User::getUserId)
                .collect(Collectors.toList());
        dto.setUserId(userIds);
        return dto;
    }
}
