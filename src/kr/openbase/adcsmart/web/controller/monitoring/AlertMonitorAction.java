package kr.openbase.adcsmart.web.controller.monitoring;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.dto.AdcAlertLogDto;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;
import kr.openbase.adcsmart.web.facade.monitoring.AlertMonitorFacade;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

import org.apache.commons.collections.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope(value = "prototype")
public class AlertMonitorAction extends BaseAction
{
	private transient Logger log = LoggerFactory.getLogger(AlertMonitorAction.class);
	
	@Autowired
	private AlertMonitorFacade alertMonitorFacade;
	
	private OBDtoADCObject adcObject;	
	private OBDtoOrdering orderOption;
	private String searchKey;
	private Date fromPeriod;
	private Date toPeriod;
	private Long startTimeL;
    private Long endTimeL;
	private Integer beginIndex;
	private Integer endIndex;
	private Integer rowTotal;	

	private List<AdcAlertLogDto> adcAlertLogList;	

	@SuppressWarnings("unchecked")
	public String loadAlertMonitorContent() throws Exception 
	{
		try
		{
			if (beginIndex != null && beginIndex < 0)
			{
				adcAlertLogList = ListUtils.EMPTY_LIST;
			}
			else 
			{
				log.debug("adcObject: {}, searchKey:{}, fromPeriod:{}, toPeriod:{}, beginIndex:{}, endIndex:{}",
						new Object[]{adcObject, searchKey, fromPeriod, toPeriod, beginIndex, endIndex});
				setSearchTime();
				OBDtoSearch searchOption = new OBDtoSearch();
				searchOption.setSearchKey(searchKey);
				searchOption.setFromTime(fromPeriod);
				searchOption.setToTime(toPeriod);
				searchOption.setBeginIndex(beginIndex);
				searchOption.setEndIndex(endIndex);
				SessionDto sessionData = session.getSessionDto();
				adcAlertLogList = alertMonitorFacade.getAlertLog(adcObject, searchOption , orderOption, sessionData.getAccountIndex());
			}			
		} 
		catch (Exception e) 
		{
			throw e;
		}
		return SUCCESS;
	}	

	public String retrieveAlertLogCount() throws Exception
	{
		isSuccessful = true;
		try 
		{			
			log.debug("adcObject: {}, fromTime:{}, toTime:{}", new Object[]{adcObject, fromPeriod, toPeriod});
			setSearchTime();
			OBDtoSearch searchOption = new OBDtoSearch();
			searchOption.setSearchKey(searchKey);
			searchOption.setFromTime(fromPeriod);
			searchOption.setToTime(toPeriod);
			SessionDto sessionData = session.getSessionDto();
			rowTotal = alertMonitorFacade.getAlertLogCount(adcObject, searchOption, sessionData.getAccountIndex());
			log.debug("rowTotal: {}", rowTotal);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			isSuccessful = false;
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_ADC_RETRIEVE);
		}		
		return SUCCESS;
	}
	private void setSearchTime()
    {
        if (null != startTimeL && null != endTimeL)
        {
            fromPeriod = new Date(startTimeL);
            toPeriod = new Date(endTimeL);   
        } 
        else
        {
            toPeriod = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(toPeriod);      
            calendar.add(Calendar.HOUR_OF_DAY, -12);            
            fromPeriod = calendar.getTime();         
        }       
        log.debug("startTime: " + fromPeriod.toString() + ", endTime: " + toPeriod.toString());
    }
	
	public OBDtoADCObject getAdcObject()
	{
		return adcObject;
	}
	public void setAdcObject(OBDtoADCObject adcObject)
	{
		this.adcObject = adcObject;
	}
	public OBDtoOrdering getOrderOption()
	{
		return orderOption;
	}
	public void setOrderOption(OBDtoOrdering orderOption)
	{
		this.orderOption = orderOption;
	}
	public String getSearchKey()
	{
		return searchKey;
	}
	public void setSearchKey(String searchKey)
	{
		this.searchKey = searchKey;
	}
	public Date getFromPeriod()
	{
		return fromPeriod;
	}
	public void setFromPeriod(Date fromPeriod)
	{
		this.fromPeriod = fromPeriod;
	}
	public Date getToPeriod()
	{
		return toPeriod;
	}
	public void setToPeriod(Date toPeriod)
	{
		this.toPeriod = toPeriod;
	}
	public Integer getBeginIndex()
	{
		return beginIndex;
	}
	public void setBeginIndex(Integer beginIndex)
	{
		this.beginIndex = beginIndex;
	}
	public Integer getEndIndex()
	{
		return endIndex;
	}
	public void setEndIndex(Integer endIndex)
	{
		this.endIndex = endIndex;
	}
	public Integer getRowTotal()
	{
		return rowTotal;
	}
	public void setRowTotal(Integer rowTotal)
	{
		this.rowTotal = rowTotal;
	}
	public List<AdcAlertLogDto> getAdcAlertLogList()
	{
		return adcAlertLogList;
	}
	public void setAdcAlertLogList(List<AdcAlertLogDto> adcAlertLogList)
	{
		this.adcAlertLogList = adcAlertLogList;
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
        return "AlertMonitorAction [alertMonitorFacade=" + alertMonitorFacade + ", adcObject=" + adcObject + ", orderOption=" + orderOption + ", searchKey=" + searchKey + ", fromPeriod=" + fromPeriod + ", toPeriod=" + toPeriod + ", startTimeL=" + startTimeL + ", endTimeL=" + endTimeL + ", beginIndex=" + beginIndex + ", endIndex=" + endIndex + ", rowTotal=" + rowTotal + ", adcAlertLogList=" + adcAlertLogList + "]";
    }   
}