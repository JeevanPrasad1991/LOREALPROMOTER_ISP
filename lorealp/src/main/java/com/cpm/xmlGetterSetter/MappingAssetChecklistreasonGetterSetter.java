package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by yadavendras on 13-06-2017.
 */

public class MappingAssetChecklistreasonGetterSetter {

    ArrayList<String> CHECKLIST_ID = new ArrayList<>();
    ArrayList<String> CREASON_ID = new ArrayList<>();

    String table_MAPPING_ASSET_CHECKLIST_REASON;

    public ArrayList<String> getCHECKLIST_ID() {
        return CHECKLIST_ID;
    }

    public void setCHECKLIST_ID(String CHECKLIST_ID) {
        this.CHECKLIST_ID.add(CHECKLIST_ID);
    }

    public ArrayList<String> getCREASON_ID() {
        return CREASON_ID;
    }

    public void setCREASON_ID(String CREASON_ID) {
        this.CREASON_ID.add(CREASON_ID);
    }

    public String getTable_MAPPING_ASSET_CHECKLIST_REASON() {
        return table_MAPPING_ASSET_CHECKLIST_REASON;
    }

    public void setTable_MAPPING_ASSET_CHECKLIST_REASON(String table_MAPPING_ASSET_CHECKLIST_REASON) {
        this.table_MAPPING_ASSET_CHECKLIST_REASON = table_MAPPING_ASSET_CHECKLIST_REASON;
    }
}
