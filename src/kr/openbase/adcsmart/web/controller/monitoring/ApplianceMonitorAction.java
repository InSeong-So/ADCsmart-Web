package kr.openbase.adcsmart.web.controller.monitoring;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvAdditional;
import kr.openbase.adcsmart.service.dto.fault.OBDtoAdcCurrentSession;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCpuDataObj;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCpuHistory;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultDataObj;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultHWStatus;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBEnvManagementImpl;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcMonCpuDataObj;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.monitoring.ApplianceMonitorFacade;
import kr.openbase.adcsmart.web.util.CsvMaker;
import kr.openbase.adcsmart.web.util.NumberUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope(value = "prototype")
public class ApplianceMonitorAction extends BaseAction
{
	private transient Logger log = LoggerFactory.getLogger(ApplianceMonitorAction.class);
	
	@Autowired
	private ApplianceMonitorFacade applianceMonitorFacade;
	
	private OBDtoADCObject adcObject;
	private OBDtoSearch searchOption;	
	private Date startTime;
	private Date endTime;
	private Long startTimeL;
    private Long endTimeL;
	private OBDtoAdcInfo adcInfo;
	private OBDtoFaultHWStatus faultHWStatus;
	private OBDtoAdcCurrentSession sessionHistory;
	private OBDtoFaultDataObj bpsHistory;
	private OBDtoAdcMonCpuDataObj cpuHistroy;           // F5 CPU, All CPU
	private OBDtoFaultCpuDataObj cpuSPHistory;         // Alteon CPU
	private OBDtoAdcMonCpuDataObj cpuAllHistroy;        // Alteon All CPU
	private OBDtoFaultCpuHistory cpuSpConns;           // Alteon Connection
	private OBDtoFaultDataObj memHistory;
	private OBDtoFaultDataObj pktErrHistory;
	private OBDtoFaultDataObj pktDropHistory;
	private OBDtoFaultDataObj httpRequestHistory;
	private OBDtoFaultDataObj sslTransactionHistory;
	private Integer cpuNum;
	private Integer monitoringPeriod;
    private Integer intervalMonitor;
    private String selectedChartTapMode;
    
    public ApplianceMonitorAction() 
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
	
