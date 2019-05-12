package kr.openbase.adcsmart.service.snmp.pas;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.snmp4j.Snmp;
import org.snmp4j.smi.Counter64;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.VariableBinding;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSnmpInfo;
import kr.openbase.adcsmart.service.dto.OBDtoConnectionData;
import kr.openbase.adcsmart.service.dto.OBDtoHddInfo;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonL2Ports;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficPoolMembers;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficVServer;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolMemberPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcVServerPAS;
import kr.openbase.adcsmart.service.snmp.OBSnmp;
import kr.openbase.adcsmart.service.snmp.OBSnmpOidDB;
import kr.openbase.adcsmart.service.snmp.alteon.dto.DtoRptOPL3;
import kr.openbase.adcsmart.service.snmp.alteon.dto.DtoRptOPSys;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidCfgPorts;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidFaultCheckHW;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidInfoAdcResc;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidInfoCfgPAS;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidRptOPGen;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidRptOPL2;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidRptOPL3;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidRptOPL4;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidRptOPSys;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidStatVirtServer;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptConnStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptCpu;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptL3Gateway;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptL3Int;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptLinkStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptMem;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptOPGen;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptOPL2;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptOPL4;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptPMStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptPortStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptStgStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptStpStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptTrunkStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptVSStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptVlanStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoSnmpAdcResc;
import kr.openbase.adcsmart.service.snmp.dto.OBDtoAdcPerformanceData;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBSnmpPAS extends OBSnmp
{
//	public OBSnmpPAS(int version, String host)
//	{
//        setSnmpVersion(version);
//        setHost(host);
//	}  
//	public OBSnmpPAS(String host)
//	{
//        setSnmpVersion(OBSnmp.VERSION_2C);
//        setHost(host);
//	}
	
	public OBSnmpPAS(String host, OBDtoAdcSnmpInfo authInfo)
	{
        setHost(host);
    	this.setAuthInfo(authInfo);
    	if(authInfo.getVersion()==3)
    	{
    		setSnmpVersion(OBSnmp.VERSION_3);
    	}
    	else // if(snmpInfo.getVersion()==2) //default
        {
        	setSnmpVersion(OBSnmp.VERSION_2C);
        }
	}	
	
	public OBDtoAdcPerformanceData  getAdcPerformances(int adcType,  String swVersion)  throws OBException
    {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBDtoAdcPerformanceData retVal = new OBDtoAdcPerformanceData();
		OBSnmpOidDB oidDB = new OBSnmpOidDB();
		
		try
		{
			DtoOidStatVirtServer oid = oidDB.getStatVirtServer(adcType, swVersion);
			retVal = getAdcPerformanceData(oid);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", retVal));
			return retVal;
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
    }
	
	/**
	 * 
	 * @param oid
	 * @param community
	 * @return 리턴되는 값은 bps, pps, cps 단위이다.
	 * @throws OBException
	 */
	private OBDtoAdcPerformanceData getAdcPerformanceData(DtoOidStatVirtServer oid)  throws OBException
    {
		OBDtoAdcPerformanceData retVal = new OBDtoAdcPerformanceData();
		retVal.setOccurTime(OBDateTime.toTimestamp(OBDateTime.now()));
		List<VariableBinding> tmpList;
    	
    	try
		{
    		String oidReq = "";
			long tmpVal = 0;
    		// bps in
    		oidReq = oid.getBytesIn();
			tmpList = walk(oidReq);
			tmpVal = 0;
			for(VariableBinding var:tmpList)
			{
				tmpVal += var.getVariable().toLong();
			}
			retVal.setClientBytesIn(tmpVal);
			
			// bps out
			oidReq = oid.getBytesOut();
			tmpList = walk(oidReq);
			tmpVal = 0;
			for(VariableBinding var:tmpList)
			{
				tmpVal += var.getVariable().toLong();
			}
			retVal.setClientBytesOut(tmpVal);
    		
			// pps in
			oidReq = oid.getPktsIn();
			tmpList = walk(oidReq);
			tmpVal = 0;
			for(VariableBinding var:tmpList)
			{
				tmpVal += var.getVariable().toLong();
			}
			retVal.setClientPktsIn(tmpVal);
			
			// pps out
			oidReq = oid.getPktsOut();
			tmpList = walk(oidReq);
			tmpVal = 0;
			for(VariableBinding var:tmpList)
			{
				tmpVal += var.getVariable().toLong();
			}
			retVal.setClientPktsOut(tmpVal);
			
			// cps 
			oidReq = oid.getCurConns();
			tmpList = walk(oidReq);
			tmpVal = 0;
			for(VariableBinding var:tmpList)
			{
				tmpVal += var.getVariable().toLong();
			}
			retVal.setClientCurConns(tmpVal);

			return retVal;
    	}
    	catch(OBException e)
    	{
    		throw e;
    	}  
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			
		} 
    }
//	public static void main(String[] args)
//	{
//		OBDatabase db = new OBDatabase();
//		try
//		{
//			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(1);
//			OBSnmpPAS snmp = new OBSnmpPAS(OBSnmp.VERSION_2C, adcInfo.getAdcIpAddress());
//
//			db.openDB();
//			DtoRptOPL4 info = snmp.getRptOPL4Info(adcInfo, db);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		
//	}
	
	public DtoRptOPL4 getRptOPL4Info(int adcIndex, int adcType, String swVersion)  throws OBException
    {
	    OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();
		
		DtoOidRptOPL4 oid;
		try
		{
			oid = oidDB.getRptOPL4Info(adcType, swVersion);
			ArrayList<OBDtoAdcVServerPAS> vsStatusList = getSlbStatus(adcIndex, adcType, swVersion);
			DtoRptOPL4 result = getRptOPL4Info(vsStatusList, oid);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;		
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			
		} 

    }
	
	private DtoRptOPL4 getRptOPL4Info(ArrayList<OBDtoAdcVServerPAS> vsStatusList, DtoOidRptOPL4 oid)  throws OBException
    {
		DtoRptOPL4 retVal = new DtoRptOPL4();
		
    	List<VariableBinding> tmpList;
    	String reqOid="";
		HashMap<String, DtoRptPMStatus> pmStatusMap = new HashMap<String, DtoRptPMStatus>();//key: vs이름+real id
		HashMap<String, DtoRptVSStatus> vsStatusMap = new HashMap<String, DtoRptVSStatus>();//key: vs이름.
		
		try
    	{
    		// poolmember status. vs 이름.  index, addr, status 정보만 추출한다.
    		reqOid=oid.getPoolMemberIndex();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						String vsName = parseOidName(var.getOid().toString(), reqOid);
						if(vsName==null)
						{
							OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("invalid data.(target:%s, getPoolMemberIndex:%s)", var.getOid().toString(), oid.getPoolMemberIndex()));
							continue;
						}

						// pool member 정보 저장.
						Integer poolIndex = var.getVariable().toInt();
						String  pmMapKey = vsName+"_"+poolIndex;
						if(pmStatusMap.get(pmMapKey)==null)
						{
							DtoRptPMStatus pmObj = new DtoRptPMStatus();
							pmObj.setPoolName(vsName);
							pmObj.setMemberIndex(poolIndex);
							pmObj.setStatus(-1);
							pmStatusMap.put(pmMapKey, pmObj);
						}
						
						// vs 정보 저장.
						if(vsStatusMap.get(vsName)==null)
						{
							DtoRptVSStatus vsObj = new DtoRptVSStatus();
							vsObj.setName(vsName);
							vsObj.setStatus(-1);// 초기값으로 설정함.
							vsStatusMap.put(vsObj.getName(), vsObj);
						}
					}
				}
	    	}
    		//pool member상태 정보 추출 작업 필요함.
	    	reqOid=oid.getPoolMemberStatusAvail();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					String vsName = parseOidName(var.getOid().toString(), reqOid);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					int vsNameLen = oidList.get(0);
					int poolIndex = oidList.get(vsNameLen);
					String  pmMapKey = vsName+"_"+poolIndex;
					DtoRptPMStatus pmObj = pmStatusMap.get(pmMapKey);
					if(pmObj != null)
					{
						pmObj.setStatus(convertNodeStatus(var.getVariable().toString()));
						
						// vs status 계산. avail: 모두 avail일 경우. disable: 모두 inactive일 경우. 나머지는 unavial.
//						DtoRptVSStatus vsObj = vsStatusMap.get(vsName);
//						if(vsObj != null)
//						{
//							vsObj.setStatus(calcVSStatus(vsObj.getStatus(), pmObj.getStatus()));
//						}
					}
				}
	    	}
	    	
    		// connection 정보.. vs의 총 합으로 계산한다.
			DtoRptConnStatus connStatus = new DtoRptConnStatus();
	    	reqOid=oid.getConcurrentConn();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				long currConn = 0;
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						currConn +=  var.getVariable().toLong();
					}
				}
				connStatus.setCurConn(currConn);
	    	}
			retVal.setConnStatus(connStatus);
	    	
    		// direct mode.. 지원하지 않음.
    	}
    	catch(OBException e)
    	{
    		throw e;
    	}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} 

    	retVal.setPmList(new ArrayList<DtoRptPMStatus>(pmStatusMap.values()));
    
		// 리스트 형태로 입력된 vsStatusList를 hashmap으로 변경하여 사용한다.
		HashMap<String, OBDtoAdcVServerPAS> vsSrcMap = new HashMap<String, OBDtoAdcVServerPAS>();//key: vs이름.
		for(OBDtoAdcVServerPAS obj:vsStatusList)
		{
			if(vsSrcMap.get(obj.getName())==null)
			{
				vsSrcMap.put(obj.getName(), obj);
			}
		}
    	//VS별 status를 업데이트한다.
		ArrayList<DtoRptVSStatus> vsRetVal = new ArrayList<DtoRptVSStatus>(vsStatusMap.values());
    	for(DtoRptVSStatus obj:vsRetVal)
    	{
    		OBDtoAdcVServerPAS vsInfo = vsSrcMap.get(obj.getName());
    		if(vsInfo!=null)
    		{
    			obj.setStatus(vsInfo.getStatus());
    		}
    	}
		 	
    	retVal.setVsList(vsRetVal);
		
		return retVal;
    }
	
