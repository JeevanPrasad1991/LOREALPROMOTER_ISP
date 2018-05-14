package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by jeevanp on 10-10-2017.
 */

public class AssetNonReasonGetterSetter {
    public String NonAssetReasonTable;

    public String getNonAssetReasonTable() {
        return NonAssetReasonTable;
    }

    public void setNonAssetReasonTable(String nonAssetReasonTable) {
        NonAssetReasonTable = nonAssetReasonTable;
    }

    public ArrayList<String> getAreason_cd() {
        return areason_cd;
    }

    public void setAreason_cd(String areason_cd) {
        this.areason_cd.add(areason_cd);
    }

    public ArrayList<String> getAreason() {
        return areason;
    }

    public void setAreason(String areason) {
        this.areason.add(areason);
    }

    public ArrayList<String>areason_cd=new ArrayList<>();
    public ArrayList<String>areason=new ArrayList<>();

}
