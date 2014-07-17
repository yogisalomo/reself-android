package com.example.cam;



import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
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
import android.widget.TextView;

import com.example.cam.service.backend.SASmartViewProviderImpl;

public class CamTestActivity extends Activity {
	private static final String TAG = "CamTestActivity";
	Preview preview;
	Button buttonClick;
	Button flashClick;
	Camera camera;
	String fileName;
	Activity act;
	Context ctx;
	private Intent intent;
	private String connectedPeerId;
	private String data;
	private boolean isFlashOn = false;
	private String eventName;
	boolean inProcessing = false;
	byte[] preFrame = new byte[1024*1024*8];


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = this;
		act = this;
		 intent = new Intent(this, SASmartViewProviderImpl.class);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);
		
		TextView tapgear = (TextView) findViewById(R.id.tap_text);
		tapgear.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/CODE-Bold.otf"));
		
		preview = new Preview(this, (SurfaceView)findViewById(R.id.surfaceView));
		preview.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		((FrameLayout) findViewById(R.id.camera_preview)).addView(preview);
		preview.setKeepScreenOn(true);
		
//		buttonClick = (Button) findViewById(R.id.buttonClick);
//		flashClick = (Button) findViewById(R.id.buttonFlashlight);
//		
//		buttonClick.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				//				preview.camera.takePicture(shutterCallback, rawCallback, jpegCallback);
//				camera.takePicture(shutterCallback, rawCallback, jpegCallback);
//			}
//		});
//		
//		flashClick.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				final Parameters p = camera.getParameters();
//				if(isFlashOn){
//					Log.d(TAG,"Turning Off the Flash");
//					p.setFlashMode(Parameters.FLASH_MODE_OFF);
//					camera.setParameters(p);
//					isFlashOn = false;
//				}
//				else{
//					Log.d(TAG,"Turning On the Flash");
//					p.setFlashMode(Parameters.FLASH_MODE_ON);
//					camera.setParameters(p);
//					isFlashOn = true;
//				}
//			}
//		});
//		
//		buttonClick.setOnLongClickListener(new OnLongClickListener(){
//			@Override
//			public boolean onLongClick(View arg0) {
//				camera.autoFocus(new AutoFocusCallback(){
//					@Override
//					public void onAutoFocus(boolean arg0, Camera arg1) {
//						//camera.takePicture(shutterCallback, rawCallback, jpegCallback);
//					}
//				});
//				return true;
//			}
//		});
		
	}
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            connectedPeerId = intent.getStringExtra("connectedPeerId");
            data = intent.getStringExtra("data");
            eventName = intent.getStringExtra("eventName");
            if (eventName.equals("capture")) {
                updateCapture(intent);
            } else if (eventName.equals("streaming")){
                updateStreaming(intent);
            } else if (eventName.equals("flashOn")){
                updateFlashOn(intent);
            } else if (eventName.equals("flashOff")){
                updateFlashOff(intent);
            }
            
        }
    };   
    
    private void updateCapture(Intent intent) {
        camera.takePicture(shutterCallback, rawCallback, jpegCallback);
    }
    
    private void updateFlashOn(Intent intent) {
    	final Parameters p = camera.getParameters();
    	Log.d(TAG,"Turning On the Flash");
		p.setFlashMode(Parameters.FLASH_MODE_ON);
		camera.setParameters(p);
		isFlashOn = true;
    }
    
    private void updateFlashOff(Intent intent) {
    	final Parameters p = camera.getParameters();
    	Log.d(TAG,"Turning Off the Flash");
		p.setFlashMode(Parameters.FLASH_MODE_OFF);
		camera.setParameters(p);
		isFlashOn = false;
    }
    
    private void updateStreaming(Intent intent) {
        YuvImage newImage = new YuvImage(preFrame, ImageFormat.JPEG, 320, 320, null);
        SASmartViewProviderImpl.getInstance().sendResponseImg(connectedPeerId, newImage.getYuvData());
    }

	@Override
	protected void onResume() {
		super.onResume();
		//      preview.camera = Camera.open();
		camera = Camera.open();
		setupCamera(preview_cb);
		camera.startPreview();
		preview.setCamera(camera);
		startService(intent);
		registerReceiver(broadcastReceiver, new IntentFilter(SASmartViewProviderImpl.BROADCAST_ACTION));
	}
	
	 private PreviewCallback preview_cb = new PreviewCallback() {
        public void onPreviewFrame(byte[] frame, Camera c) {
        	//Log.d("test","hello");
            if ( !inProcessing ) {
                inProcessing = true;
                
                int picWidth = 320;
                int picHeight = 320; 
                ByteBuffer bbuffer = ByteBuffer.wrap(frame); 
                bbuffer.get(preFrame, 0, picWidth*picHeight + picWidth*picHeight/2);
                //Log.d("test","hello");
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                // Send to Gear
                Camera.Parameters parameters = camera.getParameters();
                int width = parameters.getPreviewSize().width;
                int height = parameters.getPreviewSize().height;
                YuvImage image = new YuvImage(frame, parameters.getPreviewFormat(),
                		width, height, null);
                ByteArrayOutputStream oS = new ByteArrayOutputStream();
                image.compressToJpeg(
                        new Rect(0, 0, 320, 320), 90,
                        		oS);
                byte[] imgframebyte = oS.toByteArray();
                SASmartViewProviderImpl.getInstance().pullDownPrevImage(imgframebyte, 320, 320);
				//SASmartViewProviderImpl.getInstance().sendImgRsp(connectedPeerId, 1, "prev_"+ timeStamp + ".jpg", frame.length, 320, 320);
                inProcessing = false;
            }
        }
    };

	private void setupCamera(PreviewCallback cb) {
	    Camera.Parameters p = camera.getParameters();        
        //p.setPreviewSize(320, 320);
        camera.setParameters(p);
        camera.setPreviewCallback(cb);
    }

    @Override
	protected void onPause() {
        inProcessing = true;
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
//	    setupCamera(preview_cb);
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
				SASmartViewProviderImpl.getInstance().sendImgRsp(connectedPeerId, 1, "IMG_"+ timeStamp + ".jpg", data.length, 320, 320);
				
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
