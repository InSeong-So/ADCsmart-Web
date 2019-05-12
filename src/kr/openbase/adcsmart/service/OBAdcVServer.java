package kr.openbase.adcsmart.service;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcHealthCheckAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeDetail;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcProfile;
import kr.openbase.adcsmart.service.dto.OBDtoAdcRealServerGroup;
import kr.openbase.adcsmart.service.dto.OBDtoAdcScope;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerNotice;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoNetworkInterface;
import kr.openbase.adcsmart.service.dto.OBDtoSLBUpdateStatus;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcVServerAll;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcVlan;
import kr.openbase.adcsmart.service.impl.f5.DtoVlanTunnelFilter;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcNodePAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcVServerPAS;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcHealthCheckPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcNodePASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcVServerPASK;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBLicenseExpiredException;

/**
 * @author Rock
 *
 */
public interface OBAdcVServer
{
	/**
	 * 설정된 SLB의 status를 업데이트한다.
	 * 
	 * @param adcIndex : 장비 index, null 불가.
	 * @return 
	 * @throws OBException
	 */
	public OBDtoSLBUpdateStatus updateSLBStatus(Integer adcIndex) throws OBException;
	
	/**
	 * 이중화 설정이 정상적인지 테스트한다. 
	 *  
	 * @param ipaddress : null 불가
	 * @param accountID : null 불가
	 * @param password : null 불가
	 * @param swVersion : null 가능 
	 * @param vsIP : null 불가
	 * @param routerIndex : null 불가
	 * @param vrIndex : null 불가
	 * @param ifNum : null 불가
	 * @return  boolean
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
	public boolean checkVrrp(String ipaddress, String accountID, String password, String swVersion, String vsIP, Integer routerIndex, Integer vrIndex, Integer ifNum) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	
	/**
	 * VRRP 설정을 위한 interface 목록을 추출한다.
	 * 
	 * @param adcIndex : null 불가
	 * @return ArrayList<OBDtoNetworkInterface>
	 * @throws OBException
	 */
	public ArrayList<OBDtoNetworkInterface> getL3InterfaceList(Integer adcIndex) throws OBException;
	
	/**
	 * 지정된 adc 장비에 할당된 Virtual server 목록을 제공한다.
	 * @param adcIndex : ADC 장비 index. null 불가.
	 * @return Integer
	 * @throws OBException
	 */
//	public Integer getVSTotalCount(Integer adcIndex) throws OBException;
	
