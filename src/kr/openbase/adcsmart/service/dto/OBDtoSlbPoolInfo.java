package kr.openbase.adcsmart.service.dto;

public class OBDtoSlbPoolInfo
{
	private String 	index;
	private Integer lbMethod;
	private Integer healthCheckMethod;
	private String 	name;
	@Override
	public String toString()
	{
		return String.format("OBDtoSlbPoolInfo [index=%s, lbMethod=%s, healthCheckMethod=%s, name=%s]", index, lbMethod, healthCheckMethod, name);
	}
	public Integer getLbMethod()
	{
		return lbMethod;
	}
	public void setLbMethod(Integer lbMethod)
	{
		this.lbMethod = lbMethod;
	}
	public Integer getHealthCheckMethod()
	{
		return healthCheckMethod;
	}
	public void setHealthCheckMethod(Integer healthCheckMethod)
	{
		this.healthCheckMethod = healthCheckMethod;
	}
	public String getIndex()
	{
		return index;
	}
	public void setIndex(String index)
	{
		this.index = index;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
}
