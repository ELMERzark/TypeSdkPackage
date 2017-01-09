/**
 *@ TypeSDKClient
 *@ 2016-10
 *@ Copyright© 2016 www.typesdk.com. All rights reserved. 
 */

package com.type.sdk.android.baidu;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.type.sdk.android.BaseMainActivity;

import com.type.sdk.android.TypeSDKDefine;
import com.type.sdk.android.TypeSDKEvent;
import com.type.sdk.android.TypeSDKEventListener;
import com.type.sdk.android.TypeSDKEventManager;
import com.type.sdk.android.TypeSDKLogger;
import com.type.utils.*;
import com.type.sdk.android.TypeSDKDefine.AttName;


import android.os.Bundle;
import android.content.res.Configuration;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.View;

import com.type.sdk.android.TypeSDKData;
import com.type.sdk.android.TypeSDKTool;
import com.type.sdk.android.baidu.TypeSDKBonjour_baidu;
import com.type.sdk.android.*;

import android.os.Handler;
import android.os.Looper;

public class MainActivity extends BaseMainActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){

    	
    	String result="";
    	result +="~"+ TypeSDKBonjour_baidu.Instance().isHasRequest(TypeSDKDefine.AttName.REQUEST_INIT_WITH_SEVER);
    	result +="~"+ TypeSDKBonjour_baidu.Instance().isHasRequest(TypeSDKDefine.AttName.SUPPORT_PERSON_CENTER);
    	result +="~"+ TypeSDKBonjour_baidu.Instance().isHasRequest(TypeSDKDefine.AttName.SUPPORT_SHARE);
        super.onCreate(savedInstanceState);
    	TypeSDKLogger.i("result "+result);
		CallInitSDK();
//    	TypeSDKUpdateManager update = new TypeSDKUpdateManager(this, 
//    			TypeSDKBonjour_baidu.Instance().platform.GetData(AttName.CHANNEL_ID), TypeSDKBonjour_baidu.Instance().platform.GetData("check_update_url"));
//        update.checkUpdateInfo();
    	
    	/* TypeSDKLogger.i("android on create finish");
        CallInitSDK(); */
        
