package kr.openbase.adcsmart.web.facade.dashboard;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBDashboardAdc;
import kr.openbase.adcsmart.service.OBIntegratedDashboard;
import kr.openbase.adcsmart.service.dashboard.OBIntegratedDashboardImpl;
import kr.openbase.adcsmart.service.dashboard.dto.OBDtoIntegrated;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardFaultStatus;
import kr.openbase.adcsmart.service.impl.OBDashboardAdcImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.adcmon.OBDtoAdcmonFaultStatus;

@Component
public class DashBoardServiceFacade {
	private static transient Logger log = LoggerFactory.getLogger(DashBoardServiceFacade.class);

	private OBIntegratedDashboard dashboardServiceSvc;
	private OBDashboardAdc adcMonitoringDashboard; // ADC모니터링 Dashboard 인터페이스

	public DashBoardServiceFacade() {
		dashboardServiceSvc = new OBIntegratedDashboardImpl();
		adcMonitoringDashboard = new OBDashboardAdcImpl();
	}

	public OBDtoIntegrated getDashboardServiceData() throws Exception {
		OBDtoIntegrated dashboardSvcData = new OBDtoIntegrated();
		dashboardSvcData = dashboardServiceSvc.getIntegratedDashboard();

		log.debug("dashboardSvcData : {}", dashboardSvcData);
		return dashboardSvcData;
	}

	// 2,3. 장애 모니터링 현황(1주일)
	public OBDtoAdcmonFaultStatus getFaultMonitoring(Integer accountIndex, Date fromTime, Date toTime)
			throws OBException, Exception {
		OBDtoDashboardFaultStatus item = adcMonitoringDashboard.getDynamicFaultMonitoring(accountIndex, fromTime,
				toTime); // 기존 데시보드와 다르게 5개까지 지원
		return new OBDtoAdcmonFaultStatus().toAdcmonFaultStatus(item);
	}
}