	/**
	 * VirtualServer 목록을 가져온다. 
	 * Alteon ADC용
	 * @param adcIndex : ADC 인덱스. null 불가
	 * @return ArrayList<OBDtoAdcVServerAlteon>
	 * @return ArrayList<OBDtoAdcVServerF5>
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcVServerAlteon> getVServerListAlteon(Integer adcIndex) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	public ArrayList<OBDtoAdcVServerF5> getVServerListF5(Integer adcIndex) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	public ArrayList<OBDtoAdcVServerPAS> getVServerListPAS(Integer adcIndex) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	public ArrayList<OBDtoAdcVServerPASK> getVServerListPASK(Integer adcIndex) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	/**
	 * 지정된 장비의 virtual server 목록을 추출한다.
	 * @param adcIndex : ADC 인덱스. null 불가
	 * @param beginIndex : 제공되는 데이터의 시작 index. null일 경우에는 0으로 간주한다. fromIndex는 항상 endIndex보다 작거나 같아야 한다.
	 * @param endIndex : 제공되는 데이터의 종료 index. null일 경우에는 전체 데이터를 제공한다. 
	 * @return ArrayList<OBDtoAdcVServerAlteon>
	 * @return ArrayList<OBDtoAdcVServerF5>
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
//	public ArrayList<OBDtoAdcVServerAlteon> getVServerListAlteon(Integer adcIndex, Integer beginIndex, Integer endIndex) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
//	public ArrayList<OBDtoAdcVServerF5> getVServerListF5(Integer adcIndex, Integer beginIndex, Integer endIndex) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
//	public ArrayList<OBDtoAdcVServerPAS> getVServerListPAS(Integer adcIndex, Integer beginIndex, Integer endIndex) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
//	public ArrayList<OBDtoAdcVServerPASK> getVServerListPASK(Integer adcIndex, Integer beginIndex, Integer endIndex) throws OBExceptionUnreachable, OBExceptionLogin, OBException;

	/**
	 * 
	 * @param adcIndex
	 * @param beginIndex
	 * @param endIndex
	 * @param orderType
	 * @param orderDir. OBDefine.ORDER_DIR_ASCEND, OBDefine.ORDER_DIR_DESCEND
	 * @return
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcVServerAlteon> getVServerListAlteon(Integer adcIndex, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	
	/**
	 * 
	 * @param adcIndex
	 * @param beginIndex
	 * @param endIndex
	 * @param orderType
	 * @param orderDir. OBDefine.ORDER_DIR_ASCEND, OBDefine.ORDER_DIR_DESCEND
	 * @return
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcVServerF5> getVServerListF5(Integer adcIndex, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	
	/**
	 * 
	 * @param adcIndex
	 * @param beginIndex
	 * @param endIndex
	 * @param orderType
	 * @param orderDir. OBDefine.ORDER_DIR_ASCEND, OBDefine.ORDER_DIR_DESCEND
	 * @return
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcVServerPAS> getVServerListPAS(Integer adcIndex, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	
	/**
	 * 
	 * @param adcIndex
	 * @param beginIndex
	 * @param endIndex
	 * @param orderType
	 * @param orderDir. OBDefine.ORDER_DIR_ASCEND, OBDefine.ORDER_DIR_DESCEND
	 * @return
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcVServerPASK> getVServerListPASK(Integer adcIndex, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	/**
	 * SLB 설정을 ADC에서 가져와 DB를 업데이트한다.
	 * 
	 * @param adcIndex : ADC 인덱스. null 불가
	 * 
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
//	public boolean downloadSlbConfig(Integer adcIndex, boolean force) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	
	public OBDtoSLBUpdateStatus downloadSlbConfig(Integer adcIndex, boolean isforce) throws OBExceptionUnreachable, OBExceptionLogin, OBException;

	/**
	 * VirtualServer의 상세정보를 가져온다.
	 * Alteon ADC용 
	 * @param adcIndex : ADC 인덱스. null 불가
	 * @param vsIndex : VirtualServer 인덱스. null 불가
	 * @return
	 *  - VirtualServer 상세정보
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
	public OBDtoAdcVServerAlteon getVServerAlteonInfo(Integer adcIndex, String vsIndex) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	public OBDtoAdcVServerF5 getVServerF5Info(Integer adcIndex, String vsIndex) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	public OBDtoAdcVServerPAS getVServerPASInfo(Integer adcIndex, String vsIndex) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	public OBDtoAdcVServerPASK getVServerPASKInfo(Integer adcIndex, String vsIndex) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
//	/**
//	 * 지정된 버추얼 서버에 속한 Virtual service(pool/member) 정보를 추출한다.
//	 * 
//	 * @param adcIndex
//	 * @param vsName
//	 * @return ArrayList<OBDtoAdcVService>
//	 * @throws OBException
//	 */
//	public ArrayList<OBDtoAdcVService> getVServiceList(Integer adcIndex, String vsName) throws OBException;
    /**
     * timeSync Check
     * 
     * @param adcIndex : ADC 인덱스. null 불가
     * @return true, false
     */
    public boolean timeSyncCheck(Integer adcIndex) throws OBException;	
	/**
	 * 복수의 VirtualServer 상태를 Enabled로 설정한다.
	 * 
	 * @param adcIndex : ADC 인덱스. null 불가
	 * @param vsIndexList : 상태를 변경할 VirtualServer들의 인덱스 리스트. null 불가
	 * @param extraInfo : 부가적인 정보. accountIndex, 부가적인 설명을 위한 message가 전달된다. null 불가.
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
	public void enableVServer(Integer adcIndex, ArrayList<String> vsIndexList, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	
	/**
	 * 복수의 VirtualServer 상태를 Disabled로 설정한다.
	 * 
	 * @param adcIndex : ADC 인덱스. null 불가
	 * @param vsIndexList : 상태를 변경할 VirtualServer들의 인덱스 리스트. null 불가
	 * @param extraInfo : 부가적인 정보. accountIndex, 부가적인 설명을 위한 message가 전달된다. null 불가.
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
	public void disableVServer(Integer adcIndex, ArrayList<String> vsIndexList, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	
	/**
	 * 새 VirtualServer를 추가한다.
	 * Alteon ADC용
	 * @param virtualServer : 추가할 VirtualServer. null 불가
	 * @param extraInfo : 부가적인 정보. accountIndex, 부가적인 설명을 위한 message가 전달된다. null 불가.
	 * @throws OBLicenseExpiredException
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
	public void addVServerAlteon(OBDtoAdcVServerAlteon virtualServer, OBDtoExtraInfo extraInfo) throws OBLicenseExpiredException, OBExceptionUnreachable, OBExceptionLogin, OBException;
	public void addVServerF5(OBDtoAdcVServerF5 virtualServer, OBDtoExtraInfo extraInfo) throws OBLicenseExpiredException, OBExceptionUnreachable, OBExceptionLogin, OBException;
	public void addVServerPAS(OBDtoAdcVServerPAS virtualServer, OBDtoExtraInfo extraInfo) throws OBLicenseExpiredException, OBExceptionUnreachable, OBExceptionLogin, OBException;
	public void addVServerPASK(OBDtoAdcVServerPASK virtualServer, OBDtoExtraInfo extraInfo) throws OBLicenseExpiredException, OBExceptionUnreachable, OBExceptionLogin, OBException;

	/**
	 * VirtualServer 설정을 변경한다.
	 * Alteon ADC 용
	 * @param virtualServer : 수정할 VirtualServer 상세정보. null 불가 
	 * @param extraInfo : 부가적인 정보. accountIndex, 부가적인 설명을 위한 message가 전달된다. null 불가.
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
	public void setVServerAlteon(OBDtoAdcVServerAlteon virtualServer, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	public void setVServerF5(OBDtoAdcVServerF5 virtualServer, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	public void setVServerPAS(OBDtoAdcVServerPAS virtualServer, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	public void setVServerPASK(OBDtoAdcVServerPASK virtualServer, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	/**
	 * VirtualServer 설정을 변경한다.
	 * F5 ADC 용
	 * @param virtualServer : 수정할 VirtualServer 상세정보. null 불가
	 * @param extraInfo : 부가적인 정보. accountIndex, 부가적인 설명을 위한 message가 전달된다. null 불가.
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
	
	/**
	 * 복수의 VirtualServer를 삭제한다.
	 *  
	 * @param adcIndex : ADC 인덱스. null 불가
	 * @param vsIndexList : 삭제할 VirtualServer 인덱스 목록. null 불가
	 * @param extraInfo : 부가적인 정보. accountIndex, 부가적인 설명을 위한 message가 전달된다. null 불가.
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
	public void delVServer(Integer adcIndex, ArrayList<String> vsIndexList, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	
	/**
	 * ADC 장비의 pool 목록 전체를 가져온다.
	 * @param adcIndex : ADC 인덱스. null 불가
	 * @return ArrayList<OBDtoAdcPoolAlteon>
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcPoolAlteon> getPoolAlteonList(Integer adcIndex) throws OBException;
	public ArrayList<OBDtoAdcPoolF5> getPoolF5List(Integer adcIndex) throws OBException;
	public ArrayList<OBDtoAdcPoolPAS> getPoolPASList(Integer adcIndex) throws OBException; //TODO 필요한가? 확인!
	public ArrayList<OBDtoAdcPoolPASK> getPoolPASKList(Integer adcIndex) throws OBException;
	
    /**
     * Virtual Server를 동기화한다. - F5 전용
     * @param virtualServerNew
     * @param extraInfo
     * @throws OBExceptionUnreachable
     * @throws OBExceptionLogin
     * @throws OBException
     */
    public void relashVServerF5(OBDtoAdcVServerF5 virtualServerNew, OBDtoExtraInfo extraInfo)
            throws OBExceptionUnreachable, OBExceptionLogin, OBException;

