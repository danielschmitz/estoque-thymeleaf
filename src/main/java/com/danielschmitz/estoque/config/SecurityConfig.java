package com.danielschmitz.estoque.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login", "/register", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL que aciona o logout
                        .logoutSuccessUrl("/login?logout") // Para onde redirecionar após o logout
                        .invalidateHttpSession(true) // Invalida a sessão
                        .deleteCookies("JSESSIONID") // Deleta o cookie da sessão
                );

        return http.build();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                // Permite TODAS as requisições sem autenticação
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/**").permitAll()
//                )
//                // Desabilita o formulário de login, já que não é mais necessário
//                .formLogin(form -> form.disable())
//                // Desabilita a proteção CSRF, que também não é necessária neste modo
//                .csrf(csrf -> csrf.disable());
//
//        return http.build();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Define o codificador de senhas que usaremos. BCrypt é o padrão ouro.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}