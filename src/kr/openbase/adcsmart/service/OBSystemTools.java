package kr.openbase.adcsmart.service;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.utility.OBException;

public interface OBSystemTools
{
	/**
	 *  포트 사용량에 대한 정보를 제공한다.
	 * @return String
	 * @throws OBException
	 */
	public String portUsageGetContent() throws OBException;
	
	/**
	 * CSV 파일의 헤더 정보를 제공한다.
	 * @return String []
	 * @throws OBException
	 */
	public String [] portUsageMakeCsvHeader() throws OBException;
	
	/**
	 * CSV 형태로 포트 사용량에 대한 정보를 재 구성한다.
	 * @return ArrayList<String[]> 
	 * @throws OBException
	 */
	public ArrayList<String[]> portUsageMakeCsvBody() throws OBException;
	/**
	 *  CIP/DIP를 검색 키워드로 사용한 SlbSession 정보를 제공한다.
	 * @param adcInfo 
	 *  
	 */
	public String slbSessionGetContent(String adcIp , String adcType , String accountId, String password, int connService, int connPort, int ipType, String ip) throws OBException;
	/**
	 * 사용하지 않은 pool과 node를 추출하여 제공한다.
	 * @param accntIndex
	 * @param adcIndex
	 * @param searchType. 1:pool, 2:node
	 * @return
	 * @throws OBException
	 */
	public String 				unUsedSlbInfoContent(Integer accntIndex, Integer adcIndex, Integer searchType) throws OBException;
	public String []  			unUsedSlbInfoCsvHeader(Integer accntIndex, Integer adcIndex) throws OBException;
	public ArrayList<String[]> 	unUsedSlbInfoCsvBody(Integer accntIndex, Integer adcIndex) throws OBException;
}
