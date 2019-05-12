package kr.openbase.adcsmart.service.dto;

import java.io.Serializable;

public class OBDtoAdcVService implements Serializable
{
	private static final long serialVersionUID = 10L;
	private String alteonName;
	private String serviceIndex;
	private Integer servicePort;
	private Integer realPort;
	private OBDtoAdcPoolAlteon pool;
	private Integer status;
	
	public OBDtoAdcVService()
	{
	}
	public String getServiceIndex()
	{
		return serviceIndex;
	}
	public void setServiceIndex(String serviceIndex)
	{
		this.serviceIndex = serviceIndex;
	}
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	public OBDtoAdcVService(OBDtoAdcVService obj)
	{
		if(obj!=null)
		{
			this.serviceIndex = obj.getServiceIndex();
			this.servicePort = obj.getServicePort();
			this.realPort = obj.getRealPort();
			this.status = obj.getStatus();
			if(obj.getPool()!=null)
			{
				this.pool = new OBDtoAdcPoolAlteon(obj.getPool());
			}
			else
			{
				this.pool = null;
			}
		}
	}
	
	@Override
	public String toString()
	{
		return "OBDtoAdcVService [serviceIndex=" + serviceIndex
				+ ", servicePort=" + servicePort + ", realPort=" + realPort
				+ ", pool=" + pool + ", status=" + status + "]";
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public Integer getServicePort()
	{
		return servicePort;
	}
	public void setServicePort(Integer servicePort)
	{
		this.servicePort = servicePort;
	}
	public Integer getRealPort()
	{
		return realPort;
	}
	public void setRealPort(Integer realPort)
	{
		this.realPort = realPort;
	}
	public OBDtoAdcPoolAlteon getPool()
	{
		return pool;
	}
	public void setPool(OBDtoAdcPoolAlteon pool)
	{
		this.pool = pool;
	}
	public String getAlteonName()
	{
		return alteonName;
	}
	public void setAlteonName(String alteonName)
	{
		this.alteonName = alteonName;
	}
}
