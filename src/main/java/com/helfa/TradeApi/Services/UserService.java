package com.helfa.TradeApi.Services;

import java.util.List;
import java.util.Optional;

import com.helfa.TradeApi.Entities.User;

public interface UserService {
	
	Optional<User> getUserById(Long id);
	
	User addOrUpdateUser(User user);
	
	Optional<User> deleteUser(Long id);
	
	Optional<User> getUserByEmail(String email);
	
	List<User> getAllUsers();
	
}
