package kr.openbase.adcsmart.web.controller.monitoring;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcScope;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoServiceMonitoringChart;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvAdditional;
import kr.openbase.adcsmart.service.impl.OBEnvManagementImpl;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalAdc;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalAdcCondition;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalAdcOne;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalConditionUnit;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalFilterUnit;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalGroup;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalGroupCondition;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalGroupOne;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalReal;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalRealCondition;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalRealOne;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalService;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalServiceCondition;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalServiceOne;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.dashboard.DashboardFacade;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;
import kr.openbase.adcsmart.web.facade.monitoring.GroupTotalMonitorFacade;
import kr.openbase.adcsmart.web.util.CsvMaker;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

@Controller
@Scope(value = "prototype")
public class GroupTotalMonitorAction extends BaseAction
{
	private transient Logger log = LoggerFactory.getLogger(GroupTotalMonitorAction.class);
	
	@Autowired
	private GroupTotalMonitorFacade groupTotalMonitorFacade;
	@Autowired
	private DashboardFacade dashboardFacade;
	
	private Integer rowTotal;
	private String searchKey;
	private Integer fromRow;
	private Integer toRow;
	private Integer orderDir; // 오른차순 = 1, 내림차순 = 2
	private Integer orderType; //
	//ADC 모니터링
	private OBDtoMonTotalAdc montotalAdc;
	private ArrayList<OBDtoMonTotalAdcOne> montotalAdcList;
	private OBDtoMonTotalAdcCondition adcCondition;
	//서비스 모니터링
	private OBDtoMonTotalService montotalSvc;
	private ArrayList<OBDtoMonTotalServiceOne> montotalSvcList;
	private OBDtoMonTotalServiceCondition svcCondition;
	//Group 모니터링
	private OBDtoMonTotalGroup montotalGrp;
	private ArrayList<OBDtoMonTotalGroupOne> montotalGrpList;
	private OBDtoMonTotalGroupCondition grpCondition;
	//RealSever 모니터링
	private OBDtoMonTotalReal montotalRs;
	private ArrayList<OBDtoMonTotalRealOne> montotalRsList;
	private OBDtoMonTotalRealCondition rsCondition;
	
	private OBDtoAdcScope adcScope;
	private String selectedVal;
	private String selectedCol;
	private OBDtoADCObject adcObject;
//	private ArrayList<OBDtoBpsConnData> multiBpsConnInfo;
//	private ArrayList<OBDtoBpsConnData> multiConcurrentInfo;
//	private OBDtoBpsConnData multiBpsConnAvgMaxData;
	
