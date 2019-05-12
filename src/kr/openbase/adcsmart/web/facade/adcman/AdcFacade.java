package kr.openbase.adcsmart.web.facade.adcman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import kr.openbase.adcsmart.service.OBAccount;
import kr.openbase.adcsmart.service.OBAdcManagement;
import kr.openbase.adcsmart.service.OBMonitoringFlb;
import kr.openbase.adcsmart.service.dto.OBAdcCfgInfo;
import kr.openbase.adcsmart.service.dto.OBAdcCheckResult;
import kr.openbase.adcsmart.service.dto.OBAdcConfigInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcGroup;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcRSInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVSInfo;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoLastAdcCheckTime;
import kr.openbase.adcsmart.service.dto.OBDtoSLBUpdateStatus;
import kr.openbase.adcsmart.service.dto.OBDtoVrrpInfo;
import kr.openbase.adcsmart.service.dto.flb.OBDtoFlbGroupMonitorInfo;
import kr.openbase.adcsmart.service.impl.OBAccountImpl;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBMonitoringFlbAlteonImpl;
import kr.openbase.adcsmart.service.utility.OBCipherAES;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.AccountDto;
import kr.openbase.adcsmart.web.facade.dto.AdcAddDto;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.AdcGroupDto;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;
import kr.openbase.adcsmart.web.facade.dto.VrrpInfoDto;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/*
 * 코딩 가이드:
 * 1. public 함수의 Exception은 무조건 OBException, Exception 두종류로  throw 한다.
 * 2. 내부에서 exception의 try-catch는 가급적이면 하지 않는다. 필요할 경우에만 추가한다.
 * 3. AdcFacade.java 파일을 참조한다.
 *
 */

@Component
public class AdcFacade 
{
	private static transient Logger log = LoggerFactory.getLogger(AdcFacade.class);
	
	private OBAdcManagement adcSvc;
	private OBAccount accountSvc;
//	private OBAdcSystemInfo info;
	private OBMonitoringFlb flbMonAlteonSvc;

	public AdcFacade() 
	{
		adcSvc = new OBAdcManagementImpl();
		accountSvc = new OBAccountImpl();
		flbMonAlteonSvc = new OBMonitoringFlbAlteonImpl();
	}

	public SortedSet<AdcGroupDto> getAdcGroups() throws OBException, Exception 
	{
		SortedSet<AdcGroupDto> adcGroups = new TreeSet<AdcGroupDto>();
		for (OBDtoAdcGroup adcGroupFromSvc : adcSvc.getAdcGroupList(null)) 
		{
			AdcGroupDto adcGroup = new AdcGroupDto();
			adcGroup.setIndex(adcGroupFromSvc.getIndex());
			adcGroup.setName(adcGroupFromSvc.getName());
			adcGroup.setDescription(adcGroupFromSvc.getDescription());

			adcGroups.add(adcGroup);
		}

		return adcGroups;
	}
	
	public List<AdcGroupDto> getGroupsByGroupIndex(Integer accountIndex, Integer groupIndex, String searchKey, Integer orderType, Integer orderDir, Integer adcListPageOption) throws OBException, Exception 
	{
		log.debug("getGroupsByGroupIndex: {}", accountIndex);
		ArrayList<AdcGroupDto> groups = new ArrayList<AdcGroupDto>();
		for (OBDtoAdcGroup groupFromSvc : adcSvc.getAdcGroupListByAccount(accountIndex, groupIndex, searchKey, orderType, orderDir, adcListPageOption)) 
		{
			log.debug("{}", groupFromSvc);
			AdcGroupDto group = AdcGroupDto.toAdcGroupDto(groupFromSvc);
			groups.add(group);
		}
		return groups;
	}

	public List<AdcDto> getAdcsByAccountIndex(Integer accountIndex) throws OBException, Exception 
	{
		log.debug("getAdcsByAccountIndex: {}", accountIndex);
		ArrayList<AdcDto> adcs = new ArrayList<AdcDto>();
		for (OBDtoAdcInfo adcFromSvc : adcSvc.getAdcInfoList(accountIndex)) 
		{
			log.debug("{}", adcFromSvc);
			AdcDto adc = AdcDto.toAdcDto(adcFromSvc);
			adcs.add(adc);
		}

		return adcs;
	}

