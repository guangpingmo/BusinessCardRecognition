package com.stu.mgp.BussinessCardRecognition;

public class LocalOcrEngine extends OcrEngine{
	
	ResultsActivity currentActivity;
	String language;
	
	public LocalOcrEngine(ResultsActivity currentActivity, String language) {
		this.currentActivity = currentActivity;
		setLanguage(language);
	}
	
	@Override
	public void setLanguage(String lang) {
		if (lang == "eng") {
			this.language = "eng";
		} else {
			this.language = "chi_sim";
		}
	}
	
	@Override
	public void recognize() {
		// TODO Auto-generated method stub
		super.recognize();
	}
}
