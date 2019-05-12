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
import kr.openbase.adcsmart.service.dto.OBDtoVlanInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoObjectIndexInfo;
import kr.openbase.adcsmart.service.impl.dto.OBDtoCpuMemStatus;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonL2Ports;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoPktdumpInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoPktdumpOption;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoPktdumpStatusInfo;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.fault.FaultAnalysisFacade;
import kr.openbase.adcsmart.web.util.OBFileHandler;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

@Controller
@Scope(value = "prototype")
public class FaultAnalysisAction extends BaseAction
{
	private transient Logger log = LoggerFactory.getLogger(FaultAnalysisAction.class);
	
	@Autowired
	private FaultAnalysisFacade	faultAnalysisFacade;	
	
	private OBDtoADCObject	adcObject;									// 현재 선택한 adc object.
	private OBDtoSearch	searchObj;										// 검색 object
	private OBDtoOrdering	orderObj;									// 정렬 object
	private Integer	rowTotal;											// 장애 진단 로그 카운트
	private AdcDto	adc;
	private String	searchKey;
	private Date	fromPeriod;
	private Date	toPeriod; 	
	private Long startTimeL;
    private Long endTimeL;
	private Integer	fromRow;
	private Integer	toRow;
	private Long	logKey;												// 장애진단 결과 페이지 전환시 사용
	private String 	logKeyText;											//	
	private List<OBDtoPktdumpInfo>	pktdumpInfoList;					// 지정된 장비의 패킷분석 리스트
	private OBDtoPktdumpInfo	pktdumpInfo;							// 패킷분석 정보
	private ArrayList<OBDtoObjectIndexInfo>	pktDumpIndexList;			// 패킷덤프 목록
	private List<OBDtoMonL2Ports>	portInterfaceNameList;				// 포트 인터페이스의 이름 목록
	private List<OBDtoVlanInfo>		vlanInterfaceNameList;				// VLAN 인터페이스 이름 목록
	private ArrayList<OBDtoPktdumpStatusInfo>	pktDumpStatusInfoList;	// 패킷 덤프 상태 조회
	private OBDtoPktdumpStatusInfo	pktDumpStatusInfo;					// 패킷 덤프 상태 조회
	private ArrayList<Long>	logKeyList;									//
//	private ArrayList<OBDtoCpuMemStatus> faultAdcCpuMemoryList;			// 패킷 수집 중에 발생된 cpu/memory 이력을 추출
	private OBDtoCpuMemStatus pktDumpCpuMemoryInfo;						// 패킷 수집 중에 발생된 cpu/memory 이력을 추출				
	private ArrayList<Long>	pktDumpIndices;								// 삭제 목록	
	private	ArrayList<OBDtoPktdumpOption>	pktDumpOption;				// 	
	private Date	occurTime;											//
	private	Date	endTime;											//
	private Integer	elapsedTime;										// 
	private Integer	status;												//
	private String	interfaceName = "";									//
	private String	vlanName = "";										//
	private	Long	fileSize = 0L;										// byte 단위.	
	private String	fileName = "";										//
	private String	strFilter;											//
	private Integer	optionMaxPkt = 100;	    							// 10000;
	private Integer	optionMaxTime = 60;  								// 600000;// msec. 600sec
	private Long	optionMaxSize = 10L;  							// 10000000L;// bytes. 10M
	private ArrayList<OBDtoPktdumpOption>	filterList;					// 필터 리스트		
	//필터 리스트 종류
    private String srcIp;
    private String srcPort;
    private String dstIp;
    private String dstPort;
    private String protocol;
    private String host;
    private String port;   
	
