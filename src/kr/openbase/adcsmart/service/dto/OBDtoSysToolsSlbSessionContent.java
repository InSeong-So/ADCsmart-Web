package kr.openbase.adcsmart.service.dto;


public class OBDtoSysToolsSlbSessionContent
{
	private String adcIp;
	private String adcType;
	private String accountId;
	private String password;
	private String ipType;
	private String ip;
	private String content;
	public String getAdcIp()
	{
		return adcIp;
	}
	public void setAdcIp(String adcIp)
	{
		this.adcIp = adcIp;
	}
	public String getAdcType()
	{
		return adcType;
	}
	public void setAdcType(String adcType)
	{
		this.adcType = adcType;
	}
	public String getAccountId()
	{
		return accountId;
	}
	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getIpType()
	{
		return ipType;
	}
	public void setIpType(String ipType)
	{
		this.ipType = ipType;
	}
	public String getIp()
	{
		return ip;
	}
	public void setIp(String ip)
	{
		this.ip = ip;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	@Override
	public String toString()
	{
		return "OBDtoSysToolsSlbSessionContent [adcIp=" + adcIp + ", adcType="
				+ adcType + ", accountId=" + accountId + ", password="
				+ password + ", ipType=" + ipType + ", ip=" + ip + ", content="
				+ content + "]";
	}
	
	
}
