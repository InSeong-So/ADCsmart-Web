package kr.openbase.adcsmart.service.impl.pas.handler.dto;

public class OBDtoGatewayInfoPAS
{
	private String destination	="";
	private	String gateway		="";
	private	String interfaceName="";
	@Override
	public String toString()
	{
		return "OBDtoGatewayInfoPAS [destination=" + destination + ", gateway="
				+ gateway + ", interfaceName=" + interfaceName + "]";
	}
	public String getDestination()
	{
		return destination;
	}
	public void setDestination(String destination)
	{
		this.destination = destination;
	}
	public String getGateway()
	{
		return gateway;
	}
	public void setGateway(String gateway)
	{
		this.gateway = gateway;
	}
	public String getInterfaceName()
	{
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName)
	{
		this.interfaceName = interfaceName;
	}
}