	//장애 패킷분석 목록 개수를 제공
	public String retrieveFaultAnalysisTotal() throws OBException 
	{
		isSuccessful = true;
		try 
		{
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
				
				rowTotal = faultAnalysisFacade.retrieveAnalysisListTotal(adcObject, searchObj);
			}
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
	//장애패킷분석 리스트 load
	public String loadFaultAnalysisListContent() throws OBException
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
				pktdumpInfoList = ListUtils.EMPTY_LIST; //Type safety: The expression of type List needs unchecked conversion to conform to List<FaultCheckLogDto>
			} 
			else 
			{				
				pktdumpInfoList = faultAnalysisFacade.getAnalysisList(adcObject, searchObj, orderObj);					
				log.debug("pktdumpInfoList:{}", pktdumpInfoList);	
			}				
			
			
			if (!adcObject.getCategory().equals(2) || !adcObject.getDesciption().equals("available"))
            {
                portInterfaceNameList = ListUtils.EMPTY_LIST;
                vlanInterfaceNameList = ListUtils.EMPTY_LIST;
            }
            else
            {
                portInterfaceNameList = faultAnalysisFacade.getPortInterfaceNameList(adcObject);    
                vlanInterfaceNameList = faultAnalysisFacade.getVlanInterfaceNameList(adcObject);
            }
			
			log.debug("portInterfaceNameList : {}", portInterfaceNameList);
			log.debug("vlanInterfaceNameList : {}", vlanInterfaceNameList);
			
//			portInterfaceNameList = faultAnalysisFacade.getPortInterfaceNameList(adcObject);
//			log.debug("portInterfaceNameList : {}", portInterfaceNameList);
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
	
