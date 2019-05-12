package kr.openbase.adcsmart.service.impl;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficVServer;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcVServerPAS;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcVServerPASK;
import kr.openbase.adcsmart.service.utility.*;

public interface OBAdcMonitor
{
	/**
	 * ADC 디바이스로부터 버추얼 서버 트래픽 정보를 추출하여 전달한다.
	 * @param adcIndex
	 * @param ipaddress
	 * @param accountID
	 * @param password
	 * @param swVersion
	 * @return ArrayList<OBDtoMonTrafficVServer>
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
	public ArrayList<OBDtoMonTrafficVServer> getVServerTrafficAlteon(Integer adcIndex, String ipaddress, String accountID, String password, String swVersion) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	
	public ArrayList<OBDtoMonTrafficVServer> getVServerTrafficF5(Integer adcIndex, String ipaddress, String accountID, String password, String swVersion) throws OBExceptionUnreachable, OBExceptionLogin, OBException;

	public ArrayList<OBDtoMonTrafficVServer> getVServerTrafficPAS(Integer adcIndex, String ipaddress, String accountID, String password, String swVersion) throws OBExceptionUnreachable, OBExceptionLogin, OBException;

	public ArrayList<OBDtoMonTrafficVServer> getVServerTrafficPASK(Integer adcIndex, String ipaddress, String accountID, String password, String swVersion) throws OBExceptionUnreachable, OBExceptionLogin, OBException;

//	public ArrayList<OBDtoMonL2Ports> getL2PortsInfo(String ipaddress, String swVersion, String community) throws OBException;
	
	public ArrayList<OBDtoAdcVServerAlteon> getSlbStatusAlteon(int adcIndex) throws OBException;
	
	public ArrayList<OBDtoAdcVServerF5> getSlbStatusAllF5(int adcIndex) throws OBException;

	public ArrayList<OBDtoAdcVServerPAS> getSlbStatusPAS(int adcIndex) throws OBException;

	public ArrayList<OBDtoAdcVServerPASK> getSlbStatusPASK(int adcIndex) throws OBException;

//	public ArrayList<OBDtoPoolMembersStatus> getPoolMembersStatus(int adcIndex, OBDatabase db) throws OBException;
}
