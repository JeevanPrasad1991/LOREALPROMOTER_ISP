package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

public class POGGetterSetter {

    String QUESTION_ID;
    String QUESTION;
    String ANSWER_ID="0";
    String ANSWER;
    String VISITOR_NAME;
    String VISITOR_DESIGNATION;

    public String getVISITOR_NAME() {
        return VISITOR_NAME;
    }

    public void setVISITOR_NAME(String VISITOR_NAME) {
        this.VISITOR_NAME = VISITOR_NAME;
    }

    public String getVISITOR_DESIGNATION() {
        return VISITOR_DESIGNATION;
    }

    public void setVISITOR_DESIGNATION(String VISITOR_DESIGNATION) {
        this.VISITOR_DESIGNATION = VISITOR_DESIGNATION;
    }

    public String getCATEGORY_CD() {
        return CATEGORY_CD;
    }

    public void setCATEGORY_CD(String CATEGORY_CD) {
        this.CATEGORY_CD = CATEGORY_CD;
    }

    String CATEGORY_CD;

    ArrayList<POGGetterSetter> answerList = new ArrayList<>();

    public String getQUESTION_ID() {
        return QUESTION_ID;
    }

    public void setQUESTION_ID(String QUESTION_ID) {
        this.QUESTION_ID = QUESTION_ID;
    }


    public String getQUESTION() {
        return QUESTION;
    }

    public void setQUESTION(String QUESTION) {
        this.QUESTION = QUESTION;
    }


    public String getANSWER_ID() {
        return ANSWER_ID;
    }

    public void setANSWER_ID(String ANSWER_ID) {
        this.ANSWER_ID = ANSWER_ID;
    }

    public String getANSWER() {
        return ANSWER;
    }

    public void setANSWER(String ANSWER) {
        this.ANSWER = ANSWER;
    }

    public ArrayList<POGGetterSetter> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(ArrayList<POGGetterSetter> answerList) {
        this.answerList = answerList;
    }

}
