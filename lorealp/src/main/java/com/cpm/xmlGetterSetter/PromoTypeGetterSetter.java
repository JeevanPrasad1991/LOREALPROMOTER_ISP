package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by jeevanp on 23-10-2017.
 */

public class PromoTypeGetterSetter {
    public String protypeTable;
    ArrayList<String>promoType=new ArrayList<>();

    public String getProtypeTable() {
        return protypeTable;
    }

    public void setProtypeTable(String protypeTable) {
        this.protypeTable = protypeTable;
    }

    public ArrayList<String> getPromoType() {
        return promoType;
    }

    public void setPromoType(String promoType) {
        this.promoType.add(promoType);
    }

    public ArrayList<String> getPromoType_cd() {
        return promoType_cd;
    }

    public void setPromoType_cd(String promoType_cd) {
        this.promoType_cd.add(promoType_cd);
    }

    ArrayList<String>promoType_cd=new ArrayList<>();

}

