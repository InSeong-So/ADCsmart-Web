package kr.openbase.adcsmart.service.snmp.pask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.snmp4j.Snmp;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.VariableBinding;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSnmpInfo;
import kr.openbase.adcsmart.service.dto.OBDtoConnectionData;
import kr.openbase.adcsmart.service.dto.OBDtoHddInfo;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonL2Ports;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficVServer;
import kr.openbase.adcsmart.service.snmp.OBSnmp;
import kr.openbase.adcsmart.service.snmp.OBSnmpOidDB;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidCfgPorts;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidFaultCheckHW;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidInfoCfgPASK;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidStatVirtServer;
import kr.openbase.adcsmart.service.snmp.dto.OBDtoAdcPerformanceData;
import kr.openbase.adcsmart.service.snmp.pask.dto.DtoSnmpAdcRescPASK;
import kr.openbase.adcsmart.service.snmp.pask.dto.OBDtoMonTrafficPoolMembersPASK;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBSnmpPASK extends OBSnmp
{
//	public OBSnmpPASK(int version, String host)
//	{
//        setSnmpVersion(version);
//        setHost(host);
//	}
//	public OBSnmpPASK(String host)
//	{
//        setSnmpVersion(OBSnmp.VERSION_2C);
//        setHost(host);
//	}
	public OBSnmpPASK(String host, OBDtoAdcSnmpInfo authInfo)
	{
        super(host, authInfo);
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
			tmpVal = 0;
    		if(oidReq!=null && !oidReq.isEmpty())
    		{
				tmpList = walk(oidReq);
				for(VariableBinding var:tmpList)
				{
					tmpVal += var.getVariable().toLong()*1000;
				}
    		}
			retVal.setClientBytesIn(tmpVal);
			
			// bps out
			oidReq = oid.getBytesOut();
			tmpVal = 0;
    		if(oidReq!=null && !oidReq.isEmpty())
    		{
				tmpList = walk(oidReq);
				for(VariableBinding var:tmpList)
				{
					tmpVal += var.getVariable().toLong()*1000;
				}
    		}
			retVal.setClientBytesOut(tmpVal);
    		
			// pps in
			oidReq = oid.getPktsIn();
			tmpVal = 0;
    		if(oidReq!=null && !oidReq.isEmpty())
    		{
				tmpList = walk(oidReq);
				for(VariableBinding var:tmpList)
				{
					tmpVal += var.getVariable().toLong();
				}
    		}
			retVal.setClientPktsIn(tmpVal);
			
			// pps out
			oidReq = oid.getPktsOut();
			tmpVal = 0;
    		if(oidReq!=null && !oidReq.isEmpty())
    		{
				tmpList = walk(oidReq);
				for(VariableBinding var:tmpList)
				{
					tmpVal += var.getVariable().toLong();
				}
    		}
			retVal.setClientPktsOut(tmpVal);
			
			// cps 
			oidReq = oid.getCurConns();
			tmpVal = 0;
    		if(oidReq!=null && !oidReq.isEmpty())
    		{
				tmpList = walk(oidReq);
				for(VariableBinding var:tmpList)
				{
					tmpVal += var.getVariable().toLong();
				}
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
//		OBSnmpPASK snmp = new OBSnmpPASK(OBSnmp.VERSION_2C, "192.168.200.110");
//		OBDtoAdcInfo adcInfo=new OBDtoAdcInfo();
//		adcInfo.setAdcType(OBDefine.ADC_TYPE_PIOLINK_PASK);
//		adcInfo.setSwVersion("");		
//		adcInfo.setSnmpRComm("default");
//		OBDatabase db = new OBDatabase();
//		try
//		{
//			db.openDB();
//			OBDtoConnectionData info = snmp.getVServerConns(adcInfo, "web_80", db);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		
//	}
	
	public OBDtoConnectionData  getVServerConns(int adcType, String swVersion, String vsName)  throws OBException
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
	    	result.setTotConns((long)0);
	    	result.setMaxConns((long)0);
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
	
//	// vs, poolmember단위로 status정보만 추출한다. 
//	public ArrayList<OBDtoAdcVServerPASK> getSlbStatus(OBDtoAdcInfo adcInfo, OBDatabase db) throws OBException
//	{// telnet 을 이용해서 추출한다. 
//		return null;
//	}
	
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
	{// telnet 을 이용해서 추출한다.
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. oid:%s", oid));
//		ArrayList<OBDtoMonL2Ports> result=new ArrayList<OBDtoMonL2Ports>();
		HashMap<Integer, OBDtoMonL2Ports> portInfoMap=new HashMap<Integer, OBDtoMonL2Ports>();
		
		List<VariableBinding> tmpList;
		
		try
		{
			String oidReq ="";
			// get a interface name
			oidReq = oid.getPortName();
			if(oidReq!=null && !oidReq.isEmpty())
			{
				tmpList = walk(oidReq);
				for(VariableBinding var:tmpList)
				{
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oidReq);
					if(oidList.size()!=1)
					{
						throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);//, String.format("invalid data.(target:%s, portName:%s)", var.getOid().toString(), oidReq));
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
			oidReq = oid.getPortAliasName();
			if(oidReq!=null && !oidReq.isEmpty())			
			{
				tmpList = walk(oidReq);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oidReq);
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
			oidReq = oid.getPortDuplex();
			if(oidReq!=null && !oidReq.isEmpty())				
			{
				tmpList = walk(oidReq);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oidReq);
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
			oidReq = oid.getPortPktsDiscardsIn();
			if(oidReq!=null && !oidReq.isEmpty())				
			{
				tmpList = walk(oidReq);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oidReq);
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
			oidReq = oid.getPortPktsDiscardsOut();
			if(oidReq!=null && !oidReq.isEmpty())			
			{
				tmpList = walk(oidReq);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oidReq);
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
			oidReq = oid.getPortBytesIn();
			if(oidReq!=null && !oidReq.isEmpty())
			{			
				tmpList = walk(oidReq);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oidReq);
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
			oidReq = oid.getPortBytesOut();
			if(oidReq!=null && !oidReq.isEmpty())			
			{
				tmpList = walk(oidReq);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oidReq);
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
			oidReq = oid.getPortPktsErrorsIn();
			if(oidReq!=null && !oidReq.isEmpty())			
			{
				tmpList = walk(oidReq);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oidReq);
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
			oidReq = oid.getPortPktsErrorsOut();
			if(oidReq!=null && !oidReq.isEmpty())			
			{
				tmpList = walk(oidReq);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oidReq);
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
			oidReq = oid.getPortPktsIn();
			if(oidReq!=null && !oidReq.isEmpty())			
			{
				tmpList = walk(oidReq);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oidReq);
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
			oidReq = oid.getPortPktsOut();
			if(oidReq!=null && !oidReq.isEmpty())			
			{
				tmpList = walk(oidReq);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oidReq);
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
			
			// mulit in
			oidReq = oid.getPortPktsMultiIn();
			if(oidReq!=null && !oidReq.isEmpty())			
			{
				tmpList = walk(oidReq);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oidReq);
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
			
			// multi out
			oidReq = oid.getPortPktsMultiOut();
			if(oidReq!=null && !oidReq.isEmpty())			
			{
				tmpList = walk(oidReq);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oidReq);
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
			
			// broad in
			oidReq = oid.getPortPktsBroadIn();
			if(oidReq!=null && !oidReq.isEmpty())			
			{
				tmpList = walk(oidReq);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oidReq);
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
			oidReq = oid.getPortPktsBroadOut();
			if(oidReq!=null && !oidReq.isEmpty())			
			{
				tmpList = walk(oidReq);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oidReq);
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
//			oidReq = oid.getPortPktsUnknownProtos();
//			if(oidReq!=null && !oidReq.isEmpty())			
//			{
//				tmpList = walk(oidReq);
//				for(int i=0;i<tmpList.size();i++)
//				{
//					VariableBinding var=tmpList.get(i);
//					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oidReq);
//					if(oidList.size()==1)
//					{
//						Integer index = oidList.get(0);
//						OBDtoMonL2Ports info = portInfoMap.get(index);
//						if(null != info)
//						{
//							info.set(var.getVariable().toLong());
//						}					
//					}
//				}
//			}
			
			// speed
			oidReq = oid.getPortSpeed();
			if(oidReq!=null && !oidReq.isEmpty())			
			{
				tmpList = walk(oidReq);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oidReq);
					if(oidList.size()==1)
					{
						Integer index = oidList.get(0);
						OBDtoMonL2Ports info = portInfoMap.get(index);
						if(null != info)
						{
							info.setSpeed(convertPortSpeed(var.getVariable().toInt()));
						}					
					}
					else
					{
						throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);//, String.format("invalid data.(target:%s, portName:%s)", var.getOid().toString(), oid.getPortName()));
					}
				}
			}
			
			// state
			oidReq = oid.getPortStatus();
			if(oidReq!=null && !oidReq.isEmpty())			
			{
				tmpList = walk(oidReq);
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oidReq);
					if(oidList.size()==1)
					{
						Integer index = oidList.get(0);
						OBDtoMonL2Ports info = portInfoMap.get(index);
						if(null != info)
						{
							info.setStatus(convertPortStatus(var.getVariable().toInt()));
						}					
					}
					else
					{
						throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);//, String.format("invalid data.(target:%s, portName:%s)", var.getOid().toString(), oid.getPortName()));
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
	
	private ArrayList<String> parseOidNameID(String targetOid, String baseOid)
	{//SNMPv2-SMI::enterprises.10188.1.1.1.2.1.3.6.98.119.112.97.114.107.1 = IpAddress: 192.168.199.43
		int tmpIndex = targetOid.indexOf(baseOid);
		String sLine = targetOid.substring(tmpIndex+baseOid.length()+1);
		String[] data = sLine.split("\\.");
		String name="";
		int length = Integer.parseInt(data[0]);
		if(length+1>=data.length)
			return null;
		
    	for(int i=1;i<=length;i++)
    	{
    		byte[] bytes = new byte[1];
    		bytes[0]=(byte) Integer.parseInt(data[i]);
    		if((bytes[0]>='!') && (bytes[0]<='~'))
    		{
    			String letter = new String(bytes);
    			name+=letter;
    		}
    	}
    	
		ArrayList<String> retVal = new ArrayList<String>();
		retVal.add(name);
		retVal.add(data[length+1]);
		return retVal;
	}
	
//	public static void main(String[] args)
//	{
//		OBSnmpPASK snmp = new OBSnmpPASK("192.168.200.110");
//		try
//		{
//			OBDatabase db = new OBDatabase();
//			db.openDB();
//			DtoSnmpAdcRescPASK list = snmp.getAdcResc(OBDefine.ADC_TYPE_PIOLINK_PASK, "", "default", db);
//			System.out.println(list);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
    
    public DtoSnmpAdcRescPASK getAdcResc(int adcType, String swVersion)  throws OBException
    {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();
		
		try
		{
			DtoOidInfoCfgPASK oidInfo = oidDB.getCfgPASK(adcType, swVersion);
			DtoSnmpAdcRescPASK info = getAdcResc(oidInfo);
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
//			oidInfo = oidDB.getAdcResource(OBDefine.ADC_TYPE_PIOLINK_PASK, "", db);
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
//		} 
//    }
//    
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
////			OBSystemLog.debug(OBDefine.LOGFILE_SYSTEM, String.format("end. result:%s.", result));
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
    
    private DtoSnmpAdcRescPASK getAdcResc(DtoOidInfoCfgPASK oid) throws OBException
    {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. oid:%s", oid));
		
		DtoSnmpAdcRescPASK retVal = new DtoSnmpAdcRescPASK();
		String tempOid;
		List<VariableBinding> tmpList;

    	try
    	{
			tempOid = oid.getResManCpuUsage();
			if((tempOid!=null) && (!tempOid.isEmpty()))
			{// cpu usage
				tmpList = walk(tempOid);
				long cpuUsed=0;
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					if(var!=null)
						cpuUsed += var.getVariable().toLong();
				}
				if(tmpList.size()>0)
					retVal.setMpCpuUsage((int) (cpuUsed/tmpList.size()));
				else
					retVal.setMpCpuUsage(0);
			}
			
			tempOid = oid.getResManMemFree();
			if((tempOid!=null) && (!tempOid.isEmpty()))
			{
				tmpList = walk(tempOid);
				long tmpVal = 0;
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					if(var!=null)
						tmpVal += var.getVariable().toLong();
				}
				if(tmpList.size()>0)
					retVal.setMpMemFree(tmpVal/tmpList.size());
				else
					retVal.setMpMemFree(0L);
			}
		
			tempOid = oid.getResManMemTotal();
			if((tempOid!=null) && (!tempOid.isEmpty()))
			{
				tmpList = walk(tempOid);
				long tmpVal = 0;
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					if(var!=null)
						tmpVal += var.getVariable().toLong();
				}
				if(tmpList.size()>0)
					retVal.setMpMemTotal(tmpVal/tmpList.size());
				else
					retVal.setMpMemTotal(0L);
			}		
	
			tempOid = oid.getResManMemUsage();
			if((tempOid!=null) && (!tempOid.isEmpty()))
			{
				tmpList = walk(tempOid);
				long tmpVal = 0;
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					if(var!=null)
						tmpVal += var.getVariable().toLong();
				}
				if(tmpList.size()>0)
					retVal.setMpMemUsage((int) (tmpVal/tmpList.size()));
				else
					retVal.setMpMemUsage(0);
			}	
			
			tempOid = oid.getResManMemUsed();
			if((tempOid!=null) && (!tempOid.isEmpty()))
			{
				tmpList = walk(tempOid);
				long tmpVal = 0;
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					if(var!=null)
						tmpVal += var.getVariable().toLong();
				}
				if(tmpList.size()>0)
					retVal.setMpMemUsed((tmpVal/tmpList.size()));
				else
					retVal.setMpMemUsed(0L);
			}
			
			tempOid = oid.getResPktCpuUsage();
			if((tempOid!=null) && (!tempOid.isEmpty()))
			{// cpu usage
				tmpList = walk(tempOid);
				long cpuUsed=0;
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					if(var!=null)
						cpuUsed += var.getVariable().toLong();
				}
				if(tmpList.size()>0)
					retVal.setSpCpuUsage((int) (cpuUsed/tmpList.size()));
				else
					retVal.setSpCpuUsage(0);
			}
			
			tempOid = oid.getResPktMemFree();
			if((tempOid!=null) && (!tempOid.isEmpty()))
			{
				tmpList = walk(tempOid);
				long tmpVal = 0;
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					if(var!=null)
						tmpVal += var.getVariable().toLong();
				}
				if(tmpList.size()>0)
					retVal.setSpMemFree(tmpVal/tmpList.size());
				else
					retVal.setSpMemFree(0L);
			}
		
			tempOid = oid.getResPktMemTotal();
			if((tempOid!=null) && (!tempOid.isEmpty()))
			{
				tmpList = walk(tempOid);
				long tmpVal = 0;
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					if(var!=null)
						tmpVal += var.getVariable().toLong();
				}
				if(tmpList.size()>0)
					retVal.setSpMemTotal(tmpVal/tmpList.size());
				else
					retVal.setSpMemTotal(0L);
			}		
	
			tempOid = oid.getResPktMemUsage();
			if((tempOid!=null) && (!tempOid.isEmpty()))
			{
				tmpList = walk(tempOid);
				long tmpVal = 0;
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					if(var!=null)
						tmpVal += var.getVariable().toLong();
				}
				if(tmpList.size()>0)
					retVal.setSpMemUsage((int) (tmpVal/tmpList.size()));
				else
					retVal.setSpMemUsage(0);
			}	
			
			tempOid = oid.getResPktMemUsed();
			if((tempOid!=null) && (!tempOid.isEmpty()))
			{
				tmpList = walk(tempOid);
				long tmpVal = 0;
				for(int i=0;i<tmpList.size();i++)
				{
					VariableBinding var=tmpList.get(i);
					if(var!=null)
						tmpVal += var.getVariable().toLong();
				}
				if(tmpList.size()>0)
					retVal.setSpMemUsed((tmpVal/tmpList.size()));
				else
					retVal.setSpMemUsed(0L);
			}
			
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s.", retVal));
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
//		OBSnmpPAS snmp = new OBSnmpPAS("192.168.100.11");
//		try
//		{
//			OBDatabase db = new OBDatabase();
//			db.openDB();
//			OBDtoAdcInfo adcInfo = new OBDtoAdcInfo();
//			adcInfo.setIndex(11);
//			adcInfo.setSwVersion("");
//			adcInfo.setAdcType(OBDefine.ADC_TYPE_ALTEON);
//			HashMap<Integer, Integer> list = snmp.getNodeStatus(adcInfo, db);
//			System.out.println(list);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	
//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDatabase db = new OBDatabase();
//			db.openDB();
//			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(1);
//			OBSnmpPASK snmp = new OBSnmpPASK("192.168.200.110", adcInfo.getSnmpInfo());
//			ArrayList<OBDtoMonTrafficVServer> list = snmp.getTrafficVServer(adcInfo, db);
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
			DtoOidInfoCfgPASK oid = oidDB.getCfgPASK(adcType, swVersion);
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
			DtoOidInfoCfgPASK oid = oidDB.getCfgPASK(adcType, swVersion);
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
    
