/**
 * 
 */
package com.edian.www.app;

import java.util.Date;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.edian.www.util.AppCache;
import com.edian.www.base.BaseApp;
import com.edian.www.base.BaseAuth;
import com.edian.www.base.BaseHandler;
import com.edian.www.base.BaseMessage;
import com.edian.www.base.BaseTask;
import com.edian.www.R;
import com.edian.www.util.ChoiceOnClickListener;
import com.edian.www.auth.AuthApp;
import com.edian.www.base.C;
import com.edian.www.model.User;
import com.edian.www.util.ExitApplication;

/**
 * @author walker
 *
 */
public class UserCenter extends AuthApp {
	private final String TAG = "UserDetail";
	private final int STATURE_DIALOG = 1;
	private final int BODYSHAPE_DIALOG = 2;
	private final int ISMARRIED_DIALOG = 3;
	private final int DEGREE_DIALOG = 4;
	private final int CITY_DIALOG = 5;
	private final int ATTITUDE_DIALOG = 6;
	private final int EXP_DIALOG = 7;
	private String ms_Uid = "";
	private ImageView fingure;
	private String faceurl = "";
	private String ms_nickname = "";
	private int	   i_money = 0;
	private Boolean   b_vip = false;
	private String ms_qq = "";
	private String ms_tel = "";
	private String ms_weight = "";
	private String ms_stature = "";//身高
	private String ms_age = "";
	private String ms_ismarried = "";
	private String ms_bodyshape = "";
	private String ms_degree = "";
	private String ms_occupation = "";
	private String ms_createtime = "";
	private String ms_attitude = "";
	private String ms_heartsay = "";
	private String ms_experience = "";
	private String ms_city = "";
	private TextView tv_nickname;
	private TextView tv_money;
	private ImageView iv_vip;
	private EditText et_qq;
	private EditText et_tel;
	private EditText et_age;
	private EditText et_stature;;
	private EditText et_weight;
	private TextView tv_bodyshape;
	private TextView tv_ismarried;
	private TextView tv_degree;
	private EditText et_occupation;
	private TextView tv_city;
	private TextView tv_attitude;
	private TextView tv_exp;
	private EditText et_heartsay;
	
	public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ExitApplication.getInstance().addActivity(this);
        setContentView(R.layout.user_center);
        this.setHandler(new IndexHandler(this));
        
