package kr.openbase.adcsmart.service.impl.dto;

public class OBDtoAdcVlan
{
	String vlanName;

	public String getVlanName()
	{
		return vlanName;
	}

	public void setVlanName(String vlanName)
	{
		this.vlanName = vlanName;
	}

	@Override
	public String toString()
	{
		return "OBDtoAdcVlan [vlanName=" + vlanName + "]";
	}
}
