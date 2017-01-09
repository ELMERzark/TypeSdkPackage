package com.type.sdk.android.xiaomi;

//import java.util.EventListener;

import com.type.sdk.android.TypeSDKData;
import com.type.sdk.android.TypeSDKDefine;
import com.type.sdk.android.TypeSDKEvent;
import com.type.sdk.android.TypeSDKEventManager;
import com.type.sdk.android.TypeSDKLogger;
import com.type.sdk.android.TypeSDKDefine.AttName;
import com.type.sdk.android.TypeSDKDefine.ReceiveFunction;

public class TypeSDKNotify_xiaomi
{
		public void sendToken(String _id_string, String _session_string)
		{
			// TODO Auto-generated method stub
			String uid = _id_string;
			String session = _session_string;
			TypeSDKLogger.e("login success intent extra:uid:" + uid + ";session:" + session);
		
			TypeSDKData.UserInfoData userData= TypeSDKBonjour_xiaomi.Instance().userInfo;
			userData.SetData(TypeSDKDefine.AttName.USER_ID, uid);
			userData.SetData(TypeSDKDefine.AttName.USER_TOKEN, session);
			userData.CopyAtt(TypeSDKBonjour_xiaomi.Instance().platform, AttName.CP_ID);
			userData.CopyAtt(TypeSDKBonjour_xiaomi.Instance().platform, AttName.SDK_NAME);
			userData.CopyAtt(TypeSDKBonjour_xiaomi.Instance().platform, AttName.PLATFORM);
			
			TypeSDKLogger.e("userData:" + userData.DataToString());
			
			TypeSDKEventManager.Instance().SendEvent(TypeSDKEvent.EventType.AND_EVENT_LOGIN, ReceiveFunction.MSG_LOGIN, 
					userData.DataToString(), TypeSDKBonjour_xiaomi.Instance().platform.GetData(AttName.PLATFORM));
			
		}
		
		public void Init()
		{
			
			TypeSDKEventManager.Instance().SendEvent(TypeSDKEvent.EventType.AND_EVENT_INIT_FINISH, ReceiveFunction.MSG_INITFINISH, 
					TypeSDKBonjour_xiaomi.Instance().platform.DataToString(), TypeSDKBonjour_xiaomi.Instance().platform.GetData(AttName.PLATFORM));
			
			TypeSDKEventManager.Instance().SendEvent(TypeSDKEvent.EventType.AND_EVENT_UPDATE_FINISH, ReceiveFunction.MSG_UPDATEFINISH, 
					TypeSDKBonjour_xiaomi.Instance().platform.DataToString(), TypeSDKBonjour_xiaomi.Instance().platform.GetData(AttName.PLATFORM));
			
		}
		
		public void Pay(String string)
		{
			TypeSDKLogger.i("pay");
			
			TypeSDKEventManager.Instance().SendEvent(TypeSDKEvent.EventType.AND_EVENT_PAY_RESULT, ReceiveFunction.MSG_PAYRESULT, 
					string, TypeSDKBonjour_xiaomi.Instance().platform.GetData(AttName.PLATFORM));
			
		}
		
		public void Logout()
		{
			TypeSDKLogger.i("user sdk logout");
			TypeSDKEventManager.Instance().SendEvent(TypeSDKEvent.EventType.AND_EVENT_LOGOUT, ReceiveFunction.MSG_LOGOUT, 
					TypeSDKBonjour_xiaomi.Instance().userInfo.DataToString(), TypeSDKBonjour_xiaomi.Instance().platform.GetData(AttName.PLATFORM));
			
		}

		public void reLogin()
		{
			TypeSDKLogger.i("user sdk reLogin");
			
			TypeSDKEventManager.Instance().SendEvent(TypeSDKEvent.EventType.AND_EVENT_RELOGIN, ReceiveFunction.MSG_RELGOIN, 
					TypeSDKBonjour_xiaomi.Instance().userInfo.DataToString(), TypeSDKBonjour_xiaomi.Instance().platform.GetData(AttName.PLATFORM));
			
			TypeSDKLogger.i("user sdk reLogin2");
		}


}
