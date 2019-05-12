/*
 * ADC 장비의 추가/삭제/변경등의 작업을 수행.
 */
package kr.openbase.adcsmart.service;

import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBAdcCfgInfo;
import kr.openbase.adcsmart.service.dto.OBAdcCheckResult;
import kr.openbase.adcsmart.service.dto.OBAdcConfigInfo;
import kr.openbase.adcsmart.service.dto.OBDtoADCGroupInfo;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAccount;
import kr.openbase.adcsmart.service.dto.OBDtoAdcGroup;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcLogSearchOption;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNoticeGroup;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSchedule;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;
import kr.openbase.adcsmart.service.dto.OBDtoAuditLogAdcConfig;
import kr.openbase.adcsmart.service.dto.OBDtoAuditLogAdcSystem;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoLastAdcCheckTime;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoRespGroup;
import kr.openbase.adcsmart.service.dto.OBDtoRespMultiChartData;
import kr.openbase.adcsmart.service.dto.OBDtoSLBUpdateStatus;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoSlbUser;
import kr.openbase.adcsmart.service.dto.OBDtoVSGroupInfo;
import kr.openbase.adcsmart.service.dto.OBDtoVrrpInfo;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBLicenseExpiredException;

/**
 * @author ju
 *
 */
public interface OBAdcManagement
{
	/**
	 * config sync을 위한 pair 장비가 설정되어 있는 경우의 peer 장비의 index를 구한다.
	 * 
	 * @param adcIndex
	 * @return peer 장비의 index. null 또는 0 이면 등록되어 있지 않음을 의미.
	 * @throws OBException
	 */
	public Integer getActivePairIndex(Integer adcIndex) throws OBException;
	
	/**
	 * config sync를 위한 설정이 되어 있는지 확인한다.
	 * 
	 * @param adcIndex
	 * @return true: 설정. false: 설정 안됨.
	 * @throws OBException
	 */
	public boolean isExistPeerIP(Integer adcIndex) throws OBException;
	
	/**
	 *   장비에 설정된 vrrp 정보를 제공한다.
	 *   
	 * @param adcIndex
	 * @return OBDtoVrrpInfo
	 * @throws OBException
	 */
	public OBDtoVrrpInfo getVrrpInfo(Integer adcIndex) throws OBException;
	
	/**
	 * 알테온 장비에 대해서 save 명령을 수행한다.
	 * 
	 * @param adcIndex : adc 장비 index.
	 * @param extraInfo : 부가적인 정보. accountIndex, 부가적인 설명을 위한 message가 전달된다. null 불가.
	 * @throws OBException 장비에 접속할 수 없거나, 작업 진행중에 오류가 발생될 경우..
	 */
	public void saveConfigAlteon(Integer adcIndex, OBDtoExtraInfo extraInfo) throws OBException;
	
	/**
	 * ADC 연결상태를 확인한다. 네트워크 통신과 로그인을 테스트 하여 상황에 따른 반환을 한다. 0: 정상. 1: unreachable, 2: login fail, 3. snmp community 오류.  
	 * @param type
	 * @param ipaddress
	 * @param account
	 * @param password
	 * @param swVersion
	 * @param snmpCommunity
	 * @param connService - adc 접속 서비스, Telnet or SSH
	 * @param connPort - adc 접속 포트
	 * @return
	 * @throws OBException
	 */
//	public Integer isReachable(Integer type, String ipaddress, String account, String password,  String cliAccount, String cliPassword, String swVersion, String snmpCommunity, int connService, int connPort) throws OBException;

//	/**
//	 * 연결 테스트를 시작한다. 연결 테스트는 thread 형태로 진행된다.
//	 * @param adcInfo. 다음과 같은 항목은 필수적으로 설정되어 있어야 한다. 
//	 * 			adcIpAddress, adcAccount, adcPassword, adcCliAccount, adcCliPassword, adcType, snmpRComm, connService, connPort
//	 * @return 테스트 ID가 리턴됨. long 형. 테스트 ID는 테스트 결과를 조회하기 위해 사용됨. 테스트 ID는 현재 시간을 long형태로 변형하여 사용하도록 함.
//	 * @throws OBException
//	 */
//	public Long startConnectionTest(OBDtoAdcInfo adcInfo) throws OBException;

//	public OBDtoConnTestStatus getConnectionTestStatus(Long testID) throws OBException;
	
