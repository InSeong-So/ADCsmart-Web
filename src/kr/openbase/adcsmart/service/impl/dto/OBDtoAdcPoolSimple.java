package kr.openbase.adcsmart.service.impl.dto;

public class OBDtoAdcPoolSimple
{
	private String dbIndex;
	private Integer adcIndex;
	private String name;
	private String alteonId;
	private Integer flbMonitorOn; 

	@Override
	public String toString()
	{
		return "OBDtoAdcPoolSimple [dbIndex=" + dbIndex + ", adcIndex="
				+ adcIndex + ", name=" + name + ", alteonId=" + alteonId
				+ ", flbMonitorOn=" + flbMonitorOn + "]";
	}
	public String getDbIndex()
	{
		return dbIndex;
	}
	public void setDbIndex(String dbIndex)
	{
		this.dbIndex = dbIndex;
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getAlteonId()
	{
		return alteonId;
	}
	public void setAlteonId(String alteonId)
	{
		this.alteonId = alteonId;
	}
	public Integer getFlbMonitorOn()
	{
		return flbMonitorOn;
	}
	public void setFlbMonitorOn(Integer flbMonitorOn)
	{
		this.flbMonitorOn = flbMonitorOn;
	}
}
