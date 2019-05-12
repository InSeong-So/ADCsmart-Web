package kr.openbase.adcsmart.service.impl.f5;

import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTraffic;

class DtoNodeTraffic
{
//	private String name;
	private String ipaddress;
	private int port;
	private int alteonID;
	private OBDtoMonTraffic traffic;
	private int status;
	
	public void setIpaddress(String ipaddress)
	{
		this.ipaddress = ipaddress;
	}
	public String getIpaddress()
	{
		return this.ipaddress;
	}

	public void setPort(int port)
	{
		this.port = port;
	}
	public int getPort()
	{
		return this.port;
	}

	public void setAlteonID(int alteonID)
	{
		this.alteonID = alteonID;
	}
	public int getAlteonID()
	{
		return this.alteonID;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}
	public int getStatus()
	{
		return this.status;
	}
	
//	public void setName(String name)
//	{
//		this.name = name;
//	}
//	public String getName()
//	{
//		return this.name;
//	}

	public void setTraffic(OBDtoMonTraffic traffic)
	{
		this.traffic = traffic;
	}
	public OBDtoMonTraffic getTraffic()
	{
		return this.traffic;
	}
	
	@Override
	public String toString()
	{
		return "OBDtoMonTrafficNode [ipaddress=" + ipaddress + ", port=" + port
				+ ", alteonID=" + alteonID + ", traffic=" + traffic
				+ ", status=" + status + "]";
	}	
}
