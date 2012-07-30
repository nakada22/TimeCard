package jp.co.timecard;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class MonthlyActivity extends Activity{

	public class monthlyList {
		int date;
		String attendance;
		String leave;
		String work_hour;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monthly_main);

		ArrayList<monthlyList> dateList = new ArrayList<monthlyList>();
		monthlyList ml = new monthlyList();
		for(int i = 1; i > 31; i++) {
			ml.date = i;
			ml.attendance = "09:00";
			ml.leave = "17:00";
			ml.work_hour = "8:00";
			dateList.add(ml);
		}

		MonthlyAdapter la = new MonthlyAdapter(this, android.R.layout.simple_list_item_1, dateList);
		ListView lv = (ListView) findViewById(R.id.listview);
		lv.setAdapter(la);
	}
}
