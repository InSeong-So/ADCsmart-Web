package kr.openbase.adcsmart.service.snmp.dto;

import java.util.ArrayList;

public class DtoRptOPEtc
{
	private ArrayList<DtoRptSyslog> syslogList;
	private DtoRptNtp ntpInfo;
	public DtoRptNtp getNtpInfo()
	{
		return ntpInfo;
	}
	public void setNtpInfo(DtoRptNtp ntpInfo)
	{
		this.ntpInfo = ntpInfo;
	}
	@Override
	public String toString()
	{
		return "DtoRptOPEtc [syslogList=" + syslogList + ", ntpInfo=" + ntpInfo
				+ "]";
	}
	public ArrayList<DtoRptSyslog> getSyslogList()
	{
		return syslogList;
	}
	public void setSyslogList(ArrayList<DtoRptSyslog> syslogList)
	{
		this.syslogList = syslogList;
	}
}
