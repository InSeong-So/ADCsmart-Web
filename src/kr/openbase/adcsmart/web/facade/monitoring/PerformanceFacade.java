/**
 * 
 */
package kr.openbase.adcsmart.web.facade.monitoring;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.OBMonitoring;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPerformance;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSysRescStatus;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSystemResources;
import kr.openbase.adcsmart.service.impl.OBMonitoringImpl;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.PerformanceDto;
import kr.openbase.adcsmart.web.facade.dto.SystemStatusDto;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author paul
 *
 */
@Component
public class PerformanceFacade
{
	
	private static transient Logger log = LoggerFactory.getLogger(PerformanceFacade.class);

	private OBMonitoring monitoringSvc;
	
	public PerformanceFacade() 
	{
		monitoringSvc = new OBMonitoringImpl();
	}
	
	public PerformanceDto getPerformance(AdcDto adc, Date startTime, Date endTime) throws Exception 
	{
		PerformanceDto performance = null;
		
		log.debug("adcIndex:{}, startTime:{}, endTime:{}", new Object[]{adc.getIndex()
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime)
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime)});
		
		OBDtoAdcPerformance adcPerformance = monitoringSvc.getAdcPerformance(adc.getIndex(), startTime, endTime);
		log.debug("{}", adcPerformance);
		
		if (null != adcPerformance)
		{
			performance = PerformanceDto.getPerformance(adcPerformance);
			log.debug("{}", performance);
		}
		
		return performance;
	}
	
	public List<SystemStatusDto> getSystemStatus(AdcDto adc, Date startTime, Date endTime) throws Exception 
	{
		List<SystemStatusDto> systemStatusList = new ArrayList<SystemStatusDto>();
		
		OBDtoAdcSysRescStatus adcSysRescStatus = monitoringSvc.getAdcSysRescStatus(adc.getIndex(), startTime, endTime);
		if (null != adcSysRescStatus)
		{
			ArrayList<OBDtoAdcSystemResources> adcSystemResourceStatusList = adcSysRescStatus.getRescList();
			log.debug("{}", adcSystemResourceStatusList);
			
			if (!CollectionUtils.isEmpty(adcSystemResourceStatusList))
			{
				for (OBDtoAdcSystemResources systemResourceStatus : adcSystemResourceStatusList)
				{
					systemStatusList.add(getSystemStatus(systemResourceStatus));
				}
			}
		}
		
		return systemStatusList;
	}
	
	private SystemStatusDto getSystemStatus(OBDtoAdcSystemResources systemResourceStatus)
	{
		SystemStatusDto systemStatusDto = new SystemStatusDto();
		
		systemStatusDto.setOccurredTime(systemResourceStatus.getOccurTime());
		systemStatusDto.setCpuUsage(systemResourceStatus.getCpu1Usage());
		systemStatusDto.setMemoryUsage(systemResourceStatus.getMemUsage());
		systemStatusDto.setSp1Usage(systemResourceStatus.getCpu2Usage());
		systemStatusDto.setSp2Usage(systemResourceStatus.getCpu3Usage());
		systemStatusDto.setSp3Usage(systemResourceStatus.getCpu4Usage());
		systemStatusDto.setSp4Usage(systemResourceStatus.getCpu5Usage());
				
		return systemStatusDto;
	}
	
}
