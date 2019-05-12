package kr.openbase.adcsmart.service.impl.dto;

import java.sql.Timestamp;

public class OBDtoMonTrafficVServer
{
	private Timestamp time	=null;	
	private String 	objName	="";
	private String name		="";
	private String ipaddress="";
	private String alteonID;
	private int 	srvPort;
//	private Integer status;
	private long curConns	=0;
	private long maxConns	=0;
	private long totConns	=0;
	private long pktsIn		=0;
	private long pktsOut	=0;
	private long bytesIn	=0;
	private long bytesOut	=0;
	
	@Override
	public String toString()
	{
		return String.format("OBDtoMonTrafficVServer [time=%s, objName=%s, name=%s, ipaddress=%s, alteonID=%s, srvPort=%s, curConns=%s, maxConns=%s, totConns=%s, pktsIn=%s, pktsOut=%s, bytesIn=%s, bytesOut=%s]", time, objName, name, ipaddress, alteonID, srvPort, curConns, maxConns, totConns, pktsIn, pktsOut, bytesIn, bytesOut);
	}

	public String getObjName()
	{
		return objName;
	}

	public int getSrvPort()
	{
		return srvPort;
	}

	public void setSrvPort(int srvPort)
	{
		this.srvPort = srvPort;
	}

	public void setObjName(String objName)
	{
		this.objName = objName;
	}

	public Timestamp getTime()
	{
		return time;
	}

	public void setTime(Timestamp time)
	{
		this.time = time;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getIpaddress()
	{
		return ipaddress;
	}

	public void setIpaddress(String ipaddress)
	{
		this.ipaddress = ipaddress;
	}

	public String getAlteonID()
	{
		return alteonID;
	}

	public void setAlteonID(String alteonID)
	{
		this.alteonID = alteonID;
	}

	public long getCurConns()
	{
		return curConns;
	}

	public void setCurConns(long curConns)
	{
		this.curConns = curConns;
	}

	public long getMaxConns()
	{
		return maxConns;
	}

	public void setMaxConns(long maxConns)
	{
		this.maxConns = maxConns;
	}

	public long getTotConns()
	{
		return totConns;
	}

	public void setTotConns(long totConns)
	{
		this.totConns = totConns;
	}

	public long getPktsIn()
	{
		return pktsIn;
	}

	public void setPktsIn(long pktsIn)
	{
		this.pktsIn = pktsIn;
	}

	public long getPktsOut()
	{
		return pktsOut;
	}

	public void setPktsOut(long pktsOut)
	{
		this.pktsOut = pktsOut;
	}

	public long getBytesIn()
	{
		return bytesIn;
	}

	public void setBytesIn(long bytesIn)
	{
		this.bytesIn = bytesIn;
	}

	public long getBytesOut()
	{
		return bytesOut;
	}

	public void setBytesOut(long bytesOut)
	{
		this.bytesOut = bytesOut;
	}
}
