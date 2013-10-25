package com.edian.www.model;

import com.edian.www.base.BaseModel;
import com.edian.www.base.C;

public class User extends BaseModel {
	// model columns
	public final static String COL_UID = "uid";
	public final static String COL_SID = "sid";
	public final static String COL_NICKNAME = "nickname";
	public final static String COL_PASS = "pwd";
	public final static String COL_MONEY = "money";
	public final static String COL_FACEURL = "faceurl";
	public final static String COL_REGTIME = "regtime";
	public final static String COL_VIPENDTIME = "vipendtime";
	public final static String COL_AGE = "age";
	public final static String COL_SEX = "sex";
	public final static String COL_STATURE = "stature";
	public final static String COL_WEIGHT = "weight";
	public final static String COL_BODYSHAPE = "bodyshape";
	public final static String COL_ISMARRIED = "ismarried";
	public final static String COL_DEGREE = "degree";
	public final static String COL_OCUPATION = "ocupation";
	public final static String COL_CITY = "city";
	public final static String COL_QQ = "qq";
	public final static String COL_TEL = "tel";
	public final static String COL_EMAIL = "email";
	public final static String COL_ATTITUDE = "attitude";
	public final static String COL_EXPERIENCE = "experience";
	public final static String COL_INTRODUCTION = "introduction";
	public final static String COL_LASTLOGINTIME = "lastlogintime";
	public final static String COL_HEARTSAY = "heartsay";
	public final static String NOSAY = "";
	
	private String uid = "";
	private String sid = "";
	private String nickname = "";
	private String pass = "";
	private String money = "";
	private String faceurl = "";
	private String regtime = "";
	private String vipendtime = "";
	private String age = "";
	private String sex = "";
	private String stature = "";
	private String weight = "";
	private String bodyshape = "";
	private String ismarried = "";
	private String degree = "";
	private String ocupation = "";
	private String city = "";
	private String qq = "";
	private String tel = "";
	private String email = "";
	private String attitude = "";
	private String experience = "";
	private String introduction = "";
	private String lastlogintime = "";
	private String heartsay = "";
	

	// default is no login
	private boolean isLogin = false;
	
	// single instance for login
	static private User user = null;	
	
	static public User getInstance () {
		if (User.user == null) {
			User.user = new User();
		}
		return User.user;
	}
	
	public User () {}
	
	public boolean isLogin() {
		return isLogin;
	}
	
	public Boolean getLogin () {
		return this.isLogin;
	}
	
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getFaceurl() {
		return faceurl;
	}
	public void setFaceurl(String faceurl) {
		this.faceurl = faceurl;
	}
	public String getRegtime() {
		return regtime;
	}
	public void setRegtime(String regtime) {
		this.regtime = regtime;
	}
	public String getVipendtime() {
		return vipendtime;
	}
	public void setVipendtime(String vipendtime) {
		this.vipendtime = vipendtime;
	}
	public String getAge() {
		if(this.age.equals("null"))
			return "";
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getSex() {
		if(sex.equalsIgnoreCase("0"))
			return C.con.male;
		else return C.con.female;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getStature() {
		if(this.stature.equals("null"))
			return "";
		return stature;
	}
	public void setStature(String stature) {
		this.stature = stature;
	}
	public String getWeight() {
		if(this.weight.equals("null"))
			return "";
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getBodyshape() {
		if(this.bodyshape.equals("null"))
			return "";
		return bodyshape;
	}
	public void setBodyshape(String bodyshape) {
		this.bodyshape = bodyshape;
	}
	public String getIsmarried() {
		if(this.ismarried.equals("null"))
			return NOSAY;
		return this.ismarried;
	}
	public void setIsmarried(String ismarried) {
		this.ismarried = ismarried;
	}
	public String getDegree() {
		if(this.degree.equals("null"))
			return "";
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getOcupation() {
		if(this.ocupation.equals("null"))
			return "";
		return ocupation;
	}
	public void setOcupation(String ocupation) {
		this.ocupation = ocupation;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getQq() {
		if(this.qq.equals("null"))
			return "";
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getTel() {
		if(this.tel.equals("null"))
			return "";
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		if(this.email.equals("null"))
			return "";
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAttitude() {
		if(attitude.length()>0 && !attitude.equalsIgnoreCase("null")){
			return attitude;
		}else{
			return NOSAY;
		}
	}
	public void setAttitude(String attitude) {
		this.attitude = attitude;
	}
	public String getExperience() {
		if(experience.length()>0 && !experience.equalsIgnoreCase("null")){
			return experience;
		}else{
			return NOSAY;
		}
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}
	public String getIntroduction() {
		if(this.introduction.equals("null"))
			return "";
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getLastlogintime() {
		return lastlogintime;
	}
	public void setLastlogintime(String lastlogintime) {
		this.lastlogintime = lastlogintime;
	}
	public String getHeartsay() {
		if(heartsay.length()>0 && !heartsay.equalsIgnoreCase("null")){
			return heartsay;
		}else{
			return NOSAY;
		}
	}
	public void setHeartsay(String heartsay) {
		this.heartsay = heartsay;
	}
}
