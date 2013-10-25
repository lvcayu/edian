/**
 * 
 */
package com.edian.www.util;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.edian.www.model.Share;

/**
 * @author walker
 *
 */
public class shareSortByCreateTime implements Comparator<Share> {
	@Override
	public int compare(Share o1, Share o2) {
		Date s1d = null;
		Date s2d = null;
		try{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		s1d = sdf.parse(o1.getCreatetime());
		s2d = sdf.parse(o2.getCreatetime());

		}catch(java.text.ParseException e){
			e.printStackTrace();
		}
		if (s1d.before(s2d))
			return 1;
		else 
			return -1;
	}
}
