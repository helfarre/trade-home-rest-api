package com.helfa.TradeApi.Security;

public class RefreshTokenRequest {

		    private String refreshToken;
		    private String userId;
		    
		    
			public RefreshTokenRequest() {
				
			}
			public String getRefreshToken() {
				return refreshToken;
			}
			public void setRefreshToken(String refreshToken) {
				this.refreshToken = refreshToken;
			}
			public String getUserId() {
				return userId;
			}
			public void setUserId(String userId) {
				this.userId = userId;
			}
		
}
