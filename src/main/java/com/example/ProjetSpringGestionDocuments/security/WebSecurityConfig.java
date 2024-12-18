package com.example.ProjetSpringGestionDocuments.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

        @Autowired
        private UserDetailsService uds;
 
        @Autowired
        private BCryptPasswordEncoder encoder;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/", "/ListeDocumentUser/AboutUs",
                                                                "/ListeDocumentUser", "/ListeDocumentUser/view/{id}",
                                                                "/home", "/contact", "/ListeDocumentUser/download/{id}", "/css/**",
                                                                "/webjars/**", "/images/**")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .formLogin((form) -> form
                                                .loginPage("/login")
                                                .permitAll()
                                                
                                                .successHandler((request, response, authentication) -> {
                                                        var savedRequest = (org.springframework.security.web.savedrequest.DefaultSavedRequest) request
                                                                        .getSession()
                                                                        .getAttribute("SPRING_SECURITY_SAVED_REQUEST");
                                                        if (savedRequest != null) {
                                                                response.sendRedirect(savedRequest.getRequestURL());
                                                        } else {
                                                                response.sendRedirect("/admin/home");
                                                        }
                                                }))
                                .logout((logout) -> logout.permitAll().logoutSuccessUrl("/"));

                return http.build();
        }

        @Bean
        public UserDetailsService userDetailsService(BCryptPasswordEncoder passwordEncoder) {
                UserDetails user = User.withUsername("admin")
                                .password(passwordEncoder.encode("admin123"))
                                //admin123
                                .roles("ADMIN")
                                .build();
                return new InMemoryUserDetailsManager(user);
        }
        @Bean
        public AuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
            authenticationProvider.setUserDetailsService(uds);
            authenticationProvider.setPasswordEncoder(encoder);
            return authenticationProvider;
    }
}
