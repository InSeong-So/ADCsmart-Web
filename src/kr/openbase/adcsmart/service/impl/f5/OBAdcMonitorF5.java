package kr.openbase.adcsmart.service.impl.f5;

import java.util.ArrayList;
import java.util.HashMap;

import kr.openbase.adcsmart.service.adcmond.OBAdcMonitorDB;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;
import kr.openbase.adcsmart.service.dto.OBDtoVServerStatus;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBAdcMonitor;
import kr.openbase.adcsmart.service.impl.OBVServerDB;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficPoolMembers;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficVServer;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcVServerPAS;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcVServerPASK;
import kr.openbase.adcsmart.service.snmp.f5.OBSnmpF5;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBAdcMonitorF5 implements OBAdcMonitor {
	public ArrayList<OBDtoMonTrafficPoolMembers> getPoolMembersTrafficF5(Integer adcIndex, String ipaddress)
			throws OBException {
		OBDtoAdcInfo adcInfo;
		ArrayList<OBDtoMonTrafficPoolMembers> list;

		try {
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
					adcInfo.getSnmpInfo());// new OBSnmpF5(ipaddress);
			ArrayList<OBDtoAdcVServerF5> vsList = new OBVServerDB().getVServerListAllF5(adcInfo.getIndex());
			list = snmp.getTrafficPoolMembers(adcInfo.getAdcType(), adcInfo.getSwVersion(), vsList);
		} catch (OBException e1) {
			throw new OBException(e1.getMessage());
		}
		return list;
	}

	@Override
	public ArrayList<OBDtoMonTrafficVServer> getVServerTrafficF5(Integer adcIndex, String ipaddress, String accountID,
			String password, String swVersion) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBDtoAdcInfo adcInfo;
		ArrayList<OBDtoMonTrafficVServer> list;
		try {
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
					adcInfo.getSnmpInfo());// new OBSnmpF5(ipaddress);
			list = snmp.getTrafficVServer(adcInfo.getAdcType(), adcInfo.getSwVersion());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return list;
	}

//	//test functions --------------------------------------------------------------------
//	public static void main(String[] args)
//	{
//		Interfaces inter = new Interfaces();
//		
//		inter.initialize("175.196.72.100", "admin", "openbase");
//		try
//		{
//			testGetStatistics(inter);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//	public static void testGetStatistics(Interfaces inter) throws Exception
//	{
//		ArrayList<OBDtoMonTrafficVServerF5> vsMonDtoList = new OBAdcMonitorF5().getVServerTrafficF5(9, "175.196.72.100", "admin", "openbase", "");
//
//		testPrintStatistics(vsMonDtoList);
//	}

//	public static void testPrintStatistics(ArrayList<OBDtoMonTrafficVServerF5> vsMonDtoList)
//	{
//		for(OBDtoMonTrafficVServerF5 vsMon: vsMonDtoList)
//		{
//			System.out.println("-------------------------------------");
//			System.out.println("virtual server: " + vsMon.getName());
//			System.out.println("\t status: " + vsMon.getStatus());
//
//			Pool.testPrintStatistics(vsMon.getPoolList());
//		}
//	}

	@Override
	public ArrayList<OBDtoMonTrafficVServer> getVServerTrafficAlteon(Integer adcIndex, String ipaddress,
			String accountID, String password, String swVersion)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			ArrayList<OBDtoMonL2Ports> list = new OBAdcMonitorF5().getL2PortsInfo("192.168.200.11", "", "openbase");
//			System.out.println(list);			
//		}
//		catch(Exception e1)
//		{
//			e1.printStackTrace();
//		}
//	}

//	public static void main(String[] args)
//	{
//		ArrayList<OBDtoVServerStatus> list;
//		try
//		{
//			list = new OBAdcMonitorF5().getVServerStatus(20);
//			for(OBDtoVServerStatus stat: list)
//				System.out.println(stat);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	@Override
	public ArrayList<OBDtoAdcVServerAlteon> getSlbStatusAlteon(int adcIndex) throws OBException {
		return null;
	}

	@Override
	public ArrayList<OBDtoAdcVServerF5> getSlbStatusAllF5(int adcIndex) throws OBException {
		OBDtoAdcInfo adcInfo;
		ArrayList<OBDtoAdcVServerF5> list;
		try {
			OBVServerDB vserverDB = new OBVServerDB();
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
					adcInfo.getSnmpInfo());
			ArrayList<OBDtoAdcVServerF5> vsList = vserverDB.getVServerListF5InternalUse(adcInfo.getIndex(), null); // 전체
																													// vs
																													// 목록
																													// 구하기
			list = snmp.getSlbStatusAll(adcInfo.getAdcType(), adcInfo.getSwVersion(), vsList);

			OBAdcMonitorDB adcMonitorDB = new OBAdcMonitorDB();
			HashMap<String, OBDtoVServerStatus> vsOldStatus = vserverDB.getVirtualServerStatus(adcInfo.getIndex());
			adcMonitorDB.writeSlbStatusAllF5(adcIndex, list);
			// VS Status에 대한 장애로그를 생성 ex)Virtual Server가 다운되었습니다.(장비명: /Common/testvs,
			// Virtual Server IP: 192.168.200.239)
			adcMonitorDB.writeAdcFaultLogVSUpDownF5(adcInfo.getIndex(), vsOldStatus, list);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return list;
	}

	public ArrayList<OBDtoAdcVServerF5> getSlbStatusPartialF5(int adcIndex, ArrayList<String> vsIndexList)
			throws OBException {
		OBDtoAdcInfo adcInfo;
		ArrayList<OBDtoAdcVServerF5> list;
		try {
			OBVServerDB vserverDB = new OBVServerDB();
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
					adcInfo.getSnmpInfo());
			ArrayList<OBDtoAdcVServerF5> vsList = vserverDB.getVServerListF5InternalUse(adcInfo.getIndex(),
					vsIndexList); // 선택 vs 목록 구하기
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "calling getSlbStatusPartial().");
			list = snmp.getSlbStatusPartial(adcInfo.getAdcType(), adcInfo.getSwVersion(), vsList);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "done getSlbStatusPartial().");
			OBAdcMonitorDB adcMonitorDB = new OBAdcMonitorDB();
			vserverDB.getVirtualServerStatus(adcInfo.getIndex()); // TODO
			adcMonitorDB.writeSlbStatusPartialF5(adcIndex, list); // 선택 vs 상태 만 업데이트 한다. 선택 vs의 멤버 상태만 업데이트 한다.
			// VS Status에 대한 장애로그를 생성 ex)Virtual Server가 다운되었습니다.(장비명: /Common/testvs,
			// Virtual Server IP: 192.168.200.239)
			// adcMonitorDB.writeAdcFaultLogVSUpDownF5(adcInfo.getIndex(), vsOldStatus,
			// list); //TODO
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return list;
	}

	@Override
	public ArrayList<OBDtoMonTrafficVServer> getVServerTrafficPAS(Integer adcIndex, String ipaddress, String accountID,
			String password, String swVersion) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		return null;
	}

	@Override
	public ArrayList<OBDtoAdcVServerPAS> getSlbStatusPAS(int adcIndex) throws OBException {
		return null;
	}

	@Override
	public ArrayList<OBDtoMonTrafficVServer> getVServerTrafficPASK(Integer adcIndex, String ipaddress, String accountID,
			String password, String swVersion) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		return null;
	}

	@Override
	public ArrayList<OBDtoAdcVServerPASK> getSlbStatusPASK(int adcIndex) throws OBException {
		return null;
	}
}
