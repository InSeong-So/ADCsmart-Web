package kr.openbase.adcsmart.service.dto;

public class OBDtoRSInfo
{
	private String rsIndex;
	private String rsName;
	private String rsIPAddress;
	
	@Override
	public String toString()
	{
		return "OBDtoRSInfo [rsIndex=" + rsIndex + ", rsName=" + rsName + ", rsIPAddress=" + rsIPAddress + "]";
	}

	public String getRsIndex()
	{
		return rsIndex;
	}

	public void setRsIndex(String rsIndex)
	{
		this.rsIndex = rsIndex;
	}

	public String getRsName()
	{
		return rsName;
	}

	public void setRsName(String rsName)
	{
		this.rsName = rsName;
	}

	public String getRsIPAddress()
	{
		return rsIPAddress;
	}

	public void setRsIPAddress(String rsIPAddress)
	{
		this.rsIPAddress = rsIPAddress;
	}
	
	

}
