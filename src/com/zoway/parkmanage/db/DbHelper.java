package com.zoway.parkmanage.db;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zoway.parkmanage.bean.EscapeRecord;
import com.zoway.parkmanage.bean.IgnoreRecord;
import com.zoway.parkmanage.bean.ParkRecord;
import com.zoway.parkmanage.bean.ParkRecordByTags;
import com.zoway.parkmanage.bean.PayRecord;
import com.zoway.parkmanage.utils.PathUtils;
import com.zoway.parkmanage.utils.TimeUtil;

public class DbHelper {

	private static SQLiteDatabase db = null;
	private static String dbpath = PathUtils.getSdPath() + File.separator
			+ "parkmanage.db";

	private static synchronized void openDatabase() {
		if (db == null) {
			db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
		}
	}

	public static synchronized void createTables() {
		openDatabase();
		String ct1Str = null;
		ct1Str = " Create TABLE if not exists  t_parkrecord  (tid integer PRIMARY KEY AUTOINCREMENT,recordid integer,recordno text,parkid integer,parkno text,hphm text,hpzl text,hphmcolor text,parktime text,leavetime text,fees REAL,status integer,filepath text,isupload int,isprint int)";
		db.execSQL(ct1Str);
		ct1Str = " Create  TABLE if not exists t_uploadevasion(tid int PRIMARY KEY,recordno text,hphm text,escapetime text,uploadtime text,uploadstatus int )";
		db.execSQL(ct1Str);
		ct1Str = " Create  TABLE if not exists t_uploadpay(tid int PRIMARY KEY,recordno text,hphm text,fare float,uploadtime text,uploadstatus int)";
		db.execSQL(ct1Str);
		ct1Str = " Create  TABLE if not exists t_uploadignore(tid int PRIMARY KEY,recordno text,hphm text,ignoretime text,uploadtime text,uploadstatus int )";
		db.execSQL(ct1Str);
		ct1Str = " Create TABLE if not exists  t_parkrecord_bytags (tid integer PRIMARY KEY AUTOINCREMENT,recordno text,parkno text,parktime text,status int)";
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

	public static synchronized boolean insertRecord(String recordno,
			String hphm, String hpzl, String hphmcolor, Date parktime,
			String filepath, int status, int isupload, int isprint) {
		boolean flg = false;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String datetext = sdf.format(parktime);
			String inSql = " insert into t_parkrecord(recordno,hphm,hpzl,hphmcolor,parktime,filepath,status,isupload,isprint) values(?,?,?,?,?,?,?,?,?)";
			Object[] objArr = new Object[] { recordno, hphm, hpzl, hphmcolor,
					datetext, filepath, status, isupload, isprint };
			execSql(inSql, objArr);
			flg = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flg;
	}

	public static synchronized boolean updateUploadFlag(int tid, int isupload) {
		boolean flg = false;
		try {
			String s1 = "update t_parkrecord set isupload=? where tid=?";
			execSql(s1, new Object[] { isupload, tid });
			flg = true;
		} catch (Exception er) {
			er.printStackTrace();
		}
		return flg;
	}

	public static synchronized boolean updateUploadPayFlag(int tid, int isupload) {
		boolean flg = false;
		try {
			String s1 = "update t_uploadpay set uploadstatus=? where tid=?";
			execSql(s1, new Object[] { isupload, tid });
			flg = true;
		} catch (Exception er) {
			er.printStackTrace();
		}
		return flg;
	}

	public static synchronized boolean updateUploadEscapeFlag(int tid,
			int isupload) {
		boolean flg = false;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String da = sdf.format(TimeUtil.getTime());
			String s1 = "update t_uploadevasion set uploadtime=?,uploadstatus=? where tid=?";
			execSql(s1, new Object[] { da, isupload, tid });
			flg = true;
		} catch (Exception er) {
			er.printStackTrace();
		}
		return flg;
	}

