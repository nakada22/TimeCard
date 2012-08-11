package jp.co.timecard.db.mapping;

public class Leave {
	private int leaveofficeId;
	private Kintai kintai;
	private int leaveofficeDate;
	private int leaveofficeTime;
	private int registDatetime;
	private int updateDatetime;
	
	public int getLeaveofficeId() {
		return leaveofficeId;
	}
	public void setLeaveofficeId(int leaveofficeId) {
		this.leaveofficeId = leaveofficeId;
	}
	public Kintai getKintai() {
		return kintai;
	}
	public void setKintai(Kintai kintai) {
		this.kintai = kintai;
	}
	public int getLeaveofficeDate() {
		return leaveofficeDate;
	}
	public void setLeaveofficeDate(int leaveofficeDate) {
		this.leaveofficeDate = leaveofficeDate;
	}
	public int getLeaveofficeTime() {
		return leaveofficeTime;
	}
	public void setLeaveofficeTime(int leaveofficeTime) {
		this.leaveofficeTime = leaveofficeTime;
	}
	public int getRegistDatetime() {
		return registDatetime;
	}
	public void setRegistDatetime(int registDatetime) {
		this.registDatetime = registDatetime;
	}
	public int getUpdateDatetime() {
		return updateDatetime;
	}
	public void setUpdateDatetime(int updateDatetime) {
		this.updateDatetime = updateDatetime;
	}
}
