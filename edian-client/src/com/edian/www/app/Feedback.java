package com.edian.www.app;

import java.util.HashMap;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.edian.www.R;
import com.edian.www.auth.AuthApp;
import com.edian.www.base.BaseAuth;
import com.edian.www.base.BaseMessage;
import com.edian.www.base.C;
import com.edian.www.util.ExitApplication;

public class Feedback extends AuthApp {
	private EditText content = null;
	private String ms_content = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        ExitApplication.getInstance().addActivity(this);
        
        this.setContentView(R.layout.feedback);
        content = (EditText)findViewById(R.id.feedback_content);
	}
	
	@Override
	public void onTaskComplete(int taskId,BaseMessage message){
		String retcode = message.getCode();
		switch(taskId){
		case C.task.feedbackcreate:
			if(retcode.equals(C.errcode.SUCCESS)){
				this.toast("我们已收到反馈，非常感谢您对我们的支持!");
				this.forward(SquareIndex.class);
			}else{
				this.toast("由于网络错误发送失败，请尝试重新发送");
			}
			break;
		}
	}
	
	public void cancel(View v){
		this.forward(EdianIndex.class);
	}
	
	public void sendfeedback(View v){
		ms_content = content.getText().toString();
		if(ms_content!=null && ms_content.length()>0){
			HashMap<String,String> para = new HashMap<String,String>(2);
			para.put("uid", BaseAuth.getUser().getUid());
			para.put("msg", ms_content);
			this.doTaskAsync(C.task.feedbackcreate, C.api.feedbackcreate, para);
		}else{
			this.toast("您好像忘了填写想对我们说的话呢～");
		}
	}
}
