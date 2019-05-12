package kr.openbase.adcsmart.web.facade.adcman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBAdcVServer;
import kr.openbase.adcsmart.service.dto.OBDtoAdcHealthCheckAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcScope;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVSGroup;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVService;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSLBUpdateStatus;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.alteon.OBAdcVServerAlteon;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcVServerAll;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcVlan;
import kr.openbase.adcsmart.service.impl.f5.DtoVlanTunnelFilter;
import kr.openbase.adcsmart.service.impl.f5.OBAdcVServerF5;
import kr.openbase.adcsmart.service.impl.pas.OBAdcVServerPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcNodePAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcVServerPAS;
import kr.openbase.adcsmart.service.impl.pask.OBAdcVServerPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcHealthCheckPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcNodePASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcVServerPASK;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.adcman.AdcSettingAction;
import kr.openbase.adcsmart.web.facade.dto.AdcALTEONHealthCheckDto;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.AdcGroupDto;
import kr.openbase.adcsmart.web.facade.dto.AdcNodeDto;
import kr.openbase.adcsmart.web.facade.dto.AdcPASKHealthCheckDto;
import kr.openbase.adcsmart.web.facade.dto.AdcPoolDto;
import kr.openbase.adcsmart.web.facade.dto.AlteonVsAddDto;
import kr.openbase.adcsmart.web.facade.dto.F5VsAddDto;
import kr.openbase.adcsmart.web.facade.dto.InterfaceDto;
import kr.openbase.adcsmart.web.facade.dto.PasVsAddDto;
import kr.openbase.adcsmart.web.facade.dto.PaskVsAddDto;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;
import kr.openbase.adcsmart.web.facade.dto.VirtualSvcDto;
import kr.openbase.adcsmart.web.facade.dto.VirtualSvrAlteonDto;
import kr.openbase.adcsmart.web.facade.dto.VirtualSvrDto;
import kr.openbase.adcsmart.web.facade.dto.VirtualSvrF5Dto;
import kr.openbase.adcsmart.web.facade.dto.VirtualSvrPASDto;
import kr.openbase.adcsmart.web.facade.dto.VirtualSvrPASKDto;

/*
 * 코딩 가이드:
 * 1. public 함수의 Exception은 무조건 OBException, Exception 두종류로  throw 한다.
 * 2. 내부에서 exception의 try-catch는 가급적이면 하지 않는다. 필요할 경우에만 추가한다.
 *
 */

@Component
public class VirtualSvrFacade 
{
	private static transient Logger log = LoggerFactory.getLogger(AdcSettingAction.class);
	
	@Autowired
	private AdcFacade adcFacade;
	
	private OBAdcManagementImpl adcImpl;
	private OBAdcVServer alteonVsMgmt;
	private OBAdcVServer f5VsMgmt;
	private OBAdcVServer pasVsMgmt;
	private OBAdcVServer paskVsMgmt;

	public VirtualSvrFacade() 
	{
	    adcImpl = new OBAdcManagementImpl();
		alteonVsMgmt = new OBAdcVServerAlteon();
		f5VsMgmt = new OBAdcVServerF5();
		pasVsMgmt = new OBAdcVServerPAS();
		paskVsMgmt = new OBAdcVServerPASK();
		
	}
	
	public Integer getVirtualSvrAllTotal(OBDtoAdcScope adcScope, Integer accntIndex, String searchKey) throws OBException, Exception
    {
        return f5VsMgmt.searchVServerListAllListCount(adcScope, accntIndex, searchKey);
    }
    
	public Integer getVirtualSvrTotal(AdcDto adc, Integer accntIndex, String searchKey) throws OBException, Exception  
	{
		if (adc.getType().equals("F5")) 
		{
			return f5VsMgmt.searchVServerListF5Count(adc.getIndex(), accntIndex, searchKey);
		} 
		else if (adc.getType().equals("Alteon"))
		{
			return alteonVsMgmt.searchVServerListAlteonCount(adc.getIndex(), accntIndex, searchKey);
		}
		else if (adc.getType().equals("PAS"))
		{
			return pasVsMgmt.searchVServerListPASCount(adc.getIndex(), accntIndex, searchKey);
		}
		else if (adc.getType().equals("PASK"))
		{
			return paskVsMgmt.searchVServerListPASKCount(adc.getIndex(), accntIndex, searchKey);
		}
		else
		{
			return null;
		}
	}

	public Map<String, List<VirtualSvrDto>> getAdcGroupToVirtualServersMap(Integer accountIndex) throws OBException, Exception
	{
		log.debug("getAdcGroupToVirtualServersMap: {}", accountIndex);
		Map<String, List<VirtualSvrDto>> adcGroupToVirtualServersMap = new HashMap<String, List<VirtualSvrDto>>();
		for (Entry<String, List<AdcDto>> entry : adcFacade.getAdcGroupToAdcsMap(accountIndex, null).entrySet()) 
		{
			List<VirtualSvrDto> virtualServers = new ArrayList<VirtualSvrDto>();
			for (AdcDto adc : entry.getValue()) 
			{
				if (adc.getType().equals("F5")) 
				{
					List<VirtualSvrF5Dto> virtualSvrs = VirtualSvrF5Dto.toVirtualSvrF5Dto(f5VsMgmt.getVServerListF5(adc.getIndex()));
					log.debug("{}", virtualSvrs);
					virtualServers.addAll(virtualSvrs);
				} 
				else if (adc.getType().equals("Alteon"))
				{
					List<VirtualSvrAlteonDto> virtualSvrs = VirtualSvrAlteonDto.toVirtualSvrAlteonDto(alteonVsMgmt.getVServerListAlteon(adc.getIndex()));
					log.debug("{}", virtualSvrs);
					virtualServers.addAll(virtualSvrs);
				}
				else if (adc.getType().equals("PAS"))
				{
					List<VirtualSvrPASDto> virtualSvrs = VirtualSvrPASDto.toVirtualSvrPasDto(pasVsMgmt.getVServerListPAS(adc.getIndex()));
					log.debug("{}", virtualSvrs);
					virtualServers.addAll(virtualSvrs);
				}
//				else if (adc.getType().equals("PASK"))
//				{//TODO
//					List<VirtualSvrPASKDto> virtualSvrs = VirtualSvrPASKDto.toVirtualSvrPaskDto(paskVsMgmt.getVServerListPASK(adc.getIndex()));
//					log.debug("{}", virtualSvrs);
//					virtualServers.addAll(virtualSvrs);
//				}
			}

			adcGroupToVirtualServersMap.put(entry.getKey(), virtualServers);
		}

		return adcGroupToVirtualServersMap;
	}

	public Map<String, AdcDto> getAdcMap(Integer accountIndex) throws OBException, Exception 
	{
		log.debug("getAdcMap: {}", accountIndex);
		Map<String, AdcDto> adcMap = new HashMap<String, AdcDto>();
		List<AdcDto> adcs = adcFacade.getAdcsByAccountIndex(accountIndex);
		log.debug("{}", adcs);
		for (AdcDto adc : adcs)
			adcMap.put(String.valueOf(adc.getIndex()), adc);

		return adcMap;
	}

	public Map<String, AdcGroupDto> getGroupMap() throws OBException, Exception 
	{
		Map<String, AdcGroupDto> adcGroupsMap = adcFacade.getAdcGroupsMap();
		log.debug("{}", adcGroupsMap);
		return adcGroupsMap;
	}

	public ArrayList<String> getVsNameList(int adcIndex) throws OBException, Exception
	{
		return alteonVsMgmt.getVsNameList(adcIndex);
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
		else if (adc.getType().equals("PAS"))
		{
		    retVal =  pasVsMgmt.updateSLBStatus(adc.getIndex());
		}
		
		else if (adc.getType().equals("PASK"))
		{
		    retVal =  paskVsMgmt.updateSLBStatus(adc.getIndex());
		}
        return retVal;		
	}	
	