//    private String makeOidString(String name)
//    {
//    	String retVal = "";
//    	if(name.isEmpty())
//    		return retVal;
//    	retVal += "."+name.length();
//    	for(int i=0;i<name.length();i++)
//    	{
//    		retVal += "."+name.charAt(i);
//    	}
//    	return retVal;
//    }
    
    
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
	
    private OBDtoMonTrafficVServer getTrafficVServerInfo(DtoOidInfoCfgPASK oid, String vsName)  throws OBException
    {
		OBDtoMonTrafficVServer retVal = new OBDtoMonTrafficVServer();
    	Snmp snmp = openSnmp();
    	String oidInfo = "";
    	try
    	{
	    	String nameString = makeOidName(vsName);
	    	
			// slbServiceActConn 추출. 
	    	String reqOid = oid.getSlbServiceActConn();
	    	oidInfo = reqOid+"."+nameString;
    		if(oidInfo!=null)
    		{
    			Gauge32 obj = (Gauge32)getSnmp(snmp, oidInfo);
    			if(obj!=null)
    			{
					if(checkObject(obj))
					{
						retVal.setCurConns(obj.toLong());
					}
    			}
    			else
    				{// 데이터 추출에 실패한 경우. 나머지의 경우도 실패처리한다.
    				retVal.setCurConns(0);
    				retVal.setPktsIn(0);
    				retVal.setPktsOut(0);
    				retVal.setBytesIn(0);
    				retVal.setBytesOut(0);
    				closeSnmp(snmp);
    				return retVal;
    			}
    		}
			
	    	// in pps
	    	reqOid = oid.getSlbServiceInpps();
	    	oidInfo = reqOid+"."+nameString;
    		if(oidInfo!=null)
    		{
    			Gauge32 obj = (Gauge32)getSnmp(snmp, oidInfo);
    			if(obj!=null)
    			{
					if(checkObject(obj))
					{
						retVal.setPktsIn(obj.toLong());
					}
    			}
    			else
    				{// 데이터 추출에 실패한 경우. 나머지의 경우도 실패처리한다.
    				retVal.setCurConns(0);
    				retVal.setPktsIn(0);
    				retVal.setPktsOut(0);
    				retVal.setBytesIn(0);
    				retVal.setBytesOut(0);
    				closeSnmp(snmp);
    				return retVal;
    			}
    		}
	    	
	    	// in bps
	    	reqOid = oid.getSlbServiceInbps();
	    	oidInfo = reqOid+"."+nameString;
    		if(oidInfo!=null)
    		{
    			Gauge32 obj = (Gauge32)getSnmp(snmp, oidInfo);
    			if(obj!=null)
    			{
					if(checkObject(obj))
					{
						retVal.setBytesIn(obj.toLong());
					}
    			}
    			else
    				{// 데이터 추출에 실패한 경우. 나머지의 경우도 실패처리한다.
    				retVal.setCurConns(0);
    				retVal.setPktsIn(0);
    				retVal.setPktsOut(0);
    				retVal.setBytesIn(0);
    				retVal.setBytesOut(0);
    				closeSnmp(snmp);
    				return retVal;
    			}    			
    		}
	    	
	    	// out pps
	    	reqOid = oid.getSlbServiceOutpps();
	    	oidInfo = reqOid+"."+nameString;
    		if(oidInfo!=null)
    		{
    			Gauge32 obj = (Gauge32)getSnmp(snmp, oidInfo);
    			if(obj!=null)
    			{
					if(checkObject(obj))
					{
						retVal.setPktsOut(obj.toLong());
					}
    			}
    			else
    				{// 데이터 추출에 실패한 경우. 나머지의 경우도 실패처리한다.
    				retVal.setCurConns(0);
    				retVal.setPktsIn(0);
    				retVal.setPktsOut(0);
    				retVal.setBytesIn(0);
    				retVal.setBytesOut(0);
    				closeSnmp(snmp);
    				return retVal;
    			}    			
    		}
	    	
	    	// out bps
	    	reqOid = oid.getSlbServiceOutbps();
	    	oidInfo = reqOid+"."+nameString;
    		if(oidInfo!=null)
    		{
    			Gauge32 obj = (Gauge32)getSnmp(snmp, oidInfo);
    			if(obj!=null)
    			{
					if(checkObject(obj))
					{
						retVal.setBytesOut(obj.toLong());
					}
    			}
    			else
    				{// 데이터 추출에 실패한 경우. 나머지의 경우도 실패처리한다.
    				retVal.setCurConns(0);
    				retVal.setPktsIn(0);
    				retVal.setPktsOut(0);
    				retVal.setBytesIn(0);
    				retVal.setBytesOut(0);
    				closeSnmp(snmp);
    				return retVal;
    			}      			
    		}
    		closeSnmp(snmp);
	    	return retVal;
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
    
    private ArrayList<OBDtoMonTrafficVServer> getTrafficVServer(DtoOidInfoCfgPASK oid)  throws OBException
    {
    	try
    	{
	    	HashMap<String, OBDtoMonTrafficVServer> vsMap = new HashMap<String, OBDtoMonTrafficVServer>();
	    	List<VariableBinding> tmpList;
	    	
			// slbServiceActConn 추출. 
	    	String reqOid = oid.getSlbServiceActConn();
	    	if(reqOid!=null && !reqOid.isEmpty())
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
	    	if(reqOid!=null && !reqOid.isEmpty())
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
	    	if(reqOid!=null && !reqOid.isEmpty())
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
							vsObj.setBytesIn(var.getVariable().toLong()*1024);// PASK의 경우에는 전달되는 단위가 kilo Bits per second
					}
					else
					{
						OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), reqOid));
					}
				}
	    	}
	    	
	    	// out pps
	    	reqOid = oid.getSlbServiceOutpps();
	    	if(reqOid!=null && !reqOid.isEmpty())
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
	    	if(reqOid!=null && !reqOid.isEmpty())
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
							vsObj.setBytesOut(var.getVariable().toLong()*1024);// PASK의 경우에는 전달되는 단위가 kilo Bits per second
					}
					else
					{
						OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("invalid data.(target:%s, vsID:%s)", var.getOid().toString(), reqOid));
					}
				}
	    	}
	    	ArrayList<OBDtoMonTrafficVServer> retVal = new ArrayList<OBDtoMonTrafficVServer>(vsMap.values());
