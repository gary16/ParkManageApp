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

	public static void execSql(String sql, Object[] bindArgs) {
		db.execSQL(sql, bindArgs);
	}

	public static Cursor rawQuery(String sql, String[] selectionArgs) {
		return db.rawQuery(sql, selectionArgs);
	}

	public static void closeDatabase() {
		if (db != null) {
			db.close();
			db = null;
		}
	}
}
