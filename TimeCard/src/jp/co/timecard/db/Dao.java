package jp.co.timecard.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.timecard.db.mapping.Attendance;
import jp.co.timecard.db.mapping.Kintai;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Dao {

	private DbOpenHelper helper = null;

	public Dao(Context context) {
		helper = new DbOpenHelper(context);
	}

	// TODO 勤怠IDをまず登録・採番してから、そのIDを元に他のデータを登録する？？
	// TODO トランザクション
	// TODO saveから呼び出されるように　privateに
	// TODO insertしたら結果返すっていうメソッドがありそうなもんだけど。。。そしたらload()いらない。
	/*
	 * 各テーブルの登録前に、外部キーとして使用される勤怠IDを登録し、勤怠クラスを取得・返却する。
	 */
	public Kintai preSave(Date date) {
		SQLiteDatabase db = helper.getWritableDatabase();
		Kintai kintai = null;
		ContentValues cv = new ContentValues();
		try {
			cv.put(DbConstants.COLUMN_KINTAI_DATE, date.toString());
			db.insert(DbConstants.TABLE_NAME1, null, cv);
			kintai = load(date);
		} finally {
			db.close();
		}
		return kintai;
	}

	// TODO sdfパース処理はutilなものとして、切り出しといた方がよさそう
	/*
	 * 勤怠年月日を元に、勤怠クラスを返却する。
	 */
	private Kintai load(Date date) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Kintai kintai = null;

		try {
			Cursor cursor = db.query(DbConstants.TABLE_NAME1, null,
					DbConstants.COLUMN_KINTAI_DATE + "=?",
					new String[] { String.valueOf(date) }, null, null, null);
			if (cursor.moveToFirst()) {
				kintai = new Kintai();
				SimpleDateFormat sdf = new SimpleDateFormat();
				String stringId = cursor.getString(1);
				try {
					kintai.setKintaiDate(sdf.parse(stringId));
				} catch (ParseException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			} else {
				// TODO ちゃんと例外処理
				Log.d("debug", "cursor.moveToFirst()に失敗");
			}
		} finally {
			db.close();
		}
		return kintai;
	}

	// TODO マッピングクラス全部を引数としてとれたら良い
	public void save(Attendance attendance) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		try {
			// cv.put(key, value)
			// TODO insert or update
			db.insert(DbConstants.TABLE_NAME2, null, cv);

		} finally {
			db.close();
		}
	}
}
