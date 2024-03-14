package com.example.airlineproject.config;

import com.example.airlineproject.entity.enums.UserRole;
import com.example.airlineproject.security.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration

public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf ->
                csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository
                                .withHttpOnlyFalse())
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
                        .failureUrl("/login/error")
        ).logout(withDefaults());

        return httpSecurity.build();
    }


}
