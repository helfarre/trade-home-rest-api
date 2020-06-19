package com.helfa.TradeApi.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helfa.TradeApi.Entities.Operation;
import com.helfa.TradeApi.Entities.User;
import com.helfa.TradeApi.Repositories.OperationRepo;
import com.helfa.TradeApi.Repositories.UserRepo;

@Service
public class OperationServiceImpl implements OperationService {

	@Autowired
	private OperationRepo operationRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Override
	public Optional<Operation> getOperationById(Long id) {
		
		return operationRepo.findById(id);
	}

	@Override
	public List<Operation> getOperationByUser(Long id) {
		
		Optional<User> user = userRepo.findById(id);
		
		return operationRepo.findByUser(user.get());
		
	}

	@Override
	public Operation addOrUpdateOperation(Operation operation) {
		System.out.println(operation);
		operationRepo.saveAndFlush(operation);
		return operation;
	}

	@Override
	public Optional<Operation> deleteOperation(Long id) {
	
		Optional<Operation> op = operationRepo.findById(id);
		operationRepo.deleteById(id);
		return op;
	}

}
