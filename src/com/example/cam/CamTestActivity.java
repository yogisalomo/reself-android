package com.example.cam;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.cam.service.backend.SASmartViewProviderImpl;

public class CamTestActivity extends Activity {
	private static final String TAG = "CamTestActivity";
	Preview preview;
	Button buttonClick;
	Camera camera;
	String fileName;
	Activity act;
	Context ctx;
	private Intent intent;
	private String connectedPeerId;
	private String data;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = this;
		act = this;
		 intent = new Intent(this, SASmartViewProviderImpl.class);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);
		
		preview = new Preview(this, (SurfaceView)findViewById(R.id.surfaceView));
		preview.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		((FrameLayout) findViewById(R.id.camera_preview)).addView(preview);
		preview.setKeepScreenOn(true);
		
		buttonClick = (Button) findViewById(R.id.buttonClick);
		
		buttonClick.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//				preview.camera.takePicture(shutterCallback, rawCallback, jpegCallback);
				camera.takePicture(shutterCallback, rawCallback, jpegCallback);
			}
		});
		
		buttonClick.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View arg0) {
				camera.autoFocus(new AutoFocusCallback(){
					@Override
					public void onAutoFocus(boolean arg0, Camera arg1) {
						//camera.takePicture(shutterCallback, rawCallback, jpegCallback);
					}
				});
				return true;
			}
		});
	}
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            connectedPeerId = intent.getStringExtra("connectedPeerId");
            data = intent.getStringExtra("data");
        	updateCapture(intent);
            
        }
    };   
    
    private void updateCapture(Intent intent) {
        camera.takePicture(shutterCallback, rawCallback, jpegCallback);
    }

	@Override
	protected void onResume() {
		super.onResume();
		//      preview.camera = Camera.open();
		camera = Camera.open();
		camera.startPreview();
		preview.setCamera(camera);
		startService(intent);
		registerReceiver(broadcastReceiver, new IntentFilter(SASmartViewProviderImpl.BROADCAST_ACTION));
	}

	@Override
	protected void onPause() {
		if(camera != null) {
			camera.stopPreview();
			preview.setCamera(null);
			camera.release();
			camera = null;
		}
		super.onPause();
		unregisterReceiver(broadcastReceiver);
        stopService(intent);        
	}

	private void resetCam() {
		camera.startPreview();
		preview.setCamera(camera);
	}

	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			// Log.d(TAG, "onShutter'd");
		}
	};

	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			// Log.d(TAG, "onPictureTaken - raw");
			System.out.println("Masuk raw");
		}
	};

	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d(TAG,"Masuk jpeg");
			FileOutputStream outStream = null;
			try {
				// Write to SD Card
				File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "reself");
		    	if(!mediaStorageDir.exists()){
		    		if(!mediaStorageDir.mkdirs()){
		    			return;
		    		}
		    	}
		    	String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
				fileName = mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg";
				Log.d(TAG,fileName);
				outStream = new FileOutputStream(fileName);
				outStream.write(data);
				outStream.close();
				Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
				MediaStore.Images.Media.insertImage(getContentResolver(), fileName, "IMG_"+ timeStamp + ".jpg", "");
				//Sending to Gear
				SASmartViewProviderImpl.getInstance().pullDownscaledImg(fileName, 320, 320);
				SASmartViewProviderImpl.getInstance().sendImgRsp(connectedPeerId, 123, "IMG_"+ timeStamp + ".jpg", data.length, 320, 320);
				
				resetCam();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
			Log.d(TAG, "onPictureTaken - jpeg");
		}
	};
}
