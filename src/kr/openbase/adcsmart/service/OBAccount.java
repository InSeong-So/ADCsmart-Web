package kr.openbase.adcsmart.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoAccount;
import kr.openbase.adcsmart.service.dto.OBDtoAccountRole;
import kr.openbase.adcsmart.service.dto.OBDtoAccountRoleMap;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcRSInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVSInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAuditLogAccount;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBLicenseExpiredException;

/**
 * @author Rock
 *
 */
public interface OBAccount
{
	/**
	 *  패스워드를 초기화 한다.
	 *  
	 * @param accntIndex. null 불가.
	 * @param extraInfo
	 * @throws OBException
	 */
	public void resetPassword(Integer accntIndex, OBDtoExtraInfo extraInfo) throws OBException;
	
	/**
	 * 지정된 계정의 패스워드를 변경한다.
	 * 
	 * @param accountIndex : null 불가.
	 * @param plainPassword : 암호화되지 않은 패스워드.
	 * @param extraInfo 
	 * @throws OBException
	 */
	public void changePassword(Integer accountIndex, String plainPassword, OBDtoExtraInfo extraInfo) throws OBException;
	
	/**
	 * 사용자로부터 입력 받은 계정에 대한 패스워드를 암호화 한다.
	 * @param password plain text 형태의 패스워드 문구.
	 * @return String
	 * @throws OBException
	 */
	public String encryptPassword(String password) throws OBException;
	
	/**
	 * 사용자 index로 계정 정보를 구한다.
	 * 
	 * @param accountIndex : 계정 index, null 불가.
	 * @return 계정 정보. 해당 계정 정보가 없으면 null
	 * @throws OBException
	 */
	public OBDtoAccount getAccountInfo(Integer accountIndex) throws OBException;
	
	/**
	 * 사용자 ID로 계정 정보를 구한다.
	 * 
	 * @param accountID : null 불가.
	 * @return 계정 정보. 해당 계정 정보가 없으면 null
	 * @throws OBException
	 */
	public OBDtoAccount getAccountInfo(String accountID) throws OBException;

	/**
	 * 사용자(계정) 로그인 시각을 저장한다. 저장할 시각은 시스템에서 구한다. 
	 * 
	 * @param accountIndex : 계정 인덱스. null 불가
	 * @throws OBException
	 */
	public void setlastLoginTime(Integer accountIndex) throws OBException;
	
	/**
	 * 사용자(계정) 최근 로그인 시각을 가져온다.
	 * 
	 * @param accountIndex : 계정 인덱스. null 불가 
	 * @return : 사용자의 마지막 로그인 시각 timestamp
	 * @throws OBException
	 */
	public Timestamp getLastloginTime(Integer accountIndex) throws OBException;
	
	/**
	 * 사용자(계정) 로그인 실패 회수를 카운트 한다. 로그인 실패마다 1회씩 증가시킨다.
	 * @param accountIndex : 계정 인덱스. null 불가
	 * @param extraInfo : 부가적인 정보. accountIndex, 부가적인 설명을 위한 message가 전달된다. null 불가.
	 * @throws OBException
	 */
	public void incrementLoginFailCount(Integer accountIndex, OBDtoExtraInfo extraInfo) throws OBException;

	/**
	 * 지정된 계정에 대한 로그인 실패 횟수를 반환한다.
	 * @return 실패 횟수.
	 * @throws OBException
	 */
	public Integer getLoginFailCount(Integer accountIndex) throws OBException;
	
	/**
	 * 사용자(계정) 로그인 실패 회수를 0으로 되돌린다.
	 * 
	 * @param accountIndex : 계정 인덱스. null 불가
	 * @throws SQLException
	 * @throws OBException
	 */
	public void crearLoginFailCount(Integer accountIndex) throws OBException;

	/**
	 * 계정 상세정보 목록을 가져온다. accountIndex가 null이면 전체 계정 정보 목록을 가져오고, 유효 accountIndex가 있으면 해당 계정 정보 목록 1건을 가져온다.
	 * 
	 * @param accountIndex : 
	 *  - 계정 인덱스
	 *  - null 가능. null이면 전체 계정 상세 정보 목록을 가져온다.
	 * @return
	 *  - accountIndex = null : 전체 계정 상세정보 목록 반환
	 *  - accountIndex 유효값 : 해당 계정 상세 정보 반환
	 * @throws OBException
	 */
	public ArrayList<OBDtoAccount> getAccountList(Integer accountIndex) throws OBException;

