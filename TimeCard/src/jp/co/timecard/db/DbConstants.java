package jp.co.timecard.db;

/**
 * データベース関連の定数を定義する。
 * @version 1.0
 * @author Tomohiro Tano
 */
public class DbConstants {

	//データベースの名前とバージョン
    public static final String DATABASE_NAME    = "TimeCardDB";
    public static final int    DATABASE_VERSION = 1;

    //作成、使用するテーブル名とカラム名
    public  static final String TABLE_NAME1  = "mst_kintai";
    public  static final String TABLE_NAME2  = "mst_attendance";
    public  static final String TABLE_NAME3  = "mst_leaveoffice";
    public  static final String TABLE_NAME4  = "mst_break";
    
    public  static final String COLUMN_KINTAI_ID  = "kintai_id";
    public  static final String COLUMN_KINTAI_DATE  = "kintai_date";
    
    public  static final String COLUMN_ATTENDANCE_ID = "attendance_id";
    public  static final String COLUMN_ATTENDANCE_DATE = "attendance_date";
    public  static final String COLUMN_ATTENDANCE_TIME = "attendance_time";
    public  static final String COLUMN_REGIST_DATETIME = "regist_datetime";
    public  static final String COLUMN_UPDATE_DATETIME = "update_datetime";

    public  static final String COLUMN_LEAVEOFFICE_ID = "leaveoffice_id";
    public  static final String COLUMN_LEAVEOFFICE_DATE = "leaveoffice_date";
    public  static final String COLUMN_LEAVEOFFICE_TIME = "leaveoffice_time";
    
    public  static final String COLUMN_BREAK_ID = "break_id";
    public  static final String COLUMN_BREAK_TIME = "break_time";
    
    //SQL CREATE文(TABLE1)
    // TODO AUTOINCREMENTでOK??
    public static final String CREATE_TABLE1 =
            "CREATE TABLE " + TABLE_NAME1  + " ("
                            + COLUMN_KINTAI_ID  + " INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL,"
                            + COLUMN_KINTAI_DATE + " TEXT NOT NULL"
                            + " );";
    public static final String CREATE_TABLE2 =
            "CREATE TABLE " + TABLE_NAME2  + " ("
                            + COLUMN_ATTENDANCE_ID  + " INTEGER PRIMARY KEY NOT NULL,"
                            + COLUMN_KINTAI_ID + " INTEGER NOT NULL, "
                            + COLUMN_ATTENDANCE_DATE + " TEXT NOT NULL, "
                            + COLUMN_ATTENDANCE_TIME + " TEXT DAFAULT '9:00' NOT NULL, "
                            + COLUMN_REGIST_DATETIME + " TEXT NOT NULL, "
                            + COLUMN_UPDATE_DATETIME + " TEXT NOT NULL"
                            + " );";
    public static final String CREATE_TABLE3 =
            "CREATE TABLE " + TABLE_NAME3  + " ("
                            + COLUMN_LEAVEOFFICE_ID  + " INTEGER PRIMARY KEY NOT NULL,"
                            + COLUMN_KINTAI_ID + " INTEGER NOT NULL, "
                            + COLUMN_LEAVEOFFICE_DATE + " TEXT NOT NULL, "
                            + COLUMN_LEAVEOFFICE_TIME + " TEXT DAFAULT '17:30' NOT NULL, "
                            + COLUMN_REGIST_DATETIME + " TEXT NOT NULL, "
                            + COLUMN_UPDATE_DATETIME + " TEXT NOT NULL"
                            + " );";
    public static final String CREATE_TABLE4 =
            "CREATE TABLE " + TABLE_NAME4  + " ("
                            + COLUMN_BREAK_ID  + " INTEGER PRIMARY KEY NOT NULL,"
                            + COLUMN_KINTAI_ID + " INTEGER NOT NULL, "
                            + COLUMN_BREAK_TIME + " TEXT DAFAULT '01:00' NOT NULL, "
                            + COLUMN_REGIST_DATETIME + " TEXT NOT NULL, "
                            + COLUMN_UPDATE_DATETIME + " TEXT NOT NULL"
                            + " );"; 
    
    
    //SQL SELECT文
    public static final String SQL_SELECT_ATTENDANC =
    		" SELECT " + COLUMN_ATTENDANCE_DATE + ", " + COLUMN_ATTENDANCE_TIME +
    		" FROM "   + TABLE_NAME1  + ", " + TABLE_NAME2 +
    		" WHERE "  + COLUMN_KINTAI_ID  + " = ? AND " +
    		TABLE_NAME1 + "." + COLUMN_KINTAI_ID + "=" + TABLE_NAME2 + "." + COLUMN_KINTAI_ID +
    	    ";";
    
    public static final String SQL_SELECT_LEAVEOFFICE =
    		" SELECT " + COLUMN_LEAVEOFFICE_DATE + ", " + COLUMN_LEAVEOFFICE_TIME +
    		" FROM "   + TABLE_NAME1  + ", " + TABLE_NAME3 +
    		" WHERE "  + COLUMN_KINTAI_ID  + " = ? AND " +
    		TABLE_NAME1 + "." + COLUMN_KINTAI_ID + "=" + TABLE_NAME3 + "." + COLUMN_KINTAI_ID +
    	    ";";

    public static final String SQL_SELECT_BREAK =
    		" SELECT " + COLUMN_BREAK_TIME +
    		" FROM "   + TABLE_NAME1  + ", " + TABLE_NAME4 +
    		" WHERE "  + COLUMN_KINTAI_ID  + " = ? AND " +
    		TABLE_NAME1 + "." + COLUMN_KINTAI_ID + "=" + TABLE_NAME4 + "." + COLUMN_KINTAI_ID +
    	    ";";
    
    
    //SQL DROP TABLE文
    public static final String DATABASE_UPDATE1  ="DROP TABLE IF EXISTS " + TABLE_NAME1;
    public static final String DATABASE_UPDATE2  ="DROP TABLE IF EXISTS " + TABLE_NAME2;
    public static final String DATABASE_UPDATE3  ="DROP TABLE IF EXISTS " + TABLE_NAME3;
    public static final String DATABASE_UPDATE4  ="DROP TABLE IF EXISTS " + TABLE_NAME4;
    
}
