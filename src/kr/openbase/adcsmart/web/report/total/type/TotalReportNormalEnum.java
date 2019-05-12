package kr.openbase.adcsmart.web.report.total.type;

public enum TotalReportNormalEnum {
	NO							("No"),
	HOST_NAME					("Host Name"),
	SERIAL						("Serial Number"),
	MODEL_NAME					("모델명"),
	OS							("OS"),
	POWER_STATUS				("Power 상태"),
	CPU							("CPU"),
	PORT_STATUS				("Port 상태"),
	VRRP_STATUS				("이중화 상태"),
	DIRECT						("Direct 기능"),
	REAL_SERVER_STATUS		("Real Server 상태"),
	VIRTUAL_SERVER_STATUS 	("Virtual Server 상태"),
	SLB_SESSION				("Slb 세션 통계"),
	RESULT						("종합 결과");
	
	private String title;
	
	private TotalReportNormalEnum(String title){
		this.title = title;
	}
	
	public String getTitle(){
		return this.title;
	}
}