	/**
	 * ID나 사용자 이름에 검색조건(searchKey)이 들어 있는 계정 목록(상세정보포함)을 가져온다.  
	 * 
	 * @param searchKey : 
	 *  - 계정 ID나 계정 사용자이름에서 검색하려는 문자열.
	 *  - null 가능. null 이면 전체 계정 목록을 가져온다.
	 * @return
	 *  - searchKey 조건에 맞는 계정 목록(상세정보 포함)을 반환한다.
	 *  - searchKey가 null이면 전체 목록을 반환한다.
	 * @throws OBException
	 */
//	public ArrayList<OBDtoAccount> searchAccountList(String searchKey) throws OBException;
	
	/**
	 * 
	 * @param searchKey
	 * @param orderType, ORDER_TYPE_FIRST(아이디), ORDER_TYPE_SECOND(이름), ORDER_TYPE_THIRD(최종로그인시간), ORDER_TYPE_FOURTH(역할), ORDER_TYPE_SIXTH(설명)
	 * @param orderDir. ORDER_DIR_DESCEND, ORDER_DIR_ASCEND
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoAccount> searchAccountList(String searchKey, Integer orderType, Integer orderDir) throws OBException;
	/**
	 * 계정 정보를 수정한다. 계정에 할당된 ADC 정보까지 full로 수정한다.
	 *  
	 * @param accountInfo : 수정할 계정 상세 정보. null 불가
	 * @param extraInfo : 부가적인 정보. accountIndex, 부가적인 설명을 위한 message가 전달된다. null 불가.
	 * @throws OBException
	 */
	public void setAccount(OBDtoAccount accountInfo, OBDtoExtraInfo extraInfo) throws OBException;
	
	/**
	 * 새 사용자 계정을 등록한다.
	 * 
	 * @param accountInfo : 추가할 계정 상세 정보. null 불가
	 * @param extraInfo : 부가적인 정보. accountIndex, 부가적인 설명을 위한 message가 전달된다. null 불가.
	 * @throws OBLicenseExpiredException
	 * @throws OBException
	 */
	public void addAccount(OBDtoAccount accountInfo, OBDtoExtraInfo extraInfo) throws OBLicenseExpiredException, OBException;
	
	/**
	 * 복수의 계정을 삭제한다.
	 * 
	 * @param accountIndexList : 삭제할 계정의 인덱스 목록. null 불가
	 * @param extraInfo : 부가적인 정보. accountIndex, 부가적인 설명을 위한 message가 전달된다. null 불가.
	 * @throws OBException
	 */
	public void delAccount(ArrayList<Integer> accountIndexList, OBDtoExtraInfo extraInfo) throws OBException;
	
	/**
	 * 계정 역할 정보 목록을 가져온다. roleIndex가 null이면 전체 역할 목록을 가져오고, 유효 roleIndex를 주면 해당 역할을 가져온다.
	 * 
	 * @param roleIndex : 
	 *  - 계정 역할 인덱스
	 *  - null 가능. null 이면 전체 역할 목록을 가져온다. 
	 * @return
	 *  - roleIndex = null : 전체 역할 목록 반환
	 *  - roleIndex 유효값 : 해당 역할 반환
	 * @throws OBException
	 */
	public ArrayList<OBDtoAccountRole> getAccountRoleList(Integer roleIndex) throws OBException;
	
	/**
	 * 사용자에게 할당되지 않은(NS:non-selected) ADC들 중에서 검색조건(searchKey)으로 필터링한 목록을 가져온다.
	 * searchKey가 null이면 사용자에게 할당되지 않은 모든 ADC 목록을 가져온다.
	 * 
	 * @param searchKey : 
	 *  - ADC IP나 ADC 이름에서 검색하려는 문자열
	 *  - null 가능. null이면 사용자에게 할당되지 않은 전 ADC 목록을 가져온다.
	 * @return
	 *  - 유효 searchKey : 사용자에게 할당되지 않았으면서 검색조건에 맞는 ADC 목록을 반환한다.
	 *  - searchKey = null: 사용자에게 할당되지 않은 ADC 전 목록을 반환한다.
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcInfo> searchAdcInfoNSList(Integer accountIndex, String searchKey) throws OBException;

	/**
	 * 계정 역할 중 검색 조건(searchKey)에 맞는 역할 목록을 가져온다.
	 * @param searchKey :
	 *  - 계정 역할 이름에서 검색하려는 문자열 
	 *  - null가능. null이면 모든 계정 역할 목록을 가져온다.
	 * 
	 * @return 
	 *  - 유효 searchKey : 검색 조건에 맞는 계정 역할 목록 반환
	 *  - searchKey = null: 모든 계정 역할 목록 반환
	 * @throws OBException
	 */
	public ArrayList<OBDtoAccountRoleMap> getAccountRoleMapList(String searchKey) throws OBException;
	
