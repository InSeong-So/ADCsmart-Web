package kr.openbase.adcsmart.service.dto.report;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAuditLogAdcSystem;

public class OBDtoRptAdcLogInfo
{
	private String adcName;
	private Integer adcIndex;
	private String adcIPAddress;
	private ArrayList<OBDtoAuditLogAdcSystem> logList;
	@Override
	public String toString()
	{
		return "OBDtoRptAdcLogInfo [adcName=" + adcName + ", adcIndex="
				+ adcIndex + ", adcIPAddress=" + adcIPAddress + ", logList="
				+ logList + "]";
	}
	public String getAdcName()
	{
		return adcName;
	}
	public String getAdcIPAddress()
	{
		return adcIPAddress;
	}
	public void setAdcIPAddress(String adcIPAddress)
	{
		this.adcIPAddress = adcIPAddress;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public ArrayList<OBDtoAuditLogAdcSystem> getLogList()
	{
		return logList;
	}
	public void setLogList(ArrayList<OBDtoAuditLogAdcSystem> logList)
	{
		this.logList = logList;
	}
}
