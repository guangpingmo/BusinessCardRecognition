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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

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

		mImageView.setImageURI(Uri.fromFile(MainActivity.ocrPicture));
		

	}

	// turn the image to grayscale
	public void turnImageToGray() {
		Log.d(MainActivity.TAG, "downsize and grayscale");
		try {
			File newFile = ImageTool.downsampleAndGray(MainActivity.ocrPicture
					.toString());
			MainActivity.ocrPicture = newFile;
			mImageView.setImageURI(Uri.fromFile(newFile));
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	// preprocessing the image , rectify the image
	public void rectify(View view) {
		Toast.makeText(this, "对正", Toast.LENGTH_LONG).show();

		Log.d(MainActivity.TAG, "rectify");
		ImageTool.rectify();
		mImageView.setImageURI(Uri.fromFile(MainActivity.ocrPicture));

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
		Log.d(MainActivity.TAG,
				"DisplayActivity onRecognizeButtonClicked(View view)");
		Intent results = new Intent(this, ResultsActivity.class);
		startActivity(results);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.display, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.rotateAntiClockwise:
			Toast.makeText(this, "antiClockWise", Toast.LENGTH_LONG).show();
			try {
				ImageTool.rotate(MainActivity.ocrPicture.getPath(), 90, 50);
				mImageView.setImageURI(Uri.fromFile(MainActivity.ocrPicture));
				mImageView.invalidate(); //刷新图片
				mImageView.refreshDrawableState();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			break;
		case R.id.rotateClockwise:
			Toast.makeText(this, "ClockWise", Toast.LENGTH_LONG).show();
			try {
				ImageTool.rotate(MainActivity.ocrPicture.getPath(), -90, 50);
				
				mImageView.setImageURI(Uri.fromFile(MainActivity.ocrPicture));
				mImageView.invalidate();
				mImageView.refreshDrawableState();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
