package kr.openbase.adcsmart.web.controller.adcman;

import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvAdditional;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.adcman.AdcAlertFacade;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.AlarmActionDto;
import kr.openbase.adcsmart.web.facade.dto.AlarmConfigDto;
import kr.openbase.adcsmart.web.facade.dto.AlertSettingsDto;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope(value = "prototype")
public class AdcAlertAction extends BaseAction 
{
	private transient Logger log = LoggerFactory.getLogger(AdcAlertAction.class);
	
	@Autowired
	private AdcAlertFacade adcAlertFacade;
	
	private AdcDto adc;
	private AlarmConfigDto alarmConfig;
	private AlarmConfigDto alarmConfigs;
	private List<AlarmActionDto> alarmAction;
	private OBDtoSystemEnvAdditional envAdditional; 	// 환경설정의 부가기능 sysLog Ip
	private OBDtoADCObject adcObject;
	private AlertSettingsDto alertSetting;
	
	public String loadListContent() throws OBException 
	{
		return SUCCESS;
	}

	public String loadAdcAlertListContent() throws OBException 
	{
		try 
		{			
			log.debug("{}", adc);
			SessionDto sessionData = session.getSessionDto();
			envAdditional = adcAlertFacade.getAdditionalConfigList();
			alertSetting = adcAlertFacade.getSettings(session.getAccountIndex());
			if (adc.getType().equals("F5")) 
			{
				alarmConfig = adcAlertFacade.getAlarmConfigList(adcObject, sessionData.getAccountIndex());
				log.debug("{}", alarmConfig);				
				return "F5";
			} 
			else if (adc.getType().equals("Alteon"))
			{
				alarmConfig = adcAlertFacade.getAlarmConfigList(adcObject, sessionData.getAccountIndex());
				log.debug("{}", alarmConfig);				
				return "ALTEON";
			}
			else if (adc.getType().equals("PAS"))
			{
				alarmConfig = adcAlertFacade.getAlarmConfigList(adcObject, sessionData.getAccountIndex());
				log.debug("{}", alarmConfig);			
				return "PAS";
			}
			else
			{
				alarmConfig = adcAlertFacade.getAlarmConfigList(adcObject, sessionData.getAccountIndex());
				log.debug("{}", alarmConfig);				
				return "PASK";
			}
		}
		catch (OBException e) 
        {
            throw e;
        }
        catch (Exception e) 
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
	}
	public String loadAdcAlertListToGlobalContent() throws OBException 
	{
		try 
		{
			log.debug("{}", adc);
			SessionDto sessionData = session.getSessionDto();
			envAdditional = adcAlertFacade.getAdditionalConfigList();
			alertSetting = adcAlertFacade.getSettings(session.getAccountIndex());
			if (adc.getType().equals("F5")) 
			{
				alarmConfig = adcAlertFacade.changeAlarmConfigurationToGlobal(adcObject, sessionData.getAccountIndex());
				log.debug("{}", alarmConfig);
				return "F5";
			} 
			else if (adc.getType().equals("Alteon"))
			{
				alarmConfig = adcAlertFacade.changeAlarmConfigurationToGlobal(adcObject, sessionData.getAccountIndex());
				log.debug("{}", alarmConfig);
				return "ALTEON";
			}
			else if (adc.getType().equals("PAS"))
			{
				alarmConfig = adcAlertFacade.changeAlarmConfigurationToGlobal(adcObject, sessionData.getAccountIndex());
				log.debug("{}", alarmConfig);
				return "PAS";
			}
			else
			{
				alarmConfig = adcAlertFacade.changeAlarmConfigurationToGlobal(adcObject, sessionData.getAccountIndex());
				log.debug("{}", alarmConfig);
				return "PASK";
			}
		}
		catch (OBException e) 
        {
            throw e;
        }
        catch (Exception e) 
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
	}
	public String loadAdcAlertListToGroupContent() throws OBException 
	{
		try 
		{			
			log.debug("{}", adc);
			SessionDto sessionData = session.getSessionDto();
			envAdditional = adcAlertFacade.getAdditionalConfigList();
			alertSetting = adcAlertFacade.getSettings(session.getAccountIndex());
			if (adc.getType().equals("F5")) 
			{
				alarmConfig = adcAlertFacade.changeAlarmConfigurationToGroup(adcObject, sessionData.getAccountIndex());
				log.debug("{}", alarmConfig);
				return "F5";
			} 
			else if (adc.getType().equals("Alteon"))
			{
				alarmConfig = adcAlertFacade.changeAlarmConfigurationToGroup(adcObject, sessionData.getAccountIndex());
				log.debug("{}", alarmConfig);
				return "ALTEON";
			}
			else if (adc.getType().equals("PAS"))
			{
				alarmConfig = adcAlertFacade.changeAlarmConfigurationToGroup(adcObject, sessionData.getAccountIndex());
				log.debug("{}", alarmConfig);
				return "PAS";
			}
			else
			{
				alarmConfig = adcAlertFacade.changeAlarmConfigurationToGroup(adcObject, sessionData.getAccountIndex());
				log.debug("{}", alarmConfig);
				return "PASK";
			}
		}
		catch (OBException e) 
        {
            throw e;
        }
        catch (Exception e) 
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
	}
	// Global, Group List Get
	public String loadGroupAlertListContent() throws OBException 
	{
		try 
		{			
			log.debug("{}", adcObject);
			SessionDto sessionData = session.getSessionDto();
			alarmConfig = adcAlertFacade.getAlarmConfigList(adcObject, sessionData.getAccountIndex());
			envAdditional = adcAlertFacade.getAdditionalConfigList();
			alertSetting = adcAlertFacade.getSettings(session.getAccountIndex());
			log.debug("{}", alarmConfig);
					
		}
		catch (OBException e) 
        {
            throw e;
        }
        catch (Exception e) 
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
		return SUCCESS;
	}
	public String loadAlertListTargetChangeContent() throws OBException 
	{
		try 
		{
			log.debug("{}", adcObject);
			SessionDto sessionData = session.getSessionDto();
			envAdditional = adcAlertFacade.getAdditionalConfigList();
			alertSetting = adcAlertFacade.getSettings(session.getAccountIndex());
			if (adcObject.getVendor() == OBDefine.ADC_TYPE_F5) 
			{
				alarmConfig = adcAlertFacade.getAlarmConfigList(adcObject, sessionData.getAccountIndex());
				log.debug("{}", alarmConfig);
				return "F5";
			} 
			else if (adcObject.getVendor() == OBDefine.ADC_TYPE_ALTEON)
			{
				alarmConfig = adcAlertFacade.getAlarmConfigList(adcObject, sessionData.getAccountIndex());
				log.debug("{}", alarmConfig);
				return "ALTEON";
			}
			else if (adcObject.getVendor() == OBDefine.ADC_TYPE_PIOLINK_PAS)
			{
				alarmConfig = adcAlertFacade.getAlarmConfigList(adcObject, sessionData.getAccountIndex());
				log.debug("{}", alarmConfig);
				return "PAS";
			}
			else
			{
				alarmConfig = adcAlertFacade.getAlarmConfigList(adcObject, sessionData.getAccountIndex());
				log.debug("{}", alarmConfig);
				return "PASK";
			}
		}
		catch (OBException e) 
        {
            throw e;
        }
        catch (Exception e) 
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
	}
	public String modifyAdcAlertContent() throws OBException
	{		
		isSuccessful = true;		
		try 
		{				
			log.debug("modifyAlarm: {}", alarmConfigs);	
			adcAlertFacade.modifyConfigContent(alarmConfigs, session.getSessionDto());			
			log.debug("modifyAlarmAfter: {}", alarmConfigs);	
		} 
		catch (OBException e) 
        {
            throw e;
        }
        catch (Exception e) 
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
			
		return SUCCESS;	
	}
	
