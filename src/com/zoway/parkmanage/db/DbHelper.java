package com.zoway.parkmanage.db;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zoway.parkmanage.bean.EscapeRecord;
import com.zoway.parkmanage.bean.ParkRecord;
import com.zoway.parkmanage.bean.PayRecord;
import com.zoway.parkmanage.utils.PathUtils;
import com.zoway.parkmanage.utils.TimeUtil;

public class DbHelper {

	private static SQLiteDatabase db = null;
	private static String dbpath = PathUtils.getSdPath() + File.separator
			+ "parkmanage.db";

	private static void openDatabase() {
		if (db == null) {
			db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
		}
	}

	public static void createTables() {
		openDatabase();
		// String deleteStr = " drop table if exists t_uploadevasion";
		// db.execSQL(deleteStr);
		// String insertSql =
		// "insert into t_parkrecord(recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isprint) values(1,1,1,'','X12345','blue','201510231200','201510231230',30,1,'',0,0)";
		// db.execSQL(insertSql);
		// insertSql =
		// "insert into t_parkrecord(recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isprint) values(1,1,1,'','X23456','blue','201509231200','201509231300',30,1,'',0,0)";
		// db.execSQL(insertSql);
		// insertSql =
		// "insert into t_parkrecord(recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isprint) values(1,1,1,'','X23456','blue','201509231300','201509231330',30,1,'',0,0)";
		// db.execSQL(insertSql);
		// String insertSql =
		// "insert into t_parkrecord(recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isupload,isprint) values(1,1,1,'','XAB123','blue','201510251200','',30,0,'',0,0)";
		// db.execSQL(insertSql);
		// insertSql =
		// "insert into t_parkrecord(recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isupload,isprint) values(1,1,1,'','XWD345','blue','201509241200','',30,0,'',0,0)";
		// db.execSQL(insertSql);
		// insertSql =
		// "insert into t_parkrecord(recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isprint) values(1,1,1,'','XGK098','blue','201509241300','',30,0,'',0,0)";
		// db.execSQL(insertSql);
		// insertSql =
		// "insert into t_parkrecord(recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isprint) values(1,1,1,'','XAB123','blue','201510251200','',30,2,'',0,0)";
		// db.execSQL(insertSql);
		// insertSql =
		// "insert into t_parkrecord(recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isprint) values(1,1,1,'','XWD345','blue','201509241200','',30,2,'',0,0)";
		// db.execSQL(insertSql);
		// insertSql =
		// "insert into t_parkrecord(recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isprint) values(1,1,1,'','XGK098','blue','201509241300','',30,2,'',0,0)";
		// db.execSQL(insertSql);
		String ct1Str = null;
		ct1Str = " Create TABLE if not exists  t_parkrecord  (tid integer PRIMARY KEY AUTOINCREMENT,recordid integer,recordno text,parkid integer,parkno text,hphm text,hphmcolor text,parktime text,leavetime text,fees REAL,status integer,filepath text,isupload int,isprint int)";
		db.execSQL(ct1Str);
		ct1Str = " Create  TABLE if not exists t_uploadevasion(tid int PRIMARY KEY,recordno text,hphm text,escapetime text,uploadtime text,uploadstatus int )";
		db.execSQL(ct1Str);
		ct1Str = " Create  TABLE if not exists t_uploadpay(tid int PRIMARY KEY,recordno text,hphm text,fare float,uploadtime text,uploadstatus int)";
		db.execSQL(ct1Str);
		closeDatabase();
	}

