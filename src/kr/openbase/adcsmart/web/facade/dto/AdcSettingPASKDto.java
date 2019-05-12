package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;

import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigInfoPASK;

public class AdcSettingPASKDto
{
	private Date setTime;
	private VirtualSvrPASKDto virtualSvr;
	private String changeSummary;
	private String vipInfo;
	
	public static AdcSettingPASKDto toAdcSettingPASKDto(OBDtoAdcConfigInfoPASK adcSettingFromSvc)
	{
		if (adcSettingFromSvc == null)
			return null;
		
		AdcSettingPASKDto adcSetting = new AdcSettingPASKDto();
		adcSetting.virtualSvr = toVirtualSvrPASKDto(adcSettingFromSvc);
		adcSetting.setTime = adcSettingFromSvc.getLastTime();
		adcSetting.changeSummary = adcSettingFromSvc.getSummary();
		adcSetting.vipInfo = adcSettingFromSvc.getSubInfo();
		
		return adcSetting;
	}
	
	private static VirtualSvrPASKDto toVirtualSvrPASKDto(OBDtoAdcConfigInfoPASK adcSettingFromSvc)
	{
		if (adcSettingFromSvc == null)
			return null;
		
		VirtualSvrPASKDto virtualSvr = new VirtualSvrPASKDto();
		virtualSvr.setName(adcSettingFromSvc.getVsName());
		virtualSvr.setVirtualIp(adcSettingFromSvc.getVsIPAddress());
		virtualSvr.setServicePort(adcSettingFromSvc.getVsSrvPort());
		virtualSvr.setPool(AdcPoolDto.toAdcPoolDto(adcSettingFromSvc.getPool()));
		return virtualSvr;
	}
	public String getVipInfo()
	{
		return vipInfo;
	}

	public void setVipInfo(String vipInfo)
	{
		this.vipInfo = vipInfo;
	}

	public Date getSetTime()
	{
		return setTime;
	}

	public void setSetTime(Date setTime)
	{
		this.setTime = setTime;
	}

	public VirtualSvrPASKDto getVirtualSvr()
	{
		return virtualSvr;
	}

	public void setVirtualSvr(VirtualSvrPASKDto virtualSvr)
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
		return "AdcSettingPASKDto [setTime=" + setTime + ", virtualSvr=" + virtualSvr + ", changeSummary=" + changeSummary + ", vipInfo=" + vipInfo + "]";
	}
	

}