	/**
	 * 특정 pool의 상세 정보를 가져온다. 
	 * @param poolIndex : pool index 정보.  null 불가 
	 * @return
	 *  - pool의 상세정보
	 * @throws OBException
	 */
	public OBDtoAdcPoolAlteon getPoolAlteon(String poolIndex) throws OBException;
	public OBDtoAdcPoolF5 getPoolF5(String poolIndex) throws OBException;
	public OBDtoAdcPoolPAS getPoolPAS(String poolIndex) throws OBException; //TODO:필요?
	public OBDtoAdcPoolPASK getPoolPASK(String poolIndex) throws OBException;
	
	public OBDtoAdcHealthCheckPASK getHealthCheckPASK(String healthcheckIndex) throws OBException;

	/**
	 * 특정 pool에 할당 할 수 있는 node 목록을 가져온다.
	 * @param adcIndex : ADC 인덱스. null 불가
	 * @param poolIndex : pool index 정보.  null 불가 
	 *  - node 할당 작업을 할(하고 있는) pool index. 
	 *  - null 가능. 신규 pool 작업일 경우 null로 호출하고, 그러면 전체 node 목록을 가져온다. 
	 * @return
	 *  - poolId 유효값 : pool에 할당 할 수 있는 node의 목록
	 *  - poolId null : 전체 node의 목록
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcNodeAlteon> getNodeAvailableListAlteon(Integer adcIndex, String poolIndex) throws OBException;
	public ArrayList<OBDtoAdcNodeF5> getNodeAvailableListF5(Integer adcIndex) throws OBException;
	public ArrayList<OBDtoAdcNodePAS> getNodeAvailableListPAS(Integer adcIndex, String poolIndex) throws OBException;
	public ArrayList<OBDtoAdcNodePASK> getNodeAvailableListPASK(Integer adcIndex, String poolIndex) throws OBException; //TODO
	/**
	 * VirtualServer에 지정하기 위해 persistence profile 전체 목록을 가져온다. 
	 * F5 ADC 전용. Alteon에는 기능 없음
	 * @param adcIndex
	 * @param orderType. ORDER_TYPE_FIRST(Profile 이름), ORDER_TYPE_SECOND(Timeout), ORDER_TYPE_THIRD(Match Across Service)
	 * @param orderDir. ORDER_DIR_DESCEND, ORDER_DIR_ASCEND
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcProfile> getProfileList(Integer adcIndex, Integer orderType, Integer orderDir) throws OBException;

	/**
	 * Piolink PASK, ALTEON ADC의 health-check 목록 전체를 가져온다.
	 * @param adcIndex : ADC 인덱스. null 불가
	 * @return ArrayList<OBDtoAdcHealthCheckPASK>
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcHealthCheckPASK> getHealthCheckListPASK(Integer adcIndex) throws OBException;
	public ArrayList<OBDtoAdcHealthCheckAlteon> getHealthCheckListAlteon(Integer adcIndex) throws OBException;
	
	/**
	 * VirtualServer 중복을 확인한다. 이름과 IP로 중복을 식별한다.
	 * Alteon ADC 전용
	 * 
	 * @param adcIndex : ADC 인덱스. null 불가
	 * @param port : alteon에서는  사용하지 않음
	 * @param alteonID : 확인할 VirtualServer alteonID. null 불가. 
	 * @param ipAddress : 확인할 VirtualServer IP. null 불가
	 * @return
	 *  - true : alteonID나 ipAddress가 같은 VirtualServer 있음
	 *  - false : alteonID나 ipAddress를 쓰고 있는 VirtualServer가 없음
	 * @throws OBException
	 */
	public boolean isAvailableVirtualServerAlteon(Integer adcIndex, Integer port, String alteonID, String ipAddress) throws OBException;
	/**
	 * VirtualServer 중복을 확인한다. VirtualServer의 이름, IP, Port 로 중복을 식별한다.
	 * F5 ADC 전용
	 * 
	 * @param adcIndex : ADC 인덱스. null 불가
	 * @param vsName : 확인하려는 VirtualServer 이름. null 불가. 
	 * @param ipAddress : 확인하려는 VirtualServer IP. null 불가
	 * @param port : 확인하려는 VirtualServer Virtual port. null 불가
	 * @param alteonId : F5에서는 사용하지 않음
	 * @return
	 *  - true : VirtualServer 있음
	 *  - false : VirtualServer 없음
	 * @throws OBException
	 */
	public boolean isAvailableVirtualServerF5(Integer adcIndex, String vsName, String ipAddress, Integer port, String alteonId) throws OBException;
	public boolean isAvailableVirtualServerPASK(Integer adcIndex, String vsName, String ipAddress, Integer port) throws OBException;

