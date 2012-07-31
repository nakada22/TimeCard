package jp.co.timecard;

/**
 * データベース関連の定数を定義する。
 * @version 1.0
 * @author Tomohiro Tano
 */
public class DbConstants {

	//データベースの名前とバージョン
    public static final String DATABASE_NAME    = "twitter_client";
    public static final int    DATABASE_VERSION = 1;

    //作成、使用するテーブル名とカラム名
    public  static final String TABLE_NAME  = "image_cache";
    public  static final String COLUMN_URI  = "uri";
    public  static final String COLUMN_ICON = "icon";

    //SQL CREATE文
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME  + " ("
                            + COLUMN_URI  + " TEXT PRIMARY KEY,"
                            + COLUMN_ICON + " BLOB"
                            + " );";

    //SQL SELECT文
    public static final String SQL_SELECT =
    		" SELECT "      + COLUMN_ICON +
    		" FROM "        + TABLE_NAME  +
    		" WHERE "       + COLUMN_URI  +
    	    " = ? ;";

    //SQL DROP TABLE文
    public static final String DATABASE_UPDATE  ="DROP TABLE IF EXISTS " + TABLE_NAME;
}
