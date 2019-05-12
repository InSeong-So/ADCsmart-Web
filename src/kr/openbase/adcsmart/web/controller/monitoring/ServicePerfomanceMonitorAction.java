package kr.openbase.adcsmart.web.controller.monitoring;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoDataObj;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvAdditional;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultBpsConnInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultPreBpsConnChart;
import kr.openbase.adcsmart.service.impl.OBEnvManagementImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.FaultSvcPerfInfoDto;
import kr.openbase.adcsmart.web.facade.dto.VsTrafficInfoDto;
import kr.openbase.adcsmart.web.facade.monitoring.ServicePerfomanceMonitorFacade;
import kr.openbase.adcsmart.web.util.CsvMaker;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

@Controller
@Scope(value = "prototype")

public class ServicePerfomanceMonitorAction extends BaseAction
{
	private transient Logger log = LoggerFactory.getLogger(ServicePerfomanceMonitorAction.class);
	
	@Autowired
	private ServicePerfomanceMonitorFacade ServicePerfomanceFacade;
	
	private AdcDto adc;
	private OBDtoADCObject adcObject;
	private OBDtoSearch searchOption;
	private OBDtoOrdering orderOption;
	private Integer totalCount;
	private Integer totalRealCount;
	private VsTrafficInfoDto vsTrafficInfo;
	private String vsIndex;
	private Integer svcPort;	
	private Date startTime;
	private Date endTime;
	private Long startTimeL;
    private Long endTimeL;	
	private String searchTimeMode;
	private String selectedChartTapMode;
	private ArrayList<FaultSvcPerfInfoDto> svcPerfInfoList;
	private ArrayList<OBDtoFaultBpsConnInfo> BpsConnInfoList;
	private OBDtoFaultPreBpsConnChart BpsConnInfo;
	private OBDtoFaultPreBpsConnChart BpsConnAvgMaxInfo;
	private OBDtoFaultPreBpsConnChart ResponseTimeInfo;
	private OBDtoFaultPreBpsConnChart MemberHistoryInfo;
    private OBDtoFaultPreBpsConnChart MemberAvgMaxInfo;
	private OBDtoFaultBpsConnInfo RealTimeBpsConnInfo;
	private OBDtoFaultBpsConnInfo prevInfo;
	private Long prevOccurTime;
	private Long inBpsValue;
	private Long outBpsValue;
	private Long totalBpsValue;
	private Long totalPpsValue;
	private Long totalConnCurrValue;
	private Long totalConnMaxValue;
	private Long totalConnTotValue;
	private Integer preCompare;
	private Date compareStartTime;
    private Date compareEndTime;
    private Long selectedDate;
    private Date selectedTime;
    private Integer monitoringPeriod;
    private Integer intervalMonitor;
        
