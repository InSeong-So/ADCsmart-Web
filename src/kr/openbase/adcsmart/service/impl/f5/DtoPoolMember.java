package kr.openbase.adcsmart.service.impl.f5;

import iControl.LocalLBPoolMemberMemberRatio;

public class DtoPoolMember 
//public class OBDtoAdcPoolMemberF5
{
	private String poolName;
	private String ipAddress;
	private Integer port;
	private Integer state;
	private Integer status;
	private Integer ratio;

	
	public String getPoolName()
	{
		return poolName;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public void setPoolName(String poolName)
	{
		this.poolName = poolName;
	}
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	public String getIpAddress()
	{
		return this.ipAddress;
	}
	
	public void setPort(Integer port)
	{
		this.port = port;
	}
	public Integer getPort()
	{
		return this.port;
	}
	public void setState(Integer state)
	{
		this.state = state;
	}
	public Integer getState()
	{
		return this.state;
	}	
	public Integer getRatio()
    {
        return ratio;
    }
    public void setRatio(Integer ratio)
    {
        this.ratio = ratio;
    }
    @Override
	public String toString() {
		return "DtoPoolMember [poolName=" + poolName + ", ipAddress="
				+ ipAddress + ", port=" + port + ", state=" + state
				+ ", status=" + status + ", ratio=" + ratio + "]";
	}
	public void setRatio(
			LocalLBPoolMemberMemberRatio[] localLBPoolMemberMemberRatios) {
		// TODO Auto-generated method stub
		
	}
	
}
