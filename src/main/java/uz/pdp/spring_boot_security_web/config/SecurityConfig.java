package uz.pdp.spring_boot_security_web.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uz.pdp.spring_boot_security_web.service.AuthService;

import java.util.Properties;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
//@PropertySource("classpath:application.properties")
public class SecurityConfig {

    private final AuthService authService;
    private static final String[] TEST_LIST = new String[]{
            "/subject/{title}"
            , "/adminSubject/deleteSubject/{id}"
            , "/adminSubject/get/{id}"
            ,"/adminSubject/subjects"
            ,"/adminTopic/deleteTopic/{id}"
            ,"/adminTopic/topics"
            ,"/adminQuestion/deleteQuestion/{id}"
    };

    private static final String[] TEST_LIST_POST = new String[]{
            "/adminSubject/addSubject"
            ,"/adminTopic/addTopic"
            ,"/adminTopic/editTopic"
            ,"/adminQuestion/addQuestion"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, TEST_LIST).permitAll()
                .requestMatchers(HttpMethod.POST, TEST_LIST_POST).permitAll()
                .requestMatchers(HttpMethod.GET, "/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/user/add").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/user/verify/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .defaultSuccessUrl("/user")
                .and()
                .logout()
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID");

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(authService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
        return authenticationManagerBuilder.build();
    }

    @Bean
    @Qualifier("javasampleapproachMailSender")
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("abdulhafizxalimjonov@gmail.com");
        mailSender.setPassword("ekmgsedlyteijmdg");

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");
        return mailSender;
    }
}
