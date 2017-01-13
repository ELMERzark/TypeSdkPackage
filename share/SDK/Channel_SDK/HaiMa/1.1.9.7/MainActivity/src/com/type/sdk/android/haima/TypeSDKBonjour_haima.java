package com.type.sdk.android.haima;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Looper;

import com.type.sdk.android.TypeSDKBaseBonjour;
import com.type.sdk.android.TypeSDKData;
import com.type.sdk.android.TypeSDKDefine;
import com.type.sdk.android.TypeSDKData.PayInfoData;
import com.type.sdk.android.TypeSDKData.PayResultData;
import com.type.sdk.android.TypeSDKDefine.AttName;
import com.type.sdk.android.TypeSDKLogger;
import com.type.sdk.android.TypeSDKTool;
import com.haima.loginplugin.ZHErrorInfo;
import com.haima.loginplugin.ZHPayUserInfo;
import com.haima.loginplugin.callback.OnCheckUpdateListener;
import com.haima.loginplugin.callback.OnLoginListener;
import com.haima.payPlugin.callback.OnPayListener;
import com.haima.payPlugin.infos.ZHPayOrderInfo;
import com.haima.plugin.haima.HMPay;

public class TypeSDKBonjour_haima extends TypeSDKBaseBonjour {

	public Context appContext;
	public Activity appActivity;
	private boolean isLandscape = true;
	private boolean isDebug = false;
	private String token;
	private String uid;
	
	private static class SingletonHandler {
		static final TypeSDKBonjour_haima instance = new TypeSDKBonjour_haima();
	}
	
	public static TypeSDKBonjour_haima Instance(){
		return SingletonHandler.instance;
	}

	@Override
	public void initSDK(Context _in_context, String _in_data) {
		// TODO Auto-generated method stub
		TypeSDKLogger.e( "initSDK");
		appContext = _in_context;
		appActivity = (Activity) appContext;
		if(TypeSDKTool.isScreenOriatationPortrait(appContext)){
			isLandscape = false;
		}
		
		isDebug = TypeSDKTool.isPayDebug;
		if (isInit) {
			TypeSDKLogger.e( "sdk is already init");
			TypeSDKNotify_haima notify = new TypeSDKNotify_haima();
			notify.Init();
			return;
		}
		this.haimaInit();
		
	}

	@Override
	public void ShowLogin(Context _in_context, String _in_data) {
		// TODO Auto-generated method stub
		TypeSDKLogger.e( "ShowLogin");
		super.ShowLogin(_in_context, _in_data);
		if(HMPay.isLogined()){
			TypeSDKNotify_haima notify = new TypeSDKNotify_haima();
    		notify.sendToken(token, uid);
		} else {
			this.haimaLogin();
		}
		
	}

	@Override
	public void ShowLogout(Context _in_context) {
		// TODO Auto-generated method stub
		TypeSDKLogger.e( "ShowLogout");
		this.haimaLogout();
	}

	@Override
	public void ShowPersonCenter(Context _in_context) {
		// TODO Auto-generated method stub
		TypeSDKLogger.e( "ShowPersonCenter");
//		HMPay.startUserCenter(appContext);
	}

	@Override
	public void HidePersonCenter(Context _in_context) {
		// TODO Auto-generated method stub
		TypeSDKLogger.e( "HidePersonCenter");
	}

	@Override
	public void ShowToolBar(Context _in_context) {
		// TODO Auto-generated method stub
		TypeSDKLogger.e( "ShowToolBar");
	}

	@Override
	public void HideToolBar(Context _in_context) {
		// TODO Auto-generated method stub
		TypeSDKLogger.e( "HideToolBar");
	}

	public String PayItemByData(Context _in_context,
			TypeSDKData.PayInfoData _in_pay) {
		TypeSDKLogger.e( "pay begin");
		String _in_OrderID = _in_pay.GetData(TypeSDKDefine.AttName.BILL_NUMBER);
		this.haimaPay(_in_pay);
		return _in_OrderID;
	}

	@Override
	public String PayItem(Context _in_context, String _in_data) {
		// TODO Auto-generated method stub
		TypeSDKData.PayInfoData _in_pay = new TypeSDKData.PayInfoData();
		_in_pay.StringToData(_in_data);
		return PayItemByData(_in_context, _in_pay);
	}