	public String loadFaultAnalysisPort() throws OBException
	{
		//log.debug("adcObject: {}", adcObject);
//		isSuccessful = true;
//		try
//		{
//			portInterfaceNameList = faultAnalysisFacade.getPortInterfaceNameList(adcObject);
//			log.debug("portInterfaceNameList : {}", portInterfaceNameList);
//		}
//		catch (OBException e)
//		{
//			throw e;
//		}
//		catch (Exception e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
		return SUCCESS;
	}
	// fault Port정보
	public String loadFaultAnalysisPortContent() throws OBException
	{
		log.debug("adcObject: {}", adcObject);
//		isSuccessful = true;
		try
		{
			portInterfaceNameList = faultAnalysisFacade.getPortInterfaceNameList(adcObject);	
			vlanInterfaceNameList = faultAnalysisFacade.getVlanInterfaceNameList(adcObject);
			log.debug("portInterfaceNameList : {}", portInterfaceNameList);
			log.debug("vlanInterfaceNameList : {}", vlanInterfaceNameList);
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
	
	//패킷 삭제
	public String delPktDumps() throws OBException
	{
		isSuccessful = true;
		try
		{
			log.debug("pktDumpIndices : {}" , pktDumpIndices);
			faultAnalysisFacade.delPktDumps(pktDumpIndices, session.getSessionDto());
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
	
	//패킷 분석 시작
	public String startPktdump() throws OBException
	{
		isSuccessful = true;
		try
		{
			filterList = selectedFilterList(srcIp, dstIp, srcPort, dstPort, protocol, host, port);
			OBDtoPktdumpInfo dumpInfo = new OBDtoPktdumpInfo();
			dumpInfo.setAdcIndex(adcObject.getIndex());
			dumpInfo.setAdcName(adcObject.getName());
			dumpInfo.setFileName(fileName);
			dumpInfo.setInterfaceName(interfaceName);
//			dumpInfo.setVlanName(vlanName);
			dumpInfo.setOptionMaxPkt(optionMaxPkt);
			dumpInfo.setOptionMaxTime(optionMaxTime);
			dumpInfo.setOptionMaxSize(optionMaxSize);
			dumpInfo.setFilterList(filterList);
			
			pktDumpIndexList = faultAnalysisFacade.startPktdump(adcObject, dumpInfo, session.getSessionDto());
			
			log.debug("pktDumpIndexList : {}", pktDumpIndexList);
			log.debug("dumpInfo : {}", dumpInfo);
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
	
	//현재 패킷 덤프한 데이터 조회 
	public String loadPktDumpStatusListContent() throws OBException
	{
		try
		{
			log.debug("logKeyList: {}", logKeyList);
			pktDumpStatusInfoList = faultAnalysisFacade.getPktdumpStatusList(logKeyList);
			log.debug("pktDumpStatusInfoList: {}", pktDumpStatusInfoList);
			
//			pktDumpCpuMemoryInfo = faultAnalysisFacade.getFaultAdcCpuMemoryUsage(adcObject);
//			log.debug("{}", pktDumpCpuMemoryInfo);
		}
		catch (OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		return SUCCESS;
	}
	
	public String loadPktDumpStatusInfoContent() throws OBException
	{
		isSuccessful = true;
		try
		{
			log.debug("logKeyList: {}", logKeyList);
			pktDumpStatusInfoList = faultAnalysisFacade.getPktdumpStatusList(logKeyList);
			log.debug("pktDumpStatusInfoList: {}", pktDumpStatusInfoList);			
		}
		catch (OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		return SUCCESS;
	}
	
	//현재 패킷 덤프한 데이터 조회 
	public String loadPktDumpChartStatusListContent() throws OBException
	{
		try
		{
			pktDumpCpuMemoryInfo = faultAnalysisFacade.getFaultAdcCpuMemoryUsage(adcObject);
			log.debug("{}", pktDumpCpuMemoryInfo);
		}
		catch (OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		return SUCCESS;
	}
		
	//현재 패킷 덤프한 데이터 조회 
	/*public String loadPktdumpStatusContent(Long logKey) throws OBException
	{
		try
		{
			log.debug("logKeyList: {}", logKey);
			pktDumpStatusInfo = faultAnalysisFacade.getPktdumpStatus(logKey);
			log.debug("pktDumpStatusInfo: {}", pktDumpStatusInfo);
		}
		catch (OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		return SUCCESS;
	}*/
	public String loadCpuChartInfo() throws OBException
	{
		try
		{
			pktDumpCpuMemoryInfo = faultAnalysisFacade.getFaultAdcCpuMemoryUsage(adcObject);
			log.debug("{}", pktDumpCpuMemoryInfo);
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage());
		}
		return SUCCESS;
	}
/*	
	public String loadCpuChartInfo() throws OBException
	{
		isSuccessful = true;	
		try
		{
//			logKey = 3583328374772L;
//			faultAdcCpuMemoryList = faultAnalysisFacade.getFaultAdcCpuMemoryList(logKeyList);
//			faultAdcCpuMemoryList = faultAnalysisFacade.getFaultAdcCpuMemoryList(logKey);
			pktDumpCpuMemoryInfo = faultAnalysisFacade.getFaultAdcCpuMemoryUsage(adcObject);
			log.debug("{}", pktDumpCpuMemoryInfo);
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
*/	
	//패킷 덤프 작업을 중지
	public String stopPktdump() throws OBException
	{
		isSuccessful = true;
		try
		{
			log.debug("logKeyText: {}", logKeyText);
			
//			String logKey = "1384838660547,2484350288934,";
			String logKeyObj[] = logKeyText.split(",");
			
			ArrayList<Long> indexKeyList = new ArrayList<Long>();
			for (String logKeySvc : logKeyObj)
			{
				indexKeyList.add(Long.parseLong(logKeySvc));
			}
			
			logKeyList = indexKeyList;
			
			log.debug("logKeyList: {}", logKeyList);
			
			faultAnalysisFacade.stopPktdump(logKeyList, session.getSessionDto());		

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
	
	// 패킷분석 취소
	public String cancelPktdump() throws OBException
	{
		try
		{
			log.debug("logKeyText: {}", logKeyText);
			String logKeyObj[] = logKeyText.split(",");
			
			ArrayList<Long> indexKeyList = new ArrayList<Long>();
			for (String logKeySvc : logKeyObj)
			{
				indexKeyList.add(Long.parseLong(logKeySvc));
			}
			
			logKeyList = indexKeyList;
			faultAnalysisFacade.cancelPktdump(logKeyList, session.getSessionDto());
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
	
	public String checkDownloadPktDumpDataExist() throws OBException 
	{
		try 
		{
			log.debug("logKey : {}", logKey);
//			logKey = 3582141018292L;
			String pkDumpFileName = faultAnalysisFacade.getDumpFileName(logKey);
					
			if(OBFileHandler.isFileExist(pkDumpFileName))
			{
				isSuccessful = true;						
			}
			else
			{
				isSuccessful = false;				
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_PACKET_DUMP_FILE_NOT_EXIST);
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
	
	public String downloadPktDump() throws OBException 
	{
		try 
		{
			log.debug("logKey:{}", logKey);	
//			logKey = 3582141018292L;
			String filePath = OBDefine.PKT_DUMP_FILE_PATH + logKey+".pcap";
			
			File file = new File(filePath);
			if (file != null)
			{
				setStrutsStream(file);
			}			
		}
		catch (Exception e) 
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }		
		return SUCCESS;
	}
	private ArrayList<OBDtoPktdumpOption> selectedFilterList(String src_ip, String dst_ip, String src_port, String dst_port, String proto, String host, String port)throws OBException
	{
        try
        {
            ArrayList<OBDtoPktdumpOption> selectedFilterList = new ArrayList<OBDtoPktdumpOption>();
                    
            if(src_ip!=null && !src_ip.isEmpty())
            {
	            OBDtoPktdumpOption srcipSearch = new OBDtoPktdumpOption();
	            srcipSearch.setContent(src_ip);
	            srcipSearch.setType(OBDtoPktdumpOption.OPTION_TYPE_SRC_IP);
	            selectedFilterList.add(srcipSearch);
            }

            if(dst_ip!=null && !dst_ip.isEmpty())
            {
	            OBDtoPktdumpOption dstipSearch = new OBDtoPktdumpOption();
	            dstipSearch.setContent(dst_ip);
	            dstipSearch.setType(OBDtoPktdumpOption.OPTION_TYPE_DST_IP);
	            selectedFilterList.add(dstipSearch);
            }
            
            if(src_port!=null && !src_port.isEmpty())
            {
	            OBDtoPktdumpOption srcPortSearch = new OBDtoPktdumpOption();
	            srcPortSearch.setContent(src_port);
	            srcPortSearch.setType(OBDtoPktdumpOption.OPTION_TYPE_SRC_PORT);
	            selectedFilterList.add(srcPortSearch);
            }
            
            if(dst_port!=null && !dst_port.isEmpty())
            {
	            OBDtoPktdumpOption dstPortSearch = new OBDtoPktdumpOption();
	            dstPortSearch.setContent(dst_port);
	            dstPortSearch.setType(OBDtoPktdumpOption.OPTION_TYPE_DST_PORT);
	            selectedFilterList.add(dstPortSearch);
            }
            
            if(proto!=null && !proto.isEmpty())
            {
	            OBDtoPktdumpOption protocolSearch = new OBDtoPktdumpOption();
	            protocolSearch.setContent(proto);
	            protocolSearch.setType(OBDtoPktdumpOption.OPTION_TYPE_PROTOCOL);
	            selectedFilterList.add(protocolSearch);
            }
            
            if (host!=null && !host.isEmpty())
            {
            	OBDtoPktdumpOption hostSearch = new OBDtoPktdumpOption();
            	hostSearch.setContent(host);
            	hostSearch.setType(OBDtoPktdumpOption.OPTION_TYPE_HOST);
            	selectedFilterList.add(hostSearch);
            }
            
            if (port!=null && !port.isEmpty())
            {
            	OBDtoPktdumpOption portSearch = new OBDtoPktdumpOption();
            	portSearch.setContent(port);
            	portSearch.setType(OBDtoPktdumpOption.OPTION_TYPE_PORT);
            	selectedFilterList.add(portSearch);
            }
            return selectedFilterList;     	 
        }
        catch(Exception e)
        {
        	throw new OBException(e.getMessage());
        }
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

	public Integer getRowTotal()
	{
		return rowTotal;
	}

	public void setRowTotal(Integer rowTotal)
	{
		this.rowTotal = rowTotal;
	}

	public AdcDto getAdc()
	{
		return adc;
	}

	public void setAdc(AdcDto adc)
	{
		this.adc = adc;
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

	public Long getLogKey()
	{
		return logKey;
	}

	public void setLogKey(Long logKey)
	{
		this.logKey = logKey;
	}

	public List<OBDtoPktdumpInfo> getPktdumpInfoList()
	{
		return pktdumpInfoList;
	}

	public void setPktdumpInfoList(List<OBDtoPktdumpInfo> pktdumpInfoList)
	{
		this.pktdumpInfoList = pktdumpInfoList;
	}

	public OBDtoPktdumpInfo getPktdumpInfo()
	{
		return pktdumpInfo;
	}

	public void setPktdumpInfo(OBDtoPktdumpInfo pktdumpInfo)
	{
		this.pktdumpInfo = pktdumpInfo;
	}

	public ArrayList<OBDtoObjectIndexInfo> getPktDumpIndexList()
	{
		return pktDumpIndexList;
	}

	public void setPktDumpIndexList(ArrayList<OBDtoObjectIndexInfo> pktDumpIndexList)
	{
		this.pktDumpIndexList = pktDumpIndexList;
	}

	public List<OBDtoMonL2Ports> getPortInterfaceNameList()
	{
		return portInterfaceNameList;
	}

	public void setPortInterfaceNameList(List<OBDtoMonL2Ports> portInterfaceNameList)
	{
		this.portInterfaceNameList = portInterfaceNameList;
	}

	public ArrayList<OBDtoPktdumpStatusInfo> getPktDumpStatusInfoList()
	{
		return pktDumpStatusInfoList;
	}

	public void setPktDumpStatusInfoList(ArrayList<OBDtoPktdumpStatusInfo> pktDumpStatusInfoList)
	{
		this.pktDumpStatusInfoList = pktDumpStatusInfoList;
	}

	public OBDtoPktdumpStatusInfo getPktDumpStatusInfo()
	{
		return pktDumpStatusInfo;
	}

	public void setPktDumpStatusInfo(OBDtoPktdumpStatusInfo pktDumpStatusInfo)
	{
		this.pktDumpStatusInfo = pktDumpStatusInfo;
	}

	public ArrayList<Long> getLogKeyList()
	{
		return logKeyList;
	}

	public void setLogKeyList(ArrayList<Long> logKeyList)
	{
		this.logKeyList = logKeyList;
	}

	public OBDtoCpuMemStatus getPktDumpCpuMemoryInfo()
	{
		return pktDumpCpuMemoryInfo;
	}

	public void setPktDumpCpuMemoryInfo(OBDtoCpuMemStatus pktDumpCpuMemoryInfo)
	{
		this.pktDumpCpuMemoryInfo = pktDumpCpuMemoryInfo;
	}

	public ArrayList<Long> getPktDumpIndices()
	{
		return pktDumpIndices;
	}

	public void setPktDumpIndices(ArrayList<Long> pktDumpIndices)
	{
		this.pktDumpIndices = pktDumpIndices;
	}

	public ArrayList<OBDtoPktdumpOption> getPktDumpOption()
	{
		return pktDumpOption;
	}

	public void setPktDumpOption(ArrayList<OBDtoPktdumpOption> pktDumpOption)
	{
		this.pktDumpOption = pktDumpOption;
	}

	public Date getOccurTime()
	{
		return occurTime;
	}

	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}

	public Date getEndTime()
	{
		return endTime;
	}

	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}

	public Integer getElapsedTime()
	{
		return elapsedTime;
	}

	public void setElapsedTime(Integer elapsedTime)
	{
		this.elapsedTime = elapsedTime;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public String getInterfaceName()
	{
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName)
	{
		this.interfaceName = interfaceName;
	}

	public Long getFileSize()
	{
		return fileSize;
	}

	public void setFileSize(Long fileSize)
	{
		this.fileSize = fileSize;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getStrFilter()
	{
		return strFilter;
	}

	public void setStrFilter(String strFilter)
	{
		this.strFilter = strFilter;
	}

	public Integer getOptionMaxPkt()
	{
		return optionMaxPkt;
	}

	public void setOptionMaxPkt(Integer optionMaxPkt)
	{
		this.optionMaxPkt = optionMaxPkt;
	}

	public Integer getOptionMaxTime()
	{
		return optionMaxTime;
	}

	public void setOptionMaxTime(Integer optionMaxTime)
	{
		this.optionMaxTime = optionMaxTime;
	}

	public Long getOptionMaxSize()
	{
		return optionMaxSize;
	}

	public void setOptionMaxSize(Long optionMaxSize)
	{
		this.optionMaxSize = optionMaxSize;
	}

	public ArrayList<OBDtoPktdumpOption> getFilterList()
	{
		return filterList;
	}

	public void setFilterList(ArrayList<OBDtoPktdumpOption> filterList)
	{
		this.filterList = filterList;
	}

	public String getSrcIp()
	{
		return srcIp;
	}

	public void setSrcIp(String srcIp)
	{
		this.srcIp = srcIp;
	}

	public String getSrcPort()
	{
		return srcPort;
	}

	public void setSrcPort(String srcPort)
	{
		this.srcPort = srcPort;
	}

	public String getDstIp()
	{
		return dstIp;
	}

	public void setDstIp(String dstIp)
	{
		this.dstIp = dstIp;
	}

	public String getDstPort()
	{
		return dstPort;
	}

	public void setDstPort(String dstPort)
	{
		this.dstPort = dstPort;
	}

	public String getProtocol()
	{
		return protocol;
	}

	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
	}
	
	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public String getLogKeyText()
	{
		return logKeyText;
	}

	public void setLogKeyText(String logKeyText)
	{
		this.logKeyText = logKeyText;
	}

	public String getPort()
	{
		return port;
	}

	public void setPort(String port)
	{
		this.port = port;
	}	

	public String getVlanName()
	{
		return vlanName;
	}

	public void setVlanName(String vlanName)
	{
		this.vlanName = vlanName;
	}

	public List<OBDtoVlanInfo> getVlanInterfaceNameList()
	{
		return vlanInterfaceNameList;
	}

	public void setVlanInterfaceNameList(List<OBDtoVlanInfo> vlanInterfaceNameList)
	{
		this.vlanInterfaceNameList = vlanInterfaceNameList;
	}

    @Override
    public String toString()
    {
        return "FaultAnalysisAction [faultAnalysisFacade=" + faultAnalysisFacade + ", adcObject=" + adcObject + ", searchObj=" + searchObj + ", orderObj=" + orderObj + ", rowTotal=" + rowTotal + ", adc=" + adc + ", searchKey=" + searchKey + ", fromPeriod=" + fromPeriod + ", toPeriod=" + toPeriod + ", startTimeL=" + startTimeL + ", endTimeL=" + endTimeL + ", fromRow=" + fromRow + ", toRow=" + toRow + ", logKey=" + logKey + ", logKeyText=" + logKeyText + ", pktdumpInfoList=" + pktdumpInfoList + ", pktdumpInfo=" + pktdumpInfo + ", pktDumpIndexList=" + pktDumpIndexList + ", portInterfaceNameList=" + portInterfaceNameList + ", vlanInterfaceNameList=" + vlanInterfaceNameList + ", pktDumpStatusInfoList=" + pktDumpStatusInfoList + ", pktDumpStatusInfo=" + pktDumpStatusInfo + ", logKeyList=" + logKeyList + ", pktDumpCpuMemoryInfo=" + pktDumpCpuMemoryInfo + ", pktDumpIndices=" + pktDumpIndices + ", pktDumpOption=" + pktDumpOption + ", occurTime=" + occurTime + ", endTime=" + endTime + ", elapsedTime=" + elapsedTime + ", status=" + status + ", interfaceName=" + interfaceName + ", vlanName=" + vlanName + ", fileSize=" + fileSize + ", fileName=" + fileName + ", strFilter=" + strFilter + ", optionMaxPkt=" + optionMaxPkt + ", optionMaxTime=" + optionMaxTime + ", optionMaxSize=" + optionMaxSize + ", filterList=" + filterList + ", srcIp=" + srcIp + ", srcPort=" + srcPort + ", dstIp=" + dstIp + ", dstPort=" + dstPort + ", protocol=" + protocol + ", host=" + host + ", port=" + port + "]";
    }
}