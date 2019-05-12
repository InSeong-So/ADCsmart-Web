package kr.openbase.adcsmart.web.controller.dashboard;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import kr.openbase.adcsmart.service.dashboard.dto.OBDtoIntegrated;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.dashboard.DashBoardServiceFacade;
import kr.openbase.adcsmart.web.facade.dto.adcmon.OBDtoAdcmonFaultStatus;
import kr.openbase.adcsmart.web.util.OBDateTimeWeb;

@Controller
@Scope(value = "prototype")
public class DashBoardServiceAction extends BaseAction
{
    private transient Logger log = LoggerFactory.getLogger(DashBoardServiceAction.class);
    
    private OBDtoIntegrated dashboardServiceData; 
    
    private Date startTime;
	private Date endTime;
    private Integer	accountIndex;
    private OBDtoAdcmonFaultStatus dashFaultStatus;						// 장애 모니터링 최근 장애 내역
    
    @Autowired
    private DashBoardServiceFacade dashboardServiceFacade;
        
    // 화면 로드
    public String loadDashHeader() throws OBException
    {
        try
        {
            dashboardServiceData = dashboardServiceFacade.getDashboardServiceData();            
            log.debug("dashboardServiceData.adcs : {}", dashboardServiceData.getAdcs());
            loadFaultMonitoringList();
        }
        catch(Exception e)
        {
            throw new OBException(e.getMessage());
        }
        
        return SUCCESS;
    }
    
    // 2.장애 모니터링 현황(1주일) Chart & 3. 장애 모니터링 현황 최근장애내역
 	public String loadFaultMonitoringList() throws OBException 
 	{
 		try 
 		{
 			accountIndex = session.getAccountIndex();
 			startTime = OBDateTimeWeb.toDate(OBDateTimeWeb.toTimestamp(OBDateTimeWeb.getDateWithDayOffset(-6, null)));
 			endTime = OBDateTimeWeb.toDate(OBDateTimeWeb.toTimestamp(OBDateTimeWeb.now()));

 			dashFaultStatus = dashboardServiceFacade.getFaultMonitoring(accountIndex, startTime, endTime);					

 			log.debug("{}", dashFaultStatus);			
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
    public String loadDashContent() throws OBException
    {        
        return SUCCESS;
    }
   
    public OBDtoIntegrated getDashboardServiceData()
    {
        return dashboardServiceData;
    }

    public void setDashboardServiceData(OBDtoIntegrated dashboardServiceData)
    {
        this.dashboardServiceData = dashboardServiceData;
    }
    
    public OBDtoAdcmonFaultStatus getDashFaultStatus()
	{
		return dashFaultStatus;
	}

	public void setDashFaultStatus(OBDtoAdcmonFaultStatus dashFaultStatus)
	{
		this.dashFaultStatus = dashFaultStatus;
	}
	
    @Override
    public String toString()
    {
        return "DashBoardServiceAction "
        		+ "["
        		+ "dashboardServiceData=" + dashboardServiceData 
        		+ ", dashboardServiceFacade=" + dashboardServiceFacade
        		+ ", dashFaultStatus=" + dashFaultStatus 
        		+"]";
    }
}
