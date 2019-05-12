package kr.openbase.adcsmart.service;

import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoBackupInfo;
import kr.openbase.adcsmart.service.dto.OBDtoBackupSchedule;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.utility.OBException;

public interface OBBackupRestore
{
	/**
	 * 백업 목록을 제공한다. 최대 1000개까지만 제공한다.
	 * 
	 * @param accntIndex : 사용자 계정 정보. null 불가.
	 * @param searchKeys : 검색 키워드. 백업 파일명, 설명을 키워드로 사용한다. null 가능.
	 * @param beginTime : 검색 시작 시각. null 일 경우에는 시작 시각을 지정하지 않는다.
	 * @param endTime : 검색 종료 시각. null 일 경우에는 현재 시각으로 간주한다.
	 * @param beginIndex : 검색 레코드 index 시작 번호. null일 경우에는 0으로 간주.
	 * @param endIdex : 검색 레코드 종료 번호. null일 경우에는 beginIndex+20개로 간주한다.
	 * @return ArrayList<OBDtoBackupInfo>
	 * @throws OBException
	 */
	public ArrayList<OBDtoBackupInfo> getBackupInfoList(Integer accntIndex, String searchKeys, Date beginTime, Date endTime, Integer beginIndex, Integer endIndex) throws OBException;
	
	/**
	 * 
	 * @param accntIndex
	 * @param searchKeys
	 * @param beginTime
	 * @param endTime
	 * @param beginIndex
	 * @param endIndex
	 * @param orderType. ORDER_TYPE_OCCURTIME, ORDER_TYPE_STATUS, ORDER_TYPE_NAME, ORDER_TYPE_CONTENT, ORDER_TYPE_OPTION, ORDER_TYPE_SIZE
	 * @param orderDir. ORDER_DIR_DESCEND, ORDER_DIR_ASCEND
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoBackupInfo> getBackupInfoList(Integer accntIndex, String searchKeys, Date beginTime, Date endTime, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;
	/**
	 * 주어진 조건에 맞는 백업 목록의 개수를 제공하낟. 최대 1000개까지만 제공한다.
	 * 
	 * @param accntIndex : 사용자 계정 정보. null 불가.
	 * @param searchKeys : 검색 키워드. 백업 파일명, 설명을 키워드로 사용한다. null 가능.
	 * @param beginTime : 검색 시작 시각. null 일 경우에는 시작 시각을 지정하지 않는다.
	 * @param endTime : 검색 종료 시각. null 일 경우에는 현재 시각으로 간주한다.
	 * @return Integer
	 * @throws OBException
	 */
	public Integer getBackupInfoListCount(Integer accntIndex, String searchKeys, Date beginTime, Date endTime) throws OBException;
	
	/**
	 * 전체 백업 예약 목록을 추출한다.
	 * @param accntIndex
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoBackupSchedule> getBackupScheduleList(Integer accntIndex) throws OBException;
	
	/**
	 * 백업 정보를 제공한다. 
	 * 
	 * @param index : 백업 목록의 index. null 불가.
	 * @return OBDtoBackupInfo
	 * @throws OBException
	 */
	public OBDtoBackupInfo getBackupInfo(String index) throws OBException;
	
	/**
	 * 새로운 백업을 작성한다. 
	 * 
	 * @param backupInfo : 백업 조건. null 불가.
	 * @param extraInfo : 부가 정보. null 불가.
	 * @throws OBException
	 */
	public void addBackup(OBDtoBackupInfo backupInfo, OBDtoExtraInfo extraInfo) throws OBException;
	
	/**
	 * 새로운 백업을 예약한다. 
	 * 
	 * @param backupSchedule : 백업 예약 정보. null 불가.
	 * @param extraInfo : 부가 정보. null 불가.
	 * @throws OBException
	 */
	public void addBackupSchedule(OBDtoBackupSchedule backupSchedule, OBDtoExtraInfo extraInfo) throws OBException;
	
	/**
	 * 백업된 목록을 삭제한다.
	 * 
	 * @param backupList : 삭제할 백업 index 리스트. null 불가.
	 * @param extraInfo : 부가 정보. null 불가.
	 * @throws OBException
	 */
	public void delBackup(ArrayList<String> backupList, OBDtoExtraInfo extraInfo) throws OBException;
	
	/**
	 * 백업 예약을 취소한다.
	 * @param index
	 * @param extraInfo
	 * @throws OBException
	 */
	public void delBackupSchedule(long index, OBDtoExtraInfo extraInfo) throws OBException;
	
	/**
	 * 지정된 백업을 복구한다.
	 * 
	 * @param index : 복구할 백업 index. null 불가.
	 * @param extraInfo : 부가 정보. null 불가.
	 * @throws OBException
	 */
	public void restoreBackup(String index, OBDtoExtraInfo extraInfo) throws OBException;
	
	/**
	 * 백업 상태롤 조회한다.
	 * @param index
	 * @return Integer
	 * @throws OBException
	 */
	public Integer getBackupStatus(String index) throws OBException;
	
	/**
	 * 백업 상태를 저장한다.
	 * @param index
	 * @param status
	 * @throws OBException
	 */
	public void setBackupStatus(String index, Integer status) throws OBException;
}
