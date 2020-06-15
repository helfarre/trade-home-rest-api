package com.helfa.TradeApi.Entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
@Entity
public class Operation implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	@OneToOne(cascade = CascadeType.DETACH)
	private Stock stock;
	
	private String operationNature;
	
	private float price;
	
	//added quantity to the operation
//	******************************
	private float quantity;
	
	public float getQuantity() {
		return quantity;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}
	////*******************************
	
	private Date date;
	
	@ManyToOne
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public String getOperationNature() {
		return operationNature;
	}

	public void setOperationNature(String operationNature) {
		this.operationNature = operationNature;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Operation(Long id, Stock stock, String operationNature, float price, Date date, User user) {
		super();
		this.id = id;
		this.stock = stock;
		this.operationNature = operationNature;
		this.price = price;
		this.date = date;
		this.user = user;
	}

	public Operation() {
		super();
	}

	@Override
	public String toString() {
		return "Operation [id=" + id + ", stock=" + stock + ", operationNature=" + operationNature + ", price=" + price
				+ ", date=" + date + ", user=" + user + "]";
	}
	
}