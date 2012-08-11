package jp.co.timecard.db.mapping;

import java.util.Date;

public class Attendance {
	// TODO Dateで持っておくべきなのかな〜？？
	private int attendanceId;
	private Kintai kintai;
	private Date attendanceDate;
	private Date attendanceTime;
	private Date registDatetime;
	private Date updateDatetime;
	
	public int getAttendanceId() {
		return attendanceId;
	}
	public void setAttendanceId(int attendanceId) {
		this.attendanceId = attendanceId;
	}
	public Kintai getKintai() {
		return kintai;
	}
	public void setKintai(Kintai kintai) {
		this.kintai = kintai;
	}
	public Date getAttendanceDate() {
		return attendanceDate;
	}
	public void setAttendanceDate(Date attendanceDate) {
		this.attendanceDate = attendanceDate;
	}
	public Date getAttendanceTime() {
		return attendanceTime;
	}
	public void setAttendanceTime(Date attendanceTime) {
		this.attendanceTime = attendanceTime;
	}
	public Date getRegistDatetime() {
		return registDatetime;
	}
	public void setRegistDatetime(Date registDatetime) {
		this.registDatetime = registDatetime;
	}
	public Date getUpdateDatetime() {
		return updateDatetime;
	}
	public void setUpdateDatetime(Date updateDatetime) {
		this.updateDatetime = updateDatetime;
	}
}
