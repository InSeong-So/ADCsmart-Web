package kr.openbase.adcsmart.service.snmp.f5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.snmp4j.Snmp;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSnmpInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficPoolMembers;
import kr.openbase.adcsmart.service.snmp.OBSnmpOidDB;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidInfoAdcResc;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidRptOPL4;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidStatPoolMembers;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidStatVirtServer;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptConnStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptOPL4;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptPMStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptVSStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoSnmpStatPoolMembers;
import kr.openbase.adcsmart.service.snmp.dto.OBDtoOidRptInspection;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBSnmpF5V11 extends OBSnmpF5
{
//	public static final String AUDIT_ADC_ADD_SUCCESS = "ADC 장비 추가에 성공했습니다. 장비 이름:%s";
//	public static void main(String[] args)
//	{
//		String aaa = String.format(OBSnmpF5.AUDIT_ADC_ADD_SUCCESS, "aaa");
//		System.out.print(aaa);
//	}
//	public static void main(String[] args)
//	{
//		OBDatabase db = new OBDatabase();
//		try
//		{
//			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(2);
//			OBSnmpF5 snmp = new OBSnmpF5(OBSnmp.VERSION_2C, adcInfo.getAdcIpAddress());
//
//			db.openDB();
//			DtoRptOPL7 info = snmp.getRptOPL7Info(adcInfo, db);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	
//	public OBSnmpF5V11(int version, String host)
//	{
//		super(version, host);
//	}
//	public OBSnmpF5V11(String host)
//	{
//		super(host);
//	}
	public OBSnmpF5V11(String host, OBDtoAdcSnmpInfo authInfo)
	{
        super(host, authInfo);
	}	
	public DtoRptOPL4  getRptOPL4Info(int adcType, String swVersion, OBDatabase db)  throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();
		
		DtoOidRptOPL4 oid;
		try
		{
			oid = oidDB.getRptOPL4Info(adcType, swVersion);
			DtoRptOPL4 result = getRptOPL4Info(oid, db);
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

	private HashMap<String, DtoStatusTmp> getVSStatus(DtoOidStatVirtServer oid, ArrayList<String> vsNameList) throws OBException
	{
		HashMap<String, DtoStatusTmp> result = new HashMap<String, DtoStatusTmp>();

    	List<VariableBinding> tmpList;
    	
		// virtual server name
		tmpList = walk(oid.getVsName());
		for(VariableBinding var:tmpList)
		{
//			String name = parseOidName(var.getOid().toString(), oid.getVsName());
			String name = var.getVariable().toString();
			if((name!=null) && (!name.isEmpty()))
			{
				DtoStatusTmp info = new DtoStatusTmp();
				info.setAvail(OBDefine.STATUS_DISABLE);
				info.setEnable(OBDefine.STATUS_DISABLE);
				info.setVsName(name);
				info.setStatus(caluVsStatus(info.getAvail(), info.getEnable()));
				result.put(name, info);
			}
		}	
		
		// vs status 추출.
		tmpList = walk(oid.getStatus());
		for(VariableBinding var:tmpList)
		{
			String name = parseOidName(var.getOid().toString(), oid.getStatus());
			if((name!=null) && (!name.isEmpty()))
			{
				DtoStatusTmp info = result.get(name);
				if(info!=null)
				{
					info.setAvail(var.getVariable().toInt());
					info.setStatus(caluVsStatus(info.getAvail(), info.getEnable()));
				}
			}
		}	
		tmpList = walk(oid.getEnabled());
		for(VariableBinding var:tmpList)
		{
			String name = parseOidName(var.getOid().toString(), oid.getEnabled());
			if((name!=null) && (!name.isEmpty()))
			{
				DtoStatusTmp info = result.get(name);
				if(info!=null)
				{
					info.setEnable(var.getVariable().toInt());
					info.setStatus(caluVsStatus(info.getAvail(), info.getEnable()));
				}
			}
		}	
		
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result-size:%d", result.size()));
		return result;
	}
	
	private DtoRptOPL4 getRptOPL4Info(DtoOidRptOPL4 oid, OBDatabase db)  throws OBException
    {
		DtoRptOPL4 result = new DtoRptOPL4();
		
    	Snmp snmp = openSnmp();
    	
 //   	List<VariableBinding> tmpList;
    	String reqOid="";
    	try
    	{
    		// poolmember status
    		HashMap<String, DtoStatusTmp> pmStatusMap = getPoolMemberStatus(oid.getPoolMemberAddr(), oid.getPoolMemberStatusAvail(), oid.getPoolMemberStatusEnabled()); 
    		ArrayList<DtoStatusTmp> pmTmpStatusList = new ArrayList<DtoStatusTmp>(pmStatusMap.values());
    		
    		ArrayList<DtoRptPMStatus> pmStatusList = new ArrayList<DtoRptPMStatus>();
    		for(DtoStatusTmp tmp:pmTmpStatusList)
    		{
    			DtoRptPMStatus pmStatus = new DtoRptPMStatus();
    			pmStatus.setAddr(tmp.getNodeIP());
    			pmStatus.setPoolName(tmp.getPoolName());
    			pmStatus.setSrvPort(tmp.getSrvPort());
    			pmStatus.setStatus(tmp.getStatus());
    			pmStatusList.add(pmStatus);
    		}
	    	result.setPmList(pmStatusList);
	    	
	    	// vs status 추출. 
	    	HashMap<String, DtoStatusTmp> vsTmpStatusMap = getVSStatus(oid.getVsAddr(), oid.getVsStatusAvail(),oid.getVsStatusEnabled());
	    	ArrayList<DtoStatusTmp> vsTmpStatusList = new ArrayList<DtoStatusTmp>(vsTmpStatusMap.values());
	    	ArrayList<DtoRptVSStatus> vsStatusList = new ArrayList<DtoRptVSStatus>();
	    	for(DtoStatusTmp tmp:vsTmpStatusList)
	    	{
	    		DtoRptVSStatus vsStatus = new DtoRptVSStatus();
	    		vsStatus.setAddr(tmp.getNodeIP());
	    		vsStatus.setStatus(tmp.getStatus());
	    		vsStatusList.add(vsStatus);
	    	}
	    	result.setVsList(vsStatusList);
	
	    	// connection status
	    	DtoRptConnStatus connStatus = new DtoRptConnStatus();
	    	reqOid=oid.getConcurrentConn();
	    	if(reqOid!=null)
	    	{
		    	Object obj = getSnmp(snmp, reqOid);
		    	if(checkObject(obj))
		    		connStatus.setCurConn(Long.parseLong(obj.toString()));
				else
				{
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get concurrent connections. ip:%s, oid:%s", this.getHost(), reqOid));
				}
		    }
	    	reqOid=oid.getMaxConn();
	    	if(reqOid!=null)
	    	{
		    	Object obj = getSnmp(snmp, reqOid);
		    	if(checkObject(obj))
		    		connStatus.setMaxConn(Long.parseLong(obj.toString()));
				else
				{
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get max connections. ip:%s, oid:%s", this.getHost(), reqOid));
				}
	    	}
	    	result.setConnStatus(connStatus);
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

    private String convertIPAddress(OctetString ip)
    {
    	String result=(ip.get(0)&0xff)+"."+(ip.get(1)&0xff)+"."+(ip.get(2)&0xff)+"."+(ip.get(3)&0xff);
    	return result;
    }
    	
	private HashMap<String, DtoStatusTmp> getVSStatus(String vsIPAddrOid, String availOid, String enabledOid) throws OBException
	{
		HashMap<String, DtoStatusTmp> result = new HashMap<String, DtoStatusTmp>();

    	List<VariableBinding> tmpList;
    	
//		// virtual server name
//		tmpList = walk(oid.getVsName());
//		for(VariableBinding var:tmpList)
//		{
//			String name = parseOidName(var.getOid().toString(), oid.getVsName());
//			if((name!=null) && (!name.isEmpty()))
//			{
//				DtoStatusTmp info = new DtoStatusTmp();
//				info.setAvail(OBDefine.STATUS_DISABLE);
//				info.setEnable(OBDefine.STATUS_DISABLE);
//				info.setStatus(caluVsStatus(info.getAvail(), info.getEnable()));
//				result.put(name, info);
//			}
//		}	
		String reqOid="";
    	// VS 이름별로 VS 주소를 추출하여 hashmap에 저장한다.
		HashMap<String, String> vsIPMap = new HashMap<String, String>();
		reqOid=vsIPAddrOid;
    	if(reqOid!=null)
    	{
			tmpList = walk(reqOid);
			for(VariableBinding var:tmpList)
			{
				String ipAddr = convertIPAddress((OctetString)var.getVariable());
				String vsName = parseOidName(var.getOid().toString(), reqOid);
				vsIPMap.put(vsName, ipAddr);
			}
    	} 
    	
		// vs status 추출.
		tmpList = walk(availOid);
		for(VariableBinding var:tmpList)
		{
			String name = parseOidName(var.getOid().toString(), availOid);
			if((name!=null) && (!name.isEmpty()))
			{
				DtoStatusTmp info = new DtoStatusTmp();
				info.setAvail(var.getVariable().toInt());
				info.setEnable(OBDefine.STATUS_DISABLE);
				info.setStatus(caluVsStatus(info.getAvail(), info.getEnable()));
				info.setVsName(name);
				info.setNodeIP(vsIPMap.get(info.getVsName()));
				info.setStatus(caluVsStatus(info.getAvail(), info.getEnable()));
				result.put(name, info);
			}
		}	
		tmpList = walk(enabledOid);
		for(VariableBinding var:tmpList)
		{
			String name = parseOidName(var.getOid().toString(), enabledOid);
			if((name!=null) && (!name.isEmpty()))
			{
				DtoStatusTmp info = result.get(name);
				if(info!=null)
				{
					info.setEnable(var.getVariable().toInt());
					info.setStatus(caluVsStatus(info.getAvail(), info.getEnable()));
				}
			}
		}	
		
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result-size:%d", result.size()));
		return result;
	}
	
	private ArrayList<String> parseOidPoolMembers(String targetOid, String baseOid)
    {
    	int tmpIndex = targetOid.indexOf(baseOid);
    	String sLine = targetOid.substring(tmpIndex+baseOid.length());
    	String[] data = sLine.split("\\.");

    	int iIndex=0;
    	
    	for(;iIndex<data.length;iIndex++)
    	{
    		if(!data[iIndex].isEmpty())
				break;;
    	}
    	// pool name 추출.
    	iIndex++;
    	String poolName="";
    	for(;iIndex<data.length;iIndex++)
    	{
    		byte[] bytes = new byte[1];
    		bytes[0]=(byte) Integer.parseInt(data[iIndex]);
    		if((bytes[0]>')') && (bytes[0]<='~'))
    			poolName+=new String(bytes);
    		else
    			break;
    	}
    	
    	if(data.length<=iIndex+1)// || (data[iIndex+1].compareTo("4")!=0))
    	{
    		OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("Invalid poolName : %s", poolName));
    		return null;
    	}
    	// node name 주소 추출.
//    	iIndex+=2;
    	String nodeIPName="";
//    	iIndex++;
    	int nameLen = Integer.parseInt(data[iIndex]);
    	iIndex++;		
    	for(int i=0;i<nameLen;i++, iIndex++)
    	{
    		byte[] bytes = new byte[1];
    		bytes[0]=(byte) Integer.parseInt(data[iIndex]);
    		if((bytes[0]>='!') && (bytes[0]<='~'))
    			nodeIPName+=new String(bytes);
    		else
    			break;
    	} 	
    	// service port 추출.
    	String srvPort="";
    	srvPort = data[iIndex];
    	
    	ArrayList<String> result = new ArrayList<String>();
    	result.add(poolName);
    	result.add(nodeIPName);
    	result.add(srvPort);
    	return result;
    }	

    private Integer caluVsStatus(Integer availableState, Integer enabledState)
    {
    	if(enabledState!=1)
    	{// none or disabledbyparent
    		return OBDefine.STATE_DISABLE;
    	}
    	
    	switch(availableState)
    	{
    	case 1://available
    		return OBDefine.STATUS_AVAILABLE;
    	case 2://unavailable
    		return OBDefine.STATUS_UNAVAILABLE;
    	case 3: //offline
    		return OBDefine.STATUS_UNAVAILABLE;
    	case 4:// unknown
    		return OBDefine.STATUS_AVAILABLE;
    	default:
    		return OBDefine.STATUS_UNAVAILABLE;
    	}
    }
	
    private HashMap<String, DtoSnmpStatPoolMembers> getTrafficPoolMembers(DtoOidStatPoolMembers oid) throws OBException
    {
//		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. %s.", oid.toString()));
    	
		HashMap<String, DtoSnmpStatPoolMembers> result = new HashMap<String, DtoSnmpStatPoolMembers>();

    	List<VariableBinding> tmpList;
    	
    	try
		{
			// bytes in
			tmpList = walk(oid.getBytesIn());
			for(VariableBinding var:tmpList)
			{
				ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), oid.getBytesIn());
				if((nameList!=null) && (nameList.size()==3))
				{
					DtoSnmpStatPoolMembers member = new DtoSnmpStatPoolMembers();
					member.setPoolName(nameList.get(0));
					//member.setMemberIP(nameList.get(1));
					member.setNodeName(nameList.get(1));
					member.setServicePort(Integer.parseInt(nameList.get(2)));
					member.setBytesIn(var.getVariable().toLong());
					
					String name = member.getPoolName() + "_" + member.getNodeName() + "_" + member.getServicePort();
					result.put(name, member);
				}
				else
				{
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
				}
			}

			// bytes out
			tmpList = walk(oid.getBytesOut());
			for(VariableBinding var:tmpList)
			{
				ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), oid.getBytesOut());
				if((nameList!=null) && (nameList.size()==3))
				{
					String name=nameList.get(0)+"_"+nameList.get(1)+"_"+nameList.get(2);
					DtoSnmpStatPoolMembers member = result.get(name);
					if(member!=null)
						member.setBytesOut(var.getVariable().toLong());
					else
						throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
				}
				else
				{
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
				}
			}	
			
			// pkts in
			tmpList = walk(oid.getPktsIn());
			for(VariableBinding var:tmpList)
			{
				ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), oid.getPktsIn());
				if((nameList!=null) && (nameList.size()==3))
				{
					String name=nameList.get(0)+"_"+nameList.get(1)+"_"+nameList.get(2);
					DtoSnmpStatPoolMembers member = result.get(name);
					if(member!=null)
						member.setPktsIn(var.getVariable().toLong());
					else
						throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
				}
				else
				{
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
				}
			}	
			
			// pkts out
			tmpList = walk(oid.getPktsOut());
			for(VariableBinding var:tmpList)
			{
				ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), oid.getPktsOut());
				if((nameList!=null) && (nameList.size()==3))
				{
					String name=nameList.get(0)+"_"+nameList.get(1)+"_"+nameList.get(2);
					DtoSnmpStatPoolMembers member = result.get(name);
					if(member!=null)
						member.setPktsOut(var.getVariable().toLong());
					else
						throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
				}
				else
				{
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
				}
			}				
			// max conns
			tmpList = walk(oid.getMaxConns());
			for(VariableBinding var:tmpList)
			{
				ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), oid.getMaxConns());
				if((nameList!=null) && (nameList.size()==3))
				{
					String name=nameList.get(0)+"_"+nameList.get(1)+"_"+nameList.get(2);
					DtoSnmpStatPoolMembers member = result.get(name);
					if(member!=null)
						member.setMaxConns(var.getVariable().toLong());
					else
						throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
				}
				else
				{
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
				}
			}			
			// tot conns
			tmpList = walk(oid.getTotConns());
			for(VariableBinding var:tmpList)
			{
				ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), oid.getTotConns());
				if((nameList!=null) && (nameList.size()==3))
				{
					String name=nameList.get(0)+"_"+nameList.get(1)+"_"+nameList.get(2);
					DtoSnmpStatPoolMembers member = result.get(name);
					if(member!=null)
						member.setTotConns(var.getVariable().toLong());
					else
						throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
				}
				else
				{
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
				}
			}			
			// cur conns
			tmpList = walk(oid.getCurConns());
			for(VariableBinding var:tmpList)
			{
				ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), oid.getCurConns());
				if((nameList!=null) && (nameList.size()==3))
				{
					String name=nameList.get(0)+"_"+nameList.get(1)+"_"+nameList.get(2);
					DtoSnmpStatPoolMembers member = result.get(name);
					if(member!=null)
						member.setCurConns(var.getVariable().toLong());
					else
						throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
				}
				else
				{
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
				}
			}
			
			//node ip 구하기. F5 v11부터는 IP를 member에서 식별할 수 없으므로 직접 구한다.
			tmpList = walk(oid.getNodeIPAddress());
			for(VariableBinding var:tmpList)
			{
				ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), oid.getNodeIPAddress());
				if((nameList!=null) && (nameList.size()==3))
				{
					String name=nameList.get(0)+"_"+nameList.get(1)+"_"+nameList.get(2);
					DtoSnmpStatPoolMembers member = result.get(name);
					if(member!=null)
					{
						String ipAddress = convertIPAddress((OctetString)var.getVariable());
						member.setMemberIP(ipAddress);
					}
					else
					{
						throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
					}
				}
				else
				{
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
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
    	// hash key를 poolName+memberIP+servicePort로 바꾼다. member(node) IP를 먼저 알 수 없기 때문에 nodeName으로 key를 만들었다가 마지막에 바꾸는 것이다.
    	ArrayList<DtoSnmpStatPoolMembers> retList = new ArrayList<DtoSnmpStatPoolMembers>(result.values());
    	HashMap<String, DtoSnmpStatPoolMembers> retVal = new HashMap<String, DtoSnmpStatPoolMembers>();
    	for(DtoSnmpStatPoolMembers obj: retList)
    	{
    		String key = obj.getPoolName()+"_"+obj.getMemberIP()+"_"+obj.getServicePort();
    		retVal.put(key, obj);
    	}
		return retVal;
    }
    
    public ArrayList<OBDtoMonTrafficPoolMembers>  getTrafficPoolMembers(int adcType, String swVersion, ArrayList<OBDtoAdcVServerF5> vsList)  throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();
		
		DtoOidStatPoolMembers oid;
		try
		{
			oid = oidDB.getStatPoolMembers(adcType, swVersion);
		
			HashMap<String, DtoSnmpStatPoolMembers> poolMembersTraffic = getTrafficPoolMembers(oid);
//			ArrayList<OBDtoAdcVServerF5> vsList = new OBVServerDB().getVServerListAllF5(adcInfo.getIndex());
			ArrayList<OBDtoMonTrafficPoolMembers> result = new ArrayList<OBDtoMonTrafficPoolMembers>();
			for(OBDtoAdcVServerF5 vsInfo : vsList)
			{
				OBDtoAdcPoolF5 pool = vsInfo.getPool();
				if(pool==null)
					continue;
				
				ArrayList<OBDtoAdcPoolMemberF5> memberList = pool.getMemberList();
				for(OBDtoAdcPoolMemberF5 member: memberList)
				{
					String name=pool.getName()+"_"+member.getIpAddress()+"_"+member.getPort();
					DtoSnmpStatPoolMembers stat = poolMembersTraffic.get(name);
					
					if(stat==null)
						continue;
	
					OBDtoMonTrafficPoolMembers traffic = new OBDtoMonTrafficPoolMembers();
					traffic.setBytesIn(stat.getBytesIn());
					traffic.setBytesOut(stat.getBytesOut());
					traffic.setCurConns(stat.getCurConns());
					traffic.setMaxConns(stat.getMaxConns());
					traffic.setNodeAddress(member.getIpAddress());
					traffic.setNodePort(member.getPort());
					traffic.setPktsIn(stat.getPktsIn());
					traffic.setPktsOut(stat.getPktsOut());
					traffic.setPoolIndex(pool.getIndex());
					traffic.setPoolName(pool.getName());
					traffic.setSrvPort(vsInfo.getServicePort());
					traffic.setTotConns(stat.getTotConns());
					traffic.setVsIndex(vsInfo.getIndex());
					traffic.setVsIPAddress(vsInfo.getvIP());
					traffic.setVsName(vsInfo.getName());
					
					result.add(traffic);
				}
			}		
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result-size:%d", result.size()));
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
    private HashMap<String, DtoStatusTmp> getPoolMemberStatusPartial(String nodeIPOid, String availOid, String enabledOid, ArrayList<String> vsNameList) throws OBException
    {
        HashMap<String, DtoStatusTmp> result = new HashMap<String, DtoStatusTmp>();
        String vsOid = "";
        String walkOid = ""; //availOid와 eanbledOid에 vsOid를 붙여서 만듦. Oid를 범위를 좁혀 부분 walk를 vs별로 하게.
        long tt0, tt1, tt2, tt3, tt4, tt5;
        List<VariableBinding> tmpList;
        try
        {
            tt0 = System.currentTimeMillis();
            for(String vsName: vsNameList)
            {
                OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "pool member status partial read. vsName = " + vsName);
                vsOid = makeVsOid(vsName);
                tt1 = System.currentTimeMillis();//기준시간

                if(availOid!=null)
                {
                    walkOid = availOid + vsOid;
                    tmpList = walk(walkOid);
                    for(VariableBinding var:tmpList)
                    {
                        //아래 실행문 주의할 것. walk는 vsOid를 붙여서 했지만 parsing은 원래대로 한다. availOid로. 이렇게 하면 부분업데이트에 대한 별도 파싱을 짜지 않아도 된다.
                        ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), availOid);
                        if((nameList!=null) && (nameList.size()==3))
                        {
                            DtoStatusTmp member = new DtoStatusTmp();
                            
                            member.setAvail(var.getVariable().toInt());
                            member.setEnable(OBDefine.STATUS_DISABLE);
                            member.setStatus(caluVsStatus(member.getAvail(), member.getEnable()));
                            
                            member.setPoolName(nameList.get(0));
                            member.setNodeName(nameList.get(1));
                            member.setSrvPort(Integer.parseInt(nameList.get(2)));
                            
                            String name=nameList.get(0)+"_"+nameList.get(1)+"_"+nameList.get(2);
                            
                            result.put(name, member);
                        }
                        else
                        {
                            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
                        }
                    }
                }
                tt2 = System.currentTimeMillis();
                OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "     partial availOid read time: " + (tt2-tt1)/1000.0 );
                //node ip 구하기. F5 v11부터는 IP를 member에서 식별할 수 없으므로 직접 구한다.
                if(nodeIPOid!=null)
                {
                    walkOid = nodeIPOid + vsOid;
                    tmpList = walk(walkOid);
                    for(VariableBinding var:tmpList)
                    {
                        //아래 실행문 주의할 것. walk는 vsOid를 붙여서 했지만 parsing은 원래대로 한다. nodeIPOid로. 이렇게 하면 부분업데이트에 대한 별도 파싱을 짜지 않아도 된다.
                        ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), nodeIPOid);
                        if((nameList!=null) && (nameList.size()==3))
                        {
                            String name=nameList.get(0)+"_"+nameList.get(1)+"_"+nameList.get(2);
                            DtoStatusTmp member = result.get(name);
                            if(member!=null)
                            {
                                String ipAddress = convertIPAddress((OctetString)var.getVariable());
                                member.setNodeIP(ipAddress);
                            }
                            else
                            {
                                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
                            }
                        }
                        else
                        {
                            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
                        }
                    }
                }
                tt3 = System.currentTimeMillis();
                OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "     partial nodeIPOid read time: " + (tt3-tt2)/1000.0 );
                if(enabledOid!=null)
                {
                    walkOid = enabledOid + vsOid;
                    tmpList = walk(walkOid);
                    for(VariableBinding var:tmpList)
                    {
                        //아래 실행문 주의할 것. walk는 vsOid를 붙여서 했지만 parsing은 원래대로 한다. enabledOid로. 이렇게 하면 부분업데이트에 대한 별도 파싱을 짜지 않아도 된다.
                        ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), enabledOid);
                        if((nameList!=null) && (nameList.size()==3))
                        {
                            String name=nameList.get(0)+"_"+nameList.get(1)+"_"+nameList.get(2);
                            DtoStatusTmp member = result.get(name);
                            if(member!=null)
                            {
                                member.setEnable(var.getVariable().toInt());
                                member.setStatus(caluVsStatus(member.getAvail(), member.getEnable()));
                            }
                            else
                            {
                                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
                            }
                        }
                        else
                        {
                            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
                        }
                    }
                }
                tt4 = System.currentTimeMillis();
                OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "     partial enabledOid read time: " + (tt4-tt3)/1000.0 );
            }
            tt5 = System.currentTimeMillis();
            OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "read time: " + (tt5-tt0)/1000.0 );
            OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "read size: " + result.size());
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
            
        }
        // hash key를 poolName+nodeIP+servicePort형태로 변경한다.
        ArrayList<DtoStatusTmp> retList = new ArrayList<DtoStatusTmp>(result.values());
        HashMap<String, DtoStatusTmp> retVal = new HashMap<String, DtoStatusTmp>();
        for(DtoStatusTmp obj: retList)
        {
            String key = obj.getPoolName()+"_"+obj.getNodeIP()+"_"+obj.getSrvPort();
            retVal.put(key, obj);
        }
        
        return retVal;
    }
    private HashMap<String, DtoStatusTmp> getPoolMemberStatus(String nodeIPOid, String availOid, String enabledOid) throws OBException
    {
        HashMap<String, DtoStatusTmp> result = new HashMap<String, DtoStatusTmp>();
        try
        {
            List<VariableBinding> tmpList;
            
            if(availOid!=null)
            {
                tmpList = walk(availOid);
                for(VariableBinding var:tmpList)
                {
                    ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), availOid);
                    if((nameList!=null) && (nameList.size()==3))
                    {
                        DtoStatusTmp member = new DtoStatusTmp();
                        
                        member.setAvail(var.getVariable().toInt());
                        member.setEnable(OBDefine.STATUS_DISABLE);
                        member.setStatus(caluVsStatus(member.getAvail(), member.getEnable()));
                        
                        member.setPoolName(nameList.get(0));
                        member.setNodeName(nameList.get(1));
                        member.setSrvPort(Integer.parseInt(nameList.get(2)));
                        
                        String name=nameList.get(0)+"_"+nameList.get(1)+"_"+nameList.get(2);
                        
                        result.put(name, member);
                    }
                    else
                    {
                		OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("Invalid nameList : %s", nameList));
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
                    }
                }
            }
            //node ip 구하기. F5 v11부터는 IP를 member에서 식별할 수 없으므로 직접 구한다.
            if(nodeIPOid!=null)
            {
                tmpList = walk(nodeIPOid);
                for(VariableBinding var:tmpList)
                {
                    ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), nodeIPOid);
                    if((nameList!=null) && (nameList.size()==3))
                    {
                        String name=nameList.get(0)+"_"+nameList.get(1)+"_"+nameList.get(2);
                        DtoStatusTmp member = result.get(name);
                        if(member!=null)
                        {
                            String ipAddress = convertIPAddress((OctetString)var.getVariable());
                            member.setNodeIP(ipAddress);
                        }
                        else
                        {
                            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
                        }
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
                    }
                }
            }

            if(enabledOid!=null)
            {
                tmpList = walk(enabledOid);
                for(VariableBinding var:tmpList)
                {
                    ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), enabledOid);
                    if((nameList!=null) && (nameList.size()==3))
                    {
                        String name=nameList.get(0)+"_"+nameList.get(1)+"_"+nameList.get(2);
                        DtoStatusTmp member = result.get(name);
                        if(member!=null)
                        {
                            member.setEnable(var.getVariable().toInt());
                            member.setStatus(caluVsStatus(member.getAvail(), member.getEnable()));
                        }
                        else
                        {
                            throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
                        }
                    }
                    else
                    {
                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid data");
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
        // hash key를 poolName+nodeIP+servicePort형태로 변경한다.
        ArrayList<DtoStatusTmp> retList = new ArrayList<DtoStatusTmp>(result.values());
        HashMap<String, DtoStatusTmp> retVal = new HashMap<String, DtoStatusTmp>();
        for(DtoStatusTmp obj: retList)
        {
            String key = obj.getPoolName()+"_"+obj.getNodeIP()+"_"+obj.getSrvPort();
            retVal.put(key, obj);
        }
        
        return retVal;
    }
   
