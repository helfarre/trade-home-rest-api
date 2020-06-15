package com.helfa.TradeApi;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

import com.helfa.TradeApi.Entities.User;
import com.helfa.TradeApi.Repositories.UserRepo;

@SpringBootApplication
@EnableJpaRepositories
public class TradeApiApplication implements CommandLineRunner {

	@Autowired
	private UserRepo userRepo;
	
	@Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
       // Do any additional configuration here
       return builder.build();
    }
	public static void main(String[] args) {
		
		SpringApplication.run(TradeApiApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
//	SimpleDateFormat date= new SimpleDateFormat("dd-mm-yyyy");
//	User user= new User(null, "ELFARRE", "HAMZA", "email1@gmail.com", "password1", 0, null, null);
//	User user1= new User(null, "Barbache", "Oumaima", "email2@gmail.com", "password2", 0, null, null);
//
//
//	userRepo.save(user);
//	userRepo.save(user1);

	}


}