	/**
	 * ADC의 연결 상태를 검사한다.
	 * 
	 * @param adcInfo
	 * @param checkItemID. OBDtoConnTestStatus에 정의되어 있음. 네트워크 오픈, 로그인, syslog, snmp 등등을 검사함. 
	 * @param isNewADC. 신규 추가할 ADC일 경우에만 true. 기존에 추가된 ADC일 경우에는 false.
	 * @return
	 * @throws OBException
	 */
	public OBAdcCheckResult checkADCStatus(OBDtoAdcInfo adcInfo, Integer checkItemID, boolean isNewADC) throws OBException;

	/**
	 * 특정 이름/IP를 갖는 ADC가 있는지 확인한다. adcNameIP와 이름이나 IP가 일치하는 ADC가 있는지 찾는다. 
	 * @param adcNameIP : 확인하려는 ADC 이름이나 ADC IP. null 불가
	 * @return
	 *  - true: 주어진 조건에 맞는 ADC가 있음
	 *  - false: 주어진 조건에 맞는 ADC가 없음
	 * @throws OBException
	 */
	public boolean isExistAdc(String adcNameIP) throws OBException;
	
	/**
	 * 특정 이름의 ADC 그룹이 있는지 확인한다.
	 *  
	 * @param groupName : 확인하려는 그룹 이름. null 불가
	 * @return
	 *  - true: 조건에 맞는 그룹이 있음
	 *  - false: 조건에 맞는 그룹이 없음
	 * @throws OBException
	 */
	public boolean isExistGroup(String groupName) throws OBException;

	/**
	 * 특정 그룹에 속한 ADC 수를 가져온다.
	 * @param groupIndex : ADC 수를 확인하려는 그룹의 인덱스. null 불가
	 * @return
	 *  - 그룹 소속 장비 개수 0 또는 양의 정수
	 * @throws OBException
	 */
	public Integer getGroupAdcCount(Integer groupIndex) throws OBException;
	
	/**
	 * 사용자가 액세스 할 수 있는 ADC의 목록을 가져온다. 
	 * SystemAdmin이 아니면 해당 사용자에게 할당된 ADC 목록을 가져오고 SystemAdmin역할의 계정이면 전체 ADC 목록을 가져온다. 
	 * @param accountIndex : 사용자 계정 인덱스. null 불가
	 * @return
	 *  - 사용자가 접근 할 수 있는 ADC 목록
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcInfo> getAdcInfoList(Integer accountIndex) throws OBException;

	/**
	 * 지정된 계정에 할당된 ADC 장비의 개수를 리턴한다.
	 * @param accountIndex : 로그인 계정. null 불가.
	 * @return Integer
	 * @throws OBException
	 */
	public Integer getAdcTotalCount(Integer accountIndex) throws OBException;
	
	/**
	 * 입력된 계정에 할당된 ADC 정보를 추출한다. 입력된 계정이 admin권한일 경우에는 모든 ADC 목록을 제공한다.
	 * 
	 * @param accountIndex
	 * @param beginIndex
	 * @param endIndex
	 * @param orderType. OBDefine.ORDER_TYPE_ADCNAME, OBDefine.ORDER_TYPE_ADCSTATUS, OBDefine.ORDER_TYPE_ADCIPADDRESS, OBDefine.ORDER_TYPE_OCCURTIME
	 * @param orderDir. ORDER_DIR_ASCEND, ORDER_DIR_DESCEND
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcInfo> getAdcInfoList(Integer accountIndex, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;
	
	/**
	 * 사용자가 액세스 할 수 있는 ADC 범위에서 검색 조건(searchKey)으로 필터링한 ADC 목록을 가져온다. 
	 * SystemAdmin이 아니면 해당 사용자에게 할당된 ADC 목록 중에서 검색하고, SystemAdmin역할의 계정이면 전체 ADC 목록에서 검색한다.
	 * 검색 조건(searchKey)이 null이면 액세스 가능한 전체 ADC 목록을 가져온다.
	 * 
	 * @param accountIndex : 사용자 계정 인덱스. null 불가
	 * @param searchKey : 검색하고자 하는 ADC 이름이나 ADC IP의 일부 문자열. null 가능 
	 * @return
	 *  - 사용자가 액세스 할 수 있는 범위 안에서 검색조건에 맞는 ADC의 목록
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcInfo> searchAdcInfoList(Integer accountIndex, String searchKey) throws OBException;
	
	/**
	 * ADC 그룹에 속해 있는 ADC의 목록을 가져온다. 사용자의 계정에 할당 된 것들만 가져온다.
	 * @param groupIndex : ADC 그룹 인덱스. null 불가
	 * @param accountIndex : 계정 인덱스. null 불가
	 * @return
	 *  - ADC 그룹에 소속된 ADC의 목록
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcInfo> getAdcInfoListInGroup(Integer groupIndex, Integer accountIndex) throws OBException;
	
	/**
	 * 지정 ADC의 상세 정보를 가져온다.
	 *  
	 * @param adcIndex : ADC 인덱스. null 불가
	 * @return
	 *  - ADC의 상세 정보
	 * @throws OBException
	 */
	public OBDtoAdcInfo getAdcInfo(Integer adcIndex) throws OBException;
	
