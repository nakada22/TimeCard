package jp.co.timecard.db.mapping;

public class Break {
	private int breakId;
	private Kintai kintai;
	private int breakTime;
	private int registDatetime;
	private int updateDatetime;
	
	public int getBreakId() {
		return breakId;
	}
	public void setBreakId(int breakId) {
		this.breakId = breakId;
	}
	public Kintai getKintai() {
		return kintai;
	}
	public void setKintai(Kintai kintai) {
		this.kintai = kintai;
	}
	public int getBreakTime() {
		return breakTime;
	}
	public void setBreakTime(int breakTime) {
		this.breakTime = breakTime;
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
