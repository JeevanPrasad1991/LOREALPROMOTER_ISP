package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

public class MappingAssetGetterSetter {
	
	String mapping_asset_table;
	
	public String getMapping_asset_table() {
		return mapping_asset_table;
	}


	public void setMapping_asset_table(String mapping_asset_table) {
		this.mapping_asset_table = mapping_asset_table;
	}


	ArrayList<String> store_cd = new ArrayList<String>();
	ArrayList<String> category_cd = new ArrayList<String>();
	ArrayList<String> asset_cd = new ArrayList<String>();
	
	public ArrayList<String> getStore_cd() {
		return store_cd;
	}

	public void setStore_cd(String store_cd) {
		this.store_cd.add(store_cd);
	}



	
	public ArrayList<String> getAsset_cd() {
		return asset_cd;
	}


	public void setAsset_cd(String asset_cd) {
		this.asset_cd.add(asset_cd);
	}


	public ArrayList<String> getCategory_cd() {
		return category_cd;
	}

	public void setCategory_cd(String category_cd) {
		this.category_cd.add(category_cd);
	}
}