	/**
	 * 새 ADC를 등록한다.
	 * 단일 ADC를 등록한다. active/standby ADC를 동시에 등록하려면 이 다음의 함수를 쓴다. 
	 * 
	 * @param adcInfo : 새 ADC의 정보. null 불가
	 * @param extraInfo : 부가적인 정보. accountIndex, 부가적인 설명을 위한 message가 전달된다. null 불가.
	 * @return adcIndex
	 * @throws OBLicenseExpiredException
	 * @throws OBException
	 */
	 public Integer addAdcInfo(OBDtoAdcInfo adcInfo, OBDtoExtraInfo extraInfo, boolean isReachabled, int isReachable) throws OBLicenseExpiredException, OBException;
	//public Integer addAdcInfo(OBDtoAdcInfo adcInfo, OBDtoExtraInfo extraInfo, boolean isReachabled) throws OBLicenseExpiredException, OBException;
	
	/**
	 * 복수의 ADC를 삭제한다. 
	 * @param adcIndexList : 삭제할 ADC의 인덱스 목록. null 불가 
	 * @param extraInfo : 부가적인 정보. accountIndex, 부가적인 설명을 위한 message가 전달된다. null 불가.
	 * @throws OBException
	 */
	public void delAdcInfoList(ArrayList<Integer> adcIndexList, OBDtoExtraInfo extraInfo) throws OBException;

	/**
	 * ADC 정보를 수정한다.
	 * @param adcInfo : 수정할 ADC 상세정보. null 불가
	 * @param extraInfo : 부가적인 정보. accountIndex, 부가적인 설명을 위한 message가 전달된다. null 불가.
	 * @throws OBException
	 */
	public void setAdcInfo(OBDtoAdcInfo adcInfo, OBDtoExtraInfo extraInfo, int isReachable) throws OBException;
	//public void setAdcInfo(OBDtoAdcInfo adcInfo, OBDtoExtraInfo extraInfo) throws OBException;

	/**
	 * ADC 그룹의 목록을 가져온다. 그룹 인덱스(groupIndex)를 지정하면 특정 그룹 정보를 가져오고, 그룹 인덱스를 지정하지 않으면 전체 그룹 정보 목록을 가져온다.
	 * 
	 * @param groupIndex : 그룹 인덱스. null 가능
	 * @return
	 *  - 그룹 정보 목록
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcGroup> getAdcGroupList(Integer groupIndex) throws OBException;

	/**
	 * 
	 * @param accountIndex
	 * @param adcGroupIndex
	 * @param searchKey
	 * @param orderType. ORDER_TYPE_FIRST(상태), ORDER_TYPE_SECOND(ADC이름), ORDER_TYPE_THIRD(종류), ORDER_TYPE_FOURTH(IP), ORDER_TYPE_FIFTH(최근업데이트), ORDER_TYPE_SIXTH(버전), ORDER_TYPE_SEVENTH(설명)
	 * @param orderDir. ORDER_DIR_DESCEND, ORDER_DIR_ASCEND
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcGroup> getAdcGroupListByAccount(Integer accountIndex, Integer adcGroupIndex, String searchKey, Integer orderType, Integer orderDir, Integer adcListPageOption) throws OBException;
	/**
	 * 새 ADC 그룹을 추가한다.
	 * 
	 * @param adcGroup : 추가할 ADC 그룹 정보. null 불가
	 * @param extraInfo : 부가적인 정보. accountIndex, 부가적인 설명을 위한 message가 전달된다. null 불가.
	 * @throws OBException
	 */
	public void addAdcGroup(OBDtoAdcGroup adcGroup, OBDtoExtraInfo extraInfo) throws OBException;

