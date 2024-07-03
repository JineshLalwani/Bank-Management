package banking.management.service;

import banking.management.dto.AccountDetailsDTO;

public interface AccountService {
    AccountDetailsDTO createAccount(AccountDetailsDTO accountDetailsDTO);
    AccountDetailsDTO getAccountById(Long accountId);
    AccountDetailsDTO updateAccount(Long accountId, AccountDetailsDTO accountDetailsDTO);
    void deleteAccount(Long accountId);
}
