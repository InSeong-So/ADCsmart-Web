package kr.openbase.adcsmart.web.controller.adcman;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import kr.openbase.adcsmart.service.dto.OBAdcCfgInfo;
import kr.openbase.adcsmart.service.dto.OBAdcCheckResult;
import kr.openbase.adcsmart.service.dto.OBAdcConfigInfo;
import kr.openbase.adcsmart.service.dto.OBDtoLastAdcCheckTime;
import kr.openbase.adcsmart.service.dto.OBDtoSLBUpdateStatus;
import kr.openbase.adcsmart.service.dto.OBDtoValidLicense;
import kr.openbase.adcsmart.service.dto.flb.OBDtoFlbGroupMonitorInfo;
import kr.openbase.adcsmart.service.impl.OBLicenseImpl;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.adcman.AdcFacade;
import kr.openbase.adcsmart.web.facade.dto.AccountDto;
import kr.openbase.adcsmart.web.facade.dto.AdcAddDto;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.AdcGroupDto;
import kr.openbase.adcsmart.web.facade.dto.VrrpInfoDto;
import kr.openbase.adcsmart.web.util.OBDefineWeb;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

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

@Controller
@Scope(value = "prototype")
public class AdcSettingAction extends BaseAction
{
	private transient Logger log = LoggerFactory.getLogger(AdcSettingAction.class);

	@Autowired
	private AdcFacade adcFacade;

	private SortedSet<AdcGroupDto> adcGroups;
	private AdcGroupDto adcGroup;
	private List<Integer> adcGroupIndices; // for delete
	private AdcDto adc;
	private OBAdcCfgInfo config; // ADC 설정 정보
	private OBAdcCheckResult info; // ADC 기능 정보
	private List<Integer> adcIndices; // for delete
	private Map<String, AdcGroupDto> adcGroupMap;
	private Map<String, AdcGroupDto> adcGroupInfoMap;
	private Map<String, List<AdcDto>> adcGroupToAdcsMap;
	// private Map<String, AdcGroupDto> adcGroupToAdcsMap;
	private String searchKey = "";
	private AdcAddDto adcAdd;
	private int adcCount;
	private List<AccountDto> registeredAccounts;
	private List<AccountDto> availableAccounts;
	//private boolean isReachable;
	private int isReachable;// 1: reachable, 2: unreachable, 0: not test
	private int connTestResult;// 비트 마스크로 결과 전달, 1,2비트: 네트워크 연결, 3,4:로그인, 5,6비트:snmp, 7,8:syslog. 0: not test, 1: ok, 2: fail
	private int isExistNotTestItem;// 0: all test, 1: exist. 연결 테스트 안한 항목이 있는지 검사함.
	private int groupIndex;
	private int adcIndex;
	private String adcIpaddress;
	private String adcId;
	private String adcPassword;
	private String adcType;
	private String configType;
	private Integer opMode;
	private Integer functionCheckType;
	private Integer configCheckType;
	private Integer orderDir; // 오른차순 = 1, 내림차순 = 2
	private Integer orderType; //
	private VrrpInfoDto vrrpInfo; // vrrp 이중화 설정 정보
	private List<VrrpInfoDto> vrrpInfos;
	private List<String> adcNameList;// 등록된 모든 장비의 이름.
	private ArrayList<OBDtoFlbGroupMonitorInfo> flbGroupList; // FLB 그룹 리스트
	private List<String> selecteAdcGroup;
	private OBAdcCheckResult chkResult; 
	private Integer checkId;
//	private boolean isNewADC;
	private ArrayList<OBDtoLastAdcCheckTime> lastAdcChkTime;
	private ArrayList<OBAdcCheckResult> chkResultList;
	private ArrayList<OBAdcConfigInfo> adcConfigList;
	private Integer adcListPageOption;
	private Integer extraKey;
	private boolean confirmMsg = false;
	
	public int getGroupIndex()
	{
		return groupIndex;
	}

	public void setGroupIndex(int groupIndex)
	{
		this.groupIndex = groupIndex;
	}

	public Map<String, AdcGroupDto> getAdcGroupInfoMap()
	{
		return adcGroupInfoMap;
	}

	public void setAdcGroupInfoMap(Map<String, AdcGroupDto> adcGroupInfoMap)
	{
		this.adcGroupInfoMap = adcGroupInfoMap;
	}

