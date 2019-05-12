package kr.openbase.adcsmart.web.facade.dto;

import kr.openbase.adcsmart.service.dto.OBDtoAlertConfig;
import kr.openbase.adcsmart.web.util.OBDefineWeb;

public class AlertSettingsDto {
	private Integer alertType;
	private Boolean isBeepOn = false;
	
	public static AlertSettingsDto toAlertSettingsDto(OBDtoAlertConfig settingsFromSvc) {
		AlertSettingsDto settings = new AlertSettingsDto();
		if (settingsFromSvc == null)
			return settings;
		
		settings.setAlertType(settingsFromSvc.getAlertType());
		settings.setIsBeepOn(settingsFromSvc.getAlertSound() == OBDefineWeb.ALERT_SOUND_ON ? true : false);
		return settings;
	}	
	public Integer getAlertType()
	{
		return alertType;
	}
	
	public void setAlertType(Integer alertType)
	{
		this.alertType = alertType;
	}
	
	public Boolean getIsBeepOn()
	{
		return isBeepOn;
	}
	
	public void setIsBeepOn(Boolean isBeepOn)
	{
		this.isBeepOn = isBeepOn;
	}
	@Override
	public String toString()
	{
		return "AlertSettingsDto [alertType=" + alertType + ", isBeepOn=" + isBeepOn + "]";
	}

}