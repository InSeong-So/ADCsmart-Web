package kr.openbase.adcsmart.web.report.total.type;

public enum TotalReportAbNormalEnum {
	NO							("No"),
	HOST_NAME					("Host Name"),
	POWER_STATUS				("Power 상태"),
	CPU							("CPU"),
	PORT_STATUS				("Port 상태");
	
	private String title;
	
	private TotalReportAbNormalEnum(String title){
		this.title = title;
	}
	
	public String getTitle(){
		return this.title;
	}
}
