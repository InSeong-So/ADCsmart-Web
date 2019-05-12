package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoSystemEnvAdditional {
	private Date occurTime;
	private Integer isTimeSync = 0;// 0:disabled, 1: enabled
	private String timeServerAddress;// time.kriss.re.kr
	private Integer syslogPort = 514;
	private Integer intervalSystemInfo = 60;
	private Integer intervalAdcConfSync = 600;
	// private Integer alertShow; //alarm으로 popup을 통합관리하므로 필요 없음
	private String syslogServerAddress; // syslog를 전송할 서버(NMS 등) IP 주소
	private Integer alteonAutoSave;
	private Integer alarmPopupYn = 0;

	// sms 설정 관련...
	private Integer smsActionYn = 0;
	private String smsActionType = "";
	private String smsHPNumbers = "";

	private Integer doubleLoginAccess = 0;// 중복 로그인 허용. 0: 중복 허용, 1: 중복허용하지 않음.

	private Integer serviceResponseTime = 0;

	private OBDtoSnmpTrap snmpTrap;
	private OBDtoSystemSnmpInfo snmpCommunity;
//    private String snmpTrapServerAddress;
//    private Integer snmpTrapPort = 161;
//    private String snmpTrapCommunity;
//    private Integer snmpTrapVersion;

	private Integer respTimeSection;
	private Integer respTimeInterval;

	@Override
	public String toString() {
		return "OBDtoSystemEnvAdditional [occurTime=" + occurTime + ", isTimeSync=" + isTimeSync
				+ ", timeServerAddress=" + timeServerAddress + ", syslogPort=" + syslogPort + ", intervalSystemInfo="
				+ intervalSystemInfo + ", intervalAdcConfSync=" + intervalAdcConfSync + ", syslogServerAddress="
				+ syslogServerAddress + ", alteonAutoSave=" + alteonAutoSave + ", doubleLoginAccess="
				+ doubleLoginAccess + ", serviceResponseTime=" + serviceResponseTime + ", snmpTrap=" + snmpTrap
				+ ", snmpCommunity=" + snmpCommunity + ", respTimeSection=" + respTimeSection + ", respTimeInterval="
				+ respTimeInterval + "]";
	}

	public Integer getAlarmPopupYn() {
		return alarmPopupYn;
	}

	public void setAlarmPopupYn(Integer alarmPopupYn) {
		this.alarmPopupYn = alarmPopupYn;
	}

	public Integer getSmsActionYn() {
		return smsActionYn;
	}

	public void setSmsActionYn(Integer smsActionYn) {
		this.smsActionYn = smsActionYn;
	}

	public String getSmsActionType() {
		return smsActionType;
	}

	public void setSmsActionType(String smsActionType) {
		this.smsActionType = smsActionType;
	}

	public String getSmsHPNumbers() {
		return smsHPNumbers;
	}

	public void setSmsHPNumbers(String smsHPNumbers) {
		this.smsHPNumbers = smsHPNumbers;
	}

	public Integer getRespTimeSection() {
		return respTimeSection;
	}

	public void setRespTimeSection(Integer respTimeSection) {
		this.respTimeSection = respTimeSection;
	}

	public Integer getRespTimeInterval() {
		return respTimeInterval;
	}

	public void setRespTimeInterval(Integer respTimeInterval) {
		this.respTimeInterval = respTimeInterval;
	}

	public Integer getAlteonAutoSave() {
		return alteonAutoSave;
	}

	public void setAlteonAutoSave(Integer alteonAutoSave) {
		this.alteonAutoSave = alteonAutoSave;
	}

	public Integer getDoubleLoginAccess() {
		return doubleLoginAccess;
	}

	public void setDoubleLoginAccess(Integer doubleLoginAccess) {
		this.doubleLoginAccess = doubleLoginAccess;
	}

	public Integer getIsTimeSync() {
		return isTimeSync;
	}

	public Date getOccurTime() {
		return occurTime;
	}

	public void setOccurTime(Date occurTime) {
		this.occurTime = occurTime;
	}

	public void setIsTimeSync(Integer isTimeSync) {
		this.isTimeSync = isTimeSync;
	}

	public String getTimeServerAddress() {
		return timeServerAddress;
	}

	public void setTimeServerAddress(String timeServerAddress) {
		this.timeServerAddress = timeServerAddress;
	}

	public Integer getSyslogPort() {
		return syslogPort;
	}

	public void setSyslogPort(Integer syslogPort) {
		this.syslogPort = syslogPort;
	}

	public Integer getIntervalSystemInfo() {
		return intervalSystemInfo;
	}

	public void setIntervalSystemInfo(Integer intervalSystemInfo) {
		this.intervalSystemInfo = intervalSystemInfo;
	}

	public Integer getIntervalAdcConfSync() {
		return intervalAdcConfSync;
	}

	public void setIntervalAdcConfSync(Integer intervalAdcConfSync) {
		this.intervalAdcConfSync = intervalAdcConfSync;
	}

	public String getSyslogServerAddress() {
		return syslogServerAddress;
	}

	public void setSyslogServerAddress(String syslogServerAddress) {
		this.syslogServerAddress = syslogServerAddress;
	}

	public Integer getServiceResponseTime() {
		return serviceResponseTime;
	}

	public void setServiceResponseTime(Integer serviceResponseTime) {
		this.serviceResponseTime = serviceResponseTime;
	}

	public OBDtoSnmpTrap getSnmpTrap() {
		return snmpTrap;
	}

	public void setSnmpTrap(OBDtoSnmpTrap snmpTrap) {
		this.snmpTrap = snmpTrap;
	}

	public OBDtoSystemSnmpInfo getSnmpCommunity() {
		return snmpCommunity;
	}

	public void setSnmpCommunity(OBDtoSystemSnmpInfo snmpCommunity) {
		this.snmpCommunity = snmpCommunity;
	}
}