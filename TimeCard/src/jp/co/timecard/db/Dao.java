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

	/*
	 * 月次画面の勤怠情報を取得
	 * */
	public String[] MonthlyList(String date){
		SQLiteDatabase db = helper.getWritableDatabase();
		String[] bindStr = new String[]{date};
		String kintai_list[] = new String[3];

		// まずは勤怠ID取得
		Cursor c1 = db.rawQuery("SELECT mk.kintai_id, mk.kintai_date FROM " +
				"mst_kintai mk" + " WHERE mk." + DbConstants.COLUMN_KINTAI_DATE + "=?", bindStr);
		if (c1.moveToFirst()){
			String kintai_id = c1.getString(0);

			// 出勤時刻のみ打刻されていた場合の出勤時刻取得
			Cursor c2 = db.rawQuery("SELECT ma.attendance_time FROM mst_attendance ma WHERE ma.kintai_id=" + kintai_id,null);
			if (c2.moveToFirst()){
				if (c2.getCount() != 0) {
					kintai_list[0] = c2.getString(c2.getColumnIndex("attendance_time"));
				}else{
					kintai_list[0] = "";
				}
			}

			//勤怠IDを元に退勤時刻・休憩時間を取得
			Cursor c3 = db.rawQuery("SELECT ml.leaveoffice_time, mb.break_time FROM" +
					" mst_attendance ma, mst_leaveoffice ml, mst_break mb WHERE ma.kintai_id=" + kintai_id + 
					" AND ma.kintai_id = ml.kintai_id AND ma.kintai_id = mb.kintai_id", null);

			if (c3.moveToFirst()){
				kintai_list[1] = c3.getString(c3.getColumnIndex("leaveoffice_time"));
				kintai_list[2] = c3.getString(c3.getColumnIndex("break_time"));
			}else{
				kintai_list[1] = "";
				kintai_list[2] = "";
			}
		} else {
			kintai_list[0] = "";
			kintai_list[1] = "";
			kintai_list[2] = "";
		}
		db.close();
		return kintai_list;
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