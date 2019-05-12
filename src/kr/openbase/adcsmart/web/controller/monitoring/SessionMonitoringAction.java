package kr.openbase.adcsmart.web.controller.monitoring;

import java.io.File;
import java.util.ArrayList;
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
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultSessionInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoSessionSearchOption;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.FaultSessionInfoDto;
import kr.openbase.adcsmart.web.facade.dto.SessionSearchOption;
import kr.openbase.adcsmart.web.facade.monitoring.SessionMonitoringFacade;
import kr.openbase.adcsmart.web.util.CsvMaker;
import kr.openbase.adcsmart.web.util.OBDefineWeb;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

@Controller
@Scope(value = "prototype")
public class SessionMonitoringAction extends BaseAction
{
	private transient Logger log = LoggerFactory.getLogger(SessionMonitoringAction.class);
	
	@Autowired
	private SessionMonitoringFacade sessionInfoFacade;
    
	private AdcDto adc;
	private Integer rowTotal = 0;
	private String searchKey;
	private Integer fromRow;
	private Integer toRow;	
	private Integer orderDir; 								// 오른차순 = 1, 내림차순 = 2
	private Integer orderType;
    private List<FaultSessionInfoDto> sessionList;
    private SessionSearchOption KeyWords;
    private OBDtoSearch searchObj;
    private OBDtoADCObject adcObj;
    private OBDtoOrdering orderObj;
    private String srcIp;
    private String srcPort;
    private String dstIp;
    private String dstPort;
    private String protocol;    
    private String realIP;// real server ip
    private String realPort;// real server port    
    private String agingTime; 
    private List<OBDtoFaultSessionInfo> searchList;  
    private ArrayList<OBDtoSessionSearchOption> sessionSearchList;
    private String selectedOption;
       
