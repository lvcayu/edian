package com.edian.www.app;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import com.edian.www.R;
import com.edian.www.base.BaseApp;
import com.edian.www.base.BaseAuth;
import com.edian.www.base.BaseMessage;
import com.edian.www.base.BaseService;
import com.edian.www.base.C;
import com.edian.www.model.User;
import com.edian.www.service.NoticeService;
import com.edian.www.util.ExitApplication;
import com.edian.www.util.UpdateManager;

public class AppLogin extends BaseApp{
	private final String TAG= "BaseApp";
	private EditText mEditName;
	private EditText mEditPass;
	private CheckBox mCheckBox;
	private SharedPreferences settings;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		settings = getPreferences(Context.MODE_PRIVATE);
		//检查软件更新
		UpdateManager manager = new UpdateManager(this,settings);
		manager.checkUpdate();
		//check if logined
		if(BaseAuth.isLogin())
			this.forward(EdianIndex.class);
		
		this.setContentView(R.layout.app_login);
		mEditName = (EditText)this.findViewById(R.id.inputbox_username);
		mEditPass = (EditText)this.findViewById(R.id.signin_inputbox_password);
		mCheckBox = (CheckBox)this.findViewById(R.id.ifsavepwd_checkbox);
		
		if(settings.getBoolean("remember", false)){
			mCheckBox.setChecked(true);
			mEditName.setText(settings.getString("username", ""));
			mEditPass.setText(settings.getString("password", ""));
		}
		mCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				SharedPreferences.Editor editer = settings.edit();
				if(mCheckBox.isChecked()){
					editer.putBoolean("remember", true);
					editer.putString("username", mEditName.getText().toString());
					editer.putString("password", mEditPass.getText().toString());
				}else{
					editer.putBoolean("remember", false);
					editer.putString("username", "");
					editer.putString("password", "");
				}
				editer.commit();
			}
		});
		
		/*
		OnClickListener mOnClickListener = new OnClickListener(){
			@Override
			public void onClick(View v){
				switch (v.getId()){
				case R.id.signinbtn:
					doTaskLogin();
					break;
				case R.id.signup_tv_btn:
					
				}
			}
		};
		this.findViewById(R.id.signinbtn).setOnClickListener(mOnClickListener);
		*/
	}
	
	public void doTaskLogin(View v){
		if(mEditName.length()>0 && mEditPass.length()>0){
			HashMap<String,String> urlParams = new HashMap<String,String>();
			urlParams.put("name", mEditName.getText().toString());
			urlParams.put("pass", mEditPass.getText().toString());
			try{
				Log.i(TAG, "[AppLogin]taskArg1:"+urlParams.get("name")+"|taskArg2:"+urlParams.get("pass"));
				this.doTaskAsync(C.task.login, C.api.login, urlParams);
			} catch(Exception e){
				e.printStackTrace();
			}
		}else{
			toast(C.err.emptysignupbox);
		}
	}
	
	public void doSignup(View v){
		this.forward(AppReg.class);
	}
	
	
	//////////////////////////
	// async task callback method
	@Override
	public void onTaskComplete(int taskId,BaseMessage message){
		super.onTaskComplete(taskId, message);
		switch (taskId){
		case C.task.login:
			User user = null;
			try{
				String ret = message.getCode();
				if(ret.equals(C.errcode.USER_BANED)){
					toast(C.err.userbaned);
					break;
				}
				if(ret.equals(C.errcode.NOUSER)){
					toast("没有这个用户");
					break;
				}
				user = (User)message.getResult("User");
				if(user.getNickname()!=null){
					BaseAuth.setUser(user);
					BaseAuth.setLogin(true);
				}else{
					BaseAuth.setUser(user);//set sid
					BaseAuth.setLogin(false);
					toast(this.getString(R.string.msg_login_fail));
				}
			}catch(Exception e){
				e.printStackTrace();
				toast(e.getMessage());
			}
			if(BaseAuth.isLogin()){
				BaseService.start(this, NoticeService.class);
				forward(EdianIndex.class);
			}
			break;
		}
	}
	@Override
	public void onNetworkError (int taskId) {
		super.onNetworkError(taskId);
	}
	
	@Override
	public boolean onKeyDown(int keycode,KeyEvent event) {
		if (keycode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){
			ExitApplication.getInstance().exit();
		}
		return super.onKeyDown(keycode, event);
	}
	
}
