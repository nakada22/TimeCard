package jp.co.timecard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class TopActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /**************************
         * チェックボックスを押したときの処理
         **************************/
        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox1);
        // 設定ボタンオブジェクト取得
        final Button inibtn = (Button) findViewById(R.id.ini);

        // チェックボックスがクリックされた時に呼び出されるコールバックリスナーを登録します
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            // チェックボックスがクリックされた時に呼び出されます
            public void onClick(View v) {
                if (checkBox.isChecked() == true) {
                	// チェックされた時、設定ボタン非表示にする
                	inibtn.setVisibility(View.INVISIBLE);
                } else {
                	inibtn.setVisibility(View.VISIBLE);
                }
            }
        });
        /**************************
         * 設定ボタンを押したときの処理
	     **************************/
        inibtn.setOnClickListener(new View.OnClickListener() {
        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
            
    	@Override
    	public void onClick(View v) {
    		// クリックされた時の処理を記述
    		new TimePickerDialog(TopActivity.this, new TimePickerDialog.OnTimeSetListener() {
    			@Override
    			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    				Toast.makeText(TopActivity.this,
                            "時間設定",
                            Toast.LENGTH_SHORT).show();
    				
    				// TODO Auto-generated method stub
    				// 時間が設定されたときの処理
    				//Log.v("Time", String.format("%02d:%02d", hourOfDay, minute));
    				}
    			} , hour-15, minute, true).show();



    				 

    	}
        });
	    
        
        
        
        // 現在日表示
        Calendar calender = Calendar.getInstance();
        //Calendar.SUNDAY～Calendar.SATURDAY 中身は、1(日曜)～7(土曜)
        int week = calender.get(Calendar.DAY_OF_WEEK)-1;
        String[] week_name = {"日", "月", "火", "水", "木", "金", "土"};

        SimpleDateFormat  sdf = new SimpleDateFormat("yyyy'/'MM'/'dd'('"+week_name[week]+"')'");   
        Date date = new Date();   
        //Log.d("date : ", sdf.format(date));   
      
        TextView textView = (TextView)findViewById(R.id.datetime);   
        textView.setText(sdf.format(date));   

      // 現在時刻表示
        TextView currenttime = (TextView)findViewById(R.id.currenttime);
        Time time = new Time("Asia/Tokyo");
		time.setToNow();

		int hour = calender.get(Calendar.HOUR_OF_DAY); //(5)現在の時を取得
	    int minute = calender.get(Calendar.MINUTE);
	    

		currenttime.setText(hour-15 + ":" + minute);
		//currenttime.setText(DateUtils.FORMAT_SHOW_TIME);
        //SimpleDateFormat cr_time = new SimpleDateFormat("HH:mm");   
        //TextView currenttime = (TextView)findViewById(R.id.currenttime);
		
        //textView.setText(cr_time.format(new Date()));  
    }
}
