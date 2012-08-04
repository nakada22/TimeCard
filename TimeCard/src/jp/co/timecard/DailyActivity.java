package jp.co.timecard;

import java.util.Calendar;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class DailyActivity extends Activity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daily_main);
		
		final Context context = this;
		Button bAttendance = (Button)findViewById(R.id.button_set_attendance);
		bAttendance.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("debug","onClick");
				Calendar calendar = Calendar.getInstance();
				int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				boolean is24HourView = false;
				
				new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
					
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						// TODO DB登録
						TextView tv = (TextView) findViewById(R.id.daily_attendance);
						StringBuilder sb = new StringBuilder();
						sb.append(Integer.toString(hourOfDay));
						sb.append(":");
						sb.append(Integer.toString(minute));
						tv.setText(sb);
					}
				}, hourOfDay, minute, is24HourView).show();
			}
		});
	}
}





