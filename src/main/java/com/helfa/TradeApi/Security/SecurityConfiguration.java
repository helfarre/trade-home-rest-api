package com.helfa.TradeApi.Security;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.authentication.AuthenticationManagerBeanDefinitionParser;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
		
	
		@Autowired
		UserDetailsService userDetailsService;
		
		@Autowired
		jwtRequestFiliter jwtRequestFilter;
		//if we want to use the dataSource directly without userDetailsService
		//Dependency injection to link our data source with the datbase from applications.properties
//		@Autowired
//		DataSource dataSource;
		
		
		@Override
		//Configure authentication (authentication manager)
		//
		protected void configure(AuthenticationManagerBuilder auth) throws Exception{
			//using userdetails
			//Add authentication based upon the custom UserDetailsService that is passed in. 
			//It then returns a DaoAuthenticationConfigurer to allow customization of the authentication.
			//allows DaoAuthenticationProvider

			auth.userDetailsService(userDetailsService);
			
			//hardcoded example
//			 auth.inMemoryAuthentication()
//	          .withUser("user1").password("user1Pass").roles("USER")
//	          .and()
//	          .withUser("admin").password("adminPass").roles("ADMIN");
//	    }
			
			//When using dataSource u have to you jdbcAuthentication
			
//			 auth.jdbcAuthentication().dataSource(dataSource)
//			 .usersByUsernameQuery("select client.login as username,client.password as password,1 as enabled from client where login=?")
//			 .authoritiesByUsernameQuery("select client.login as username,role.role_name as authority from client " + 
//			 		"JOIN client_roles ON client.id = client_roles.client_id " + 
//			 		"JOIN role ON role.role_id = client_roles.roles_role_id " + 
//			 		"WHERE login=?" );
		}
		
	    @Override
		protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().cors().and()
		.authorizeRequests()
	//	.anyRequest().permitAll()
		//permettre l'access a cet url sans authentification
		// ca veut pas dire qu'on passe pas par les filtres
		
			.antMatchers("/authenticate").permitAll()
			.antMatchers("/renew").permitAll()
			.antMatchers("/User/Greeting").permitAll()
			.antMatchers("/User/addUser").permitAll()
			// to be removed later
			.antMatchers("/prediction/getPrediction").permitAll()
			.anyRequest().authenticated()
		.and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.httpBasic();
		//add our jwt filter before usernamepasswordauthenticationfilter
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		}
	    @Bean
	    @Override
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	        return super.authenticationManagerBean();
	    }
		//give spring a passwordencoder bean
		@Bean
		public PasswordEncoder getPasswordEncoder() {
			return NoOpPasswordEncoder.getInstance();
		}
		
		
}
