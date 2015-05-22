package com.stu.mgp.BussinessCardRecognition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EngExtractor extends Extractor {

	@Override
	public void extract(String inputText) {
		inputText = trimInfomation(inputText);
		Pattern p;
	    Matcher m;

	    /*
	     * Name-matching Expression - Matches: T.V. Raman Alan Viverette Charles L.
	     * Chen Julie Lythcott-Haimes - Does not match: Google Google User
	     * Experience Team 650-720-5555 cell
	     */
	    p = Pattern.compile("^([A-Z]([a-z]*|\\.) *){1,2}([A-Z][a-z]+-?)+$", Pattern.MULTILINE);
	    m = p.matcher(inputText);

	    if (m.find()) {
	      name = m.group().toString();
	    }
	    
	    /*
	     * Email-matching Expression - Matches: email: raman@google.com
	     * spam@google.co.uk v0nn3gu7@ice9.org name @ host.com - Does not match:
	     * #@/.cJX Google c@t
	     */
	    //p = Pattern.compile("([A-Za-z0-9]+ *@ *[A-Za-z0-9]+(\\.[A-Za-z]{2,4})+)$", Pattern.MULTILINE);
	    //p = Pattern.compile("(.+ *@ *.+(\\..{2,4})+)$", Pattern.MULTILINE);
	    p = Pattern.compile("([^ \n]+ *@ *.+(\\..{2,4})+)$", Pattern.MULTILINE);
	    m = p.matcher(inputText);

	    if (m.find()) {
	      email = m.group(1);
	    }

	    /*
	     * Phone-matching Expression - Matches: 1234567890 (650) 720-5678
	     * 650-720-5678 650.720.5678 - Does not match: 12345 12345678901 720-5678
	     */
	    p = Pattern.compile("(?:^|\\D)(\\d{3})[)\\-. ]*?(\\d{3})[\\-. ]*?(\\d{4})(?:$|\\D)");
	    m = p.matcher(inputText);

	    if (m.find()) {
	      String phone = "(" + m.group(1) + ") " + m.group(2) + "-" + m.group(3);
	      
	      phoneNumber = phone;
	    }


//	    //displays results for testing
//	    String output = new String();
//	    output = "Name: " + name + "\n" + "Phone: " + phoneNumber + "\n" + "Email: " + email + "\n";
//	    Log.d(TAG, "Input: " + inputText);
//	    Log.d(TAG,output);
		
	}

}