	@Override
	public String ExchangeItem(Context _in_context, String _in_data) {
		// TODO Auto-generated method stub
		return PayItem(_in_context, _in_data);
	}

	@Override
	public int LoginState(Context _in_context) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void ShowShare(Context _in_context, String _in_data) {
		// TODO Auto-generated method stub
		TypeSDKLogger.e( "ShowShare");
	}

	@Override
	public void SetPlayerInfo(Context _in_context, String _in_data) {
		// TODO Auto-generated method stub
		TypeSDKLogger.e( "SetPlayerInfo");
		SendInfo(_in_context, _in_data);
	}

	@Override
	public void SendInfo(Context _in_context, String _in_data) {
		// TODO Auto-generated method stub
		TypeSDKLogger.e( "SendInfo:" + _in_data);
		try {
			userInfo.StringToData(_in_data);
			
			String extendInfo = new StringBuilder()
			.append("gameId=").append(platform.GetData(AttName.APP_ID))
			.append("&service=").append(userInfo.GetData(AttName.SERVER_NAME))
			.append("&role=").append(userInfo.GetData(AttName.ROLE_ID))
			.append("&grade=").append(userInfo.GetData(AttName.ROLE_LEVEL)).toString();
			TypeSDKLogger.e( "extendInfo:" + extendInfo);
			
		} catch (Exception e) {
			TypeSDKLogger.e( "上传用户信息:" + e.getMessage());
		}

	}

	@Override
	public void ExitGame(Context _in_context) {
		TypeSDKLogger.e( "执行ExitGame方法");
		Handler mainHandler = new Handler(Looper.getMainLooper());
		mainHandler.post(new Runnable() {
			@Override
			public void run() {
				if(exitGameListenser()){
					System.exit(0);
				}
				
			}
		});
	}

	public void onResume() {
		TypeSDKLogger.e( "onResume");
		if(isInit){
			HMPay.onResume(appActivity);
		}
		
	}
	
