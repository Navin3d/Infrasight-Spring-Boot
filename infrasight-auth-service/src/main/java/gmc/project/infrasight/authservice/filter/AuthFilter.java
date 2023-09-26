package gmc.project.infrasight.authservice.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import gmc.project.infrasight.authservice.configurations.AuthConfig;
import gmc.project.infrasight.authservice.entities.UserEntity;
import gmc.project.infrasight.authservice.models.LoginModel;
import gmc.project.infrasight.authservice.services.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthFilter extends UsernamePasswordAuthenticationFilter {
	
	private final AuthConfig authConfig;
	private final AuthService authService;
	
	public AuthFilter(AuthConfig authConfig, AuthService authService, AuthenticationManager authManager) {
		super.setAuthenticationManager(authManager);
		this.authConfig = authConfig;
		this.authService = authService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		try {
			LoginModel creds = new ObjectMapper().readValue(request.getInputStream(), LoginModel.class);
			return getAuthenticationManager().authenticate(
						new UsernamePasswordAuthenticationToken(creds.getUserName(), creds.getPassword())
					);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) {
		byte[] secretKey = authConfig.getJwtSecret().getBytes(StandardCharsets.UTF_8);
		Key hamacKey = new SecretKeySpec(secretKey, SignatureAlgorithm.HS256.getJcaName());
		String userName = ((User)authResult.getPrincipal()).getUsername();
		UserEntity foundUser = authService.findOneUser(userName);
		String jwtToken = Jwts.builder()
				.setSubject(foundUser.getId())
				.setIssuer(authConfig.getIssuer())
				.setExpiration(new Date(System.currentTimeMillis() + authConfig.getExpeiry()))
				.signWith(hamacKey)
				.compact();
		response.setHeader("Authorization", jwtToken);
	}

}