	public List<AdcDto> getAllAdcs(Integer accountIndex, String searchKey) throws Exception 
	{
		log.debug("getAllAdcs: {}", accountIndex);
		ArrayList<AdcDto> adcs = new ArrayList<AdcDto>();
		for (OBDtoAdcInfo adcFromSvc : adcSvc.searchAdcInfoList(accountIndex, StringUtils.isEmpty(searchKey) ? null : searchKey)) 
		{
			AdcDto adc = AdcDto.toAdcDto(adcFromSvc);
			adcs.add(adc);
		}

		return adcs;
	}

	public List<OBDtoAdcVSInfo> getAllAdcVSs(Integer accountIndex, String searchKey) throws Exception 
	{
		return accountSvc.getAdcVSInfoList(accountIndex, StringUtils.isEmpty(searchKey) ? null : searchKey); 
	}
	
	public List<OBDtoAdcRSInfo> getAllAdcRSs(Integer accountIndex, String searchKey) throws Exception
    {
        return accountSvc.getAdcRSInfoList(accountIndex, StringUtils.isEmpty(searchKey) ? null : searchKey);
    }
	
	public List<AdcDto> getAvailableAdcs(Integer accountIndex, String searchKey) throws OBException, Exception 
	{
		log.debug("getAvailableAdcs: {}", accountIndex);
		ArrayList<AdcDto> adcs = new ArrayList<AdcDto>();
		ArrayList<OBDtoAdcInfo> adcsFromSvc = accountSvc.searchAdcInfoNSList(accountIndex, StringUtils.isEmpty(searchKey) ? null : searchKey);
		log.debug("{}", adcsFromSvc);
		for (OBDtoAdcInfo adcFromSvc : adcsFromSvc) 
		{
			AdcDto adc = AdcDto.toAdcDto(adcFromSvc);
			adcs.add(adc);
		}

		return adcs;
	}

	public List<OBDtoAdcVSInfo> getAvailableAdcVSs(Integer accountIndex, String searchKey) throws OBException, Exception 
	{
		ArrayList<OBDtoAdcVSInfo> adcsFromSvc = accountSvc.getAdcVSInfoNSList(accountIndex, searchKey);

		return adcsFromSvc;
	}
	
	public List<OBDtoAdcRSInfo> getAvailableAdcRSs(Integer accountIndex, String searchKey) throws OBException ,Exception
    {
        ArrayList<OBDtoAdcRSInfo> adcsFromSvc = accountSvc.getAdcRSInfoNSList(accountIndex, searchKey);
        return adcsFromSvc;
    }
	
	public Map<String, AdcDto> getRegisteredAdcMap(Integer accountIndex) throws OBException, Exception 
	{
		log.debug("getRegisteredAdcMap: {}", accountIndex);
		Map<String, AdcDto> adcMap = new HashMap<String, AdcDto>();
		for (OBDtoAdcInfo adcFromSvc : adcSvc.getAdcInfoList(accountIndex)) 
		{
			AdcDto adc = AdcDto.toAdcDto(adcFromSvc);
			adcMap.put(String.valueOf(adc.getIndex()), adc);
		}

		return adcMap;
	}
	
	public Map<String, OBDtoAdcVSInfo> getRegisteredAdcVSMap(Integer accountIndex) throws OBException, Exception 
	{
		log.debug("getRegisteredAdcMap: {}", accountIndex);
		Map<String, OBDtoAdcVSInfo> adcMap = new HashMap<String, OBDtoAdcVSInfo>();
		for (OBDtoAdcVSInfo obj : accountSvc.getAdcVSInfoList(accountIndex, null)) 
		{
			adcMap.put(String.valueOf(obj.getAdcIndex()), obj);
		}

		return adcMap;
	}

	public Map<String, OBDtoAdcRSInfo> getRegisteredAdcRSMap(Integer accountIndex) throws OBException, Exception
    {
        Map<String, OBDtoAdcRSInfo> adcRsMap = new HashMap<String, OBDtoAdcRSInfo>();
        for (OBDtoAdcRSInfo obj : accountSvc.getAdcRSInfoList(accountIndex, null)) 
        {
            adcRsMap.put(String.valueOf(obj.getAdcIndex()), obj);           
        }
        return adcRsMap;
    }
	
