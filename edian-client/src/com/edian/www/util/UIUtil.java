package com.edian.www.util;

import com.edian.www.R;
import com.edian.www.model.User;

import android.content.Context;
import android.content.res.Resources;

public class UIUtil {

	// tag for log
//	private static String TAG = UIUtil.class.getSimpleName();
	
	public static String getUserInfo (Context ctx, User user) {
		Resources resources = ctx.getResources();
		StringBuffer sb = new StringBuffer();
//		sb.append(resources.getString(R.string.User_blog));
		sb.append(" ");
//		sb.append(User.getBlogcount());
		sb.append(" | ");
//		sb.append(resources.getString(R.string.User_fans));
		sb.append(" ");
//		sb.append(User.getFanscount());
		return sb.toString();
	}
}