	public String retrieveAdcGroupToAdcsMap() throws OBException
	{
		log.debug("{}", adc);
		try
		{
			prepareAdcGroupMapAndGroupToAdcsMap(adc.getName());
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

	/*
	 * 하단 prepareAdcGroupMapAndGroupToAdcsMap 는 Group List를 불러오는데는 더이상 사용하지않지만,
	 * retrieveAdcGroupToAdcsMap 에서 사용하기 때문에 삭제를 보류합니다. Facade도 동일합니다.
	 */
	private void prepareAdcGroupMapAndGroupToAdcsMap(String searchKey) throws OBException, Exception
	{
		adcGroupMap = adcFacade.getAdcGroupsMap();
		adcGroupToAdcsMap = adcFacade.getAdcGroupToAdcsMap(session.getAccountIndex(), searchKey);
		removeUnunsedAdcGroupsFromAdcGroupMap(adcGroupToAdcsMap);
		log.debug("{}", adcGroupMap);
		log.debug("{}", adcGroupToAdcsMap);
	}

	private void removeUnunsedAdcGroupsFromAdcGroupMap(Map<String, List<AdcDto>> adcGroupToAdcsMap) throws OBException, Exception
	{
		for(String key : new ArrayList<String>(adcGroupMap.keySet()))
		{
			if(!adcGroupToAdcsMap.containsKey(key))
				adcGroupMap.remove(key);
		}
	}

	public String loadAdcListContent() throws OBException
	{
		log.debug("searchKey: {}", searchKey);
		try
		{
			adcGroupInfoMap = adcFacade.getAdcGroupInfoMap(session.getAccountIndex(), groupIndex, searchKey, orderType, orderDir, adcListPageOption);
			log.debug("adcGroupToAdcsMap: {}", adcGroupToAdcsMap);
			adcCount = 0;

			ArrayList<AdcGroupDto> groupList = new ArrayList<AdcGroupDto>(adcGroupInfoMap.values());
			for(AdcGroupDto adcGroupInfo : groupList)
			{
				if(adcGroupInfo.getAdcs() != null)
					adcCount += adcGroupInfo.getAdcs().size();
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
	//TODO FLB Group List Get
	public String loadFlbGroupMonitorContent() throws OBException
	{
		try
		{
			flbGroupList = adcFacade.getFlbGroupMonitorInfo(adc.getIndex());
			log.debug("flbGroupList: {}", flbGroupList);			
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
	//TODO FLB Group List Set
	public String modifyFlbGroupMonitorContent() throws OBException
	{
		isSuccessful = true;
		try
		{
			log.debug("selecteAdcGroup: {}", selecteAdcGroup);	
			adcFacade.modifyFlbGroupMonitorInfo(adc.getIndex(), selecteAdcGroup);			
			log.debug("selecteAdcGroupAfter: {}", selecteAdcGroup);			
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

	public String chkAdcStatusAll() throws OBException
    {
        log.debug("adcType:{}", adcIndex);
        try 
        {
            chkResultList = adcFacade.getChkAdcStatusAll(adcIndex);
            lastAdcChkTime = adcFacade.getLastAdcCheckTime(adcIndex);
            
            //연결테스트 후 성공여부에 따라 adcStatus 업데이트
            Integer conStatus = null;
            ArrayList<Integer> conStatusList = new ArrayList<Integer>();
            for (int i = 0; i < chkResultList.size(); i++) 
            {
                conStatus = chkResultList.get(i).getStatus();
//              checkID = chkResultList.get(i).getCheckID();
//              conStatusList.add(checkID);
                conStatusList.add(conStatus);
            }       
            
            if(conStatusList.contains(OBAdcConfigInfo.STATUS_ABNORMAL) || conStatusList.contains(OBAdcConfigInfo.STATUS_FAIL))
            {   //연결테스트 결과에 실패가 있으면 adcStatus를 unReachable로 업데이트 
                adcFacade.updateAdcStatusUnReachable(adcIndex);
            }
            else
            {   //연결테스트 결과에 실패가 없으면, 즉 모두 성공하면 adcStatus를 reachable로 업데이트 
                adcFacade.updateAdcStatusReachable(adcIndex);
            }           
            
            if ( opMode == 1)
            {
                if (adcType.equals("Alteon") )
                {
                    return "monitoringmodeAlteon";
                }
                else
                {
                    return "monitoringmode";
                }
               
            }
            else if ( opMode == 2 && adcType.equals("Alteon"))
            {
                return "settingAlteon";
            }
            else
            {               
                return "normal"; 
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
    }
	
//	public String chkAdcStatusAll() throws OBException
//	{
//		isSuccessful = true;
//		try
//		{
//			chkResultList = adcFacade.getChkAdcStatusAll(adcIndex);	
//			log.debug("chkResultList: {}", chkResultList);			
//		}
//		catch(OBException e)
//		{
//			throw e;
//		}
//		catch(Exception e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//		return SUCCESS;
//	}
	
//	public String configCheck() throws OBException
//	{	
//		//ADC 설정 점검 
//		log.debug("adcIndex:{}", adcIndex);
//		if(configCheckType != null)
//		{
//			try
//			{
//				if(adcType.equals("Alteon"))
//				{
//					config = adcFacade.checkAdcConfig(adcIndex);
//					return "Alteon";
//				}
//				else if(adcType.equals("F5"))
//				{
//					config = adcFacade.checkAdcConfig(adcIndex);
//					return "F5";
//				}
//				else if(adcType.equals("PAS"))
//				{
//					config = adcFacade.checkAdcConfig(adcIndex);
//					return "PAS";
//				}
//				else if(adcType.equals("PASK"))
//				{
//					config = adcFacade.checkAdcConfig(adcIndex);
//					return "PASK";
//				}	
//				
//				log.debug("config: {}", config);
//			}
//			catch(OBException e)
//			{
//				throw e;
//			}
//			catch(Exception e)
//			{
//				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//			}
//		}
//		return SUCCESS;
//	}
	
//	public String config() throws OBException
//	{	
//		//ADC 설정 상태 - 변경, 동기화 
//		log.debug("adcType:{}", adcType);
//		try
//		{
//			if(adcType.equals("Alteon"))
//			{
//				config = adcFacade.loadAdcConfigInfo(adcIndex);
//				adcFacade.configFaill(adcIndex, configType);
//				return "Alteon";
//			}
//			else if(adcType.equals("F5"))
//			{
//				config = adcFacade.loadAdcConfigInfo(adcIndex);
//				adcFacade.configFaill(adcIndex, configType);
//				return "F5";
//			}
//			else if(adcType.equals("PAS"))
//			{
//				config = adcFacade.loadAdcConfigInfo(adcIndex);
//				adcFacade.configFaill(adcIndex, configType);
//				return "PAS";
//			}
//			else if(adcType.equals("PASK"))
//			{
//				config = adcFacade.loadAdcConfigInfo(adcIndex);
//				adcFacade.configFaill(adcIndex, configType);
//				return "PASK";
//			}
//		}
//		catch(OBException e)
//		{
//			throw e;
//		}
//		catch(Exception e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//		return SUCCESS;
//	}
	
	public String loadAdcNameList() throws OBException
	{
		log.debug("searchKey: {}", searchKey);
		try
		{
			adcNameList = adcFacade.getAdcNameList();
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

	public String loadAdcAddContent() throws OBException
	{
		try
		{
			if(adcAdd == null)
			{
				adcAdd = new AdcAddDto();
				adcAdd.setAdc(new AdcDto());
			}

//			adcAdd.getAdc().setSnmpCommunity("public");
			adcGroups = adcFacade.getAdcGroups(); // sorted groups in Group Selection Dialog
			
			log.debug("{}", adcGroups);
			availableAccounts = adcFacade.getAccounts();
			log.debug("{}", availableAccounts);
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

	public String loadAdcModifyContent() throws OBException
	{
		try
		{
			log.debug("{}", adcAdd);
			adcAdd.setAdc(adcFacade.getAdc(adcAdd.getAdc().getIndex())); // ADC 정보 가져오기
			
			log.debug("{}", adcAdd);

			vrrpInfos = adcFacade.getVrrpInfo(adcAdd.getAdc().getIndex());
			log.debug("{}", vrrpInfos);
			adcGroups = adcFacade.getAdcGroups();
			log.debug("{}", adcGroups);
			registeredAccounts = adcFacade.getRegisteredAccounts(adcAdd.getAdc().getIndex());
			log.debug("{}", registeredAccounts);
			availableAccounts = adcFacade.getAvailableAccounts(adcAdd.getAdc().getIndex());
			log.debug("{}", availableAccounts);
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

	public String delAdcs() throws OBException
	{
		isSuccessful = true;
		try
		{
			log.debug("{}", adcIndices);
			adcFacade.delAdcs(adcIndices, session.getSessionDto());
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

	public String addAdc() throws OBException
	{
		isSuccessful = true;
		confirmMsg = false;
		//Integer adcIndex = 0;
		try
		{
			log.debug("addAdc: {}", adcAdd);
			// 라이센스 검사.
            int adcCnt=adcFacade.getAdcTotalCount(null);
            OBDtoValidLicense LicenseObj = new OBDtoValidLicense();
            LicenseObj = new OBLicenseImpl().isValidLicenseExt(adcCnt);
            if(LicenseObj.getMsgKey() != null)
            {
                if(LicenseObj.getMsgKey() == OBDefine.LICENSE_INVALID_DATE)
                {
                    isSuccessful = false;
                    confirmMsg = true;
                    message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_FCHKMSG_LICENSE_INVALID_DATE) + "\n"
                            + OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_FCHKMSG_LICENSE_MOVE_PAGE);
                    return SUCCESS;
                }
                if(LicenseObj.getMsgKey() == OBDefine.LICENSE_INVALID_MAC)
                {
                    isSuccessful = false;
                    confirmMsg = true;
                    message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_FCHKMSG_LICENSE_INVALID_MAC) + "\n"
                            + OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_FCHKMSG_LICENSE_MOVE_PAGE);
                    
                    return SUCCESS;
                }
                if(LicenseObj.getMsgKey() == OBDefine.LICENSE_INVALID_ADC_CNT)
                {
                    isSuccessful = false;
                    confirmMsg = true;
                    message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_FCHKMSG_LICENSE_INVALID_ADC_CNT) +"\n"
                            + OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_FCHKMSG_LICENSE_INVALID_ADC_CNT_ONE)
                            + LicenseObj.getDeviceModel() 
                            + OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_FCHKMSG_LICENSE_INVALID_ADC_CNT_TWO)
                            + LicenseObj.getMaxAdc()
                            + OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_FCHKMSG_LICENSE_INVALID_ADC_CNT_THREE) +"\n"
                            + OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_FCHKMSG_LICENSE_MOVE_PAGE);
                    
                    return SUCCESS;
                }         
            }
              
			if(adcFacade.existsAdc(adcAdd.getAdc().getIp()))
			{
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_IP_DUPLICATED);
				return SUCCESS;
			}
			
			if(adcFacade.existsAdc(adcAdd.getAdc().getName()))
			{
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_NAME_DUPLICATED);
				return SUCCESS;
			}
			
			boolean isRecahbledADC = true;
			
			int adcStatus = OBDefine.ADC_STATUS.UNREACHABLE;
			
			// adc 정보 등록함.
			adcIndex = adcFacade.addAdc(adcAdd, session.getSessionDto(), adcStatus, isRecahbledADC , isReachable);
			
			if(isReachable==0)
            {// a연결 테스트를 수행하지 않은 경우. 연결 테스트를 수행하여 reachable 상태인 경우에만 slb download 를 수행한다. 하나라도 테스트를 안한 경우임.
				if(adcAdd.getAdc().getOpMode() == OBDefine.OP_MODE_MONITORING)
                { // a모니터링 모드이면 네트워크접근, snmp 연결테스트 진행 후 SLB다운로드
                    if(adcFacade.getCheckADCStatusMonitoring(adcIndex))    
                    {// a연결 테스트 성공한 경우
                        if(adcFacade.snmpInfoCheck(adcIndex))
                        {//snmp정보가 존재하는지 체크한다. 체크 후 없으면 snmp정보를 가져온다. 안가져와지면 false가 리턴되고 slb다운로드는 하지 않는다.
                            adcFacade.updateAdcStatusReachable(adcIndex);
                            // slb download 시도함.
                            if(!downloadSLBConfig(adcIndex))
                            {
                                isSuccessful = false;
                            }
                        }
                    }  
                }
				else
				{ // 그외 설정, 진단 모드이면 연결테스트를 한번 수행하고 SLB 다운로드
					String retVal = chkAdcStatus(adcIndex);                     

	                if(retVal.equals(OBDefineWeb.CONFIG_SUCCESS))       // adc를 추가한 후 ADC 연결테스트를 진핸한다.
	                {// a연결 테스트 성공한 경우                 
	                    if(adcFacade.snmpInfoCheck(adcIndex))
	                    {//snmp정보가 존재하는지 체크한다. 체크 후 없으면 snmp정보를 가져온다. 안가져와지면 false가 리턴되고 slb다운로드는 하지 않는다.
	                        adcFacade.updateAdcStatusReachable(adcIndex);
                            // slb download 시도함.
                            if(!downloadSLBConfig(adcIndex))
                            {
                                isSuccessful = false;
                            }
	                    }
	                }   
				}
				log.debug("adcAdd: {}", adcAdd);
				return SUCCESS;
            }
			
			if(isReachable == 1)
            {// a 연결 테스트 항목중에 4개 항목이 정상인 경우.  slb download 를 수행한다.
                if(adcFacade.snmpInfoCheck(adcIndex))
                {//snmp정보가 존재하는지 체크한다. 체크 후 없으면 snmp정보를 가져온다. 안가져와지면 false가 리턴되고 slb다운로드는 하지 않는다.
                    adcFacade.updateAdcStatusReachable(adcIndex);
                    // slb download 시도함.
                    if(!downloadSLBConfig(adcIndex))
                    {
                        isSuccessful = false;
                    }
                }
                
                log.debug("adcAdd: {}", adcAdd);
                return SUCCESS;
            }
			
            if(isReachable == 2) // 연결테스트가 하나라도 비정상인 경우. 다시한번 연결 테스트를 한다.
            { 
                if(adcAdd.getAdc().getOpMode() == OBDefine.OP_MODE_MONITORING)
                { // a모니터링 모드이면 네트워크접근, snmp 연결테스트 진행 후 SLB다운로드
                    if(adcFacade.getCheckADCStatusMonitoring(adcIndex))    
                    {// a연결 테스트 성공한 경우 
                        if(adcFacade.snmpInfoCheck(adcIndex))
                        {//snmp정보가 존재하는지 체크한다. 체크 후 없으면 snmp정보를 가져온다. 안가져와지면 false가 리턴되고 slb다운로드는 하지 않는다.
                            adcFacade.updateAdcStatusReachable(adcIndex);
                            // slb download 시도함.
                            if(!downloadSLBConfig(adcIndex))
                            {
                                isSuccessful = false;
                            }
                        }
                    }  
                }
                else
                { // a그외 설정, 진단 모드이면 연결테스트를 한번 수행하고 SLB 다운로드
                    String retVal = chkAdcStatus(adcIndex);                     
                    
                    if(retVal.equals(OBDefineWeb.CONFIG_SUCCESS))       // adc를 추가한 후 ADC 연결테스트를 진핸한다.
                    {// a연결 테스트 성공한 경우                   
                        if(adcFacade.snmpInfoCheck(adcIndex))
                        {//snmp정보가 존재하는지 체크한다. 체크 후 없으면 snmp정보를 가져온다. 안가져와지면 false가 리턴되고 slb다운로드는 하지 않는다.
                            adcFacade.updateAdcStatusReachable(adcIndex);
                            // slb download 시도함.
                            if(!downloadSLBConfig(adcIndex))
                            {
                                isSuccessful = false;
                            }
                        }
                    }   
                }
                log.debug("adcAdd: {}", adcAdd);
                return SUCCESS;
            }
            
            if(isReachable == 3) // 네트워크 접근, snmp 테스트만 수행하여 정상인 경우
            {
                if(adcAdd.getAdc().getOpMode() == OBDefine.OP_MODE_MONITORING)
                { // a모니터링 모드이면 SLB다운로드
                    if(adcFacade.snmpInfoCheck(adcIndex))
                    {//snmp정보가 존재하는지 체크한다. 체크 후 없으면 snmp정보를 가져온다. 안가져와지면 false가 리턴되고 slb다운로드는 하지 않는다.
                        adcFacade.updateAdcStatusReachable(adcIndex);
                        // slb download 시도함.
                        if(!downloadSLBConfig(adcIndex))
                        {
                            isSuccessful = false;
                        }
                    }
                }
                else
                { // a그외 설정, 진단 모드이면 연결테스트를 한번 수행하고 SLB 다운로드
                    String retVal = chkAdcStatus(adcIndex);                     
                    
                    if(retVal.equals(OBDefineWeb.CONFIG_SUCCESS))       // adc를 추가한 후 ADC 연결테스트를 진핸한다.
                    {// a연결 테스트 성공한 경우                   
                        if(adcFacade.snmpInfoCheck(adcIndex))
                        {//snmp정보가 존재하는지 체크한다. 체크 후 없으면 snmp정보를 가져온다. 안가져와지면 false가 리턴되고 slb다운로드는 하지 않는다.
                            adcFacade.updateAdcStatusReachable(adcIndex);
                            // slb download 시도함.
                            if(!downloadSLBConfig(adcIndex))
                            {
                                isSuccessful = false;
                            }
                        }
                    }   
                }
                log.debug("adcAdd: {}", adcAdd);
                return SUCCESS;
            }
			
			return SUCCESS;
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

    public String modifyAdc() throws OBException
    {
        isSuccessful = true;
        try
        {
            log.debug("modifyAdc: {}", adcAdd);
            
            adcFacade.modifyAdc(adcAdd, session.getSessionDto(), isReachable);
            
            if(isReachable == 0)
            {// a연결 테스트를 수행하지 않은 경우. 연결 테스트를 수행하여 reachable 상태인 경우에만 slb download 를 수행한다.
                if(adcAdd.getAdc().getOpMode() == OBDefine.OP_MODE_MONITORING)
                { // a모니터링 모드이면 네트워크접근, snmp 연결테스트 진행 후 SLB다운로드
                    if(adcFacade.getCheckADCStatusMonitoring(adcAdd.getAdc().getIndex()))    
                    {//a 연결 테스트 성공한 경우
                        if(adcFacade.snmpInfoCheck(adcAdd.getAdc().getIndex()))
                        {//snmp정보가 존재하는지 체크한다. 체크 후 없으면 snmp정보를 가져온다. 안가져와지면 false가 리턴되고 slb다운로드는 하지 않는다.
                            adcFacade.updateAdcStatusReachable(adcAdd.getAdc().getIndex());
                            // slb download 시도함.
                            if(!downloadSLBConfig(adcAdd.getAdc().getIndex()))
                            {
                                isSuccessful = false;
                            }
                        }
                    }
                    else
                    {
                        adcFacade.updateAdcStatusUnReachable(adcAdd.getAdc().getIndex());
                    }
                }
                else
                { // 그외 설정, 진단 모드이면 연결테스트를 한번 수행하고 SLB 다운로드
                    String retVal = chkAdcStatus(adcAdd.getAdc().getIndex());                     
                    
                    if(retVal.equals(OBDefineWeb.CONFIG_SUCCESS))       // adc를 추가한 후 ADC 연결테스트를 진핸한다.
                    {// a연결 테스트 성공한 경우
                        if(adcFacade.snmpInfoCheck(adcAdd.getAdc().getIndex()))
                        {//snmp정보가 존재하는지 체크한다. 체크 후 없으면 snmp정보를 가져온다. 안가져와지면 false가 리턴되고 slb다운로드는 하지 않는다.
                            adcFacade.updateAdcStatusReachable(adcAdd.getAdc().getIndex());
                            // slb download 시도함.
                            if(!downloadSLBConfig(adcAdd.getAdc().getIndex()))
                            {
                                isSuccessful = false;
                            }
                        }
                    }
                    else
                    {
                        adcFacade.updateAdcStatusUnReachable(adcAdd.getAdc().getIndex());
                    }
                }                
                log.debug("adcAdd: {}", adcAdd);
                return SUCCESS;
            }
            
            if(isReachable == 1 )       // 연결된 상태이면 download 진행을 한다.
            {
                if(adcFacade.snmpInfoCheck(adcAdd.getAdc().getIndex()))
                {//snmp정보가 존재하는지 체크한다. 체크 후 없으면 snmp정보를 가져온다. 안가져와지면 false가 리턴되고 slb다운로드는 하지 않는다.
                    adcFacade.updateAdcStatusReachable(adcAdd.getAdc().getIndex());
                    // slb download 시도함.
                    if(!downloadSLBConfig(adcAdd.getAdc().getIndex()))
                    {
                        isSuccessful = false;
                    }
                }
                log.debug("adcAdd: {}", adcAdd);
                return SUCCESS;
            }
            
            if(isReachable == 2 )   // 연결테스트가 하나라도 비정상인 경우
            {
                if(adcAdd.getAdc().getOpMode() == OBDefine.OP_MODE_MONITORING)
                { // a모니터링 모드이면 네트워크접근, snmp 연결테스트 진행 후 SLB다운로드
                    if(adcFacade.getCheckADCStatusMonitoring(adcAdd.getAdc().getIndex()))    
                    {// a연결 테스트 성공한 경우
                        if(adcFacade.snmpInfoCheck(adcAdd.getAdc().getIndex()))
                        {//snmp정보가 존재하는지 체크한다. 체크 후 없으면 snmp정보를 가져온다. 안가져와지면 false가 리턴되고 slb다운로드는 하지 않는다.
                            adcFacade.updateAdcStatusReachable(adcAdd.getAdc().getIndex());
                            // slb download 시도함.
                            if(!downloadSLBConfig(adcAdd.getAdc().getIndex()))
                            {
                                isSuccessful = false;
                            }
                        }
                    }
                    else
                    {
                        adcFacade.updateAdcStatusUnReachable(adcAdd.getAdc().getIndex());
                    }
                }
                else
                { // a 그외 설정, 진단 모드이면 연결테스트를 한번 수행하고 SLB 다운로드
                    String retVal = chkAdcStatus(adcAdd.getAdc().getIndex());                     
                    
                    if(retVal.equals(OBDefineWeb.CONFIG_SUCCESS))       // adc를 추가한 후 ADC 연결테스트를 진핸한다.
                    {// a연결 테스트 성공한 경우 
                        if(adcFacade.snmpInfoCheck(adcAdd.getAdc().getIndex()))
                        {//snmp정보가 존재하는지 체크한다. 체크 후 없으면 snmp정보를 가져온다. 안가져와지면 false가 리턴되고 slb다운로드는 하지 않는다.
                            adcFacade.updateAdcStatusReachable(adcAdd.getAdc().getIndex());
                            // slb download 시도함.
                            if(!downloadSLBConfig(adcAdd.getAdc().getIndex()))
                            {
                                isSuccessful = false;
                            }
                        }
                    }
                    else
                    {
                        adcFacade.updateAdcStatusUnReachable(adcAdd.getAdc().getIndex());
                    }
                }
                log.debug("adcAdd: {}", adcAdd);
                return SUCCESS;
             }
             
            if(isReachable == 3)   // 네트워크 접근, snmp 테스트만 수행하여 정상인 경우
             {
                if(adcAdd.getAdc().getOpMode() == OBDefine.OP_MODE_MONITORING)
                { // a모니터링 모드이면 SLB다운로드
                    if(adcFacade.snmpInfoCheck(adcAdd.getAdc().getIndex()))
                    {//snmp정보가 존재하는지 체크한다. 체크 후 없으면 snmp정보를 가져온다. 안가져와지면 false가 리턴되고 slb다운로드는 하지 않는다.
                        adcFacade.updateAdcStatusReachable(adcAdd.getAdc().getIndex());
                        // slb download 시도함.
                        if(!downloadSLBConfig(adcAdd.getAdc().getIndex()))
                        {
                            isSuccessful = false;
                        }
                    }
                }
                else
                { // a그외 설정, 진단 모드이면 연결테스트를 한번 수행하고 SLB 다운로드
                    String retVal = chkAdcStatus(adcAdd.getAdc().getIndex());                     
                    
                    if(retVal.equals(OBDefineWeb.CONFIG_SUCCESS))       // adc를 추가한 후 ADC 연결테스트를 진핸한다.
                    {// a연결 테스트 성공한 경우  
                        if(adcFacade.snmpInfoCheck(adcAdd.getAdc().getIndex()))
                        {//snmp정보가 존재하는지 체크한다. 체크 후 없으면 snmp정보를 가져온다. 안가져와지면 false가 리턴되고 slb다운로드는 하지 않는다.
                            adcFacade.updateAdcStatusReachable(adcAdd.getAdc().getIndex());
                            // slb download 시도함.
                            if(!downloadSLBConfig(adcAdd.getAdc().getIndex()))
                            {
                                isSuccessful = false;
                            } 
                        }
                    }
                    else
                    {
                        adcFacade.updateAdcStatusUnReachable(adcAdd.getAdc().getIndex());
                    }
                }
                log.debug("adcAdd: {}", adcAdd);
                return SUCCESS;
             }
            log.debug("adcAdd: {}", adcAdd);
            return SUCCESS;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
    }

	public String retrieveAdcGroups() throws OBException
	{
		try
		{
			adcGroups = adcFacade.getAdcGroups();
			log.debug("{}", adcGroups);
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

	public String addAdcGroup() throws OBException
	{
		try
		{
			log.debug("adcGroup: {}", adcGroup);
			if(adcFacade.existsAdcGroup(adcGroup.getName()))
			{
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_NAME_DUPLICATED);
				return SUCCESS;
			}

			adcFacade.addAdcGroup(adcGroup, session.getSessionDto());
			adcGroups = adcFacade.getAdcGroups();
			log.debug("{}", adcGroups);
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

	public String delAdcGroups() throws OBException
	{
		log.debug("adcGroupIndices: {}", (Object) adcGroupIndices);
		isSuccessful = true;
		try
		{
			if(CollectionUtils.isNotEmpty(adcGroupIndices))
			{
				adcFacade.delAdcGroups(adcGroupIndices, session.getSessionDto());
				adcGroups = adcFacade.getAdcGroups();
				log.debug("{}", adcGroups);
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

//	// All test Connection 
//	public String testConnection() throws OBException
//	{
//		try
//		{
//			log.debug("{}", adcAdd);
//			isReachable = adcFacade.testConnection(adcAdd.getAdc()); // boolean 에서 int 로 변경
//																																		
//			log.debug("isReachable: {}", isReachable);
//		}
//		catch(OBException e)
//		{
//			throw e;
//		}
//		catch(Exception e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//		return SUCCESS;
//	}
	
	// 개별 test Connection
	public String checkAdcConnection() throws OBException
	{
		isSuccessful = true;
		try 
		{
			log.debug("{}", adcAdd);			
			boolean isNewADC=true;
			chkResult = adcFacade.checkAdcConnection(adcAdd, checkId, isNewADC);
		} 
//		catch (OBException e)
//		{
//			isSuccessful = false;
//			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_SLB_DOWNLOAD_FAIL);
//			throw e;
//		}
		catch (Exception e) 
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}	
	
	// last update time
	public String loadLastAdcChkTime() throws OBException
	{
		try 
		{
			lastAdcChkTime = adcFacade.getLastAdcCheckTime(adcIndex);
			log.debug("{}", lastAdcChkTime);
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
	
	// ADC 설정정보 기본 페이지 open
	public String loadContent() throws OBException
	{
		log.debug("adcType:{}", adcType);
		try
		{
	        if(adcType.equals("Alteon"))
            {
                config = adcFacade.loadAdcConfigInfo(adcIndex);
                return "Alteon";
            }
            else if(adcType.equals("F5"))
            {
                config = adcFacade.loadAdcConfigInfo(adcIndex);
                return "F5";
            }
            else if(adcType.equals("PAS"))
            {
                config = adcFacade.loadAdcConfigInfo(adcIndex);
                return "PAS";
            }
            else if(adcType.equals("PASK"))
            {
                config = adcFacade.loadAdcConfigInfo(adcIndex);
                return "PASK";
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
	
	// ADC 설정 정보 - ADC 추가시 (adcType 에 상관없음)
	public String checkAdc() throws OBException
	{	
		//ADC 설정 점검 
		log.debug("adcIndex:{}", adcIndex);
		try
		{
			adcConfigList = adcFacade.checkAdcConfigAll(adcIndex);
			log.debug("adcConfigList: {}", adcConfigList);
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
	
	public String chkAdcStatus(Integer adcIndex) throws OBException
    {
        try 
        {  
            chkResultList = adcFacade.getChkAdcStatusAll(adcIndex);
            
            Integer conStatus = null;
            ArrayList<Integer> conStatusList = new ArrayList<Integer>();
            for (int i = 0; i < chkResultList.size(); i++) 
            {
                conStatus = chkResultList.get(i).getStatus();
//              checkID = chkResultList.get(i).getCheckID();
//              conStatusList.add(checkID);
                conStatusList.add(conStatus);
            }       
            
            if(conStatusList.contains(OBAdcConfigInfo.STATUS_ABNORMAL) || conStatusList.contains(OBAdcConfigInfo.STATUS_FAIL))
            {
                return ERROR;
            }
            else
            {
                return SUCCESS;
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
    }
	
	public String chkAdcStatus() throws OBException
	{
		try 
		{
			chkResultList = adcFacade.getChkAdcStatusAll(adcIndex);
			
			Integer conStatus = null;
			ArrayList<Integer> conStatusList = new ArrayList<Integer>();
			for (int i = 0; i < chkResultList.size(); i++) 
			{
				conStatus = chkResultList.get(i).getStatus();
//				checkID = chkResultList.get(i).getCheckID();
//				conStatusList.add(checkID);
				conStatusList.add(conStatus);
			}		
			
			if(conStatusList.contains(OBAdcConfigInfo.STATUS_ABNORMAL) || conStatusList.contains(OBAdcConfigInfo.STATUS_FAIL))
			{
				return ERROR;
			}
			else
			{
				return SUCCESS;
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
	}
	
	// ADC 설정 정보
	public String checkAdcConfig() throws OBException
	{	
		//ADC 설정 점검 
		log.debug("adcIndex:{}", adcIndex);

		try
        {
		    if(configCheckType != null)
	        {		       
                if(adcType.equals("Alteon"))
                {
                    adcConfigList = adcFacade.checkAdcConfigAll(adcIndex);
//	                    lastAdcChkTime = adcFacade.getLastAdcCheckTime(adcIndex);
                    loadContent();
                    log.debug("adcConfigList: {}", adcConfigList);
//	                    log.debug("lastAdcChkTime: {}", lastAdcChkTime);
                    return "Alteon";
                }
                else if(adcType.equals("F5"))
                {
                    adcConfigList = adcFacade.checkAdcConfigAll(adcIndex);
                    lastAdcChkTime = adcFacade.getLastAdcCheckTime(adcIndex);
                    return "F5";
                }
                else if(adcType.equals("PAS"))
                {
                    adcConfigList = adcFacade.checkAdcConfigAll(adcIndex);
                    lastAdcChkTime = adcFacade.getLastAdcCheckTime(adcIndex);
                    return "PAS";
                }
                else if(adcType.equals("PASK"))
                {
                    adcConfigList = adcFacade.checkAdcConfigAll(adcIndex);
                    lastAdcChkTime = adcFacade.getLastAdcCheckTime(adcIndex);
                    return "PASK";
                }             
               
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

	public String saveAdcConfigContent() throws OBException
	{
		try
		{
			log.debug("addAdc: {}", adcAdd);
			adcFacade.saveAdcConfigContent(adcAdd, session.getSessionDto());
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
	//TODO
	public boolean downloadSLBConfig(Integer adcIndex) throws OBException
    {
        OBDtoSLBUpdateStatus retVal = new OBDtoSLBUpdateStatus();
        try 
        {            
            retVal = adcFacade.downloadSlbConfig(adcIndex);
            if(retVal.isUpdateSuccess())                
            {
                isSuccessful = true;
                message = "";
                return true;
            }
            else
            {
                if (retVal.getExtraMsg2() == null || retVal.getExtraMsg2().isEmpty())
                {   
                    extraKey = retVal.getExtraKey();
                    message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_SLB_DOWNLOAD_FAIL);                       
                }
                else
                {                       
                    message = retVal.getExtraMsg2();
                } 
                return false;
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
    }
	
	public String downloadSLBConfigForced() throws OBException
    {
	    isSuccessful = true;
        OBDtoSLBUpdateStatus retVal = new OBDtoSLBUpdateStatus();
        try 
        {            
            retVal = adcFacade.downloadSlbConfig(adcIndex);
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

	public SortedSet<AdcGroupDto> getAdcGroups()
	{
		return adcGroups;
	}

	public void setAdcGroups(SortedSet<AdcGroupDto> adcGroups)
	{
		this.adcGroups = adcGroups;
	}

	public String getSearchKey()
	{
		return searchKey;
	}

	public void setSearchKey(String searchKey)
	{
		this.searchKey = searchKey;
	}

	public AdcGroupDto getAdcGroup()
	{
		return adcGroup;
	}

	public void setAdcGroup(AdcGroupDto adcGroup)
	{
		this.adcGroup = adcGroup;
	}

	public List<Integer> getAdcGroupIndices()
	{
		return adcGroupIndices;
	}

	public void setAdcGroupIndices(List<Integer> adcGroupIndices)
	{
		this.adcGroupIndices = adcGroupIndices;
	}

	public List<Integer> getAdcIndices()
	{
		return adcIndices;
	}

	public void setAdcIndices(List<Integer> adcIndices)
	{
		this.adcIndices = adcIndices;
	}

	public AdcDto getAdc()
	{
		return adc;
	}

	public void setAdc(AdcDto adc)
	{
		this.adc = adc;
	}

	public Map<String, AdcGroupDto> getAdcGroupMap()
	{
		return adcGroupMap;
	}

	public void setAdcGroupMap(Map<String, AdcGroupDto> adcGroupMap)
	{
		this.adcGroupMap = adcGroupMap;
	}

	public Map<String, List<AdcDto>> getAdcGroupToAdcsMap()
	{
		return adcGroupToAdcsMap;
	}

	public void setAdcGroupToAdcsMap(Map<String, List<AdcDto>> adcGroupToAdcsMap)
	{
		this.adcGroupToAdcsMap = adcGroupToAdcsMap;
	}

	public AdcAddDto getAdcAdd()
	{
		return adcAdd;
	}

	public void setAdcAdd(AdcAddDto adcAdd)
	{
		this.adcAdd = adcAdd;
	}

	public int getAdcCount()
	{
		return adcCount;
	}

	public void setAdcCount(int adcCount)
	{
		this.adcCount = adcCount;
	}

	public List<AccountDto> getRegisteredAccounts()
	{
		return registeredAccounts;
	}

	public void setRegisteredAccounts(List<AccountDto> registeredAccounts)
	{
		this.registeredAccounts = registeredAccounts;
	}

	public List<AccountDto> getAvailableAccounts()
	{
		return availableAccounts;
	}

	public void setAvailableAccounts(List<AccountDto> availableAccounts)
	{
		this.availableAccounts = availableAccounts;
	}

	public List<VrrpInfoDto> getVrrpInfos()
	{
		return vrrpInfos;
	}

	public void setVrrpInfos(List<VrrpInfoDto> vrrpInfos)
	{
		this.vrrpInfos = vrrpInfos;
	}

	public VrrpInfoDto getVrrpInfo()
	{
		return vrrpInfo;
	}

	public void setVrrpInfo(VrrpInfoDto vrrpInfo)
	{
		this.vrrpInfo = vrrpInfo;
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

	public List<String> getAdcNameList()
	{
		return adcNameList;
	}

	public void setAdcNameList(List<String> adcNameList)
	{
		this.adcNameList = adcNameList;
	}

	public int getAdcIndex()
	{
		return adcIndex;
	}

	public void setAdcIndex(int adcIndex)
	{
		this.adcIndex = adcIndex;
	}

	public OBAdcCheckResult getInfo()
	{
		return info;
	}

	public void setInfo(OBAdcCheckResult info)
	{
		this.info = info;
	}

	public String getAdcIpaddress()
	{
		return adcIpaddress;
	}

	public void setAdcIpaddress(String adcIpaddress)
	{
		this.adcIpaddress = adcIpaddress;
	}

	public String getAdcId()
	{
		return adcId;
	}

	public void setAdcId(String adcId)
	{
		this.adcId = adcId;
	}

	public String getAdcPassword()
	{
		return adcPassword;
	}

	public void setAdcPassword(String adcPassword)
	{
		this.adcPassword = adcPassword;
	}

	public Integer getFunctionCheckType()
	{
		return functionCheckType;
	}

	public void setFunctionCheckType(Integer functionCheckType)
	{
		this.functionCheckType = functionCheckType;
	}

	public Integer getConfigCheckType()
	{
		return configCheckType;
	}

	public void setConfigCheckType(Integer configCheckType)
	{
		this.configCheckType = configCheckType;
	}

	public OBAdcCfgInfo getConfig()
	{
		return config;
	}

	public void setConfig(OBAdcCfgInfo config)
	{
		this.config = config;
	}

	public String getConfigType()
	{
		return configType;
	}

	public void setConfigType(String configType)
	{
		this.configType = configType;
	}

	public String getAdcType()
	{
		return adcType;
	}

	public void setAdcType(String adcType)
	{
		this.adcType = adcType;
	}

	public ArrayList<OBDtoFlbGroupMonitorInfo> getFlbGroupList()
	{
		return flbGroupList;
	}

	public void setFlbGroupList(ArrayList<OBDtoFlbGroupMonitorInfo> flbGroupList)
	{
		this.flbGroupList = flbGroupList;
	}

	public List<String> getSelecteAdcGroup()
	{
		return selecteAdcGroup;
	}

	public void setSelecteAdcGroup(List<String> selecteAdcGroup)
	{
		this.selecteAdcGroup = selecteAdcGroup;
	}	
		
	public OBAdcCheckResult getChkResult() 
	{
		return chkResult;
	}

	public void setChkResult(OBAdcCheckResult chkResult) 
	{
		this.chkResult = chkResult;
	}

	public Integer getCheckId() 
	{
		return checkId;
	}

	public void setCheckId(Integer checkId) 
	{
		this.checkId = checkId;
	}

	public ArrayList<OBDtoLastAdcCheckTime> getLastAdcChkTime() 
	{
		return lastAdcChkTime;
	}

	public void setLastAdcChkTime(ArrayList<OBDtoLastAdcCheckTime> lastAdcChkTime) 
	{
		this.lastAdcChkTime = lastAdcChkTime;
	}	

	public ArrayList<OBAdcCheckResult> getChkResultList() 
	{
		return chkResultList;
	}

	public void setChkResultList(ArrayList<OBAdcCheckResult> chkResultList) 
	{
		this.chkResultList = chkResultList;
	}	

	public ArrayList<OBAdcConfigInfo> getAdcConfigList() 
	{
		return adcConfigList;
	}

	public void setAdcConfigList(ArrayList<OBAdcConfigInfo> adcConfigList) 
	{
		this.adcConfigList = adcConfigList;
	}

	public int getConnTestResult() {
		return connTestResult;
	}

	public void setConnTestResult(int connTestResult) {
		this.connTestResult = connTestResult;
	}

	public int getIsExistNotTestItem() {
		return isExistNotTestItem;
	}

	public void setIsExistNotTestItem(int isExistNotTestItem) {
		this.isExistNotTestItem = isExistNotTestItem;
	}
	
	public Integer getAdcListPageOption()
	{
		return adcListPageOption;
	}

	public void setAdcListPageOption(Integer adcListPageOption)
	{
		this.adcListPageOption = adcListPageOption;
	}
	
	public Integer getOpMode()
    {
        return opMode;
    }

    public void setOpMode(Integer opMode)
    {
        this.opMode = opMode;
    }

    public int getIsReachable()
    {
        return isReachable;
    }

    public void setIsReachable(int isReachable)
    {
        this.isReachable = isReachable;
    }
    
    public Integer getExtraKey()
    {
        return extraKey;
    }

    public void setExtraKey(Integer extraKey)
    {
        this.extraKey = extraKey;
    }    

    public boolean isConfirmMsg()
    {
        return confirmMsg;
    }

    public void setConfirmMsg(boolean confirmMsg)
    {
        this.confirmMsg = confirmMsg;
    }

    @Override
    public String toString()
    {
        return "AdcSettingAction [adcFacade=" + adcFacade + ", adcGroups=" + adcGroups + ", adcGroup=" + adcGroup + ", adcGroupIndices=" + adcGroupIndices + ", adc=" + adc + ", config=" + config + ", info=" + info + ", adcIndices=" + adcIndices + ", adcGroupMap=" + adcGroupMap + ", adcGroupInfoMap=" + adcGroupInfoMap + ", adcGroupToAdcsMap=" + adcGroupToAdcsMap + ", searchKey=" + searchKey + ", adcAdd=" + adcAdd + ", adcCount=" + adcCount + ", registeredAccounts=" + registeredAccounts + ", availableAccounts=" + availableAccounts + ", isReachable=" + isReachable + ", connTestResult=" + connTestResult + ", isExistNotTestItem=" + isExistNotTestItem + ", groupIndex=" + groupIndex + ", adcIndex=" + adcIndex + ", adcIpaddress=" + adcIpaddress + ", adcId=" + adcId + ", adcPassword=" + adcPassword + ", adcType=" + adcType + ", configType=" + configType + ", opMode=" + opMode + ", functionCheckType=" + functionCheckType + ", configCheckType=" + configCheckType + ", orderDir=" + orderDir + ", orderType=" + orderType + ", vrrpInfo=" + vrrpInfo + ", vrrpInfos=" + vrrpInfos + ", adcNameList=" + adcNameList + ", flbGroupList=" + flbGroupList + ", selecteAdcGroup=" + selecteAdcGroup + ", chkResult=" + chkResult + ", checkId=" + checkId + ", lastAdcChkTime=" + lastAdcChkTime + ", chkResultList=" + chkResultList + ", adcConfigList=" + adcConfigList + ", adcListPageOption=" + adcListPageOption + ", extraKey=" + extraKey + "]";
    }
}