package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

/**
 * Created by rishikaa on 10-05-2016.
 */
public class TodayQuestionGetterSetter {
    String today_question_table;
    ArrayList<String> TOT_QUESTION = new ArrayList<String>();
    ArrayList<String> RIGHT_ANSWER = new ArrayList<String>();

    public String getToday_question_table() {
        return today_question_table;
    }

    public void setToday_question_table(String today_question_table) {
        this.today_question_table = today_question_table;
    }

    public ArrayList<String> getTOT_QUESTION() {
        return TOT_QUESTION;
    }

    public void setTOT_QUESTION(String TOT_QUESTION) {
        this.TOT_QUESTION.add(TOT_QUESTION);
    }

    public ArrayList<String> getRIGHT_ANSWER() {
        return RIGHT_ANSWER;
    }

    public void setRIGHT_ANSWER(String RIGHT_ANSWER) {
        this.RIGHT_ANSWER.add(RIGHT_ANSWER);
    }
}
