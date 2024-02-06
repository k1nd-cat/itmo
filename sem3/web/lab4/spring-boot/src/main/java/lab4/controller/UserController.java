package lab4.controller;

import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lab4.persistence.User;
import lab4.persistence.UserRepository;
import lab4.utils.Security;

@RestController
@RequestMapping("user")
public class UserController {
	
	private final UserRepository repository;

	UserController(UserRepository repository) {
		this.repository = repository;
	}

	/**
	 * Login and create token. 
	 */
	@PostMapping("/login")
	Map<String, String> login(@RequestBody User user) {
		String login = user.getLogin(), password = user.getPassword();
		if (!StringUtils.hasLength(StringUtils.trimAllWhitespace(login)) || !StringUtils.hasLength(StringUtils.trimAllWhitespace(password))) { 
			return Collections.singletonMap("error", "No login / password");
		}
		Optional<User> userOpt = repository.findById(login);
		if (!userOpt.isPresent()) { 
			return Collections.singletonMap("error", "User not found");
		}
		user = userOpt.get();
		Security security = Security.getSecurity();
		if (!security.checkPassword(user.getPassword(), password)) { 
			return Collections.singletonMap("error", "Wrong login / password");
		}
		String token = Base64.getUrlEncoder().encodeToString(new String(login + System.currentTimeMillis()).getBytes());
		user.setToken(token);
		repository.save(user);
		return Collections.singletonMap("token", token);
	}


	/**
	 * Register and create token.
	 */
	@PostMapping("/register")
	Map<String, String> register(@RequestBody User user) {
		String login = user.getLogin(), password = user.getPassword();
		login = StringUtils.trimAllWhitespace(login);
		if (!StringUtils.hasLength(login)) {
			return Collections.singletonMap("error", "Login is empty");
		}
		password = StringUtils.trimAllWhitespace(password);
		if (!StringUtils.hasLength(password) || password.length() < 4) {
			return Collections.singletonMap("error", "Password must have 4 or more symbols");
		}
		Optional<User> userOpt = repository.findById(login);
		if (userOpt.isPresent()) {
			return Collections.singletonMap("error", "User already exists");
		}
		password = Security.getSecurity().getPasswordHash(password);
		user.setLogin(login);
		user.setPassword(password);
		String token = Base64.getUrlEncoder().encodeToString(new String(login + System.currentTimeMillis()).getBytes());
		user.setToken(token);
		try {
			repository.save(user);
		} catch (Exception exc) {
			return Collections.singletonMap("error", "user already exists");
		}
		return Collections.singletonMap("token", token);
	}

	/**
	 * Logout. 
	 */
	@PostMapping("/logout")
	void logout(@RequestBody User user) {
		String token = user.getToken();
		if (!StringUtils.hasLength(StringUtils.trimAllWhitespace(token))) return;
		Optional<User> userOpt = repository.findByToken(token);
		if (!userOpt.isPresent()) return;
		user = userOpt.get();
		user.setToken(null);
		repository.save(user);
	}
	
	/**
	 * Check token. 
	 */
	@PostMapping("/check")
	Map<String, Boolean> check(@RequestBody User user) {
		String token = user.getToken();
		if (!StringUtils.hasLength(StringUtils.trimAllWhitespace(token))) return Collections.singletonMap("result", false);
		Optional<User> userOpt = repository.findByToken(token);
		return Collections.singletonMap("result", userOpt.isPresent());
	}

}