//	private int calcVSStatus(int vsStatus, int memStatus)
//	{
//		int retVal = OBDefine.STATUS_UNAVAILABLE;
//		if(memStatus==OBDefine.STATUS_AVAILABLE)// 무조건 available;//
//			return OBDefine.STATUS_AVAILABLE;
//		
//		switch(vsStatus)
//		{
//		case OBDefine.STATUS_AVAILABLE:
////			if(memStatus==OBDefine.STATUS_AVAILABLE)
//			retVal= OBDefine.STATUS_AVAILABLE;
//			break;
//		case OBDefine.STATUS_DISABLE:
//			if(memStatus==OBDefine.STATUS_DISABLE)
//				retVal = OBDefine.STATUS_DISABLE;
//			break;
//		case OBDefine.STATUS_UNAVAILABLE:
//			if(memStatus==OBDefine.STATUS_UNAVAILABLE)
//				retVal = OBDefine.STATUS_UNAVAILABLE;
//			break;
//		default:
//			retVal= memStatus;
//			break;
//		}
//		return retVal;
//	}
	
	/**
	 * virtual server status를 계산한다.
	 * - virtual server state가 disable이면 "꺼짐"
	 * - virtual server state가 enable이면 
	 * -- 멤버가 살아 있으면 "정상"
	 * -- 멤버가 모두 죽어 있으면 "단절"
	 * @param vsObj
	 * @return OBDefine.VS_STATUS.DISABLE / OBDefine.VS_STATUS.AVAILABLE / OBDefine.VS_STATUS.UNAVAILABLE
	 * @throws OBException
	 */
	private Integer calcVSStatus(OBDtoAdcVServerPAS vsObj) throws OBException
	{
		if(vsObj.getState().equals(OBDefine.VS_STATE.DISABLED)) //virtual server가 disable이면 "꺼짐"
		{
			return OBDefine.VS_STATUS.DISABLE;
		}
		else //virtual server가 enable이면 멤버를 파악해서 "정상"/"단절"
		{
			for(OBDtoAdcPoolMemberPAS memObj:vsObj.getPool().getMemberList())
			{
				if(memObj.getStatus().equals(OBDefine.MEMBER_STATUS.AVAILABLE))
				{
					return OBDefine.VS_STATUS.AVAILABLE;// 멤버 하나라도 available이면 availabe
				}
			}
			return OBDefine.VS_STATUS.UNAVAILABLE; //모든 멤버가 멀쩡하지 않다.
		}
	}
	
//	public static void main(String[] args)
//	{
//		OBDatabase db = new OBDatabase();
//		try
//		{
//			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(1);
//			OBSnmpPAS snmp = new OBSnmpPAS(OBSnmp.VERSION_2C, adcInfo.getAdcIpAddress());
//
//			db.openDB();
//			DtoRptOPL3 info = snmp.getRptOPL3Info(adcInfo, db);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		
//	}
	
	public DtoRptOPL3  getRptOPL3Info(int adcType,  String swVersion, OBDatabase db)  throws OBException
    {
	    OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();
		
		DtoOidRptOPL3 oid;
		try
		{
			oid = oidDB.getRptOPL3Info(adcType, swVersion);
			DtoRptOPL3 result = getRptOPL3Info(oid, db);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;	
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			
		} 
    }
	
	private DtoRptOPL3 getRptOPL3Info(DtoOidRptOPL3 oid, OBDatabase db)  throws OBException
    {
		DtoRptOPL3 result = new DtoRptOPL3();
		
    	Snmp snmp = openSnmp();
    	
    	List<VariableBinding> tmpList;
    	String reqOid="";
    	try
    	{
    		// interface info--index
    		HashMap<Integer, DtoRptL3Int> intMap = new HashMap<Integer, DtoRptL3Int>();
	    	reqOid=oid.getIntIndex();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						DtoRptL3Int intStatus = new DtoRptL3Int();
						intStatus.setIndex(var.getVariable().toInt());
						intMap.put(intStatus.getIndex(), intStatus);
					}
				}
	    	}
    		// interface info--addr
	    	reqOid=oid.getIntAddr();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
						DtoRptL3Int intStatus = intMap.get(oidList.get(0));
						intStatus.setAddr(var.getVariable().toString());
					}
				}
	    	}	    	
    		// interface info--netmask
	    	reqOid=oid.getIntNetmask();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
						DtoRptL3Int intStatus = intMap.get(oidList.get(0));
						intStatus.setNetmask(var.getVariable().toString());
					}
				}
	    	}	    	
    		// interface info--bcastaddr
	    	reqOid=oid.getIntBcastAddr();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
						DtoRptL3Int intStatus = intMap.get(oidList.get(0));
						intStatus.setBcastAddr(var.getVariable().toString());
					}
				}
	    	}
	    	// interface info--vlan
	    	reqOid=oid.getIntVlan();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
						DtoRptL3Int intStatus = intMap.get(oidList.get(0));
						intStatus.setVlanIndex(var.getVariable().toInt());
					}
				}
	    	}   		
	    	// interface info--status
	    	reqOid=oid.getIntStatus();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
						DtoRptL3Int intStatus = intMap.get(oidList.get(0));
						intStatus.setStatus(convertLinkStatus(var.getVariable().toInt()));
					}
				}
	    	} 
	    	ArrayList<DtoRptL3Int> intStatusList = new ArrayList<DtoRptL3Int>(intMap.values());
	    	result.setIntList(intStatusList);

	    	//gateway info
	    	HashMap<Integer, DtoRptL3Gateway> gwMap = new HashMap<Integer, DtoRptL3Gateway>();
	    	reqOid=oid.getGwIndex();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						DtoRptL3Gateway gwStatus = new DtoRptL3Gateway();
						gwStatus.setIndex(var.getVariable().toInt());
						gwMap.put(gwStatus.getIndex(), gwStatus);
					}
				}
	    	}
    		// gw info--addr
	    	reqOid=oid.getGwAddr();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
						DtoRptL3Gateway gwStatus = gwMap.get(oidList.get(0));
						gwStatus.setAddr(var.getVariable().toString());
					}
				}
	    	}	    
	    	//gw info--vlan
	    	reqOid=oid.getGwVlan();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
						DtoRptL3Gateway gwStatus = gwMap.get(oidList.get(0));
						gwStatus.setVlanIndex(var.getVariable().toInt());
					}
				}
	    	}	    
	    	//gw info--status
	    	reqOid=oid.getGwStatus();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
						DtoRptL3Gateway gwStatus = gwMap.get(oidList.get(0));
						gwStatus.setStatus(convertGWState(var.getVariable().toInt()));
					}
				}
	    	}	    
	    	ArrayList<DtoRptL3Gateway> gwStatusList = new ArrayList<DtoRptL3Gateway>(gwMap.values());
	    	result.setGwList(gwStatusList);   	
    	}
    	catch(OBException e)
    	{
    		closeSnmp(snmp);
    		throw e;
    	}    	
		catch(Exception e)
		{
			closeSnmp(snmp);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			
		} 

    	closeSnmp(snmp);
		return result;
    }
	
//	public static void main(String[] args)
//	{
//		OBDatabase db = new OBDatabase();
//		try
//		{
//			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(1);
//			OBSnmpPAS snmp = new OBSnmpPAS(OBSnmp.VERSION_2C, adcInfo.getAdcIpAddress());
//
//			db.openDB();
//			DtoRptOPL2 info = snmp.getRptOPL2Info(adcInfo, db);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		
//	}
	
	public DtoRptOPL2  getRptOPL2Info(int adcType,  String swVersion, OBDatabase db)  throws OBException
    {
	    OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();
		
		DtoOidRptOPL2 oid;
		try
		{
			oid = oidDB.getRptOPL2Info(adcType, swVersion);
			DtoRptOPL2 result = getRptOPL2Info(oid, db);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			
		} 

    }
	
	private int convertLinkStatus(int status)
	{
		switch(status)
		{
		case 1:// link up
			return OBDefine.L2_LINK_STATUS_UP;
		case 2:// down
			return OBDefine.L2_LINK_STATUS_DOWN;
		case 3:// disabled
			return OBDefine.L2_LINK_STATUS_DISABLED;
		case 4:// inoperative
			return OBDefine.L2_LINK_STATUS_INOPERATIVE;
		default:
			return OBDefine.L2_LINK_STATUS_INOPERATIVE;
		}
	}

	private int convertVlanState(int status)
	{
		switch(status)
		{
		case 2:// link up
			return OBDefine.L2_VLAN_STATE_ENABLED;
		case 3:// down
			return OBDefine.L2_VLAN_STATE_DISABLED;
		default:
			return OBDefine.L2_VLAN_STATE_DISABLED;
		}
	}
	
	private int convertStpState(int status)
	{
		switch(status)
		{
		case 1:// on
			return OBDefine.L2_STP_STATE_ENABLED;
		case 2:// off
			return OBDefine.L2_STP_STATE_DISABLED;
		default:
			return OBDefine.L2_STP_STATE_DISABLED;
		}
	}
	
	private int convertTrunkStatus(int status)
	{
		switch(status)
		{
		case 1:// on
			return OBDefine.L2_TRUNK_STATE_ENABLED;
		case 2:// off
			return OBDefine.L2_TRUNK_STATE_DISABLED;
		default:
			return OBDefine.L2_TRUNK_STATE_DISABLED;
		}
	}	
	
	private int convertStgStatus(int status)
	{
		switch(status)
		{//disabled:1, blocking:2, listening:3, learning:4, forwarding:5, broken:6, discarding:7
		case 1:// 
			return OBDefine.L2_STG_STATUS_DISABLED;
		case 2:// f
			return OBDefine.L2_STG_STATUS_BLOCKING;
		case 3:// f
			return OBDefine.L2_STG_STATUS_LISTENING;
		case 4:// f
			return OBDefine.L2_STG_STATUS_LEARNING;
		case 5:// f
			return OBDefine.L2_STG_STATUS_FORWARDING;
		case 6:// f
			return OBDefine.L2_STG_STATUS_BROKEN;
		case 7:// f
			return OBDefine.L2_STG_STATUS_DISCARDING;
		default:
			return OBDefine.L2_STG_STATUS_DISCARDING;
		}
	}
	
	private int convertGWState(int status)
	{
		switch(status)
		{
		case 1:// link up
			return OBDefine.L3_GW_STATUS_UP;
		case 2:// down
			return OBDefine.L3_GW_STATUS_FAILED;
		default:
			return OBDefine.L3_GW_STATUS_FAILED;
		}
	}

	private DtoRptOPL2 getRptOPL2Info(DtoOidRptOPL2 oid, OBDatabase db)  throws OBException
    {
		DtoRptOPL2 result = new DtoRptOPL2();
		
    	Snmp snmp = openSnmp();
    	
    	List<VariableBinding> tmpList;
    	String reqOid="";
    	try
    	{
    		// l2LinkUpInfo;
    		ArrayList<DtoRptLinkStatus> linkupList= new  ArrayList<DtoRptLinkStatus>();
 	    	HashMap<String, DtoRptPortStatus> portStatusMap = new HashMap<String, DtoRptPortStatus>();
	    	reqOid=oid.getLinkUpInfo();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
						DtoRptLinkStatus linkup = new DtoRptLinkStatus();
						linkup.setName(oidList.get(0).toString());
						linkup.setStatus(convertLinkStatus(var.getVariable().toInt()));
						linkupList.add(linkup);
						
						DtoRptPortStatus portStatus = new DtoRptPortStatus();
						portStatus.setName(oidList.get(0).toString());
						portStatus.setStatus(convertLinkStatus(var.getVariable().toInt()));
						portStatusMap.put(portStatus.getName(), portStatus);
					}
				}
	    	}
	    	result.setLinkupList(linkupList);
	    	
	    	// port status:
	    	// port status-- statechanged time;
	    	reqOid=oid.getLinkStateChanged();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
						
						DtoRptPortStatus portStatus = portStatusMap.get(oidList.get(0).toString());
						portStatus.setChangedTime(var.getVariable().toInt());
					}
				}
	    	}	    	
	    	// port status-- discard;
	    	reqOid=oid.getLinkDiscardsIn();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
						
						DtoRptPortStatus portStatus = portStatusMap.get(oidList.get(0).toString());
						portStatus.setDiscardsIn(var.getVariable().toLong());
					}
				}
	    	}	    	
	    	reqOid=oid.getLinkDiscardsOut();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
						
						DtoRptPortStatus portStatus = portStatusMap.get(oidList.get(0).toString());
						portStatus.setDiscardsOut(var.getVariable().toLong());
					}
				}
	    	}	    	
	    	
	    	// port status-- error
	    	reqOid=oid.getLinkErrorIn();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
						
						DtoRptPortStatus portStatus = portStatusMap.get(oidList.get(0).toString());
						portStatus.setErrorIn(var.getVariable().toLong());
					}
				}
	    	}	    	
	    	reqOid=oid.getLinkErrorOut();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
						
						DtoRptPortStatus portStatus = portStatusMap.get(oidList.get(0).toString());
						portStatus.setErrorOut(var.getVariable().toLong());
					}
				}
	    	}		    	
	    	ArrayList<DtoRptPortStatus> portStatusList = new ArrayList<DtoRptPortStatus>(portStatusMap.values());
	    	result.setPortStatusList(portStatusList);
	    	
	    	// vlan info
	    	HashMap<Integer, DtoRptVlanStatus> vlanStatusMap = new HashMap<Integer, DtoRptVlanStatus>();
	    	// vlan info--id
	    	reqOid=oid.getVlanId();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						DtoRptVlanStatus vlanStatus = new DtoRptVlanStatus();
						vlanStatus.setIndex(convertLinkStatus(var.getVariable().toInt()));
						vlanStatusMap.put(vlanStatus.getIndex(), vlanStatus);
					}
				}
	    	}		    	
	    	// vlan info--name
	    	reqOid=oid.getVlanName();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
						DtoRptVlanStatus vlanStatus = vlanStatusMap.get(oidList.get(0));
						if(vlanStatus!=null)
							vlanStatus.setName(var.getVariable().toString());
					}
				}
	    	}		    	
	    	// vlan info--ports
	    	reqOid=oid.getVlanPorts();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
						DtoRptVlanStatus vlanStatus = vlanStatusMap.get(oidList.get(0));
				    	ArrayList<Integer> memberList = parsePortList((OctetString) var.getVariable());
						vlanStatus.setPortList(memberList);
					}
				}
	    	}		    
	    	// vlan info--state
	    	reqOid=oid.getVlanState();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
						DtoRptVlanStatus vlanStatus = vlanStatusMap.get(oidList.get(0));
						vlanStatus.setStatus(convertVlanState(var.getVariable().toInt()));
					}
				}
	    	}		    
	    	ArrayList<DtoRptVlanStatus> vlanStatusList = new ArrayList<DtoRptVlanStatus>(vlanStatusMap.values());
	    	result.setVlanList(vlanStatusList);
	    	
	    	// stp info
	    	DtoRptStpStatus stpInfo = new DtoRptStpStatus();
	    	reqOid=oid.getStpState();
	    	if(reqOid!=null)
	    	{
		    	Object obj = getSnmp(snmp, reqOid);
		    	if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
		    	{
		    		stpInfo.setState(convertStpState(Integer.parseInt(obj.toString())));
		    	}
	    	}	
	    	if(stpInfo.getState()==OBDefine.L2_STP_STATE_ENABLED)
	    	{// enabled 된 상태에서는 세부 정보 추출.
		    	reqOid=oid.getStpInfo();
		    	if(reqOid!=null)
		    	{
					tmpList = walk(reqOid);
					ArrayList<DtoRptStgStatus> stgList = new ArrayList<DtoRptStgStatus>();
					for(VariableBinding var:tmpList)
					{
						if(var!=null)
						{
							ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
							
							DtoRptStgStatus stgStatus = new DtoRptStgStatus();
							stgStatus.setIndex(oidList.get(0));
							stgStatus.setStatus(convertStgStatus(var.getVariable().toInt()));
							stgList.add(stgStatus);
						}
					}
					stpInfo.setStgList(stgList);
		    	}		    
	    	}
	    	result.setStpInfo(stpInfo);
	    	
	    	// trunk info
			ArrayList<DtoRptTrunkStatus> trunkList = new ArrayList<DtoRptTrunkStatus>();
	    	reqOid=oid.getTrunkStatus();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
						
						DtoRptTrunkStatus trunkStatus = new DtoRptTrunkStatus();
						trunkStatus.setIndex(oidList.get(0));
						trunkStatus.setStatus(convertTrunkStatus(var.getVariable().toInt()));
						trunkList.add(trunkStatus);
					}
				}
	    	}	
	    	result.setTrunkList(trunkList);
    	}
    	catch(OBException e)
    	{
    		closeSnmp(snmp);
    		throw e;
    	}  
		catch(Exception e)
		{
			closeSnmp(snmp);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} 

    	closeSnmp(snmp);
		return result;
    }
	
