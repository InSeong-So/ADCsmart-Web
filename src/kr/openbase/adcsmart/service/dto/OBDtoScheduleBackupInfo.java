package kr.openbase.adcsmart.service.dto;

import java.util.Date;

/**
 * 환경설정에서 예약 백업을 처리하기 위한 DTO, 현재는 시스템 백업에 예약 백업을 통합
 * @author sw.jung
 * @see kr.openbase.adcsmart.service.dto.OBDtoBackupSchedule
 */
@Deprecated
public class OBDtoScheduleBackupInfo
{
	public static Integer USE_YES=1;
	public static Integer USE_NO=2;
	public static Integer OPTION_TOTAL=0;
	public static Integer OPTION_CFG=1;
	public static Integer OPTION_LOG=2;
	public static Integer LOGDEL_YES=1;
	public static Integer LOGDEL_NO=2;
	public static Integer FTP_YES=1;
	public static Integer FTP_NO=2;
	
	private Integer useYN;//1: used, 2: not use
	private Integer intervalDay;
	private Integer	backupTime;
	private Date 	startTime;// 백업 예약 기간. 시작 날짜.
	private Date 	endTime;// 백업 예약 기간. 종료 날짜.
	private Integer option;//0:전체. 1: ADC 설정정보. 2: ADC 로그 정보.
	private Integer logDelYN;//1:delete, 2: not delete
	private Integer ftpYN;//1:yes, 2:no. 파일 전송 유무.
	private String  ftpIPAddress;
	private String  ftpID;
	public Integer getBackupTime()
	{
		return backupTime;
	}
	public void setBackupTime(Integer backupTime)
	{
		this.backupTime = backupTime;
	}
	private String  ftpPasswd;
	private Integer ftpPort;
	
	@Override
	public String toString()
	{
		return "OBDtoScheduleBackupInfo [useYN=" + useYN + ", intervalDay="
				+ intervalDay + ", backupTime=" + backupTime + ", startTime="
				+ startTime + ", endTime=" + endTime + ", option=" + option
				+ ", logDelYN=" + logDelYN + ", ftpYN=" + ftpYN
				+ ", ftpIPAddress=" + ftpIPAddress + ", ftpID=" + ftpID
				+ ", ftpPasswd=" + ftpPasswd + ", ftpPort=" + ftpPort + "]";
	}
	public Integer getUseYN()
	{
		return useYN;
	}
	public Integer getFtpYN()
	{
		return ftpYN;
	}
	public void setFtpYN(Integer ftpYN)
	{
		this.ftpYN = ftpYN;
	}
	public String getFtpIPAddress()
	{
		return ftpIPAddress;
	}
	public void setFtpIPAddress(String ftpIPAddress)
	{
		this.ftpIPAddress = ftpIPAddress;
	}
	public String getFtpID()
	{
		return ftpID;
	}
	public void setFtpID(String ftpID)
	{
		this.ftpID = ftpID;
	}
	public String getFtpPasswd()
	{
		return ftpPasswd;
	}
	public void setFtpPasswd(String ftpPasswd)
	{
		this.ftpPasswd = ftpPasswd;
	}
	public Integer getFtpPort()
	{
		return ftpPort;
	}
	public void setFtpPort(Integer ftpPort)
	{
		this.ftpPort = ftpPort;
	}
	public void setUseYN(Integer useYN)
	{
		this.useYN = useYN;
	}
	public Integer getIntervalDay()
	{
		return intervalDay;
	}
	public void setIntervalDay(Integer intervalDay)
	{
		this.intervalDay = intervalDay;
	}
	public Date getStartTime()
	{
		return startTime;
	}
	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}
	public Date getEndTime()
	{
		return endTime;
	}
	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}
	public Integer getOption()
	{
		return option;
	}
	public void setOption(Integer option)
	{
		this.option = option;
	}
	public Integer getLogDelYN()
	{
		return logDelYN;
	}
	public void setLogDelYN(Integer logDelYN)
	{
		this.logDelYN = logDelYN;
	}
}
