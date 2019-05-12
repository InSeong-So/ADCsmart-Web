package kr.openbase.adcsmart.web.controller.adcman;

/*
 * 코딩 가이드. 
 * 	1. public 함수의 Exception은 무조건 OBException만 throw 한다.
 *  2. public 함수내에서는 try-catch를 추가하면 catch로 OBException, Exception을 받는다.
 *  3. try-catch로 Exception을 받았을 경우에는 OBException으로 throw 한다. 
 *    참고: public String retrieveAdcGroupToAdcsMap() throws OBException
 *  4. private 함수는 OBException, Exception 두종류를 throw한다. 
 *    참고: private void prepareAdcGroupMapAndGroupToAdcsMap(String searchKey)
 *  5. 내부 처리 도중 사용자에게 메세지를 알릴 필요가 있을 경우에는 다음과 같이 두단계로 진행한다. 
 *    5.1 isSuccessful = false;
 *    5.2 message = OBMessageWeb.LOGIN_ALIVE_TIMEOUT;
 *    5.3. 참고: String addAdc() ....
 *
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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

import kr.openbase.adcsmart.service.dto.OBDtoAdcScope;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVSGroup;
import kr.openbase.adcsmart.service.dto.OBDtoSLBUpdateStatus;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcVServerAll;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcVlan;
import kr.openbase.adcsmart.service.impl.f5.DtoVlanTunnelFilter;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.adcman.AdcFacade;
import kr.openbase.adcsmart.web.facade.adcman.ProfileFacade;
import kr.openbase.adcsmart.web.facade.adcman.VirtualSvrFacade;
import kr.openbase.adcsmart.web.facade.dto.AdcALTEONHealthCheckDto;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.AdcNodeDto;
import kr.openbase.adcsmart.web.facade.dto.AdcPASKHealthCheckDto;
import kr.openbase.adcsmart.web.facade.dto.AdcPoolDto;
import kr.openbase.adcsmart.web.facade.dto.AlteonVsAddDto;
import kr.openbase.adcsmart.web.facade.dto.F5VsAddDto;
import kr.openbase.adcsmart.web.facade.dto.InterfaceDto;
import kr.openbase.adcsmart.web.facade.dto.PasVsAddDto;
import kr.openbase.adcsmart.web.facade.dto.PaskVsAddDto;
import kr.openbase.adcsmart.web.facade.dto.ProfileDto;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;
import kr.openbase.adcsmart.web.facade.dto.VirtualSvrAlteonDto;
import kr.openbase.adcsmart.web.facade.dto.VirtualSvrDto;
import kr.openbase.adcsmart.web.facade.dto.VirtualSvrF5Dto;
import kr.openbase.adcsmart.web.facade.dto.VirtualSvrPASDto;
import kr.openbase.adcsmart.web.facade.dto.VirtualSvrPASKDto;
import kr.openbase.adcsmart.web.json.AdcVSAdapter;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

@Controller
@Scope(value = "prototype")
public class VirtualServerAction extends BaseAction
{
	private transient Logger log = LoggerFactory.getLogger(VirtualServerAction.class);

	@Autowired
	private VirtualSvrFacade virtualSvrFacade;
	@Autowired
	private ProfileFacade profileFacade;
	@Autowired
	private AdcFacade adcFacade;

	private Integer rowTotal;
	private AdcDto adc;
	private String poolIndex;
	private String healthCheckDbIndex;
	private List<VirtualSvrDto> virtualServers;
	private List<String> virtualSvrIndices;
	private AlteonVsAddDto alteonVsAdd;
	private F5VsAddDto f5VsAdd;
	private PasVsAddDto pasVsAdd;
	private PaskVsAddDto paskVsAdd;
	private List<InterfaceDto> interfaces;
	private List<AdcPASKHealthCheckDto> adcHealths;
	private List<AdcALTEONHealthCheckDto> adcHealths_alteon;
	private List<AdcPoolDto> adcPools;
	private List<String> alteonPoolIndexList;
	private List<AdcNodeDto> adcNodes;
	private List<AdcNodeDto> adcRsNodes;
	private AdcPoolDto virtualSvc;
	private AdcPASKHealthCheckDto PASKHealths;
	private VirtualSvrAlteonDto virtualSvrAlteon;
	private VirtualSvrF5Dto virtualSvrF5;
	private VirtualSvrPASDto virtualSvrPAS;
	private VirtualSvrPASKDto virtualSvrPASK;
	private Boolean existsVirtualSvr;
	private String searchKey;
	private Integer fromRow;
	private Integer toRow;
	private Boolean refreshes = false;
	private Integer orderDir; // 오른차순 = 1, 내림차순 = 2
	private Integer orderType; // occurTime = 11 , vsName = 1 , vsIpaddress =2, content=3
	private AdcDto version;
	private String version_;
	private List<ProfileDto> profiles; // F5 Profiles
	private Integer pairIndex; //이중화 pairIndex
	private Integer isExistVSIndex; // 없음: 0, 있음.
	private String alteonVSIndex;
	private String alteonPoolIndex;
	private String ipAddress;
	private Integer isExistVSIPAddress;
	private Integer isExistPoolIndex; // 없음: 0, 있음.
	private Integer extraKey; // 1 최신 SLB 정보
	private List<String> vsNameList;
	private List<OBDtoAdcVlan> availableVlans; // 선택된 Vlan and Tunnel Traffic
	private DtoVlanTunnelFilter registeredVlanMap; // 선택가능한 Vlan and Tunnel Traffic

	private OBDtoAdcScope adcScope;
	private List<OBDtoAdcVServerAll> virtualServerAllList;
//	private String vsCheckInString;
//    private ArrayList<OBDtoADCObject> vsCheckItems; 

	private String vsAdcCheckInString;
	private ArrayList<OBDtoAdcVSGroup> vsAdcList;

	private ArrayList<OBDtoAdcVSGroup> vsFailList;
	private ArrayList<String> vsIndexList;
	private ArrayList<String> vsIpList;
	private F5VsAddDto f5VsMemberInfo;
//    private ArrayList<OBDtoADCObject> convertVsCheckToJSON(String elementString)
//    {
//        if (StringUtils.isEmpty(elementString))
//            return null;
//        
//        vsCheckItems.clear();
//        Gson gson = new GsonBuilder().registerTypeAdapter(OBDtoADCObject.class, new ADCObjectJsonAdapter()).create();
//        JsonParser parser = new JsonParser();
//        JsonArray jarray = parser.parse(elementString).getAsJsonArray();
//        for (JsonElement e : jarray)
//            vsCheckItems.add(gson.fromJson(e, OBDtoADCObject.class));
//        
//        return vsCheckItems;        
//    }

	public VirtualServerAction()
	{
		vsAdcList = new ArrayList<OBDtoAdcVSGroup>();
		vsIndexList = new ArrayList<String>();
		vsIpList = new ArrayList<String>();
		vsFailList = new ArrayList<OBDtoAdcVSGroup>();
	}

	public String retrieveVirtualSvrTotal() throws Exception
	{
		isSuccessful = true;
		try
		{
			log.debug("adc: {}, searchKey: {}", adc, searchKey);
			SessionDto sessionData = session.getSessionDto();

			if (adcScope != null && !adcScope.equals("") && adcScope.getLevel() != 2)
			{
				rowTotal = virtualSvrFacade.getVirtualSvrAllTotal(adcScope, sessionData.getAccountIndex(), searchKey);
			}
			else
			{
				rowTotal = virtualSvrFacade.getVirtualSvrTotal(adc, sessionData.getAccountIndex(), searchKey);
			}

//			rowTotal = virtualSvrFacade.getVirtualSvrTotal(adc, sessionData.getAccountIndex(), searchKey);
			log.debug("row total: {}", rowTotal);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String loadListContent() throws Exception
	{
		try
		{
			log.debug("{}, searchKey:{}, fromRow:{}, toRow:{}, refreshes:{}", new Object[]
			{ adc, searchKey, fromRow, toRow, refreshes });
			SessionDto sessionData = session.getSessionDto();

			if (adcScope != null && !adcScope.equals("") && adcScope.getLevel() != 2)
			{
				virtualServerAllList = virtualSvrFacade.getVirtualServerAllList(adcScope, sessionData.getAccountIndex(),
						searchKey, fromRow, toRow, orderType != null ? orderType : 0, orderDir != null ? orderDir : 0);
				log.debug("virtualServerAllList: {}", virtualServerAllList);
			}
			else
			{
				virtualServers = virtualSvrFacade.getVirtualServerList(adc, sessionData.getAccountIndex(), searchKey,
						fromRow, toRow, orderType, orderDir);
				log.debug("virtualServers: {}", virtualServers);
			}

//			virtualServers = virtualSvrFacade.getVirtualServerList(adc, sessionData.getAccountIndex(), searchKey, fromRow, toRow, orderType, orderDir);
//			log.debug("virtualServers: {}", virtualServers);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
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
			log.debug("{}, searchKey:{}, fromRow:{}, toRow:{}, refreshes:{}", new Object[]
			{ adc, searchKey, fromRow, toRow, refreshes });
			if (refreshes)
			{
				retVal = virtualSvrFacade.downloadVirtualServerList(adc);
				if (retVal.isUpdateSuccess())
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
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String searchVSListUsedByPoolAlteon() throws Exception
	{
		try
		{
			log.debug("{}, searchKey:{}, fromRow:{}, toRow:{}, refreshes:{}", new Object[]
			{ adc, searchKey, fromRow, toRow, refreshes });
			virtualServers = virtualSvrFacade.getVSListUsedByPool(adc, alteonPoolIndex.toString());
			log.debug("virtualServers: {}", virtualServers);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	/**
	 * Alteon 관련 Method
	 * loadAlteonVsAddContent
	 * loadAlteonVsModifyContent
	 * addAlteonVs
	 * modifyAlteonVs
	 */
	public String loadAlteonVsAddContent() throws Exception
	{
		try
		{
			log.debug("{}", adc);
			version = adcFacade.getAdc(adc.getIndex());
			this.version_ = version.getVersion().substring(0, 2);
			interfaces = virtualSvrFacade.getInterfaces(adc.getIndex(), adc.getType());
			log.debug("{}", interfaces);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String loadAlteonVsModifyContent() throws Exception
	{
		try
		{
			log.debug("{}", adc);
			version = adcFacade.getAdc(adc.getIndex());
			this.version_ = version.getVersion().substring(0, 2);
			alteonVsAdd = virtualSvrFacade.getVirtualServerAsAlteonVsAddDto(adc.getIndex(), alteonVsAdd.getIndex());
			log.debug("{}", alteonVsAdd);
			interfaces = virtualSvrFacade.getInterfaces(adc.getIndex(), adc.getType());
			log.debug("{}", interfaces);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String loadVsNameList() throws OBException
	{
		try
		{
			vsNameList = virtualSvrFacade.getVsNameList(adc.getIndex());

			log.debug("vsNameList : {}", vsNameList);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return SUCCESS;
	}

	public String addAlteonVs() throws Exception
	{
		isSuccessful = true;
		try
		{
			log.debug("{}", alteonVsAdd);
			if (virtualSvrFacade.existsVirtualSvr(alteonVsAdd.getAdcIndex(), "Alteon", alteonVsAdd.getName(),
					alteonVsAdd.getIp(), null, alteonVsAdd.getAlteonId()))
			{
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_ALTEONID_DUPLICATED);
			}
			else
			{
				virtualSvrFacade.addAlteonVs(alteonVsAdd, session.getSessionDto());
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_SLB_SUCCESS);
			}
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String addAlteonVsPeer() throws Exception
	{
		isSuccessful = true;
		try
		{
			log.debug("{}", alteonVsAdd);
			if (virtualSvrFacade.existsVirtualSvr(pairIndex, "Alteon", alteonVsAdd.getName(), alteonVsAdd.getIp(), null,
					alteonVsAdd.getAlteonId()))
			{
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_PEER_VSRV_EXIST);
			}
			else
			{
				virtualSvrFacade.addAlteonVsPeer(pairIndex, alteonVsAdd, session.getSessionDto());
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_PEER_SUCCESS);
			}
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String modifyAlteonVs() throws Exception
	{
		isSuccessful = true;
		try
		{
			log.debug("{}", alteonVsAdd);
			virtualSvrFacade.modifyAlteonVs(alteonVsAdd, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_SLB_SUCCESS);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String modifyAlteonVsPeer() throws Exception
	{
		isSuccessful = true;
		try
		{
			log.debug("{}", alteonVsAdd);
			if (virtualSvrFacade.existsVirtualSvr(pairIndex, "Alteon", alteonVsAdd.getName(), alteonVsAdd.getIp(), null,
					alteonVsAdd.getAlteonId()))
			{
				virtualSvrFacade.modifyAlteonVsPeer(pairIndex, alteonVsAdd, session.getSessionDto());
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_PEER_SUCCESS);
			}
			else
			{
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_PEER_VSRV_NOT_EXIST);
			}
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	/**
	 * F5 관련 Method
	 * loadF5VsAddContent
	 * loadF5VsModifyContent
	 * addF5Vs
	 * modifyF5Vs
	 */
	public String loadF5VsAddContent() throws Exception
	{
		try
		{
			adcPools = virtualSvrFacade.getAdcPools(adc.getIndex(), adc.getType());
			log.debug("{}", adcPools);
			adcNodes = virtualSvrFacade.getAdcNodes(adc.getIndex(), adc.getType(), null);
			log.debug("{}", adcNodes);
			profiles = profileFacade.getProfiles(adc.getIndex(), null);
			log.debug("{}", profiles);

//			availableVlans = virtualSvrFacade.getVlans(adc.getIndex(), null);
			availableVlans = virtualSvrFacade.getVlansAll(adc.getIndex());
			log.debug("{}", availableVlans);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String loadF5VsModifyContent() throws Exception
	{
		try
		{
			f5VsMemberInfo = virtualSvrFacade.getVirtualSvrAsF5VsAddDto(adc.getIndex(), f5VsAdd.getIndex());
			if (f5VsMemberInfo.getPoolIndex() != null)
			{
				virtualSvrFacade.refreshF5Vs(f5VsMemberInfo, session.getSessionDto());
			}

			f5VsAdd = virtualSvrFacade.getVirtualSvrAsF5VsAddDto(adc.getIndex(), f5VsAdd.getIndex());
			log.debug("{}", f5VsAdd);
			adcPools = virtualSvrFacade.getAdcPools(adc.getIndex(), adc.getType());
			log.debug("{}", adcPools);
			if (session.getAccountRole().equals(OBDefine.ACCNT_STRING_RSADMIN))
			{
				adcRsNodes = virtualSvrFacade.getAdcNodes(adc.getIndex(), adc.getType(), f5VsAdd.getPoolIndex());
				adcNodes = virtualSvrFacade.getAdcNodes(0, adc.getType(), f5VsAdd.getPoolIndex());
				for (AdcNodeDto adcRsNode : adcRsNodes)
				{
					if (f5VsAdd.getMembers().toString().contains(adcRsNode.getIp()))
					{
						adcNodes.add(adcRsNode);
					}
				}
			}
			else
			{
				adcNodes = virtualSvrFacade.getAdcNodes(adc.getIndex(), adc.getType(), f5VsAdd.getPoolIndex());
			}

			log.debug("{}", adcNodes);
			profiles = profileFacade.getProfiles(adc.getIndex(), null);
			log.debug("{}", profiles);

			// vlan and tunnel tarffic 정보 load
			registeredVlanMap = virtualSvrFacade.getRegisteredVlans(adc.getIndex(), f5VsAdd.getIndex());
			log.debug("{}", registeredVlanMap);
			availableVlans = virtualSvrFacade.getVlans(adc.getIndex(), f5VsAdd.getIndex());
			log.debug("{}", availableVlans);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String addF5Vs() throws Exception
	{
		isSuccessful = true;
		try
		{
			log.debug("{}", f5VsAdd);
			if (virtualSvrFacade.existsVirtualSvr(f5VsAdd.getAdcIndex(), "F5", f5VsAdd.getName(), f5VsAdd.getIp(),
					f5VsAdd.getPort(), null))
			{
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_NAME_DUPLICATED_F5);
			}
			else
			{
				virtualSvrFacade.addF5Vs(f5VsAdd, session.getSessionDto());
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_SLB_SUCCESS);
			}
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String addF5VsPeer() throws Exception
	{
		isSuccessful = true;
		try
		{
			log.debug("{}", f5VsAdd);
			if (virtualSvrFacade.existsVirtualSvr(f5VsAdd.getAdcIndex(), "F5", f5VsAdd.getName(), f5VsAdd.getIp(),
					f5VsAdd.getPort(), null))
			{
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_PEER_VSRV_EXIST);
			}
			else
			{
				log.debug("{}", adc);
				virtualSvrFacade.syncConifgF5(adc, session.getSessionDto());
				downloadSLBConfigForced(f5VsAdd.getAdcIndex());
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_PEER_SUCCESS);
			}
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String downloadSLBConfigForced(Integer adcIndex) throws OBException
	{
		isSuccessful = true;
		OBDtoSLBUpdateStatus retVal = new OBDtoSLBUpdateStatus();
		try
		{
			retVal = adcFacade.downloadSlbConfig(adcIndex);
			if (retVal.isUpdateSuccess())
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
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String modifyF5Vs() throws Exception
	{
		isSuccessful = true;
		try
		{
			log.debug("{}", f5VsAdd);
			virtualSvrFacade.modifyF5Vs(f5VsAdd, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_SLB_SUCCESS);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String modifyF5VsPeer() throws Exception
	{
		isSuccessful = true;
		try
		{
			log.debug("f5VsAdd:{}, adc:{}", f5VsAdd, adc);
			//Alteon은 vs단위로 sync를 하기 때문에 대칭 vs가 있어야 sync를 하지만, F5는 vs sync가 아니라 full-sync(설정 전체 동기화)기때문에 대칭 vs가 있는지 확인할 필요가 없다.
			//오히려 불필요한 vs 대칭 검사가 sync 방해요소가 된다. 그래서 vs 대칭검사를 제거한다.
//			if (virtualSvrFacade.existsVirtualSvr(f5VsAdd.getAdcIndex(), "F5", f5VsAdd.getName(), f5VsAdd.getIp(), f5VsAdd.getPort())) 
//			{
			log.debug("{}", adc);
			virtualSvrFacade.syncConifgF5(adc, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_PEER_SUCCESS);
//			} 
//			else 
//			{
//				isSuccessful = false;
//				message = OBMessageWeb.MSG_PEER_VSRV_NOT_EXIST;
//			}
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	/**
	 * PAS 관련 Method
	 * loadPASVsAddVsAddContent
	 * loadPASVsModifyContent
	 * addPASVs
	 * modifyPASVs
	 */
	public String loadPASVsAddVsAddContent() throws Exception
	{
		try
		{
			adcPools = virtualSvrFacade.getAdcPools(adc.getIndex(), adc.getType());
			log.debug("{}", adcPools);
			adcNodes = virtualSvrFacade.getAdcNodes(adc.getIndex(), adc.getType(), pasVsAdd.getPoolIndex());
			log.debug("{}", adcNodes);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}

	public String loadPASVsModifyContent() throws Exception
	{
		try
		{
			pasVsAdd = virtualSvrFacade.getVirtualSvrAsPASVsAddDto(adc.getIndex(), pasVsAdd.getIndex());
			log.debug("{}", pasVsAdd);
			adcPools = virtualSvrFacade.getAdcPools(adc.getIndex(), adc.getType());
			log.debug("{}", adcPools);
			adcNodes = virtualSvrFacade.getAdcNodes(adc.getIndex(), adc.getType(), pasVsAdd.getPoolIndex());
			log.debug("{}", adcNodes);

			pasVsAdd.setAdcMode(OBDefine.OP_MODE_MONITORING);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String addPASVs() throws Exception
	{
		isSuccessful = true;
		try
		{
			log.debug("{}", pasVsAdd);
			if (virtualSvrFacade.existsVirtualSvr(pasVsAdd.getAdcIndex(), "PAS", pasVsAdd.getName(), pasVsAdd.getIp(),
					pasVsAdd.getPort(), null))
			{
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_NAME_DUPLICATED);
			}
			else
			{
				virtualSvrFacade.addPasVs(pasVsAdd, session.getSessionDto());
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_SLB_SUCCESS);
			}
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}

	public String addPASVsPeer() throws Exception
	{
		isSuccessful = true;
		try
		{
			log.debug("{}", alteonVsAdd);
			if (virtualSvrFacade.existsVirtualSvr(pairIndex, "PAS", pasVsAdd.getName(), pasVsAdd.getIp(),
					pasVsAdd.getPort(), null))
			{
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_PEER_VSRV_EXIST);
			}
			else
			{
				virtualSvrFacade.addPasVsPeer(pairIndex, pasVsAdd, session.getSessionDto());
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_PEER_SUCCESS);
			}
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String modifyPASVs() throws Exception
	{
		isSuccessful = true;
		try
		{
			log.debug("{}", pasVsAdd);
			virtualSvrFacade.modifyPASVs(pasVsAdd, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_SLB_SUCCESS);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String modifyPASVsPeer() throws Exception
	{//
		isSuccessful = true;
		try
		{
			log.debug("{}", paskVsAdd);
			if (virtualSvrFacade.existsVirtualSvr(pairIndex, "PAS", pasVsAdd.getName(), pasVsAdd.getIp(),
					pasVsAdd.getPort(), null))
			{
				virtualSvrFacade.modifyPASVsPeer(pairIndex, pasVsAdd, session.getSessionDto());
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_PEER_SUCCESS);
			}
			else
			{
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_PEER_VSRV_NOT_EXIST);
			}
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}

	/**
	 * PASK 관련 Method
	 * loadPASKVsAddVsAddContent
	 * loadPASKVsModifyContent
	 * addPASKVs
	 * modifyPASKVs
	 */
	public String loadPASKVsAddVsAddContent() throws Exception
	{
		try
		{
			adcHealths = virtualSvrFacade.getPASKHealths(adc.getIndex(), adc.getType());
			log.debug("{}", adcHealths);
			adcPools = virtualSvrFacade.getAdcPools(adc.getIndex(), adc.getType());
			log.debug("{}", adcPools);
			adcNodes = virtualSvrFacade.getAdcNodes(adc.getIndex(), adc.getType(), paskVsAdd.getPoolIndex());
			log.debug("{}", adcNodes);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}

	public String loadPASKVsModifyContent() throws Exception
	{
		try
		{
			adcHealths = virtualSvrFacade.getPASKHealths(adc.getIndex(), adc.getType());
			log.debug("{}", adcHealths);
			paskVsAdd = virtualSvrFacade.getVirtualSvrAsPASKVsAddDto(adc.getIndex(), paskVsAdd.getIndex());
			log.debug("{}", pasVsAdd);
			adcPools = virtualSvrFacade.getAdcPools(adc.getIndex(), adc.getType());
			log.debug("{}", adcPools);
			adcNodes = virtualSvrFacade.getAdcNodes(adc.getIndex(), adc.getType(), paskVsAdd.getPoolIndex());
			log.debug("{}", adcNodes);

			paskVsAdd.setAdcMode(OBDefine.OP_MODE_MONITORING);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String addPASKVs() throws Exception
	{
		isSuccessful = true;
		try
		{
			log.debug("{}", pasVsAdd);
			if (virtualSvrFacade.existsVirtualSvr(paskVsAdd.getAdcIndex(), "PASK", paskVsAdd.getName(),
					paskVsAdd.getIp(), paskVsAdd.getPort(), null))
			{
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_NAME_DUPLICATED);
			}
			else
			{
				virtualSvrFacade.addPASKVs(paskVsAdd, session.getSessionDto());
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_SLB_SUCCESS);
			}
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}

	public String addPASKVsPeer() throws Exception
	{
		isSuccessful = true;
		try
		{
			log.debug("{}", alteonVsAdd);
			if (virtualSvrFacade.existsVirtualSvr(pairIndex, "PASK", paskVsAdd.getName(), paskVsAdd.getIp(),
					paskVsAdd.getPort(), null))
			{
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_PEER_VSRV_EXIST);
			}
			else
			{
				virtualSvrFacade.addPASKVsPeer(pairIndex, paskVsAdd, session.getSessionDto());
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_PEER_SUCCESS);
			}
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String modifyPASKVs() throws Exception
	{
		isSuccessful = true;
		try
		{
			log.debug("{}", paskVsAdd);
			virtualSvrFacade.modifyPASKVs(paskVsAdd, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_SLB_SUCCESS);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String modifyPASKVsPeer() throws Exception
	{
		isSuccessful = true;
		try
		{
			log.debug("{}", paskVsAdd);
			if (virtualSvrFacade.existsVirtualSvr(pairIndex, "PASK", paskVsAdd.getName(), paskVsAdd.getIp(),
					paskVsAdd.getPort(), null))
			{
				virtualSvrFacade.modifyPASKVsPeer(pairIndex, paskVsAdd, session.getSessionDto());
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_PEER_SUCCESS);
			}
			else
			{
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_PEER_VSRV_NOT_EXIST);
			}
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	/**
	 * ADC 공용 Method
	 * enableVss, disableVss, delVss
	 * retrieveAdcPools, retrieveAdcNodes, retrieveVirtualSvc
	 * existsVirtualSvrName, existsVirtualSvr
	 */

	public String enableVss() throws Exception
	{
		try
		{
//            if(adcScope != null && !adcScope.equals("") &&  adcScope.getLevel() != 2)
//            {
//                log.debug("{}, {}", adcScope, vsAdcCheckInString);
//                vsFailList = virtualSvrFacade.enableAllVss(convertVsAdcCheckToJSON(vsAdcCheckInString), session.getSessionDto());
//            }
//            else
//            {
//                log.debug("{}, {}", adc, virtualSvrIndices);
//                virtualSvrFacade.enableVss(adc.getIndex(), adc.getType(), virtualSvrIndices, session.getSessionDto());
//            }  

			virtualSvrFacade.enableVss(adc.getIndex(), adc.getType(), virtualSvrIndices, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_SLB_SUCCESS);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		String rtnType = "";

		if (adcScope.getLevel() == 2)
		{
			if (adc.getType().equals("F5"))
			{
				rtnType = "F5";
			}
			else if (adc.getType().equals("Alteon"))
			{
				rtnType = "ALTEON";
			}
			else if (adc.getType().equals("PAS"))
			{
				rtnType = "PAS";
			}
			else if (adc.getType().equals("PASK"))
			{
				rtnType = "PASK";
			}
		}
		else
		{
			rtnType = "ALL";
		}
		//return adc.getType().equals("F5") ? "F5" : (adc.getType().equals("Alteon") ? "ALTEON" : "PAS");

		return rtnType;
	}

	public String enableGroupVss() throws Exception
	{
		isSuccessful = true;

		try
		{
			if (adcScope != null && !adcScope.equals("") && adcScope.getLevel() != 2)
			{
				log.debug("{}, {}", adcScope, vsAdcCheckInString);
				vsFailList = virtualSvrFacade.enableAllVss(convertVsAdcCheckToJSON(vsAdcCheckInString),
						session.getSessionDto());

				for (int i = 0; i < vsFailList.size(); i++)
				{
					vsIndexList.add(vsFailList.get(i).getVsIndex());
					vsIpList.add(vsFailList.get(i).getVsIp());
				}

				if (!vsFailList.isEmpty())
				{
					isSuccessful = false;
					message = vsIpList.size() + " "
							+ OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_SLB_CONFIG_NOT_SETTING) + "\n"
							+ OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_SLB_SETTING_FAIL_RESETTING);
//			        message = vsIpList.size() + " " + OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_VS_CONFIG_NOT_SETTING) + "\n" +
//                            OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_SLB_SETTING_FAIL) + "\n" +
//                            OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_SLB_AGAIN_SETTING); 
//			        message = vsIpList.size() + " 건의 동기화 되지 않은 Virtual Server가 있어 \n" +
//			                "설정에 실패했습니다. \n" +
//                            "다시 설정해 보시기 바랍니다."; 
//                                " enable에 실패한 VS : " + vsIpList;
				}
				else
				{
					message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_SLB_SUCCESS);
				}
			}
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String enableVssPeer() throws Exception
	{
		if (adc.getType().equals("F5"))
		{
			return enableVssPeerF5();
		}
		else if (adc.getType().equals("Alteon"))
		{
			return enableVssPeerAlteon();
		}
		else if (adc.getType().equals("PAS"))
		{
			return enableVssPeerPAS();
		}
		else if (adc.getType().equals("PASK"))
		{
			return enableVssPeerPASK();
		}
		return "";
	}

	private String enableVssPeerF5() throws Exception
	{
		try
		{
			log.debug("{}, {}", adc, virtualSvrIndices);
			virtualSvrFacade.syncConifgF5(adc, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_PEER_SUCCESS);
			return "F5";
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private String enableVssPeerAlteon() throws Exception
	{
		try
		{
			ArrayList<String> newVSIndices = convertSvrIndices(pairIndex, virtualSvrIndices);
			Integer adcIndex = pairIndex;
			String adcType = adc.getType();

			log.debug("{}, {}", adc, virtualSvrIndices);
			virtualSvrFacade.enableVss(adcIndex, adcType, newVSIndices, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_PEER_SUCCESS);
			return "ALTEON";
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private String enableVssPeerPAS() throws Exception
	{
		try
		{
			ArrayList<String> newVSIndices = convertSvrIndices(pairIndex, virtualSvrIndices);
			Integer adcIndex = pairIndex;
			String adcType = adc.getType();

			log.debug("{}, {}", adc, virtualSvrIndices);
			virtualSvrFacade.enableVss(adcIndex, adcType, newVSIndices, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_PEER_SUCCESS);
			return "PAS";
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private String enableVssPeerPASK() throws Exception
	{
		try
		{
			ArrayList<String> newVSIndices = convertSvrIndices(pairIndex, virtualSvrIndices);
			Integer adcIndex = pairIndex;
			String adcType = adc.getType();

			log.debug("{}, {}", adc, virtualSvrIndices);
			virtualSvrFacade.enableVss(adcIndex, adcType, newVSIndices, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_PEER_SUCCESS);
			return "PASK";
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public String checkPairIndex() throws Exception
	{
		try
		{
			pairIndex = virtualSvrFacade.getPeerIndex(adc.getIndex());

			String peerAdcIPAddress = "";
			String peerAdcName = "";

			if (pairIndex != null && pairIndex > 0)
			{
				AdcDto adcInfo = new AdcFacade().getAdc(pairIndex);
				peerAdcIPAddress = adcInfo.getIp();
				peerAdcName = adcInfo.getName();
				message = String.format(OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIRM_ACTIVE_STANDBY),
						peerAdcName, peerAdcIPAddress);
			}
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}

	public String disableGroupVss() throws Exception
	{
		isSuccessful = true;

		try
		{
			if (adcScope != null && !adcScope.equals("") && adcScope.getLevel() != 2)
			{
				log.debug("{}, {}", adcScope, vsAdcCheckInString);
				vsFailList = virtualSvrFacade.disableAllVss(convertVsAdcCheckToJSON(vsAdcCheckInString),
						session.getSessionDto());

				for (int i = 0; i < vsFailList.size(); i++)
				{
					vsIndexList.add(vsFailList.get(i).getVsIndex());
					vsIpList.add(vsFailList.get(i).getVsIp());
				}

				if (!vsFailList.isEmpty())
				{
					isSuccessful = false;
					message = vsIpList.size() + " "
							+ OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_SLB_CONFIG_NOT_SETTING) + "\n"
							+ OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_SLB_SETTING_FAIL_RESETTING);
//                    message = vsIpList.size() + " " + OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_VS_CONFIG_NOT_SETTING) + "\n" +
//                            OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_SLB_SETTING_FAIL) + "\n" +
//                            OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_SLB_AGAIN_SETTING); 
//                    message = vsIpList.size() + " 건의 동기화 되지 않은 Virtual Server가 있어 \n" +
//                            "설정에 실패했습니다. \n" +
//                            "다시 설정해 보시기 바랍니다."; 
//                                " disable에 실패한 VS : " + vsIpList;
				}
				else
				{
					message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_SLB_SUCCESS);
				}
			}
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String disableVss() throws Exception
	{
		try
		{
			log.debug("{}, {}", adc, virtualSvrIndices);
//			
//			if(adcScope != null && !adcScope.equals("") &&  adcScope.getLevel() != 2)
//            {
//                log.debug("{}, {}", adcScope, vsAdcCheckInString);
//                virtualSvrFacade.disableAllVss(convertVsAdcCheckToJSON(vsAdcCheckInString), session.getSessionDto());
//            }
//            else
//            {
//                log.debug("{}, {}", adc, virtualSvrIndices);
//                virtualSvrFacade.disableVss(adc.getIndex(), adc.getType(), virtualSvrIndices, session.getSessionDto());
//            }  

			virtualSvrFacade.disableVss(adc.getIndex(), adc.getType(), virtualSvrIndices, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_SLB_SUCCESS);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		String rtnType = "";
		if (adc.getType().equals("F5"))
		{
			rtnType = "F5";
		}
		else if (adc.getType().equals("Alteon"))
		{
			rtnType = "ALTEON";
		}
		else if (adc.getType().equals("PAS"))
		{
			rtnType = "PAS";
		}
		else if (adc.getType().equals("PASK"))
		{
			rtnType = "PASK";
		}

		return rtnType;
	}

	private ArrayList<String> convertSvrIndices(Integer adcIndex, List<String> list)
	{
		ArrayList<String> retVal = new ArrayList<String>();

		for (String oneLine : list)
		{
			String[] items = oneLine.split("_", 2);
			if (items.length != 2) continue;
			String newVal = adcIndex + "_" + items[1];
			retVal.add(newVal);
		}
		return retVal;
	}

	public String disableVssPeer() throws Exception
	{
		if (adc.getType().equals("F5"))
		{
			return disableVssPeerF5();
		}
		else if (adc.getType().equals("Alteon"))
		{
			return disableVssPeerAlteon();
		}
		else if (adc.getType().equals("PAS"))
		{
			return disableVssPeerPAS();
		}
		else if (adc.getType().equals("PASK"))
		{
			return disableVssPeerPASK();
		}
		return "";
	}

	private String disableVssPeerF5() throws Exception
	{
//		isSuccessful = true;
		try
		{
			log.debug("{}, {}", adc, virtualSvrIndices);
			virtualSvrFacade.syncConifgF5(adc, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_PEER_SUCCESS);
			return "F5";
		} catch (OBException e)
		{
			throw e;
//			isSuccessful = false;
//			message = e.getMessage();
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//			e.printStackTrace();
//			isSuccessful = false;
//			message = OBMessageWeb.MSG_DISABLE_FAIL;
//			message = "Virtual Server Disable 중 오류가 발생하였습니다.\n관리자에게 문의 하십시오.";
		}
	}

	private String disableVssPeerAlteon() throws Exception
	{
		try
		{
			ArrayList<String> newVSIndices = convertSvrIndices(pairIndex, virtualSvrIndices);
			Integer adcIndex = pairIndex;
			String adcType = adc.getType();

			log.debug("{}, {}", adc, virtualSvrIndices);
			virtualSvrFacade.disableVss(adcIndex, adcType, newVSIndices, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_PEER_SUCCESS);

			return "ALTEON";
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private String disableVssPeerPAS() throws Exception
	{
		try
		{
			ArrayList<String> newVSIndices = convertSvrIndices(pairIndex, virtualSvrIndices);
			Integer adcIndex = pairIndex;
			String adcType = adc.getType();

			log.debug("{}, {}", adc, virtualSvrIndices);
			virtualSvrFacade.disableVss(adcIndex, adcType, newVSIndices, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_PEER_SUCCESS);

			return "PAS";
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private String disableVssPeerPASK() throws Exception
	{
		try
		{
			ArrayList<String> newVSIndices = convertSvrIndices(pairIndex, virtualSvrIndices);
			Integer adcIndex = pairIndex;
			String adcType = adc.getType();

			log.debug("{}, {}", adc, virtualSvrIndices);
			virtualSvrFacade.disableVss(adcIndex, adcType, newVSIndices, session.getSessionDto());
			return "PASK";
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public String delVss() throws Exception
	{
		try
		{
			log.debug("adc:{}, vsNames:{}, vsAdcCheck:{}", adc, virtualSvrIndices,
					convertVsAdcCheckToJSON(vsAdcCheckInString));

//            if(adcScope != null && !adcScope.equals("") &&  adcScope.getLevel() != 2)
//            {
//                log.debug("{}, {}", adcScope, vsAdcCheckInString);
//                virtualSvrFacade.delAllVss(convertVsAdcCheckToJSON(vsAdcCheckInString), session.getSessionDto());
//            }
//            else
//            {
//                log.debug("{}, {}", adc, virtualSvrIndices);
//                virtualSvrFacade.delVss(adc.getIndex(), adc.getType(), virtualSvrIndices, session.getSessionDto());
//            }

			virtualSvrFacade.delVss(adc.getIndex(), adc.getType(), virtualSvrIndices, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_SLB_SUCCESS);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		String rtnType = "";
		if (adc.getType().equals("F5"))
		{
			rtnType = "F5";
		}
		else if (adc.getType().equals("Alteon"))
		{
			rtnType = "ALTEON";
		}
		else if (adc.getType().equals("PAS"))
		{
			rtnType = "PAS";
		}
		else if (adc.getType().equals("PASK"))
		{
			rtnType = "PASK";
		}

		return rtnType;
	}

	public String delGroupVss() throws Exception
	{
		isSuccessful = true;

		try
		{
			if (adcScope != null && !adcScope.equals("") && adcScope.getLevel() != 2)
			{
				log.debug("{}, {}", adcScope, vsAdcCheckInString);
				vsFailList = virtualSvrFacade.delAllVss(convertVsAdcCheckToJSON(vsAdcCheckInString),
						session.getSessionDto());

				for (int i = 0; i < vsFailList.size(); i++)
				{
					vsIndexList.add(vsFailList.get(i).getVsIndex());
					vsIpList.add(vsFailList.get(i).getVsIp());
				}

				if (!vsFailList.isEmpty())
				{
					isSuccessful = false;
					message = vsIpList.size() + " "
							+ OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_SLB_CONFIG_NOT_SETTING) + "\n"
							+ OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_SLB_SETTING_FAIL_RESETTING);
//                    message = vsIpList.size() + " " + OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_VS_CONFIG_NOT_SETTING) + "\n" +
//                            OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_SLB_SETTING_FAIL) + "\n" +
//                            OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_SLB_AGAIN_SETTING); 
//                    message = vsIpList.size() + " 건의 동기화 되지 않은 Virtual Server가 있어 \n" +
//                            "설정에 실패했습니다. \n" +
//                            "다시 설정해 보시기 바랍니다."; 
//                                " 삭제에 실패한 VS : " + vsIpList;
				}
				else
				{
					message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_SLB_SUCCESS);
				}
			}
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String delVssPeer() throws Exception
	{
		if (adc.getType().equals("F5"))
		{
			return delVssPeerF5();
		}
		else if (adc.getType().equals("Alteon"))
		{
			return delVssPeerAlteon();
		}
		else if (adc.getType().equals("PAS"))
		{
			return delVssPeerPAS();
		}
		else if (adc.getType().equals("PASK"))
		{
			return delVssPeerPASK();
		}
		return "";
	}

//	public String retrieveValidGroupIndexAlteon() throws Exception
//	{
//		try
//		{
//			this.alteonPoolIndex = this.virtualSvrFacade.getValidPoolIndexAlteon(this.adc.getIndex(), this.alteonID);
//			this.alteonID = 111;
//		}
//		catch (OBException e) 
//		{
//			throw e;
//		}
//		catch (Exception e) 
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}			
//		return SUCCESS;
//	}

	public String retrieveValidIndexAlteon() throws Exception
	{
		try
		{
			this.alteonVSIndex = this.virtualSvrFacade.getValidVSIndexAlteon(this.adc.getIndex(), this.alteonVSIndex);
//			this.alteonPoolIndex = 111;//this.virtualSvrFacade.getValidPoolIndexAlteon(this.adc.getIndex(), this.alteonPoolIndex);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}

	public String isExistVirtualServer() throws Exception
	{
		try
		{
			isSuccessful = true;
			ArrayList<String> newVSIndices = convertSvrIndices(pairIndex, virtualSvrIndices);
			String adcType = adc.getType();

			isExistVSIndex = 0;
			if (virtualSvrFacade.isExistVirtualServer(pairIndex, adcType, newVSIndices))
			{
				isExistVSIndex = 1;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_PEER_VSRV_EXIST);
			}
			else
			{
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_PEER_VSRV_NOT_EXIST);
			}
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}

	public String checkVSIPAddress() throws Exception
	{
		try
		{
			isExistVSIPAddress = 0;
			String adcType = adc.getType();

			if (virtualSvrFacade.isExistVSIPAddress(adc.getIndex(), adcType, ipAddress))
			{
				isExistVSIPAddress = 1;
			}
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}

	private String delVssPeerF5() throws Exception
	{
		try
		{
			log.debug("{}, {}", adc, virtualSvrIndices);
			virtualSvrFacade.syncConifgF5(adc, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_PEER_SUCCESS);
			return "F5";
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private String delVssPeerAlteon() throws Exception
	{
		try
		{
			ArrayList<String> newVSIndices = convertSvrIndices(pairIndex, virtualSvrIndices);
			Integer adcIndex = pairIndex;
			String adcType = adc.getType();

			log.debug("{}, {}", adc, virtualSvrIndices);
			virtualSvrFacade.delVss(adcIndex, adcType, newVSIndices, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_PEER_SUCCESS);
			return "ALTEON";
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private String delVssPeerPAS() throws Exception
	{
		try
		{
			ArrayList<String> newVSIndices = convertSvrIndices(pairIndex, virtualSvrIndices);
			Integer adcIndex = pairIndex;
			String adcType = adc.getType();

			log.debug("{}, {}", adc, virtualSvrIndices);
			virtualSvrFacade.delVss(adcIndex, adcType, newVSIndices, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_PEER_SUCCESS);
			return "PAS";
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private String delVssPeerPASK() throws Exception
	{
		try
		{
			ArrayList<String> newVSIndices = convertSvrIndices(pairIndex, virtualSvrIndices);
			Integer adcIndex = pairIndex;
			String adcType = adc.getType();

			log.debug("{}, {}", adc, virtualSvrIndices);
			virtualSvrFacade.delVss(adcIndex, adcType, newVSIndices, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_PEER_SUCCESS);
			return "PASK";
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public String retrieveAdcPools() throws Exception
	{
		try
		{
			adcHealths_alteon = virtualSvrFacade.getALTEONHealths(adc.getIndex(), adc.getType());
			log.debug("{}", adcHealths_alteon);
			adcPools = virtualSvrFacade.getAdcPools(adc.getIndex(), adc.getType());
			this.alteonPoolIndex = this.virtualSvrFacade.getValidPoolIndexAlteon(this.adc.getIndex(),
					this.alteonVSIndex);
			this.alteonPoolIndexList = this.virtualSvrFacade.getPoolIndexListAlteon(this.adc.getIndex());
			log.debug("{}", adcPools);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

//	public String isExistPoolIndex() throws Exception
//	{
//		try 
//		{
//			isExistPoolIndex=0;
//			boolean retVal = this.virtualSvrFacade.isExistPoolIndex(this.adc.getIndex(), this.alteonPoolIndex);
//			if(retVal)
//				isExistPoolIndex=1;
//			
//			log.debug("{}", adcPools);
//		} 
//		catch (OBException e) 
//		{
//			throw e;
//		}
//		catch (Exception e) 
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//		
//		return SUCCESS;
//	}

	public String retrieveAdcNodes() throws Exception
	{
		try
		{
			adcNodes = virtualSvrFacade.getAdcNodes(adc.getIndex(), adc.getType(), poolIndex);
			log.debug("{}", adcNodes);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String retrieveVirtualSvc() throws Exception
	{
		try
		{
			adcHealths_alteon = virtualSvrFacade.getALTEONHealths(adc.getIndex(), adc.getType());
			log.debug("{}", adcHealths_alteon);
			virtualSvc = virtualSvrFacade.getAdcPool(adc.getIndex(), adc.getType(), poolIndex);
			log.debug("{}", virtualSvc);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String retrievePASKHealths() throws Exception
	{
		try
		{
			PASKHealths = virtualSvrFacade.getPASKHelathsChange(adc.getIndex(), adc.getType(), healthCheckDbIndex);
			log.debug("{}", PASKHealths);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String existsVirtualSvrName() throws Exception
	{
		log.debug("{}", adc);
		try
		{
			if (adc.getType().equals("F5"))
			{
				log.debug("{}", virtualSvrF5);
				existsVirtualSvr = virtualSvrFacade.existsVirtualSvrName(adc.getIndex(), adc.getType(),
						virtualSvrF5.getName());
			}
			else if (adc.getType().equals("Alteon"))
			{
				log.debug("{}", virtualSvrAlteon);
				existsVirtualSvr = virtualSvrFacade.existsVirtualSvrName(adc.getIndex(), adc.getType(),
						virtualSvrAlteon.getName());
			}
			else if (adc.getType().equals("PAS"))
			{
				log.debug("{}", virtualSvrPAS);
				existsVirtualSvr = virtualSvrFacade.existsVirtualSvrName(adc.getIndex(), adc.getType(),
						virtualSvrPAS.getName());
			}
			else if (adc.getType().equals("PASK"))
			{
				log.debug("{}", virtualSvrPASK);
				existsVirtualSvr = virtualSvrFacade.existsVirtualSvrName(adc.getIndex(), adc.getType(),
						virtualSvrPASK.getName());
			}

			log.debug("{}", existsVirtualSvr);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String existsVirtualSvr() throws Exception
	{
		log.debug("{}", adc);
		isSuccessful = true;
		try
		{
			if (adc.getType().equals("F5"))
			{
				log.debug("{}", virtualSvrF5);
				existsVirtualSvr = virtualSvrFacade.existsVirtualSvr(adc.getIndex(), adc.getType(),
						virtualSvrF5.getName(), virtualSvrF5.getVirtualIp(), virtualSvrF5.getServicePort(), null);
			}
			else if (adc.getType().equals("Alteon"))
			{
				log.debug("{}", virtualSvrAlteon);
				existsVirtualSvr = virtualSvrFacade.existsVirtualSvr(adc.getIndex(), adc.getType(),
						virtualSvrAlteon.getName(), virtualSvrAlteon.getVirtualIp(), null, null);
			}
			else if (adc.getType().equals("PAS"))
			{
				log.debug("{}", virtualSvrPAS);
				existsVirtualSvr = virtualSvrFacade.existsVirtualSvr(adc.getIndex(), adc.getType(),
						virtualSvrPAS.getName(), virtualSvrPAS.getVirtualIp(), null, null);
			}
			else if (adc.getType().equals("PASK"))
			{
				log.debug("{}", virtualSvrPASK);
				existsVirtualSvr = virtualSvrFacade.existsVirtualSvr(adc.getIndex(), adc.getType(),
						virtualSvrPASK.getName(), virtualSvrPASK.getVirtualIp(), null, null);
			}

			log.debug("{}", existsVirtualSvr);
		} catch (OBException e)
		{
			throw e;
		} catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public ArrayList<OBDtoAdcVSGroup> convertVsAdcCheckToJSON(String elementString)
	{
		if (StringUtils.isEmpty(elementString)) return null;

		vsAdcList.clear();
		Gson gson = new GsonBuilder().registerTypeAdapter(OBDtoAdcVSGroup.class, new AdcVSAdapter()).create();
		JsonParser parser = new JsonParser();
		JsonArray jarray = parser.parse(elementString).getAsJsonArray();
		for (JsonElement e : jarray)
			vsAdcList.add(gson.fromJson(e, OBDtoAdcVSGroup.class));

		return vsAdcList;
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

	public List<String> getVirtualSvrIndices()
	{
		return virtualSvrIndices;
	}

	public void setVirtualSvrIndices(List<String> virtualSvrIndices)
	{
		this.virtualSvrIndices = virtualSvrIndices;
	}

	public AlteonVsAddDto getAlteonVsAdd()
	{
		return alteonVsAdd;
	}

	public void setAlteonVsAdd(AlteonVsAddDto alteonVsAdd)
	{
		this.alteonVsAdd = alteonVsAdd;
	}

	public F5VsAddDto getF5VsAdd()
	{
		return f5VsAdd;
	}

	public void setF5VsAdd(F5VsAddDto f5VsAdd)
	{
		this.f5VsAdd = f5VsAdd;
	}

	public PasVsAddDto getPasVsAdd()
	{
		return pasVsAdd;
	}

	public void setPasVsAdd(PasVsAddDto pasVsAdd)
	{
		this.pasVsAdd = pasVsAdd;
	}

	public PaskVsAddDto getPaskVsAdd()
	{
		return paskVsAdd;
	}

	public void setPaskVsAdd(PaskVsAddDto paskVsAdd)
	{
		this.paskVsAdd = paskVsAdd;
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

	public void setAdcHealths_alteon(List<AdcALTEONHealthCheckDto> adcHealths_alteon)
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

	public AdcPASKHealthCheckDto getPASKHealths()
	{
		return PASKHealths;
	}

	public void setPASKHealths(AdcPASKHealthCheckDto pASKHealths)
	{
		PASKHealths = pASKHealths;
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

	public VirtualSvrPASDto getVirtualSvrPAS()
	{
		return virtualSvrPAS;
	}

	public void setVirtualSvrPAS(VirtualSvrPASDto virtualSvrPAS)
	{
		this.virtualSvrPAS = virtualSvrPAS;
	}

	public VirtualSvrPASKDto getVirtualSvrPASK()
	{
		return virtualSvrPASK;
	}

	public void setVirtualSvrPASK(VirtualSvrPASKDto virtualSvrPASK)
	{
		this.virtualSvrPASK = virtualSvrPASK;
	}

	public Boolean getExistsVirtualSvr()
	{
		return existsVirtualSvr;
	}

	public void setExistsVirtualSvr(Boolean existsVirtualSvr)
	{
		this.existsVirtualSvr = existsVirtualSvr;
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

	public Boolean getRefreshes()
	{
		return refreshes;
	}

	public void setRefreshes(Boolean refreshes)
	{
		this.refreshes = refreshes;
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

	public List<ProfileDto> getProfiles()
	{
		return profiles;
	}

	public void setProfiles(List<ProfileDto> profiles)
	{
		this.profiles = profiles;
	}

	public Integer getPairIndex()
	{
		return pairIndex;
	}

	public void setPairIndex(Integer pairIndex)
	{
		this.pairIndex = pairIndex;
	}

	public Integer getIsExistVSIndex()
	{
		return isExistVSIndex;
	}

	public void setIsExistVSIndex(Integer isExistVSIndex)
	{
		this.isExistVSIndex = isExistVSIndex;
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

	public String getIpAddress()
	{
		return ipAddress;
	}

	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}

	public Integer getIsExistVSIPAddress()
	{
		return isExistVSIPAddress;
	}

	public void setIsExistVSIPAddress(Integer isExistVSIPAddress)
	{
		this.isExistVSIPAddress = isExistVSIPAddress;
	}

	public Integer getIsExistPoolIndex()
	{
		return isExistPoolIndex;
	}

	public void setIsExistPoolIndex(Integer isExistPoolIndex)
	{
		this.isExistPoolIndex = isExistPoolIndex;
	}

	public Integer getExtraKey()
	{
		return extraKey;
	}

	public void setExtraKey(Integer extraKey)
	{
		this.extraKey = extraKey;
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

	public VirtualSvrFacade getVirtualServerFacade()
	{
		return virtualSvrFacade;
	}

	public OBDtoAdcScope getAdcScope()
	{
		return adcScope;
	}

	public void setAdcScope(OBDtoAdcScope adcScope)
	{
		this.adcScope = adcScope;
	}

	public List<OBDtoAdcVServerAll> getVirtualServerAllList()
	{
		return virtualServerAllList;
	}

	public void setVirtualServerAllList(List<OBDtoAdcVServerAll> virtualServerAllList)
	{
		this.virtualServerAllList = virtualServerAllList;
	}

	public String getVsAdcCheckInString()
	{
		return vsAdcCheckInString;
	}

	public void setVsAdcCheckInString(String vsAdcCheckInString)
	{
		this.vsAdcCheckInString = vsAdcCheckInString;
	}

	public ArrayList<OBDtoAdcVSGroup> getVsAdcList()
	{
		return vsAdcList;
	}

	public void setVsAdcList(ArrayList<OBDtoAdcVSGroup> vsAdcList)
	{
		this.vsAdcList = vsAdcList;
	}

	public ArrayList<OBDtoAdcVSGroup> getVsFailList()
	{
		return vsFailList;
	}

	public void setVsFailList(ArrayList<OBDtoAdcVSGroup> vsFailList)
	{
		this.vsFailList = vsFailList;
	}

	public ArrayList<String> getVsIndexList()
	{
		return vsIndexList;
	}

	public void setVsIndexList(ArrayList<String> vsIndexList)
	{
		this.vsIndexList = vsIndexList;
	}

	public ArrayList<String> getVsIpList()
	{
		return vsIpList;
	}

	public void setVsIpList(ArrayList<String> vsIpList)
	{
		this.vsIpList = vsIpList;
	}

	public List<AdcNodeDto> getAdcRsNodes()
	{
		return adcRsNodes;
	}

	public void setAdcRsNodes(List<AdcNodeDto> adcRsNodes)
	{
		this.adcRsNodes = adcRsNodes;
	}

	@Override
	public String toString()
	{
		return "VirtualServerAction [virtualSvrFacade=" + virtualSvrFacade + ", profileFacade=" + profileFacade
				+ ", adcFacade=" + adcFacade + ", rowTotal=" + rowTotal + ", adc=" + adc + ", poolIndex=" + poolIndex
				+ ", healthCheckDbIndex=" + healthCheckDbIndex + ", virtualServers=" + virtualServers
				+ ", virtualSvrIndices=" + virtualSvrIndices + ", alteonVsAdd=" + alteonVsAdd + ", f5VsAdd=" + f5VsAdd
				+ ", pasVsAdd=" + pasVsAdd + ", paskVsAdd=" + paskVsAdd + ", interfaces=" + interfaces + ", adcHealths="
				+ adcHealths + ", adcHealths_alteon=" + adcHealths_alteon + ", adcPools=" + adcPools
				+ ", alteonPoolIndexList=" + alteonPoolIndexList + ", adcNodes=" + adcNodes + ", virtualSvc="
				+ virtualSvc + ", PASKHealths=" + PASKHealths + ", virtualSvrAlteon=" + virtualSvrAlteon
				+ ", virtualSvrF5=" + virtualSvrF5 + ", virtualSvrPAS=" + virtualSvrPAS + ", virtualSvrPASK="
				+ virtualSvrPASK + ", existsVirtualSvr=" + existsVirtualSvr + ", searchKey=" + searchKey + ", fromRow="
				+ fromRow + ", toRow=" + toRow + ", refreshes=" + refreshes + ", orderDir=" + orderDir + ", orderType="
				+ orderType + ", version=" + version + ", version_=" + version_ + ", profiles=" + profiles
				+ ", pairIndex=" + pairIndex + ", isExistVSIndex=" + isExistVSIndex + ", alteonVSIndex=" + alteonVSIndex
				+ ", alteonPoolIndex=" + alteonPoolIndex + ", ipAddress=" + ipAddress + ", isExistVSIPAddress="
				+ isExistVSIPAddress + ", isExistPoolIndex=" + isExistPoolIndex + ", extraKey=" + extraKey
				+ ", vsNameList=" + vsNameList + ", availableVlans=" + availableVlans + ", registeredVlanMap="
				+ registeredVlanMap + ", adcScope=" + adcScope + ", virtualServerAllList=" + virtualServerAllList
				+ ", vsAdcCheckInString=" + vsAdcCheckInString + ", vsAdcList=" + vsAdcList + ", vsFailList="
				+ vsFailList + ", vsIndexList=" + vsIndexList + ", vsIpList=" + vsIpList + "]";
	}
}