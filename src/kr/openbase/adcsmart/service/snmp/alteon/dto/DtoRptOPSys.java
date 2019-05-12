package kr.openbase.adcsmart.service.snmp.alteon.dto;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.snmp.dto.DtoRptCpu;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptMem;

public class DtoRptOPSys
{
	private String upTime;
	private ArrayList<DtoRptCpu> cpuList;
	private ArrayList<DtoRptMem> memList;
	private int fanStatus;
	private int powerSupplyStatus;
	@Override
	public String toString()
	{
		return "DtoRptOPSys [upTime=" + upTime + ", cpuList=" + cpuList
				+ ", memList=" + memList + ", fanStatus=" + fanStatus
				+ ", powerSupplyStatus=" + powerSupplyStatus + "]";
	}
	public String getUpTime()
	{
		return upTime;
	}
	public void setUpTime(String upTime)
	{
		this.upTime = upTime;
	}
	public ArrayList<DtoRptCpu> getCpuList()
	{
		return cpuList;
	}
	public void setCpuList(ArrayList<DtoRptCpu> cpuList)
	{
		this.cpuList = cpuList;
	}
	public ArrayList<DtoRptMem> getMemList()
	{
		return memList;
	}
	public void setMemList(ArrayList<DtoRptMem> memList)
	{
		this.memList = memList;
	}
	public int getFanStatus()
	{
		return fanStatus;
	}
	public void setFanStatus(int fanStatus)
	{
		this.fanStatus = fanStatus;
	}
	public int getPowerSupplyStatus()
	{
		return powerSupplyStatus;
	}
	public void setPowerSupplyStatus(int powerSupplyStatus)
	{
		this.powerSupplyStatus = powerSupplyStatus;
	}
}
