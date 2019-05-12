package kr.openbase.adcsmart.web.facade.dto;

public class FaultRealInfoDto
{
	private String  DbIndex;
	private String  Id;
	private String 	Name;
	private String 	ipAddress;
	private Integer status;
	private String bpsIn;
	private String bpsOut;
	private	String bpsTotal;
	private String connCurr;
	
	public String getDbIndex()
	{
		return DbIndex;
	}
	public void setDbIndex(String dbIndex)
	{
		DbIndex = dbIndex;
	}
	public String getId()
	{
		return Id;
	}
	public void setId(String id)
	{
		Id = id;
	}
	public String getName()
	{
		return Name;
	}
	public void setName(String name)
	{
		Name = name;
	}
	public String getIpAddress()
	{
		return ipAddress;
	}
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public String getBpsIn()
	{
		return bpsIn;
	}
	public void setBpsIn(String bpsIn)
	{
		this.bpsIn = bpsIn;
	}
	public String getBpsOut()
	{
		return bpsOut;
	}
	public void setBpsOut(String bpsOut)
	{
		this.bpsOut = bpsOut;
	}
	public String getBpsTotal()
	{
		return bpsTotal;
	}
	public void setBpsTotal(String bpsTotal)
	{
		this.bpsTotal = bpsTotal;
	}
	public String getConnCurr()
	{
		return connCurr;
	}
	public void setConnCurr(String connCurr)
	{
		this.connCurr = connCurr;
	}
	@Override
	public String toString()
	{
		return "FaultRealInfoDto [DbIndex=" + DbIndex + ", Id=" + Id + ", Name=" + Name + ", ipAddress=" + ipAddress + ", status=" + status + ", bpsIn=" + bpsIn + ", bpsOut=" + bpsOut + ", bpsTotal=" + bpsTotal + ", connCurr=" + connCurr + "]";
	}	
}