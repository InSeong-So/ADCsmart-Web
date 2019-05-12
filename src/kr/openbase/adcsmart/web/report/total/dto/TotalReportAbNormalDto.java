package kr.openbase.adcsmart.web.report.total.dto;

/*
 * 비정상 상세내역
 */
public class TotalReportAbNormalDto {

	private String hostName;				// Host Name
	private String powerStatus;				// Power
	private String cpu;						// CPU
	private String portStatus;				// Port 상태
	
	public TotalReportAbNormalDto(){
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getPowerStatus() {
		return powerStatus;
	}

	public void setPowerStatus(String powerStatus) {
		this.powerStatus = powerStatus;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getPortStatus() {
		return portStatus;
	}

	public void setPortStatus(String portStatus) {
		this.portStatus = portStatus;
	}
	
}
