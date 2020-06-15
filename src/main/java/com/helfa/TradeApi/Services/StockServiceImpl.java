package com.helfa.TradeApi.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helfa.TradeApi.Entities.Stock;
import com.helfa.TradeApi.Repositories.StockRepo;

@Service
public class StockServiceImpl implements StockService {

	@Autowired
	private StockRepo stockRepo;

	@Override
	public Optional<Stock> getStockById(Long id) {
		
		return stockRepo.findById(id);
	}

	@Override
	public Optional<Stock> getStockByname(String name) {
	
		return stockRepo.findByName(name);
	}

	@Override
	public Optional<Stock> getStockBySymbol(String symbol) {
		
		return stockRepo.findBySymbol(symbol);
	}

	@Override
	public Stock addOrUpdateStock(Stock stock) {
		
		stockRepo.saveAndFlush(stock);
		return stock;
	}

	@Override
	public Optional<Stock> deleteStock(Long id) {
		Optional<Stock> stock = stockRepo.findById(id);
		stockRepo.deleteById(id);
		return stock;
	}

	@Override
	public List<Stock> getAllStocks() {
	
		return stockRepo.findAll();
	}

	@Override
	public List<Stock> getStockByCategory(String name) {
		
		return stockRepo.findByCategory_Name(name);
	}
	

}
