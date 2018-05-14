package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by rishikaa on 10-05-2016.
 */
public class IncentiveGetterSetter {
    String incentive_table;
    ArrayList<String> month = new ArrayList<String>();
    ArrayList<String> year = new ArrayList<String>();
    ArrayList<String> incentive = new ArrayList<String>();

    public String getIncentive_table() {
        return incentive_table;
    }

    public void setIncentive_table(String incentive_table) {
        this.incentive_table = incentive_table;
    }

    public ArrayList<String> getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month.add(month);
    }

    public ArrayList<String> getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year.add(year);
    }

    public ArrayList<String> getIncentive() {
        return incentive;
    }

    public void setIncentive(String incentive) {
        this.incentive.add(incentive);
    }
}
