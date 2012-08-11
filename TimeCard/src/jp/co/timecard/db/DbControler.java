package jp.co.timecard.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * データベースとやりとりをする。
 * @author Tomohiro Tano
 */
public class DbControler {
	
	
	public void save(){}
	
	

    /**
     * データベースにプロフィール画像データを挿入。
     * @param context コンテキスト
     * @param uri     プロフィール画像のURL
     * @return        データベースに挿入した結果。
     */
//    public long setIcon(Context context, String uri) {
//
//        DbOpenHelper helper = new DbOpenHelper(context);
//        SQLiteDatabase db = helper.getWritableDatabase();
//
//        try {
//            //DBに挿入する行の各フィールドのデータを設定
//            ContentValues values = new ContentValues();
//            values.put(DbConstants.COLUMN_URI, uri);
//
//            //DBに行を挿入する
//            return db.insert(DbConstants.TABLE_NAME, null, values);
//
//        } finally {
//
//            if (db != null) {
//                db.close();
//            }
//        }
//    }
//
//    /**
//     * データベースからプロフィール画像データを取得。
//     * @param context コンテキスト
//     * @param _uri    取得したいプロフィール画像のURL文字列
//     * @return        画像があればbyteデータを返し、なければnullを返す。
//     */
//    public byte[] getIcon(Context context, String _uri) {
//
//        SQLiteDatabase db = null;
//        Cursor cursor = null;
//
//        try {
//            //DBを読み取り専用で開く
//            DbOpenHelper helper = new DbOpenHelper(context);
//            db = helper.getReadableDatabase();
//
//            //SELECT文を使用してマッチするレコードがあるか検索
//            String[] uri = {_uri};
//            cursor = db.rawQuery(DbConstants.SQL_SELECT, uri);
//
//            //マッチするレコードがあればbyteデータを取得する
//            if (cursor.moveToFirst()) {
//
//                return cursor.getBlob(0);
//
//            } else {
//
//                return null;
//            }
//
//        } finally {
//
//            if (cursor != null) {
//                cursor.close();
//            }
//
//            if (db != null) {
//                db.close();
//            }
//        }
//    }
}