	public List<AdcDto> searchAdcsByAccountIndex(Integer accountIndex, String searchKey) throws OBException, Exception 
	{
		log.debug("searchAdcsByAccountIndex: {}, {}", accountIndex, searchKey);
		ArrayList<AdcDto> adcs = new ArrayList<AdcDto>();
		for (OBDtoAdcInfo adcFromSvc : adcSvc.searchAdcInfoList(accountIndex, StringUtils.isEmpty(searchKey) ? null : searchKey)) 
		{
			AdcDto adc = AdcDto.toAdcDto(adcFromSvc);
			adcs.add(adc);
		}

		return adcs;
	}

	public Map<String, AdcGroupDto> getAdcGroupsMap() throws OBException, Exception 
	{
		Map<String, AdcGroupDto> adcGroupsMap = new LinkedHashMap<String, AdcGroupDto>();
		for (AdcGroupDto adcGroupDto : getAdcGroups())
			adcGroupsMap.put(String.valueOf(adcGroupDto.getIndex()), adcGroupDto);

		return adcGroupsMap;
	}

	public Map<String, AdcGroupDto> getAdcGroupInfoMap(Integer accntIndex, Integer groupIndex, String searchKey, Integer orderType, Integer orderDir, Integer adcListPageOption) throws OBException, Exception 
	{
		Map<String, AdcGroupDto> retVal = new LinkedHashMap<String, AdcGroupDto>();
		List<AdcGroupDto> groupList = getGroupsByGroupIndex(accntIndex, groupIndex, searchKey, orderType, orderDir, adcListPageOption);
		for (AdcGroupDto group : groupList)
		{
			retVal.put(String.valueOf(group.getIndex()), group);
		}
		return retVal;
	}
	
	public ArrayList<String> getAdcNameList() throws OBException, Exception 
	{
		return adcSvc.getAdcNameList();
	}
	
	//ADC 설정 정보 (DB)
	public OBAdcCfgInfo loadAdcConfigInfo(Integer adcIndex) throws OBException, Exception
	{
		return adcSvc.getAdcConfigInfo(adcIndex);
	}
	
	//ADC 설정 정보 점검 
	public OBAdcCfgInfo checkAdcConfig(Integer adcIndex) throws OBException, Exception
	{
		OBAdcCfgInfo configCheck = new OBAdcCfgInfo();
		configCheck = adcSvc.getAllConfigInfo(adcIndex);
		return configCheck;
	}

	public void configFaill(int adcIndex,String configType) throws OBException, Exception
	{
		adcSvc.configFaill(adcIndex, configType);	
	}
	
	public Map<String, List<AdcDto>> getAdcGroupToAdcsMap(Integer accountIndex, String searchKey) throws OBException, Exception 
	{
		log.debug("getAdcGroupToAdcsMap: {}, {}", accountIndex, searchKey);
		Map<String, List<AdcDto>> adcGroupToAdcsMap = new HashMap<String, List<AdcDto>>();
		List<AdcDto> adcs = StringUtils.isEmpty(searchKey) ? getAdcsByAccountIndex(accountIndex) : searchAdcsByAccountIndex(accountIndex,
				searchKey);
		for (AdcDto adc : adcs) 
		{
			if (!adcGroupToAdcsMap.containsKey(String.valueOf(adc.getGroupIndex())))
				adcGroupToAdcsMap.put(String.valueOf(adc.getGroupIndex()), new ArrayList<AdcDto>());

			List<AdcDto> adcsInGroup = adcGroupToAdcsMap.get(String.valueOf(adc.getGroupIndex()));
			adcsInGroup.add(adc);
		}

		return adcGroupToAdcsMap;
	}

	public AdcDto getAdc(int index) 
	{
		log.debug("getAdc: {}", index);
		AdcDto adc = null;
		try 
		{
			adc = AdcDto.toAdcDto(adcSvc.getAdcInfo(index));
		} 
		catch (Exception e) 
		{
			
		}

		return adc;
	}
	
	public VrrpInfoDto getVrrp(int adcIndex) 
	{
		VrrpInfoDto vrrpInfo = null;
		try 
		{
			vrrpInfo = VrrpInfoDto.toVrrpInfoDto(adcSvc.getVrrpInfo(adcIndex));
		} 
		catch (Exception e) 
		{
			
		}
		return vrrpInfo;
	}	
	