    public ServicePerfomanceMonitorAction()
    {       
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
	
	
	public String loadServicePerfomanceContent() throws OBException
	{
		return SUCCESS;		
	}
	
	public String loadRealServerContent() throws OBException
	{
		return SUCCESS;		
	}
	// 서비스 성능 모니터링 Table Data Total Count
	public String retrieveSvcPerfInfoTotalCount() throws Exception
	{
		try
		{
			if (null != adcObject && null != adcObject.getIndex()) 
			{
				totalCount = ServicePerfomanceFacade.getTotalCountTrafficInfoList(adcObject, searchOption, session.getAccountIndex(), session.getAccountRole());				
			}
			
		}
		catch(OBException e)
		{
			e.printStackTrace();
			throw e;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new OBException(e.getMessage());
		}
		
		return SUCCESS;
	}
	// 서비스 성능 모니터링 Table Data List
	public String loadSvcPerfInfoList() throws Exception
	{
		try 
		{
			if (null != adcObject && null != adcObject.getIndex() && -1 != searchOption.getBeginIndex() && -1 != searchOption.getEndIndex()) 
			{
				svcPerfInfoList = ServicePerfomanceFacade.getSvcPerfInfoList(adcObject, searchOption, orderOption, session.getAccountIndex(), session.getAccountRole());				
				log.debug("{}", svcPerfInfoList);				
			}				
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw e;
		}
		
		return SUCCESS;
	}
	// 서비스 Member 성능 모니터링 Table Data Total Count
	public String retrieveVsMemberInfoTotalCount() throws Exception
	{
		try
		{
			if (null != adcObject && null != adcObject.getIndex()) 
			{
				totalRealCount = ServicePerfomanceFacade.getTotalCountMemberInfo(adcObject, searchOption);
			}
			log.debug("{}",totalRealCount);
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage());
		}
		
		return SUCCESS;
	}
	// 서비스 Member 성능 모니터링 Table Data List
	public String loadSvcMemberInfoList() throws Exception
	{
		try
		{
			if (null != adcObject && null != adcObject.getIndex()&& null != adcObject.getIndex() ) 
			{
				vsTrafficInfo = ServicePerfomanceFacade.getVsMemberInfo(adcObject, searchOption, orderOption);
//				vsTrafficInfo = ServicePerfomanceFacade.getVsMemberInfo(adcObject, searchOption, orderOption, vsIndex, svcPort);
//				log.debug("{}", vsTrafficInfo);				
			}
			log.debug("{}", vsTrafficInfo);
		} 
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage());
		}
		
		return SUCCESS;
	}
	// 서비스 Member 성능 모니터링 Chart Data
	public String loadVsMemberHistoryInfoList() throws Exception
	{
		try
		{
			if (null != adcObject && null != adcObject.getStrIndex()) 
			{
			    setSearchTime();
				OBDtoSearch searchOption = new OBDtoSearch();
				searchOption.setFromTime(startTime);
				searchOption.setToTime(endTime);
				MemberHistoryInfo = ServicePerfomanceFacade.getVsMemberHistory(adcObject, searchOption);
				log.debug("{}", MemberHistoryInfo);			
			}		
		} 
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage());
		}
		
		return SUCCESS;
	}
	
	public String loadVsMemberHistoryInfo() throws Exception
    {
        try
        {
            if (null != adcObject && null != adcObject.getStrIndex())
            {
                setSearchTime();
                setCompareSearchTime();
                OBDtoSearch searchOption = new OBDtoSearch();
                searchOption.setFromTime(startTime);
                searchOption.setToTime(endTime);
                searchOption.setPreFromTime(compareStartTime);
                searchOption.setPreToTime(compareEndTime);
                                   
                MemberHistoryInfo = ServicePerfomanceFacade.getVsMemberHistory(adcObject, searchOption);
                intervalMonitor = monitoringPeriod;
                log.debug("{}", MemberHistoryInfo);
            }
        } 
        catch(OBException e)
        {
            e.printStackTrace();
            throw e;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new OBException(e.getMessage());
        }
        
        return SUCCESS;
    }
	
	public String loadVsMemberConnHistoryInfo() throws Exception
    {
        try
        {
            if (null != adcObject && null != adcObject.getStrIndex())
            {
                setSearchTime();
                setCompareSearchTime();
                OBDtoSearch searchOption = new OBDtoSearch();
                searchOption.setFromTime(startTime);
                searchOption.setToTime(endTime);
                searchOption.setPreFromTime(compareStartTime);
                searchOption.setPreToTime(compareEndTime);
                                   
                MemberHistoryInfo = ServicePerfomanceFacade.getVsMemberHistory(adcObject, searchOption);
                intervalMonitor = monitoringPeriod;
                log.debug("{}", MemberHistoryInfo);
            }
        } 
        catch(OBException e)
        {
            e.printStackTrace();
            throw e;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new OBException(e.getMessage());
        }
        
        return SUCCESS;
    }
	
	// 서비스 Member 성능 모니터링 Max, Avg, Curr Chart Data
    public String loadVsMemberMaxAvgHistoryInfo() throws Exception
    {
        try
        {
            if (null != adcObject && null != adcObject.getStrIndex())
            {
                setSearchTime();
                setCompareSearchTime();
                OBDtoSearch searchOption = new OBDtoSearch();
                searchOption.setFromTime(startTime);
                searchOption.setToTime(endTime);
                searchOption.setPreFromTime(compareStartTime);
                searchOption.setPreToTime(compareEndTime);
                
                MemberAvgMaxInfo = ServicePerfomanceFacade.getVsMemberMaxAvgHistory(adcObject, searchOption);
                log.debug("{}", MemberAvgMaxInfo);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        
        return SUCCESS;
    }

	// 서비스 성능 모니터링 Chart Data
	public String loadBpsConnHistoryInfo() throws Exception
	{
		try
		{
			if (null != adcObject && null != adcObject.getIndex() && !StringUtils.isEmpty(vsIndex))
			{
			    setSearchTime();
			    setCompareSearchTime();
				OBDtoSearch searchOption = new OBDtoSearch();
				searchOption.setFromTime(startTime);
				searchOption.setToTime(endTime);
				searchOption.setPreFromTime(compareStartTime);
                searchOption.setPreToTime(compareEndTime);
								   
				BpsConnInfo = ServicePerfomanceFacade.getBpsConnHistory(adcObject, vsIndex, svcPort, searchOption);
				intervalMonitor = monitoringPeriod;
				log.debug("{}", BpsConnInfo);
			}
		} 
		catch(OBException e)
		{
			e.printStackTrace();
			throw e;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new OBException(e.getMessage());
		}
		
		return SUCCESS;
	}
	public String loadConnHistoryInfo() throws Exception
    {
        try
        {
            if (null != adcObject && null != adcObject.getIndex() && !StringUtils.isEmpty(vsIndex))
            {
                setSearchTime();
                setCompareSearchTime();
                OBDtoSearch searchOption = new OBDtoSearch();
                searchOption.setFromTime(startTime);
                searchOption.setToTime(endTime);
                searchOption.setPreFromTime(compareStartTime);
                searchOption.setPreToTime(compareEndTime);
                                   
                BpsConnInfo = ServicePerfomanceFacade.getBpsConnHistory(adcObject, vsIndex, svcPort, searchOption);
                intervalMonitor = monitoringPeriod;
                log.debug("{}", BpsConnInfo);
            }
        } 
        catch(OBException e)
        {
            e.printStackTrace();
            throw e;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new OBException(e.getMessage());
        }
        
        return SUCCESS;
    }
	public String loadBpsConnMaxAvgHistoryInfo() throws Exception
    {
        try
        {
            if (null != adcObject && null != adcObject.getIndex() && !StringUtils.isEmpty(vsIndex))
            {
                setSearchTime();
                setCompareSearchTime();
                OBDtoSearch searchOption = new OBDtoSearch();
                searchOption.setFromTime(startTime);
                searchOption.setToTime(endTime);
                searchOption.setPreFromTime(compareStartTime);
                searchOption.setPreToTime(compareEndTime);
                
                BpsConnAvgMaxInfo = ServicePerfomanceFacade.getBpsConnMaxAvgHistory(adcObject, vsIndex, svcPort, searchOption);
                log.debug("{}", BpsConnAvgMaxInfo);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        
        return SUCCESS;
    }
	
	// 응답시간 Chart Data
	public String loadResponseTimeHistoryInfo() throws Exception
	{
		try
		{
			if (null != adcObject && null != adcObject.getIndex() && !StringUtils.isEmpty(vsIndex))
			{
			    setSearchTime();
			    setCompareSearchTime();
                OBDtoSearch searchOption = new OBDtoSearch();
                searchOption.setFromTime(startTime);
                searchOption.setToTime(endTime);
                searchOption.setPreFromTime(compareStartTime);
                searchOption.setPreToTime(compareEndTime);
				ResponseTimeInfo = ServicePerfomanceFacade.getResponseTimeHistory(adcObject, vsIndex, svcPort, searchOption);
				intervalMonitor = monitoringPeriod;
				log.debug("{}", ResponseTimeInfo);
			}
		} 
		catch(OBException e)
		{
			e.printStackTrace();
			throw e;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new OBException(e.getMessage());
		}
		return SUCCESS;
	}
	
	// 응답시간 curr / avg / max 값
	public String loadResponseMaxAvgHistoryInfo() throws Exception
    {
        try
        {
            if (null != adcObject && null != adcObject.getIndex() && !StringUtils.isEmpty(vsIndex))
            {
                setSearchTime();
                setCompareSearchTime();
                OBDtoSearch searchOption = new OBDtoSearch();
                searchOption.setFromTime(startTime);
                searchOption.setToTime(endTime);
                searchOption.setPreFromTime(compareStartTime);
                searchOption.setPreToTime(compareEndTime);
                
                ResponseTimeInfo = ServicePerfomanceFacade.getResponseTimeMaxAvgHistory(adcObject, vsIndex, svcPort, searchOption);
                log.debug("{}", ResponseTimeInfo);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        
        return SUCCESS;
    }
	
	// 실시간 Chart Data
	public String loadRealTimeBpsConnInfo() throws Exception
	{
		try
		{
			if (null != adcObject && null != adcObject.getIndex() && !StringUtils.isEmpty(vsIndex))
			{
				Calendar calendar = Calendar.getInstance();
				if(prevOccurTime != null)
				{
					calendar.setTimeInMillis(prevOccurTime);
				}
				
				OBDtoDataObj inBpsObj = new OBDtoDataObj();
				inBpsObj.setValue(inBpsValue);
	            if(prevOccurTime != null)
                {
	                inBpsObj.setOccurTime(calendar.getTime());
                }	 
	            
	            OBDtoDataObj outBpsObj = new OBDtoDataObj();
	            outBpsObj.setValue(outBpsValue);
	            if(prevOccurTime != null)
                {
	                outBpsObj.setOccurTime(calendar.getTime());
                }
				
				OBDtoDataObj totalBpsObj = new OBDtoDataObj();				
				totalBpsObj.setValue(totalBpsValue);
				if(prevOccurTime != null)
				{
					totalBpsObj.setOccurTime(calendar.getTime());
				}
				
				OBDtoDataObj totalPpsObj = new OBDtoDataObj();				
				totalPpsObj.setValue(totalPpsValue);
				if(prevOccurTime != null)
				{
					totalPpsObj.setOccurTime(calendar.getTime());
				}
				
				OBDtoDataObj totalConnCurrObj = new OBDtoDataObj();				
				totalConnCurrObj.setValue(totalConnCurrValue);
				if(prevOccurTime != null)
				{
					totalConnCurrObj.setOccurTime(calendar.getTime());
				}
				
				OBDtoDataObj totalConnMaxObj = new OBDtoDataObj();				
				totalConnMaxObj.setValue(totalConnMaxValue);
				if(prevOccurTime != null)
				{
					totalConnMaxObj.setOccurTime(calendar.getTime());
				}
				
				OBDtoDataObj totalConnTotObj = new OBDtoDataObj();				
				totalConnTotObj.setValue(totalConnTotValue);
				if(prevOccurTime != null)
				{
					totalConnTotObj.setOccurTime(calendar.getTime());
				}
				
				OBDtoFaultBpsConnInfo prevInfo = new OBDtoFaultBpsConnInfo();
				prevInfo.setInBpsRawData(inBpsObj);
				prevInfo.setOutBpsRawData(outBpsObj);				
				prevInfo.setTotalBpsRawData(totalBpsObj);
				prevInfo.setTotalPpsRawData(totalPpsObj);
				prevInfo.setTotalConnCurrRawData(totalConnCurrObj);
				prevInfo.setTotalConnMaxRawData(totalConnMaxObj);
				prevInfo.setTotalConnTotRawData(totalConnTotObj);			
				
				RealTimeBpsConnInfo = ServicePerfomanceFacade.getRealTimeBpsConnInfo(adcObject, vsIndex, svcPort, prevInfo);
				log.debug("{}", RealTimeBpsConnInfo);
			}
		} 
		catch(OBException e)
		{
			e.printStackTrace();
			throw e;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new OBException(e.getMessage());
		}
		
		return SUCCESS;
	}
	
	public String checkExportSvcPerfDataExist() throws Exception
	{
		try
		{		
			if(adcObject.getIndex() != null && vsIndex != null && svcPort != null)
			{
			    
			    setSearchTime();
                setCompareSearchTime();
                OBDtoSearch searchOption = new OBDtoSearch();
                searchOption.setFromTime(startTime);
                searchOption.setToTime(endTime);
                searchOption.setPreFromTime(compareStartTime);
                searchOption.setPreToTime(compareEndTime);
                
				if(selectedChartTapMode.equals("bpsConnChartTap"))
				{
					BpsConnInfo = ServicePerfomanceFacade.getBpsConnHistory(adcObject, vsIndex, svcPort, searchOption);					
					
					log.debug("{}", BpsConnInfo);
					
					if(BpsConnInfo != null)
					{
						isSuccessful = true;
					}
					else
					{
						isSuccessful = false;
						message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_EXPORT_DATA_NOT_EXIST);
					}
					BpsConnInfo = null;
				}
				else if (selectedChartTapMode.equals("responsChartTap"))
				{
					ResponseTimeInfo = ServicePerfomanceFacade.getResponseTimeHistory(adcObject, vsIndex, svcPort, searchOption);
					log.debug("{}", ResponseTimeInfo);
					
					if(ResponseTimeInfo != null)
					{
						isSuccessful = true;			
					}
					else
					{
						isSuccessful = false;
						message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_EXPORT_DATA_NOT_EXIST);
					}
					ResponseTimeInfo = null;
				}				
			}
		}
		catch(OBException e)
		{
			throw e;
		}		
		catch(Exception e)
		{
			throw new OBException(e.getMessage());
		}
		
		return SUCCESS;	
	}
	public String downloadSvcPerfInfo() throws Exception 
	{
		try 
		{
			if(adcObject.getIndex() != null && vsIndex != null && svcPort != null)
			{
				
			    setSearchTime();
			    setCompareSearchTime();
				OBDtoSearch searchOption = new OBDtoSearch();
				searchOption.setFromTime(startTime);
				searchOption.setToTime(endTime);
				searchOption.setPreFromTime(compareStartTime);
                searchOption.setPreToTime(compareEndTime);
				BpsConnInfo = ServicePerfomanceFacade.getBpsConnHistory(adcObject, vsIndex, svcPort, searchOption);
				
				if (selectedChartTapMode.equals("responsChartTap"))
				{
				    ResponseTimeInfo = ServicePerfomanceFacade.getResponseTimeHistory(adcObject, vsIndex, svcPort, searchOption);
				}
				log.debug("{}", BpsConnInfo);
				log.debug("{}", ResponseTimeInfo);
				
				CsvMaker csvMaker = new CsvMaker();
//					csvMaker.initWithBpsConnHistory(BpsConnInfo.getBpsConnData(), ResponseTimeInfo.getBpsConnData(), adcObject.getVendor(), preCompare, adcObject.getStatus());
					
				if (selectedChartTapMode.equals("responsChartTap"))
                {
				    csvMaker.initWithBpsConnResponseHistory(BpsConnInfo.getBpsConnData(), ResponseTimeInfo.getBpsConnData(), adcObject.getVendor(), preCompare, adcObject.getStatus());
                }
				else
				{
				    csvMaker.initWithBpsConnHistory(BpsConnInfo.getBpsConnData(), adcObject.getVendor(), preCompare, adcObject.getStatus());
				}
				
				File csv = csvMaker.write();
				if (csv != null)
				{
					setStrutsStream(csv);
				}
				BpsConnInfo = null;
				ResponseTimeInfo = null;
			}
//				else if (selectedChartTapMode.equals("responsChartTap"))
//				{
//				    setSearchTime();
//					OBDtoSearch searchOption = new OBDtoSearch();
//					searchOption.setFromTime(startTime);
//					searchOption.setToTime(endTime);
//					ResponseTimeInfo = ServicePerfomanceFacade.getResponseTimeHistory(adcObject, vsIndex, svcPort, searchOption);
//					log.debug("{}", ResponseTimeInfo);
//					
//					CsvMaker csvMaker = new CsvMaker();
////					csvMaker.initWithResponseTimeHistory(ResponseTimeInfo);
//					File csv = csvMaker.write();
//					if (csv != null)
//					{
//						setStrutsStream(csv);
//					}
//					ResponseTimeInfo = null;
//				}
//			}		
		} 
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage());
		}
		
		return SUCCESS;
	}	
	
	public String checkExportMemberDataExist() throws Exception
	{
		try
		{		
			if(adcObject.getIndex() != null && vsIndex != null && svcPort != null)
			{
			    setSearchTime();
			    setCompareSearchTime();
				OBDtoSearch searchOption = new OBDtoSearch();
				searchOption.setFromTime(startTime);
				searchOption.setToTime(endTime);			
				searchOption.setPreFromTime(compareStartTime);
                searchOption.setPreToTime(compareEndTime);
				MemberHistoryInfo = ServicePerfomanceFacade.getVsMemberHistory(adcObject, searchOption);
				
				log.debug("{}", MemberHistoryInfo);
				
				if(MemberHistoryInfo != null)
				{
					isSuccessful = true;			
				}
				else
				{
					isSuccessful = false;			
					message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_EXPORT_DATA_NOT_EXIST);
				}
				MemberHistoryInfo = null;
			}
		}
		catch(OBException e)
		{
			throw e;
		}		
		catch(Exception e)
		{
			throw new OBException(e.getMessage());
		}
		
		return SUCCESS;	
	}
	public String downloadMemberInfo() throws Exception 
	{
		try 
		{
			if(adcObject.getIndex() != null && vsIndex != null && svcPort != null)
			{				
			    setSearchTime();
			    setCompareSearchTime();
				OBDtoSearch searchOption = new OBDtoSearch();
				searchOption.setFromTime(startTime);
				searchOption.setToTime(endTime);
//				adcObject.setStrIndex(vsIndex);
				searchOption.setPreFromTime(compareStartTime);
                searchOption.setPreToTime(compareEndTime);
				MemberHistoryInfo = ServicePerfomanceFacade.getVsMemberHistory(adcObject, searchOption);
				log.debug("{}", MemberHistoryInfo);
				
				CsvMaker csvMaker = new CsvMaker();
				csvMaker.initWithBpsConnHistory(MemberHistoryInfo.getBpsConnData(), adcObject.getVendor(), preCompare, adcObject.getStatus());
				File csv = csvMaker.write();
				if (csv != null)
				{
					setStrutsStream(csv);
				}
				MemberHistoryInfo = null;
			}		
		} 
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage());
		}
		
		return SUCCESS;
	}
	private void setSearchTime()
    {
        if (null != startTimeL && null != endTimeL)
        {
            startTime = new Date(startTimeL);
            endTime = new Date(endTimeL);   
        } 
        else
        {
            endTime = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endTime);      
            calendar.add(Calendar.HOUR_OF_DAY, -12);            
            startTime = calendar.getTime();         
        }       
