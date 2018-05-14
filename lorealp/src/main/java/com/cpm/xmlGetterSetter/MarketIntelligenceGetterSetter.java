package com.cpm.xmlGetterSetter;

/**
 * Created by jeevanp on 10-10-2017.
 */

public class MarketIntelligenceGetterSetter {
    String company_cd;
    String company;
    String category_cd;
    String category;
    String promotype_cd;
    String promotype;
    String remark;
    String photo;
    String subcategory;
    String subcategory_cd;

    public String getSubcategory_cd() {
        return subcategory_cd;
    }

    public void setSubcategory_cd(String subcategory_cd) {
        this.subcategory_cd = subcategory_cd;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }



    public String getCompany_cd() {
        return company_cd;
    }
    public void setCompany_cd(String company_cd) {
        this.company_cd = company_cd;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCategory_cd() {
        return category_cd;
    }

    public void setCategory_cd(String category_cd) {
        this.category_cd = category_cd;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPromotype_cd() {
        return promotype_cd;
    }

    public void setPromotype_cd(String promotype_cd) {
        this.promotype_cd = promotype_cd;
    }

    public String getPromotype() {
        return promotype;
    }

    public void setPromotype(String promotype) {
        this.promotype = promotype;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isExists() {
        return isExists;
    }

    public void setExists(boolean exists) {
        isExists = exists;
    }

    boolean isExists;
    public String getKey_id() {
        return key_id;
    }
    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }
    String key_id;
}
