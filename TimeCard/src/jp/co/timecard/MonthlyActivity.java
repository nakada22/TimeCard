package jp.co.timecard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
	private int mYear;
	private int mMonth;

	final int PRE_MONTH = -1;
	final int NEX_MONTH = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monthly_main);

		//表示する年月（最初は現在の年月）
		cal = Calendar.getInstance();

		// 初期表示月のセット
		setTargetMonth(0);

		// 表示月のカレンダーを作成
		createCalender();

		// 前月ボタン
		findViewById(R.id.button_pre_month).setOnClickListener(this);
		// 次月ボタン
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
		// メニュー
		switch(item.getItemId()){
		// トップ画面へ戻る
		case R.id.return_top:
			Intent i2 = new Intent();
			 i2.setClassName(
	                    "jp.co.timecard",
	                    "jp.co.timecard.TopActivity");
            startActivity(i2);
            return true;

		// 終了する
		 case R.id.finish:
			 AlertDialog.Builder dlg = new AlertDialog.Builder(this);
			 dlg.setMessage("アプリを終了してよろしいですか？");
			 dlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//finish();
					// アクティビティをバックグラウンドに移動する
					moveTaskToBack(true);
				}
			 });
			 dlg.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
			 });
			 dlg.create().show();
			 return true;
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
			//DBから勤怠を取得してくる（なければ空欄）
			DailyState ds = new DailyState();
			
			String crrent_mMonth = String.format("%1$02d",mMonth+1);
			String disp_date = String.format("%1$02d", i)+"日";
			ds.setDate(disp_date);
			
			String date_param = mYear + "/" + crrent_mMonth + "/" + String.format("%1$02d", i);
			ds.setTargetDate(date_param);
			
			// 戻り値より出勤時刻・退勤時刻を取得
			String[] daily_param = dao.MonthlyList(date_param);
			String attendance_time = daily_param[0];
			String leaveoffice_time = daily_param[1];
			String break_time = daily_param[2];

			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			try{
				if (attendance_time != "" && leaveoffice_time != "" && break_time != ""){
					Date at = sdf.parse(attendance_time);
					Date lt = sdf.parse(leaveoffice_time);
					Date bt = sdf.parse(break_time);
					// 全ての時間があれば合計時間計算、セット
					long sumtime = lt.getTime() - at.getTime()- bt.getTime()+1000*60*60*6;
					ds.setWorkHour(sdf.format(sumtime));
				}
				ds.setAttendance(attendance_time);
				ds.setLeave(leaveoffice_time);
				ds.setBreakTime(break_time);
				dayOfMonth.add(ds);
			}catch(java.text.ParseException e){
				e.printStackTrace();
			}
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