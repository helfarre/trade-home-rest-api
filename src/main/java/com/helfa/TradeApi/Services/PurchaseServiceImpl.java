package com.helfa.TradeApi.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helfa.TradeApi.Entities.Purchase;
import com.helfa.TradeApi.Entities.User;
import com.helfa.TradeApi.Repositories.PurchaseRepo;
import com.helfa.TradeApi.Repositories.UserRepo;



@Service
public class PurchaseServiceImpl implements PurchaseService {

	@Autowired
	private PurchaseRepo purchaseRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Override
	public Optional<Purchase> getPurchaseById(Long id) {
		
		return purchaseRepo.findById(id);
	}

	@Override
	public List<Purchase> getPurchaseByUser(Long id) {
		
		Optional<User> user = userRepo.findById(id);
		
		return purchaseRepo.findByUser(user.get());
	}

	@Override
	public Purchase addorUpdatePurchase(Purchase purchase) {
	
		purchaseRepo.saveAndFlush(purchase);
		return purchase;
	}

	@Override
	public Optional<Purchase> deletePurchase(Long id) {
		
		Optional<Purchase> purchase = purchaseRepo.findById(id);
		if(purchase.isPresent()) {
		purchaseRepo.deleteById(id);
		}
		return purchase;
	}

	@Override
	public Optional<Purchase> getpurchaeByStockSymbol(String Symbol) {
		return purchaseRepo.findByStock_Symbol(Symbol);
	}

}
