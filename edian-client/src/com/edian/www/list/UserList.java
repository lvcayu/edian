package com.edian.www.list;

import java.util.ArrayList;
import java.util.Collections;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edian.www.model.User;
import com.edian.www.util.AppCache;
import com.edian.www.util.AppUtil;
import com.edian.www.util.userSortByRegTime;
import com.edian.www.base.BaseApp;
import com.edian.www.base.BaseList;
import com.edian.www.base.C;
import com.edian.www.R;

public class UserList extends BaseList {
	private BaseApp app;
	private LayoutInflater inflater;
	private ArrayList<User> userlist;
	
	public final class UserListItem{
		public ImageView face;
		public TextView nickname;
		public TextView age;
		public ImageView sex;
		public TextView time;
		public TextView heartsay;
	}
	
	public UserList (BaseApp app, ArrayList<User> userlist) {
		this.app = app;
		this.inflater = LayoutInflater.from(this.app);
		this.userlist = userlist;
		//按最后登录日期倒序排序
		Collections.sort(this.userlist, new userSortByRegTime());
	}
	
	@Override
	public int getCount() {
		return userlist.size();
	}

	@Override
	public User getItem(int position) {
		return userlist.get(position);
	}
	/*
	public void addItemlist(ArrayList<User> userlist){
		this.userlist.addAll(userlist);
	}*/

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int p, View v, ViewGroup parent) {
		// init tpl
		UserListItem  userItem = null;
		// if cached expired
		if (v == null) {
			v = inflater.inflate(R.layout.tpl_list_newuser, null);
			userItem = new UserListItem();
			userItem.face = (ImageView) v.findViewById(com.edian.www.R.id.tpl_list_newuser_image);
			userItem.nickname = (TextView) v.findViewById(com.edian.www.R.id.tpl_list_newuser_nickname);
			userItem.age = (TextView) v.findViewById(com.edian.www.R.id.tpl_list_newuser_age);
			userItem.sex = (ImageView)v.findViewById(R.id.tpl_list_newuser_sex);
			userItem.time = (TextView) v.findViewById(R.id.tpl_list_newuser_time);
			userItem.heartsay = (TextView) v.findViewById(com.edian.www.R.id.tpl_list_newuser_heartsay);
			v.setTag(userItem);
		} else {
			userItem = (UserListItem) v.getTag();
		}
		User user = userlist.get(p);
		// fill data
		userItem.nickname.setText(user.getNickname());
		// fill html data
		userItem.age.setText(user.getAge());
		if(user.getSex().equals(C.con.male))
			userItem.sex.setImageResource(R.drawable.boy_sign);
		else userItem.sex.setImageResource(R.drawable.girl_sign);
		userItem.time.setText("|"+AppUtil.time2now(user.getLastlogintime()));
		userItem.heartsay.setText(user.getHeartsay());
		// load face image
		String faceUrl = user.getFaceurl();
		if(faceUrl.length()>0){
			userItem.face.setTag(faceUrl);
			app.loadImage(faceUrl);
		}
		return v;
	}
}
