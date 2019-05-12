package kr.openbase.adcsmart.web.report.admin;

import kr.openbase.adcsmart.service.dto.report.OBDtoRptSysInfoEtc;

public class SystemMiscStatusDto
{
	private ResultDetailDto sysLog;
	private ResultDetailDto ntp;
	private ResultDetailDto log;
	
	public static SystemMiscStatusDto toSystemMiscStatusDto(OBDtoRptSysInfoEtc statusFromSvc) 
	{
		if (statusFromSvc == null)
			return null;
		
		SystemMiscStatusDto status = new SystemMiscStatusDto();
		status.setSysLog(ResultDetailDto.toResultDetailDto(statusFromSvc.getSyslogInf()));
		status.setNtp(ResultDetailDto.toResultDetailDto(statusFromSvc.getNtpInfo()));
		status.setLog(ResultDetailDto.toResultDetailDto(statusFromSvc.getLogInfo()));
		return status;
	}
	
	public ResultDetailDto getSysLog() 
	{
		return sysLog;
	}
	public void setSysLog(ResultDetailDto sysLog)
	{
		this.sysLog = sysLog;
	}
	public ResultDetailDto getNtp()
	{
		return ntp;
	}
	public void setNtp(ResultDetailDto ntp)
	{
		this.ntp = ntp;
	}
	public ResultDetailDto getLog()
	{
		return log;
	}
	public void setLog(ResultDetailDto log)
	{
		this.log = log;
	}
	@Override
	public String toString()
	{
		return "SystemMiscStatusDto [sysLog=" + sysLog + ", ntp=" + ntp + ", log=" + log + "]";
	}
	
}
