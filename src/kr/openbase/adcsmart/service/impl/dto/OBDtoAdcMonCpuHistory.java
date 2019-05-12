package kr.openbase.adcsmart.service.impl.dto;

import java.util.ArrayList;
import java.util.Date;

public class OBDtoAdcMonCpuHistory
{
	private Date 	occurTime;
	private Integer mpCpu;
	private Integer spCpuAvg;
	private ArrayList<Integer> cpus;
	
	public Date getOccurTime()
	{
		return occurTime;
	}
	public Integer getMpCpu()
	{
		return mpCpu;
	}
	public void setMpCpu(Integer mpCpu)
	{
		this.mpCpu = mpCpu;
	}
	public Integer getSpCpuAvg()
	{
		return spCpuAvg;
	}
	public void setSpCpuAvg(Integer spCpuAvg)
	{
		this.spCpuAvg = spCpuAvg;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public ArrayList<Integer> getCpus()
	{
		return cpus;
	}
	public void setCpus(ArrayList<Integer> cpus)
	{
		this.cpus = cpus;
	}
}
