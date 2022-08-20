package br.com.api.restful.configs;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import br.com.api.restful.security.jwt.JwtConfigurer;
import br.com.api.restful.security.jwt.JwtTokenProvider;

@SuppressWarnings("deprecation")
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Bean
	public PasswordEncoder passwordEnconder() {
		//como encriptar senhas para fazer comparação com db
		
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		
		DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
		
		passwordEncoder.setDefaultPasswordEncoderForMatches(new Pbkdf2PasswordEncoder());
		
		return passwordEncoder;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic().disable()
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
			//significa que as sessões não vao armanezenar estado, nenhum registro das interações anteriores são salvos.
				.and()
					.authorizeRequests()
					.antMatchers("/auth/signin", "/auth/refresh", "/api-docs/**", "/swagger-ui.html**").permitAll()
					.antMatchers("/api/**").authenticated()
					.antMatchers("/users").denyAll()
				.and()
					.cors()
				.and()
				.apply(new JwtConfigurer(tokenProvider));
	}
}