//	public static void main(String[] args)
//	{
//		OBDatabase db = new OBDatabase();
//		try
//		{
//			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(9);
//			OBSnmpPAS snmp = new OBSnmpPAS(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
//
//			db.openDB();
//			DtoRptOPSys info = snmp.getRptOPSystemInfo(adcInfo, db);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		
//	}
	
	public DtoRptOPSys  getRptOPSystemInfo(int adcType,  String swVersion)  throws OBException
    {
	    OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();
		
		DtoOidRptOPSys oid;
		try
		{
			oid = oidDB.getRptOPSystemInfo(adcType, swVersion);
			DtoRptOPSys result = getRptOPSystemInfo(oid);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			
		} 
   }
	
	private int convertFanState(int status)
	{
		switch(status)
		{
		case 1:// OK
			return OBDefine.SYS_FAN_STATUS_OK;
		case 2:// fail
			return OBDefine.SYS_FAN_STATUS_FAIL;
		default:
			return OBDefine.SYS_FAN_STATUS_FAIL;
		}
	}

	private DtoRptOPSys getRptOPSystemInfo(DtoOidRptOPSys oid)  throws OBException
    {
		DtoRptOPSys result = new DtoRptOPSys();
		
    	Snmp snmp = openSnmp();
    	
    	List<VariableBinding> tmpList;
    	String reqOid="";
    	try
    	{
    		// sysUpTime;
	    	reqOid=oid.getUpTime();
	    	if(reqOid!=null)
	    	{
		    	Object obj = getSnmp(snmp, reqOid);
		    	if(checkObject(obj))
		    		result.setUpTime(obj.toString());
				else
				{
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get uptime. ip:%s, oid:%s", this.getHost(), reqOid));
				}
	    	}
	    	
	    	// sysLastApplyTime -- DB/Telnet을 이용해서 추출해야 함.
	    	
	    	// cpu info-MP
	    	ArrayList<DtoRptCpu> cpuList = new ArrayList<DtoRptCpu>();
	    	reqOid=oid.getCpuMPUtil64();
	    	if(reqOid!=null)
	    	{
		    	Object obj = getSnmp(snmp, reqOid);
		    	if(checkObject(obj))
		    	{
		    		DtoRptCpu cpuInfo = new DtoRptCpu();
		    		cpuInfo.setType(OBDefine.SYS_CPU_TYPE_MP);
		    		cpuInfo.setUsage(Integer.parseInt(obj.toString()));
		    		cpuList.add(cpuInfo);
		    	}
				else
				{
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get mp util64. ip:%s, oid:%s", this.getHost(), reqOid));
				}
	    	}	    	
	    	reqOid=oid.getCpuSPUtil64();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
			    		DtoRptCpu cpuInfo = new DtoRptCpu();
			    		cpuInfo.setType(OBDefine.SYS_CPU_TYPE_SP);
			    		cpuInfo.setUsage(var.getVariable().toInt());
			    		cpuList.add(cpuInfo);
					}
				}
	    	}
	    	result.setCpuList(cpuList);
	    	
	    	// memory info
	    	ArrayList<DtoRptMem> memList = new ArrayList<DtoRptMem>();
    		DtoRptMem memMPInfo = new DtoRptMem();
    		memMPInfo.setType(OBDefine.SYS_MEM_TYPE_MP);
	    	reqOid = oid.getMemMPTotal();
			if((reqOid!=null) && (!reqOid.isEmpty()))
			{
				Object obj = getSnmp(snmp, reqOid);
				if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
		    	{
		    		memMPInfo.setTotal(Long.parseLong(obj.toString()));
		    	}
				else
				{
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get mp memory total. ip:%s, oid:%s", this.getHost(), reqOid));
				}
		    }
	    	reqOid = oid.getMemMPUsed();
			if((reqOid!=null) && (!reqOid.isEmpty()))
			{
				Object obj = getSnmp(snmp, reqOid);
				if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
		    	{
		    		memMPInfo.setUsed(Long.parseLong(obj.toString()));
		    	}
				else
				{
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get mp memory used. ip:%s, oid:%s", this.getHost(), reqOid));
				}
		    }
	    	memList.add(memMPInfo);
	    	
	    	reqOid = oid.getMemSPTotal();
	    	tmpList = walk(reqOid);
	    	HashMap<Integer, Long> memSPMap = new HashMap<Integer, Long>();
			for(VariableBinding var:tmpList)
			{
				ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
				if(oidList!=null && (oidList.size())>0)
				{
					memSPMap.put(oidList.get(0), Long.parseLong(var.toValueString()));
				}
			}
			
	    	reqOid = oid.getMemSPUsed();
	    	tmpList = walk(reqOid);
			for(VariableBinding var:tmpList)
			{
				if(var!=null)
				{
	                ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if(memSPMap.get(oidList.get(0))>0)
					{
						DtoRptMem memInfo = new DtoRptMem();
						memInfo.setType(OBDefine.SYS_MEM_TYPE_SP);
						memInfo.setTotal(memSPMap.get(oidList.get(0)));
						memInfo.setUsed(var.getVariable().toLong());
						memList.add(memInfo);
					}
				}
			}
			result.setMemList(memList);
			
			// fan status
	    	reqOid=oid.getFanStatus();
	    	if(reqOid!=null)
	    	{
		    	Object obj = getSnmp(snmp, reqOid);
		    	if(checkObject(obj))
		    	{
		    		result.setFanStatus(convertFanState(Integer.parseInt(obj.toString())));
		    	}
				else
				{
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get fan status. ip:%s, oid:%s", this.getHost(), reqOid));
				}
	    	}
	    	
			// power supply
    	}
    	catch(OBException e)
    	{
    		closeSnmp(snmp);
    		throw e;
    	}  
    	catch(Exception e)
    	{
    		closeSnmp(snmp);
    		throw new OBException(e.getMessage());
    	}

    	closeSnmp(snmp);
		return result;
    }
