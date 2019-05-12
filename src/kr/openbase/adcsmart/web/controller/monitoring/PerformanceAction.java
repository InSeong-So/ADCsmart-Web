package kr.openbase.adcsmart.web.controller.monitoring;

import java.io.File;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.PerformanceDto;
import kr.openbase.adcsmart.web.facade.dto.SystemStatusDto;
import kr.openbase.adcsmart.web.facade.monitoring.PerformanceFacade;
import kr.openbase.adcsmart.web.util.CsvMaker;
import kr.openbase.adcsmart.web.util.OBDefineWeb;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope(value = "prototype")
public class PerformanceAction extends BaseAction
{
	private transient Logger log = LoggerFactory.getLogger(PerformanceAction.class);	
	
	@Autowired
	private PerformanceFacade performanceFacade;	
	
	private AdcDto adc;
	private String searchTimeMode;
	private Integer hour;
	private Date startTime;
	private Date endTime;
	private PerformanceDto performance;	
//	private List<PerformanceDto> performanceList;
//	private List<PerfSslTransactionInfoDto> perfSslTransactionList; 
//	private List<PerfThroughputInfoDto> perfThroughputList;
//	private List<PerfActiveConnectionInfoDto> perfActiveConnectionList;
//	private List<PerfHttpRequestInfoDto> perfHttpRequestList;	
	private List<SystemStatusDto> systemStatusList;
	
	/**
	 * @return the adc
	 */
	public AdcDto getAdc()
	{
		return adc;
	}
	
	/**
	 * @param adc the adc to set
	 */
	public void setAdc(AdcDto adc)
	{
		this.adc = adc;
	}	
	
	/**
	 * @return the searchTimeMode
	 */
	public String getSearchTimeMode() 
	{
		return searchTimeMode;
	}
	
	/**
	 * @param searchTimeMode the searchTimeMode to set
	 */
	public void setSearchTimeMode(String searchTimeMode)
	{
		this.searchTimeMode = searchTimeMode;
	}
	
	/**
	 * @return the hour
	 */
	public Integer getHour()
	{
		return hour;
	}
	
	/**
	 * @param hour the hour to set
	 */
	public void setHour(Integer hour) 
	{
		this.hour = hour;
	}
	
	/**
	 * @return the startTime
	 */
	public Date getStartTime()
	{
		return startTime;
	}
	
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}
	
	/**
	 * @return the endTime
	 */
	public Date getEndTime() 
	{
		return endTime;
	}
	
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}
	
	/**
	 * @return the performance
	 */
	public PerformanceDto getPerformance()
	{
		return performance;
	}
	
	/**
	 * @param performance the performance to set
	 */
	public void setPerformance(PerformanceDto performance)
	{
		this.performance = performance;
	}
	
	public String getContentDisposition()
	{
		return contentDisposition;
	}
	
	public void setContentDisposition(String contentDisposition)
	{
		this.contentDisposition = contentDisposition;
	}
	
	public InputStream getInputStream() 
	{
		return inputStream;
	}
	
	public void setInputStream(InputStream inputStream) 
	{
		this.inputStream = inputStream;
	}
	
	public long getContentLength()
	{
		return contentLength;
	}
	
	public void setContentLength(long contentLength)
	{
		this.contentLength = contentLength;
	}
