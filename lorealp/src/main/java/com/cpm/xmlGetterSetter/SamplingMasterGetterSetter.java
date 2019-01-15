package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by upendra on 10-05-2018.
 */
public class SamplingMasterGetterSetter {
    String sampling_master_table;
    ArrayList<String> SAMPLE_CD = new ArrayList<String>();
    ArrayList<String> SAMPLE = new ArrayList<String>();

    public String getSampling_master_table() {
        return sampling_master_table;
    }

    public void setSampling_master_table(String sampling_master_table) {
        this.sampling_master_table = sampling_master_table;
    }

    public ArrayList<String> getSAMPLE_CD() {
        return SAMPLE_CD;
    }

    public void setSAMPLE_CD(String SAMPLE_CD) {
        this.SAMPLE_CD.add(SAMPLE_CD);
    }

    public ArrayList<String> getSAMPLE() {
        return SAMPLE;
    }

    public void setSAMPLE(String SAMPLE) {
        this.SAMPLE.add(SAMPLE);
    }
}
