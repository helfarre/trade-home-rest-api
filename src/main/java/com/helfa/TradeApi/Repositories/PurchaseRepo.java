package com.helfa.TradeApi.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.helfa.TradeApi.Entities.Purchase;
import com.helfa.TradeApi.Entities.User;

public interface PurchaseRepo extends JpaRepository<Purchase, Long>{
	
	List<Purchase> findByUser(User user);
	Optional<Purchase> findByStock_Symbol(String symbol);

}
