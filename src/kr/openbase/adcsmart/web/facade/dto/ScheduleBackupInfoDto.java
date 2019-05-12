package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoScheduleBackupInfo;
import kr.openbase.adcsmart.web.util.OBDefineWeb;

@SuppressWarnings("deprecation")
public class ScheduleBackupInfoDto {

	private Integer intervalDay;
//	private Date	backupTime;
	private Date startTime;// 백업 예약 기간. 시작 날짜.
	private Date endTime;// 백업 예약 기간. 종료 날짜.
	private Integer backOption;// 0:전체. 1: ADC 설정정보. 2: ADC 로그 정보.
	private Integer ftpYN;// 1:yes, 2:no. 파일 전송 유무.
	private String ftpIPAddress;
	private String ftpID;
	private String ftpPasswd;
	private Integer ftpPort;

	private Integer backupTime;
	private boolean useYn = false;
	private boolean logDelYn = false;

//	private boolean alertYn = false;

	public static OBDtoScheduleBackupInfo toScheduleBackupInfo(ScheduleBackupInfoDto sbackup) {
		OBDtoScheduleBackupInfo schbackupFromSvc = new OBDtoScheduleBackupInfo();
		schbackupFromSvc.setUseYN(sbackup.getUseYn() ? OBDefineWeb.CFG_SCH_BACKUP_ON : OBDefineWeb.CFG_SCH_BACKUP_OFF);
		schbackupFromSvc.setIntervalDay(sbackup.getIntervalDay());
		schbackupFromSvc.setBackupTime(sbackup.getBackupTime());
		schbackupFromSvc.setStartTime(sbackup.getStartTime());
		schbackupFromSvc.setEndTime(sbackup.getEndTime());
		schbackupFromSvc.setOption(sbackup.getBackOption());
		schbackupFromSvc.setLogDelYN(
				sbackup.getLogDelYn() ? OBDefineWeb.CFG_BACKUP_LOGDEL_ON : OBDefineWeb.CFG_BACKUP_LOGDEL_OFF);

//		schbackupFromSvc.setLogDelYN(sbackup.getLogDelYN());
//		schbackupFromSvc.setFtpYN(sbackup.getFtpYN());
//		schbackupFromSvc.setFtpIPAddress(sbackup.getFtpIPAddress());
//		schbackupFromSvc.setFtpID(sbackup.getFtpID());
//		schbackupFromSvc.setFtpPasswd(sbackup.getFtpPasswd());
//		schbackupFromSvc.setFtpPort(sbackup.getFtpPort());		
//		schbackupFromSvc.setIntervalDay(50);

		return schbackupFromSvc;
	}

	public Integer getIntervalDay() {
		return intervalDay;
	}

	public void setIntervalDay(Integer intervalDay) {
		this.intervalDay = intervalDay;
	}

//	public Date getBackupTime()
//	{
//		return backupTime;
//	}
//
//	public void setBackupTime(Date backupTime)
//	{
//		this.backupTime = backupTime;
//	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getBackOption() {
		return backOption;
	}

	public void setBackOption(Integer backOption) {
		this.backOption = backOption;
	}

	public Integer getFtpYN() {
		return ftpYN;
	}

	public void setFtpYN(Integer ftpYN) {
		this.ftpYN = ftpYN;
	}

	public String getFtpIPAddress() {
		return ftpIPAddress;
	}

	public void setFtpIPAddress(String ftpIPAddress) {
		this.ftpIPAddress = ftpIPAddress;
	}

	public String getFtpID() {
		return ftpID;
	}

	public void setFtpID(String ftpID) {
		this.ftpID = ftpID;
	}

	public String getFtpPasswd() {
		return ftpPasswd;
	}

	public void setFtpPasswd(String ftpPasswd) {
		this.ftpPasswd = ftpPasswd;
	}

	public Integer getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(Integer ftpPort) {
		this.ftpPort = ftpPort;
	}

	public boolean getUseYn() {
		return useYn;
	}

	public void setUseYn(boolean useYn) {
		this.useYn = useYn;
	}

	public boolean getLogDelYn() {
		return logDelYn;
	}

	public void setLogDelYn(boolean logDelYn) {
		this.logDelYn = logDelYn;
	}

	public Integer getBackupTime() {
		return backupTime;
	}

	public void setBackupTime(Integer backupTime) {
		this.backupTime = backupTime;
	}

	public String toString() {
		return "ScheduleBackupInfoDto [useYN=" + useYn + ", intervalDay=" + intervalDay + ", backupTime=" + backupTime
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", backOption=" + backOption + ", logDelYN="
				+ logDelYn + ", ftpYN=" + ftpYN + ", ftpIPAddress=" + ftpIPAddress + ", ftpID=" + ftpID + ", ftpPasswd="
				+ ftpPasswd + ", ftpPort=" + ftpPort + "]";
	}
}
