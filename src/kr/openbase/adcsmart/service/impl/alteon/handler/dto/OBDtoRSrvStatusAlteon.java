package kr.openbase.adcsmart.service.impl.alteon.handler.dto;

public class OBDtoRSrvStatusAlteon
{
	private	int		realId;// alteon id
	private	String 	realIP;
	private int		realPort;// 
	private	int		status;
	@Override
	public String toString()
	{
		return "OBDtoRSrvStatusAlteon [realId=" + realId + ", realIP=" + realIP + ", realPort=" + realPort + ", status=" + status + "]";
	}
	public int getRealId()
	{
		return realId;
	}
	public void setRealId(int realId)
	{
		this.realId = realId;
	}
	public String getRealIP()
	{
		return realIP;
	}
	public void setRealIP(String realIP)
	{
		this.realIP = realIP;
	}
	public int getRealPort()
	{
		return realPort;
	}
	public void setRealPort(int realPort)
	{
		this.realPort = realPort;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
}
