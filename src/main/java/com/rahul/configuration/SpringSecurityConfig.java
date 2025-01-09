package com.rahul.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private CustomAuthenticationProvider customAuthenticationProvider;
    private JwtRequestFilter jwtRequestFilter;

    public SpringSecurityConfig(CustomAuthenticationProvider customAuthenticationProvider,
    JwtRequestFilter jwtRequestFilter) {
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.jwtRequestFilter=jwtRequestFilter;
    }

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/admin/**").permitAll()
                .requestMatchers("/api/t2t/admin/transaction/**").hasRole("ADMIN")
                .requestMatchers("api/courses/add/**","api/courses/edit/**",
                                "api/courses/delete/**","api/trainer/add/**",
                                "api/trainer/edit/**","api/trainer/delete/**").hasRole("ADMIN")
                .requestMatchers("/","/index","/courses","/course-details/**","/trainers","/assets/**","/contact","/about","api/users/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .authenticationProvider(customAuthenticationProvider)
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

 
}
