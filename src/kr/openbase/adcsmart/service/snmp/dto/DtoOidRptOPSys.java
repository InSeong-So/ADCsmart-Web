package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidRptOPSys
{
	private String upTime;
	private String lastApplyTime;
	private String cpuMPUtil64;
	private String cpuSPUtil64;
	private String memMPUsed;
	private String memMPTotal;
	private String memSPUsed;
	private String memSPTotal;
	private String memTmmUsed;
	private String memTmmTotal;
	private String fanStatus;
	private String powerSupplyStatus;
	@Override
	public String toString()
	{
		return "DtoOidRptOPSys [upTime=" + upTime + ", lastApplyTime="
				+ lastApplyTime + ", cpuMPUtil64=" + cpuMPUtil64
				+ ", cpuSPUtil64=" + cpuSPUtil64 + ", memMPUsed=" + memMPUsed
				+ ", memMPTotal=" + memMPTotal + ", memSPUsed=" + memSPUsed
				+ ", memSPTotal=" + memSPTotal + ", memTmmUsed=" + memTmmUsed
				+ ", memTmmTotal=" + memTmmTotal + ", fanStatus=" + fanStatus
				+ ", powerSupplyStatus=" + powerSupplyStatus + "]";
	}
	public String getUpTime()
	{
		return upTime;
	}
	public String getMemSPUsed()
	{
		return memSPUsed;
	}
	public void setMemSPUsed(String memSPUsed)
	{
		this.memSPUsed = memSPUsed;
	}
	public String getMemSPTotal()
	{
		return memSPTotal;
	}
	public void setMemSPTotal(String memSPTotal)
	{
		this.memSPTotal = memSPTotal;
	}
	public void setUpTime(String upTime)
	{
		this.upTime = upTime;
	}
	public String getLastApplyTime()
	{
		return lastApplyTime;
	}
	public void setLastApplyTime(String lastApplyTime)
	{
		this.lastApplyTime = lastApplyTime;
	}
	public String getCpuMPUtil64()
	{
		return cpuMPUtil64;
	}
	public void setCpuMPUtil64(String cpuMPUtil64)
	{
		this.cpuMPUtil64 = cpuMPUtil64;
	}
	public String getCpuSPUtil64()
	{
		return cpuSPUtil64;
	}
	public void setCpuSPUtil64(String cpuSPUtil64)
	{
		this.cpuSPUtil64 = cpuSPUtil64;
	}
	public String getMemMPUsed()
	{
		return memMPUsed;
	}
	public void setMemMPUsed(String memMPUsed)
	{
		this.memMPUsed = memMPUsed;
	}
	public String getMemMPTotal()
	{
		return memMPTotal;
	}
	public void setMemMPTotal(String memMPTotal)
	{
		this.memMPTotal = memMPTotal;
	}
	public String getMemTmmUsed()
	{
		return memTmmUsed;
	}
	public void setMemTmmUsed(String memTmmUsed)
	{
		this.memTmmUsed = memTmmUsed;
	}
	public String getMemTmmTotal()
	{
		return memTmmTotal;
	}
	public void setMemTmmTotal(String memTmmTotal)
	{
		this.memTmmTotal = memTmmTotal;
	}
	public String getFanStatus()
	{
		return fanStatus;
	}
	public void setFanStatus(String fanStatus)
	{
		this.fanStatus = fanStatus;
	}
	public String getPowerSupplyStatus()
	{
		return powerSupplyStatus;
	}
	public void setPowerSupplyStatus(String powerSupplyStatus)
	{
		this.powerSupplyStatus = powerSupplyStatus;
	}
}
