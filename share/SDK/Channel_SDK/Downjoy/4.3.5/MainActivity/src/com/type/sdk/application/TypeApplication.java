/**
 *@ TypeSDKClient
 *@ 2016-10
 *@ Copyright© 2016 www.typesdk.com. All rights reserved.
 */


package com.type.sdk.application;

import android.app.Application;

import com.type.sdk.android.TypeSDKLogger;
import com.type.sdk.android.TypeSDKTool;
import com.type.sdk.notification.PushService;
import com.type.sdk.android.downjoy.TypeSDKBonjour_downjoy;
public class TypeApplication extends Application{

	public TypeApplication() {
		
	}
	
	@Override
	public void onCreate() { 
		super.onCreate();
		TypeSDKLogger.e("TypeApplication");
		String buffStr = TypeSDKTool.getFromAssets(this, "CPSettings.txt");
        TypeSDKLogger.i(buffStr);
    	if(buffStr.length()>0)
    	{
    		TypeSDKBonjour_downjoy.Instance().platform.StringToData(buffStr);
    		TypeSDKLogger.i(TypeSDKBonjour_downjoy.Instance().platform.DataToString());
    	}
		PushService.channelName = TypeSDKBonjour_downjoy.Instance().platform.GetData("channelName");
	}
	
}
