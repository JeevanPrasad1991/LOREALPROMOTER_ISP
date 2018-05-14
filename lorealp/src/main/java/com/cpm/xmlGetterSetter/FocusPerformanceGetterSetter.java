package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

public class FocusPerformanceGetterSetter {

    private String  focusperformance_table;
    ArrayList<String> STORE_CD = new ArrayList<String>();
    ArrayList<Float> FOCUS_TARGET = new ArrayList<Float>();
    ArrayList<Float> SALES = new ArrayList<Float>();




    public String getFocusperformance_table() {
        return focusperformance_table;
    }

    public void setFocusperformance_table(String focusperformance_table) {
        this.focusperformance_table = focusperformance_table;
    }



    public ArrayList<String> getSTORE_CD() {
        return STORE_CD;
    }

    public void setSTORE_CD(String STORE_CD) {
        this.STORE_CD.add(STORE_CD);
    }

    public ArrayList<Float> getFOCUS_TARGET() {
        return FOCUS_TARGET;
    }

    public void setFOCUS_TARGET(Float FOCUS_TARGET) {
        this.FOCUS_TARGET.add(FOCUS_TARGET);
    }

    public ArrayList<Float> getSALES() {
        return SALES;
    }

    public void setSALES(Float SALES) {
        this.SALES.add(SALES);
    }
}
