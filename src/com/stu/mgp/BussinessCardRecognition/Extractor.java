package com.stu.mgp.BussinessCardRecognition;

public  class Extractor {
	
	public static String TAG = "Extractor";
	
	public String name;
	public String phoneNumber;
	public String email;
	
	static Extractor engExtractor = new EngExtractor();
	static Extractor chiSimExtractor = new ChiSimExtractor();
	
	public static Extractor getExtractor(String lang)
	{
		if(lang == "eng")
		{
			return engExtractor.reset();
		}
		else
		{
			return chiSimExtractor.reset();
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
	
	//解析不出的字段赋值为空字符串
	public void normalize()
	{
		if(name == null)
		{
			name = "";
		}
		if(phoneNumber == null)
		{
			phoneNumber = "";
		}
		if(email == null)
		{
			email = "";
		}
	}
	
	//重置各个字段为null
	public Extractor reset()
	{
		name = null;
		phoneNumber = null;
		email = null;
		
		return this;
	}
	
}
