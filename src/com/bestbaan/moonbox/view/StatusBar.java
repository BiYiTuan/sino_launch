package com.bestbaan.moonbox.view;

import java.util.Timer;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bestbaan.moonbox.util.CheckSdcard;
import com.bestbaan.moonbox.util.MACUtils;
import com.bestbaan.moonbox.util.RequestUtil;
import com.bestbaan.moonbox.util.VersionUtil;
import com.moon.android.iptv.Configs;
import com.moon.android.iptv.LauncherApplication;
import com.moon.android.iptv.Configs.ServerInterface;
import com.sinopad.launch.R;

public class StatusBar extends LinearLayout {

	private Context mContext;
	private ImageView mLanImg;
	private ImageView mWifiImg;
	private ImageView mUsbImg;
	private ImageView mMsgImg;
	private TextView mtv_mac,mtv_ver,mtv_ip;
	private int GetIP_MINUTE=5;
	private final int IP_INIT = 4;
	private String Ip;
	public StatusBar(Context paramContext) {
		this(paramContext, null);
	}

	public StatusBar(Context context, AttributeSet attributeset) {
		super(context, attributeset);
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.status_bar, this) ;
		mContext = context;
		mWifiImg = (ImageView) view.findViewById(R.id.status_bar_wifi);
		mLanImg = (ImageView) view.findViewById(R.id.status_bar_lan);
		mUsbImg = (ImageView) view.findViewById(R.id.status_bar_sub);
		mMsgImg = (ImageView) view.findViewById(R.id.status_bar_msg);
		mtv_mac=(TextView) view.findViewById(R.id.status_mac);
		mtv_ip=(TextView) view.findViewById(R.id.status_ip);
		mtv_ver=(TextView) view.findViewById(R.id.status_ver);
		mtv_mac.setText(MACUtils.getMac());
		mtv_ver.setText(VersionUtil.getVersionName(LauncherApplication.getApplication()));
		initEthConnect();
		regNetworkReceiver();
		setSdcardStatus();
		TimerStart();
	}
	private void TimerStart() {
		// TODO Auto-generated method stub
		 Timer timer = new Timer();
		 timer.schedule(new java.util.TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				initIp();
			}

			              
			 
		 },1000,1000*60*GetIP_MINUTE);
	}
	private void initIp() {
		// TODO Auto-generated method stub
		
		final String Url=new Configs().new ServerInterface().GET_IP;
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
			      
				try {
					 String ip=RequestUtil.getInstance().request(Url);
					 Log.d("ip",ip);
				
					
					 
					 
					 boolean isIP = ip.length()>20?false:true; 
					
					 if(isIP){
						 Ip=ip;
//						 Log.d("ip",Ip);
						 mHandler.sendEmptyMessage(IP_INIT);
					 }
				} catch (Exception e) {
					// TODO: handle exception
				}
			   
			   
			}
			
		}.start();
//		fn.post(new Configs().new ServerInterface().GET_IP, new AjaxCallBack<Object>(){
//
//			@Override
//			public void onSuccess(Object t) {
//				// TODO Auto-generated method stub
//				 String ip=t.toString();
//				 Log.d("debug",ip);
//				 boolean isIP = ip.length()>20?false:true; 
//				
//				 if(isIP){
//					 mtv_ip.setText(ip);
//				 }
//			}
//			       
//		});
	}   
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case IP_INIT:
				mtv_ip.setText(Ip);
				break;
			}
		}
		
	};
	private void regNetworkReceiver() {
		IntentFilter filter = new IntentFilter();
        
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);  
        filter.addAction(Intent.ACTION_MEDIA_CHECKING);  
        filter.addAction(Intent.ACTION_MEDIA_EJECT);  
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);  
        filter.addDataScheme("file"); 
        mContext.registerReceiver(mReceiver, filter);
        
        IntentFilter filterNetwrok = new IntentFilter();
        filterNetwrok.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filterNetwrok.addAction(Configs.BroadCastConstant.ACTION_NEW_USER_MSG);
        filterNetwrok.addAction(Configs.BroadCastConstant.ACTION_NEW_USER_NO_MSG);
        mContext.registerReceiver(networkReceiver, filterNetwrok);   
        
	}

	private void initEthConnect() {
		int status = getNetworkConnect(mContext);
		setNetworkConnect(status);
	}
	
	private void setNetworkConnect(int status){
		if(status == 1){
			mWifiImg.setBackgroundResource(R.drawable.icon_wifi);
			mWifiImg.setVisibility(View.VISIBLE);
			mLanImg.setBackgroundResource(R.drawable.transport);
			mLanImg.setVisibility(View.GONE);
		}else if(status == 2){
			mWifiImg.setBackgroundResource(R.drawable.transport);
			mWifiImg.setVisibility(View.GONE);
			mLanImg.setBackgroundResource(R.drawable.icon_eth_focus);
			mLanImg.setVisibility(View.VISIBLE);
		} else{
			mWifiImg.setBackgroundResource(R.drawable.transport);
			mLanImg.setBackgroundResource(R.drawable.transport);
			mLanImg.setVisibility(View.GONE);
			mWifiImg.setVisibility(View.GONE);
		} 
	}
	
	public int getNetworkConnect(Context context){
		NetworkInfo networkinfo = ((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo();
	    if(networkinfo != null){
	        String s = networkinfo.getTypeName();
	        if(s.equalsIgnoreCase("wifi"))
	           return 1;
	        else
	        if(s.equalsIgnoreCase("ETH")|| s.equalsIgnoreCase("ETHERNET"))
	            return 2;
	    }
		return 0;
    }
	
	
	private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            	setNetworkConnect(getNetworkConnect(mContext));
            }
            
            if(action.equals(Configs.BroadCastConstant.ACTION_NEW_USER_MSG)){
            	mMsgImg.setBackgroundResource(R.drawable.icon_msg_focus);
            	mMsgImg.setVisibility(View.VISIBLE);
            } else if(action.equals(Configs.BroadCastConstant.ACTION_NEW_USER_NO_MSG)){
            	mMsgImg.setBackgroundResource(R.drawable.transport);
            	mMsgImg.setVisibility(View.GONE);
            }
		};
	};
	
	 private BroadcastReceiver mReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            String action = intent.getAction();
	            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
	            	setNetworkConnect(getNetworkConnect(mContext));
	            }
	            
	            if (action.equals(Intent.ACTION_MEDIA_MOUNTED) || 
	            		action.equals(Intent.ACTION_MEDIA_CHECKING)
	            		|| action.equals(Intent.ACTION_MEDIA_REMOVED)) {
	            	setSdcardStatus();
                }
	            
	        }
	    };
	    
	    private void setSdcardStatus(){
	    	boolean has = CheckSdcard.isHasExtendStorage(mContext);
        	int visibility = has ? View.VISIBLE : View.GONE;
        	mUsbImg.setVisibility(visibility);
	    }
	    
	    public void unRegReceiver(){
	    	mContext.unregisterReceiver(mReceiver);
	    	mContext.unregisterReceiver(networkReceiver);
	    }
	    
}