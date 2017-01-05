package com.catemaster.catemaster.bean;

import android.R.id;

public class MainRecipe {
	private int image;
	private String pic;
	private String recipeName;
	private String duation;
	public MainRecipe(int image, String recipeName, String duation) {
		super();
		this.image = image;
		this.recipeName = recipeName;
		this.duation = duation;
	}
	public int getImage() {
		return image;
	}
	public void setImage(int image) {
		this.image = image;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getRecipeName() {
		return recipeName;
	}
	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}
	public String getDuation() {
		return duation;
	}
	public void setDuation(String duation) {
		this.duation = duation;
	}
	
}