	public List<VrrpInfoDto> getVrrpInfo(int adcIndex) 
	{
		List<VrrpInfoDto> vrrpInfos = new ArrayList<VrrpInfoDto>();
		try 
		{
			OBDtoVrrpInfo vrrpInfoFromSvc = adcSvc.getVrrpInfo(adcIndex);
			if (null != vrrpInfoFromSvc) vrrpInfos.add(toVrrpInfoDto(vrrpInfoFromSvc));
		} 
		catch (Exception e) 
		{
			
		}
		return vrrpInfos;
	}
	
	private VrrpInfoDto toVrrpInfoDto(OBDtoVrrpInfo vrrpInfoFromSvc) 
	{
		VrrpInfoDto vrrpInfo = new VrrpInfoDto();
		try 
		{
			vrrpInfo.setAdcIndex(vrrpInfoFromSvc.getAdcIndex());
			vrrpInfo.setSyncStatus(vrrpInfoFromSvc.getSyncStatus());
			vrrpInfo.setPeerIP(vrrpInfoFromSvc.getPeerIP());
			vrrpInfo.setPriority(vrrpInfoFromSvc.getPriority());
			vrrpInfo.setTrackVrs(vrrpInfoFromSvc.getTrackVrs());
			vrrpInfo.setTrackPorts(vrrpInfoFromSvc.getTrackPorts());
			vrrpInfo.setTrackInt(vrrpInfoFromSvc.getTrackInt());
			vrrpInfo.setTrackL4pts(vrrpInfoFromSvc.getTrackL4pts());
			vrrpInfo.setTrackReals(vrrpInfoFromSvc.getTrackReals());
			vrrpInfo.setTrackHsrp(vrrpInfoFromSvc.getTrackHsrp());
			vrrpInfo.setTrackHsrv(vrrpInfoFromSvc.getTrackHsrv());
			vrrpInfo.setSharing(vrrpInfoFromSvc.getSharing());	
		}
		catch (Exception e) 
		{
			
		}
		return vrrpInfo;
	}

	public List<AccountDto> getAccounts() throws OBException, Exception 
	{
		List<AccountDto> accounts = null;
		accounts = AccountDto.toAccountDto(adcSvc.getTrivialAccountList(null, null));
		log.debug("{}", accounts);
		return accounts;
	}

	public List<AccountDto> getRegisteredAccounts(int adcIndex) throws OBException, Exception 
	{
		log.debug("adcIndex: {}", adcIndex);
		List<AccountDto> registeredAccounts = new ArrayList<AccountDto>();
		OBDtoAdcInfo adc = adcSvc.getAdcInfo(adcIndex);
		
		if (adc == null)
			return registeredAccounts;
		
		List<Integer> accountIndices = adc.getAccountIndexList();
		if (accountIndices == null)
			return registeredAccounts;
		
		for (int accountIndex : accountIndices)
			registeredAccounts.add(AccountDto.toAccountDto(accountSvc.getAccountList(accountIndex).get(0)));
		return registeredAccounts;
	}

	public List<AccountDto> getAvailableAccounts(int adcIndex) throws OBException, Exception 
	{
		return AccountDto.toAccountDto(adcSvc.getTrivialAccountList(adcIndex, null));
	}

	public boolean existsAdc(String nameOrIp) throws OBException, Exception 
	{
		log.debug("existsAdc: {}", nameOrIp);
		boolean exists = false;
		exists = adcSvc.isExistAdc(nameOrIp);
		return exists;
	}

	public boolean existsAdcGroup(String groupName) throws OBException, Exception 
	{
		log.debug("existsAdcGroup: {}", groupName);
		boolean exists = false;
		exists = adcSvc.isExistGroup(groupName);
		return exists;
	}

	public void delAdcs(List<Integer> adcIndices, SessionDto session) throws OBException, Exception 
	{
		log.debug("delAdcs: {}", adcIndices);
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(adcIndices.toString());
		adcSvc.delAdcInfoList(new ArrayList<Integer>(adcIndices), extraInfo);
	}

	public Integer addAdc(AdcAddDto adcAdd, SessionDto session, int adcStatus, boolean isReachabled, int isReachable) throws OBException, Exception
	{		
		Integer adcIndex = 0;
		OBDtoAdcInfo adcInfo = AdcAddDto.toOBDtoAdcInfo(adcAdd);
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(adcAdd.getAdc().getName());
		adcInfo.setStatus(adcStatus);//gstest
		adcInfo=passwordEncrypt(adcInfo); //password Enctypt

		adcIndex = adcSvc.addAdcInfo(adcInfo, extraInfo, isReachabled, isReachable);
		
		return adcIndex;
	}

//	public boolean downloadSlbConfig(Integer adcIndex) throws OBException, Exception
//	{
//		try
//		{
//		    return adcSvc.downloadSlbConfig(adcIndex);
//		}
//		catch(OBException e)
//		{
//			return false;
//		}
//	}
	
