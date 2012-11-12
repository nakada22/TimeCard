package jp.co.timecard;

import java.text.DecimalFormat;
import java.util.Calendar;

import jp.co.timecard.db.Dao;
import jp.co.timecard.db.mapping.Attendance;
import jp.co.timecard.db.mapping.Kintai;
import android.R.string;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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

		// 画面繊維直後の表示
		String date = ds.getDate();
		String attendance = ds.getAttendance();
		String leave = ds.getLeave();
		String break_time= ds.getBreakTime();

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

		Button bPre = (Button) findViewById(R.id.button_pre_day);
		bPre.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				setTargetDay(PRE_DAY);
			}
		});

		Button bNex = (Button) findViewById(R.id.button_next_day);
		bNex.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				setTargetDay(NEX_DAY);
			}
		});

		Button bAttendance = (Button) findViewById(R.id.button_set_attendance);
		bAttendance.setOnClickListener(new MyListener(R.id.daily_attendance));

		Button bLeave = (Button) findViewById(R.id.button_set_leave);
		bLeave.setOnClickListener(new MyListener(R.id.daily_leave));

		Button bBreak = (Button) findViewById(R.id.button_set_break);
		bBreak.setOnClickListener(new MyListener(R.id.daily_break));

		Button bRegist = (Button) findViewById(R.id.button_regist);
		bRegist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				Toast.makeText(getApplicationContext(), "DBに登録しました", Toast.LENGTH_LONG).show();
			}
		});
		Button bDelete = (Button) findViewById(R.id.button_delete);
		bDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自動生成されたメソッド・スタブ
				Toast.makeText(getApplicationContext(), "DBから削除しました", Toast.LENGTH_LONG).show();
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
		    	is24HourView = true;
				break;
			case R.id.daily_break:
				this.layout_id = layout_id;
				// TODO 休憩時間は画面表示されていないのでDBから取ってくる
				this.hourOfDay = 1; // 休憩時間(仮で1時間をセット)
				break;
			}
		}

		// 各ボタンのリスナー
		public void onClick(View v) {
			TimePickerDialog timePickerDialog;
			
			timePickerDialog = new TimePickerDialog(DailyActivity.this, 
					new TPDialogListener(layout_id), 
					hourOfDay, minute, is24HourView);

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
		
		private class TPDialogListener implements TimePickerDialog.OnTimeSetListener {
			int layout_id;

			public TPDialogListener(int layout_id) {
				super();
				this.layout_id = layout_id;
				
			}
			
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				
				// TODO 設定した時刻をTPDialogの時刻に反映させたい。
				TextView tv = (TextView) findViewById(layout_id);
				DecimalFormat df = new DecimalFormat("00");
				StringBuilder sb = new StringBuilder()
				.append(df.format(hourOfDay))
				.append(":")
				.append(df.format(minute));
				tv.setText(sb);
			}
		}
	}
}
