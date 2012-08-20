package jp.co.timecard.db;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jp.co.timecard.R;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.CheckBox;
import android.widget.TextView;

public class TopDao {
	
	private static DbOpenHelper helper = null;
	
	public TopDao(Context context) {
		helper = new DbOpenHelper(context);
	}

	/*
	 * 各テーブルの登録前に、外部キーとして使用される勤怠ID登録
	 * 但しアプリ起動日付のデータがある時は、Insertしない
	 */
	public void preKintaiSave(String str) {
		// 勤怠マスタにデータがあるか、なければInsert
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		//db.execSQL("DELETE FROM mst_kintai;");
		
		Cursor c = null;
		c = db.query(true, DbConstants.TABLE_NAME1, null, 
	    		DbConstants.COLUMN_KINTAI_DATE + "='" + str + "'", null, null, null, null, null);

		//long ret;
		if (c.getCount() == 0) {
			// データが0件であれば勤怠マスタ登録
			try {
				cv.put(DbConstants.COLUMN_KINTAI_DATE, str);
				db.insert(DbConstants.TABLE_NAME1, null, cv);
			} finally {
				db.close();
			}
		}
	}
	
	/*
	 * 時刻設定マスタのデータ登録(アプリ起動時１回のみ)
	 * */
	public void preTimeSave(String str) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		//db.execSQL("DELETE FROM mst_initime;");
		Cursor c = null;
		c = db.query(true, DbConstants.TABLE_NAME5, null, 
	    		null, null, null, null, null, null);
		