//	public static void main(String[] args)
//	{
//		OBDatabase db = new OBDatabase();
//		try
//		{
//			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(1);
//			OBSnmpPAS snmp = new OBSnmpPAS(OBSnmp.VERSION_2C, adcInfo.getAdcIpAddress());
//
//			db.openDB();
//			DtoRptOPGen info = snmp.getRptOpGeneralInfo(adcInfo, db);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		
//	}
	
	public DtoRptOPGen  getRptOpGeneralInfo(int adcType,  String swVersion, OBDatabase db)  throws OBException
    {
	    OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();
		
		DtoOidRptOPGen oid;
		try
		{
			oid = oidDB.getRptOPGeneralInfo(adcType, swVersion);
			DtoRptOPGen result = getRptOpGeneralInfo(oid, db);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			
		} 
    }

	private String convertLicenseType(int code)
	{
		switch(code)
		{
		case 1: 
			return 	OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_LICENSE_UNLIMIT);//OBTerminologyDB.TYPE_LICENSE_UNLIMIT;
		case 2:
			return 	OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_LICENSE_TEMP);//OBTerminologyDB.TYPE_LICENSE_TEMP;
		case 3:
			return 	OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_LICENSE_DELETE);//OBTerminologyDB.TYPE_LICENSE_DELETE;
		case 4:
			return 	OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_LICENSE_EXPIRE);//OBTerminologyDB.TYPE_LICENSE_EXPIRE;
		default:
			return 	OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_LICENSE_EXPIRE);//OBTerminologyDB.TYPE_LICENSE_EXPIRE;
		}
	}
	
	private DtoRptOPGen getRptOpGeneralInfo(DtoOidRptOPGen oid, OBDatabase db)  throws OBException
    {
		DtoRptOPGen result = new DtoRptOPGen();
		
    	Snmp snmp = openSnmp();
    	
    	List<VariableBinding> tmpList;
    	String reqOid="";
    	try
    	{
    		// hostName
	    	reqOid=oid.getHostName();
	    	if(reqOid!=null)
	    	{
		    	Object obj = getSnmp(snmp, reqOid);
		    	if(checkObject(obj))
		    		result.setHostName(obj.toString());
				else
				{
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get host name. ip:%s, oid:%s", this.getHost(), reqOid));
				}
	    	}
	    	
	    	// model name--DB/Telnet을 이용해서 추출해야 함.
//	    	reqOid=oid.getModelName();
//	    	if(reqOid!=null)
//	    	{
//		    	Object obj = getSnmp(snmp, reqOid);
//		    	if(checkObject(obj))
//		    		result.setModelName(obj.toString());
//	    	}
	    	
	    	// os version
	    	reqOid=oid.getOsVersion();
	    	if(reqOid!=null)
	    	{
		    	Object obj = getSnmp(snmp, reqOid);
		    	if(checkObject(obj))
		    		result.setOsVersion(obj.toString());
				else
				{
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get os version. ip:%s, oid:%s", this.getHost(), reqOid));
				}
	    	}
	    	
	    	// license key
			reqOid=oid.getLicenseKey();
			tmpList = walk(reqOid);
			HashMap<Integer, String> licenseMap = new HashMap<Integer, String>();
			for(VariableBinding var:tmpList)
			{
				ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
				if(oidList!=null && (oidList.size())>0)
				{
					licenseMap.put(oidList.get(0), var.toValueString());
				}
			}    	
	    	// license type
			String licenseInfo="";
			reqOid=oid.getLicenseType();
			tmpList = walk(reqOid);
			for(VariableBinding var:tmpList)
			{
				ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
				if(oidList!=null && (oidList.size())>0)
				{
					String key = licenseMap.get(oidList.get(0));
					int code = Integer.parseInt(var.toValueString());
					String type = convertLicenseType(code);
					licenseInfo += String.format("%s:%s\n", key, type);
				}
			}    	
	    	result.setLicenseInfo(licenseInfo);
	    	
	    	// serial num
	    	reqOid=oid.getSerialNum();
	    	if(reqOid!=null)
	    	{
		    	Object obj = getSnmp(snmp, reqOid);
		    	if(checkObject(obj))
		    		result.setSerialNum(obj.toString());
				else
				{
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get serical number. ip:%s, oid:%s", this.getHost(), reqOid));
				}
	    	}	  
	    	
	    	// ipAddr
			reqOid=oid.getIpAddress();
			tmpList = walk(reqOid);
			for(VariableBinding var:tmpList)
			{
				ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
				if(oidList!=null && (oidList.size())>0)
				{
					result.setIpAddress(var.toValueString());
					break;
				}
			}    	
	    	
	    	// macAddr
			reqOid=oid.getMacAddress();
			tmpList = walk(reqOid);
			String macAddr="";
			for(VariableBinding var:tmpList)
			{
				ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
				if(oidList!=null && (oidList.size())>0)
				{
					if(macAddr.indexOf(var.toValueString())<0)// 동일한 MAC 주소는 제거.
							macAddr += String.format("%s\n", var.toValueString());
				}
			}
			result.setMacAddress(macAddr);
	    	
	    	// upTime
	    	reqOid=oid.getUpTime();
	    	if(reqOid!=null)
	    	{
		    	Object obj = getSnmp(snmp, reqOid);
		    	if(checkObject(obj))
		    		result.setUpTime(obj.toString());
				else
				{
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get up time. ip:%s, oid:%s", this.getHost(), reqOid));
				}
	    	}	    	
	    	// activeStandby
    	}
    	catch(OBException e)
    	{
    		closeSnmp(snmp);
    		throw e;
    	}  
    	catch(Exception e)
    	{
    		closeSnmp(snmp);
    		throw new OBException(e.getMessage());
    	}

    	closeSnmp(snmp);
		return result;
    }
	
//	public static void main(String[] args)
//	{
//		OBSnmpPAS snmp = new OBSnmpPAS(OBSnmp.VERSION_2C, "192.168.100.11");
//		OBDtoAdcInfo oid=new OBDtoAdcInfo();
//		oid.setAdcType(OBDefine.ADC_TYPE_ALTEON);
//		oid.setSwVersion("");		
//		OBDatabase db = new OBDatabase();
//		try
//		{
//			db.openDB();
//			OBDtoConnectionData info = snmp.getVServerConns(oid, 1, db);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		
//	}
	
	public OBDtoConnectionData  getVServerConns(int adcType,  String swVersion, String vsName)  throws OBException
    {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s, vsID:%s", adcType, swVersion, vsName));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();
		
		DtoOidStatVirtServer oid;
		try
		{
			oid = oidDB.getStatVirtServer(adcType, swVersion);
			OBDtoConnectionData result = getVServerConns(vsName, oid);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			
		} 
    }

//	public static void main(String[] args) throws Exception
//	{
//		OBSnmpPAS snmp = new OBSnmpPAS("192.168.100.11");
//		System.out.println(snmp.makeOidName("abc"));
//	}
	
	private String makeOidName(String name) throws Exception
	{
		String retVal = "";
		byte[] bytes;
		bytes = name.getBytes("US-ASCII");
		for(int i=0;i<name.length();i++)
		{
			retVal += "."+ bytes[i];
		}
		
		return name.length()+retVal;
	}
	
	private OBDtoConnectionData getVServerConns(String vsName, DtoOidStatVirtServer oid)  throws OBException
    {
		OBDtoConnectionData result = new OBDtoConnectionData();
		result.setOccurTime(OBDateTime.toTimestamp(OBDateTime.now()));
		
    	Snmp snmp = openSnmp();
    	
    	String reqOid="";
    	try
    	{
    		Object obj;
    		// current connection
//	    	reqOid=oid.getCurConns()+makeOidName(vsName);
    		String oidName = makeOidName(vsName);
	    	reqOid = oid.getCurConns();
	    	reqOid += "." + oidName;
	    	if(reqOid!=null)
	    	{
		    	obj = getSnmp(snmp, reqOid);
		    	if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
		    		result.setCurConns(Long.parseLong(obj.toString()));
				else
				{
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get concurrent connections. ip:%s, oid:%s", this.getHost(), reqOid));
				}
	    	}
    	}
    	catch(OBException e)
    	{
    		closeSnmp(snmp);
    		throw e;
    	}  
		catch(Exception e)
		{
			closeSnmp(snmp);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} 

    	closeSnmp(snmp);
		return result;
    }
 
//	public static void main(String[] args)
//	{
//		OBSnmpPAS snmp = new OBSnmpPAS(OBSnmp.VERSION_2C, "192.168.160.11");
//
//		ArrayList<OBDtoAdcVServerPAS> list;
//		try
//		{
//			OBDatabase db = new OBDatabase();
//			db.openDB();
//			OBDtoAdcInfo adcInfo = new OBDtoAdcInfo();
//			adcInfo.setIndex(1);
//			adcInfo.setAdcIpAddress("192.168.160.11");
//			adcInfo.setAdcType(OBDefine.ADC_TYPE_PIOLINK_PAS);
//			adcInfo.setSwVersion("");
//			adcInfo.setSnmpRComm("anwkddo6619");
//			
//			list = snmp.getSlbStatus(adcInfo, db);
//			for(OBDtoAdcVServerPAS status: list)
//				System.out.println(status);
//			
////			new OBAdcMonitorDB().writeSlbStatusAlteon(25, list, db);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	
	// vs, poolmember단위로 status정보만 추출한다.
	public ArrayList<OBDtoAdcVServerPAS> getSlbStatus(int adcIndex, int adcType, String swVersion) throws OBException
	{
	    OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		try
		{
			OBSnmpOidDB oidDB = new OBSnmpOidDB();
	 
			DtoOidInfoCfgPAS oidCfgPAS = oidDB.getCfgPAS(adcType, swVersion);
	    	
	    	// PAS의 경우에는 vs에 real server가 추가되는 형태로 운영됨. 또한 vs의 status는 real server의 상태에 따라 달라짐...
			HashMap<String, OBDtoAdcVServerPAS> vsMap = new HashMap<String, OBDtoAdcVServerPAS>();//key: vs이름.
			
			List<VariableBinding> tmpList;
    		// poolmember status. vs 이름.  index, addr, status 정보만 추출한다.
	    	String	reqOid=oidCfgPAS.getSlbServiceState();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						String vsName = parseOidName(var.getOid().toString(), reqOid);
						if(vsName==null)
						{
							OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("invalid data.(target:%s, getPoolMemberIndex:%s)", var.getOid().toString(), reqOid));
							continue;
						}

						// vs 정보 저장.
						OBDtoAdcVServerPAS vsObj =vsMap.get(vsName);
						if(vsObj==null)
						{

							ArrayList<OBDtoAdcPoolMemberPAS> memberList = new ArrayList<OBDtoAdcPoolMemberPAS>();
							
							// pool 정보 저장..
							OBDtoAdcPoolPAS poolObj = new OBDtoAdcPoolPAS();
							poolObj.setName(vsName);// pool 이름과 vs 이름을 동일하게 처리한다.
							poolObj.setMemberList(memberList);
							
							// vs 정보 저장..
							vsObj = new OBDtoAdcVServerPAS();
							String vsIndex = OBCommon.makeVSIndexPAS(adcIndex, vsName);
							vsObj.setDbIndex(vsIndex);
							vsObj.setName(vsName);
							if(var.getVariable().toInt()==1)
								vsObj.setState(OBDefine.VS_STATE.ENABLED);
							else
								vsObj.setState(OBDefine.VS_STATE.DISABLED);
							vsObj.setPool(poolObj);
							
							vsMap.put(vsObj.getName(), vsObj);
						}
					}
				}
	    	}
	    	
	    	reqOid=oidCfgPAS.getSlbRealActStatus();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						String vsName = parseOidName(var.getOid().toString(), reqOid);
						if(vsName==null)
						{
							OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("invalid data.(target:%s, getPoolMemberIndex:%s)", var.getOid().toString(), reqOid));
							continue;
						}

						String nodeIndex = parseOidNameIndex(var.getOid().toString(), reqOid);
						// vs 정보 저장.
						OBDtoAdcVServerPAS vsObj =vsMap.get(vsName);
						if(vsObj==null)
						{
							OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("invalid dataxxx.(target:%s, getPoolMemberIndex:%s)", var.getOid().toString(), reqOid));
						}
						else
						{
							OBDtoAdcPoolMemberPAS memObj = new OBDtoAdcPoolMemberPAS();
							memObj.setDbIndex(vsObj.getPool().getName());
							memObj.setId(Integer.parseInt(nodeIndex));
							memObj.setStatus(convertNodeStatus(var.getVariable().toString()));
							vsObj.getPool().getMemberList().add(memObj);
						}
					}
				}
	    	}
	    	
	    	//Pool Member port 설정
	    	reqOid=oidCfgPAS.getSlbRealPort();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						String vsName = parseOidName(var.getOid().toString(), reqOid);
						if(vsName==null)
						{
							OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("invalid data.(target:%s, getPoolMemberIndex:%s)", var.getOid().toString(), reqOid));
							continue;
						}

						String nodeIndex = parseOidNameIndex(var.getOid().toString(), reqOid);
						
						// vs 정보 저장.
						OBDtoAdcVServerPAS vsObj =vsMap.get(vsName);
						// rs 정보
						if(vsObj==null)
						{
							OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("invalid dataxxx.(target:%s, getPoolMemberIndex:%s)", var.getOid().toString(), reqOid));
						}
						else
						{
							ArrayList<OBDtoAdcPoolMemberPAS> memObj = vsObj.getPool().getMemberList();
							int nodeId = Integer.parseInt(nodeIndex);
							for(OBDtoAdcPoolMemberPAS member : memObj)
							{
								if(member.getId() == nodeId)
								{
									member.setPort(var.getVariable().toInt());
								}
							}
						}
					}
				}
	    	}

	    	// port 추출.
	    	reqOid=oidCfgPAS.getSlbServiceVport();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					if(var!=null)
					{
						String vsName = parseOidName(var.getOid().toString(), reqOid);
						if(vsName==null)
						{
							OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("invalid data.(target:%s, getPoolMemberIndex:%s)", var.getOid().toString(), reqOid));
							continue;
						}

						//  정보 저장.
						OBDtoAdcVServerPAS vsObj =vsMap.get(vsName);
						if(vsObj!=null)
						{
							// proto:port 형태로 전달됨.
							String retVar = var.getVariable().toString();
							String [] protPort = retVar.split(":");
							if(protPort.length != 2)
							{
							    vsObj.setSrvPort(0);
							}
							else
							{
							    vsObj.setSrvPort(Integer.parseInt(protPort[1]));
							}
						}
					}
				}
	    	}
    					
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result-size:%d", vsMap.size()));
			
			ArrayList<OBDtoAdcVServerPAS> retVal = new ArrayList<OBDtoAdcVServerPAS>(vsMap.values());
			for(OBDtoAdcVServerPAS obj:retVal)
			{
				obj.setStatus(calcVSStatus(obj));
			}
			
			return retVal;
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			
		}
	}
	
	public ArrayList<OBDtoMonL2Ports> getPortsInfo(int adcType, String swVersion) throws OBException
	{
		OBSnmpOidDB oidDB = new OBSnmpOidDB();
		
		DtoOidCfgPorts oid;
		try
		{			
			oid = oidDB.getCfgPortInfo(adcType, swVersion);
			ArrayList<OBDtoMonL2Ports>  retVal = getPortsInfo(oid);
			return retVal;
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} 
	}
	
	private  ArrayList<OBDtoMonL2Ports> getPortsInfo(DtoOidCfgPorts oid) throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. oid:%s", oid));
