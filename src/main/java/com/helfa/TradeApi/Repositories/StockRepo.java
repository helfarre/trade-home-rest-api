package com.helfa.TradeApi.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.helfa.TradeApi.Entities.Stock;

public interface StockRepo extends JpaRepository<Stock, Long> {

	Optional<Stock> findByName(String Name);
	Optional<Stock> findBySymbol(String Symbol);
	List<Stock> findByCategory_Name(String name);
	List<Stock> findAll();
}
