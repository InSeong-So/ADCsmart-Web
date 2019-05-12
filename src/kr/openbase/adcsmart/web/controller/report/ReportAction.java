package kr.openbase.adcsmart.web.controller.report;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckLog;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.adcman.AdcFacade;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.ReportAddDto;
import kr.openbase.adcsmart.web.facade.dto.ReportDto;
import kr.openbase.adcsmart.web.facade.fault.FaultHistoryFacade;
import kr.openbase.adcsmart.web.facade.report.ReportFacade;
import kr.openbase.adcsmart.web.util.OBFileHandler;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

@Controller
@Scope(value = "prototype")
public class ReportAction extends BaseAction 
{
	private transient Logger log = LoggerFactory.getLogger(ReportAction.class);	
	
	@Autowired
	private ReportFacade reportFacade;
	
	@Autowired
	private AdcFacade adcFacade;
	
	@Autowired
	private FaultHistoryFacade faultHistoryFacade;
	
	@Value("#{settings['site.isSDSSite']}") 
	private boolean varIsSDSSite;													// ftl에서 사용하는 변수임.
	
	private AdcDto adc;
	private List<ReportDto> reports;
	private Integer rowTotal;
	private String reportIndex;
	private List<String> reportIndices;
	private ReportAddDto reportAdd;
	private List<AdcDto> availableAdcs;
	private String searchKey;
	private Integer fromRow;
	private Integer toRow;
	private Date	fromPeriod;
	private Date    toPeriod;
	private Long startTimeL;
    private Long endTimeL;
	private Integer orderDir;														// 오른차순 = 1, 내림차순 = 2
	private Integer orderType;														// occurTime = 11 , vsName = 1 , vsIpaddress =2, content=3
	
	private List<OBDtoFaultCheckLog> faultCheckLogList;								// 장애 진단 로그 목록 list
	private OBDtoADCObject adcObject;		// 현재 선택한 adc object.


	@SuppressWarnings("unchecked")
	public String loadListContent() throws OBException 
	{
		try 
		{
			log.debug("adc:{}, searchKey:{}, fromPeriod:{}, toPeriod:{}, fromRow:{}, toRow:{}", new Object[]{adc, searchKey, fromPeriod, toPeriod, fromRow, toRow});
			if (fromRow != null && fromRow < 0)
			{
				reports = ListUtils.EMPTY_LIST;
			}		
			else
			{
			    setSearchTime();
				reports = reportFacade.getReports(session.getAccountIndex(), adc.getIndex(), adc.getGroupIndex(), searchKey , fromPeriod, toPeriod, fromRow, toRow, orderType, orderDir);
			}
			
			log.debug("{}", reports);
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
	
	public String retrieveReportsTotal() throws OBException
	{
		isSuccessful = true;
		try 
		{
			log.debug("adc: {}, searchKey:{}, fromPeriod:{} , toPeriod:{}", new Object[] {adc, searchKey , fromPeriod, toPeriod});
			setSearchTime();
			log.debug("adc: {}, searchKey:{}, fromPeriod:{} , toPeriod:{}", new Object[] {adc, searchKey , fromPeriod, toPeriod});
			rowTotal = reportFacade.getReportsTotal(session.getAccountIndex(), adc.getIndex(), adc.getGroupIndex(), searchKey , fromPeriod, toPeriod);
			log.debug("rowTotal: {}", rowTotal);
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
	
	public String loadAddContent() throws OBException 
	{
		log.debug("loadAddContent");
		if (reportAdd == null)
		{
			reportAdd = new ReportAddDto();
		}	
		
		try 
		{
		    setSearchTime();
			OBDtoSearch searchObj = new OBDtoSearch();
			searchObj.setFromTime(null);                // 진단 결과 모두 보이도록 조회
			searchObj.setToTime(null);                  // 진단 결과 모두 보이도록 조회
			searchObj.setSearchKey(searchKey);		
			searchObj.setBeginIndex(fromRow);
			searchObj.setEndIndex(toRow);
			
			OBDtoOrdering orderObj = new OBDtoOrdering();
			orderObj.setOrderDirection(OBDtoOrdering.DIR_DESCEND);
			orderObj.setOrderType(OBDtoOrdering.TYPE_1FIRST);
			
			availableAdcs = adcFacade.getAllAdcs(session.getAccountIndex(), null);
			faultCheckLogList = faultHistoryFacade.getHistoryList(adcObject, searchObj, orderObj);
			log.debug("faultCheckLogList:{}", faultCheckLogList);		
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
	
	public String addReport() throws OBException 
	{
		isSuccessful = true;
		try
		{
			log.debug("reportAdd: {}", reportAdd);
			reportFacade.addReport(reportAdd, session.getSessionDto());
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
	
	public String delReports() throws OBException 
	{
		isSuccessful = true;
		try 
		{
			log.debug("delReports");
			reportFacade.delReports(reportIndices, session.getSessionDto());
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
	
	public String checkDownloadReportDataExist() throws OBException 
	{
		try 
		{
			log.debug("reportIndex:{}", reportIndex);
			ReportDto report = reportFacade.getReport(reportIndex);
					
			OBFileHandler.isFileExist(report.getOutPathFile()); // 레포드 다운로드시 파일의 유무를 확인 한다.						
			if(OBFileHandler.isFileExist(report.getOutPathFile()))
			{
				isSuccessful = true;						
			}
			else
			{
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_DOWNLOAD_REPORT_NOT_EXIST);
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
	
	public String downloadReport() throws OBException 
	{
		try 
		{
			log.debug("reportIndex:{}", reportIndex);
			ReportDto report = reportFacade.getReport(reportIndex);
			log.debug("{}", report);
			File file = new File(report.getOutPathFile());
			if (file != null)
			{
				setStrutsStream(file);
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
	
	public AdcDto getAdc()
	{
		return adc;
	}
	
	public void setAdc(AdcDto adc)
	{
		this.adc = adc;
	}
	
	public List<ReportDto> getReports()
	{
		return reports;
	}
	
	public void setReports(List<ReportDto> reports)
	{
		this.reports = reports;
	}
	
	public Integer getRowTotal()
	{
		return rowTotal;
	}
	
	public void setRowTotal(Integer rowTotal)
	{
		this.rowTotal = rowTotal;
	}
	
	public String getReportIndex()
	{
		return reportIndex;
	}
	
	public void setReportIndex(String reportIndex)
	{
		this.reportIndex = reportIndex;
	}
	
	public List<String> getReportIndices()
	{
		return reportIndices;
	}
	
	public void setReportIndices(List<String> reportIndices)
	{
		this.reportIndices = reportIndices;
	}
	
	public ReportAddDto getReportAdd()
	{
		return reportAdd;
	}
	
	public void setReportAdd(ReportAddDto reportAdd)
	{
		this.reportAdd = reportAdd;
	}
	
	public List<AdcDto> getAvailableAdcs() 
	{
		return availableAdcs;
	}
	
	public void setAvailableAdcs(List<AdcDto> availableAdcs) 
	{
		this.availableAdcs = availableAdcs;
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
	
	public boolean isVarIsSDSSite()
	{
		return varIsSDSSite;
	}
	
	public void setVarIsSDSSite(boolean varIsSDSSite)
	{
		this.varIsSDSSite = varIsSDSSite;
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
	
	public List<OBDtoFaultCheckLog> getFaultCheckLogList()
	{
		return faultCheckLogList;
	}
	
	public void setFaultCheckLogList(List<OBDtoFaultCheckLog> faultCheckLogList)
	{
		this.faultCheckLogList = faultCheckLogList;
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
}
