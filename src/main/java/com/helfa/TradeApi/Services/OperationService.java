package com.helfa.TradeApi.Services;

import java.util.List;
import java.util.Optional;

import com.helfa.TradeApi.Entities.Operation;

public interface OperationService {

	Optional<Operation> getOperationById(Long id);
	
	List<Operation> getOperationByUser(Long id);
	
	Operation addOrUpdateOperation(Operation operation);
	
	Optional<Operation> deleteOperation(Long id);
	
	
	
}