	public String loadApplianceMonitorContent() throws OBException
	{
		try
		{		    
			adcInfo = applianceMonitorFacade.getAdcInfo(adcObject.getIndex());
			intervalMonitor = monitoringPeriod;
			log.debug("{}", adcInfo);								
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
	
	public String loadApplianceAllChartContent() throws OBException
	{			    
		return SUCCESS;
	}
	
	public String loadApplianceDetailChartContent() throws OBException
	{		
		return SUCCESS;
	}
	
	public String loadIntervalInfo() throws OBException
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
        
        return SUCCESS;

	}
	public String loadApplianceMapContent() throws OBException
	{
		try
		{
		    adcInfo = applianceMonitorFacade.getAdcInfo(adcObject.getIndex());        
			faultHWStatus = applianceMonitorFacade.getHWStatus(adcObject);
			intervalMonitor = monitoringPeriod;
			log.debug("{}", faultHWStatus);								
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
	
	public String checkExportDataExist() throws OBException
    {
        try
        {
            if (null != adcObject && null != adcObject.getIndex())
            {
                setSearchTime();
                OBDtoSearch searchOption = new OBDtoSearch();
                searchOption.setFromTime(startTime);
                searchOption.setToTime(endTime);
                adcInfo = applianceMonitorFacade.getAdcInfo(adcObject.getIndex());
                
                if(selectedChartTapMode.equals("csChartTap"))
                {                
                    sessionHistory = applianceMonitorFacade.getSessionHistoryInfo(adcObject, searchOption);
                }
                else if(selectedChartTapMode.equals("throughputChartTap"))
                {
                    bpsHistory = applianceMonitorFacade.getBpsHistoryInfo(adcObject, searchOption);
                }
                else if(selectedChartTapMode.equals("cpuChartTap"))
                {
                    adcInfo = new OBAdcManagementImpl().getAdcInfo(adcObject.getIndex());
                    if(adcInfo.getAdcType() == 2)
                    {
                        cpuSPHistory = applianceMonitorFacade.getCpuSPHistroyInfo(adcObject, searchOption, cpuNum);
                    }
                    else
                    {
                        cpuHistroy = applianceMonitorFacade.getCpuHistroyInfo(adcObject, searchOption);
                    }
                }
                else if(selectedChartTapMode.equals("memoryChartTap"))
                {
                    memHistory = applianceMonitorFacade.getMemHistoryInfo(adcObject, searchOption);   
                }
                else if(selectedChartTapMode.equals("sslConnChartTap"))
                {
                    sslTransactionHistory = applianceMonitorFacade.getSSLTransactionHistoryInfo(adcObject, searchOption);
                }
                else if(selectedChartTapMode.equals("httpChartTap"))
                {
                    httpRequestHistory = applianceMonitorFacade.getHTTPRequestHistoryInfo(adcObject, searchOption);
                }
                else if(selectedChartTapMode.equals("errpChartTap"))
                {
                    pktErrHistory = applianceMonitorFacade.getPktErrHistoryInfo(adcObject, searchOption);      
                }
                else if(selectedChartTapMode.equals("dropChartTap"))
                {
                    pktDropHistory = applianceMonitorFacade.getPktDropHistoryInfo(adcObject, searchOption);  
                }
                log.debug("{}", sessionHistory);
            }
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
	
	public String download() throws Exception
    {
        try
        {
            setSearchTime();  
            OBDtoSearch searchOption = new OBDtoSearch();
            searchOption.setFromTime(startTime);
            searchOption.setToTime(endTime);
            
            CsvMaker csvMaker = new CsvMaker();
            
            if(selectedChartTapMode.equals("csChartTap"))
            {                
                sessionHistory = applianceMonitorFacade.getSessionHistoryInfo(adcObject, searchOption);
                
                csvMaker.initWithConcurrentSessionsContents(sessionHistory.getHistory());
                File csv = csvMaker.write();
                if (csv != null)
                {
                    log.debug("{}",sessionHistory);
                    setStrutsStream(csv);   
                }
                else
                {
                    log.debug("{}",sessionHistory);
                }
                sessionHistory = null;
            }
            else if(selectedChartTapMode.equals("throughputChartTap"))
            {
                bpsHistory = applianceMonitorFacade.getBpsHistoryInfo(adcObject, searchOption);
                
                csvMaker.initWithBpsContents(bpsHistory.getHistory());
                File csv = csvMaker.write();
                if (csv != null)
                {
                    log.debug("{}",bpsHistory);
                    setStrutsStream(csv);   
                }
                else
                {
                    log.debug("{}",bpsHistory);
                }
                bpsHistory = null;
            }
            else if(selectedChartTapMode.equals("cpuChartTap"))
            {
                adcInfo = new OBAdcManagementImpl().getAdcInfo(adcObject.getIndex());
                
                if(adcInfo.getAdcType() == 2)
                {
                    cpuSPHistory = applianceMonitorFacade.getCpuSPHistroyInfo(adcObject, searchOption, cpuNum);
                    csvMaker.initWithSPCpuHistoryContents(cpuSPHistory.getHistory());
                    File csv = csvMaker.write();
                    if (csv != null)
                    {
                        log.debug("{}",cpuSPHistory);
                        setStrutsStream(csv);   
                    }
                    else
                    {
                        log.debug("{}",cpuSPHistory);
                    }
                    cpuSPHistory = null;
//                    cpuSpConns = applianceMonitorFacade.getCpuSpConnectionInfo(adcObject, searchOption);
//                    csvMaker.initWithSPCpuContents(cpuSpConns);
//                    File csv = csvMaker.write();
//                    if (csv != null)
//                    {
//                        log.debug("{}",cpuSpConns);
//                        setStrutsStream(csv);   
//                    }
//                    else
//                    {
//                        log.debug("{}",cpuSpConns);
//                    }
//                    cpuSpConns = null;
                }
                else
                {                
                    cpuHistroy = applianceMonitorFacade.getCpuHistroyInfo(adcObject, searchOption);
                    csvMaker.initWithCpuContents(cpuHistroy.getHistory());
                    File csv = csvMaker.write();
                    if (csv != null)
                    {
                        log.debug("{}",cpuHistroy);
                        setStrutsStream(csv);   
                    }
                    else
                    {
                        log.debug("{}",cpuHistroy);
                    }
                    cpuHistroy = null;
                }
            }
            else if(selectedChartTapMode.equals("memoryChartTap"))
            {
                memHistory = applianceMonitorFacade.getMemHistoryInfo(adcObject, searchOption);   
                csvMaker.initWithMemoryContents(memHistory.getHistory());
                File csv = csvMaker.write();
                if(csv != null)
                {
                    log.debug("{}", memHistory);
                    setStrutsStream(csv);
                }
                else
                {
                    log.debug("{}", memHistory);
                }
                memHistory = null;
            }
            else if(selectedChartTapMode.equals("errpChartTap"))
            {
                pktErrHistory = applianceMonitorFacade.getPktErrHistoryInfo(adcObject, searchOption);  
                csvMaker.initWithPktErrorContents(pktErrHistory.getHistory());
                File csv = csvMaker.write();
                if(csv != null)
                {
                    log.debug("{}", pktErrHistory);
                    setStrutsStream(csv);
                }
                else
                {
                    log.debug("{}", pktErrHistory);
                }
                pktErrHistory = null;
            }
            else if(selectedChartTapMode.equals("dropChartTap"))
            {
                pktDropHistory = applianceMonitorFacade.getPktDropHistoryInfo(adcObject, searchOption);  
                csvMaker.initWithPktErrorContents(pktDropHistory.getHistory());
                File csv = csvMaker.write();
                if(csv != null)
                {
                    log.debug("{}", pktDropHistory);
                    setStrutsStream(csv);
                }
                else
                {
                    log.debug("{}", pktDropHistory);
                }
                pktErrHistory = null;
            }
            else if(selectedChartTapMode.equals("sslConnChartTap"))
            {
                sslTransactionHistory = applianceMonitorFacade.getSSLTransactionHistoryInfo(adcObject, searchOption);
                csvMaker.initWithSslTransactionContents(sslTransactionHistory.getHistory());
                File csv = csvMaker.write();
                if(csv != null)
                {
                    log.debug("{}", sslTransactionHistory);
                    setStrutsStream(csv);
                }
                else
                {
                    log.debug("{}", sslTransactionHistory);
                }
                sslTransactionHistory = null;
               
            }
            else if(selectedChartTapMode.equals("httpChartTap"))
            {
                httpRequestHistory = applianceMonitorFacade.getHTTPRequestHistoryInfo(adcObject, searchOption);
                csvMaker.initWithHttpRequestContents(httpRequestHistory.getHistory());
                File csv = csvMaker.write();
                if(csv != null)
                {
                    log.debug("{}", httpRequestHistory);
                    setStrutsStream(csv);
                }
                else
                {
                    log.debug("{}", httpRequestHistory);
                }
                httpRequestHistory = null;
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
    
	public String loadSessionHistoryInfo() throws OBException
	{
		try
		{
			if (null != adcObject && null != adcObject.getIndex())
			{
			    setSearchTime();
				OBDtoSearch searchOption = new OBDtoSearch();
				searchOption.setFromTime(startTime);
				searchOption.setToTime(endTime);
				adcInfo = applianceMonitorFacade.getAdcInfo(adcObject.getIndex());
				sessionHistory = applianceMonitorFacade.getSessionHistoryInfo(adcObject, searchOption);
				log.debug("{}", sessionHistory);
			}
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
	
	public String loadBpsHistoryInfo() throws OBException
	{
		try
		{
			if (null != adcObject && null != adcObject.getIndex())
			{
			    setSearchTime();
				OBDtoSearch BpsHistorysearchOption = new OBDtoSearch();
				BpsHistorysearchOption.setFromTime(startTime);
				BpsHistorysearchOption.setToTime(endTime);
				bpsHistory = applianceMonitorFacade.getBpsHistoryInfo(adcObject, BpsHistorysearchOption);
				log.debug("{}", bpsHistory);
			}		
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
	// 전체 CPU
	public String loadAdcCpuHistroyInfo() throws OBException
    {
        try
        {
            if (null != adcObject && null != adcObject.getIndex())
            {
                setSearchTime();
                OBDtoSearch CpuHistroysearchOption = new OBDtoSearch();
                CpuHistroysearchOption.setFromTime(startTime);
                CpuHistroysearchOption.setToTime(endTime);
                cpuAllHistroy = applianceMonitorFacade.getAdcCpuHistroyInfo(adcObject, CpuHistroysearchOption);
                log.debug("{}", cpuAllHistroy);
            }           
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
	
	// F5 CPU
	public String loadCpuHistroyInfo() throws OBException
	{
		try
		{
			if (null != adcObject && null != adcObject.getIndex())
			{
			    setSearchTime();
				OBDtoSearch CpuHistroysearchOption = new OBDtoSearch();
				CpuHistroysearchOption.setFromTime(startTime);
				CpuHistroysearchOption.setToTime(endTime);
				cpuHistroy = applianceMonitorFacade.getCpuHistroyInfo(adcObject, CpuHistroysearchOption);
				log.debug("{}", cpuHistroy);
			}			
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
	
	// Alteon CPU
	public String loadCpuSPHistroyInfo() throws OBException
	{
		try
		{
			if (null != adcObject && null != adcObject.getIndex())
			{
			    setSearchTime();
				OBDtoSearch CpuHistroysearchOption = new OBDtoSearch();
				CpuHistroysearchOption.setFromTime(startTime);
				CpuHistroysearchOption.setToTime(endTime);
				
//				OBDtoCpuMemStatus adcSpSession = new OBDtoCpuMemStatus();						
				cpuSPHistory = applianceMonitorFacade.getCpuSPHistroyInfo(adcObject, CpuHistroysearchOption, cpuNum);				
//				cpuSPHistroy.setSpSessionMax(adcSpSession.getSpSessionMax().longValue());
				adcInfo = applianceMonitorFacade.getAdcInfo(adcObject.getIndex());
				//TODO 맥스값 수정해야 함
				cpuSPHistory.setSpSessionMax(adcInfo.getSpSessionMax().longValue());
				cpuSPHistory.setSpSessionMaxUnit(NumberUtil.toStringWithUnit(adcInfo.getSpSessionMax().longValue(), ""));
				log.debug("{}", cpuSPHistory);
			}			
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
	
	// SP 별  Connection 정보
	public String loadCpuSpConnectionInfo() throws OBException
	{
		try
		{
			if (null != adcObject && null != adcObject.getIndex())
			{
			    setSearchTime();
				OBDtoSearch CpuHistroysearchOption = new OBDtoSearch();
				CpuHistroysearchOption.setFromTime(startTime);
				CpuHistroysearchOption.setToTime(endTime);
				cpuSpConns = applianceMonitorFacade.getCpuSpConnectionInfo(adcObject, CpuHistroysearchOption);
				if(null != cpuSpConns)
				{
				    adcInfo = applianceMonitorFacade.getAdcInfo(adcObject.getIndex());
				    
				    // SP CPU Usage Minimum
//				    cpuSpConns.setSpUsageMin(spUsageMin);				    
				    // SP CPU Session Minimum
//				    cpuSpConns.setSpSessionMin(spSessionMin);
				    //TODO 맥스값 수정해야 함
				    cpuSpConns.setSpSessionMax(adcInfo.getSpSessionMax().longValue());
				    cpuSpConns.setSpSessionMaxUnit(NumberUtil.toStringWithUnit(adcInfo.getSpSessionMax().longValue(), ""));
				    log.debug("{}", cpuSpConns);
				}
			}			
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
	
	public String loadMemHistoryInfo() throws OBException
	{
		try
		{
			if (null != adcObject && null != adcObject.getIndex())
			{
			    setSearchTime();
				OBDtoSearch MemHistorysearchOption = new OBDtoSearch();
				MemHistorysearchOption.setFromTime(startTime);
				MemHistorysearchOption.setToTime(endTime);
				memHistory = applianceMonitorFacade.getMemHistoryInfo(adcObject, MemHistorysearchOption);				
				log.debug("{}", memHistory);
			}			
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
	
	public String loadPktErrHistoryInfo() throws OBException
	{
		try
		{
			if (null != adcObject && null != adcObject.getIndex())
			{
			    setSearchTime();
				OBDtoSearch PktErrHistorysearchOption = new OBDtoSearch();
				PktErrHistorysearchOption.setFromTime(startTime);
				PktErrHistorysearchOption.setToTime(endTime);
				pktErrHistory = applianceMonitorFacade.getPktErrHistoryInfo(adcObject, PktErrHistorysearchOption);				
				log.debug("{}", pktErrHistory);
			}		
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
	
	public String loadPktDropHistoryInfo() throws OBException
	{
		try
		{
			if (null != adcObject && null != adcObject.getIndex())
			{
			    setSearchTime();
				OBDtoSearch PktDropsearchOption = new OBDtoSearch();
				PktDropsearchOption.setFromTime(startTime);
				PktDropsearchOption.setToTime(endTime);
				pktDropHistory = applianceMonitorFacade.getPktDropHistoryInfo(adcObject, PktDropsearchOption);				
				log.debug("{}", pktDropHistory);
			}
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
	
	public String loadHTTPRequestHistoryInfo() throws OBException
	{
		try
		{
			if (null != adcObject && null != adcObject.getIndex())
			{
			    setSearchTime();
				OBDtoSearch httpRequestsearchOption = new OBDtoSearch();
				httpRequestsearchOption.setFromTime(startTime);
				httpRequestsearchOption.setToTime(endTime);
				httpRequestHistory = applianceMonitorFacade.getHTTPRequestHistoryInfo(adcObject, httpRequestsearchOption);				
				log.debug("{}", httpRequestHistory);
			}
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
	
	public String loadSSLTransactionHistoryInfo() throws OBException
	{
		try
		{
			if (null != adcObject && null != adcObject.getIndex())
			{
			    setSearchTime();
				OBDtoSearch SSLTransactionsearchOption = new OBDtoSearch();
				SSLTransactionsearchOption.setFromTime(startTime);
				SSLTransactionsearchOption.setToTime(endTime);
				sslTransactionHistory = applianceMonitorFacade.getSSLTransactionHistoryInfo(adcObject, SSLTransactionsearchOption);				
				log.debug("{}", sslTransactionHistory);
			}
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
        log.debug("startTime: " + startTime.toString() + ", endTime: " + endTime.toString());
    }

	public OBDtoADCObject getAdcObject()
	{
		return adcObject;
	}

	public void setAdcObject(OBDtoADCObject adcObject)
	{
		this.adcObject = adcObject;
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

	public OBDtoFaultHWStatus getFaultHWStatus()
	{
		return faultHWStatus;
	}

	public void setFaultHWStatus(OBDtoFaultHWStatus faultHWStatus)
	{
		this.faultHWStatus = faultHWStatus;
	}	

	public OBDtoSearch getSearchOption()
	{
		return searchOption;
	}

	public void setSearchOption(OBDtoSearch searchOption)
	{
		this.searchOption = searchOption;
	}

	public OBDtoAdcCurrentSession getSessionHistory()
	{
		return sessionHistory;
	}

	public void setSessionHistory(OBDtoAdcCurrentSession sessionHistory)
	{
		this.sessionHistory = sessionHistory;
	}

	public OBDtoFaultDataObj getBpsHistory()
	{
		return bpsHistory;
	}

	public void setBpsHistory(OBDtoFaultDataObj bpsHistory)
	{
		this.bpsHistory = bpsHistory;
	}

	public OBDtoAdcMonCpuDataObj getCpuHistroy()
	{
		return cpuHistroy;
	}

	public void setCpuHistroy(OBDtoAdcMonCpuDataObj cpuHistroy)
	{
		this.cpuHistroy = cpuHistroy;
	}

	public OBDtoFaultDataObj getMemHistory()
	{
		return memHistory;
	}

	public void setMemHistory(OBDtoFaultDataObj memHistory)
	{
		this.memHistory = memHistory;
	}

	public OBDtoFaultDataObj getPktErrHistory()
	{
		return pktErrHistory;
	}

	public void setPktErrHistory(OBDtoFaultDataObj pktErrHistory)
	{
		this.pktErrHistory = pktErrHistory;
	}

	public OBDtoFaultDataObj getPktDropHistory()
	{
		return pktDropHistory;
	}

	public void setPktDropHistory(OBDtoFaultDataObj pktDropHistory)
	{
		this.pktDropHistory = pktDropHistory;
	}
	
	public OBDtoFaultDataObj getHttpRequestHistory()
	{
		return httpRequestHistory;
	}

	public void setHttpRequestHistory(OBDtoFaultDataObj httpRequestHistory)
	{
		this.httpRequestHistory = httpRequestHistory;
	}

	public OBDtoFaultDataObj getSslTransactionHistory()
	{
		return sslTransactionHistory;
	}

	public void setSslTransactionHistory(OBDtoFaultDataObj sslTransactionHistory)
	{
		this.sslTransactionHistory = sslTransactionHistory;
	}

	public OBDtoAdcInfo getAdcInfo()
	{
		return adcInfo;
	}

	public void setAdcInfo(OBDtoAdcInfo adcInfo)
	{
		this.adcInfo = adcInfo;
	}		
	
	public OBDtoFaultCpuHistory getCpuSpConns()
	{
		return cpuSpConns;
	}

	public void setCpuSpConns(OBDtoFaultCpuHistory cpuSpConns)
	{
		this.cpuSpConns = cpuSpConns;
	}

	public OBDtoFaultCpuDataObj getCpuSPHistory()
	{
		return cpuSPHistory;
	}

	public void setCpuSPHistory(OBDtoFaultCpuDataObj cpuSPHistory)
	{
		this.cpuSPHistory = cpuSPHistory;
	}
	
	public Integer getCpuNum() 
	{
		return cpuNum;
	}

	public void setCpuNum(Integer cpuNum) 
	{
		this.cpuNum = cpuNum;
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

    public String getSelectedChartTapMode()
    {
        return selectedChartTapMode;
    }

    public void setSelectedChartTapMode(String selectedChartTapMode)
    {
        this.selectedChartTapMode = selectedChartTapMode;
    }
    
    public OBDtoAdcMonCpuDataObj getCpuAllHistroy()
    {
        return cpuAllHistroy;
    }

    public void setCpuAllHistroy(OBDtoAdcMonCpuDataObj cpuAllHistroy)
    {
        this.cpuAllHistroy = cpuAllHistroy;
    }

    @Override
    public String toString()
    {
        return "ApplianceMonitorAction [applianceMonitorFacade="
                + applianceMonitorFacade + ", adcObject=" + adcObject
                + ", searchOption=" + searchOption + ", startTime=" + startTime
                + ", endTime=" + endTime + ", startTimeL=" + startTimeL
                + ", endTimeL=" + endTimeL + ", adcInfo=" + adcInfo
                + ", faultHWStatus=" + faultHWStatus + ", sessionHistory="
                + sessionHistory + ", bpsHistory=" + bpsHistory
                + ", cpuHistroy=" + cpuHistroy + ", cpuSPHistory="
                + cpuSPHistory + ", cpuAllHistroy=" + cpuAllHistroy
                + ", cpuSpConns=" + cpuSpConns + ", memHistory=" + memHistory
                + ", pktErrHistory=" + pktErrHistory + ", pktDropHistory="
                + pktDropHistory + ", httpRequestHistory=" + httpRequestHistory
                + ", sslTransactionHistory=" + sslTransactionHistory
                + ", cpuNum=" + cpuNum + ", monitoringPeriod="
                + monitoringPeriod + ", intervalMonitor=" + intervalMonitor
                + ", selectedChartTapMode=" + selectedChartTapMode + "]";
    }	
}