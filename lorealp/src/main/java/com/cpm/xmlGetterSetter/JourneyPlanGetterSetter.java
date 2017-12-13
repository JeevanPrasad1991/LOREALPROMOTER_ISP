package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

public class JourneyPlanGetterSetter {
	
	String table_journey_plan;

	ArrayList<String> store_cd = new ArrayList<String>();
	ArrayList<String> emp_cd = new ArrayList<String>();
	ArrayList<String> VISIT_DATE = new ArrayList<String>();
	ArrayList<String> key_account = new ArrayList<String>();
	ArrayList<String> store_name = new ArrayList<String>();
	ArrayList<String> city = new ArrayList<String>();
	ArrayList<String> store_type = new ArrayList<String>();
	ArrayList<String> category_type = new ArrayList<String>();
	ArrayList<String> uploadStatus = new ArrayList<String>();
	ArrayList<String> checkOutStatus = new ArrayList<String>();
	ArrayList<String> geotagStatus = new ArrayList<String>();
	ArrayList<String> latitude = new ArrayList<String>();
	ArrayList<String> longitude = new ArrayList<String>();

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

	public ArrayList<String> getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude.add(latitude);
	}

	public ArrayList<String> getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude.add(longitude);
	}

	public ArrayList<String> getGeotagStatus() {
		return geotagStatus;
	}

	public void setGeotagStatus(String geotagStatus) {
		this.geotagStatus.add(geotagStatus);
	}


	
	
	public ArrayList<String> getCheckOutStatus() {
		return checkOutStatus;
	}
	public void setCheckOutStatus(String checkOutStatus) {
		this.checkOutStatus.add(checkOutStatus);
	}
	public ArrayList<String> getVISIT_DATE() {
		return VISIT_DATE;
	}
	public void setVISIT_DATE(String vISIT_DATE) {
		this.VISIT_DATE.add(vISIT_DATE);
	}
	public ArrayList<String> getStore_cd() {
		return store_cd;
	}
	public void setStore_cd(String store_cd) {
		this.store_cd.add(store_cd);
	}
	public ArrayList<String> getEmp_cd() {
		return emp_cd;
	}
	public void setEmp_cd(String emp_cd) {
		this.emp_cd.add(emp_cd);
	}
	public ArrayList<String> getKey_account() {
		return key_account;
	}
	public void setKey_account(String key_account) {
		this.key_account.add(key_account);
	}
	public ArrayList<String> getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name.add(store_name);
	}
	public ArrayList<String> getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city.add(city);
	}
	public ArrayList<String> getStore_type() {
		return store_type;
	}
	public void setStore_type(String store_type) {
		this.store_type.add(store_type);
	}
	public ArrayList<String> getCategory_type() {
		return category_type;
	}
	public void setCategory_type(String category_type) {
		this.category_type.add(category_type);
	}
	public ArrayList<String> getUploadStatus() {
		return uploadStatus;
	}
	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus.add(uploadStatus);
	}
	public String getTable_journey_plan() {
		return table_journey_plan;
	}
	public void setTable_journey_plan(String table_journey_plan) {
		this.table_journey_plan = table_journey_plan;
	}
	
}
