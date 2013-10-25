package com.edian.www.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.text.format.DateFormat;
import android.util.Log;

import com.edian.www.base.BaseMessage;
import com.edian.www.base.BaseModel;
import com.edian.www.model.User;

public class AppUtil {
	
	/* md5 加密 */
	static public String md5 (String str) {
		MessageDigest algorithm = null;
		try {
			algorithm = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		if (algorithm != null) {
			algorithm.reset();
			algorithm.update(str.getBytes());
			byte[] bytes = algorithm.digest();
			StringBuilder hexString = new StringBuilder();
			for (byte b : bytes) {
				hexString.append(Integer.toHexString(0xFF & b));
			}
			return hexString.toString();
		}
		return "";
		
	}
	
	/* 首字母大写 */
	static public String ucfirst (String str) {
		if (str != null && str != "") {
			str  = str.substring(0,1).toUpperCase()+str.substring(1);
		}
		return str;
	}
	
	/* 为 EntityUtils.toString() 添加 gzip 解压功能 */
	public static String gzipToString(final HttpEntity entity, final String defaultCharset) throws IOException, ParseException {
		if (entity == null) {
			throw new IllegalArgumentException("HTTP entity may not be null");
		}
		InputStream instream = entity.getContent();
		if (instream == null) {
			return "";
		}
		// gzip logic start
		if (entity.getContentEncoding().getValue().contains("gzip")) {
			instream = new GZIPInputStream(instream);
		}
		// gzip logic end
		if (entity.getContentLength() > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
		}
		int i = (int)entity.getContentLength();
		if (i < 0) {
			i = 4096;
		}
		String charset = EntityUtils.getContentCharSet(entity);
		if (charset == null) {
			charset = defaultCharset;
		}
		if (charset == null) {
			charset = HTTP.DEFAULT_CONTENT_CHARSET;
		}
		Reader reader = new InputStreamReader(instream, charset);
		CharArrayBuffer buffer = new CharArrayBuffer(i);
		try {
			char[] tmp = new char[1024];
			int l;
			while((l = reader.read(tmp)) != -1) {
				buffer.append(tmp, 0, l);
			}
		} finally {
			reader.close();
		}
		return buffer.toString();
	}
	
	/* 为 EntityUtils.toString() 添加 gzip 解压功能 */
	public static String gzipToString(final HttpEntity entity)
		throws IOException, ParseException {
		return gzipToString(entity, null);
	}
	
	public static SharedPreferences getSharedPreferences (Context ctx) {
		return ctx.getSharedPreferences("com.edian.www.sp.global", Context.MODE_PRIVATE);
	}
	
	public static SharedPreferences getSharedPreferences (Service service) {
		return service.getSharedPreferences("com.edian.www.sp.global", Context.MODE_PRIVATE);
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	// 业务逻辑
	
	/* 获取 Session Id */
	static public String getSessionId () {
		User user = User.getInstance();
		return user.getSid();
	}
	
	/* 获取 Message */
	static public BaseMessage getMessage (String jsonStr) throws Exception {
		BaseMessage message = new BaseMessage();
		JSONObject jsonObject = null;
		Log.i("JSON-SAMPLE", jsonStr);
		try {
			jsonObject = new JSONObject(jsonStr);
			if (jsonObject != null) {
				message.setCode(jsonObject.getString("code"));
				message.setMessage(jsonObject.getString("message"));
				message.setResult(jsonObject.getString("result"));
			}
		} catch (JSONException e) {
			Log.d("DEBUG", e.getMessage());
			throw new Exception("Json format error");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}
	
	/* Model 数组转化成 Map 列表 */
	static public List<? extends Map<String,?>> dataToList (List<? extends BaseModel> data, String[] fields) {
		ArrayList<HashMap<String,?>> list = new ArrayList<HashMap<String,?>>();
		for (BaseModel item : data) {
			list.add((HashMap<String, ?>) dataToMap(item, fields));
		}
		return list;
	}
	
	/* Model 转化成 Map */
	static public Map<String,?> dataToMap (BaseModel data, String[] fields) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		try {
			for (String fieldName : fields) {
				Field field = data.getClass().getDeclaredField(fieldName);
				field.setAccessible(true); // have private to be accessable
				map.put(fieldName, field.get(data));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 把指定日期时间距离现在的时间转换成一个描述字符串
	 * @param date
	 * @return String
	 */
	public static String time2now(String date){
		String retstr = "";
		try{
			SimpleDateFormat thattime =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date time = thattime.parse(date);
			long thesec = time.getTime();
			Date t_now = new Date();
			long now = t_now.getTime();
			long howlong = now-thesec;
			Log.i("time", "now:"+Long.toString(now)+" thesec:"+Long.toString(thesec)+" howlong:"+Long.toString(howlong));
			howlong = howlong<0?0:howlong;
			if(howlong<60000){
				retstr = "现在";
			} else if(howlong>=60000 && howlong<300000){
				retstr = "1分钟前";
			} else if(howlong>=300000 && howlong<1800000){
				retstr = "5分钟前";
			} else if(howlong>=1800000 && howlong<3600000){
				retstr = "30分钟前";
			} else if(howlong>=3600000 && howlong<7200000){
				retstr = "1小时前";
			} else if(howlong>=7200000 && howlong<10900000){
				retstr = "2小时前";
			} else if(howlong>=10900000 && howlong<14200000){
				retstr = "3小时前";
			} else if(howlong>=14200000 && howlong<86400000){
				retstr = "今天";
			} else if(howlong>=86400000 && howlong<172800000){
				retstr = "昨天";
			} else retstr = date.substring(0,date.indexOf(32));
		
		}catch (java.text.ParseException e){
			e.printStackTrace();
		}catch (RuntimeException e){
			e.printStackTrace();
		}
		return retstr;
	}
	
}