	public String loadInitAdcAlertConfiguration() throws OBException
	{		
		isSuccessful = true;		
		try 
		{
			adcAlertFacade.initAlarmConfiguration(adcObject);
			log.debug("modifyAlarmAfter: {}", adcObject);			
		} 
		catch (OBException e) 
        {
            throw e;
        }
        catch (Exception e) 
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
			
		return SUCCESS;	
	}

	public AdcDto getAdc()
	{
		return adc;
	}

	public void setAdc(AdcDto adc)
	{
		this.adc = adc;
	}
	
	public AlarmConfigDto getAlarmConfig()
	{
		return alarmConfig;
	}

	public void setAlarmConfig(AlarmConfigDto alarmConfig)
	{
		this.alarmConfig = alarmConfig;
	}

	public List<AlarmActionDto> getAlarmAction()
	{
		return alarmAction;
	}

	public void setAlarmAction(List<AlarmActionDto> alarmAction)
	{
		this.alarmAction = alarmAction;
	}

	public AlarmConfigDto getAlarmConfigs()
	{
		return alarmConfigs;
	}

	public void setAlarmConfigs(AlarmConfigDto alarmConfigs)
	{
		this.alarmConfigs = alarmConfigs;
	}

	public OBDtoADCObject getAdcObject()
	{
		return adcObject;
	}

	public void setAdcObject(OBDtoADCObject adcObject)
	{
		this.adcObject = adcObject;
	}

	public OBDtoSystemEnvAdditional getEnvAdditional()
	{
		return envAdditional;
	}

	public void setEnvAdditional(OBDtoSystemEnvAdditional envAdditional)
	{
		this.envAdditional = envAdditional;
	}

	public AlertSettingsDto getAlertSetting()
	{
		return alertSetting;
	}

	public void setAlertSetting(AlertSettingsDto alertSetting)
	{
		this.alertSetting = alertSetting;
	}	
}