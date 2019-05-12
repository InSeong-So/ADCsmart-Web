package kr.openbase.adcsmart.service.impl.pas.handler.dto;

public class OBDtoSystemInfoPAS
{
	private String 	productName;
	private String	serialNum;
	private String	version;
	private String	upTime;
	@Override
	public String toString()
	{
		return "OBDtoSystemInfoPAS [productName=" + productName
				+ ", serialNum=" + serialNum + ", version=" + version
				+ ", upTime=" + upTime + "]";
	}
	public String getProductName()
	{
		return productName;
	}
	public void setProductName(String productName)
	{
		this.productName = productName;
	}
	public String getSerialNum()
	{
		return serialNum;
	}
	public void setSerialNum(String serialNum)
	{
		this.serialNum = serialNum;
	}
	public String getVersion()
	{
		return version;
	}
	public void setVersion(String version)
	{
		this.version = version;
	}
	public String getUpTime()
	{
		return upTime;
	}
	public void setUpTime(String upTime)
	{
		this.upTime = upTime;
	}
}
