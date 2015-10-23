package com.zoway.parkmanage.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zoway.parkmanage.bean.ParkRecord;

public class DbHelper {

	private static SQLiteDatabase db = null;

	public static void openDatabase(Context c) {
		if (db == null) {
			db = c.openOrCreateDatabase("parkmanage.db", Context.MODE_PRIVATE,
					null);
		}
		createTables();
	}

	private static void createTables() {
		String deleteStr = " drop table t_parkrecord";
		db.execSQL(deleteStr);
		String ct1Str = " create table  if not exists t_parkrecord (tid integer primary key AUTOINCREMENT,recordid integer,recordno text,parkid integer,parkno  text,hphm text,hphmcolor text,parktime text,leavetime text ,fees  REAL,status integer ,filepathlist text,isprint int)";
		db.execSQL(ct1Str);
		String insertSql = "insert into t_parkrecord(recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepathlist,isprint) values(1,1,1,'','X12345','blue','201510231200','201510231230',30,1,'',0)";
		db.execSQL(insertSql);
		insertSql = "insert into t_parkrecord(recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepathlist,isprint) values(1,1,1,'','X23456','blue','201509231200','201509231300',30,1,'',0)";
		db.execSQL(insertSql);
		insertSql = "insert into t_parkrecord(recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepathlist,isprint) values(1,1,1,'','X23456','blue','201509231300','201509231330',30,1,'',0)";
		db.execSQL(insertSql);
		insertSql = "insert into t_parkrecord(recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepathlist,isprint) values(1,1,1,'','XAB123','blue','201510251200','',30,0,'',0)";
		db.execSQL(insertSql);
		insertSql = "insert into t_parkrecord(recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepathlist,isprint) values(1,1,1,'','XWD345','blue','201509241200','',30,0,'',0)";
		db.execSQL(insertSql);
		insertSql = "insert into t_parkrecord(recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepathlist,isprint) values(1,1,1,'','XGK098','blue','201509241300','',30,0,'',0)";
		db.execSQL(insertSql);
		insertSql = "insert into t_parkrecord(recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepathlist,isprint) values(1,1,1,'','XAB123','blue','201510251200','',30,2,'',0)";
		db.execSQL(insertSql);
		insertSql = "insert into t_parkrecord(recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepathlist,isprint) values(1,1,1,'','XWD345','blue','201509241200','',30,2,'',0)";
		db.execSQL(insertSql);
		insertSql = "insert into t_parkrecord(recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepathlist,isprint) values(1,1,1,'','XGK098','blue','201509241300','',30,2,'',0)";
		db.execSQL(insertSql);
		// ct1Str =
		// "cretae table  if not exists t_uploadrecord (tid int primary key,recordid int,uploadtime datetime,uploadstatus int)";
		// db.execSQL(ct1Str);
	}

	private static void execSql(String sql, Object[] bindArgs) {
		db.beginTransaction();
		if (bindArgs == null || bindArgs.length == 0) {
			db.execSQL(sql);
		} else {
			db.execSQL(sql, bindArgs);
		}

		db.endTransaction();
	}

	private static Cursor rawQuery(String sql, String[] selectionArgs) {
		db.beginTransaction();
		Cursor cur = db.rawQuery(sql, selectionArgs);
		db.endTransaction();
		return cur;
	}

	private static List<ParkRecord> queryRecordList(String payStatus) {
		String sql1 = "select recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,,filepathlist,isprint from t_parkrecord where status=?";
		Cursor cur = rawQuery(sql1, new String[] { payStatus });
		List<ParkRecord> list = new ArrayList<ParkRecord>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		while (cur.moveToNext()) {

			ParkRecord rec = new ParkRecord();
			rec.setRecoidid(cur.getInt(0));
			rec.setRecordno(cur.getString(1));
			rec.setParkid(cur.getInt(2));
			rec.setParkno(cur.getString(3));
			rec.setHphm(cur.getString(4));
			rec.setHphmcolor(cur.getString(5));
			try {
				rec.setParktime(sdf.parse(cur.getString(6)));
				rec.setLeavetime(sdf.parse(cur.getString(7)));
			} catch (Exception er) {
				er.printStackTrace();
			}
			rec.setFees(cur.getFloat(8));
			rec.setStatus(cur.getInt(9));
			rec.setFilepathlist(cur.getString(10));
			rec.setIsprint(cur.getInt(11));
			list.add(rec);
		}
		return list;
	}

	public static void closeDatabase() {
		if (db != null) {
			db.close();
			db = null;
		}
	}
}
