package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by yadavendras on 13-06-2017.
 */

public class NonComplianceChecklistGetterSetter {

    ArrayList<String> CREASON_ID = new ArrayList<>();
    ArrayList<String> CREASON = new ArrayList<>();

    String table_NON_COMPLIANCE_CHECKLIST;

    public ArrayList<String> getCREASON_ID() {
        return CREASON_ID;
    }

    public void setCREASON_ID(String CREASON_ID) {
        this.CREASON_ID.add(CREASON_ID);
    }

    public ArrayList<String> getCREASON() {
        return CREASON;
    }

    public void setCREASON(String CREASON) {
        this.CREASON.add(CREASON);
    }

    public String getTable_NON_COMPLIANCE_CHECKLIST() {
        return table_NON_COMPLIANCE_CHECKLIST;
    }

    public void setTable_NON_COMPLIANCE_CHECKLIST(String table_NON_COMPLIANCE_CHECKLIST) {
        this.table_NON_COMPLIANCE_CHECKLIST = table_NON_COMPLIANCE_CHECKLIST;
    }
}
