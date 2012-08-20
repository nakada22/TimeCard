package jp.co.timecard;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * 設定画面
 * @author Tomohiro
 *
 */
public class ConfigActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);
    }

}
