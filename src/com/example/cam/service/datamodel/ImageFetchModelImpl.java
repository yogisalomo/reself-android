/*    
 * Copyright (c) 2014 Samsung Electronics Co., Ltd.   
 * All rights reserved.   
 *   
 * Redistribution and use in source and binary forms, with or without   
 * modification, are permitted provided that the following conditions are   
 * met:   
 *   
 *     * Redistributions of source code must retain the above copyright   
 *        notice, this list of conditions and the following disclaimer.  
 *     * Redistributions in binary form must reproduce the above  
 *       copyright notice, this list of conditions and the following disclaimer  
 *       in the documentation and/or other materials provided with the  
 *       distribution.  
 *     * Neither the name of Samsung Electronics Co., Ltd. nor the names of its  
 *       contributors may be used to endorse or promote products derived from  
 *       this software without specific prior written permission.  
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS  
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT  
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR  
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT  
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,  
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY  
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE  
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package com.example.cam.service.datamodel;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageFetchModelImpl {
	public static final class TBListReqMsg implements Model.JsonSerializable {

		String mMessgaeId = "";
		Long mId = -1L;
		public static final String ID = "offset";

		// public static final String COUNT = "count";

	    /**
	     * 
	     */
		public TBListReqMsg() {

		}

	    /**
	     * 
	     * @param id
	     */
		public TBListReqMsg(Long id) {
			mMessgaeId = Model.THUMBNAIL_LIST_RQST;
			mId = id;

		}
	    /**
	     * 
	     * @return Object
	     */
		public Object toJSON() throws JSONException {
			JSONObject json = new JSONObject();
			json.put(Model.MSG_ID, mMessgaeId);
			json.put(ID, mId);
			return json;
		}

	    /**
	     * 
	     * @param Object
	     */
		public void fromJSON(Object obj) throws JSONException {
			JSONObject json = (JSONObject) obj;
			mMessgaeId = json.getString(Model.MSG_ID);
			mId = json.getLong(ID);

		}

	    /**
	     * 
	     * @return String
	     */
		public String getMessageIdentifier() {
			return mMessgaeId;
		}

	    /**
	     * 
	     * @return long
	     */
		public long getID() {
			return mId;
		}

	}

	public static final class TBListRespMsg implements Model.JsonSerializable {
		String mMessgaeId = "";
		String mResult = "";
		int mReason = 0;
		int mCount = 0;
		public List<TBModelJson> msgTBList = null;

		public static final String COUNT = "count";
		public static final String LIST = "list";
		public static final String REASON = "reason";
		public static final String RESULT = "result";

	    /**
	     * 
	     */
		public TBListRespMsg() {
		}

	    /**
	     * 
	     * @param result
	     * @param reason
	     * @param count
	     * @param TBlist
	     */
		public TBListRespMsg(String result, int reason, int count,
				List<TBModelJson> TBList) {
			mMessgaeId = Model.THUMBNAIL_LIST_RESP;
			mResult = result;
			mReason = reason;
			mCount = count;
			msgTBList = new ArrayList<TBModelJson>();
			msgTBList.addAll(TBList);
		}

	    /**
	     * 
	     * @return Object
	     */
		@Override
		public Object toJSON() throws JSONException {
			JSONObject json = new JSONObject();
			json.put(Model.MSG_ID, mMessgaeId);
			json.put(RESULT, mResult);
			json.put(REASON, mReason);
			json.put(COUNT, mCount);

			JSONArray msgarray = new JSONArray();

			for (TBModelJson sms : msgTBList) {
				Object obj = sms.toJSON();
				msgarray.put(obj);
			}

			json.put(LIST, msgarray);

			return json;
		}

	    /**
	     * 
	     * @param Object
	     */
		@Override
		public void fromJSON(Object obj) throws JSONException {
			JSONObject json = (JSONObject) obj;
			mMessgaeId = json.getString(Model.MSG_ID);
			mResult = json.getString(RESULT);
			mReason = json.getInt(REASON);
			mCount = json.getInt(COUNT);

			JSONArray jsonArray = json.getJSONArray(LIST);
			msgTBList = new ArrayList<TBModelJson>();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObjct = (JSONObject) jsonArray.getJSONObject(i);
				TBModelJson sms = new TBModelJson();
				sms.fromJSON(jsonObjct);
				msgTBList.add(sms);
			}
		}

	    /**
	     * 
	     * @return String
	     */
		public String getMessageIdentifier() {
			return mMessgaeId;
		}

	    /**
	     * 
	     * @return int
	     */
		public int getMsgCount() {
			return mCount;
		}

	    /**
	     * 
	     * @return String
	     */
		public String getResult() {
			return mResult;
		}

	    /**
	     * 
	     * @return int
	     */
		public int getReason() {
			return mReason;
		}

	    /**
	     * 
	     * @return List<TBModelJson>
	     */
		public List<TBModelJson> getmsgTBList() {
			return msgTBList;
		}

	}

	public static final class ImgReqMsg implements Model.JsonSerializable {

		String mMessgaeId = "";
		public static final String ID = "id";
		public static final String WIDTH = "width";
		public static final String HEIGHT = "height";

		Long mId = -1L;
		int mWidth = 0;
		int mHeight = 0;

	    /**
	     * 
	     */
		public ImgReqMsg() {

		}

	    /**
	     * 
	     * @param id
	     * @param width
	     * @param height
	     */
		public ImgReqMsg(Long id, int width, int height) {
			mMessgaeId = Model.DOWNSCALE_IMG_RQST;
			mId = id;
			mWidth = width;
			mHeight = height;
		}

	    /**
	     * 
	     * @return String
	     */
		public String getMessageIdentifier() {
			return mMessgaeId;
		}

	    /**
	     * 
	     * @return long
	     */
		public long getID() {
			return mId;
		}

	    /**
	     * 
	     * @return int
	     */
		public int getWidth() {
			return mWidth;
		}

	    /**
	     * 
	     * @return int
	     */
		public int getHeight() {
			return mHeight;
		}

	    /**
	     * 
	     * @return Object
	     */
		@Override
		public Object toJSON() throws JSONException {
			JSONObject json = new JSONObject();
			json.put(Model.MSG_ID, mMessgaeId);
			json.put(ID, mId);
			json.put(WIDTH, mWidth);
			json.put(HEIGHT, mHeight);

			return json;
		}

	    /**
	     * 
	     * @param obj
	     */
		@Override
		public void fromJSON(Object obj) throws JSONException {
			JSONObject json = (JSONObject) obj;
			mMessgaeId = json.getString(Model.MSG_ID);
			mId = json.getLong(ID);
			mWidth = json.getInt(WIDTH);
			mHeight = json.getInt(HEIGHT);

		}

	}

	public static final class ImgRespMsg implements Model.JsonSerializable {

		String mMessgaeId = "";
		String mResult = "";
		int mReason = 0;
		TBModelJson mDownscaledImg = null;

		public static final String RESULT = "result";
		public static final String REASON = "reason";
		public static final String IMAGE = "image";

	    /**
	     * 
	     */
		public ImgRespMsg() {

		}

	    /**
	     * 
	     * @param result
	     * @param reason
	     * @param img
	     */
		public ImgRespMsg(String result, int reason, TBModelJson img) {
			mMessgaeId = Model.DOWNSCALE_IMG_RESP;
			mResult = result;
			mReason = reason;
			mDownscaledImg = img;

		}

	    /**
	     * 
	     * @return Object
	     */
		@Override
		public Object toJSON() throws JSONException {
			JSONObject json = new JSONObject();
			json.put(Model.MSG_ID, mMessgaeId);
			json.put(RESULT, mResult);
			json.put(REASON, mReason);

			//JSONObject obj = new JSONObject();
			//obj = (JSONObject) mDownscaledImg.toJSON();
			json.put(IMAGE, (JSONObject) mDownscaledImg.toJSON());

			return json;
		}

	    /**
	     * 
	     * @param Object
	     */
		@Override
		public void fromJSON(Object obj) throws JSONException {
			JSONObject json = (JSONObject) obj;
			mMessgaeId = json.getString(Model.MSG_ID);
			mResult = json.getString(RESULT);
			mReason = json.getInt(REASON);

			JSONObject jobj = json.getJSONObject(IMAGE);
			mDownscaledImg = new TBModelJson();
			mDownscaledImg.fromJSON(jobj);

		}

	    /**
	     * 
	     * @return String
	     */
		public String getMessageIdentifier() {

			return mMessgaeId;
		}

	    /**
	     * 
	     * @return String
	     */
		public String getResult() {
			return mResult;

		}

	    /**
	     * 
	     * @return int
	     */
		public int getReason() {
			return mReason;
		}
		
	    /**
	     * 
	     * @return TBModelJson
	     */
		public TBModelJson getDownscaledImg() {
			return mDownscaledImg;
		}

	}

	public static final class TBModelJson implements Model.JsonSerializable {

	    /**
	     * 
	     * @return String
	     */
		public String getData() {
			return mData;
		}

	    /**
	     * 
	     * @return String
	     */
		public String getName() {
			return mName;
		}

	    /**
	     * 
	     * @return int
	     */
		public int getWidth() {
			return mWidth;
		}

	    /**
	     * 
	     * @return int
	     */
		public int getHeight() {
			return mHeight;
		}

	    /**
	     * 
	     * @return long
	     */
		public long getSize() {
			return mSize;
		}

	    /**
	     * 
	     * @return long
	     */
		public long getId() {
			return mId;
		}

		public static final String ID = "id";
		public static final String DATA = "image";
		public static final String SIZE = "size";
		public static final String NAME = "name";
		public static final String WIDTH = "width";
		public static final String HEIGHT = "height";

		long mId = -1L;
		String mData = "";
		long mSize = 0L;
		String mName = "";
		int mWidth = 0;
		int mHeight = 0;

	    /**
	     * 
	     */
		public TBModelJson() {
		};

	    /**
	     * 
	     * @param id
	     * @param name
	     * @param data
	     * @param size
	     * @param width
	     * @param height
	     */
		public TBModelJson(long id, String name, String data, long size,
				int width, int height) {
			super();
			mId = id;
			mName = name;
			mData = data;
			mWidth = width;
			mHeight = height;
			mSize = size;

		}

	    /**
	     * 
	     * @param jsonObj
	     */
		@Override
		public void fromJSON(Object jsonObj) throws JSONException {
			JSONObject json = (JSONObject) jsonObj;
			mId = json.getLong(ID);
			mData = json.getString(DATA);
			mName = json.getString(NAME);
			mSize = json.getLong(SIZE);
			mHeight = json.getInt(HEIGHT);
			mWidth = json.getInt(WIDTH);

		}

	    /**
	     * 
	     * @return Object
	     */
		@Override
		public Object toJSON() throws JSONException {
			JSONObject json = new JSONObject();
			json.put(ID, mId);
			json.put(NAME, mName);
			json.put(DATA, mData);
			json.put(SIZE, mSize);
			json.put(WIDTH, mWidth);
			json.put(HEIGHT, mHeight);

			return json;
		}

	}

}
