package com.edian.www.base;

public final class C {
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// core settings (important)
	public static final class list {
		public final static int LISTVIEW_ACTION_INIT = 0x01;
		public final static int LISTVIEW_ACTION_REFRESH = 0x02;
		public final static int LISTVIEW_ACTION_SCROLL = 0x03;
		public final static int LISTVIEW_ACTION_CHANGE_CATALOG = 0x04;
		
		public final static int LISTVIEW_DATA_MORE = 0x01;
		public final static int LISTVIEW_DATA_LOADING = 0x02;
		public final static int LISTVIEW_DATA_FULL = 0x03;
		public final static int LISTVIEW_DATA_EMPTY = 0x04;
		
		public final static int LISTVIEW_DATATYPE_NEWS = 0x01;
		public final static int LISTVIEW_DATATYPE_BLOG = 0x02;
		public final static int LISTVIEW_DATATYPE_POST = 0x03;
		public final static int LISTVIEW_DATATYPE_TWEET = 0x04;
		public final static int LISTVIEW_DATATYPE_ACTIVE = 0x05;
		public final static int LISTVIEW_DATATYPE_MESSAGE = 0x06;
		public final static int LISTVIEW_DATATYPE_COMMENT = 0x07;
		
		public final static int REQUEST_CODE_FOR_RESULT = 0x01;
		public final static int REQUEST_CODE_FOR_REPLY = 0x02;
		
		public final static int LIST_MAX				= 120;
	}
	
	public static final class dir {
		public static final String base				= "/sdcard/demos";
		public static final String faces			= base + "/faces";
		public static final String images			= base + "/images";
	}
	
	public static final class api {
		public static final String base				= "http://api.edianlab.com";//"http://192.168.1.10:8001";
		public static final String updateversion	= "http://static.edianlab.com/version.xml";//"http://192.168.1.10/version.xml";
		public static final String login			= "/index/login";
		public static final String logout			= "/index/logout";
		public static final String notice			= "/notify/notice";
		public static final String checkname		= "/user/userCheck";
		public static final String usercreate		= "/user/userCreate";
		public static final String userlist		    = "/user/userList";
		public static final String userview		    = "/user/userView";
		public static final String squarelist		= "/square/squareList";
		public static final String sharelist		= "/share/shareList";
		public static final String userupdate		= "/user/userUpdate";
		public static final String inform			= "/inform/informCreate";
		public static final String pubsquare		= "/square/squareCreate";
		public static final String pubshare			= "/share/shareCreate";
		public static final String shareview		= "/share/shareView";
		public static final String feedbackcreate	= "/feedback/feedbackCreate";
	}
	
	public static final class task {
		public static final int loadimage			= 1000;
		public static final int notice				= 1014;
		public static final int login				= 1015;
		public static final int logout				= 1023;
		public static final int checkname			= 1016;
		public static final int usercreate			= 1017;
		public static final int userlist			= 1018;
		public static final int userview			= 1019;
		public static final int squarelist			= 1020;
		public static final int sharelist			= 1021;
		public static final int userupdate			= 1022;
		public static final int inform				= 1023;
		public static final int pubsquare			= 1024;
		public static final int pubshare			= 1025;
		public static final int shareview			= 1026;
		public static final int feedbackcreate		= 1027;
	}
	
	public static final class err {
		public static final String network			= "网络错误";
		public static final String message			= "消息错误";
		public static final String jsonFormat		= "消息格式错误";
		public static final String emptysignupbox		= "似乎有漏填项哦～";
		public static final String pwdcheck		= "两次输入的密码不一致，请重新输入";
		public static final String userexist		= "用户名已存在";
		public static final String nameorpassempty		= "取用户名和密码时应用出错，请返回到上一页";
		public static final String userbaned		="此用户帐号已被封";
		public static final String getuserfailed		="获取用户信息失败！T_T";
		public static final String updatesuccess		="资料更新成功！";
	}
	
	public static final class errcode {
		public static final String SUCCESS	= "10000";
		public static final String NOT_LOGIN	= "10001";
		public static final String USER_BANED		= "10064";
		public static final String NICKNAME_EXIST	= "10065";
		public static final String EMPTY			= "10008";
		public static final String NOUSER			= "10062";
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// intent & action settings
	
	public static final class intent {
		public static final class action {
			public static final String PUBSQUARE	= "com.edian.www.PUBSQUARE";
			public static final String PUBSHARE		= "com.edian.www.PUBSHARE";
		}
	}
	
	public static final class action {
		public static final class edittext {
			public static final int CONFIG			= 2001;
			public static final int COMMENT			= 2002;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// additional settings
	
	public static final class web {
		public static final String base				= "http://192.168.1.100:8002";
		public static final String index			= base + "/index.php";
		public static final String gomap			= base + "/gomap.php";
	}
	
	//////////////////////////////////////////
	///城市列表
	public static final class City {
	}
	
	//////////////////////////////
	//const integer
	public static final class con {
		public static final int toast_dur = 2;
		public static final String male				= "0";
		public static final String female			= "1";
		public static final String male_zh_cn		= "男";
		public static final String female_zh_cn		= "女";
		public static final String notmarried		= "0";
		public static final String married			= "1";
		public static final String married_zh_cn	= "已婚";
		public static final String notmarried_zh_cn	= "未婚";
	}
}