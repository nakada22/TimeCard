package jp.co.timecard;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BaseSetListAdapter extends ArrayAdapter<HashMap<String, String>>{
    private LayoutInflater li;
    private TextView tv_title;
    private TextView tv_ini;

    public BaseSetListAdapter(Context context, List<HashMap<String, String>> objects)
    {
        super(context, 0, objects);
        li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            convertView = li.inflate(R.layout.ini_list_row, null);
        }
        
        HashMap<String, String> item = this.getItem(position);
        if (item != null) {
        	tv_title = (TextView)convertView.findViewById(R.id.title);
        	tv_title.setText(item.get("title"));
            
        	tv_ini = (TextView)convertView.findViewById(R.id.desc);
        	tv_ini.setText(item.get("desc"));
        }
        return convertView;
    }
}

