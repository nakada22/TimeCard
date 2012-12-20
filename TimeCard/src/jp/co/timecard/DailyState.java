package jp.co.timecard;

import java.io.Serializable;

/**
 * 日ごとの勤怠状況をもつ。
 */
public class DailyState implements Serializable {

	private static final long serialVersionUID = 1L;

	private String date;
	private String attendance;
	private String leave;
	private String break_time;
	private String work_hour;
	private String mon_target;
	
	public void setDate(String _date) {
		this.date = _date;
	}
	public String getDate() {
		return this.date;
	}
	
	public void setTargetDate(String _mon_target) {
		this.mon_target = _mon_target;
	}
	public String getTargetDate() {
		return this.mon_target;
	}
	
	public void setAttendance(String _attendance) {
		this.attendance = _attendance;
	}
	public String getAttendance() {
		return this.attendance;
	}
	public void setLeave(String _leave) {
		this.leave = _leave;
	}
	public String getLeave() {
		return this.leave;
	}
	public void setWorkHour(String _work_hour) {
		this.work_hour = _work_hour;
	}
	public String getWorkHour() {
		return this.work_hour;
	}
	public String getBreakTime() {
		return this.break_time;
	}
	public void setBreakTime(String _break_time) {
		this.break_time = _break_time;
	}
}