	// 전체 그룹인 경우 list
    public ArrayList<OBDtoAdcVServerAll> getVirtualServerAllList(OBDtoAdcScope adcScope, Integer accntIndex, String searchKey, Integer fromRow, Integer toRow, Integer orderType, Integer orderDir) throws OBException, Exception
    {       
        ArrayList<OBDtoAdcVServerAll> virtualSvrsAllList = new ArrayList<OBDtoAdcVServerAll>();
//        nodeF5Detail = VirtualSvrDto.toNodeF5DetailDto(f5VsMgmt.searchNodeListAll(adcScope, accntIndex, searchKey, fromRow, toRow, orderType, orderDir));
//        List<VirtualSvrF5Dto> f5VirtualSvrs = VirtualSvrF5Dto.toVirtualSvrF5Dto(f5VsMgmt.searchVServerListAll(adcScope, accntIndex, StringUtils.isEmpty(searchKey) ? null : searchKey, fromRow, toRow, orderType, orderDir));
        virtualSvrsAllList = f5VsMgmt.searchVServerListAll(adcScope, accntIndex, searchKey, fromRow, toRow, orderType, orderDir);
        log.debug("{}", virtualSvrsAllList); 
        return virtualSvrsAllList;
    }
	
	public List<VirtualSvrDto> getVirtualServerList(AdcDto adc, Integer accntIndex, String searchKey, Integer fromRow, Integer toRow, Integer orderType, Integer orderDir) throws OBException, Exception 
	{
		List<VirtualSvrDto> virtualServers = new ArrayList<VirtualSvrDto>();
		if (adc.getType().equals("F5")) 
		{
			List<VirtualSvrF5Dto> f5VirtualSvrs = VirtualSvrF5Dto.toVirtualSvrF5Dto(f5VsMgmt.searchVServerListF5(adc.getIndex(), accntIndex, StringUtils.isEmpty(searchKey) ? null : searchKey, fromRow, toRow, orderType, orderDir));
			log.debug("{}", f5VirtualSvrs);
			virtualServers.addAll(f5VirtualSvrs);
		}
		else if (adc.getType().equals("Alteon"))
		{
			List<VirtualSvrAlteonDto> alteonVirtualSvrs = VirtualSvrAlteonDto.toVirtualSvrAlteonDto(alteonVsMgmt.searchVServerListAlteon(adc.getIndex(), accntIndex, StringUtils.isEmpty(searchKey) ? null : searchKey, fromRow, toRow, orderType, orderDir));
			log.debug("{}", alteonVirtualSvrs);
			virtualServers.addAll(alteonVirtualSvrs);
		}
		else if (adc.getType().equals("PAS"))
		{
			List<VirtualSvrPASDto> pasVirtualSvrs = VirtualSvrPASDto.toVirtualSvrPasDto(pasVsMgmt.searchVServerListPAS(adc.getIndex(), accntIndex, StringUtils.isEmpty(searchKey) ? null : searchKey, fromRow, toRow, orderType, orderDir));
			log.debug("{}", pasVirtualSvrs);
			virtualServers.addAll(pasVirtualSvrs);
		}
		else if (adc.getType().equals("PASK"))
		{
			List<VirtualSvrPASKDto> paskVirtualSvrs = VirtualSvrPASKDto.toVirtualSvrPASKDto(paskVsMgmt.searchVServerListPASK(adc.getIndex(), accntIndex, StringUtils.isEmpty(searchKey) ? null : searchKey, fromRow, toRow, orderType, orderDir));
			log.debug("{}", paskVirtualSvrs);
			virtualServers.addAll(paskVirtualSvrs);
		}
		return virtualServers;
	}	
	
	public List<VirtualSvrDto> getVSListUsedByPool(AdcDto adc, String poolNameID) throws OBException, Exception 
	{
		List<VirtualSvrDto> virtualServers = new ArrayList<VirtualSvrDto>();
		if (adc.getType().equals("F5")) 
		{
			List<VirtualSvrF5Dto> f5VirtualSvrs = VirtualSvrF5Dto.toVirtualSvrF5Dto(f5VsMgmt.searchVSListUsedByPoolF5(adc.getIndex(), poolNameID));
			log.debug("{}", f5VirtualSvrs);
			virtualServers.addAll(f5VirtualSvrs);
		}
		else if (adc.getType().equals("Alteon"))
		{
			List<VirtualSvrAlteonDto> alteonVirtualSvrs = VirtualSvrAlteonDto.toVirtualSvrAlteonDto(alteonVsMgmt.searchVSListUsedByPoolAlteon(adc.getIndex(), Integer.parseInt(poolNameID)));
			log.debug("{}", alteonVirtualSvrs);
			virtualServers.addAll(alteonVirtualSvrs);
		}
		else if (adc.getType().equals("PAS"))
		{
			List<VirtualSvrPASDto> pasVirtualSvrs = VirtualSvrPASDto.toVirtualSvrPasDto(pasVsMgmt.searchVSListUsedByPoolPAS(adc.getIndex(), poolNameID));
			log.debug("{}", pasVirtualSvrs);
			virtualServers.addAll(pasVirtualSvrs);
		}
		else if (adc.getType().equals("PASK"))
		{
			List<VirtualSvrPASKDto> paskVirtualSvrs = VirtualSvrPASKDto.toVirtualSvrPASKDto(paskVsMgmt.searchVSListUsedByPoolPASK(adc.getIndex(), poolNameID));
			log.debug("{}", paskVirtualSvrs);
			virtualServers.addAll(paskVirtualSvrs);
		}
		return virtualServers;
	}	
	
	public AlteonVsAddDto getVirtualServerAsAlteonVsAddDto(int adcIndex, String index) throws OBException, Exception 
	{
		log.debug("getVirtualServerAsAlteonVsAddDto: {}, {}", adcIndex, index);
		VirtualSvrAlteonDto virtualSvr = null;
		OBDtoAdcVServerAlteon virtualSvrFromSvc = alteonVsMgmt.getVServerAlteonInfo(adcIndex, index);
		log.debug("{}", virtualSvrFromSvc);
		virtualSvr = VirtualSvrAlteonDto.toVirtualSvrDto(virtualSvrFromSvc);
		log.debug("{}", virtualSvr);
		
		return makeAlteonVsAddDto(adcIndex, virtualSvr);
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
	
	public F5VsAddDto getVirtualSvrAsF5VsAddDto(int adcIndex, String index) throws OBException, Exception 
	{
		log.debug("getVirtualSvrAsF5VsAddDto: {}, {}", adcIndex, index);
		VirtualSvrF5Dto virtualSvr = null;
		virtualSvr = VirtualSvrF5Dto.toVirtualSvrDto(f5VsMgmt.getVServerF5Info(adcIndex, index));
		log.debug("{}", virtualSvr);
		return makeF5VsAddDto(adcIndex, virtualSvr);
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
			f5VsAddDto.setEventMembers(virtualSvr.getPool().getEventMembers());
		}
		
		f5VsAddDto.setPort(virtualSvr.getServicePort());
		f5VsAddDto.setProfileIndex(virtualSvr.getProfileIndex());
		f5VsAddDto.setVlanTunnel(virtualSvr.getVlanFilter());
		return f5VsAddDto;
	}
	
	public  PasVsAddDto getVirtualSvrAsPASVsAddDto(int adcIndex, String index) throws OBException, Exception 
	{
		log.debug("getVirtualSvrAsPASVsAddDto: {}, {}", adcIndex, index);
		VirtualSvrPASDto virtualSvr = null;
		virtualSvr = VirtualSvrPASDto.toVirtualSvrDto(pasVsMgmt.getVServerPASInfo(adcIndex, index));
		log.debug("{}", virtualSvr);
		return makePASVsAddDto(adcIndex, virtualSvr);
	}	
	private PasVsAddDto makePASVsAddDto(int adcIndex, VirtualSvrPASDto virtualSvr) throws OBException, Exception 
	{
		log.debug("makePASVsAddDto: {}, {}", adcIndex, virtualSvr);
		PasVsAddDto PasVsAddDto = new PasVsAddDto();
		PasVsAddDto.setIndex(virtualSvr.getIndex());
		PasVsAddDto.setAdcIndex(adcIndex);
		PasVsAddDto.setIp(virtualSvr.getVirtualIp());
		PasVsAddDto.setName(virtualSvr.getName());
		PasVsAddDto.setProtocol(virtualSvr.getServiceProtocol());
		PasVsAddDto.setState(virtualSvr.getState());
		if (virtualSvr.getPool() != null)
		{
			PasVsAddDto.setPoolIndex(virtualSvr.getPool().getIndex());
			PasVsAddDto.setPoolName(virtualSvr.getPool().getName());			
			PasVsAddDto.setLoadBalancingType(virtualSvr.getPool().getLoadBalancingType());
			if(virtualSvr.getPool().getAdcPASHealthCheck() != null)
			{
				PasVsAddDto.setHealthCheckType(virtualSvr.getPool().getAdcPASHealthCheck().getType());
				PasVsAddDto.setHealthCheckId(virtualSvr.getPool().getAdcPASHealthCheck().getId());
			}
			PasVsAddDto.setMembers(virtualSvr.getPool().getMembers());			
		}		
		PasVsAddDto.setPort(virtualSvr.getServicePort());
		
		return PasVsAddDto;
	}
	
