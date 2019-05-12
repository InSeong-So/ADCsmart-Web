package kr.openbase.adcsmart.service.impl.dto;

public class OBDtoStatsSlbMaint
{
	private Long	allocFails=0L;
	private Long    vipPktDrops=0L;
	@Override
	public String toString()
	{
		return "OBDtoStatsSlbMaint [allocFails=" + allocFails + ", vipPktDrops=" + vipPktDrops + "]";
	}
	public Long getAllocFails()
	{
		return allocFails;
	}
	public void setAllocFails(Long allocFails)
	{
		this.allocFails = allocFails;
	}
	public Long getVipPktDrops()
	{
		return vipPktDrops;
	}
	public void setVipPktDrops(Long vipPktDrops)
	{
		this.vipPktDrops = vipPktDrops;
	}
}
