package com.edian.www.model;

import com.edian.www.base.BaseModel;
import com.edian.www.base.C;

public class Square extends BaseModel {
	// model columns
	public final static String COL_ID = "id";
	public final static String COL_UID = "uid";
	public final static String COL_AUTHOR = "author";
	public final static String COL_AUTHORSEX = "authorsex";
	public final static String COL_AUTHORFACE = "authorface";
	public final static String COL_CLICK = "click";
	public final static String COL_CREATETIME = "createtime";
	public final static String COL_CONTENT = "content";
	public final static String NOSAY = "无内容";
	
	private String id = "";
	private String uid = "";
	private String author = "";
	private String authorsex = "";
	private String authorface = "";
	private String click = "";
	private String createtime = "";
	private String content = "";
	
	public Square () {}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getAuthorSex() {
		return authorsex;
	}
	public void setAuthorSex(String authorsex) {
		this.authorsex = authorsex;
	}
	public String getAuthorFace() {
		return authorface;
	}
	public void setAuthorFace(String authorface) {
		this.authorface = authorface;
	}
	public String getClick() {
		return click;
	}
	public void setClick(String click) {
		this.click = click;
	}
	public String getContent() {
		if(content.length()>0 && !content.equalsIgnoreCase("null")){
			return content;
		}else{
			return NOSAY;
		}
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
}
