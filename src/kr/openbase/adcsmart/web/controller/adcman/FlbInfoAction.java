package kr.openbase.adcsmart.web.controller.adcman;

import java.io.File;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoInterfaceSummary;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSLBUpdateStatus;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoTargetObject;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.adcman.FlbInfoFacade;
import kr.openbase.adcsmart.web.facade.adcman.VirtualSvrFacade;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.FlbFilterInfoDto;
import kr.openbase.adcsmart.web.util.CsvMaker;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

@Controller
@Scope(value = "prototype")

public class FlbInfoAction extends BaseAction
{
	@Autowired
	private FlbInfoFacade flbInfoFacade;
	
	@Autowired
	private VirtualSvrFacade virtualSvrFacade;
	
	private transient Logger log = LoggerFactory.getLogger(FlbInfoAction.class);
	
	private AdcDto adc;
	private OBDtoADCObject adcObject;
	private OBDtoTargetObject targetObject;
	private OBDtoSearch searchOption;
	private OBDtoOrdering orderOption;
	private Integer totalCount;
	private ArrayList<OBDtoInterfaceSummary> physicalPortList;
	private ArrayList<FlbFilterInfoDto> filterInfoList;
	private Boolean refreshes = false;
	private Integer extraKey;                       // 1 최신 SLB 정보
	
	public String loadFlbInfoContent() throws OBException
	{
		try
		{
			if (null != adcObject)
			{
				physicalPortList = flbInfoFacade.getPhysicalPortForFilter(adcObject.getIndex());
			}
			if (null != adcObject && null != adcObject.getIndex() && -1 != searchOption.getBeginIndex() && -1 != searchOption.getEndIndex())
			{				
				filterInfoList = flbInfoFacade.getFilterInfo(adcObject.getIndex(), searchOption, orderOption);					
			}
			log.debug("physicalPortList:{}", physicalPortList);
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
	
	public String loadFlbInfoTotalCount() throws OBException
	{
		isSuccessful = true;
		try 
		{
			if (adcObject.getIndex() != null)
			{
				totalCount = flbInfoFacade.getFilterCount(adcObject.getIndex(), searchOption);
			}
			log.debug("totalCount: {}", totalCount);
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
	public String loadRefreshListContent() throws Exception
	{
		isSuccessful = true;
		OBDtoSLBUpdateStatus retVal = new OBDtoSLBUpdateStatus();
		try 
		{			
			if (refreshes)
			{
			    retVal = virtualSvrFacade.downloadVirtualServerList(adc);
			    if(retVal.isUpdateSuccess())             
                {       
                    message = "";
                }
			    else
                {
                    isSuccessful = false;
                    if (retVal.getExtraMsg() == null || retVal.getExtraMsg().isEmpty())
                    {   
                        extraKey = retVal.getExtraKey();
                        message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_SLB_DOWNLOAD_FAIL);
                    }
                    else
                    {                       
                        message = retVal.getExtraMsg();
                    }                    
                }				
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
	
	public String loadFlbInfoList() throws OBException
	{
		try
		{
			if (null != adcObject && null != adcObject.getIndex() && -1 != searchOption.getBeginIndex() && -1 != searchOption.getEndIndex()) 
			{
				filterInfoList = flbInfoFacade.getFilterInfo(adcObject.getIndex(), searchOption, orderOption);					
				log.debug("l2InfoList:{}", filterInfoList);
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
	
	public String checkFlbInfoExist() throws OBException
	{
		try
		{		
			filterInfoList = flbInfoFacade.getFilterInfo(adcObject.getIndex(), searchOption, orderOption);					
			log.debug("l2InfoList:{}", filterInfoList);
			
			if (filterInfoList != null &&filterInfoList.size()>0)
			{
				isSuccessful = true;
				filterInfoList = null;
			}
			else
			{
				isSuccessful = false;
				filterInfoList = null;
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
	
	public String downloadFlbInfoExist() throws OBException
	{
		try
		{		
			filterInfoList = flbInfoFacade.getFilterInfo(adcObject.getIndex(), searchOption, orderOption);					
			log.debug("l2InfoList:{}", filterInfoList);
			
			CsvMaker csvMaker = new CsvMaker();
			csvMaker.initWithfilterInfo(filterInfoList);
			File csv = csvMaker.write();
			filterInfoList = null;
			if (csv != null)
			{
				setStrutsStream(csv);
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

	public OBDtoADCObject getAdcObject()
	{
		return adcObject;
	}

	public void setAdcObject(OBDtoADCObject adcObject)
	{
		this.adcObject = adcObject;
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

	public Integer getTotalCount()
	{
		return totalCount;
	}

	public void setTotalCount(Integer totalCount)
	{
		this.totalCount = totalCount;
	}
	
	public ArrayList<OBDtoInterfaceSummary> getPhysicalPortList()
	{
		return physicalPortList;
	}

	public void setPhysicalPortList(ArrayList<OBDtoInterfaceSummary> physicalPortList)
	{
		this.physicalPortList = physicalPortList;
	}

	public ArrayList<FlbFilterInfoDto> getFilterInfoList()
	{
		return filterInfoList;
	}

	public void setFilterInfoList(ArrayList<FlbFilterInfoDto> filterInfoList)
	{
		this.filterInfoList = filterInfoList;
	}

	public AdcDto getAdc()
	{
		return adc;
	}

	public void setAdc(AdcDto adc)
	{
		this.adc = adc;
	}

	public Boolean getRefreshes()
	{
		return refreshes;
	}

	public void setRefreshes(Boolean refreshes)
	{
		this.refreshes = refreshes;
	}

	public Integer getExtraKey()
    {
        return extraKey;
    }

    public void setExtraKey(Integer extraKey)
    {
        this.extraKey = extraKey;
    }

    @Override
    public String toString()
    {
        return "FlbInfoAction [flbInfoFacade=" + flbInfoFacade
                + ", virtualSvrFacade=" + virtualSvrFacade + ", adc=" + adc
                + ", adcObject=" + adcObject + ", targetObject=" + targetObject
                + ", searchOption=" + searchOption + ", orderOption="
                + orderOption + ", totalCount=" + totalCount
                + ", physicalPortList=" + physicalPortList
                + ", filterInfoList=" + filterInfoList + ", refreshes="
                + refreshes + ", extraKey=" + extraKey + "]";
    }
}