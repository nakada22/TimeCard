package jp.co.timecard;

import android.app.TimePickerDialog;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

/**
 * 時刻設定ダイアログ
 * @author Tomohiro
 *
 */
public class TimePickerPreference extends DialogPreference {

	private TimePicker timePicker;

    public TimePickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自動生成されたコンストラクター・スタブ
	}
    public TimePickerPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO 自動生成されたコンストラクター・スタブ
	}

    @Override
    protected View onCreateDialogView() {

    	// http://ichitcltk.hustle.ne.jp/gudon/modules/pico_rd/index.php?content_id=97

    	this.timePicker = new TimePicker(this.getContext());
    	return null;

    }

}
