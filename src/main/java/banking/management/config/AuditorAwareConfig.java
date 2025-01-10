package banking.management.config;

import banking.management.util.AccountContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
public class AuditorAwareConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}

class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Long accountId = AccountContext.getCurrentAccountId();
        if (accountId != null) {
            return Optional.of("AccID:" + accountId); // Return accountId as a String
        }
        return Optional.of("Unknown"); // Default value if accountId is not set
    }
}