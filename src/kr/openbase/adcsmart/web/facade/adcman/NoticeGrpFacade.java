package kr.openbase.adcsmart.web.facade.adcman;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.OBAdcManagement;
import kr.openbase.adcsmart.service.OBAdcVServer;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNoticeGroup;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerNotice;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.alteon.OBAdcVServerAlteon;
import kr.openbase.adcsmart.service.impl.f5.OBAdcVServerF5;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NoticeGrpFacade
{
    private static transient Logger log = LoggerFactory.getLogger(NoticeGrpFacade.class);
    
    private OBAdcVServer f5VsMgmt;
    private OBAdcVServer alteonVsMgmt;
    private OBAdcManagement mgmt;
    
    public NoticeGrpFacade()
    {
        f5VsMgmt = new OBAdcVServerF5();
        alteonVsMgmt = new OBAdcVServerAlteon();
        mgmt = new OBAdcManagementImpl();
    }
    
    public Integer searchVServerNoticeOnCount(AdcDto adc, Integer accntIndex, String searchKey) throws OBException, Exception
    {
        return f5VsMgmt.searchVServerNoticeOnListCount(adc.getIndex(), accntIndex, searchKey);
    }
    
    public Integer searchVServerNoticeOffCount(AdcDto adc, Integer accntIndex, String searchKey) throws OBException, Exception
    {
        return f5VsMgmt.searchVServerNoticeOffListCount(adc.getIndex(), accntIndex, searchKey);       
    }
    
    public ArrayList<OBDtoAdcVServerNotice> searchVServerNoticeOnList(AdcDto adc, Integer accntIndex, String searchKey, Integer fromRow, Integer toRow, Integer orderType, Integer orderDir) throws OBException, Exception
    {
//        ArrayList<OBDtoAdcVServerNotice> adcVServerNoticeFromSvc = new ArrayList<OBDtoAdcVServerNotice>();
        
        ArrayList<OBDtoAdcVServerNotice> adcVServerNoticeOnFromSvc = new ArrayList<OBDtoAdcVServerNotice>();
        if(adc.getType().equals(OBDefine.ADC_TYPESTR_ALTEON))
            adcVServerNoticeOnFromSvc = alteonVsMgmt.searchVServerNoticeOnList(adc.getIndex(), accntIndex, searchKey, fromRow, toRow, orderType, orderDir);
        else
            adcVServerNoticeOnFromSvc = f5VsMgmt.searchVServerNoticeOnList(adc.getIndex(), accntIndex, searchKey, fromRow, toRow, orderType, orderDir);
                
//        log.debug("{}", adcVServerNoticeOnFromSvc);
        
        return adcVServerNoticeOnFromSvc;
//        ArrayList<OBDtoAdcVServerNotice> adcVServerNoticeOffFromSvc = new ArrayList<OBDtoAdcVServerNotice>();
//        adcVServerNoticeOffFromSvc = f5VsMgmt.searchVServerNoticeOffList(adc.getIndex(), accntIndex, searchKey, fromRow, toRow, orderType, orderDir);
//        log.debug("{}", adcVServerNoticeOffFromSvc);
//        
//        adcVServerNoticeFromSvc.addAll(adcVServerNoticeOnFromSvc);
//        adcVServerNoticeFromSvc.addAll(adcVServerNoticeOffFromSvc);
        
//        return adcVServerNoticeFromSvc;        
    }
    
    public ArrayList<OBDtoAdcVServerNotice> searchVServerNoticeOffList(AdcDto adc, Integer accntIndex, String searchKey, Integer fromRow, Integer toRow, Integer orderType, Integer orderDir) throws OBException, Exception
    {
//        ArrayList<OBDtoAdcVServerNotice> adcVServerNoticeFromSvc = new ArrayList<OBDtoAdcVServerNotice>();
        
//        ArrayList<OBDtoAdcVServerNotice> adcVServerNoticeOnFromSvc = new ArrayList<OBDtoAdcVServerNotice>();
//        adcVServerNoticeOnFromSvc = f5VsMgmt.searchVServerNoticeOnList(adc.getIndex(), accntIndex, searchKey, fromRow, toRow, orderType, orderDir);
//        log.debug("{}", adcVServerNoticeOnFromSvc);
        
        ArrayList<OBDtoAdcVServerNotice> adcVServerNoticeOffFromSvc = new ArrayList<OBDtoAdcVServerNotice>();
        if(adc.getType().equals(OBDefine.ADC_TYPESTR_ALTEON))
           adcVServerNoticeOffFromSvc = alteonVsMgmt.searchVServerNoticeOffList(adc.getIndex(), accntIndex, searchKey, fromRow, toRow, orderType, orderDir);
        else
            adcVServerNoticeOffFromSvc = f5VsMgmt.searchVServerNoticeOffList(adc.getIndex(), accntIndex, searchKey, fromRow, toRow, orderType, orderDir);
//        log.debug("{}", adcVServerNoticeOffFromSvc);
        
        return adcVServerNoticeOffFromSvc;
//        adcVServerNoticeFromSvc.addAll(adcVServerNoticeOnFromSvc);
//        adcVServerNoticeFromSvc.addAll(adcVServerNoticeOffFromSvc);
        
//        return adcVServerNoticeFromSvc;        
    }
    
    public ArrayList<OBDtoAdcNoticeGroup> getNoticeGrpList(AdcDto adc) throws OBException, Exception
    {
        ArrayList<OBDtoAdcNoticeGroup> noticeGrpListFromSvc = new ArrayList<OBDtoAdcNoticeGroup>();
        noticeGrpListFromSvc = mgmt.getNoticeGrp(adc.getIndex());
//        log.debug("{}", noticeGrpListFromSvc);
        return noticeGrpListFromSvc;
    }
    
    // notice group check
    public boolean existsNoticeGrp(String poolIndex) throws OBException, Exception 
    {
        log.debug("existsNoticeGrp: {}", poolIndex);
        boolean exists = false;        
        
        exists = mgmt.isNoticePoolUsed(poolIndex);
        
        log.debug("exists: {}", exists);
        return exists;
    }
    
    public void setNoticeGroup(AdcDto adc, ArrayList<OBDtoAdcNoticeGroup> noticeGroupList, SessionDto session) throws Exception
    {        
        OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
        extraInfo.setExtraMsg1(noticeGroupList.toString());
        mgmt.setNoticeGroup(adc.getIndex(), noticeGroupList, session.getAccountIndex(), extraInfo);           
    }
    
    public void setVServerNoticeOn(AdcDto adc, ArrayList<OBDtoAdcVServerNotice> vsList, SessionDto session) throws Exception
    {
        OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
        extraInfo.setExtraMsg1(vsList.toString());
        if(adc.getType().equals(OBDefine.ADC_TYPESTR_ALTEON))
            alteonVsMgmt.setVServerNoticeOnAlteon(adc.getIndex(), vsList, extraInfo);
        else
            f5VsMgmt.setVServerNoticeOnF5(adc.getIndex(), vsList, extraInfo);
    }
    
    public boolean existVServerNoticeOn(AdcDto adc, ArrayList<OBDtoAdcVServerNotice> vsList, SessionDto session) throws Exception
    {
        boolean exists = false;
        
        OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
        extraInfo.setExtraMsg1(vsList.toString());
        if(adc.getType().equals(OBDefine.ADC_TYPESTR_ALTEON))
        {
//            exists = alteonVsMgmt.existVServerNoticeOnAlteon(adc.getIndex(), vsList, extraInfo);
            exists = alteonVsMgmt.isVServerSyncNotice(vsList, adc.getIndex()); 
        }
        else
        {
//            exists = f5VsMgmt.existVServerNoticeOn(adc.getIndex(), vsList, extraInfo);
            exists = f5VsMgmt.isVServerSyncNotice(vsList, adc.getIndex()); 
        }
        
        log.debug("exists: {}", exists);
        return exists;
    }
    
    public void setVServerNoticeOff(AdcDto adc, ArrayList<OBDtoAdcVServerNotice> vsList, SessionDto session) throws Exception 
    {
        OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
        extraInfo.setExtraMsg1(vsList.toString());
        if(adc.getType().equals(OBDefine.ADC_TYPESTR_ALTEON))
            alteonVsMgmt.setVServerNoticeOffAlteon(adc.getIndex(), vsList, extraInfo);
        else
            f5VsMgmt.setVServerNoticeOffF5(adc.getIndex(), vsList, extraInfo);        
    }
}
