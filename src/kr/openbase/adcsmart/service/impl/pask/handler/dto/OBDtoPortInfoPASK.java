package kr.openbase.adcsmart.service.impl.pask.handler.dto;

public class OBDtoPortInfoPASK
{
	private String	portName		="";
	private int		linkStatus		=0;// 
	private String	speed			="";
	private String	duplex			="";
	@Override
	public String toString()
	{
		return "OBDtoPortInfoPASK [portName=" + portName + ", linkStatus=" + linkStatus + ", speed=" + speed + ", duplex=" + duplex + "]";
	}
	public String getPortName()
	{
		return portName;
	}
	public void setPortName(String portName)
	{
		this.portName = portName;
	}
	public int getLinkStatus()
	{
		return linkStatus;
	}
	public void setLinkStatus(int linkStatus)
	{
		this.linkStatus = linkStatus;
	}
	public String getSpeed()
	{
		return speed;
	}
	public void setSpeed(String speed)
	{
		this.speed = speed;
	}
	public String getDuplex()
	{
		return duplex;
	}
	public void setDuplex(String duplex)
	{
		this.duplex = duplex;
	}
}
