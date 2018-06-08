package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

public class FocusPerformanceGetterSetter {

    private String  focusperformance_table;
    ArrayList<String> STORE_CD = new ArrayList<String>();
    ArrayList<String> FOCUS_TARGET = new ArrayList<String>();
    ArrayList<String> SALES = new ArrayList<String>();
    ArrayList<String> ALL_TARGET = new ArrayList<String>();
    ArrayList<String> SHOW_TARGET = new ArrayList<String>();
    ArrayList<String> STATUS = new ArrayList<String>();

    public ArrayList<String> getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS.add(STATUS);
    }

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

    public ArrayList<String> getFOCUS_TARGET() {
        return FOCUS_TARGET;
    }

    public void setFOCUS_TARGET(String FOCUS_TARGET) {
        this.FOCUS_TARGET.add(FOCUS_TARGET);
    }

    public ArrayList<String> getSALES() {
        return SALES;
    }

    public void setSALES(String SALES) {
        this.SALES.add(SALES);
    }

    public ArrayList<String> getALL_TARGET() {
        return ALL_TARGET;
    }

    public void setALL_TARGET(String ALL_TARGET) {
        this.ALL_TARGET.add(ALL_TARGET);
    }

    public ArrayList<String> getSHOW_TARGET() {
        return SHOW_TARGET;
    }

    public void setSHOW_TARGET(String SHOW_TARGET) {
        this.SHOW_TARGET.add(SHOW_TARGET);
    }

}
