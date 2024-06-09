package com.example.airlineproject.config;

import com.example.airlineproject.entity.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final DataSource dataSource;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf ->
                csrf
                        .csrfTokenRepository(new CookieCsrfTokenRepository())
        ).authorizeHttpRequests(authorize ->
                authorize
                        .requestMatchers("/","/user/register", "/user/login/**", "/user/register/verification/**",
                                "/user/login/successfully/**", "/user/codeVerification/**", "/user/forgetPassword/**",
                                "/user/recovery/**", "/user/account/restore", "/user/verificationRestore",
                                "/user/userVerificationPage", "/flight/list", "/flight/list/filter").permitAll()
                        .requestMatchers("/admin/**").hasAnyAuthority(UserRole.ADMIN.name())
                        .requestMatchers("/manager/**").hasAnyAuthority(UserRole.MANAGER.name())
                        .requestMatchers("/subscribe/**", "/news").hasAnyAuthority(UserRole.USER.name())
                        .requestMatchers(HttpMethod.GET, "/css/**", "/js/**",
                                "/media/**", "/s/**", "/sass/**", "/staticAdminManager/**",
                                "/up/**", "/css2").permitAll()
                        .anyRequest()
                        .authenticated()
        ).formLogin(login ->
                login.loginPage("/user/login")
                        .loginProcessingUrl("/user/login")
                        .successForwardUrl("/user/login/successfully")
                        .defaultSuccessUrl("/user/login/successfully", true)
                        .failureUrl("/user/login?errorMessage=Your username or password is incorrect")
        ).rememberMe(remember ->
                remember.tokenRepository(persistentTokenRepository())
                        .rememberMeParameter("remember-me")
                        .tokenValiditySeconds(86400)
        );
        return httpSecurity.build();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepo = new JdbcTokenRepositoryImpl();
        tokenRepo.setDataSource(dataSource);
        return tokenRepo;
    }

}
