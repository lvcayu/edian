package com.edian.www.model;

import com.edian.www.base.BaseModel;

public class Share extends BaseModel {
	// model columns
	public final static String COL_ID = "id";
	public final static String COL_UID = "uid";
	public final static String COL_AUTHOR = "author";
	public final static String COL_FACE = "faceurl";
	public final static String COL_TITLE = "title";
	public final static String COL_TYPE = "type";
	public final static String COL_CLICK = "click";
	public final static String COL_CONTENT = "content";
	public final static String COL_CREATETIME = "createtime";
	public final static String NOSAY = "无内容";
	
	private String id = "";
	private String uid = "";
	private String author = "";
	private String faceurl = "";
	private String type = "";
	private String title = "";
	private String click = "";
	private String createtime = "";
	private String content = "";
	
	public Share () {}
	
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
	public String getFace() {
		return faceurl;
	}
	public void setFace(String face) {
		this.faceurl = face;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
