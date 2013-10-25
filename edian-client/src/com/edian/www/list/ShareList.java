package com.edian.www.list;

import java.util.ArrayList;
import java.util.Collections;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edian.www.model.Share;
import com.edian.www.util.AppCache;
import com.edian.www.util.AppUtil;
import com.edian.www.util.shareSortByCreateTime;
import com.edian.www.app.UserDetail;
import com.edian.www.base.BaseApp;
import com.edian.www.base.BaseList;
import com.edian.www.R;

public class ShareList extends BaseList {
	private BaseApp app;
	private LayoutInflater inflater;
	private ArrayList<Share> sharelist;
	private sharelistItem item;
	
	public final class sharelistItem{
		public ImageView face;
		public TextView nickname;
		public TextView title;
		public TextView content;
		public TextView click;
		public TextView createtime;
	}
	
	public ShareList (BaseApp app, ArrayList<Share> sharelist) {
		this.app = app;
		this.inflater = LayoutInflater.from(this.app);
		this.sharelist = sharelist;
		//按最后登录日期倒序排序
		Collections.sort(this.sharelist, new shareSortByCreateTime());
	}
	
	@Override
	public int getCount() {
		return sharelist.size();
	}

	@Override
	public Share getItem(int position) {
		return sharelist.get(position);
	}
	
	public void addItemlist(ArrayList<Share> sharelist){
		sharelist.addAll(sharelist);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int p, View v, ViewGroup parent) {
		// if cached expired
		if (v == null) {
			v = inflater.inflate(R.layout.tpl_list_share, null);
			item = new sharelistItem();
			item.face = (ImageView) v.findViewById(R.id.tpl_list_share_image);
			item.nickname = (TextView) v.findViewById(R.id.tpl_list_share_author);
			item.title = (TextView) v.findViewById(R.id.tpl_list_share_title);
			item.content = (TextView) v.findViewById(R.id.tpl_list_share_content);
			item.createtime = (TextView) v.findViewById(R.id.tpl_list_share_createtime);
			v.setTag(item);
		} else {
			item = (sharelistItem) v.getTag();
		}
		Share share = sharelist.get(p);
		// fill data
		item.title.setText(share.getTitle());
		item.nickname.setText(share.getAuthor());
		item.content.setText(share.getContent());
		// fill html data
		
		item.createtime.setText(AppUtil.time2now(share.getCreatetime()));
		// load face image
		String faceUrl = share.getFace();
		if(faceUrl.length()>0){
			item.face.setTag(faceUrl);
			app.loadImage(faceUrl);
		}
		item.face.setOnClickListener(new faceListener(p));
		return v;
	}
	
	class faceListener implements OnClickListener{
		private int pos;
		
		faceListener(int p){
			this.pos = p;
		}
		
		@Override
		public void onClick(View v){
			int vid = v.getId();
			if(vid == item.face.getId()){
				Bundle para = new Bundle();
				para.putString("uid", sharelist.get(pos).getUid());
				app.overlay(UserDetail.class, para);
			}
		}
	}
}
