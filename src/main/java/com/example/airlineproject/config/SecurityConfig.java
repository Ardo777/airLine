package com.example.airlineproject.config;

import com.example.airlineproject.entity.enums.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;


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
                        .failureUrl("/user/login?errorMessage=your username or password is incorrect")
        );

        return httpSecurity.build();
    }


}