	public void onStart(Context _in_context) {
		TypeSDKLogger.e( "onStart");
		((Activity)_in_context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
	}

	public void onPause() {
		TypeSDKLogger.e( "onPause");
		if(isInit){
			HMPay.onPause();
		}
	}

	public void onStop() {
		TypeSDKLogger.e( "onStop");
	}

	public void onDestroy() {
		TypeSDKLogger.e( "onDestroy");
		
	}

	private void haimaInit() {

		TypeSDKLogger.e( "init begin");

		// final Activity runActivi;
		// if (UnityPlayer.currentActivity != null)
		// runActivi = UnityPlayer.currentActivity;
		// else
		// runActivi = appActivity;

		Handler mainHandler = new Handler(Looper.getMainLooper());
		mainHandler.post(new Runnable() {
			@Override
			public void run() {
				// runActivi.runOnUiThread(new Runnable() {
				// public void run() {
				try {
					TypeSDKLogger.e( "initSDK_start");
					if (HMPay.init(appActivity, isLandscape, platform.GetData(AttName.APP_ID), new OnCheckUpdateListener() {
						
						@Override
						public void onCheckUpdateSuccess(boolean isNeedUpdate, boolean isForceUpdate, boolean isTestMode) {
							// TODO Auto-generated method stub
							TypeSDKLogger.e("isNeedUpdate:" + isNeedUpdate);//更新
							TypeSDKLogger.e("isForceUpdate:" + isForceUpdate);//强更
							TypeSDKLogger.e("isTestMode:" + isTestMode);//测试模式				
						}
						
						@Override
						public void onCheckUpdateFailed(ZHErrorInfo errInfo, int ifErrorType) {
							// TODO Auto-generated method stub
							TypeSDKLogger.e("errInfo:" + errInfo.toString());
							TypeSDKLogger.e("ifErrorType:" + ifErrorType);
						}
					}, isDebug, HMPay.CHECKUPDATE_FAILED_SHOW_NONE)) {
						TypeSDKLogger.e("init_success");
		                TypeSDKNotify_haima notify = new TypeSDKNotify_haima();
		                notify.Init();
						isInit = true;
		                return;
		            } else {
		            	TypeSDKLogger.e("init_fail");
		            }
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		TypeSDKLogger.e( "init done");

	}

	private void haimaLogin() {
		Handler mainHandler = new Handler(Looper.getMainLooper());
		mainHandler.post(new Runnable() {
			@Override
			public void run() {
				HMPay.login(appContext, new OnLoginListener() {
					
					@Override
					public void onLoginSuccess(ZHPayUserInfo payUserInfo) {
						// TODO Auto-generated method stub
						TypeSDKLogger.d( "LOGIN_SUCCESS");
		                TypeSDKLogger.d( "getUdid:" + payUserInfo.userId);
		                TypeSDKLogger.d( "getLoginToken:" + payUserInfo.getLoginToken());
		                // 发送登录SDK成功广播通知
						token = payUserInfo.getLoginToken();
						uid = payUserInfo.userId;
		                TypeSDKNotify_haima notify = new TypeSDKNotify_haima();
    					notify.sendToken(payUserInfo.getLoginToken(), payUserInfo.userId);
					}
					
					@Override
					public void onLoginFailed(ZHErrorInfo errorInfo) {
						// TODO Auto-generated method stub
						TypeSDKLogger.d( "LOGIN_FAIL");
						TypeSDKLogger.d( "errorInfo:" + errorInfo.toString());
					}
					
					@Override
					public void onLogOut() {
						// TODO Auto-generated method stub
						TypeSDKNotify_haima notify = new TypeSDKNotify_haima();
						notify.Logout();
					}
				});
				
			}
		});

	}

	private void haimaLogout() {
		Handler mainHandler = new Handler(Looper.getMainLooper());
		mainHandler.post(new Runnable() {
			@Override
			public void run() {
				HMPay.logOut();
			}
		});
		
	}
	
	private void haimaPay(final PayInfoData _in_pay) {
		Handler mainHandler = new Handler(Looper.getMainLooper());
		mainHandler.post(new Runnable() {
			@Override
			public void run() {
				try {
					TypeSDKLogger.e( "pay_start");
					
					TypeSDKLogger.e( "ITEM_NAME:" + _in_pay.GetData(AttName.ITEM_NAME));
					TypeSDKLogger.e( "ITEM_DESC:" + _in_pay.GetData(AttName.ITEM_DESC));
					TypeSDKLogger.e( "USER_ID:" + userInfo.GetData(AttName.USER_ID));
					TypeSDKLogger.e( "BILL_NUMBER:" + _in_pay.GetData(AttName.BILL_NUMBER));
					// 支付
					ZHPayOrderInfo orderInfo = new ZHPayOrderInfo();
					// 商品名称
					orderInfo.goodName = _in_pay.GetData(AttName.ITEM_NAME);
					// 游戏名称
					orderInfo.gameName = platform.GetData(AttName.APP_NAME);//"英雄联盟总动员"
					// 商品价格
					if(isDebug){
						orderInfo.goodPrice = 0.01f;
					} else {
						orderInfo.goodPrice = _in_pay.GetInt(AttName.REAL_PRICE) * 0.01f;
					}
					
					// 订单号
					orderInfo.orderNo = _in_pay.GetData(AttName.BILL_NUMBER);
					orderInfo.userParam = userInfo.GetData(AttName.USER_ID);
					
					HMPay.pay(orderInfo, appActivity, new OnPayListener() {
						PayResultData payResult = new PayResultData();
						TypeSDKNotify_haima notify = new TypeSDKNotify_haima();
						@Override
						public void onPaySuccess(ZHPayOrderInfo arg0) {
							// TODO Auto-generated method stub
							// 支付成功
							TypeSDKLogger.e( "pay_success");
							payResult.SetData(AttName.PAY_RESULT, "1");
							payResult.SetData(AttName.PAY_RESULT_REASON, "SUCCESS");
							notify.Pay(payResult.DataToString());
						}
						
						@Override
						public void onPayFailed(ZHPayOrderInfo arg0, ZHErrorInfo arg1) {
							// TODO Auto-generated method stub
							TypeSDKLogger.e( "return Error");
							payResult.SetData(AttName.PAY_RESULT, "0");
							payResult.SetData(AttName.PAY_RESULT_REASON, "PAY_FAIL");
							notify.Pay(payResult.DataToString());
						}
					});
					
				} catch (NumberFormatException exception) {
					TypeSDKLogger.e( "Price input parse error: " + exception.toString());
				}

			}
		});

	}
	
	@Override
	public String getUserFriends(Context _in_context, String _in_data) {
		// TODO Auto-generated method stub
		return"";
	}

	@Override
	public void ShowInvite(Context _in_context, String _in_data) {
		// TODO Auto-generated method stub
		
	}

}
