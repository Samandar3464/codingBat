package uz.pdp.spring_boot_security_web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
//@EnableJpaAuditing
public class Auditing {
    @Bean
    public AuditorAware<String> auditorAware() {
        return new EntityListener();
    }
}