	private static void execSql(String sql, Object[] bindArgs) {
		openDatabase();
		db.beginTransaction();
		if (bindArgs == null || bindArgs.length == 0) {
			db.execSQL(sql);
		} else {
			db.execSQL(sql, bindArgs);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		closeDatabase();
	}

	public static boolean insertRecord(String recordno, String hphm,
			String hphmcolor, Date parktime, String filepath, int status,
			int isupload, int isprint) {
		boolean flg = false;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String datetext = sdf.format(parktime);
			String inSql = " insert into t_parkrecord(recordno,hphm,hphmcolor,parktime,filepath,status,isupload,isprint) values(?,?,?,?,?,?,?,?)";
			Object[] objArr = new Object[] { recordno, hphm, hphmcolor,
					datetext, filepath, status, isupload, isprint };
			execSql(inSql, objArr);
			flg = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flg;
	}

	public static boolean updateUploadFlag(int tid, int isupload) {
		boolean flg = false;
		String s1 = "update t_parkrecord set isupload=? where tid=?";
		execSql(s1, new Object[] { isupload, tid });
		flg = true;
		try {

		} catch (Exception er) {
			er.printStackTrace();
		}
		return flg;
	}

	public static boolean updateUploadPayFlag(int tid, int isupload) {
		boolean flg = false;
		String s1 = "update t_uploadpay set uploadstatus=? where tid=?";
		execSql(s1, new Object[] { isupload, tid });
		flg = true;
		try {

		} catch (Exception er) {
			er.printStackTrace();
		}
		return flg;
	}

	public static boolean updateUploadEscapeFlag(int tid, int isupload) {
		boolean flg = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String da = sdf.format(TimeUtil.getTime());
		String s1 = "update t_uploadevasion set uploadtime=?,uploadstatus=? where tid=?";
		execSql(s1, new Object[] { da, isupload, tid });
		flg = true;
		try {

		} catch (Exception er) {
			er.printStackTrace();
		}
		return flg;
	}

	public static boolean setEscapeRecord(int tid, String recordno,
			String hphm, Date escapetime) {
		boolean flg = false;
		String s1 = "update t_parkrecord set status=2 where tid=?";
		execSql(s1, new Object[] { tid });
		String s2 = "insert into t_uploadevasion(tid,recordno,hphm,escapetime,uploadstatus) values(?,?,?,?,0)";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String da = sdf.format(escapetime);
		execSql(s2, new Object[] { tid, recordno, hphm, da });
		flg = true;
		try {

		} catch (Exception er) {
			er.printStackTrace();
		}
		return flg;
	}

	public static boolean setPayRecord(int tid, String recordno, String hphm,
			float fare) {
		boolean flg = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String leavetime = sdf.format(TimeUtil.getTime());
		String s1 = "update t_parkrecord set status=1,leavetime=?,fees=? where tid=?";
		execSql(s1, new Object[] { leavetime, fare, tid });
		String s2 = "insert into t_uploadpay(tid,recordno,hphm,fare,uploadtime,uploadstatus) values(?,?,?,?,?,0)";

		execSql(s2, new Object[] { tid, recordno, hphm, fare, leavetime });
		flg = true;
		try {

		} catch (Exception er) {
			er.printStackTrace();
		}
		return flg;
	}

	public static List<ParkRecord> queryNeedUpload(int limit) {
		openDatabase();
		db.beginTransaction();
		String sql1 = "select recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isprint,tid from t_parkrecord where status=0 and isupload=0 order by parktime limit 0,?";
		Cursor cur = db.rawQuery(sql1, new String[] { limit + "" });
		List<ParkRecord> list = new ArrayList<ParkRecord>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		while (cur.moveToNext()) {
			ParkRecord rec = new ParkRecord();
			rec.setRecoidid(cur.getInt(0));
			rec.setRecordno(cur.getString(1));
			rec.setParkid(cur.getInt(2));
			rec.setParkno(cur.getString(3));
			rec.setHphm(cur.getString(4));
			rec.setHphmcolor(cur.getString(5));
			try {
				if (cur.getString(6) != null && !cur.getString(6).equals("")) {
					rec.setParktime(sdf.parse(cur.getString(6)));
				}
				if (cur.getString(7) != null && !cur.getString(7).equals("")) {
					rec.setLeavetime(sdf.parse(cur.getString(7)));
				}

			} catch (Exception er) {
				er.printStackTrace();
			}
			rec.setFees(cur.getFloat(8));
			rec.setStatus(cur.getInt(9));
			rec.setFilepath(cur.getString(10));
			rec.setIsprint(cur.getInt(11));
			rec.setTid(cur.getInt(12));
			list.add(rec);
		}
		db.endTransaction();
		cur.close();
		closeDatabase();
		return list;
	}

	public static List<PayRecord> queryNeedUploadPay(int limit) {
		openDatabase();
		db.beginTransaction();
		String sql1 = "select tid,recordno,hphm,fare,uploadtime,uploadstatus from t_uploadpay where uploadstatus=0 order by uploadtime limit 0,?";
		Cursor cur = db.rawQuery(sql1, new String[] { limit + "" });
		List<PayRecord> list = new ArrayList<PayRecord>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		while (cur.moveToNext()) {
			PayRecord par = new PayRecord();
			par.setTid(cur.getInt(0));
			par.setRecordno(cur.getString(1));
			par.setHphm(cur.getString(2));
			par.setFare(cur.getFloat(3));
			try {
				if (cur.getString(4) != null && !cur.getString(4).equals("")) {
					par.setUploadtime(sdf.parse(cur.getString(4)));
				}
			} catch (Exception er) {
				er.printStackTrace();
			}
			par.setUploadstatus(0);
			list.add(par);
		}
		db.endTransaction();
		cur.close();
		closeDatabase();
		return list;
	}

	public static List<EscapeRecord> queryNeedUploadEvasion(int limit) {
		openDatabase();
		db.beginTransaction();
		String sql1 = " select t.tid,t.recordno,t.hphm,t.escapetime,t.uploadtime,t.uploadstatus,s.filepath from t_uploadevasion as  t,t_parkrecord as  s where t.uploadstatus=0 and t.tid=s.tid order by t.escapetime limit 0,?";
		Cursor cur = db.rawQuery(sql1, new String[] { limit + "" });
		List<EscapeRecord> list = new ArrayList<EscapeRecord>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		while (cur.moveToNext()) {

			EscapeRecord er = new EscapeRecord();
			er.setTid(cur.getInt(0));
			er.setRecordno(cur.getString(1));
			er.setHphm(cur.getString(2));

			try {
				if (cur.getString(3) != null && !cur.getString(3).equals("")) {
					er.setEscapetime(sdf.parse(cur.getString(3)));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			er.setUploadstatus(0);
			er.setFilepath(cur.getString(6));
			list.add(er);
		}
		db.endTransaction();
		cur.close();
		closeDatabase();
		return list;
	}

	public static List<ParkRecord> queryRecordList(String payStatus, int limit,
			String hphmStr) {
		openDatabase();
		db.beginTransaction();
		Cursor cur = null;
		String sqlStr = null;
		if (hphmStr == null || hphmStr.equals("")) {
			sqlStr = "select recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isprint,tid from t_parkrecord where   status=?  order by parktime limit 0,? ";
			cur = db.rawQuery(sqlStr, new String[] { payStatus, limit + "" });
		} else {
			hphmStr = "%" + hphmStr + "%";
			sqlStr = "select recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isprint,tid from t_parkrecord where   status=?  and hphm like ? order by parktime limit 0,? ";
			cur = db.rawQuery(sqlStr, new String[] { payStatus, hphmStr,
					limit + "" });
		}
		List<ParkRecord> list = new ArrayList<ParkRecord>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		while (cur.moveToNext()) {
			ParkRecord rec = new ParkRecord();
			rec.setRecoidid(cur.getInt(0));
			rec.setRecordno(cur.getString(1));
			rec.setParkid(cur.getInt(2));
			rec.setParkno(cur.getString(3));
			rec.setHphm(cur.getString(4));
			rec.setHphmcolor(cur.getString(5));
			try {
				if (cur.getString(6) != null && !cur.getString(6).equals("")) {
					rec.setParktime(sdf.parse(cur.getString(6)));
				}
				if (cur.getString(7) != null && !cur.getString(7).equals("")) {
					rec.setLeavetime(sdf.parse(cur.getString(7)));
				}

			} catch (Exception er) {
				er.printStackTrace();
			}
			rec.setFees(cur.getFloat(8));
			rec.setStatus(cur.getInt(9));
			rec.setFilepath(cur.getString(10));
			rec.setIsprint(cur.getInt(11));
			rec.setTid(cur.getInt(12));
			list.add(rec);
		}
		db.endTransaction();
		cur.close();
		closeDatabase();
		return list;
	}

	public static ParkRecord queryRecordByHphm(String hphm) {
		openDatabase();
		db.beginTransaction();
		String sql1 = "select recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isprint,tid from t_parkrecord where hphm=? order by parktime desc limit 0,1";
		Cursor cur = db.rawQuery(sql1, new String[] { hphm });
		List<ParkRecord> list = new ArrayList<ParkRecord>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParkRecord rec = null;
		while (cur.moveToNext()) {
			rec = new ParkRecord();
			rec.setRecoidid(cur.getInt(0));
			rec.setRecordno(cur.getString(1));
			rec.setParkid(cur.getInt(2));
			rec.setParkno(cur.getString(3));
			rec.setHphm(cur.getString(4));
			rec.setHphmcolor(cur.getString(5));
			try {
				if (cur.getString(6) != null && !cur.getString(6).equals("")) {
					rec.setParktime(sdf.parse(cur.getString(6)));
				}
				if (cur.getString(7) != null && !cur.getString(7).equals("")) {
					rec.setLeavetime(sdf.parse(cur.getString(7)));
				}

			} catch (Exception er) {
				er.printStackTrace();
			}
			rec.setFees(cur.getFloat(8));
			rec.setStatus(cur.getInt(9));
			rec.setFilepath(cur.getString(10));
			rec.setIsprint(cur.getInt(11));
			rec.setTid(cur.getInt(12));
		}
		db.endTransaction();
		cur.close();
		closeDatabase();

		return rec;
	}

	public static ParkRecord queryRecordByTid(int tid) {
		openDatabase();
		db.beginTransaction();
		String sql1 = "select recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isprint,tid from t_parkrecord where tid=? order by parktime desc limit 0,1";
		Cursor cur = db.rawQuery(sql1, new String[] { tid + "" });
		List<ParkRecord> list = new ArrayList<ParkRecord>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParkRecord rec = null;
		while (cur.moveToNext()) {
			rec = new ParkRecord();
			rec.setRecoidid(cur.getInt(0));
			rec.setRecordno(cur.getString(1));
			rec.setParkid(cur.getInt(2));
			rec.setParkno(cur.getString(3));
			rec.setHphm(cur.getString(4));
			rec.setHphmcolor(cur.getString(5));
			try {
				if (cur.getString(6) != null && !cur.getString(6).equals("")) {
					rec.setParktime(sdf.parse(cur.getString(6)));
				}
				if (cur.getString(7) != null && !cur.getString(7).equals("")) {
					rec.setLeavetime(sdf.parse(cur.getString(7)));
				}

			} catch (Exception er) {
				er.printStackTrace();
			}
			rec.setFees(cur.getFloat(8));
			rec.setStatus(cur.getInt(9));
			rec.setFilepath(cur.getString(10));
			rec.setIsprint(cur.getInt(11));
			rec.setTid(cur.getInt(12));
		}
		db.endTransaction();
		cur.close();
		closeDatabase();

		return rec;
	}

	public static List<ParkRecord> queryInOrOut30Min(int type, int limit) {
		openDatabase();
		db.beginTransaction();
		String sql1 = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String pt = sdf.format(TimeUtil.getTime());
		// in 30s
		if (type == 0) {
			sql1 = "select recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isprint,tid from t_parkrecord where (strftime('%s',?)-strftime('%s',parktime))<1800 and status=0 order by parktime limit 0,?";
		}
		// out 30s
		else {
			sql1 = "select recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isprint,tid from t_parkrecord where (strftime('%s',?)-strftime('%s',parktime))>=1800 and status =0 order by parktime limit 0,?";
		}
		Cursor cur = db.rawQuery(sql1, new String[] { pt, limit + "" });
		List<ParkRecord> list = new ArrayList<ParkRecord>();
		while (cur.moveToNext()) {
			ParkRecord rec = new ParkRecord();
			rec.setRecoidid(cur.getInt(0));
			rec.setRecordno(cur.getString(1));
			rec.setParkid(cur.getInt(2));
			rec.setParkno(cur.getString(3));
			rec.setHphm(cur.getString(4));
			rec.setHphmcolor(cur.getString(5));
			try {
				if (cur.getString(6) != null && !cur.getString(6).equals("")) {
					rec.setParktime(sdf.parse(cur.getString(6)));
				}
				if (cur.getString(7) != null && !cur.getString(7).equals("")) {
					rec.setLeavetime(sdf.parse(cur.getString(7)));
				}

			} catch (Exception er) {
				er.printStackTrace();
			}
			rec.setFees(cur.getFloat(8));
			rec.setStatus(cur.getInt(9));
			rec.setFilepath(cur.getString(10));
			rec.setIsprint(cur.getInt(11));
			rec.setTid(cur.getInt(12));
			list.add(rec);
		}
		db.endTransaction();
		cur.close();
		closeDatabase();
		return list;
	}

	private static void closeDatabase() {
		if (db != null) {
			db.close();
			db = null;
		}
	}
}