	public static synchronized boolean updateUploadIgnoreFlag(int tid,
			int isupload) {
		boolean flg = false;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String da = sdf.format(TimeUtil.getTime());
			String s1 = "update t_uploadignore set uploadtime=?,uploadstatus=? where tid=?";
			execSql(s1, new Object[] { da, isupload, tid });
			flg = true;
		} catch (Exception er) {
			er.printStackTrace();
		}
		return flg;
	}

	public static synchronized boolean setEscapeRecord(int tid,
			String recordno, String hphm, Date escapetime) {
		boolean flg = false;
		try {
			String s1 = "update t_parkrecord set status=2 where tid=?";
			execSql(s1, new Object[] { tid });
			String s2 = "insert into t_uploadevasion(tid,recordno,hphm,escapetime,uploadstatus) values(?,?,?,?,0)";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String da = sdf.format(escapetime);
			execSql(s2, new Object[] { tid, recordno, hphm, da });
			flg = true;
		} catch (Exception er) {
			er.printStackTrace();
		}
		return flg;
	}

	public static synchronized boolean setIgnoreRecord(int tid,
			String recordno, String hphm, Date ignoretime) {
		boolean flg = false;
		try {
			String s1 = "update t_parkrecord set status=3 where tid=?";
			execSql(s1, new Object[] { tid });
			String s2 = "insert into t_uploadignore(tid,recordno,hphm,ignoretime,uploadstatus) values(?,?,?,?,0)";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String da = sdf.format(ignoretime);
			execSql(s2, new Object[] { tid, recordno, hphm, da });
			flg = true;
		} catch (Exception er) {
			er.printStackTrace();
		}
		return flg;
	}

	public static synchronized boolean setPayRecord(int tid, String recordno,
			String hphm, float fare) {
		boolean flg = false;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String leavetime = sdf.format(TimeUtil.getTime());
			String s1 = "update t_parkrecord set status=1,leavetime=?,fees=? where tid=?";
			execSql(s1, new Object[] { leavetime, fare, tid });
			String s2 = "insert into t_uploadpay(tid,recordno,hphm,fare,uploadtime,uploadstatus) values(?,?,?,?,?,0)";

			execSql(s2, new Object[] { tid, recordno, hphm, fare, leavetime });
			flg = true;
		} catch (Exception er) {
			er.printStackTrace();
		}
		return flg;
	}

	public static synchronized List<ParkRecord> queryNeedUpload(int limit) {
		List<ParkRecord> list = new ArrayList<ParkRecord>();
		try {
			openDatabase();
			db.beginTransaction();
			String sql1 = "select recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isprint,tid,hpzl from t_parkrecord where status=0 and isupload=0 order by parktime limit 0,?";
			Cursor cur = db.rawQuery(sql1, new String[] { limit + "" });
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
					if (cur.getString(6) != null
							&& !cur.getString(6).equals("")) {
						rec.setParktime(sdf.parse(cur.getString(6)));
					}
					if (cur.getString(7) != null
							&& !cur.getString(7).equals("")) {
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
				rec.setHpzl(cur.getString(13));
				list.add(rec);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			cur.close();
			closeDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static synchronized List<PayRecord> queryNeedUploadPay(int limit) {
		List<PayRecord> list = new ArrayList<PayRecord>();
		try {
			openDatabase();
			db.beginTransaction();
			String sql1 = "select tid,recordno,hphm,fare,uploadtime,uploadstatus from t_uploadpay where uploadstatus=0 order by uploadtime limit 0,?";
			Cursor cur = db.rawQuery(sql1, new String[] { limit + "" });
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while (cur.moveToNext()) {
				PayRecord par = new PayRecord();
				par.setTid(cur.getInt(0));
				par.setRecordno(cur.getString(1));
				par.setHphm(cur.getString(2));
				par.setFare(cur.getFloat(3));
				try {
					if (cur.getString(4) != null
							&& !cur.getString(4).equals("")) {
						par.setUploadtime(sdf.parse(cur.getString(4)));
					}
				} catch (Exception er) {
					er.printStackTrace();
				}
				par.setUploadstatus(0);
				list.add(par);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			cur.close();
			closeDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static synchronized List<EscapeRecord> queryNeedUploadEvasion(
			int limit) {
		List<EscapeRecord> list = new ArrayList<EscapeRecord>();
		try {
			openDatabase();
			db.beginTransaction();
			String sql1 = " select t.tid,t.recordno,t.hphm,t.escapetime,t.uploadtime,t.uploadstatus,s.filepath from t_uploadevasion as  t,t_parkrecord as  s where t.uploadstatus=0 and t.tid=s.tid order by t.escapetime limit 0,?";
			Cursor cur = db.rawQuery(sql1, new String[] { limit + "" });
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while (cur.moveToNext()) {

				EscapeRecord er = new EscapeRecord();
				er.setTid(cur.getInt(0));
				er.setRecordno(cur.getString(1));
				er.setHphm(cur.getString(2));

				try {
					if (cur.getString(3) != null
							&& !cur.getString(3).equals("")) {
						er.setEscapetime(sdf.parse(cur.getString(3)));
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				er.setUploadstatus(0);
				er.setFilepath(cur.getString(6));
				list.add(er);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			cur.close();
			closeDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static synchronized List<IgnoreRecord> queryNeedUploadIgnore(
			int limit) {
		List<IgnoreRecord> list = new ArrayList<IgnoreRecord>();
		try {
			openDatabase();
			db.beginTransaction();
			String sql1 = " select t.tid,t.recordno,t.hphm,t.ignoretime,t.uploadtime,t.uploadstatus from t_uploadignore as  t where t.uploadstatus=0  order by t.ignoretime limit 0,?";
			Cursor cur = db.rawQuery(sql1, new String[] { limit + "" });
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while (cur.moveToNext()) {

				IgnoreRecord er = new IgnoreRecord();
				er.setTid(cur.getInt(0));
				er.setRecordno(cur.getString(1));
				er.setHphm(cur.getString(2));

				try {
					if (cur.getString(3) != null
							&& !cur.getString(3).equals("")) {
						er.setIgnoretime(sdf.parse(cur.getString(3)));
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				er.setUploadstatus(0);
				list.add(er);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			cur.close();
			closeDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static synchronized List<ParkRecord> queryRecordList(
			String payStatus, int limit, String order, String hphmStr) {
		List<ParkRecord> list = new ArrayList<ParkRecord>();
		try {
			String ord = "asc";
			if (order.equals("desc")) {
				ord = "desc";
			}
			openDatabase();
			db.beginTransaction();
			Cursor cur = null;
			String sqlStr = null;
			if (hphmStr == null || hphmStr.equals("")) {
				sqlStr = "select recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isprint,tid,hpzl from t_parkrecord where   status=?  order by parktime "
						+ ord + " limit 0,? ";
				cur = db.rawQuery(sqlStr,
						new String[] { payStatus, limit + "" });
			} else {
				hphmStr = "%" + hphmStr + "%";
				sqlStr = "select recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isprint,tid,hpzl from t_parkrecord where   status=?  and hphm like ? order by parktime "
						+ ord + " limit 0,? ";
				cur = db.rawQuery(sqlStr, new String[] { payStatus, hphmStr,
						limit + "" });
			}

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
					if (cur.getString(6) != null
							&& !cur.getString(6).equals("")) {
						rec.setParktime(sdf.parse(cur.getString(6)));
					}
					if (cur.getString(7) != null
							&& !cur.getString(7).equals("")) {
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
				rec.setHpzl(cur.getString(13));
				list.add(rec);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			cur.close();
			closeDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static synchronized ParkRecord queryRecordByHphm(String hphm) {
		ParkRecord rec = null;
		try {
			openDatabase();
			db.beginTransaction();
			String sql1 = "select recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isprint,tid,hpzl from t_parkrecord where hphm=? order by parktime desc limit 0,1";
			Cursor cur = db.rawQuery(sql1, new String[] { hphm });
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while (cur.moveToNext()) {
				rec = new ParkRecord();
				rec.setRecoidid(cur.getInt(0));
				rec.setRecordno(cur.getString(1));
				rec.setParkid(cur.getInt(2));
				rec.setParkno(cur.getString(3));
				rec.setHphm(cur.getString(4));
				rec.setHphmcolor(cur.getString(5));
				try {
					if (cur.getString(6) != null
							&& !cur.getString(6).equals("")) {
						rec.setParktime(sdf.parse(cur.getString(6)));
					}
					if (cur.getString(7) != null
							&& !cur.getString(7).equals("")) {
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
				rec.setHpzl(cur.getString(13));
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			cur.close();
			closeDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rec;
	}

	public static synchronized ParkRecord queryRecordByTid(int tid) {
		ParkRecord rec = null;
		try {
			openDatabase();
			db.beginTransaction();
			String sql1 = "select recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isprint,tid,hpzl from t_parkrecord where tid=? order by parktime desc limit 0,1";
			Cursor cur = db.rawQuery(sql1, new String[] { tid + "" });
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while (cur.moveToNext()) {
				rec = new ParkRecord();
				rec.setRecoidid(cur.getInt(0));
				rec.setRecordno(cur.getString(1));
				rec.setParkid(cur.getInt(2));
				rec.setParkno(cur.getString(3));
				rec.setHphm(cur.getString(4));
				rec.setHphmcolor(cur.getString(5));
				try {
					if (cur.getString(6) != null
							&& !cur.getString(6).equals("")) {
						rec.setParktime(sdf.parse(cur.getString(6)));
					}
					if (cur.getString(7) != null
							&& !cur.getString(7).equals("")) {
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
				rec.setHpzl(cur.getString(13));
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			cur.close();
			closeDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rec;
	}

	public static synchronized List<ParkRecord> queryInOrOut30Min(int type,
			int limit, String qryStr) {
		List<ParkRecord> list = new ArrayList<ParkRecord>();
		try {
			openDatabase();
			db.beginTransaction();
			String sql1 = null;
			Cursor cur = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String pt = sdf.format(TimeUtil.getTime());
			// in 30s
			if (type == 0) {
				if (qryStr == null || qryStr.equals("")) {
					sql1 = "select recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isprint,tid,hpzl from t_parkrecord where (strftime('%s',?)-strftime('%s',parktime))<1800 and status=0 order by parktime limit 0,?";
					cur = db.rawQuery(sql1, new String[] { pt, limit + "" });
				} else {
					qryStr = "%" + qryStr + "%";
					sql1 = "select recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isprint,tid,hpzl from t_parkrecord where (strftime('%s',?)-strftime('%s',parktime))<1800 and status=0  and hphm like ? order by parktime limit 0,?";
					cur = db.rawQuery(sql1, new String[] { pt, qryStr,
							limit + "" });
				}
			}
			// out 30s
			else {
				if (qryStr == null || qryStr.equals("")) {
					sql1 = "select recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isprint,tid,hpzl from t_parkrecord where (strftime('%s',?)-strftime('%s',parktime))>=1800 and status =0 order by parktime limit 0,?";
					cur = db.rawQuery(sql1, new String[] { pt, limit + "" });
				} else {
					qryStr = "%" + qryStr + "%";
					sql1 = "select recordid,recordno,parkid,parkno,hphm,hphmcolor,parktime,leavetime,fees,status,filepath,isprint,tid,hpzl from t_parkrecord where (strftime('%s',?)-strftime('%s',parktime))>=1800 and status =0 and hphm like ? order by parktime limit 0,?";
					cur = db.rawQuery(sql1, new String[] { pt, qryStr,
							limit + "" });
				}
			}

			while (cur.moveToNext()) {
				ParkRecord rec = new ParkRecord();
				rec.setRecoidid(cur.getInt(0));
				rec.setRecordno(cur.getString(1));
				rec.setParkid(cur.getInt(2));
				rec.setParkno(cur.getString(3));
				rec.setHphm(cur.getString(4));
				rec.setHphmcolor(cur.getString(5));
				try {
					if (cur.getString(6) != null
							&& !cur.getString(6).equals("")) {
						rec.setParktime(sdf.parse(cur.getString(6)));
					}
					if (cur.getString(7) != null
							&& !cur.getString(7).equals("")) {
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
				rec.setHpzl(cur.getString(13));
				list.add(rec);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			cur.close();
			closeDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static synchronized boolean insertTagsRecord(String recordno,
			String sno, Date parktime, int status) {
		boolean flg = false;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String datetext = sdf.format(parktime);
			String inSql = " insert into t_parkrecord_bytags(recordno,parkno,parktime,status) values(?,?,?,?)";
			Object[] objArr = new Object[] { recordno, sno, datetext, status };
			execSql(inSql, objArr);
			flg = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flg;
	}

	public static synchronized List<ParkRecordByTags> queryInOrOut30MinTasgsRecord(
			int status) {
		List<ParkRecordByTags> list = new ArrayList<ParkRecordByTags>();
		try {
			openDatabase();
			db.beginTransaction();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String datetext = sdf.format(new Date());
			String sql1 = "select recordno,parkno,parktime,status from t_parkrecord_bytags  where (strftime('%s',?)-strftime('%s',parktime))>=1380 and status = ? order by parktime asc limit 0,1";
			Cursor cur = db.rawQuery(sql1,
					new String[] { datetext, status + "" });
			while (cur.moveToNext()) {
				ParkRecordByTags rec = new ParkRecordByTags();
				rec.setRecordno(cur.getString(0));
				rec.setParkno(cur.getString(1));
				if (cur.getString(2) != null && !cur.getString(2).equals("")) {
					rec.setParktime(sdf.parse(cur.getString(2)));
				}
				rec.setStauts(cur.getInt(3));
				list.add(rec);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			closeDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private static synchronized void closeDatabase() {
		if (db != null) {
			db.close();
			db = null;
		}
	}
}
