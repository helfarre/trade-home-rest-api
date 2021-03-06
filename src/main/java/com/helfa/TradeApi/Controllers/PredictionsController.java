package com.helfa.TradeApi.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.helfa.TradeApi.Entities.Operation;
import com.helfa.TradeApi.Entities.PredictionResult;
import com.helfa.TradeApi.Entities.Purchase;
import com.helfa.TradeApi.Entities.Stock;
import com.helfa.TradeApi.Entities.User;
import com.helfa.TradeApi.Repositories.PurchaseRepo;
import com.helfa.TradeApi.Services.OperationService;
import com.helfa.TradeApi.Services.PurchaseService;
import com.helfa.TradeApi.Services.StockService;
import com.helfa.TradeApi.Services.UserService;

@RestController
public class PredictionsController {

	@Autowired
	private PurchaseService purchaseService;
	
	@Autowired
	private PurchaseRepo purchaseRepo;
	
	@Autowired
	private OperationService opService;
	
	@Autowired
	private UserService userService;
	
	@Autowired 
	private StockService stockService;
	
	@Autowired 
	private RestTemplate restTemplate;
	private List<Stock> toBuy = new ArrayList<Stock>();
	
    private HashMap<String, PredictionResult> hmap = new HashMap<String, PredictionResult>();
	static final String URL_EMPLOYEES = "https://prediction-web-service.herokuapp.com/";
//	static final String URL_EMPLOYEES = "http://127.0.0.1:5000/";
	//Get All purchases of a client	
	@Scheduled(cron = "0 0 4 */3 * ?")
	public void getPrediction() {
		hmap = new HashMap<String, PredictionResult>();
		toBuy = new ArrayList<Stock>();
		HttpHeaders headers = new HttpHeaders();
	    headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
	     headers.setContentType(MediaType.APPLICATION_JSON);
	     HttpEntity<PredictionResult> entity = new HttpEntity<PredictionResult>(headers);
		List<Stock> stocklist = stockService.getAllStocks();
		
		int i ;
		for (Stock st: stocklist) {
			i=0;
			ResponseEntity<PredictionResult> response = restTemplate.exchange(URL_EMPLOYEES+"predict/"+st.getSymbol(),
	                HttpMethod.GET, entity,PredictionResult.class);
			PredictionResult res= response.getBody();
			hmap.put(st.getSymbol(), res);
		}
		for (Stock st: stocklist) {
			i=0;
			
			for (float k : hmap.get(st.getSymbol()).getPredictions()) {
				if(hmap.get(st.getSymbol()).getTodayPrice() < k) {
					i++;
				}
			}
			if (i > 2) {
				toBuy.add(st);
			}
		}

		List<User> clients = userService.getAllUsers();
		for (User s : clients) {	
			if(s.getBalance()>=25 && s.isAutoTrade()) {
				System.out.println("helloworld");
			List<Purchase> ps = purchaseService.getPurchaseByUser(s.getId());
			for (Stock buy : toBuy) {
				if(!containsStock(ps,buy)) {
					System.out.println(buy.toString());
					buyStock(buy,s);

					System.out.println();

				}
			}
			int ind = 0;
			for(Purchase purs :ps) {
				ind =0;
			if(purs.getStockPrice() < (hmap.get(purs.getStock().getSymbol()).getTodayPrice()) ) {
				float[] values = hmap.get(purs.getStock().getSymbol()).getPredictions();
				float d = hmap.get(purs.getStock().getSymbol()).getTodayPrice();
				for(float k : values) {
					if ( k > d) {
						ind++;
					}
				}
				if(ind < 3) {
					sellStock(purs,s);
				}
			}	
			}
			System.out.println();
			}
		}
	}
	
