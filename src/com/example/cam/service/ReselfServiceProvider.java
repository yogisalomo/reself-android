package com.example.cam.service;

import java.io.IOException;
import java.util.HashMap;

import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;

import com.samsung.android.sdk.accessory.SAAgent;
import com.samsung.android.sdk.accessory.SAPeerAgent;
import com.samsung.android.sdk.accessory.SASocket;

public class ReselfServiceProvider extends SAAgent {
	
	public final static String TAG = "ReselfServiceProvider";
	public final static int RESELF_SERVICE_CHANNEL_ID = 123;
	
	private final IBinder mIBinder = new LocalBinder();
	
	HashMap<Integer, ReselfServiceProviderConnection> connectionMap = null;
	
	public ReselfServiceProvider() {
		super(TAG, ReselfServiceProviderConnection.class);

	}

	@Override
	protected void onFindPeerAgentResponse(SAPeerAgent arg0, int arg1) {
		// 
		
	}

	@Override
	protected void onServiceConnectionResponse(SASocket thisConnection, int result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		
	}
	
	public String getDeviceInfo(){
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		
		return manufacturer+ " "+ model;
	}
	
	@Override
	protected void onServiceConnectionRequested(SAPeerAgent arg0){

	}
	
	public class LocalBinder extends Binder{
		public ReselfServiceProvider getService(){
			return ReselfServiceProvider.this;
		}
	}
	
	public class ReselfServiceProviderConnection extends SASocket {

		private int connectionID;
		
		protected ReselfServiceProviderConnection() {
			super(ReselfServiceProviderConnection.class.getName());
		}

		@Override
		public void onError(int channelID, String errString, int errCode) {
			Log.e(TAG,"ERROR: "+ errString+ " "+ errCode);
			
		}

		@Override
		public void onReceive(int channelID, byte[] data) {
			final String message;
			
			Time time = new Time();
			
			time.set(System.currentTimeMillis());
			
			String timeStr = " "+ String.valueOf(time.minute)+ " : "+
					String.valueOf(time.second);
			
			String strToUpdateUI = new String(data);
			
			message = getDeviceInfo() + strToUpdateUI.concat(timeStr);
			
			Log.d("SAP MESSAGE", message);
			
			final ReselfServiceProviderConnection uHandler = connectionMap.get(Integer.parseInt(String.valueOf(connectionID)));
			
			if(uHandler==null){
				Log.e(TAG, "Error, cannot get Handler");
				return;
			}
			
			new Thread(new Runnable(){
				public void run(){
					try{
						uHandler.send(RESELF_SERVICE_CHANNEL_ID, message.getBytes());
					}
					catch(IOException e){
						e.printStackTrace();
					}
				}
			}).start();
			
		}

		@Override
		protected void onServiceConnectionLost(int arg0) {
			if(connectionMap != null){
				connectionMap.remove(connectionID);
			}
			
		}
		
	}
}
