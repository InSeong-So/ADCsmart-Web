package kr.openbase.adcsmart.service.impl.dto;

public class OBDtoVrrpIndexInfo
{
	private Integer vrrp;
	private Integer vrid;
	@Override
	public String toString()
	{
		return "OBDtoVrrpIndexInfo [vrrp=" + vrrp + ", vrid=" + vrid + "]";
	}
	public Integer getVrrp()
	{
		return vrrp;
	}
	public void setVrrp(Integer vrrp)
	{
		this.vrrp = vrrp;
	}
	public Integer getVrid()
	{
		return vrid;
	}
	public void setVrid(Integer vrid)
	{
		this.vrid = vrid;
	}
}
