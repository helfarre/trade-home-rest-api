package com.helfa.TradeApi.Controllers;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.helfa.TradeApi.Entities.Category;
import com.helfa.TradeApi.Entities.Stock;
import com.helfa.TradeApi.Repositories.CategoryRepo;
import com.helfa.TradeApi.Services.StockService;

@RestController
@RequestMapping("/Stock/*")
public class StockController {
	
	@Autowired
	private StockService stockService;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@GetMapping(value = "/Stockname/{stockSymbl}")
	public ResponseEntity<Stock> getStockBySymbol(@PathVariable String stockSymbl) {
		Optional<Stock> stocks = stockService.getStockBySymbol(stockSymbl);
		if(stocks.isPresent())
			return new ResponseEntity<Stock>(stocks.get(), HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}
	
	@GetMapping(value = "/Stocks")
	public ResponseEntity<List<Stock>> getAllstocks() {
		List<Stock> stocks = stockService.getAllStocks();
		
		return new ResponseEntity<List<Stock>>(stocks, HttpStatus.OK);
	}
	
	@GetMapping(value = "/Stocks/{categoryname}")
	public ResponseEntity<?> getStocksByCategory(@PathVariable String categoryname) {
		
		Optional<Category> categorie= categoryRepo.findByName(categoryname);
		if(categorie.isPresent())
		{
			List<Stock> stocks = stockService.getStockByCategory(categoryname);
		
		return new ResponseEntity<List<Stock>>(stocks, HttpStatus.OK);
		}
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getstockById(@PathVariable Long id) {
		Optional<Stock> stock = stockService.getStockById(id);
		if (stock.isPresent())
			return new ResponseEntity<Stock>(stock.get(), HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping(value = "/AddStock/{categoryname}")
	@Transactional
	public ResponseEntity<?> addStock(@RequestBody Stock stock,@PathVariable String categoryname) {
		Optional<Category> categorie= categoryRepo.findByName(categoryname);
		if(categorie.isPresent())
		{
			stock.setCategory(categorie.get());
			stockService.addOrUpdateStock(stock);
			return new ResponseEntity<Stock>(stock, HttpStatus.OK);
		}
		return new ResponseEntity<String>("Category not found", HttpStatus.NOT_ACCEPTABLE);


	}
	
	
	@PostMapping(value = "/UpdateStock")
	@Transactional
	public ResponseEntity<Stock> updateStock(@RequestBody Stock stock) {

		stockService.addOrUpdateStock(stock);
			return new ResponseEntity<Stock>(stock, HttpStatus.OK);

	}
	
	@DeleteMapping(value = "/deleteStock/{id}")
	public ResponseEntity<?> deleteStock(@PathVariable Long id) {

		Optional<Stock> stock = stockService.getStockById(id);
		if(stock.isPresent()) {
		stockService.deleteStock(id);
			return new ResponseEntity<Stock>(stock.get(), HttpStatus.OK);
		}
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}
}
