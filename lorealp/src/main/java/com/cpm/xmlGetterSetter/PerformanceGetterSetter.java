package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

public class PerformanceGetterSetter {

    private String  performance_table;
    ArrayList<String> STORE_CD = new ArrayList<String>();
    ArrayList<Float> STORE_TARGET = new ArrayList<Float>();
    ArrayList<Float> SALES = new ArrayList<Float>();

    public String getPerformance_table() {
        return performance_table;
    }

    public void setPerformance_table(String performance_table) {
        this.performance_table = performance_table;
    }

    public ArrayList<String> getSTORE_CD() {
        return STORE_CD;
    }

    public void setSTORE_CD(String STORE_CD) {
        this.STORE_CD.add(STORE_CD);
    }

    public ArrayList<Float> getSTORE_TARGET() {
        return STORE_TARGET;
    }

    public void setSTORE_TARGET(Float STORE_TARGET) {
        this.STORE_TARGET.add(STORE_TARGET);
    }

    public ArrayList<Float> getSALES() {
        return SALES;
    }

    public void setSALES(Float SALES) {
        this.SALES.add(SALES);
    }
}