	private boolean containsStock(List<Purchase> purchases,Stock stock) {
		List<Stock> stocks = new ArrayList<Stock>();
		for (Purchase s :purchases) {
			stocks.add(s.getStock());
		}
		return stocks.contains(stock);
		
	}
	private void sellStock(Purchase buy, User s) {
		System.out.println(buy.toString());
		System.out.println(s.toString());
		Operation op = new Operation();
		op.setDate(new Date());
		op.setOperationNature("SELL");
		op.setPrice((hmap.get(buy.getStock().getSymbol()).getTodayPrice())*buy.getQuantity());
		op.setQuantity(buy.getQuantity());
		op.setStock(buy.getStock());
		op.setUser(s);
		addOperation(op, s.getId());
		Purchase purchase = new Purchase();
		purchase.setQuantity(buy.getQuantity());
		purchase.setStock(buy.getStock());
		purchase.setUser(s);
		purchase.setStockPrice((hmap.get(buy.getStock().getSymbol()).getTodayPrice()));
		sellPurchase(purchase, s.getId());
	}
	
	
	private void buyStock(Stock buy, User s) {
		System.out.println(buy.toString());
		System.out.println(s.toString());
		Operation op = new Operation();
		op.setDate(new Date());
		op.setOperationNature("BUY");
		float price =s.getBalance()/10;
		op.setPrice(price);
		op.setQuantity(price/(hmap.get(buy.getSymbol()).getTodayPrice()));
		op.setStock(buy);
		op.setUser(s);
		addOperation(op, s.getId());
		Purchase purchase = new Purchase();
		purchase.setQuantity(price/(hmap.get(buy.getSymbol()).getTodayPrice()));
		purchase.setStock(buy);
		purchase.setUser(s);
		purchase.setStockPrice(hmap.get(buy.getSymbol()).getTodayPrice());
		addpurchase(purchase, s.getId());
	}
	private void addOperation(Operation operation, Long idUser) {
		Optional<User> user = userService.getUserById(idUser);
		if (user.isPresent()) {
			if(operation.getOperationNature().equals("BUY"))
			{
				System.out.println("buy");
				if( operation.getPrice() > user.get().getBalance() )
					return ;
				else
				{
					user.get().setBalance(user.get().getBalance()-operation.getPrice());
					userService.addOrUpdateUser(user.get());
					operation.setUser(user.get());
					opService.addOrUpdateOperation(operation);
					return;
				}
			}
			else 
			{
				System.out.println("SELL");
			
				List<Purchase> purchases = purchaseRepo.findByUser(user.get());
				Purchase purchase = null;
				for (Purchase l : purchases) {
					if (l.getStock().getSymbol().equals(operation.getStock().getSymbol())){
						purchase = l;
						break;
					}
				}
				if( purchase== null || purchase.getQuantity() < operation.getQuantity())
					return ;
				else
				{
					user.get().setBalance(user.get().getBalance()+operation.getPrice());
					userService.addOrUpdateUser(user.get());
					operation.setUser(user.get());
					opService.addOrUpdateOperation(operation);
					return ;
				}
			}
		}
		else
			return ;
	}
	public void  addpurchase( Purchase purchase,  Long idUser) {
		System.out.println("id is :"  +idUser);
		Optional<User> user = userService.getUserById(idUser);
		if (user.isPresent()) {
			List<Purchase> purchases=purchaseService.getPurchaseByUser(idUser);
			System.out.println(purchase.getStock().getSymbol());
			for(Purchase pr : purchases) {
//				System.out.println(purchase.getStock().getSymbol());
				if(pr.getStock().getSymbol().equals(purchase.getStock().getSymbol()))
				{
					System.out.println("same");
					pr.setQuantity(pr.getQuantity()+purchase.getQuantity());
					if(pr.getStockPrice()<purchase.getStockPrice()) {
						pr.setStockPrice(purchase.getStockPrice());	
					}
					purchaseService.addorUpdatePurchase(pr);
					return ;
				}
			}
			System.out.println("not same");

			purchase.setUser(user.get());
			purchaseService.addorUpdatePurchase(purchase);
			return ;
		}
		else
			return ;
	}
	public void sellPurchase( Purchase purchase,  Long idUser) {
		Optional<User> user = userService.getUserById(idUser);
		if (user.isPresent()) {
			List<Purchase> purchases = purchaseRepo.findByUser(user.get());
			Purchase pur = null;
			for (Purchase l : purchases) {
				if (l.getStock().getSymbol().equals(purchase.getStock().getSymbol())){
					pur = l;
					break;
				}
			}				
			if(pur!= null)
				{
					System.out.println("same");
					pur.setQuantity(pur.getQuantity()-purchase.getQuantity());
					purchaseService.addorUpdatePurchase(pur);
					if (pur.getQuantity()==0) {
						purchaseService.deletePurchase(pur.getId());
					}
					return ;
				}
				else
					return ;
			}
		else
			return ;
	}
	
}
