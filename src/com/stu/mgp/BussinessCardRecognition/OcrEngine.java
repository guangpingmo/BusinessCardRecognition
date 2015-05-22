package com.stu.mgp.BussinessCardRecognition;

import android.util.Log;

public class OcrEngine {
	public String resultString;
	public static OcrEngine cloudOcrEngine;
	public static OcrEngine localOcrEngine;
	
	public static OcrEngine getOcrEngine(ResultsActivity currentActivity, String method, String lang)
	{
		if(method == "local")
		{
//			if(localOcrEngine == null)
//			{
//				localOcrEngine = new LocalOcrEngine(currentActivity, lang);
//			}
//			else
//			{
//				localOcrEngine.setLanguage(lang);
//			}
			
			Log.d(MainActivity.TAG, "getOcrEngine" + " local");
			localOcrEngine = new LocalOcrEngine(currentActivity, lang);
			return localOcrEngine;
		}
		else
		{
//			if(cloudOcrEngine == null)
//			{
//				cloudOcrEngine = new CloudOcrEngine(currentActivity, lang);
//			}
//			else
//			{
//				cloudOcrEngine.setLanguage(lang);
//			}
			Log.d(MainActivity.TAG, "getOcrEngine" + " network");
			cloudOcrEngine = new CloudOcrEngine(currentActivity, lang);
			return cloudOcrEngine;
		}
	}
	public void recognize()
	{
		
	}
	
	public void setLanguage(String lang)
	{
		
	}
}
