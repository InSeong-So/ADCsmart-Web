package kr.openbase.adcsmart.service.impl.alteon.handler.dto;

public class OBDtoInfoVlanAlteon
{
	private	int	vlanId;
	private	String	name;
	private	int		status;
	private	String  ports;
	@Override
	public String toString()
	{
		return "OBDtoInfoVlanAlteon [vlanId=" + vlanId + ", name=" + name + ", status=" + status + ", ports=" + ports + "]";
	}
	public int getVlanId()
	{
		return vlanId;
	}
	public void setVlanId(int vlanId)
	{
		this.vlanId = vlanId;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public String getPorts()
	{
		return ports;
	}
	public void setPorts(String ports)
	{
		this.ports = ports;
	}
}
