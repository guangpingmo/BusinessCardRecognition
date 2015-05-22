package com.stu.mgp.BussinessCardRecognition;

import android.util.Log;
import android.view.View;


//云Ocr引擎, 将图片上传到云端ocr引擎进行识别, 然后将结果下载到本地, 使用 https://cloud.ocrsdk.com
public class CloudOcrEngine extends OcrEngine {
	ResultsActivity currentActivity;
	String language;

	public CloudOcrEngine(ResultsActivity currentActivity, String language) {
		this.currentActivity = currentActivity;
		setLanguage(language);
	}

	@Override
	public void setLanguage(String lang) {
		if (lang == "eng") {
			this.language = "English";
		} else {
			this.language = "ChinesePRC,English";
		}
	}
	
	@Override
	public void recognize() {
		Log.d(MainActivity.TAG, "CloudOcrEngine recognize()");
		new AsyncProcessTask(currentActivity, language).execute(
				MainActivity.ocrPicture.getPath(), MainActivity.ocrText.getPath());
	}
	
	
}
