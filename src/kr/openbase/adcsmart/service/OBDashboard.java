package kr.openbase.adcsmart.service;

import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoAdcSummary;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSystemLog;
import kr.openbase.adcsmart.service.dto.OBDtoConnection;
import kr.openbase.adcsmart.service.dto.OBDtoCpu;
import kr.openbase.adcsmart.service.dto.OBDtoMemory;
import kr.openbase.adcsmart.service.dto.OBDtoStatusSummary;
import kr.openbase.adcsmart.service.dto.OBDtoThroughput;
import kr.openbase.adcsmart.service.dto.OBDtoVSMonitorLog;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoGroupHistory;
import kr.openbase.adcsmart.service.utility.OBException;

public interface OBDashboard
{
	/**
	 * 대쉬보드의 장비별, 버추얼 서버별, 시스템의 상태를 요약한 정보(Status Summary)를 제공한다.
	 * 
	 * @param accountIndex : 로그인 사용자 계정. null 불가.
	 * @return OBDtoStatusSummary
	 * @throws OBException
	 */
	public OBDtoStatusSummary getStatusSummary(Integer accountIndex) throws OBException;
	
	/**
	 * ADC Virtual server의 설정 로그를 제공한다.
	 * 
	 * @param accountIndex : 로그인 사용자 계정. null 불가.
	 * @param logCount : 조회할 로그 개수. null일 경우에는 기본값으로 20개 만 제공. 0일 경우에는 모든 로그를 제공함.
	 * @return ArrayList<OBDtoVSMonitorLog>
	 * @throws OBException
	 */
	public ArrayList<OBDtoVSMonitorLog> getVSConfigLog(Integer accountIndex, Integer logCount) throws OBException;
	
	/**
	 * 장비의 장애 로그를 제공한다.
	 * @param accountIndex : 로그인 사용자 계정. null 불가.
	 * @param logCount : 조회할 로그 개수. null일 경우에는 기본값으로 20개 만 제공. 0일 경우에는 모든 로그를 제공함.
	 * @return ArrayList<OBDtoAdcSystemLog>
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcSystemLog> getAdcSystemFaultLog(Integer accountIndex, Integer logCount) throws OBException;
	
	/**
	 * 지정된 virtual service에서 사용된 connection 추이를 제공한다.
	 * @param adcIndex : adc 장비의 인덱스. null 불가.
	 * @param vsIndex : virtual server의 인덱스. null 불가.
	 * @param virtPort : virtual service 번호. null 불가.
	 * @param beginTime : 조회하고자하는 시작 날짜. null일 경우에는 endTime 시점 7일 전으로 간주한다. 
	 * @param endTime : 조회하고자하는 종료 날짜. null일 경우에는 현재 시점으로 간주.
	 * @param logCount : 조회하고자하는 데이터 개수. null일 경우에는 최대 20개만 제공. 0일 경우에는 모든 데이터 제공.
	 * @return ArrayList<OBDtoUsageConnection>
	 * @throws OBException
	 */
	public OBDtoConnection getUsageConnections(Integer adcIndex, String vsIndex, Integer virtPort, Date beginTime, Date endTime, Integer logCount) throws OBException;

	/**
	 * 지정된 virtual server에서 사용된 Connection 추이 그래프를 위한 데이터를 제공한다. 
	 * @param adcIndex : adc 장비의 인덱스. null 불가.
	 * @param vsIndex : virtual server의 인덱스. null 불가.
	 * @param beginTime : 조회하고자하는 시작 날짜. null일 경우에는 endTime 시점 7일 전으로 간주한다. 
	 * @param endTime : 조회하고자하는 종료 날짜. null일 경우에는 현재 시점으로 간주.
	 * @param logCount : 쓰이지 않음. 제거예정. 
	 *     원래 다음과 같은 용도였으나 시간 from-to 구간이면 모든 데이터를 뽑아오는 것이 맞으므로 무시함.
	 *     <원래 용도> 모든 데이터조회하고자하는 데이터 개수. null일 경우에는 최대 100개만 제공. 0일 경우에는 모든 데이터 제공.
	 * @return OBDtoConnection
	 * @throws OBException
	 */
	public OBDtoConnection getUsageConnections(Integer adcIndex, String vsIndex, Date beginTime, Date endTime, Integer logCount) throws OBException;

