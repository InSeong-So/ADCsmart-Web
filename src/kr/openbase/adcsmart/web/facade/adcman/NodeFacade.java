package kr.openbase.adcsmart.web.facade.adcman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import kr.openbase.adcsmart.service.OBAdcVServer;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcRealServerGroup;
import kr.openbase.adcsmart.service.dto.OBDtoAdcScope;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSLBUpdateStatus;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.alteon.OBAdcVServerAlteon;
import kr.openbase.adcsmart.service.impl.f5.OBAdcVServerF5;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.AdcNodeDetailDto;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NodeFacade 
{
	private static transient Logger log = LoggerFactory.getLogger(NodeFacade.class);
	
	private OBAdcVServer f5VsMgmt;
	private OBAdcVServer alteonVsMgmt;
	private OBAdcManagementImpl adcImpl;
	
	public NodeFacade()
	{
	    adcImpl = new OBAdcManagementImpl();
		f5VsMgmt = new OBAdcVServerF5();
		alteonVsMgmt = new OBAdcVServerAlteon();
		
	}
	
	public Integer getNodeAdcListTotal(AdcDto adc, Integer accntIndex, String searchKey) throws OBException, Exception
    {
        Integer nodeAdcListCount = 0;
        if(adc.getType().equals("F5"))
        {
            nodeAdcListCount = f5VsMgmt.searchNodeF5ListCount(adc.getIndex(), accntIndex, searchKey);
        }
        else if(adc.getType().equals("Alteon"))
        {
            nodeAdcListCount = alteonVsMgmt.searchNodeAlteonListCount(adc.getIndex(), accntIndex, searchKey);
        }
       
        return nodeAdcListCount;
    }
    
    public Integer getNodeAllListTotal(OBDtoAdcScope adcScope, Integer accntIndex, String searchKey) throws OBException, Exception
    {
        return f5VsMgmt.searchNodeAllListCount(adcScope, accntIndex, searchKey);
    }
	
    public ArrayList<OBDtoAdcRealServerGroup> getNodeGrpList(AdcDto adc, Integer accntIndex, Integer orderType, Integer orderDir) throws OBException, Exception    
    {
        ArrayList<OBDtoAdcRealServerGroup> nodeGrpList = f5VsMgmt.searchNodeGrpListF5(adc.getIndex(), accntIndex, orderType, orderDir);
//      ArrayList<OBDtoAdcRealServerGroup> nodeGrpList = new ArrayList<OBDtoAdcRealServerGroup>();
//      OBDtoAdcRealServerGroup nodeGrp1 = new OBDtoAdcRealServerGroup();
//      nodeGrp1.setGroupIndex(1);
//      nodeGrp1.setGroupName("RS_GROUP1");
//      nodeGrp1.setAccntIndex(1);
//      nodeGrpList.add(nodeGrp1);
//      
//      OBDtoAdcRealServerGroup nodeGrp2 = new OBDtoAdcRealServerGroup();
//      nodeGrp2.setGroupIndex(2);
//      nodeGrp2.setGroupName("RS_GROUP2");
//      nodeGrp2.setAccntIndex(1);
//      nodeGrpList.add(nodeGrp2);
        
        return nodeGrpList;
    }
    
    public ArrayList<OBDtoAdcRealServerGroup> getNodeGrpAllList(OBDtoAdcScope adc, Integer accntIndex, Integer orderType, Integer orderDir) throws OBException, Exception
    {
        ArrayList<OBDtoAdcRealServerGroup> nodeGrpList = f5VsMgmt.searchNodeGrpListF5(adc.getIndex(), accntIndex, orderType, orderDir);
        return nodeGrpList;
    }
    
//    public ArrayList<AdcNodeDetailDto> getNodeF5Deatail(AdcDto adc, Integer accntIndex, String searchKey, Integer fromRow, Integer toRow, Integer orderType, Integer orderDir) throws OBException, Exception
//    {       
//        ArrayList<AdcNodeDetailDto> nodeF5Detail = new ArrayList<AdcNodeDetailDto>();
//        nodeF5Detail = AdcNodeDetailDto.toNodeF5DetailDto(f5VsMgmt.searchNodeListF5(adc.getIndex(), accntIndex, searchKey, fromRow, toRow, orderType, orderDir));
//        log.debug("{}", nodeF5Detail); 
//        return nodeF5Detail;
//    }
    
    public ArrayList<AdcNodeDetailDto> getNodeAdcDeatail(AdcDto adc, Integer accntIndex, String searchKey, Integer fromRow, Integer toRow, Integer orderType, Integer orderDir) throws OBException, Exception
    {       
        ArrayList<AdcNodeDetailDto> nodeF5Detail = new ArrayList<AdcNodeDetailDto>();
        
        if(adc.getType().equals("F5"))
        {
            nodeF5Detail = AdcNodeDetailDto.toNodeF5DetailDto(f5VsMgmt.searchNodeListF5(adc.getIndex(), accntIndex, searchKey, fromRow, toRow, orderType, orderDir));
        }
        else if(adc.getType().equals("Alteon"))
        {
            nodeF5Detail = AdcNodeDetailDto.toNodeF5DetailDto(alteonVsMgmt.searchNodeListAlteon(adc.getIndex(), accntIndex, searchKey, fromRow, toRow, orderType, orderDir));
        }
        
        log.debug("{}", nodeF5Detail); 
        return nodeF5Detail;
    }
    
    // 전체 그룹인 경우 list
    public ArrayList<AdcNodeDetailDto> getNodeAllDeatail(OBDtoAdcScope adcScope, Integer accntIndex, String searchKey, Integer fromRow, Integer toRow, Integer orderType, Integer orderDir) throws OBException, Exception
    {       
        ArrayList<AdcNodeDetailDto> nodeF5Detail = new ArrayList<AdcNodeDetailDto>();
        nodeF5Detail = AdcNodeDetailDto.toNodeF5DetailDto(f5VsMgmt.searchNodeListAll(adcScope, accntIndex, searchKey, fromRow, toRow, orderType, orderDir));
        log.debug("{}", nodeF5Detail); 
        return nodeF5Detail;
    }
    
    public ArrayList<AdcNodeDetailDto> getNodeF5DeatailGroup(AdcDto adc, Integer accntIndex, String searchKey, Integer orderType, Integer orderDir) throws OBException, Exception
    {       
        ArrayList<AdcNodeDetailDto> nodeF5DetailGroup = new ArrayList<AdcNodeDetailDto>();
        
        if(adc.getType().equals("F5"))
        {
            nodeF5DetailGroup = AdcNodeDetailDto.toNodeF5DetailDto(f5VsMgmt.searchGroupListF5(adc.getIndex(), accntIndex, searchKey, orderType, orderDir));
        }
        else if(adc.getType().equals("Alteon"))
        {
            nodeF5DetailGroup = AdcNodeDetailDto.toNodeF5DetailDto(alteonVsMgmt.searchGroupListAlteon(adc.getIndex(), accntIndex, searchKey, orderType, orderDir));
        }
        
        log.debug("{}", nodeF5DetailGroup); 
        return nodeF5DetailGroup;
    }
    
//	public Integer getNodeListTotal(AdcDto adc, Integer accntIndex, String searchKey) throws OBException, Exception
//	{
//		return f5VsMgmt.searchNodeF5ListCount(adc.getIndex(), accntIndex, searchKey);
//	}
	
    
//  public Integer getNodeListTotalCount(OBDtoAdcScope adcScope, Integer accntIndex, OBDtoAdcNodeF5Detail nodeSet) throws OBException, Exception
//    {
//        return f5VsMgmt.searchNodeListCount(adcScope, accntIndex, nodeSet);
//    }
	
	// 그룹 중복 체크
//	public boolean isExistRealServerGroup(AdcDto adc, OBDtoAdcRealServerGroup rsGroup) throws OBException, Exception 
//	{
//		log.debug("isExistRealServerGroup: {}, {}", new Object[]{adc, rsGroup});
//		boolean exists = false;		
//		exists = f5VsMgmt.isExistRealServerGroup(adc.getIndex(), rsGroup);
//		return exists;
//	}	
	// RealServerGroup Add
	public boolean addRsGroup(AdcDto adc, OBDtoAdcRealServerGroup rsGroup) throws Exception
	{
		return f5VsMgmt.addRealServerGroup(adc.getIndex(), rsGroup);
	}
	
	// RealServerGroup Modify
	public boolean modifyRsGroup(AdcDto adc, OBDtoAdcRealServerGroup rsGroup) throws Exception
	{
		return f5VsMgmt.setRealServerGroup(adc.getIndex(), rsGroup);
	}
	
	// RealServerGroup Delete
	public void delRsGroup(AdcDto adc, OBDtoAdcRealServerGroup rsGroup) throws Exception
	{
		f5VsMgmt.delRealServerGroup(adc.getIndex(), rsGroup);
	}
	
	// RealServerGroup Move
	public boolean updateRsGroup(AdcDto adc, OBDtoAdcRealServerGroup rsGroup, ArrayList<OBDtoAdcNodeF5> nodeList) throws Exception
	{
		// rsGroup의 index 가 0 이면 del , 그 외에는 insert
		return f5VsMgmt.updateRealServerMap(adc.getIndex(), rsGroup, nodeList);
	}
	    
    public ArrayList<OBDtoAdcNodeF5> setNodeAllState(ArrayList<OBDtoAdcNodeF5> nodeList, Integer state, SessionDto session) throws OBException, Exception  
    {
        OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
        extraInfo.setExtraMsg1(nodeList.toString());
        
        Collections.sort(nodeList, new NoAscCompare());
        ArrayList<OBDtoAdcNodeF5> result = new ArrayList<OBDtoAdcNodeF5>();
        ArrayList<OBDtoAdcNodeF5> nodes = new ArrayList<OBDtoAdcNodeF5>();
        ArrayList<OBDtoAdcNodeF5> remove = new ArrayList<OBDtoAdcNodeF5>();
        ArrayList<Integer> reflashList = new ArrayList<Integer>();
        int nodeSize = nodeList.size();
        
        for(int i=0; i < nodeSize; i++)
        {
            if(nodeList.get(i).getAdcType() == OBDefine.ADC_TYPE_F5)
            {
                if(!f5VsMgmt.timeSyncCheck(nodeList.get(i).getAdcIndex()))
                {
                    result.add(nodeList.get(i));
                    reflashList.add(nodeList.get(i).getAdcIndex());
                    remove.add(nodeList.get(i));
                }
            }
            else if(nodeList.get(i).getAdcType() == OBDefine.ADC_TYPE_ALTEON)
            {
                if(!alteonVsMgmt.timeSyncCheck(nodeList.get(i).getAdcIndex()))
                {
                    result.add(nodeList.get(i));
                    reflashList.add(nodeList.get(i).getAdcIndex());
                    remove.add(nodeList.get(i));
                }
            }
        }
        
        if(reflashList != null && !reflashList.isEmpty())
        {
            HashSet<Integer> hs = new HashSet<Integer>(reflashList);
            reflashList = new ArrayList<Integer>(hs);
            nodeList.removeAll(remove);
        }
        
        if(nodeList == null || nodeList.isEmpty())
        {
            int reflashSize = reflashList.size();
            AdcDto adc = new AdcDto();
            for(int i = 0; i < reflashSize; i++)
            {
                Integer type = adcImpl.getAdcType(reflashList.get(i));
                adc.setIndex(reflashList.get(i));
                if (type == OBDefine.ADC_TYPE_F5) 
                {
                    adc.setType("F5");
                }
                else if (type == OBDefine.ADC_TYPE_ALTEON)
                {
                    adc.setType("Alteon");
                }
                downloadVirtualServerList(adc);
            }
            return result;
        }
        
        nodeSize = nodeList.size();
        
        if(nodeSize == 1)
        {
            if(nodeList.get(0).getAdcType() == OBDefine.ADC_TYPE_F5)
            {
                f5VsMgmt.setNodeState(nodeList.get(0).getAdcIndex(), nodeList, state, extraInfo);
            }
            else if(nodeList.get(0).getAdcType() == OBDefine.ADC_TYPE_ALTEON)
            {
                alteonVsMgmt.setNodeState(nodeList.get(0).getAdcIndex(), nodeList, state, extraInfo);
            }
        }
        else
        {
            for(int i=0; i < nodeSize; i++)
            {
                nodes.add(nodeList.get(i));
                if(i+1 < nodeSize && nodeList.get(i).getAdcIndex() != nodeList.get(i+1).getAdcIndex())
                {
                    if(nodes.get(0).getAdcType() == OBDefine.ADC_TYPE_F5)
                    {
                        f5VsMgmt.setNodeState(nodes.get(0).getAdcIndex(), nodes, state, extraInfo);
                    }
                    else if(nodes.get(0).getAdcType() == OBDefine.ADC_TYPE_ALTEON)
                    {
                        alteonVsMgmt.setNodeState(nodes.get(0).getAdcIndex(), nodes, state, extraInfo);
                    }
                    nodes.clear();
                }
                else if(i+1 == nodeSize)
                {
                    if(nodes.get(0).getAdcType() == OBDefine.ADC_TYPE_F5)
                    {
                        f5VsMgmt.setNodeState(nodes.get(0).getAdcIndex(), nodes, state, extraInfo);
                    }
                    else if(nodes.get(0).getAdcType() == OBDefine.ADC_TYPE_ALTEON)
                    {
                        alteonVsMgmt.setNodeState(nodes.get(0).getAdcIndex(), nodes, state, extraInfo);
                    }
                }
            }
        }
        
        if(reflashList != null || !reflashList.isEmpty())
        {
            int reflashSize = reflashList.size();
            AdcDto adc = new AdcDto();

            for(int i = 0; i < reflashSize; i++)
            {
                Integer type = adcImpl.getAdcType(reflashList.get(i));
                adc.setIndex(reflashList.get(i));
                if (type == OBDefine.ADC_TYPE_F5) 
                {
                    adc.setType("F5");
                }
                else if (type == OBDefine.ADC_TYPE_ALTEON)
                {
                    adc.setType("Alteon");
                }
                downloadVirtualServerList(adc);
            }
        }
        
        return result;
    }
    
    public OBDtoSLBUpdateStatus downloadVirtualServerList(AdcDto adc) throws OBException, Exception 
    {
        
        OBDtoSLBUpdateStatus retVal = new OBDtoSLBUpdateStatus();
        if (adc.getType().equals("F5")) 
        {
            retVal = f5VsMgmt.updateSLBStatus(adc.getIndex());
        }
        else if (adc.getType().equals("Alteon"))
        {
            retVal =  alteonVsMgmt.updateSLBStatus(adc.getIndex());
        }
        return retVal;      
    }
    
    public void setNodeState(AdcDto adc, ArrayList<OBDtoAdcNodeF5> nodeList, Integer state, SessionDto session) throws OBException, Exception  
    {
        OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
        extraInfo.setExtraMsg1(nodeList.toString());
        if(adc.getType().equals("F5"))
        {
            f5VsMgmt.setNodeState(adc.getIndex(), nodeList, state, extraInfo);
        }
        else if(adc.getType().equals("Alteon"))
        {
            alteonVsMgmt.setNodeState(adc.getIndex(), nodeList, state, extraInfo);
        }
    }

	static class NoAscCompare implements Comparator<OBDtoAdcNodeF5> 
    {
        @Override
        public int compare(OBDtoAdcNodeF5 arg0, OBDtoAdcNodeF5 arg1)
        {
            return arg0.getAdcIndex() < arg1.getAdcIndex() ? -1 : arg0.getAdcIndex() > arg1.getAdcIndex() ? 1:0;
        }
    }
}