//		ArrayList<OBDtoMonL2Ports> result=new ArrayList<OBDtoMonL2Ports>();
		HashMap<Integer, OBDtoMonL2Ports> portInfoMap=new HashMap<Integer, OBDtoMonL2Ports>();
		
		List<VariableBinding> tmpList;
		
		try
		{
			String reqOid = "";
			// get a interface name
			reqOid = oid.getPortName();
			if(reqOid!=null)
			{
				tmpList = walk(reqOid);
				for(VariableBinding var:tmpList)
				{
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if(oidList.size()!=1)
					{
						throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("invalid data.(target:%s, portName:%s)", var.getOid().toString(), reqOid));
					}
					Integer index=oidList.get(0);
					
					if(index!=null)
					{
						
						OBDtoMonL2Ports info = new OBDtoMonL2Ports();
						info.setPortName(index.toString());
						portInfoMap.put(index, info);
					}
				}	
			}
			// get alias name
			reqOid = oid.getPortAliasName();
			if(reqOid!=null)
			{
				tmpList = walk(reqOid);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if(oidList.size()==1)
					{
						Integer index = oidList.get(0);
						OBDtoMonL2Ports info = portInfoMap.get(index);
						if(null != info)
						{
							info.setAliasName(var.getVariable().toString());
						}
					}
				}
			}
			// get a port duplex
			reqOid = oid.getPortDuplex();
			if(reqOid!=null)
			{
				tmpList = walk(reqOid);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if(oidList.size()==1)
					{
						Integer index = oidList.get(0);
						OBDtoMonL2Ports info = portInfoMap.get(index);
						if(null != info)
						{
							info.setDuplex(convertPortDuplex(var.getVariable().toInt()));
						}
					}
				}	
			}
			// discards pkts.
			reqOid = oid.getPortPktsDiscardsIn();
			if(reqOid!=null)
			{
				tmpList = walk(reqOid);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if(oidList.size()==1)
					{
						Integer index = oidList.get(0);
						OBDtoMonL2Ports info = portInfoMap.get(index);
						if(null != info)
						{
							info.setDropsIn(var.getVariable().toLong());
						}
					}
				}			
			}			
			// discards pkts
			reqOid = oid.getPortPktsDiscardsOut();
			if(reqOid!=null)
			{
				tmpList = walk(reqOid);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if(oidList.size()==1)
					{
						Integer index = oidList.get(0);
						OBDtoMonL2Ports info = portInfoMap.get(index);
						if(null != info)
						{
							info.setDropsOut(var.getVariable().toLong());
						}
					}
				}	
			}
			
			// bytes in
			reqOid = oid.getPortBytesIn();
			if(reqOid!=null)
			{			
				tmpList = walk(reqOid);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if(oidList.size()==1)
					{
						Integer index = oidList.get(0);
						OBDtoMonL2Ports info = portInfoMap.get(index);
						if(null != info)
						{
							info.setBytesIn(var.getVariable().toLong());
						}
					}
				}
			}
			
			// bytes out
			reqOid = oid.getPortBytesOut();
			if(reqOid!=null)
			{
				tmpList = walk(reqOid);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if(oidList.size()==1)
					{
						Integer index = oidList.get(0);
						OBDtoMonL2Ports info = portInfoMap.get(index);
						if(null != info)
						{
							info.setBytesOut(var.getVariable().toLong());
						}					
					}
				}
			}
			
			// errors pkts
			reqOid = oid.getPortPktsErrorsIn();
			if(reqOid!=null)
			{
				tmpList = walk(reqOid);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if(oidList.size()==1)
					{
						Integer index = oidList.get(0);
						OBDtoMonL2Ports info = portInfoMap.get(index);
						if(null != info)
						{
							info.setErrorsIn(var.getVariable().toLong());
						}					
					}
				}
			}
			
			// errors
			reqOid = oid.getPortPktsErrorsOut();
			if(reqOid!=null)
			{
				tmpList = walk(reqOid);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if(oidList.size()==1)
					{
						Integer index = oidList.get(0);
						OBDtoMonL2Ports info = portInfoMap.get(index);
						if(null != info)
						{
							info.setErrorsOut(var.getVariable().toLong());
						}					
					}
				}
			}
			
			// in
			reqOid = oid.getPortPktsIn();
			if(reqOid!=null)
			{
				tmpList = walk(reqOid);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if(oidList.size()==1)
					{
						Integer index = oidList.get(0);
						OBDtoMonL2Ports info = portInfoMap.get(index);
						if(null != info)
						{
							info.setPktsIn(var.getVariable().toLong());
						}					
					}
				}
			}
			
			// out
			reqOid = oid.getPortPktsOut();
			if(reqOid!=null)
			{
				tmpList = walk(reqOid);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if(oidList.size()==1)
					{
						Integer index = oidList.get(0);
						OBDtoMonL2Ports info = portInfoMap.get(index);
						if(null != info)
						{
							info.setPktsOut(var.getVariable().toLong());
						}					
					}
				}
			}
			
			// multi in
			reqOid = oid.getPortPktsMultiIn();
			if(reqOid!=null)
			{
				tmpList = walk(reqOid);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if(oidList.size()==1)
					{
						Integer index = oidList.get(0);
						OBDtoMonL2Ports info = portInfoMap.get(index);
						if(null != info)
						{
							info.setPktsMultiIn(var.getVariable().toLong());
						}					
					}
				}
			}
			
			// multi out
			reqOid = oid.getPortPktsMultiOut();
			if(reqOid!=null)
			{
				tmpList = walk(reqOid);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if(oidList.size()==1)
					{
						Integer index = oidList.get(0);
						OBDtoMonL2Ports info = portInfoMap.get(index);
						if(null != info)
						{
							info.setPktsMultiOut(var.getVariable().toLong());
						}					
					}
				}
			}
			
			// broad in
			reqOid = oid.getPortPktsBroadIn();
			if(reqOid!=null)
			{
				tmpList = walk(reqOid);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if(oidList.size()==1)
					{
						Integer index = oidList.get(0);
						OBDtoMonL2Ports info = portInfoMap.get(index);
						if(null != info)
						{
							info.setPktsBroadIn(var.getVariable().toLong());
						}					
					}
				}
			}
			
			// broad out
			reqOid = oid.getPortPktsBroadOut();
			if(reqOid!=null)
			{
				tmpList = walk(reqOid);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if(oidList.size()==1)
					{
						Integer index = oidList.get(0);
						OBDtoMonL2Ports info = portInfoMap.get(index);
						if(null != info)
						{
							info.setPktsBroadOut(var.getVariable().toLong());
						}					
					}
				}
			}
			
			// unknown protocols
