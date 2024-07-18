//package banking.management.service;
//
//import banking.management.dto.LoanAccountDTO;
//import banking.management.model.Loan;
//import banking.management.repository.LoanRepository;
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class TestServiceImpl extends TestService {
//
//    @Autowired
//    private LoanRepository loanRepository;
//
//    @Override
//    public LoanAccountDTO getLoanById(Long loanId) {
//        Loan loan = loanRepository.findById(loanId)
//                .orElseThrow(() -> new EntityNotFoundException("Loan not found with ID: " + loanId));
//
//        LoanAccountDTO loanAccountDTO = new LoanAccountDTO();
//        loanAccountDTO = mapToDTO(loan);
//        return loanAccountDTO;
//    }
//
//
//private LoanAccountDTO mapToDTO(Loan loan) {
//    LoanAccountDTO dto = new LoanAccountDTO();
//    dto.setLoanId(loan.getLoanId());
//    dto.setAccountId(loan.getAccount().getAccountId());
//    dto.setLoanType(loan.getLoanType());
//    dto.setLoanAmount(loan.getLoanAmount());
//    dto.setLoanStatus(loan.getLoanStatus());
//    dto.setRemainingAmount(loan.getRemainingAmount());
//    dto.setMonthlyEMI(loan.getMonthlyEMI());
//    return dto;
//}
//
//}
//
