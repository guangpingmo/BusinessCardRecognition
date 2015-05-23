package com.stu.mgp.BussinessCardRecognition;

import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

public class LocalOcrEngine extends OcrEngine {

	ResultsActivity currentActivity;
	String language;
	public static TessBaseAPI baseApi = new TessBaseAPI();

	public LocalOcrEngine(ResultsActivity currentActivity, String language) {
		this.currentActivity = currentActivity;
		setLanguage(language);
	}

	@Override
	public void setLanguage(String lang) {
		if (lang == "eng") {
			this.language = "eng";
		} else {
			//中文时会设置两种语言, 因为电子邮件是英文字母
			this.language = "chi_sim+eng";
		}
		//设置Tesseract的识别语言
		baseApi.init(MainActivity.appBasePath.toString(), language);
		baseApi.setDebug(true);
	}

	@Override
	public void recognize() {
		/*
		 * 参见 
		 * https://github.com/rmtheis/tess-two
		 * https://github.com/tesseract-ocr/tesseract
		 */
		
		baseApi.setImage(MainActivity.ocrPicture);
		String result = baseApi.getUTF8Text();
		
		Log.d(MainActivity.TAG, "recognize result " + result);
		currentActivity.resultEditText.setText(result);

		
	}
}
