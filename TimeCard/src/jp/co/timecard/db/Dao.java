package jp.co.timecard.db;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
	 * @param update_param Update時刻
	 * 
	 * */
	public void DailyUpdate(String[] update_param){
		SQLiteDatabase db = helper.getWritableDatabase();
		SimpleDateFormat timestamp_sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String currenttime = timestamp_sdf.format(Calendar.getInstance().getTime());
		String[] put_key = new String[]{"attendance_time",
				"leaveoffice_time","break_time"};
		String[] put_table = new String[]{"mst_attendance",
				"mst_leaveoffice","mst_break"};
		String[] put_pos = new String[]{"attendance_date",
				"leaveoffice_date","kintai_id"};
		
		try {
			// 画面表示されている日付より勤怠マスタよりkintai_idを取得してくる
			Cursor c = db.rawQuery("SELECT mk.kintai_id FROM " +
					"mst_kintai mk" + " WHERE mk.kintai_date=?", 
					new String[]{update_param[3]});
			Cursor c2 = db.rawQuery("SELECT ma.kintai_id FROM " +
					"mst_attendance ma" + " WHERE ma.attendance_date=?", 
					new String[]{update_param[3]});
			Cursor c3 = db.rawQuery("SELECT ml.kintai_id FROM " +
					"mst_leaveoffice ml" + " WHERE ml.leaveoffice_date=?", 
					new String[]{update_param[3]});
			
			// 表示されている日次画面の日付の出勤・退勤マスタのデータがあるかどうか確認
			// なければInsert、あればUpdateを行う。
			if (c.moveToFirst()){
				// 勤怠IDがあれば、休憩マスタのデータの有無の確認ができる
				String kintai_id = c.getString(0);
				Cursor c4 = db.rawQuery("SELECT mb.break_time FROM " +
						"mst_break mb" + " WHERE mb.kintai_id=?", 
						new String[]{kintai_id});
				
				int atd_cnt = c2.getCount(); 	// 出勤記録の有無(0:無 1:有)
				int lo_cnt = c3.getCount();  	// 退勤記録の有無(0:無 1:有)
				int break_cnt = c4.getCount();  // 休憩記録の有無(0:無 1:有)
				
				// 出勤・退勤・休憩記録の有無によってInsert、updateを分岐
				// 出勤記録処理
				ContentValues atd_cv = new ContentValues();
				if (atd_cnt == 0) {
					// 出勤記録がなければInsert
					atd_cv.put("kintai_id", kintai_id);
					atd_cv.put(put_pos[0], update_param[3]);
					atd_cv.put(put_key[0], update_param[0]);
					atd_cv.put("regist_datetime", currenttime);
					atd_cv.put("update_datetime", currenttime);
					db.insert(DbConstants.TABLE_NAME2, null, atd_cv);
				} else {
					// 出勤記録あればUpdate
					atd_cv.put(put_key[0], update_param[0]);
					db.update(put_table[0], atd_cv, put_pos[0]+"=?",
							new String[]{update_param[3]});
				}
				//Log.d("debug", String.valueOf(lo_cnt));
				
				// 退勤記録処理
				ContentValues lo_cv = new ContentValues();
				if (lo_cnt == 0) {
					// 退勤記録がなければInsert
					lo_cv.put("kintai_id", kintai_id);
					lo_cv.put(put_pos[1], update_param[3]);
					lo_cv.put(put_key[1], update_param[1]);
					lo_cv.put("regist_datetime", currenttime);
					lo_cv.put("update_datetime", currenttime);
					db.insert(DbConstants.TABLE_NAME3, null, lo_cv);
				} else {
					// 退勤記録あればUpdate
					lo_cv.put(put_key[1], update_param[1]);
					db.update(put_table[1], lo_cv, put_pos[1]+"=?",
							new String[]{update_param[3]});
				}
				// 休憩記録処理
				ContentValues break_cv = new ContentValues();
				if (break_cnt == 0) {
					// 休憩記録がなければInsert
					break_cv.put("kintai_id", kintai_id);
					break_cv.put(put_key[2], update_param[2]);
					break_cv.put("regist_datetime", currenttime);
					break_cv.put("update_datetime", currenttime);
					db.insert(DbConstants.TABLE_NAME4, null, break_cv);
				} else {
					// 休憩記録あればUpdate
					break_cv.put(put_key[2], update_param[2]);
					db.update(put_table[2], break_cv, put_pos[2]+"=?",
							new String[]{kintai_id});
				}
				
			} else {
				// 勤怠IDがない場合(当日日付でないや、未来日付等)全てをInsert
				// 勤怠マスタへの登録(勤怠id発行)
				ContentValues cv = new ContentValues();
				cv.put("kintai_date", update_param[3]);
				db.insert(DbConstants.TABLE_NAME1, null, cv);
				
				Cursor c4 = db.rawQuery("SELECT mk.kintai_id FROM " +
						"mst_kintai mk" + " WHERE mk.kintai_date=?", 
						new String[]{update_param[3]});
				
				if (c4.moveToFirst()){
					// 登録されていれば、その勤怠idを元に出勤・退勤・休憩マスタ登録
					for (int i = 0; i < put_key.length; i++){
						//Log.d("debug",update_param[3]);
						ContentValues cv2 = new ContentValues();
						// Insertデータ生成
						cv2.put("kintai_id", c4.getString(0));
						cv2.put(put_key[i], update_param[i]); // 時刻セット
						
						if (i != 2) {
							// 休憩マスタの以外の時だけ
							cv2.put(put_pos[i], update_param[3]);
						}
						cv2.put("regist_datetime", currenttime);
						cv2.put("update_datetime", currenttime);
						db.insert(put_table[i], null, cv2);
					}
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