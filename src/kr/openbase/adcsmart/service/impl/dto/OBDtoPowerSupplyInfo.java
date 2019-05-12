package kr.openbase.adcsmart.service.impl.dto;

import java.sql.Timestamp;

public class OBDtoPowerSupplyInfo
{
	private Integer adcIndex;
	private Timestamp occurTime;
	private Integer pan1Status=-1;
	private Integer pan2Status=-1;
	private Integer pan3Status=-1;
	private Integer pan4Status=-1;
	@Override
	public String toString()
	{
		return String.format("OBDtoPowerSupplyInfo [adcIndex=%s, occurTime=%s, pan1Status=%s, pan2Status=%s, pan3Status=%s, pan4Status=%s]", adcIndex, occurTime, pan1Status, pan2Status, pan3Status, pan4Status);
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public Timestamp getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Timestamp occurTime)
	{
		this.occurTime = occurTime;
	}
	public Integer getPan1Status()
	{
		return pan1Status;
	}
	public void setPan1Status(Integer pan1Status)
	{
		this.pan1Status = pan1Status;
	}
	public Integer getPan2Status()
	{
		return pan2Status;
	}
	public void setPan2Status(Integer pan2Status)
	{
		this.pan2Status = pan2Status;
	}
	public Integer getPan3Status()
	{
		return pan3Status;
	}
	public void setPan3Status(Integer pan3Status)
	{
		this.pan3Status = pan3Status;
	}
	public Integer getPan4Status()
	{
		return pan4Status;
	}
	public void setPan4Status(Integer pan4Status)
	{
		this.pan4Status = pan4Status;
	}
}
