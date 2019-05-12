package kr.openbase.adcsmart.service.impl.pask;

import java.util.ArrayList;
import java.util.HashMap;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBAdcMonitor;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficPoolMembers;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficVServer;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcVServerPAS;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolMemberPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcVServerPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.OBAdcPASKHandler;
import kr.openbase.adcsmart.service.impl.pask.handler.OBCLIParserPASK;
import kr.openbase.adcsmart.service.snmp.pask.OBSnmpPASK;
import kr.openbase.adcsmart.service.snmp.pask.dto.OBDtoMonTrafficPoolMembersPASK;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;

public class OBAdcMonitorPASK implements OBAdcMonitor
{
	@Override
	public ArrayList<OBDtoMonTrafficVServer> getVServerTrafficAlteon(Integer adcIndex, String ipaddress, String accountID, String password, String swVersion) throws OBExceptionUnreachable, OBExceptionLogin, OBException
	{
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoMonTrafficVServer> getVServerTrafficF5(Integer adcIndex, String ipaddress, String accountID, String password, String swVersion) throws OBExceptionUnreachable, OBExceptionLogin, OBException
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
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

//	public static void main(String[] args)
//	{
//		OBDatabase db = new OBDatabase();
//		try
//		{
//			db.openDB();
//			ArrayList<OBDtoMonL2Ports> list = new OBAdcMonitorPASK().getL2PortsInfo("192.168.200.110", "", "default", db);
//			db.closeDB();
//			System.out.println(list);			
//		}
//		catch(Exception e1)
//		{
//			db.closeDB();
//			e1.printStackTrace();
//		}
//	}

	@Override
	public ArrayList<OBDtoAdcVServerAlteon> getSlbStatusAlteon(int adcIndex) throws OBException
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
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}
	
