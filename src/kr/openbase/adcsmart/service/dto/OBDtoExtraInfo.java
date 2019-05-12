package kr.openbase.adcsmart.service.dto;

public class OBDtoExtraInfo
{
	private Integer accountIndex;
	private String clientIPAddress;
	private String extraMsg1;
	private String extraMsg2;
	@Override
	public String toString()
	{
		return "OBDtoExtraInfo [accountIndex=" + accountIndex
				+ ", clientIPAddress=" + clientIPAddress + ", extraMsg1="
				+ extraMsg1 + ", extraMsg2=" + extraMsg2 + "]";
	}
	public Integer getAccountIndex()
	{
		return accountIndex;
	}
	public void setAccountIndex(Integer accountIndex)
	{
		this.accountIndex = accountIndex;
	}
	public String getClientIPAddress()
	{
		return clientIPAddress;
	}
	public void setClientIPAddress(String clientIPAddress)
	{
		this.clientIPAddress = clientIPAddress;
	}
	public String getExtraMsg1()
	{
		return extraMsg1;
	}
	public void setExtraMsg1(String extraMsg1)
	{
		this.extraMsg1 = extraMsg1;
	}
	public String getExtraMsg2()
	{
		return extraMsg2;
	}
	public void setExtraMsg2(String extraMsg2)
	{
		this.extraMsg2 = extraMsg2;
	}
}
