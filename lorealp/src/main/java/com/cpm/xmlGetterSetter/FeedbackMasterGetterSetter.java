package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by upendra on 10-05-2018.
 */
public class FeedbackMasterGetterSetter {
    String feedback_table;
    ArrayList<String> FEEDBACK = new ArrayList<String>();

    public String getFeedback_table() {
        return feedback_table;
    }

    public void setFeedback_table(String feedback_table) {
        this.feedback_table = feedback_table;
    }

    public ArrayList<String> getFEEDBACK() {
        return FEEDBACK;
    }

    public void setFEEDBACK(String FEEDBACK) {
        this.FEEDBACK.add(FEEDBACK);
    }
}
