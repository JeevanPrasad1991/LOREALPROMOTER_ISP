package com.cpm.xmlGetterSetter;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by jeevanp on 10-10-2017.
 */

public class NonPromotionReasonGetterSetter {
    public String nonpromotionreasonTable;
    ArrayList<String> preason_cd = new ArrayList<>();

    public String getNonpromotionreasonTable() {
        return nonpromotionreasonTable;
    }

    public void setNonpromotionreasonTable(String nonpromotionreasonTable) {
        this.nonpromotionreasonTable = nonpromotionreasonTable;
    }

    public ArrayList<String> getPreason_cd() {
        return preason_cd;
    }

    public void setPreason_cd(String preason_cd) {
        this.preason_cd.add(preason_cd);
    }

    public ArrayList<String> getPreason() {
        return preason;
    }

    public void setPreason(String preason) {
        this.preason.add(preason);
    }

    ArrayList<String> preason = new ArrayList<>();
}
