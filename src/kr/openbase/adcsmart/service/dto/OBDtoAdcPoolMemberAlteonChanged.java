package kr.openbase.adcsmart.service.dto;

public class OBDtoAdcPoolMemberAlteonChanged
{
	private String alteonNodeID;
	private String ipAddress;// ip 주소가 입력된다.
	private boolean isIpAddressChanged;
	private Integer state;
	private boolean isStateChanged;
	@Override
	public String toString()
	{
		return "OBDtoAdcPoolMemberAlteonChanged [alteonNodeID=" + alteonNodeID
				+ ", ipAddress=" + ipAddress + ", isIpAddressChanged="
				+ isIpAddressChanged + ", state=" + state + ", isStateChanged="
				+ isStateChanged + "]";
	}
	public String getAlteonNodeID()
	{
		return alteonNodeID;
	}
	public void setAlteonNodeID(String alteonNodeID)
	{
		this.alteonNodeID = alteonNodeID;
	}
	public String getIpAddress()
	{
		return ipAddress;
	}
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	public boolean isIpAddressChanged()
	{
		return isIpAddressChanged;
	}
	public void setIpAddressChanged(boolean isIpAddressChanged)
	{
		this.isIpAddressChanged = isIpAddressChanged;
	}
	public Integer getState()
	{
		return state;
	}
	public void setState(Integer state)
	{
		this.state = state;
	}
	public boolean isStateChanged()
	{
		return isStateChanged;
	}
	public void setStateChanged(boolean isStateChanged)
	{
		this.isStateChanged = isStateChanged;
	}
}
