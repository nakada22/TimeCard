package jp.co.timecard;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 月次画面。
 * @author TomohiroTano
 */
 public class MonthlyActivity extends Activity implements View.OnClickListener {

	int mYear;
	int mMonth;

	final int PRE = -1;
	final int NEX = 1;

	/**
	 * 日ごとの勤怠状況をもつ。
	 */
	public class DailyState {
		String date;
		String attendance;
		String leave;
		String work_hour;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monthly_main);

		Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH) + 1;

		setMonTarget(0);

		createCalender();

		findViewById(R.id.button_pre_month).setOnClickListener(this);
		findViewById(R.id.button_next_month).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_pre_month:
			setMonTarget(-1);
			break;
		case R.id.button_next_month:
			setMonTarget(1);
			break;
		default:
			break;
		}
	}

	/**
	 * 表示対象の年月を取得。
	 */
	public void setMonTarget(int value) {

		if (mMonth == 1 && value == PRE) {
			mYear += value;
			mMonth = 12;
		} else if (mMonth == 12 && value == NEX) {
			mYear += value;
			mMonth = 1;
		} else {
			mMonth += value;
		}

		TextView tv = (TextView) findViewById(R.id.mon_target);
		tv.setText(String.valueOf(mYear) + "年" + String.valueOf(mMonth) + "月");
	}

	/**
	 * 月次データを取得し、ListViewに表示する。
	 */
	public void createCalender() {

		final ArrayList<DailyState> dayOfMonth = new ArrayList<DailyState>();
		DailyState ds = new DailyState();

		for(int i = 1; i < 31; i++) {
			ds.date = String.valueOf(i);
			ds.attendance = "09:00";
			ds.leave = "17:00";
			ds.work_hour = "8:00";
			dayOfMonth.add(ds);
		}

		MonthlyAdapter la = new MonthlyAdapter(getApplicationContext(),
									android.R.layout.simple_list_item_1, dayOfMonth);
		ListView lv = (ListView) findViewById(R.id.listview);
		lv.setAdapter(la);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterview, View view, int position, long id) {
				DailyState ds = dayOfMonth.get(position);
				Intent i = new Intent(MonthlyActivity.this, DailyActivity.class);
				i.putExtra("date", ds.date);
				i.putExtra("attendance", ds.attendance);
				i.putExtra("leave", ds.leave);
				i.putExtra("work_hour", ds.work_hour);
				startActivity(i);
			}
		});
	}
}