	/**
	 * 복수의 ADC 그룹을 삭제한다.
	 * 
	 * @param groupIndexList : 삭제할 ADC 그룹 인덱스 목록. null 불가
	 * @param extraInfo : 부가적인 정보. accountIndex, 부가적인 설명을 위한 message가 전달된다. null 불가.
	 * @throws OBException
	 */
	public void delAdcGroupList(ArrayList<Integer> groupIndexList, OBDtoExtraInfo extraInfo) throws OBException;

	/**
	 * ADC 그룹의 정보를 수정한다.
	 *  
	 * @param adcGroup : 수정할 ADC 그룹 상세정보. null 불가
	 * @param extraInfo : 부가적인 정보. accountIndex, 부가적인 설명을 위한 message가 전달된다. null 불가.
	 * @throws OBException
	 */
	public void setAdcGroup(OBDtoAdcGroup adcGroup, OBDtoExtraInfo extraInfo) throws OBException;
	
	/**
	 * ADC에 할당되지 않은 사용자 목록을 가져온다. searchKey가 유효한 값이면 searchKey로 필터링한 목록을 가져온다. 
	 * SystemAdmin이 아닌 일반 사용자이면서 그 ADC에 이미 할당되지 않은 사용자의 목록을 가져온다. 
	 * (SystemAdmin은 모든 장비에 액세스 권한을 갖고 있으므로 할당할 필요가 없다.) 
	 *  
	 * @param adcIndex : ADC 인덱스
	 * @param searchKey : ADC에 할당되지 않은 사용자 목록에서 검색하고자 하는 사용자ID 문자열. null 이면 할당되지 않은 사용자 전체 목록을 가져온다.
	 * @return 
	 *  - searchKey 유효값 : ADC에 할당되지 않은 사용자 중 id가 searchKey를 포함하는 사용자들의 목록
	 *  - searchKey null : ADC에 할당되지 않은 사용자 전체 목록 
	 * @throws OBException
	 */
	public ArrayList<OBDtoAccount> getTrivialAccountList(Integer adcIndex, String searchKey) throws OBException;
	
	/**
	 * 지정된 계정에 할당된 ADC 장비에서 발생된 ADC 로그(syslog)를 조회한다.
	 *  
	 * @param adcIndex : adc 장비의 index, null일 경우에는 지정된 계정에 할당된 모든 장비를 대상으로 조회한다.
	 * @param searchKeys : 검색 키워드. ADC 장비 이름, IP, 상세내용을 대상으로 검색한다. null일 경우에는 검색 조건 없음.
	 * @param beginTime : 검색 시작 시각. null일 경우에는 전체를 대상.
	 * @param endTime : 검색 종료 시각. null일 경우에는 현재 시각. beginTime보다 같거나 이후 시각이어야 한다.
	 * @param recordCount : 데이터 레코드 개수. null일 경우에는 기본적으로 20개. 0일 경우에는 모든 항목.
	 * @return ArrayList<OBDtoAuditLogAdcSystem>
	 * @throws OBException
	 */
	public ArrayList<OBDtoAuditLogAdcSystem> getAdcAuditLog(Integer adcIndex, String searchKeys, Date beginTime, Date endTime, Integer recordCount) throws OBException;

	/**
	 * 지정된 계정에 할당된 ADC 장비에서 발생된 ADC 로그(syslog)를 출력한다. orderOption 없음
	 *  
	 * @param adcObject
	 * @param searchOption
	 * @param accountIndex	 
	 * @return ArrayList<OBDtoAuditLogAdcSystem>
	 * @throws OBException
	 */
	public ArrayList<OBDtoAuditLogAdcSystem> getAdcAuditLogExOrdering(OBDtoADCObject adcObject, OBDtoSearch searchOption, OBDtoAdcLogSearchOption selectOption, Integer accountIndex) throws OBException;