    public String retriveCount() throws OBException
    {
    	try
    	{
			if (adcObj != null && adcObj.getIndex() != null)
			{	
				OBDtoSearch searchObj = new OBDtoSearch();
				searchObj.setBeginIndex(fromRow);
				searchObj.setEndIndex(toRow);				
				rowTotal = sessionInfoFacade.getSessionListCount(adcObj, searchObj);		
			}		  
    	}
		catch(OBException e)
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
	public String loadDefaultPage() throws OBException
    {
    	try
    	{
    		sessionList = ListUtils.EMPTY_LIST;
    		sessionInfoFacade.cleanLocalSessionList(adcObj);
    	}
		catch (Exception e) 
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
    	
    	return SUCCESS;
    }

	public String loadSessionSearchListContent() throws OBException //검색의 경우 DB에쓴후 totalCount를 리턴하여 리스트를 구하는데 쓰도록 한다.
	{
		try
		{	
			if((srcIp !=null && srcIp.isEmpty() ==false) || (dstIp != null && dstIp.isEmpty() == false) || (realIP != null && realIP.isEmpty() == false))
			{	
				OBDtoSearch searchObj = new OBDtoSearch();
				searchObj.setSearchKey(searchKey);
				searchObj.setBeginIndex(fromRow);
				searchObj.setEndIndex(toRow);
				log.debug("adcObject: {}, searchObj: {}", new Object[]{adcObj, searchObj});
				sessionSearchList = sessionSearchOption(srcIp, dstIp, realIP, srcPort, dstPort, realPort, protocol, selectedOption, agingTime);
				rowTotal = sessionInfoFacade.getFaultSessionSearchList(adcObj, sessionSearchList, searchObj, orderObj);
				sessionList = sessionInfoFacade.getFaultSessionList(adcObj, searchObj, orderObj);
			}
			else
			{	
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_SESSION_SEARCH_KEYWORD);
			}
		}
		catch(OBException e)
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
	public String loadSortingListContent() throws OBException
	{
		try
		{	
			if (fromRow != null && fromRow < 0)
			{
				sessionList = ListUtils.EMPTY_LIST;
			}
			else if (adcObj != null && adcObj.getIndex() != null)
			{	
				OBDtoSearch searchObj = new OBDtoSearch();
				searchObj.setSearchKey(searchKey);
				searchObj.setBeginIndex(fromRow);
				searchObj.setEndIndex(toRow);
				log.debug("adcObject: {}, searchObj: {}", new Object[]{adcObj, searchObj});
				
				sessionList = sessionInfoFacade.getFaultSessionList(adcObj, searchObj, orderObj);
			}
		}
		catch(OBException e)
		{
			throw e;
		}
		catch (Exception e) 
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		return SUCCESS;
	}
	
    private ArrayList<OBDtoSessionSearchOption> sessionSearchOption(String srcIp, String dstIp, String realIP, String srcPort, String dstPort, String realPort, String protocol, String lbType, String agingTime) throws OBException
    {        
         try
         {
             ArrayList<OBDtoSessionSearchOption> searchList = new ArrayList<OBDtoSessionSearchOption>();
             
             if(srcIp == null && dstIp == null && realIP == null)
             {
                  isSuccessful = false;
                  message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_SESSION_SEARCH_KEYWORD);
             }
             
             if(srcIp!=null && !srcIp.isEmpty())
             {
	             OBDtoSessionSearchOption srcipSearch = new OBDtoSessionSearchOption();
	             srcipSearch.setContent(srcIp);
	             srcipSearch.setType(OBDtoSessionSearchOption.OPTION_TYPE_SRC_IP);
	             srcipSearch.setLbType(lbType);
	             searchList.add(srcipSearch);
             }
             
             if(dstIp!=null && !dstIp.isEmpty())
             {
	             OBDtoSessionSearchOption dstipSearch = new OBDtoSessionSearchOption();
	             dstipSearch.setContent(dstIp);
	             dstipSearch.setType(OBDtoSessionSearchOption.OPTION_TYPE_DST_IP);
	             dstipSearch.setLbType(lbType);
	             searchList.add(dstipSearch);
	         }
             
             if(srcPort!=null && !srcPort.isEmpty())
             {
	             OBDtoSessionSearchOption srcPortSearch = new OBDtoSessionSearchOption();
	             srcPortSearch.setContent(srcPort);
	             srcPortSearch.setType(OBDtoSessionSearchOption.OPTION_TYPE_SRC_PORT);
	             srcPortSearch.setLbType(lbType);
	             searchList.add(srcPortSearch);
             }
             
             if(dstPort!=null && !dstPort.isEmpty())
             {
	             OBDtoSessionSearchOption dstPortSearch = new OBDtoSessionSearchOption();
	             dstPortSearch.setContent(dstPort);
	             dstPortSearch.setType(OBDtoSessionSearchOption.OPTION_TYPE_DST_PORT);
	             dstPortSearch.setLbType(lbType);
	             searchList.add(dstPortSearch);
             }
             
             if(protocol!=null && !protocol.isEmpty())
             {
	             OBDtoSessionSearchOption protocolSearch = new OBDtoSessionSearchOption();
	             protocolSearch.setContent(protocol);
	             protocolSearch.setType(OBDtoSessionSearchOption.OPTION_TYPE_PROTOCOL);
	             protocolSearch.setLbType(lbType);
	             searchList.add(protocolSearch);
             }
             
             if(realIP!=null && !realIP.isEmpty())
             {
	             OBDtoSessionSearchOption realIPSearch = new OBDtoSessionSearchOption();
	             realIPSearch.setContent(realIP);
	             realIPSearch.setType(OBDtoSessionSearchOption.OPTION_TYPE_REAL_IP);
	             realIPSearch.setLbType(lbType);
	             searchList.add(realIPSearch);
             }
             
             if(realPort!=null && !realPort.isEmpty())
             {
	             OBDtoSessionSearchOption realPortSearch = new OBDtoSessionSearchOption();
	             realPortSearch.setContent(realPort);
	             realPortSearch.setType(OBDtoSessionSearchOption.OPTION_TYPE_REAL_PORT);
	             realPortSearch.setLbType(lbType);
	             searchList.add(realPortSearch);
             }
             
             if(agingTime!=null && !agingTime.isEmpty())
             {
                 OBDtoSessionSearchOption agingTimeSearch = new OBDtoSessionSearchOption();
                 agingTimeSearch.setContent(agingTime);
                 agingTimeSearch.setType(OBDtoSessionSearchOption.OPTION_TYPE_AGING_TIME);
                 agingTimeSearch.setLbType(lbType);
                 searchList.add(agingTimeSearch);
             }
             return searchList;     	 
         }
         catch(Exception e)
         {
        	 throw new OBException(e.getMessage());
         }
    }	
    
