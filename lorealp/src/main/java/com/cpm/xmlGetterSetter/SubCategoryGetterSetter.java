package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by yadavendras on 29-01-2016.
 */
public class SubCategoryGetterSetter {

    String sub_category_master_table;

    ArrayList<String> SUB_CATEGORY_CD=new ArrayList<String>();
    ArrayList<String> SUB_CATEGORY=new ArrayList<String>();
    ArrayList<String> CATEGORY_CD=new ArrayList<String>();

    public String getSub_category_master_table() {
        return sub_category_master_table;
    }

    public void setSub_category_master_table(String sub_category_master_table) {
        this.sub_category_master_table = sub_category_master_table;
    }

    public ArrayList<String> getSUB_CATEGORY_CD() {
        return SUB_CATEGORY_CD;
    }

    public void setSUB_CATEGORY_CD(String SUB_CATEGORY_CD) {
        this.SUB_CATEGORY_CD.add(SUB_CATEGORY_CD);
    }

    public ArrayList<String> getSUB_CATEGORY() {
        return SUB_CATEGORY;
    }

    public void setSUB_CATEGORY(String SUB_CATEGORY) {
        this.SUB_CATEGORY.add(SUB_CATEGORY);
    }

    public ArrayList<String> getCATEGORY_CD() {
        return CATEGORY_CD;
    }

    public void setCATEGORY_CD(String CATEGORY_CD) {
        this.CATEGORY_CD.add(CATEGORY_CD);
    }
}