		//long ret;
		if (c.getCount() == 0) {
			// データが0件であれば時刻設定マスタ登録
			try {
				cv.put(DbConstants.COLUMN_START_TIME, "09:00");
				cv.put(DbConstants.COLUMN_END_TIME, "17:30");
				cv.put(DbConstants.COLUMN_BREAK_TIME, "01:00");
				cv.put(DbConstants.COLUMN_REGIST_DATETIME, str);
				cv.put(DbConstants.COLUMN_UPDATE_DATETIME, str);
				db.insert(DbConstants.TABLE_NAME5, null, cv);
				
			} finally {
				db.close();
			}
		}
	}
	
	/*
	 * 休憩マスタ(mst_break)の当日日付データ登録(退勤ボタン押下時)
	 * mst_initimeの休憩時間を登録
	 * */
	public void preBreakSave(String kintai_date, String kintai_date_time) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		//db.execSQL("DELETE FROM mst_break;");
		
		Cursor c = null;
		c = db.rawQuery("SELECT mk.kintai_id, mi.break_time FROM " +
				"mst_kintai mk, mst_initime mi WHERE mk." + DbConstants.COLUMN_KINTAI_DATE + "='" + kintai_date + "'",null);

		if (c.moveToFirst()){
			String kintai_id = c.getString(c.getColumnIndex("kintai_id"));
			String break_time = c.getString(c.getColumnIndex("break_time"));
			
			Cursor c2 = db.rawQuery("SELECT * FROM " +
					"mst_break WHERE " + DbConstants.COLUMN_KINTAI_ID + "=" + kintai_id, null);
			
			// データがなければInsert
			if (c2.getCount() == 0) {
				try {
					cv.put(DbConstants.COLUMN_KINTAI_ID, kintai_id);
					cv.put(DbConstants.COLUMN_BREAK_TIME, break_time);
					cv.put(DbConstants.COLUMN_REGIST_DATETIME, kintai_date_time);
					cv.put(DbConstants.COLUMN_UPDATE_DATETIME, kintai_date_time);
					
					db.insert(DbConstants.TABLE_NAME4, null, cv);
				} finally {
					db.close();
				}
			}
		}
	}
	
	/*
	 * 出勤マスタ(mst_attendance)のデータ登録
	 * */
	public boolean AttendanceSave(String kintai_date, String kintai_date_time, TextView atd_time) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		//db.execSQL("DELETE FROM mst_attendance;");
		
		// Insertする謹怠ID取得
		Cursor c = null;
		c = db.rawQuery("SELECT mk.kintai_id FROM " +
				"mst_kintai mk" + " WHERE mk." + DbConstants.COLUMN_KINTAI_DATE + "='" + kintai_date + "'", null);
		
		if (c.moveToFirst()){
			String kintai_id = c.getString(c.getColumnIndex("kintai_id"));
			String ins_atd_time;
			
			// 現在時刻使用チェック時
			if (atd_time != null) {
				ins_atd_time = atd_time.getText().toString();// 画面表示時刻で登録
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");// 現在時刻で登録
				ins_atd_time = sdf.format(Calendar.getInstance().getTime());
			}
			
			Cursor c2 = db.rawQuery("SELECT * FROM " +
					"mst_attendance WHERE " + DbConstants.COLUMN_KINTAI_ID + "=" + kintai_id, null);
			
			// 既に退勤済の場合は、更新処理を行わない
			Cursor c3 = db.rawQuery("SELECT * FROM " +
					"mst_leaveoffice WHERE " + DbConstants.COLUMN_KINTAI_ID + "=" + kintai_id, null);
			
			if (c2.getCount() == 0) {
				// データがなければ新規登録
				try {
					cv.put(DbConstants.COLUMN_KINTAI_ID, kintai_id);
					cv.put(DbConstants.COLUMN_ATTENDANCE_DATE, kintai_date);
					cv.put(DbConstants.COLUMN_ATTENDANCE_TIME, ins_atd_time);
					cv.put(DbConstants.COLUMN_REGIST_DATETIME, kintai_date_time);
					cv.put(DbConstants.COLUMN_UPDATE_DATETIME, kintai_date_time);
					
					db.insert(DbConstants.TABLE_NAME2, null, cv);
				} finally {
					db.close();
				}
			} else {
				
				// 既に出勤時刻登録済の場合は、新たな時刻で更新
				// また退勤済の場合は、更新処理を行わなわずToast表示
				if (c3.getCount() == 0) {
					try {
						cv.put(DbConstants.COLUMN_ATTENDANCE_TIME, ins_atd_time);
						cv.put(DbConstants.COLUMN_UPDATE_DATETIME, kintai_date_time);
						db.update(DbConstants.TABLE_NAME2, cv, DbConstants.COLUMN_KINTAI_ID + "=" + kintai_id, null);
					}finally {
						db.close();
					}
				} else {
					// Toast表示
					return false;
				}
				
			}
		}
		return true;
	}
	
	/*
	 * 始業時刻表示用メソッド
	 * */
	public void AttendanceTimeDisp(String atd_date, TextView tv) {
		SQLiteDatabase db = helper.getWritableDatabase();
		
		Cursor c = null;
		c = db.rawQuery("SELECT attendance_time FROM " +
				"mst_attendance " + " WHERE " + DbConstants.COLUMN_ATTENDANCE_DATE + "='" + atd_date + "'", null);
		if (c.moveToFirst()){
			String attendance_time = c.getString(c.getColumnIndex("attendance_time"));
			tv.setText(attendance_time);
		}
	}
	
	/*
	 * 退勤マスタ(mst_leaveoffice)のデータ登録
	 * */
	public void LeaveofficeSave(String kintai_date, String kintai_date_time, TextView leave_time) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		//db.execSQL("DELETE FROM mst_leaveoffice;");
		
		// Insertする謹怠ID取得
		Cursor c = null;
		c = db.rawQuery("SELECT mk.kintai_id FROM " +
				"mst_kintai mk" + " WHERE mk." + DbConstants.COLUMN_KINTAI_DATE + "='" + kintai_date + "'", null);
		
		if (c.moveToFirst()){
			String kintai_id = c.getString(c.getColumnIndex("kintai_id"));
			String ins_leave_time;
			
			// 現在時刻使用チェック時
			if (leave_time != null) {
				ins_leave_time = leave_time.getText().toString();// 画面表示時刻で登録
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");// 現在時刻で登録
				ins_leave_time = sdf.format(Calendar.getInstance().getTime());
			}
			
			Cursor c2 = db.rawQuery("SELECT * FROM " +
					"mst_leaveoffice WHERE " + DbConstants.COLUMN_KINTAI_ID + "=" + kintai_id, null);

			if (c2.getCount() == 0) {
				// データがなければ新規登録
				try {
					cv.put(DbConstants.COLUMN_KINTAI_ID, kintai_id);
					cv.put(DbConstants.COLUMN_LEAVEOFFICE_DATE, kintai_date);
					cv.put(DbConstants.COLUMN_LEAVEOFFICE_TIME, ins_leave_time);
					cv.put(DbConstants.COLUMN_REGIST_DATETIME, kintai_date_time);
					cv.put(DbConstants.COLUMN_UPDATE_DATETIME, kintai_date_time);
					db.insert(DbConstants.TABLE_NAME3, null, cv);
				} finally {
					db.close();
				}
			} else {
				// 既に退勤時刻登録済の場合は、新たな時刻で更新
				try {
					cv.put(DbConstants.COLUMN_LEAVEOFFICE_TIME, ins_leave_time);
					cv.put(DbConstants.COLUMN_UPDATE_DATETIME, kintai_date_time);
					db.update(DbConstants.TABLE_NAME3, cv, DbConstants.COLUMN_KINTAI_ID + "=" + kintai_id, null);
				}finally {
					db.close();
				}
			}
		}
	}
	
	/*
	 * 始業・終業・休憩時刻・合計時間 表示用メソッド
	 * */
	public void TopTimeDisp(String leave_date, 
							TextView tv1, TextView tv2, TextView tv3, TextView tv4) {
		SQLiteDatabase db = helper.getWritableDatabase();
		
		Cursor c = null;
		c = db.rawQuery("SELECT ml.leaveoffice_time, mb.break_time, ma.attendance_time" +
				" FROM mst_leaveoffice ml, mst_break mb, mst_attendance ma" +
				" WHERE ml." + DbConstants.COLUMN_LEAVEOFFICE_DATE + "='" + leave_date + "'" +
				" AND ml.kintai_id = mb.kintai_id" +
				" AND ml.kintai_id = ma.kintai_id", null);
		
		if (c.moveToFirst()){
			String leave_time = c.getString(c.getColumnIndex("leaveoffice_time"));
			String break_time = c.getString(c.getColumnIndex("break_time"));
			String attendance_time = c.getString(c.getColumnIndex("attendance_time"));
			
			tv1.setText(attendance_time);
			tv2.setText(leave_time);
			tv3.setText(break_time);
			
			// 合計時間を計算の上表示
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			try{
				Date at = sdf.parse(attendance_time);
				Date lt = sdf.parse(leave_time);
				Date bt = sdf.parse(break_time);
				long sumtime = lt.getTime() - at.getTime() - bt.getTime()+1000*60*60*6;
				//long sumtime = lt.getTime() - at.getTime() - bt.getTime();
				
				// 秒を時間に変換、合計時間がマイナスの時は00:00
				if (sumtime < 0) {
					tv4.setText("00:00");
				} else {
					tv4.setText(sdf.format(sumtime));
				}
				
			}catch(java.text.ParseException e){
				e.printStackTrace();
			}
		}
	}
}
