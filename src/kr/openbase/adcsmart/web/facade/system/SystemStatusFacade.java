/**
 * 
 */
package kr.openbase.adcsmart.web.facade.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.OBMonitoring;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSysRescStatus;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSystemResources;
import kr.openbase.adcsmart.service.impl.OBMonitoringImpl;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
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
public class SystemStatusFacade {

	private static transient Logger log = LoggerFactory.getLogger(SystemStatusFacade.class);
	
	private OBMonitoring monitoringSvc;
	
	public SystemStatusFacade() {
		monitoringSvc = new OBMonitoringImpl();
	}
	
	public List<SystemStatusDto> getSystemStatus(AdcDto adc, Date startTime, Date endTime)
			throws Exception {
		List<SystemStatusDto> systemStatusList = new ArrayList<SystemStatusDto>();
		
		OBDtoAdcSysRescStatus adcSysRescStatus = monitoringSvc.getAdcSysRescStatus(
				adc.getIndex(), startTime, endTime);
		if (null != adcSysRescStatus) {
			ArrayList<OBDtoAdcSystemResources> adcSystemResourceStatusList = adcSysRescStatus.getRescList();
			log.debug("{}", adcSystemResourceStatusList);
			if (!CollectionUtils.isEmpty(adcSystemResourceStatusList)) {
				for (OBDtoAdcSystemResources systemResourceStatus : adcSystemResourceStatusList) {
					systemStatusList.add(getSystemStatus(systemResourceStatus));
				}
			}
		}
		
		return systemStatusList;
	}
	
	private SystemStatusDto getSystemStatus(OBDtoAdcSystemResources systemResourceStatus) {
		SystemStatusDto systemStatusDto = new SystemStatusDto();
		systemStatusDto.setOccurredTime(systemResourceStatus.getOccurTime());
		systemStatusDto.setCpuUsage(systemResourceStatus.getCpu1Usage());
		systemStatusDto.setMemoryUsage(systemResourceStatus.getMemUsage());
		return systemStatusDto;
	}
	
}
