package com.bestbaan.moonbox.view;

import com.moon.android.iptv.MainActivity;


import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.LinearLayout;

@SuppressWarnings("deprecation")
public class ShopView extends LinearLayout{
    public Context mContext;
	public ShopView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext=context;
		init();
		// TODO Auto-generated constructor stub
	}

	private void init() {
		// TODO Auto-generated method stub
		  // I//ntent i = new Intent();  
		   
         //  i.setClass(mContext, ShopActivity.class);  

         //  mContext.startActivity(i);  
	}

	public ShopView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		// TODO Auto-generated constructor stub
	}

	public ShopView(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}


         
}
