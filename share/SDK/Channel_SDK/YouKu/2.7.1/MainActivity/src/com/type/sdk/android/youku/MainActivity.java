package com.type.sdk.android.youku;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.type.sdk.android.TypeSDKDefine;
import com.type.sdk.android.TypeSDKDefine.AttName;
import com.type.sdk.android.TypeSDKLogger;
//import com.type.sdk.android.TypeSDKUpdateManager;

import android.os.Bundle;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.type.sdk.android.BaseMainActivity;
import com.youku.gamesdk.act.YKPlatform;

import com.type.sdk.android.TypeSDKData;
import com.type.sdk.android.TypeSDKTool;
import com.type.utils.*;
//import com.type.sdk.notification.HttpUtil;
import android.os.Handler;
import android.os.Looper;
public class MainActivity extends BaseMainActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);		
    	String result="";
    	result +="~"+ TypeSDKBonjour_youku.Instance().isHasRequest(TypeSDKDefine.AttName.REQUEST_INIT_WITH_SEVER);
    	result +="~"+ TypeSDKBonjour_youku.Instance().isHasRequest(TypeSDKDefine.AttName.SUPPORT_PERSON_CENTER);
    	result +="~"+ TypeSDKBonjour_youku.Instance().isHasRequest(TypeSDKDefine.AttName.SUPPORT_SHARE);
    	TypeSDKLogger.i("result "+result);
        TypeSDKLogger.i("android on create finish");
