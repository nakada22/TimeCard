package jp.co.timecard;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.widget.TextView;

public class TopActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // åªç›ì˙ï\é¶
        Calendar calender = Calendar.getInstance();
        //Calendar.SUNDAYÅ`Calendar.SATURDAY íÜêgÇÕÅA1(ì˙ój)Å`7(ìyój)
        int week = calender.get(Calendar.DAY_OF_WEEK)-1;
       String[] week_name = {"ì˙", "åé", "âŒ", "êÖ", 
                "ñÿ", "ã‡", "ìy"};

        SimpleDateFormat  sdf = new SimpleDateFormat("yyyy'/'MM'/'dd'('"+week_name[week]+"')'");   
        Date date = new Date();   
        //Log.d("date : ", sdf.format(date));   
      
        TextView textView = (TextView)findViewById(R.id.datetime);   
        textView.setText(sdf.format(date));   

      // åªç›éûçèï\é¶
        TextView currenttime = (TextView)findViewById(R.id.currenttime);
        Time time = new Time("Asia/Tokyo");
		time.setToNow();
		
		currenttime.setText(time.hour+":"+time.minute);
		//currenttime.setText(DateUtils.FORMAT_SHOW_TIME);
        //SimpleDateFormat cr_time = new SimpleDateFormat("HH:mm");   
        //TextView currenttime = (TextView)findViewById(R.id.currenttime);
		
        //textView.setText(cr_time.format(new Date()));  
    }
}
