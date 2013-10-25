package com.edian.www.list;

import java.util.ArrayList;
import java.util.Collections;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edian.www.model.Square;
import com.edian.www.util.AppCache;
import com.edian.www.util.AppUtil;
import com.edian.www.util.squareSortByCreateTime;
import com.edian.www.app.UserDetail;
import com.edian.www.base.BaseApp;
import com.edian.www.base.BaseList;
import com.edian.www.R;

public class SquareList extends BaseList {
	private BaseApp app;
	private LayoutInflater inflater;
	private ArrayList<Square> squarelist;
	private squarelistItem item;
	
	public final class squarelistItem{
		public ImageView face;
		public TextView nickname;
		public ImageView sexlogo;
		public TextView content;
		//public TextView click;
		public TextView createtime;
	}
	
	public SquareList (BaseApp app, ArrayList<Square> squarelist) {
		this.app = app;
		this.inflater = LayoutInflater.from(this.app);
		this.squarelist = squarelist;
		//按最后登录日期倒序排序
		Collections.sort(this.squarelist, new squareSortByCreateTime());
	}
	
	@Override
	public int getCount() {
		return squarelist.size();
	}

	@Override
	public Square getItem(int position) {
		return squarelist.get(position);
	}
	
	public void addItemlist(ArrayList<Square> squarelist){
		squarelist.addAll(squarelist);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int p, View v, ViewGroup parent) {
		// init tpl
		squarelistItem  SquareItem = null;
		// if cached expired
		if (v == null) {
			v = inflater.inflate(R.layout.tpl_list_square, null);
			item = new squarelistItem();
			item.face = (ImageView) v.findViewById(R.id.tpl_list_square_image);
			item.nickname = (TextView) v.findViewById(R.id.tpl_list_square_nickname);
			item.sexlogo = (ImageView) v.findViewById(R.id.tpl_list_square_sexlogo);
			//SquareItem.click = (TextView) v.findViewById(R.id.tpl_list_square_click);
			item.content = (TextView) v.findViewById(R.id.tpl_list_square_content);
			item.createtime = (TextView) v.findViewById(R.id.tpl_list_square_createtime);
			v.setTag(item);
		} else {
			item = (squarelistItem) v.getTag();
		}
		Square square = squarelist.get(p);
		// fill data
		item.nickname.setText(square.getAuthor());
		item.content.setText(square.getContent());
		// fill html data
		Log.v("SquareList", "square.sex:"+square.getAuthorSex());
		if(square.getAuthorSex().equals("0"))
			item.sexlogo.setImageResource(R.drawable.boy_sign);
		else item.sexlogo.setImageResource(R.drawable.girl_sign);
		item.createtime.setText(AppUtil.time2now(square.getCreatetime()));
		//item.click.setText("阅读:"+square.getClick());
		// load face image
		String faceUrl = square.getAuthorFace();
		if(faceUrl.length()>0){
			item.face.setTag(faceUrl);
			app.loadImage(faceUrl);
		}
		item.face.setOnClickListener(new faceListener(p));
		return v;
	}
	
	class faceListener implements OnClickListener {
		private int pos;
		
		faceListener(int p){
			this.pos = p;
		}
		
		@Override
		public void onClick(View v){
			int vid = v.getId();
			if(vid==item.face.getId()){
				Bundle bd = new Bundle();
				bd.putString("uid",squarelist.get(pos).getUid());
				app.overlay(UserDetail.class,bd);
			}
		}
	}
}
