package kr.openbase.adcsmart.web.controller.adcman;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcLogSearchOption;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.adcman.LogAnalysisFacade;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.AdcLogDto;
import kr.openbase.adcsmart.web.facade.dto.AuditLogDto;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;
import kr.openbase.adcsmart.web.util.CsvMaker;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

@Controller
@Scope(value = "prototype")
public class LogAnalysisAction extends BaseAction 
{
	private transient Logger log = LoggerFactory.getLogger(LogAnalysisAction.class);
	
	@Autowired
	private LogAnalysisFacade logAnalysisFacade;
	
	private OBDtoADCObject adcObject;	
	private OBDtoOrdering orderOption;
	private String searchKey;
	private Date fromPeriod;
	private Date toPeriod;
	private Long startTimeL;
	private Long endTimeL;
	private Integer fromRow;
	private Integer toRow;
	private Integer orderDir; // 오름차순 = 1, 내림차순 = 2
	private Integer orderType; // occurTime = 11
	private Integer extraContentKey; // 추가 한글 log
	private AdcDto adc;
	private OBDtoAdcLogSearchOption selectOption;
	
	private List<AdcLogDto> adcLogs;
	private List<AuditLogDto> auditLogs;
	private Integer rowTotal;	
	
	// ADC 로그 Total Count Get
	public String retrieveAdcLogTotal() throws OBException 
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
			rowTotal = logAnalysisFacade.getAdcLogTotal(adcObject, searchOption, selectOption, sessionData.getAccountIndex());
			log.debug("adc log total: {}", rowTotal);
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
	
