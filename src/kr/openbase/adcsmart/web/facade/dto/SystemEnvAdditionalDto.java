package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoSnmpTrap;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvAdditional;
import kr.openbase.adcsmart.service.dto.OBDtoSystemSnmpInfo;
import kr.openbase.adcsmart.web.util.OBDefineWeb;

public class SystemEnvAdditionalDto {
	private Date occurTime;

	private Integer isTimeSync;// 0:disabled, 1: enabled
	private Integer syslogPort = 514;
	private Integer intervalSystemInfo;
	private Integer intervalAdcConfSync;
	private String timeServerAddress;// time.kriss.re.kr
//	private Integer alertShow;
	/*
	 * ADC 관리에 경보화면 추가로 인한 환경 설정에 경보 제외 private boolean alertShowYn=false;
	 */
	private String syslogServerAddress; // syslog를 전송할 서버(NMS 등) IP 주소

	// alteon auto save
//	private Integer alteonAutoSave;
	private boolean alteonAutoSaveYn = false;
	private boolean alarmPopupYn = false;
	private boolean loginAccessYn = false;// false: 로그인 접근 제어를 하지 않음. 즉 중복 로그인 허용. true: 중복 로그인 불허.

	private boolean serviceRespTime = false; // false : 응답시간 off 상태 (default)

	// 구간응답시간 체크 및 interval time 추가
	private boolean respTimeSection = false;

	private Integer respTimeInterval;

	// sms 설정 관련...
	private boolean smsActionYn = false;
	private String smsActionType;
	private String smsHPNumbers;

	public boolean isRespTimeSection() {
		return respTimeSection;
	}

	public void setRespTimeSection(boolean respTimeSection) {
		this.respTimeSection = respTimeSection;
	}

	public boolean isSmsActionYn() {
		return smsActionYn;
	}

