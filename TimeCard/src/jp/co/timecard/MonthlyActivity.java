package jp.co.timecard;

import java.util.ArrayList;
import java.util.Calendar;

import jp.co.timecard.db.Dao;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 月次画面。
 * @author TomohiroTano
 */
 public class MonthlyActivity extends Activity implements View.OnClickListener {

	private Calendar cal;
	private int  mYear;
	private int mMonth;

	final int PRE_MONTH = -1;
	final int NEX_MONTH = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monthly_main);

		//表示する年月（最初は現在の年月）
		cal = Calendar.getInstance();

		setTargetMonth(0);

		createCalender();

		findViewById(R.id.button_pre_month).setOnClickListener(this);
		findViewById(R.id.button_next_month).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_pre_month:
			setTargetMonth(PRE_MONTH);
			createCalender();
			break;
		case R.id.button_next_month:
			setTargetMonth(NEX_MONTH);
			createCalender();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu, menu);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu){

		super.onPrepareOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		 switch(item.getItemId()){
		 case R.id.settings:

			 break;

		 case R.id.return_top:
			 Intent i = new Intent(this, TopActivity.class);
			 startActivity(i);
			 finish();
			 break;

		 case R.id.finish:
			 AlertDialog.Builder dlg = new AlertDialog.Builder(this);
			 dlg.setMessage("アプリを終了してよろしいですか？");
			 dlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			 });
			 dlg.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
			 });
			 dlg.create().show();

			 break;

		 default:
		 break;
		 }
		 return super.onOptionsItemSelected(item);
	}

	/**
	 * 表示対象の年月を取得。
	 * @param value 前月か次月ボタンの値
	 */
	public void setTargetMonth(int value) {

		cal.add(Calendar.MONTH, value);
		mYear = cal.get(Calendar.YEAR);
		mMonth = cal.get(Calendar.MONTH);
		cal.set(mYear, mMonth, 1);

		TextView tv = (TextView) findViewById(R.id.mon_target);
		tv.setText(String.valueOf(mYear) + "年" + String.valueOf(mMonth+1) + "月");
	}

	/**
	 * 月次データを取得し、ListViewに表示する。
	 */
	public void createCalender() {

		final ArrayList<DailyState> dayOfMonth = new ArrayList<DailyState>();

		Dao dao = new Dao(this);

		int dom = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		for(int i = 1; i <= dom; i++) {
			// TODO DBから勤怠を取得してくる（なければ空欄）
			DailyState ds = new DailyState();
			ds.setDate(mYear + "/" + mMonth + "/" +String.valueOf(i));
			ds.setAttendance("09:00");
			ds.setLeave("17:00");
			ds.setBreakTime("1:00");
			ds.setWorkHour("08:00");
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
				i.putExtra("DailyState", ds);
				startActivity(i);
			}
		});
	}
}