        /* TypeSDKEventManager.Instance().AddEventListener(TypeSDKEvent.EventType.AND_EVENT_INIT_FINISH, new TypeSDKEventListener()
        {
			
			@Override
			public Boolean NotifySDKEvent(TypeSDKEvent event) {
				// TODO Auto-generated method stub
				TypeSDKLogger.i("响应事件 "+ event.type + " " + event.data);
				return true;
			}
		}); */
	}
  
    /* TypeSDKEventListener initListener = new TypeSDKEventListener(){
		@Override
		public Boolean NotifySDKEvent(TypeSDKEvent event){
			RealOncreate();
			return null;
		}
	}; */
	
	private void RealOncreate()
	{
//		SharkSDK.Instance().ClientInit(_in_context, "");
	}
	
    @Override
    protected void onDestroy(){
		super.onDestroy();
    	TypeSDKLogger.e("sdk do destory");
    	TypeSDKBonjour_baidu.Instance().onDestroy();
    	
    }

	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
		if(this.getResources().getConfiguration().orientation
			==Configuration.ORIENTATION_LANDSCAPE){
			
		}else if(this.getResources().getConfiguration().orientation
				==Configuration.ORIENTATION_PORTRAIT){
			
		}
		if(newConfig.hardKeyboardHidden==Configuration.HARDKEYBOARDHIDDEN_NO){
			
		}else if(newConfig.hardKeyboardHidden==Configuration.HARDKEYBOARDHIDDEN_YES){
			
		}
	}
    
    @Override
    protected void onResume() 
    {
    	super.onResume();
    	TypeSDKBonjour_baidu.Instance().onResume();
    }
    
    @Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		TypeSDKBonjour_baidu.Instance().onStop();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		TypeSDKBonjour_baidu.Instance().onPause();
	}

	/**
     *  ���ⲿ call �� init����
     * @param _in_context
     * @param _in_data
     */
    public  void CallInitSDK()
    {
    	String _in_data = "";    	
    	TypeSDKBonjour_baidu.Instance().initSDK(_in_context,_in_data);
    }
    /**
     * ���ⲿ call�� login����
     * @param _in_context
     * @param _in_data
     */
    public  void CallLogin(String _in_data)
    {
    	TypeSDKLogger.i(_in_data);
    	TypeSDKBonjour_baidu.Instance().ShowLogin(_in_context,_in_data);
    }
    /**
     * ���ⲿ call ��logout����
     * @param _in_context
     */
    public  void CallLogout()
    {
    	TypeSDKBonjour_baidu.Instance().ShowLogout(_in_context);
    }
    /***
     * 
     * payData.SetData(U3DSharkAttName.REAL_PRICE,inputStr);
			payData.SetData(U3DSharkAttName.ITEM_NAME,"sk bi");
			payData.SetData(U3DSharkAttName.ITEM_DESC,"desc");
			payData.SetData(U3DSharkAttName.ITEM_COUNT,"1");
			payData.SetData(U3DSharkAttName.ITEM_SEVER_ID,"id");
			payData.SetData(U3DSharkAttName.SEVER_ID,"1");
			payData.SetData(U3DSharkAttName.EXTRA,"extra
			
     * ���ⲿcall�Ķ���֧������(rmb�һ� ��Ϸ��)
     * @param _in_context
     * @param _in_data
     * @return
     */
    public  String CallPayItem(final String _in_data)
    {
    	TypeSDKLogger.i(_in_data);
    	new Thread() {
			@Override
			public void run() {
				String payMessage;
				try {
					payMessage = HttpUtil.http_get(TypeSDKBonjour_baidu
							.Instance().platform
							.GetData(AttName.SWITCHCONFIG_URL));
					if (((payMessage.equals("") || payMessage.isEmpty()) && openPay)
							|| TypeSDKTool.openPay(TypeSDKBonjour_baidu
									.Instance().platform
									.GetData(AttName.SDK_NAME), payMessage)) {
						TypeSDKBonjour_baidu.Instance().PayItem(_in_context,
								_in_data);
					} else {
						TypeSDKNotify_baidu notify = new TypeSDKNotify_baidu();
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
    	return TypeSDKBonjour_baidu.Instance().ExchangeItem(_in_context, _in_data);
    }
    /***
     * ���ⲿ���õ� ��ʵ����������
     * @param _in_context
     */
    public  void CallToolBar()
    {
    	TypeSDKBonjour_baidu.Instance().ShowToolBar(_in_context);
    }
    public void CallHideToolBar()
    {
    	TypeSDKBonjour_baidu.Instance().HideToolBar(_in_context);
    }
    /***
     * ���ⲿ���õ���ʵ�û����ĺ���
     * @param _in_context
     */
    public  void CallPersonCenter()
    {
    	TypeSDKBonjour_baidu.Instance().ShowPersonCenter(_in_context);
    }
    public void CallHidePersonCenter()
    {
    	TypeSDKBonjour_baidu.Instance().HidePersonCenter(_in_context);
    }
    public void CallShare(String _in_data)
    {
    	TypeSDKBonjour_baidu.Instance().ShowShare(_in_context, _in_data);
    }
    public void CallSetPlayerInfo(String _in_data)
    {
    	TypeSDKBonjour_baidu.Instance().SetPlayerInfo(_in_context, _in_data);
    }
    public void CallExitGame()
    {
    	TypeSDKBonjour_baidu.Instance().ExitGame(_in_context);
    }
    public void CallDestory()
    {
    	TypeSDKBonjour_baidu.Instance().onDestroy();
    }
    public int CallLoginState()
    {
    	return TypeSDKBonjour_baidu.Instance().LoginState(_in_context);
    }
    public String CallUserData()
    {
    	return TypeSDKBonjour_baidu.Instance().GetUserData();
    }
    public String CallPlatformData()
    {
    	return TypeSDKBonjour_baidu.Instance().GetPlatformData();
    }
    public boolean CallIsHasRequest(String _in_data)
    {
    	return TypeSDKBonjour_baidu.Instance().isHasRequest(_in_data);
    }
    
    public String CallAnyFunction(String FuncName,String _in_data)
    {
    	Method[] me = TypeSDKBonjour_baidu.Instance().getClass().getMethods();
    	for(int i = 0;i<me.length;++i)
    	{
    		if(me[i].getName().equals(FuncName))
    		{
    			try 
    			{
					return (String) me[i].invoke(TypeSDKBonjour_baidu.Instance(),_in_context ,_in_data);
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
    	TypeSDKBonjour_baidu.Instance().AddLocalPush(_in_context, _in_data);
    }
    
    public void RemoveLocalPush(String _in_data)
    {
    	TypeSDKLogger.i(_in_data);
    	TypeSDKBonjour_baidu.Instance().RemoveLocalPush(_in_context, _in_data);
    }
    
    public void RemoveAllLocalPush()
    {
    	
    	TypeSDKBonjour_baidu.Instance().RemoveAllLocalPush(_in_context);
    }
}
