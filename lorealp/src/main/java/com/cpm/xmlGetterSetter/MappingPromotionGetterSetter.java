package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

public class MappingPromotionGetterSetter {
	
String mapping_promotion_table;
	
	public String getMapping_promotion_table() {
		return mapping_promotion_table;
	}


	public void setMapping_promotion_table(String mapping_promotion_table) {
		this.mapping_promotion_table = mapping_promotion_table;
	}
	
	/*ArrayList<String> store_cd = new ArrayList<String>();
	ArrayList<String> sku_cd = new ArrayList<String>();
	ArrayList<String> promotion = new ArrayList<String>();
	ArrayList<String> brand_sequence = new ArrayList<String>();
	ArrayList<String> sku_sequence = new ArrayList<String>();
	ArrayList<String> category_type = new ArrayList<String>();
	ArrayList<String> brand_cd = new ArrayList<String>();
	ArrayList<String> pid = new ArrayList<>();*/

	ArrayList<String> pid = new ArrayList<>();
	ArrayList<String> keyaccount_cd = new ArrayList<>();
	ArrayList<String> city_cd = new ArrayList<>();
	ArrayList<String> storetype_cd = new ArrayList<>();
	ArrayList<String> brand_cd = new ArrayList<>();
	ArrayList<String> promotion = new ArrayList<>();

	public ArrayList<String> getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid.add(pid);
	}

	public ArrayList<String> getKeyaccount_cd() {
		return keyaccount_cd;
	}

	public void setKeyaccount_cd(String keyaccount_cd) {
		this.keyaccount_cd.add(keyaccount_cd);
	}

	public ArrayList<String> getCity_cd() {
		return city_cd;
	}

	public void setCity_cd(String city_cd) {
		this.city_cd.add(city_cd);
	}

	public ArrayList<String> getStoretype_cd() {
		return storetype_cd;
	}

	public void setStoretype_cd(String storetype_cd) {
		this.storetype_cd.add(storetype_cd);
	}

	public ArrayList<String> getBrand_cd() {
		return brand_cd;
	}

	public void setBrand_cd(String brand_cd) {
		this.brand_cd.add(brand_cd);
	}

	public ArrayList<String> getPromotion() {
		return promotion;
	}

	public void setPromotion(String promotion) {
		this.promotion.add(promotion);
	}
}