	public OBDtoSLBUpdateStatus downloadSlbConfig(Integer adcIndex) throws OBException, Exception
    {
	    OBDtoSLBUpdateStatus retVal = new OBDtoSLBUpdateStatus();      
        retVal = adcSvc.downloadSlbConfig(adcIndex);
        return retVal;        
    }
	
	public void modifyAdc(AdcAddDto adcAdd, SessionDto session, int isReachable) throws OBException, Exception
	{	
		OBDtoAdcInfo adcInfo = AdcAddDto.toOBDtoAdcInfo(adcAdd);
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(adcAdd.getAdc().getName());

		adcSvc.setAdcInfo(adcInfo, extraInfo, isReachable);
//		adcSvc.downloadSlbConfig(adcAdd.getAdc().getIndex());
		return;
	}

	public void addAdcGroup(AdcGroupDto groupDto, SessionDto session) throws OBException, Exception
	{
		log.debug("addAdcGroup: {}", groupDto);
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(groupDto.getName());
		adcSvc.addAdcGroup(toOBDtoAdcGroup(groupDto), extraInfo);
	}

	public void delAdcGroups(List<Integer> groupIndices, SessionDto session) throws OBException, Exception
	{
		log.debug("delAdcGroups: {}", groupIndices);
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(groupIndices.toString());
		adcSvc.delAdcGroupList(new ArrayList<Integer>(groupIndices), extraInfo);
	}

//	public boolean testConnection(AdcDto adc) throws Exception {
	public int testConnection(AdcDto adc) throws OBException, Exception 
	{
		log.debug("testConnection: {}", adc);
		int isReachable=0;
		
//		int vendorType=OBDefine.ADC_TYPE_UNKNOWN;
//		if(adc.getType().equals("F5")==true)
//			vendorType=OBDefine.ADC_TYPE_F5;
//		else if(adc.getType().equals("Alteon")==true)
//			vendorType=OBDefine.ADC_TYPE_ALTEON;
//		else if(adc.getType().equals("PAS")==true)
//			vendorType=OBDefine.ADC_TYPE_PIOLINK_PAS;
//		else if(adc.getType().equals("PASK")==true)
//			vendorType=OBDefine.ADC_TYPE_PIOLINK_PASK;
//		else if(adc.getType().equals("PiolinkUnknown")==true)
//			vendorType=OBDefine.ADC_TYPE_PIOLINK_UNKNOWN;
//		
//		isReachable = adcSvc.isReachable(vendorType, adc.getIp(), adc.getAccountId(), adc.getPassword(), adc.getCliAccountId(), adc.getCliPassword(), adc.getVersion(), adc.getSnmpCommunity(), adc.getConnService(), adc.getConnPort());
		return isReachable;
	}

	// ADC 개별 연결테스트
	public OBAdcCheckResult checkAdcConnection(AdcAddDto adcAdd, Integer chkItemId, boolean isNewADC) throws OBException, Exception
	{
		OBDtoAdcInfo adcInfo = AdcAddDto.toOBDtoAdcInfo(adcAdd);
		
		if (adcInfo.getIndex() != 0)		// ADC 수정의 경우 (adcIndex 존재함)
		{
			isNewADC = false;
		}
		else								// ADC 추가의 경우 (adcIndex null 인경우)
		{
			isNewADC = true;
		}
		OBAdcCheckResult adcChkResult =  adcSvc.checkADCStatus(adcInfo, chkItemId, isNewADC);
		
		log.debug("{}", adcChkResult);
		return adcChkResult;
	}	
	
	// ADC 연결테스트 LastTime
	public ArrayList<OBDtoLastAdcCheckTime> getLastAdcCheckTime(Integer adcIndex) throws OBException
	{
		ArrayList<OBDtoLastAdcCheckTime> lastAdcChkTime = new ArrayList<OBDtoLastAdcCheckTime>();		
		lastAdcChkTime = adcSvc.getLastAdcCheckTime(adcIndex);
		
		return lastAdcChkTime;
	}
	