	/**
	 * 지정된 계정에 할당된 ADC 장비에서 발생된 ADC 로그(syslog)를 조회한다.
	 * @param adcObject
	 * @param searchOption
	 * @param orderOption
	 * @param accountIndex
	 * @return ArrayList<OBDtoAuditLogAdcSystem>
	 * @throws OBException
	 */
	public ArrayList<OBDtoAuditLogAdcSystem> getAdcAuditLog(OBDtoADCObject adcObject, OBDtoSearch searchOption, OBDtoOrdering orderOption, OBDtoAdcLogSearchOption selectOption, Integer accountIndex) throws OBException;


	/**
	 * 지정된 장비의 syslog 개수를 리턴한다.
	 * @param searchKeys : 검색 키워드. ADC 장비 이름, IP, 상세내용을 대상으로 검색한다. null일 경우에는 검색 조건 없음.
	 * @param beginTime : 검색 시작 시각. null일 경우에는 전체를 대상.
	 * @param endTime : 검색 종료 시각. null일 경우에는 현재 시각. beginTime보다 같거나 이후 시각이어야 한다.
	 * @return Integer
	 * @throws OBException
	 */
	public Integer getAdcAuditLogCount(OBDtoADCObject adcObject, OBDtoSearch searchOption, OBDtoAdcLogSearchOption selectOption, Integer accountIndex) throws OBException;
	
	/**
	 * ADC 설정관련 감사로그를 조회한다.
	 * 
	 * @param searchKeys : 검색 키워드. 접속 IP, 사용자, 상세내용을 대상으로 검색한다. null일 경우에는 검색 조건 없음.
	 * @param beginTime : 검색 시작 시각. null일 경우에는 전체를 대상.
	 * @param endTime : 검색 종료 시각. null일 경우에는 현재 시각. beginTime보다 같거나 이후 시각이어야 한다.
	 * @param beginIndex : 제공되는 데이터의 시작 index. null일 경우에는 0으로 간주한다. fromIndex는 항상 endIndex보다 작거나 같아야 한다.
	 * @param endIndex : 제공되는 데이터의 종료 index. null일 경우에는 전체 데이터를 제공한다. 
	 * @return Integer
	 * @throws OBException
	 */
	public ArrayList<OBDtoAuditLogAdcConfig> getSystemAuditLog(String searchKeys, Date beginTime, Date endTime, Integer beginIndex, Integer endIndex) throws OBException;
	
	/**
	 * 
	 * @param searchKeys
	 * @param beginTime
	 * @param endTime
	 * @param beginIndex
	 * @param endIndex
	 * @param orderType. OBDefine.ORDER_TYPE_TYPE, OBDefine.ORDER_TYPE_ADCNAME, OBDefine.ORDER_TYPE_CONTENT, OBDefine.ORDER_TYPE_OCCURTIME
	 * @param orderDir. OBDefine.ORDER_DIR_ASCEND, OBDefine.ORDER_DIR_DESCEND
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoAuditLogAdcConfig> getSystemAuditLog(String searchKeys, Date beginTime, Date endTime, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;

	/**
	 * 제품에서 발생하는 감사로그의 개수를 리턴한다.
	 * @param searchKeys
	 * @param beginTime : 검색 시작 시각. null일 경우에는 전체를 대상.
	 * @param endTime : 검색 종료 시각. null일 경우에는 현재 시각. beginTime보다 같거나 이후 시각이어야 한다.
	 * @return Intgeger
	 * @throws OBException
	 */
	public Integer getSystemAuditLogCount(String searchKeys, Date beginTime, Date endTime) throws OBException;
	
	public void setAdcGroupInfo(Integer index, OBDtoAdcGroup groupInfo, OBDtoExtraInfo extraInfo) throws OBException;
	
//	/**
//	 * 지정된 ADC의 SLB 설정 정보를 다운로드 한다.
//	 * @param adcIndex
//	 */
//	public boolean downloadSlbConfig(Integer adcIndex) throws OBException;
	
	/**
     * 지정된 ADC의 SLB 설정 정보를 다운로드 한다.
     * @param adcIndex
     */
    public OBDtoSLBUpdateStatus downloadSlbConfig(Integer adcIndex) throws OBException;
	
	/**
	 * 등록된 ADC 개수를 리턴한다.
	 * @throws OBException
	 */
	public Integer getUsedAdcCnt() throws OBException;
	