	/**
	 * VirtualServer 중에서 검색조건(searchKey)에 맞는 목록을 가져온다.
	 * VirtualServer IP와 이름이 검색조건(searchKey)를 포함하는 VirtualServer의 목록을 가져온다.
	 * searchKey가 null이면 전체 VirtualServer목록을 가져온다.
	 * @param adcIndex : ADC 인덱스. null 불가
	 * @param searchKey : VirtualServer 이름/IP에서 검색할 문자열. null 가능
	 * @param beginIndex : null일 경우 0으로 간주.
	 * @param endIndex : null일 경우 모든 항목으로 간주.
	 * @return ArrayList<OBDtoAdcVServerAlteon>
	 * @throws OBException
	 */
//	public ArrayList<OBDtoAdcVServerAlteon> searchVServerListAlteon(Integer adcIndex, String searchKey, Integer beginIndex, Integer endIndex) throws OBException;
//	public ArrayList<OBDtoAdcVServerF5> searchVServerListF5(Integer adcIndex, String searchKey, Integer beginIndex, Integer endIndex) throws OBException;
//	public ArrayList<OBDtoAdcVServerPAS> searchVServerListPAS(Integer adcIndex, String searchKey, Integer beginIndex, Integer endIndex) throws OBException;
//	public ArrayList<OBDtoAdcVServerPASK> searchVServerListPASK(Integer adcIndex, String searchKey, Integer beginIndex, Integer endIndex) throws OBException;
	
