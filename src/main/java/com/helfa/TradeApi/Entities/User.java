package com.helfa.TradeApi.Entities;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity

public class User implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	private String lname;
	
	private String fname;
	
	private boolean autoTrade;

	public boolean isAutoTrade() {
		return autoTrade;
	}

	public void setAutoTrade(boolean autoTrade) {
		this.autoTrade = autoTrade;
	}

	@Column(unique = true)
	private String email;
	
	private String password;
	
	

	private float balance;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Purchase> purchases;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Operation> Operations;
	
	@ManyToOne(cascade = CascadeType.DETACH, fetch= FetchType.EAGER)
	private Role role;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public List<Purchase> getPurchases() {
		return purchases;
	}

	public void setPurchases(List<Purchase> purchases) {
		this.purchases = purchases;
	}

	public List<Operation> getOperations() {
		return Operations;
	}

	public void setOperations(List<Operation> operations) {
		Operations = operations;
	}

	public User(Long id, String lname, String fname, String email, String password, float balance,
			List<Purchase> purchases, List<Operation> operations) {
		super();
		this.id = id;
		this.lname = lname;
		this.fname = fname;
		this.email = email;
		this.password = password;
		this.balance = balance;
		this.purchases = purchases;
		Operations = operations;
	}

	public User() {
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", lname=" + lname + ", fname=" + fname + ", email=" + email + ", password="
				+ password + ", balance=" + balance + ", purchases=" + purchases + ", Operations=" + Operations + "]";
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
}