package com.stu.mgp.BussinessCardRecognition;

import java.io.File;
import java.io.IOException;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.BitmapFactory;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_display);
		// getSupportActionBar().hide();
		mImageView = (ImageView) findViewById(R.id.cardView);

		// reflesh the GUI
		refleshGUI();

		// reflesh the content of mImageView
		refleshImage();
		Log.d(MainActivity.TAG, "Setting: " + Setting.getInstance().toString());

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
		if (ImageTool.rectify()) {
			MainActivity.ocrPicture = ImageTool
					.getFileFromPreProcessPath("binary.jpg");
			refleshImage();
		} else {
			Toast.makeText(this, "像素太低无法对正", Toast.LENGTH_LONG).show();
		}

	}

	public void refleshGUI() {
		switch (Setting.getInstance().language) {
		case "eng":
			((RadioButton) findViewById(R.id.eng)).setChecked(true);
			break;
		case "chi_sim":
			((RadioButton) findViewById(R.id.chi)).setChecked(true);
			break;
		case "chi_sim+eng":
			((RadioButton) findViewById(R.id.chiAndEng)).setChecked(true);
			break;
		default:
			break;
		}
		switch (Setting.getInstance().method) {
		case "local":
			((RadioButton) findViewById(R.id.local)).setChecked(true);
			break;
		case "network":
			((RadioButton) findViewById(R.id.network)).setChecked(true);
			break;
		default:
			break;
		}
	}

	// method called when RadioButton is clicked
	public void onRadioButtonClicked(View view) {
		// 识别方式
		String mLang = Setting.getInstance().language;
		String mMethod = Setting.getInstance().method;
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
		case R.id.chiAndEng:
			if (checked)
				mLang = "chi_sim+eng";
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
		Setting.getInstance().language = mLang;
		Setting.getInstance().method = mMethod;

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
			Toast.makeText(this, "逆时针旋转90度", Toast.LENGTH_LONG).show();
			toRotate(90);
			break;
		case R.id.rotateClockwise:
			Toast.makeText(this, "顺时针旋转90度", Toast.LENGTH_LONG).show();
			toRotate(-90);
			break;
		case R.id.gray:
			Toast.makeText(this, "灰度化", Toast.LENGTH_LONG).show();
			toGray();
			break;
		case R.id.binary:
			Toast.makeText(this, "二值化", Toast.LENGTH_LONG).show();
			toBinarization();
			break;
		case R.id.resetImage:
			Toast.makeText(this, "重设图像", Toast.LENGTH_LONG).show();
			resetImage();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void resetImage() {
		File file = new File(MainActivity.appOcrPicturePath,
				MainActivity.dateOfRecognition + ".jpg");
		Log.d(MainActivity.TAG, "resetImage :" + file.getPath().toString());
		MainActivity.ocrPicture = file;
		refleshImage();
	}

	private void toRotate(double angle) {
		File dst = new File(MainActivity.appImagePreprocessPath,
				MainActivity.dateOfRecognition + "-" + "toRotate.jpg");
		dst = ImageTool.toRotate(MainActivity.ocrPicture, dst, angle, 80);
		if (dst != null) {
			MainActivity.ocrPicture = dst;
			refleshImage();
		}
	}

	private void toGray() {
		File dst = new File(MainActivity.appImagePreprocessPath,
				MainActivity.dateOfRecognition + "-" + "toGray.jpg");
		dst = ImageTool.toGray(MainActivity.ocrPicture, dst);
		if (dst != null) {
			MainActivity.ocrPicture = dst;
			refleshImage();
		}
	}

	private void toBinarization() {
		File dst = new File(MainActivity.appImagePreprocessPath,
				MainActivity.dateOfRecognition + "-" + "toBinarization.jpg");
		dst = ImageTool.toBinarization(MainActivity.ocrPicture, dst);
		if (dst != null) {
			MainActivity.ocrPicture = dst;
			refleshImage();
		}
	}

	// 刷新ImageView的内容
	private void refleshImage() {
		// mImageView.setImageURI(Uri.fromFile(MainActivity.ocrPicture));
		mImageView.setImageBitmap(BitmapFactory
				.decodeFile(MainActivity.ocrPicture.getPath()));
	}

}
