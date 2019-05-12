package kr.openbase.adcsmart.service.impl.alteon.handler.dto;

public class OBDtoSnmpCfgAlteon
{
	private	String 	sysName;
	private	String 	readCommunity;
	private	String	writeCommunity;
	private	boolean isEnabled;
	@Override
	public String toString()
	{
		return "OBDtoSnmpCfgAlteon [sysName=" + sysName + ", readCommunity=" + readCommunity + ", writeCommunity=" + writeCommunity + ", isEnabled=" + isEnabled + "]";
	}
	public String getSysName()
	{
		return sysName;
	}
	public void setSysName(String sysName)
	{
		this.sysName = sysName;
	}
	public String getReadCommunity()
	{
		return readCommunity;
	}
	public void setReadCommunity(String readCommunity)
	{
		this.readCommunity = readCommunity;
	}
	public String getWriteCommunity()
	{
		return writeCommunity;
	}
	public void setWriteCommunity(String writeCommunity)
	{
		this.writeCommunity = writeCommunity;
	}
	public boolean isEnabled()
	{
		return isEnabled;
	}
	public void setEnabled(boolean isEnabled)
	{
		this.isEnabled = isEnabled;
	}	
}
