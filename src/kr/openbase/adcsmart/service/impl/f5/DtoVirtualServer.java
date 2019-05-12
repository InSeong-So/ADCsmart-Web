package kr.openbase.adcsmart.service.impl.f5;

import java.sql.Timestamp;

public class DtoVirtualServer
{
	private Integer index;
	private Integer adcIndex;
	private Timestamp applyTime;
	private Integer status;
	private String name;
	private String vIP;
	private Integer servicePort;
	private String persistenceName;
	private DtoVlanTunnelFilter vlanTunnel;
	private Integer useYN;
	private Integer deleteYN;
	
	private String poolName;

	public DtoVirtualServer()
    {
    }

    @Override
	public String toString()
	{
		return "DtoVirtualServer [index=" + index + ", adcIndex=" + adcIndex + ", applyTime=" + applyTime + ", status=" + status + ", name=" + name + ", vIP=" + vIP + ", servicePort=" + servicePort + ", persistenceName=" + persistenceName + ", vlanTunnel=" + vlanTunnel + ", useYN=" + useYN + ", poolName=" + poolName + "]";
	}
	public Integer getIndex()
	{
		return index;
	}
	public void setIndex(Integer index)
	{
		this.index = index;
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public Timestamp getApplyTime()
	{
		return applyTime;
	}
	public void setApplyTime(Timestamp applyTime)
	{
		this.applyTime = applyTime;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getvIP()
	{
		return vIP;
	}
	void setvIP(String vIP)
	{
		this.vIP = vIP;
	}
	public Integer getServicePort()
	{
		return servicePort;
	}
	public void setServicePort(Integer servicePort)
	{
		this.servicePort = servicePort;
	}
	public String getPersistenceName()
	{
		return persistenceName;
	}
	public void setPersistenceName(String persistenceName)
	{
		this.persistenceName = persistenceName;
	}
	public Integer getUseYN()
	{
		return useYN;
	}
	public void setUseYN(Integer useYN)
	{
		this.useYN = useYN;
	}
	public String getPoolName()
	{
		return poolName;
	}
	public void setPoolName(String poolName)
	{
		this.poolName = poolName;
	}
	public DtoVlanTunnelFilter getVlanTunnel()
	{
		return vlanTunnel;
	}
	public void setVlanTunnel(DtoVlanTunnelFilter vlanTunnel)
	{
		this.vlanTunnel = vlanTunnel;
	}

    public Integer getDeleteYN()
    {
        return deleteYN;
    }

    public void setDeleteYN(Integer deleteYN)
    {
        this.deleteYN = deleteYN;
    }
}