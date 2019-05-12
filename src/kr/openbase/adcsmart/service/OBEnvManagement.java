/*
 * 시스템 관리를 위한 인터페이스
 */
package kr.openbase.adcsmart.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcLogFilter;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoScheduleBackupInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSyncSystemTime;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnv;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvAdditional;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvNetwork;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvView;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;

public interface OBEnvManagement {
	/**
	 * 예약 백업을 위한 설정 작업을 지원한다.
	 * 
	 * @param backupInfo. null 불가.
	 * @param extraInfo. null 불가.
	 * @throws OBException
	 */
	public void setScheduleBackupInfo(OBDtoScheduleBackupInfo backupInfo, OBDtoExtraInfo extraInfo) throws OBException;

	/**
	 * ADC 로그 필터링 패턴을 추가한다.
	 * 
	 * @param info
	 * @param extraInfo : 부가 정보. null 불가.
	 * @throws OBException
	 */
	public void addAdcLogFitlerPattern(OBDtoAdcLogFilter info, OBDtoExtraInfo extraInfo) throws OBException;

	/**
	 * ADC 로그 필터링 패턴을 삭제한다.
	 * 
	 * @param delList
	 * @param extraInfo : 부가 정보. null 불가.
	 * @throws OBException
	 */
	public void delAdcLogFilterPattern(ArrayList<Integer> delList, OBDtoExtraInfo extraInfo) throws OBException;

	/**
	 * ADC 로그 필터링 패턴을 조회한다.
	 * 
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcLogFilter> getAdcLogFilterPatternList() throws OBException;

	/**
	 * 화면 표시 기능을 설정한다.
	 * 
	 * @param info
	 * @param extraInfo : 부가 정보. null 불가.
	 * @throws OBException
	 */
	public void setViewConfig(OBDtoSystemEnvView info, OBDtoExtraInfo extraInfo) throws OBException;

	/**
	 * 부가 정보를 설정한다.
	 * 
	 * @param info
	 * @param extraInfo : 부가 정보. null 불가.
	 * @throws OBException
	 */
	public void setAdditionalConfig(OBDtoSystemEnvAdditional info, OBDtoExtraInfo extraInfo) throws OBException;

	/**
	 * 네트워크 설정 작업을 진행한다.
	 * 
	 * @param info
	 * @param extraInfo : 부가 정보. null 불가.
	 * @throws OBException
	 */
	public void setNetworkConfig(OBDtoSystemEnvNetwork info, OBDtoExtraInfo extraInfo) throws OBException;

	/**
	 * 시스템 SyncTime정보를 설정한다.
	 * 
	 * @param info
	 * @param extraInfo : 부가 정보. null 불가.
	 * @throws OBException
	 */
	public boolean setSyncSystemTimeConfig(OBDtoSyncSystemTime info, OBDtoExtraInfo extraInfo) throws OBException;

	/**
	 * 시스템 SyncTime정보를 가져온다.
	 * 
	 * @return OBDtoSyncSystemTime
	 * @throws OBException
	 */
	public OBDtoSyncSystemTime getSyncSystemTimeConfig() throws OBException;

	/**
	 * 시스템 설정 데이터를 제공한다.
	 * 
	 * @return OBDtoSystemSetting 클래스. 시스템 설정 데이터가 저장되어 있다.
	 * @throws NullPointerException     -- if a null point access occurs. 필수 항목이 누락된
	 *                                  경우에 발생된다.
	 * @throws SQLException             -- 데이터 베이스 관련 오류 발생시. 계정이 이미 있는 경우에도 발생된다.
	 * @throws IllegalArgumentException -- 잘못된 입력값 입력시.
	 */
	public OBDtoSystemEnv getSystemConfig() throws OBException;

//	/**
//	 * 시스템 설정을 업데이트한다. 
//	 * 
//	 * @param config
//	 *			-- OBDtoSystemSetting 클래스. 업데이트할 시스템 설정 데이터가 저장되어 있다.  
//	 * @throws NullPointerException
//	 * 			-- if a null point access occurs. 필수 항목이 누락된 경우에 발생된다.
//	 * @throws SQLException
//	 * 			-- 데이터 베이스 관련 오류 발생시. 계정이 이미 있는 경우에도 발생된다.
//	 * @throws IllegalArgumentException
//	 * 			-- 잘못된 입력값 입력시.
//	 */		
//	public void updateSystemConfig(OBDtoSystemEnv config) throws OBException;

	/**
	 * 시스템 시간을 NTP 서버를 통해 동기화 한다.
	 * 
	 * @param ntpServer -- ntp 서버 호스트 이름. 예: time.bora.net
	 * @param extraInfo : 부가 정보. null 불가.
	 * @throws IOException              -- 동기화 실패시에 발생.
	 * @throws IllegalArgumentException -- 잘못된 입력값 입력시.
	 * @throws InterruptedException
	 */
	public void syncNTPServer(String ntpServer, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBException;

	/**
	 * 중복 로그인 허용 여부.
	 * 
	 * @return 0: 중복 허용. 1: 중복 로그인 허용하지 않음.
	 * @throws OBException
	 */
	public Integer getEnvLoginAccess() throws OBException;

	/**
	 * 시스템 덤프를 수행한다.
	 * 
	 * @return String : 덤프 실행 결과
	 * @throws OBException
	 */
	public String runDumpConfig() throws OBException;

	public Integer getEnvAlarmPopup() throws OBException;

}
