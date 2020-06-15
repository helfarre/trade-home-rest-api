package com.helfa.TradeApi.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.helfa.TradeApi.Entities.Category;


public interface CategoryRepo extends JpaRepository<Category, Long> {

	Optional<Category> findByName(String Name);
	List<Category> findAll();
}
