package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidRptOPL7
{
	private String vsIPAddr;
	private String iRuleName;
	private String vsiRuleName;
	private String oneConnect;
	private String ramcache;
	private String compression;
	private String sslAccel;
	private String vsProfileName;
	@Override
	public String toString()
	{
		return "DtoOidRptOPL7 [vsIPAddr=" + vsIPAddr + ", iRuleName="
				+ iRuleName + ", vsiRuleName=" + vsiRuleName + ", oneConnect="
				+ oneConnect + ", ramcache=" + ramcache + ", compression="
				+ compression + ", sslAccel=" + sslAccel + ", vsProfileName="
				+ vsProfileName + "]";
	}
	public String getVsIPAddr()
	{
		return vsIPAddr;
	}
	public void setVsIPAddr(String vsIPAddr)
	{
		this.vsIPAddr = vsIPAddr;
	}
	public String getVsProfileName()
	{
		return vsProfileName;
	}
	public void setVsProfileName(String vsProfileName)
	{
		this.vsProfileName = vsProfileName;
	}
	public String getiRuleName()
	{
		return iRuleName;
	}
	public void setiRuleName(String iRuleName)
	{
		this.iRuleName = iRuleName;
	}
	public String getVsiRuleName()
	{
		return vsiRuleName;
	}
	public void setVsiRuleName(String vsiRuleName)
	{
		this.vsiRuleName = vsiRuleName;
	}
	public String getOneConnect()
	{
		return oneConnect;
	}
	public void setOneConnect(String oneConnect)
	{
		this.oneConnect = oneConnect;
	}
	public String getRamcache()
	{
		return ramcache;
	}
	public void setRamcache(String ramcache)
	{
		this.ramcache = ramcache;
	}
	public String getCompression()
	{
		return compression;
	}
	public void setCompression(String compression)
	{
		this.compression = compression;
	}
	public String getSslAccel()
	{
		return sslAccel;
	}
	public void setSslAccel(String sslAccel)
	{
		this.sslAccel = sslAccel;
	}	
}
