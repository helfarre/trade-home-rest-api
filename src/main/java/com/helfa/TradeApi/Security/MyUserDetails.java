package com.helfa.TradeApi.Security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.helfa.TradeApi.Entities.Role;
import com.helfa.TradeApi.Entities.User;

public class MyUserDetails implements UserDetails {
	
	// They simply store user information which is later encapsulated into Authentication objects. This allows non-security 
	//related user information (such as email addresses, telephone numbers etc) to be stored in a convenient location.
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private Boolean active;
	private List<GrantedAuthority> authorities;
	public MyUserDetails() {
		
	}

	public MyUserDetails(User user) {
		this.username=user.getEmail();
		this.password=user.getPassword();
		String roles="";
		this.active=true;
		
			 roles=roles+"ROLE_"+user.getRole()+",";
		 
		 this.authorities=Arrays.stream(roles.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
				 		
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return this.authorities;
		
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
