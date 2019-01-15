package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by upendra on 10-05-2018.
 */
public class MapingSamplingGetterSetter {
    String maping_sampling_table;
    ArrayList<String> STORE_CD = new ArrayList<String>();
    ArrayList<String> SAMPLE_CD = new ArrayList<String>();

    public String getMaping_sampling_table() {
        return maping_sampling_table;
    }

    public void setMaping_sampling_table(String maping_sampling_table) {
        this.maping_sampling_table = maping_sampling_table;
    }

    public ArrayList<String> getSTORE_CD() {
        return STORE_CD;
    }

    public void setSTORE_CD(String STORE_CD) {
        this.STORE_CD.add(STORE_CD);
    }

    public ArrayList<String> getSAMPLE_CD() {
        return SAMPLE_CD;
    }

    public void setSAMPLE_CD(String SAMPLE_CD) {
        this.SAMPLE_CD.add(SAMPLE_CD);
    }
}
