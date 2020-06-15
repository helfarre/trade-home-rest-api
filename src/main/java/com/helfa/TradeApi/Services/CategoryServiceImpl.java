package com.helfa.TradeApi.Services;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helfa.TradeApi.Entities.Category;
import com.helfa.TradeApi.Repositories.CategoryRepo;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepo categoryRepo;
	@Override
	public Optional<Category> getCategoryById(Long id) {
		return categoryRepo.findById(id);
	}

	@Override
	public Optional<Category> getCategoryByName(String name) {
		return categoryRepo.findByName(name);
	}

	@Override
	public List<Category> getAllCategories() {
		return categoryRepo.findAll();
	}

	@Override
	public Category addOrUpdateCategory(Category category) {
		categoryRepo.saveAndFlush(category);
		return category;
	}

	@Override
	public Optional<Category> deleteCategory(Long id) {
		
		Optional<Category> categorie = categoryRepo.findById(id);
		categoryRepo.deleteById(id);
		return categorie;
	}

	
}