//        TypeSDKUpdateManager update = new TypeSDKUpdateManager(this, 
//        		TypeSDKBonjour_youku.Instance().platform.GetData(AttName.CHANNEL_ID), 
//				TypeSDKBonjour_youku.Instance().platform.GetData("check_update_url"));
//        update.checkUpdateInfo();
        ApplicationInfo appInfo;
		try {
			appInfo = MainActivity.this.getPackageManager().getApplicationInfo(MainActivity.this.getPackageName(), PackageManager.GET_META_DATA);
			int appId = appInfo.metaData.getInt("YKGAME_APPID");
			String appKey = appInfo.metaData.getString("YKGAME_APPKEY");
			String appName = appInfo.metaData.getString("YKGAME_APPNAME");
			String appPrivate = appInfo.metaData.getString("YKGAME_PRIVATEKEY");
			TypeSDKLogger.e("YKGAME_APPID:" + appId);
			TypeSDKLogger.e("YKGAME_APPKEY:" + appKey);
			TypeSDKLogger.e("YKGAME_APPNAME:" + appName);
			TypeSDKLogger.e("YKGAME_PRIVATEKEY:" + appPrivate);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
  
    
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		TypeSDKLogger.e("onPause");
		TypeSDKBonjour_youku.Instance().onPause();
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		TypeSDKLogger.e("onStop");
		TypeSDKBonjour_youku.Instance().onStop();
	}


	@Override
    protected void onDestroy(){
		super.onDestroy();
    	TypeSDKLogger.e("onDestroy");
    	TypeSDKBonjour_youku.Instance().onDestroy(); 	
    }

    @Override
    protected void onResume(){
    	super.onResume();
    	TypeSDKLogger.e("onResume");
    	YKPlatform.exceptionYKFinish(MainActivity.this);
    	TypeSDKBonjour_youku.Instance().onResume();
    }
    
    
    /**
     *  ���ⲿ call �� init����
     * @param _in_context
     * @param _in_data
     */
    public  void CallInitSDK(){
    	String _in_data = "";
    	TypeSDKBonjour_youku.Instance().initSDK(_in_context, _in_data);
    }
    /**
     * ���ⲿ call�� login����
     * @param _in_context
     * @param _in_data
     */
    public void CallLogin(String _in_data)
    {
    	TypeSDKLogger.i("call login:" + _in_data);
    	TypeSDKBonjour_youku.Instance().ShowLogin(_in_context,_in_data);
    }
    /**
     * ���ⲿ call ��logout����
     * @param _in_context
     */
    public  void CallLogout()
    {
    	TypeSDKBonjour_youku.Instance().ShowLogout(_in_context);
    }
    public  String CallPayItem(final String _in_data)
    {
    	TypeSDKLogger.i("CallPayItem:" + _in_data);
    	new Thread() {
			@Override
			public void run() {
				String payMessage;
				try {
					payMessage = HttpUtil.http_get(TypeSDKBonjour_youku
							.Instance().platform
							.GetData(AttName.SWITCHCONFIG_URL));
					if (((payMessage.equals("") || payMessage.isEmpty()) && openPay)
							|| TypeSDKTool.openPay(TypeSDKBonjour_youku
									.Instance().platform
									.GetData(AttName.SDK_NAME), payMessage)) {
						TypeSDKBonjour_youku.Instance().PayItem(_in_context,
								_in_data);
					} else {
						TypeSDKNotify_youku notify = new TypeSDKNotify_youku();
						TypeSDKData.PayInfoData payResult = new TypeSDKData.PayInfoData();
						payResult.SetData(AttName.PAY_RESULT, "0");
						notify.Pay(payResult.DataToString());
						Handler dialogHandler = new Handler(Looper.getMainLooper());
						dialogHandler.post(new Runnable(){
							@Override
							public void run() {
								// TODO Auto-generated method stub
								TypeSDKTool.showDialog("暂未开放充值！！！", _in_context);
							}});							
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}.start();
		return "client pay function finished";
    }
    /***
     * ���ⲿcall �� �Ƕ���֧������һ��ƶ�����Ʒ��
     * @param _in_context
     * @param _in_data
     * @return
     */
    public  String CallExchangeItem(String _in_data)
    {
    	return TypeSDKBonjour_youku.Instance().ExchangeItem(_in_context, _in_data);
    }
    /***
     * ���ⲿ���õ� ��ʵ����������
     * @param _in_context
     */
    public  void CallToolBar()
    {
    	TypeSDKBonjour_youku.Instance().ShowToolBar(_in_context);
    }
    public void CallHideToolBar()
    {
    	TypeSDKBonjour_youku.Instance().HideToolBar(_in_context);
    }
    /***
     * ���ⲿ���õ���ʵ�û����ĺ���
     * @param _in_context
     */
    public  void CallPersonCenter()
    {
    	TypeSDKBonjour_youku.Instance().ShowPersonCenter(_in_context);
    }
    public void CallHidePersonCenter()
    {
    	TypeSDKBonjour_youku.Instance().HidePersonCenter(_in_context);
    }
    public void CallShare(String _in_data)
    {
    	TypeSDKBonjour_youku.Instance().ShowShare(_in_context, _in_data);
    }
    public void CallSetPlayerInfo(String _in_data)
    {
    	TypeSDKBonjour_youku.Instance().SetPlayerInfo(_in_context, _in_data);
    }
    public void CallExitGame()
    {
    	TypeSDKBonjour_youku.Instance().ExitGame(_in_context);
    }
    public void CallDestory()
    {
    	TypeSDKBonjour_youku.Instance().onDestroy();
    }
    public int CallLoginState()
    {
    	return TypeSDKBonjour_youku.Instance().LoginState(_in_context);
    }
    public String CallUserData()
    {
    	return TypeSDKBonjour_youku.Instance().GetUserData();
    }
    public String CallPlatformData()
    {
    	return TypeSDKBonjour_youku.Instance().GetPlatformData();
    }
    public boolean CallIsHasRequest(String _in_data)
    {
    	return TypeSDKBonjour_youku.Instance().isHasRequest(_in_data);
    }        
    public String CallAnyFunction(String FuncName,String _in_data)
    {
    	Method[] me = TypeSDKBonjour_youku.Instance().getClass().getMethods();
    	for(int i = 0;i<me.length;++i)
    	{
    		if(me[i].getName().equals(FuncName))
    		{
    			try 
    			{
					return (String) me[i].invoke(TypeSDKBonjour_youku.Instance(),_in_context ,_in_data);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    	return "error";
    }	
	public void AddLocalPush(String _in_data)
    {
    	TypeSDKLogger.i(_in_data);
    	TypeSDKBonjour_youku.Instance().AddLocalPush(_in_context, _in_data);
    }
    
    public void RemoveLocalPush(String _in_data)
    {
    	TypeSDKLogger.i(_in_data);
    	TypeSDKBonjour_youku.Instance().RemoveLocalPush(_in_context, _in_data);
    }
    
    public void RemoveAllLocalPush()
    {
    	
    	TypeSDKBonjour_youku.Instance().RemoveAllLocalPush(_in_context);
    }
	
}
