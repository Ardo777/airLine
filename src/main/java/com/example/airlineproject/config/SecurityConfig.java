package com.example.airlineproject.config;

import com.example.airlineproject.entity.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;


import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final DataSource dataSource;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {


        httpSecurity.csrf(csrf ->
                csrf
                        .csrfTokenRepository(new HttpSessionCsrfTokenRepository())
        ).authorizeHttpRequests(authorize ->
                authorize
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/user/register", "/user/login", "/user/register/verification/**").permitAll()
                        .requestMatchers("/admin/**").hasAnyAuthority(UserRole.ADMIN.name())
                        .requestMatchers("/manager/**").hasAnyAuthority(UserRole.MANAGER.name())
                        .requestMatchers(HttpMethod.GET, "/css/**", "/js/**",
                                "/media/**", "/s/**", "/sass/**", "/staticAdminManager/**",
                                "/up/**", "/css2").permitAll()
                        .anyRequest()
                        .authenticated()
        ).formLogin(form ->
                form.loginPage("/user/login")
                        .loginProcessingUrl("/login")
                        .successForwardUrl("/login/successfully")
                        .defaultSuccessUrl("/login/successfully", true)
                        .failureUrl("/user/login?errorMessage=your username or password is incorrect")
        ).rememberMe(remember->
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
