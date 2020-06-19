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

import com.helfa.TradeApi.Entities.Purchase;
import com.helfa.TradeApi.Entities.User;
import com.helfa.TradeApi.Repositories.PurchaseRepo;
import com.helfa.TradeApi.Services.PurchaseService;
import com.helfa.TradeApi.Services.UserService;

@RestController

@RequestMapping("/Purchase/*")
public class PurchaseController {

	@Autowired
	private PurchaseService purchaseService;
	
	@Autowired
	private UserService userService;
	@Autowired
	private PurchaseRepo purchaserepo;

	//Get All purchases of a client
	@GetMapping(value = "/Client/{idUser}")
	public ResponseEntity<?> getUserPurchases(@PathVariable Long idUser) {
		Optional<User> user = userService.getUserById(idUser);
		System.out.println("here");
		if (user.isPresent())
		{
			System.out.println("there");

		List<Purchase> purchases = purchaseService.getPurchaseByUser(idUser);
		return new ResponseEntity<List<Purchase>>(purchases, HttpStatus.OK);
		}
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	//Get All purchases of a client
		@GetMapping(value = "/stock/{idClient}/{Symbol}")
		public ResponseEntity<?> getpurchasebystockname(@PathVariable String Symbol,@PathVariable Long idClient) {
			Optional<User> user = userService.getUserById(idClient);
			if (user.isPresent())
			{
				System.out.println("there");

			List<Purchase> purchases = purchaseService.getPurchaseByUser(idClient);
			for(Purchase p : purchases) {
				if ((p.getStock().getSymbol()).equals(Symbol))
					return new ResponseEntity<Purchase>(p,HttpStatus.OK);
			}
			}
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	//get Purchase by id
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getpurchaseById(@PathVariable Long id) {
		Optional<Purchase> purchase = purchaseService.getPurchaseById(id);
		if (purchase.isPresent())
			return new ResponseEntity<Purchase>(purchase.get(), HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	//add Purchase to a client
	@PostMapping(value = "/addpurchase/{idUser}")
	@Transactional
	public ResponseEntity<?> addpurchase(@RequestBody Purchase purchase, @PathVariable Long idUser) {
		System.out.println("id is :"  +idUser);
		Optional<User> user = userService.getUserById(idUser);
		if (user.isPresent()) {
			List<Purchase> purchases=purchaseService.getPurchaseByUser(idUser);
			for(Purchase pr : purchases) {
				System.out.println(purchase.getStock().getSymbol());
				if(pr.getStock().getSymbol().equals(purchase.getStock().getSymbol()))
				{
					System.out.println("same");
					pr.setQuantity(pr.getQuantity()+purchase.getQuantity());
					if(pr.getStockPrice()<purchase.getStockPrice()) {
						pr.setStockPrice(purchase.getStockPrice());	
					}
					purchaseService.addorUpdatePurchase(pr);
					return new ResponseEntity<Purchase>(pr, HttpStatus.OK);
				}
			}
			System.out.println("not same");

			purchase.setUser(user.get());
			purchaseService.addorUpdatePurchase(purchase);
			return new ResponseEntity<Purchase>(purchase, HttpStatus.OK);
		}
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	@PostMapping(value = "/sellPurchase/{idUser}")
	@Transactional
	public ResponseEntity<?> sellPurchase(@RequestBody Purchase purchase, @PathVariable Long idUser) {
		Optional<User> user = userService.getUserById(idUser);
		if (user.isPresent()) {
			List<Purchase> purchases = purchaserepo.findByUser(user.get());
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
					return new ResponseEntity<Purchase>(pur, HttpStatus.OK);
				}
				else
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping(value = "/deletePurchase/{id}")
	public ResponseEntity<?> deletepurchase(@PathVariable Long id) {

		Optional<Purchase> purchase = purchaseService.getPurchaseById(id);
		if(purchase.isPresent()) {
		purchaseService.deletePurchase(id);
			return new ResponseEntity<Purchase>(purchase.get(), HttpStatus.OK);
		}
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