//			reqOid = oid.getPortPktsUnknownProtos();
//			if(reqOid!=null)
//			{
//				tmpList = walk(reqOid);
//				for(int i=0;i<tmpList.size();i++)
//				{
//					VariableBinding var=tmpList.get(i);
//					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
//					if(oidList.size()==1)
//					{
//						Integer index = oidList.get(0);
//						OBDtoMonL2Ports info = portInfoMap.get(index);
//						if(null != info)
//						{
//							info.setPktsUnknownProtos(var.getVariable().toLong());
//						}					
//					}
//				}
//			}
			
			// speed
			reqOid = oid.getPortSpeed();
			if(reqOid!=null)
			{
				tmpList = walk(reqOid);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if(oidList.size()==1)
					{
						Integer index = oidList.get(0);
						OBDtoMonL2Ports info = portInfoMap.get(index);
						if(null != info)
						{
							info.setSpeed(convertPortSpeed(var.getVariable().toInt()));
						}					
					}
				}
			}
			
			// state
			reqOid = oid.getPortStatus();
			if(reqOid!=null)
			{
				tmpList = walk(reqOid);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if(oidList.size()==1)
					{
						Integer index = oidList.get(0);
						OBDtoMonL2Ports info = portInfoMap.get(index);
						if(null != info)
						{
							info.setStatus(convertPortStatus(var.getVariable().toInt()));
						}					
					}
				}
			}
			
			// connector type. 관련 정보가 없는 관계로 수집하지 않는다.		
		}
    	catch(OBException e)
    	{
    		throw e;
    	}  
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			
		} 

		ArrayList<OBDtoMonL2Ports> result=new ArrayList<OBDtoMonL2Ports>(portInfoMap.values());
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}
	
	private Integer convertPortStatus(Integer state)
	{
		switch(state)
		{
		case 1:
			return OBDtoMonL2Ports.STATUS_UP;
		case 2:
			return OBDtoMonL2Ports.STATUS_DOWN;
		default:
			return OBDtoMonL2Ports.STATUS_DOWN;
		}		
	}
	
	private Integer convertPortDuplex(Integer duplex)
	{
		switch(duplex)
		{
		case 3:
			return OBDefine.DUPLEX_HALF;
		case 2:
			return OBDefine.DUPLEX_FULL;
		default:
			return OBDefine.DUPLEX_NONE;
		}
	}
	
	private Integer convertPortSpeed(Integer duplex)
	{
		switch(duplex)
		{
		case 2:
			return OBDefine.PORT_SPEED_10;
		case 3:
			return OBDefine.PORT_SPEED_100;
		case 4:
			return OBDefine.PORT_SPEED_1000;
		default:
			return OBDefine.PORT_SPEED_0;
		}
	}
	
	// name 정보 뒤의 index 정보를 추출한다.
	private String parseOidNameIndex(String targetOid, String baseOid)
	{//SNMPv2-SMI::enterprises.10188.1.1.1.2.1.3.6.98.119.112.97.114.107.1 = IpAddress: 192.168.199.43
		int tmpIndex = targetOid.indexOf(baseOid);
		String sLine = targetOid.substring(tmpIndex+baseOid.length()+1);
		String[] data = sLine.split("\\.");
	
		int length = Integer.parseInt(data[0]);
		if(length+1>=data.length)
			return "";
		return data[length+1];
	}
	
	private ArrayList<Integer> parsePortList(OctetString members)
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i=0;i<members.length();i++)
		{
			Byte octect = members.get(i);
			if(octect == 0)
				continue;
			
			int base=0x80;
			Integer nodeID=0;
			for(int bit=7;bit>=0;bit--)
			{
				if((octect&base)>0)
				{
					nodeID = i*8 + (8-bit);
					list.add(nodeID-1);
				}
				base=base>>1;
			}
		}
		return list;
	}
	
	private Integer convertNodeStatus(String state)
	{
		if(state.compareTo("ACT")==0)
			return OBDefine.STATUS_AVAILABLE;
		if(state.compareTo("INACT")==0)
			return OBDefine.STATUS_UNAVAILABLE;
		if(state.compareTo("UNKNOWN")==0)
			return OBDefine.STATUS_DISABLE;
		return OBDefine.STATUS_UNAVAILABLE;
	}

    public DtoSnmpAdcResc getAdcResc(int adcType, String swVersion)  throws OBException
    {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();
		
		DtoOidInfoAdcResc oidInfo;
		
		try
		{
			oidInfo = oidDB.getAdcResource(adcType, swVersion);
			DtoSnmpAdcResc info = getAdcResc(oidInfo);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", info));
			return info;
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			
		} 
    }
    
    public String getAdcSWVersion(int adcType,  String swVersion)  throws OBException
    {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start"));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();
		
		DtoOidInfoAdcResc oidInfo;
		
		try
		{
			oidInfo = oidDB.getAdcResource(adcType, swVersion);
			String name = getAdcSWVersion(oidInfo.getAdcSwVersion());
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", name));
			return name;
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			
		} 
    }
    
    private String getAdcSWVersion(String oid) throws OBException
    {
//		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. %s.", oid.toString()));
		
		String result="";
		String tempOid;
    	Snmp snmp = openSnmp();

    	try
    	{
			tempOid = oid;
			if((tempOid!=null) && (!tempOid.isEmpty()))
			{
				Object obj = getSnmp(snmp, tempOid);
				if(checkObject(obj))
					result=obj.toString();
			}		
			closeSnmp(snmp);
//			OBSystemLog.debug(OBDefine.LOGFILE_SYSTEM, String.format("end. result:%s.", result));
			return result;			
    	}
    	catch(OBException e)
    	{
    		closeSnmp(snmp);
    		throw e;
    	}  
		catch(Exception e)
		{
			closeSnmp(snmp);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} 
    }
    
//	public static void main(String[] args)
//	{
//		OBSnmpPAS snmp = new OBSnmpPAS(OBSnmp.VERSION_2C, "192.168.200.120");
//
//		ArrayList<OBDtoAdcVServerPAS> list=null;
//		try
//		{
//			OBDatabase db = new OBDatabase();
//			db.openDB();
////			OBSnmpOidDB oidDB = new OBSnmpOidDB();
////			DtoOidInfoCfgPAS oid = oidDB.getCfgPAS(OBDefine.ADC_TYPE_PIOLINK_PAS, null, db);
//			String name = snmp.getAdcHostname("default", db);
//			System.out.println(name);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	
//    public String getAdcHostname(OBDatabase db)  throws OBException
//    {
//		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start"));
//		OBSnmpOidDB oidDB = new OBSnmpOidDB();
//		
//		DtoOidInfoAdcResc oidInfo;
//		
//		try
//		{
//			oidInfo = oidDB.getAdcResource(OBDefine.ADC_TYPE_PIOLINK_PAS, "", db);
//			String name = getAdcHostname(oidInfo.getAdcName());
//			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", name));
//			return name;
//		}
//		catch(OBException e)
//		{
//			throw e;
//		}
//		catch(Exception e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//			
//		} 
//    }
    
//    private String getAdcHostname(String oid) throws OBException
//    {
////		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. %s.", oid.toString()));
//		
//		String result="";
//		String tempOid;
//    	Snmp snmp = openSnmp();
//
//    	try
//    	{
//			tempOid = oid;
//			if((tempOid!=null) && (!tempOid.isEmpty()))
//			{
//				Object obj = getSnmp(snmp, tempOid);
//				if(checkObject(obj))
//					result=obj.toString();
//			}		
//			closeSnmp(snmp);
//			return result;			
//    	}
//    	catch(OBException e)
//    	{
//    		closeSnmp(snmp);
//    		throw e;
//    	}  
//		catch(Exception e)
//		{
//			closeSnmp(snmp);
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		} 
//    }
    
    private DtoSnmpAdcResc getAdcResc(DtoOidInfoAdcResc oid) throws OBException
    {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. oid:%s", oid));
		
		DtoSnmpAdcResc info = new DtoSnmpAdcResc();
		String tempOid;
		List<VariableBinding> tmpList;

    	Snmp snmp = openSnmp();

    	try
    	{
			tempOid = oid.getAdcCpuIdle();
			if((tempOid!=null) && (!tempOid.isEmpty()))
			{// cpu used임.
				tmpList = walk(tempOid);
				long cpuUsed=0;
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					if(var!=null)
						cpuUsed += var.getVariable().toLong();
				}
				if(tmpList.size()>0)
					info.setCpuUsed(cpuUsed/tmpList.size());
				else
					info.setCpuUsed(0);
//				Object obj = getSnmp(snmp, tempOid);
//				if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
//				info.setCpuUsed(Long.parseLong(obj.toString()));
			}
			else
			{
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get cpu idle time. ip:%s, oid:%s", this.getHost(), tempOid));
			}
			
			tempOid = oid.getAdcMemTotal();
			if((tempOid!=null) && (!tempOid.isEmpty()))
			{
				Object obj = getSnmp(snmp, tempOid);
				if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
					info.setMemTotal(Long.parseLong(obj.toString()));
				else
				{
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get total memory. ip:%s, oid:%s", this.getHost(), tempOid));
				}
			}
		
			tempOid = oid.getAdcMemFree();
			if((tempOid!=null) && (!tempOid.isEmpty()))
			{
				Object obj = getSnmp(snmp, tempOid);
				if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
				{
					long free = Long.parseLong(obj.toString());
					info.setMemUsed(info.getMemTotal()-free);
				}
				else
				{
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get mem free. ip:%s, oid:%s", this.getHost(), tempOid));
				}
			}		
	
			// concurrent connections
			tempOid = oid.getAdcCurConns();
			if((tempOid!=null) && (!tempOid.isEmpty()))
			{
				tmpList = walk(tempOid);
				long currConns=0;
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					if(var!=null)
						currConns += var.getVariable().toLong();
				}
				info.setVsCurConns(currConns);
			}
	
//			tempOid = oid.getAdcCurConns();
//			if((tempOid!=null) && (!tempOid.isEmpty()))
//			{
//				Object obj = getSnmp(snmp, tempOid);
//				if((checkObject(obj)) && (OBParser.isNumeric(obj.toString())))
//					info.setVsCurConns(Long.parseLong(obj.toString()));
//				else
//				{
//					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get concurrent connections. ip:%s, oid:%s", this.getHost(), tempOid));
//				}
//			}		
			
			tempOid = oid.getAdcModel();
			if((tempOid!=null) && (!tempOid.isEmpty()))
			{
				Object obj = getSnmp(snmp, tempOid);
				if(checkObject(obj))
					info.setModel(obj.toString());
				else
				{
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get adc model. ip:%s, oid:%s", this.getHost(), tempOid));
				}
			}		
		
			tempOid = oid.getAdcSwVersion();
			if((tempOid!=null) && (!tempOid.isEmpty()))
			{
				Object obj = getSnmp(snmp, tempOid);
				if(checkObject(obj))
					info.setSwVersion(obj.toString());
				else
				{
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get sw version. ip:%s, oid:%s", this.getHost(), tempOid));
				}
			}		

			tempOid = oid.getAdcName();
			if((tempOid!=null) && (!tempOid.isEmpty()))
			{
				Object obj = getSnmp(snmp, tempOid);
				if(checkObject(obj))
					info.setName(obj.toString());
				else
				{
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get adc name. ip:%s, oid:%s", this.getHost(), tempOid));
				}
			}		
	
			tempOid = oid.getAdcDescr();
			if((tempOid!=null) && (!tempOid.isEmpty()))
			{
				Object obj = getSnmp(snmp, tempOid);
				if(checkObject(obj))
					info.setDescr(obj.toString());
				else
				{
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get adc description. ip:%s, oid:%s", this.getHost(), tempOid));
				}
			}		

			tempOid = oid.getAdcUpTime();
			if((tempOid!=null) && (!tempOid.isEmpty()))
			{
				Object obj = getSnmp(snmp, tempOid);
				if((checkObject(obj)))// && (OBParser.isNumeric(obj.toString())))
				{
					org.snmp4j.smi.TimeTicks elapseTime=(TimeTicks) obj;
					Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
					Timestamp upTime = new Timestamp(now.getTime()-elapseTime.toLong()*10);// getTime()함수의 단위는 millisec, TimeTicks는 10msec
					info.setUpTime(upTime);
				}
				else
				{
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get up time. ip:%s, oid:%s", this.getHost(), tempOid));
				}
			}
			closeSnmp(snmp);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s.", info));
			return info;			
    	}
    	catch(OBException e)
    	{
    		closeSnmp(snmp);
    		throw e;
    	}  
		catch(Exception e)
		{
			closeSnmp(snmp);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} 
    }
	