	// ADC 설정정보에서 연결테스트 
	public ArrayList<OBAdcCheckResult> getChkAdcStatusAll(Integer adcIndex) throws OBException
	{
		ArrayList<OBAdcCheckResult> adcChkResult = new ArrayList<OBAdcCheckResult>();
		adcChkResult = adcSvc.checkADCStatusAll(adcIndex);
		
		return adcChkResult;
	}
	
	//ADC 설정 정보 점검 
	public ArrayList<OBAdcConfigInfo> checkAdcConfigAll(Integer adcIndex) throws OBException, Exception
	{
		ArrayList<OBAdcConfigInfo> configCheck = new ArrayList<OBAdcConfigInfo>();

		configCheck = adcSvc.checkAdcConfigInfo(adcIndex);
		log.debug("{}", configCheck);

		return configCheck;
	}
	
	
	public static OBDtoAdcGroup toOBDtoAdcGroup(AdcGroupDto groupDto) 
	{
		log.debug("toOBDtoAdcGroup: {}", groupDto);
		OBDtoAdcGroup groupFromSvc = new OBDtoAdcGroup();
		groupFromSvc.setName(groupDto.getName());
		groupFromSvc.setDescription(groupDto.getDescription());
		return groupFromSvc;
	}
	
	public void saveAdcConfigContent(AdcAddDto adcAdd, SessionDto session) throws OBException, Exception
	{
		log.debug("adcAdd: {}", adcAdd);
		log.debug("index: {}", adcAdd.getAdc().getIndex());
		log.debug("saveTime: {}", adcAdd.getAdc().getSaveTime());
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();	
		extraInfo.setExtraMsg1((adcAdd.getAdc().getName()));
		adcSvc.saveConfigAlteon(adcAdd.getAdc().getIndex(), extraInfo);
	}
	
	// FLB Group List Get
	public ArrayList<OBDtoFlbGroupMonitorInfo> getFlbGroupMonitorInfo(Integer adcIndex) throws OBException, Exception 
	{
		return flbMonAlteonSvc.getFlbGroupMonitorInfo(adcIndex);			
	}
	
	// FLB Group List Set
	public void modifyFlbGroupMonitorInfo(Integer adcIndex, List<String> selecteAdcGroup) throws OBException, Exception
	{
		ArrayList<String> selctedGroupList;
		if (null == selecteAdcGroup)
		{
			selctedGroupList = null;
		}
		else
		{
			selctedGroupList = new ArrayList<String>(selecteAdcGroup);
		}
		
		flbMonAlteonSvc.setFlbGroupMonitorInfo(adcIndex, selctedGroupList);
	}
	
	public OBDtoAdcInfo passwordEncrypt(OBDtoAdcInfo adcInfo) throws OBException
	{
	    String passWord;
        try
        {
            passWord = new OBCipherAES().Encrypt(adcInfo.getAdcPassword());
            adcInfo.setAdcPassword(passWord);
            if(adcInfo.getAdcType() != 1)
            {
                adcInfo.setAdcCliAccount(adcInfo.getAdcAccount());
                adcInfo.setAdcCliPassword(new OBCipherAES().Encrypt(adcInfo.getAdcPassword()));
            }
            else
            {
                adcInfo.setAdcCliAccount(adcInfo.getAdcCliAccount());
                adcInfo.setAdcCliPassword(new OBCipherAES().Encrypt(adcInfo.getAdcCliPassword()));
            }
        }
        catch(OBException e)
        {
            throw e;
        }
        
        return adcInfo;
	}
	
	public void updateAdcStatusReachable(Integer adcIndex) throws OBException
	{
	    adcSvc.updateAdcStatusReachable(adcIndex);
	}
	
	public void updateAdcStatusUnReachable(Integer adcIndex) throws OBException
	{
	    adcSvc.updateAdcStatusUnReachable(adcIndex);
	}
	
	public Integer getAdcTotalCount(Integer accountIndex) throws OBException
	{
	    return adcSvc.getAdcTotalCount(accountIndex);
	}
	
	public boolean getCheckADCStatusMonitoring(Integer adcIndex) throws OBException
	{
	    return adcSvc.checkADCStatusMonitoring(adcIndex);
	}
	
	public boolean snmpInfoCheck(Integer adcIndex) throws OBException
	{
	    return adcSvc.snmpInfoCheck(adcIndex);
	}
}