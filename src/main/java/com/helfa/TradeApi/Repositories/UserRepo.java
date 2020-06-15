package com.helfa.TradeApi.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.helfa.TradeApi.Entities.User;

public interface UserRepo extends JpaRepository<User, Long>  {
	
	Optional<User> findByEmail(String Email);
	List<User> findAll();


}
