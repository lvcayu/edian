package com.edian.www.base;

import com.edian.www.model.User;

public class BaseAuth {
	
	static public boolean isLogin () {
		User user = User.getInstance();
		if (user.getLogin() == true) {
			return true;
		}
		return false;
	}
	
	static public void setLogin (Boolean status) {
		User user = User.getInstance();
		user.setLogin(status);
	}
	
	static public void setUser (User mc) {
		User user = User.getInstance();
		user.setUid(mc.getUid());
		user.setSid(mc.getSid());
		user.setNickname(mc.getNickname());
		user.setMoney(mc.getMoney());
		user.setFaceurl(mc.getFaceurl());
		user.setRegtime(mc.getRegtime());
		user.setVipendtime(mc.getVipendtime());
		user.setAge(mc.getAge());
		user.setSex(mc.getSex());
		user.setStature(mc.getStature());
		user.setWeight(mc.getWeight());
		user.setBodyshape(mc.getBodyshape());
		user.setIsmarried(mc.getIsmarried());
		user.setDegree(mc.getDegree());
		user.setOcupation(mc.getOcupation());
		user.setCity(mc.getCity());
		user.setQq(mc.getQq());
		user.setTel(mc.getTel());
		user.setEmail(mc.getEmail());
		user.setAttitude(mc.getAttitude());
		user.setExperience(mc.getExperience());
		user.setIntroduction(mc.getIntroduction());
		user.setLastlogintime(mc.getLastlogintime());
		user.setHeartsay(mc.getHeartsay());
	}
	
	static public User getUser () {
		return User.getInstance();
	}
}