package kr.openbase.adcsmart.service.dto.report;

public class OBDtoRptSysInfoEtc
{
	private		OBDtoRptSysInfo 	syslogInf;
	private		OBDtoRptSysInfo 	ntpInfo;
	private		OBDtoRptSysInfo 	logInfo;
	@Override
	public String toString()
	{
		return "OBDtoRptSysInfoEtc [syslogInf=" + syslogInf + ", ntpInfo="
				+ ntpInfo + ", logInfo=" + logInfo + "]";
	}
	public OBDtoRptSysInfo getSyslogInf()
	{
		return syslogInf;
	}
	public void setSyslogInf(OBDtoRptSysInfo syslogInf)
	{
		this.syslogInf = syslogInf;
	}
	public OBDtoRptSysInfo getNtpInfo()
	{
		return ntpInfo;
	}
	public void setNtpInfo(OBDtoRptSysInfo ntpInfo)
	{
		this.ntpInfo = ntpInfo;
	}
	public OBDtoRptSysInfo getLogInfo()
	{
		return logInfo;
	}
	public void setLogInfo(OBDtoRptSysInfo logInfo)
	{
		this.logInfo = logInfo;
	}
}
