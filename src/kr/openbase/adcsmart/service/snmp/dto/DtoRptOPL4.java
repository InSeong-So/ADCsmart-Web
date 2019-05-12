package kr.openbase.adcsmart.service.snmp.dto;

import java.util.ArrayList;

public class DtoRptOPL4
{
	private ArrayList<DtoRptPMStatus> pmList		=null;
	private ArrayList<DtoRptVSStatus> vsList		=null;
	private DtoRptConnStatus 		  connStatus	=null;
	private int	directMode							=2;//enabled:1, disabled:2
	
	@Override
	public String toString()
	{
		return "DtoRptOPL4 [pmList=" + pmList + ", vsList=" + vsList
				+ ", connStatus=" + connStatus + ", directMode=" + directMode
				+ "]";
	}
	public ArrayList<DtoRptPMStatus> getPmList()
	{
		return pmList;
	}
	public void setPmList(ArrayList<DtoRptPMStatus> pmList)
	{
		this.pmList = pmList;
	}
	public ArrayList<DtoRptVSStatus> getVsList()
	{
		return vsList;
	}
	public void setVsList(ArrayList<DtoRptVSStatus> vsList)
	{
		this.vsList = vsList;
	}
	public DtoRptConnStatus getConnStatus()
	{
		return connStatus;
	}
	public void setConnStatus(DtoRptConnStatus connStatus)
	{
		this.connStatus = connStatus;
	}
	public int getDirectMode()
	{
		return directMode;
	}
	public void setDirectMode(int directMode)
	{
		this.directMode = directMode;
	}
}
