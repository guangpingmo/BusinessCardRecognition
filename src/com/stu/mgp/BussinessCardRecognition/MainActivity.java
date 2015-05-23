package com.stu.mgp.BussinessCardRecognition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	public static String TAG = "BusinessCardRecognition";
	// App的目录结构
	public static File appBasePath = new File(
			Environment.getExternalStorageDirectory(), "名片识别");
	public static File appOcrPicturePath = new File(appBasePath, "图片");
	public static File appOcrTextPath = new File(appBasePath, "文本");
	public static File appImagePreprocessPath = new File(appBasePath, "预处理");
	public static String dateOfRecognition = "";

	public static File ocrPicture = null;
	public static File ocrText = null;

	

	//
	private final int TAKE_PICTURE = 0;
	private final int SELECT_FILE = 1;

	

	// 创建App的目录结构
	private void createAppDir() {

		Log.d(TAG, "createAppDir");
		if (!appBasePath.exists()) {
			appBasePath.mkdir();
		}
		if (!appOcrPicturePath.exists()) {
			appOcrPicturePath.mkdir();
		}
		if (!appOcrTextPath.exists()) {
			appOcrTextPath.mkdir();
		}
		if (!appImagePreprocessPath.exists()) {
			appImagePreprocessPath.mkdir();
		}
		
		//复制Tersseract的数据
		initTersseractData();
	}

	// 获取拍照名片的时间来命名图片, 输出的文本名, 格式如: 150521-215633.jpg, 150521-215633.txt

	private String getImageNameFromDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd-HHmmss"); // 时间格式模板
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate); // 时间的格式化
		dateOfRecognition = str;

		ocrPicture = new File(appOcrPicturePath, str + ".jpg");
		ocrText = new File(appOcrTextPath, str + ".txt");

		return str;
	}

	public void captureImageFromSdCard(View view) {

		Log.d(TAG, "captureImageFromSdCard");
		// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		// intent.setType("image/*");

		// 设置输出文本的文件名
		getImageNameFromDate();
		startActivityForResult(intent, SELECT_FILE);
	}

	public void captureImageFromCamera(View view) {
		Log.d(TAG, "captureImageFromCamera");
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

		// 设置拍照的图片文件名
		getImageNameFromDate();
		Uri fileUri = Uri.fromFile(ocrPicture);// 创建一个文件来保存拍摄的图片
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // 设置拍摄的图片名

		startActivityForResult(intent, TAKE_PICTURE);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult " + requestCode + " " + resultCode + " "
				+ data);
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK)
			return;

		String imageFilePath = null;

		switch (requestCode) {
		case TAKE_PICTURE:
			imageFilePath = ocrPicture.getPath();
			try {
				ImageTool.rotate(imageFilePath, -90.0f, 80);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.d(TAG, "TAKE_PICTURE " + imageFilePath);
			break;
		case SELECT_FILE: {
			// 得到选择的文件路径名
			Uri imageUri = data.getData();

			String[] projection = { MediaStore.Images.Media.DATA };
			Cursor cur = managedQuery(imageUri, projection, null, null, null);
			cur.moveToFirst();
			imageFilePath = cur.getString(cur
					.getColumnIndex(MediaStore.Images.Media.DATA));
			ocrPicture = new File(imageFilePath);

			Log.d(TAG, "SELECT_File " + ocrPicture);

		}
			break;
		}

		Intent results = new Intent(this, DisplayActivity.class);
		results.putExtra("IMAGE_PATH", imageFilePath);
		results.putExtra("RESULT_PATH", ocrText.getPath());
		startActivity(results);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		

		createAppDir();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	
	/*
	 * 参见OpenCV官方教程
	 * http://docs.opencv.org/platforms/android/service/doc/BaseLoaderCallback.html
	 * 加载OpenCV类库的回调函数和在Activity恢复时调用OpenCV类库
	 */
	
	private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
		   @Override
		   public void onManagerConnected(int status) {
		     switch (status) {
		       case LoaderCallbackInterface.SUCCESS:
		       {
		          Log.d(MainActivity.TAG, "OpenCV loaded successfully");
		          
		          //在OpenCV初始化成功后加载本地类库
		          //Load native library after(!) OpenCV initialization
                  System.loadLibrary("cardreader");
                  Log.d(TAG, "Native library loaded successfully");
		          
		         
		       } break;
		       default:
		       {
		          super.onManagerConnected(status);
		       } break;
		     }
		   }
		};

		/** Call on every application resume **/
		//加载OpenCV类库
		@Override
		protected void onResume()
		{
		    Log.d(MainActivity.TAG, "Called onResume");
		    super.onResume();

		    Log.d(MainActivity.TAG, "Trying to load OpenCV library");
		    if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this, mOpenCVCallBack))
		    {
		        Log.e(MainActivity.TAG, "Cannot connect to OpenCV Manager");
		    }
		    
		}

	// 复制assets下面Tersseract的资源到手机的存储卡中
	private void initTersseractData() {
		Log.d(TAG, "initTersseractData");
		File tessdataPath = new File(MainActivity.appBasePath, "tessdata");
		if (tessdataPath.exists()) {
			return;
		}
		tessdataPath.mkdir();
		AssetManager assetManager = getAssets();
		
		try {
			for(String file: assetManager.list("tessdata"))
			{
				file = "tessdata/" + file;
				Log.d(TAG, "copying " + file);
				copyFromAssets(file, appBasePath.toString());
			}
		} catch (IOException e) {
			Log.d(TAG, "initTersseractData Error");
			e.printStackTrace();
		}
	}
	
	private void copyFromAssets(String srcFile, String dstDir)
	{
		File outFile = new File(dstDir, srcFile);
		try {
			
			AssetManager assetManager = getAssets();
			InputStream in = assetManager.open(srcFile);
			OutputStream out = new FileOutputStream(outFile);
					

			// 复制所有字节
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			
			Log.d(TAG, "Copied " + srcFile);
		} catch (IOException e) {
			Log.d(TAG, "Was unable to copy " + srcFile);
		}
	}
}
