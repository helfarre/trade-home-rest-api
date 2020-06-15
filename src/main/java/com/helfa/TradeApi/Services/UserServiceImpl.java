package com.helfa.TradeApi.Services;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helfa.TradeApi.Entities.User;
import com.helfa.TradeApi.Repositories.RoleRepo;
import com.helfa.TradeApi.Repositories.UserRepo;
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private RoleRepo roleRepo;
	
	@Override
	public Optional<User> getUserById(Long id) {
		
		return userRepo.findById(id);
	}

	@Override
	public User addOrUpdateUser(User user) {
		user.setRole(roleRepo.findById(new Long(1)).get());
		userRepo.saveAndFlush(user);
		return user;
	}

	@Override
	public Optional<User> deleteUser(Long id) {
		
		Optional<User> user = userRepo.findById(id);
		userRepo.deleteById(id);
		return user;
	}

	@Override
	public Optional<User> getUserByEmail(String email) {
		
		return userRepo.findByEmail(email);
	}

	@Override
	public List<User> getAllUsers() {
		
		return userRepo.findAll();
	}

}
