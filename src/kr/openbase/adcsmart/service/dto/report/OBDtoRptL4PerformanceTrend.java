package kr.openbase.adcsmart.service.dto.report;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoUsageThroughput;

public class OBDtoRptL4PerformanceTrend
{
	private static final int NAME_MIN_LENGTH = 20;
	private static final int DOT_LENGTH = 4;
	
	private Integer rank=0;
	private String vsIPAddress="";
	private Integer vsPort=0;
	private ArrayList<OBDtoUsageThroughput> usage;
	private String vsName="";
	
	public static String toVsName(String vsName)
	{
		String ret = "";
		if(vsName.length()>NAME_MIN_LENGTH)
		{
			ret = vsName.substring(0,(NAME_MIN_LENGTH - DOT_LENGTH))+" ...";
		}
		else
		{
			ret = vsName;
		}	
		return ret;
	}
		
	@Override
	public String toString()
	{
		return "OBDtoRptL4PerformanceTrend [rank=" + rank + ", vsIPAddress="
				+ vsIPAddress + ", vsPort=" + vsPort + ", usage=" + usage
				+ ", vsName=" + vsName + "]";
	}
	public Integer getRank()
	{
		return rank;
	}
	public void setRank(Integer rank)
	{
		this.rank = rank;
	}
	public String getVsIPAddress()
	{
		return vsIPAddress;
	}
	public Integer getVsPort()
	{
		return vsPort;
	}
	public void setVsPort(Integer vsPort)
	{
		this.vsPort = vsPort;
	}
	public void setVsIPAddress(String vsIPAddress)
	{
		this.vsIPAddress = vsIPAddress;
	}
	public ArrayList<OBDtoUsageThroughput> getUsage()
	{
		return usage;
	}
	public void setUsage(ArrayList<OBDtoUsageThroughput> usage)
	{
		this.usage = usage;
	}
	public String getVsName()
	{
		return vsName;
	}
	public void setVsName(String vsName)
	{
		this.vsName = vsName;
	}
}
