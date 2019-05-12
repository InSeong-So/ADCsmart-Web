package kr.openbase.adcsmart.service.dto.report;

public class OBDtoRptSysInfoL7
{
	private		OBDtoRptSysInfo 	iruleStatus;
	private		OBDtoRptSysInfo 	oneConnStatus;
	private		OBDtoRptSysInfo 	ramCacheStatus;
	private		OBDtoRptSysInfo 	compStatus;
	private		OBDtoRptSysInfo 	sslStatus;
	@Override
	public String toString()
	{
		return "OBDtoRptSysInfoL7 [iruleStatus=" + iruleStatus
				+ ", oneConnStatus=" + oneConnStatus + ", ramCacheStatus="
				+ ramCacheStatus + ", compStatus=" + compStatus
				+ ", sslStatus=" + sslStatus + "]";
	}
	public OBDtoRptSysInfo getIruleStatus()
	{
		return iruleStatus;
	}
	public void setIruleStatus(OBDtoRptSysInfo iruleStatus)
	{
		this.iruleStatus = iruleStatus;
	}
	public OBDtoRptSysInfo getOneConnStatus()
	{
		return oneConnStatus;
	}
	public void setOneConnStatus(OBDtoRptSysInfo oneConnStatus)
	{
		this.oneConnStatus = oneConnStatus;
	}
	public OBDtoRptSysInfo getRamCacheStatus()
	{
		return ramCacheStatus;
	}
	public void setRamCacheStatus(OBDtoRptSysInfo ramCacheStatus)
	{
		this.ramCacheStatus = ramCacheStatus;
	}
	public OBDtoRptSysInfo getCompStatus()
	{
		return compStatus;
	}
	public void setCompStatus(OBDtoRptSysInfo compStatus)
	{
		this.compStatus = compStatus;
	}
	public OBDtoRptSysInfo getSslStatus()
	{
		return sslStatus;
	}
	public void setSslStatus(OBDtoRptSysInfo sslStatus)
	{
		this.sslStatus = sslStatus;
	}
}
