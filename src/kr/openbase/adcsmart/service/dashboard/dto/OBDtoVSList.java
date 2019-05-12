package kr.openbase.adcsmart.service.dashboard.dto;

public class OBDtoVSList
{
	private String vsIndex;
	private String vsName;
	private String vsIP;
	private Integer vsPort;
	private Integer vsStatus;

	@Override
	public String toString()
	{
		return "OBDtoVSList [vsIndex=" + vsIndex + ", vsName=" + vsName + ", vsIP=" + vsIP + ", vsPort=" + vsPort
				+ ", vsStatus=" + vsStatus + "]";
	}

	public String getVsIndex()
	{
		return vsIndex;
	}

	public void setVsIndex(String vsIndex)
	{
		this.vsIndex = vsIndex;
	}

	public String getVsName()
	{
		return vsName;
	}

	public void setVsName(String vsName)
	{
		this.vsName = vsName;
	}

	public String getVsIP()
	{
		return vsIP;
	}

	public void setVsIP(String vsIP)
	{
		this.vsIP = vsIP;
	}

	public Integer getVsPort()
	{
		return vsPort;
	}

	public void setVsPort(Integer vsPort)
	{
		this.vsPort = vsPort;
	}

	public Integer getVsStatus()
	{
		return vsStatus;
	}

	public void setVsStatus(Integer vsStatus)
	{
		this.vsStatus = vsStatus;
	}
}
