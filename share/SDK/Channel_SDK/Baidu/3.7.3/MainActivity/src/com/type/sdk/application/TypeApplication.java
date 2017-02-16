/**
 *@ TypeSDKClient
 *@ 2016-10
 *@ Copyright© 2016 www.typesdk.com. All rights reserved. 
 */
package com.type.sdk.application;

import com.baidu.gamesdk.BDGameApplication;
import com.type.sdk.android.TypeSDKLogger;
import com.type.sdk.android.TypeSDKTool;
import com.type.sdk.android.baidu.TypeSDKBonjour_baidu;
import com.type.sdk.notification.PushService;

public class TypeApplication extends BDGameApplication{

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
    		TypeSDKBonjour_baidu.Instance().platform.StringToData(buffStr);
    		TypeSDKLogger.i(TypeSDKBonjour_baidu.Instance().platform.DataToString());
    	}
        PushService.channelName = TypeSDKBonjour_baidu.Instance().platform.GetData("channelName");
	}
	
}