	private OBDtoServiceMonitoringChart bpsConnInfoData;
	private OBDtoServiceMonitoringChart bpsConnCurAvgMaxData;
	private Date startTime;
	private Date endTime;
	private Integer hour;
	private ArrayList<String> vsIndexList;
	private Integer monitoringPeriod;
    private Integer intervalMonitor;
	public GroupTotalMonitorAction() 
	{
	    vsIndexList = new ArrayList<String>();
	    OBDtoSystemEnvAdditional env = null;
        int interval = 0;
        try
        {
            env = new OBEnvManagementImpl().getAdditionalConfig();
            interval = env.getIntervalAdcConfSync();
        }
        catch(OBException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        monitoringPeriod = (int) (interval * 2.5);
	}
	public String retrieveAdcMonTotal() throws Exception
	{
		isSuccessful = true;
		try 
		{
			SessionDto sessionData = session.getSessionDto();
			
			if(selectedVal != null || selectedCol != null)
			{							
				rowTotal = groupTotalMonitorFacade.getTotalAdcListCount(adcScope, sessionData.getAccountIndex(), adcConditionSet(selectedCol, selectedVal));
				log.debug("rowTotal {}", rowTotal);
			}
			else
			{
				if(searchKey != null)
				{					
					OBDtoMonTotalAdcCondition searchCondition = new OBDtoMonTotalAdcCondition();
					searchCondition.setSearchKeyword(searchKey);
					rowTotal = groupTotalMonitorFacade.getTotalAdcListCount(adcScope, sessionData.getAccountIndex(), searchCondition);
				}
				else
				{					
					rowTotal = groupTotalMonitorFacade.getTotalAdcListCount(adcScope, sessionData.getAccountIndex(), adcCondition);
				}
				log.debug("rowTotal {}", rowTotal);
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
		
		return SUCCESS;
	}
	
	public String loadApplianceMonitorList() throws OBException
	{		
		try 
		{
			SessionDto sessionData = session.getSessionDto();
					
			// filter 값 전달
			if (selectedVal!=null || selectedCol!=null)
			{
				montotalAdc = groupTotalMonitorFacade.getTotalAdcList(adcScope, sessionData.getAccountIndex(),  adcConditionSet(selectedCol, selectedVal), fromRow, toRow, orderType, orderDir);				
				log.debug("montotalAdc: montotalAdc{}", montotalAdc);
			}
			else
			{		
				if(searchKey != null)
				{
					OBDtoMonTotalAdcCondition searchCondition = new OBDtoMonTotalAdcCondition();
					searchCondition.setSearchKeyword(searchKey);
					montotalAdc = groupTotalMonitorFacade.getTotalAdcList(adcScope, sessionData.getAccountIndex(), searchCondition, fromRow, toRow, orderType, orderDir);
				}
				else
				{
					montotalAdc = groupTotalMonitorFacade.getTotalAdcList(adcScope, sessionData.getAccountIndex(), adcCondition, fromRow, toRow, orderType, orderDir);
				}
				log.debug("montotalAdc: montotalAdc{}", montotalAdc);
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
		
		return SUCCESS;
	}
	
	public String checkExportAdcMonDataExist() throws OBException
	{
		try 
		{
			SessionDto sessionData = session.getSessionDto();
			
			OBDtoSearch searchOption = new OBDtoSearch();
			searchOption.setBeginIndex(null);
			searchOption.setEndIndex(OBDefine.MAX_EXPORT_COUNT);
			
			// filter 값 전달
			if (selectedVal!=null || selectedCol!=null)
			{				
				montotalAdc = groupTotalMonitorFacade.getTotalAdcListToExport(adcScope, sessionData.getAccountIndex(), adcConditionSet(selectedCol, selectedVal), searchOption.getBeginIndex(), searchOption.getEndIndex());
				log.debug("montotalAdc: montotalAdc{}", montotalAdc);
			}
			else
			{		
				if(searchKey != null)
				{
					OBDtoMonTotalAdcCondition searchCondition = new OBDtoMonTotalAdcCondition();
					searchCondition.setSearchKeyword(searchKey);
					montotalAdc = groupTotalMonitorFacade.getTotalAdcListToExport(adcScope, sessionData.getAccountIndex(), searchCondition, fromRow, toRow);
				}
				else
				{
					montotalAdc = groupTotalMonitorFacade.getTotalAdcListToExport(adcScope, sessionData.getAccountIndex(), adcCondition, fromRow, toRow);
				}
				log.debug("montotalAdc: montotalAdc{}", montotalAdc);
			}
			
			
			if (montotalAdc.getAdcList() != null &&montotalAdc.getAdcList().size()>0)
			{
				isSuccessful = true;
				montotalAdc = null;
			}
			else
			{
				isSuccessful = false;
				montotalAdc = null;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_EXPORT_DATA_NOT_EXIST);
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
		
		return SUCCESS;
	}
	
	public String downloadAdcMonList() throws OBException
	{
		try 
		{			
			SessionDto sessionData = session.getSessionDto();
						
			OBDtoSearch searchOption = new OBDtoSearch();
			searchOption.setBeginIndex(null);
			searchOption.setEndIndex(OBDefine.MAX_EXPORT_COUNT);
			
			// filter 값 전달
			if (selectedVal!=null || selectedCol!=null)
			{	
				montotalAdc = groupTotalMonitorFacade.getTotalAdcListToExport(adcScope, sessionData.getAccountIndex(), adcConditionSet(selectedCol, selectedVal), searchOption.getBeginIndex(), searchOption.getEndIndex());
				log.debug("montotalAdc: montotalAdc{}", montotalAdc);
			}
			else
			{		
				if(searchKey != null)
				{
					OBDtoMonTotalAdcCondition searchCondition = new OBDtoMonTotalAdcCondition();
					searchCondition.setSearchKeyword(searchKey);
					montotalAdc = groupTotalMonitorFacade.getTotalAdcListToExport(adcScope, sessionData.getAccountIndex(), searchCondition, fromRow, toRow);
				}
				else
				{
					montotalAdc = groupTotalMonitorFacade.getTotalAdcListToExport(adcScope, sessionData.getAccountIndex(), adcCondition, fromRow, toRow);
				}
				log.debug("montotalAdc: montotalAdc{}", montotalAdc);
			}			

			montotalAdcList = montotalAdc.getAdcList();
			adcCondition = montotalAdc.getCondition();
			log.debug("{}", montotalAdcList);
			CsvMaker csvMaker = new CsvMaker();
//			csvMaker.initWithAdcMonTotalList2(montotalAdcList);
			
//			csvMaker.initWithAdcMonTotal(montotalAdc);
			csvMaker.initWithAdcMonTotalList(montotalAdcList, adcCondition);
			File csv = csvMaker.write();
//			montotalAdcList = null;
			if (csv != null)
			{
				log.debug("{}",montotalAdc);
				setStrutsStream(csv);
			}	
			else
			{
				log.debug("{}",montotalAdc);
			}
			
			montotalAdc = null;
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

	// 서비스
	public String retrieveServiceMonTotal() throws Exception
	{
		isSuccessful = true;
		try 
		{
			SessionDto sessionData = session.getSessionDto();
			
			if(selectedVal != null || selectedCol!=null)
			{		
				rowTotal = groupTotalMonitorFacade.getTotalSvcListCount(adcScope, sessionData.getAccountIndex(), session.getAccountRole(), svcConditionSet(selectedCol, selectedVal));
				log.debug("rowTotal {}", rowTotal);
			}
			else
			{
				if(searchKey != null)
				{
					OBDtoMonTotalServiceCondition searchCondition = new OBDtoMonTotalServiceCondition();
					searchCondition.setSearchKeyword(searchKey);					
					rowTotal = groupTotalMonitorFacade.getTotalSvcListCount(adcScope, sessionData.getAccountIndex(), session.getAccountRole(), searchCondition);
				}
				else
				{
					rowTotal = groupTotalMonitorFacade.getTotalSvcListCount(adcScope, sessionData.getAccountIndex(), session.getAccountRole(), svcCondition);
				}
				log.debug("rowTotal {}", rowTotal);
			}
//			rowTotal = groupTotalMonitorFacade.getTotalSvcListCount(adcScope, sessionData.getAccountIndex(), svcCondition);
//			rowTotal = 751;
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

	public String loadServiceMonitorList() throws OBException
	{
		try 
		{
			SessionDto sessionData = session.getSessionDto();
						
			if (selectedVal!=null || selectedCol!=null)
			{
				montotalSvc = groupTotalMonitorFacade.getTotalSvcList(adcScope, sessionData.getAccountIndex(), session.getAccountRole(), svcConditionSet(selectedCol, selectedVal), fromRow, toRow, orderType, orderDir);				
				log.debug("montotalSvc: montotalSvc{}", montotalSvc);
			}
			else
			{		
				if(searchKey != null)
				{
					OBDtoMonTotalServiceCondition searchCondition = new OBDtoMonTotalServiceCondition();
					searchCondition.setSearchKeyword(searchKey);
					montotalSvc = groupTotalMonitorFacade.getTotalSvcList(adcScope, sessionData.getAccountIndex(), session.getAccountRole(), searchCondition, fromRow, toRow, orderType, orderDir);
				}
				else
				{
					montotalSvc = groupTotalMonitorFacade.getTotalSvcList(adcScope, sessionData.getAccountIndex(), session.getAccountRole(), svcCondition, fromRow, toRow, orderType, orderDir);
				}
				log.debug("montotalSvc: montotalSvc{}", montotalSvc);
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
		
		return SUCCESS;
	}
	
	// TODO
    // 서비스 Throughput / ConcurrentSession
	public String loadMulitBpsConnInfo() throws OBException
	{
	    try
        {
	        setSearchTimeInterval();
	        OBDtoSearch searchOption = new OBDtoSearch();
	        searchOption.setFromTime(startTime);
	        searchOption.setToTime(endTime);
	        searchOption.setSearchKey(searchKey);
	        	        
//	        vsIndexList.add(adcObject.getStrIndex());	        	        
	        bpsConnInfoData = groupTotalMonitorFacade.getMultiBpsConnInfo(adcObject.getStrIndex(), adcObject.getName(), searchOption);
	        intervalMonitor = monitoringPeriod;
	        log.debug("{}", bpsConnInfoData);
//	        System.out.println(bpsConnInfoData);
//	        System.out.println(bpsConnInfoData.getBpsConn());
//	        System.out.println(bpsConnInfoData.getBpsInfo());
//	        System.out.println(bpsConnInfoData.getBpsInfo().size());
        }
	    catch(OBException e)
	    {
	        throw e;
	    }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
	    return SUCCESS;
	}
	
//	// 서비스 ConcurrentSession 
//	public String loadMulitConcurrentSessionInfo() throws OBException
//    {
//        try
//        {
//            setSearchTimeInterval();
//            OBDtoSearch searchOption = new OBDtoSearch();
//            searchOption.setFromTime(startTime);
//            searchOption.setToTime(endTime);
//            searchOption.setSearchKey(searchKey);
//            
//            vsIndexList.add(adcObject.getStrIndex());                       
//            multiConcurrentInfo = groupTotalMonitorFacade.getVsThroughputInfoGroup(adcObject.getStrIndex(), searchOption);
//            
//            log.debug("{}", multiConcurrentInfo);
//        }
//        catch(OBException e)
//        {
//            throw e;
//        }
//        catch(Exception e)
//        {
//            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//        }
//        return SUCCESS;
//    }
	
	public String loadMultiBpsConnMaxAvgInfo() throws Exception
    {
        try
        {
            setSearchTimeInterval();
            OBDtoSearch searchOption = new OBDtoSearch();
            searchOption.setFromTime(startTime);
            searchOption.setToTime(endTime);
            searchOption.setSearchKey(searchKey);
            
//            vsIndexList.add(adcObject.getStrIndex());                       
//            bpsConnCurAvgMaxData = groupTotalMonitorFacade.getMultiBpsConnMaxAvgInfo(adcObject.getStrIndex(), adcObject.getName(), searchOption);
            bpsConnCurAvgMaxData = groupTotalMonitorFacade.getMultiBpsConnInfo(adcObject.getStrIndex(), adcObject.getName(), searchOption);
            
            log.debug("{}", bpsConnCurAvgMaxData);            
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        
        return SUCCESS;
    }

	
	public String checkExportSvcMonDataExist() throws OBException
	{
		try 
		{
			SessionDto sessionData = session.getSessionDto();
			
			OBDtoSearch searchOption = new OBDtoSearch();
			searchOption.setBeginIndex(null);
			searchOption.setEndIndex(OBDefine.MAX_EXPORT_COUNT);
			
			if(selectedVal != null || selectedCol != null)
			{
				montotalSvc = groupTotalMonitorFacade.getTotalSvcListToExport(adcScope, sessionData.getAccountIndex(), session.getAccountRole(), svcConditionSet(selectedCol, selectedVal), searchOption.getBeginIndex(), searchOption.getEndIndex());
				log.debug("montotalSvc: {}", montotalSvc);
			}
			else
			{
				if(searchKey != null)
				{
					OBDtoMonTotalServiceCondition searchCondition = new OBDtoMonTotalServiceCondition();
					searchCondition.setSearchKeyword(searchKey);
					montotalSvc = groupTotalMonitorFacade.getTotalSvcListToExport(adcScope, sessionData.getAccountIndex(), session.getAccountRole(), searchCondition, fromRow, toRow);
				}
				else
				{
					montotalSvc = groupTotalMonitorFacade.getTotalSvcListToExport(adcScope, sessionData.getAccountIndex(), session.getAccountRole(), svcCondition, fromRow, toRow);
				}
				log.debug("montotalSvc: {}", montotalSvc);
			}
			
			if(montotalSvc.getServiceList() != null && montotalSvc.getServiceList().size() >0)
			{
				isSuccessful = true;
				montotalSvc = null;
			}
			else
			{
				isSuccessful = false;
				montotalSvc = null;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_EXPORT_DATA_NOT_EXIST);
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
		
		return SUCCESS;
	}
	
	public String downloadSvcMonList() throws OBException
	{
		try 
		{
			SessionDto sessionData = session.getSessionDto();
			
			OBDtoSearch searchOption = new OBDtoSearch();
			searchOption.setBeginIndex(null);
			searchOption.setEndIndex(OBDefine.MAX_EXPORT_COUNT);
			
			if(selectedCol != null)
			{
				montotalSvc = groupTotalMonitorFacade.getTotalSvcListToExport(adcScope, sessionData.getAccountIndex(), session.getAccountRole(), svcConditionSet(selectedCol, selectedVal), searchOption.getBeginIndex(), searchOption.getEndIndex());
				log.debug("montotalSvc: {}", montotalSvc);
			}
			else
			{
				if(searchKey != null)
				{
					OBDtoMonTotalServiceCondition searchCondition = new OBDtoMonTotalServiceCondition();
					searchCondition.setSearchKeyword(searchKey);
					montotalSvc = groupTotalMonitorFacade.getTotalSvcListToExport(adcScope, sessionData.getAccountIndex(), session.getAccountRole(), searchCondition, fromRow, toRow);
				}
				else
				{
					montotalSvc = groupTotalMonitorFacade.getTotalSvcListToExport(adcScope, sessionData.getAccountIndex(), session.getAccountRole(), svcCondition, fromRow, toRow);
				}
				log.debug("montotalSvc: {}", montotalSvc);
			}
			
			montotalSvcList = montotalSvc.getServiceList();
			svcCondition = montotalSvc.getCondition();
			log.debug("{}", montotalSvcList);
			
			CsvMaker csvMaker = new CsvMaker();
			csvMaker.initWithSvcMonTotalList(montotalSvcList, svcCondition);
			File csv = csvMaker.write();
			if(csv != null)
			{
				log.debug("{}", montotalSvc);
				setStrutsStream(csv);
			}
			else
			{
				log.debug("{}", montotalSvc);
			}
			montotalSvc = null;
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

	// 그룹
	public String retrieveGroupMonTotal() throws Exception
	{
		isSuccessful = true;
		try 
		{
			SessionDto sessionData = session.getSessionDto();
			if(selectedVal != null || selectedCol != null)
			{
				rowTotal = groupTotalMonitorFacade.getTotalGrpListCount(adcScope, sessionData.getAccountIndex(), grpConditionSet(selectedCol, selectedVal));
				log.debug("rowTotal {}", rowTotal);
			}
			else
			{
				if(searchKey != null)
				{
					OBDtoMonTotalGroupCondition searchCondition = new OBDtoMonTotalGroupCondition();
					searchCondition.setSearchKeyword(searchKey);					
					rowTotal = groupTotalMonitorFacade.getTotalGrpListCount(adcScope, sessionData.getAccountIndex(), searchCondition);
				}
				else
				{
					rowTotal = groupTotalMonitorFacade.getTotalGrpListCount(adcScope, sessionData.getAccountIndex(), grpCondition);
				}
				log.debug("rowTotal {}", rowTotal);
			}	
//			rowTotal = groupTotalMonitorFacade.getTotalGrpListCount(adcScope, sessionData.getAccountIndex(), grpCondition);
//			rowTotal = 23;
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
	
	public String loadGroupMonitorList() throws OBException
	{
		try 
		{
			SessionDto sessionData = session.getSessionDto();
						
			if(selectedVal != null || selectedCol != null)
			{				
				montotalGrp = groupTotalMonitorFacade.getTotalGrpList(adcScope, sessionData.getAccountIndex(), grpConditionSet(selectedCol, selectedVal), fromRow, toRow, orderType, orderDir);
				log.debug("montotalGrp {}", montotalGrp);
			}		
			else
			{
				if(searchKey != null)
				{
					OBDtoMonTotalGroupCondition searchCondition = new OBDtoMonTotalGroupCondition();
					searchCondition.setSearchKeyword(searchKey);
					montotalGrp = groupTotalMonitorFacade.getTotalGrpList(adcScope, sessionData.getAccountIndex(), searchCondition, fromRow, toRow, orderType, orderDir);					
				}
				else
				{
					montotalGrp = groupTotalMonitorFacade.getTotalGrpList(adcScope, sessionData.getAccountIndex(), grpCondition, fromRow, toRow, orderType, orderDir);
				}
				log.debug("montotalGrp {}", montotalGrp);
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
		return SUCCESS;
	}
	
	public String checkExportGroupMonDataExist() throws OBException
	{
		try 
		{
			SessionDto sessionData = session.getSessionDto();
			
			if(selectedVal != null || selectedCol != null)
			{
				montotalGrp = groupTotalMonitorFacade.getTotalGrpListToExport(adcScope, sessionData.getAccountIndex(), grpConditionSet(selectedCol, selectedVal), fromRow, toRow);
				log.debug("montotalGrp: {}", montotalGrp);
			}
			else
			{
				if(searchKey != null)
				{
					OBDtoMonTotalGroupCondition searchCondition = new OBDtoMonTotalGroupCondition();
					searchCondition.setSearchKeyword(searchKey);
					montotalGrp = groupTotalMonitorFacade.getTotalGrpListToExport(adcScope, sessionData.getAccountIndex(), searchCondition, fromRow, toRow);
				}
				else
				{
					montotalGrp = groupTotalMonitorFacade.getTotalGrpListToExport(adcScope, sessionData.getAccountIndex(), grpCondition, fromRow, toRow);
				}
				log.debug("montotalGrp: {}", montotalGrp);
			}
			
			if(montotalGrp.getGroupList() != null && montotalGrp.getGroupList().size() > 0)
			{
				isSuccessful = true;
				montotalGrp = null;
			}
			else
			{
				isSuccessful = false;
				montotalGrp = null;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_EXPORT_DATA_NOT_EXIST);
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
		
		return SUCCESS;
	}
	
	public String downloadGroupMonList() throws OBException
	{
		try 
		{
			SessionDto sessionData = session.getSessionDto();
			
			OBDtoSearch searchOption = new OBDtoSearch();
			searchOption.setBeginIndex(null);
			searchOption.setEndIndex(OBDefine.MAX_EXPORT_COUNT);
			if(selectedVal != null || selectedCol != null)
			{
				montotalGrp = groupTotalMonitorFacade.getTotalGrpListToExport(adcScope, sessionData.getAccountIndex(), grpConditionSet(selectedCol, selectedVal), searchOption.getBeginIndex(), searchOption.getEndIndex());
				log.debug("montotalGrp: {}", montotalGrp);
			}
			else
			{
				if(searchKey != null)
				{
					OBDtoMonTotalGroupCondition searchCondition = new OBDtoMonTotalGroupCondition();
					searchCondition.setSearchKeyword(searchKey);
					montotalGrp = groupTotalMonitorFacade.getTotalGrpListToExport(adcScope, sessionData.getAccountIndex(), searchCondition, searchOption.getBeginIndex(), searchOption.getEndIndex());
				}
				else
				{
					montotalGrp = groupTotalMonitorFacade.getTotalGrpListToExport(adcScope, sessionData.getAccountIndex(), grpCondition, searchOption.getBeginIndex(), searchOption.getEndIndex());
				}
				log.debug("montotalGrp: {}", montotalGrp);
			}
			
			montotalGrpList = montotalGrp.getGroupList();
			grpCondition = montotalGrp.getCondition();
			log.debug("{}", montotalGrpList);
			
			CsvMaker csvMaker = new CsvMaker();
			csvMaker.initWithGrpMonTotalList(montotalGrpList, grpCondition);
			File csv = csvMaker.write();
			if(csv != null)
			{
				log.debug("{}", montotalGrp);
				setStrutsStream(csv);
			}
			else
			{
				log.debug("{}", montotalGrp);
			}
			montotalGrp = null;
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
	
	public String retrieveRealServerMonTotal() throws Exception
	{
		isSuccessful = true;
		try 
		{
			SessionDto sessionData = session.getSessionDto();
			if(selectedVal != null || selectedCol != null)
			{
				rowTotal = groupTotalMonitorFacade.getTotalRealListCount(adcScope, sessionData.getAccountIndex(), rsConditionSet(selectedCol, selectedVal));
				log.debug("rowTotal {}", rowTotal);
			}
			else
			{
				if(searchKey != null)
				{
					OBDtoMonTotalRealCondition searchCondition = new OBDtoMonTotalRealCondition();
					searchCondition.setSearchKeyword(searchKey);					
					rowTotal = groupTotalMonitorFacade.getTotalRealListCount(adcScope, sessionData.getAccountIndex(), searchCondition);
				}
				else
				{					
					rowTotal = groupTotalMonitorFacade.getTotalRealListCount(adcScope, sessionData.getAccountIndex(), rsCondition);
				}
				log.debug("rowTotal {}", rowTotal);
			}	
			
//			rowTotal = 22;
//			log.debug("rowTotal {}", rowTotal);
		} 
//		catch (OBException e)		
//		{
//			throw e;
//		}
		catch (Exception e) 
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		return SUCCESS;
	}
	
	public String loadRealServerMonitorList() throws OBException
	{
		try 
		{
			SessionDto sessionData = session.getSessionDto();
		
			if(selectedVal != null || selectedCol != null)
			{				
				montotalRs = groupTotalMonitorFacade.getTotalRealList(adcScope, sessionData.getAccountIndex(), rsConditionSet(selectedCol, selectedVal), fromRow, toRow, orderType, orderDir);
				log.debug("montotalRs :{}", montotalRs);
			}
			else
			{
				if(searchKey != null)
				{
					OBDtoMonTotalRealCondition searchKeyCondition = new OBDtoMonTotalRealCondition();
					searchKeyCondition.setSearchKeyword(searchKey);
					montotalRs = groupTotalMonitorFacade.getTotalRealList(adcScope, sessionData.getAccountIndex(), searchKeyCondition, fromRow, toRow, orderType, orderDir);
				}
				else
				{
					montotalRs = groupTotalMonitorFacade.getTotalRealList(adcScope, sessionData.getAccountIndex(), rsCondition, fromRow, toRow, orderType, orderDir);
				}
				log.debug("montotalRs :{}", montotalRs);
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
		
		return SUCCESS;
	}
	
	public String checkExportRealServerMonDataExist() throws OBException
	{
		try 
		{
			SessionDto sessionData = session.getSessionDto();
			
			OBDtoSearch searchOption = new OBDtoSearch();
			searchOption.setBeginIndex(null);
			searchOption.setEndIndex(OBDefine.MAX_EXPORT_COUNT);
			
			if(selectedVal != null || selectedCol != null)
			{
				montotalRs = groupTotalMonitorFacade.getTotalRealListToExport(adcScope, sessionData.getAccountIndex(), rsConditionSet(selectedCol, selectedVal),  searchOption.getBeginIndex(), searchOption.getEndIndex());
				log.debug("montotalRs: {}", montotalRs);
			}
			else
			{
				if(searchKey != null)
				{
					OBDtoMonTotalRealCondition searchCondition = new OBDtoMonTotalRealCondition();
					searchCondition.setSearchKeyword(searchKey);
					montotalRs = groupTotalMonitorFacade.getTotalRealListToExport(adcScope, sessionData.getAccountIndex(), searchCondition,  searchOption.getBeginIndex(), searchOption.getEndIndex());
				}
				else
				{
					montotalRs = groupTotalMonitorFacade.getTotalRealListToExport(adcScope, sessionData.getAccountIndex(), rsCondition,  searchOption.getBeginIndex(), searchOption.getEndIndex());
				}
				log.debug("montotalRs: {}", montotalRs);
			}
			
			if (montotalRs.getRealList() != null && montotalRs.getRealList().size() > 0)
			{
				isSuccessful = true;
				montotalRs = null;
			}
			else
			{
				isSuccessful = false;
				montotalSvc = null;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_EXPORT_DATA_NOT_EXIST);
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
		return SUCCESS;		
	}
	
	public String downloadRealServerMonList() throws OBException
	{
		try 
		{
			SessionDto sessionData = session.getSessionDto();
			
			OBDtoSearch searchOption = new OBDtoSearch();
			searchOption.setBeginIndex(null);
			searchOption.setEndIndex(OBDefine.MAX_EXPORT_COUNT);
			
			if(selectedVal != null || selectedCol != null)
			{
				montotalRs = groupTotalMonitorFacade.getTotalRealListToExport(adcScope, sessionData.getAccountIndex(), rsConditionSet(selectedCol, selectedVal), searchOption.getBeginIndex(), searchOption.getEndIndex());
				log.debug("montotalRs: {}", montotalRs);
			}
			else
			{
				if(searchKey != null)
				{
					OBDtoMonTotalRealCondition searchCondition = new OBDtoMonTotalRealCondition();
					searchCondition.setSearchKeyword(searchKey);
					montotalRs = groupTotalMonitorFacade.getTotalRealListToExport(adcScope, sessionData.getAccountIndex(), searchCondition, fromRow, toRow);
				}
				else
				{
					montotalRs = groupTotalMonitorFacade.getTotalRealListToExport(adcScope, sessionData.getAccountIndex(), rsCondition, fromRow, toRow);
				}
				log.debug("montotalRs: {}", montotalRs);
			}
			
			montotalRsList = montotalRs.getRealList();
			rsCondition = montotalRs.getCondition();
			log.debug("{}", montotalRsList);
			
			CsvMaker csvMaker = new CsvMaker();
			csvMaker.initWithRsMonTotalList(montotalRsList, rsCondition);
			File csv = csvMaker.write();
			if(csv != null)
			{
				log.debug("{}", montotalRs);
				setStrutsStream(csv);
			}
			else
			{
				log.debug("{}", montotalRs);
			}
			montotalRs = null;
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
		
	public OBDtoMonTotalAdcCondition adcConditionSet(String selectedCol, String selectedVal)
	{
		JsonParser parser = new JsonParser();
		OBDtoMonTotalAdcCondition condition = new OBDtoMonTotalAdcCondition();
		JsonArray colArray = parser.parse(selectedCol).getAsJsonArray();
		OBDtoMonTotalConditionUnit conditionSetIsSelect = null;
		
		for (int i = 0; i < colArray.size(); i ++)
		{
			conditionSetIsSelect = new OBDtoMonTotalConditionUnit(Boolean.parseBoolean(colArray.get(i).toString()), null);
			switch(i)
			{
			case 0:
				condition.setStatus(conditionSetIsSelect);
				break;
			case 1:
				condition.setType(conditionSetIsSelect);
				break;
			case 2:
				condition.setName(conditionSetIsSelect);
				break;
			case 3:
				condition.setActiveBackupState(conditionSetIsSelect);
				break;
			case 4:
				condition.setModel(conditionSetIsSelect);
				break;
			case 5:
				condition.setSwVersion(conditionSetIsSelect);
				break;
			case 6:
				condition.setConcurrentSession(conditionSetIsSelect);
				break;
			case 7:
				condition.setThroughput(conditionSetIsSelect);
				break;
			case 8:
				condition.setUptimeAge(conditionSetIsSelect);
				break;
			case 9:
				condition.setAdcIp(conditionSetIsSelect);
				break;
			case 10:
				condition.setConfigTime(conditionSetIsSelect);
				break;
			case 11:
				condition.setCpu(conditionSetIsSelect);
				break;
			case 12:
				condition.setMemory(conditionSetIsSelect);
				break;
			case 13:
				condition.setErrorPackets(conditionSetIsSelect);
				break;
			case 14:
				condition.setDropPackets(conditionSetIsSelect);
				break;
			case 15:
				condition.setSslCertValidDays(conditionSetIsSelect);
				break;
			case 16:
				condition.setInterfaceCount(conditionSetIsSelect);
				break;
			case 17:
				condition.setFilter(conditionSetIsSelect);
				break;
			case 18:
				condition.setService(conditionSetIsSelect);
				break;
			case 19:
				condition.setAdcLog24Hour(conditionSetIsSelect);
				break;
			case 20:
				condition.setSlbConfig24Hour(conditionSetIsSelect);
				break;
			default:
				break;
			}
		}	
		
		JsonObject obj = parser.parse(selectedVal).getAsJsonObject();
		String[] parsedStr = null;
//		OBDtoMonTotalAdcCondition condition = null;
//		condition = new OBDtoMonTotalAdcCondition();
		JsonElement elem = null;
		JsonArray filterUnit = null;
		elem = obj.get("group1");			
		if (elem.equals(obj.get("group1")))
		{
			filterUnit = elem.getAsJsonArray();
			ArrayList<OBDtoMonTotalFilterUnit> filterList = new ArrayList<OBDtoMonTotalFilterUnit>();
			OBDtoMonTotalConditionUnit conditionSet= condition.getStatus() != null ? condition.getStatus() : new OBDtoMonTotalConditionUnit(true, null);									
			
			for (int i = 0; i < filterUnit.size(); i++) 
            {
				parsedStr = filterUnit.get(i).toString().replaceAll("\"", "") .split("\\|");
                                 
				filter(parsedStr, filterList);
            }
			conditionSet.setFilter(filterList);
			condition.setStatus(conditionSet);
		}
		
		elem = obj.get("group2");
		if (elem.equals(obj.get("group2")))
		{
			filterUnit = elem.getAsJsonArray();
			ArrayList<OBDtoMonTotalFilterUnit> filterList = new ArrayList<OBDtoMonTotalFilterUnit>();
			OBDtoMonTotalConditionUnit conditionSet= condition.getType() != null ? condition.getType() : new OBDtoMonTotalConditionUnit(true, null);									
			
			for (int i = 0; i < filterUnit.size(); i++) 
            {
				parsedStr = filterUnit.get(i).toString().replaceAll("\"", "") .split("\\|");
                                 
				filter(parsedStr, filterList);
            }
			conditionSet.setFilter(filterList);
			condition.setType(conditionSet);
		}
		
		elem = obj.get("group3");
		if (elem.equals(obj.get("group3")))
		{
			filterUnit = elem.getAsJsonArray();
			ArrayList<OBDtoMonTotalFilterUnit> filterList = new ArrayList<OBDtoMonTotalFilterUnit>();
			OBDtoMonTotalConditionUnit conditionSet= condition.getActiveBackupState() != null ? condition.getActiveBackupState() : new OBDtoMonTotalConditionUnit(true, null);									
			
			for (int i = 0; i < filterUnit.size(); i++) 
            {
				parsedStr = filterUnit.get(i).toString().replaceAll("\"", "") .split("\\|");
                                 
				filter(parsedStr, filterList);
            }
			conditionSet.setFilter(filterList);
			condition.setActiveBackupState(conditionSet);
		}
		elem = obj.get("group4");
		if(elem.equals(obj.get("group4")))
		{
			filterUnit = elem.getAsJsonArray();
			ArrayList<OBDtoMonTotalFilterUnit> filterList = new ArrayList<OBDtoMonTotalFilterUnit>();
			OBDtoMonTotalConditionUnit conditionSet= condition.getModel() !=null ? condition.getModel() : new OBDtoMonTotalConditionUnit(true, null);			
			for (int i = 0; i < filterUnit.size(); i++) 
            {
				parsedStr = filterUnit.get(i).toString().replaceAll("\"", "") .split("\\|");
                                 
				filter(parsedStr, filterList);
            }
			conditionSet.setFilter(filterList);
			condition.setModel(conditionSet);
		}
		elem = obj.get("group5");
		if(elem.equals(obj.get("group5")))
		{
			filterUnit = elem.getAsJsonArray();					
			ArrayList<OBDtoMonTotalFilterUnit> filterList = new ArrayList<OBDtoMonTotalFilterUnit>();
			OBDtoMonTotalConditionUnit conditionSet= condition.getSwVersion() != null ? condition.getSwVersion() : new OBDtoMonTotalConditionUnit(true, null);			
			for (int i = 0; i < filterUnit.size(); i++) 
            {
				parsedStr = filterUnit.get(i).toString().replaceAll("\"", "") .split("\\|");
                                 
				filter(parsedStr, filterList);
            }
			conditionSet.setFilter(filterList);
			condition.setSwVersion(conditionSet);
		}
		elem = obj.get("group6");
		if(elem.equals(obj.get("group6")))
		{
			filterUnit = elem.getAsJsonArray();					
			ArrayList<OBDtoMonTotalFilterUnit> filterList = new ArrayList<OBDtoMonTotalFilterUnit>();
			OBDtoMonTotalConditionUnit conditionSet= condition.getConcurrentSession() != null ? condition.getConcurrentSession() : new OBDtoMonTotalConditionUnit(true, null);			
			for (int i = 0; i < filterUnit.size(); i++) 
            {
				parsedStr = filterUnit.get(i).toString().replaceAll("\"", "") .split("\\|");
                                 
				filter(parsedStr, filterList);
            }
			conditionSet.setFilter(filterList);					
			condition.setConcurrentSession(conditionSet);
		}
		elem = obj.get("group7");
		if(elem.equals(obj.get("group7")))
		{
			filterUnit = elem.getAsJsonArray();					
			ArrayList<OBDtoMonTotalFilterUnit> filterList = new ArrayList<OBDtoMonTotalFilterUnit>();
			OBDtoMonTotalConditionUnit conditionSet= condition.getThroughput() != null ? condition.getThroughput() : new OBDtoMonTotalConditionUnit(true, null);			
			for (int i = 0; i < filterUnit.size(); i++) 
            {
				parsedStr = filterUnit.get(i).toString().replaceAll("\"", "") .split("\\|");
                                 
				filter(parsedStr, filterList);
            }
			conditionSet.setFilter(filterList);
			condition.setThroughput(conditionSet);			
		}
		elem = obj.get("group8");
		if(elem.equals(obj.get("group8")))
		{
			filterUnit = elem.getAsJsonArray();					
			ArrayList<OBDtoMonTotalFilterUnit> filterList = new ArrayList<OBDtoMonTotalFilterUnit>();
			OBDtoMonTotalConditionUnit conditionSet= condition.getUptimeAge() != null ? condition.getUptimeAge() : new OBDtoMonTotalConditionUnit(true, null);			
			for (int i = 0; i < filterUnit.size(); i++) 
            {
				parsedStr = filterUnit.get(i).toString().replaceAll("\"", "") .split("\\|");
                                 
				filter(parsedStr, filterList);
            }
			conditionSet.setFilter(filterList);					
			condition.setUptimeAge(conditionSet);
		}
		elem = obj.get("group9");
		if(elem.equals(obj.get("group9")))
		{
			filterUnit = elem.getAsJsonArray();					
			ArrayList<OBDtoMonTotalFilterUnit> filterList = new ArrayList<OBDtoMonTotalFilterUnit>();
			OBDtoMonTotalConditionUnit conditionSet= condition.getSslCertValidDays() !=null ? condition.getSslCertValidDays() : new OBDtoMonTotalConditionUnit(true, null);			
			for (int i = 0; i < filterUnit.size(); i++) 
            {
				parsedStr = filterUnit.get(i).toString().replaceAll("\"", "") .split("\\|");
                                 
				filter(parsedStr, filterList);
            }
			conditionSet.setFilter(filterList);
			condition.setSslCertValidDays(conditionSet);
		}
		condition.setSearchKeyword(searchKey);	
		return condition;
	}
	
	public OBDtoMonTotalServiceCondition svcConditionSet(String selectedCol, String selectedVal) 
	{
		JsonParser parser = new JsonParser();
		OBDtoMonTotalServiceCondition condition = new OBDtoMonTotalServiceCondition();
		
		JsonArray colArray = parser.parse(selectedCol).getAsJsonArray();
		OBDtoMonTotalConditionUnit conditionSetIsSelect = null;
		
		for (int i = 0; i < colArray.size(); i ++)
		{
			conditionSetIsSelect = new OBDtoMonTotalConditionUnit(Boolean.parseBoolean(colArray.get(i).toString()), null);
			switch(i)
			{
			case 0:
				condition.setStatus(conditionSetIsSelect);
				break;
			case 1:
				condition.setName(conditionSetIsSelect);
				break;
			case 2:
				condition.setIp(conditionSetIsSelect);
				break;
			case 3:
				condition.setPort(conditionSetIsSelect);
				break;
			case 4:
				condition.setBpsIn(conditionSetIsSelect);
				break;
			case 5:
				condition.setBpsOut(conditionSetIsSelect);
				break;
			case 6:
				condition.setBpsTotal(conditionSetIsSelect);
				break;
			case 7:
				condition.setConcurrentSession(conditionSetIsSelect);
				break;
			case 8:
				condition.setAdcName(conditionSetIsSelect);
				break;
			case 9:
				condition.setAdcIp(conditionSetIsSelect);
				break;
			case 10:
				condition.setMember(conditionSetIsSelect);
				break;
			case 11:
				condition.setGroup(conditionSetIsSelect);
				break;
			case 12:
				condition.setLoadbalancing(conditionSetIsSelect);
				break;
			case 13:
				condition.setHealthCheck(conditionSetIsSelect);
				break;
			case 14:
				condition.setPersistence(conditionSetIsSelect);
				break;
			case 15:
				condition.setNoticeGroup(conditionSetIsSelect);
				break;
			case 16:
				condition.setUpdateTime(conditionSetIsSelect);
				break;
			case 17:
				condition.setConfigHistory(conditionSetIsSelect);
				break;
			default:
				break;
			}
		}	
		
		if(selectedVal!=null)
		{	
			JsonObject obj = parser.parse(selectedVal).getAsJsonObject();
			String[] parsedStr = null;
			JsonElement elem = null;
			JsonArray filterUnit = null;
			elem = obj.get("group1");			
			if (elem.equals(obj.get("group1")))
			{
				filterUnit = elem.getAsJsonArray();
				ArrayList<OBDtoMonTotalFilterUnit> filterList = new ArrayList<OBDtoMonTotalFilterUnit>();
				OBDtoMonTotalConditionUnit conditionSet= condition.getStatus() != null ? condition.getStatus() : new OBDtoMonTotalConditionUnit(true, null);									
				
				for (int i = 0; i < filterUnit.size(); i++) 
	            {
					parsedStr = filterUnit.get(i).toString().replaceAll("\"", "") .split("\\|");
	                                 
					filter(parsedStr, filterList);
	            }
				conditionSet.setFilter(filterList);
				condition.setStatus(conditionSet);
			}
			
			elem = obj.get("group2");
			if (elem.equals(obj.get("group2")))
			{
				filterUnit = elem.getAsJsonArray();
				ArrayList<OBDtoMonTotalFilterUnit> filterList = new ArrayList<OBDtoMonTotalFilterUnit>();
				OBDtoMonTotalConditionUnit conditionSet= condition.getPort() != null ? condition.getPort() : new OBDtoMonTotalConditionUnit(true, null);									
				
				for (int i = 0; i < filterUnit.size(); i++) 
	            {
					parsedStr = filterUnit.get(i).toString().replaceAll("\"", "") .split("\\|");
	                                 
					filter(parsedStr, filterList);
	            }
				conditionSet.setFilter(filterList);
				condition.setPort(conditionSet);
			}
//			elem = obj.get("group3");
//			if(elem.equals(obj.get("group3")))
//			{
//				filterUnit = elem.getAsJsonArray();
//				ArrayList<OBDtoMonTotalFilterUnit> filterList = new ArrayList<OBDtoMonTotalFilterUnit>();
//				OBDtoMonTotalConditionUnit conditionSet= condition.getBackup() != null ? condition.getBackup() : new OBDtoMonTotalConditionUnit(true, null);			
//				for (int i = 0; i < filterUnit.size(); i++) 
//	            {
//					parsedStr = filterUnit.get(i).toString().replaceAll("\"", "") .split("\\|");
//	                                 
//					filter(parsedStr, filterList);
//	            }
//				conditionSet.setFilter(filterList);
//				condition.setBackup(conditionSet);
//			}
			elem = obj.get("group3");
			if(elem.equals(obj.get("group3")))
			{
				filterUnit = elem.getAsJsonArray();					
				ArrayList<OBDtoMonTotalFilterUnit> filterList = new ArrayList<OBDtoMonTotalFilterUnit>();
//				OBDtoMonTotalConditionUnit conditionSet= condition.getThroughput() != null ? condition.getThroughput() : new OBDtoMonTotalConditionUnit(true, null);			
				OBDtoMonTotalConditionUnit conditionSet= condition.getBpsTotal() != null ? condition.getBpsTotal() : new OBDtoMonTotalConditionUnit(true, null);
				for (int i = 0; i < filterUnit.size(); i++) 
	            {
					parsedStr = filterUnit.get(i).toString().replaceAll("\"", "") .split("\\|");
	                                 
					filter(parsedStr, filterList);
	            }
				conditionSet.setFilter(filterList);
//				condition.setThroughput(conditionSet);
				condition.setBpsTotal(conditionSet);
			}
			elem = obj.get("group4");
			if(elem.equals(obj.get("group4")))
			{
				filterUnit = elem.getAsJsonArray();					
				ArrayList<OBDtoMonTotalFilterUnit> filterList = new ArrayList<OBDtoMonTotalFilterUnit>();
				OBDtoMonTotalConditionUnit conditionSet= condition.getConcurrentSession() != null ? condition.getConcurrentSession() : new OBDtoMonTotalConditionUnit(true, null);			
				for (int i = 0; i < filterUnit.size(); i++) 
	            {
					parsedStr = filterUnit.get(i).toString().replaceAll("\"", "") .split("\\|");
	                                 
					filter(parsedStr, filterList);
	            }
				conditionSet.setFilter(filterList);
				condition.setConcurrentSession(conditionSet);
			}
		}
		condition.setSearchKeyword(searchKey);
		return condition;
	}
	
	public OBDtoMonTotalGroupCondition grpConditionSet(String selectedCol, String selectedVal)
	{
		JsonParser parser = new JsonParser();
		OBDtoMonTotalGroupCondition condition = new OBDtoMonTotalGroupCondition();
		
		JsonArray colArray = parser.parse(selectedCol).getAsJsonArray();
		OBDtoMonTotalConditionUnit conditionSetIsSelect = null;
		
		for (int i = 0; i < colArray.size(); i ++)
		{
			conditionSetIsSelect = new OBDtoMonTotalConditionUnit(Boolean.parseBoolean(colArray.get(i).toString()), null);
			switch(i)
			{
			case 0:
				condition.setName(conditionSetIsSelect);
				break;
			case 1:
				condition.setId(conditionSetIsSelect);
				break;
			case 2:
				condition.setBackup(conditionSetIsSelect);
				break;
			case 3:
				condition.setMember(conditionSetIsSelect);
				break;
			case 4:
				condition.setFilter(conditionSetIsSelect);
				break;
			case 5:
				condition.setVsAssigned(conditionSetIsSelect);
				break;
			case 6:
				condition.setAdcName(conditionSetIsSelect);
				break;
			case 7:
				condition.setAdcType(conditionSetIsSelect);
				break;
			case 8:
				condition.setAdcIp(conditionSetIsSelect);
				break;			
			default:
				break;
			}
		}	
		
		JsonObject obj = parser.parse(selectedVal).getAsJsonObject();
		String[] parsedStr = null;
		JsonElement elem = null;
		JsonArray filterUnit = null;
		elem = obj.get("group1");
//		if(elem.equals(obj.get("group1")))
//		{
			filterUnit = elem.getAsJsonArray();
			ArrayList<OBDtoMonTotalFilterUnit> filterList = new ArrayList<OBDtoMonTotalFilterUnit>();			
			OBDtoMonTotalConditionUnit conditionSet= condition.getBackup() != null ? condition.getBackup() : new OBDtoMonTotalConditionUnit(true, null);	
			
//			parsedStr = filterUnit.toString().replaceAll("\"", "").split("\\|");	
			
			for (int i = 0; i < filterUnit.size(); i++) 
            {
				parsedStr = filterUnit.get(i).toString().replaceAll("\"", "") .split("\\|");
                                 
				filter(parsedStr, filterList);
            }
					        
			conditionSet.setFilter(filterList);
			condition.setBackup(conditionSet);
//		}
			
		condition.setSearchKeyword(searchKey);
		return condition;
	}
	
	public OBDtoMonTotalRealCondition rsConditionSet(String selectedCol, String selectedVal)
	{
		JsonParser parser = new JsonParser();
		OBDtoMonTotalRealCondition condition = new OBDtoMonTotalRealCondition();
		
		JsonArray colArray = parser.parse(selectedCol).getAsJsonArray();
		OBDtoMonTotalConditionUnit conditionSetIsSelect = null;
		
		for (int i = 0; i < colArray.size(); i ++)
		{
			conditionSetIsSelect = new OBDtoMonTotalConditionUnit(Boolean.parseBoolean(colArray.get(i).toString()), null);
			switch(i)
			{
			case 0:
				condition.setStatus(conditionSetIsSelect);
				break;
			case 1:
				condition.setState(conditionSetIsSelect);
				break;
			case 2:
				condition.setName(conditionSetIsSelect);
				break;
			case 3:
				condition.setIp(conditionSetIsSelect);
				break;
			case 4:
				condition.setUsed(conditionSetIsSelect);
				break;
			case 5:
				condition.setGroup(conditionSetIsSelect);
				break;
			case 6:
				condition.setAdcName(conditionSetIsSelect);
				break;
			case 7:
				condition.setAdcType(conditionSetIsSelect);
				break;
			case 8:
				condition.setAdcIp(conditionSetIsSelect);
				break;
			case 9:
				condition.setRatio(conditionSetIsSelect);
				break;
			default:
				break;
			}
		}	
		
		JsonObject obj = parser.parse(selectedVal).getAsJsonObject();
		String[] parsedStr = null;
		JsonElement elem = null;
		JsonArray filterUnit = null;
		elem = obj.get("group1");
		if(elem.equals(obj.get("group1")))
		{
			filterUnit = elem.getAsJsonArray();
			ArrayList<OBDtoMonTotalFilterUnit> filterList = new ArrayList<OBDtoMonTotalFilterUnit>();
			OBDtoMonTotalConditionUnit conditionSet= condition.getStatus() != null ? condition.getStatus() : new OBDtoMonTotalConditionUnit(true, null);
			
			for (int i = 0; i < filterUnit.size(); i++) 
			{
				parsedStr = filterUnit.get(i).toString().replaceAll("\"", "").split("\\|");
				filter(parsedStr, filterList);
			}
			conditionSet.setFilter(filterList);
			condition.setStatus(conditionSet);
		}
		
		elem = obj.get("group2");
		if(elem.equals(obj.get("group2")))
		{
			filterUnit = elem.getAsJsonArray();
			ArrayList<OBDtoMonTotalFilterUnit> filterList = new ArrayList<OBDtoMonTotalFilterUnit>();
			OBDtoMonTotalConditionUnit conditionSet= condition.getState() != null ? condition.getState() : new OBDtoMonTotalConditionUnit(true, null);
			
			for (int i = 0; i < filterUnit.size(); i++) 
			{
				parsedStr = filterUnit.get(i).toString().replaceAll("\"", "").split("\\|");
				filter(parsedStr, filterList);
			}
			conditionSet.setFilter(filterList);
			condition.setState(conditionSet);
		}
				
		elem = obj.get("group3");
		if(elem.equals(obj.get("group3")))
		{
			filterUnit = elem.getAsJsonArray();
			ArrayList<OBDtoMonTotalFilterUnit> filterList = new ArrayList<OBDtoMonTotalFilterUnit>();
			OBDtoMonTotalConditionUnit conditionSet= condition.getUsed() != null ? condition.getUsed() : new OBDtoMonTotalConditionUnit(true, null);
			
			for (int i = 0; i < filterUnit.size(); i++) 
			{
				parsedStr = filterUnit.get(i).toString().replaceAll("\"", "").split("\\|");
				filter(parsedStr, filterList);
			}
			conditionSet.setFilter(filterList);
			condition.setUsed(conditionSet);
		}
		
		condition.setSearchKeyword(searchKey);
		return condition;
	}
	public void filter(String[] parsedStr, ArrayList<OBDtoMonTotalFilterUnit> filterList)
	{
	     OBDtoMonTotalFilterUnit filter = new OBDtoMonTotalFilterUnit(null, null, null, true);
	          filter.setIndex(Integer.parseInt(parsedStr[0]));
	          filter.setTitle(parsedStr[1]);
	          filter.setValue(parsedStr[2]);
	          filter.setSelect(Boolean.parseBoolean(parsedStr[3]));
	          filterList.add(filter);
	}
	
	public OBDtoMonTotalAdcCondition setCondition(OBDtoMonTotalAdcCondition condition, OBDtoMonTotalConditionUnit conditionSetIsSelect, int i)
	{
//		OBDtoMonTotalConditionUnit conditionSetIsSelect = null;
		switch(i)
		{
		case 0:
			condition.setStatus(conditionSetIsSelect);
			break;
		case 1:
			condition.setName(conditionSetIsSelect);
			break;
		case 2:
			condition.setActiveBackupState(conditionSetIsSelect);
			break;
		case 3:
			condition.setModel(conditionSetIsSelect);
			break;
		case 4:
			condition.setSwVersion(conditionSetIsSelect);
			break;
		case 5:
			condition.setThroughput(conditionSetIsSelect);
			break;
		case 6:
			condition.setConcurrentSession(conditionSetIsSelect);
			break;
		case 7:
			condition.setUptimeAge(conditionSetIsSelect);
			break;
		case 8:
			condition.setAdcIp(conditionSetIsSelect);
			break;
		case 9:
			condition.setConfigTime(conditionSetIsSelect);
			break;
		case 10:
			condition.setCpu(conditionSetIsSelect);
			break;
		case 11:
			condition.setMemory(conditionSetIsSelect);
			break;
		case 12:
			condition.setErrorPackets(conditionSetIsSelect);
			break;
		case 13:
			condition.setDropPackets(conditionSetIsSelect);
			break;
		case 14:
			condition.setSslCertValidDays(conditionSetIsSelect);
			break;
		case 15:
			condition.setInterfaceCount(conditionSetIsSelect);
			break;
		case 16:
			condition.setFilter(conditionSetIsSelect);
			break;
		case 17:
			condition.setService(conditionSetIsSelect);
			break;
		case 18:
			condition.setAdcLog24Hour(conditionSetIsSelect);
			break;
		case 19:
			condition.setSlbConfig24Hour(conditionSetIsSelect);
			break;
		default:
			break;
		}
		return condition;
	}
	
	private void setSearchTimeInterval()
    {
        endTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endTime);
        if (null != hour) {
            calendar.add(Calendar.HOUR_OF_DAY, -hour);
        } else {
            calendar.add(Calendar.HOUR_OF_DAY, -12);
        }
        startTime = calendar.getTime();
        log.debug("startTime: " + startTime.toString() + "endTime: " + endTime.toString());
    }
	
	public Integer getRowTotal() 
	{
		return rowTotal;
	}

	public void setRowTotal(Integer rowTotal) 
	{
		this.rowTotal = rowTotal;
	}

	public String getSearchKey() 
	{
		return searchKey;
	}

	public void setSearchKey(String searchKey) 
	{
		this.searchKey = searchKey;
	}

	public Integer getFromRow() 
	{
		return fromRow;
	}

	public void setFromRow(Integer fromRow) 
	{
		this.fromRow = fromRow;
	}

	public Integer getToRow() 
	{
		return toRow;
	}

	public void setToRow(Integer toRow) 
	{
		this.toRow = toRow;
	}

	public Integer getOrderDir() 
	{
		return orderDir;
	}

	public void setOrderDir(Integer orderDir) 
	{
		this.orderDir = orderDir;
	}

	public Integer getOrderType() 
	{
		return orderType;
	}

	public void setOrderType(Integer orderType) 
	{
		this.orderType = orderType;
	}

	public OBDtoMonTotalAdc getMontotalAdc() 
	{
		return montotalAdc;
	}

	public void setMontotalAdc(OBDtoMonTotalAdc montotalAdc) 
	{
		this.montotalAdc = montotalAdc;
	}

	public OBDtoMonTotalAdcCondition getAdcCondition() 
	{
		return adcCondition;
	}

	public void setAdcCondition(OBDtoMonTotalAdcCondition adcCondition) 
	{
		this.adcCondition = adcCondition;
	}

	public OBDtoAdcScope getAdcScope() 
	{
		return adcScope;
	}

	public void setAdcScope(OBDtoAdcScope adcScope) 
	{
		this.adcScope = adcScope;
	}

	public String getSelectedVal() 
	{
		return selectedVal;
	}

	public void setSelectedVal(String selectedVal) 
	{
		this.selectedVal = selectedVal;
	}

	public OBDtoMonTotalService getMontotalSvc() 
	{
		return montotalSvc;
	}

	public void setMontotalSvc(OBDtoMonTotalService montotalSvc) 
	{
		this.montotalSvc = montotalSvc;
	}

	public OBDtoMonTotalServiceCondition getSvcCondition() 
	{
		return svcCondition;
	}

	public void setSvcCondition(OBDtoMonTotalServiceCondition svcCondition) 
	{
		this.svcCondition = svcCondition;
	}

	public String getSelectedCol() 
	{
		return selectedCol;
	}

	public void setSelectedCol(String selectedCol) 
	{
		this.selectedCol = selectedCol;
	}

	public OBDtoMonTotalGroup getMontotalGrp() 
	{
		return montotalGrp;
	}

	public void setMontotalGrp(OBDtoMonTotalGroup montotalGrp) 
	{
		this.montotalGrp = montotalGrp;
	}

	public OBDtoMonTotalGroupCondition getGrpCondition() 
	{
		return grpCondition;
	}

	public void setGrpCondition(OBDtoMonTotalGroupCondition grpCondition) 
	{
		this.grpCondition = grpCondition;
	}

	public OBDtoMonTotalReal getMontotalRs() 
	{
		return montotalRs;
	}

	public void setMontotalRs(OBDtoMonTotalReal montotalRs) 
	{
		this.montotalRs = montotalRs;
	}

	public OBDtoMonTotalRealCondition getRsCondition() 
	{
		return rsCondition;
	}

	public void setRsCondition(OBDtoMonTotalRealCondition rsCondition) 
	{
		this.rsCondition = rsCondition;
	}

	public OBDtoADCObject getAdcObject()
    {
        return adcObject;
    }

    public void setAdcObject(OBDtoADCObject adcObject)
    {
        this.adcObject = adcObject;
    }

//    public ArrayList<OBDtoBpsConnData> getMultiBpsConnInfo()
//    {
//        return multiBpsConnInfo;
//    }
//
//    public void setMultiThroughtputInfo(ArrayList<OBDtoBpsConnData> multiBpsConnInfo)
//    {
//        this.multiBpsConnInfo = multiBpsConnInfo;
//    }
//    
//    public OBDtoBpsConnData getMultiBpsConnAvgMaxData()
//    {
//        return multiBpsConnAvgMaxData;
//    }
//
//    public void setMultiBpsConnAvgMaxData(OBDtoBpsConnData multiBpsConnAvgMaxData)
//    {
//        this.multiBpsConnAvgMaxData = multiBpsConnAvgMaxData;
//    }        

    public OBDtoServiceMonitoringChart getBpsConnInfoData()
    {
        return bpsConnInfoData;
    }
    
    public void setBpsConnInfoData(OBDtoServiceMonitoringChart bpsConnInfoData)
    {
        this.bpsConnInfoData = bpsConnInfoData;
    }
    
    public OBDtoServiceMonitoringChart getBpsConnCurAvgMaxData()
    {
        return bpsConnCurAvgMaxData;
    }
    
    public void setBpsConnCurAvgMaxData(OBDtoServiceMonitoringChart bpsConnCurAvgMaxData)
    {
        this.bpsConnCurAvgMaxData = bpsConnCurAvgMaxData;
    }
    
    public Date getStartTime()
    {
        return startTime;
    }
    
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public ArrayList<String> getVsIndexList()
    {
        return vsIndexList;
    }

    public void setVsIndexList(ArrayList<String> vsIndexList)
    {
        this.vsIndexList = vsIndexList;
    }

    public Integer getMonitoringPeriod()
    {
        return monitoringPeriod;
    }
    public void setMonitoringPeriod(Integer monitoringPeriod)
    {
        this.monitoringPeriod = monitoringPeriod;
    }
    public Integer getIntervalMonitor()
    {
        return intervalMonitor;
    }
    public void setIntervalMonitor(Integer intervalMonitor)
    {
        this.intervalMonitor = intervalMonitor;
    }
    @Override
    public String toString()
    {
        return "GroupTotalMonitorAction [groupTotalMonitorFacade="
                + groupTotalMonitorFacade + ", dashboardFacade="
                + dashboardFacade + ", rowTotal=" + rowTotal + ", searchKey="
                + searchKey + ", fromRow=" + fromRow + ", toRow=" + toRow
                + ", orderDir=" + orderDir + ", orderType=" + orderType
                + ", montotalAdc=" + montotalAdc + ", montotalAdcList="
                + montotalAdcList + ", adcCondition=" + adcCondition
                + ", montotalSvc=" + montotalSvc + ", montotalSvcList="
                + montotalSvcList + ", svcCondition=" + svcCondition
                + ", montotalGrp=" + montotalGrp + ", montotalGrpList="
                + montotalGrpList + ", grpCondition=" + grpCondition
                + ", montotalRs=" + montotalRs + ", montotalRsList="
                + montotalRsList + ", rsCondition=" + rsCondition
                + ", adcScope=" + adcScope + ", selectedVal=" + selectedVal
                + ", selectedCol=" + selectedCol + ", adcObject=" + adcObject
                + ", bpsConnInfoData=" + bpsConnInfoData
                + ", bpsConnCurAvgMaxData=" + bpsConnCurAvgMaxData
                + ", startTime=" + startTime + ", endTime=" + endTime
                + ", hour=" + hour + ", vsIndexList=" + vsIndexList
                + ", monitoringPeriod=" + monitoringPeriod
                + ", intervalMonitor=" + intervalMonitor + "]";
    }		
}
