package com.type.sdk.android.haima;

import android.util.Log;

import com.type.sdk.android.TypeSDKData;
import com.type.sdk.android.TypeSDKDefine;
import com.type.sdk.android.TypeSDKEvent;
import com.type.sdk.android.TypeSDKEventManager;
import com.type.sdk.android.TypeSDKLogger;
import com.type.sdk.android.TypeSDKDefine.AttName;
import com.type.sdk.android.TypeSDKDefine.ReceiveFunction;
import com.type.sdk.android.TypeSDKEvent.EventType;
import com.unity3d.player.UnityPlayer;

public class TypeSDKNotify_haima
{
		public void sendToken(String _token_string, String _uid_string)
		{
			// TODO Auto-generated method stub
			String userToken = _token_string;
			String uid = _uid_string;
			android.util.Log.i("login info", "login success intent extra:" + userToken);
		
			TypeSDKData.UserInfoData userData = TypeSDKBonjour_haima.Instance().userInfo;
			userData.SetData(TypeSDKDefine.AttName.USER_TOKEN, userToken);
			userData.SetData(TypeSDKDefine.AttName.USER_ID, uid);
			userData.CopyAtt(TypeSDKBonjour_haima.Instance().platform, AttName.CP_ID);
			userData.CopyAtt(TypeSDKBonjour_haima.Instance().platform, AttName.SDK_NAME);
			userData.CopyAtt(TypeSDKBonjour_haima.Instance().platform, AttName.PLATFORM);
			
			TypeSDKEventManager.Instance().SendEvent(TypeSDKEvent.EventType.AND_EVENT_LOGIN, 
				ReceiveFunction.MSG_LOGIN, 
				userData.DataToString(), 
				TypeSDKBonjour_haima.Instance().platform.GetData(AttName.PLATFORM));
		}
		
		
		public void Logout()
		{
			TypeSDKLogger.i("user sdk logout");
			TypeSDKEventManager.Instance().SendEvent(TypeSDKEvent.EventType.AND_EVENT_LOGOUT, ReceiveFunction.MSG_LOGOUT, 
				TypeSDKBonjour_haima.Instance().userInfo.DataToString(), TypeSDKBonjour_haima.Instance().platform.GetData(AttName.PLATFORM));
		}

		public void Init()
		{
			TypeSDKEventManager.Instance().SendEvent(TypeSDKEvent.EventType.AND_EVENT_INIT_FINISH, 
					ReceiveFunction.MSG_INITFINISH, 
					TypeSDKBonjour_haima.Instance().platform.DataToString(), 
					TypeSDKBonjour_haima.Instance().platform.GetData(AttName.PLATFORM));
			TypeSDKEventManager.Instance().SendEvent(TypeSDKEvent.EventType.AND_EVENT_UPDATE_FINISH, 
					ReceiveFunction.MSG_UPDATEFINISH, 
					TypeSDKBonjour_haima.Instance().platform.DataToString(), 
					TypeSDKBonjour_haima.Instance().platform.GetData(AttName.PLATFORM));		
		}
		
		public void Pay(String string)
		{
			TypeSDKLogger.i("pay");
			TypeSDKEventManager.Instance().SendEvent(EventType.AND_EVENT_PAY_RESULT,
					ReceiveFunction.MSG_PAYRESULT, 
					string, 
					TypeSDKBonjour_haima.Instance().platform.GetData(AttName.PLATFORM));			
		}
}
