package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidCfgPool
{
	private String poolID;
	private String poolName;
	private String poolLBMethod;
	private String poolMemberID;
	private String poolHealthCheck;
	private String poolMemberState;
	private String poolRealBackup;
	private String poolBackup;
	
	
	@Override
	public String toString()
	{
		return "DtoOidCfgPool [poolID=" + poolID + ", poolName=" + poolName + ", poolLBMethod=" + poolLBMethod + 
				", poolMemberID=" + poolMemberID + ", poolHealthCheck=" + poolHealthCheck + ", poolMemberState=" + 
				poolMemberState + ", poolRealBackup=" + poolRealBackup + ", poolBackup=" + poolBackup + "]";
	}
	public String getPoolHealthCheck()
	{
		return poolHealthCheck;
	}
	public String getPoolMemberState()
	{
		return poolMemberState;
	}
	public void setPoolMemberState(String poolMemberState)
	{
		this.poolMemberState = poolMemberState;
	}
	public void setPoolHealthCheck(String poolHealthCheck)
	{
		this.poolHealthCheck = poolHealthCheck;
	}
	public String getPoolID()
	{
		return poolID;
	}
	public void setPoolID(String poolID)
	{
		this.poolID = poolID;
	}
	public String getPoolName()
	{
		return poolName;
	}
	public void setPoolName(String poolName)
	{
		this.poolName = poolName;
	}
	public String getPoolLBMethod()
	{
		return poolLBMethod;
	}
	public void setPoolLBMethod(String poolLBMethod)
	{
		this.poolLBMethod = poolLBMethod;
	}
	public String getPoolMemberID()
	{
		return poolMemberID;
	}
	public void setPoolMemberID(String poolMemberID)
	{
		this.poolMemberID = poolMemberID;
	}
	public String getPoolRealBackup()
	{
		return poolRealBackup;
	}
	public void setPoolRealBackup(String poolRealBackup)
	{
		this.poolRealBackup = poolRealBackup;
	}
	public String getPoolBackup()
	{
		return poolBackup;
	}
	public void setPoolBackup(String poolBackup)
	{
		this.poolBackup = poolBackup;
	}
}
