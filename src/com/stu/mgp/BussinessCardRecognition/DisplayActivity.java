package com.stu.mgp.BussinessCardRecognition;

import java.io.File;
import java.io.IOException;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

public class DisplayActivity extends ActionBarActivity {

	ImageView mImageView;
	String outText;

	// 识别方式
	public static String mLang = "eng";
	public static String mMethod = "network";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_display);
		// getSupportActionBar().hide();
		mImageView = (ImageView) findViewById(R.id.cardView);

		String imageUrl = "unknown";

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			imageUrl = extras.getString("IMAGE_PATH");
			outText = extras.getString("RESULT_PATH");
		}

		Log.d(MainActivity.TAG, imageUrl);
		Log.d(MainActivity.TAG, mMethod + " " + mLang);
		
		mImageView.setImageURI(Uri.fromFile(new File(imageUrl)));
		
		
		
		

	}
	
	//turn the image to grayscale
	public void turnImageToGray()
	{
		Log.d(MainActivity.TAG, "downsize and grayscale");
    	try {
			File newFile = ImageTool.downsampleAndGray(MainActivity.ocrPicture.toString());
			MainActivity.ocrPicture = newFile;
			mImageView.setImageURI(Uri.fromFile(newFile));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	// method called when RadioButton is clicked
	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch (view.getId()) {
		case R.id.eng:
			if (checked)
				mLang = "eng";
			break;
		case R.id.chi:
			if (checked)
				mLang = "chi_sim";
			break;
		case R.id.local:
			if (checked)
				mMethod = "local";
			break;
		case R.id.network:
			if (checked)
				mMethod = "network";
			break;
		}
		
		Log.d(MainActivity.TAG, "onRadioButtonClicked " + mMethod + " " + mLang);
	}

	// method called when RecognizeButton is clicked
	public void onRecognizeButtonClicked(View view) {
		Log.d(MainActivity.TAG, "DisplayActivity onRecognizeButtonClicked(View view)");
		Intent results = new Intent(this, ResultsActivity.class);
		startActivity(results);
	}
	
	
	

}
