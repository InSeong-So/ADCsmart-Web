package kr.openbase.adcsmart.service.impl.fault.dto;

public class OBDtoFaultCheckThreadInfo
{
	private Integer adcIndex;
	private Long checkKey;
	private String clientIP;
	private Integer accntIndex;
	
	@Override
	public String toString()
	{
		return String.format("OBDtoFaultCheckThreadInfo [adcIndex=%s, checkKey=%s, clientIP=%s, accntIndex=%s]", adcIndex, checkKey, clientIP, accntIndex);
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public Long getCheckKey()
	{
		return checkKey;
	}
	public void setCheckKey(Long checkKey)
	{
		this.checkKey = checkKey;
	}
	public String getClientIP()
	{
		return clientIP;
	}
	public void setClientIP(String clientIP)
	{
		this.clientIP = clientIP;
	}
	public Integer getAccntIndex()
	{
		return accntIndex;
	}
	public void setAccntIndex(Integer accntIndex)
	{
		this.accntIndex = accntIndex;
	}
}
