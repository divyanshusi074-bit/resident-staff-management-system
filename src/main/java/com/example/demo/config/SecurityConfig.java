
package com.example.demo.config;

import com.example.demo.entity.Admin;
import com.example.demo.repository.AdminRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Encode a legacy plaintext admin password if necessary
    @Bean
    public CommandLineRunner encodeAdminPassword(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            Admin admin = adminRepository.findByUsername("admin")
                    .orElse(null);

            if (admin != null
                    && admin.getPassword() != null
                    && !admin.getPassword().startsWith("$2")) {

                admin.setPassword(
                        passwordEncoder.encode(admin.getPassword())
                );

                adminRepository.save(admin);

                System.out.println(
                        "Admin password encoded successfully!"
                );
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/error"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler((request, response, authentication) -> {
                            request.getSession().setAttribute(
                                    "loggedInUser",
                                    authentication.getName()
                            );

                            response.sendRedirect("/");
                        })
                        .failureUrl("/login?error")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        // CSRF protection remains enabled by default.
        // Logout must therefore be submitted using POST.
        return http.build();
    }
}
