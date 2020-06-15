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

import com.helfa.TradeApi.Entities.Category;
import com.helfa.TradeApi.Entities.Operation;
import com.helfa.TradeApi.Entities.User;
import com.helfa.TradeApi.Services.CategoryService;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", exposedHeaders = "Authorization")  

@RequestMapping("/Category/*")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@GetMapping(value = "/Category/{categoryName}")
	public ResponseEntity<?> getCategory(@PathVariable String categoryName) {
		Optional<Category> category = categoryService.getCategoryByName(categoryName);
		if (category.isPresent())
		{
		return new ResponseEntity<Category>(category.get(), HttpStatus.OK);
		}
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@GetMapping(value = "/Categories")
	public ResponseEntity<?> getAllCategories() {
		List<Category> category = categoryService.getAllCategories();
		return new ResponseEntity<List<Category>>(category, HttpStatus.OK);
	}
	
	@PostMapping(value = "/addCategory")
	@Transactional
	public ResponseEntity<Category> addCategory(@RequestBody Category category) {

		categoryService.addOrUpdateCategory(category);
		return new ResponseEntity<Category>(category, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/deleteCategory/{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable Long id) {

		Optional<Category> category = categoryService.getCategoryById(id);
		if(category.isPresent()) {
			categoryService.deleteCategory(id);
			return new ResponseEntity<Category>(category.get(), HttpStatus.OK);
		}
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
		
	
}
