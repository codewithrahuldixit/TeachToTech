package com.rahul.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

	private JwtRequestFilter jwtRequestFilter;

	public SpringSecurityConfig(JwtRequestFilter jwtRequestFilter) {
		this.jwtRequestFilter = jwtRequestFilter;
	}

	@SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
	    .csrf(AbstractHttpConfigurer::disable)
	    .authorizeHttpRequests(auth -> auth
	        // Public endpoints
	        .requestMatchers("/api/admin/**", "/api/otp/**").permitAll()
	        .requestMatchers(HttpMethod.GET, 
	            "/api/courses/add/**", "/api/courses/edit/**", "/api/view-image/**", "/editNotes.html", "api/topic/edit/**",
	            "/api/trainer/add/**", "/api/trainer/edit/**", "/api/trainer/delete/**",
	            "/category/**", "/preview/**", "/save-content", "/article/**", 
	            "/discoverarticles/**", "/success", "/articletypes", 
	            "/articlesByCategory", "/addassignment/**", "/topics", "/addnotes"
	        ).permitAll()
	        .requestMatchers(HttpMethod.POST, 
	            "/savenote", "/test", "/save-assignment", "/api/notes/**"
	        ).permitAll()
	        .requestMatchers(HttpMethod.DELETE, 
	            "/delete/**", "/api/trainer/delete/**"
	        ).permitAll()
	        .requestMatchers(
	            "/", "/index", "/courses", "/course-details/**", "/trainers", 
	            "/assets/**", "/contact", "/about", "/api/users/**", 
	            "/api/trainer/get", "/teachtotech-app/**", "/articlewriting/**", 
	            "/articlereview/**", "/save-content/**", "/articles/category/**", 
	            "/preview", "/adminpreview/**", "/articletypes", "/comments", 
	            "/savenote", "/test/**", "/topic/**", "/allnotes"
	        ).permitAll()

	        // Admin endpoints
	        .requestMatchers(
	            "/api/t2t/admin/transaction/**", 
	            "/api/courses/add/**", "/api/courses/edit/**", 
	            "/api/courses/delete/**", "/api/trainer/add/**", 
	            "/api/trainer/edit/**", "/api/trainer/delete/**", 
	            "/category/**", "/api/users/allusers/**"
	        ).hasRole("ADMIN")

	        // All other requests require authentication
	        .anyRequest().authenticated()
	    )
	    .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

	@Bean
	public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
