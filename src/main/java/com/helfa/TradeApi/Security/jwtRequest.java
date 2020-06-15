package com.helfa.TradeApi.Security;

public class jwtRequest {

	private String email;
	private String password;
	
	//need default constructor for JSON Parsing
	public jwtRequest()
	{
		
	}
	public jwtRequest(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
