package kr.openbase.adcsmart.web.controller.adcman;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeDetail;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcRealServerGroup;
import kr.openbase.adcsmart.service.dto.OBDtoAdcScope;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.adcman.NodeFacade;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.AdcNodeDetailDto;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;
import kr.openbase.adcsmart.web.json.NodeGroupJsonAdapter;
import kr.openbase.adcsmart.web.json.NodeJsonAdapter;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

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

@Controller
@Scope(value = "prototype")
public class NodeAction extends BaseAction 
{
	private transient Logger log = LoggerFactory.getLogger(NodeAction.class);
	
	@Autowired
	private NodeFacade nodeFacade;
	
	private AdcDto adc;
	private Integer rowTotal;
	private ArrayList<OBDtoAdcNodeDetail> nodeF5Detail;
	private ArrayList<AdcNodeDetailDto> nodeDetail;
	private ArrayList<AdcNodeDetailDto> nodeDetailGroup;
	
	private List<String> nodeIndexList;
	private Integer state;
	
	private String nodeCheckInString;
	private ArrayList<OBDtoAdcNodeF5> nodeList;
	
	private ArrayList<OBDtoAdcRealServerGroup> nodeGrpList;
	private OBDtoAdcRealServerGroup nodeGrp;
	private String rsGroupName;
	private Integer rsGroupIndex;
	
	private String searchKey;
	private Integer fromRow;
	private Integer toRow;
	private Integer orderDir; // 오른차순 = 1, 내림차순 = 2
	private Integer orderType; //
	
	private Integer orderGroupDir; // 오른차순 = 1, 내림차순 = 2
	private Integer orderGroupType; // 
	
	private OBDtoAdcScope adcScope;
	private String selectedCol;
	private ArrayList<OBDtoAdcNodeF5> rsFailList;
	private ArrayList<String> rsIndexList;
	private ArrayList<String> rsIpList;
	
	public NodeAction()
	{
	    nodeList = new ArrayList<OBDtoAdcNodeF5>();
	    rsIndexList = new ArrayList<String>();
	    rsIpList = new ArrayList<String>();
	    rsFailList = new ArrayList<OBDtoAdcNodeF5>();
	}
	
