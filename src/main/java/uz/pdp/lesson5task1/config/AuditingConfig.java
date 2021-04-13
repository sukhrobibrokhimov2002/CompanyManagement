package uz.pdp.lesson5task1.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.pdp.lesson5task1.entity.User;

import java.util.Optional;
import java.util.UUID;

public class AuditingConfig implements AuditorAware<UUID> {
    @Override
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null &&
                authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")) {
//            User user = (User) authentication.getPrincipal();
            return Optional.of(((User)authentication.getPrincipal()).getId());
        }
        return Optional.empty();
    }
}
