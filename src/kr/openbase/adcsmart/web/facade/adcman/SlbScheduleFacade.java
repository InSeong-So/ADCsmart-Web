package kr.openbase.adcsmart.web.facade.adcman;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBAdcManagement;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSchedule;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSlbUser;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.AlteonVsAddDto;
import kr.openbase.adcsmart.web.facade.dto.F5VsAddDto;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;
import kr.openbase.adcsmart.web.facade.dto.VirtualSvrAlteonDto;
import kr.openbase.adcsmart.web.facade.dto.VirtualSvrF5Dto;

@Component
public class SlbScheduleFacade
{
    private static transient Logger log = LoggerFactory.getLogger(SlbScheduleFacade.class);
    
    private OBAdcManagement adcMgmt;

    public SlbScheduleFacade()
    {
        adcMgmt = new OBAdcManagementImpl();
    }
    
    public Integer getSlbScheduleListTotal(AdcDto adc, Integer accntIndex, String searchKey) throws OBException, Exception
    {
        return adcMgmt.getSlbScheduleListTotal(adc.getIndex(), accntIndex, searchKey);
    }
    
    public List<OBDtoAdcSchedule> getSlbScheduleList(AdcDto adc, String searchKey, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException, Exception
    {        
//        List<OBDtoAdcSchedule> schedules = new ArrayList<OBDtoAdcSchedule>();
        List<OBDtoAdcSchedule> schedules = adcMgmt.getSlbScheduleList(adc.getIndex(), searchKey, beginIndex, endIndex, orderType, orderDir);
        
        // smsReceive=tester,77778888|테스터,12345678
        
        
        log.debug("schedules: {}", schedules);
        return schedules;
    }
    
    public OBDtoAdcSchedule getSlbSchedule(Integer scheduleIndex) throws OBException, Exception
    {
        return adcMgmt.getSlbSchedule(scheduleIndex);
    }
    
    public boolean isExistSlbSchedule(OBDtoAdcSchedule adcSchedule) throws OBException, Exception
    {
        boolean exists = false;
        exists = adcMgmt.isExistSlbSchedule(adcSchedule);
        return exists;
    }
    
    public AlteonVsAddDto getVirtualServerAsAlteonVsAddDto(OBDtoAdcSchedule schedule) throws OBException, Exception 
    {        
        VirtualSvrAlteonDto virtualSvr = null;
        OBDtoAdcVServerAlteon virtualSvrFromSvc = schedule.getChunkAlteon().getVsConfig().getVsNew();
        log.debug("{}", virtualSvrFromSvc);
        virtualSvr = VirtualSvrAlteonDto.toVirtualSvrDto(virtualSvrFromSvc);
        log.debug("{}", virtualSvr);
        
        return makeAlteonVsAddDto(schedule.getAdcIndex(), virtualSvr);
    }
    
    private AlteonVsAddDto makeAlteonVsAddDto(int adcIndex, VirtualSvrAlteonDto virtualSvr) throws OBException, Exception 
    {
        log.debug("makeAlteonVsAddDto: {}, {}", adcIndex, virtualSvr);
        AlteonVsAddDto alteonVsAddDto = new AlteonVsAddDto();
        alteonVsAddDto.setIndex(virtualSvr.getIndex());
        alteonVsAddDto.setAdcIndex(adcIndex);
        alteonVsAddDto.setName(virtualSvr.getName());
        alteonVsAddDto.setIp(virtualSvr.getVirtualIp());
        alteonVsAddDto.setAlteonId(virtualSvr.getAlteonId());
        alteonVsAddDto.setVrrpState(virtualSvr.getVrrpState());
        alteonVsAddDto.setRouterId(virtualSvr.getRouterId());
        alteonVsAddDto.setVrId(virtualSvr.getVrId());
        alteonVsAddDto.setInterfaceNo(virtualSvr.getIntefaceNo());
        alteonVsAddDto.setSubInfo(virtualSvr.getSubInfo());
        alteonVsAddDto.setVirtualSvcs(virtualSvr.getVirtualSvcs());
        return alteonVsAddDto;
    }
    
    public F5VsAddDto getVirtualServerAsF5VsAddDto(OBDtoAdcSchedule schedule) throws OBException, Exception 
    {
        VirtualSvrF5Dto virtualSvr = null;
        OBDtoAdcVServerF5 virtualSvrFromSvc = schedule.getChunkF5().getVsConfig().getVsNew();
        virtualSvr = VirtualSvrF5Dto.toVirtualSvrDto(virtualSvrFromSvc);
        log.debug("{}", virtualSvr);
        return makeF5VsAddDto(schedule.getAdcIndex(), virtualSvr);
    }   
    private F5VsAddDto makeF5VsAddDto(int adcIndex, VirtualSvrF5Dto virtualSvr) throws OBException, Exception 
    {
        log.debug("makeF5VsAddDto: {}, {}", adcIndex, virtualSvr);
        F5VsAddDto f5VsAddDto = new F5VsAddDto();
        f5VsAddDto.setIndex(virtualSvr.getIndex());
        f5VsAddDto.setAdcIndex(adcIndex);
        f5VsAddDto.setIp(virtualSvr.getVirtualIp());
        f5VsAddDto.setName(virtualSvr.getName());
        if (virtualSvr.getPool() != null) {
            f5VsAddDto.setPoolIndex(virtualSvr.getPool().getIndex());
            f5VsAddDto.setPoolName(virtualSvr.getPool().getName());
            f5VsAddDto.setLoadBalancingType(virtualSvr.getPool().getLoadBalancingType());
            f5VsAddDto.setHealthCheckType(virtualSvr.getPool().getHealthCheckType());
            f5VsAddDto.setMembers(virtualSvr.getPool().getMembers());
        }
        
        f5VsAddDto.setPort(virtualSvr.getServicePort());
        f5VsAddDto.setProfileIndex(virtualSvr.getProfileIndex());
        f5VsAddDto.setVlanTunnel(virtualSvr.getVlanFilter());
        return f5VsAddDto;
    }
    
    public void modifySlbSchedule(OBDtoAdcSchedule schedule, AlteonVsAddDto alteonVsAdd, F5VsAddDto f5VsAdd, SessionDto session, Long startTimeL) throws OBException, Exception 
    {
        log.debug("modifyAlteonVs: {}, {}", schedule);
        
        OBDtoAdcVServerAlteon virtualAlteonSvrFromSvc = null;
        OBDtoAdcVServerF5 virtualF5SvrFromSvc = null;
        
        if(alteonVsAdd != null)
        {
            virtualAlteonSvrFromSvc = new OBDtoAdcVServerAlteon();
            virtualAlteonSvrFromSvc = AlteonVsAddDto.toOBDtoAdcVServer(alteonVsAdd);
            log.debug("{}", virtualAlteonSvrFromSvc);
            
            schedule.setVsIndex(virtualAlteonSvrFromSvc.getAdcIndex() + "_" + virtualAlteonSvrFromSvc.getAlteonId()); 
            schedule.setVsIp(virtualAlteonSvrFromSvc.getvIP());
            schedule.setVsName(virtualAlteonSvrFromSvc.getName());
        }
        
        if(f5VsAdd != null)
        {
            virtualF5SvrFromSvc = new OBDtoAdcVServerF5();
            virtualF5SvrFromSvc = F5VsAddDto.toOBDtoAdcVServer(f5VsAdd);
            log.debug("{}", virtualF5SvrFromSvc);
            
            schedule.setVsIndex(virtualF5SvrFromSvc.getIndex());
            schedule.setVsIp(virtualF5SvrFromSvc.getvIP());
            schedule.setVsName(virtualF5SvrFromSvc.getName());
        }
        schedule.setAccntIndex(session.getAccountIndex());
        schedule.setAccntIp(session.getClientIp());
        
        schedule.setChangeObjectType(0);
        schedule.setSummary("");  
        schedule.setState(0);
//        schedule.setNotice(30);
        Timestamp  inputTime= new Timestamp(startTimeL);
        schedule.setReservationTime(inputTime);
        
//        adcMgmt.setSlbSchedule(schedule, virtualF5SvrFromSvc, virtualAlteonSvrFromSvc);
        adcMgmt.setSlbSchedule(schedule, virtualF5SvrFromSvc, virtualAlteonSvrFromSvc);
    }
    
    public void addSlbSchedule(OBDtoSlbUser slbUser, OBDtoAdcSchedule schedule, AlteonVsAddDto alteonVsAdd, F5VsAddDto f5VsAdd, SessionDto session, Long startTimeL) throws OBException, Exception
    {
        log.debug("AlteonVsAddDto: {}", alteonVsAdd);
        
        OBDtoAdcVServerAlteon virtualAlteonSvrFromSvc = null;
        OBDtoAdcVServerF5 virtualF5SvrFromSvc = null;
        
        if(alteonVsAdd != null)
        {
            virtualAlteonSvrFromSvc = new OBDtoAdcVServerAlteon();
            virtualAlteonSvrFromSvc = AlteonVsAddDto.toOBDtoAdcVServer(alteonVsAdd);
            log.debug("{}", virtualAlteonSvrFromSvc);
            
            schedule.setVsIndex(virtualAlteonSvrFromSvc.getAdcIndex() + "_" + virtualAlteonSvrFromSvc.getAlteonId()); 
            schedule.setVsIp(virtualAlteonSvrFromSvc.getvIP());
            schedule.setVsName(virtualAlteonSvrFromSvc.getName());
        }
        
        if(f5VsAdd != null)
        {
            virtualF5SvrFromSvc = new OBDtoAdcVServerF5();
            virtualF5SvrFromSvc = F5VsAddDto.toOBDtoAdcVServer(f5VsAdd);
            log.debug("{}", virtualF5SvrFromSvc);
            
            schedule.setVsIndex(virtualF5SvrFromSvc.getIndex());
            schedule.setVsIp(virtualF5SvrFromSvc.getvIP());
            schedule.setVsName(virtualF5SvrFromSvc.getName());
        }
        
        schedule.setAccntIndex(session.getAccountIndex());
        schedule.setAccntIp(session.getClientIp());
        schedule.setChangeObjectType(0);
        schedule.setState(0);
        schedule.setSummary("");  
        Timestamp  inputTime= new Timestamp(startTimeL);
        schedule.setReservationTime(inputTime);
        
        adcMgmt.addSlbSchedule(slbUser, schedule, virtualF5SvrFromSvc, virtualAlteonSvrFromSvc);
    }
    
    public void delSlbSchedule(List<Integer> scheduleIndexes, SessionDto session) throws OBException, Exception
    {
        log.debug("delSchedule: {}", scheduleIndexes);
        OBDtoExtraInfo extraInfo = new OBDtoExtraInfo();
        extraInfo.setExtraMsg1(scheduleIndexes.toString());
        
        adcMgmt.delSlbSchedule(new ArrayList<Integer>(scheduleIndexes));
    }
    
//    public void addSlbSchedule(AlteonVsAddDto vsAdd, SessionDto session) throws OBException, Exception 
//    {
//        log.debug("AlteonVsAddDto: {}", vsAdd);
//        OBDtoAdcSchedule slbSchedule = OBDtoAdcSchedule.toOBDtoAdcVServer(vsAdd);
//                log.debug("{}", slbSchedule);
//        OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
//        extraInfo.setExtraMsg1(vsAdd.getName());
//        alteonVsMgmt.addVServerAlteon(virtualSvrFromSvc, extraInfo);
//        
//        adcMgmt.addSlbSchedule(slbSchedule);
//        log.debug("-- alteonVsMgmt.addVServerAlteon finished.");
//    }
    
    public Integer getSlbUserListTotal(Integer slbUserType, Integer accntIndex, String searchKey) throws OBException, Exception
    {
        return adcMgmt.getSlbUserListCount(slbUserType, accntIndex, searchKey);
    }
    
    public List<OBDtoSlbUser> getSlbUserList(Integer slbUserType, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException, Exception
    {
        List<OBDtoSlbUser> slbUsers = adcMgmt.getSlbUserList(slbUserType, beginIndex, endIndex, orderType, orderDir);
        log.debug("slbUsers: {}", slbUsers);
        return slbUsers;
    }
    
    public OBDtoSlbUser getSlbUSer(Integer slbUserIndex) throws OBException, Exception
    {
        return adcMgmt.getSlbUser(slbUserIndex);
    }
    
    public OBDtoSlbUser getLastRespUserInfo() throws Exception
    {
        return adcMgmt.getLastRespUserInfo();        
    }
    
    public void addSlbUser(OBDtoSlbUser slbUser) throws OBException, Exception
    {
        adcMgmt.addSlbUser(slbUser);
    }
    
    public void modifySlbUser(OBDtoSlbUser slbUser) throws OBException, Exception
    {
        adcMgmt.setSlbUser(slbUser);
    }
    
    public void delSlbUser(List<Integer> slbUserIndex) throws OBException
    {
        adcMgmt.delSlbUser(new ArrayList<Integer> (slbUserIndex));
    }
    
    public boolean isExistSlbUserCheck(OBDtoSlbUser slbUser) throws OBException, Exception
    {
        boolean existsUsers = false;
        existsUsers = adcMgmt.isExistSlbUserCheck(slbUser);
        return existsUsers;
    }
    
    public void senMessage(String userPhone) throws OBException, Exception
    {
        adcMgmt.addMessageToSMS(userPhone);
    }
    
    
//    public boolean setSlbSchedule(OBDtoAdcSchedule slbSchedule) throws OBException;
//    
//    public boolean delSlbSchedule(ArrayList<Integer> adcScheduleIndex) throws OBException;
}