//        log.debug("startTime: " + startTime.toString() + ", endTime: " + endTime.toString());
    }
	
	private void setCompareSearchTime()
    {	        
	    if (preCompare.equals(0))      // 전일
        {	        
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);      
            calendar.add(Calendar.DATE, -1);            
            compareStartTime = calendar.getTime();  
            
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(endTime);      
            calendar1.add(Calendar.DATE, -1);            
            compareEndTime = calendar1.getTime(); 
        }
        else if (preCompare.equals(1))  // 전주
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);      
            calendar.add(Calendar.DATE, -7);            
            compareStartTime = calendar.getTime();  
            
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(endTime);      
            calendar1.add(Calendar.DATE, -7);            
            compareEndTime = calendar1.getTime(); 
        }
        else if (preCompare.equals(2))  // 전월
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);      
            calendar.add(Calendar.MONTH, -1);            
            compareStartTime = calendar.getTime();  
            
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(endTime);      
            calendar1.add(Calendar.MONTH, -1);            
            compareEndTime = calendar1.getTime(); 
        }
        else if (preCompare.equals(3))
        {          
//            if (null != selectedDate)
//            {
//                selectedTime = new Date(selectedDate);
//            } 
//            else 
//            {
//                selectedTime = new Date();
//            }
            
           //Wed Jul 01 00:00:00 KST 2015
           Calendar calendar = Calendar.getInstance();   // 2015-06-25 , yy-mm-dd
           calendar.setTime(selectedTime); 
           int year = calendar.get(Calendar.YEAR);
           int month = calendar.get(Calendar.MONTH);
           int day = calendar.get(Calendar.DAY_OF_MONTH);
           Calendar calendar1 = Calendar.getInstance(); 
           calendar1.setTime(endTime);
           
           calendar1.set(year, month, day);
           compareEndTime = calendar1.getTime();  
           long diffTime = endTime.getTime() - startTime.getTime();
           long setTime = compareEndTime.getTime() - diffTime;
           compareStartTime = new Date();
           compareStartTime.setTime(setTime);            
        }
        else
        {    
            compareStartTime = startTime;
            compareEndTime = endTime;
        }       
