package banking.management.service;

import banking.management.dto.LoanAccountDTO;

import java.util.List;

public interface LoanService {
    LoanAccountDTO createLoan(LoanAccountDTO loanAccountDTO);

    LoanAccountDTO getLoanById(Long loanId);

    LoanAccountDTO updateLoan(Long loanId, LoanAccountDTO loanAccountDTO);

    void deleteLoan(Long loanId);

}
