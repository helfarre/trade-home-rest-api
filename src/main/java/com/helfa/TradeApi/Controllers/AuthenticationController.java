package com.helfa.TradeApi.Controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.helfa.TradeApi.Entities.User;
import com.helfa.TradeApi.Security.JwtUtil;
import com.helfa.TradeApi.Security.MyUserDetailsService;
import com.helfa.TradeApi.Security.RefreshToken;
import com.helfa.TradeApi.Security.RefreshTokenRepo;
import com.helfa.TradeApi.Security.RefreshTokenRequest;
import com.helfa.TradeApi.Security.jwtRequest;
import com.helfa.TradeApi.Security.jwtResponse;
import com.helfa.TradeApi.Services.UserService;
import com.fasterxml.uuid.Generators;

@RestController

public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private MyUserDetailsService userDetailsService;
	@Autowired
	private UserService userService;
	@Autowired
	private JwtUtil jwtTokenUtil;
	
	@Autowired
	private RefreshTokenRepo refreshRepo;
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody jwtRequest authenticationRequest) throws Exception {
		//faire l'authentification en utilsiation les donnes du formulaire login 
		//si le user name et le mot de passe sont correctes on renvoie le token, sinon on leve on exception de type
		//badcreadentielsexception
		HttpHeaders httpHeader = null;  
		try {
		authenticationManager
		.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
				authenticationRequest.getPassword()));
		} catch(BadCredentialsException e)
		{
			return new ResponseEntity<Integer>(-1, httpHeader, HttpStatus.OK);
			//throw new Exception("incorrect username or password",e);
		}
		//si on arrive a ce stade les infos sont correctes donc on cree le token 
		//get userdetails by username
		
		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getEmail());
		//creer le token
		
		final String token = jwtTokenUtil.generateToken(userDetails);
		Optional<User> cl=userService.getUserByEmail(authenticationRequest.getEmail());
		// check if we already have a refresh token
		Optional<RefreshToken> refresher = refreshRepo.findByUserId(cl.get().getId());
		   // Create the Header Object  
        httpHeader = new HttpHeaders(); 
		if(refresher.isPresent()) {
	        httpHeader.add("RefreshToken", refresher.get().getRefreshToken());
		}
		else
		{
			 // generate a refreshToken
	        UUID refreshToken = UUID.randomUUID();
	        httpHeader.add("RefreshToken", refreshToken.toString());
	        RefreshToken ref = new RefreshToken();
	        ref.setRefreshToken(refreshToken.toString());
	        ref.setUserId(cl.get().getId());
	        refreshRepo.saveAndFlush(ref);
		}
        
		//envoyer le token dans la payload
        // Add token to the Header.  
        httpHeader.add("Authorization", token);
        
		return new ResponseEntity<Long>(cl.get().getId(), httpHeader, HttpStatus.OK);
	}
	
	@PostMapping("/renew")
    public ResponseEntity<?> renew(@RequestBody RefreshTokenRequest data) {

        
        String userId = data.getUserId();
        
        String refreshToken = data.getRefreshToken();
        
        if (refreshToken == null) {
    		return new ResponseEntity<Integer>(0,HttpStatus.OK);
        }

        // check if the refreshToken is for this username
        Optional<RefreshToken> ref = refreshRepo.findByToken(refreshToken);
        
        if (ref.isPresent() && ref.get().getUserId().equals(Long.parseLong(userId))) {
            // generate a new access token for this user
        	Optional<User> client = userService.getUserById(Long.parseLong(userId));
        	final UserDetails userDetails = userDetailsService.
    				loadUserByUsername(client.get().getEmail());
    		final String token = jwtTokenUtil.generateToken(userDetails);
    		//creer le token
    		 HttpHeaders httpHeader = new HttpHeaders();
    	     httpHeader.add("Authorization", token);
    		return new ResponseEntity<String>("1",httpHeader,HttpStatus.OK);
        }

		return new ResponseEntity<String>("0",HttpStatus.OK);

	}
	@RequestMapping(value = "/logoutcl/{idclient}", method = RequestMethod.GET)
	public ResponseEntity<?> LogoutClient(@PathVariable Long idclient) throws Exception {
		System.out.println("logdasdasdasdasdasdasdasdasdout");
		System.out.println();
		Optional<RefreshToken> s = this.refreshRepo.findByUserId(idclient);
		if (s.isPresent()) {
			this.refreshRepo.delete(s.get());
			System.out.println("logout");
			return new ResponseEntity<RefreshToken> (s.get(),HttpStatus.OK);
		}
		System.out.println("logout1");

		return new ResponseEntity<Integer> (-1,HttpStatus.OK);

	}
}
