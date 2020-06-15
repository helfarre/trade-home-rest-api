package com.helfa.TradeApi.Services;

import java.util.List;
import java.util.Optional;

import com.helfa.TradeApi.Entities.Category;

public interface CategoryService {
	
	Optional<Category> getCategoryById(Long id);
	
	Optional<Category> getCategoryByName(String name);
	
	List<Category> getAllCategories();
	
	Category addOrUpdateCategory(Category category);
	
	Optional<Category> deleteCategory(Long id);
	

}
