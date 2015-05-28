package com.stu.mgp.BussinessCardRecognition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import android.provider.CalendarContract.Instances;
import android.util.Property;

public class Setting {
	public String method;
	public String language;
	public String abbyy_applicationId;
	public String abbyy_password;
	private Properties pros;
	private static Setting instance;

	public void init(File configFile) {
		pros = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(configFile);
			pros.load(inputStream);
			method = pros.getProperty("ocr.method").trim();
			language = pros.getProperty("ocr.language").trim();
			abbyy_applicationId = pros.getProperty("abbyy.applicationId").trim();
			abbyy_password = pros.getProperty("abbyy.password").trim();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public void save(File configFile) {
		OutputStream out;
		try {
			out = new FileOutputStream(configFile);
			pros.save(out, "the setting, idiot");
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Setting getInstance() {
		if (instance == null) {
			instance = new Setting();
		}
		return instance;
	}

	@Override
	public String toString() {
		return method + " " + language + " " + abbyy_applicationId + " "
				+ abbyy_password;
	}
}