	@SuppressWarnings("unchecked")
	public String loadAdcLogListContent() throws OBException 
	{
		try 
		{
			log.debug("adcObject: {}, searchKey:{}, fromRow:{}, toRow:{}, fromPeriod:{}, toPeriod:{}, orderOption:{}", new Object[]{adcObject, searchKey, fromRow, toRow, fromPeriod, toPeriod, orderOption});

			if (fromRow != null && fromRow < 0)
			{
				adcLogs = ListUtils.EMPTY_LIST;
			}
				
			else 
			{
			    setSearchTime();
				OBDtoSearch searchOption = new OBDtoSearch();
				searchOption.setSearchKey(searchKey);
				searchOption.setFromTime(fromPeriod);
				searchOption.setToTime(toPeriod);
				searchOption.setBeginIndex(fromRow);
				searchOption.setEndIndex(toRow);
				SessionDto sessionData = session.getSessionDto();
				adcLogs = logAnalysisFacade.selectAdcLogs(adcObject, searchOption, orderOption, extraContentKey, selectOption, sessionData.getAccountIndex());			
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
	public String checkAdcDataExist() throws OBException 
	{
		try 
		{
		    setSearchTime();
			OBDtoSearch searchOption = new OBDtoSearch();
			searchOption.setSearchKey(searchKey);
			searchOption.setFromTime(fromPeriod);
			searchOption.setToTime(toPeriod);
			searchOption.setBeginIndex(null);
			searchOption.setEndIndex(OBDefine.SYSENV_MAX_ADCLOG_EXPORT);
			SessionDto sessionData = session.getSessionDto();
			log.debug("adcObject: {}, searchOption:{}", new Object[]{adcObject, searchOption});
			adcLogs = logAnalysisFacade.selectAdcLogsToExport(adcObject, searchOption, extraContentKey, selectOption, sessionData.getAccountIndex());

			if (adcLogs != null &&adcLogs.size()>0)
			{
				isSuccessful = true;
				adcLogs = null;
			}
			else
			{
				isSuccessful = false;
				adcLogs = null;
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
	
	public String downloadAdcLog() throws OBException 
	{
		try 
		{
		    setSearchTime();
			OBDtoSearch searchOption = new OBDtoSearch();
			searchOption.setSearchKey(searchKey);
			searchOption.setFromTime(fromPeriod);
			searchOption.setToTime(toPeriod);
			searchOption.setBeginIndex(null);
			searchOption.setEndIndex(OBDefine.SYSENV_MAX_ADCLOG_EXPORT);
			SessionDto sessionData = session.getSessionDto();
			log.debug("adcObject: {}, searchOption:{}", new Object[]{adcObject, searchOption});
			adcLogs = logAnalysisFacade.selectAdcLogsToExport(adcObject, searchOption, extraContentKey, selectOption, sessionData.getAccountIndex());
			
			log.debug("{}", adcLogs);
			CsvMaker csvMaker = new CsvMaker();
			csvMaker.initWithAdcLog(adcLogs);
			File csv = csvMaker.write();
			adcLogs = null;
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
	
	@SuppressWarnings("unchecked")
	public String loadAuditLogListContent() throws OBException 
	{
		try 
		{
			log.debug("adc:{}, searchKey:{}, fromRow:{}, toRow:{}, fromPeriod:{}, toPeriod:{}, orderType:{}, orderDir:{}", new Object[]{adc, searchKey, fromRow, toRow, fromPeriod, toPeriod, orderType, orderDir});
			if (fromRow != null && fromRow < 0)
			{
				auditLogs = ListUtils.EMPTY_LIST;
			}
			else
			{
			    setSearchTime();			   
				auditLogs = logAnalysisFacade.selectAuditLogs( searchKey , fromPeriod, toPeriod,fromRow, toRow, session.getSessionDto(), orderType, orderDir);
			}
				
			log.debug("{}", auditLogs);
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
	public String checkAuditDataExist() throws OBException 
	{
		try 
		{
		    setSearchTime();
			log.debug("adc:{}, searchKey:{}, fromPeriod:{}, toPeriod:{}", new Object[]{adc, searchKey, fromPeriod, toPeriod});			
			auditLogs = logAnalysisFacade.selectAuditLogs( searchKey,fromPeriod, toPeriod, null , OBDefine.SYSENV_MAX_ADCLOG_EXPORT , session.getSessionDto());

			if (auditLogs != null && auditLogs.size()>0)
			{
				isSuccessful = true;
				auditLogs = null;
			}
			else
			{
				isSuccessful = false;
				auditLogs = null;
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
	public String downloadAuditLog() throws OBException 
	{
		try 
		{
		    setSearchTime();			
			String decodeSearchKey = OBCommon.decodeURIComponent(searchKey);
			log.debug("adc:{}, searchKey:{}, fromRow:{}, toRow:{} , fromPeriod:{} , toPeriod:{}", new Object[]{adc, searchKey, fromRow, toRow , fromPeriod, toPeriod});
			auditLogs = logAnalysisFacade.selectAuditLogs(decodeSearchKey, fromPeriod, toPeriod, null , OBDefine.SYSENV_MAX_ADCLOG_EXPORT , session.getSessionDto());
			log.debug("{}", auditLogs);
			CsvMaker csvMaker = new CsvMaker();
			csvMaker.initWithAuditLog(auditLogs);
			File csv = csvMaker.write();
			auditLogs = null;
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
	
	
	
	public String retrieveAuditLogTotal() throws OBException 
	{
		isSuccessful = true;
		try 
		{
			setSearchTime();
			log.debug("searchKey: {}, fromPeriod: {}, toPeriod: {}", new Object[]{searchKey, fromPeriod, toPeriod});
			rowTotal = logAnalysisFacade.getAuditLogTotal(searchKey, fromPeriod, toPeriod);
			log.debug("audit log total: {}", rowTotal);
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

	public Integer getExtraContentKey()
	{
		return extraContentKey;
	}

	public void setExtraContentKey(Integer extraContentKey)
	{
		this.extraContentKey = extraContentKey;
	}

	public AdcDto getAdc()
	{
		return adc;
	}

	public void setAdc(AdcDto adc)
	{
		this.adc = adc;
	}

	public List<AdcLogDto> getAdcLogs()
	{
		return adcLogs;
	}

	public void setAdcLogs(List<AdcLogDto> adcLogs)
	{
		this.adcLogs = adcLogs;
	}

	public List<AuditLogDto> getAuditLogs()
	{
		return auditLogs;
	}

	public void setAuditLogs(List<AuditLogDto> auditLogs)
	{
		this.auditLogs = auditLogs;
	}

	public Integer getRowTotal()
	{
		return rowTotal;
	}

	public void setRowTotal(Integer rowTotal)
	{
		this.rowTotal = rowTotal;
	}

	public OBDtoAdcLogSearchOption getSelectOption()
	{
		return selectOption;
	}

	public void setSelectOption(OBDtoAdcLogSearchOption selectOption)
	{
		this.selectOption = selectOption;
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
        return "LogAnalysisAction [logAnalysisFacade=" + logAnalysisFacade + ", adcObject=" + adcObject + ", orderOption=" + orderOption + ", searchKey=" + searchKey + ", fromPeriod=" + fromPeriod + ", toPeriod=" + toPeriod + ", startTimeL=" + startTimeL + ", endTimeL=" + endTimeL + ", fromRow=" + fromRow + ", toRow=" + toRow + ", orderDir=" + orderDir + ", orderType=" + orderType + ", extraContentKey=" + extraContentKey + ", adc=" + adc + ", selectOption=" + selectOption + ", adcLogs=" + adcLogs + ", auditLogs=" + auditLogs + ", rowTotal=" + rowTotal + "]";
    }
}