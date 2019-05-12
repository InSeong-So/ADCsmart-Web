package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;

import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigInfoPAS;

public class AdcSettingPASDto
{
	private Date setTime;
	private VirtualSvrPASDto virtualSvr;
	private String changeSummary;
	
	public static AdcSettingPASDto toAdcSettingPASDto(OBDtoAdcConfigInfoPAS adcSettingFromSvc) {
		if (adcSettingFromSvc == null)
			return null;
		
		AdcSettingPASDto adcSetting = new AdcSettingPASDto();
		adcSetting.virtualSvr = toVirtualSvrPASDto(adcSettingFromSvc);
		adcSetting.setTime = adcSettingFromSvc.getLastTime();
		adcSetting.changeSummary = adcSettingFromSvc.getSummary();
		
		return adcSetting;
	}

	private static VirtualSvrPASDto toVirtualSvrPASDto(OBDtoAdcConfigInfoPAS adcSettingFromSvc) {
		if (adcSettingFromSvc == null)
			return null;
		
		VirtualSvrPASDto virtualSvr = new VirtualSvrPASDto();
		virtualSvr.setName(adcSettingFromSvc.getVsName());
		virtualSvr.setVirtualIp(adcSettingFromSvc.getVsIPAddress());
		virtualSvr.setServicePort(adcSettingFromSvc.getVsSrvPort());
		virtualSvr.setPool(AdcPoolDto.toAdcPoolDto(adcSettingFromSvc.getPool()));
		return virtualSvr;
	}

	public Date getSetTime()
	{
		return setTime;
	}

	public void setSetTime(Date setTime)
	{
		this.setTime = setTime;
	}

	public VirtualSvrPASDto getVirtualSvr()
	{
		return virtualSvr;
	}

	public void setVirtualSvr(VirtualSvrPASDto virtualSvr)
	{
		this.virtualSvr = virtualSvr;
	}

	public String getChangeSummary()
	{
		return changeSummary;
	}

	public void setChangeSummary(String changeSummary)
	{
		this.changeSummary = changeSummary;
	}

	@Override
	public String toString()
	{
		return "AdcSettingPASDto [setTime=" + setTime + ", virtualSvr="
				+ virtualSvr + ", changeSummary=" + changeSummary + "]";
	}

}