	public String checkExportSessionInfoDataExist() throws Exception
	{
		try
		{
//			sessionList = sessionInfoFacade.getFaultSessionList(adcObj ,searchObj , orderObj);
			if((srcIp !=null && srcIp.isEmpty() ==false) || (dstIp != null && dstIp.isEmpty() == false) || (realIP != null && realIP.isEmpty() == false))
			{	
				sessionList = sessionInfoFacade.getFaultSessionList(adcObj ,searchObj , orderObj);
				
				if(sessionList != null && sessionList.isEmpty() == false)
				{
					isSuccessful = true;
					sessionList = null;
				}
				else
				{
					isSuccessful = false;
					sessionList = null;
					message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_EXPORT_DATA_NOT_EXIST);
				}
			}
			else
			{
				isSuccessful = false;
				sessionList = null;
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
	
	public String downloadfaultSessionInfo() throws Exception
	{
		try
		{
			CsvMaker csvMaker = new CsvMaker();
			sessionList = sessionInfoFacade.getFaultSessionList(adcObj ,searchObj , orderObj);
//			if(adc.getType()=="Alteon")
			if(adc.getType().equals(OBDefineWeb.SESSION_ALTEON))	
			{
				csvMaker.initWithAlteonSessionListContents(sessionList, selectedOption);
			}
			else
			{
				csvMaker.initWithSessionListContents(sessionList);
			}									
//			csvMaker.initWithSessionListContents(sessionList);
			File csv = csvMaker.write();
			if (csv != null)
			{
				log.debug("{}",sessionList);
				setStrutsStream(csv);	
			}
			else
			{
				log.debug("{}",sessionList);
			}
			sessionList = null;
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
	
	public Logger getLog()
	{
		return log;
	}
	public void setLog(Logger log)
	{
		this.log = log;
	}
	public SessionMonitoringFacade getSessionInfoFacade()
	{
		return sessionInfoFacade;
	}
	public void setSessionInfoFacade(SessionMonitoringFacade sessionInfoFacade)
	{
		this.sessionInfoFacade = sessionInfoFacade;
	}
	public AdcDto getAdc()
	{
		return adc;
	}
	public void setAdc(AdcDto adc)
	{
		this.adc = adc;
	}

	public List<FaultSessionInfoDto> getSessionList()
	{
		return sessionList;
	}
	public void setSessionList(List<FaultSessionInfoDto> sessionList)
	{
		this.sessionList = sessionList;
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
	public Integer getRowTotal()
	{
		return rowTotal;
	}
	public void setRowTotal(Integer rowTotal)
	{
		this.rowTotal = rowTotal;
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
	public OBDtoADCObject getAdcObj()
	{
		return adcObj;
	}
	public void setAdcObj(OBDtoADCObject adcObj)
	{
		this.adcObj = adcObj;
	}
	public SessionSearchOption getKeyWords()
	{
		return KeyWords;
	}
	public void setKeyWords(SessionSearchOption keyWords)
	{
		KeyWords = keyWords;
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
	public String getProtocol()
	{
		return protocol;
	}
	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
	}
	public List<OBDtoFaultSessionInfo> getSearchList()
	{
		return searchList;
	}
	public void setSearchList(List<OBDtoFaultSessionInfo> searchList)
	{
		this.searchList = searchList;
	}
	public ArrayList<OBDtoSessionSearchOption> getSessionSearchList()
	{
		return sessionSearchList;
	}
	public void setSessionSearchList(
			ArrayList<OBDtoSessionSearchOption> sessionSearchList)
	{
		this.sessionSearchList = sessionSearchList;
	}
	public String getRealIP()
	{
		return realIP;
	}
	public void setRealIP(String realIP)
	{
		this.realIP = realIP;
	}
	public String getRealPort()
	{
		return realPort;
	}
	public void setRealPort(String realPort)
	{
		this.realPort = realPort;
	}
	public String getSelectedOption()
	{
		return selectedOption;
	}
	public void setSelectedOption(String selectedOption)
	{
		this.selectedOption = selectedOption;
	}
    public String getAgingTime()
    {
        return agingTime;
    }
    public void setAgingTime(String agingTime)
    {
        this.agingTime = agingTime;
    }
    @Override
    public String toString()
    {
        return "SessionMonitoringAction [sessionInfoFacade="
                + sessionInfoFacade + ", adc=" + adc + ", rowTotal=" + rowTotal
                + ", searchKey=" + searchKey + ", fromRow=" + fromRow
                + ", toRow=" + toRow + ", orderDir=" + orderDir
                + ", orderType=" + orderType + ", sessionList=" + sessionList
                + ", KeyWords=" + KeyWords + ", searchObj=" + searchObj
                + ", adcObj=" + adcObj + ", orderObj=" + orderObj + ", srcIp="
                + srcIp + ", srcPort=" + srcPort + ", dstIp=" + dstIp
                + ", dstPort=" + dstPort + ", protocol=" + protocol
                + ", realIP=" + realIP + ", realPort=" + realPort
                + ", agingTime=" + agingTime + ", searchList=" + searchList
                + ", sessionSearchList=" + sessionSearchList
                + ", selectedOption=" + selectedOption + "]";
    }
	
	
}