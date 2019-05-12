package kr.openbase.adcsmart.service.impl.pask.handler.dto;

public class DtoRptTrunkInfoPASK
{
	private String 	name;
	private String 	algorithm;
	private String	portList;
	@Override
	public String toString()
	{
		return "DtoRptTrunkInfoPASK [name=" + name + ", algorithm=" + algorithm + ", portList=" + portList + "]";
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getAlgorithm()
	{
		return algorithm;
	}
	public void setAlgorithm(String algorithm)
	{
		this.algorithm = algorithm;
	}
	public String getPortList()
	{
		return portList;
	}
	public void setPortList(String portList)
	{
		this.portList = portList;
	}
}
