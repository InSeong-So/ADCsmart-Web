package kr.openbase.adcsmart.service.dto;

public class OBDtoAdcVServiceChanged
{
	private Integer servicePort;
	private Integer realPort;
	private boolean isRealPortChanged;
	private OBDtoAdcPoolAlteonChanged pool;
	private boolean isPoolChanged;
	@Override
	public String toString()
	{
		return "OBDtoAdcVServiceChanged [servicePort=" + servicePort
				+ ", realPort=" + realPort + ", isRealPortChanged="
				+ isRealPortChanged + ", pool=" + pool + ", isPoolChanged="
				+ isPoolChanged + "]";
	}
	public boolean isPoolChanged()
	{
		return isPoolChanged;
	}
	public void setPoolChanged(boolean isPoolChanged)
	{
		this.isPoolChanged = isPoolChanged;
	}
	public OBDtoAdcPoolAlteonChanged getPool()
	{
		return pool;
	}
	public void setPool(OBDtoAdcPoolAlteonChanged pool)
	{
		this.pool = pool;
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
	public boolean isRealPortChanged()
	{
		return isRealPortChanged;
	}
	public void setRealPortChanged(boolean isRealPortChanged)
	{
		this.isRealPortChanged = isRealPortChanged;
	}
}
