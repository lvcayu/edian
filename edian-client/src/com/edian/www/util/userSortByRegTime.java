/**
 * 
 */
package com.edian.www.util;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.edian.www.model.User;

/**
 * @author walker
 *
 */
public class userSortByRegTime implements Comparator<User> {
	@Override
	public int compare(User o1, User o2) {
		Date s1d = null;
		Date s2d = null;
		try{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		s1d = sdf.parse(o1.getRegtime());
		s2d = sdf.parse(o2.getRegtime());

		}catch(java.text.ParseException e){
			e.printStackTrace();
		}
		if (s1d.before(s2d))
			return 1;
		else 
			return -1;
	}
}
