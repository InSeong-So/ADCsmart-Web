package kr.openbase.adcsmart.service.impl.fault.dto;

public class OBDtoFaultCheckItemCountInfo
{
	private Integer hwCheckTotal=0;
	private Integer svcCheckTotal=0;
	public Integer getHwCheckTotal()
	{
		return hwCheckTotal;
	}
	public void setHwCheckTotal(Integer hwCheckTotal)
	{
		this.hwCheckTotal = hwCheckTotal;
	}
	public Integer getSvcCheckTotal()
	{
		return svcCheckTotal;
	}
	public void setSvcCheckTotal(Integer svcCheckTotal)
	{
		this.svcCheckTotal = svcCheckTotal;
	}
}