//	public static void main(String[] args)
//	{
//		OBSnmpPAS snmp = new OBSnmpPAS("192.168.100.11");
//		try
//		{
//			OBDatabase db = new OBDatabase();
//			db.openDB();
//			OBDtoAdcInfo adcInfo = new OBDtoAdcInfo();
//			adcInfo.setIndex(1);
//			adcInfo.setSwVersion("");
//			adcInfo.setAdcType(OBDefine.ADC_TYPE_PIOLINK_PAS);
//			adcInfo.setSnmpRComm("default");
//			ArrayList<OBDtoMonTrafficVServer> list = snmp.getTrafficVServer(adcInfo, db);
////			String now = OBDateTime.now();
////			ArrayList<OBDtoMonTrafficServiceAlteon> list;
////			list = new OBAdcMonitorAlteon().getVServiceTrafficAlteon(1, "192.168.100.11", "", "admin", "");
//			
////			new OBAdcMonitorDB().writeVServiceTrafficAlteon(now, 1, list, db);
//			
//			db.closeDB();
//			System.out.println(list);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	
    public ArrayList<OBDtoMonTrafficVServer>  getTrafficVServer(int adcType, String swVersion)  throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();
		
		try
		{
			DtoOidInfoCfgPAS oid = oidDB.getCfgPAS(adcType, swVersion);
			ArrayList<OBDtoMonTrafficVServer> list = getTrafficVServer(oid);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result-size:%d", list.size()));
			return list;			
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			
		} 
	}
    
    public OBDtoMonTrafficVServer  getTrafficVServerInfo(int adcType, String swVersion, String vsName)  throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();
		
		try
		{
			DtoOidInfoCfgPAS oid = oidDB.getCfgPAS(adcType, swVersion);
			OBDtoMonTrafficVServer retVal = getTrafficVServerInfo(oid, vsName);
			return retVal;			
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			
		} 
	}
    
    private ArrayList<OBDtoMonTrafficVServer> getTrafficVServer(DtoOidInfoCfgPAS oid)  throws OBException
    {
    	try
    	{
	    	HashMap<String, OBDtoMonTrafficVServer> vsMap = new HashMap<String, OBDtoMonTrafficVServer>();
	    	List<VariableBinding> tmpList;
	    	
			// slbServiceActConn 추출. 
	    	String reqOid = oid.getSlbServiceActConn();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					String vsName = parseOidName(var.getOid().toString(), reqOid);
					if(vsName!=null)
					{
						OBDtoMonTrafficVServer vsObj = new OBDtoMonTrafficVServer();
						vsObj.setName(vsName);
						vsObj.setCurConns(var.getVariable().toLong());
						vsMap.put(vsName, vsObj);
					}
					else
					{
						OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), reqOid));
					}
				}
	    	}
			
	    	// in pps
	    	reqOid = oid.getSlbServiceInpps();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					String vsName = parseOidName(var.getOid().toString(), reqOid);
					if(vsName!=null)
					{
						OBDtoMonTrafficVServer vsObj = vsMap.get(vsName);
						if(vsObj!=null)
							vsObj.setPktsIn(var.getVariable().toLong());
					}
					else
					{
						OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), reqOid));
					}
				}
	    	}
	    	
	    	// in bps
	    	reqOid = oid.getSlbServiceInbps();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					String vsName = parseOidName(var.getOid().toString(), reqOid);
					if(vsName!=null)
					{
						OBDtoMonTrafficVServer vsObj = vsMap.get(vsName);
						if(vsObj!=null)
							vsObj.setBytesIn(var.getVariable().toLong());
					}
					else
					{
						OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), reqOid));
					}
				}
	    	}
	    	
	    	// out pps
	    	reqOid = oid.getSlbServiceOutpps();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					String vsName = parseOidName(var.getOid().toString(), reqOid);
					if(vsName!=null)
					{
						OBDtoMonTrafficVServer vsObj = vsMap.get(vsName);
						if(vsObj!=null)
							vsObj.setPktsOut(var.getVariable().toLong());
					}
					else
					{
						OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), reqOid));
					}
				}
	    	}
	    	
	    	// out bps
	    	reqOid = oid.getSlbServiceOutbps();
	    	if(reqOid!=null)
	    	{
				tmpList = walk(reqOid);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					String vsName = parseOidName(var.getOid().toString(), reqOid);
					if(vsName!=null)
					{
						OBDtoMonTrafficVServer vsObj = vsMap.get(vsName);
						if(vsObj!=null)
							vsObj.setBytesOut(var.getVariable().toLong());
					}
					else
					{
						OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), reqOid));
					}
				}
	    	}
			return new ArrayList<OBDtoMonTrafficVServer>(vsMap.values());
    	}
    	catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}    
    }
    
    private OBDtoMonTrafficVServer getTrafficVServerInfo(DtoOidInfoCfgPAS oid, String vsName)  throws OBException
    {
		OBDtoMonTrafficVServer retVal = new OBDtoMonTrafficVServer();
    	Snmp snmp = openSnmp();
    	String oidInfo = "";
    	try
    	{
//	    	List<VariableBinding> tmpList;
	    	
	    	String nameString = makeOidName(vsName);
	    	
			// slbServiceActConn 추출. 
	    	String reqOid = oid.getSlbServiceActConn();
	    	oidInfo = reqOid+nameString;
    		if(oidInfo!=null)
    		{
    			Counter64 obj = (Counter64) getSnmp(snmp, oidInfo);
				if(checkObject(obj))
				{
					retVal.setCurConns((Long) obj.toLong());
				}
    		}
			
	    	// in pps
	    	reqOid = oid.getSlbServiceInpps();
	    	oidInfo = reqOid+nameString;
    		if(oidInfo!=null)
    		{
    			Counter64 obj = (Counter64) getSnmp(snmp, oidInfo);
				if(checkObject(obj))
				{
					retVal.setPktsIn((Long) obj.toLong());
				}
    		}
	    	
	    	// in bps
	    	reqOid = oid.getSlbServiceInbps();
	    	oidInfo = reqOid+nameString;
    		if(oidInfo!=null)
    		{
    			Counter64 obj = (Counter64) getSnmp(snmp, oidInfo);
				if(checkObject(obj))
				{
					retVal.setBytesIn((Long) obj.toLong());
				}
    		}
	    	
	    	// out pps
	    	reqOid = oid.getSlbServiceOutpps();
	    	oidInfo = reqOid+nameString;
    		if(oidInfo!=null)
    		{
    			Counter64 obj = (Counter64) getSnmp(snmp, oidInfo);
				if(checkObject(obj))
				{
					retVal.setPktsOut((Long) obj.toLong());
				}
    		}
	    	
	    	// out bps
	    	reqOid = oid.getSlbServiceOutbps();
	    	oidInfo = reqOid+nameString;
    		if(oidInfo!=null)
    		{
    			Counter64 obj = (Counter64) getSnmp(snmp, oidInfo);
				if(checkObject(obj))
				{
					retVal.setBytesOut((Long) obj.toLong());
				}
    		}
    	}
    	catch(OBException e)
		{
    		closeSnmp(snmp);
    		throw e;
		}
		catch(Exception e)
		{
			closeSnmp(snmp);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}    
    	closeSnmp(snmp);
		return retVal;
    }
    
