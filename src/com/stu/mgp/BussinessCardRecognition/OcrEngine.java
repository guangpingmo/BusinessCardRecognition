package com.stu.mgp.BussinessCardRecognition;

import android.util.Log;

public class OcrEngine {
	public String resultString;
	public static OcrEngine cloudOcrEngine;
	public static OcrEngine localOcrEngine;
	
	public static OcrEngine getOcrEngine(ResultsActivity currentActivity, String method, String lang)
	{
		Log.d(MainActivity.TAG, "getOcrEngine " + method + " " + lang);
		if(method.equalsIgnoreCase("local"))
		{			
			Log.d(MainActivity.TAG, "getOcrEngine" + " local" + " " + lang);
			localOcrEngine = new LocalOcrEngine(currentActivity, lang);
			return localOcrEngine;
		}
		else
		{
			Log.d(MainActivity.TAG, "getOcrEngine" + " network" + " " + lang);
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
