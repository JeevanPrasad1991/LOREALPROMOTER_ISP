package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

public class AssetInsertdataGetterSetter {

	ArrayList<ChecklistInsertDataGetterSetter> checklist = new ArrayList<>();

	ArrayList<SkuGetterSetter> skulist = new ArrayList<>();

	String key_id, brand, brand_cd, store_cd, asset, asset_cd, present,remark,img, category, category_cd, planogram_img;

	ArrayList<StockNewGetterSetter> listSkuData = new ArrayList<>();

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getBrand_cd() {
		return brand_cd;
	}

	public void setBrand_cd(String brand_cd) {
		this.brand_cd = brand_cd;
	}

	public String getStore_cd() {
		return store_cd;
	}

	public void setStore_cd(String store_cd) {
		this.store_cd = store_cd;
	}

	public String getAsset() {
		return asset;
	}

	public void setAsset(String asset) {
		this.asset = asset;
	}

	public String getAsset_cd() {
		return asset_cd;
	}

	public void setAsset_cd(String asset_cd) {
		this.asset_cd = asset_cd;
	}

	public String getPresent() {
		return present;
	}

	public void setPresent(String present) {
		this.present = present;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory_cd() {
		return category_cd;
	}

	public void setCategory_cd(String category_cd) {
		this.category_cd = category_cd;
	}

	public ArrayList<StockNewGetterSetter> getListSkuData() {
		return listSkuData;
	}

	public void setListSkuData(ArrayList<StockNewGetterSetter> listSkuData) {
		this.listSkuData = listSkuData;
	}

	public ArrayList<ChecklistInsertDataGetterSetter> getChecklist() {
		return checklist;
	}

	public void setChecklist(ArrayList<ChecklistInsertDataGetterSetter> checklist) {
		this.checklist = checklist;
	}

	public ArrayList<SkuGetterSetter> getSkulist() {
		return skulist;
	}

	public void setSkulist(ArrayList<SkuGetterSetter> skulist) {
		this.skulist = skulist;
	}

	public String getKey_id() {
		return key_id;
	}

	public void setKey_id(String key_id) {
		this.key_id = key_id;
	}

	public String getPlanogram_img() {
		return planogram_img;
	}

	public void setPlanogram_img(String planogram_img) {
		this.planogram_img = planogram_img;
	}

	String reason="";

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReason_cd() {
		return reason_cd;
	}

	public void setReason_cd(String reason_cd) {
		this.reason_cd = reason_cd;
	}

	String reason_cd="";
}