	public ArrayList<OBDtoAdcVServerPASK> getSlbStatusPASK(int adcIndex, OBDatabase db) throws OBException
	{// telnet을 이용해서 추출한다. 
		ArrayList<OBDtoAdcVServerPASK>  retVal = null;
		OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
		OBAdcPASKHandler adcHandler = OBCommon.getValidPASKHandler(adcInfo.getSwVersion());
		try
		{
			adcHandler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
			adcHandler.login();
			
			retVal = getSlbStatusPASK(adcIndex, db, adcHandler);
			adcHandler.disconnect();
		}
		catch(OBException e)
		{
			adcHandler.disconnect();
			throw e;
		}
		catch(Exception e)
		{
			adcHandler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}
	
	public ArrayList<OBDtoAdcVServerPASK> getSlbStatusPASK(int adcIndex, OBDatabase db, OBAdcPASKHandler adcHandler) throws OBException
	{// telnet을 이용해서 추출한다. 
		ArrayList<OBDtoAdcVServerPASK>  retVal = null;
		try
		{
			String infoSlbDump = adcHandler.cmndShowInfoSlb();
			retVal = new OBCLIParserPASK().parseSlbStatus(adcIndex, infoSlbDump);
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
	
	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할 예정이다.
	public ArrayList<OBDtoAdcVServerPASK> getSlbStatusPASK(int adcIndex, OBAdcPASKHandler adcHandler) throws OBException
	{// telnet을 이용해서 추출한다. 
		ArrayList<OBDtoAdcVServerPASK>  retVal = null;
		try
		{
			String infoSlbDump = adcHandler.cmndShowInfoSlb();
			retVal = new OBCLIParserPASK().parseSlbStatus(adcIndex, infoSlbDump);
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
//		OBDatabase db = new OBDatabase();
//		try
//		{
//			db.openDB();
//			ArrayList<OBDtoMonTrafficPoolMembers> list = new OBAdcMonitorPASK().getPoolMembersTrafficPASK(9, "192.168.200.110", db);
//			db.closeDB();
//			System.out.println(list);			
//		}
//		catch(Exception e1)
//		{
//			db.closeDB();
//			e1.printStackTrace();
//		}
//	}
	
	public ArrayList<OBDtoMonTrafficPoolMembers> getPoolMembersTrafficPASK(Integer adcIndex, String ipaddress, OBDatabase db) throws OBException
	{//
		OBDtoAdcInfo adcInfo;
		ArrayList<OBDtoMonTrafficPoolMembers> retVal = new ArrayList<OBDtoMonTrafficPoolMembers>();
		
		try
		{
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			ArrayList<OBDtoAdcVServerPASK> vsList = new OBAdcVServerPASK().getVServerListPASK(adcInfo.getIndex(), null, null);
			
			OBSnmpPASK snmp = new OBSnmpPASK(ipaddress, adcInfo.getSnmpInfo());
			HashMap<String, OBDtoMonTrafficPoolMembersPASK> hashMap = snmp.getTrafficPoolMembers(adcInfo.getIndex(), adcInfo.getAdcType(), adcInfo.getSwVersion());
			for(OBDtoAdcVServerPASK vsObj:vsList)
			{
				OBDtoAdcPoolPASK poolObj = vsObj.getPool();
				if(poolObj==null)
					continue;
				for(OBDtoAdcPoolMemberPASK memObj:poolObj.getMemberList())
				{
					OBDtoMonTrafficPoolMembers obj = new OBDtoMonTrafficPoolMembers();
					obj.setVsIndex(vsObj.getDbIndex());
					obj.setVsIPAddress(vsObj.getvIP());
					obj.setVsName(vsObj.getName());
					obj.setPoolIndex(poolObj.getDbIndex());
					obj.setPoolName(poolObj.getName());
					obj.setNodeAddress(memObj.getIpAddress());
					obj.setNodeID(memObj.getId());
					obj.setNodePort(memObj.getPort());
					obj.setSrvPort(vsObj.getSrvPort());

					String mapKey = vsObj.getName() + "_" + memObj.getId();
					OBDtoMonTrafficPoolMembersPASK memInfo = hashMap.get(mapKey);
					if(memInfo!=null)
					{	
						obj.setPktsIn(memInfo.getPktsIn());
						obj.setPktsOut(memInfo.getPktsOut());
						obj.setBytesIn(memInfo.getBytesIn());
						obj.setBytesOut(memInfo.getBytesOut());
						obj.setCurConns(memInfo.getCurConns());
					}
					else
					{	
						obj.setPktsIn(0);
						obj.setPktsOut(0);
						obj.setBytesIn(0);
						obj.setBytesOut(0);
						obj.setCurConns(0);
					}
						
					retVal.add(obj);
				}
			}
		}
		catch(OBExceptionUnreachable e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE);
		}
		catch(OBExceptionLogin e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN);//, e.getMessage());
		}		
		catch(OBException e)
		{
			throw e;//new OBException(e.getMessage());
		}		
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return retVal;
	}
	
	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할 예정이다.
	public ArrayList<OBDtoMonTrafficPoolMembers> getPoolMembersTrafficPASK(Integer adcIndex, String ipaddress) throws OBException
	{//
		OBDtoAdcInfo adcInfo;
		ArrayList<OBDtoMonTrafficPoolMembers> retVal = new ArrayList<OBDtoMonTrafficPoolMembers>();
		
		try
		{
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			ArrayList<OBDtoAdcVServerPASK> vsList = new OBAdcVServerPASK().getVServerListPASK(adcInfo.getIndex(), null, null);
			
			OBSnmpPASK snmp = new OBSnmpPASK(ipaddress, adcInfo.getSnmpInfo());
			HashMap<String, OBDtoMonTrafficPoolMembersPASK> hashMap = snmp.getTrafficPoolMembers(adcInfo.getIndex(), adcInfo.getAdcType(), adcInfo.getSwVersion());
			for(OBDtoAdcVServerPASK vsObj:vsList)
			{
				OBDtoAdcPoolPASK poolObj = vsObj.getPool();
				if(poolObj==null)
					continue;
				for(OBDtoAdcPoolMemberPASK memObj:poolObj.getMemberList())
				{
					OBDtoMonTrafficPoolMembers obj = new OBDtoMonTrafficPoolMembers();
					obj.setVsIndex(vsObj.getDbIndex());
					obj.setVsIPAddress(vsObj.getvIP());
					obj.setVsName(vsObj.getName());
					obj.setPoolIndex(poolObj.getDbIndex());
					obj.setPoolName(poolObj.getName());
					obj.setNodeAddress(memObj.getIpAddress());
					obj.setNodeID(memObj.getId());
					obj.setNodePort(memObj.getPort());
					obj.setSrvPort(vsObj.getSrvPort());

					String mapKey = vsObj.getName() + "_" + memObj.getId();
					OBDtoMonTrafficPoolMembersPASK memInfo = hashMap.get(mapKey);
					if(memInfo!=null)
					{	
						obj.setPktsIn(memInfo.getPktsIn());
						obj.setPktsOut(memInfo.getPktsOut());
						obj.setBytesIn(memInfo.getBytesIn());
						obj.setBytesOut(memInfo.getBytesOut());
						obj.setCurConns(memInfo.getCurConns());
					}
					else
					{	
						obj.setPktsIn(0);
						obj.setPktsOut(0);
						obj.setBytesIn(0);
						obj.setBytesOut(0);
						obj.setCurConns(0);
					}
						
					retVal.add(obj);
				}
			}
		}
		catch(OBExceptionUnreachable e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE);
		}
		catch(OBExceptionLogin e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN);//, e.getMessage());
		}		
		catch(OBException e)
		{
			throw e;//new OBException(e.getMessage());
		}		
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return retVal;
	}

	@Override
	public ArrayList<OBDtoMonTrafficVServer> getVServerTrafficPASK(Integer adcIndex, String ipaddress, String accountID, String password, String swVersion) throws OBExceptionUnreachable, OBExceptionLogin, OBException
	{
	 // DB에서 VServer 목록을 추출하여 ID을 확인한다.
        OBDtoAdcInfo adcInfo;
        ArrayList<OBDtoMonTrafficVServer> retVal = new ArrayList<OBDtoMonTrafficVServer>();

        try
        {
            adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
            OBSnmpPASK snmp = new OBSnmpPASK(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
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
			
//	public static void main(String[] args)
//	{
//		try
//		{
//			ArrayList<OBDtoMonL2Ports> list = new OBAdcMonitorPASK().getL2PortsInfo("192.168.200.120", "", "default");
//			System.out.println(list);			
//		}
//		catch(Exception e1)
//		{
//			e1.printStackTrace();
//		}
//	}

	@Override
	public ArrayList<OBDtoAdcVServerPASK> getSlbStatusPASK(int adcIndex) throws OBException
	{
		OBDatabase db=new OBDatabase();
		ArrayList<OBDtoAdcVServerPASK> retVal = null;
		try
		{
			db.openDB();
			retVal = getSlbStatusPASK(adcIndex, db);
		}
		catch(OBException e1)
		{
			throw e1;
		}	
		finally
		{
			if(db!=null) db.closeDB();
		}	
		return retVal;
	}
}
