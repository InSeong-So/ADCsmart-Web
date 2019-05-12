package kr.openbase.adcsmart.service.impl.pask.handler.dto;

public class OBDtoSystemInfoPASK
{
	private String 	productName;
	private String	serialNum;
	private String	version;
	private String	upTime;
	@Override
	public String toString()
	{
		return "OBDtoSystemInfoPASK [productName=" + productName + ", serialNum=" + serialNum + ", version=" + version + ", upTime=" + upTime + ", mngIPAddress=" + "]";
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
