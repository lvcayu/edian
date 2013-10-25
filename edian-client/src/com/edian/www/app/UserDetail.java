/**
 * 
 */
package com.edian.www.app;

import java.util.HashMap;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.edian.www.util.AppCache;
import com.edian.www.base.BaseApp;
import com.edian.www.base.BaseAuth;
import com.edian.www.base.BaseHandler;
import com.edian.www.base.BaseMessage;
import com.edian.www.base.BaseTask;
import com.edian.www.R;
import com.edian.www.auth.AuthApp;
import com.edian.www.base.C;
import com.edian.www.model.User;
import com.edian.www.util.ExitApplication;

/**
 * @author walker
 *
 */
public class UserDetail extends AuthApp {
	private final String TAG = "UserDetail";
	private String ms_Uid = "";
	private ImageView fingure;
	private String faceurl = "";
	private String ms_nickname = "";
	private String ms_sex = "";
	private String ms_age = "";
	private String ms_ismarried = "";
	private String ms_createtime = "";
	private String ms_attitude = "";
	private String ms_heartsay = "";
	private String ms_experience = "";
	private String ms_qq = "";
	private String ms_tel = "";
	private TextView tv_nickname;
	private ImageView iv_sex;
	private TextView tv_sex;
	private TextView tv_age;
	private TextView tv_ismarried;
	private TextView tv_createtime;
	private TextView tv_attitude;
	private TextView tv_heartsay;
	private TextView tv_experience;
	private TextView tv_qq;
	private TextView tv_tel;
	@Override
	public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ExitApplication.getInstance().addActivity(this);
        setContentView(R.layout.user_detail);
        this.setHandler(new IndexHandler(this));
        tv_nickname = (TextView)this.findViewById(R.id.detail_nickname);
        iv_sex = (ImageView)this.findViewById(R.id.detail_sexlogo);
        tv_sex = (TextView)this.findViewById(R.id.detail_sex);
        tv_age = (TextView)this.findViewById(R.id.detail_age);
        tv_ismarried = (TextView)this.findViewById(R.id.detail_ismarried);
        tv_createtime = (TextView)this.findViewById(R.id.detail_signuptime);
        tv_attitude = (TextView)this.findViewById(R.id.detail_attitude);
        tv_heartsay = (TextView)this.findViewById(R.id.detail_heartsay);
        tv_experience = (TextView)this.findViewById(R.id.detail_experience);
        tv_qq = (TextView)this.findViewById(R.id.detail_qq);
        tv_tel = (TextView)this.findViewById(R.id.detail_tel);
        
		Intent intent = this.getIntent();
		Bundle data = intent.getExtras();
        ms_Uid = data.getString("uid");
        HashMap<String,String> para = new HashMap<String,String>();
        para.put("uid", ms_Uid);
        para.put("type", "0");
        doTaskAsync(C.task.userview,C.api.userview,para);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		
	}
	
	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		super.onTaskComplete(taskId, message);
		Log.i(TAG, "get userview complete");
		String ret = message.getCode();
		switch (taskId){
		case C.task.userview:
			try{
				if(ret.equals(C.errcode.SUCCESS)){
					User ta = (User)message.getResult("User");
					faceurl = ta.getFaceurl();
					loadImage(faceurl);
					ms_nickname = ta.getNickname();
					ms_sex = ta.getSex();
					ms_age = ta.getAge();
					ms_ismarried = ta.getIsmarried();
					ms_createtime = ta.getRegtime();
					ms_attitude = ta.getAttitude();
					ms_heartsay = ta.getHeartsay();
					ms_experience = ta.getExperience();
					ms_qq = ta.getQq();
					ms_tel = ta.getTel();
					tv_nickname.setText(ms_nickname);
					if(ms_sex.equals(C.con.female)){
						iv_sex.setImageResource(R.drawable.girl_sign);
						tv_sex.setText(C.con.female_zh_cn);
					}else{
						iv_sex.setImageResource(R.drawable.boy_sign);
						tv_sex.setText(C.con.male_zh_cn);
					}
					if(ms_age.equals(C.con.married)){
						tv_ismarried.setText(C.con.married_zh_cn);
					}else{
						tv_ismarried.setText(C.con.notmarried_zh_cn);
					}
					tv_age.setText(ms_age);
					tv_createtime.setText(ms_createtime);
			        //态度
			        String[] attitudes = getResources().getStringArray(R.array.attitude);
			        ms_attitude = user.getAttitude().equals("")?user.getAttitude():attitudes[Integer.parseInt(user.getAttitude())];
			        tv_attitude.setText(ms_attitude);
			        //经历
			        String[] exps = getResources().getStringArray(R.array.exp);
			        ms_experience = user.getExperience().equals("")?user.getExperience():exps[Integer.parseInt(user.getExperience())];
			        tv_experience.setText(ms_experience);
					tv_heartsay.setText(ms_heartsay);
					tv_qq.setText(ms_qq);
					tv_tel.setText(ms_tel);
				}else if(ret.equals(C.errcode.USER_BANED)){
					toast(C.err.userbaned);
				}else{
					toast(C.err.getuserfailed);
				}
			}catch (Exception e){
				e.printStackTrace();
				//Log.i("JSONERR", e.getMessage());
			}
			break;
		case C.task.inform:
			if(ret.equals(C.errcode.SUCCESS)){
				this.toast("感谢您的举报，我们会尽快处理！");
			}else if(ret.equals(C.errcode.NOUSER)){
				this.toast("没有这个用户，举报失败！");
			}
		}
	}

    @Override
    public void onNetworkError(int taskId){
    	super.onNetworkError(taskId);
    	toast("NetworkError!");
    }
    
	private class IndexHandler extends BaseHandler {
		public IndexHandler(BaseApp app) {
			super(app);
		}
		@Override
		public void handleMessage(Message msg) {
			Log.v(TAG, "handleMessage");
			super.handleMessage(msg);
			try {
				switch (msg.what) {
					case BaseTask.LOAD_IMAGE:
						Bitmap face = AppCache.getImage(faceurl);
						fingure.setImageBitmap(face);
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				//app.toast("=>"+e.getMessage());
			}
		}
	}
	
	public void back(View v){
		Intent intent = new Intent();
		intent.setClass(this, EdianIndex.class);
		startActivity(intent);
		overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
	}
	//举报按钮响应
	public void confirm(View v){
		Log.v(TAG, "inform user:"+this.user.getUid());
		final Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("确认");
		builder.setMessage("怀疑TA是酒托，要举报TA？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				inform();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.create().show();
	}
	//发起举报请求
	public void inform(){
		HashMap<String,String> para = new HashMap<String,String>();
		para.put("informerid", this.user.getUid());
		para.put("informedid", ms_Uid);
		para.put("type", "2");
		this.doTaskAsync(C.task.inform, C.api.inform, para);
	}
}