	/**
	 * 등록된 VS 개수를 리턴한다.
	 * @throws OBException
	 */
	public Integer getUsedVSCnt() throws OBException;
	
	/**
	 * 등록된 User 개수를 리턴한다.
	 * @throws OBException
	 */
	public Integer getUsedUserCnt() throws OBException;

	/**
	 * ADC에 설정되어 있는 정보를 추출하여 제공한다. 설정 정보는 adcsmart 구동을 위한 정보를 말한다.
	 *  
	 * @param adcIndex
	 * @return
	 * @throws OBException
	 */
	
	public OBAdcCfgInfo getAdcConfigInfo(Integer adcIndex) throws OBException;
	/**
	 * 전체 설정, 동작 상태 점검 - ADC에 설정 되어 있는 정보를 추출 하여 설정, 동작 상태 점검 결과를 제공한다.
	 * @param adcIndex
	 * @return
	 * @throws OBException
	 */
	public OBAdcCfgInfo getAllConfigInfo(Integer adcIndex) throws OBException;

	/**
	 * ADC설정 점검의 결과가 실패 일 경우 설정을 성공으로 바꾼다.
	 * @param adcIndex
	 * @param configType
	 * @throws OBException
	 */
	public void configFaill(Integer adcIndex, String configType) throws OBException;

	/**
	 * adcsmart에서 ADC를 사용하기 위해 설정된 값이 정상적으로 설정되어 있는지 검사한다.
	 * @param adcIndex
	 * @return
	 * @throws OBException
	 */
	public OBAdcCfgInfo checkAdcConfigState(Integer adcIndex) throws OBException;
	
	
	/**
	 * ADC 설정을 검사한다.  현재 DB에 저장된 상태와 ADC에 접속해서 추출한 값을 제공한다.
	 * @param adcIndex
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBAdcConfigInfo> checkAdcConfigInfo(Integer adcIndex) throws OBException;
	
	/**
	 * 지정된 항목을 ADC에 설정한다.
	 * @param adcIndex
	 * @param adcInfo. configID, adcInfo에 정보만 입력 후 전달한다. 
	 * @return
	 * @throws OBException
	 */
	public boolean setAdcConfigInfo(Integer adcIndex, OBAdcConfigInfo adcInfo) throws OBException;
	
	/**
	 * ADC 연결 확인을 진행한다. 모든 항목을 진행한 후 리턴한다.
	 * @param adcInfo
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBAdcCheckResult> checkADCStatusAll(Integer adcIndex) throws OBException;
	
	/**
	 * ADC 검사(수신) 최종 시간을 제공한다.
	 * @param adcIndex
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoLastAdcCheckTime> getLastAdcCheckTime(Integer adcIndex) throws OBException;
	
	
	/**
	 * 등록된 ADC 장비의 이름 목록을 제공한다.
	 * @return
	 * @throws OBException
	 */
	public ArrayList<String> getAdcNameList() throws OBException;
	
	/**
     * 
     * @param adcIndex
     * @return
     * @throws OBException
     */
    public ArrayList<OBDtoAdcNoticeGroup> getNoticeGrp(Integer adcIndex) throws OBException;
    
	/**
     * 공지그룹을 지정한다.
     * @param adcIndex
     * @param noticeGroupList
     * @param extraInfo
     * @throws OBException
     */
    public void setNoticeGroup(Integer adcIndex, ArrayList<OBDtoAdcNoticeGroup> noticeGroupList, Integer accntIndex, OBDtoExtraInfo extraInfo) throws OBException;
    
    public boolean isNoticePoolUsed(String poolIndex) throws OBException;
    
    /**
     * ADC 연결 상태를 업데이트한다. 연결된 상태로 업데이트. 
     * @param adcIndex
     * @return
     * @throws OBException
     */
    public void updateAdcStatusReachable(Integer adcIndex) throws OBException;
    
    /**
     * ADC 연결 상태를 업데이트한다. 연결되지 않는 상태로 업데이트. 
     * @param adcIndex
     * @return
     * @throws OBException
     */
    public void updateAdcStatusUnReachable(Integer adcIndex) throws OBException;
    
    /**
     * ADC 연결 확인을 진행한다. 네트워크 접근, snmp만 확인한 후 진행한다. 
     * @param adcIndex
     * @return
     * @throws OBException
     */
    public boolean checkADCStatusMonitoring(Integer adcIndex) throws OBException;
    
