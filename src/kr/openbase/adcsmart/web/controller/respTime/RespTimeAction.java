package kr.openbase.adcsmart.web.controller.respTime;


import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoRespGroup;
import kr.openbase.adcsmart.service.dto.OBDtoRespInfo;
import kr.openbase.adcsmart.service.dto.OBDtoRespMultiChartData;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvAdditional;
import kr.openbase.adcsmart.service.impl.OBEnvManagementImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.respTime.RespTimeFacade;
import kr.openbase.adcsmart.web.util.CsvMaker;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


@Controller
@Scope(value = "prototype")
public class RespTimeAction extends BaseAction
{
	private transient Logger log = LoggerFactory.getLogger(RespTimeAction.class);
	
	@Autowired
	private RespTimeFacade respTimeFacade;
	
	private String             FILE_NAME_PORT_USAGE_CSV = "port_usage";
    private String              FILE_NAME_UNUSED_SLB_CSV = "unused_slb";
    private String              FILE_NAME_USED_SLB_CSV   = "slb_session";
    private boolean             isInitailized = false;
    private String              contents;
    private String              adcType;
    private Integer             adcIndex;
    private String              adcIPAddress;
    private String              adcUserID;
    private String              adcPasswd;
    private Integer             searchType;// 미사용 SLB에서 사용됨.
    private int                 searchIPType;
    private String              searchIPAddress;
    private int                 connService;
    private int                 connPort;
    private ArrayList<AdcDto>   adcList;
    
 // 구간 응답시간
    private OBDtoADCObject adcObject;
    private OBDtoSearch searchOption;
    private Date startTime;
    private Date endTime;
    private Long startTimeL;
    private Long endTimeL;  
    private ArrayList<OBDtoRespGroup> respGroupInfoList;
    private OBDtoRespGroup respGroupInfo;
    private ArrayList<OBDtoRespInfo> respInfo;
    private String respName;
    private Integer respIndex;
    private List<String> respInfoIndexes;
    private ArrayList<OBDtoRespMultiChartData> ResponseTimeInfo;
    private Integer orderType;
    private Integer orderDir;
    private Integer monitoringPeriod;
    private Integer intervalMonitor;
    
    public RespTimeAction()
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
    
