package kr.openbase.adcsmart.web.controller.adcman;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcSchedule;
import kr.openbase.adcsmart.service.dto.OBDtoSLBUpdateStatus;
import kr.openbase.adcsmart.service.dto.OBDtoSlbUser;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcVlan;
import kr.openbase.adcsmart.service.impl.f5.DtoVlanTunnelFilter;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.adcman.AdcFacade;
import kr.openbase.adcsmart.web.facade.adcman.ProfileFacade;
import kr.openbase.adcsmart.web.facade.adcman.SlbScheduleFacade;
import kr.openbase.adcsmart.web.facade.adcman.VirtualSvrFacade;
import kr.openbase.adcsmart.web.facade.dto.AdcALTEONHealthCheckDto;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.AdcNodeDto;
import kr.openbase.adcsmart.web.facade.dto.AdcPASKHealthCheckDto;
import kr.openbase.adcsmart.web.facade.dto.AdcPoolDto;
import kr.openbase.adcsmart.web.facade.dto.AlteonVsAddDto;
import kr.openbase.adcsmart.web.facade.dto.F5VsAddDto;
import kr.openbase.adcsmart.web.facade.dto.InterfaceDto;
import kr.openbase.adcsmart.web.facade.dto.ProfileDto;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;
import kr.openbase.adcsmart.web.facade.dto.VirtualSvrAlteonDto;
import kr.openbase.adcsmart.web.facade.dto.VirtualSvrDto;
import kr.openbase.adcsmart.web.facade.dto.VirtualSvrF5Dto;
import kr.openbase.adcsmart.web.util.OBDateTimeWeb;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Controller
@Scope(value = "prototype")
public class SlbScheduleAction extends BaseAction
{
    private transient Logger log = LoggerFactory.getLogger(SlbScheduleAction.class);
    
    @Autowired
    private VirtualSvrFacade vsScheduleFacade;
    @Autowired
    private ProfileFacade profileFacade;
    @Autowired
    private SlbScheduleFacade slbScheduleFacade;
    @Autowired
    private AdcFacade adcFacade;
    
    private Integer rowTotal;
    private Integer fromRow;
    private Integer toRow;
    private Integer orderDir;                       // 오른차순 = 1, 내림차순 = 2
    private Integer orderType;
    private String searchKey;
    private AdcDto adc;
    private String poolIndex;
    private String healthCheckDbIndex;
    private List<VirtualSvrDto> virtualServers;
    private F5VsAddDto f5VsAdd;
    private AlteonVsAddDto alteonVsAdd;
    private List<InterfaceDto> interfaces;
    private List<AdcPASKHealthCheckDto> adcHealths;
    private List<AdcALTEONHealthCheckDto> adcHealths_alteon;
    private List<AdcPoolDto> adcPools;
    private List<String> alteonPoolIndexList;
    private List<AdcNodeDto> adcNodes;
    private AdcPoolDto virtualSvc;
    private VirtualSvrAlteonDto virtualSvrAlteon;
    private VirtualSvrF5Dto virtualSvrF5;
    private AdcDto version; 
    private String version_; 
    private List<ProfileDto> profiles;              // F5 Profiles
    private String alteonVSIndex;
    private String alteonPoolIndex;
    private List<String> vsNameList;
    private List<OBDtoAdcVlan> availableVlans;  // 선택된 Vlan and Tunnel Traffic
    private DtoVlanTunnelFilter registeredVlanMap;      // 선택가능한 Vlan and Tunnel Traffic
    private Boolean refreshes = false;
    private Integer extraKey;                       // 1 최신 SLB 정보
    private Date startTime;
    private Long startTimeL;
    private Integer hour;
    private Integer min;
    private Date systemTime;    
    
    private OBDtoAdcSchedule schedule;
    private Integer scheduleIndex;
    private List<OBDtoAdcSchedule> scheduleList;
    private List<Integer> scheduleIndexes;
    private String smsReceiveCheckInString;
    private ArrayList<String> smsReceiveList;
    private List<Integer> slbUserIndexes;
    private List<OBDtoSlbUser> slbUserList;
    private OBDtoSlbUser slbUser;
    private OBDtoSlbUser slbReceiveUser;
    private OBDtoSlbUser slbUserModify;
    private Integer slbUserIndex;
    private Integer slbUserType;
    private String slbUserName;
    private String slbUserTeam;
    private String slbUserPhone;
    private String userPhone;   
    private String smsReceive;
    private String smsReceiveText;
    