	/**
	 * 
	 * @param adcIndex
	 * @param searchKey
	 * @param beginIndex
	 * @param endIndex
	 * @param orderType
	 * @param orderDir. OBDefine.ORDER_DIR_ASCEND, OBDefine.ORDER_DIR_DESCEND
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcVServerAlteon> searchVServerListAlteon(Integer adcIndex, String searchKey, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;
	public ArrayList<OBDtoAdcVServerF5> searchVServerListF5(Integer adcIndex, String searchKey, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;
	public ArrayList<OBDtoAdcVServerPAS> searchVServerListPAS(Integer adcIndex, String searchKey, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;
	public ArrayList<OBDtoAdcVServerPASK> searchVServerListPASK(Integer adcIndex, String searchKey, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;

//	public ArrayList<OBDtoAdcVServerAlteon> searchVServerListAlteon(Integer adcIndex, Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex) throws OBException;
//	public ArrayList<OBDtoAdcVServerF5> searchVServerListF5(Integer adcIndex, Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex) throws OBException;
//	public ArrayList<OBDtoAdcVServerPAS> searchVServerListPAS(Integer adcIndex, Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex) throws OBException;
//	public ArrayList<OBDtoAdcVServerPASK> searchVServerListPASK(Integer adcIndex, Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex) throws OBException;

	/**
	 * 
	 * @param adcIndex
	 * @param accntIndex
	 * @param searchKey
	 * @param beginIndex
	 * @param endIndex
	 * @param orderType. OBDefine.ORDER_TYPE_STATUS, ORDER_TYPE_INDEX, ORDER_TYPE_VSNAME, ORDER_TYPE_VSIPADDRESS, ORDER_TYPE_SERVICEPORT, ORDER_TYPE_OCCURTIME
	 * @param orderDir. OBDefine.ORDER_DIR_ASCEND, OBDefine.ORDER_DIR_DESCEND
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcVServerAlteon> searchVServerListAlteon(Integer adcIndex, Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;
	public ArrayList<OBDtoAdcVServerF5> searchVServerListF5(Integer adcIndex, Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;
	public ArrayList<OBDtoAdcVServerPAS> searchVServerListPAS(Integer adcIndex, Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;
	public ArrayList<OBDtoAdcVServerPASK> searchVServerListPASK(Integer adcIndex, Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;

	public ArrayList<OBDtoAdcVServerAlteon> searchVSListUsedByPoolAlteon(Integer adcIndex, Integer poolID) throws OBException;
	public ArrayList<OBDtoAdcVServerF5> searchVSListUsedByPoolF5(Integer adcIndex, String poolName) throws OBException;
	public ArrayList<OBDtoAdcVServerPAS> searchVSListUsedByPoolPAS(Integer adcIndex, String poolName) throws OBException;
	public ArrayList<OBDtoAdcVServerPASK> searchVSListUsedByPoolPASK(Integer adcIndex, String poolName) throws OBException;
	
	/**
	 * 주어진 장비의 VS 개수를 제공한다.
	 * 
	 * @param adcIndex : ADC 인덱스. null 불가
	 * @param searchKey : VirtualServer 이름/IP에서 검색할 문자열. null 가능
	 * @return Integer
	 * @throws OBException
	 */
	public Integer searchVServerListAlteonCount(Integer adcIndex, String searchKey) throws OBException;
	public Integer searchVServerListF5Count(Integer adcIndex, String searchKey) throws OBException;
	public Integer searchVServerListPASCount(Integer adcIndex, String searchKey) throws OBException;
	public Integer searchVServerListPASKCount(Integer adcIndex, String searchKey) throws OBException;

