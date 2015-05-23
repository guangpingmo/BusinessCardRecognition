package com.stu.mgp.BussinessCardRecognition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
			long addr_outImg_binarized, long addr_addTextRoi,
			long addr_outImg_pyrDown, long addr_outImg_pyrDown_gray_cvtColor,
			long addr_outImg_detectedEdges_Canny,
			long addr_outImg_detectedEdges_dilate);

	public static void rectify() {
		Log.d(MainActivity.TAG, "in rectifyImage()");

		Bitmap capturedBitmap = BitmapFactory
				.decodeFile(MainActivity.ocrPicture.toString());

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

		Mat mat_outImg_pyrDown = new Mat();
		Mat mat_outImg_pyrDown_gray_cvtColor = new Mat();
		Mat mat_outImg_detectedEdges_Canny = new Mat();
		Mat mat_outImg_detectedEdges_dilate = new Mat();

		Log.d(MainActivity.TAG, "Before rectifyCard call");
		RectifyCard(mat.getNativeObjAddr(), matout.getNativeObjAddr(),
				matout_gray.getNativeObjAddr(),
				matout_binary.getNativeObjAddr(),
				matout_textROI.getNativeObjAddr(),
				mat_outImg_pyrDown.getNativeObjAddr(),
				mat_outImg_pyrDown_gray_cvtColor.getNativeObjAddr(),
				mat_outImg_detectedEdges_Canny.getNativeObjAddr(),
				mat_outImg_detectedEdges_dilate.getNativeObjAddr());
		Log.d(MainActivity.TAG, "After rectifyCard call");

		// 保存文件
		saveMatToFile(matout, "rectified");
		saveMatToFile(matout_gray, "gray");
		saveMatToFile(matout_binary, "binary");
		saveMatToFile(mat_outImg_pyrDown, "1_pyrDown");
		saveMatToFile(mat_outImg_pyrDown_gray_cvtColor, "2_pyrDown_gray");
		saveMatToFile(mat_outImg_detectedEdges_Canny, "3_detectedEdges_Canny");
		saveMatToFile(mat_outImg_detectedEdges_dilate, "4_detectedEdges_dilate");

		height = matout_binary.rows();
		width = matout_binary.cols();

		int numROI = matout_textROI.height();
		int[][] textRoi = new int[numROI][4];
		for (int i = 0; i < numROI; i++) {
			for (int j = 0; j < 4; j++) {
				double[] data = matout_textROI.get(i, j);
				textRoi[i][j] = (int) (data[0]);
			}
			Log.d(MainActivity.TAG, "ROI " + i + ": " + textRoi[i][0] + " "
					+ " " + textRoi[i][1] + " " + textRoi[i][2] + " "
					+ textRoi[i][3]);
		}

		Bitmap rectifiedBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(matout_binary, rectifiedBitmap);

		try {
			File rectifiedFile = new File(MainActivity.ocrPicture.toString()
					.replaceAll("\\.", "-rectified."));
			FileOutputStream out = new FileOutputStream(rectifiedFile);
			rectifiedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
			out.close();
			MainActivity.ocrPicture = rectifiedFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 把Mat保存为图像文件
	private static void saveMatToFile(Mat mat, String filename) {
		int height = mat.rows();
		int width = mat.cols();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(mat, bitmap);
		try {
			File file = new File(MainActivity.appImagePreprocessPath,
					MainActivity.dateOfRecognition + "-" + filename + ".jpg");
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void copyFile(File srcFile, File dstFile)
	{
		try{
			InputStream in = new FileInputStream(srcFile);
			OutputStream out = new FileOutputStream(dstFile);
					

			// 复制所有字节
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			
			Log.d(MainActivity.TAG, "Copied " + srcFile);
		} catch (IOException e) {
			Log.d(MainActivity.TAG, "unable to copy " + srcFile + " to " + dstFile);
		}
		
	}

}
