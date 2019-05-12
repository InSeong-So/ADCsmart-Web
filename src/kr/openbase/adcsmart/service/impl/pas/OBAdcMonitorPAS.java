package kr.openbase.adcsmart.service.impl.pas;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBAdcMonitor;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficPoolMembers;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficVServer;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcVServerPAS;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcVServerPASK;
import kr.openbase.adcsmart.service.snmp.pas.OBSnmpPAS;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;

public class OBAdcMonitorPAS implements OBAdcMonitor
{
	@Override
	public ArrayList<OBDtoMonTrafficVServer> getVServerTrafficAlteon(
			Integer adcIndex, String ipaddress, String accountID,
			String password, String swVersion) throws OBExceptionUnreachable,
			OBExceptionLogin, OBException
	{
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoMonTrafficVServer> getVServerTrafficF5(
			Integer adcIndex, String ipaddress, String accountID,
			String password, String swVersion) throws OBExceptionUnreachable,
			OBExceptionLogin, OBException
	{
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			ArrayList<OBDtoMonTrafficVServer> list = new OBAdcMonitorPAS().getVServerTrafficPAS(1, "192.168.200.110", "root", "admin", "");
//			for(OBDtoMonTrafficVServer info:list)
//				System.out.println(info);
//		}
//		catch(OBExceptionUnreachable e)
//		{
//			e.printStackTrace();
//		}
//		catch(OBExceptionLogin e)
//		{
//			e.printStackTrace();
//		}
//		catch(OBException e)
//		{
//			e.printStackTrace();
//		}
//	}
	
	@Override
	public ArrayList<OBDtoMonTrafficVServer> getVServerTrafficPAS(Integer adcIndex, String ipaddress, String accountID,	String password, String swVersion) throws OBExceptionUnreachable, OBExceptionLogin, OBException
	{
		// DB에서 VServer 목록을 추출하여 ID을 확인한다.
		OBDtoAdcInfo adcInfo;
		ArrayList<OBDtoMonTrafficVServer> retVal = new ArrayList<OBDtoMonTrafficVServer>();
		
		try
		{
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			OBSnmpPAS snmp = new OBSnmpPAS(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
			retVal = snmp.getTrafficVServer(adcInfo.getAdcType(), adcInfo.getSwVersion()); 
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		return retVal;
	}
//	
//	public static void main(String[] args)
//	{
//		try
//		{
//			ArrayList<OBDtoMonL2Ports> list = new OBAdcMonitorPAS().getL2PortsInfo("192.168.200.120", "", "default");
//			System.out.println(list);			
//		}
//		catch(Exception e1)
//		{
//			e1.printStackTrace();
//		}
//	}
	
	@Override
	public ArrayList<OBDtoAdcVServerAlteon> getSlbStatusAlteon(int adcIndex)
			throws OBException
	{
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerF5> getSlbStatusAllF5(int adcIndex) throws OBException
	{
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerPAS> getSlbStatusPAS(int adcIndex) throws OBException
	{
		OBDtoAdcInfo adcInfo;
		ArrayList<OBDtoAdcVServerPAS>  retVal = null;
		try
		{
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			OBSnmpPAS snmp = new OBSnmpPAS(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
			retVal = snmp.getSlbStatus(adcInfo.getIndex(), adcInfo.getAdcType(), adcInfo.getSwVersion());
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}
		
	public ArrayList<OBDtoMonTrafficPoolMembers> getPoolMembersTrafficPAS(Integer adcIndex, String ipaddress) throws OBException
	{
		OBDtoAdcInfo adcInfo;
		ArrayList<OBDtoMonTrafficPoolMembers> list;
		
		try
		{
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			OBSnmpPAS snmp = new OBSnmpPAS(ipaddress, adcInfo.getSnmpInfo());
			list = snmp.getTrafficPoolMembers(adcIndex, OBDefine.ADC_TYPE_PIOLINK_PAS, "");
		}
		catch(OBException e1)
		{
			throw new OBException(e1.getMessage());
		}		
		return list;
	}

	@Override
	public ArrayList<OBDtoMonTrafficVServer> getVServerTrafficPASK(Integer adcIndex, String ipaddress, String accountID, String password, String swVersion) throws OBExceptionUnreachable, OBExceptionLogin, OBException
	{
		return null;
	}

	@Override
	public ArrayList<OBDtoAdcVServerPASK> getSlbStatusPASK(int adcIndex) throws OBException
	{
		return null;
	}
}
