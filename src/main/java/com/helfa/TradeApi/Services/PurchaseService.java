package com.helfa.TradeApi.Services;

import java.util.List;
import java.util.Optional;

import com.helfa.TradeApi.Entities.Purchase;

public interface PurchaseService {

	Optional<Purchase> getPurchaseById(Long id);
	Optional<Purchase> getpurchaeByStockSymbol(String Symbol);

	
	List<Purchase> getPurchaseByUser(Long id);
	
	
	Purchase addorUpdatePurchase(Purchase purchase);
	
	Optional<Purchase> deletePurchase(Long id);
	
}
