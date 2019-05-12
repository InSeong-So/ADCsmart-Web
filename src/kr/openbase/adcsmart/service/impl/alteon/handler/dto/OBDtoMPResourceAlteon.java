package kr.openbase.adcsmart.service.impl.alteon.handler.dto;

public class OBDtoMPResourceAlteon
{
	private int 	cpuUsage;
	private long 	memTotal;
	private	long	memFree;
	private	int 	memUsage;
	@Override
	public String toString()
	{
		return "OBDtoMPResourceAlteon [cpuUsage=" + cpuUsage + ", memTotal=" + memTotal + ", memFree=" + memFree + ", memUsage=" + memUsage + "]";
	}
	public long getMemFree()
	{
		return memFree;
	}
	public void setMemFree(long memFree)
	{
		this.memFree = memFree;
	}
	public int getCpuUsage()
	{
		return cpuUsage;
	}
	public void setCpuUsage(int cpuUsage)
	{
		this.cpuUsage = cpuUsage;
	}
	public long getMemTotal()
	{
		return memTotal;
	}
	public void setMemTotal(long memTotal)
	{
		this.memTotal = memTotal;
	}
	public int getMemUsage()
	{
		return memUsage;
	}
	public void setMemUsage(int memUsage)
	{
		this.memUsage = memUsage;
	}
}
