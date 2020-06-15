package com.helfa.TradeApi.Entities;

import java.io.Serializable;
import java.util.Arrays;

public class PredictionResult {


	
	private float[] predictions;
	private float todayPrice;
	
	public PredictionResult() {
		super();
		// TODO Auto-generated constructor stub
	}
	public float[] getPredictions()  {
		return predictions;
	}
	@Override
	public String toString() {
		return "PredictionResult [predictions=" + Arrays.toString(predictions) + ", todayPrice=" + todayPrice + "]";
	}
	public void setPredictions(float[] predictions) {
		this.predictions = predictions;
	}
	public float getTodayPrice() {
		return todayPrice;
	}
	public void setTodayPrice(float todayPrice) {
		this.todayPrice = todayPrice;
	}
}