//        log.debug("compareStartTime: " + compareStartTime.toString() + ", compareEndTime: " + compareEndTime.toString());
    }
	
	public Long getTotalBpsValue()
	{
		return totalBpsValue;
	}

	public void setTotalBpsValue(Long totalBpsValue)
	{
		this.totalBpsValue = totalBpsValue;
	}

	public Long getTotalPpsValue()
	{
		return totalPpsValue;
	}

	public void setTotalPpsValue(Long totalPpsValue)
	{
		this.totalPpsValue = totalPpsValue;
	}

	public Long getTotalConnCurrValue()
	{
		return totalConnCurrValue;
	}

	public void setTotalConnCurrValue(Long totalConnCurrValue)
	{
		this.totalConnCurrValue = totalConnCurrValue;
	}
	
	public Long getTotalConnMaxValue()
	{
		return totalConnMaxValue;
	}

	public void setTotalConnMaxValue(Long totalConnMaxValue)
	{
		this.totalConnMaxValue = totalConnMaxValue;
	}

	public Long getTotalConnTotValue()
	{
		return totalConnTotValue;
	}

	public void setTotalConnTotValue(Long totalConnTotValue)
	{
		this.totalConnTotValue = totalConnTotValue;
	}

	public Long getPrevOccurTime()
	{
		return prevOccurTime;
	}

	public void setPrevOccurTime(Long prevOccurTime)
	{
		this.prevOccurTime = prevOccurTime;
	}

	public OBDtoSearch getSearchOption()
	{
		return searchOption;
	}

	public void setSearchOption(OBDtoSearch searchOption)
	{
		this.searchOption = searchOption;
	}

	public OBDtoOrdering getOrderOption()
	{
		return orderOption;
	}

	public void setOrderOption(OBDtoOrdering orderOption)
	{
		this.orderOption = orderOption;
	}

	public String getVsIndex()
	{
		return vsIndex;
	}

	public void setVsIndex(String vsIndex)
	{
		this.vsIndex = vsIndex;
	}

	public Integer getSvcPort()
	{
		return svcPort;
	}

	public void setSvcPort(Integer svcPort)
	{
		this.svcPort = svcPort;
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

	public String getSearchTimeMode()
	{
		return searchTimeMode;
	}

	public void setSearchTimeMode(String searchTimeMode)
	{
		this.searchTimeMode = searchTimeMode;
	}
	
	public ArrayList<FaultSvcPerfInfoDto> getSvcPerfInfoList()
	{
		return svcPerfInfoList;
	}

	public void setSvcPerfInfoList(ArrayList<FaultSvcPerfInfoDto> svcPerfInfoList)
	{
		this.svcPerfInfoList = svcPerfInfoList;
	}

	public OBDtoADCObject getAdcObject()
	{
		return adcObject;
	}

	public void setAdcObject(OBDtoADCObject adcObject)
	{
		this.adcObject = adcObject;
	}

	public Integer getTotalCount()
	{
		return totalCount;
	}

	public void setTotalCount(Integer totalCount)
	{
		this.totalCount = totalCount;
	}	

	public ArrayList<OBDtoFaultBpsConnInfo> getBpsConnInfoList()
	{
		return BpsConnInfoList;
	}

	public void setBpsConnInfoList(ArrayList<OBDtoFaultBpsConnInfo> BpsConnInfoList)
	{
	    this.BpsConnInfoList = BpsConnInfoList;
	}

	public OBDtoFaultPreBpsConnChart getResponseTimeInfo()
	{
		return ResponseTimeInfo;
	}

	public void setResponseTimeInfo(OBDtoFaultPreBpsConnChart responseTimeInfo)
	{
		ResponseTimeInfo = responseTimeInfo;
	}

	public OBDtoFaultBpsConnInfo getRealTimeBpsConnInfo()
	{
		return RealTimeBpsConnInfo;
	}

	public void setRealTimeBpsConnInfo(OBDtoFaultBpsConnInfo realTimeBpsConnInfo)
	{
		RealTimeBpsConnInfo = realTimeBpsConnInfo;
	}	

	public String getSelectedChartTapMode()
	{
		return selectedChartTapMode;
	}

	public void setSelectedChartTapMode(String selectedChartTapMode)
	{
		this.selectedChartTapMode = selectedChartTapMode;
	}

	public OBDtoFaultBpsConnInfo getPrevInfo()
	{
		return prevInfo;
	}

	public void setPrevInfo(OBDtoFaultBpsConnInfo prevInfo)
	{
		this.prevInfo = prevInfo;
	}	

	public VsTrafficInfoDto getVsTrafficInfo()
	{
		return vsTrafficInfo;
	}

	public void setVsTrafficInfo(VsTrafficInfoDto vsTrafficInfo)
	{
		this.vsTrafficInfo = vsTrafficInfo;
	}

	public Integer getTotalRealCount()
	{
		return totalRealCount;
	}

	public void setTotalRealCount(Integer totalRealCount)
	{
		this.totalRealCount = totalRealCount;
	}
	
	public OBDtoFaultPreBpsConnChart getMemberHistoryInfo()
	{
		return MemberHistoryInfo;
	}

	public void setMemberHistoryInfo(OBDtoFaultPreBpsConnChart memberHistoryInfo)
	{
		MemberHistoryInfo = memberHistoryInfo;
	}	

	public AdcDto getAdc()
	{
		return adc;
	}

	public void setAdc(AdcDto adc)
	{
		this.adc = adc;
	}	

    public Long getInBpsValue()
    {
        return inBpsValue;
    }

    public void setInBpsValue(Long inBpsValue)
    {
        this.inBpsValue = inBpsValue;
    }

    public Long getOutBpsValue()
    {
        return outBpsValue;
    }

    public void setOutBpsValue(Long outBpsValue)
    {
        this.outBpsValue = outBpsValue;
    }

    public Long getStartTimeL()
    {
        return startTimeL;
    }

    public void setStartTimeL(Long startTimeL)
    {
        this.startTimeL = startTimeL;
    }

    public Long getEndTimeL()
    {
        return endTimeL;
    }

    public void setEndTimeL(Long endTimeL)
    {
        this.endTimeL = endTimeL;
    }
    
    public Integer getPreCompare()
    {
        return preCompare;
    }

    public void setPreCompare(Integer preCompare)
    {
        this.preCompare = preCompare;
    }

    public Date getCompareStartTime()
    {
        return compareStartTime;
    }

    public void setCompareStartTime(Date compareStartTime)
    {
        this.compareStartTime = compareStartTime;
    }

    public Date getCompareEndTime()
    {
        return compareEndTime;
    }

    public void setCompareEndTime(Date compareEndTime)
    {
        this.compareEndTime = compareEndTime;
    }

    public Long getSelectedDate()
    {
        return selectedDate;
    }

    public void setSelectedDate(Long selectedDate)
    {
        this.selectedDate = selectedDate;
    }

    public Date getSelectedTime()
    {
        return selectedTime;
    }

    public void setSelectedTime(Date selectedTime)
    {
        this.selectedTime = selectedTime;
    }
 
    public OBDtoFaultPreBpsConnChart getBpsConnInfo()
    {
        return BpsConnInfo;
    }

    public void setBpsConnInfo(OBDtoFaultPreBpsConnChart bpsConnInfo)
    {
        BpsConnInfo = bpsConnInfo;
    }

    public OBDtoFaultPreBpsConnChart getBpsConnAvgMaxInfo()
    {
        return BpsConnAvgMaxInfo;
    }

    public void setBpsConnAvgMaxInfo(OBDtoFaultPreBpsConnChart bpsConnAvgMaxInfo)
    {
        BpsConnAvgMaxInfo = bpsConnAvgMaxInfo;
    }
        
    public OBDtoFaultPreBpsConnChart getMemberAvgMaxInfo()
    {
        return MemberAvgMaxInfo;
    }

    public void setMemberAvgMaxInfo(OBDtoFaultPreBpsConnChart memberAvgMaxInfo)
    {
        MemberAvgMaxInfo = memberAvgMaxInfo;
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
        return "ServicePerfomanceMonitorAction [ServicePerfomanceFacade="
                + ServicePerfomanceFacade + ", adc=" + adc + ", adcObject="
                + adcObject + ", searchOption=" + searchOption
                + ", orderOption=" + orderOption + ", totalCount=" + totalCount
                + ", totalRealCount=" + totalRealCount + ", vsTrafficInfo="
                + vsTrafficInfo + ", vsIndex=" + vsIndex + ", svcPort="
                + svcPort + ", startTime=" + startTime + ", endTime=" + endTime
                + ", startTimeL=" + startTimeL + ", endTimeL=" + endTimeL
                + ", searchTimeMode=" + searchTimeMode
                + ", selectedChartTapMode=" + selectedChartTapMode
                + ", svcPerfInfoList=" + svcPerfInfoList + ", BpsConnInfoList="
                + BpsConnInfoList + ", BpsConnInfo=" + BpsConnInfo
                + ", BpsConnAvgMaxInfo=" + BpsConnAvgMaxInfo
                + ", ResponseTimeInfo=" + ResponseTimeInfo
                + ", MemberHistoryInfo=" + MemberHistoryInfo
                + ", MemberAvgMaxInfo=" + MemberAvgMaxInfo
                + ", RealTimeBpsConnInfo=" + RealTimeBpsConnInfo
                + ", prevInfo=" + prevInfo + ", prevOccurTime=" + prevOccurTime
                + ", inBpsValue=" + inBpsValue + ", outBpsValue=" + outBpsValue
                + ", totalBpsValue=" + totalBpsValue + ", totalPpsValue="
                + totalPpsValue + ", totalConnCurrValue=" + totalConnCurrValue
                + ", totalConnMaxValue=" + totalConnMaxValue
                + ", totalConnTotValue=" + totalConnTotValue + ", preCompare="
                + preCompare + ", compareStartTime=" + compareStartTime
                + ", compareEndTime=" + compareEndTime + ", selectedDate="
                + selectedDate + ", selectedTime=" + selectedTime
                + ", monitoringPeriod=" + monitoringPeriod
                + ", intervalMonitor=" + intervalMonitor + "]";
    }
}