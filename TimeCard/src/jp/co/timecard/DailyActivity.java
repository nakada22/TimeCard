package jp.co.timecard;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;

import jp.co.timecard.db.Dao;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class DailyActivity extends Activity {

	private int mYear;
	private int mMonth;
	private int mDay;

	static private Calendar calendar;

	final int PRE_DAY = -1;
	final int NEX_DAY = 1;

	TextView tvDate;
	TextView tvAttendance;
	TextView tvLeave;
	TextView tvBreak;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daily_main);

		Intent i = getIntent();
		DailyState ds = (DailyState) i.getSerializableExtra("DailyState");

		//画面遷移直後の表示(月次画面リストで値がない場合は、mst_initimeの値をセットとする)
		final String date = ds.getDate();
		final Dao dao = new Dao(getApplicationContext());
		String[] default_param = dao.DailyDefaultTime(); // mst_initimeの値
		//Log.d("debug", );
		
		String attendance = null;
		String leave = null;
		String break_time = null;
		// 値がセットされていればセット、そうでなければ、DBの値セット
		if (ds.getAttendance() != null && ds.getAttendance().length()!= 0){
			attendance = ds.getAttendance();
		} else{attendance = default_param[0];}
		
		if (ds.getLeave() != null && ds.getLeave().length()!= 0){
			leave = ds.getLeave();
		} else{leave = default_param[1];}
		
		if (ds.getBreakTime() != null && ds.getBreakTime().length()!= 0){
			break_time = ds.getBreakTime();
		} else{break_time = default_param[2];}

		
		String str= new String(date);
		String[] strArray = str.split("/");

		calendar = Calendar.getInstance();
		calendar.set(Integer.valueOf(strArray[0]),
				Integer.valueOf(strArray[1]), Integer.valueOf(strArray[2]));

		// 表示するVIEW
		tvDate = (TextView) findViewById(R.id.day_target);
		tvAttendance = (TextView) findViewById(R.id.daily_attendance);
		tvLeave = (TextView) findViewById(R.id.daily_leave);
		tvBreak = (TextView) findViewById(R.id.daily_break);
		
		tvDate.setText(strArray[1]+"月" + strArray[2]+"日");
		tvAttendance.setText(attendance);
		tvLeave.setText(leave);
		tvBreak.setText(break_time);
		
		// 「前」ボタン
		Button bPre = (Button) findViewById(R.id.button_pre_day);
		bPre.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				setTargetDay(PRE_DAY);
			}
		});
		
		// 「次」ボタン
		Button bNex = (Button) findViewById(R.id.button_next_day);
		bNex.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				setTargetDay(NEX_DAY);
			}
		});
		
		// 始業時間「設定」ボタン
		Button bAttendance = (Button) findViewById(R.id.button_set_attendance);
		bAttendance.setOnClickListener(new MyListener(R.id.daily_attendance));
		
		// 終業時間「設定」ボタン
		Button bLeave = (Button) findViewById(R.id.button_set_leave);
		bLeave.setOnClickListener(new MyListener(R.id.daily_leave));
		
		// 休憩時間「設定」ボタン
		Button bBreak = (Button) findViewById(R.id.button_set_break);
		bBreak.setOnClickListener(new MyListener(R.id.daily_break));
		
		// 「登録」ボタン
		Button bRegist = (Button) findViewById(R.id.button_regist);
		bRegist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 出勤マスタ・退勤マスタ・休憩マスタへDB更新（画面で設定した時刻）
				dao.DailyUpdate(new String[]{(String) tvAttendance.getText(),
						(String) tvLeave.getText(),
						(String) tvBreak.getText(),date});

				Toast.makeText(getApplicationContext(), "謹怠記録を登録しました。", Toast.LENGTH_SHORT).show();
			}
		});
		
		// 「削除」ボタン
		Button bDelete = (Button) findViewById(R.id.button_delete);
		// 勤怠記録がない場合は、「削除」ボタンは非表示にする
		if(Arrays.binarySearch(dao.MonthlyList(date), "") == 1) {
			bDelete.setVisibility(View.INVISIBLE);
		}
		bDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 削除処理
				dao.DailyDelete(date);
				Toast.makeText(getApplicationContext(), "勤怠記録を削除しました", Toast.LENGTH_SHORT).show();
			}
		});
	}

	
	/**
	 * 表示対象日のデータをを取得。
	 * @param value 前か次ボタンの値
	 */
	public void setTargetDay(int value) {
		calendar.add(Calendar.DAY_OF_MONTH, value);
		mYear = calendar.get(Calendar.YEAR);
		mMonth = calendar.get(Calendar.MONTH);
		mDay = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(mYear, mMonth, mDay);

		// TODO DBから対象日の勤怠情報取得
		Dao dao = new Dao(this);
		String attendance = "9:00";
		String leave = "18:00";
		String break_time= "01:00";

		tvDate.setText(mMonth + "月" + mDay + "日");
		tvAttendance.setText(attendance);
		tvLeave.setText(leave);
		tvBreak.setText(break_time);
	}

	private class MyListener implements OnClickListener {
		int layout_id;
		int hourOfDay;
		int minute;
		boolean is24HourView = true;
		Dao dao = new Dao(getApplicationContext());
		Intent i = getIntent();
		DailyState ds = (DailyState) i.getSerializableExtra("DailyState");
		final String date = ds.getDate();
		
		public MyListener(int layout_id) {
			super();
			switch (layout_id) {
			case R.id.daily_attendance:
			case R.id.daily_leave:
				this.layout_id = layout_id;
				final TextView tv = (TextView) findViewById(layout_id);
				
				// 画面表示されている時刻取得
				String disptime = String.valueOf(tv.getText());
		    	hourOfDay = Integer.parseInt(disptime.substring(0, 2));
		    	minute = Integer.parseInt(disptime.substring(3, 5));
				break;
			case R.id.daily_break:
				this.layout_id = layout_id;
				// 休憩時間は画面表示されていないのでDBから取ってくる必要がある
				String break_time = dao.BreakTimeGet(date);
		    	hourOfDay = Integer.parseInt(break_time.substring(0, 2));
		    	minute = Integer.parseInt(break_time.substring(3, 5));
				break;
			}
		}

		// 各ボタンのリスナー
		public void onClick(View v) {
			TimePickerDialog timePickerDialog;
			final TextView tv = (TextView) findViewById(layout_id);
			String disptime = String.valueOf(tv.getText());
			
	    	hourOfDay = Integer.parseInt(disptime.substring(0, 2));
	    	minute = Integer.parseInt(disptime.substring(3, 5));
	    	is24HourView = true;

			TimePickerDialog.OnTimeSetListener TimeSetListener = new TimePickerDialog.OnTimeSetListener() {
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					// 設定した時刻をTPDialogの時刻に反映
					TextView tv = (TextView) findViewById(layout_id);
					DecimalFormat df = new DecimalFormat("00");
					StringBuilder sb = new StringBuilder()
					.append(df.format(hourOfDay))
					.append(":")
					.append(df.format(minute));
					tv.setText(sb);
				}
			};
			
			timePickerDialog = new TimePickerDialog(DailyActivity.this, TimeSetListener, hourOfDay, minute, is24HourView);
			// timePickerDialog時のメッセージ設定
			switch (layout_id) {
				case R.id.daily_attendance:
					timePickerDialog.setMessage("始業時間設定");
					break;
				case R.id.daily_leave:
					timePickerDialog.setMessage("終業時間設定");
					break;
				case R.id.daily_break:
					timePickerDialog.setMessage("休憩時間設定");
					break;
			}
			timePickerDialog.show();
		}
	}
}
