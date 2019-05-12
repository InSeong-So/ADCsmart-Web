package kr.openbase.adcsmart.service.snmp.dto;

import java.util.ArrayList;

public class DtoRptOPL7
{
	private ArrayList<DtoRptOPL7iRule> iRuleList;
	private ArrayList<DtoRptOPL7iRule> oneConnect;
	private ArrayList<DtoRptOPL7iRule> ramCache;
	private ArrayList<DtoRptOPL7iRule> compression;
	private ArrayList<DtoRptOPL7iRule> sslAccel;
	@Override
	public String toString()
	{
		return "DtoRptOPL7 [iRuleList=" + iRuleList + ", oneConnect="
				+ oneConnect + ", ramCache=" + ramCache + ", compression="
				+ compression + ", sslAccel=" + sslAccel + "]";
	}
	public ArrayList<DtoRptOPL7iRule> getiRuleList()
	{
		return iRuleList;
	}
	public void setiRuleList(ArrayList<DtoRptOPL7iRule> iRuleList)
	{
		this.iRuleList = iRuleList;
	}
	public ArrayList<DtoRptOPL7iRule> getOneConnect()
	{
		return oneConnect;
	}
	public void setOneConnect(ArrayList<DtoRptOPL7iRule> oneConnect)
	{
		this.oneConnect = oneConnect;
	}
	public ArrayList<DtoRptOPL7iRule> getRamCache()
	{
		return ramCache;
	}
	public void setRamCache(ArrayList<DtoRptOPL7iRule> ramCache)
	{
		this.ramCache = ramCache;
	}
	public ArrayList<DtoRptOPL7iRule> getCompression()
	{
		return compression;
	}
	public void setCompression(ArrayList<DtoRptOPL7iRule> compression)
	{
		this.compression = compression;
	}
	public ArrayList<DtoRptOPL7iRule> getSslAccel()
	{
		return sslAccel;
	}
	public void setSslAccel(ArrayList<DtoRptOPL7iRule> sslAccel)
	{
		this.sslAccel = sslAccel;
	}
}
