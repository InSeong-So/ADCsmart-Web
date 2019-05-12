package kr.openbase.adcsmart.service.dto.report;

public class OBDtoRptSysInfoBasic
{
	private		OBDtoRptSysInfo upTime;
	private		OBDtoRptSysInfo lastApplyTime;
	private		OBDtoRptSysInfo cpuInfo;
	private		OBDtoRptSysInfo memoryInfo;
	private		OBDtoRptSysInfo powerInfo;
	private		OBDtoRptSysInfo fanInfo;
	@Override
	public String toString()
	{
		return "OBDtoRptSysInfoBasic [upTime=" + upTime + ", lastApplyTime="
				+ lastApplyTime + ", cpuInfo=" + cpuInfo + ", memoryInfo="
				+ memoryInfo + ", powerInfo=" + powerInfo + ", fanInfo="
				+ fanInfo + "]";
	}
	public OBDtoRptSysInfo getUpTime()
	{
		return upTime;
	}
	public void setUpTime(OBDtoRptSysInfo upTime)
	{
		this.upTime = upTime;
	}
	public OBDtoRptSysInfo getLastApplyTime()
	{
		return lastApplyTime;
	}
	public void setLastApplyTime(OBDtoRptSysInfo lastApplyTime)
	{
		this.lastApplyTime = lastApplyTime;
	}
	public OBDtoRptSysInfo getCpuInfo()
	{
		return cpuInfo;
	}
	public void setCpuInfo(OBDtoRptSysInfo cpuInfo)
	{
		this.cpuInfo = cpuInfo;
	}
	public OBDtoRptSysInfo getMemoryInfo()
	{
		return memoryInfo;
	}
	public void setMemoryInfo(OBDtoRptSysInfo memoryInfo)
	{
		this.memoryInfo = memoryInfo;
	}
	public OBDtoRptSysInfo getPowerInfo()
	{
		return powerInfo;
	}
	public void setPowerInfo(OBDtoRptSysInfo powerInfo)
	{
		this.powerInfo = powerInfo;
	}
	public OBDtoRptSysInfo getFanInfo()
	{
		return fanInfo;
	}
	public void setFanInfo(OBDtoRptSysInfo fanInfo)
	{
		this.fanInfo = fanInfo;
	}
}