	public String retrieverNodeListTotal() throws Exception
    {
        isSuccessful = true;
        try 
        {
            log.debug("adc: {}, searchKey: {}", adc, searchKey);
            SessionDto sessionData = session.getSessionDto();
            rowTotal = nodeFacade.getNodeAdcListTotal(adc, sessionData.getAccountIndex(), searchKey);
//            if(adcScope != null && !adcScope.equals("") &&  adcScope.getLevel() != 2)
//            {
//                rowTotal = nodeFacade.getNodeAllListTotal(adcScope, sessionData.getAccountIndex(), searchKey);
//            }
//            else
//            {            
//                rowTotal = nodeFacade.getNodeAdcListTotal(adc, sessionData.getAccountIndex(), searchKey);
//            } 
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
	
	public String retrieverNodeListGroupTotal() throws Exception
    {
        isSuccessful = true;
        try 
        {
            SessionDto sessionData = session.getSessionDto();
            if(adcScope != null && !adcScope.equals("") &&  adcScope.getLevel() != 2)
            {
                rowTotal = nodeFacade.getNodeAllListTotal(adcScope, sessionData.getAccountIndex(), searchKey);
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
	
	public String loadNodeListContent() throws Exception 
    {
        log.debug("{}, {}", adc, searchKey);
        try 
        {
            SessionDto sessionData = session.getSessionDto();
//          nodeF5Detail = nodeFacade.getNodeF5Deatail(adc, sessionData.getAccountIndex(), searchKey, fromRow, toRow, orderType, orderDir);
            nodeGrpList = nodeFacade.getNodeGrpList(adc, sessionData.getAccountIndex(), orderGroupType != null ? orderGroupType : 0, orderGroupDir != null ? orderGroupDir : 0); // rsGroupList
            nodeDetail = nodeFacade.getNodeAdcDeatail(adc, sessionData.getAccountIndex(), searchKey, fromRow, toRow, orderType != null ? orderType : 0, orderDir != null ? orderDir : 0);
            nodeDetailGroup = nodeFacade.getNodeF5DeatailGroup(adc, sessionData.getAccountIndex(), searchKey, orderGroupType != null ? orderGroupType : 0, orderGroupDir != null ? orderGroupDir : 0);
            log.debug("{}", nodeGrpList);
            log.debug("{}", nodeDetail);
            log.debug("{}", nodeDetailGroup);
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
	
	public String loadNodeListGroupContent() throws Exception 
    {
        try 
        {
            SessionDto sessionData = session.getSessionDto();
            nodeDetail = nodeFacade.getNodeAllDeatail(adcScope, sessionData.getAccountIndex(), searchKey, fromRow, toRow, orderType != null ? orderType : 0, orderDir != null ? orderDir : 0);
//            nodeGrpList = nodeFacade.getNodeGrpAllList(adcScope, sessionData.getAccountIndex(), orderGroupType != null ? orderGroupType : 0, orderGroupDir != null ? orderGroupDir : 0); // rsGroupList
            log.debug("{}", nodeDetail);
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
    
	// RealServerGroup Add
	public String addRsGroup() throws Exception
	{
		isSuccessful = true;
		try 
		{
			log.debug("{}, {}", adc, rsGroupName);
			
			SessionDto sessionData = session.getSessionDto();	        
			OBDtoAdcRealServerGroup rsGroup = new OBDtoAdcRealServerGroup();	
//			rsGroup.setGroupIndex(10);
			rsGroup.setGroupName(rsGroupName);
			rsGroup.setAvailable(OBDefine.DATA_AVAILABLE);
			rsGroup.setDescription(rsGroupName);
			rsGroup.setAccntIndex(sessionData.getAccountIndex());
			
//			nodeFacade.addRsGroup(adc, rsGroup);
//			message = OBMessageWeb.getMessageWeb("Real Server Group이 추가되었습니다.");
			
			if (!nodeFacade.addRsGroup(adc, rsGroup)) 
			{
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_REALSERVERGROUP_DUPLICATED);
			} 
			else 
			{
//				nodeFacade.addRsGroup(adc, rsGroup);
//				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_SLB_SUCCESS);
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_REALSERVERGROUP_ADD);	
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
	
	// RealServerGroup Modify
	public String modifyRsGroup() throws Exception
	{
		isSuccessful = true;
		try 
		{
			log.debug("{}, {}, {}", adc, rsGroupIndex, rsGroupName);
			
			SessionDto sessionData = session.getSessionDto();
			OBDtoAdcRealServerGroup rsGroup = new OBDtoAdcRealServerGroup();
			rsGroup.setGroupIndex(rsGroupIndex);
			rsGroup.setGroupName(rsGroupName);
			rsGroup.setAccntIndex(sessionData.getAccountIndex());
			
//			nodeFacade.modifyRsGroup(adc, rsGroup);
//			message = OBMessageWeb.getMessageWeb("Real Server Group이 수정되었습니다.");
			
			if (!nodeFacade.modifyRsGroup(adc, rsGroup)) 
			{
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_REALSERVERGROUP_DUPLICATED);
			} 
			else 
			{
//				nodeFacade.addRsGroup(adc, rsGroup);
//				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_SLB_SUCCESS);
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_REALSERVERGROUP_MODIFY);	
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
	
	// RealServerGroup Delete
	public String delRsGroup() throws Exception
	{
		isSuccessful = true;
		try 
		{
			log.debug("{}, {}", adc, rsGroupIndex);
			
			SessionDto sessionData = session.getSessionDto();
			OBDtoAdcRealServerGroup rsGroup = new OBDtoAdcRealServerGroup();
			rsGroup.setGroupIndex(rsGroupIndex);
			rsGroup.setAccntIndex(sessionData.getAccountIndex());
			
			nodeFacade.delRsGroup(adc, rsGroup);
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_REALSERVERGROUP_DELETE);
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
	
	// RealServerGroup Move
	public String updateRsGroup() throws Exception
	{
		isSuccessful = true;
		try 
		{
			log.debug("{}, {}, {}, {}", adc, nodeCheckInString, convertNodeCheckToJSON(nodeCheckInString), rsGroupIndex);
			
			SessionDto sessionData = session.getSessionDto();
			OBDtoAdcRealServerGroup rsGroup = new OBDtoAdcRealServerGroup();
			rsGroup.setGroupIndex(rsGroupIndex);
			rsGroup.setAccntIndex(sessionData.getAccountIndex());
			
//			nodeFacade.updateRsGroup(adc, rsGroup, convertNodeCheckToJSON(nodeCheckInString));
			
			if (!nodeFacade.updateRsGroup(adc, rsGroup, convertNodeCheckToJSON(nodeCheckInString))) 
			{
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_REALSERVERGROUP_DUPLICATED);
			} 
			else 
			{
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_REALSERVERGROUP_MOVE);	
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
	
	public String setGroupNode() throws Exception
    {     
	    isSuccessful = true;
        try 
        {
//          log.debug("{}, {}, {}", adc, nodeIndexList, state);
//          nodeFacade.setNodeState(adc, nodeIndexList, state, session.getSessionDto());
            
//          OBDtoAdcNodeF5 adcNodeF5 = new OBDtoAdcNodeF5();
//          templateObj.setHwCheckItems(convertHwCheckToJSON(hwCheckInString));
            
            
            if(adcScope != null && !adcScope.equals("") &&  adcScope.getLevel() != 2)
            {
                log.debug("{}, {}, {}", nodeCheckInString, convertNodeGroupCheckToJSON(nodeCheckInString), state);
                rsFailList = nodeFacade.setNodeAllState(convertNodeGroupCheckToJSON(nodeCheckInString), state, session.getSessionDto()); 
                
                for(int i = 0; i < rsFailList.size(); i++)
                {
                    rsIndexList.add(rsFailList.get(i).getIndex());
                    rsIpList.add(rsFailList.get(i).getIpAddress());
                }
                 
                if(!rsFailList.isEmpty())
                {                    
                    isSuccessful = false;
                    message = rsIpList.size() + " " + OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_SLB_CONFIG_NOT_SETTING) + "\n" +
                            OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_SLB_SETTING_FAIL_RESETTING); 
                    /*
                    if(state == 1)
                        message = rsIpList.size() + " 건의 동기화 되지 않은 Real Server가 있어 \n" +
                                "설정에 실패했습니다. \n" +
                                "다시 설정하세요."; 
//                                    "enable에 실패한 RS : " + rsIpList;
                    else if(state == 0)
                        message = rsIpList.size() + " 건의 동기화 되지 않은 Real Server가 있어 \n" +
                                "설정에 실패했습니다. \n" +
                                "다시 설정하세요."; 
//                                    " disable에 실패한 RS : " + rsIpList;
                    else if(state == 2)
                        message = rsIpList.size() + " 건의 동기화 되지 않은 Real Server가 있어 \n" +
                                "설정에 실패했습니다. \n" +
                                "다시 설정하세요."; 
//                                    " ForcedOffline 에 실패한 RS : " + rsIpList;
                    */
                }                   
                else
                {                              
                    message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_SLB_SUCCESS);
                }
            }   
            
//            log.debug("{}, {}, {}, {}", adc, nodeCheckInString, convertNodeCheckToJSON(nodeCheckInString), state);
//            if(adcScope != null && !adcScope.equals("") &&  adcScope.getLevel() != 2)
//            {
//                nodeFacade.setNodeAllState(convertNodeCheckToJSON(nodeCheckInString), state, session.getSessionDto());
//            }
//            message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_SLB_SUCCESS);
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
	
	public String setNode() throws Exception
	{	
	    isSuccessful = true;
		try 
		{
//			log.debug("{}, {}, {}", adc, nodeIndexList, state);
//			nodeFacade.setNodeState(adc, nodeIndexList, state, session.getSessionDto());
		    
//		    OBDtoAdcNodeF5 adcNodeF5 = new OBDtoAdcNodeF5();
//		    templateObj.setHwCheckItems(convertHwCheckToJSON(hwCheckInString));
		    
		    log.debug("{}, {}, {}, {}", adc, nodeCheckInString, convertNodeCheckToJSON(nodeCheckInString), state);
//			if(adcScope != null && !adcScope.equals("") &&  adcScope.getLevel() != 2)
//            {
//		        nodeFacade.setNodeAllState(convertNodeCheckToJSON(nodeCheckInString), state, session.getSessionDto());
//            }
//            else
//            {            
//                nodeFacade.setNodeState(adc, convertNodeCheckToJSON(nodeCheckInString), state, session.getSessionDto());
//            } 
			
			nodeFacade.setNodeState(adc, convertNodeCheckToJSON(nodeCheckInString), state, session.getSessionDto());
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
	
	private ArrayList<OBDtoAdcNodeF5> convertNodeCheckToJSON(String elementString)
    {
        if (StringUtils.isEmpty(elementString))
            return null;
        
        nodeList.clear();
        Gson gson = new GsonBuilder().registerTypeAdapter(OBDtoAdcNodeF5.class, new NodeJsonAdapter()).create();
        JsonParser parser = new JsonParser();
        JsonArray jarray = parser.parse(elementString).getAsJsonArray();
        for (JsonElement e : jarray)
            nodeList.add(gson.fromJson(e, OBDtoAdcNodeF5.class));
        
        return nodeList;        
    }
	
   private ArrayList<OBDtoAdcNodeF5> convertNodeGroupCheckToJSON(String elementString)
    {
        if (StringUtils.isEmpty(elementString))
            return null;
        
        nodeList.clear();
        Gson gson = new GsonBuilder().registerTypeAdapter(OBDtoAdcNodeF5.class, new NodeGroupJsonAdapter()).create();
        JsonParser parser = new JsonParser();
        JsonArray jarray = parser.parse(elementString).getAsJsonArray();
        for (JsonElement e : jarray)
            nodeList.add(gson.fromJson(e, OBDtoAdcNodeF5.class));
        
        return nodeList;        
    }
//	public String disableNode() throws Exception
//	{		
//		return SUCCESS;
//	}	
//	public String forcedOfflineNode() throws Exception
//	{		
//		return SUCCESS;
//	}		
	public AdcDto getAdc() 
	{
		return adc;
	}
	public void setAdc(AdcDto adc) 
	{
		this.adc = adc;
	}	
	public List<String> getNodeIndexList() 
	{
		return nodeIndexList;
	}
	public void setNodeIndexList(List<String> nodeIndexList) 
	{
		this.nodeIndexList = nodeIndexList;
	}
	public Integer getState() 
	{
		return state;
	}
	public void setState(Integer state) 
	{
		this.state = state;
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

    public ArrayList<OBDtoAdcNodeDetail> getNodeF5Detail()
    {
        return nodeF5Detail;
    }

    public void setNodeF5Detail(ArrayList<OBDtoAdcNodeDetail> nodeF5Detail)
    {
        this.nodeF5Detail = nodeF5Detail;
    }

    public ArrayList<AdcNodeDetailDto> getNodeDetail()
    {
        return nodeDetail;
    }

    public void setNodeDetail(ArrayList<AdcNodeDetailDto> nodeDetail)
    {
        this.nodeDetail = nodeDetail;
    }

    public ArrayList<OBDtoAdcNodeF5> getNodeList()
    {
        return nodeList;
    }

    public void setNodeList(ArrayList<OBDtoAdcNodeF5> nodeList)
    {
        this.nodeList = nodeList;
    }

    public String getNodeCheckInString()
    {
        return nodeCheckInString;
    }

    public void setNodeCheckInString(String nodeCheckInString)
    {
        this.nodeCheckInString = nodeCheckInString;
    }

	public ArrayList<OBDtoAdcRealServerGroup> getNodeGrpList() 
	{
		return nodeGrpList;
	}

	public void setNodeGrpList(ArrayList<OBDtoAdcRealServerGroup> nodeGrpList) 
	{
		this.nodeGrpList = nodeGrpList;
	}

	public OBDtoAdcRealServerGroup getNodeGrp() 
	{
		return nodeGrp;
	}

	public void setNodeGrp(OBDtoAdcRealServerGroup nodeGrp) 
	{
		this.nodeGrp = nodeGrp;
	}

	public String getRsGroupName() 
	{
		return rsGroupName;
	}

	public void setRsGroupName(String rsGroupName)
	{
		this.rsGroupName = rsGroupName;
	}

	public Integer getRsGroupIndex() 
	{
		return rsGroupIndex;
	}

	public void setRsGroupIndex(Integer rsGroupIndex) 
	{
		this.rsGroupIndex = rsGroupIndex;
	}

	public ArrayList<AdcNodeDetailDto> getNodeDetailGroup() 
	{
		return nodeDetailGroup;
	}

	public void setNodeDetailGroup(ArrayList<AdcNodeDetailDto> nodeDetailGroup) 
	{
		this.nodeDetailGroup = nodeDetailGroup;
	}

	public Integer getOrderGroupDir() 
	{
		return orderGroupDir;
	}

	public void setOrderGroupDir(Integer orderGroupDir) 
	{
		this.orderGroupDir = orderGroupDir;
	}

	public Integer getOrderGroupType() 
	{
		return orderGroupType;
	}

	public void setOrderGroupType(Integer orderGroupType) 
	{
		this.orderGroupType = orderGroupType;
	}

    public OBDtoAdcScope getAdcScope()
    {
        return adcScope;
    }

    public void setAdcScope(OBDtoAdcScope adcScope)
    {
        this.adcScope = adcScope;
    }

    public String getSelectedCol()
    {
        return selectedCol;
    }

    public void setSelectedCol(String selectedCol)
    {
        this.selectedCol = selectedCol;
    }

    public ArrayList<OBDtoAdcNodeF5> getRsFailList()
    {
        return rsFailList;
    }

    public void setRsFailList(ArrayList<OBDtoAdcNodeF5> rsFailList)
    {
        this.rsFailList = rsFailList;
    }

    public ArrayList<String> getRsIndexList()
    {
        return rsIndexList;
    }

    public void setRsIndexList(ArrayList<String> rsIndexList)
    {
        this.rsIndexList = rsIndexList;
    }

    public ArrayList<String> getRsIpList()
    {
        return rsIpList;
    }

    public void setRsIpList(ArrayList<String> rsIpList)
    {
        this.rsIpList = rsIpList;
    }
}