//	public List<PerformanceDto> getPerformanceList()
//	{
//		return performanceList;
//	}
//	public void setPerformanceList(List<PerformanceDto> performanceList)
//	{
//		this.performanceList = performanceList;
//	}
//	public List<PerfSslTransactionInfoDto> getPerfSslTransactionList()
//	{
//		return perfSslTransactionList;
//	}
//	public void setPerfSslTransactionList(List<PerfSslTransactionInfoDto> perfSslTransactionList)
//	{
//		this.perfSslTransactionList = perfSslTransactionList;
//	}	
//	public List<PerfThroughputInfoDto> getPerfThroughputList()
//	{
//		return perfThroughputList;
//	}
//	public void setPerfThroughputList(List<PerfThroughputInfoDto> perfThroughputList)
//	{
//		this.perfThroughputList = perfThroughputList;
//	}
//	public List<PerfActiveConnectionInfoDto> getPerfActiveConnectionList()
//	{
//		return perfActiveConnectionList;
//	}
//	public void setPerfActiveConnectionList(List<PerfActiveConnectionInfoDto> perfActiveConnectionList)
//	{
//		this.perfActiveConnectionList = perfActiveConnectionList;
//	}
//	public List<PerfHttpRequestInfoDto> getPerfHttpRequestList()
//	{
//		return perfHttpRequestList;
//	}
//	public void setPerfHttpRequestList(List<PerfHttpRequestInfoDto> perfHttpRequestList)
//	{
//		this.perfHttpRequestList = perfHttpRequestList;
//	}	
	/**
	 * @return the systemStatusList
	 */
	public List<SystemStatusDto> getSystemStatusList() 
	{
		return systemStatusList;
	}
	
	/**
	 * @param systemStatusList the systemStatusList to set
	 */
	public void setSystemStatusList(List<SystemStatusDto> systemStatusList)
	{
		this.systemStatusList = systemStatusList;
	}
	
	public String loadPerformanceContent() throws Exception
	{
		return SUCCESS;
	}
	
	public String loadPerformanceInfo() throws Exception
	{
		try
		{
			if (null != adc && null != adc.getIndex() && !StringUtils.isEmpty(adc.getType()))
			{
				setSearchTimeInterval();
				performance = performanceFacade.getPerformance(adc, startTime, endTime);				
				log.debug("{}", performance);
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
	
	public String loadSystemPerformanceContent() throws Exception
	{
		return SUCCESS;
	}	
	
	public String loadSystemPerformanceInfo() throws Exception
	{
		try
		{
			if (null != adc && null != adc.getIndex() && !StringUtils.isEmpty(adc.getType()))
			{
				setSearchTimeInterval();
				performance = performanceFacade.getPerformance(adc, startTime, endTime);
				systemStatusList = performanceFacade.getSystemStatus(adc, startTime, endTime);
				log.debug("{}", performance);
				log.debug("{}", systemStatusList);
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
//	public String downloadPerformance() throws Exception
//	{
//		try
//	{
////			log.debug("adc:{}", new Object[]{adc});
//			setSearchTimeInterval();
//			performance = performanceFacade.getPerformance(adc, startTime, endTime);
//			log.debug("{}", performance);
//			
//			CsvMaker csvMaker = new CsvMaker();
//			csvMaker.initWithPerformanceList(performance);
//			File csv = csvMaker.write();
//			if(csv != null)
//				setStrutsStream(csv);
//		} 
//		catch (Exception e)
//		{
//			e.printStackTrace();
//			throw e;
//		}
//		
//		return SUCCESS;
//	}	

	public String checkExportPerformDataExist() throws Exception
	{
		try
		{		
			setSearchTimeInterval();
			performance = performanceFacade.getPerformance(adc, startTime, endTime);
			systemStatusList = performanceFacade.getSystemStatus(adc, startTime, endTime);
			
			if(null != performance && null != systemStatusList && systemStatusList.size() >0)
			{
				isSuccessful = true;
			}
			else
			{
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_EXPORT_DATA_NOT_EXIST);
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
	
	public String downloadPerformanceF5() throws Exception 
	{
		try
		{		
			setSearchTimeInterval();
			performance = performanceFacade.getPerformance(adc, startTime, endTime);
			systemStatusList = performanceFacade.getSystemStatus(adc, startTime, endTime);
			
			CsvMaker csvMaker = new CsvMaker();
			csvMaker.initWithSystemPerformance(systemStatusList,performance);
							
			File csv = csvMaker.write();
			if(csv != null)
			{
				setStrutsStream(csv);
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
	
	public String downloadPerformanceDefault() throws Exception 
	{
		try
		{		
			setSearchTimeInterval();
			performance = performanceFacade.getPerformance(adc, startTime, endTime);
			systemStatusList = performanceFacade.getSystemStatus(adc, startTime, endTime);
			
			CsvMaker csvMaker = new CsvMaker();
			csvMaker.initWithExportTo(systemStatusList,performance);
			
			File csv = csvMaker.write();
			if(csv != null)
			{
				setStrutsStream(csv);
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
	
	/*public String downloadPerformancePAS() throws Exception 
	{
		try 
		{
			setSearchTimeInterval();
			performance = performanceFacade.getPerformance(adc, startTime, endTime);
			systemStatusList = performanceFacade.getSystemStatus(adc, startTime, endTime);
			
			CsvMaker csvMaker = new CsvMaker();
			csvMaker.initWithExportTo(systemStatusList,performance);
			File csv = csvMaker.write();
			if (csv != null)
			{
				setStrutsStream(csv);
			}							
		} 
		catch (Exception e) 
		{
			throw e;
		}
		
		return SUCCESS;
	}*/
	
/*	public String downloadPerformancePASK() throws Exception 
	{
		try 
		{
			setSearchTimeInterval();
			//performance = performanceFacade.getPerformance(adc, startTime, endTime);
			systemStatusList = performanceFacade.getSystemStatus(adc, startTime, endTime);
			
			CsvMaker csvMaker = new CsvMaker();
			csvMaker.initWithSystemStatusList(systemStatusList);
			File csv = csvMaker.write();
			if (csv != null)
			{
				setStrutsStream(csv);
			}							
		} 
		catch (Exception e) 
		{
			throw e;
		}
		
		return SUCCESS;
	}
	*/
	private void setSearchTimeInterval()
	{
		if (!StringUtils.isEmpty(searchTimeMode) && searchTimeMode.equals(OBDefineWeb.SEARCH_TIME_PERIOD_MODE) && null != startTime && null != endTime)
		{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endTime);
			calendar.add(Calendar.HOUR_OF_DAY, 24);
			calendar.add(Calendar.MILLISECOND, -1);			
			endTime =calendar.getTime();
			
			Date curTime = new Date();
			
			if(endTime.getTime() > curTime.getTime())
				
			{
				endTime = curTime;
			}			
		} 
		else
		{
			endTime = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endTime);
			if (null != hour) 
			{
				calendar.add(Calendar.HOUR_OF_DAY, -hour);
			} 
			else 
			{
				calendar.add(Calendar.HOUR_OF_DAY, -1);
			}
			startTime = calendar.getTime();			
		}
		
		log.debug("startTime: " + startTime.toString() + ", endTime: " + endTime.toString());
	}
}
