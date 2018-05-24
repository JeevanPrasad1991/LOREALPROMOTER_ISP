package com.cpm.GetterSetter;

public class StoreStockinPopupGetterSetter {
	
	
	public String storeid;
	public String key_id;
	public String select_brand="";
	public String current_date="";

	public String getCurrent_date() {
		return current_date;
	}

	public void setCurrent_date(String current_date) {
		this.current_date = current_date;
	}

	public String getStoreid() {
		return storeid;
	}

	public void setStoreid(String storeid) {
		this.storeid = storeid;
	}

	public String getKey_id() {
		return key_id;
	}

	public void setKey_id(String key_id) {
		this.key_id = key_id;
	}

	public String getSelect_brand() {
		return select_brand;
	}

	public void setSelect_brand(String select_brand) {
		this.select_brand = select_brand;
	}
}
