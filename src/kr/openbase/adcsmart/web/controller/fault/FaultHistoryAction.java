package kr.openbase.adcsmart.web.controller.fault;

import java.io.File;
import java.util.ArrayList;
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
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvAdditional;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckLog;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckResult;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckSchedule;
import kr.openbase.adcsmart.service.impl.OBEnvManagementImpl;
import kr.openbase.adcsmart.service.impl.dto.OBDtoCpuMemStatus;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoFaultCheckPacketLossInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoFaultCheckResponseTimeInfo;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.fault.FaultHistoryFacade;
import kr.openbase.adcsmart.web.util.CsvMaker;
import kr.openbase.adcsmart.web.util.OBFileHandler;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

@Controller
@Scope(value = "prototype")
public class FaultHistoryAction extends BaseAction
{
	private transient Logger log = LoggerFactory.getLogger(FaultHistoryAction.class);
	
	@Autowired
	private FaultHistoryFacade faultHistoryFacade;
	
	private List<OBDtoFaultCheckLog> faultCheckLogList;								// 장애 진단 로그 목록 list
	private OBDtoFaultCheckLog faultCheckLogInfo;									// 장애 진단 로그 정보
	private OBDtoADCObject adcObject;												// 현재 선택한 adc object.
	private OBDtoSearch searchObj;													// 검색 object
	private OBDtoOrdering orderObj;													// 정렬 object
	private AdcDto adc;
	private Integer rowTotal;														// 장애 진단 로그 카운트
	private String searchKey;
	private Date fromPeriod;
	private Date toPeriod;
	private Long startTimeL;
	private Long endTimeL;
	private Integer fromRow;
	private Integer toRow;
	private Long logKey;															// 장애진단 결과 페이지 전환시 사용
	private Long scheduleIndex;														//	
	private List<OBDtoFaultCheckSchedule> faultCheckScheduleList;					// 지정된 장비의 할당된 장애 점검 예약 리스트
	private OBDtoFaultCheckSchedule faultCheckScheduleInfo;							// 장애 점검 예약 정보	
	private OBDtoFaultCheckResult faultCheckResult;									// 지정된 로그의 상세 정보 (진단결과)
	private ArrayList<OBDtoCpuMemStatus> faultAdcCpuMemoryList;						// 장애 점검 중에 발생된 cpu/memory 이력을 추출	
	private List<String> historyIndices;											// 진단 이력 삭제할 Logkey
	public ArrayList<OBDtoFaultCheckPacketLossInfo> faultCheckPacketLossInfoList;	// 패킷 손실 분석용 데이터 분석리스트
	public OBDtoFaultCheckResponseTimeInfo faultCheckResponseTimeInfo;				// 응답 시간 분석 정보
	public String packetResult = "";												// 패킷 데이터 추출
	public String packetLossInfoFileName = "";
	private Integer monitoringPeriod;
    private Integer intervalMonitor;
    
    public FaultHistoryAction()
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
		
