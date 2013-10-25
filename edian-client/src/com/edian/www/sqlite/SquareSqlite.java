package com.edian.www.sqlite;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.edian.www.base.BaseSqlite;
import com.edian.www.model.Square;
import com.edian.www.model.User;

public class SquareSqlite extends BaseSqlite {

	public SquareSqlite(Context context) {
		super(context);
	}

	@Override
	protected String tableName() {
		return "square";
	}

	@Override
	protected String[] tableColumns() {
		String[] columns = { Square.COL_AUTHOR,Square.COL_AUTHORFACE,Square.COL_AUTHORSEX,Square.COL_CLICK,
				Square.COL_CONTENT,Square.COL_CREATETIME,Square.COL_ID,Square.COL_UID};
		return columns;
	}

	@Override
	protected String createSql() {
		return "CREATE TABLE " + tableName() + " (" + Square.COL_ID
				+ " INTEGER PRIMARY KEY, " + Square.COL_UID + " TEXT, "
				+ Square.COL_AUTHOR + " TEXT "
				+ Square.COL_AUTHORFACE + " TEXT "
				+ Square.COL_AUTHORSEX + " TEXT "
				+ Square.COL_CLICK + " TEXT "
				+ Square.COL_CONTENT + " TEXT "
				+ Square.COL_CREATETIME + " TEXT "+")";
	}

	@Override
	protected String upgradeSql() {
		return "DROP TABLE IF EXISTS " + tableName();
	}

	public boolean updateSquare(Square square) {
		ContentValues values = new ContentValues();
		values.put(Square.COL_AUTHOR, square.getAuthor());
		values.put(Square.COL_AUTHORFACE, square.getAuthorFace());
		values.put(Square.COL_AUTHORSEX, square.getAuthorSex());
		values.put(Square.COL_CLICK, square.getClick());
		values.put(Square.COL_CONTENT, square.getContent());
		values.put(Square.COL_CREATETIME, square.getCreatetime());
		values.put(Square.COL_UID, square.getUid());
		// prepare sql
		String whereSql = Square.COL_ID + "=?";
		String[] whereParams = new String[]{square.getId()};
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
	
	public ArrayList<Square> getAllSquares () {
		ArrayList<Square> squarelist = new ArrayList<Square>();
		try {
			Cursor cursor = this.query(null, null);
			while (cursor.moveToNext()) {
				Square square = new Square();
				square.setAuthor(cursor.getString(0));
				square.setAuthorFace(cursor.getString(1));
				square.setAuthorSex(cursor.getString(2));
				square.setClick(cursor.getString(3));
				square.setContent(cursor.getString(4));
				square.setCreatetime(cursor.getString(5));
				square.setUid(cursor.getString(6));
				squarelist.add(square);
			}
		} catch (Exception e) {
			
		}
		return squarelist;
	}
}
