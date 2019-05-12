package kr.openbase.adcsmart.service.impl.pask.handler.dto;

public class OBDtoResourceInfoPASK
{
	private int		cpuUsageMP		=0;
	private int		cpuUsageSP		=0;
	private long	totalMemMP		=0L;//Byte단위.
	private long	totalMemSP		=0L;//Byte단위.
	private long	usedMemMP		=0L;
	private long	freeMemMP		=0L;
	private int		memUsageMP		=0;
	private long	usedMemSP		=0L;
	private long	freeMemSP		=0L;
	private int		memUsageSP		=0;
	@Override
	public String toString()
	{
		return "OBDtoResourceInfoPASK [cpuUsageMP=" + cpuUsageMP + ", cpuUsageSP=" + cpuUsageSP + ", totalMemMP=" + totalMemMP + ", totalMemSP=" + totalMemSP + ", usedMemMP=" + usedMemMP + ", freeMemMP=" + freeMemMP + ", memUsageMP=" + memUsageMP + ", usedMemSP=" + usedMemSP + ", freeMemSP=" + freeMemSP + ", memUsageSP=" + memUsageSP + "]";
	}
	public int getCpuUsageMP()
	{
		return cpuUsageMP;
	}
	public void setCpuUsageMP(int cpuUsageMP)
	{
		this.cpuUsageMP = cpuUsageMP;
	}
	public int getCpuUsageSP()
	{
		return cpuUsageSP;
	}
	public void setCpuUsageSP(int cpuUsageSP)
	{
		this.cpuUsageSP = cpuUsageSP;
	}
	public long getTotalMemMP()
	{
		return totalMemMP;
	}
	public void setTotalMemMP(long totalMemMP)
	{
		this.totalMemMP = totalMemMP;
	}
	public long getTotalMemSP()
	{
		return totalMemSP;
	}
	public void setTotalMemSP(long totalMemSP)
	{
		this.totalMemSP = totalMemSP;
	}
	public long getUsedMemMP()
	{
		return usedMemMP;
	}
	public void setUsedMemMP(long usedMemMP)
	{
		this.usedMemMP = usedMemMP;
	}
	public long getFreeMemMP()
	{
		return freeMemMP;
	}
	public void setFreeMemMP(long freeMemMP)
	{
		this.freeMemMP = freeMemMP;
	}
	public int getMemUsageMP()
	{
		return memUsageMP;
	}
	public void setMemUsageMP(int memUsageMP)
	{
		this.memUsageMP = memUsageMP;
	}
	public long getUsedMemSP()
	{
		return usedMemSP;
	}
	public void setUsedMemSP(long usedMemSP)
	{
		this.usedMemSP = usedMemSP;
	}
	public long getFreeMemSP()
	{
		return freeMemSP;
	}
	public void setFreeMemSP(long freeMemSP)
	{
		this.freeMemSP = freeMemSP;
	}
	public int getMemUsageSP()
	{
		return memUsageSP;
	}
	public void setMemUsageSP(int memUsageSP)
	{
		this.memUsageSP = memUsageSP;
	}
}