        tv_nickname = (TextView)this.findViewById(R.id.uc_nickname);
        //tv_money = (TextView)this.findViewById(R.id.uc_money);
        //iv_vip = (ImageView)this.findViewById(R.id.uc_isvip);
        et_qq = (EditText)this.findViewById(R.id.uc_qq_edit);
        et_tel = (EditText)this.findViewById(R.id.uc_tel_edit);
        et_age = (EditText)this.findViewById(R.id.uc_age_edit);
        et_stature = (EditText)this.findViewById(R.id.uc_stature_edit);
        et_weight = (EditText)this.findViewById(R.id.uc_weight_edit);
        tv_bodyshape = (TextView)this.findViewById(R.id.uc_bodyshape_textview);
        tv_bodyshape.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//单击体形时
				showDialog(BODYSHAPE_DIALOG);
			}
		});
        tv_ismarried = (TextView)this.findViewById(R.id.uc_ismarried_textview);
        tv_ismarried.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 单击婚姻状况时
				showDialog(ISMARRIED_DIALOG);
			}
		});
        tv_degree = (TextView)this.findViewById(R.id.uc_degree_textview);
        tv_degree.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 单击学历时
				showDialog(DEGREE_DIALOG);
			}
		});
        et_occupation = (EditText)this.findViewById(R.id.uc_occupation_edit);
        tv_city = (TextView)this.findViewById(R.id.uc_city_textview);
        tv_city.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 单击城市时
				showDialog(CITY_DIALOG);
			}
		});
        tv_attitude = (TextView)this.findViewById(R.id.uc_attitude_content);
        tv_attitude.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 单击态度时
				showDialog(ATTITUDE_DIALOG);
			}
		});
        tv_exp = (TextView)this.findViewById(R.id.uc_exp_content);
        tv_exp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 单击经历时
				showDialog(EXP_DIALOG);
			}
		});
        et_heartsay = (EditText)this.findViewById(R.id.uc_heartsay_edit);

        HashMap<String,String> para = new HashMap<String,String>();
        para.put("uid", BaseAuth.getUser().getUid());
        para.put("type", "1");
        this.doTaskAsync(C.task.userview, C.api.userview, para);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		//View v = (Button)this.findViewById(R.id.uc_done_btn);
		//this.save(v);
	}
	
	public void back(View v){
		Intent intent = new Intent();
		intent.setClass(this, EdianIndex.class);
		startActivity(intent);
		overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
	}
	
	public void save(View v){
		ms_qq = et_qq.getText().toString();
		ms_tel = et_tel.getText().toString();
		ms_age = et_age.getText().toString();
		ms_stature = et_stature.getText().toString();
		ms_weight = et_weight.getText().toString();
		ms_occupation = et_occupation.getText().toString();
		ms_heartsay = et_heartsay.getText().toString();
        ms_Uid = AuthApp.user.getUid();
        HashMap<String,String> para = new HashMap<String,String>();
        para.put("uid", ms_Uid);
        para.put("qq", ms_qq);
        para.put("tel", ms_tel);
        para.put("age", ms_age);
        para.put("stature", ms_stature);
        para.put("weight", ms_weight);
        para.put("bodyshape", ms_bodyshape);
        para.put("ismarried", ms_ismarried);
        para.put("degree", ms_degree);
        para.put("attitude", ms_attitude);
        para.put("experience", ms_experience);
        para.put("ocupation", ms_occupation);
        para.put("heartsay", ms_heartsay);
        para.put("city", ms_city);
        doTaskAsync(C.task.userupdate,C.api.userupdate,para);
	}
	
	public void logout(View v){
		doTaskAsync(C.task.logout,C.api.logout);
	}
	
	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		super.onTaskComplete(taskId, message);
		Log.i(TAG, "get userview complete");
		switch (taskId){
		case C.task.userview:
			String retcode = message.getCode();
			if(retcode.equals(C.errcode.SUCCESS)){
				try {
					User tuser = (User)message.getResult("User");
					if(tuser!=null){
						tuser.setSid(user.getSid());
						BaseAuth.setUser(tuser);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			User user = BaseAuth.getUser();
	        //loadImage(user.getFaceurl());
	        tv_nickname.setText(user.getNickname());
	        //tv_money.setText(user.getMoney());
	        /*if(!user.getVipendtime().isEmpty()){
		        long endtime = Date.parse(user.getVipendtime());
		        long now = new Date().getTime();
		        if(endtime<now){
		        	iv_vip.setImageResource(R.drawable.vip_none);
		        }else{
		        	iv_vip.setImageResource(R.drawable.vip);
		        }
	        }else{
	        	iv_vip.setImageResource(R.drawable.vip_none);
	        }*/
	        et_qq.setText(user.getQq());
	        et_tel.setText(user.getTel());
	        et_age.setText(user.getAge());
	        et_stature.setText(user.getStature());
	        et_weight.setText(user.getWeight());
	        //体形
	        String[] shapes = getResources().getStringArray(R.array.bodyshape);
	        String sbodyshape = user.getBodyshape().equals("")?user.getBodyshape():shapes[Integer.parseInt(user.getBodyshape())];
	        tv_bodyshape.setText(sbodyshape);
	        //婚否
	        String[] ismarrieds = getResources().getStringArray(R.array.ismarried);
	        String sismarried = user.getIsmarried().equals("")?user.getIsmarried():ismarrieds[Integer.parseInt(user.getIsmarried())];
	        tv_ismarried.setText(sismarried);
	        //学历
	        String[] degrees = getResources().getStringArray(R.array.degree);
	        String sdegree = user.getDegree().equals("")?user.getDegree():degrees[Integer.parseInt(user.getDegree())];
	        tv_degree.setText(sdegree);
	        
	        et_occupation.setText(user.getOcupation());
	        tv_city.setText(this.getCity(Integer.parseInt((user.getCity()))));
	        //态度
	        String[] attitudes = getResources().getStringArray(R.array.attitude);
	        String sattitude = user.getAttitude().equals("")?user.getAttitude():attitudes[Integer.parseInt(user.getAttitude())];
	        tv_attitude.setText(sattitude);
	        //经历
	        String[] exps = getResources().getStringArray(R.array.exp);
	        String sexperience = user.getExperience().equals("")?user.getExperience():exps[Integer.parseInt(user.getExperience())];
	        tv_exp.setText(sexperience);
	        //心声
	        et_heartsay.setText(user.getHeartsay());
	        break;
		case C.task.userupdate:
			try{
				String ret = message.getCode();
				if(ret.equals(C.errcode.SUCCESS)){
					toast(C.err.updatesuccess);
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
		case C.task.logout:
			String ret = message.getCode();
			if(ret.equals(C.errcode.SUCCESS)){
				BaseAuth.setLogin(false);
				this.forward(AppLogin.class);
			}
		}
	}

    @Override
    public void onNetworkError(int taskId){
    	super.onNetworkError(taskId);
    	toast("NetworkError!");
    }
    
	@Override
	protected Dialog onCreateDialog(int id){
		Dialog tdlg = null;
		Builder builder = null;
		final ChoiceOnClickListener cocl = new ChoiceOnClickListener();
		switch(id){
		case BODYSHAPE_DIALOG:
			builder = new AlertDialog.Builder(this);
			builder.setTitle("选择体形");
			builder.setSingleChoiceItems(R.array.bodyshape, 0, cocl);
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ms_bodyshape = Integer.toString(cocl.getWhich());
					String[] shapes = getResources().getStringArray(R.array.bodyshape);
					tv_bodyshape.setText(shapes[cocl.getWhich()]);
				}
			});
			tdlg = builder.create();
			break;
		case ISMARRIED_DIALOG:
			builder = new AlertDialog.Builder(this);
			builder.setTitle("是否结婚");
			builder.setSingleChoiceItems(R.array.ismarried, 0, cocl);
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ms_ismarried = Integer.toString(cocl.getWhich());
					String[] ismarried = getResources().getStringArray(R.array.ismarried);
					tv_ismarried.setText(ismarried[cocl.getWhich()]);
				}
			});
			tdlg = builder.create();
			break;
		case DEGREE_DIALOG:
			builder = new AlertDialog.Builder(this);
			builder.setTitle("选择学历");
			builder.setSingleChoiceItems(R.array.degree, 0, cocl);
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ms_degree = Integer.toString(cocl.getWhich());
					String[] degree = getResources().getStringArray(R.array.degree);
					tv_degree.setText(degree[cocl.getWhich()]);
				}
			});
			tdlg = builder.create();
			break;
		case CITY_DIALOG:
			builder = new AlertDialog.Builder(this);
			builder.setTitle("选择城市");
			builder.setSingleChoiceItems(R.array.city, 0, cocl);
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ms_city = Integer.toString(cocl.getWhich());
					String[] citys = getResources().getStringArray(R.array.city);
					tv_city.setText(citys[cocl.getWhich()]);
				}
			});
			tdlg = builder.create();
			break;
		case ATTITUDE_DIALOG:
			builder = new AlertDialog.Builder(this);
			builder.setTitle("选择你的态度");
			builder.setSingleChoiceItems(R.array.attitude, 0, cocl);
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ms_attitude = Integer.toString(cocl.getWhich());
					String[] attitudes = getResources().getStringArray(R.array.attitude);
					tv_attitude.setText(attitudes[cocl.getWhich()]);
				}
			});
			tdlg = builder.create();
			break;
		case EXP_DIALOG:
			builder = new AlertDialog.Builder(this);
			builder.setTitle("你的经历是怎样的？");
			builder.setSingleChoiceItems(R.array.exp, 0, cocl);
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ms_experience = Integer.toString(cocl.getWhich());
					String[] exps = getResources().getStringArray(R.array.exp);
					tv_exp.setText(exps[cocl.getWhich()]);
				}
			});
			tdlg = builder.create();
			break;
		}
		return tdlg;
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
				app.toast(e.getMessage());
			}
		}
	}
}