	public  PaskVsAddDto getVirtualSvrAsPASKVsAddDto(int adcIndex, String index) throws OBException, Exception 
	{
		log.debug("getVirtualSvrAsPASKVsAddDto: {}, {}", adcIndex, index);
		VirtualSvrPASKDto virtualSvr = null;
		virtualSvr = VirtualSvrPASKDto.toVirtualSvrDto(paskVsMgmt.getVServerPASKInfo(adcIndex, index));
		log.debug("{}", virtualSvr);
		return makePASKVsAddDto(adcIndex, virtualSvr);
	}	
	private PaskVsAddDto makePASKVsAddDto(int adcIndex, VirtualSvrPASKDto virtualSvr) throws OBException, Exception 
	{
		log.debug("makePASKVsAddDto: {}, {}", adcIndex, virtualSvr);
		PaskVsAddDto PaskVsAddDto = new PaskVsAddDto();
		PaskVsAddDto.setIndex(virtualSvr.getIndex());
		PaskVsAddDto.setAdcIndex(adcIndex);
		PaskVsAddDto.setIp(virtualSvr.getVirtualIp());
		PaskVsAddDto.setIpView(virtualSvr.getServiceIpView());
		PaskVsAddDto.setName(virtualSvr.getName());
		PaskVsAddDto.setProtocol(virtualSvr.getServiceProtocol());
		PaskVsAddDto.setState(virtualSvr.getState());
		PaskVsAddDto.setVipInfo(virtualSvr.getServiceSubInfo());
		if (virtualSvr.getPool() != null)
		{
			PaskVsAddDto.setPoolIndex(virtualSvr.getPool().getIndex());
			PaskVsAddDto.setPoolName(virtualSvr.getPool().getName());			
			PaskVsAddDto.setLoadBalancingType(virtualSvr.getPool().getLoadBalancingType());
			if(virtualSvr.getPool().getAdcPASKHealthCheck() != null)
			{
				PaskVsAddDto.setHealthCheckDbIndex(virtualSvr.getPool().getAdcPASKHealthCheck().getDbIndex());
				PaskVsAddDto.setHealthCheckId(virtualSvr.getPool().getAdcPASKHealthCheck().getId());
				PaskVsAddDto.setHealthCheckName(virtualSvr.getPool().getAdcPASKHealthCheck().getName());				
				PaskVsAddDto.setHealthCheckType(virtualSvr.getPool().getAdcPASKHealthCheck().getType());
				PaskVsAddDto.setHealthCheckPort(virtualSvr.getPool().getAdcPASKHealthCheck().getPort());
				PaskVsAddDto.setHealthCheckInterval(virtualSvr.getPool().getAdcPASKHealthCheck().getInterval());
				PaskVsAddDto.setHealthCheckTimeout(virtualSvr.getPool().getAdcPASKHealthCheck().getTimeout());
				PaskVsAddDto.setHealthCheckState(virtualSvr.getPool().getAdcPASKHealthCheck().getState());				
			}
			PaskVsAddDto.setMembers(virtualSvr.getPool().getMembers());			
		}		
		PaskVsAddDto.setPort(virtualSvr.getServicePort());
		PaskVsAddDto.setPortView(virtualSvr.getServicePortView());
		if (virtualSvr.isServiceConfigurable() == true)
		{
			PaskVsAddDto.setConfigurable(1);
		}
		else if (virtualSvr.isServiceConfigurable() == false)
		{
			PaskVsAddDto.setConfigurable(0);
		}
		
		return PaskVsAddDto;
	}
	
	public AdcPoolDto getAdcPool(int adcIndex, String adcType, String poolIndex) throws OBException, Exception  
	{
		log.debug("getAdcPool: {}, {}, {}", new Object[]{adcIndex, adcType, poolIndex});
		AdcPoolDto pool = null;
		if (adcType.equals("F5"))
		{
			OBDtoAdcPoolF5 poolFromSvc = f5VsMgmt.getPoolF5(poolIndex);
			log.debug("{}", poolFromSvc);
			pool = AdcPoolDto.toAdcPoolDto(poolFromSvc);
		} 
		else if (adcType.equals("Alteon"))
		{
			OBDtoAdcPoolAlteon poolFromSvc = alteonVsMgmt.getPoolAlteon(poolIndex);
			log.debug("{}", poolFromSvc);
			pool = AdcPoolDto.toAdcPoolDto(poolFromSvc);
		}
		else if (adcType.equals("PAS"))
		{
			OBDtoAdcPoolPAS poolFromSvc = pasVsMgmt.getPoolPAS(poolIndex);
			log.debug("{}", poolFromSvc);
			pool = AdcPoolDto.toAdcPoolDto(poolFromSvc);
		}
		else if (adcType.equals("PASK"))
		{
			OBDtoAdcPoolPASK poolFromSvc = paskVsMgmt.getPoolPASK(poolIndex);
			log.debug("{}", poolFromSvc);
			pool = AdcPoolDto.toAdcPoolDto(poolFromSvc);
		}		
		log.debug("{}", pool);
		return pool;
	}
	
	public List<AdcPoolDto> getAdcPools(int adcIndex, String adcType) throws OBException, Exception  
	{
		log.debug("adcIndex: {}, {}", adcIndex, adcType);
		List<AdcPoolDto> pools = null;
		if (adcType.equals("F5")) 
		{
			List<OBDtoAdcPoolF5> poolsFromSvc = f5VsMgmt.getPoolF5List(adcIndex);
			log.debug("{}", poolsFromSvc);
			pools = AdcPoolDto.toAdcPoolF5Dto(poolsFromSvc);
		}
		else if (adcType.equals("Alteon")) 
		{
			List<OBDtoAdcPoolAlteon> poolsFromSvc = alteonVsMgmt.getPoolAlteonList(adcIndex);
			log.debug("{}", poolsFromSvc);
			pools = AdcPoolDto.toAdcPoolAlteonDto(poolsFromSvc);
		}
		else if (adcType.equals("PAS"))
		{
			List<OBDtoAdcPoolPAS> poolsFromSvc = pasVsMgmt.getPoolPASList(adcIndex);
			log.debug("{}", poolsFromSvc);
			pools = AdcPoolDto.toAdcPoolPASDto(poolsFromSvc);
			
		}
		else if (adcType.equals("PASK"))
		{
			List<OBDtoAdcPoolPASK> poolsFromSvc = paskVsMgmt.getPoolPASKList(adcIndex);
			log.debug("{}", poolsFromSvc);
			pools = AdcPoolDto.toAdcPoolPASKDto(poolsFromSvc);
			
		}
		log.debug("{}", pools);
		return pools;
	}

