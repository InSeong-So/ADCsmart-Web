package kr.openbase.adcsmart.web.controller.system;

import java.util.Calendar;
import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvAdditional;
import kr.openbase.adcsmart.service.impl.OBEnvManagementImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.dto.SystemCpuInfoDto;
import kr.openbase.adcsmart.web.facade.dto.SystemDatabaseInfoDto;
import kr.openbase.adcsmart.web.facade.dto.SystemHddInfoDto;
import kr.openbase.adcsmart.web.facade.dto.SystemMemoryInfoDto;
import kr.openbase.adcsmart.web.facade.system.SystemInfoFacade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope(value = "prototype")
public class SystemInfoAction extends BaseAction 
{
	
	private transient Logger log = LoggerFactory.getLogger(SystemInfoAction.class);
	
	@Autowired
	private SystemInfoFacade systemInfoFacade;	
	
	private Long startTimeL;
	private Long endTimeL;
	private Date startTime;
	private Date endTime;
	private SystemCpuInfoDto systemCpuInfo;
	private SystemMemoryInfoDto systemMemoryInfo;
	private SystemHddInfoDto systemHddInfo;
	private SystemDatabaseInfoDto systemDatabaseInfo;
	private Integer monitoringPeriod;
    private Integer intervalMonitor;
    public SystemInfoAction() 
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
	 * @return the systemCpuInfo
	 */
	public SystemCpuInfoDto getSystemCpuInfo() 
	{
		return systemCpuInfo;
	}

	/**
	 * @param systemCpuInfo the systemCpuInfo to set
	 */
	public void setSystemCpuInfo(SystemCpuInfoDto systemCpuInfo) 
	{
		this.systemCpuInfo = systemCpuInfo;
	}

	/**
	 * @return the systemMemoryInfo
	 */
	public SystemMemoryInfoDto getSystemMemoryInfo() 
	{
		return systemMemoryInfo;
	}

	/**
	 * @param systemMemoryInfo the systemMemoryInfo to set
	 */
	public void setSystemMemoryInfo(SystemMemoryInfoDto systemMemoryInfo) 
	{
		this.systemMemoryInfo = systemMemoryInfo;
	}

	/**
	 * @return the systemHddInfo
	 */
	public SystemHddInfoDto getSystemHddInfo() 
	{
		return systemHddInfo;
	}

	/**
	 * @param systemHddInfo the systemHddInfo to set
	 */
	public void setSystemHddInfo(SystemHddInfoDto systemHddInfo) 
	{
		this.systemHddInfo = systemHddInfo;
	}

	/**
	 * @return the systemDatabaseInfo
	 */
	public SystemDatabaseInfoDto getSystemDatabaseInfo() 
	{
		return systemDatabaseInfo;
	}

	/**
	 * @param systemDatabaseInfo the systemDatabaseInfo to set
	 */
	public void setSystemDatabaseInfo(SystemDatabaseInfoDto systemDatabaseInfo) 
	{
		this.systemDatabaseInfo = systemDatabaseInfo;
	}
	
	public String loadSystemInfoContent() throws OBException 
	{
		return SUCCESS;
	}
	
	public String loadSystemInfo() throws OBException 
	{
		try 
		{
			setSearchTime();
			systemCpuInfo = systemInfoFacade.getSystemCpuInfo(startTime, endTime);
			systemMemoryInfo = systemInfoFacade.getSystemMemoryInfo(startTime, endTime);
			systemHddInfo = systemInfoFacade.getSystemHddInfo();
			systemDatabaseInfo = systemInfoFacade.getSystemDatabaseInfo();
			intervalMonitor = monitoringPeriod;
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
}
