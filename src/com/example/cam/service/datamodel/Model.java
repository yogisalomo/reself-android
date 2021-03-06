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

import org.json.JSONException;

public class Model {

	public static final String THUMBNAIL_LIST_RQST = "gallery-thumbnail-req";
	public static final String THUMBNAIL_LIST_RESP = "gallery-thumbnail-rsp";
	public static final String DOWNSCALE_IMG_RQST = "gallery-image-req";
	public static final String DOWNSCALE_IMG_RESP = "gallery-image-rsp";
	public static final String RESELF_CAPTURE = "reself-capture";
	public static final String RESELF_STREAMING = "reself-streaming";
	public static final String RESELF_FLASH_ON = "reself-flash-on";
	public static final String RESELF_FLASH_OFF = "reself-flash-off";
	public static final String MSG_ID = "msgId";

	 /**
     * 
     * @author amit.s5
     *
     */
	public interface JsonSerializable {
		public Object toJSON() throws JSONException;

		public void fromJSON(Object json) throws JSONException;
	}

}
