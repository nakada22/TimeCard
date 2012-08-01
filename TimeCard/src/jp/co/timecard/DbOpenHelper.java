package jp.co.timecard;

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
        db.execSQL(DbConstants.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DbConstants.DATABASE_UPDATE);
        onCreate(db);
    }
}