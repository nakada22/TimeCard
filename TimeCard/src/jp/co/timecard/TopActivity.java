package jp.co.timecard;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jp.co.timecard.db.TopDao;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class TopActivity extends Activity implements View.OnClickListener
{
	int mYear;
	int mMonth;
	int hourOfDay;
	int minute;
    boolean is24HourView = true;
    Calendar c = Calendar.getInstance();
    DecimalFormat df = new DecimalFormat("00");
    Date date = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	SimpleDateFormat timestamp_sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        findViewById(R.id.checkBox1).setOnClickListener(this);
        findViewById(R.id.attendance).setOnClickListener(this);
        findViewById(R.id.leaveoffice).setOnClickListener(this);
        findViewById(R.id.ini).setOnClickListener(this);
        findViewById(R.id.tsukiji).setOnClickListener(this);
        
        
        mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH) + 1;
		hourOfDay = c.get(Calendar.HOUR_OF_DAY);
	    minute = c.get(Calendar.MINUTE);
		
	    //TextViewに線をセット
        TextView textView_line = (TextView) findViewById(R.id.textView_line);
        TextView textView_line2 = (TextView) findViewById(R.id.textView_line2);
        TextView textView_line3 = (TextView) findViewById(R.id.textView_line3);
        TextView textView_line4 = (TextView) findViewById(R.id.textView_line4);
        
        textView_line.setBackgroundResource(R.layout.line);
        textView_line2.setBackgroundResource(R.layout.line);
        textView_line3.setBackgroundResource(R.layout.line);
        textView_line4.setBackgroundResource(R.layout.line);

	    CurrentDisp();
        TopPreInsert();
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
		case R.id.tsukiji:
			MonthlyListChange();
			break;
		default:
			break;
		}
	}
	
	/*
	 * トップ画面を開くと同時に勤怠マスタ(当日日付無ければ)
	 * 時刻設定マスタへデータ登録・既に当日の謹怠記録があれば画面表示
	 * */
	public void TopPreInsert() {
		final TextView start_tv = (TextView) findViewById(R.id.start_time2); // 始業時刻
		final TextView end_tv = (TextView) findViewById(R.id.last_time2); 	// 終業時刻
		final TextView break_tv = (TextView) findViewById(R.id.bleak_time2); // 休憩時間
		final TextView sumtime_tv = (TextView) findViewById(R.id.sum_time2); // 合計時間

		TopDao td = new TopDao(getApplicationContext());
        td.preKintaiSave(sdf.format(date)); // 謹怠ID発行
        td.preTimeSave(timestamp_sdf.format(date)); // 
        td.TopTimeDisp(sdf.format(date),start_tv, end_tv, break_tv, sumtime_tv);
	}
	
	
	//現在時刻
	public void Current_date(TextView tv) {
		Calendar c = Calendar.getInstance();
		hourOfDay = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
		is24HourView = true;
		
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
		TimePickerDialog timePickerDialog;
		
		// 画面表示されている時刻取得
		final TextView tv = (TextView) findViewById(R.id.currenttime);
		String disptime = String.valueOf(tv.getText());
		
    	hourOfDay = Integer.parseInt(disptime.substring(0, 2));
    	minute = Integer.parseInt(disptime.substring(3, 5));
    	is24HourView = true;
		
		TimePickerDialog.OnTimeSetListener TimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		    	
		    	// 初期表示時刻を設定(画面表示されている時刻)
		    	StringBuilder sb = new StringBuilder()
				.append(df.format(hourOfDay))
				.append(":")
				.append(df.format(minute));
				Current_date(tv);
	    		tv.setText(sb);
		    }
		};
		
		//時刻設定ダイアログの作成
		timePickerDialog = new TimePickerDialog(TopActivity.this, TimeSetListener, hourOfDay, minute, is24HourView);
		//timePickerDialog.setTitle("時間設定");
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
		final Button inibtn = (Button) findViewById(R.id.ini);
		final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox1);
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
		
		final ImageButton imgbutton = new ImageButton(this);
	    imgbutton.setImageResource(R.drawable.atendance);
	    
		final TextView start_tv = (TextView) findViewById(R.id.start_time2);
		
		// 出勤マスタへDB登録（画面で設定した時刻）
		TextView atd_tv = (TextView) findViewById(R.id.currenttime);
		TopDao td = new TopDao(getApplicationContext());
		
		String currenttime = timestamp_sdf.format(Calendar.getInstance().getTime());
		final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox1);
		boolean atd_flg;
		
		if (checkBox.isChecked() == true) {
			//現在時刻使用チェック時、現在時刻で退勤時刻を記録
			atd_flg = td.AttendanceSave(sdf.format(date), currenttime, null);
		} else {
			atd_flg = td.AttendanceSave(sdf.format(date), currenttime, atd_tv);
        }
		
		// 既に退勤済みの場合
		if (atd_flg == false) {
			Toast.makeText(TopActivity.this,
		    "既に退勤済みです",
		    Toast.LENGTH_SHORT).show();
		} else {
			if (imgbutton.isEnabled() == true) {
				Toast.makeText(TopActivity.this,
			    "出勤",
			    Toast.LENGTH_SHORT).show();
			}
		}
		td.AttendanceTimeDisp(sdf.format(date),start_tv);
	}
	
	/*
	 * 退勤ボタン押下処理
	 */
	public void LeaveofficeChange() {
		final ImageButton leaveimgbutton = new ImageButton(this);
		leaveimgbutton.setImageResource(R.drawable.leave);
	    
        final TextView start_tv = (TextView) findViewById(R.id.start_time2); // 始業時刻
    	final TextView end_tv = (TextView) findViewById(R.id.last_time2); 	// 終業時刻
    	final TextView break_tv = (TextView) findViewById(R.id.bleak_time2); // 休憩時間
    	final TextView sumtime_tv = (TextView) findViewById(R.id.sum_time2); // 合計時間
        
        // 退勤マスタ・休憩マスタへDB登録（画面で設定した時刻）
		TextView leave_tv = (TextView) findViewById(R.id.currenttime);
		TopDao td = new TopDao(getApplicationContext());
		
		// まだ未出勤の場合は、退勤記録をしないようにする
		String currenttime = timestamp_sdf.format(Calendar.getInstance().getTime());
		final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox1);
		boolean leave_flg;
		
		if (checkBox.isChecked() == true) {
			//現在時刻使用チェック時、現在時刻で退勤時刻を記録
			leave_flg = td.LeaveofficeSave(sdf.format(date), currenttime, null);
		} else {
			leave_flg = td.LeaveofficeSave(sdf.format(date), currenttime, leave_tv);
        }
		
		// まだ未出勤の場合
		if (leave_flg == false) {
			Toast.makeText(TopActivity.this,
		    "先に出勤記録を行って下さい。",
		    Toast.LENGTH_SHORT).show();
		} else {
			if (leaveimgbutton.isEnabled() == true) {
				Toast.makeText(TopActivity.this,
			    "退勤",
			    Toast.LENGTH_SHORT).show();
			}
		}
		
		td.preBreakSave(sdf.format(date), currenttime);
        td.TopTimeDisp(sdf.format(date),start_tv, end_tv, break_tv, sumtime_tv);
	}
	
	/*
	 * 月次リストボタンクリック時の処理
	 * */
	public void MonthlyListChange() {
		final ImageButton monthlstimgbtn = new ImageButton(this);
		monthlstimgbtn.setImageResource(R.drawable.tsukiji);
		final Intent intent = new Intent();
		
		intent.setClassName(
                "jp.co.timecard",
                "jp.co.timecard.MonthlyActivity");
        startActivity(intent);
		
	}
	
    
	/*
	 * デフォルト画面表示
	 * */
	public void CurrentDisp() {

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
        
        // チェックボックス true 設定ボタン非表示
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox1);
    	checkBox.setChecked(true);
    	Button inibtn = (Button) findViewById(R.id.ini);
		inibtn.setVisibility(View.INVISIBLE);
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
    	final Intent intent = new Intent();
        
        switch (item.getItemId()) {
        case MENU_ID_A:
        	intent.setClassName(
                    "jp.co.timecard",
                    "jp.co.timecard.MonthlyActivity");
            startActivity(intent);
            return true;
            
        case MENU_ID_B:
            final CharSequence[] items = {"基本設定"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("設定");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                	intent.setClassName(
                            "jp.co.timecard",
                            "jp.co.timecard.BaseSetListActivity");
                    startActivity(intent);
                }
            });
            builder.create();
            builder.show();
            return true;
     
        case MENU_ID_C:
        	finish();
            return true;
        }
        return false;
    }	
}