	/**
	 * 지정된 adc장비에서 사용된 Connection 추이 그래프를 위한 데이터를 제공한다. 
	 * @param adcIndex : adc 장비의 인덱스. null 불가.
	 * @param beginTime : 조회하고자하는 시작 날짜. null일 경우에는 endTime 시점 7일 전으로 간주한다. 
	 * @param endTime : 조회하고자하는 종료 날짜. null일 경우에는 현재 시점으로 간주.
	 * @param logCount : 쓰이지 않음. 제거예정. 
	 *     원래 다음과 같은 용도였으나 시간 from-to 구간이면 모든 데이터를 뽑아오는 것이 맞으므로 무시함.
	 *     <원래 용도> 모든 데이터조회하고자하는 데이터 개수. null일 경우에는 최대 100개만 제공. 0일 경우에는 모든 데이터 제공.
	 * @return OBDtoConnection
	 * @throws OBException
	 */
	public OBDtoConnection getUsageConnections(Integer adcIndex, Date beginTime, Date endTime, Integer logCount) throws OBException;

	/**
	 * 지정된 adc장비에서 사용된 cpu 추이 그래프를 위한 데이터를 제공한다. 
	 * @param adcIndex : adc 장비의 인덱스. null 불가.
	 * @param beginTime : 조회하고자하는 시작 날짜. null일 경우에는 endTime 시점 7일 전으로 간주한다. 
	 * @param endTime : 조회하고자하는 종료 날짜. null일 경우에는 현재 시점으로 간주.
	 * @param logCount : 쓰이지 않음. 제거예정. 
	 *     원래 다음과 같은 용도였으나 시간 from-to 구간이면 모든 데이터를 뽑아오는 것이 맞으므로 무시함.
	 *     <원래 용도> 모든 데이터조회하고자하는 데이터 개수. null일 경우에는 최대 100개만 제공. 0일 경우에는 모든 데이터 제공.
	 * @return OBDtoCpu
	 * @throws OBException
	 */	
	public OBDtoCpu getUsageCpu(Integer adcIndex, Date beginTime, Date endTime, Integer logCount) throws OBException;
	
	   /**
     * 지정된 adc장비에서 사용된 cpu Group 추이 그래프를 위한 데이터를 제공한다. 
     * @param adcIndex : adc 장비 그룹의 인덱스. null 불가.
     * @param beginTime : 조회하고자하는 시작 날짜. null일 경우에는 endTime 시점 7일 전으로 간주한다. 
     * @param endTime : 조회하고자하는 종료 날짜. null일 경우에는 현재 시점으로 간주.
     * @param logCount : 쓰이지 않음. 제거예정. 
     *     원래 다음과 같은 용도였으나 시간 from-to 구간이면 모든 데이터를 뽑아오는 것이 맞으므로 무시함.
     *     <원래 용도> 모든 데이터조회하고자하는 데이터 개수. null일 경우에는 최대 100개만 제공. 0일 경우에는 모든 데이터 제공.
     * @return OBDtoCpu
     * @throws OBException
     */ 
    public ArrayList<OBDtoGroupHistory> getUsageCpuGroup(Integer adcGroupIndex, Date beginTime, Date endTime, Integer logCount) throws OBException;
	
	/**
	 * 지정된 adc장비에서 사용된 memory 추이 그래프를 위한 데이터를 제공한다. 
	 * @param adcIndex : adc 장비의 인덱스. null 불가.
	 * @param beginTime : 조회하고자하는 시작 날짜. 
	 * @param endTime : 조회하고자하는 종료 날짜. null일 경우에는 현재 시점으로 간주.
	 * @param logCount : 쓰이지 않음. 제거예정. 
	 *     원래 다음과 같은 용도였으나 시간 from-to 구간이면 모든 데이터를 뽑아오는 것이 맞으므로 무시함.
	 *     <원래 용도> 모든 데이터조회하고자하는 데이터 개수. null일 경우에는 최대 100개만 제공. 0일 경우에는 모든 데이터 제공.
	 * @return OBDtoMemory
	 * @throws OBException
	 */	
	public OBDtoMemory getUsageMem(Integer adcIndex, Date beginTime, Date endTime, Integer logCount) throws OBException;
	