//	public static void main(String[] args)
//	{
//		OBSnmpF5 snmp = new OBSnmpF5(OBSnmp.VERSION_2C, "192.168.200.11");
//		OBDtoAdcInfo adcInfo=new OBDtoAdcInfo();
//		adcInfo.setAdcType(OBDefine.ADC_TYPE_F5);
//		adcInfo.setSwVersion("");
//		adcInfo.setIndex(13);
//
//		OBDatabase db=new OBDatabase();
//		try
//		{
//			db.openDB();
//			ArrayList<OBDtoMonTrafficPoolMembersF5> list = snmp.getTrafficPoolMembers(adcInfo, db);
//			System.out.println(list);
//			db.closeDB();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
    
    private ArrayList<OBDtoMonTrafficPoolMembers> getTrafficPoolMembers(int adcIndex, DtoOidInfoCfgPAS oid) throws OBException
    {
//		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. %s.", oid.toString()));
    	
		HashMap<String, OBDtoMonTrafficPoolMembers> memHash = new HashMap<String, OBDtoMonTrafficPoolMembers>();//key = vsName+id
		HashMap<String, OBDtoMonTrafficPoolMembers> vsHash = new HashMap<String, OBDtoMonTrafficPoolMembers>();// key = vsName

    	List<VariableBinding> tmpList;
    	
    	try
		{
    		// vsIndex, vsName, poolIndex, poolName
    		String oidReq = "";
    		oidReq = oid.getSlbServiceName();
			tmpList = walk(oidReq);
			for(VariableBinding var:tmpList)
			{
				String vsName = parseOidName(var.getOid().toString(), oidReq);
				if(vsName!=null)
				{
					OBDtoMonTrafficPoolMembers vsObj = vsHash.get(vsName);
					if(vsObj==null)
					{
						vsObj = new OBDtoMonTrafficPoolMembers();
						String vsIndex = OBCommon.makeVSIndexPAS(adcIndex, vsName);
						vsObj.setVsIndex(vsIndex);
						vsObj.setVsName(vsName);
						vsObj.setPoolName(vsName);// poolName == vsName
						vsObj.setPoolIndex(vsIndex);// pool의 db index를 의미. PAS의 경우에는 vsIndex와 동일함. 
						vsHash.put(vsName, vsObj);
					}
				}
			}	
    		
    		// vsIPAddress
    		oidReq = oid.getSlbServiceVip();
			tmpList = walk(oidReq);
			for(VariableBinding var:tmpList)
			{
			    if(var==null)
			        continue;
				String vsName = parseOidName(var.getOid().toString(), oidReq);
				if(vsName!=null)
				{
					OBDtoMonTrafficPoolMembers vsObj = vsHash.get(vsName);
					if(vsObj!=null)
					{
//						if(var!=null)
							vsObj.setVsIPAddress(var.getVariable().toString()); 
					}
				}
			}	

			// srvPort
    		oidReq = oid.getSlbServiceVport();
			tmpList = walk(oidReq);
			for(VariableBinding var:tmpList)
			{
			    if(var==null)
			        continue;
				String vsName = parseOidName(var.getOid().toString(), oidReq);
				if(vsName!=null && !vsName.isEmpty())
				{
					OBDtoMonTrafficPoolMembers vsObj = vsHash.get(vsName);
					if(vsObj!=null)
					{
						String retVar = var.getVariable().toString();
						String [] protPort = retVar.split(":");
						if(protPort.length != 2)
						{
						    vsObj.setSrvPort(0);
						}
						else
						{
						    vsObj.setSrvPort(Integer.parseInt(protPort[1]));
						}
						
					}
				}
			}	
    		
    		// nodeID
	  		oidReq = oid.getSlbRealID();
			tmpList = walk(oidReq);
			for(VariableBinding var:tmpList)
			{
				String vsName = parseOidName(var.getOid().toString(), oidReq);
				if(vsName!=null)
				{
					Integer nodeId = var.getVariable().toInt();
					String memHashKey = adcIndex+"_"+vsName+"_"+nodeId;
					OBDtoMonTrafficPoolMembers memObj = memHash.get(memHashKey);
					if(memObj==null)
					{
						memObj = new OBDtoMonTrafficPoolMembers();
						OBDtoMonTrafficPoolMembers vsObj = vsHash.get(vsName);
						if(vsObj!=null)
						{
							memObj.setVsIndex(vsObj.getVsIndex());
							memObj.setVsIPAddress(vsObj.getVsIPAddress());
							memObj.setVsName(vsObj.getVsName());
							memObj.setPoolIndex(vsObj.getPoolIndex());
							memObj.setPoolName(vsObj.getPoolName());
							memObj.setSrvPort(vsObj.getSrvPort());
							
							memObj.setNodeID(nodeId);
							memHash.put(memHashKey, memObj);
						}
					}
				}
			}
			
			// nodeAddress
	  		oidReq = oid.getSlbRealIP();
			tmpList = walk(oidReq);
			for(VariableBinding var:tmpList)
			{
				String vsName = parseOidName(var.getOid().toString(), oidReq);
				if(vsName!=null)
				{
					String nodeId = parseOidNameIndex(var.getOid().toString(), oidReq);
					String memHashKey = adcIndex+"_"+vsName+"_"+nodeId;
					OBDtoMonTrafficPoolMembers memObj = memHash.get(memHashKey);
					if(memObj!=null)
					{
						OBDtoMonTrafficPoolMembers vsObj = vsHash.get(vsName);
						if(vsObj!=null)
						{
							memObj.setNodeAddress(var.getVariable().toString());
						}
					}
				}
			}
			
    		// nodePort
	  		oidReq = oid.getSlbRealPort();
			tmpList = walk(oidReq);
			for(VariableBinding var:tmpList)
			{
				String vsName = parseOidName(var.getOid().toString(), oidReq);
				if(vsName!=null)
				{
					String nodeId = parseOidNameIndex(var.getOid().toString(), oidReq);
					String memHashKey = adcIndex+"_"+vsName+"_"+nodeId;
					OBDtoMonTrafficPoolMembers memObj = memHash.get(memHashKey);
					if(memObj!=null)
					{
						OBDtoMonTrafficPoolMembers vsObj = vsHash.get(vsName);
						if(vsObj!=null)
						{
							memObj.setNodePort(var.getVariable().toInt());
						}
					}
				}
			}
			
			// bytes in
	  		oidReq = oid.getSlbRealInbps();
			tmpList = walk(oidReq);
			for(VariableBinding var:tmpList)
			{
				String vsName = parseOidName(var.getOid().toString(), oidReq);
				if(vsName!=null)
				{
					String nodeId = parseOidNameIndex(var.getOid().toString(), oidReq);
					String memHashKey = adcIndex+"_"+vsName+"_"+nodeId;
					OBDtoMonTrafficPoolMembers memObj = memHash.get(memHashKey);
					if(memObj!=null)
					{
						OBDtoMonTrafficPoolMembers vsObj = vsHash.get(vsName);
						if(vsObj!=null)
						{
							memObj.setBytesIn(var.getVariable().toLong());
						}
					}
				}
			}
    		// bytes out
	  		oidReq = oid.getSlbRealOutbps();
			tmpList = walk(oidReq);
			for(VariableBinding var:tmpList)
			{
				String vsName = parseOidName(var.getOid().toString(), oidReq);
				if(vsName!=null)
				{
					String nodeId = parseOidNameIndex(var.getOid().toString(), oidReq);
					String memHashKey = adcIndex+"_"+vsName+"_"+nodeId;
					OBDtoMonTrafficPoolMembers memObj = memHash.get(memHashKey);
					if(memObj!=null)
					{
						OBDtoMonTrafficPoolMembers vsObj = vsHash.get(vsName);
						if(vsObj!=null)
						{
							memObj.setBytesOut(var.getVariable().toLong());
						}
					}
				}
			}
    		// packet in
	  		oidReq = oid.getSlbRealInpps();
			tmpList = walk(oidReq);
			for(VariableBinding var:tmpList)
			{
				String vsName = parseOidName(var.getOid().toString(), oidReq);
				if(vsName!=null)
				{
					String nodeId = parseOidNameIndex(var.getOid().toString(), oidReq);
					String memHashKey = adcIndex+"_"+vsName+"_"+nodeId;
					OBDtoMonTrafficPoolMembers memObj = memHash.get(memHashKey);
					if(memObj!=null)
					{
						OBDtoMonTrafficPoolMembers vsObj = vsHash.get(vsName);
						if(vsObj!=null)
						{
							memObj.setPktsIn(var.getVariable().toLong());
						}
					}
				}
			}
			
    		// packet out
	  		oidReq = oid.getSlbRealOutpps();
			tmpList = walk(oidReq);
			for(VariableBinding var:tmpList)
			{
				String vsName = parseOidName(var.getOid().toString(), oidReq);
				if(vsName!=null)
				{
					String nodeId = parseOidNameIndex(var.getOid().toString(), oidReq);
					String memHashKey = adcIndex+"_"+vsName+"_"+nodeId;
					OBDtoMonTrafficPoolMembers memObj = memHash.get(memHashKey);
					if(memObj!=null)
					{
						OBDtoMonTrafficPoolMembers vsObj = vsHash.get(vsName);
						if(vsObj!=null)
						{
							memObj.setPktsOut(var.getVariable().toLong());
						}
					}
				}
			}
			
    		// curConnection
	  		oidReq = oid.getSlbRealActConn();
			tmpList = walk(oidReq);
			for(VariableBinding var:tmpList)
			{
				String vsName = parseOidName(var.getOid().toString(), oidReq);
				if(vsName!=null)
				{
					String nodeId = parseOidNameIndex(var.getOid().toString(), oidReq);
					String memHashKey = adcIndex+"_"+vsName+"_"+nodeId;
					OBDtoMonTrafficPoolMembers memObj = memHash.get(memHashKey);
					if(memObj!=null)
					{
						OBDtoMonTrafficPoolMembers vsObj = vsHash.get(vsName);
						if(vsObj!=null)
						{
							memObj.setCurConns(var.getVariable().toLong());
						}
					}
				}
			}
		}
    	catch(OBException e)
    	{
    		throw e;
    	}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			
		}

//		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return new ArrayList<OBDtoMonTrafficPoolMembers>(memHash.values());
    }
    
//	public static void main(String[] args)
//	{
//		OBSnmpPAS snmp = new OBSnmpPAS(OBSnmp.VERSION_2C, "192.168.200.120");
//
//		ArrayList<OBDtoMonTrafficPoolMembers> list=null;
//		try
//		{
//			OBDatabase db = new OBDatabase();
//			db.openDB();
//			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(5);
////			OBSnmpOidDB oidDB = new OBSnmpOidDB();
////			DtoOidInfoCfgPAS oid = oidDB.getCfgPAS(OBDefine.ADC_TYPE_PIOLINK_PAS, null, db);
//			list = snmp.getTrafficPoolMembers(adcInfo, db);
//			System.out.println(list);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
    
    public ArrayList<OBDtoMonTrafficPoolMembers>  getTrafficPoolMembers(int adcIndex, int adcType,  String swVersion)  throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();
		ArrayList<OBDtoMonTrafficPoolMembers> retVal = new ArrayList<OBDtoMonTrafficPoolMembers>();
		try
		{
			DtoOidInfoCfgPAS oidCfgPAS = oidDB.getCfgPAS(adcType, swVersion);
		
			retVal = getTrafficPoolMembers(adcIndex, oidCfgPAS);
			
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result-size:%d", retVal.size()));
			return retVal;
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			
		}
    }
    
	public ArrayList<Integer>  getSystemFanStatus(int adcType,  String swVersion)  throws OBException
    {
		OBSnmpOidDB oidDB = new OBSnmpOidDB();
		
		DtoOidFaultCheckHW oid;
		try
		{
			oid = oidDB.getFaultCheckHWInfo(adcType, swVersion);
			
			if(oid==null)
				return new ArrayList<Integer>();
			
			ArrayList<Integer> retVal = getSystemFanStatus(oid.getHwFanStatus());
			return retVal;
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			
		} 
    }
	
	private ArrayList<Integer>  getSystemFanStatus(String oid)  throws OBException
    {
		ArrayList<Integer> retVal = new ArrayList<Integer>();
		if(oid==null || oid.isEmpty())
			return retVal;
		
		try
		{
			List<VariableBinding> tmpList;
		
			tmpList = walk(oid);
			for(VariableBinding var:tmpList)
			{
				retVal.add(var.getVariable().toInt());
			}
			return retVal;
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			
		}		
    }
	
	public ArrayList<Integer>  getSystemFanSpeed(int adcType,  String swVersion)  throws OBException
    {
		OBSnmpOidDB oidDB = new OBSnmpOidDB();
		
		DtoOidFaultCheckHW oid;
		try
		{
			oid = oidDB.getFaultCheckHWInfo(adcType, swVersion);
			if(oid==null)
				return new ArrayList<Integer>();			
			ArrayList<Integer> retVal = getSystemFanSpeed(oid.getHwFanSpeed());
			return retVal;
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			
		} 
    }
	
	private ArrayList<Integer>  getSystemFanSpeed(String oid)  throws OBException
    {
		ArrayList<Integer> retVal = new ArrayList<Integer>();
		if(oid==null || oid.isEmpty())
			return retVal;
		
		try
		{
			List<VariableBinding> tmpList;
		
			tmpList = walk(oid);
			for(VariableBinding var:tmpList)
			{
				retVal.add(var.getVariable().toInt());
			}
			return retVal;
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			
		}		
    }
	
	public ArrayList<Integer>  getSystemPowerSupplyStatus(int adcType,  String swVersion)  throws OBException
    {
		OBSnmpOidDB oidDB = new OBSnmpOidDB();
		
		DtoOidFaultCheckHW oid;
		try
		{
			oid = oidDB.getFaultCheckHWInfo(adcType, swVersion);
			if(oid==null)
				return new ArrayList<Integer>();
			ArrayList<Integer> retVal = getSystemPowerSupplyStatus(oid.getPowerSupply());
			return retVal;
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			
		} 
    }
	
	private ArrayList<Integer>  getSystemPowerSupplyStatus(String oid)  throws OBException
    {
		ArrayList<Integer> retVal = new ArrayList<Integer>();
		if(oid==null || oid.isEmpty())
			return retVal;
		
		try
		{
			List<VariableBinding> tmpList;
		
			tmpList = walk(oid);
			for(VariableBinding var:tmpList)
			{
				int value = var.getVariable().toInt();
				retVal.add(value);
//				if(value == 1 || value == 4)////TODO
//					retVal.add(STATUS_NORMAL);
//				else 
//					retVal.add(STATUS_ABNORMAL);
			}
			return retVal;
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}		
    }
	
	public OBDtoHddInfo  getSystemHddtatus(int adcType,  String swVersion, OBDatabase db)  throws OBException
    {
		OBDtoHddInfo retVal = new OBDtoHddInfo();
		// alteon does not support this function.
		return retVal;
    }
	
	public OBDtoHddInfo  getSystemHddtatus(int adcType,  String swVersion)  throws OBException
    {
		OBDtoHddInfo retVal = new OBDtoHddInfo();
		// alteon does not support this function.
		return retVal;
    }
}

