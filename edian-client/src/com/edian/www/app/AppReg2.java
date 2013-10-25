package com.edian.www.app;

import java.util.HashMap;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.edian.www.R;
import com.edian.www.base.BaseApp;
import com.edian.www.base.BaseAuth;
import com.edian.www.base.BaseMessage;
import com.edian.www.base.C;
import com.edian.www.base.C.api;
import com.edian.www.base.C.err;
import com.edian.www.base.C.errcode;
import com.edian.www.base.C.task;
import com.edian.www.model.User;
import com.edian.www.util.ChoiceOnClickListener;

public class AppReg2 extends BaseApp {
	private final int PROVINCE_DIALOG=1;
	private final int CITY_DIALOG=2;
	private EditText med_qq;
	private TextView mtv_city;
	private int mi_city = -1;
	private String ms_sex = "null";
	private String ms_username;
	private String ms_password;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.app_signup_2);
		Intent intent = this.getIntent();
		Bundle data = intent.getExtras();
		ms_username = data.getString("username");
		ms_password = data.getString("password");
		Log.i("name&pass","username:"+ms_username+"password:"+ms_password);

		if(ms_username.isEmpty() || ms_password.isEmpty()){
			toast(C.err.nameorpassempty);
		}else {
			med_qq = (EditText)this.findViewById(R.id.input_signup_qq);
			RadioGroup rg = (RadioGroup)this.findViewById(R.id.radioGroup_signupsex);
			rg.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(RadioGroup arg0,int arg1){
					if(arg1==R.id.radio_male)
						ms_sex = C.con.male;//男
					else if(arg1==R.id.radio_female)
						ms_sex = C.con.female;//女
				}
			});
			
			mtv_city = (TextView)this.findViewById(R.id.signup_input_city);
			mtv_city.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					showDialog(PROVINCE_DIALOG);
				}
			});
		}
	}
	
	public void onCommitClick(View v) {
		String sqq = med_qq.getText().toString();
		if(sqq.length()<=0 || ms_sex.equalsIgnoreCase("null") || mi_city == -1){
			toast(err.emptysignupbox);
		}else {
			HashMap<String,String> para = new HashMap<String,String>();
			para.put("nickname", ms_username);
			para.put("pwd", ms_password);
			para.put("qq", sqq);
			para.put("sex", ms_sex);
			para.put("city", Integer.toString(mi_city));
			try{
			this.doTaskAsync(task.usercreate, api.usercreate, para);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onTaskComplete(int taskId,BaseMessage message){
		Log.i("signup", "userCreate task complete");
		switch(taskId){
		case task.usercreate:
			try{
			String retcode = message.getCode();
			if(retcode.equalsIgnoreCase(errcode.SUCCESS)){
				User user = (User)message.getResult("User");
				BaseAuth.setUser(user);
				BaseAuth.setLogin(true);
				this.forward(EdianIndex.class);
			}else{
				toast("注册失败 T_T");
			}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onNetworkError(int taskId){
		super.onNetworkError(taskId);
	}
	
	@Override
	protected Dialog onCreateDialog(int id){
		Dialog province = null;
		switch(id){
		case PROVINCE_DIALOG:
			Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.provincedialogtitle);
			final ChoiceOnClickListener cocl = new ChoiceOnClickListener();
			builder.setSingleChoiceItems(R.array.city, 0, cocl);
			DialogInterface.OnClickListener btnlistener = new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mi_city = cocl.getWhich();
					String cityStr = getResources().getStringArray(R.array.city)[mi_city];  
					mtv_city.setText(cityStr);
				}
			};
			builder.setPositiveButton("确定", btnlistener);
			province = builder.create();
		}
		return province;
	}
	
	@Override
	public boolean onKeyDown(int keycode,KeyEvent event) {
		if (keycode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){
			doFinish();
		}
		return super.onKeyDown(keycode, event);
	}	
	

}
