package kr.openbase.adcsmart.service.impl.alteon;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBAdcMonitor;
import kr.openbase.adcsmart.service.impl.OBVServerDB;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficServiceAlteon;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficVServer;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcVServerPAS;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcVServerPASK;
import kr.openbase.adcsmart.service.snmp.alteon.OBSnmpAlteon;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBAdcMonitorAlteon implements OBAdcMonitor
{
//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDatabase db=new OBDatabase();
//			db.openDB();
//			String now = OBDateTime.now();
//			ArrayList<OBDtoMonTrafficServiceAlteon> list = new OBAdcMonitorAlteon().getVServiceTrafficAlteon(1, "192.168.100.11", "admin", "admin", "");
//			for(OBDtoMonTrafficServiceAlteon status:list)
//				System.out.println(status);
//			
//			new OBAdcMonitorDB().writeVServiceTrafficAlteon(now, 1, list, db);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	
	public ArrayList<OBDtoMonTrafficServiceAlteon> getVServiceTrafficAlteon(Integer adcIndex, String ipaddress, String accountID, String password, String swVersion) throws OBExceptionUnreachable, OBExceptionLogin, OBException//	throws OBExceptionSocketUnreachable, SocketException, IOException, Exception
	{
		// DB에서 VServer 목록을 추출하여 ID을 확인한다.
		OBDtoAdcInfo adcInfo;
		
		ArrayList<OBDtoMonTrafficServiceAlteon> resultList = new ArrayList<OBDtoMonTrafficServiceAlteon>();
		try
		{
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			resultList = getVServiceTrafficSnmp(adcInfo);
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		return resultList;
	}
	
	private ArrayList<OBDtoMonTrafficServiceAlteon> getVServiceTrafficSnmp(OBDtoAdcInfo adcInfo) throws OBException//	throws OBExceptionSocketUnreachable, SocketException, IOException, Exception
	{
		ArrayList<OBDtoMonTrafficServiceAlteon> retVal = new ArrayList<OBDtoMonTrafficServiceAlteon>();

		try
		{
		    OBSnmpAlteon snmp = OBCommon.getValidSnmpAlteonHandler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
			ArrayList<OBDtoAdcVServerAlteon> vsList = new OBVServerDB().getVServerListAllAlteon(adcInfo.getIndex());
			retVal= snmp.getStatVirtServiceMember(adcInfo.getAdcType(), adcInfo.getSwVersion(), vsList);
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
	
//	public static void main(String[] args)
//	{
//		try
//		{
//			ArrayList<OBDtoMonTrafficVServer> list = new OBAdcMonitorAlteon().getVServerTrafficAlteon(1, "192.168.100.11", "admin", "admin", "");
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
	public ArrayList<OBDtoMonTrafficVServer> getVServerTrafficAlteon(Integer adcIndex, String ipaddress, String accountID, String password, String swVersion) throws OBExceptionUnreachable, OBExceptionLogin, OBException//	throws OBExceptionSocketUnreachable, SocketException, IOException, Exception
	{
		// DB에서 VServer 목록을 추출하여 ID을 확인한다.
		OBDtoAdcInfo adcInfo;
		ArrayList<OBDtoMonTrafficVServer> resultList = new ArrayList<OBDtoMonTrafficVServer>();
		
		try
		{
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			resultList = getVServerTrafficSnmp(adcInfo);
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		return resultList;
	}

	private ArrayList<OBDtoMonTrafficVServer> getVServerTrafficSnmp(OBDtoAdcInfo adcInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException//	throws OBExceptionSocketUnreachable, SocketException, IOException, Exception
	{
		ArrayList<OBDtoMonTrafficVServer> retVal = new ArrayList<OBDtoMonTrafficVServer>();
		OBDatabase db = new OBDatabase();

		try
		{
			db.openDB();
			OBSnmpAlteon snmp = OBCommon.getValidSnmpAlteonHandler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
                    adcInfo.getSnmpInfo());
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
		finally
		{
			if(db!=null) db.closeDB();
		}
		return retVal;
	}
	
	@Override
	public ArrayList<OBDtoMonTrafficVServer> getVServerTrafficF5(
			Integer adcIndex, String ipaddress, String accountID,
			String password, String swVersion) throws OBExceptionUnreachable,
			OBExceptionLogin, OBException
	{
		return null;
	}
	
//	public static void main(String[] args)
//	{
//		try
//		{
//			ArrayList<OBDtoMonL2Ports> list = new OBAdcMonitorAlteon().getL2PortsInfo("192.168.100.11", "", "anwkddo6619");
//			System.out.println(list);			
//		}
//		catch(Exception e1)
//		{
//			e1.printStackTrace();
//		}
//	}	
	
//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDatabase db=new OBDatabase();
//			db.openDB();
//			ArrayList<OBDtoVServerStatus> list = new OBAdcMonitorAlteon().getVServerStatus(22);
//			for(OBDtoVServerStatus status:list)
//				System.out.println(status);			
//		}
//		catch(Exception e1)
//		{
//			e1.printStackTrace();
//		}
//	}	
	
	@Override
	public ArrayList<OBDtoAdcVServerF5> getSlbStatusAllF5(int adcIndex) throws OBException
	{
		return null;
	}
	
	@Override
	public ArrayList<OBDtoAdcVServerAlteon> getSlbStatusAlteon(int adcIndex) throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));

		OBDtoAdcInfo adcInfo;
		ArrayList<OBDtoAdcVServerAlteon> list;
		
		try
		{
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			OBSnmpAlteon snmp = OBCommon.getValidSnmpAlteonHandler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
			ArrayList<OBDtoAdcVServerAlteon> vsList = new OBVServerDB().getVServerListAllAlteon(adcInfo.getIndex()); 
            ArrayList<OBDtoAdcPoolAlteon> backupGroupList = new OBVServerDB().getPoolBackupInfoAlteon(adcInfo.getIndex());
            ArrayList<OBDtoAdcPoolMemberAlteon> backupRealList = new OBVServerDB().getNodeBackupInfo(adcInfo.getIndex());
			list = snmp.getSlbStatus(adcInfo.getAdcType(), adcInfo.getSwVersion(), vsList, backupGroupList, backupRealList);
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}	
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result-size:%d", list.size()));
		return list;
	}

	@Override
	public ArrayList<OBDtoMonTrafficVServer> getVServerTrafficPAS(Integer adcIndex, String ipaddress, String accountID,	String password, String swVersion) throws OBExceptionUnreachable, OBExceptionLogin, OBException
	{
		return null;
	}

	@Override
	public ArrayList<OBDtoAdcVServerPAS> getSlbStatusPAS(int adcIndex)
			throws OBException
	{
		return null;
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

//	public static void main(String[] args)
//	{
//		ArrayList<OBDtoPoolMembersStatus> list;
//		try
//		{
//			OBDatabase db=new OBDatabase();
//			db.openDB();
//			OBAdcMonitor mon;
//			mon = new OBAdcMonitorAlteon();
//		
//			list = mon.getPoolMembersStatus(23, db);
//	
//			new OBAdcMonitorDB().writePoolMemberStatus(23, list, db);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
////		try
////		{
////			OBDatabase db=new OBDatabase();
////			db.openDB();
////			ArrayList<OBDtoPoolMembersStatus> list = new OBAdcMonitorAlteon().getPoolMembersStatus(23, db);
////			for(OBDtoPoolMembersStatus status:list)
////				System.out.println(status);			
////		}
////		catch(Exception e1)
////		{
////			e1.printStackTrace();
////		}
//	}	
}
