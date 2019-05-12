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

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvAdditional;
import kr.openbase.adcsmart.service.dto.OBDtoTargetObject;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultBpsConnInfo;
import kr.openbase.adcsmart.service.impl.OBEnvManagementImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.dto.FaultGroupMemberPerfInfoDto;
import kr.openbase.adcsmart.web.facade.dto.FaultGroupPerfInfoDto;
import kr.openbase.adcsmart.web.facade.monitoring.FlbGroupPerformanceMonitorFacade;
import kr.openbase.adcsmart.web.util.CsvMaker;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

@Controller
@Scope(value = "prototype")

public class FlbGroupPerfomanceMonitorAction extends BaseAction
{
	private transient Logger log = LoggerFactory.getLogger(FlbGroupPerfomanceMonitorAction.class);

	@Autowired
	private FlbGroupPerformanceMonitorFacade FlbGroupPerformanceMonitorFacade;
	
	private OBDtoADCObject adcObject;
	private OBDtoTargetObject targetObject;
	private OBDtoSearch searchOption;
	private OBDtoOrdering orderOption;
	private Date startTime;
	private Date endTime;
	private Long startTimeL;
    private Long endTimeL;
	private Integer totalCount;
	private Integer totalMemberCount;
	private ArrayList<FaultGroupPerfInfoDto> GroupPerfInfoList;
	private FaultGroupMemberPerfInfoDto GroupMemberPerfInfoList;
	private ArrayList<OBDtoFaultBpsConnInfo> GroupBpsConnHistoryInfo;
	private ArrayList<OBDtoFaultBpsConnInfo> GroupMemberBpsConnHistoryInfo;
	private String groupIndex;
	private String groupMemberIndex;
	private Integer monitoringPeriod;
    private Integer intervalMonitor;
    
    public FlbGroupPerfomanceMonitorAction()
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
	
	// Group Page Open
	public String loadFLBGroupPerfomanceContent() throws OBException
	{
		return SUCCESS;		
	}
	
	// Group Member Page Open
	public String loadFLBGroupMemberPerfomanceContent() throws OBException
	{
		return SUCCESS;		
	}
	
