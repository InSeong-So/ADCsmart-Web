package kr.openbase.adcsmart.web.controller.adcman;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import kr.openbase.adcsmart.service.dto.OBDtoAdcNoticeGroup;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerNotice;
import kr.openbase.adcsmart.service.dto.OBDtoSLBUpdateStatus;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.adcman.AdcFacade;
import kr.openbase.adcsmart.web.facade.adcman.NoticeGrpFacade;
import kr.openbase.adcsmart.web.facade.adcman.VirtualSvrFacade;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.AdcPoolDto;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;
import kr.openbase.adcsmart.web.json.NoticeJsonAdapter;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

@Controller
@Scope(value = "prototype")
public class NoticeGrpAction extends BaseAction 
{
    private transient Logger log = LoggerFactory.getLogger(NoticeGrpAction.class);
    
    @Autowired
    private NoticeGrpFacade noticeGrpFacade;
    
    @Autowired
    private VirtualSvrFacade virtualSvrFacade;
        
    private AdcDto adc;
    private Integer rowOnTotal;
    private Integer rowOffTotal;
    private ArrayList<OBDtoAdcVServerNotice> VServerNotice;
    private ArrayList<OBDtoAdcVServerNotice> VServerNoticeOn;
    private ArrayList<OBDtoAdcVServerNotice> VServerNoticeOff;
    private List<AdcPoolDto> adcPools;
    private ArrayList<OBDtoAdcNoticeGroup> noticeGrpList;
    private String noticeInString;    
    private ArrayList<OBDtoAdcVServerNotice> vsNoticeList;
    private String vsNoticeInString;
    
    private String searchKey;
    private Integer fromRow;
    private Integer toRow;
    private Integer fromRowOff;
    private Integer toRowOff;
    private Integer orderDir;   // 오른차순 = 1, 내림차순 = 2
    private Integer orderType;
    private Integer orderDirOff;
    private Integer orderTypeOff;
    
    private Integer pairIndex;  // 이중화 장비 Chcek pairIndex
    private Integer accntIndex;
    
    public NoticeGrpAction()
    {
        vsNoticeList = new ArrayList<OBDtoAdcVServerNotice>();
    }
    
    public String retrieverNoticeListTotal() throws Exception
    {
        isSuccessful = true;
        try
        {
            SessionDto sessionData = session.getSessionDto();
            
            log.debug("adc: {}, searchKey: {}", adc, searchKey);
            rowOnTotal = noticeGrpFacade.searchVServerNoticeOnCount(adc, sessionData.getAccountIndex(), searchKey);
            rowOffTotal = noticeGrpFacade.searchVServerNoticeOffCount(adc, sessionData.getAccountIndex(), searchKey);
//            rowOnTotal = 2;
//            rowOffTotal = 99;
            log.debug("row On total: {}", rowOnTotal);    // in     - NoticeOn: 8
            log.debug("row Off total: {}", rowOffTotal);  // not in - NoticeOff: 681
        }
        catch(Exception e)
        {
            // TODO: handle exception
        }
        return SUCCESS;
    }
    
