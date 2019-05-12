package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;

import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigInfoAlteon;

public class AdcSettingAlteonDto {
	private Date setTime;
	private VirtualSvrAlteonDto virtualSvr;
	private String changeSummary;
	
	public static AdcSettingAlteonDto toAdcSettingAlteonDto(OBDtoAdcConfigInfoAlteon adcSettingFromSvc) {
		if (adcSettingFromSvc == null)
			return null;
		
		AdcSettingAlteonDto adcSetting = new AdcSettingAlteonDto();
		adcSetting.virtualSvr = toVirtualSvrAlteonDto(adcSettingFromSvc);
		adcSetting.setTime = adcSettingFromSvc.getLastTime();
		adcSetting.changeSummary = adcSettingFromSvc.getSummary();
		return adcSetting;
	}

	private static VirtualSvrAlteonDto toVirtualSvrAlteonDto(OBDtoAdcConfigInfoAlteon adcSettingFromSvc) {
		if (adcSettingFromSvc == null)
			return null;
		
		VirtualSvrAlteonDto virtualSvr = new VirtualSvrAlteonDto();
		virtualSvr.setAlteonId(adcSettingFromSvc.getAlteonId());
		virtualSvr.setName(adcSettingFromSvc.getVsName());
		virtualSvr.setVirtualIp(adcSettingFromSvc.getVsIPAddress());
		virtualSvr.setVrrpState(adcSettingFromSvc.getVrrpState());
		virtualSvr.setIntefaceNo(adcSettingFromSvc.getIfNum());
		virtualSvr.setState(adcSettingFromSvc.getState());
		virtualSvr.setVirtualSvcs(VirtualSvcDto.toVirtualSvcDto(adcSettingFromSvc.getVserviceList()));
		return virtualSvr;
	}

	public Date getSetTime() {
		return setTime;
	}

	public void setSetTime(Date setTime) {
		this.setTime = setTime;
	}

	public VirtualSvrAlteonDto getVirtualSvr() {
		return virtualSvr;
	}

	public void setVirtualSvr(VirtualSvrAlteonDto virtualSvr) {
		this.virtualSvr = virtualSvr;
	}

	public String getChangeSummary() {
		return changeSummary;
	}

	public void setChangeSummary(String changeSummary) {
		this.changeSummary = changeSummary;
	}

	@Override
	public String toString() {
		return "AdcSettingAlteonDto [setTime=" + setTime + ", virtualSvr=" + virtualSvr + ", changeSummary="
				+ changeSummary + "]";
	}
	
}
