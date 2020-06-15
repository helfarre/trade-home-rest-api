package com.helfa.TradeApi.Security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
	
	Optional<RefreshToken> findByUserId(Long id);
	
	Optional<RefreshToken> findByToken(String token);

}
