package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

public class MappingSosGetterSetter {
	
	String mapping_sos_table;

	ArrayList<String> brand_cd = new ArrayList<String>();

	public String getMapping_sos_table() {
		return mapping_sos_table;
	}

	public void setMapping_sos_table(String mapping_sos_table) {
		this.mapping_sos_table = mapping_sos_table;
	}

	public ArrayList<String> getBrand_cd() {
		return brand_cd;
	}

	public void setBrand_cd(String brand_cd) {
		this.brand_cd.add(brand_cd);
	}
}
