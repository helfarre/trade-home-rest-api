package com.helfa.TradeApi.Security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.helfa.TradeApi.Entities.User;
import com.helfa.TradeApi.Repositories.UserRepo;


@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepo repo;
	@Override
	//Locates the user based on the username.  
	// return UserDetails
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = repo.findByEmail(username);
		
		user.orElseThrow(() -> new UsernameNotFoundException("NotFound " + username));
		
		return user.map(MyUserDetails::new).get();
	}

}