	public List<AdcNodeDto> getAdcNodes(int adcIndex, String adcType, String poolIndex) throws OBException, Exception  
	{
		log.debug("getAdcNodes: {}, {}, {}", new Object[]{adcIndex, adcType, poolIndex});
		List<AdcNodeDto> nodes = null;
		if (adcType.equals("F5")) 
		{
			ArrayList<OBDtoAdcNodeF5> nodesFromSvc = f5VsMgmt.getNodeAvailableListF5(adcIndex);
			log.debug("{}", nodesFromSvc);
			nodes = AdcNodeDto.toAdcNodeF5Dto(nodesFromSvc);
		}
		else if (adcType.equals("Alteon"))
		{
			ArrayList<OBDtoAdcNodeAlteon> nodesFromSvc = alteonVsMgmt.getNodeAvailableListAlteon(adcIndex, StringUtils.isEmpty(poolIndex) ? null : poolIndex);
			log.debug("{}", nodesFromSvc);
			nodes = AdcNodeDto.toAdcNodeAlteonDto(nodesFromSvc);
		}
		else if (adcType.equals("PAS"))
		{
			ArrayList<OBDtoAdcNodePAS> nodesFromSvc = pasVsMgmt.getNodeAvailableListPAS(adcIndex, StringUtils.isEmpty(poolIndex) ? null : poolIndex);
			log.debug("{}", nodesFromSvc);
			nodes = AdcNodeDto.toAdcNodePASDto(nodesFromSvc);
		}
		else if (adcType.equals("PASK"))
		{
			ArrayList<OBDtoAdcNodePASK> nodesFromSvc = paskVsMgmt.getNodeAvailableListPASK(adcIndex, StringUtils.isEmpty(poolIndex) ? null : poolIndex);
			log.debug("{}", nodesFromSvc);
			nodes = AdcNodeDto.toAdcNodePASKDto(nodesFromSvc);
		}
		log.debug("{}", nodes);
		return nodes;
	}
	public AdcPASKHealthCheckDto getPASKHelathsChange(int adcIndex, String adcType, String healthCheckDbIndex) throws OBException, Exception  
	{	
		log.debug("getPASKHelathsChange: {}, {}, {}", new Object[]{adcIndex, adcType, healthCheckDbIndex});
		AdcPASKHealthCheckDto health = null;
		if (adcType.equals("PASK"))
		{
			OBDtoAdcHealthCheckPASK HealthFromSvc = paskVsMgmt.getHealthCheckPASK(healthCheckDbIndex);
			log.debug("{}", HealthFromSvc);
			health = AdcPASKHealthCheckDto.toAdcPASKHealthCheckDto(HealthFromSvc);
		}		
		log.debug("{}", health);
		return health;
	}
	
	public List<AdcPASKHealthCheckDto> getPASKHealths(int adcIndex, String adcType) throws OBException, Exception  
	{
		log.debug("adcIndex: {}, {}", adcIndex, adcType);
		List<AdcPASKHealthCheckDto> helaths = null;
		if (adcType.equals("PASK"))
		{
			List<OBDtoAdcHealthCheckPASK> healthsFromSvc = paskVsMgmt.getHealthCheckListPASK(adcIndex);
			log.debug("{}", healthsFromSvc);
			helaths = AdcPASKHealthCheckDto.toAdcPASKHealthCheckDto(healthsFromSvc);				
		}
		log.debug("{}", helaths);
		return helaths;
	}
	
	public List<AdcALTEONHealthCheckDto> getALTEONHealths(int adcIndex, String adcType) throws OBException, Exception  
	{
		log.debug("adcIndex: {}, {}", adcIndex, adcType);
		List<AdcALTEONHealthCheckDto> helaths = null;
		if (adcType.equals("Alteon"))
		{
			List<OBDtoAdcHealthCheckAlteon> healthsFromSvc = alteonVsMgmt.getHealthCheckListAlteon(adcIndex);
			log.debug("{}", healthsFromSvc);
			helaths = AdcALTEONHealthCheckDto.toAdcALTEONHealthCheckDto(healthsFromSvc);				
		}
		log.debug("{}", helaths);
		return helaths;
	}
	
	public List<InterfaceDto> getInterfaces(int adcIndex, String adcType) throws OBException, Exception 
	{
		log.debug("getInterfaces: {}, {}", adcIndex, adcType);
		List<InterfaceDto> interfaces = null;
		interfaces = InterfaceDto.toInterfaceDto(adcType.equals("F5") ? f5VsMgmt.getL3InterfaceList(adcIndex) : alteonVsMgmt
				.getL3InterfaceList(adcIndex));
		log.debug("{}", interfaces);
		return interfaces;
	}

	public void enableVss(int adcIndex, String adcType, List<String> virtualSvrIndices, SessionDto session) throws OBException, Exception 
	{
		log.debug("enableVss: {}, {}, {}", new Object[]{adcIndex, adcType, virtualSvrIndices});
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(virtualSvrIndices.toString());
		if (adcType.equals("F5"))
		{
			f5VsMgmt.enableVServer(adcIndex, new ArrayList<String>(virtualSvrIndices), extraInfo);
		}
		else if (adcType.equals("Alteon"))
		{
			alteonVsMgmt.enableVServer(adcIndex, new ArrayList<String>(virtualSvrIndices), extraInfo);
		}
		else if (adcType.equals("PAS"))
		{
			pasVsMgmt.enableVServer(adcIndex, new ArrayList<String>(virtualSvrIndices), extraInfo);
		}			
		else if (adcType.equals("PASK"))
		{
			paskVsMgmt.enableVServer(adcIndex, new ArrayList<String>(virtualSvrIndices), extraInfo);
		}			
		else
			throw new OBException("Invalid Type");
	}
	
