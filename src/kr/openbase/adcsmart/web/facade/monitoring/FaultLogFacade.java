/**
 * 
 */
package kr.openbase.adcsmart.web.facade.monitoring;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBMonitoring;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSystemLog;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.impl.OBMonitoringImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.AdcSystemLogDto;

/**
 * @author lucky77th
 *
 */
@Component
public class FaultLogFacade 
{

	private static transient Logger log = LoggerFactory.getLogger(FaultLogFacade.class);
	
	private OBMonitoring monitor;
	
	public FaultLogFacade() 
	{
		monitor = new OBMonitoringImpl();
	}
	
	public int getAdcSystemFaultLogCount(OBDtoADCObject adcObject, OBDtoSearch searchOption, Integer accountIndex) throws Exception 
	{
		try 
		{
			return monitor.getAdcSystemFaultLogCount(adcObject, searchOption, accountIndex);
		}
		catch (OBException e) 
		{
			throw e;
		}
	}
		
	public List<AdcSystemLogDto> getAdcSystemFaultLog(OBDtoADCObject adcObject, OBDtoSearch searchOption, OBDtoOrdering orderOption, Integer accountIndex) throws Exception 
	{
		List<AdcSystemLogDto> adcSystemLogList = new ArrayList<AdcSystemLogDto>();
		
		List<OBDtoAdcSystemLog> svcAdcSystemLogList = monitor.getAdcSystemFaultLog(adcObject, searchOption, orderOption, accountIndex);
		log.debug("{}", svcAdcSystemLogList);
		if (!CollectionUtils.isEmpty(svcAdcSystemLogList)) 
		{
			for (OBDtoAdcSystemLog svcAdcSystemLog : svcAdcSystemLogList) 
			{
				adcSystemLogList.add(getSystemLog(svcAdcSystemLog));
			}
		}		
		return adcSystemLogList;
	}
	
	public List<AdcSystemLogDto> getAdcSystemFaultLogToExport(OBDtoADCObject adcObject, OBDtoSearch searchOption, Integer accountIndex) throws Exception 
	{
		List<AdcSystemLogDto> adcSystemLogList = new ArrayList<AdcSystemLogDto>();
		
		List<OBDtoAdcSystemLog> svcAdcSystemLogList = monitor.getAdcSystemFaultLogExOrdering(adcObject, searchOption, accountIndex);
		log.debug("{}", svcAdcSystemLogList);
		if (!CollectionUtils.isEmpty(svcAdcSystemLogList)) 
		{
			for (OBDtoAdcSystemLog svcAdcSystemLog : svcAdcSystemLogList) 
			{
				adcSystemLogList.add(getSystemLog(svcAdcSystemLog));
			}
		}		
		return adcSystemLogList;
	}
	
	private AdcSystemLogDto getSystemLog(OBDtoAdcSystemLog svcAdcSystemLog) 
	{
		AdcSystemLogDto adcSystemLog = new AdcSystemLogDto();
		if (null != svcAdcSystemLog.getOccurTime())
		{
			adcSystemLog.setOccurredTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(svcAdcSystemLog.getOccurTime()));			
		}
		adcSystemLog.setLogType(svcAdcSystemLog.getLogType());
		adcSystemLog.setLogLevel(svcAdcSystemLog.getLogLevel());
		adcSystemLog.setAdcIndex(svcAdcSystemLog.getAdcIndex());
		adcSystemLog.setAdcName(svcAdcSystemLog.getAdcName());
		adcSystemLog.setVsIndex(svcAdcSystemLog.getVsIndex());
		adcSystemLog.setEvent(svcAdcSystemLog.getEvent());
		adcSystemLog.setStatus(svcAdcSystemLog.getStatus());
		
		if (null != svcAdcSystemLog.getFinishTime()) 
		{
			adcSystemLog.setFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(svcAdcSystemLog.getFinishTime()));
		}
		return adcSystemLog;
	}	
}