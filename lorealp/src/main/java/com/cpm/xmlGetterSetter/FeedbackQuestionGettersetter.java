package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

public class FeedbackQuestionGettersetter {
	String feedback_question_table;

	public String getFeedback_question_table() {
		return feedback_question_table;
	}

	public void setFeedback_question_table(String feedback_question_table) {
		this.feedback_question_table = feedback_question_table;
	}

	ArrayList<String> FQUESTION_ID= new ArrayList<String>();
	ArrayList<String> FQUESTION = new ArrayList<String>();

	public ArrayList<String> getFQUESTION_ID() {
		return FQUESTION_ID;
	}

	public void setFQUESTION_ID(String FQUESTION_ID) {
		this.FQUESTION_ID.add(FQUESTION_ID);
	}

	public ArrayList<String> getFQUESTION() {
		return FQUESTION;
	}

	public void setFQUESTION(String FQUESTION) {
		this.FQUESTION.add(FQUESTION);
	}
}