	// Group List Total Count Get
	public String loadGroupPerfInfoTotalCount() throws OBException
	{
		try
		{
			if (null != targetObject && null != targetObject.getIndex()) 
			{
				totalCount =  FlbGroupPerformanceMonitorFacade.getGroupCount(targetObject, searchOption);				
			}
			log.debug("{}", totalCount);
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
	
	// Group List Get
	public String loadGroupPerfInfoList() throws OBException
	{
		try 
		{
			if (null != targetObject && null != targetObject.getIndex() && -1 != searchOption.getBeginIndex() && -1 != searchOption.getEndIndex()) 
			{
				GroupPerfInfoList = FlbGroupPerformanceMonitorFacade.getGroupPerformanceList(targetObject, searchOption, orderOption);
			}
			log.debug("{}", GroupPerfInfoList);	
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

	// Member List Total Count
	public String loadGroupMemberPerfTotalCount() throws Exception
	{
		try
		{
			if (null != groupIndex) 
			{
				totalMemberCount =  FlbGroupPerformanceMonitorFacade.getGroupMemberCount(groupIndex);				
			}
			log.debug("{}", totalMemberCount);
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
	
	// Member List Get
	public String loadGroupMemberPerfInfoList() throws Exception
	{
		try
		{
			if (null != groupIndex && -1 != searchOption.getBeginIndex() && -1 != searchOption.getEndIndex()) 
			{
				GroupMemberPerfInfoList = FlbGroupPerformanceMonitorFacade.getGroupMemberPerformanceList(groupIndex, searchOption, orderOption);
			}			
			log.debug("{}", GroupMemberPerfInfoList);
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
	
	// FLB Group History Chart Data
	public String loadGroupPerfBpsConnHistory() throws Exception
	{
		try
		{
			if (null != groupIndex)
			{
			    setSearchTime();
				OBDtoSearch searchOption = new OBDtoSearch();
				searchOption.setFromTime(startTime);
				searchOption.setToTime(endTime);
				GroupBpsConnHistoryInfo = FlbGroupPerformanceMonitorFacade.getGroupBpsConnHistory(groupIndex, searchOption);
				intervalMonitor = monitoringPeriod;
			}
			log.debug("{}", GroupBpsConnHistoryInfo);
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
	
	// FLB Member History Chart Data
	public String loadGroupMemberPerfBpsConnHistory() throws Exception
	{
		try
		{
			if (null != groupMemberIndex)
			{
			    setSearchTime();
				OBDtoSearch searchOption = new OBDtoSearch();
				searchOption.setFromTime(startTime);
				searchOption.setToTime(endTime);
				GroupMemberBpsConnHistoryInfo = FlbGroupPerformanceMonitorFacade.getGroupMemberBpsConnHistory(groupMemberIndex, searchOption);
				intervalMonitor = monitoringPeriod;
			}
			log.debug("{}", GroupMemberBpsConnHistoryInfo);
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
	// FLB 그룹 내보내기 Data Check
	public String checkExportGroupPerfDataExist() throws Exception
	{
		try
		{
			if (null != groupIndex)
			{
			    setSearchTime();
				OBDtoSearch searchOption = new OBDtoSearch();
				searchOption.setFromTime(startTime);
				searchOption.setToTime(endTime);
				GroupBpsConnHistoryInfo = FlbGroupPerformanceMonitorFacade.getGroupBpsConnHistory(groupIndex, searchOption);
				if(GroupBpsConnHistoryInfo != null)
				{
					isSuccessful = true;			
				}
				else
				{
					isSuccessful = false;
					message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_EXPORT_DATA_NOT_EXIST);
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
	// FLB 그룹 내보내기 기능
	public String downloadGroupPerfInfo() throws Exception
	{
		try
		{
			if (null != groupIndex)
			{
			    setSearchTime();
				OBDtoSearch searchOption = new OBDtoSearch();
				searchOption.setFromTime(startTime);
				searchOption.setToTime(endTime);
				GroupBpsConnHistoryInfo = FlbGroupPerformanceMonitorFacade.getGroupBpsConnHistory(groupIndex, searchOption);
				
				CsvMaker csvMaker = new CsvMaker();
				csvMaker.initWithFlbBpsConnHistory(GroupBpsConnHistoryInfo);
				File csv = csvMaker.write();
				if (csv != null)
				{
					setStrutsStream(csv);
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
	
	// FLB Member 내보내기 Data Check
	public String checkExportGroupMemberPerfDataExist() throws Exception
	{
		try
		{
			if (null != groupMemberIndex)
			{
			    setSearchTime();
				OBDtoSearch searchOption = new OBDtoSearch();
				searchOption.setFromTime(startTime);
				searchOption.setToTime(endTime);
				GroupMemberBpsConnHistoryInfo = FlbGroupPerformanceMonitorFacade.getGroupMemberBpsConnHistory(groupMemberIndex, searchOption);
				if(GroupMemberBpsConnHistoryInfo != null)
				{
					isSuccessful = true;			
				}
				else
				{
					isSuccessful = false;
					message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_EXPORT_DATA_NOT_EXIST);
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
	
	// FLB Member 내보내기 기능
	public String downloadGroupMemberPerfInfo() throws Exception
	{
		try
		{
			if (null != groupMemberIndex)
			{
			    setSearchTime();
				OBDtoSearch searchOption = new OBDtoSearch();
				searchOption.setFromTime(startTime);
				searchOption.setToTime(endTime);
				GroupMemberBpsConnHistoryInfo = FlbGroupPerformanceMonitorFacade.getGroupMemberBpsConnHistory(groupMemberIndex, searchOption);
				
				CsvMaker csvMaker = new CsvMaker();
				csvMaker.initWithFlbBpsConnHistory(GroupMemberBpsConnHistoryInfo);
				File csv = csvMaker.write();
				if (csv != null)
				{
					setStrutsStream(csv);
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

	public OBDtoTargetObject getTargetObject()
	{
		return targetObject;
	}

	public void setTargetObject(OBDtoTargetObject targetObject)
	{
		this.targetObject = targetObject;
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
	
	public Integer getTotalCount()
	{
		return totalCount;
	}

	public void setTotalCount(Integer totalCount)
	{
		this.totalCount = totalCount;
	}

	public Integer getTotalMemberCount()
	{
		return totalMemberCount;
	}

	public void setTotalMemberCount(Integer totalMemberCount)
	{
		this.totalMemberCount = totalMemberCount;
	}

	public ArrayList<FaultGroupPerfInfoDto> getGroupPerfInfoList()
	{
		return GroupPerfInfoList;
	}

	public void setGroupPerfInfoList(ArrayList<FaultGroupPerfInfoDto> groupPerfInfoList)
	{
		GroupPerfInfoList = groupPerfInfoList;
	}
	
	public FaultGroupMemberPerfInfoDto getGroupMemberPerfInfoList()
	{
		return GroupMemberPerfInfoList;
	}

	public void setGroupMemberPerfInfoList(FaultGroupMemberPerfInfoDto groupMemberPerfInfoList)
	{
		GroupMemberPerfInfoList = groupMemberPerfInfoList;
	}

	public ArrayList<OBDtoFaultBpsConnInfo> getGroupBpsConnHistoryInfo()
	{
		return GroupBpsConnHistoryInfo;
	}

	public void setGroupBpsConnHistoryInfo(ArrayList<OBDtoFaultBpsConnInfo> groupBpsConnHistoryInfo)
	{
		GroupBpsConnHistoryInfo = groupBpsConnHistoryInfo;
	}

	public ArrayList<OBDtoFaultBpsConnInfo> getGroupMemberBpsConnHistoryInfo()
	{
		return GroupMemberBpsConnHistoryInfo;
	}

	public void setGroupMemberBpsConnHistoryInfo(ArrayList<OBDtoFaultBpsConnInfo> groupMemberBpsConnHistoryInfo)
	{
		GroupMemberBpsConnHistoryInfo = groupMemberBpsConnHistoryInfo;
	}

	public String getGroupIndex()
	{
		return groupIndex;
	}

	public void setGroupIndex(String groupIndex)
	{
		this.groupIndex = groupIndex;
	}

	public String getGroupMemberIndex()
	{
		return groupMemberIndex;
	}

	public void setGroupMemberIndex(String groupMemberIndex)
	{
		this.groupMemberIndex = groupMemberIndex;
	}	

	public OBDtoADCObject getAdcObject()
	{
		return adcObject;
	}

	public void setAdcObject(OBDtoADCObject adcObject)
	{
		this.adcObject = adcObject;
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

    @Override
    public String toString()
    {
        return "FlbGroupPerfomanceMonitorAction [FlbGroupPerformanceMonitorFacade="
                + FlbGroupPerformanceMonitorFacade
                + ", adcObject="
                + adcObject
                + ", targetObject="
                + targetObject
                + ", searchOption="
                + searchOption
                + ", orderOption="
                + orderOption
                + ", startTime="
                + startTime
                + ", endTime="
                + endTime
                + ", startTimeL="
                + startTimeL
                + ", endTimeL="
                + endTimeL
                + ", totalCount="
                + totalCount
                + ", totalMemberCount="
                + totalMemberCount
                + ", GroupPerfInfoList="
                + GroupPerfInfoList
                + ", GroupMemberPerfInfoList="
                + GroupMemberPerfInfoList
                + ", GroupBpsConnHistoryInfo="
                + GroupBpsConnHistoryInfo
                + ", GroupMemberBpsConnHistoryInfo="
                + GroupMemberBpsConnHistoryInfo
                + ", groupIndex="
                + groupIndex
                + ", groupMemberIndex="
                + groupMemberIndex
                + ", monitoringPeriod="
                + monitoringPeriod
                + ", intervalMonitor=" + intervalMonitor + "]";
    }
}