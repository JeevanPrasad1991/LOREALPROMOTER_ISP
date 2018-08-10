package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

public class FeedbackQuestionRatingGettersetter {
	String feedback_question_rating_table;

	ArrayList<String> FCATEGORY_CD= new ArrayList<String>();
	ArrayList<String> FCATEGORY = new ArrayList<String>();
	ArrayList<String> FQUESTION_CD = new ArrayList<String>();
	ArrayList<String> FQUESTION = new ArrayList<String>();
	ArrayList<String> FANSWER_CD = new ArrayList<String>();
	ArrayList<String> FANSWER = new ArrayList<String>();

	public String getFeedback_question_rating_table() {
		return feedback_question_rating_table;
	}

	public void setFeedback_question_rating_table(String feedback_question_rating_table) {
		this.feedback_question_rating_table = feedback_question_rating_table;
	}

	public ArrayList<String> getFCATEGORY_CD() {
		return FCATEGORY_CD;
	}

	public void setFCATEGORY_CD(String FCATEGORY_CD) {
		this.FCATEGORY_CD.add(FCATEGORY_CD);
	}

	public ArrayList<String> getFCATEGORY() {
		return FCATEGORY;
	}

	public void setFCATEGORY(String FCATEGORY) {
		this.FCATEGORY.add(FCATEGORY);
	}

	public ArrayList<String> getFQUESTION_CD() {
		return FQUESTION_CD;
	}

	public void setFQUESTION_CD(String FQUESTION_CD) {
		this.FQUESTION_CD.add(FQUESTION_CD);
	}

	public ArrayList<String> getFQUESTION() {
		return FQUESTION;
	}

	public void setFQUESTION(String FQUESTION) {
		this.FQUESTION.add(FQUESTION);
	}

	public ArrayList<String> getFANSWER_CD() {
		return FANSWER_CD;
	}

	public void setFANSWER_CD(String FANSWER_CD) {
		this.FANSWER_CD.add(FANSWER_CD);
	}

	public ArrayList<String> getFANSWER() {
		return FANSWER;
	}

	public void setFANSWER(String FANSWER) {
		this.FANSWER.add(FANSWER);
	}
}
