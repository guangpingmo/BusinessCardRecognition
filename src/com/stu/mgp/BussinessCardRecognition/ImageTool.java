package com.stu.mgp.BussinessCardRecognition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class ImageTool {
	// 打开一个文件, 旋转一定的角度, 然后保存
	public static void rotate(String file, float degree, int quality)
			throws IOException {
		Bitmap bitmap = BitmapFactory.decodeFile(file);
		Matrix aMatrix = new Matrix();
		aMatrix.setRotate(degree);

		Bitmap dstBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), aMatrix, true);

		FileOutputStream outputStream = new FileOutputStream(file);
		dstBitmap.compress(CompressFormat.JPEG, quality, outputStream);
		outputStream.close();

	}
	
	public static File downsampleAndGray(String file) throws IOException
	{
		File f = new File(file);
		String name = f.getName().replaceAll("\\.", "-processed.");
		File newFile = new File(f.getParent(), name);
		Bitmap srcBitmap = BitmapFactory.decodeFile(file);
		Mat srcMat = new Mat();
		Utils.bitmapToMat(srcBitmap, srcMat); 
		
		Mat dstMat = new Mat();
		Imgproc.pyrDown(srcMat, dstMat);	//降低分辨率, 行和高都降低一半
		Mat dstMat2 = dstMat.clone();
		Imgproc.cvtColor(dstMat, dstMat2, Imgproc.COLOR_BGR2GRAY); //转为灰度图像
		
		Bitmap dstBitmap = Bitmap.createBitmap(dstMat.cols(), dstMat.rows(), Config.RGB_565);		
		Utils.matToBitmap(dstMat2, dstBitmap);
		OutputStream stream = new FileOutputStream(newFile);
		dstBitmap.compress(CompressFormat.JPEG, 80, stream);
		stream.close();
		
		return newFile;
		
	}
}
