package kr.openbase.adcsmart.web.controller.monitoring;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.io.File;

import kr.openbase.adcsmart.service.dto.OBDtoMultiDataObj;
import kr.openbase.adcsmart.service.impl.dto.OBDtoConnectionDataObj;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.AdcPortStatusDto;
import kr.openbase.adcsmart.web.facade.monitoring.StatisticsFacade;
import kr.openbase.adcsmart.web.util.CsvMaker;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope(value = "prototype")
public class StatisticsAction extends BaseAction 
{	
	private transient Logger log = LoggerFactory.getLogger(StatisticsAction.class);
	
	@Autowired
	private StatisticsFacade statisticsFacade;	
	
	private List<AdcPortStatusDto> adcPortStatusList;
	private List<AdcPortStatusDto> graph;
	private AdcDto adc;
	private String selectedTraffic;
	private List<Long> interfaceIndics;
	private List<OBDtoMultiDataObj> graphDataList;
	private ArrayList<String> interfaceNameList;
	private String interfaceName;	
	private List<OBDtoMultiDataObj> statistic5data;
	private Date startTime;
	private Date endTime;	
	private Long startTimeL;
    private Long endTimeL;
	private Integer orderDir; 								// 오른차순 = 1, 내림차순 = 2
	private Integer orderType; 								// port = 3
	
	
	public String loadStatisticsContent() throws Exception
	{		
		try
		{
			if (adc != null && adc.getIndex() != null)
			{
			    setSearchTime();
				adcPortStatusList = statisticsFacade.getAdcPortStatusLog(adc, orderType, orderDir);
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
	
	public String loadInterFaceGraphData() throws Exception
	{
//		Date firstWeb = new Date();
//		log.debug("firstWeb: " + firstWeb.toString() + ", endTime: " + firstWeb.toString());		
		try
		{
			if(adc != null && adc.getIndex() != null)
			{
			    setSearchTime();				
				statistic5data = statisticsFacade.getStatistics5GraphInfo(adc, interfaceNameList, selectedTraffic, startTime, endTime);
				//graphDataList = statisticsFacade.getGraphData(adc, interfaceNameList, selectedTraffic, startTime, endTime);
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
		
		log.debug("{}",statistic5data);
//		Date lastWeb = new Date();
//		log.debug("lastWeb: " + lastWeb.toString() + ", endTime: " + lastWeb.toString());
		
		return SUCCESS;
		
	}
	public String checkExportStatisticsDataExist() throws Exception
	{
		try
		{
		    setSearchTime();
			statistic5data = statisticsFacade.getStatistics5GraphInfo(adc, interfaceNameList, selectedTraffic, startTime, endTime);			

			if(statistic5data==null || statistic5data.isEmpty())
			{
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_EXPORT_DATA_NOT_EXIST);
			}
			else
			{
				isSuccessful = true;					
			}
			statistic5data = null;
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
	public String downloadStatistics() throws Exception
	{
		try
		{
		    setSearchTime();
			statistic5data = statisticsFacade.getStatistics5GraphInfo(adc, interfaceNameList, selectedTraffic, startTime, endTime);	
			List<OBDtoConnectionDataObj> connectionData = new ArrayList<OBDtoConnectionDataObj>();
			connectionData = statistic5data.get(0).getData();		
			CsvMaker csvMaker = new CsvMaker();
			csvMaker.initWithInterfaceContents(connectionData, adc, interfaceNameList);
			File csv = csvMaker.write();
			if (csv != null)
			{
				log.debug("{}",statistic5data);
				setStrutsStream(csv);	
			}
			else
			{
				log.debug("{}",statistic5data);
			}
			statistic5data = null;
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
        log.debug("startTime: " + startTime.toString() + ", endTime: " + endTime.toString());
    }

    public Logger getLog()
    {
        return log;
    }

    public void setLog(Logger log)
    {
        this.log = log;
    }

    public List<AdcPortStatusDto> getAdcPortStatusList()
    {
        return adcPortStatusList;
    }

    public void setAdcPortStatusList(List<AdcPortStatusDto> adcPortStatusList)
    {
        this.adcPortStatusList = adcPortStatusList;
    }

    public List<AdcPortStatusDto> getGraph()
    {
        return graph;
    }

    public void setGraph(List<AdcPortStatusDto> graph)
    {
        this.graph = graph;
    }

    public AdcDto getAdc()
    {
        return adc;
    }

    public void setAdc(AdcDto adc)
    {
        this.adc = adc;
    }

    public String getSelectedTraffic()
    {
        return selectedTraffic;
    }

    public void setSelectedTraffic(String selectedTraffic)
    {
        this.selectedTraffic = selectedTraffic;
    }

    public List<Long> getInterfaceIndics()
    {
        return interfaceIndics;
    }

    public void setInterfaceIndics(List<Long> interfaceIndics)
    {
        this.interfaceIndics = interfaceIndics;
    }

    public List<OBDtoMultiDataObj> getGraphDataList()
    {
        return graphDataList;
    }

    public void setGraphDataList(List<OBDtoMultiDataObj> graphDataList)
    {
        this.graphDataList = graphDataList;
    }

    public ArrayList<String> getInterfaceNameList()
    {
        return interfaceNameList;
    }

    public void setInterfaceNameList(ArrayList<String> interfaceNameList)
    {
        this.interfaceNameList = interfaceNameList;
    }

    public String getInterfaceName()
    {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName)
    {
        this.interfaceName = interfaceName;
    }

    public List<OBDtoMultiDataObj> getStatistic5data()
    {
        return statistic5data;
    }

    public void setStatistic5data(List<OBDtoMultiDataObj> statistic5data)
    {
        this.statistic5data = statistic5data;
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

    @Override
    public String toString()
    {
        return "StatisticsAction [statisticsFacade=" + statisticsFacade + ", adcPortStatusList=" + adcPortStatusList + ", graph=" + graph + ", adc=" + adc + ", selectedTraffic=" + selectedTraffic + ", interfaceIndics=" + interfaceIndics + ", graphDataList=" + graphDataList + ", interfaceNameList=" + interfaceNameList + ", interfaceName=" + interfaceName + ", statistic5data=" + statistic5data + ", startTime=" + startTime + ", endTime=" + endTime + ", startTimeL=" + startTimeL + ", endTimeL=" + endTimeL + ", orderDir=" + orderDir + ", orderType=" + orderType + "]";
    }    
}