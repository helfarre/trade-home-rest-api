package com.helfa.TradeApi.Security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class jwtRequestFiliter extends OncePerRequestFilter {

	@Autowired
	private MyUserDetailsService userDetailsService;
	@Autowired
	private JwtUtil jwtUtil;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		//tout ce passe durant le securitycontext
		// quand on arrive a ce stade c'est on a essayer d'appeler notre API pour des donnees
		// donc logiquement si on est pas authentifie ca ne doit pas marcher
		//donc on creer pas un token d'access pour le filtre usernamepasswordauthenticationtoken
		
		
		
		// le header authorization : "Bearer jwtToken"
		final String authorizationHeader = request.getHeader("Autorization");
		String username =null;
		String jwt = null;
		// si authorizationHeader n'est pas vide et qu'il commence par Bearer alors il y'a une possibilite d'avoir un token
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
		{
			// on enleve le bearer et on laisse seulement le token dans la variable jwt
			jwt= authorizationHeader.substring(7);
			//on utilise les outils jwt pour extraire le Claim username
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			try {username = jwtUtil.extractUserName(jwt);}
			catch (ExpiredJwtException e) {
		        response.addHeader("isexpired", "true");
			}
		}
		//on verifie qu'il ya un username dans le token et que l'objet authentification n'existe pas
		//Si l'api est stateless pour on test sur get authentication
		if ( username != null && SecurityContextHolder.getContext().getAuthentication()==null) {
			//on essaie de recupere le user pour le comparer avec les donnees du token
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			//on verifie la validite du token, signature date d'expiration mot de passe 
			// voir la classe jwt util

			if (jwtUtil.validateToken(jwt, userDetails)) {
				//si le token est valide
				// on cree un objet usernamePasswordAuthenticationToken c'est le ticket de l'utilisateur avec ses autorites
				// pour utiliser notre api
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
				.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				//on ajoute le usernamePasswordAuthenticationToken au security contextHolder
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
			

				
			
			
		}
		// on passe a la suite de la chaine.
		// pour le filtre suivant c'est le UsernamePasswordAuthenticationFilter
		// si il trouve l'objet authentication donc on passer par les boucles suivantes
		// sinon on est pas authentifie et on aura pas le droit d'acceder au url proteger
		
		filterChain.doFilter(request, response);
		
	}

}
