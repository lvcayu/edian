package com.edian.www.app;

import java.util.HashMap;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edian.www.R;
import com.edian.www.auth.AuthApp;
import com.edian.www.base.BaseMessage;
import com.edian.www.base.C;
import com.edian.www.model.Share;
import com.edian.www.model.Square;
import com.edian.www.util.ExitApplication;

public class Publish extends AuthApp {
	private static final String TAG = "Publish";
	private String ms_pubtype;
	private TextView pub_header = null;
	private LinearLayout layout_main = null;
	private EditText title = null;
	private EditText content = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        ExitApplication.getInstance().addActivity(this);
        setContentView(R.layout.publish);
        pub_header = (TextView)findViewById(R.id.pub_header);
        layout_main = (LinearLayout)findViewById(R.id.pub_main);
       /* this.setContentView(R.layout.publish);
        layout_main = new LinearLayout(this);*/
        //取类型参数
		Intent intent = this.getIntent();
		Bundle data = intent.getExtras();
		ms_pubtype = data.getString("pubtype");
		if(ms_pubtype.equals(C.intent.action.PUBSQUARE)){
			Log.v(TAG, "is PUBSQUARE!");
			pub_header.setText("寻找好友");
			//定义说明标题
			LinearLayout titlebox = new LinearLayout(this);
			LinearLayout.LayoutParams tlp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			tlp.setMargins(3, 3, 3, 0);
			titlebox.setLayoutParams(tlp);
			//titlebox.setBackgroundResource(R.drawable.pub_title);
			TextView desc = new TextView(this);
			desc.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			desc.setText("信息发布(例如：我在xx城市，诚心寻找钟情钟意的男/女士):");
			desc.setTextSize(16);
			desc.setTextColor(0xFFFFFFFF);
			desc.setBackgroundColor(0x00ffffff);
			//desc.setPadding(3, 3, 3, 3);
			titlebox.addView(desc);
			//定义内容输入框
			LinearLayout conbox = new LinearLayout(this);
			LinearLayout.LayoutParams clp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,320);
			clp.setMargins(6, 12, 6, 6);
			conbox.setLayoutParams(clp);
			clp.gravity = Gravity.TOP;
			conbox.setBackgroundResource(R.drawable.pub_content);
			
			content = new EditText(this);
			content.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
			content.setHint("在这里输入你想说的");
			content.setTextSize(14);
			content.setTextColor(0xFF0F0E0E);
			content.setBackgroundColor(0x00FFFFFF);
			content.setGravity(Gravity.TOP);
			conbox.addView(content);
			Log.v(TAG, "after PUBSQUARE! layout_main:"+layout_main);
			layout_main.addView(titlebox);
			layout_main.addView(conbox);
			
		}else if(ms_pubtype.equals(C.intent.action.PUBSHARE)){
			pub_header.setText("分享成功经验");
			//标题
			LinearLayout titlebox = new LinearLayout(this);
			LinearLayout.LayoutParams tlp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			tlp.setMargins(3, 3, 3, 0);
			tlp.gravity = Gravity.TOP;
			titlebox.setOrientation(LinearLayout.VERTICAL);
			titlebox.setLayoutParams(tlp);
			TextView desc = new TextView(this);
			desc.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			desc.setTextSize(12);
			desc.setTextColor(0xFFFFFFFF);
			desc.setText("标题:");
			desc.setBackgroundColor(0x00ffffff);
			title = new EditText(this);
			title.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			title.setTextSize(14);
			title.setTextColor(0xFF0F0E0E);
			title.setBackgroundColor(0x00FDFDFD);
			title.setBackgroundResource(R.drawable.pub_title);
			title.setHint("在这里输入标题");
			titlebox.addView(desc);
			titlebox.addView(title);
			//内容
			LinearLayout contentbox = new LinearLayout(this);
			LinearLayout.LayoutParams clp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			clp.setMargins(3, 3, 3, 3);
			clp.gravity = Gravity.TOP;
			contentbox.setOrientation(LinearLayout.VERTICAL);
			contentbox.setLayoutParams(tlp);
			TextView cdesc = new TextView(this);
			cdesc.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			cdesc.setTextSize(12);
			cdesc.setTextColor(0xFFFFFFFF);
			cdesc.setText("要分享的内容:");
			cdesc.setBackgroundColor(0x00ffffff);
			//内容正文外框
			LinearLayout coninnerbox = new LinearLayout(this);
			LinearLayout.LayoutParams cilp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			coninnerbox.setLayoutParams(cilp);
			coninnerbox.setPadding(2, 2, 2, 2);
			coninnerbox.setBackgroundResource(R.drawable.pub_content);
			content = new EditText(this);
			content.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,320));
			content.setTextSize(12);
			content.setTextColor(0xFF0F0E0E);
			content.setBackgroundColor(0x00FDFDFD);
			content.setGravity(Gravity.TOP);
			content.setHint("在这里输入你要分享的内容");
			coninnerbox.addView(content);
			contentbox.addView(cdesc);
			contentbox.addView(coninnerbox);
			
			layout_main.addView(titlebox);
			layout_main.addView(contentbox);
		}
	}
	
	@Override
	public void onStart(){
		super.onStart();
	}
	
	@Override
	public void onTaskComplete(int taskId,BaseMessage message){
		Log.v(TAG, "pub task completed!");
		String retcode = message.getCode();
		switch(taskId){
		case C.task.pubsquare:
			if(retcode.equals(C.errcode.SUCCESS)){
				this.toast("发表成功！");
				this.forward(SquareIndex.class);
			}else{
				this.toast("发表失败，请尝试重新发送");
			}
			break;
		case C.task.pubshare:
			if(retcode.equals(C.errcode.SUCCESS)){
				this.toast("发表分享成功！");
				this.forward(ShareIndex.class);
			}else{
				this.toast("分享失败，请尝试重新发送！");
			}
		}
	}
	
	public void cancel(View v){
		if(ms_pubtype.equals(C.intent.action.PUBSQUARE))
			this.forward(SquareIndex.class);
		else if(ms_pubtype.equals(C.intent.action.PUBSHARE))
			this.forward(ShareIndex.class);
	}
	
	public void publish(View v){
		if(ms_pubtype.equals(C.intent.action.PUBSQUARE)){
			String csquare = content.getText().toString();
			if(csquare!=null && csquare.length()>0){
				HashMap<String,String> para = new HashMap<String,String>();
				para.put("content", csquare);
				doTaskAsync(C.task.pubsquare, C.api.pubsquare, para);
			}else{
				this.toast("您好像忘了填写内容～");
			}
		}else if(ms_pubtype.equals(C.intent.action.PUBSHARE)){
			String tshare = title.getText().toString();
			if(tshare==null || tshare.length()==0){
				this.toast("请把标题填上吧");
				return;
			}
			String tsquare = content.getText().toString();
			if(tsquare==null || tsquare.length()==0){
				this.toast("请把想要和大家分享的经验都写上吧");
				return;
			}
			HashMap<String,String> para = new HashMap<String,String>();
			para.put("title", tshare);
			para.put("content", tsquare);
			doTaskAsync(C.task.pubsquare, C.api.pubsquare, para);
		}
	}
}