//	    	OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("vsInfo:%s", retVal));
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
    
    private HashMap<String, OBDtoMonTrafficPoolMembersPASK> getTrafficPoolMembers(Integer adcIndex, DtoOidInfoCfgPASK oid) throws OBException
    {
    	HashMap<String, OBDtoMonTrafficPoolMembersPASK> retVal = new HashMap<String, OBDtoMonTrafficPoolMembersPASK>();

    	List<VariableBinding> tmpList;
    	
    	try
		{
    		// pps in
    		String oidReq = "";
    		oidReq = oid.getSlbRealInpps();
    		if(oidReq!=null && !oidReq.isEmpty())
    		{	
				tmpList = walk(oidReq);
				for(VariableBinding var:tmpList)
				{
					ArrayList<String> parseList = parseOidNameID(var.getOid().toString(), oidReq);
					if(parseList!=null)
					{
						OBDtoMonTrafficPoolMembersPASK obj = new OBDtoMonTrafficPoolMembersPASK();
						obj.setVsName(parseList.get(0));
						obj.setRealID(Integer.parseInt(parseList.get(1)));
						obj.setPktsIn(var.getVariable().toLong());
						
						String key = parseList.get(0) + "_" + parseList.get(1);
						retVal.put(key, obj);
					}
				}	
    		}
    		// pps out
    		oidReq = oid.getSlbRealOutpps();
    		if(oidReq!=null && !oidReq.isEmpty())
    		{	
				tmpList = walk(oidReq);
				for(VariableBinding var:tmpList)
				{
					ArrayList<String> parseList = parseOidNameID(var.getOid().toString(), oidReq);
					if(parseList!=null)
					{
						String key = parseList.get(0) + "_" + parseList.get(1);
						OBDtoMonTrafficPoolMembersPASK obj = retVal.get(key);
						if(obj!=null)
						{
							obj.setPktsOut(var.getVariable().toLong());
						}
					}
				}	
    		}

			// bps in
    		oidReq = oid.getSlbRealInbps();
    		if(oidReq!=null && !oidReq.isEmpty())
    		{	
				tmpList = walk(oidReq);
				for(VariableBinding var:tmpList)
				{
					ArrayList<String> parseList = parseOidNameID(var.getOid().toString(), oidReq);
					if(parseList!=null)
					{
						String key = parseList.get(0) + "_" + parseList.get(1);
						OBDtoMonTrafficPoolMembersPASK obj = retVal.get(key);
						if(obj!=null)
						{
							obj.setBytesIn(var.getVariable().toLong()*1024);// PASK의 경우에는 전달되는 단위가 kilo Bits per second
						}
					}
				}	
    		}
    		
    		// bps out
    		oidReq = oid.getSlbRealOutbps();
    		if(oidReq!=null && !oidReq.isEmpty())
    		{	
				tmpList = walk(oidReq);
				for(VariableBinding var:tmpList)
				{
					ArrayList<String> parseList = parseOidNameID(var.getOid().toString(), oidReq);
					if(parseList!=null)
					{
						String key = parseList.get(0) + "_" + parseList.get(1);
						OBDtoMonTrafficPoolMembersPASK obj = retVal.get(key);
						if(obj!=null)
						{
							obj.setBytesOut(var.getVariable().toLong()*1024);// PASK의 경우에는 전달되는 단위가 kilo Bits per second
						}
					}
				}	
    		}
    		
    		// cps
    		oidReq = oid.getSlbRealActConn();
    		if(oidReq!=null && !oidReq.isEmpty())
    		{	
				tmpList = walk(oidReq);
				for(VariableBinding var:tmpList)
				{
					ArrayList<String> parseList = parseOidNameID(var.getOid().toString(), oidReq);
					if(parseList!=null)
					{
						String key = parseList.get(0) + "_" + parseList.get(1);
						OBDtoMonTrafficPoolMembersPASK obj = retVal.get(key);
						if(obj!=null)
						{
						    if(var.getVariable().toString().equals("NULL"))
						    {
						        obj.setCurConns(0);
						    }
						    else
						    {
						        obj.setCurConns(var.getVariable().toLong());
						    }
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
            StackTraceElement[] ste = e.getStackTrace();
            String className = ste[0].getClassName();
            String meThodName = ste[0].getMethodName();
            int lineNumber = ste[0].getLineNumber();
            String fileName = ste[0].getFileName();
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, String.format("line:%s. msg:%s", className
                    + "." + meThodName + " " + fileName
                    + " " + lineNumber + "line",
                    e.getMessage()));
		}

//		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return retVal;
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
    /**
     * key: servicename+real_id
     * @param adcInfo
     * @param db
     * @return
     * @throws OBException
     */
    public HashMap<String, OBDtoMonTrafficPoolMembersPASK>  getTrafficPoolMembers(int adcIndex, int adcType, String swVersion)  throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();
		HashMap<String, OBDtoMonTrafficPoolMembersPASK> retVal = new HashMap<String, OBDtoMonTrafficPoolMembersPASK>();
		try
		{
			DtoOidInfoCfgPASK oidCfgPAS = oidDB.getCfgPASK(adcType, swVersion);
		
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

	public ArrayList<Integer>  getSystemFanStatus(int adcType, String swVersion)  throws OBException
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
	
	public ArrayList<Integer>  getSystemFanSpeed(int adcType, String swVersion)  throws OBException
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
	
	public ArrayList<Integer>  getSystemPowerSupplyStatus(int adcType, String swVersion)  throws OBException
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
	
	public OBDtoHddInfo  getSystemHddtatus(int adcType, String swVersion, OBDatabase db)  throws OBException
    {
		OBDtoHddInfo retVal = new OBDtoHddInfo();
		// alteon does not support this function.
		return retVal;
    }
	
	public OBDtoHddInfo  getSystemHddtatus(int adcType, String swVersion)  throws OBException
    {
		OBDtoHddInfo retVal = new OBDtoHddInfo();
		// alteon does not support this function.
		return retVal;
    }
}

