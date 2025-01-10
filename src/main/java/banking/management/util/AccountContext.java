package banking.management.util;


public class AccountContext {
    private static final ThreadLocal<Long> currentAccountId = new ThreadLocal<>();

    public static void setCurrentAccountId(Long accountId) {
        currentAccountId.set(accountId);
    }

    public static Long getCurrentAccountId() {
        return currentAccountId.get();
    }

    public static void clear() {
        currentAccountId.remove();
    }
}

