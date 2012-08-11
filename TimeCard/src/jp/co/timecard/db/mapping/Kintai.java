package jp.co.timecard.db.mapping;

import java.util.Calendar;
import java.util.Date;

public class Kintai {
	private Long kintaiId;
	private Date kintaiDate;
	
	// とりあえず
	public void init() {
		this.kintaiId = 1L;
		this.kintaiDate = Calendar.getInstance().getTime();  
	}
	
	public Long getKintaiId() {
		return kintaiId;
	}
	public void setKintaiId(Long kintaiId) {
		this.kintaiId = kintaiId;
	}
	public Date getKintaiDate() {
		return kintaiDate;
	}
	public void setKintaiDate(Date kintaiDate) {
		this.kintaiDate = kintaiDate;
	}

}
