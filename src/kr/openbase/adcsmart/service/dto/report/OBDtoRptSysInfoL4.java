package kr.openbase.adcsmart.service.dto.report;

public class OBDtoRptSysInfoL4
{
	private		OBDtoRptSysInfo 	poolMemberStatus;
	private		OBDtoRptSysInfo 	vsStatus;
	private		OBDtoRptSysInfo 	conntatus;
	private		OBDtoRptSysInfo 	directStatus;
	@Override
	public String toString()
	{
		return "OBDtoRptSysInfoL4 [poolMemberStatus=" + poolMemberStatus
				+ ", vsStatus=" + vsStatus + ", conntatus=" + conntatus
				+ ", directStatus=" + directStatus + "]";
	}
	public OBDtoRptSysInfo getPoolMemberStatus()
	{
		return poolMemberStatus;
	}
	public void setPoolMemberStatus(OBDtoRptSysInfo poolMemberStatus)
	{
		this.poolMemberStatus = poolMemberStatus;
	}
	public OBDtoRptSysInfo getVsStatus()
	{
		return vsStatus;
	}
	public void setVsStatus(OBDtoRptSysInfo vsStatus)
	{
		this.vsStatus = vsStatus;
	}
	public OBDtoRptSysInfo getConntatus()
	{
		return conntatus;
	}
	public void setConntatus(OBDtoRptSysInfo conntatus)
	{
		this.conntatus = conntatus;
	}
	public OBDtoRptSysInfo getDirectStatus()
	{
		return directStatus;
	}
	public void setDirectStatus(OBDtoRptSysInfo directStatus)
	{
		this.directStatus = directStatus;
	}
}
