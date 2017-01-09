/**
 *@ TypeSDKClient
 *@ 2016-10
 *@ Copyright© 2016 www.typesdk.com. All rights reserved. 
 */

package com.type.sdk.android;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Environment;
import android.view.Gravity;
import android.widget.TextView;

import com.type.sdk.android.TypeSDKDefine.AttName;
import com.type.sdk.exception.FtpUploadUtil;
import com.type.sdk.notification.PushService;
import com.type.sdk.tools.LogCollector;
import com.type.utils.HttpUtil;
import com.type.utils.SystemUtils;

public class TypeSDKTool {
	public static boolean isPayDebug = false;
	public static boolean isOpenPush = false;
	public static boolean isOpenPay = true;
	public static boolean isPostok = true;
	public static boolean showLogin = true;
	public static String msg = "";
	public static String getFromAssets(Context _in_context, String _in_fileName) {
		try {
			InputStreamReader inputReader = new InputStreamReader(_in_context
					.getResources().getAssets().open(_in_fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			String Result = "";
			while ((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 返回当前屏幕是否为竖屏。
	 * 
	 * @param context
	 * @return 当且仅当当前屏幕为竖屏时返回true,否则返回false。
	 */
	public static boolean isScreenOriatationPortrait(Context context) {
		return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
	}

	/** 判断字符串数组是否包含某个字符串
	 * 
	 * @param strs
	 * @param str
	 * @return
	 */
	public static boolean arrayContainsStr(String[] strs, String str) {
		if (strs.length > 0) {
			for (String s : strs) {
				if (s.equals(str)) {
					return true;
				}
			}
		}
		return false;
	}

	/**提示框
	 * 
	 * @param tipstr
	 * @param mContext
	 */
	public static void showDialog(String tipstr,Context mContext){
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("提示");
		TextView mMsg = new TextView(mContext);
		mMsg.setText(tipstr);
		mMsg.setGravity(Gravity.CENTER_HORIZONTAL);
		mMsg.setTextSize(18);
		builder.setView(mMsg);
		builder.setCancelable(true);
		builder.setNegativeButton("确定", new OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});		
		Dialog tipDialog = builder.create();
		tipDialog.show();
	}
	
	/**判断是否开启登录
	 * 
	 * @param buffstr
	 * @param sdkName
	 * @return
	 */
	public static boolean showLogin(String buffstr, String sdkName){
		boolean bl = true;
		TypeSDKData.BaseData white_id = new TypeSDKData.BaseData();
		white_id.StringToData(buffstr);
		String allowip = white_id.GetData("allowip");
		msg = white_id.GetData("msg");
		if(allowip.length() > 0){
			TypeSDKData.BaseData IpResult_data = new TypeSDKData.BaseData();
			String IpResult = HttpUtil.mHttpGet(white_id.GetData(AttName.GET_IP));
			IpResult_data.StringToData(IpResult);
			IpResult = IpResult_data.GetData(AttName.IP);
			TypeSDKLogger.i("IpResult:" + IpResult);
			if (allowip.indexOf(IpResult) >= 0){
				bl = true;
			}else{
				bl = false;
			}
		}
		return bl;
	}

	/**判断是否开启支付
	 * 
	 * @param curChannel
	 * @param str
	 * @return
	 */
	public static boolean openPay(String curChannel,String str){
		TypeSDKData.BaseData configData = new TypeSDKData.BaseData();		
		if(str.length()>0){
			configData.StringToData(str);
			String payStr = configData.GetData(AttName.PAYMENT_PROFILE);
			TypeSDKData.BaseData payData = new TypeSDKData.BaseData();
			payData.StringToData(payStr);
			String paySDKRegular = payData.GetData(AttName.SDK_OPEN_REGULAR);
			String[] paySDKList = payData.GetStringArray(AttName.SDK_OPEN_LIST);
			if (paySDKRegular.equals("all")) {
				isOpenPay = true;
			} else if (paySDKRegular.equals("contain")) {
				if ((paySDKList.length > 0)
						&& arrayContainsStr(paySDKList, curChannel)) {
					isOpenPay = true;
				} else {
					isOpenPay = false;
				}
			} else {
				if ((paySDKList.length > 0)
						&& arrayContainsStr(paySDKList, curChannel)) {
					isOpenPay = false;
				} else {
					isOpenPay = true;
				}
			}
		}
			return isOpenPay;
	}
	/**解析控制文件
	 * 
	 * @param buffStr
	 * @param _context
	 * @param sdkName
	 */
	public static void ctrlMessage(String buffStr,Context _context,String sdkName){
		TypeSDKData.BaseData ctrlData = new TypeSDKData.BaseData();
		
		if (buffStr.length() > 0) {
			ctrlData.StringToData(buffStr);
			//TypeSDKLogger.i(ctrlData.DataToString());

			String _white_id = ctrlData.GetData(AttName.WHITE_ID);			
			TypeSDKLogger.w("_white_id:" + _white_id);
			showLogin = showLogin(_white_id, sdkName);
			
			String _other = ctrlData.GetData(AttName.OTHER);
			TypeSDKLogger.w("_other:" + _other);
			TypeSDKData.BaseData otherData = new TypeSDKData.BaseData();
			otherData.StringToData(_other);
			FtpUploadUtil.crashURL = otherData.GetData(AttName.CRASH_URL);
			FtpUploadUtil.crashPort = otherData.GetData(AttName.CRASH_PORT);
			FtpUploadUtil.userName = otherData.GetData(AttName.USER_NAME);
			FtpUploadUtil.userPassword = otherData.GetData(AttName.USER_PASS_WORD);
			TypeSDKLogger.i(FtpUploadUtil.crashURL+";"+FtpUploadUtil.crashPort+";"+FtpUploadUtil.userName+";"+FtpUploadUtil.userPassword);
			String openLog = otherData.GetData(AttName.OPEN_LOG);
			if(openLog.equals("1")){
				TypeSDKLogger.showLog = true;
			}else{
				TypeSDKLogger.showLog = false;
			}
			String openPay = otherData.GetData(AttName.OPEN_PAY);
			TypeSDKLogger.i(openPay);
			if(openPay.equals("1")){
				isOpenPay = true;
			}else{
				isOpenPay = false;
			}
			String openPush = otherData.GetData(AttName.PUSHSERVICE);
			if(openPush.equals("1")){
				openPushservice(_context);
			}else{
				stopPushservice(_context);
			}
			String payMode = otherData.GetData(AttName.PAY_MODE);
			TypeSDKLogger.i("payMode:"+payMode);
			if(payMode.equals("debug")){
				isPayDebug = true;
			}else{
				isPayDebug = false;
			}
		}
	}
	/** 判断是否打开日志监控和崩溃日志收集
	 * 
	 * @param buffStr
	 * @param sdkName
	 * @return
	 */
	public static boolean openLogCollector(String buffStr, String sdkName) {
		//TypeSDKLogger.i(buffStr);
		boolean openLogCollector = false;
		TypeSDKData.BaseData crashData = new TypeSDKData.BaseData();
		if (buffStr.length() > 0) {
			crashData.StringToData(buffStr);
			//TypeSDKLogger.i(crashData.DataToString());
			String profileVersion = crashData.GetData(AttName.PROFILE_VERSION);
			//TypeSDKLogger.i("profileVersion:" + profileVersion);
			String crashProfile = crashData.GetData(AttName.CRASH_PROFILE);
			//TypeSDKLogger.i("crashProfile:" + crashProfile);
			TypeSDKData.BaseData crashProfileData = new TypeSDKData.BaseData();
			crashProfileData.StringToData(crashProfile);
			String isOpen = crashProfileData.GetData(AttName.IS_OPEN);
			//TypeSDKLogger.i("isOpen:" + isOpen);
			String[] catchTags = crashProfileData
					.GetStringArray(AttName.CATCH_TAG);
			//TypeSDKLogger.i("catchTags:" + outArrayElements(catchTags));
			if(!outArrayElements(catchTags).equals("")
				&&!outArrayElements(catchTags).isEmpty()
				&&outArrayElements(catchTags).contains(";")){
				TypeSDKLogger.w("entering logcollector thread");
				new Thread(new LogCollector(outArrayElements(catchTags))){}.start();
			}			
			String collectModelRegular = crashProfileData
					.GetData(AttName.COLLECT_MODEL_REGULAR);
			//TypeSDKLogger.i("collectModelRegular:" + collectModelRegular);
			String[] collectModels = crashProfileData
					.GetStringArray(AttName.COLLECT_MODEL);
			//TypeSDKLogger.i("collectModels:" + outArrayElements(collectModels));
			String collectSDKRegular = crashProfileData
					.GetData(AttName.COLLECT_MODEL_REGULAR);
			//TypeSDKLogger.i("collectSDKRegular:" + collectSDKRegular);
			String[] collectSDK = crashProfileData
					.GetStringArray(AttName.COLLECT_SDK);
			//TypeSDKLogger.i("collectSDK:" + outArrayElements(collectSDK));
			
			if (isOpen.equals("0")) {
				openLogCollector = false;
			} else {
				boolean isModel = false;
				boolean isSDK = false;
				String curModel = SystemUtils.getModel();
				if (collectModelRegular.equals("all")) {
					isModel = true;
				} else if (collectModelRegular.equals("contain")) {
					if ((collectModels.length > 0)
							&& arrayContainsStr(collectModels, curModel)) {
						isModel = true;
					} else {
						isModel = false;
					}
				} else {
					if ((collectModels.length > 0)
							&& arrayContainsStr(collectModels, curModel)) {
						isModel = false;
					} else {
						isModel = true;
					}
				}
				if (collectSDKRegular.equals("all")) {
					isSDK = true;
				} else if (collectSDKRegular.equals("contain")) {
					if ((collectSDK.length > 0)
							&& arrayContainsStr(collectSDK, sdkName)) {
						isSDK = true;
					} else {
						isSDK = false;
					}
				} else {
					if ((collectSDK.length > 0)
							&& arrayContainsStr(collectSDK, sdkName)) {
						isSDK = false;
					} else {
						isSDK = true;
					}
				}
				openLogCollector = isSDK & isModel;
				//TypeSDKLogger.i("openLogCollector" + String.valueOf(openLogCollector));
			}
		}
		TypeSDKLogger.i("openLogCollector" + String.valueOf(openLogCollector));
		return openLogCollector;
	}

	/** 将字符串数组所有元素拼接成字符串
	 * 
	 * @param strs
	 * @return
	 */
	public static String outArrayElements(String[] strs) {
		if(strs.length==0){
			return "";
		}
		String tempStr = "";
		for (String s : strs) {
			tempStr += s + ";";
		}
		return tempStr;
	}

	/**清空某个文件夹
	 * 
	 * @param path
	 */
	public static void clearFolder(String path){
		TypeSDKLogger.i("clearPath:"+path);
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			TypeSDKLogger.i("clear Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)");
			File file = new File(path);
			TypeSDKLogger.i("file.exists():"+String.valueOf(file.exists())+"file.isDirectory():"+String.valueOf(file.isDirectory()));
			if (file.exists()&&file.isDirectory()) {
				TypeSDKLogger.w("clear file.exists()&&file.isDirectory()");
				if(file.list().length>0){
					TypeSDKLogger.w("clear file.list().length>0");
					String[] strs = file.list();
					for(int i=0;i<strs.length;i++){
						File singleFile = new File(path+strs[i]);
						if(singleFile.isFile()){
							TypeSDKLogger.w("clear singleFile.isFile()");
							singleFile.delete();
						}
					}
				}
			}
		}
	}
	/**开启本地推送
	 * 
	 * @param _context
	 */
	
	public static void openPushservice(Context _context){
		_context.startService(new Intent(_context,PushService.class));
	}
	
	/**关闭本地推送
	 * 
	 * @param _context
	 */
	public static void stopPushservice(Context _context){
		
	}

	
	/**
	 * post请求
	 * 
	 * @param url
	 * @param map
	 * @return
	 */
	public static String mHttpPost(String url, Map<String, Object> map){
		// 组织请求参数  
		StringBuffer params = new StringBuffer();
		StringBuffer sb = null;
		if(map != null){
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				Object value = map.get(key);
	            params.append(key);  
	            params.append("=");  
	            params.append(value);  
	            params.append("&");  
	        }  
		}
        if (params.length() > 0) {  
            params.deleteCharAt(params.length() - 1);  
        }
        TypeSDKLogger.i("params:" + params.toString());
        URL mURL;
		try {
			TypeSDKLogger.i("url:" + url);
			mURL = new URL(url);
			HttpURLConnection httpConn=(HttpURLConnection)mURL.openConnection();
	        //设置参数
	        httpConn.setDoOutput(true);   //需要输出
	        httpConn.setDoInput(true);   //需要输入
	        httpConn.setUseCaches(false);  //不允许缓存
	        httpConn.setRequestMethod("POST");   //设置POST方式连接
	        //设置请求属性
	        httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
	        httpConn.setRequestProperty("Charset", "UTF-8");
	        //连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
	        httpConn.connect();
	        //建立输入流，向指向的URL传入参数
	        DataOutputStream dos=new DataOutputStream(httpConn.getOutputStream());
	        dos.writeBytes(params.toString());
	        dos.flush();
	        dos.close();
	        //获得响应状态
	        int resultCode=httpConn.getResponseCode();
	        TypeSDKLogger.i("resultCode:" + resultCode);
	        if(HttpURLConnection.HTTP_OK==resultCode){
	          sb=new StringBuffer();
	          String readLine=new String();
	          BufferedReader responseReader=new BufferedReader(new InputStreamReader(httpConn.getInputStream(),"UTF-8"));
	          while((readLine=responseReader.readLine())!=null){
	            sb.append(readLine).append("\n");
	          }
	          responseReader.close();
	          isPostok = true;
	          TypeSDKLogger.i("httpPost sb:" + sb.toString());
	          
	        }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return sb == null?"":sb.toString();
	}
}
