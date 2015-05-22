package com.stu.mgp.BussinessCardRecognition;

public  class Extractor {
	
	public static String TAG = "Extractor";
	
	String name;
	String phoneNumber;
	String email;
	
	static Extractor engExtractor = new EngExtractor();
	static Extractor chiSimExtractor = new ChiSimExtractor();
	
	public static Extractor getExtractor(String lang)
	{
		if(lang == "eng")
		{
			return engExtractor;
		}
		else
		{
			return chiSimExtractor;
		}
	}
	
	public String trimInfomation(String inputText)
	{
		String[] strArr = inputText.split("\\n");
		String result = "";
		for(String s: strArr)
		{
			s = s.trim();
			result += s + "\n";
		}
		return result;
	}
	
	
	public void extract(String inputText)
	{
		
	}
	
	
	@Override
	public String toString() {
		String result = "name: " + name + " email: " + email + " phoneNumber: " + phoneNumber;
		return result;
	}

}