    public ArrayList<OBDtoAdcVSGroup> enableAllVss(ArrayList<OBDtoAdcVSGroup> vsGroup, SessionDto session) throws OBException, Exception 
    {
        log.debug("enableVss: {}", new Object[]{vsGroup});
        OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
        extraInfo.setExtraMsg1(vsGroup.toString());
        
        Collections.sort(vsGroup, new NoAscCompare());
        ArrayList<String> vss = new ArrayList<String>();
        ArrayList<OBDtoAdcVSGroup> result = new ArrayList<OBDtoAdcVSGroup>();
        ArrayList<Integer> reflashList = new ArrayList<Integer>();
        ArrayList<OBDtoAdcVSGroup> remove = new ArrayList<OBDtoAdcVSGroup>();
        
        int vsSize = vsGroup.size();
        for(int i=0; i < vsSize; i++)
        {
            if(vsGroup.get(i).getAdcType() == OBDefine.ADC_TYPE_F5)
            {
                if(!f5VsMgmt.timeSyncCheck(vsGroup.get(i).getAdcIndex()))
                {
                    result.add(vsGroup.get(i));
                    reflashList.add(vsGroup.get(i).getAdcIndex());
                    remove.add(vsGroup.get(i));
                }
            }
            else if(vsGroup.get(i).getAdcType() == OBDefine.ADC_TYPE_ALTEON)
            {
                if(!alteonVsMgmt.timeSyncCheck(vsGroup.get(i).getAdcIndex()))
                {
                    result.add(vsGroup.get(i));
                    reflashList.add(vsGroup.get(i).getAdcIndex());
                    remove.add(vsGroup.get(i));
                }
            }
        }
        if(reflashList != null && !reflashList.isEmpty())
        {
            HashSet<Integer> hs = new HashSet<Integer>(reflashList);
            reflashList = new ArrayList<Integer>(hs);
            vsGroup.removeAll(remove);
        }
        
        
        if(vsGroup == null || vsGroup.isEmpty())
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
        vsSize = vsGroup.size();
        

        if(vsSize == 1)
        {
            if(vsGroup.get(0).getAdcType() == OBDefine.ADC_TYPE_F5)
            {
                vss.add(vsGroup.get(0).getVsIndex());
                f5VsMgmt.enableVServer(vsGroup.get(0).getAdcIndex(), new ArrayList<String>(vss), extraInfo);
            }
            else if(vsGroup.get(0).getAdcType() == OBDefine.ADC_TYPE_ALTEON)
            {
                vss.add(vsGroup.get(0).getVsIndex());
                alteonVsMgmt.enableVServer(vsGroup.get(0).getAdcIndex(), new ArrayList<String>(vss), extraInfo);
            }
        }
        else
        {
            for(int i=0; i < vsSize; i++)
            {
                vss.add(vsGroup.get(i).getVsIndex());
                if(i+1 < vsSize && vsGroup.get(i).getAdcIndex() != vsGroup.get(i+1).getAdcIndex())
                {
                    if(vsGroup.get(i).getAdcType() == OBDefine.ADC_TYPE_F5)
                    {
                        f5VsMgmt.enableVServer(vsGroup.get(i).getAdcIndex(), new ArrayList<String>(vss), extraInfo);
                    }
                    else if(vsGroup.get(i).getAdcType() == OBDefine.ADC_TYPE_ALTEON)
                    {
                        alteonVsMgmt.enableVServer(vsGroup.get(i).getAdcIndex(), new ArrayList<String>(vss), extraInfo);
                    }
                    vss.clear();
                }
                else if(i+1 == vsSize)
                {
                    if(vsGroup.get(i).getAdcType() == OBDefine.ADC_TYPE_F5)
                    {
                        f5VsMgmt.enableVServer(vsGroup.get(i).getAdcIndex(), new ArrayList<String>(vss), extraInfo);
                    }
                    else if(vsGroup.get(i).getAdcType() == OBDefine.ADC_TYPE_ALTEON)
                    {
                        alteonVsMgmt.enableVServer(vsGroup.get(i).getAdcIndex(), new ArrayList<String>(vss), extraInfo);
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
    
    static class NoAscCompare implements Comparator<OBDtoAdcVSGroup> 
    {
         @Override
         public int compare(OBDtoAdcVSGroup arg0, OBDtoAdcVSGroup arg1)
         {
             return arg0.getAdcIndex() < arg1.getAdcIndex() ? -1 : arg0.getAdcIndex() > arg1.getAdcIndex() ? 1:0;
         }
    }

	public void disableVss(int adcIndex, String adcType, List<String> virtualSvrIndices, SessionDto session) throws OBException, Exception 
	{
		log.debug("disableVss: {}, {}, {}", new Object[]{adcIndex, adcType, virtualSvrIndices});
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(virtualSvrIndices.toString());
		if (adcType.equals("F5"))
		{
			f5VsMgmt.disableVServer(adcIndex, new ArrayList<String>(virtualSvrIndices), extraInfo);
		}
		else if (adcType.equals("Alteon"))
		{
			alteonVsMgmt.disableVServer(adcIndex, new ArrayList<String>(virtualSvrIndices), extraInfo);
		}
		else if (adcType.equals("PAS"))
		{
			pasVsMgmt.disableVServer(adcIndex, new ArrayList<String>(virtualSvrIndices), extraInfo);
		}
		else if (adcType.equals("PASK"))
		{
			paskVsMgmt.disableVServer(adcIndex, new ArrayList<String>(virtualSvrIndices), extraInfo);
		}
		else
			throw new OBException("Invalid Type");
	}
	
    public ArrayList<OBDtoAdcVSGroup> disableAllVss(ArrayList<OBDtoAdcVSGroup> vsGroup, SessionDto session) throws OBException, Exception 
    {
        log.debug("disableVss: {}", new Object[]{vsGroup});
        OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
        extraInfo.setExtraMsg1(vsGroup.toString());
        Collections.sort(vsGroup, new NoAscCompare());
        ArrayList<String> vss = new ArrayList<String>();
        ArrayList<OBDtoAdcVSGroup> result = new ArrayList<OBDtoAdcVSGroup>();
        ArrayList<Integer> reflashList = new ArrayList<Integer>();
        ArrayList<OBDtoAdcVSGroup> remove = new ArrayList<OBDtoAdcVSGroup>();
        
        int vsSize = vsGroup.size();
        for(int i=0; i < vsSize; i++)
        {
            if(vsGroup.get(i).getAdcType() == OBDefine.ADC_TYPE_F5)
            {
                if(!f5VsMgmt.timeSyncCheck(vsGroup.get(i).getAdcIndex()))
                {
                    result.add(vsGroup.get(i));
                    reflashList.add(vsGroup.get(i).getAdcIndex());
                    remove.add(vsGroup.get(i));
                }
            }
            else if(vsGroup.get(i).getAdcType() == OBDefine.ADC_TYPE_ALTEON)
            {
                if(!alteonVsMgmt.timeSyncCheck(vsGroup.get(i).getAdcIndex()))
                {
                    result.add(vsGroup.get(i));
                    reflashList.add(vsGroup.get(i).getAdcIndex());
                    remove.add(vsGroup.get(i));
                }
            }
        }
        if(reflashList != null && !reflashList.isEmpty())
        {
            HashSet<Integer> hs = new HashSet<Integer>(reflashList);
            reflashList = new ArrayList<Integer>(hs);
            vsGroup.removeAll(remove);
        }
        
        
        if(vsGroup == null || vsGroup.isEmpty())
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
        vsSize = vsGroup.size();
        

        if(vsSize == 1)
        {
            if(vsGroup.get(0).getAdcType() == OBDefine.ADC_TYPE_F5)
            {
                vss.add(vsGroup.get(0).getVsIndex());
                f5VsMgmt.disableVServer(vsGroup.get(0).getAdcIndex(), new ArrayList<String>(vss), extraInfo);
            }
            else if(vsGroup.get(0).getAdcType() == OBDefine.ADC_TYPE_ALTEON)
            {
                vss.add(vsGroup.get(0).getVsIndex());
                alteonVsMgmt.disableVServer(vsGroup.get(0).getAdcIndex(), new ArrayList<String>(vss), extraInfo);
            }
        }
        else
        {
            for(int i=0; i < vsSize; i++)
            {
                vss.add(vsGroup.get(i).getVsIndex());
                if(i+1 < vsSize && vsGroup.get(i).getAdcIndex() != vsGroup.get(i+1).getAdcIndex())
                {
                    if(vsGroup.get(i).getAdcType() == OBDefine.ADC_TYPE_F5)
                    {
                        f5VsMgmt.disableVServer(vsGroup.get(i).getAdcIndex(), new ArrayList<String>(vss), extraInfo);
                    }
                    else if(vsGroup.get(i).getAdcType() == OBDefine.ADC_TYPE_ALTEON)
                    {
                        alteonVsMgmt.disableVServer(vsGroup.get(i).getAdcIndex(), new ArrayList<String>(vss), extraInfo);
                    }
                    vss.clear();
                }
                else if(i+1 == vsSize)
                {
                    if(vsGroup.get(i).getAdcType() == OBDefine.ADC_TYPE_F5)
                    {
                        f5VsMgmt.disableVServer(vsGroup.get(i).getAdcIndex(), new ArrayList<String>(vss), extraInfo);
                    }
                    else if(vsGroup.get(i).getAdcType() == OBDefine.ADC_TYPE_ALTEON)
                    {
                        alteonVsMgmt.disableVServer(vsGroup.get(i).getAdcIndex(), new ArrayList<String>(vss), extraInfo);
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

	public void delVss(int adcIndex, String adcType, List<String> virtualSvrIndices, SessionDto session) throws OBException, Exception 
	{
		log.debug("delVss: {}, {}, {}", new Object[]{adcIndex, adcType, virtualSvrIndices});
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(virtualSvrIndices.toString());
		if (adcType.equals("F5"))
		{
			f5VsMgmt.delVServer(adcIndex, new ArrayList<String>(virtualSvrIndices), extraInfo);
		}
		else if (adcType.equals("Alteon"))
		{
			alteonVsMgmt.delVServer(adcIndex, new ArrayList<String>(virtualSvrIndices), extraInfo);
		}
		else if (adcType.equals("PAS"))
		{
			pasVsMgmt.delVServer(adcIndex, new ArrayList<String>(virtualSvrIndices), extraInfo);
		}
		else if (adcType.equals("PASK"))
		{
			paskVsMgmt.delVServer(adcIndex, new ArrayList<String>(virtualSvrIndices), extraInfo);
		}
		else
			throw new OBException("Invalid Type");
	}	
	
	
   public ArrayList<OBDtoAdcVSGroup> delAllVss(ArrayList<OBDtoAdcVSGroup> vsGroup, SessionDto session) throws OBException, Exception 
    {
        log.debug("delVss: {}", new Object[]{vsGroup});
        OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
        extraInfo.setExtraMsg1(vsGroup.toString());
        Collections.sort(vsGroup, new NoAscCompare());
        ArrayList<String> vss = new ArrayList<String>();
        ArrayList<OBDtoAdcVSGroup> result = new ArrayList<OBDtoAdcVSGroup>();
        ArrayList<Integer> reflashList = new ArrayList<Integer>();
        ArrayList<OBDtoAdcVSGroup> remove = new ArrayList<OBDtoAdcVSGroup>();
        
        int vsSize = vsGroup.size();
        for(int i=0; i < vsSize; i++)
        {
            if(vsGroup.get(i).getAdcType() == OBDefine.ADC_TYPE_F5)
            {
                if(!f5VsMgmt.timeSyncCheck(vsGroup.get(i).getAdcIndex()))
                {
                    result.add(vsGroup.get(i));
                    reflashList.add(vsGroup.get(i).getAdcIndex());
                    remove.add(vsGroup.get(i));
                }
            }
            else if(vsGroup.get(i).getAdcType() == OBDefine.ADC_TYPE_ALTEON)
            {
                if(!alteonVsMgmt.timeSyncCheck(vsGroup.get(i).getAdcIndex()))
                {
                    result.add(vsGroup.get(i));
                    reflashList.add(vsGroup.get(i).getAdcIndex());
                    remove.add(vsGroup.get(i));
                }
            }
        }
        if(reflashList != null && !reflashList.isEmpty())
        {
            HashSet<Integer> hs = new HashSet<Integer>(reflashList);
            reflashList = new ArrayList<Integer>(hs);
            vsGroup.removeAll(remove);
        }
        
        
        if(vsGroup == null || vsGroup.isEmpty())
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
        vsSize = vsGroup.size();
        

        if(vsSize == 1)
        {
            if(vsGroup.get(0).getAdcType() == OBDefine.ADC_TYPE_F5)
            {
                vss.add(vsGroup.get(0).getVsIndex());
                f5VsMgmt.delVServer(vsGroup.get(0).getAdcIndex(), new ArrayList<String>(vss), extraInfo);
            }
            else if(vsGroup.get(0).getAdcType() == OBDefine.ADC_TYPE_ALTEON)
            {
                vss.add(vsGroup.get(0).getVsIndex());
                alteonVsMgmt.delVServer(vsGroup.get(0).getAdcIndex(), new ArrayList<String>(vss), extraInfo);
            }
        }
        else
        {
            for(int i=0; i < vsSize; i++)
            {
                vss.add(vsGroup.get(i).getVsIndex());
                if(i+1 < vsSize && vsGroup.get(i).getAdcIndex() != vsGroup.get(i+1).getAdcIndex())
                {
                    if(vsGroup.get(i).getAdcType() == OBDefine.ADC_TYPE_F5)
                    {
                        f5VsMgmt.delVServer(vsGroup.get(i).getAdcIndex(), new ArrayList<String>(vss), extraInfo);
                    }
                    else if(vsGroup.get(i).getAdcType() == OBDefine.ADC_TYPE_ALTEON)
                    {
                        alteonVsMgmt.delVServer(vsGroup.get(i).getAdcIndex(), new ArrayList<String>(vss), extraInfo);
                    }
                    vss.clear();
                }
                else if(i+1 == vsSize)
                {
                    if(vsGroup.get(i).getAdcType() == OBDefine.ADC_TYPE_F5)
                    {
                        f5VsMgmt.delVServer(vsGroup.get(i).getAdcIndex(), new ArrayList<String>(vss), extraInfo);
                    }
                    else if(vsGroup.get(i).getAdcType() == OBDefine.ADC_TYPE_ALTEON)
                    {
                        alteonVsMgmt.delVServer(vsGroup.get(i).getAdcIndex(), new ArrayList<String>(vss), extraInfo);
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
	// Vs name 중복 체크
	public boolean existsVirtualSvrName(int adcIndex, String adcType, String virtualSvrName) throws OBException, Exception 
	{
		log.debug("existsVirtualSvrName: {}, {}, {}", new Object[]{adcIndex, adcType, virtualSvrName});
		boolean exists = false;
		//exists = adcType.equals("F5") ? f5VsMgmt.isExistVirtualServer(adcIndex, virtualSvrName) : alteonVsMgmt.isExistVirtualServer(adcIndex, virtualSvrName);
		if (adcType.equals("F5"))
		{
			exists = f5VsMgmt.isExistVirtualServer(adcIndex, virtualSvrName);
		}
		else if (adcType.equals("Alteon"))
		{
			exists = alteonVsMgmt.isExistVirtualServer(adcIndex, virtualSvrName);
		}
		else if (adcType.equals("PAS"))
		{
			exists = pasVsMgmt.isExistVirtualServer(adcIndex, virtualSvrName);
		}
		else if (adcType.equals("PASK"))
		{
			exists = paskVsMgmt.isExistVirtualServer(adcIndex, virtualSvrName);
		}		
		log.debug("exists: {}", exists);
		return exists;
	}
	
	// Vs 중복 체크
	public boolean existsVirtualSvr(int adcIndex, String adcType, String virtualSvrName, String ip, Integer port, String alteonId) throws OBException, Exception 
	{
		log.debug("existsVirtualServer: {}, {}, {}, {}, {}", new Object[]{adcIndex, adcType, virtualSvrName, ip, port, alteonId});
		boolean exists = false;
		/*exists = adcType.equals("F5") ? f5VsMgmt.isAvailableVirtualServerF5(adcIndex, virtualSvrName, ip, portOrAlteonId) : alteonVsMgmt.isAvailableVirtualServerAlteon(adcIndex, portOrAlteonId, ip);*/
		if (adcType.equals("F5"))
		{
			exists = f5VsMgmt.isAvailableVirtualServerF5(adcIndex, virtualSvrName, ip, port, null);
		}
		else if (adcType.equals("Alteon"))
		{
			exists = alteonVsMgmt.isAvailableVirtualServerAlteon(adcIndex, null, alteonId, ip);
		}
		else if (adcType.equals("PAS"))
		{
			exists = pasVsMgmt.isExistVirtualServerPAS(adcIndex, virtualSvrName);
		}
		else if (adcType.equals("PASK"))
		{
			exists = paskVsMgmt.isExistVirtualServerPASK(adcIndex, virtualSvrName);
		}		
		log.debug("exists: {}", exists);
		return exists;
	}	
	
	// Vs 중복 체크
	public boolean isExistVirtualServer(int adcIndex, String adcType, ArrayList<String> vsList) throws OBException, Exception 
	{
		boolean exists = false;
		/*exists = adcType.equals("F5") ? f5VsMgmt.isAvailableVirtualServerF5(adcIndex, virtualSvrName, ip, portOrAlteonId) : alteonVsMgmt.isAvailableVirtualServerAlteon(adcIndex, portOrAlteonId, ip);*/
		if (adcType.equals("F5"))
		{
			exists = f5VsMgmt.isExistVirtualServer(adcIndex, vsList);
		}
		else if (adcType.equals("Alteon"))
		{
			exists = alteonVsMgmt.isExistVirtualServer(adcIndex, vsList);
		}
		else if (adcType.equals("PAS"))
		{
			exists = pasVsMgmt.isExistVirtualServer(adcIndex, vsList);
		}
		else if (adcType.equals("PASK"))
		{
			exists = paskVsMgmt.isExistVirtualServer(adcIndex, vsList);
		}		
		return exists;
	}	
	
	public boolean isExistVSIPAddress(int adcIndex, String adcType, String ipAddress) throws OBException, Exception 
	{
		boolean exists = false;
		/*exists = adcType.equals("F5") ? f5VsMgmt.isAvailableVirtualServerF5(adcIndex, virtualSvrName, ip, portOrAlteonId) : alteonVsMgmt.isAvailableVirtualServerAlteon(adcIndex, portOrAlteonId, ip);*/
		if (adcType.equals("F5"))
		{
			exists = f5VsMgmt.isExistVSIPAddress(adcIndex, ipAddress);
		}
		else if (adcType.equals("Alteon"))
		{
			exists = alteonVsMgmt.isExistVSIPAddress(adcIndex, ipAddress);
		}
		else if (adcType.equals("PAS"))
		{
			exists = pasVsMgmt.isExistVSIPAddress(adcIndex, ipAddress);
		}
		else if (adcType.equals("PASK"))
		{
			exists = paskVsMgmt.isExistVSIPAddress(adcIndex, ipAddress);
		}		
		return exists;
	}
	
	public String getValidVSIndexAlteon(Integer adcIndex, String vsIndex) throws OBException, Exception 
	{
		if(vsIndex == null)
			return null;
		return alteonVsMgmt.getValidVSIndex(adcIndex, vsIndex);
	}	
	
	public String getValidPoolIndexAlteon(Integer adcIndex, String poolIndex) throws OBException, Exception 
	{
		if(poolIndex == null)
			return null;
		return alteonVsMgmt.getValidPoolIndex(adcIndex, poolIndex);
	}	
	
	public ArrayList<String> getPoolIndexListAlteon(Integer adcIndex) throws OBException, Exception 
	{
		return alteonVsMgmt.getPoolIndexListAlteon(adcIndex);
	}	
	
	public void addAlteonVs(AlteonVsAddDto vsAdd, SessionDto session) throws OBException, Exception 
	{
		log.debug("AlteonVsAddDto: {}", vsAdd);
		OBDtoAdcVServerAlteon virtualSvrFromSvc = AlteonVsAddDto.toOBDtoAdcVServer(vsAdd);
				log.debug("{}", virtualSvrFromSvc);
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(vsAdd.getName());
		alteonVsMgmt.addVServerAlteon(virtualSvrFromSvc, extraInfo);
		log.debug("-- alteonVsMgmt.addVServerAlteon finished.");
	}

	private String convertPoolIndex(Integer adcIndex, String poolIndex)
	{
		if(poolIndex==null)
			return "";
		String [] items = poolIndex.split("_", 2);
		if(items.length!=2)
			return poolIndex;
		String newVal = adcIndex + "_" + items[1];
		return newVal;
	}
	
	// slave 장비에 VS를 추가하는 과정. 설정의 내용은 동일하지만 adcIndex만 다르다. 
	// 입력받고 있는 vsAdd 정보는 master 장비의 설정 내용.
	public void addAlteonVsPeer(Integer peerIndex, AlteonVsAddDto vsAdd, SessionDto session) throws OBException, Exception 
	{
		log.debug("AlteonVsAddDto: {}", vsAdd);
		OBDtoAdcVServerAlteon virtualSvrFromSvc = AlteonVsAddDto.toOBDtoAdcVServer(vsAdd);
				log.debug("{}", virtualSvrFromSvc);
				
		// adcIndex 변경..
		virtualSvrFromSvc.setAdcIndex(peerIndex);
		
		// service 목록에서 adcIndex 정보 변경.
		ArrayList<OBDtoAdcVService> changedList = new ArrayList<OBDtoAdcVService>();
		ArrayList<OBDtoAdcVService> vserviceList = virtualSvrFromSvc.getVserviceList();
		for(OBDtoAdcVService vsrvObj:vserviceList)
		{
			OBDtoAdcPoolAlteon pool = vsrvObj.getPool();
			if(pool==null)
				continue;
			pool.setIndex(convertPoolIndex(peerIndex, pool.getIndex()));
			changedList.add(vsrvObj);
		}
		virtualSvrFromSvc.setVserviceList(changedList);
		
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(vsAdd.getName());
		alteonVsMgmt.addVServerAlteon(virtualSvrFromSvc, extraInfo);
		log.debug("-- alteonVsMgmt.addVServerAlteon finished.");
	}
	
	
	public void modifyAlteonVs(AlteonVsAddDto vsAdd, SessionDto session) throws OBException, Exception 
	{
		log.debug("modifyAlteonVs: {}", vsAdd);
		OBDtoAdcVServerAlteon virtualSvFromSvc = AlteonVsAddDto.toOBDtoAdcVServer(vsAdd);
		log.debug("{}", virtualSvFromSvc);
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(vsAdd.getName());
		alteonVsMgmt.setVServerAlteon(virtualSvFromSvc, extraInfo);
		log.debug("-- alteonVsMgmt.setVServerAlteon finished.");
	}
	
	// slave 장비에 VS를 추가하는 과정. 설정의 내용은 동일하지만 adcIndex만 다르다. 
	// 입력받고 있는 vsAdd 정보는 master 장비의 설정 내용.
	public void modifyAlteonVsPeer(Integer peerIndex, AlteonVsAddDto vsAdd, SessionDto session) throws OBException, Exception 
	{
		log.debug("modifyAlteonVs: {}", vsAdd);
		OBDtoAdcVServerAlteon virtualSvFromSvc = AlteonVsAddDto.toOBDtoAdcVServer(vsAdd);
		log.debug("{}", virtualSvFromSvc);
		
		// index 변경
		virtualSvFromSvc.setIndex(convertPoolIndex(peerIndex, virtualSvFromSvc.getIndex()));
		
		// adcIndex 변경..
		virtualSvFromSvc.setAdcIndex(peerIndex);
		
		// service 목록에서 adcIndex 정보 변경
		ArrayList<OBDtoAdcVService> changedList = new ArrayList<OBDtoAdcVService>();
		ArrayList<OBDtoAdcVService> vserviceList = virtualSvFromSvc.getVserviceList();
		for(OBDtoAdcVService vsrvObj:vserviceList)
		{
			OBDtoAdcPoolAlteon pool = vsrvObj.getPool();
			if(pool==null)
				continue;
			pool.setIndex(convertPoolIndex(peerIndex, pool.getIndex()));
			changedList.add(vsrvObj);
		}
		virtualSvFromSvc.setVserviceList(changedList);
				
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(vsAdd.getName());
		alteonVsMgmt.setVServerAlteon(virtualSvFromSvc, extraInfo);
		log.debug("-- alteonVsMgmt.setVServerAlteon finished.");
	}

	public Integer getPeerIndex(Integer adcIndex) throws OBException, Exception 
	{
		try
		{
			Integer peerIndex = new OBAdcManagementImpl().getActivePairIndex(adcIndex);
			return peerIndex;
		}
		catch(OBException e)
		{
			throw e;
		}
	}
	
	public void syncConifgF5(AdcDto adc, SessionDto session) throws OBException, Exception 
	{
//		log.debug("syncConifgF5: {}", adcIndex);
		log.debug("syncConifgF5: {}", adc);
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(adc.getName());		
		try
		{		
			f5VsMgmt.syncConifgF5(adc.getIndex(), extraInfo);			
		}
		catch(OBException e)
		{
			throw e;
		}		
	}

	public void addF5Vs(F5VsAddDto vsAdd, SessionDto session) throws OBException, Exception 
	{
		log.debug("addF5Vs: {}", vsAdd);
		OBDtoAdcVServerF5 virtualSvrFromSvc = F5VsAddDto.toOBDtoAdcVServer(vsAdd);
		log.debug("{}", virtualSvrFromSvc);
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(vsAdd.getName());
		f5VsMgmt.addVServerF5(virtualSvrFromSvc, extraInfo);
		log.debug("-- f5VsMgmt.addVServerF5 finished.");
	}
	
	public void refreshF5Vs(F5VsAddDto vsAdd, SessionDto session) throws OBException, Exception 
    {
        log.debug("refreshF5Vs: {}", vsAdd);
        
        OBDtoAdcVServerF5 virtualSvrFromSvc = F5VsAddDto.toOBDtoAdcVServer(vsAdd);
        log.debug("{}", virtualSvrFromSvc);
        OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
        extraInfo.setExtraMsg1(vsAdd.getName());
        f5VsMgmt.relashVServerF5(virtualSvrFromSvc, extraInfo);
        log.debug("-- f5VsMgmt.refreshF5Vs finished.");
    }
	
	public void modifyF5Vs(F5VsAddDto vsAdd, SessionDto session) throws OBException, Exception 
	{
		log.debug("modifyF5Vs: {}", vsAdd);
		
//		if (vsAdd.getVlanTunnel().getStatus() == 0)
//		{
//			ArrayList<String> vlanList = new ArrayList<String>();
//			List<OBDtoAdcVlan> vlanName = getVlansAll(vsAdd.getAdcIndex());
//			for (OBDtoAdcVlan obDtoAdcVlan : vlanName) {
//				vlanList.add(obDtoAdcVlan.getVlanName());
//			}
//			vsAdd.getVlanTunnel().setVlanName(vlanList);
//		}
		
		OBDtoAdcVServerF5 virtualSvrFromSvc = F5VsAddDto.toOBDtoAdcVServer(vsAdd);
		log.debug("{}", virtualSvrFromSvc);
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(vsAdd.getName());
		f5VsMgmt.setVServerF5(virtualSvrFromSvc, extraInfo);
		log.debug("-- f5VsMgmt.setVServerF5 finished.");
	}
	
	public void addPasVs(PasVsAddDto vsAdd, SessionDto session) throws OBException, Exception 
	{
		log.debug("addPasVs: {}", vsAdd);
		OBDtoAdcVServerPAS virtualSvrFromSvc = PasVsAddDto.toOBDtoAdcVServer(vsAdd);
		log.debug("{}", virtualSvrFromSvc);
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(vsAdd.getName());
		pasVsMgmt.addVServerPAS(virtualSvrFromSvc, extraInfo);
		log.debug("-- pasVsMgmt.addVServerPAS finished.");
	}

	// slave 장비에 VS를 추가하는 과정. 설정의 내용은 동일하지만 adcIndex만 다르다. 
	// 입력받고 있는 vsAdd 정보는 master 장비의 설정 내용.
	public void addPasVsPeer(Integer peerIndex, PasVsAddDto vsAdd, SessionDto session) throws OBException, Exception 
	{//TODO
//		log.debug("addPasVs: {}", vsAdd);
//		OBDtoAdcVServerPAS virtualSvrFromSvc = PasVsAddDto.toOBDtoAdcVServer(vsAdd);
//		log.debug("{}", virtualSvrFromSvc);
//		// adcIndex 변경
//		virtualSvrFromSvc.setAdcIndex(peerIndex);
//				
//		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
//		extraInfo.setExtraMsg1(vsAdd.getName());
//		pasVsMgmt.addVServerPAS(virtualSvrFromSvc, extraInfo);
//		log.debug("-- pasVsMgmt.addVServerPAS finished.");
	}
	
	public void modifyPASVs(PasVsAddDto vsAdd, SessionDto session) throws OBException, Exception 
	{
		log.debug("modifyPASVs: {}", vsAdd);
		OBDtoAdcVServerPAS virtualSvrFromSvc = PasVsAddDto.toOBDtoAdcVServer(vsAdd);
		log.debug("{}", virtualSvrFromSvc);
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(vsAdd.getName());
		pasVsMgmt.setVServerPAS(virtualSvrFromSvc, extraInfo);
		log.debug("-- pasVsMgmt.setVServerPAS finished.");
	}
	
	public void modifyPASVsPeer(Integer peerIndex, PasVsAddDto vsAdd, SessionDto session) throws OBException, Exception 
	{
//		log.debug("modifyPASVs: {}", vsAdd);
//		OBDtoAdcVServerPAS virtualSvrFromSvc = PasVsAddDto.toOBDtoAdcVServer(vsAdd);
//		log.debug("{}", virtualSvrFromSvc);
//		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
//		extraInfo.setExtraMsg1(vsAdd.getName());
//		pasVsMgmt.setVServerPAS(virtualSvrFromSvc, extraInfo);
//		log.debug("-- pasVsMgmt.setVServerPAS finished.");
	}
	
	public void addPASKVs(PaskVsAddDto vsAdd, SessionDto session) throws OBException, Exception 
	{
		log.debug("addPaskVs: {}", vsAdd);
		OBDtoAdcVServerPASK virtualSvrFromSvc = PaskVsAddDto.toOBDtoAdcVServer(vsAdd);
		log.debug("{}", virtualSvrFromSvc);
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(vsAdd.getName());
		paskVsMgmt.addVServerPASK(virtualSvrFromSvc, extraInfo);
		log.debug("-- paskVsMgmt.addVServerPASK finished.");
	}

	public void addPASKVsPeer(Integer peerIndex, PaskVsAddDto vsAdd, SessionDto session) throws OBException, Exception 
	{//TODO
//		log.debug("addPaskVs: {}", vsAdd);
//		OBDtoAdcVServerPASK virtualSvrFromSvc = PaskVsAddDto.toOBDtoAdcVServer(vsAdd);
//		log.debug("{}", virtualSvrFromSvc);
//		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
//		extraInfo.setExtraMsg1(vsAdd.getName());
//		paskVsMgmt.addVServerPASK(virtualSvrFromSvc, extraInfo);
//		log.debug("-- paskVsMgmt.addVServerPASK finished.");
	}
	
	public void modifyPASKVs(PaskVsAddDto vsAdd, SessionDto session) throws OBException, Exception 
	{
		log.debug("modifyPASKVs: {}", vsAdd);
		OBDtoAdcVServerPASK virtualSvrFromSvc = PaskVsAddDto.toOBDtoAdcVServer(vsAdd);
		log.debug("{}", virtualSvrFromSvc);
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(vsAdd.getName());
		paskVsMgmt.setVServerPASK(virtualSvrFromSvc, extraInfo);
		log.debug("-- pasVsMgmt.setVServerPASK finished.");
	}
	
	public void modifyPASKVsPeer(Integer peerIndex, PaskVsAddDto vsAdd, SessionDto session) throws OBException, Exception 
	{//TODO
//		log.debug("modifyPASKVs: {}", vsAdd);
//		OBDtoAdcVServerPASK virtualSvrFromSvc = PaskVsAddDto.toOBDtoAdcVServer(vsAdd);
//		log.debug("{}", virtualSvrFromSvc);
//		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
//		extraInfo.setExtraMsg1(vsAdd.getName());
//		paskVsMgmt.setVServerPASK(virtualSvrFromSvc, extraInfo);
//		log.debug("-- pasVsMgmt.setVServerPASK finished.");
	}
	
	public String retrieveAlteonVsPortsString(int adcIndex, String index) throws OBException, Exception 
	{
		log.debug("retrieveAlteonVsPortsString: {}, {}", adcIndex, index);
		OBDtoAdcVServerAlteon virtualSvrAlteon = alteonVsMgmt.getVServerAlteonInfo(adcIndex, index);
		log.debug("{}", virtualSvrAlteon);
		VirtualSvrAlteonDto virtualSvr = VirtualSvrAlteonDto.toVirtualSvrDto(virtualSvrAlteon);
		log.debug("{}", virtualSvr);
		List<VirtualSvcDto> vSvcs = virtualSvr.getVirtualSvcs();
		log.debug("{}", vSvcs);
		if (virtualSvr == null || CollectionUtils.isEmpty(vSvcs))
			return "";
		
		String ports = String.valueOf(vSvcs.get(0).getSvcPort());
		for (int i=1; i < vSvcs.size(); i++)
			ports += ", " + String.valueOf(vSvcs.get(i).getSvcPort());
		
		return ports;
	}
	
	public DtoVlanTunnelFilter getRegisteredVlans(int adcIndex, String vsIndex) throws OBException, Exception 
    {        
        DtoVlanTunnelFilter registeredVlans = new DtoVlanTunnelFilter();
        registeredVlans = f5VsMgmt.getF5VlanFilterList(adcIndex, vsIndex);
        
        log.debug("{}", registeredVlans);
        return registeredVlans;
    }
    
    public List<OBDtoAdcVlan> getVlans(int adcIndex, String vsIndex) throws OBException, Exception 
    {
        log.debug("adcIndex: {}", adcIndex);
        List<OBDtoAdcVlan> vlans = f5VsMgmt.getF5VlanList(adcIndex, vsIndex);
        return vlans;
    }
    
    public List<OBDtoAdcVlan> getVlansAll(int adcIndex) throws OBException, Exception 
    {
        log.debug("adcIndex: {}", adcIndex);
        List<OBDtoAdcVlan> vlansAllList = f5VsMgmt.getF5VlanListAll(adcIndex);
        return vlansAllList;
    }
}