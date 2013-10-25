package com.edian.www.app;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.edian.www.R;
import com.edian.www.auth.AuthApp;
import com.edian.www.base.BaseMessage;
import com.edian.www.base.C;
import com.edian.www.model.Share;
import com.edian.www.util.ExitApplication;

public class ShareContent extends AuthApp {
	private TextView title = null;
	private TextView author = null;
	private TextView createtime = null;
	private TextView content = null;
	private String shareId = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ExitApplication.getInstance().addActivity(this);
        this.setContentView(R.layout.share_content);
        title = (TextView)this.findViewById(R.id.sc_title);
        author = (TextView)this.findViewById(R.id.sc_author);
        createtime = (TextView)this.findViewById(R.id.sc_pubtime);
        content = (TextView)this.findViewById(R.id.sc_content);
        
		Intent intent = this.getIntent();
		Bundle data = intent.getExtras();
		shareId = data.getString("shareid");
		if(shareId!=null){
			HashMap<String,String> para = new HashMap<String,String>();
			para.put("id", shareId);
			this.doTaskAsync(C.task.shareview, C.api.shareview, para);
		}
	}
	
	@Override
	public void onStart(){
		super.onStart();
	}
	
	@Override
	public void onTaskComplete(int taskId,BaseMessage message){
		switch (taskId){
		case C.task.shareview:
			try{
			String retcode = message.getCode();
			if(retcode.equals(C.errcode.SUCCESS)){
				Share share = (Share)message.getResult("Share");
				title.setText(share.getTitle());
				author.setText(share.getAuthor());
				createtime.setText(share.getCreatetime());
				content.setText(share.getContent());
			}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
    @Override
    public void onNetworkError(int taskId){
    	super.onNetworkError(taskId);
    	toast("网络错误，请重试");
    }
    
    public void back(View v){
    	this.forward(ShareIndex.class);
    }
}
