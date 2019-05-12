package kr.openbase.adcsmart.web.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBAlert;
import kr.openbase.adcsmart.service.dto.OBDtoAlert;
import kr.openbase.adcsmart.service.dto.OBDtoAlertConfig;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.impl.OBAlertImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.AlertSettingsDto;

@Component
public class AlertFacade 
{
	private static transient Logger log = LoggerFactory.getLogger(AlertFacade.class);
	
	private OBAlert alertSvc;

	public AlertFacade() 
	{
		alertSvc = new OBAlertImpl();
	}
	// 경보설정확인 AlertSettingData Get
	public AlertSettingsDto getSettings(Integer accountIndex) throws Exception 
	{
		try 
		{
			OBDtoAlertConfig settingsFromSvc = alertSvc.getAlertConfig(accountIndex);
			log.debug("{}", settingsFromSvc);
			AlertSettingsDto settings = AlertSettingsDto.toAlertSettingsDto(settingsFromSvc);
			log.debug("{}", settings);
			
			return settings;
		} 
		catch (OBException e) 
		{			
			throw e;
		}
	}
	// Ticker 정보 Get
	public OBDtoAlert getAlertTicker(Integer accountIndex) throws Exception 
	{
		try 
		{
			OBDtoAlert tickerData = alertSvc.getAlertTicker(accountIndex);
			log.debug("{}", tickerData);
			
			return tickerData;
		} 
		catch (OBException e) 
		{			
			throw e;
		}
	}
	// Popup 정보 Get
	public OBDtoAlert getAlertPopup(Integer accountIndex, Integer type, int alertCount, OBDtoOrdering ordering) throws Exception 
	{
		try 
		{			
			OBDtoAlert alertData = alertSvc.getAlert(accountIndex, type, alertCount, ordering);
			log.debug("{}", alertData);			
			return alertData;
		} 
		catch (OBException e) 
		{			
			throw e;
		}
	}
	// UserAlertTime 갱신
	public void updateUserAlertTime(Integer accountIndex) throws Exception 
	{
		try 
		{			
			alertSvc.updateUserAlertTime(accountIndex);
		} 
		catch (OBException e) 
		{			
			throw e;
		}
	}
}