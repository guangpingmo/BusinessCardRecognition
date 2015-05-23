package com.stu.mgp.BussinessCardRecognition;

import java.io.File;
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
import android.util.Log;

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

	public static File downsampleAndGray(String file) throws IOException {
		File f = new File(file);
		String name = f.getName().replaceAll("\\.", "-processed.");
		File newFile = new File(f.getParent(), name);
		Bitmap srcBitmap = BitmapFactory.decodeFile(file);
		Mat srcMat = new Mat();
		Utils.bitmapToMat(srcBitmap, srcMat);

		Mat dstMat = new Mat();
		Imgproc.pyrDown(srcMat, dstMat); // 降低分辨率, 行和高都降低一半
		Mat dstMat2 = dstMat.clone();
		Imgproc.cvtColor(dstMat, dstMat2, Imgproc.COLOR_BGR2GRAY); // 转为灰度图像

		Bitmap dstBitmap = Bitmap.createBitmap(dstMat.cols(), dstMat.rows(),
				Config.RGB_565);
		Utils.matToBitmap(dstMat2, dstBitmap);
		OutputStream stream = new FileOutputStream(newFile);
		dstBitmap.compress(CompressFormat.JPEG, 80, stream);
		stream.close();

		return newFile;

	}

	public static native void RectifyCard(long addr_inImg,
			long addr_outImg_rectified, long addr_outImg_gray,
			long addr_outImg_binarized, long addr_addTextRoi);

	public static void rectify() {
		Log.d(MainActivity.TAG, "in rectifyImage()");

		
		
		Bitmap capturedBitmap = BitmapFactory.decodeFile(MainActivity.ocrPicture.toString());

		int height = capturedBitmap.getHeight();
		int width = capturedBitmap.getWidth();
		Log.d(MainActivity.TAG, "height: " + height + ", width: " + width);

		// call the rectification function
		Mat mat = new Mat();
		Utils.bitmapToMat(capturedBitmap, mat);

		// images to be returned
		Mat matout = new Mat();
		Mat matout_gray = new Mat();
		Mat matout_binary = new Mat();
		Mat matout_textROI = new Mat();

		Log.d(MainActivity.TAG, "Before rectifyCard call");
		RectifyCard(mat.getNativeObjAddr(), matout.getNativeObjAddr(),
				matout_gray.getNativeObjAddr(),
				matout_binary.getNativeObjAddr(),
				matout_textROI.getNativeObjAddr());
		Log.d(MainActivity.TAG, "After rectifyCard call");

		height = matout_binary.rows();
		width = matout_binary.cols();

		int numROI = matout_textROI.height();
		int[][] textRoi = new int[numROI][4];
		for (int i = 0; i < numROI; i++) {
			for (int j = 0; j < 4; j++) {
				double[] data = matout_textROI.get(i, j);
				textRoi[i][j] = (int) (data[0]);
			}
			Log.d(MainActivity.TAG, "ROI " + i + ": " + textRoi[i][0] + " " + " "
					+ textRoi[i][1] + " " + textRoi[i][2] + " " + textRoi[i][3]);
		}

		Bitmap rectifiedBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(matout_binary, rectifiedBitmap);
		

		try {
			File rectifiedFile = new File(MainActivity.ocrPicture.toString().replaceAll("\\.", "-rectified."));
			FileOutputStream out = new FileOutputStream(rectifiedFile);
			rectifiedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
			out.close();
			MainActivity.ocrPicture = rectifiedFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
