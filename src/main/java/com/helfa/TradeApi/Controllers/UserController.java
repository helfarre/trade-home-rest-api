package com.helfa.TradeApi.Controllers;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.helfa.TradeApi.Entities.Role;
import com.helfa.TradeApi.Entities.User;
import com.helfa.TradeApi.Repositories.RoleRepo;
import com.helfa.TradeApi.Services.UserService;

@RestController
@CrossOrigin(origins = "https://tradehome.herokuapp.com", allowedHeaders = "*", exposedHeaders = "Authorization")  
@RequestMapping("/User")

public class UserController {
	

	@Autowired
	private UserService userService;
	@Autowired 
	private RoleRepo roleRepo;
	
	@GetMapping(value = "/Greeting")
	public ResponseEntity<?> greeting() {
		return new ResponseEntity<String>("Hello world", HttpStatus.OK);
	}
	
	@GetMapping(value = "/Users")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = userService.getAllUsers();
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Long id) {
		Optional<User> user = userService.getUserById(id);
		if (user.isPresent())
			return new ResponseEntity<User>(user.get(), HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	@PostMapping(value = "/addUser")
	@Transactional
	public ResponseEntity<?> addUser(@RequestBody User user) {
		Optional<User> user1= userService.getUserByEmail(user.getEmail());
		if(user1.isPresent()) {
			return new ResponseEntity<Integer> (-1, HttpStatus.OK);
		}
		else {
		user.setAutoTrade(true);
		Role role = roleRepo.getOne(new Long(1));
		user.setRole(role);
		user.setBalance(2000);
//		user.setPassword("helloworld");
		userService.addOrUpdateUser(user);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
	}
	@PutMapping(value = "/changeAutoStatus/{id}")
	public ResponseEntity<?> changeautTrade(@PathVariable Long id) {
		Optional<User> s =userService.getUserById(id);
		if (s.isPresent()) {
			User k = s.get();
			k.setAutoTrade(!k.isAutoTrade());
		userService.addOrUpdateUser(k);
		
			return new ResponseEntity<User>(k, HttpStatus.OK);
		}
		else 
			return  new ResponseEntity<Integer>(-1, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/deleteUser/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		Optional<User> user =userService.getUserById(id);
		if(user.isPresent()) {
			userService.deleteUser(id);
			return new ResponseEntity<User>(user.get(), HttpStatus.OK);
		}
		else 
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	
	
}
