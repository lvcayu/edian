package com.edian.www.sqlite;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.edian.www.base.BaseSqlite;
import com.edian.www.model.User;

public class UserSqlite extends BaseSqlite {

	public UserSqlite(Context context) {
		super(context);
	}

	@Override
	protected String tableName() {
		return "user";
	}

	@Override
	protected String[] tableColumns() {
		String[] columns = { User.COL_AGE, User.COL_FACEURL, User.COL_NICKNAME,
				User.COL_SEX, User.COL_LASTLOGINTIME, User.COL_HEARTSAY,
				User.COL_ISMARRIED, User.COL_REGTIME, User.COL_ATTITUDE,
				User.COL_EXPERIENCE, User.COL_BODYSHAPE, User.COL_CITY,
				User.COL_DEGREE, User.COL_OCUPATION, User.COL_QQ, User.COL_TEL,
				User.COL_STATURE, User.COL_WEIGHT };
		return columns;
	}

	@Override
	protected String createSql() {
		return "CREATE TABLE " + tableName() + " (" + User.COL_UID
				+ " INTEGER PRIMARY KEY, " + User.COL_AGE + " TEXT, "
				+ User.COL_FACEURL + " TEXT, " + User.COL_SEX + " TEXT, "
				+ User.COL_LASTLOGINTIME + " TEXT, " + User.COL_HEARTSAY
				+ " TEXT, " + User.COL_ISMARRIED + " TEXT, " + User.COL_REGTIME
				+ " TEXT, " + User.COL_ATTITUDE + " TEXT, "
				+ User.COL_EXPERIENCE + " TEXT, " + User.COL_BODYSHAPE
				+ " TEXT, " + User.COL_CITY + " TEXT, " + User.COL_DEGREE
				+ " TEXT, " + User.COL_OCUPATION + " TEXT, " + User.COL_QQ
				+ " TEXT, " + User.COL_TEL + " TEXT, " + User.COL_STATURE
				+ " TEXT, " + User.COL_WEIGHT + " TEXT " + ");";
	}

	@Override
	protected String upgradeSql() {
		return "DROP TABLE IF EXISTS " + tableName();
	}

	public boolean updateUser(User user) {
		ContentValues values = new ContentValues();
		values.put(User.COL_AGE, user.getAge());
		values.put(User.COL_FACEURL, user.getFaceurl());
		values.put(User.COL_NICKNAME, user.getNickname());
		values.put(User.COL_SEX, user.getSex());
		values.put(User.COL_LASTLOGINTIME, user.getLastlogintime());
		values.put(User.COL_HEARTSAY, user.getHeartsay());
		values.put(User.COL_ISMARRIED, user.getIsmarried());
		values.put(User.COL_REGTIME, user.getRegtime());
		values.put(User.COL_ATTITUDE, user.getAttitude());
		values.put(User.COL_EXPERIENCE, user.getExperience());
		values.put(User.COL_BODYSHAPE, user.getBodyshape());
		values.put(User.COL_CITY, user.getCity());
		values.put(User.COL_DEGREE, user.getDegree());
		values.put(User.COL_OCUPATION, user.getOcupation());
		values.put(User.COL_QQ, user.getQq());
		values.put(User.COL_TEL, user.getTel());
		values.put(User.COL_STATURE, user.getStature());
		values.put(User.COL_WEIGHT, user.getWeight());
		// prepare sql
		String whereSql = User.COL_UID + "=?";
		String[] whereParams = new String[]{user.getUid()};
		// create or update
		try {
			if (this.exists(whereSql, whereParams)) {
				this.update(values, whereSql, whereParams);
			} else {
				this.create(values);
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	public ArrayList<User> getAllUsers () {
		ArrayList<User> userlist = new ArrayList<User>();
		try {
			Cursor cursor = this.query(null, null);
			while (cursor.moveToNext()) {
				User user = new User();
				user.setAge(cursor.getString(0));
				user.setFaceurl(cursor.getString(1));
				user.setNickname(cursor.getString(2));
				user.setSex(cursor.getString(3));
				user.setLastlogintime(cursor.getString(4));
				userlist.add(user);
			}
		} catch (Exception e) {
			
		}
		return userlist;
	}
}
