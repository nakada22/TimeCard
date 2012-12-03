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

	/*
	 * 日次画面の時刻でDB更新
	 * @param update_param[0] 出勤時刻
	 * 
	 * */
	public void DailyUpdate(String[] update_param){
		SQLiteDatabase db = helper.getWritableDatabase();
		
		try {
			String[] put_key = new String[]{"attendance_time",
					"leaveoffice_time","break_time"};
			String[] put_table = new String[]{"mst_attendance",
					"mst_leaveoffice","mst_break"};
			String[] put_pos = new String[]{"attendance_date",
					"leaveoffice_date","kintai_id"};
			
			// 画面表示されている日付よりkintai_idを取得してくる
			Cursor c = db.rawQuery("SELECT mk.kintai_id FROM " +
					"mst_kintai mk" + " WHERE mk.kintai_date=?", 
					new String[]{update_param[3]});
			
			if (c.moveToFirst()){
				String kintai_id = c.getString(0);
				//Log.d("debug",String.valueOf(put_key.length));
				
				// 休憩時間(i=2)だけはkintai_idをキーに更新しないといけない
				// TODO 出勤時刻のみ記録しかないと、正常にUpdateされない
				for (int i = 0; i < put_key.length; i++){
					//Log.d("debug",update_param[3]);
					ContentValues cv = new ContentValues();
					// 更新データ生成
					cv.put(put_key[i], update_param[i]);
					db.update(put_table[i], cv, put_pos[i]+"=?",
							(i != 2) ? new String[]{update_param[3]} : new String[]{kintai_id});
				}
			}
		} finally {
			db.close();
		}
	}
	
	/*
	 * 日次画面表示用のデフォルトの設定時刻取得メソッド
	 * */
	public String[] DailyDefaultTime(){
		SQLiteDatabase db = helper.getWritableDatabase();
		String iniparam[] = new String[3];
		try {
			Cursor c = db.rawQuery("SELECT mi.start_time, mi.end_time, mi.break_time " +
					"FROM " + "mst_initime mi", null);
			if (c.moveToFirst()){
				iniparam[0] = c.getString(0); 	// 始業時刻
				iniparam[1] = c.getString(1);	// 終業時刻
				iniparam[2] = c.getString(2);	// 休憩時刻
			}
		} finally {
			db.close();
		}
		//Log.d("debug", iniparam[2]);
		
		return iniparam;
	}
}