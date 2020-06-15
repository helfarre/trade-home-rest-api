package com.helfa.TradeApi.Services;

import java.util.List;
import java.util.Optional;

import com.helfa.TradeApi.Entities.Stock;

public interface StockService {

	Optional<Stock> getStockById(Long id);
	
	Optional<Stock> getStockByname(String  name);
	
	Optional<Stock> getStockBySymbol(String symbol);
	
	Stock addOrUpdateStock(Stock stock);
	
	Optional<Stock> deleteStock(Long id);
	
	List<Stock> getStockByCategory(String category);
	
	List<Stock> getAllStocks();
	
}
