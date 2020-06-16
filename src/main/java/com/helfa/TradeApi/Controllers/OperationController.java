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

import com.helfa.TradeApi.Entities.Operation;
import com.helfa.TradeApi.Entities.Purchase;
import com.helfa.TradeApi.Entities.User;
import com.helfa.TradeApi.Repositories.PurchaseRepo;
import com.helfa.TradeApi.Services.OperationService;
import com.helfa.TradeApi.Services.PurchaseService;
import com.helfa.TradeApi.Services.UserService;

@RestController
@CrossOrigin(origins = "https://tradehome.herokuapp.com", allowedHeaders = "*", exposedHeaders = "Authorization")  
@RequestMapping("/Operation/*")
public class OperationController {

	@Autowired
	private OperationService operationService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PurchaseRepo purchaseService;
	
	//Get All operations of a client
		@GetMapping(value = "/Client/{idUser}")
		public ResponseEntity<?> getAllOperations(@PathVariable Long idUser) {
			Optional<User> user = userService.getUserById(idUser);
			if (user.isPresent())
			{
			List<Operation> operations = operationService.getOperationByUser(idUser);
			return new ResponseEntity<List<Operation>>(operations, HttpStatus.OK);
			}
			else
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		//get Operation by id
		@GetMapping(value = "/{id}")
		public ResponseEntity<?> getOperationById(@PathVariable Long id) {
			Optional<Operation> operation = operationService.getOperationById(id);
			if (operation.isPresent())
				return new ResponseEntity<Operation>(operation.get(), HttpStatus.OK);
			else
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		@PostMapping(value = "/addOperation/{idUser}")
		@Transactional
		public ResponseEntity<?> addOperation(@RequestBody Operation operation, @PathVariable Long idUser) {
			Optional<User> user = userService.getUserById(idUser);
			if (user.isPresent()) {
				if(operation.getOperationNature().equals("BUY"))
				{
					System.out.println("buy");
					if( operation.getPrice() > user.get().getBalance() )
						return new ResponseEntity<Integer>(-1,HttpStatus.FORBIDDEN);
					else
					{
						user.get().setBalance(user.get().getBalance()-operation.getPrice());
						userService.addOrUpdateUser(user.get());
						operation.setUser(user.get());
						operationService.addOrUpdateOperation(operation);
						return new ResponseEntity<Long>(operation.getId(), HttpStatus.OK);
					}
				}
				else 
				{
					System.out.println("SELL");
					List<Purchase> purchases= purchaseService.findByUser(user.get());
					Purchase purchase = null;
					for (Purchase l : purchases) {
						if (l.getStock().getSymbol().equals(operation.getStock().getSymbol())){
							purchase = l;
							break;
						}
					}
					if( purchase== null || purchase.getQuantity() < operation.getQuantity())
						return new ResponseEntity<Integer>(-1,HttpStatus.FORBIDDEN);
					else
					{
						user.get().setBalance(user.get().getBalance()+operation.getPrice());
						userService.addOrUpdateUser(user.get());
						operation.setUser(user.get());
						operationService.addOrUpdateOperation(operation);
						return new ResponseEntity<Long>(operation.getId(), HttpStatus.OK);
					}
				}
			}
			else
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		@DeleteMapping(value = "/deleteOperation/{id}")
		public ResponseEntity<?> deleteOperation(@PathVariable Long id) {

			Optional<Operation> operation = operationService.getOperationById(id);
			if(operation.isPresent()) {
				operationService.deleteOperation(id);
				return new ResponseEntity<Operation>(operation.get(), HttpStatus.OK);
			}
			else
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		


}
