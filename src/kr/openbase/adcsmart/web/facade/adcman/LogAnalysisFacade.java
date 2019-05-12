package kr.openbase.adcsmart.web.facade.adcman;

import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.OBAdcManagement;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcLogSearchOption;
import kr.openbase.adcsmart.service.dto.OBDtoAuditLogAdcConfig;
import kr.openbase.adcsmart.service.dto.OBDtoAuditLogAdcSystem;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.AdcLogDto;
import kr.openbase.adcsmart.web.facade.dto.AuditLogDto;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogAnalysisFacade 
{
	private static transient Logger log = LoggerFactory.getLogger(LogAnalysisFacade.class);
	
	private OBAdcManagement adcSvc;
	
	public LogAnalysisFacade() 
	{
		adcSvc = new OBAdcManagementImpl();
	}
	// ADC Log List TotalCount Get
	public int getAdcLogTotal(OBDtoADCObject adcObject, OBDtoSearch searchOption, OBDtoAdcLogSearchOption selectOption, Integer accountIndex) throws OBException, Exception 
	{	
		//throw new OBException(OBException.ERRCODE_ADC_SYSLOGCNT);
		return adcSvc.getAdcAuditLogCount(adcObject, searchOption, selectOption, accountIndex);		
	}	
	// ADC Log List Get
	public List<AdcLogDto> selectAdcLogs(OBDtoADCObject adcObject, OBDtoSearch searchOption, OBDtoOrdering orderOption, Integer extraContentKey, OBDtoAdcLogSearchOption selectOption, Integer accountIndex) throws OBException, Exception
	{
		List<OBDtoAuditLogAdcSystem> systemLogsFromSvc = adcSvc.getAdcAuditLog(adcObject, searchOption, orderOption, selectOption, accountIndex);
		log.debug("{}", systemLogsFromSvc);
		List<AdcLogDto> adcLogs = AdcLogDto.toAdcLogDto(systemLogsFromSvc, extraContentKey);
		return adcLogs;
	}
	// ADC Log Export Data Get
	public List<AdcLogDto> selectAdcLogsToExport(OBDtoADCObject adcObject, OBDtoSearch searchOption, Integer extraContentKey, OBDtoAdcLogSearchOption selectOption, Integer accountIndex) throws OBException, Exception
	{
		List<OBDtoAuditLogAdcSystem> systemLogsFromSvc = adcSvc.getAdcAuditLogExOrdering(adcObject, searchOption, selectOption, accountIndex);
		log.debug("{}", systemLogsFromSvc);
		List<AdcLogDto> adcLogs = AdcLogDto.toAdcLogDto(systemLogsFromSvc, extraContentKey);
		return adcLogs;
	}
	
	// 감사로그 TotalCount Get
	public int getAuditLogTotal(String searchKey , Date fromPeriod, Date toPeriod) throws OBException, Exception 
	{	
		return adcSvc.getSystemAuditLogCount(StringUtils.isEmpty(searchKey) ? null : searchKey, fromPeriod, toPeriod);		
	}
	// 감사로그 List Get
	public List<AuditLogDto> selectAuditLogs(String searchKey , Date fromPeriod, Date toPeriod, Integer fromRow, Integer toRow,  SessionDto session, Integer orderType, Integer orderDir) throws OBException, Exception 
	{
		List<OBDtoAuditLogAdcConfig> configLogsFromSvc = adcSvc.getSystemAuditLog(StringUtils.isEmpty(searchKey) ? null : searchKey, fromPeriod, toPeriod,  fromRow, toRow, orderType, orderDir);
		log.debug("{}", configLogsFromSvc);
		List<AuditLogDto> auditLogs = AuditLogDto.fromOBDtoAuditLogAdcConfig(configLogsFromSvc);
		return auditLogs;
	}
	// 감사로그 Export Data Get
	public List<AuditLogDto> selectAuditLogs(String searchKey , Date fromPeriod, Date toPeriod, Integer fromRow, Integer toRow,  SessionDto session) throws OBException, Exception 
	{
		List<OBDtoAuditLogAdcConfig> configLogsFromSvc = adcSvc.getSystemAuditLog(StringUtils.isEmpty(searchKey) ? null : searchKey, fromPeriod, toPeriod,  fromRow, toRow);
		log.debug("{}", configLogsFromSvc);
		List<AuditLogDto> auditLogs = AuditLogDto.fromOBDtoAuditLogAdcConfig(configLogsFromSvc);
		return auditLogs;
	}	
}