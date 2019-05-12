package kr.openbase.adcsmart.service;

import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoCpu;
import kr.openbase.adcsmart.service.dto.OBDtoDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoHddInfo;
import kr.openbase.adcsmart.service.dto.OBDtoMemory;
import kr.openbase.adcsmart.service.utility.OBException;

public interface OBSystemView
{// 시스템 정보를 제공한다.
	
	/**
	 * CPU 사용율 추이 데이터를 제공한다.
	 * 
	 * @param beginTime : 조회하고자하는 시작 날짜. null일 경우에는 endTime 시점 12시간 전으로 간주한다. 
	 * @param endTime : 조회하고자하는 종료 날짜. null일 경우에는 현재 시점으로 간주.
	 * @return OBDtoCpu
	 * @throws OBException
	 */
	public OBDtoCpu getUsageCpuList(Date beginTime, Date endTime) throws OBException;

	/**
	 * 메모리 사용율 추이 데이터를 제공한다.
	 * 
	 * @param beginTime : 조회하고자하는 시작 날짜. null일 경우에는 endTime 시점 12시간 전으로 간주한다. 
	 * @param endTime : 조회하고자하는 종료 날짜. null일 경우에는 현재 시점으로 간주.
	 * @return OBDtoMemory
	 * @throws OBException
	 */
	public OBDtoMemory getUsageMemList(Date beginTime, Date endTime) throws OBException;
	
	/**
	 * HDD 사용 현황을 제공한다.
	 * 
	 * @return OBDtoHdd
	 * @throws OBException
	 */
	public OBDtoHddInfo getUsageHdd() throws OBException;

	/**
	 * Database 사용 현황을 제공한다.
	 * 
	 * @return OBDtoDatabase
	 * @throws OBException
	 */
	public OBDtoDatabase getUsageDatabase() throws OBException;
}
