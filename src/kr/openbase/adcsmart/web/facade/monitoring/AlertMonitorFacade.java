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
import kr.openbase.adcsmart.service.dto.OBDtoAdcAlertLog;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.impl.OBMonitoringImpl;
import kr.openbase.adcsmart.web.facade.dto.AdcAlertLogDto;

@Component
public class AlertMonitorFacade {

	private static transient Logger log = LoggerFactory.getLogger(AlertMonitorFacade.class);

	private OBMonitoring monitor;

	public AlertMonitorFacade() {
		monitor = new OBMonitoringImpl();
	}

	// List Get
	public List<AdcAlertLogDto> getAlertLog(OBDtoADCObject adcObject, OBDtoSearch searchOption,
			OBDtoOrdering orderOption, Integer accountIndex) throws Exception {
		List<AdcAlertLogDto> adcAlertLogList = new ArrayList<AdcAlertLogDto>();

		List<OBDtoAdcAlertLog> svcAdcAlertLogList = monitor.getAlertLog(adcObject, searchOption, orderOption,
				accountIndex);
		log.debug("{}", svcAdcAlertLogList);
		if (!CollectionUtils.isEmpty(svcAdcAlertLogList)) {
			for (OBDtoAdcAlertLog svcAdcAlertLog : svcAdcAlertLogList) {
				adcAlertLogList.add(getAlertLog(svcAdcAlertLog));
			}
		}
		return adcAlertLogList;
	}

	private AdcAlertLogDto getAlertLog(OBDtoAdcAlertLog svcAdcAlertLog) {
		AdcAlertLogDto adcAlertLog = new AdcAlertLogDto();
		if (null != svcAdcAlertLog.getOccurTime()) {
			adcAlertLog.setOccurTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(svcAdcAlertLog.getOccurTime()));
		} else {
		}
		if (null != svcAdcAlertLog.getAlertTime()) {
			adcAlertLog.setAlertTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(svcAdcAlertLog.getAlertTime()));
		} else {
		}
		adcAlertLog.setAdcIndex(svcAdcAlertLog.getAdcIndex());
		adcAlertLog.setAdcName(svcAdcAlertLog.getAdcName());
		adcAlertLog.setType(svcAdcAlertLog.getType());
		adcAlertLog.setStatus(svcAdcAlertLog.getStatus());
		adcAlertLog.setEvent(svcAdcAlertLog.getEvent());
		adcAlertLog.setActionSyslog(svcAdcAlertLog.getActionSyslog());
		adcAlertLog.setActionSnmptrap(svcAdcAlertLog.getActionSnmptrap());
		adcAlertLog.setActionSMS(svcAdcAlertLog.getActionSMS());
		adcAlertLog.setIsNew(svcAdcAlertLog.isNew());
		return adcAlertLog;
	}

	// Total Count Get
	public int getAlertLogCount(OBDtoADCObject adcObject, OBDtoSearch searchOption, Integer accountIndex)
			throws Exception {
		return monitor.getAlertLogCount(adcObject, searchOption, accountIndex);
	}

}