    public SlbScheduleAction()
    {
        smsReceiveList = new ArrayList<String>();
    }
    
    public String retrieveSlbScheduleTotal() throws Exception
    {
        isSuccessful = true;
        try
        {
            log.debug("adc: {}, searchKey: {}", adc, searchKey);
            SessionDto sessionData = session.getSessionDto();
//            rowTotal = vsScheduleFacade.getVirtualSvrTotal(adc, sessionData.getAccountIndex(), searchKey);
            rowTotal = slbScheduleFacade.getSlbScheduleListTotal(adc, sessionData.getAccountIndex(), searchKey);
            log.debug("row Total: {}", rowTotal);
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
    
    public String loadListContent() throws Exception 
    {
        try 
        {
            log.debug("{}, searchKey:{}, fromRow:{}, toRow:{}", new Object[]{adc, searchKey, fromRow, toRow});
//            SessionDto sessionData = session.getSessionDto();
            
//            virtualServers = vsScheduleFacade.getVirtualServerList(adc, sessionData.getAccountIndex(), searchKey, fromRow, toRow, orderType, orderDir);
           scheduleList = slbScheduleFacade.getSlbScheduleList(adc, searchKey, fromRow, toRow, orderType, orderDir);
           
           int scheduleListSize = scheduleList.size();
           for(int i = 0; i < scheduleListSize; i++)
           {
               convertJson(scheduleList.get(i));
           }
                       
           log.debug("scheduleList: {}", scheduleList);
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
    
//    private String convertJson(List<OBDtoAdcSchedule> schedule)
//    {
//        int scheduleSize = schedule.size();
//        for(int i = 0; i < scheduleSize; i++)
//        {
//            JsonParser parser = new JsonParser();
//            JsonArray jarray = parser.parse(schedule.get(i).
//                    getSmsReceive()).getAsJsonArray(); //[{"name":"에이스","hp":"01011112222"},{"name":"샤오미","hp":"01055556666"}]
//            int jsonSize = jarray.size();
//            ArrayList<String> userInfo = new ArrayList<String>();
//            ArrayList<String> userNm = new ArrayList<String>();
//            ArrayList<String> userHp = new ArrayList<String>();
//            for(int j=0; j<jsonSize; j++)
//            {
//                JsonObject personObject = (JsonObject)jarray.get(j);
//                userInfo.add(personObject.get("name").getAsString());
//                userInfo.add(personObject.get("hp").getAsString());
//                userNm.add(personObject.get("name").getAsString());
//                userHp.add(personObject.get("hp").getAsString());
//            }
//            schedule.get(i).setUserName(userNm);
//            schedule.get(i).setUserPhone(userHp);
//        }
//        
//        return SUCCESS;
//    }
    
//    private String convertJson(OBDtoAdcSchedule schedule)
//    {       
//        JsonParser parser = new JsonParser();
//        JsonArray jarray = parser.parse(schedule.getSmsReceive()).getAsJsonArray();                    
//        int jsonSize = jarray.size();
//        ArrayList<String> userInfo = new ArrayList<String>();
//        ArrayList<String> userNm = new ArrayList<String>();
//        ArrayList<String> userHp = new ArrayList<String>();
//        for(int j=0; j<jsonSize; j++)
//        {
//            JsonObject personObject = (JsonObject)jarray.get(j);
//            userInfo.add(personObject.get("name").getAsString());
//            userInfo.add(personObject.get("hp").getAsString());
//            userNm.add(personObject.get("name").getAsString());
//            userHp.add(personObject.get("hp").getAsString());
//        }
//           
//        schedule.setUserInfo(userInfo);
//        schedule.setUserName(userNm);
//        schedule.setUserPhone(userHp);
//        
//        return SUCCESS;
//    }
    
    private String convertJson(OBDtoAdcSchedule schedule)
    {       
        JsonParser parser = new JsonParser();
        if(schedule.getSmsReceive().equals(null) || !schedule.getSmsReceive().equals(""))
        {
            JsonArray jarray = parser.parse(schedule.getSmsReceive()).getAsJsonArray();                    
            int jsonSize = jarray.size();
            HashMap<String, String> user = new HashMap<String, String>();
            for(int j=0; j<jsonSize; j++)
            {
                JsonObject personObject = (JsonObject)jarray.get(j);
                user.put(personObject.get("name").getAsString(),
                        personObject.get("hp").getAsString());
            }
            
            String userName ="";
            String userNamePhone ="";
            int userListSize = user.size();
            Iterator<String> iterator = user.keySet().iterator();
            int i=1;
            while (iterator.hasNext()) 
            {
                String key = (String) iterator.next();
                userName += key;
                userNamePhone += "이름:" + key + " , 전화번호:" + user.get(key);
                
                if(i != userListSize)
                {
                    userName += ",";
                    userNamePhone += ", ";
                }
                i++;
            }
            
            schedule.setUserNmHp(userNamePhone);
            schedule.setUserNm(userName);
        }
        else
        {
            schedule.setUserNmHp("");
            schedule.setUserNm("");
        }
            
        
        return SUCCESS;
    }
    //
    public String addSlbSchedule() throws Exception
    {
        isSuccessful = true;
        try
        {
            log.debug("{}", alteonVsAdd);   
//            slbUser.setIndex(slbUserIndex);
            
            slbUser.setIndex(slbUserIndex);
            slbUser.setType(slbUserType);
            slbUser.setName(slbUserName);
            slbUser.setTeam(slbUserTeam);
            slbUser.setPhone(slbUserPhone);
            
            if(slbScheduleFacade.isExistSlbUserCheck(slbUser))
            {
                slbScheduleFacade.modifySlbUser(slbUser);
            }
            else
            {
                slbScheduleFacade.addSlbUser(slbUser);
            }
            
//            schedule.setOriginUser(slbUser.getIndex());
            
            if(slbScheduleFacade.isExistSlbSchedule(schedule))
            {
                isSuccessful = false;
                message = "중복된 스케줄 설정이 존재합니다.";
            }
            else
            {
                log.debug("{}, {}, {}, {}, {}", startTime, startTimeL, hour, min, smsReceiveCheckInString);
                
                //slbUserName, slbUserPhone
                
                //[{"name":"test5","hp":"01055452354"},{"name":"test4","hp":"01000001111"},{"name":"test3","hp":"01011112222"},{"name":"test2","hp":"01025478654"}]
                if(!smsReceive.equals(""))
                    schedule.setSmsReceive(smsReceive);
                else
                    schedule.setSmsReceive(smsReceiveText);
                
                slbScheduleFacade.addSlbSchedule(slbUser, schedule, alteonVsAdd, f5VsAdd, session.getSessionDto(), startTimeL);
                message = "정상적으로 스케줄 등록 되었습니다.";
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
    
    public String modifySlbSchedule() throws Exception
    {
        isSuccessful = true;
        try 
        {
            log.debug("{}", alteonVsAdd);
            schedule.setIndex(scheduleIndex);
            
            if(!smsReceive.equals(""))
                schedule.setSmsReceive(smsReceive);
            else
                schedule.setSmsReceive(smsReceiveText);
            
            
            slbUser.setIndex(slbUserIndex);
            slbUser.setType(slbUserType);
            slbUser.setName(slbUserName);
            slbUser.setTeam(slbUserTeam);
            slbUser.setPhone(slbUserPhone);
            
            if(slbScheduleFacade.isExistSlbUserCheck(slbUser))
            {
                slbScheduleFacade.modifySlbUser(slbUser);
            }
            else
            {
                slbScheduleFacade.addSlbUser(slbUser);
            }
            
            slbScheduleFacade.modifySlbSchedule(schedule, alteonVsAdd, f5VsAdd, session.getSessionDto(), startTimeL);
//            message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_SLB_SUCCESS);
            message = "정상적으로 스케줄 등록 되었습니다.";
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
    
    public String delSlbSchedule() throws Exception
    {
        try
        {
            log.debug("adc:{}, scheduleChk:{}", adc, scheduleIndexes);
            slbScheduleFacade.delSlbSchedule(scheduleIndexes, session.getSessionDto());
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
    
    public String loadRefreshListContent() throws Exception 
    {
        isSuccessful = true;
        OBDtoSLBUpdateStatus retVal = new OBDtoSLBUpdateStatus();
        try 
        {
            log.debug("{}, searchKey:{}, fromRow:{}, toRow:{}, refreshes:{}", new Object[]{adc, searchKey, fromRow, toRow, refreshes});
            if (refreshes)
            {
                retVal = vsScheduleFacade.downloadVirtualServerList(adc);
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
    
    public String loadSlbScheduleAddContent() throws Exception 
    {
        try 
        {            
            slbUser = slbScheduleFacade.getLastRespUserInfo();
            log.debug("lastSlbUser : {}", slbUser);
            
            log.debug("{}", adc);
            systemTime = OBDateTimeWeb.toDate(OBDateTimeWeb.toTimestamp(OBDateTimeWeb.now()));
            // Alteon
            if(adc.getType().equals("Alteon"))
            {
                version = adcFacade.getAdc(adc.getIndex());
                this.version_ = version.getVersion().substring(0, 2);
                interfaces = vsScheduleFacade.getInterfaces(adc.getIndex(), adc.getType());
                log.debug("{}", interfaces);
            }
            // F5
            else if(adc.getType().equals("F5"))
            {
                adcPools = vsScheduleFacade.getAdcPools(adc.getIndex(), adc.getType());
                log.debug("{}", adcPools);
                adcNodes = vsScheduleFacade.getAdcNodes(adc.getIndex(), adc.getType(), null);
                log.debug("{}", adcNodes);
                availableVlans = vsScheduleFacade.getVlansAll(adc.getIndex());
                log.debug("{}", availableVlans);
            }
            else
            {
                
            }
//            slbUser = slbScheduleFacade.getLastRespUserInfo();
//            log.debug("lastSlbUser : {}", slbUser);
            
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
    
    public String loadSlbScheduleModifyContent() throws Exception 
    {
        try 
        {            
            slbUser = slbScheduleFacade.getLastRespUserInfo();
//            log.debug("lastSlbUser : {}", slbUser);
            schedule = slbScheduleFacade.getSlbSchedule(scheduleIndex);
            convertJson(schedule);
            log.debug("schedule : {}", schedule);
            
            log.debug("{}", adc, alteonVsAdd);
            systemTime = OBDateTimeWeb.toDate(OBDateTimeWeb.toTimestamp(OBDateTimeWeb.now()));
            // Alteon
            if(adc.getType().equals("Alteon"))
            {
                version = adcFacade.getAdc(adc.getIndex());
                this.version_ = version.getVersion().substring(0, 2);
//                alteonVsAdd = vsScheduleFacade.getVirtualServerAsAlteonVsAddDto(adc.getIndex(), alteonVsAdd.getIndex());                
                alteonVsAdd = slbScheduleFacade.getVirtualServerAsAlteonVsAddDto(schedule);
                
                log.debug("{}", alteonVsAdd);
                interfaces = vsScheduleFacade.getInterfaces(adc.getIndex(), adc.getType());
                log.debug("{}", interfaces);
            }
            // F5
            else if(adc.getType().equals("F5"))
            {
//                f5VsAdd = vsScheduleFacade.getVirtualSvrAsF5VsAddDto(adc.getIndex(), f5VsAdd.getIndex());
                f5VsAdd = slbScheduleFacade.getVirtualServerAsF5VsAddDto(schedule);
                                
                log.debug("{}", f5VsAdd);
                adcPools = vsScheduleFacade.getAdcPools(adc.getIndex(), adc.getType());
                log.debug("{}", adcPools);
                adcNodes = vsScheduleFacade.getAdcNodes(adc.getIndex(), adc.getType(), f5VsAdd.getPoolIndex());
                log.debug("{}", adcNodes);
                profiles = profileFacade.getProfiles(adc.getIndex(), null);
                log.debug("{}", profiles);
                
                // vlan and tunnel tarffic 정보 load
                registeredVlanMap = vsScheduleFacade.getRegisteredVlans(adc.getIndex(), f5VsAdd.getIndex());            
                log.debug("{}", registeredVlanMap);
                availableVlans = vsScheduleFacade.getVlans(adc.getIndex(), f5VsAdd.getIndex()); 
                log.debug("{}", availableVlans);
            }
            else
            {
                
            }
//            slbUser = slbScheduleFacade.getLastRespUserInfo();
//            log.debug("lastSlbUser : {}", slbUser);
            
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
    
//    public String loadSlbScheduleAlteonAddContent() throws Exception 
//    {
//        try 
//        {
//            log.debug("{}", adc);
//            version = adcFacade.getAdc(adc.getIndex());
//            this.version_ = version.getVersion().substring(0, 2);
//            interfaces = vsScheduleFacade.getInterfaces(adc.getIndex(), adc.getType());
//            log.debug("{}", interfaces);
//            
//            slbUser = slbScheduleFacade.getLastRespUserInfo();
//            log.debug("lastSlbUser : {}", slbUser);
//        } 
//        catch (OBException e) 
//        {
//            throw e;
//        }
//        catch (Exception e) 
//        {
//            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//        }
//        
//        return SUCCESS;
//    }
    
//    public String loadSlbScheduleF5AddContent() throws Exception 
//    {
//        try 
//        {
//            adcPools = vsScheduleFacade.getAdcPools(adc.getIndex(), adc.getType());
//            log.debug("{}", adcPools);
//            adcNodes = vsScheduleFacade.getAdcNodes(adc.getIndex(), adc.getType(), null);
//            log.debug("{}", adcNodes);
////            profiles = slbScheduleFacade.getProfiles(adc.getIndex(), null);
////            log.debug("{}", profiles);
//            
////          availableVlans = virtualSvrFacade.getVlans(adc.getIndex(), null);
//            availableVlans = vsScheduleFacade.getVlansAll(adc.getIndex());
//            log.debug("{}", availableVlans);
//        } 
//        catch (OBException e) 
//        {
//            throw e;
//        }
//        catch (Exception e) 
//        {
//            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//        }
//        
//        return SUCCESS;
//    }
    
    public String retrieveSlbListInfoTotal() throws Exception 
    {
        isSuccessful = true;
        try 
        {
            log.debug("adc: {}, searchKey: {}", adc, searchKey);
            SessionDto sessionData = session.getSessionDto();
                 
            rowTotal = vsScheduleFacade.getVirtualSvrTotal(adc, sessionData.getAccountIndex(), searchKey);
            
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
    
    public String loadSlbListInfoContent() throws OBException
    {
        try
        {
            log.debug("{}, searchKey:{}, fromRow:{}, toRow:{}", new Object[]{adc, searchKey, fromRow, toRow});
            SessionDto sessionData = session.getSessionDto();
            
            virtualServers = vsScheduleFacade.getVirtualServerList(adc, sessionData.getAccountIndex(), searchKey, fromRow, toRow, orderType, orderDir);
            log.debug("virtualServers: {}", virtualServers);
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
    
    public String retrieveSlbUsrListTotal() throws OBException
    {
        isSuccessful = true;
        try
        {
            log.debug("{}, slbUserType:{}, searchKey:{}", new Object[]{slbUserType, searchKey});
            SessionDto sessionData = session.getSessionDto();
            rowTotal = slbScheduleFacade.getSlbUserListTotal(slbUserType, sessionData.getAccountIndex(), searchKey);
            log.debug("row Total: {}", rowTotal);
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
    
    public String loadSlbUsrListContent() throws OBException
    {
        try
        {
            log.debug("{}, slbUserType:{}, fromRow:{}, toRow:{}", new Object[]{slbUserType, searchKey, fromRow, toRow});
            slbUserList = slbScheduleFacade.getSlbUserList(slbUserType, fromRow, toRow, 15, 2);
            
            log.debug("slbUserList: {}", slbUserList);
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
    
    public String loadSlbRespUserAddContent() throws OBException
    {
        return SUCCESS;
    }
    
    public String addSlbUser() throws OBException
    {
        try
        {
            slbUser.setIndex(slbUserIndex);
            slbUser.setType(slbUserType);
            slbUser.setName(slbUserName);
            slbUser.setTeam(slbUserTeam);
            slbUser.setPhone(slbUserPhone);
            slbScheduleFacade.addSlbUser(slbUser);
//            slbScheduleFacade.addSlbUser(slbReceiveUser);
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
    
    public String loadSlbUserModifyContent() throws OBException
    {
        try
        {
            slbUser = slbScheduleFacade.getSlbUSer(slbUserIndex);
            slbUserModify = slbScheduleFacade.getSlbUSer(slbUserIndex);
            log.debug("slbUser : {}", slbUserModify);
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
    
    public String loadSlbRespUserModifyContent() throws OBException
    {
        try
        {
            slbUser = slbScheduleFacade.getSlbUSer(slbUserIndex);
            log.debug("slbUser : {}", slbUserModify);
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
    
    public String modifySlbUser() throws OBException
    {
        isSuccessful = true;
        try
        {
            slbUser.setIndex(slbUserIndex);
            slbUser.setType(slbUserType);
            slbUser.setName(slbUserName);
            slbUser.setTeam(slbUserTeam);
            slbUser.setPhone(slbUserPhone);
            slbScheduleFacade.modifySlbUser(slbUser);
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
    
    public String delSlbUser() throws OBException
    {
        isSuccessful = true;
        try
        {
            log.debug("{}", slbUserIndexes);
            slbScheduleFacade.delSlbUser(slbUserIndexes);
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
    
    public String loadLastResponseUserInfo() throws OBException
    {
        try
        {
            slbUser = slbScheduleFacade.getLastRespUserInfo();
            log.debug("lastSlbUser : {}", slbUser);
        }
        catch(Exception e)
        {
            throw new OBException(e.getMessage()); 
        }
        return SUCCESS;
    }
    
    public String sendMeaage() throws OBException
    {
        isSuccessful = true;
        try
        {
            log.debug("{}", userPhone);
            slbScheduleFacade.senMessage(userPhone);
            
            message = "전송에 성공했습니다. ";             
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
    
    public Integer getRowTotal()
    {
        return rowTotal;
    }
    public void setRowTotal(Integer rowTotal)
    {
        this.rowTotal = rowTotal;
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
    public String getSearchKey()
    {
        return searchKey;
    }
    public void setSearchKey(String searchKey)
    {
        this.searchKey = searchKey;
    }
    public AdcDto getAdc()
    {
        return adc;
    }
    public void setAdc(AdcDto adc)
    {
        this.adc = adc;
    }
    public String getPoolIndex()
    {
        return poolIndex;
    }
    public void setPoolIndex(String poolIndex)
    {
        this.poolIndex = poolIndex;
    }
    public String getHealthCheckDbIndex()
    {
        return healthCheckDbIndex;
    }
    public void setHealthCheckDbIndex(String healthCheckDbIndex)
    {
        this.healthCheckDbIndex = healthCheckDbIndex;
    }
    public List<VirtualSvrDto> getVirtualServers()
    {
        return virtualServers;
    }
    public void setVirtualServers(List<VirtualSvrDto> virtualServers)
    {
        this.virtualServers = virtualServers;
    }
    public F5VsAddDto getF5VsAdd()
    {
        return f5VsAdd;
    }
    public void setF5VsAdd(F5VsAddDto f5VsAdd)
    {
        this.f5VsAdd = f5VsAdd;
    }
    public AlteonVsAddDto getAlteonVsAdd()
    {
        return alteonVsAdd;
    }
    public void setAlteonVsAdd(AlteonVsAddDto alteonVsAdd)
    {
        this.alteonVsAdd = alteonVsAdd;
    }
    public List<InterfaceDto> getInterfaces()
    {
        return interfaces;
    }
    public void setInterfaces(List<InterfaceDto> interfaces)
    {
        this.interfaces = interfaces;
    }
    public List<AdcPASKHealthCheckDto> getAdcHealths()
    {
        return adcHealths;
    }
    public void setAdcHealths(List<AdcPASKHealthCheckDto> adcHealths)
    {
        this.adcHealths = adcHealths;
    }
    public List<AdcALTEONHealthCheckDto> getAdcHealths_alteon()
    {
        return adcHealths_alteon;
    }
    public void
            setAdcHealths_alteon(List<AdcALTEONHealthCheckDto> adcHealths_alteon)
    {
        this.adcHealths_alteon = adcHealths_alteon;
    }
    public List<AdcPoolDto> getAdcPools()
    {
        return adcPools;
    }
    public void setAdcPools(List<AdcPoolDto> adcPools)
    {
        this.adcPools = adcPools;
    }
    public List<String> getAlteonPoolIndexList()
    {
        return alteonPoolIndexList;
    }
    public void setAlteonPoolIndexList(List<String> alteonPoolIndexList)
    {
        this.alteonPoolIndexList = alteonPoolIndexList;
    }
    public List<AdcNodeDto> getAdcNodes()
    {
        return adcNodes;
    }
    public void setAdcNodes(List<AdcNodeDto> adcNodes)
    {
        this.adcNodes = adcNodes;
    }
    public AdcPoolDto getVirtualSvc()
    {
        return virtualSvc;
    }
    public void setVirtualSvc(AdcPoolDto virtualSvc)
    {
        this.virtualSvc = virtualSvc;
    }
    public VirtualSvrAlteonDto getVirtualSvrAlteon()
    {
        return virtualSvrAlteon;
    }
    public void setVirtualSvrAlteon(VirtualSvrAlteonDto virtualSvrAlteon)
    {
        this.virtualSvrAlteon = virtualSvrAlteon;
    }
    public VirtualSvrF5Dto getVirtualSvrF5()
    {
        return virtualSvrF5;
    }
    public void setVirtualSvrF5(VirtualSvrF5Dto virtualSvrF5)
    {
        this.virtualSvrF5 = virtualSvrF5;
    }
    public AdcDto getVersion()
    {
        return version;
    }
    public void setVersion(AdcDto version)
    {
        this.version = version;
    }
    public String getVersion_()
    {
        return version_;
    }
    public void setVersion_(String version_)
    {
        this.version_ = version_;
    }
    public String getAlteonVSIndex()
    {
        return alteonVSIndex;
    }
    public void setAlteonVSIndex(String alteonVSIndex)
    {
        this.alteonVSIndex = alteonVSIndex;
    }
    public String getAlteonPoolIndex()
    {
        return alteonPoolIndex;
    }
    public void setAlteonPoolIndex(String alteonPoolIndex)
    {
        this.alteonPoolIndex = alteonPoolIndex;
    }
    public List<String> getVsNameList()
    {
        return vsNameList;
    }
    public void setVsNameList(List<String> vsNameList)
    {
        this.vsNameList = vsNameList;
    }
    public List<OBDtoAdcVlan> getAvailableVlans()
    {
        return availableVlans;
    }
    public void setAvailableVlans(List<OBDtoAdcVlan> availableVlans)
    {
        this.availableVlans = availableVlans;
    }
    public DtoVlanTunnelFilter getRegisteredVlanMap()
    {
        return registeredVlanMap;
    }
    public void setRegisteredVlanMap(DtoVlanTunnelFilter registeredVlanMap)
    {
        this.registeredVlanMap = registeredVlanMap;
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
    public OBDtoAdcSchedule getSchedule()
    {
        return schedule;
    }
    public void setSchedule(OBDtoAdcSchedule schedule)
    {
        this.schedule = schedule;
    }
    public List<OBDtoAdcSchedule> getScheduleList()
    {
        return scheduleList;
    }
    public void setScheduleList(List<OBDtoAdcSchedule> scheduleList)
    {
        this.scheduleList = scheduleList;
    }
    public List<Integer> getSlbUserIndexes()
    {
        return slbUserIndexes;
    }
    public void setSlbUserIndexes(List<Integer> slbUserIndexes)
    {
        this.slbUserIndexes = slbUserIndexes;
    }
    public List<OBDtoSlbUser> getSlbUserList()
    {
        return slbUserList;
    }
    public void setSlbUserList(List<OBDtoSlbUser> slbUserList)
    {
        this.slbUserList = slbUserList;
    }
    public OBDtoSlbUser getSlbUser()
    {
        return slbUser;
    }
    public void setSlbUser(OBDtoSlbUser slbUser)
    {
        this.slbUser = slbUser;
    }
    public Integer getSlbUserType()
    {
        return slbUserType;
    }
    public void setSlbUserType(Integer slbUserType)
    {
        this.slbUserType = slbUserType;
    }

    public Integer getSlbUserIndex()
    {
        return slbUserIndex;
    }

    public void setSlbUserIndex(Integer slbUserIndex)
    {
        this.slbUserIndex = slbUserIndex;
    }
    

    public OBDtoSlbUser getSlbUserModify()
    {
        return slbUserModify;
    }

    public void setSlbUserModify(OBDtoSlbUser slbUserModify)
    {
        this.slbUserModify = slbUserModify;
    }

    public String getUserPhone()
    {
        return userPhone;
    }

    public void setUserPhone(String userPhone)
    {
        this.userPhone = userPhone;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Long getStartTimeL()
    {
        return startTimeL;
    }

    public void setStartTimeL(Long startTimeL)
    {
        this.startTimeL = startTimeL;
    }

    public Integer getHour()
    {
        return hour;
    }

    public void setHour(Integer hour)
    {
        this.hour = hour;
    }

    public Integer getMin()
    {
        return min;
    }

    public void setMin(Integer min)
    {
        this.min = min;
    }

    public Date getSystemTime()
    {
        return systemTime;
    }

    public void setSystemTime(Date systemTime)
    {
        this.systemTime = systemTime;
    }

    public List<ProfileDto> getProfiles()
    {
        return profiles;
    }

    public void setProfiles(List<ProfileDto> profiles)
    {
        this.profiles = profiles;
    }

    public Integer getScheduleIndex()
    {
        return scheduleIndex;
    }

    public void setScheduleIndex(Integer scheduleIndex)
    {
        this.scheduleIndex = scheduleIndex;
    }

    public List<Integer> getScheduleIndexes()
    {
        return scheduleIndexes;
    }

    public void setScheduleIndexes(List<Integer> scheduleIndexes)
    {
        this.scheduleIndexes = scheduleIndexes;
    }

    public String getSmsReceiveCheckInString()
    {
        return smsReceiveCheckInString;
    }

    public void setSmsReceiveCheckInString(String smsReceiveCheckInString)
    {
        this.smsReceiveCheckInString = smsReceiveCheckInString;
    }

    public ArrayList<String> getSmsReceiveList()
    {
        return smsReceiveList;
    }

    public void setSmsReceiveList(ArrayList<String> smsReceiveList)
    {
        this.smsReceiveList = smsReceiveList;
    }

    public OBDtoSlbUser getSlbReceiveUser()
    {
        return slbReceiveUser;
    }

    public void setSlbReceiveUser(OBDtoSlbUser slbReceiveUser)
    {
        this.slbReceiveUser = slbReceiveUser;
    }

    public String getSlbUserName()
    {
        return slbUserName;
    }

    public void setSlbUserName(String slbUserName)
    {
        this.slbUserName = slbUserName;
    }

    public String getSlbUserTeam()
    {
        return slbUserTeam;
    }

    public void setSlbUserTeam(String slbUserTeam)
    {
        this.slbUserTeam = slbUserTeam;
    }

    public String getSlbUserPhone()
    {
        return slbUserPhone;
    }

    public void setSlbUserPhone(String slbUserPhone)
    {
        this.slbUserPhone = slbUserPhone;
    }

    public String getSmsReceive()
    {
        return smsReceive;
    }

    public void setSmsReceive(String smsReceive)
    {
        this.smsReceive = smsReceive;
    }

    public String getSmsReceiveText()
    {
        return smsReceiveText;
    }

    public void setSmsReceiveText(String smsReceiveText)
    {
        this.smsReceiveText = smsReceiveText;
    }    
}

