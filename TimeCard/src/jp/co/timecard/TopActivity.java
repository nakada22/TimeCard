package jp.co.timecard;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import android.content.Context;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class TopActivity extends Activity implements View.OnClickListener
{
	int mYear;
	int mMonth;
	int hourOfDay;
	int minute;
	//int disp_flg = 0;
    boolean is24HourView = true;
    Calendar c = Calendar.getInstance();
	
  
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViewById(R.id.checkBox1).setOnClickListener(this);
        findViewById(R.id.attendance).setOnClickListener(this);
        findViewById(R.id.leaveoffice).setOnClickListener(this);
        findViewById(R.id.ini).setOnClickListener(this);
        
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH) + 1;
		hourOfDay = c.get(Calendar.HOUR_OF_DAY);
	    minute = c.get(Calendar.MINUTE);
		
	    CurrentDateDisp();
	    
        //TextViewに線をセット
        TextView textView_line = (TextView) findViewById(R.id.textView_line);
        TextView textView_line2 = (TextView) findViewById(R.id.textView_line2);
        TextView textView_line3 = (TextView) findViewById(R.id.textView_line3);
        TextView textView_line4 = (TextView) findViewById(R.id.textView_line4);
        TextView textView_line5 = (TextView) findViewById(R.id.textView_line5);
        
        textView_line.setBackgroundResource(R.layout.line);
        textView_line2.setBackgroundResource(R.layout.line);
        textView_line3.setBackgroundResource(R.layout.line);
        textView_line4.setBackgroundResource(R.layout.line);
        textView_line5.setBackgroundResource(R.layout.line);
    }
    
	@Override
	public void onClick(View v) {
	
		switch (v.getId()) {
		case R.id.checkBox1:
			checkBoxChange();
			break;
		case R.id.attendance:
			AttendChange();
			break;
		case R.id.leaveoffice:
			LeaveofficeChange();
			break;			
		case R.id.ini:
			IniChange();
			break;
		default:
			break;
		}
	}
	
	//現在時刻
	public void Current_date(TextView tv) {
		Calendar c = Calendar.getInstance();
		hourOfDay = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
		is24HourView = true;
		
		DecimalFormat df = new DecimalFormat("00");
		StringBuilder sb = new StringBuilder()
		.append(df.format(hourOfDay))
		.append(":")
		.append(df.format(minute));
		tv.setText(sb);
	}
	
	/*
	 * 設定ボタンクリック
	 * */
	public void IniChange(){
		//this.disp_flg = 1;
		TimePickerDialog timePickerDialog;
		
		Calendar c = Calendar.getInstance();
		hourOfDay = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
		is24HourView = true;
		
		
		TimePickerDialog.OnTimeSetListener TimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		        	//updateDisplay();
		    		TextView tv = (TextView) findViewById(R.id.currenttime);
		    		Current_date(tv);
		    }
		};
		
		//時刻設定ダイアログの作成
		timePickerDialog = new TimePickerDialog(TopActivity.this, TimeSetListener, hourOfDay, minute, is24HourView);
		timePickerDialog.setTitle("時間設定");
		timePickerDialog.setMessage("出退勤時刻設定");
		timePickerDialog.show();
		
		//@Override
		//public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            // do nothing
		//}
		
	};

	
	
	/*
	 * チェックボックスチェック時の処理
	 */
	public void checkBoxChange() {
		final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox1);
		final Button inibtn = (Button) findViewById(R.id.ini);
		inibtn.setVisibility(View.INVISIBLE);
		
		if (checkBox.isChecked() == true) {
			inibtn.setVisibility(View.INVISIBLE);
		} else {
			inibtn.setVisibility(View.VISIBLE);
        }
	}
	/*
	 * 出勤ボタン押下処理
	 */
	public void AttendChange() {
		final Button attendbtn = (Button) findViewById(R.id.attendance);
		final TextView tv = (TextView) findViewById(R.id.start_time2);
		Current_date(tv);
		
		if (attendbtn.isEnabled() == true) {
			Toast.makeText(TopActivity.this,
		    "出勤",
		    Toast.LENGTH_SHORT).show();
		}
	}
	/*
	 * 退勤ボタン押下処理
	 */
	public void LeaveofficeChange() {
        final Button leaveofficebtn = (Button) findViewById(R.id.leaveoffice);
		final TextView tv = (TextView) findViewById(R.id.last_time2);
		Current_date(tv);
        
        if (leaveofficebtn.isEnabled() == true) {
        	Toast.makeText(TopActivity.this,
            "退勤",
            Toast.LENGTH_SHORT).show();
        }

	}
	
	public void OptionDisplay() {

	}
    
	/*
	 * 現在時刻表示
	 * */
	public void CurrentDateDisp() {
		
		
		Calendar calender = Calendar.getInstance();
        int week = calender.get(Calendar.DAY_OF_WEEK)-1;//1(日曜)～7(土曜)
        String[] week_name = {"日", "月", "火", "水", "木", "金", "土"};
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy'/'MM'/'dd'('"+week_name[week]+"')'");   
        Date date = new Date();   
        
        // 現在日表示
        TextView textView = (TextView)findViewById(R.id.datetime);   
        textView.setText(sdf.format(date));   
        
        // 現在時刻表示
        TextView tv = (TextView)findViewById(R.id.currenttime);
        Current_date(tv);
	}
	

    /**************
	 * オプションメニュー
	 **************/
	// メニューアイテム識別用のID
    private static final int MENU_ID_A = 0;
    private static final int MENU_ID_B = 1;
    private static final int MENU_ID_C = 2;
    
    // オプションメニューの作成
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
 
        // メニューアイテムの追加
    	MenuItem menuItemA,menuItemB,menuItemC;
    	menuItemA = menu.add(Menu.NONE, MENU_ID_A, Menu.NONE, "月次リスト");
        menuItemA.setIcon(android.R.drawable.ic_menu_month);
        
        menuItemB = menu.add(Menu.NONE, MENU_ID_B, Menu.NONE, "設定");
        menuItemB.setIcon(android.R.drawable.ic_menu_manage);
        
        menuItemC = menu.add(Menu.NONE, MENU_ID_C, Menu.NONE, "終了");
        menuItemC.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        return true;
    }
    
    // メニューが選択された時の処理
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_ID_A:
        	Intent intent = new Intent();
            intent.setClassName(
                    "jp.co.timecard",
                    "jp.co.timecard.MonthlyActivity");
            startActivity(intent);
            return true;
     
        case MENU_ID_B:
            Toast.makeText(this, "設定", Toast.LENGTH_LONG).show();
            return true;
     
        case MENU_ID_C:
        	finish();
            return true;
        }
        return false;
    }	
}
