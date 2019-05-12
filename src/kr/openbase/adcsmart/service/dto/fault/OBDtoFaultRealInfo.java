package kr.openbase.adcsmart.service.dto.fault;

public class OBDtoFaultRealInfo
{
	private String  DbIndex;
	private String  Id;
	private String 	Name;
	private String 	ipAddress;
	private Integer status;
	private Long bpsIn;
	private Long bpsOut;
	private	Long bpsTotal;
	private Long connCurr;
	@Override
	public String toString()
	{
		return "OBDtoFaultRealPerfInfo [DbIndex=" + DbIndex + ", Id=" + Id
				+ ", Name=" + Name + ", ipAddress=" + ipAddress + ", status="
				+ status + ", bpsIn=" + bpsIn + ", bpsOut=" + bpsOut
				+ ", bpsTotal=" + bpsTotal + ", connCurr=" + connCurr + "]";
	}
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
	public Long getBpsIn()
	{
		return bpsIn;
	}
	public void setBpsIn(Long bpsIn)
	{
		this.bpsIn = bpsIn;
	}
	public Long getBpsOut()
	{
		return bpsOut;
	}
	public void setBpsOut(Long bpsOut)
	{
		this.bpsOut = bpsOut;
	}
	public Long getBpsTotal()
	{
		return bpsTotal;
	}
	public void setBpsTotal(Long bpsTotal)
	{
		this.bpsTotal = bpsTotal;
	}
	public Long getConnCurr()
	{
		return connCurr;
	}
	public void setConnCurr(Long connCurr)
	{
		this.connCurr = connCurr;
	}
}
