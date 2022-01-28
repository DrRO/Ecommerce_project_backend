package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import javafx.embed.swing.JFXPanel;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	/*public static final Logger log = LogManager.getLogger(UserController.class);*/
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());

		//createUser log
		log.info("User created successfully and Username is: " + createUserRequest.getUsername());


		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);

		// password requirements and confirmation
		if(createUserRequest.getPassword().length()<7 || !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){

			// log.error("the built-in ERROR level");
			log.error("Error , CreateUser request failur due to Password is not valid !");

			return ResponseEntity.badRequest().build();
		}

		// calling of bCryptPasswordEncoder and encode password
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		userRepository.save(user);
		return ResponseEntity.ok(user);
	}
	/////////////////////////
}
