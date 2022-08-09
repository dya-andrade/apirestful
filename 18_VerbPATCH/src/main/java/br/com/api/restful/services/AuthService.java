package br.com.api.restful.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.api.restful.data.vo.v1.security.AccountCredentialsVO;
import br.com.api.restful.data.vo.v1.security.TokenVO;
import br.com.api.restful.repositories.UserRepository;
import br.com.api.restful.security.jwt.JwtTokenProvider;

@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private UserRepository repository;

	public ResponseEntity<?> signin(AccountCredentialsVO data) {

		if (checkIfParamsIsNotNull(data))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");

		var tokenResponse = new TokenVO();

		try {

			var username = data.getUsername();
			var password = data.getPassword();

			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

			var user = repository.findByUsername(username);

			if (user != null) {
				tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());
			} else {
				throw new UsernameNotFoundException("Username " + username + " not found!");
			}

		} catch (Exception e) {
			throw new BadCredentialsException("Invalid username/password supplied!");
		}

		if (tokenResponse == null)
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		else
			return ResponseEntity.ok(tokenResponse);
	}

	public ResponseEntity<?> refreshToken(String username, String refreshToken) {

		if (checkIfParamsIsNotNull(username, refreshToken))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");

		var user = repository.findByUsername(username);

		var tokenResponse = new TokenVO();

		if (user != null) {
			tokenResponse = tokenProvider.refreshToken(refreshToken);
		} else {
			throw new UsernameNotFoundException("Username " + username + " not found!");
		}

		if(tokenResponse == null)
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
		else
			return ResponseEntity.ok(tokenResponse);
	}

	private boolean checkIfParamsIsNotNull(String username, String refreshToken) {
		return refreshToken == null || refreshToken.isBlank() || username == null || username.isBlank();
	}

	private boolean checkIfParamsIsNotNull(AccountCredentialsVO data) {
		return data == null || data.getUsername() == null || data.getUsername().isBlank() || data.getPassword() == null
				|| data.getPassword().isBlank();
	}
}
