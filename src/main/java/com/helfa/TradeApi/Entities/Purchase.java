package com.helfa.TradeApi.Entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Purchase implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@OneToOne(cascade = CascadeType.DETACH  )
	private Stock stock;
	
	private float quantity;
	
	private float stockPrice;
	


	@ManyToOne
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public float getStockPrice() {
		return stockPrice;
	}

	public void setStockPrice(float stockPrice) {
		this.stockPrice = stockPrice;
	}
	
	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public float getQuantity() {
		return quantity;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Purchase(Long id, Stock stock, float quantity, User user) {
		super();
		this.id = id;
		this.stock = stock;
		this.quantity = quantity;
		this.user = user;
	}

	public Purchase() {
		super();
	}

	@Override
	public String toString() {
		return "Purchase [id=" + id + ", stock=" + stock + ", quantity=" + quantity + ", user=" + user + "]";
	}

	
	
}
