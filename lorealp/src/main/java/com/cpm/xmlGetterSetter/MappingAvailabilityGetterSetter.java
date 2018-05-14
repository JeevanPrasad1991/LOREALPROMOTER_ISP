package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

public class MappingAvailabilityGetterSetter {
	
String mapping_avail_table;
	
	public String getMapping_avail_table() {
		return mapping_avail_table;
	}


	public void setMapping_avail_table(String mapping_avail_table) {
		this.mapping_avail_table = mapping_avail_table;
	}


	/*ArrayList<String> store_cd = new ArrayList<String>();*/
	ArrayList<String> sku_cd = new ArrayList<String>();

	ArrayList<String> keyaccount_cd = new ArrayList<String>();
	ArrayList<String> city_cd = new ArrayList<String>();
	ArrayList<String> storetype_cd = new ArrayList<String>();

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

	/*	ArrayList<String> category_sequence = new ArrayList<String>();
        ArrayList<String> brand_sequence = new ArrayList<String>();
        ArrayList<String> sku_sequence = new ArrayList<String>();
    */
	/*public ArrayList<String> getStore_cd() {
		return store_cd;
	}


	public void setStore_cd(String store_cd) {
		this.store_cd.add(store_cd);
	}*/


	public ArrayList<String> getSku_cd() {
		return sku_cd;
	}


	public void setSku_cd(String sku_cd) {
		this.sku_cd.add(sku_cd);
	}


	/*public ArrayList<String> getCategory_sequence() {
		return category_sequence;
	}


	public void setCategory_sequence(String category_sequence) {
		this.category_sequence.add(category_sequence);
	}


	public ArrayList<String> getBrand_sequence() {
		return brand_sequence;
	}


	public void setBrand_sequence(String brand_sequence) {
		this.brand_sequence.add(brand_sequence);
	}


	public ArrayList<String> getSku_sequence() {
		return sku_sequence;
	}


	public void setSku_sequence(String sku_sequence) {
		this.sku_sequence.add(sku_sequence);
	}*/

	
}