    /**
     * snmp 장비 정보를 확인한다.
     * @param adcIndex
     * @return
     * @throws OBException
     */
    public boolean snmpInfoCheck(Integer adcIndex) throws OBException;
    /**
     * 구간 응답시간 체크를 설정한다.
     * @param respInfo
     * @return
     * @throws OBException
     */
    public boolean addRespSectionCheck(OBDtoRespGroup respInfo) throws OBException;
    /**
     * 구간 응답시간 체크를 변경한다.
     * @param respInfo
     * @return
     * @throws OBException
     */
    public boolean setRespSectionCheck(OBDtoRespGroup respInfo) throws OBException;
    /**
     * 구간 응답시간 체크를 삭제한다.
     * @param respIndex
     * @return
     * @throws OBException
     */
    public void delRespSectionCheck(ArrayList<String> respIndex) throws OBException;
    /**
     * 구간 응답시간 체크를 설정을 조회한다.
     * @param respIndex
     * @return
     * @throws OBException
     */
    public boolean isExistRespSectionCheck(String respName) throws OBException;
    /**
     * 구간 응답시간 체크를 설정을 조회한다.
     * @param respIndex
     * @return
     * @throws OBException
     */
    public OBDtoRespGroup getRespSectionCheck(Integer respIndex) throws OBException;
    /**
     * 구간 응답시간 리스트를 불러 온다.
     * @param orderType
     * @param orderDir
     * @return
     * @throws OBException
     */
    public ArrayList<OBDtoRespGroup> getResponseTimeList(OBDtoSearch searchOption, Integer orderType, Integer orderDir) throws OBException;
    /**
     * 꾸간 응답시간 Chart
     * @param respIndex
     * @param searchOption
     * @return
     * @throws OBException
     */
    public ArrayList<OBDtoRespMultiChartData> getResponseTimeHistory(Integer respIndex, OBDtoSearch searchOption) throws OBException;
    /**
     * VS서비스 그룹을 추가한다.
     * @param vsGroupInfo
     * @return
     * @throws OBException
     */    
    public boolean addVSServiceGroup(OBDtoVSGroupInfo vsGroupInfo) throws OBException;
    /**
     * VS서비스 그룹 설정을 변경한다.
     * @param vsGroupInfo
     * @return
     * @throws OBException
     */
    public boolean setVSServiceGroup(OBDtoVSGroupInfo vsGroupInfo) throws OBException;
    /**
     * VS서비스 그룹 설정을 삭제한다.
     * @param vsGroupName
     * @return
     * @throws OBException
     */
    public void delVSServiceGroup(ArrayList<Integer> groupIndexList) throws OBException;
    /**
     * VS서비스 그룹 이름이 존재하는지 확인한다.
     * @param vsGroupName
     * @return
     * @throws OBException
     */
    public boolean isExistVSServiceGroup(String vsGroupName) throws OBException;
    /**
     * VS서비스 그룹에 대한 정보를 조회 한다.
     * @param vsGroupIndex
     * @return
     * @throws OBException
     */
    public OBDtoVSGroupInfo getVSServiceGroup(Integer vsGroupIndex, Integer accountIndex) throws OBException;
    /**
     * 모든 VS서비스 그룹 대한 정보를 조회 한다.
     * @param
     * @return
     * @throws OBException
     */
    public ArrayList<OBDtoVSGroupInfo> getVSServiceGroup(String searchKey,
            Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;
    /**
     * 모든 VS서비스 그룹 대한 정보를 조회 한다.
     * @param
     * @return
     * @throws OBException
     */
    public ArrayList<OBDtoADCGroupInfo> getVSServiceGroupAll(Integer vsGroupIndex,
            Integer accountIndex) throws OBException;
    /**
     * 전체 그룹의 Total Count 
     * @param
     * @return
     * @throws OBException
     */
    public Integer getVSServiceGroupTotalCount(String searchKey) throws OBException;
    /**
     * slbUser 핸드폰번호가 있는지 검사 한다.
     *  
     * @param slbUser : Slb User. null 불가
     * @return
     *  - true: slbUser PHONE 번호가 있음
     *  - false: slbUser PHONE 번호가 없음
     * @throws OBException
     */
    public boolean isExistSlbUserCheck(OBDtoSlbUser slbUser) throws OBException;
    /**
     * slbUser를 추가 한다.
     *  
     * @param slbUser : Slb User 리스트. null 불가
     * @return
     *  - true: Slb User를 정상 추가명려
     *  - false: Slb User를 추가에 실패
     * @throws OBException
     */
    public void addSlbUser(OBDtoSlbUser slbUser) throws OBException;
    /**
     * slbUser를 수정 한다.
     *  
     * @param slbUser : Slb User. null 불가
     * @return
     *  - true: Slb User를 정상 수정
     *  - false: Slb User를 수정에 실패
     * @throws OBException
     */
    public void setSlbUser(OBDtoSlbUser slbUser) throws OBException;
    /**
     * slbUser를 삭제 한다.
     *  
     * @param slbUser : Slb User 리스트. null 불가
     * @return
     *  - true: Slb User를 정상 삭제
     *  - false: Slb User를 삭제에 실패
     * @throws OBException
     */
    public void delSlbUser(ArrayList<Integer> slbUserIndex) throws OBException;
    /**
     * slbUser를 검색 한다.
     *  
     * @param begeinindex
     * @param endIndex
     * @param orderType
     * @param orderDir
     * @return
     *  - User 리스트
     * @throws OBException
     */
    public OBDtoSlbUser getSlbUser(Integer slbUserIndex) throws OBException;
    
    public OBDtoSlbUser getLastRespUserInfo() throws OBException;
    
    public Integer getSlbUserListCount(Integer userType, Integer accntIndex, String searchKey) throws OBException;
    
    public ArrayList<OBDtoSlbUser> getSlbUserList(Integer userType, Integer beginIndex,
            Integer endIndex, Integer orderType, Integer orderDir)
            throws OBException;
    /**
     * SLB스케쥴이 동일 시간대에 중복 작업인지 검사 한다.
     *  
     * @param adcSchedule : adcSchedule 중복 검사. null 불가
     * @return
     *  - true: SlbSchedule 동일 시간대에 작업이 있음
     *  - false: SlbSchedule 동일 시간대에 작업이 없음
     * @throws OBException
     */
    public boolean isExistSlbSchedule(OBDtoAdcSchedule adcSchedule) throws OBException;
    /**
     * SLB스케쥴 예약을 추가 한다.
     *  
     * @param adcSchedule : adcSchedule. null 불가
     * @param OBDtoAdcVServerF5 : F5config.
     * @param OBDtoAdcVServerAlteon : alteonConfig.
     * @return
     * @throws OBException
     */
    public void addSlbSchedule(OBDtoSlbUser slbUser, OBDtoAdcSchedule slbSchedule, OBDtoAdcVServerF5 configF5, OBDtoAdcVServerAlteon configAlteon) throws OBException;
    /**
     * SLB스케쥴 예약을 수정 한다.
     *  
     * @param adcSchedule : adcSchedule. null 불가
     * @return
     *  - true: Slb adcSchedule를 정상 수정
     *  - false: Slb adcSchedule를 수정 실패
     * @throws OBException
     */
    public void setSlbSchedule(OBDtoAdcSchedule slbSchedule, OBDtoAdcVServerF5 configF5, OBDtoAdcVServerAlteon configAlteon) throws OBException;
    /**
     * SLB스케쥴 예약을 삭제 한다.
     *  
     * @param adcScheduleIndex : adcSchedule Index 리스트. null 불가
     * @return
     * @throws OBException
     */
    public void delSlbSchedule(ArrayList<Integer> adcScheduleIndex) throws OBException;
    /**
     * SLB스케쥴 예약을 모두 불러온다.
     *  
     * @param searchKey
     * @param begeinindex
     * @param endIndex
     * @param orderType
     * @param orderDir
     * @return
     * @throws OBException
     */
    
    public void addMessageToSMS(String phone) throws OBException;
    
    public Integer getSlbScheduleListTotal(Integer adcIndex, Integer accntIndex, String searchKey) throws OBException;
    
    public ArrayList<OBDtoAdcSchedule> getSlbScheduleList(Integer adcIndex, String searchKey,
            Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;
    
    public OBDtoAdcSchedule getSlbSchedule(Integer scheduleIndex) throws OBException;
}