 // 유휴포트 정보를 화면에 표시함.
    public String portUsageLoadContent() throws OBException 
    {
        // 화면에 표시하고자하는 데이터를 저장한다.
        if(false == isInitailized)
        {
            contents = "";
            return SUCCESS;
        }
        
        try
        {
            contents = respTimeFacade.portUsageGetContent();
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
    //유휴포트 데이터 없을 경우 내보내기 알림 처리
    public String checkPortUsageDataExist() throws OBException
    {
        try
        {
            contents = respTimeFacade.portUsageGetContent();
            if(contents != null)
            {
                isSuccessful = true;            
            }
            else
            {
                isSuccessful = false;
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

    // 유휴포트 정보를 csv 형태의 파일로 저장하여 다운로드함.
    public String portUsageDownloadCsv() throws OBException 
    {
        try 
        {
            String [] header = respTimeFacade.portUsageMakeCsvHeader();
            ArrayList<String[]> data = respTimeFacade.portUsageMakeCsvBody();
            
            CsvMaker csvMaker = new CsvMaker();
            csvMaker.setHeader(header);
            csvMaker.setData(data);
            
            File csv = csvMaker.write(FILE_NAME_PORT_USAGE_CSV);
            if(csv != null)
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
            throw new OBException(e.getMessage());
        }
        
        return SUCCESS;
    }
    
    public String slbSessionLoadContent() throws OBException 
    {
        // slb 세션 검색 기능 
        if(null == adcIPAddress || adcIPAddress.isEmpty())
        {
            contents = "";
            return SUCCESS;
        }
        try
        {
            //  private String infoSess;
            if(adcType.equals("Alteon"))
            {
                contents = respTimeFacade.slbSessionGetContent(adcIPAddress ,adcType , adcUserID, adcPasswd, connService, connPort, searchIPType, searchIPAddress);             
            }
            else
            {
                contents = OBMessageWeb.MSG_SYSTOOL_NOT_SUPPORT_ADC;
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
    
    // session 정보를 csv 형태의 파일로 저장하여 다운로드함.
    public String slbSessionDownloadCsv() throws OBException 
    {
        try 
        {
            String content = contents.replaceAll("<br>", "\n"); 
            CsvMaker csvMaker = new CsvMaker();
            csvMaker.initWithSessionList(content);          
            File csv = csvMaker.write(FILE_NAME_USED_SLB_CSV);
            if(csv != null)
            setStrutsStream(csv);       
        } 
        catch (OBException e) 
        {
            throw e;
        }       
        catch (Exception e) 
        {
            throw new OBException(e.getMessage());
        }
        
        return SUCCESS;
    }
    
    public String unUsedSlbLoadContent() throws OBException 
    {
        // 미사용 slb 정보 
        try
        {
            adcList = respTimeFacade.getAdcsByAccountIndex(session.getAccountIndex());
            if(adcIndex==null)
                adcIndex=0;
            if(searchType==null)
                searchType=0;
            contents = respTimeFacade.unUsedSlbGetContent(session.getAccountIndex(), adcIndex, searchType);
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
    
    // 유휴포트 정보를 csv 형태의 파일로 저장하여 다운로드함.
    public String unUsedSlbDownloadCsv() throws OBException 
    {
        try 
        {
            String [] header = respTimeFacade.unUsedSlbMakeCsvHeader(session.getAccountIndex(), adcIndex);
            ArrayList<String[]> data = respTimeFacade.unUsedSlbMakeCsvBody(session.getAccountIndex(), adcIndex);
            
            CsvMaker csvMaker = new CsvMaker();
            csvMaker.setHeader(header);
            csvMaker.setData(data);
            
            File csv = csvMaker.write(FILE_NAME_UNUSED_SLB_CSV);
            if(csv != null)
                setStrutsStream(csv);
        } 
        catch (OBException e) 
        {
            throw e;
        }       
        catch (Exception e) 
        {
            throw new OBException(e.getMessage());
        }
        
        return SUCCESS;
    }   
    
    //
    public String loadResponseTimeContent() throws OBException
    {
//      try
//        {
//            
//        }
//      catch(OBException e)
//      {
//          throw e;
//      }
//        catch(Exception e)
//        {
//            throw new OBException(e.getMessage());
//        }
        
        return SUCCESS;
    }
    
    public String loadResponseInfoList() throws Exception
    {
        try 
        {
            setSearchTime();
            OBDtoSearch searchOption = new OBDtoSearch();
            searchOption.setFromTime(startTime);
            searchOption.setToTime(endTime);
            respGroupInfoList = respTimeFacade.getRespInfoList(searchOption, orderType, orderDir);
            log.debug("{}", respGroupInfoList);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            throw e;
        }
        
        return SUCCESS;
    }
    
    // 응답시간 Chart Data
    public String loadResponseTimeHistoryInfo() throws Exception
    {
        try
        {
            setSearchTime();
            OBDtoSearch searchOption = new OBDtoSearch();
            searchOption.setFromTime(startTime);
            searchOption.setToTime(endTime);
            
            if (respIndex != null)
            {                
                ResponseTimeInfo = respTimeFacade.getResponseTimeHistory(respIndex, searchOption);                
            }
            else
            {                
                ResponseTimeInfo = respTimeFacade.getResponseTimeHistory(null, searchOption);             
            }
            intervalMonitor = monitoringPeriod;
            log.debug("{}", ResponseTimeInfo);
        } 
        catch(OBException e)
        {
            e.printStackTrace();
            throw e;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new OBException(e.getMessage());
        }
        return SUCCESS;
    }
    
    public Boolean isExistRespIntervalCheck() throws Exception
    {
        isSuccessful = true;
        try
        {
            log.debug("respGroupInfo: {}", respGroupInfo);
            if(respTimeFacade.isExistRespIntervalCheck(respName))
            {
                isSuccessful = false;
                message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_NAME_DUPLICATED);
                return false;
            }
            respTimeFacade.addRespIntervalCheck(respGroupInfo);
            
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage()); 
        }
        
        return true;
//      return SUCCESS;
    }
    
    public String addRespIntervalCheck() throws Exception
    {
        isSuccessful = true;
        try
        {
            log.debug("respGroupInfo: {}", respGroupInfo);
            if(respTimeFacade.isExistRespIntervalCheck(respName))
            {
                isSuccessful = false;
                message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_NAME_DUPLICATED);                
            }            
            else
            {
                respTimeFacade.addRespIntervalCheck(respGroupInfo);
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
        
//        return true;
      return SUCCESS;
    }
    
    public String setRespIntervalCheck() throws Exception
    {
        isSuccessful = true;
        try
        {
            log.debug("respGroupInfo: {}", respGroupInfo);
//            if(respTimeFacade.isExistRespIntervalCheck(respName))
//            {
//                isSuccessful = false;
//                message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_NAME_DUPLICATED);
//                return false;
//            }            
            respTimeFacade.setRespIntervalCheck(respGroupInfo);
            
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage()); 
        }
        
//        return true;
      return SUCCESS;
    }
    
    public String delRespIntervalCheck() throws Exception
    {
        isSuccessful = true;
        try
        {
            log.debug("respInfoIndexes: {}", respInfoIndexes);
//            if(respTimeFacade.isExistRespIntervalCheck(respName))
//            {
//                isSuccessful = false;
//                message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_NAME_DUPLICATED);
//                return SUCCESS;
//            }
            respTimeFacade.delRespIntervalCheck(respInfoIndexes);
            
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
    
    public String loadRespInfoModifyContent() throws Exception
    {
        try 
        {
            respGroupInfo = respTimeFacade.getRespInfo(respIndex);
            log.debug("{}", respGroupInfo);
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

    public Logger getLog()
    {
        return log;
    }

    public void setLog(Logger log)
    {
        this.log = log;
    }

    public RespTimeFacade getRespTimeFacade()
    {
        return respTimeFacade;
    }

    public void setRespTimeFacade(RespTimeFacade respTimeFacade)
    {
        this.respTimeFacade = respTimeFacade;
    }

    public String getFILE_NAME_PORT_USAGE_CSV()
    {
        return FILE_NAME_PORT_USAGE_CSV;
    }

    public void setFILE_NAME_PORT_USAGE_CSV(String fILE_NAME_PORT_USAGE_CSV)
    {
        FILE_NAME_PORT_USAGE_CSV = fILE_NAME_PORT_USAGE_CSV;
    }

    public String getFILE_NAME_UNUSED_SLB_CSV()
    {
        return FILE_NAME_UNUSED_SLB_CSV;
    }

    public void setFILE_NAME_UNUSED_SLB_CSV(String fILE_NAME_UNUSED_SLB_CSV)
    {
        FILE_NAME_UNUSED_SLB_CSV = fILE_NAME_UNUSED_SLB_CSV;
    }

    public String getFILE_NAME_USED_SLB_CSV()
    {
        return FILE_NAME_USED_SLB_CSV;
    }

    public void setFILE_NAME_USED_SLB_CSV(String fILE_NAME_USED_SLB_CSV)
    {
        FILE_NAME_USED_SLB_CSV = fILE_NAME_USED_SLB_CSV;
    }

    public boolean isInitailized()
    {
        return isInitailized;
    }

    public void setInitailized(boolean isInitailized)
    {
        this.isInitailized = isInitailized;
    }

    public String getContents()
    {
        return contents;
    }

    public void setContents(String contents)
    {
        this.contents = contents;
    }

    public String getAdcType()
    {
        return adcType;
    }

    public void setAdcType(String adcType)
    {
        this.adcType = adcType;
    }

    public Integer getAdcIndex()
    {
        return adcIndex;
    }

    public void setAdcIndex(Integer adcIndex)
    {
        this.adcIndex = adcIndex;
    }

    public String getAdcIPAddress()
    {
        return adcIPAddress;
    }

    public void setAdcIPAddress(String adcIPAddress)
    {
        this.adcIPAddress = adcIPAddress;
    }

    public String getAdcUserID()
    {
        return adcUserID;
    }

    public void setAdcUserID(String adcUserID)
    {
        this.adcUserID = adcUserID;
    }

    public String getAdcPasswd()
    {
        return adcPasswd;
    }

    public void setAdcPasswd(String adcPasswd)
    {
        this.adcPasswd = adcPasswd;
    }

    public Integer getSearchType()
    {
        return searchType;
    }

    public void setSearchType(Integer searchType)
    {
        this.searchType = searchType;
    }

    public int getSearchIPType()
    {
        return searchIPType;
    }

    public void setSearchIPType(int searchIPType)
    {
        this.searchIPType = searchIPType;
    }

    public String getSearchIPAddress()
    {
        return searchIPAddress;
    }

    public void setSearchIPAddress(String searchIPAddress)
    {
        this.searchIPAddress = searchIPAddress;
    }

    public int getConnService()
    {
        return connService;
    }

    public void setConnService(int connService)
    {
        this.connService = connService;
    }

    public int getConnPort()
    {
        return connPort;
    }

    public void setConnPort(int connPort)
    {
        this.connPort = connPort;
    }

    public ArrayList<AdcDto> getAdcList()
    {
        return adcList;
    }

    public void setAdcList(ArrayList<AdcDto> adcList)
    {
        this.adcList = adcList;
    }

    public OBDtoADCObject getAdcObject()
    {
        return adcObject;
    }

    public void setAdcObject(OBDtoADCObject adcObject)
    {
        this.adcObject = adcObject;
    }

    public OBDtoSearch getSearchOption()
    {
        return searchOption;
    }

    public void setSearchOption(OBDtoSearch searchOption)
    {
        this.searchOption = searchOption;
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

    public ArrayList<OBDtoRespGroup> getRespGroupInfoList()
    {
        return respGroupInfoList;
    }

    public void setRespGroupInfoList(ArrayList<OBDtoRespGroup> respGroupInfoList)
    {
        this.respGroupInfoList = respGroupInfoList;
    }

    public OBDtoRespGroup getRespGroupInfo()
    {
        return respGroupInfo;
    }

    public void setRespGroupInfo(OBDtoRespGroup respGroupInfo)
    {
        this.respGroupInfo = respGroupInfo;
    }

    public ArrayList<OBDtoRespInfo> getRespInfo()
    {
        return respInfo;
    }

    public void setRespInfo(ArrayList<OBDtoRespInfo> respInfo)
    {
        this.respInfo = respInfo;
    }

    public String getRespName()
    {
        return respName;
    }

    public void setRespName(String respName)
    {
        this.respName = respName;
    }

    public Integer getRespIndex()
    {
        return respIndex;
    }

    public void setRespIndex(Integer respIndex)
    {
        this.respIndex = respIndex;
    }

    public List<String> getRespInfoIndexes()
    {
        return respInfoIndexes;
    }

    public void setRespInfoIndexes(List<String> respInfoIndexes)
    {
        this.respInfoIndexes = respInfoIndexes;
    }

    public ArrayList<OBDtoRespMultiChartData> getResponseTimeInfo()
    {
        return ResponseTimeInfo;
    }

    public void
            setResponseTimeInfo(ArrayList<OBDtoRespMultiChartData> responseTimeInfo)
    {
        ResponseTimeInfo = responseTimeInfo;
    }

    public Integer getOrderType()
    {
        return orderType;
    }

    public void setOrderType(Integer orderType)
    {
        this.orderType = orderType;
    }

    public Integer getOrderDir()
    {
        return orderDir;
    }

    public void setOrderDir(Integer orderDir)
    {
        this.orderDir = orderDir;
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
        return "RespTimeAction [respTimeFacade=" + respTimeFacade
                + ", FILE_NAME_PORT_USAGE_CSV=" + FILE_NAME_PORT_USAGE_CSV
                + ", FILE_NAME_UNUSED_SLB_CSV=" + FILE_NAME_UNUSED_SLB_CSV
                + ", FILE_NAME_USED_SLB_CSV=" + FILE_NAME_USED_SLB_CSV
                + ", isInitailized=" + isInitailized + ", contents=" + contents
                + ", adcType=" + adcType + ", adcIndex=" + adcIndex
                + ", adcIPAddress=" + adcIPAddress + ", adcUserID=" + adcUserID
                + ", adcPasswd=" + adcPasswd + ", searchType=" + searchType
                + ", searchIPType=" + searchIPType + ", searchIPAddress="
                + searchIPAddress + ", connService=" + connService
                + ", connPort=" + connPort + ", adcList=" + adcList
                + ", adcObject=" + adcObject + ", searchOption=" + searchOption
                + ", startTime=" + startTime + ", endTime=" + endTime
                + ", startTimeL=" + startTimeL + ", endTimeL=" + endTimeL
                + ", respGroupInfoList=" + respGroupInfoList
                + ", respGroupInfo=" + respGroupInfo + ", respInfo=" + respInfo
                + ", respName=" + respName + ", respIndex=" + respIndex
                + ", respInfoIndexes=" + respInfoIndexes + ", ResponseTimeInfo="
                + ResponseTimeInfo + ", orderType=" + orderType + ", orderDir="
                + orderDir + ", monitoringPeriod=" + monitoringPeriod
                + ", intervalMonitor=" + intervalMonitor + "]";
    }
}