	/**
     * 지정된 adc장비 Group에서 사용된 memory 추이 그래프를 위한 데이터를 제공한다. 
     * @param adcGroupIndex : adc 장비의Group 인덱스. null 불가.
     * @param beginTime : 조회하고자하는 시작 날짜. 
     * @param endTime : 조회하고자하는 종료 날짜. null일 경우에는 현재 시점으로 간주.
     * @param logCount : 쓰이지 않음. 제거예정. 
     *     원래 다음과 같은 용도였으나 시간 from-to 구간이면 모든 데이터를 뽑아오는 것이 맞으므로 무시함.
     *     <원래 용도> 모든 데이터조회하고자하는 데이터 개수. null일 경우에는 최대 100개만 제공. 0일 경우에는 모든 데이터 제공.
     * @return OBDtoMemory
     * @throws OBException
     */ 
    public ArrayList<OBDtoGroupHistory> getUsageMemGroup(Integer adcIGroupndex, Date beginTime, Date endTime, Integer logCount) throws OBException;
	
	/**
	 * 지정된 virtual server에서 사용된 throughput 추이 그래프를 위한 데이터를 제공한다. 
	 * @param adcIndex : adc 장비의 인덱스. null 불가.
	 * @param vsIndex : virtual server의 인덱스. null 불가.
	 * @param beginTime : 조회하고자하는 시작 날짜.  null일 경우에는 endTime 시점 7일 전으로 간주한다. 
	 * @param endTime : 조회하고자하는 종료 날짜. null일 경우에는 현재 시점으로 간주.
	 * @param logCount : 쓰이지 않음. 제거예정. 
	 *     원래 다음과 같은 용도였으나 시간 from-to 구간이면 모든 데이터를 뽑아오는 것이 맞으므로 무시함.
	 *     <원래 용도> 모든 데이터조회하고자하는 데이터 개수. null일 경우에는 최대 100개만 제공. 0일 경우에는 모든 데이터 제공.
	 * @return OBDtoThroughput
	 * @throws OBException
	 */		
	public OBDtoThroughput getUsageThroughput(Integer adcIndex, String vsIndex, Date beginTime, Date endTime, Integer logCount) throws OBException;

	/**
	 * 지정된 adc장비에서 사용된 throughput 추이 그래프를 위한 데이터를 제공한다. 
	 * @param adcIndex : adc 장비의 인덱스. null 불가.
	 * @param beginTime : 조회하고자하는 시작 날짜.  null일 경우에는 endTime 시점 7일 전으로 간주한다. 
	 * @param endTime : 조회하고자하는 종료 날짜. null일 경우에는 현재 시점으로 간주.
	 * @param logCount : 쓰이지 않음. 제거예정. 
	 *     원래 다음과 같은 용도였으나 시간 from-to 구간이면 모든 데이터를 뽑아오는 것이 맞으므로 무시함.
	 *     <원래 용도> 모든 데이터조회하고자하는 데이터 개수. null일 경우에는 최대 100개만 제공. 0일 경우에는 모든 데이터 제공.
	 * @return OBDtoThroughput
	 * @throws OBException
	 */		
	public OBDtoThroughput getUsageThroughput(Integer adcIndex, Date beginTime, Date endTime, Integer logCount) throws OBException;
	
	/**
	 * ADC 요약 정보를 제공한다. 장비별 상태 정보를 제공한다.
	 * 
	 * @param accountIndex : 로그인 사용자 계정. null 불가.
	 * @return ArrayList<OBDtoAdcSummary>
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcSummary> getAdcSummary(Integer accountIndex) throws OBException;
}
