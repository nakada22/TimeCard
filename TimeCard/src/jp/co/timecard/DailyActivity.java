package jp.co.timecard;

import java.text.DecimalFormat;
import java.util.Calendar;

import jp.co.timecard.db.Dao;
import jp.co.timecard.db.mapping.Attendance;
import jp.co.timecard.db.mapping.Kintai;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class DailyActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daily_main);

		Button bAttendance = (Button) findViewById(R.id.button_set_attendance);
		bAttendance.setOnClickListener(new MyListener(R.id.daily_attendance));

		Button bLeave = (Button) findViewById(R.id.button_set_leave);
		bLeave.setOnClickListener(new MyListener(R.id.daily_leave));

		Button bBreak = (Button) findViewById(R.id.button_set_break);
		bBreak.setOnClickListener(new MyListener(R.id.daily_break));
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
				break;
			case R.id.daily_break:
				this.layout_id = layout_id;
				this.hourOfDay = 1;
				break;
			}
		}

		// 各ボタンのリスナー
		public void onClick(View v) {
			// attendance,leaveなら現在時刻とする breakなら初期化時の1を使用
			if (hourOfDay != 1) {
				Calendar calendar = Calendar.getInstance();
				hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
				minute = calendar.get(Calendar.MINUTE);
				is24HourView = true;
			}

			new TimePickerDialog(DailyActivity.this, new TPDialogListener(layout_id), hourOfDay, minute, is24HourView).show();
		}

		private class TPDialogListener implements TimePickerDialog.OnTimeSetListener {
			int layout_id;

			public TPDialogListener(int layout_id) {
				super();
				this.layout_id = layout_id;
			}

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				Dao dao = new Dao(DailyActivity.this);

				// TODO uniqなkintaiIdを採番する。
				Kintai kintai = new Kintai();
				kintai.init();

				Calendar calendar = Calendar.getInstance();

				// TODO setAll的なの欲しい
				Attendance attendance = new Attendance();
				attendance.setAttendanceId(1);
				attendance.setKintai(kintai);
				attendance.setAttendanceDate(calendar.getTime());
				attendance.setAttendanceTime(calendar.getTime());
				attendance.setRegistDatetime(calendar.getTime());

				dao.preSave(calendar.getTime());
				dao.save(attendance);

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
