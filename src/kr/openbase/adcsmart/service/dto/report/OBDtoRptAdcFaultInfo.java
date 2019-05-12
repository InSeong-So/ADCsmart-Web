package kr.openbase.adcsmart.service.dto.report;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcSystemLog;

public class OBDtoRptAdcFaultInfo
{
	private Integer adcIndex;
	private String adcName;
	private String adcIPAddress;
	private ArrayList<OBDtoAdcSystemLog> logList;
	@Override
	public String toString()
	{
		return "OBDtoRptAdcFaultInfo [adcIndex=" + adcIndex + ", adcName="
				+ adcName + ", adcIPAddress=" + adcIPAddress + ", logList="
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
	public ArrayList<OBDtoAdcSystemLog> getLogList()
	{
		return logList;
	}
	public void setLogList(ArrayList<OBDtoAdcSystemLog> logList)
	{
		this.logList = logList;
	}
}