    public String loadNoticeListContent() throws Exception
    {
        log.debug("{}, {}", adc, searchKey);
        try 
        {           
            SessionDto sessionData = session.getSessionDto();            
//            noticeGrpList = noticeGroupFacade.getNoticeGrpF5(adc, sessionData.getAccountIndex());
//            noticeGrpList = noticeGroupFacade.getNoF5Deatail(adc, sessionData.getAccountIndex());
//            log.debug("noticeGrpList: {}", noticeGrpList);
            
//            OBDtoADCObject adcObject = new OBDtoADCObject();
//            adcObject.setIndex(adc.getIndex());
//            adcObject.setCategory(2);
//            
//            OBDtoSearch searchOption = new OBDtoSearch();
//            searchOption.setSearchKey(searchKey);
//            OBDtoOrdering orderOption = new OBDtoOrdering();
//            orderOption.setOrderDirection(orderDir);
//            orderOption.setOrderType(orderType);
            
            log.debug("fromRow: {}, toRow: {}, orderType: {}, orderDir: {}", fromRow, toRow, orderType, orderDir);
            log.debug("fromRowOff: {}, toRowOff: {}, orderTypeOff: {}, orderDirOff: {}", fromRowOff, toRowOff, orderTypeOff, orderDirOff);
//            VServerNoticeOn = noticeGrpFacade.searchVServerNoticeOnList(adc, sessionData.getAccountIndex(), searchKey, fromRow, toRow, orderType, orderDir);
//            VServerNoticeOff = noticeGrpFacade.searchVServerNoticeOffList(adc, sessionData.getAccountIndex(), searchKey, fromRowOff, toRowOff, orderTypeOff, orderDirOff);
            if (toRow != null)
            {
                VServerNoticeOn = noticeGrpFacade.searchVServerNoticeOnList(adc, sessionData.getAccountIndex(), searchKey, fromRow, toRow, orderType, orderDir);
                log.debug("VServerNoticeOn: {}", VServerNoticeOn);
            }
            else
            {   
            }
            if (toRowOff != null)
            {
                VServerNoticeOff = noticeGrpFacade.searchVServerNoticeOffList(adc, sessionData.getAccountIndex(), searchKey, fromRowOff, toRowOff, orderTypeOff, orderDirOff);
                log.debug("VServerNoticeOff: {}", VServerNoticeOff);
            }
            else
            {                
            }
            
//            log.debug("{}", VServerNotice); 
          
            accntIndex = session.getAccountIndex();
            adcPools = virtualSvrFacade.getAdcPools(adc.getIndex(), adc.getType());
//            log.debug("{}", adcPools);
            noticeGrpList = noticeGrpFacade.getNoticeGrpList(adc);
//            log.debug("{}", noticeGrpList);
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
    
    public String downloadVSList() throws Exception
    {
        isSuccessful = true;
        OBDtoSLBUpdateStatus retVal = new OBDtoSLBUpdateStatus();
        try
        {
            log.debug("{}", adc);
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
                    message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_SLB_DOWNLOAD_FAIL);
                   
                }
                else
                {                       
                    message = retVal.getExtraMsg();  
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
    
    public String setNoticeGroup() throws Exception
    {
        isSuccessful = true;
        try
        {
            log.debug("{}, {}", adc, noticeGrpList);
            ArrayList<OBDtoAdcNoticeGroup> noticeGrpList = new ArrayList<OBDtoAdcNoticeGroup>();
            OBDtoAdcNoticeGroup noticeGrp = new OBDtoAdcNoticeGroup();
            noticeGrp.setAdcIndex(adc.getIndex());
            noticeGrp.setPoolIndex(noticeInString);
            noticeGrpList.add(noticeGrp);
            
//            if (noticeGrpFacade.existsNoticeGrp(noticeInString)) 
//            {
//                isSuccessful = false;
//                message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_NOTICE_GROUP_USING);
//            } 
//            else 
//            {      
//                noticeGrpFacade.setNoticeGroup(adc, noticeGrpList, session.getSessionDto());
//                message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_SLB_SUCCESS);
//            }
            noticeGrpFacade.setNoticeGroup(adc, noticeGrpList, session.getSessionDto());
            message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_SLB_SUCCESS);
           
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
    
    public String setVServerNoticeOn() throws Exception
    {
        isSuccessful = true;
        try
        {
            log.debug("{}, {}", adc, vsNoticeInString);
//            ArrayList<OBDtoAdcVServerNotice> vsNoticeList = new ArrayList<OBDtoAdcVServerNotice>();
//            OBDtoAdcVServerNotice vsNotice = new OBDtoAdcVServerNotice();
//            vsNotice.setNoticePoolIndex(vsNoticeInString);
//            vsNoticeList.add(vsNotice);
                       
//            if(noticeGrpFacade.existVServerNoticeOn(adc, convertNoticeCheckToJson(vsNoticeInString), session.getSessionDto()))
//            {
//                noticeGrpFacade.setVServerNoticeOn(adc, convertNoticeCheckToJson(vsNoticeInString), session.getSessionDto());
//                message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_NOTICE_CONFIG_SLB_SUCCESS);
//            }
//            else
//            {
//                isSuccessful = false;
//                message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_NOTICE_PEER_VSRV_NOT_EXIST);
//            }
            
            noticeGrpFacade.setVServerNoticeOn(adc, convertNoticeCheckToJson(vsNoticeInString), session.getSessionDto());
            message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_NOTICE_CONFIG_SLB_SUCCESS);
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
    
    public String existVServerNoticeOn() throws Exception
    {
        isSuccessful = true;
        try
        {
            log.debug("{}, {}", adc, vsNoticeInString);
                       
            if(noticeGrpFacade.existVServerNoticeOn(adc, convertNoticeCheckToJson(vsNoticeInString), session.getSessionDto()))
            {
//                message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_NOTICE_CONFIG_SLB_SUCCESS);
            }
            else
            {
                isSuccessful = false;
                message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_NOTICE_PEER_VSRV_NOT_EXIST);
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
    
//    public String setVServerNoticeOnPeer() throws Exception
//    {
//        isSuccessful = true;
//        try
//        {
//            log.debug("{}, {}", adc, vsNoticeInString);
//            
//            if(noticeGrpFacade.existVServerNoticeOn(adc, convertNoticeCheckToJson(vsNoticeInString), session.getSessionDto()))
//            {
//                noticeGrpFacade.setVServerNoticeOn(adc, convertNoticeCheckToJson(vsNoticeInString), session.getSessionDto());
//                message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_NOTICE_CONFIG_SLB_SUCCESS);
//            }
//            else
//            {
//                isSuccessful = false;
//                message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_NOTICE_PEER_VSRV_NOT_EXIST);
//            }
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

    public String setVServerNoticeOff() throws Exception
    {
        try
        {
            log.debug("{}, {}", adc, vsNoticeInString);
            
//            if(noticeGrpFacade.existVServerNoticeOn(adc, convertNoticeCheckToJson(vsNoticeInString), session.getSessionDto()))
//            {
//                noticeGrpFacade.setVServerNoticeOff(adc, convertNoticeCheckToJson(vsNoticeInString), session.getSessionDto());
//                message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_NOTICE_REVERT_SLB_SUCCESS);
//            }
//            else
//            {
//                isSuccessful = false;
//                message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_NOTICE_PEER_VSRV_NOT_EXIST);
//            }
            noticeGrpFacade.setVServerNoticeOff(adc, convertNoticeCheckToJson(vsNoticeInString), session.getSessionDto());
            message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_NOTICE_REVERT_SLB_SUCCESS);
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
    
    private ArrayList<OBDtoAdcVServerNotice> convertNoticeCheckToJson(String elementString)
    {
        if (StringUtils.isEmpty(elementString))
            return null;
        
        vsNoticeList.clear();
        Gson gson = new GsonBuilder().registerTypeAdapter(OBDtoAdcVServerNotice.class, new NoticeJsonAdapter()).create();
        JsonParser parser = new JsonParser();
        JsonArray jarray = parser.parse(elementString).getAsJsonArray();
        for (JsonElement e : jarray)
            vsNoticeList.add(gson.fromJson(e, OBDtoAdcVServerNotice.class));
        
        return vsNoticeList;        
    }

    public String checkPairIndex() throws Exception
    {
        try
        {
            pairIndex = virtualSvrFacade.getPeerIndex(adc.getIndex());      
            
            String peerAdcIPAddress = "";
            String peerAdcName = "";
            
            if(pairIndex!=null && pairIndex>0)
            {
                AdcDto adcInfo = new AdcFacade().getAdc(pairIndex);
                peerAdcIPAddress = adcInfo.getIp();
                peerAdcName = adcInfo.getName();                
                message = String.format(OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_NOTICE_CONFIRM_ACTIVE_STANDBY), peerAdcName, peerAdcIPAddress);
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
    
    public AdcDto getAdc()
    {
        return adc;
    }

    public void setAdc(AdcDto adc)
    {
        this.adc = adc;
    }

    public ArrayList<OBDtoAdcVServerNotice> getVServerNotice()
    {
        return VServerNotice;
    }

    public void setVServerNotice(ArrayList<OBDtoAdcVServerNotice> vServerNotice)
    {
        VServerNotice = vServerNotice;
    }

    public List<AdcPoolDto> getAdcPools()
    {
        return adcPools;
    }

    public void setAdcPools(List<AdcPoolDto> adcPools)
    {
        this.adcPools = adcPools;
    }

    public ArrayList<OBDtoAdcNoticeGroup> getNoticeGrpList()
    {
        return noticeGrpList;
    }

    public void setNoticeGrpList(ArrayList<OBDtoAdcNoticeGroup> noticeGrpList)
    {
        this.noticeGrpList = noticeGrpList;
    }

    public String getNoticeInString()
    {
        return noticeInString;
    }

    public void setNoticeInString(String noticeInString)
    {
        this.noticeInString = noticeInString;
    }

    public ArrayList<OBDtoAdcVServerNotice> getVsNoticeList()
    {
        return vsNoticeList;
    }

    public void setVsNoticeList(ArrayList<OBDtoAdcVServerNotice> vsNoticeList)
    {
        this.vsNoticeList = vsNoticeList;
    }

    public String getVsNoticeInString()
    {
        return vsNoticeInString;
    }

    public void setVsNoticeInString(String vsNoticeInString)
    {
        this.vsNoticeInString = vsNoticeInString;
    }

    public String getSearchKey()
    {
        return searchKey;
    }

    public void setSearchKey(String searchKey)
    {
        this.searchKey = searchKey;
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

    public Integer getRowOnTotal()
    {
        return rowOnTotal;
    }

    public void setRowOnTotal(Integer rowOnTotal)
    {
        this.rowOnTotal = rowOnTotal;
    }

    public Integer getRowOffTotal()
    {
        return rowOffTotal;
    }

    public void setRowOffTotal(Integer rowOffTotal)
    {
        this.rowOffTotal = rowOffTotal;
    }

    public Integer getFromRow()
    {
        return fromRow;
    }

    public ArrayList<OBDtoAdcVServerNotice> getVServerNoticeOn()
    {
        return VServerNoticeOn;
    }

    public void setVServerNoticeOn(ArrayList<OBDtoAdcVServerNotice> vServerNoticeOn)
    {
        VServerNoticeOn = vServerNoticeOn;
    }

    public ArrayList<OBDtoAdcVServerNotice> getVServerNoticeOff()
    {
        return VServerNoticeOff;
    }

    public void setVServerNoticeOff(ArrayList<OBDtoAdcVServerNotice> vServerNoticeOff)
    {
        VServerNoticeOff = vServerNoticeOff;
    }

    public Integer getFromRowOff()
    {
        return fromRowOff;
    }

    public void setFromRowOff(Integer fromRowOff)
    {
        this.fromRowOff = fromRowOff;
    }

    public Integer getToRowOff()
    {
        return toRowOff;
    }

    public void setToRowOff(Integer toRowOff)
    {
        this.toRowOff = toRowOff;
    }

    public Integer getOrderDirOff()
    {
        return orderDirOff;
    }

    public void setOrderDirOff(Integer orderDirOff)
    {
        this.orderDirOff = orderDirOff;
    }

    public Integer getOrderTypeOff()
    {
        return orderTypeOff;
    }

    public void setOrderTypeOff(Integer orderTypeOff)
    {
        this.orderTypeOff = orderTypeOff;
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

    public Integer getPairIndex()
    {
        return pairIndex;
    }

    public void setPairIndex(Integer pairIndex)
    {
        this.pairIndex = pairIndex;
    }

    public Integer getAccntIndex() 
    {
		return accntIndex;
	}

	public void setAccntIndex(Integer accntIndex) 
	{
		this.accntIndex = accntIndex;
	}

	@Override
	public String toString() 
	{
		return "NoticeGrpAction [noticeGrpFacade=" + noticeGrpFacade + ", virtualSvrFacade=" + virtualSvrFacade
				+ ", adc=" + adc + ", rowOnTotal=" + rowOnTotal + ", rowOffTotal=" + rowOffTotal + ", VServerNotice="
				+ VServerNotice + ", VServerNoticeOn=" + VServerNoticeOn + ", VServerNoticeOff=" + VServerNoticeOff
				+ ", adcPools=" + adcPools + ", noticeGrpList=" + noticeGrpList + ", noticeInString=" + noticeInString
				+ ", vsNoticeList=" + vsNoticeList + ", vsNoticeInString=" + vsNoticeInString + ", searchKey="
				+ searchKey + ", fromRow=" + fromRow + ", toRow=" + toRow + ", fromRowOff=" + fromRowOff + ", toRowOff="
				+ toRowOff + ", orderDir=" + orderDir + ", orderType=" + orderType + ", orderDirOff=" + orderDirOff
				+ ", orderTypeOff=" + orderTypeOff + ", pairIndex=" + pairIndex + ", accntIndex=" + accntIndex + "]";
	}       
}