	public void setSmsActionYn(boolean smsActionYn) {
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

	public Integer getRespTimeInterval() {
		return respTimeInterval;
	}

	public void setRespTimeInterval(Integer respTimeInterval) {
		this.respTimeInterval = respTimeInterval;
	}

	// snmpTrap
	private OBDtoSnmpTrap snmpTrap;
	// ADCsmart Snmp
	private OBDtoSystemSnmpInfo snmpCommunity;

//	private String snmpTrapServerAddress;
//	private Integer snmpTrapPort=161;
//	private String snmpTrapCommunity;
//	private Integer snmpTrapVersion;

//		[occurTime=null, isTimeSync=1, timeServerAddress=time.kriss.re.kr, syslogPort=5142, intervalSystemInfo=60, intervalAdcConfSync=null]

	public static OBDtoSystemEnvAdditional toSystemEnvAdditional(SystemEnvAdditionalDto eadditional) {
		OBDtoSystemEnvAdditional envAdditionalFromSvc = new OBDtoSystemEnvAdditional();
//		envAdditionalFromSvc.setIntervalAdcConfSync(eadditional.getIntervalConfSync());
		envAdditionalFromSvc.setIntervalAdcConfSync(eadditional.getIntervalAdcConfSync());
		envAdditionalFromSvc.setIntervalSystemInfo(eadditional.getIntervalSystemInfo());
		envAdditionalFromSvc.setIsTimeSync(eadditional.getIsTimeSync());
//		envAdditionalFromSvc.setIsTimeSync(0);
		envAdditionalFromSvc.setSyslogPort(eadditional.getSyslogPort());
		envAdditionalFromSvc.setTimeServerAddress(eadditional.getTimeServerAddress());
		/*
		 * envAdditionalFromSvc.setAlertShow(eadditional.getAlertShowYn() ? 1 : 0);
		 */
		envAdditionalFromSvc.setSyslogServerAddress(eadditional.getSyslogServerAddress());

//		envAdditionalFromSvc.setAlertShow(eadditional.getAlertShow());

//		System.out.println("eadditional.getAlertShowYn() : " + eadditional.getAlertShowYn());

		envAdditionalFromSvc
				.setAlteonAutoSave(eadditional.getAlteonAutoSaveYn() ? OBDefineWeb.CFG_ON : OBDefineWeb.CFG_OFF);

		envAdditionalFromSvc.setAlarmPopupYn(eadditional.isAlarmPopupYn() ? OBDefineWeb.CFG_ON : OBDefineWeb.CFG_OFF);

		envAdditionalFromSvc
				.setDoubleLoginAccess(eadditional.isLoginAccessYn() ? OBDefineWeb.CFG_ON : OBDefineWeb.CFG_OFF);

		envAdditionalFromSvc
				.setServiceResponseTime(eadditional.isServiceRespTime() ? OBDefineWeb.CFG_ON : OBDefineWeb.CFG_OFF);
		// snmptrap
		envAdditionalFromSvc.setSnmpTrap(eadditional.getSnmpTrap());

		envAdditionalFromSvc
				.setRespTimeSection(eadditional.isRespTimeSection() ? OBDefineWeb.CFG_ON : OBDefineWeb.CFG_OFF);
		envAdditionalFromSvc.setRespTimeInterval(eadditional.getRespTimeInterval());

		// ADCsmart SNMP
		envAdditionalFromSvc.setSnmpCommunity(eadditional.getSnmpCommunity());

		// sms 설정 관련
		envAdditionalFromSvc.setSmsActionType(eadditional.getSmsActionType());
		envAdditionalFromSvc.setSmsActionYn(eadditional.isSmsActionYn() ? OBDefineWeb.CFG_ON : OBDefineWeb.CFG_OFF);
		envAdditionalFromSvc.setSmsHPNumbers(eadditional.getSmsHPNumbers());
		return envAdditionalFromSvc;
	}

	public Date getOccurTime() {
		return occurTime;
	}

	public void setOccurTime(Date occurTime) {
		this.occurTime = occurTime;
	}

	public Integer getIntervalSystemInfo() {
		return intervalSystemInfo;
	}

	public void setIntervalSystemInfo(Integer intervalSystemInfo) {
		this.intervalSystemInfo = intervalSystemInfo;
	}

	public Integer getSyslogPort() {
		return syslogPort;
	}

	public void setSyslogPort(Integer syslogPort) {
		this.syslogPort = syslogPort;
	}

	public Integer getIsTimeSync() {
		return isTimeSync;
	}

	public void setIsTimeSync(Integer isTimeSync) {
		this.isTimeSync = isTimeSync;
	}

	public Integer getIntervalAdcConfSync() {
		return intervalAdcConfSync;
	}

	public void setIntervalAdcConfSync(Integer intervalAdcConfSync) {
		this.intervalAdcConfSync = intervalAdcConfSync;
	}

	public String getTimeServerAddress() {
		return timeServerAddress;
	}

	public void setTimeServerAddress(String timeServerAddress) {
		this.timeServerAddress = timeServerAddress;
	}

	/*
	 * public Integer getAlertShow() { return alertShow; }
	 * 
	 * public void setAlertShow(Integer alertShow) { this.alertShow = alertShow; }
	 */

	/*
	 * public boolean getAlertShowYn() { return alertShowYn; }
	 * 
	 * public void setAlertShowYn(boolean alertShowYn) { this.alertShowYn =
	 * alertShowYn; }
	 */

	public String getSyslogServerAddress() {
		return syslogServerAddress;
	}

	public void setSyslogServerAddress(String syslogServerAddress) {
		this.syslogServerAddress = syslogServerAddress;
	}

	public boolean getAlteonAutoSaveYn() {
		return alteonAutoSaveYn;
	}

	public boolean isAlarmPopupYn() {
		return alarmPopupYn;
	}

	public void setAlarmPopupYn(boolean alarmPopupYn) {
		this.alarmPopupYn = alarmPopupYn;
	}

	public void setAlteonAutoSaveYn(boolean alteonAutoSaveYn) {
		this.alteonAutoSaveYn = alteonAutoSaveYn;
	}

	public boolean isLoginAccessYn() {
		return loginAccessYn;
	}

	public void setLoginAccessYn(boolean loginAccessYn) {
		this.loginAccessYn = loginAccessYn;
	}

	public boolean isServiceRespTime() {
		return serviceRespTime;
	}

	public void setServiceRespTime(boolean serviceRespTime) {
		this.serviceRespTime = serviceRespTime;
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

	@Override
	public String toString() {
		return "SystemEnvAdditionalDto [occurTime=" + occurTime + ", isTimeSync=" + isTimeSync + ", syslogPort="
				+ syslogPort + ", intervalSystemInfo=" + intervalSystemInfo + ", intervalAdcConfSync="
				+ intervalAdcConfSync + ", timeServerAddress=" + timeServerAddress + ", syslogServerAddress="
				+ syslogServerAddress + ", alteonAutoSaveYn=" + alteonAutoSaveYn + ", loginAccessYn=" + loginAccessYn
				+ ", serviceRespTime=" + serviceRespTime + ", respTimeSection=" + respTimeSection
				+ ", respTimeInterval=" + respTimeInterval + ", snmpTrap=" + snmpTrap + ", snmpCommunity="
				+ snmpCommunity + "]";
	}
}