package com.proj.expensetracker.security;


import com.proj.expensetracker.service.UserInfoService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Data
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig { 

	@Autowired
	private JwtAuthFilter authFilter; 

	// User Creation 
	@Bean
	public UserDetailsService userDetailsService() { 
		return new UserInfoService();
	} 

	// Configuring HttpSecurity 
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { 
		return http.csrf().disable() 
				.authorizeHttpRequests() 
				.requestMatchers("/auth/welcome", "/auth/register", "/auth/login","/auth/current-user").permitAll()
				.and()
				.authorizeHttpRequests().anyRequest().authenticated()
				.and()
				.sessionManagement() 
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
				.and() 
				.authenticationProvider(authenticationProvider()) 
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class) 
				.build(); 
	} 

	// Password Encoding 
	@Bean
	public PasswordEncoder passwordEncoder() { 
		return new BCryptPasswordEncoder(); 
	} 

	@Bean
	public AuthenticationProvider authenticationProvider() { 
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(); 
		authenticationProvider.setUserDetailsService(userDetailsService()); 
		authenticationProvider.setPasswordEncoder(passwordEncoder()); 
		return authenticationProvider; 
	} 

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { 
		return config.getAuthenticationManager(); 
	}




} 