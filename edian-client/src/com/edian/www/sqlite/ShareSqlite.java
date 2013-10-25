package com.edian.www.sqlite;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.edian.www.base.BaseSqlite;
import com.edian.www.model.Share;
import com.edian.www.model.User;

public class ShareSqlite extends BaseSqlite {

	public ShareSqlite(Context context) {
		super(context);
	}

	@Override
	protected String tableName() {
		return "share";
	}

	@Override
	protected String[] tableColumns() {
		String[] columns = { Share.COL_ID,Share.COL_UID,Share.COL_AUTHOR,Share.COL_CLICK,Share.COL_CONTENT,Share.COL_CREATETIME,Share.COL_FACE,
				Share.COL_TITLE,Share.COL_TYPE,};
		return columns;
	}

	@Override
	protected String createSql() {
		return "CREATE TABLE " + tableName() + " (" + Share.COL_ID
				+ " INTEGER PRIMARY KEY, " + Share.COL_UID + " TEXT, "
				+ Share.COL_AUTHOR + " TEXT "
				+ Share.COL_CLICK + " TEXT "
				+ Share.COL_CONTENT + " TEXT "
				+ Share.COL_CREATETIME + " TEXT "
				+ Share.COL_FACE + " TEXT "
				+ Share.COL_TITLE + " TEXT "
				+ Share.COL_TYPE + ")";
	}

	@Override
	protected String upgradeSql() {
		return "DROP TABLE IF EXISTS " + tableName();
	}

	public boolean updateShare(Share share) {
		ContentValues values = new ContentValues();
		values.put(Share.COL_ID, share.getId());
		values.put(Share.COL_UID, share.getUid());
		values.put(Share.COL_AUTHOR, share.getAuthor());
		values.put(Share.COL_CLICK, share.getClick());
		values.put(Share.COL_CONTENT, share.getContent());
		values.put(Share.COL_FACE, share.getFace());
		values.put(Share.COL_TYPE, share.getType());
		// prepare sql
		String whereSql = Share.COL_ID + "=?";
		String[] whereParams = new String[]{share.getId()};
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
	
	public ArrayList<Share> getAllShares () {
		ArrayList<Share> sharelist = new ArrayList<Share>();
		try {
			Cursor cursor = this.query(null, null);
			while (cursor.moveToNext()) {
				Share share = new Share();
				share.setAuthor(cursor.getString(0));
				share.setAuthor(cursor.getString(1));
				share.setClick(cursor.getString(3));
				share.setContent(cursor.getString(4));
				share.setCreatetime(cursor.getString(5));
				share.setUid(cursor.getString(6));
				sharelist.add(share);
			}
		} catch (Exception e) {
			
		}
		return sharelist;
	}
}
