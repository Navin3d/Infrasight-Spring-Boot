package gmc.project.infrasight.authservice.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import gmc.project.infrasight.authservice.filter.AuthFilter;
import gmc.project.infrasight.authservice.services.AuthService;

@Component
@Configuration
public class SecurityConfig {

	private AuthenticationManager authManager;

	@Autowired
	private AuthConfig authConfig;
	@Autowired
	private AuthService authService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
		auth.userDetailsService(authService).passwordEncoder(bCryptPasswordEncoder);
		authManager = auth.build();
		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(authReq -> authReq.requestMatchers("/**").permitAll().anyRequest().authenticated())
			.headers(headers -> headers.frameOptions(opt -> opt.disable()).disable());
		http.addFilter(getAuthManager());
		http.authenticationManager(authManager);
		return http.build();
	}

	private AuthFilter getAuthManager() {
		AuthFilter authFilter = new AuthFilter(authConfig, authService, authManager);
		authFilter.setFilterProcessesUrl(authConfig.getAuthUrl());
		return authFilter;
	}

}
