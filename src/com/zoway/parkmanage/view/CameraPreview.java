package com.zoway.parkmanage.view;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements Callback {

	private SurfaceHolder mHolder;
	private Camera mCamera;
	private int maxzoom;

	public CameraPreview(Context context, Camera camera) {
		super(context);
		mCamera = camera;

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		// mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw the
		// preview.
		try {
			Parameters mparam = mCamera.getParameters();
			boolean b = mparam.isSmoothZoomSupported();
			boolean c = mparam.isZoomSupported();
			mparam.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
			List l = mparam.getSupportedPictureSizes();
			// Size s = mparam.getPictureSize();
			maxzoom = mparam.getMaxZoom();
			mparam.setPictureFormat(PixelFormat.JPEG);
			// mparam.setRotation(90);
			// mparam.setPictureSize(1920, 1080);
			mparam.setPictureSize(1600, 1200);

			mCamera.setParameters(mparam);
			mCamera.setDisplayOrientation(90);
			mCamera.setPreviewDisplay(holder);

			mCamera.startPreview();
			Log.d("...", "create preview success");
		} catch (IOException e) {
			Log.d("", "Error setting camera preview: " + e.getMessage());
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// empty. Take care of releasing the Camera preview in your activity.
		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}
		// stop preview before making changes
		try {
			// mCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}

	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.

		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}

		// set preview size and make any resize, rotate or
		// reformatting changes here

		// start preview with new settings
		try {

			// Parameters mparam = mCamera.getParameters();
			// boolean b = mparam.isSmoothZoomSupported();
			// boolean c = mparam.isZoomSupported();
			// mparam.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
			// List l = mparam.getSupportedPictureSizes();
			// // Size s = mparam.getPictureSize();
			// maxzoom = mparam.getMaxZoom();
			// mparam.setPictureFormat(PixelFormat.JPEG);
			// // mparam.setRotation(90);
			// // mparam.setPictureSize(1920, 1080);
			// mparam.setPictureSize(1600, 1200);
			// mCamera.setParameters(mparam);
			// mCamera.setDisplayOrientation(90);
			mCamera.setPreviewDisplay(mHolder);

			mCamera.startPreview();

		} catch (Exception e) {
			Log.d("", "Error starting camera preview: " + e.getMessage());
		}
	}

	public void setZoomView(int zoomVal) {
		Parameters mparam = mCamera.getParameters();
		int zm = mparam.getZoom() + zoomVal;
		if (zm < maxzoom && zm > 0) {
			mparam.setZoom(zm);
		} else {

		}
		if (mHolder.getSurface() == null) {
			return;
		}
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
		}
		try {
			mCamera.setParameters(mparam);
			mCamera.setDisplayOrientation(90);
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
		} catch (Exception e) {
			Log.d("", "Error starting camera preview: " + e.getMessage());
		}
	}

	// 开启设备的闪关灯；
	public void openFlahsLight() {
		if (mCamera != null) {
			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
			mCamera.setParameters(parameters);
			mCamera.startPreview();
		}

	}

	public void closeFlashLigth() {
		if (mCamera != null) {
			mCamera.stopPreview();
			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			mCamera.setParameters(parameters);
			mCamera.startPreview();

		}
	}

}