	/**
	 * 지정된 계정의 로그인/로그아웃 정보를 제공한다. 
	 * 
	 * @param accountIndex : 로그인된 계정의 index. null 불가.
	 * @param searchKeys : 검색 키워드. 접속 IP를 대상으로 검색한다. null일 경우에는 검색 조건 없음.
	 * @param beginTime : 검색 시작 시각. null일 경우에는 전체를 대상.
	 * @param endTime : 검색 종료 시각. null일 경우에는 현재 시각. beginTime보다 같거나 이후 시각이어야 한다.
	 * @param recordCount : 데이터 레코드 개수. null일 경우에는 기본적으로 20개. 0일 경우에는 모든 항목.
	 * @return ArrayList<OBDtoAuditLogAccount>
	 * @throws OBException
	 */
	public ArrayList<OBDtoAuditLogAccount> getAuditLogAccount(Integer accountIndex, String searchKeys, Date beginTime, Date endTime, Integer recordCount) throws OBException;
	
	/**
	 * 지정된 계정으로 로그인된 Client IP 정보를 제공한다. 로그인되지 않은 경우에는 null을 리턴한다.
	 * 
	 * @param accountIndex
	 * @return String
	 * @throws OBException
	 */
	public String getLoginClientIP(Integer accountIndex) throws OBException;
	
	/**
	 * 지정된 계정에 로그인된 Client IP 정보를 설정한다.
	 * 
	 * @param accountIndex
	 * @param ipAddress
	 * @throws OBException
	 */
	public void setLoginClientIP(Integer accountIndex, String ipAddress) throws OBException;
	
	/**
	 * 로그인 상태 정보를 clear한다.
	 * 
	 * @param accountIndex
	 * @throws OBException
	 */
//	public void resetLoginClientIP(Integer accountIndex) throws OBException;
	
	public Date getLastAliveTime(Integer accountIndex) throws OBException;
	
	public void setLastAliveTime(Integer accountIndex, Date currentTime) throws OBException;
	
	/**
	 * 지정된 계정에 할당된 ADC/VS 목록을 제공한다.
	 * 
	 * @param accntIndex
	 * @param searchKey
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcVSInfo> getAdcVSInfoList(Integer accntIndex, String searchKey) throws OBException;
	
	/** 지정된 계정에 할당되지 않은 ADC/VS 목록을 제공한다.
	 * 
	 * @param accntIndex
	 * @param searchKey
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcVSInfo> getAdcVSInfoNSList(Integer accntIndex, String searchKey) throws OBException;
	
	/** 지정된 계정에 할당된 ADC/RS 목록을 제공한다.
	 * 
	 * @param accntIndex
	 * @param searchKey
	 * @return
	 * @throws OBException
	 */
	
	public ArrayList<OBDtoAdcRSInfo> getAdcRSInfoList(Integer accntIndex, String searchKey) throws OBException;
	
	/** 지정된 계정에 할당되지 않은 ADC/RS 목록을 제공한다.
	 * 
	 * @param accntIndex
	 * @param searchKey
	 * @return
	 * @throws OBException
	 */
	
	public ArrayList<OBDtoAdcRSInfo> getAdcRSInfoNSList(Integer accntIndex, String searchKey) throws OBException;
	
	/**
	 * 관리자 로그 파일을 제공한다.
	 * @param fileName
	 * @return
	 * @throws OBException
	 */
	public String getLogContent(String fileName) throws OBException;

	/**
	 * 접속자 IP가 DB에 저장된 대역에 매칭 되는지 확인한다. 
	 * @param accntIndex
	 * @param ipAddress
	 * @return true/false
	 * @throws OBException
	 */
	public Boolean isIpFilter(Integer accntIndex, String ipAddress) throws OBException;
	
	
}