	public Integer searchVServerListAlteonCount(Integer adcIndex, Integer accntIndex, String searchKey) throws OBException;
	public Integer searchVServerListF5Count(Integer adcIndex, Integer accntIndex, String searchKey) throws OBException;
	public Integer searchVServerListPASCount(Integer adcIndex, Integer accntIndex, String searchKey) throws OBException;
	public Integer searchVServerListPASKCount(Integer adcIndex, Integer accntIndex, String searchKey) throws OBException;
	
	/**
     * All 노드 목록을 구한다. 노드정보와 관련된 virtual server목록을 준다.
     * @param scope
     * @param accntIndex
     * @param searchKey
     * @param beginIndex
     * @param endIndex
     * @param orderType
     * @param orderDir
     * @return
     * @throws OBException
     */
    public ArrayList<OBDtoAdcNodeDetail> searchNodeListAll(OBDtoAdcScope scope, Integer accntIndex, String searchKey, 
            Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;
	/**
	 * F5 노드 목록을 구한다. 노드정보와 관련된 virtual server목록을 준다.
	 * @param adcIndex
	 * @param searchKey
	 * @param beginIndex
	 * @param endIndex
	 * @param orderType
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcNodeDetail> searchNodeListF5(Integer adcIndex, Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;
	
	   /**
     * Alteon 노드 목록을 구한다. 노드정보와 관련된 virtual server목록을 준다.
     * @param adcIndex
     * @param searchKey
     * @param beginIndex
     * @param endIndex
     * @param orderType
     * @param orderDir
     * @return
     * @throws OBException
     */
    public ArrayList<OBDtoAdcNodeDetail> searchNodeListAlteon(Integer adcIndex, Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;
	
	/**
	 * F5 노드 목록을 구한다. 노드정보와 관련된 virtual server목록을 준다.
	 * @param adcIndex
	 * @param searchKey
	 * @param orderType
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcNodeDetail> searchGroupListF5(Integer adcIndex, Integer accntIndex, String searchKey, Integer orderType, Integer orderDir) throws OBException;
	
	   /**
     * Alteon 노드 목록을 구한다. 노드정보와 관련된 virtual server목록을 준다.
     * @param adcIndex
     * @param searchKey
     * @param orderType
     * @param orderDir
     * @return
     * @throws OBException
     */
     public ArrayList<OBDtoAdcNodeDetail> searchGroupListAlteon(Integer adcIndex, Integer accntIndex, String searchKey, Integer orderType, Integer orderDir) throws OBException;
	/**
	 * F5 노드 목록의 노드수를 구한다.
	 * @param adcIndex
	 * @param searchKey
	 * @return
	 * @throws OBException
	 */
	public Integer searchNodeF5ListCount(Integer adcIndex, Integer accntIndex, String searchKey) throws OBException;
	   /**
     * Alteon 노드 목록의 노드수를 구한다.
     * @param adcIndex
     * @param searchKey
     * @return
     * @throws OBException
     */
    public Integer searchNodeAlteonListCount(Integer adcIndex, Integer accntIndex, String searchKey) throws OBException;
    /**
     * All 노드 목록의 노드수를 구한다.
     * @param scope
     * @param searchKey
     * @return
     * @throws OBException
     */
    public Integer searchNodeAllListCount(OBDtoAdcScope scope, Integer accntIndex, String searchKey) throws OBException;
    /**
     * All VS 목록의 노드수를 구한다.
     * @param scope
     * @param searchKey
     * @return
     * @throws OBException
     */
    public Integer searchVServerListAllListCount(OBDtoAdcScope scope, Integer accntIndex, String searchKey) throws OBException;
    
    /**
     * All VS 목록을 구한다. 노드정보와 관련된 virtual server목록을 준다.
     * @param scope
     * @param accntIndex
     * @param searchKey
     * @param beginIndex
     * @param endIndex
     * @param orderType
     * @param orderDir
     * @return
     * @throws OBException
     */
    public ArrayList<OBDtoAdcVServerAll> searchVServerListAll(OBDtoAdcScope scope, Integer accntIndex, String searchKey, 
            Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;

	/**
	 * 복수의 node 상태를 enable/disable/forced offline 설정한다.
	 * 
	 * @param adcIndex : ADC 인덱스. null 불가
	 * @param nodeList : 상태를 변경할 node 정보 - 상태 포함
	 * @param extraInfo : 부가적인 정보. accountIndex, 부가적인 설명을 위한 message가 전달된다. null 불가.
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
    
	public void setNodeState(Integer adcIndex, ArrayList<OBDtoAdcNodeF5> nodeList, Integer action, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	
	/**
	 * 리얼 그룹을 insert, update, delete를 한다.
	 * 
	 * @param adcIndex : ADC 인덱스. null 불가
	 * @param rsGroup : 상태를 변경할 node 정보 - 상태 포함
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
	
	public boolean addRealServerGroup(Integer adcIndex, OBDtoAdcRealServerGroup rsGroup) throws OBException;
	public boolean setRealServerGroup(Integer adcIndex, OBDtoAdcRealServerGroup rsGroup) throws OBException;
	public void delRealServerGroup(Integer adcIndex, OBDtoAdcRealServerGroup rsGroup) throws OBException;
	public ArrayList<OBDtoAdcRealServerGroup> searchNodeGrpListF5(Integer adcIndex, Integer accntIndex, Integer orderType, Integer orderDir) throws OBException;
	public boolean updateRealServerMap(Integer adcIndex, OBDtoAdcRealServerGroup rsGroup, ArrayList<OBDtoAdcNodeF5> nodeList) throws OBException;
	
	/**
	 * VS의 상태, 이름, 아이피, 포트, 현재 poolname, 공지 poolname을 가져온다. 
	 * @param adcIndex
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcVServerNotice> searchVServerNoticeOnList(Integer adcIndex, Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;
	public Integer searchVServerNoticeOnListCount(Integer adcIndex, Integer accntIndex, String searchKey) throws OBException;
	public ArrayList<OBDtoAdcVServerNotice> searchVServerNoticeOffList(Integer adcIndex, Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;
	public Integer searchVServerNoticeOffListCount(Integer adcIndex, Integer accntIndex, String searchKey) throws OBException;

	/**
	 * setVServerNoticeOnF5 : virtual server 공지 enable,  service group --> notice group
	 * setVServerNoticeOffF5: virtual server 공지 disable, service group <-- notice group
	 * @param adcIndex : ADC index
	 * @param vsList : 작업할 virtual server들의 정보. notice group, service group 포함
	 * @param extraInfo
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 * 2014.10.29 - F5만 구현함
	 */
	public void setVServerNoticeOnF5(Integer adcIndex, ArrayList<OBDtoAdcVServerNotice> vsList, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	public void setVServerNoticeOffF5(Integer adcIndex, ArrayList<OBDtoAdcVServerNotice> vsList, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException;

	/**
	 * 설정 되돌리기를 제공한다. 
	 * 
	 * @param adcIndex : ADC 인덱스. null 불가.
	 * @param revertConfig : 되돌리고자하는 SLB 설정 내용.
	 * @param currentConfig : 현재 SLB 설정 내용. 
	 * @param newVServiceList : 새롭게 추가된 Virtual service 목록. 여러개는 ","로 구분함.
	 * @param newPoolList : 새롭게 추가된 pool 목록. ","로 구분함.
	 * @param newNodeList : 새롭게 추가된 node 목록. ","로 구분함.
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
	public void revertSlbConfig(Integer adcIndex, String revertConfig, String currentConfig, String newVServiceList, String newPoolList, String newNodeList) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	
	/**
	 * F5 장비에 대한 config sync 명령. Alteon은 제공하지 않는다.
	 * 
	 * @param adcIndex. 현재 접속하고 있는 ADC 장비의 index.
	 * @param extraInfo
	 * @throws OBException
	 */
	public void syncConifgF5(Integer adcIndex, OBDtoExtraInfo extraInfo) throws OBException;

	/**
	 * VirtualServer 중복을 확인한다. VirtualServer 이름 중복을 확인한다.
	 * @param adcIndex : ADC 인덱스. null 불가
	 * @param vsName : 확인하려는 VirtualServer 이름. null 불가. 
	 * @return
	 *  - true : VirtualServer 있음
	 *  - false : VirtualServer 없음
	 *  @throws OBException
	 */
	public boolean isExistVirtualServer(Integer adcIndex, String vsName) throws OBException;
	public boolean isExistVSIPAddress(Integer adcIndex, String vsIPAddress) throws OBException;
	public boolean isExistVirtualServer(Integer adcIndex, ArrayList<String> vsIndexList) throws OBException;

	/**
	 * VS 가 등록되어 있는지 검사한다.
	 * @param adcIndex
	 * @param alteonID
	 * @return true: 등록되어 있는 경우, false: 등록되어 있지 않은 경우.
	 * @throws OBException
	 */
	public boolean isExistVirtualServerAlteon(Integer adcIndex, Integer alteonID) throws OBException;
	public boolean isExistVirtualServerF5(Integer adcIndex, String vsName) throws OBException;
	public boolean isExistVirtualServerPAS(Integer adcIndex, String vsName) throws OBException;
	public boolean isExistVirtualServerPASK(Integer adcIndex, String vsName) throws OBException;
	
	public String getValidVSIndex(Integer adcIndex, String vsIndex) throws OBException; 
	public String getValidPoolIndex(Integer adcIndex, String poolIndex) throws OBException; 
	
	public ArrayList<String> getPoolIndexListAlteon(Integer adcIndex) throws OBException;
	
	public ArrayList<String> getVsNameList(Integer adcIndex) throws OBException;
	
	/**
	 * Peer Host의 OS가 같은지 검사한다. 같으면 true, 그렇지 않으면 false를 리턴한다.
	 * @param adcIndex
	 * @param peerIndex
	 * @return
	 * @throws OBException
	 */
	public boolean isPeerOSEqual(Integer adcIndex, Integer peerIndex) throws OBException;
	
	/**
	 * Peer 장비의 VRRP가 유효한지 검사한다. 유효하면 true, 그렇지 않으면 false를 리턴한다.
	 * @param adcIndex
	 * @param peerIndex
	 * @return
	 * @throws OBException
	 */
	public boolean isPeerVrrpValid(Integer adcIndex, Integer peerIndex) throws OBException;
	
	/**
     * VS Add할경우 VlanList를 가져온다.
     * @param adcIndex
     * @param vsIndex
     * @return ArrayList<OBDtoAdcVlan>
     * @throws OBException
     */
	public ArrayList<OBDtoAdcVlan> getF5VlanListAll(Integer adcIndex) throws OBException;

    /**
     * VS 수정할경우 VlanList를 가져온다.
     * @param adcIndex
     * @param vsIndex
     * @return ArrayList<OBDtoAdcVlan>
     * @throws OBException
     */
	
	public ArrayList<OBDtoAdcVlan> getF5VlanList(Integer adcIndex, String vsIndex) throws OBException;
		
    /**
     * VS 수정할경우 VlanFilterList를 가져온다.
     * @param adcIndex
     * @param vsIndex
     * @return DtoVlanTunnelFilter
     * @throws OBException
     */
	
	public DtoVlanTunnelFilter getF5VlanFilterList(Integer adcIndex, String vsIndex) throws OBException;

    /**
     * Alteon 공지 설정 기능
     * @param adcIndex
     * @param vsList
     * @param extraInfo
     * @throws OBExceptionUnreachable
     * @throws OBExceptionLogin
     * @throws OBException
     */
    public void setVServerNoticeOnAlteon(Integer adcIndex, ArrayList<OBDtoAdcVServerNotice> vsList, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException;

    /**
     * Alteon 공지 해제 기능
     * @param adcIndex
     * @param vsList
     * @param extraInfo
     * @throws OBExceptionUnreachable
     * @throws OBExceptionLogin
     * @throws OBException
     */
    public void setVServerNoticeOffAlteon(Integer adcIndex, ArrayList<OBDtoAdcVServerNotice> vsList, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException;

    
    /**
     * Peer Sync VSNAME 체크
     * @param adcIndex
     * @param vsList
     * @param extraInfo
     * @throws OBExceptionUnreachable
     * @throws OBExceptionLogin
     * @throws OBException
     */
	public boolean isVServerSyncNotice(ArrayList<OBDtoAdcVServerNotice> vsList, Integer adcIndex) throws OBException;

}
