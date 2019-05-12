package kr.openbase.adcsmart.web.facade.adcman;

import kr.openbase.adcsmart.service.OBAlarm;
import kr.openbase.adcsmart.service.OBAlert;
import kr.openbase.adcsmart.service.OBEnvManagement;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAlarmConfig;
import kr.openbase.adcsmart.service.dto.OBDtoAlertConfig;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvAdditional;
import kr.openbase.adcsmart.service.impl.OBAlarmImpl;
import kr.openbase.adcsmart.service.impl.OBAlertImpl;
import kr.openbase.adcsmart.service.impl.OBEnvManagementImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.AlarmConfigDto;
import kr.openbase.adcsmart.web.facade.dto.AlertSettingsDto;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AdcAlertFacade 
{
	private transient Logger log = LoggerFactory.getLogger(AdcAlertFacade.class);
	
	private OBAlarm alarm;
	private OBEnvManagement envManage;
	private OBAlert alertSvc;
	
	public AdcAlertFacade() 
	{
		envManage = new OBEnvManagementImpl();
		alarm = new OBAlarmImpl();
		alertSvc = new OBAlertImpl();
	}
	
	// alarm 설정 내역을 조회 (Global, Group, ADC)
	public AlarmConfigDto getAlarmConfigList(OBDtoADCObject object, Integer accountIndex) throws OBException , Exception
	{		
		{
			AlarmConfigDto alarmConfig = new AlarmConfigDto();
			OBDtoAlarmConfig alarmSvc = alarm.getAlarmConfiguration(object, accountIndex);
			log.debug("{}", alarmSvc);
			
			if (null != alarmSvc)
			{
				alarmConfig = toAlarmConfigDto(alarmSvc);		 
			}
			log.debug("{}", alarmConfig);
			return alarmConfig;
		} 	
	}
	
	//ADC의 alarm 설정을 Grobal 설정으로 변경
	public AlarmConfigDto changeAlarmConfigurationToGlobal(OBDtoADCObject object, Integer accountIndex) throws OBException , Exception
	{		
		{
			AlarmConfigDto alarmConfig = new AlarmConfigDto();
			OBDtoAlarmConfig alarmSvc = alarm.changeAlarmConfigurationToGlobal(object.getIndex());
			log.debug("{}", alarmSvc);
			
			if (null != alarmSvc)
			{
				alarmConfig = toAlarmConfigDto(alarmSvc);		 
			}
			log.debug("{}", alarmConfig);
			return alarmConfig;
		} 	
	}
	
	//ADC의 alarm 설정을 Group 설정으로 변경
	public AlarmConfigDto changeAlarmConfigurationToGroup(OBDtoADCObject object, Integer accountIndex) throws OBException , Exception
	{		
		{
			AlarmConfigDto alarmConfig = new AlarmConfigDto();
			OBDtoAlarmConfig alarmSvc = alarm.changeAlarmConfigurationToGroup(object.getIndex());
			log.debug("{}", alarmSvc);
			
			if (null != alarmSvc)
			{
				alarmConfig = toAlarmConfigDto(alarmSvc);		 
			}
			log.debug("{}", alarmConfig);
			return alarmConfig;
		} 	
	}
	
	private AlarmConfigDto toAlarmConfigDto(OBDtoAlarmConfig alarmSvc) throws OBException, Exception
	{
		AlarmConfigDto alarmConfig = new AlarmConfigDto(true);

		alarmConfig.setObject(alarmSvc.getObject());
		alarmConfig.setAdcType(alarmSvc.getAdcType());
		alarmConfig.setConfigLevel(alarmSvc.getConfigLevel());

		// 장애정보 Svc Get
		alarmConfig.getAdcDisconnectAction().setCustomAlarmAction(alarmSvc.getAdcDisconnect());
		alarmConfig.getAdcBootAction().setCustomAlarmAction(alarmSvc.getAdcBooting());
		alarmConfig.getAdcStandbyAction().setCustomAlarmAction(alarmSvc.getAdcActiveToStandby());
		alarmConfig.getAdcActiveAction().setCustomAlarmAction(alarmSvc.getAdcStandbyToActive());
		alarmConfig.getVirtualServerDownAction().setCustomAlarmAction(alarmSvc.getVirtualServerDown());
		alarmConfig.getPoolMemberDownAction().setCustomAlarmAction(alarmSvc.getPoolMemberDown());	
		alarmConfig.getVrrpCollisionAction().setCustomAlarmAction(alarmSvc.getVrrpCollision());
		alarmConfig.getGatewayDownAction().setCustomAlarmAction(alarmSvc.getGatewayFailDown());
		alarmConfig.getLinkDownAction().setCustomAlarmAction(alarmSvc.getInterfaceDown());
		
		// ADC 성능 정보 Svc Get
		alarmConfig.getAdcCpuAction().setCustomAlarmAction(alarmSvc.getAdcCpuLimit());
		alarmConfig.getAdcMPAction().setCustomAlarmAction(alarmSvc.getAdcMPLimit());
		alarmConfig.getAdcSPAction().setCustomAlarmAction(alarmSvc.getAdcSPLimit());
		alarmConfig.getAdcMemAction().setCustomAlarmAction(alarmSvc.getAdcMemLimit());
		alarmConfig.getConnectionLimitHighAction().setCustomAlarmAction(alarmSvc.getConnectionLimitHigh());
		alarmConfig.getConnectionLimitLowAction().setCustomAlarmAction(alarmSvc.getConnectionLimitLow());
		alarmConfig.getFilterSessionLimitHighAction().setCustomAlarmAction(alarmSvc.getFilterSessionLimitHigh());
		alarmConfig.getAdcSslTransAction().setCustomAlarmAction(alarmSvc.getAdcSslTransaction());
		
		alarmConfig.setAdcCpuValue(alarmSvc.getAdcCpuLimit().getThreshold().intValue());
		alarmConfig.setAdcMPValue(alarmSvc.getAdcMPLimit().getThreshold().intValue());
		alarmConfig.setAdcSPValue(alarmSvc.getAdcSPLimit().getThreshold().intValue());
		alarmConfig.setAdcMemValue(alarmSvc.getAdcMemLimit().getThreshold().intValue());		
		alarmConfig.setAdcConnHighValue(alarmSvc.getConnectionLimitHigh().getThreshold());
		alarmConfig.setAdcConnLowValue(alarmSvc.getConnectionLimitLow().getThreshold());
		alarmConfig.setFilterSessionHighValue(alarmSvc.getFilterSessionLimitHigh().getThreshold());
		alarmConfig.setAdcSslTransValue(alarmSvc.getAdcSslTransaction().getThreshold());
		
		// ADC 기간 정보 Svc Get
		alarmConfig.getAdcUptimeAction().setCustomAlarmAction(alarmSvc.getAdcUptime());
		alarmConfig.getAdcPurchaseAction().setCustomAlarmAction(alarmSvc.getAdcPurchase());
		alarmConfig.getAdcSslcertAction().setCustomAlarmAction(alarmSvc.getAdcSslcert());	
		
		alarmConfig.setAdcUptimeValue(alarmSvc.getAdcUptime().getThreshold().intValue());		
		alarmConfig.setAdcPurchaseValue(alarmSvc.getAdcPurchase().getThreshold().intValue());
		alarmConfig.setAdcSslcertValue(alarmSvc.getAdcSslcert().getThreshold().intValue());
		
		// ADC 시스템 정보 Svc Get
		alarmConfig.getInterfaceErrorAction().setCustomAlarmAction(alarmSvc.getInterfaceError());
		alarmConfig.getInterfaceUsageLimitAction().setCustomAlarmAction(alarmSvc.getInterfaceUsageLimit());
		alarmConfig.getInterfaceDuplexChangeAction().setCustomAlarmAction(alarmSvc.getInterfaceDuplexChange());
		alarmConfig.getInterfaceSpeedChangeAction().setCustomAlarmAction(alarmSvc.getInterfaceSpeedChange());
		alarmConfig.getAdcConfBackupFailureAction().setCustomAlarmAction(alarmSvc.getAdcConfBackupFailure());
		
		alarmConfig.setInterfaceErrorValue(alarmSvc.getInterfaceError().getThreshold().intValue());
		alarmConfig.setInterfaceUsageValue(alarmSvc.getInterfaceUsageLimit().getThreshold().intValue());
		
		// ADC 시스템 정보 Svc Get (Only Alteon Use)
		alarmConfig.getTemperatureTooHighAction().setCustomAlarmAction(alarmSvc.getTemperatureTooHigh());
		alarmConfig.getFanNotOperationalAction().setCustomAlarmAction(alarmSvc.getFanNotOperational());
		alarmConfig.getOnlyOnePowerSupplyAction().setCustomAlarmAction(alarmSvc.getOnlyOnePowerSupply());
		
		// ADC 시스템 정보 Svc Get (Only F5 Use)
		alarmConfig.getAdcConfSyncFailureAction().setCustomAlarmAction(alarmSvc.getAdcConfSyncFailure());
		alarmConfig.getCpuTempTooHighAction().setCustomAlarmAction(alarmSvc.getCpuTempTooHigh());
		alarmConfig.getCpuFanTooSlowAction().setCustomAlarmAction(alarmSvc.getCpuFanTooSlow());
		alarmConfig.getCpuFanBadAction().setCustomAlarmAction(alarmSvc.getCpuFanBad());
		alarmConfig.getChassisTempTooHighAction().setCustomAlarmAction(alarmSvc.getChassisTempTooHigh());
		alarmConfig.getChassisFanBadAction().setCustomAlarmAction(alarmSvc.getChassisFanBad());
		alarmConfig.getChassisPowerSupplyBadAction().setCustomAlarmAction(alarmSvc.getChassisPowerSupplyBad());
		alarmConfig.getVoltageTooHighAction().setCustomAlarmAction(alarmSvc.getVoltageTooHigh());
		alarmConfig.getChassisFanTooSlowAction().setCustomAlarmAction(alarmSvc.getChassisFanTooSlow());
		alarmConfig.getBlockDDoSAction().setCustomAlarmAction(alarmSvc.getBlockDDoS());
		
		alarmConfig.getResponseTimeAction().setCustomAlarmAction(alarmSvc.getResponseTime());
		alarmConfig.setResponseTimeValue(alarmSvc.getResponseTime().getThreshold().intValue());
		alarmConfig.getRedundancyCheckAction().setCustomAlarmAction(alarmSvc.getRedundancyCheck());
		
		return alarmConfig;
	}
	
	public void modifyConfigContent(AlarmConfigDto alarmConfigs, SessionDto session) throws OBException, Exception
	{	
		log.debug("modify : {}", alarmConfigs);
		// #3926-3 #6: 14.07.23 sw.jung 경보 감사로그 추가를 위해 extraInfo 전달
		alarm.setAlarmConfiguration(AlarmConfigDto.toOBDtoAlarmConfig(alarmConfigs), session.toOBDtoExtraInfo());
	}
	public void initAlarmConfiguration(OBDtoADCObject object) throws OBException, Exception
	{	
		log.debug("adcObject : {}", object);				
			alarm.initAlarmConfiguration(object);
	}
	//  환경설정 부가기능중 syslogIp Get
	public OBDtoSystemEnvAdditional getAdditionalConfigList() throws OBException, Exception
	{		
		OBDtoSystemEnvAdditional eAdditionalSvc = envManage.getSystemConfig().getAdditionalInfo();
		return eAdditionalSvc;
	}
	// 경보설정확인 AlertSettingData Get
	public AlertSettingsDto getSettings(Integer accountIndex) throws Exception 
	{		
		OBDtoAlertConfig settingsFromSvc = alertSvc.getAlertConfig(accountIndex);			
		AlertSettingsDto settings = AlertSettingsDto.toAlertSettingsDto(settingsFromSvc);
		log.debug("{}", settings);
		return settings;
	}
}