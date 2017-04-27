package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

public class Audit_QuestionGetterSetter {

    String audit_question_table;

    ArrayList<String> QUESTION_ID = new ArrayList<>();
    ArrayList<String> QUESTION = new ArrayList<>();
    ArrayList<String> ANSWER_ID = new ArrayList<>();
    ArrayList<String> ANSWER = new ArrayList<>();
    ArrayList<String> QUESTION_TYPE = new ArrayList<>();

    public String getAudit_question_table() {
        return audit_question_table;
    }

    public void setAudit_question_table(String audit_question_table) {
        this.audit_question_table = audit_question_table;
    }

    public ArrayList<String> getQUESTION_ID() {
        return QUESTION_ID;
    }

    public void setQUESTION_ID(String QUESTION_ID) {
        this.QUESTION_ID.add(QUESTION_ID);
    }

    public ArrayList<String> getQUESTION() {
        return QUESTION;
    }

    public void setQUESTION(String QUESTION) {
        this.QUESTION.add(QUESTION);
    }

    public ArrayList<String> getANSWER_ID() {
        return ANSWER_ID;
    }

    public void setANSWER_ID(String ANSWER_ID) {
        this.ANSWER_ID.add(ANSWER_ID);
    }

    public ArrayList<String> getANSWER() {
        return ANSWER;
    }

    public void setANSWER(String ANSWER) {
        this.ANSWER.add(ANSWER);
    }

    public ArrayList<String> getQUESTION_TYPE() {
        return QUESTION_TYPE;
    }

    public void setQUESTION_TYPE(String QUESTION_TYPE) {
        this.QUESTION_TYPE.add(QUESTION_TYPE);
    }
}
