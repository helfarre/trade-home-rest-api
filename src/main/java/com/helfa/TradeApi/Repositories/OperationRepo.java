package com.helfa.TradeApi.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.helfa.TradeApi.Entities.Operation;
import com.helfa.TradeApi.Entities.User;

public interface OperationRepo extends JpaRepository<Operation, Long> {

	List<Operation> findByUser(User user);
}
