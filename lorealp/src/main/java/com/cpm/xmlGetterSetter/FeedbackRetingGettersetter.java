package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

public class FeedbackRetingGettersetter {
	String feedback_rating_table;

	public String getFeedback_rating_table() {
		return feedback_rating_table;
	}

	public void setFeedback_rating_table(String feedback_rating_table) {
		this.feedback_rating_table = feedback_rating_table;
	}

	ArrayList<String> RATING= new ArrayList<String>();



	public ArrayList<String> getRATING() {
		return RATING;
	}

	public void setRATING(String RATING) {
		this.RATING.add(RATING);
	}
}
