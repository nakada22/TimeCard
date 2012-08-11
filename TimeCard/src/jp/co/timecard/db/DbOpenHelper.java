package jp.co.timecard.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * データベースの作成、更新を行う。
 * @version 1.0
 * @author Tomohiro Tano
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    /**
     * コンストラクタ
     * @param context コンテキスト
     */
    public DbOpenHelper(Context context) {
        super(context, DbConstants.DATABASE_NAME, null, DbConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbConstants.CREATE_TABLE1);
        db.execSQL(DbConstants.CREATE_TABLE2);
        db.execSQL(DbConstants.CREATE_TABLE3);
        db.execSQL(DbConstants.CREATE_TABLE4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String []DATABASE_UPDATE = {DbConstants.DATABASE_UPDATE1,
    								DbConstants.DATABASE_UPDATE2,
    								DbConstants.DATABASE_UPDATE3,
    								DbConstants.DATABASE_UPDATE4};
        for (int i=0; i<DATABASE_UPDATE.length; i++) {
        	db.execSQL(DATABASE_UPDATE[i]);
        }
        onCreate(db);
    }
}
