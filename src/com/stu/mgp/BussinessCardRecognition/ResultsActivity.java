package com.stu.mgp.BussinessCardRecognition;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ResultsActivity extends ActionBarActivity {

	public static EditText resultEditText;
	Button addContactButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		resultEditText = (EditText) findViewById(R.id.result);
		addContactButton = (Button) findViewById(R.id.addContact);

		OcrEngine.getOcrEngine(this, DisplayActivity.mMethod,
				DisplayActivity.mLang).recognize();

	}

	// stub method for AsyncProcessTask
	public void updateResults(Boolean success) {
		if (!success)
			return;
		try {
			StringBuffer contents = new StringBuffer();

			FileInputStream fis = new FileInputStream(MainActivity.ocrText);
			try {
				Reader reader = new InputStreamReader(fis, "UTF-8");
				BufferedReader bufReader = new BufferedReader(reader);
				String text = null;
				while ((text = bufReader.readLine()) != null) {
					contents.append(text).append(
							System.getProperty("line.separator"));
				}
			} finally {
				fis.close();
			}

			displayMessage(contents.toString());
		} catch (Exception e) {
			displayMessage("Error: " + e.getMessage());
		}
	}

	public void displayMessage(String text) {
		Log.d(MainActivity.TAG, "update message");
		// tv.post(new MessagePoster(text));
		resultEditText.setText(text);
	}

	// Button listener for addContact
	public void onAddContactClicked(View view) {
		Log.d(MainActivity.TAG, "onAddContactClicked");
		String inputText = resultEditText.getText().toString();
		Log.d(MainActivity.TAG, "inputText : " + inputText);
		
		Extractor extractor = Extractor.getExtractor(DisplayActivity.mLang);
		extractor.extract(inputText);
		
		Log.d(MainActivity.TAG, "outPutText : " + extractor.toString());
		
		//添加提取到的信息到通讯录
		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

		extractor.normalize();
		intent.putExtra(ContactsContract.Intents.Insert.NAME, extractor.name);
		intent.putExtra(ContactsContract.Intents.Insert.PHONE, extractor.phoneNumber);
		intent.putExtra(ContactsContract.Intents.Insert.EMAIL, extractor.email);

		
		this.startActivity(intent);
	}
}
