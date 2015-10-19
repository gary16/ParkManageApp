package com.zoway.parkmanage.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbHelper {

	private static SQLiteDatabase db = null;

	public static void openDatabase(Context c) {
		if (db == null) {
			db = c.openOrCreateDatabase("parkmanage.db", Context.MODE_PRIVATE,
					null);
		}
	}

	public static void createTables() {
		String ct1Str = " cretae table  if not exists t_unhandlelist (tid int primary key,recordid int,recordno varchar,parkid int,parkno varchar)";
		execSql(ct1Str, null);
	}

	public static void execSql(String sql, Object[] bindArgs) {
		db.beginTransaction();
		db.execSQL(sql, bindArgs);
		db.endTransaction();
	}

	public static Cursor rawQuery(String sql, String[] selectionArgs) {
		db.beginTransaction();
		Cursor cur = db.rawQuery(sql, selectionArgs);
		db.endTransaction();
		return cur;
	}

	public static void closeDatabase() {
		if (db != null) {
			db.close();
			db = null;
		}
	}
}
