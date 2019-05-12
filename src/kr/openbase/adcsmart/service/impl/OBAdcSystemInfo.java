package kr.openbase.adcsmart.service.impl;

import kr.openbase.adcsmart.service.dto.OBDtoAdcSnmpInfo;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcSystemInfo;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;

public interface OBAdcSystemInfo
{
	public String getAdcHostName(String ipAddress, String swVersion, OBDtoAdcSnmpInfo snmpInfo) throws OBException;
	
	/**
	 * ADC 시스템의 기본적인 정보를 추출하여 제공한다.
	 * 
	 * @param adcIndex
	 * 			-- 장비 index.
	 * @param ipaddress
	 * 			-- 장비 주소.
	 * @param account
	 * 			-- 장비 접근 계정.
	 * @param password
	 * 			--장비 접근 비밀번호.
	 * @param swVersion
	 * 
	 * @return OBDtoAdcSystemInfo
	 */
	public OBDtoAdcSystemInfo getAdcSystemInfo(Integer adcIndex, String ipaddress, String account, String password, int connService, int connPort, String swVersion, OBDtoAdcSnmpInfo snmpInfo, int opMode) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	
	/**
	 * 시스템에 접근하여 로그인할 수 있는지 확인한다.
	 * 
	 * @param ipaddress
	 * @param account
	 * @param password
	 * @param vendor
	 * @param swVersion
	 * @param connService -통신 서비스 Telnet || SSH
	 * @param connPort -통신 포트, 숫자
	 * @param cliAccount  -F5에서만 사용
	 * @param cliPassword -F5에서만 사용
	 * @return true: 로그인 성공함. false: 로그인 실패.
	 */
//	public boolean isAvailableSystem(String ipaddress, String account, String password, Integer vendor, String swVersion);
	public boolean isAvailableSystem(String ipaddress, String account, String password, int connService, int connPort, String cliAccount, String cliPassword) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	/**
	 *  지정된 장비에 snmp 접근하여 swVersion을 추출한다.
	 * @param adcIPAddress
	 * @param adcCLIAccount
	 * @param adcCLIPW
	 * @param snmpCommunity
	 * @param swVersion
	 * @return
	 * @throws OBException
	 */
	public String getAdcSWVersionCli(String adcIPAddress, String adcCLIAccount, String adcCLIPW, String swVersion, int connService, int connPort) throws OBException;
	public String getAdcSWVersionSnmp(String adcIPAddress, OBDtoAdcSnmpInfo snmpInfo) throws OBException;
}