//	public static void main(String[] args)
//	{
//		OBDtoAdcInfo adcInfo;
//		try
//		{
//			adcInfo = new OBAdcManagementImpl().getAdcInfo(3);
//			OBSnmpF5 snmp = new OBSnmpF5V11("192.168.200.18", adcInfo.getSnmpInfo());
//			OBDatabase db=new OBDatabase();
//			db.openDB();
//			ArrayList<OBDtoAdcVServerF5> vsList = snmp.getSlbStatus(adcInfo, db);
//			for(OBDtoAdcVServerF5 vs:vsList)
//				System.out.println(vs);
//			db.closeDB();
//		}
//		catch(OBException e)
//		{
//			e.printStackTrace();
//		}
//	}
    public ArrayList<OBDtoAdcVServerF5> getSlbStatusPartial(int adcType, String swVersion, ArrayList<OBDtoAdcVServerF5> vsList) throws OBException
    {
        return getSlbStatusCore(adcType, swVersion, vsList, OBDefine.RANGE.PARTIAL_VS); //부분업데이트
    }
    public ArrayList<OBDtoAdcVServerF5> getSlbStatusAll(int adcType, String swVersion, ArrayList<OBDtoAdcVServerF5> vsList) throws OBException
    {
        return getSlbStatusCore(adcType, swVersion, vsList, OBDefine.RANGE.ALL); //전체 읽기
    }
	private ArrayList<OBDtoAdcVServerF5> getSlbStatusCore(int adcType, String swVersion, ArrayList<OBDtoAdcVServerF5> vsList, int range) throws OBException
	{
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
//		ArrayList<OBDtoAdcVServerF5> vsList = new OBVServerDB().getVServerListAllF5(adcInfo.getIndex());
		
		OBSnmpOidDB oidDB = new OBSnmpOidDB();
		
		DtoOidStatVirtServer oidVSStat = oidDB.getStatVirtServer(adcType, swVersion);
		DtoOidInfoAdcResc oidSysResc = oidDB.getAdcResource(adcType, swVersion);
		HashMap<String, DtoStatusTmp> poolMemberMap;
	    HashMap<String, DtoStatusTmp> vsMap = getVSStatus(oidVSStat, null);
	    
        if(range==OBDefine.RANGE.ALL)
        {
            poolMemberMap = getPoolMemberStatus(oidSysResc.getNodeIPAddress(), oidSysResc.getNodeAvaliableState(), oidSysResc.getNodeEnabledState());
        }
        else //range==OBDefine.RANGE.PARTIAL_VS, 부분 읽기. 특정 vs
        {
            ArrayList<String> vsNameList = new ArrayList<String>();
            for(OBDtoAdcVServerF5 vs: vsList)
            {
                vsNameList.add(vs.getName());
            }
            poolMemberMap = getPoolMemberStatusPartial(oidSysResc.getNodeIPAddress(), oidSysResc.getNodeAvaliableState(), oidSysResc.getNodeEnabledState(), vsNameList);
        }
		
		for(OBDtoAdcVServerF5 vs:vsList)
		{
			DtoStatusTmp vsStatus = vsMap.get(vs.getName());
			if(vsStatus==null)
			{
				vs.setStatus(OBDefine.STATUS_DISABLE);
			}
			else
				vs.setStatus(vsStatus.getStatus());
			OBDtoAdcPoolF5 pool = vs.getPool();
			if(pool==null)
			{
				continue;
			}
			ArrayList<OBDtoAdcPoolMemberF5> memberList = pool.getMemberList();
			if(memberList==null || memberList.size()==0)
			{
				continue;
			}
			for(OBDtoAdcPoolMemberF5 member:memberList)
			{
				String name=pool.getName()+"_"+member.getIpAddress()+"_"+member.getPort();
				DtoStatusTmp memberStatus = poolMemberMap.get(name);
				if(memberStatus==null)
				{
					member.setStatus(OBDefine.STATUS_DISABLE);
				}
				else
				{
					member.setStatus(memberStatus.getStatus());
				}
			}
		}
		return vsList;
	}
	
	// TODO: pool member ip 파싱 구현 필요
	protected String parsePoolMemberIPName(String targetOid, String baseOid) throws OBException
    {
        String result = "";
        // base OID 제거
        int tmpIndex = targetOid.indexOf(baseOid);
        String sLine = targetOid.substring(tmpIndex + baseOid.length() + 1);
        String[] data = sLine.split("\\.");

        // Pool Name 제거
        data = Arrays.copyOfRange(data, Integer.parseInt(data[0]) + 1, data.length);
        
        // Member Name 추출
        String[] memberNameData = Arrays.copyOfRange(data, 0, Integer.parseInt(data[0]) + 1);
        for (String charCodeString : memberNameData)
        {
            result += Character.toChars(Integer.parseInt(charCodeString))[0];
        }
	    
        return result;
    }
    
	protected String parsePoolMemberPortName(String targetOid, String baseOid) throws OBException
    {
        String result = "";
        String[] data = targetOid.split("\\.");
        result = data[data.length - 1] + "";
        return result;
    }
	
    public int getHAStatus(int adcType, String swVersion) throws OBException
    {
        OBSnmpOidDB oidDB = new OBSnmpOidDB();

        try
        {
            OBDtoOidRptInspection oid = oidDB.getRptInspection(adcType, swVersion);
            int retVal = getHAStatus(oid.getHaStatus());

            
            return retVal;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "general: "+e.getMessage());

        }
    }
    
    /**
     * HA status를 조회함.
     * @param oid
     * @return
     * @throws OBException
     */
    private int getHAStatus(String oid) throws OBException
    {
        // OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. %s.", oid.toString()));
        int retVal = -1;
        
        Snmp snmp = null;
        String queryOid = "";
        try
        {
            snmp = openSnmp();
            
            //"rptHAStatus"
            try
            {
                queryOid = oid;
                if((queryOid != null) && (!queryOid.isEmpty()))
                {
                    Object obj = getSnmp(snmp, queryOid);
                    if(checkObject(obj))
                        retVal = (((Integer32)obj).toInt());//0: 4: active, 3: stand-by, 2: forcedoffline, 1: offline, 0: unknown
                }
            }
            catch(Exception e)
            {
                OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
            }
            // a.위에서 추출한 값을 이용해서 active/standby 값으로 변환한다.
            if(retVal==3)
                retVal = OBDtoAdcInfo.ACTIVE_STANDBY_STATE_STANDBY;
            else if(retVal>=4)
                retVal = OBDtoAdcInfo.ACTIVE_STANDBY_STATE_ACTIVE;
            else 
                retVal = OBDtoAdcInfo.ACTIVE_STANDBY_STATE_UNKNOWN;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "general: " + e.getMessage());
        }
        finally
        {
            if(snmp!=null) closeSnmp(snmp);
        }
        
        return retVal;
    }
}
