package jp.co.timecard;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
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

        // ���ݓ�\��
        Calendar calender = Calendar.getInstance();
        //Calendar.SUNDAY�`Calendar.SATURDAY ���g�́A1(��j)�`7(�y�j)
        int week = calender.get(Calendar.DAY_OF_WEEK)-1;
       String[] week_name = {"��", "��", "��", "��",
                "��", "��", "�y"};

        SimpleDateFormat  sdf = new SimpleDateFormat("yyyy'/'MM'/'dd'('"+week_name[week]+"')'");
        Date date = new Date();
        //Log.d("date : ", sdf.format(date));

        TextView textView = (TextView)findViewById(R.id.datetime);
        textView.setText(sdf.format(date));

      // ���ݎ����\��
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
