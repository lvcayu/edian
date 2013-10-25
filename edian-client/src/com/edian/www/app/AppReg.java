package com.edian.www.app;

import java.util.HashMap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.edian.www.R;
import com.edian.www.base.BaseApp;
import com.edian.www.base.BaseMessage;
import com.edian.www.base.C;
import com.edian.www.base.C.con;
import com.edian.www.base.C.err;
import com.edian.www.base.C.errcode;
import com.edian.www.util.ExitApplication;

public class AppReg extends BaseApp {
	private EditText et_username;
	private EditText et_password;
	private EditText et_passconfirm;
	private String	m_username;
	private String	m_password;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		this.setContentView(R.layout.app_signup_1);
		et_username = (EditText) this.findViewById(R.id.inputbox_signup_username);
		et_password = (EditText) this.findViewById(R.id.signup_inputbox_password);
		et_passconfirm = (EditText) this.findViewById(R.id.signup_inputbox_password_confirm);
	}
	
	public void onNextbtnClick(View v){
		String username = et_username.getText().toString().trim();
		String password = et_password.getText().toString().trim();
		String passconfirm = et_passconfirm.getText().toString().trim();
		if(username.length()==0
			|| password.length()==0
			|| passconfirm.length()==0) {
			toast(err.emptysignupbox);
			return;
		}
		if(   !password.equals( passconfirm.toString() )   ){
			toast(err.pwdcheck);
			return;
		}
		m_username = username;
		m_password = password;
		doTaskCheckname();
	}
	
	private void doTaskCheckname(){
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("nickname", m_username);
		try{
			Log.i("DEBUG","Begin checkname task");
			this.doTaskAsync(C.task.checkname, C.api.checkname, params);
		}catch(Exception e){
			e.printStackTrace();
			//Log.i("exception",e.getMessage());
		}
	}
	
	@Override
	public void onTaskComplete(int taskId,BaseMessage message){
		Log.w("DEBUG","checkname taskcomplete");
		super.onTaskComplete(taskId, message);
		switch(taskId){
		case C.task.checkname:
			String retcode = message.getCode();
			Log.v("ret", "retcode:"+retcode);
			if(retcode.equalsIgnoreCase(errcode.NICKNAME_EXIST)){
				toast(err.userexist);
				return;
			}
			if(retcode.equalsIgnoreCase(errcode.SUCCESS)){
				//把username,password传递到注册第二步的Activity
				Bundle nandp = new Bundle();
				nandp.putString("username", m_username);
				nandp.putString("password", m_password);
				overlay(AppReg2.class,nandp);
			}
		}
	}
	
	@Override
	public void onNetworkError (int taskId) {
		super.onNetworkError(taskId);
	}
	
	@Override
	public boolean onKeyDown(int keycode,KeyEvent event) {
		if (keycode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){
			doFinish();
		}
		return super.onKeyDown(keycode, event);
	}
}