	//장애 진단 로그 목록 개수를 제공
	public String retrieveFaultHistoryTotal() throws OBException 
	{
		isSuccessful = true;
		try 
		{
//			toPeriod = OBDateTimeWeb.initTimeOfDate(toPeriod, true);
//			log.debug("adc: {}, searchKey: {}, fromPeriod: {}, toPeriod: {}", new Object[]{adc, searchKey, fromPeriod, toPeriod});
			
			if (adcObject != null && adcObject.getIndex() != null)
			{
			    setSearchTime();
				
				OBDtoSearch searchObj = new OBDtoSearch();
				searchObj.setFromTime(fromPeriod);
				searchObj.setToTime(toPeriod);
				searchObj.setSearchKey(searchKey);
				searchObj.setBeginIndex(fromRow);
				searchObj.setEndIndex(toRow);
				log.debug("adcObject: {}, searchObj: {}", new Object[]{adcObject, searchObj});
				
				rowTotal = faultHistoryFacade.retrieveHistoryListTotal(adcObject, searchObj);
			}
//			rowTotal = 10;
			log.debug("row total: {}", rowTotal);
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
	public String loadFaultListContent() throws OBException
	{
		try
		{	
		    setSearchTime();
			OBDtoSearch searchObj = new OBDtoSearch();
			searchObj.setFromTime(fromPeriod);
			searchObj.setToTime(toPeriod);
			searchObj.setSearchKey(searchKey);		
			searchObj.setBeginIndex(fromRow);
			searchObj.setEndIndex(toRow);
			
			log.debug("adcObject:{}, searchObj:{}, orderObj:{}", new Object[]{adcObject, searchObj, orderObj});
			if (searchObj.getBeginIndex() !=null && searchObj.getBeginIndex() < 0)
			{
				faultCheckLogList = ListUtils.EMPTY_LIST; //Type safety: The expression of type List needs unchecked conversion to conform to List<FaultCheckLogDto>
			} 
			else 
			{				
				faultCheckLogList = faultHistoryFacade.getHistoryList(adcObject, searchObj, orderObj);					
				log.debug("faultCheckLogList:{}", faultCheckLogList);				
			}
			
			//지정된 장비의 할당된 장애 점검 예약 리스트를 조회
//			if (searchObj.getBeginIndex() !=null && searchObj.getBeginIndex() < 0)
//			{
//				faultCheckScheduleList = ListUtils.EMPTY_LIST; //Type safety: The expression of type List needs unchecked conversion to conform to List<FaultCheckLogDto>
//			} 
//			else
//			{
//				faultCheckScheduleList = faultHistoryFacade.getFaultCheckScheduleList(adcObject, searchObj, orderObj);
			faultCheckScheduleList = faultHistoryFacade.getFaultCheckScheduleList(adcObject, null, null);
			log.debug("faultCheckScheduleList:{}", faultCheckScheduleList);
//			}
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
	
	//장애 점검 예약 정보를 조회
	public String loadScheduleInfo() throws OBException
	{
		try
		{
			log.debug("adcObject:{}, scheduleIndex:{}", adcObject, scheduleIndex);
			faultCheckScheduleInfo = faultHistoryFacade.getFaultCheckScheduleInfo(scheduleIndex);	
			
			log.debug("faultCheckScheduleInfo : {}", faultCheckScheduleInfo);
			
//			Long templateIndex = faultCheckScheduleInfo.getTemplateIndex();
//			
//			log.debug("{}", templateIndex);
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
	
	//장애점검 예약리스트 예약 살제
	public String deleteFaultScheduleInfo() throws OBException
	{
		isSuccessful = true;
		try
		{
			OBDtoFaultCheckSchedule faultSchedule = new OBDtoFaultCheckSchedule();
			faultSchedule.setIndex(scheduleIndex);
			
			log.debug("scheduleIndex:{}", scheduleIndex);
			faultHistoryFacade.deleteFaultScheduleInfo(faultSchedule, session.getSessionDto());
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
	
	
	public String loadFaultResult() throws OBException
	{
		try
		{
			log.debug("adcObject:{}, adc:{}, logKey:{}", adcObject, adc, logKey);	
			
			if(adc == null)
			{
			    adc = new AdcDto();
	            adc.setName(faultHistoryFacade.getFaultAdcName(logKey));
			}
			
			faultCheckResult = faultHistoryFacade.getFaultCheckLogDetail(logKey);
			
//			if (adcObject.getCategory() == 2)
//			{
//				faultCheckResult = faultHistoryFacade.getFaultCheckLogDetail(logKey, adc.getType());
//			}
//			else
//			{
//				faultCheckResult = faultHistoryFacade.getFaultCheckLogDetail(logKey, "");
//			}
			log.debug("faultCheckResult:{}", faultCheckResult);
		}
		catch (OBException e)
		{
			e.printStackTrace();
			throw e;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}
	// 진단 이력 삭제
	public String delHistorys() throws OBException 
	{
		isSuccessful = true;
		try 
		{
			log.debug("delReports");
			faultHistoryFacade.delHistorys(historyIndices, session.getSessionDto());
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
	
	// 패킷 유실 덤프 파일 유무 체크
	public String checkSvcPktDumpDataExist() throws OBException
	{
		try
		{
			log.debug("logKey : {}", logKey);
			String dmpFileName = faultHistoryFacade.getDmpFileName(logKey);
			
			if (OBFileHandler.isFileExist(dmpFileName))
			{
				isSuccessful = true;
			}
			else
			{
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_PACKET_DUMP_FILE_NOT_EXIST);
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
	// 패킷 유실 덤프 파일 다운로드 
	public String downloadSvcPktDump() throws OBException
	{
		try
		{
			log.debug("logKey:{}", logKey);	
			String filePath = OBDefine.PKT_DUMP_FILE_PATH + logKey + ".pcap";
			log.debug("filePath : {}",  filePath);
			File file = new File(filePath);
			if (file != null)
			{
				setStrutsStream(file);
			}
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		return SUCCESS;
	}
	
	public String checkResultDataExist() throws Exception
	{
		isSuccessful = true;
		try
		{
			log.debug("adcObject:{}, adc:{}, logKey:{}", adcObject, adc, logKey);	
			
			faultCheckResult = faultHistoryFacade.getFaultCheckLogDetail(logKey);
			log.debug("faultCheckResult:{}", faultCheckResult);
			
			if(faultCheckResult==null)
			{
				isSuccessful = false;
				faultCheckResult = null;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_EXPORT_DATA_NOT_EXIST);
			}			
			else
			{
				isSuccessful = true;
				faultCheckResult = null;
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
	
	public String downloadResultData() throws Exception
	{
		try
		{
			CsvMaker csvMaker = new CsvMaker();
			log.debug("adcObject:{}, adc:{}, logKey:{}", adcObject, adc, logKey);	
			
			faultCheckResult = faultHistoryFacade.getFaultCheckLogDetail(logKey);
			
//			if (adcObject.getCategory() == 2)
//			{
//				faultCheckResult = faultHistoryFacade.getFaultCheckLogDetail(logKey, adc.getType());
//			}
//			else
//			{
//				faultCheckResult = faultHistoryFacade.getFaultCheckLogDetail(logKey, "");
//			}
			log.debug("faultCheckResult:{}", faultCheckResult);
			
			csvMaker.initWithResultContents(faultCheckResult);
			File csv = csvMaker.write();
			if (csv != null)
			{
				log.debug("{}",faultCheckResult);
				setStrutsStream(csv);	
			}
			else
			{
				log.debug("{}",faultCheckResult);
			}
			faultCheckResult = null;
		}
		catch(OBException e)
		{
			e.getMessage();
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage());
		}
		return SUCCESS;
	}
	
	public String loadPacketLossInfo() throws OBException
	{
		isSuccessful = true;	
		try
		{
			packetLossInfoFileName = faultHistoryFacade.getFaultCheckPacketLossInfoImgFileName(logKey, session.getLoginTime());
			log.debug("packetRossInfoFileName : {}", packetLossInfoFileName);
//			faultCheckPacketLossInfoList = faultHistoryFacade.getFaultCheckPacketLossInfo(logKey);			
//			log.debug("faultCheckPacketLossInfoList:{}", faultCheckPacketLossInfoList);
//			
//			if (!faultCheckPacketLossInfoList.isEmpty())
//			{
//				packetLossInfoFileName = faultHistoryFacade.getFaultCheckPacketLossInfoImgFileName(logKey, session.getLoginTime());
//				log.debug("packetRossInfoFileName : {}", packetLossInfoFileName);
//			}
//			else
//			{
//				packetLossInfoFileName = "";
//			}	
		}
		catch (OBException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			isSuccessful = false;			
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}
	
	public String loadProgressbarInfo() throws OBException
	{
		isSuccessful = true;	
		try
		{
			faultCheckResponseTimeInfo = faultHistoryFacade.getFaultCheckResponseTimeInfo(logKey);
			log.debug("faultCheckResponseTimeInfo:{}", faultCheckResponseTimeInfo);
		}
		catch (OBException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			isSuccessful = false;			
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}
	
	public String loadCpuChartInfo() throws OBException
	{
		isSuccessful = true;	
		try
		{
			faultAdcCpuMemoryList = faultHistoryFacade.getFaultAdcCpuMemoryList(logKey);
			intervalMonitor = monitoringPeriod;
			log.debug("{}", faultAdcCpuMemoryList);
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

	public OBDtoSearch getSearchObj()
	{
		return searchObj;
	}

	public void setSearchObj(OBDtoSearch searchObj)
	{
		this.searchObj = searchObj;
	}

	public OBDtoOrdering getOrderObj()
	{
		return orderObj;
	}

	public void setOrderObj(OBDtoOrdering orderObj)
	{
		this.orderObj = orderObj;
	}

	public AdcDto getAdc()
	{
		return adc;
	}

	public void setAdc(AdcDto adc)
	{
		this.adc = adc;
	}
	
	public Integer getRowTotal()
	{
		return rowTotal;
	}

	public void setRowTotal(Integer rowTotal)
	{
		this.rowTotal = rowTotal;
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

	public Long getLogKey()
	{
		return logKey;
	}

	public void setLogKey(Long logKey)
	{
		this.logKey = logKey;
	}

	public List<OBDtoFaultCheckSchedule> getFaultCheckScheduleList()
	{
		return faultCheckScheduleList;
	}

	public void setFaultCheckScheduleList(List<OBDtoFaultCheckSchedule> faultCheckScheduleList)
	{
		this.faultCheckScheduleList = faultCheckScheduleList;
	}

	public OBDtoFaultCheckSchedule getFaultCheckScheduleInfo()
	{
		return faultCheckScheduleInfo;
	}

	public void setFaultCheckScheduleInfo(OBDtoFaultCheckSchedule faultCheckScheduleInfo)
	{
		this.faultCheckScheduleInfo = faultCheckScheduleInfo;
	}

	public OBDtoFaultCheckResult getFaultCheckResult()
	{
		return faultCheckResult;
	}

	public void setFaultCheckResult(OBDtoFaultCheckResult faultCheckResult)
	{
		this.faultCheckResult = faultCheckResult;
	}
	
	public ArrayList<OBDtoCpuMemStatus> getFaultAdcCpuMemoryList()
	{
		return faultAdcCpuMemoryList;
	}
	
	public void setFaultAdcCpuMemoryList(ArrayList<OBDtoCpuMemStatus> faultAdcCpuMemoryList)
	{
		this.faultAdcCpuMemoryList = faultAdcCpuMemoryList;
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
	
	public Long getScheduleIndex()
	{
		return scheduleIndex;
	}

	public void setScheduleIndex(Long scheduleIndex)
	{
		this.scheduleIndex = scheduleIndex;
	}
	
	public ArrayList<OBDtoFaultCheckPacketLossInfo> getFaultCheckPacketLossInfoList()
	{
		return faultCheckPacketLossInfoList;
	}

	public void setFaultCheckPacketLossInfoList(ArrayList<OBDtoFaultCheckPacketLossInfo> faultCheckPacketLossInfoList)
	{
		this.faultCheckPacketLossInfoList = faultCheckPacketLossInfoList;
	}

	public OBDtoFaultCheckResponseTimeInfo getFaultCheckResponseTimeInfo()
	{
		return faultCheckResponseTimeInfo;
	}

	public void setFaultCheckResponseTimeInfo(OBDtoFaultCheckResponseTimeInfo faultCheckResponseTimeInfo)
	{
		this.faultCheckResponseTimeInfo = faultCheckResponseTimeInfo;
	}
	
	
	public String getPacketResult()
	{
		return packetResult;
	}

	public void setPacketResult(String packetResult)
	{
		this.packetResult = packetResult;
	}
	
	public String getPacketLossInfoFileName()
	{
		return packetLossInfoFileName;
	}

	public void setPacketLossInfoFileName(String packetLossInfoFileName)
	{
		this.packetLossInfoFileName = packetLossInfoFileName;
	}

	public OBDtoFaultCheckLog getFaultCheckLogInfo()
	{
		return faultCheckLogInfo;
	}

	public void setFaultCheckLogInfo(OBDtoFaultCheckLog faultCheckLogInfo)
	{
		this.faultCheckLogInfo = faultCheckLogInfo;
	}

	public List<String> getHistoryIndices()
	{
		return historyIndices;
	}

	public void setHistoryIndices(List<String> historyIndices)
	{
		this.historyIndices = historyIndices;
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
        return "FaultHistoryAction [faultHistoryFacade=" + faultHistoryFacade
                + ", faultCheckLogList=" + faultCheckLogList
                + ", faultCheckLogInfo=" + faultCheckLogInfo + ", adcObject="
                + adcObject + ", searchObj=" + searchObj + ", orderObj="
                + orderObj + ", adc=" + adc + ", rowTotal=" + rowTotal
                + ", searchKey=" + searchKey + ", fromPeriod=" + fromPeriod
                + ", toPeriod=" + toPeriod + ", startTimeL=" + startTimeL
                + ", endTimeL=" + endTimeL + ", fromRow=" + fromRow
                + ", toRow=" + toRow + ", logKey=" + logKey
                + ", scheduleIndex=" + scheduleIndex
                + ", faultCheckScheduleList=" + faultCheckScheduleList
                + ", faultCheckScheduleInfo=" + faultCheckScheduleInfo
                + ", faultCheckResult=" + faultCheckResult
                + ", faultAdcCpuMemoryList=" + faultAdcCpuMemoryList
                + ", historyIndices=" + historyIndices
                + ", faultCheckPacketLossInfoList="
                + faultCheckPacketLossInfoList
                + ", faultCheckResponseTimeInfo=" + faultCheckResponseTimeInfo
                + ", packetResult=" + packetResult
                + ", packetLossInfoFileName=" + packetLossInfoFileName
                + ", monitoringPeriod=" + monitoringPeriod
                + ", intervalMonitor=" + intervalMonitor + "]";
    }
}