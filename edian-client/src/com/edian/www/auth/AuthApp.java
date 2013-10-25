package com.edian.www.auth;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.edian.www.R;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.edian.www.app.AppLogin;
import com.edian.www.app.EdianIndex;
import com.edian.www.app.Feedback;
import com.edian.www.app.ShareIndex;
import com.edian.www.app.SquareIndex;
import com.edian.www.app.UserCenter;
import com.edian.www.base.BaseApp;
import com.edian.www.base.BaseAuth;
import com.edian.www.model.User;
import com.edian.www.util.ExitApplication;

public class AuthApp extends BaseApp{
	private final int MENU_EXIT = 1;
	private final int MENU_FEEDBACK = 0;
	
	protected static User user = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		if(!BaseAuth.isLogin()){
			this.forward(AppLogin.class);
			this.onStop();
		}else{
			user = BaseAuth.getUser();
		}
	}
	
	@Override
	public void onStart(){
		super.onStart();
		this.bindMainTop();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		menu.add(0,MENU_FEEDBACK,0,"意见反馈").setIcon(R.drawable.feedback_logo);
		menu.add(0, MENU_EXIT, 1, R.string.exit).setIcon(R.drawable.exit);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem mItem){
		switch(mItem.getItemId()){
		case MENU_FEEDBACK:
			this.forward(Feedback.class);
			break;
		case MENU_EXIT:
			ExitApplication.getInstance().exit();
			break;
		}
		return true;
	}
	
	private void bindMainTop () {
		ImageButton bTopNew = (ImageButton) findViewById(R.id.top_new);
		ImageButton bTopPeople = (ImageButton) findViewById(R.id.top_people);
		ImageButton bTopShare = (ImageButton) findViewById(R.id.top_share);
		ImageButton bTopSetting = (ImageButton) findViewById(R.id.top_setting);
		if (bTopNew != null && bTopPeople != null && bTopShare != null && bTopSetting != null) {
			OnClickListener mOnClickListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (v.getId()) {
						case R.id.top_new:
							forward(EdianIndex.class);
							break;
						case R.id.top_people:
							forward(SquareIndex.class);
							break;
						case R.id.top_share:
							forward(ShareIndex.class);
							break;
						case R.id.top_setting:
							overlay(UserCenter.class);
							break;
					}
				}
			};
			bTopNew.setOnClickListener(mOnClickListener);
			bTopPeople.setOnClickListener(mOnClickListener);
			bTopShare.setOnClickListener(mOnClickListener);
			bTopSetting.setOnClickListener(mOnClickListener);
		}
	}
}
