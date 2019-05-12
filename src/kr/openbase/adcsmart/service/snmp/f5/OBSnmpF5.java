package kr.openbase.adcsmart.service.snmp.f5;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.snmp4j.Snmp;
import org.snmp4j.smi.Counter64;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.VariableBinding;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSnmpInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;
import kr.openbase.adcsmart.service.dto.OBDtoConnectionData;
import kr.openbase.adcsmart.service.dto.OBDtoHddInfo;
import kr.openbase.adcsmart.service.dto.OBNameValue;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonL2Ports;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficPoolMembers;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficVServer;
import kr.openbase.adcsmart.service.snmp.OBSnmp;
import kr.openbase.adcsmart.service.snmp.OBSnmpOidDB;
import kr.openbase.adcsmart.service.snmp.alteon.dto.DtoRptOPL3;
import kr.openbase.adcsmart.service.snmp.alteon.dto.DtoRptOPSys;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidCfgPorts;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidFaultCheckHW;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidFaultCheckL23;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidInfoAdcPerformance;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidInfoAdcResc;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidInfoSystem;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidRptOPEtc;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidRptOPGen;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidRptOPL2;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidRptOPL3;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidRptOPL4;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidRptOPL7;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidRptOPSys;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidStatPoolMembers;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidStatVirtServer;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptConnStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptCpu;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptL3Gateway;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptL3Int;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptLinkStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptMem;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptOPEtc;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptOPGen;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptOPL2;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptOPL4;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptOPL7;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptOPL7iRule;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptPMStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptPortStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptStgStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptStpStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptTrunkStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptVSStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptVlanStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoSnmpAdcResc;
import kr.openbase.adcsmart.service.snmp.dto.DtoSnmpStatPoolMembers;
import kr.openbase.adcsmart.service.snmp.dto.OBDtoAdcPerformanceData;
import kr.openbase.adcsmart.service.snmp.dto.OBDtoOidRptInspection;
import kr.openbase.adcsmart.service.snmp.f5.dto.OBDtoRptInspectionSnmpF5;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBSnmpF5 extends OBSnmp {
	// public static final String AUDIT_ADC_ADD_SUCCESS = "ADC 장비 추가에 성공했습니다. 장비
	// 이름:%s";
	// public static void main(String[] args)
	// {
	// String aaa = String.format(OBSnmpF5.AUDIT_ADC_ADD_SUCCESS, "aaa");
	// System.out.print(aaa);
	// }
	// public static void main(String[] args)
	// {
	// OBDatabase db = new OBDatabase();
	// try
	// {
	// OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(2);
	// OBSnmpF5 snmp = new OBSnmpF5(OBSnmp.VERSION_2C, adcInfo.getAdcIpAddress());
	//
	// db.openDB();
	// DtoRptOPL7 info = snmp.getRptOPL7Info(adcInfo, db);
	// System.out.println(info);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	public HashMap<String, String> getVlanInterfaceName(int adcType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		try {
			DtoOidFaultCheckL23 oid = oidDB.getFaultCheckL23Info(adcType, swVersion);
			if (oid.getVlanInterfaceType() == null)
				return new HashMap<String, String>();

			HashMap<String, String> retVal = getVlanInterfaceName(oid.getVlanInterfaceType());
			return retVal;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}
	}

	private HashMap<String, String> getVlanInterfaceName(String oidInfo) throws OBException {
		HashMap<String, String> retVal = new HashMap<String, String>();
		if (oidInfo == null)
			return retVal;

		List<VariableBinding> tmpList;
		String reqOid = oidInfo;
		try {
			tmpList = walk(reqOid);
			for (VariableBinding var : tmpList) {
				// if(var.getVariable().toInt()!=0)// 0:interface, 1:trunk
				// continue;

				// oid 형식: vlanName+InterfaceName
				ArrayList<String> nameList = parseOidNameMulti(var.getOid().toString(), reqOid);
				if (nameList.size() != 2)
					continue;
				retVal.put(nameList.get(0), nameList.get(1)); // Value는 Interface 정보
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}

		return retVal;
	}

	public DtoRptOPL7 getRptOPL7Info(int adcType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		DtoOidRptOPL7 oid;
		try {
			oid = oidDB.getRptOPL7Info(adcType, swVersion);
			DtoRptOPL7 result = getRptOPL7Info(oid);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private DtoRptOPL7 getRptOPL7Info(DtoOidRptOPL7 oid) throws OBException {
		DtoRptOPL7 result = new DtoRptOPL7();

		Snmp snmp = openSnmp();

		List<VariableBinding> tmpList;
		String reqOid = "";
		try {
			// VS 이름별로 VS 주소를 추출하여 hashmap에 저장한다.
			HashMap<String, String> vsIPMap = new HashMap<String, String>();
			reqOid = oid.getVsIPAddr();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList) {
					String ipAddr = convertIPAddress((OctetString) var.getVariable());
					String vsName = parseOidName(var.getOid().toString(), reqOid);
					vsIPMap.put(vsName, ipAddr);
				}
			}

			// iRule 목록 추출. HashMap에 저장한다.
			HashMap<String, DtoRptOPL7iRule> iRuleNameMap = new HashMap<String, DtoRptOPL7iRule>();
			reqOid = oid.getiRuleName();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList) {
					DtoRptOPL7iRule rule = new DtoRptOPL7iRule();
					rule.setName(var.getVariable().toString());
					rule.setStatus(OBDefine.STATE_DISABLE);
					rule.setVsList(new ArrayList<OBDtoAdcVServerF5>());
					iRuleNameMap.put(rule.getName(), rule);
				}
			}
			// vs에 설정된 iRule 추출한다. 리턴값에는 iRule, 리턴OID에는 vs이름이 제공된다.
			reqOid = oid.getVsiRuleName();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList) {
					String iRuleName = var.getVariable().toString();
					String vsName = parseOidName(var.getOid().toString(), reqOid).replaceAll(iRuleName, "");
					//
					DtoRptOPL7iRule rule = iRuleNameMap.get(iRuleName);
					if (rule == null) {
						OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
								String.format("failed to get irule name. ip:%s, oid:%s", this.getHost(), reqOid));
						continue;
					}
					rule.setStatus(OBDefine.STATE_ENABLE);
					OBDtoAdcVServerF5 vsInfo = new OBDtoAdcVServerF5();
					vsInfo.setName(vsName);
					vsInfo.setvIP(vsIPMap.get(vsInfo.getName()));
					rule.getVsList().add(vsInfo);
				}
			}
			ArrayList<DtoRptOPL7iRule> iRuleList = new ArrayList<DtoRptOPL7iRule>(iRuleNameMap.values());
			result.setiRuleList(iRuleList);

			// 모든 profile 목록 추출.
			HashMap<String, ArrayList<String>> profileMap = new HashMap<String, ArrayList<String>>();
			reqOid = oid.getVsProfileName();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList) {
					String profileName = var.getVariable().toString();
					String vsName = parseOidName(var.getOid().toString(), reqOid).replaceAll(profileName, "");
					ArrayList<String> vsList = profileMap.get(profileName);
					if (vsList == null || vsList.size() == 0) {
						ArrayList<String> vsTempList = new ArrayList<String>();
						vsTempList.add(vsName);
						profileMap.put(profileName, vsTempList);
					} else
						vsList.add(vsName);
				}
			}

			// oneconnect 상태 추출.
			ArrayList<DtoRptOPL7iRule> oneConnect = new ArrayList<DtoRptOPL7iRule>();
			reqOid = oid.getOneConnect();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList) {
					String profileName = var.getVariable().toString();
					ArrayList<String> vsList = profileMap.get(profileName);
					DtoRptOPL7iRule one = new DtoRptOPL7iRule();
					one.setName(profileName);
					one.setStatus(OBDefine.STATE_DISABLE);
					one.setVsList(new ArrayList<OBDtoAdcVServerF5>());
					for (String vsName : vsList) {
						OBDtoAdcVServerF5 vsInfo = new OBDtoAdcVServerF5();
						vsInfo.setName(vsName);
						vsInfo.setvIP(vsIPMap.get(vsInfo.getName()));
						one.getVsList().add(vsInfo);
						one.setStatus(OBDefine.STATE_ENABLE);
					}
					oneConnect.add(one);
				}
			}
			result.setOneConnect(oneConnect);

			// ramcache 추출.
			ArrayList<DtoRptOPL7iRule> ramCacheList = new ArrayList<DtoRptOPL7iRule>();
			reqOid = oid.getRamcache();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList) {
					if (var.getVariable().toInt() != 1)
						continue;

					String profileName = parseOidName(var.getOid().toString(), reqOid + ".x.");// .x. 뒤부터 프로파일 이름이 제공됨.
					ArrayList<String> vsList = profileMap.get(profileName);
					DtoRptOPL7iRule tmp = new DtoRptOPL7iRule();
					tmp.setName(profileName);
					tmp.setStatus(OBDefine.STATE_DISABLE);
					tmp.setVsList(new ArrayList<OBDtoAdcVServerF5>());
					if (vsList != null) {
						for (String vsName : vsList) {
							OBDtoAdcVServerF5 vsInfo = new OBDtoAdcVServerF5();
							vsInfo.setName(vsName);
							vsInfo.setvIP(vsIPMap.get(vsInfo.getName()));
							tmp.getVsList().add(vsInfo);
							tmp.setStatus(OBDefine.STATE_ENABLE);
						}
					}
					ramCacheList.add(tmp);
				}
			}
			result.setRamCache(ramCacheList);

			// compression 상태 조회
			ArrayList<DtoRptOPL7iRule> compressionList = new ArrayList<DtoRptOPL7iRule>();
			reqOid = oid.getCompression();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList) {
					if (var.getVariable().toInt() != 1)
						continue;

					String profileName = parseOidName(var.getOid().toString(), reqOid + ".x.");// .x. 뒤부터 프로파일 이름이 제공됨.
					ArrayList<String> vsList = profileMap.get(profileName);
					DtoRptOPL7iRule tmp = new DtoRptOPL7iRule();
					tmp.setName(profileName);
					tmp.setStatus(OBDefine.STATE_DISABLE);
					tmp.setVsList(new ArrayList<OBDtoAdcVServerF5>());
					if (vsList != null) {
						for (String vsName : vsList) {
							OBDtoAdcVServerF5 vsInfo = new OBDtoAdcVServerF5();
							vsInfo.setName(vsName);
							vsInfo.setvIP(vsIPMap.get(vsInfo.getName()));
							tmp.getVsList().add(vsInfo);
							tmp.setStatus(OBDefine.STATE_ENABLE);
						}
					}
					compressionList.add(tmp);
				}
			}
			result.setCompression(compressionList);

			// ssl 가속 조회
			ArrayList<DtoRptOPL7iRule> sslList = new ArrayList<DtoRptOPL7iRule>();
			reqOid = oid.getSslAccel();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList) {
					if (var.getVariable().toInt() != 1)
						continue;

					String profileName = parseOidName(var.getOid().toString(), reqOid + ".x.");// .x. 뒤부터 프로파일 이름이 제공됨.
					ArrayList<String> vsList = profileMap.get(profileName);
					DtoRptOPL7iRule tmp = new DtoRptOPL7iRule();
					tmp.setName(profileName);
					tmp.setStatus(OBDefine.STATE_DISABLE);
					tmp.setVsList(new ArrayList<OBDtoAdcVServerF5>());
					if (vsList != null) {
						for (String vsName : vsList) {
							OBDtoAdcVServerF5 vsInfo = new OBDtoAdcVServerF5();
							vsInfo.setName(vsName);
							vsInfo.setvIP(vsIPMap.get(vsInfo.getName()));
							tmp.getVsList().add(vsInfo);
							tmp.setStatus(OBDefine.STATE_ENABLE);
						}
					}
					sslList.add(tmp);
				}
			}
			result.setSslAccel(sslList);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			closeSnmp(snmp);
		}

		return result;
	}

	// public static void main(String[] args)
	// {
	// OBDatabase db = new OBDatabase();
	// try
	// {
	// OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(2);
	// OBSnmpF5 snmp = new OBSnmpF5(OBSnmp.VERSION_2C, adcInfo.getAdcIpAddress());
	//
	// db.openDB();
	// DtoRptOPEtc info = snmp.getRptOPEtcInfo(adcInfo, db);
	// System.out.println(info);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	public DtoRptOPEtc getRptOPEtcInfo(int adcType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		DtoOidRptOPEtc oid;
		try {
			oid = oidDB.getRptOPEtcInfo(adcType, swVersion);
			DtoRptOPEtc result = getRptOPEtcInfo(oid);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}
	}

	private DtoRptOPEtc getRptOPEtcInfo(DtoOidRptOPEtc oid) throws OBException {
		DtoRptOPEtc result = new DtoRptOPEtc();

		// Snmp snmp = openSnmp();
		//
		// try
		// {
		// // syslog. 정보 없음.
		// // ntp 정보. 정보 없음.
		// }
		// catch(Exception e)
		// {
		// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
		// e.getMessage());
		// }
		// finally
		// {
		// closeSnmp(snmp);
		// }

		return result;
	}

	// public static void main(String[] args)
	// {
	// OBDatabase db = new OBDatabase();
	// try
	// {
	// OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(2);
	// OBSnmpF5 snmp = new OBSnmpF5(OBSnmp.VERSION_2C, adcInfo.getAdcIpAddress());
	//
	// db.openDB();
	// DtoRptOPL4 info = snmp.getRptOPL4Info(adcInfo, db);
	// System.out.println(info);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	public DtoRptOPL4 getRptOPL4Info(int adcType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		DtoOidRptOPL4 oid;
		try {
			oid = oidDB.getRptOPL4Info(adcType, swVersion);
			DtoRptOPL4 result = getRptOPL4Info(oid);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private DtoRptOPL4 getRptOPL4Info(DtoOidRptOPL4 oid) throws OBException {
		DtoRptOPL4 result = new DtoRptOPL4();

		Snmp snmp = openSnmp();

		// List<VariableBinding> tmpList;
		String reqOid = "";
		try {
			// poolmember status
			HashMap<String, DtoStatusTmp> pmStatusMap = getPoolMemberStatus(oid.getPoolMemberStatusAvail(),
					oid.getPoolMemberStatusEnabled());
			ArrayList<DtoStatusTmp> pmTmpStatusList = new ArrayList<DtoStatusTmp>(pmStatusMap.values());

			ArrayList<DtoRptPMStatus> pmStatusList = new ArrayList<DtoRptPMStatus>();
			for (DtoStatusTmp tmp : pmTmpStatusList) {
				DtoRptPMStatus pmStatus = new DtoRptPMStatus();
				pmStatus.setAddr(tmp.getNodeIP());
				pmStatus.setPoolName(tmp.getPoolName());
				pmStatus.setSrvPort(tmp.getSrvPort());
				pmStatus.setStatus(tmp.getStatus());
				pmStatusList.add(pmStatus);
			}
			result.setPmList(pmStatusList);

			// vs status 추출.
			HashMap<String, DtoStatusTmp> vsTmpStatusMap = getVSStatus(oid.getVsAddr(), oid.getVsStatusAvail(),
					oid.getVsStatusEnabled());
			ArrayList<DtoStatusTmp> vsTmpStatusList = new ArrayList<DtoStatusTmp>(vsTmpStatusMap.values());
			ArrayList<DtoRptVSStatus> vsStatusList = new ArrayList<DtoRptVSStatus>();
			for (DtoStatusTmp tmp : vsTmpStatusList) {
				DtoRptVSStatus vsStatus = new DtoRptVSStatus();
				vsStatus.setAddr(tmp.getNodeIP());
				vsStatus.setStatus(tmp.getStatus());
				vsStatusList.add(vsStatus);
			}
			result.setVsList(vsStatusList);

			// connection status
			DtoRptConnStatus connStatus = new DtoRptConnStatus();
			reqOid = oid.getConcurrentConn();
			if (reqOid != null) {
				Object obj = getSnmp(snmp, reqOid);
				if (checkObject(obj))
					connStatus.setCurConn(Long.parseLong(obj.toString()));
				else {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String
							.format("failed to get concurrent connections. ip:%s, oid:%s", this.getHost(), reqOid));
				}
			}
			reqOid = oid.getMaxConn();
			if (reqOid != null) {
				Object obj = getSnmp(snmp, reqOid);
				if (checkObject(obj))
					connStatus.setMaxConn(Long.parseLong(obj.toString()));
				else {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("failed to get max connections. ip:%s, oid:%s", this.getHost(), reqOid));
				}
			}
			result.setConnStatus(connStatus);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			closeSnmp(snmp);
		}

		return result;
	}

	// public static void main(String[] args)
	// {
	// OBDatabase db = new OBDatabase();
	// try
	// {
	// OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(2);
	// OBSnmpF5 snmp = new OBSnmpF5(OBSnmp.VERSION_2C, adcInfo.getAdcIpAddress());
	//
	// db.openDB();
	// DtoRptOPL3 info = snmp.getRptOPL3Info(adcInfo, db);
	// System.out.println(info);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	public DtoRptOPL3 getRptOPL3Info(int adcType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		DtoOidRptOPL3 oid;
		try {
			oid = oidDB.getRptOPL3Info(adcType, swVersion);
			DtoRptOPL3 result = getRptOPL3Info(oid);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private String parseIPAddress(String targetOid, String baseOid) {
		String seedOid = baseOid + ".1.4.";
		int tmpIndex = targetOid.indexOf(seedOid);
		String result = targetOid.substring(tmpIndex + seedOid.length());

		return result;
	}

	private DtoRptOPL3 getRptOPL3Info(DtoOidRptOPL3 oid) throws OBException {
		DtoRptOPL3 result = new DtoRptOPL3();

		Snmp snmp = openSnmp();

		List<VariableBinding> tmpList;
		String reqOid = "";
		try {
			// interface info--index
			HashMap<String, DtoRptL3Int> intMap = new HashMap<String, DtoRptL3Int>();
			// reqOid=oid.getIntIndex();
			// if(reqOid!=null)
			// {
			// tmpList = walk(reqOid);
			// for(VariableBinding var:tmpList)
			// {
			// DtoRptL3Int intStatus = new DtoRptL3Int();
			// intStatus.setIndex(var.getVariable().toInt());
			// intMap.put(intStatus.getIndex(), intStatus);
			// }
			// }
			// interface info--addr
			reqOid = oid.getIntAddr();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList) {
					DtoRptL3Int intStatus = new DtoRptL3Int();
					intStatus.setAddr(convertIPAddress((OctetString) var.getVariable()));
					intMap.put(intStatus.getAddr(), intStatus);
				}
			}
			// interface info--netmask
			reqOid = oid.getIntNetmask();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList) {
					if (var != null) {
						String name = parseIPAddress(var.getOid().toString(), reqOid);
						if (name == null) {
							OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
									String.format("failed to get interface ip. ip:%s, oid:%s", this.getHost(), reqOid));
							continue;
						}
						DtoRptL3Int intStatus = intMap.get(name);
						if (intStatus != null)
							intStatus.setNetmask(convertIPAddress((OctetString) var.getVariable()));
						else
							OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
									String.format("invalid ip:%s, oid:%s", this.getHost(), reqOid));
					}
				}
			}
			// interface info--bcastaddr
			reqOid = oid.getIntBcastAddr();
			// if(reqOid!=null)
			// {
			// tmpList = walk(reqOid);
			// for(VariableBinding var:tmpList)
			// {
			// String name = parseIPAddress(var.getOid().toString(), reqOid);
			// if(name==null)
			// {
			// OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get
			// interface ip. ip:%s, oid:%s", this.getHost(), reqOid));
			// continue;
			// }
			// DtoRptL3Int intStatus = intMap.get(name);
			// if(intStatus!=null)
			// intStatus.setBcastAddr(var.getVariable().toString());
			// else
			// OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("invalid ip:%s,
			// oid:%s", this.getHost(), reqOid));
			// }
			// }
			// interface info--vlan
			reqOid = oid.getIntVlan();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList) {
					String name = parseIPAddress(var.getOid().toString(), reqOid);
					if (name == null) {
						OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
								String.format("failed to get interface ip. ip:%s, oid:%s", this.getHost(), reqOid));
						continue;
					}
					DtoRptL3Int intStatus = intMap.get(name);
					if (intStatus != null)
						intStatus.setVlanIndexName(var.getVariable().toString());
					else
						OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
								String.format("invalid ip:%s, oid:%s", this.getHost(), reqOid));
				}
			}
			// // interface info--status
			// reqOid=oid.getIntStatus();
			// if(reqOid!=null)
			// {
			// tmpList = walk(reqOid);
			// for(VariableBinding var:tmpList)
			// {
			// if(var!=null)
			// {
			// ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
			// DtoRptL3Int intStatus = intMap.get(oidList.get(0));
			// intStatus.setStatus(convertLinkStatus(var.getVariable().toInt()));
			// }
			// }
			// }
			ArrayList<DtoRptL3Int> intStatusList = new ArrayList<DtoRptL3Int>(intMap.values());
			result.setIntList(intStatusList);

			// gateway info. 1:type이 gateway인 목록 추출. 추출된 목록의 이름을 조회.
			ArrayList<String> gwOidList = new ArrayList<String>();
			reqOid = oid.getGwType();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList) {
					if (var.getVariable().toInt() == 0)// gateway type
					{
						String subOid = var.getOid().toString().substring(reqOid.length());
						gwOidList.add(subOid);
					}
				}
			}
			// gw info--addr
			ArrayList<DtoRptL3Gateway> gwStatusList = new ArrayList<DtoRptL3Gateway>();
			reqOid = oid.getGwAddr();
			if (reqOid != null) {
				for (String gwOid : gwOidList) {
					String subOid = reqOid + "." + gwOid;
					if (subOid != null) {
						Object obj = getSnmp(snmp, subOid);
						if (checkObject(obj)) {
							DtoRptL3Gateway gwInfo = new DtoRptL3Gateway();
							gwInfo.setAddr(convertIPAddress((OctetString) obj));
							gwStatusList.add(gwInfo);
						} else {
							OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
									String.format("failed to get gw info. ip:%s, oid:%s", this.getHost(), reqOid));
						}
					}
				}
			}

			result.setGwList(gwStatusList);
		} catch (OBException e) {
			closeSnmp(snmp);
			throw e;
		} catch (Exception e) {
			closeSnmp(snmp);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		closeSnmp(snmp);
		return result;
	}

	// public static void main(String[] args)
	// {
	// OBDatabase db = new OBDatabase();
	// try
	// {
	// OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(2);
	// OBSnmpF5 snmp = new OBSnmpF5(OBSnmp.VERSION_2C, adcInfo.getAdcIpAddress());
	//
	// db.openDB();
	// DtoRptOPL2 info = snmp.getRptOPL2Info(adcInfo, db);
	// System.out.println(info);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	public DtoRptOPL2 getRptOPL2Info(int adcType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		DtoOidRptOPL2 oid;
		try {
			oid = oidDB.getRptOPL2Info(adcType, swVersion);
			DtoRptOPL2 result = getRptOPL2Info(oid);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private int convertLinkStatus(int status) {
		switch (status) {
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

	private int convertTrunkStatus(int status) {
		switch (status) {
		case 0:// up
			return OBDefine.L2_TRUNK_STATUS_UP;
		case 1:// down
			return OBDefine.L2_TRUNK_STATUS_DOWN;
		case 2:// disabled
			return OBDefine.L2_TRUNK_STATUS_DISABLED;
		case 3:// uninitialized
			return OBDefine.L2_TRUNK_STATUS_UNINIT;
		case 4:// loopback
			return OBDefine.L2_TRUNK_STATUS_LOOPBACK;
		case 5:// unpopulated
			return OBDefine.L2_TRUNK_STATUS_UNPOPULATED;
		default:
			return OBDefine.L2_TRUNK_STATE_DISABLED;
		}
	}

	private int convertStgStatus(int status) {
		switch (status) {// 0:Detach, 1:block, 2:listen, 3:learn, 4:forward, 5:disabled
		case 0:// f
			return OBDefine.L2_STG_STATUS_DETACH;
		case 1:// f
			return OBDefine.L2_STG_STATUS_BLOCKING;
		case 2:// f
			return OBDefine.L2_STG_STATUS_LISTENING;
		case 3:// f
			return OBDefine.L2_STG_STATUS_LEARNING;
		case 4:// f
			return OBDefine.L2_STG_STATUS_FORWARDING;
		case 5://
			return OBDefine.L2_STG_STATUS_DISABLED;
		default:
			return OBDefine.L2_STG_STATUS_DETACH;
		}
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(2);
	// OBSnmpF5 snmp = new OBSnmpF5(OBSnmp.VERSION_2C, adcInfo.getAdcIpAddress());
	//
	// System.out.print(snmp.convertOid("ext"));
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	private String convertOid(String name) {
		String output = "";
		byte[] bytes = name.getBytes();
		for (byte a : bytes) {
			if (!output.isEmpty())
				output += ".";
			output += a;
		}
		return output;
	}

	private DtoRptOPL2 getRptOPL2Info(DtoOidRptOPL2 oid) throws OBException {
		DtoRptOPL2 result = new DtoRptOPL2();

		Snmp snmp = openSnmp();

		List<VariableBinding> tmpList;
		String reqOid = "";
		try {
			// l2LinkUpInfo;
			ArrayList<DtoRptLinkStatus> linkupList = new ArrayList<DtoRptLinkStatus>();
			HashMap<String, DtoRptPortStatus> portStatusMap = new HashMap<String, DtoRptPortStatus>();
			reqOid = oid.getLinkUpInfo();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList) {
					if (var != null) {
						String portName = parseOidName(var.getOid().toString(), reqOid);
						DtoRptLinkStatus linkup = new DtoRptLinkStatus();
						// linkup.setName(oidList.get(0).toString());
						linkup.setName(portName);
						linkup.setStatus(convertLinkStatus(var.getVariable().toInt()));
						linkupList.add(linkup);

						DtoRptPortStatus portStatus = new DtoRptPortStatus();
						// portStatus.setName(oidList.get(0).toString());
						portStatus.setName(portName);
						portStatus.setStatus(convertLinkStatus(var.getVariable().toInt()));
						portStatusMap.put(portStatus.getName(), portStatus);
					}
				}
			}
			result.setLinkupList(linkupList);

			// port status:
			// port status-- statechanged time;
			reqOid = oid.getLinkStateChanged();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList) {
					if (var != null) {
						String portName = parseOidName(var.getOid().toString(), reqOid);

						DtoRptPortStatus portStatus = portStatusMap.get(portName);
						portStatus.setChangedTime(var.getVariable().toInt());
					}
				}
			}
			// port status-- discard;
			reqOid = oid.getLinkDiscardsIn();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList) {
					if (var != null) {
						// ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
						String portName = parseOidName(var.getOid().toString(), reqOid);
						DtoRptPortStatus portStatus = portStatusMap.get(portName);
						portStatus.setDiscardsIn(var.getVariable().toLong());
					}
				}
			}
			reqOid = oid.getLinkDiscardsOut();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList) {
					if (var != null) {
						// ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
						String portName = parseOidName(var.getOid().toString(), reqOid);
						DtoRptPortStatus portStatus = portStatusMap.get(portName);
						portStatus.setDiscardsOut(var.getVariable().toLong());
					}
				}
			}

			// port status-- error
			reqOid = oid.getLinkErrorIn();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList) {
					if (var != null) {
						// ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
						String portName = parseOidName(var.getOid().toString(), reqOid);
						DtoRptPortStatus portStatus = portStatusMap.get(portName);
						portStatus.setErrorIn(var.getVariable().toLong());
					}
				}
			}
			reqOid = oid.getLinkErrorOut();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList) {
					if (var != null) {
						// ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
						String portName = parseOidName(var.getOid().toString(), reqOid);
						DtoRptPortStatus portStatus = portStatusMap.get(portName);
						portStatus.setErrorOut(var.getVariable().toLong());
					}
				}
			}
			ArrayList<DtoRptPortStatus> portStatusList = new ArrayList<DtoRptPortStatus>(portStatusMap.values());
			result.setPortStatusList(portStatusList);

			// vlan name
			ArrayList<String> vlanNameList = new ArrayList<String>();
			reqOid = oid.getVlanName();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList)
					vlanNameList.add(var.getVariable().toString());
			}

			// vlan info--ports
			ArrayList<DtoRptVlanStatus> vlanStatusList = new ArrayList<DtoRptVlanStatus>();
			reqOid = oid.getVlanPorts();
			if (reqOid != null) {
				for (String vlanName : vlanNameList) {
					String reqOidSub = reqOid + ".3." + convertOid(vlanName);
					tmpList = walk(reqOidSub);
					DtoRptVlanStatus vlanStatus = new DtoRptVlanStatus();
					ArrayList<String> portNameList = new ArrayList<String>();
					for (VariableBinding var : tmpList) {
						portNameList.add(var.getVariable().toString());
					}
					vlanStatus.setName(vlanName);
					vlanStatus.setStatus(OBDefine.L2_VLAN_STATE_ENABLED);
					vlanStatus.setPortNameList(portNameList);
					vlanStatusList.add(vlanStatus);
				}
			}
			result.setVlanList(vlanStatusList);

			// stp info
			DtoRptStpStatus stpInfo = new DtoRptStpStatus();
			stpInfo.setState(OBDefine.L2_STP_STATE_DISABLED);
			// stp Info - 이름 추출 후 상태 추출함...
			reqOid = oid.getStpState();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				ArrayList<DtoRptStgStatus> stgList = new ArrayList<DtoRptStgStatus>();
				for (VariableBinding var : tmpList) {
					String name = parseOidName(var.getOid().toString(), reqOid);
					if (name == null) {
						OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
								String.format("failed to get stp name. ip:%s, oid:%s", this.getHost(), reqOid));
						continue;
					}
					DtoRptStgStatus stgStatus = new DtoRptStgStatus();

					stgStatus.setName(name);
					stgStatus.setStatus(convertStgStatus(var.getVariable().toInt()));
					stgList.add(stgStatus);
					stpInfo.setState(OBDefine.L2_STP_STATE_ENABLED);
				}
				stpInfo.setStgList(stgList);
			}
			result.setStpInfo(stpInfo);

			// trunk info
			ArrayList<DtoRptTrunkStatus> trunkList = new ArrayList<DtoRptTrunkStatus>();
			reqOid = oid.getTrunkStatus();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList) {
					String name = parseOidName(var.getOid().toString(), reqOid);
					if (name == null) {
						OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
								String.format("failed to get trunk name. ip:%s, oid:%s", this.getHost(), reqOid));
						continue;
					}
					DtoRptTrunkStatus trunkStatus = new DtoRptTrunkStatus();
					trunkStatus.setName(name);
					trunkStatus.setStatus(convertTrunkStatus(var.getVariable().toInt()));
					trunkList.add(trunkStatus);
				}
			}
			result.setTrunkList(trunkList);
		} catch (OBException e) {
			closeSnmp(snmp);
			throw e;
		} catch (Exception e) {
			closeSnmp(snmp);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		closeSnmp(snmp);
		return result;
	}

	// public static void main(String[] args)
	// {
	// OBDatabase db = new OBDatabase();
	// try
	// {
	// OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(2);
	// OBSnmpF5 snmp = new OBSnmpF5(OBSnmp.VERSION_2C, adcInfo.getAdcIpAddress());
	//
	// db.openDB();
	// DtoRptOPSys info = snmp.getRptOPSystemInfo(adcInfo, db);
	// System.out.println(info);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	public DtoRptOPSys getRptOPSystemInfo(int adcType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start."));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		DtoOidRptOPSys oid;
		try {
			oid = oidDB.getRptOPSystemInfo(adcType, swVersion);
			DtoRptOPSys result = getRptOPSystemInfo(oid);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private int convertFanState(int status) {
		switch (status) {
		case 0:// fail
			return OBDefine.SYS_FAN_STATUS_FAIL;
		case 1:// OK
			return OBDefine.SYS_FAN_STATUS_OK;
		case 2:// fail
			return OBDefine.SYS_FAN_STATUS_NOTPRESENT;
		default:
			return OBDefine.SYS_FAN_STATUS_FAIL;
		}
	}

	private int convertPowerSupplyState(int status) {
		switch (status) {
		case 0:// fail
			return OBDefine.SYS_POWERSUPPLY_STATUS_FAIL;
		case 1:// OK
			return OBDefine.SYS_POWERSUPPLY_STATUS_OK;
		case 2:// fail
			return OBDefine.SYS_POWERSUPPLY_STATUS_NOTPRESENT;
		default:
			return OBDefine.SYS_POWERSUPPLY_STATUS_FAIL;
		}
	}

	private DtoRptOPSys getRptOPSystemInfo(DtoOidRptOPSys oid) throws OBException {
		DtoRptOPSys result = new DtoRptOPSys();

		Snmp snmp = openSnmp();

		List<VariableBinding> tmpList;
		String reqOid = "";
		try {
			// sysUpTime;
			reqOid = oid.getUpTime();
			if (reqOid != null) {
				Object obj = getSnmp(snmp, reqOid);
				if (checkObject(obj))
					result.setUpTime(obj.toString());
				else {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("failed to get uptime. ip:%s, oid:%s", this.getHost(), reqOid));
				}
			}

			// sysLastApplyTime -- DB/Telnet을 이용해서 추출해야 함.

			// cpu info-MP
			ArrayList<DtoRptCpu> cpuList = new ArrayList<DtoRptCpu>();
			reqOid = oid.getCpuMPUtil64();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList) {
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if (oidList != null && (oidList.size()) > 0) {
						DtoRptCpu cpuInfo = new DtoRptCpu();
						cpuInfo.setType(OBDefine.SYS_CPU_TYPE_SP);
						cpuInfo.setUsage(var.getVariable().toInt());
						cpuList.add(cpuInfo);
					}
				}
			}
			result.setCpuList(cpuList);

			// memory info. tmm 메모리만 추출.
			ArrayList<DtoRptMem> memList = new ArrayList<DtoRptMem>();
			DtoRptMem memMPInfo = new DtoRptMem();
			memMPInfo.setType(OBDefine.SYS_MEM_TYPE_TMM);
			reqOid = oid.getMemTmmTotal();
			if ((reqOid != null) && (!reqOid.isEmpty())) {
				Object obj = getSnmp(snmp, reqOid);
				if (checkObject(obj)) {
					memMPInfo.setTotal(Long.parseLong(obj.toString()));
				} else {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("failed to get mp memory total. ip:%s, oid:%s", this.getHost(), reqOid));
				}
			}
			reqOid = oid.getMemTmmUsed();
			if ((reqOid != null) && (!reqOid.isEmpty())) {
				Object obj = getSnmp(snmp, reqOid);
				if (checkObject(obj)) {
					memMPInfo.setUsed(Integer.parseInt(obj.toString()));
				} else {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("failed to get mp memory used. ip:%s, oid:%s", this.getHost(), reqOid));
				}
			}
			memList.add(memMPInfo);
			result.setMemList(memList);

			// fan status
			reqOid = oid.getFanStatus();
			int fanStatus = OBDefine.SYS_FAN_STATUS_OK;
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList) {
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if (oidList != null && (oidList.size()) > 0) {
						if (convertFanState(var.getVariable().toInt()) == OBDefine.SYS_FAN_STATUS_FAIL)
							fanStatus = OBDefine.SYS_FAN_STATUS_FAIL;
					}
				}
			}
			result.setFanStatus(fanStatus);

			// power supply
			reqOid = oid.getFanStatus();
			int psStatus = OBDefine.SYS_POWERSUPPLY_STATUS_OK;
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList) {
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if (oidList != null && (oidList.size()) > 0) {
						if (convertPowerSupplyState(var.getVariable().toInt()) == OBDefine.SYS_POWERSUPPLY_STATUS_OK)
							fanStatus = OBDefine.SYS_FAN_STATUS_FAIL;
					}
				}
			}
			result.setPowerSupplyStatus(psStatus);
		} catch (OBException e) {
			closeSnmp(snmp);
			throw e;
		} catch (Exception e) {
			closeSnmp(snmp);
			throw new OBException(e.getMessage());
		}

		closeSnmp(snmp);
		return result;
	}

	// public static void main(String[] args)
	// {
	// OBDatabase db = new OBDatabase();
	// try
	// {
	// OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(2);
	// OBSnmpF5 snmp = new OBSnmpF5(OBSnmp.VERSION_2C, adcInfo.getAdcIpAddress());
	//
	// db.openDB();
	// DtoRptOPGen info = snmp.getRptOpGeneralInfo(adcInfo, db);
	// System.out.println(info);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	public DtoRptOPGen getRptOpGeneralInfo(int adcType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		DtoOidRptOPGen oid;
		try {
			oid = oidDB.getRptOPGeneralInfo(adcType, swVersion);
			DtoRptOPGen result = getRptOpGeneralInfo(oid);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}
	}

	/**
	 * 공통 된 부분을 클래스로 묶어서 코드를 재사용했다.
	 * 
	 * @author 최영조
	 */
	class InfoManager<T> {
		public T getRptInfo(int adcType, String swVersion) {

			return null;
		}
	}

	private DtoRptOPGen getRptOpGeneralInfo(DtoOidRptOPGen oid) throws OBException {
		DtoRptOPGen result = new DtoRptOPGen();

		Snmp snmp = openSnmp();

		List<VariableBinding> tmpList;
		String reqOid = "";
		try {
			// hostName
			reqOid = oid.getHostName();
			if (reqOid != null) {
				Object obj = getSnmp(snmp, reqOid);
				if (checkObject(obj))
					result.setHostName(obj.toString());
				else {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("failed to get host name. ip:%s, oid:%s", this.getHost(), reqOid));
				}
			}

			// model name--DB/Telnet을 이용해서 추출해야 함.
			// reqOid=oid.getModelName();
			// if(reqOid!=null)
			// {
			// Object obj = getSnmp(snmp, reqOid);
			// if(checkObject(obj))
			// result.setModelName(obj.toString());
			// }

			// os version
			reqOid = oid.getOsVersion();
			if (reqOid != null) {
				Object obj = getSnmp(snmp, reqOid);
				if (checkObject(obj))
					result.setOsVersion(obj.toString());
				else {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("failed to get os version. ip:%s, oid:%s", this.getHost(), reqOid));
				}
			}

			// license key
			reqOid = oid.getLicenseKey();
			HashMap<Integer, String> licenseMap = null;
			if (reqOid != null) {
				tmpList = walk(reqOid);
				licenseMap = new HashMap<Integer, String>();
				for (VariableBinding var : tmpList) {
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if (oidList != null && (oidList.size()) > 0) {
						licenseMap.put(oidList.get(0), var.toValueString());
					}
				}
			}
			// license type
			// String licenseInfo="";
			// reqOid=oid.getLicenseType();
			// if(reqOid!=null)
			// {
			// tmpList = walk(reqOid);
			// for(VariableBinding var:tmpList)
			// {
			// ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
			// if(oidList!=null && (oidList.size())>0)
			// {
			// String key="";
			// if(licenseMap!=null)
			// key = licenseMap.get(oidList.get(0));
			// int code = Integer.parseInt(var.toValueString());
			// String type = new
			// OBTerminologyDB().getName(OBTerminologyDB.TYPE_ALTEON_LICENSE_TYPE, code);
			// licenseInfo += String.format("%s:%s\n", key, type, db);
			// }
			// }
			// result.setLicenseInfo(licenseInfo);
			// }

			// serial num
			reqOid = oid.getSerialNum();
			if (reqOid != null) {
				Object obj = getSnmp(snmp, reqOid);
				if (checkObject(obj))
					result.setSerialNum(obj.toString());
				else {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("failed to get serical number. ip:%s, oid:%s", this.getHost(), reqOid));
				}
			}

			// ipAddr
			reqOid = oid.getIpAddress();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (VariableBinding var : tmpList) {
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if (oidList != null && (oidList.size()) > 0) {
						result.setIpAddress(convertIPAddress((OctetString) var.getVariable()));
						break;
					}
				}
			}

			// macAddr
			reqOid = oid.getMacAddress();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				String macAddr = "";
				for (VariableBinding var : tmpList) {
					ArrayList<Integer> oidList = parseOid(var.getOid().toString(), reqOid);
					if (oidList != null && (oidList.size()) > 0) {
						if (macAddr.indexOf(var.toValueString()) < 0)// 동일한 MAC 주소는 제거.
							macAddr += String.format("%s\n", var.toValueString());
					}
				}
				result.setMacAddress(macAddr);
			}

			// upTime
			reqOid = oid.getUpTime();
			if (reqOid != null) {
				Object obj = getSnmp(snmp, reqOid);
				if (checkObject(obj))
					result.setUpTime(obj.toString());
				else {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("failed to get up time. ip:%s, oid:%s", this.getHost(), reqOid));
				}
			}
			// activeStandby
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			closeSnmp(snmp);
		}

		return result;
	}

	// public static void main(String[] args)
	// {
	// // OBSnmpF5 snmp = new OBSnmpF5("192.168.200.11");
	// OBSnmpF5 snmp = new OBSnmpF5("112.106.11.35");
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// OBDtoAdcPerformanceData list = snmp.getAdcPerformances(OBDefine.ADC_TYPE_F5,
	// "", "public", db);
	// System.out.println(list);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	public OBDtoAdcPerformanceData getAdcPerformances(int adcType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));

		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		DtoOidInfoAdcPerformance oid;
		try {
			oid = oidDB.getAdcPerformance(adcType, swVersion);

			OBDtoAdcPerformanceData result = getAdcPerformances(oid);

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}

	}

	private OBDtoAdcPerformanceData getAdcPerformances(DtoOidInfoAdcPerformance oid) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. oid:%s", oid));
		OBDtoAdcPerformanceData result = new OBDtoAdcPerformanceData();

		Snmp snmp = openSnmp();

		String reqOid = "";
		try {
			Object obj = null;
			// interface port에서 추출된 값을 사용한다.
			// reqOid=oid.getClientBytesIn();
			// if(reqOid!=null)
			// {
			// obj = getSnmp(snmp, reqOid);
			// if(checkObject(obj))
			// {
			// result.setClientBytesIn(Long.parseLong(obj.toString()));
			// }
			// else
			// {
			// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
			// String.format("failed to get snmp data. oid:%s", reqOid));
			// }
			// }
			//
			// reqOid=oid.getClientBytesOut();
			// if(reqOid!=null)
			// {
			// obj = getSnmp(snmp, reqOid);
			// if(checkObject(obj))
			// result.setClientBytesOut(Long.parseLong(obj.toString()));
			// else
			// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
			// String.format("failed to get snmp data. oid:%s", reqOid));
			// }

			reqOid = oid.getClientCurConns();
			if (reqOid != null) {
				obj = getSnmp(snmp, reqOid);
				if (checkObject(obj))
					result.setClientCurConns(Long.parseLong(obj.toString()));
				else
					OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data. oid:%s", reqOid));
				// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
				// String.format("failed to get snmp data. oid:%s", reqOid));
			}

			// interface port에서 추출된 값을 사용한다.
			// reqOid=oid.getClientPktsIn();
			// if(reqOid!=null)
			// {
			// obj = getSnmp(snmp, reqOid);
			// if(checkObject(obj))
			// result.setClientPktsIn(Long.parseLong(obj.toString()));
			// else
			// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
			// String.format("failed to get snmp data. oid:%s", reqOid));
			// }
			//
			// reqOid=oid.getClientPktsOut();
			// if(reqOid!=null)
			// {
			// obj = getSnmp(snmp, reqOid);
			// if(checkObject(obj))
			// result.setClientPktsOut(Long.parseLong(obj.toString()));
			// else
			// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
			// String.format("failed to get snmp data. oid:%s", reqOid));
			// }

			reqOid = oid.getClientSslTotCompactConns();
			if (reqOid != null) {
				obj = getSnmp(snmp, reqOid);
				if (checkObject(obj))
					result.setClientSslTotCompactConns(Long.parseLong(obj.toString()));
				else
					OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data. oid:%s", reqOid));
			}

			reqOid = oid.getClientSslTotNativeConns();
			if (reqOid != null) {
				obj = getSnmp(snmp, reqOid);
				if (checkObject(obj))
					result.setClientSslTotNativeConns(Long.parseLong(obj.toString()));
				else
					OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data. oid:%s", reqOid));
			}

			reqOid = oid.getHttpStatGetReqs();
			if (reqOid != null) {
				obj = getSnmp(snmp, reqOid);
				if (checkObject(obj))
					result.setHttpStatGetReqs(Long.parseLong(obj.toString()));
				else
					OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to get snmp data. oid:%s", reqOid));
				// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
				// String.format("failed to get snmp data. oid:%s", reqOid));
			}
		} catch (OBException e) {
			closeSnmp(snmp);
			throw e;
		} catch (Exception e) {
			closeSnmp(snmp);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		closeSnmp(snmp);

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	// public static void main(String[] args)
	// {
	// OBDatabase db = new OBDatabase();
	// try
	// {
	// db.openDB();
	// OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(15);
	// // OBSnmpF5 snmp = new OBSnmpF5("172.172.1.154", adcInfo.getSnmpInfo());
	// OBSnmpF5 snmp = new OBSnmpF5("192.168.200.11", adcInfo.getSnmpInfo());
	// DtoSnmpAdcResc aa = new DtoSnmpAdcResc();
	// aa = snmp.getAdcResc(adcType, swVersion, db);
	// System.out.println(aa);
	// // ArrayList<OBDtoMonL2Ports> list = snmp.getPortsInfo(adcType, swVersion,
	// adcInfo.getSwVersion());
	// //
	// // for(OBDtoMonL2Ports info:list)
	// // System.out.println(info);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	public ArrayList<OBDtoMonL2Ports> getPortsInfo(int adcType, String swVersion) throws OBException {
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		DtoOidCfgPorts oid;

		try {
			oid = oidDB.getCfgPortInfo(adcType, swVersion);
			ArrayList<OBDtoMonL2Ports> retVal = getPortsInfo(oid);
			return retVal;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private ArrayList<OBDtoMonL2Ports> getPortsInfo(DtoOidCfgPorts oid) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. oid:%s", oid));
		// ArrayList<OBDtoMonL2Ports> result=new ArrayList<OBDtoMonL2Ports>();
		HashMap<String, OBDtoMonL2Ports> portInfoMap = new HashMap<String, OBDtoMonL2Ports>();

		List<VariableBinding> tmpList;

		try {
			// get a interface name
			String reqOid = "";
			reqOid = oid.getPortName();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				int portIndex = 1;
				for (VariableBinding var : tmpList) {
					String name = parseOidName(var.getOid().toString(), reqOid);
					if ((name != null) && (!name.isEmpty())) {
						OBDtoMonL2Ports info = new OBDtoMonL2Ports();
						info.setPortName(name);
						info.setPortIndex(portIndex++);
						portInfoMap.put(name, info);
					}
				}
			}

			// get a port duplex
			reqOid = oid.getPortName();
			if (reqOid != null) {
				tmpList = walk(oid.getPortDuplex());
				for (int i = 0; i < tmpList.size(); i++) {
					VariableBinding var = tmpList.get(i);
					String name = parseOidName(var.getOid().toString(), oid.getPortDuplex());
					if ((name != null) && (!name.isEmpty())) {
						OBDtoMonL2Ports info = portInfoMap.get(name);
						if (null != info) {
							info.setDuplex(convertPortDuplex(var.getVariable().toInt()));
						}
					}
				}
			}

			// discards pkts.
			reqOid = oid.getPortPktsDiscardsIn();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (int i = 0; i < tmpList.size(); i++) {
					VariableBinding var = tmpList.get(i);
					String name = parseOidName(var.getOid().toString(), reqOid);
					if ((name != null) && (!name.isEmpty())) {
						OBDtoMonL2Ports info = portInfoMap.get(name);
						if (null != info) {
							info.setDropsIn(var.getVariable().toLong());
						}
					}
				}
			}

			// discards pkts
			reqOid = oid.getPortPktsDiscardsOut();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (int i = 0; i < tmpList.size(); i++) {
					VariableBinding var = tmpList.get(i);
					String name = parseOidName(var.getOid().toString(), reqOid);
					if ((name != null) && (!name.isEmpty())) {
						OBDtoMonL2Ports info = portInfoMap.get(name);
						if (null != info) {
							info.setDropsOut(var.getVariable().toLong());
						}
					}
				}
			}

			// errors pkts
			reqOid = oid.getPortPktsErrorsIn();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (int i = 0; i < tmpList.size(); i++) {
					VariableBinding var = tmpList.get(i);
					String name = parseOidName(var.getOid().toString(), reqOid);
					if ((name != null) && (!name.isEmpty())) {
						OBDtoMonL2Ports info = portInfoMap.get(name);
						if (null != info) {
							info.setErrorsIn(var.getVariable().toLong());
						}
					}
				}
			}

			// errors
			reqOid = oid.getPortPktsErrorsOut();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (int i = 0; i < tmpList.size(); i++) {
					VariableBinding var = tmpList.get(i);
					String name = parseOidName(var.getOid().toString(), reqOid);
					if ((name != null) && (!name.isEmpty())) {
						OBDtoMonL2Ports info = portInfoMap.get(name);
						if (null != info) {
							info.setErrorsOut(var.getVariable().toLong());
						}
					}
				}
			}

			// bytes in
			reqOid = oid.getPortBytesIn();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (int i = 0; i < tmpList.size(); i++) {
					VariableBinding var = tmpList.get(i);
					String name = parseOidName(var.getOid().toString(), reqOid);
					if ((name != null) && (!name.isEmpty())) {
						OBDtoMonL2Ports info = portInfoMap.get(name);
						if (null != info) {
							info.setBytesIn(var.getVariable().toLong());
						}
					}
				}
			}

			// bytes out
			reqOid = oid.getPortBytesOut();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (int i = 0; i < tmpList.size(); i++) {
					VariableBinding var = tmpList.get(i);
					String name = parseOidName(var.getOid().toString(), reqOid);
					if ((name != null) && (!name.isEmpty())) {
						OBDtoMonL2Ports info = portInfoMap.get(name);
						if (null != info) {
							info.setBytesOut(var.getVariable().toLong());
						}
					}
				}
			}

			// in
			reqOid = oid.getPortPktsIn();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (int i = 0; i < tmpList.size(); i++) {
					VariableBinding var = tmpList.get(i);
					String name = parseOidName(var.getOid().toString(), reqOid);
					if ((name != null) && (!name.isEmpty())) {
						OBDtoMonL2Ports info = portInfoMap.get(name);
						if (null != info) {
							info.setPktsIn(var.getVariable().toLong());
						}
					}
				}
			}

			// out
			reqOid = oid.getPortPktsOut();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (int i = 0; i < tmpList.size(); i++) {
					VariableBinding var = tmpList.get(i);
					String name = parseOidName(var.getOid().toString(), reqOid);
					if ((name != null) && (!name.isEmpty())) {
						OBDtoMonL2Ports info = portInfoMap.get(name);
						if (null != info) {
							info.setPktsOut(var.getVariable().toLong());
						}
					}
				}
			}

			// multi in
			reqOid = oid.getPortPktsMultiIn();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (int i = 0; i < tmpList.size(); i++) {
					VariableBinding var = tmpList.get(i);
					String name = parseOidName(var.getOid().toString(), reqOid);
					if ((name != null) && (!name.isEmpty())) {
						OBDtoMonL2Ports info = portInfoMap.get(name);

						if (null != info) {
							info.setPktsMultiIn(var.getVariable().toLong());
						}
					}
				}
			}
			// multi out
			reqOid = oid.getPortPktsMultiOut();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (int i = 0; i < tmpList.size(); i++) {
					VariableBinding var = tmpList.get(i);
					String name = parseOidName(var.getOid().toString(), reqOid);
					if ((name != null) && (!name.isEmpty())) {
						OBDtoMonL2Ports info = portInfoMap.get(name);
						if (null != info) {
							info.setPktsMultiOut(var.getVariable().toLong());
						}
					}
				}
			}

			// broad in
			reqOid = oid.getPortPktsBroadIn();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (int i = 0; i < tmpList.size(); i++) {
					VariableBinding var = tmpList.get(i);
					String name = parseOidName(var.getOid().toString(), reqOid);
					if ((name != null) && (!name.isEmpty())) {
						OBDtoMonL2Ports info = portInfoMap.get(name);
						if (null != info) {
							info.setPktsBroadIn(var.getVariable().toLong());
						}
					}
				}
			}

			// broad out
			reqOid = oid.getPortPktsBroadIn();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (int i = 0; i < tmpList.size(); i++) {
					VariableBinding var = tmpList.get(i);
					String name = parseOidName(var.getOid().toString(), reqOid);
					if ((name != null) && (!name.isEmpty())) {
						OBDtoMonL2Ports info = portInfoMap.get(name);
						if (null != info) {
							info.setPktsBroadOut(var.getVariable().toLong());
						}
					}
				}
			}

			// unknown protocol
			// reqOid = oid.getPortPktsUnknownProtos();
			// if(reqOid != null)
			// {
			// tmpList = walk(reqOid);
			// for(int i=0;i<tmpList.size();i++)
			// {
			// VariableBinding var=tmpList.get(i);
			// String name = parseOidName(var.getOid().toString(), reqOid);
			// if((name!=null) && (!name.isEmpty()))
			// {
			// OBDtoMonL2Ports info = portInfoMap.get(name);
			// if(null != info)
			// {
			// info.setPktsUnknownProtos(var.getVariable().toLong());
			// }
			// }
			// }
			// }
			//
			// speed
			reqOid = oid.getPortSpeed();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (int i = 0; i < tmpList.size(); i++) {
					VariableBinding var = tmpList.get(i);
					String name = parseOidName(var.getOid().toString(), reqOid);
					if ((name != null) && (!name.isEmpty())) {
						OBDtoMonL2Ports info = portInfoMap.get(name);
						if (null != info) {
							info.setSpeed(var.getVariable().toInt());
						}
					}
				}
			}

			// state
			reqOid = oid.getPortStatus();
			if (reqOid != null) {
				tmpList = walk(reqOid);
				for (int i = 0; i < tmpList.size(); i++) {
					VariableBinding var = tmpList.get(i);
					String name = parseOidName(var.getOid().toString(), reqOid);
					if ((name != null) && (!name.isEmpty())) {
						OBDtoMonL2Ports info = portInfoMap.get(name);
						if (null != info) {
							info.setStatus(convertPortStatus(var.getVariable().toInt()));
						}
					}
				}
			}
			// connector type. 관련 정보가 없는 관계로 수집하지 않는다.
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}
		// Port Number 순서대로 Index 할당
		HashMap<Integer, String> portTmp = new HashMap<Integer, String>();
		ArrayList<OBDtoMonL2Ports> portArrayList = new ArrayList<OBDtoMonL2Ports>(portInfoMap.values());
		ArrayList<Integer> sortTmp = new ArrayList<Integer>();
		for (OBDtoMonL2Ports info : portArrayList) {
			sortTmp.add(transPortValue(info.getPortName()));
			portTmp.put(transPortValue(info.getPortName()), info.getPortName());
		}

		Collections.sort(sortTmp);
		for (int i = 0; i < sortTmp.size(); i++) {
			Integer key = (Integer) sortTmp.get(i);
			portInfoMap.get(portTmp.get(key)).setPortIndex(i + 1);
		}

		// hash map으로 구성된 데이터를 port index 순으로 정렬하여 리턴함.
		OBDtoMonL2Ports[] portInfoArray = new OBDtoMonL2Ports[portInfoMap.size()];
		ArrayList<OBDtoMonL2Ports> poretInfoArrayList = new ArrayList<OBDtoMonL2Ports>(portInfoMap.values());
		for (OBDtoMonL2Ports info : poretInfoArrayList) {
			Integer index = info.getPortIndex() - 1;
			if (index >= portInfoMap.size() || index < 0) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("invalid index: %d, max:%d", index, portInfoMap.size()));
				continue;
			}
			portInfoArray[index] = info;
		}

		ArrayList<OBDtoMonL2Ports> retVal = new ArrayList<OBDtoMonL2Ports>();
		Collections.addAll(retVal, portInfoArray);
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", retVal));
		return retVal;
	}

	// Port를 숫자로 변환 /제거 "."으로 2개로 나눈후 앞부분에 100을 곱하고 뒤에있는 숫자를 더함
	private int transPortValue(String portNumber) {
		portNumber = portNumber.replace("/", "");
		String[] comparePort1 = portNumber.split("\\.");
		if (!portNumber.contains("mgmt")) {
			int transNum = Integer.parseInt(comparePort1[0]) * 100 + Integer.parseInt(comparePort1[1]);

			return transNum;
		} else if (comparePort1[0].length() == 5) {
			int num = Integer.parseInt(comparePort1[0].replace("mgmt", ""));
			return 9000 + num; // F5 2400 MGMT
		} else
			return 9999; // MGMT는 9999번으로 할당
	}

	private Integer convertPortStatus(Integer state) {
		switch (state) {
		case 0:
			return OBDtoMonL2Ports.STATUS_UP;
		case 1:
			return OBDtoMonL2Ports.STATUS_DOWN;
		case 2:
			return OBDtoMonL2Ports.STATUS_DISABLED;
		default:
			return OBDtoMonL2Ports.STATUS_INOPERATIVE;
		}
	}

	private Integer convertPortDuplex(Integer duplex) {
		switch (duplex) {
		case 0:
			return OBDefine.DUPLEX_NONE;
		case 1:
			return OBDefine.DUPLEX_HALF;
		case 2:
			return OBDefine.DUPLEX_FULL;
		default:
			return OBDefine.DUPLEX_NONE;
		}
	}

	// public static void main(String[] args)
	// {
	// OBSnmpF5 snmp = new OBSnmpF5("192.168.200.10");
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// DtoSnmpAdcResc list = snmp.getAdcResc(OBDefine.ADC_TYPE_F5, "",
	// OBDefine.DEFAULT_SNMP_RCOMM, db);
	// System.out.println(list);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	public DtoSnmpAdcResc getAdcResc(int adcType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		DtoOidInfoAdcResc oidInfo;

		try {
			oidInfo = oidDB.getAdcResource(adcType, swVersion);
			DtoSnmpAdcResc info = getAdcResc(oidInfo);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", info));
			return info;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}
	}

	private DtoSnmpAdcResc getAdcResc(DtoOidInfoAdcResc oid) throws OBException {
		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. %s.",
		// oid.toString()));

		DtoSnmpAdcResc info = new DtoSnmpAdcResc();
		String tempOid;
		Snmp snmp = openSnmp();

		List<VariableBinding> tmpList;

		try {
			// idle time임.
			tmpList = walk(oid.getAdcCpuIdle());
			ArrayList<Long> idleTime = new ArrayList<Long>();
			for (int i = 0; i < tmpList.size(); i++) {
				VariableBinding var = tmpList.get(i);
				idleTime.add(var.getVariable().toLong());
			}
			info.setCpuIdleTime(idleTime);

			// tempOid = oid.getAdcMemTotal();
			// if((tempOid!=null) && (!tempOid.isEmpty()))
			// {
			// Object obj = getSnmp(snmp, tempOid);
			// if(checkObject(obj))
			// info.setMemTotal(Long.parseLong(obj.toString()));
			// }
			//
			// tempOid = oid.getAdcMemUsed();
			// long adcMemUsed=0;
			// if((tempOid!=null) && (!tempOid.isEmpty()))
			// {
			// Object obj = getSnmp(snmp, tempOid);
			// if(checkObject(obj))
			// adcMemUsed=Long.parseLong(obj.toString());
			// }
			// tmm total. F5의 메모리는 TMM 메모리를 기준으로 추출한다.
			tempOid = oid.getTmmMemTotal();
			// long tmmMemTotal=0;
			if ((tempOid != null) && (!tempOid.isEmpty())) {
				Object obj = getSnmp(snmp, tempOid);
				if (checkObject(obj))
					info.setMemTotal(Long.parseLong(obj.toString()));
				// tmmMemTotal=Long.parseLong(obj.toString());
			}

			// tmm used
			tempOid = oid.getTmmMemUsed();
			// long tmmMemUsed=0;
			if ((tempOid != null) && (!tempOid.isEmpty())) {
				Object obj = getSnmp(snmp, tempOid);
				if (checkObject(obj))
					info.setMemUsed(Long.parseLong(obj.toString()));
				// tmmMemUsed=Long.parseLong(obj.toString());
			}
			// long memUsed = tmmMemUsed + (adcMemUsed-tmmMemTotal);
			// info.setMemUsed(memUsed)
			// current connections
			tmpList = walk(oid.getAdcCurConns());
			long currConns = 0;
			for (int i = 0; i < tmpList.size(); i++) {
				VariableBinding var = tmpList.get(i);
				currConns += var.getVariable().toLong();
			}
			info.setVsCurConns(currConns);

			tmpList = walk(oid.getAdcBytesIn());
			long bytesIn = 0;
			for (int i = 0; i < tmpList.size(); i++) {
				VariableBinding var = tmpList.get(i);
				currConns += var.getVariable().toLong();
			}
			info.setVsBytesIn(bytesIn);

			tmpList = walk(oid.getAdcBytesOut());
			long bytesOut = 0;
			for (int i = 0; i < tmpList.size(); i++) {
				VariableBinding var = tmpList.get(i);
				currConns += var.getVariable().toLong();
			}
			info.setVsBytesOut(bytesOut);

			tmpList = walk(oid.getAdcPktsIn());
			long pktsIn = 0;
			for (int i = 0; i < tmpList.size(); i++) {
				VariableBinding var = tmpList.get(i);
				currConns += var.getVariable().toLong();
			}
			info.setVsPktsIn(pktsIn);

			tmpList = walk(oid.getAdcPktsOut());
			long pktsOut = 0;
			for (int i = 0; i < tmpList.size(); i++) {
				VariableBinding var = tmpList.get(i);
				currConns += var.getVariable().toLong();
			}
			info.setVsPktsOut(pktsOut);

			tempOid = oid.getAdcCurConns();
			if ((tempOid != null) && (!tempOid.isEmpty())) {
				Object obj = getSnmp(snmp, tempOid);
				if (checkObject(obj))
					info.setVsCurConns(Long.parseLong(obj.toString()));
			}

			tempOid = oid.getAdcTypeModel(); // 1.3.6.1.4.1.3375.2.1.3.3.1.0
			if ((tempOid != null) && (!tempOid.isEmpty())) {
				Object obj = getSnmp(snmp, tempOid); // D63a
				if (checkObject(obj)) { // 2014.4.7 F5 9.x 버전은 model oid가 있지만 "unknown"을 반환한다. "unknown"은 못 가져온 것으로
										// 처리한다. 9.x는 CLI로 모델을 파악하게 바꿔야 한다.
										// if(!typePlatform(obj.toString()).equals(null)) // 이미 반환되는 값이 null 인 경우가 있음.
					if (typePlatform(obj.toString()) != null) {
						info.setModel(typePlatform(obj.toString()));
					} else {
						tempOid = oid.getAdcModel();
						if ((tempOid != null) && (!tempOid.isEmpty())) {
							obj = getSnmp(snmp, tempOid);
							if (checkObject(obj)) {
								if (obj.toString().equals("unknown") == false) {
									info.setModel(obj.toString());
								} else {
									info.setModel(null);
								}
							}
						}
					}
				}
			}

			tempOid = oid.getAdcSwVersion();
			if ((tempOid != null) && (!tempOid.isEmpty())) {
				Object obj = getSnmp(snmp, tempOid);
				if (checkObject(obj))
					info.setSwVersion(obj.toString());
			}

			tempOid = oid.getAdcName();
			if ((tempOid != null) && (!tempOid.isEmpty())) {
				Object obj = getSnmp(snmp, tempOid);
				if (checkObject(obj))
					info.setName(obj.toString());
			}

			tempOid = oid.getAdcDescr();
			if ((tempOid != null) && (!tempOid.isEmpty())) {
				Object obj = getSnmp(snmp, tempOid);
				if (checkObject(obj))
					info.setDescr(obj.toString());
			}

			tempOid = oid.getAdcUpTime();
			if ((tempOid != null) && (!tempOid.isEmpty())) {
				Object obj = getSnmp(snmp, tempOid);
				if (checkObject(obj)) {
					org.snmp4j.smi.TimeTicks elapseTime = (TimeTicks) obj;
					Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
					Timestamp upTime = new Timestamp(now.getTime() - (elapseTime.toLong() * 10));
					info.setUpTime(upTime);
				}
			}

			ArrayList<Integer> vsAvailableState = new ArrayList<Integer>();
			ArrayList<Integer> vsEnabledState = new ArrayList<Integer>();

			tmpList = walk(oid.getVsAvaliableState());
			for (int i = 0; i < tmpList.size(); i++) {
				VariableBinding var = tmpList.get(i);
				vsAvailableState.add(var.getVariable().toInt());
			}
			tmpList = walk(oid.getVssEnabledState());
			for (int i = 0; i < tmpList.size(); i++) {
				VariableBinding var = tmpList.get(i);
				vsEnabledState.add(var.getVariable().toInt());
			}

			if (vsEnabledState.size() != vsAvailableState.size())
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get virtual server's status. host:%s", super.getHost()));
			int total = vsAvailableState.size();
			int avaliable = 0;
			int unavailable = 0;
			int disabled = 0;
			for (int i = 0; i < vsAvailableState.size(); i++) {
				Integer status = caluVsStatus(vsAvailableState.get(i), vsEnabledState.get(i));
				if (status == OBDefine.STATE_DISABLE)
					disabled++;
				else if (status == OBDefine.STATUS_AVAILABLE)
					avaliable++;
				else if (status == OBDefine.STATUS_UNAVAILABLE)
					unavailable++;
			}
			info.setVsCount(total);
			info.setVsCountAvailable(avaliable);
			info.setVsCountDisabled(disabled);
			info.setVsCountBlocked(0);
			info.setVsCountUnavailable(unavailable);
		} catch (OBException e) {
			closeSnmp(snmp);
			throw e;
		} catch (Exception e) {
			closeSnmp(snmp);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		closeSnmp(snmp);
		return info;
	}

	private Integer caluVsStatus(Integer availableState, Integer enabledState) {
		if (enabledState != 1) {// none or disabledbyparent
			return OBDefine.STATE_DISABLE;
		}

		switch (availableState) {
		case 1:// available
			return OBDefine.STATUS_AVAILABLE;
		case 2:// unavailable
			return OBDefine.STATUS_UNAVAILABLE;
		case 3: // offline
			return OBDefine.STATUS_UNAVAILABLE;
		case 4:// unknown
			return OBDefine.STATUS_AVAILABLE;
		default:
			return OBDefine.STATUS_UNAVAILABLE;
		}
	}

	// SNMP로 모델이 unknown인 경우 해당 모델로 가져온다.
	private String typePlatform(String typeNumber) {
		if (typeNumber == null) {
			return null;
		}

		switch (typeNumber) {
		case "C36":// F5 1500
			return "1500";
		case "C102": // F5 1600
			return "1600";
		case "C112":// F5 2000s나 2200s
			return "2000";
		case "D114":// F5 2200
			return "2200";
		case "F100":// VIPRION 2400
			return "2400";
		case "A109":// VIPRION 2400
			return "2400";
		case "C62":// F5 3400
			return "3400";
		case "C100":// F5 3410
			return "3410";
		case "C103":// F5 3600
			return "3600";
		case "C106": // F5 3900
			return "3900";
		case "C113":// F5 4000/4200
			return "4000";
		case "D46":// F5 4100
			return "4100";
		case "J100":// VIPRION 4400
			return "4400";
		case "J101":// F5 4400
			return "4400";
		case "J102":// VIPRION 4480
			return "4480";
		case "J103":// F5 4480
			return "4480";
		case "S100": // VIPRION 4800
			return "4800";
		case "S101":// F5 4800
			return "4800";
		case "C109":// F5 5000/5200
			return "5000";
		case "D63":// F5 6400
			return "6400";
		case "D63a":// F5 6400
			return "6400";
		case "D68": // F5 6800
			return "6800";
		case "D104":// F5 6900
			return "6900";
		case "D110":// F5 7000/7200
			return "7000";
		case "D84":// F5 8400
			return "8400";
		case "D88": // F5 8800
			return "8800";
		case "D106":// F5 8900
			return "8900";
		case "D107":// F5 8950
			return "8950";
		case "D113":// F5 10000/10200
			return "10000";
		case "D112":// F5 10050
			return "10050";
		case "E101":// F5 11000
			return "11000";
		case "E102": // F5 11050
			return "11050";
		default:
			return null;
		}
	}

	// public static void main(String[] args)
	// {
	// OBSnmpF5 snmp = new OBSnmpF5(OBSnmp.VERSION_2C, "192.168.200.11");
	// OBDtoAdcInfo oid=new OBDtoAdcInfo();
	// oid.setAdcType(OBDefine.ADC_TYPE_F5);
	// oid.setSwVersion("");
	//
	// OBDatabase db=new OBDatabase();
	// OBDtoConnectionData list;
	// try
	// {
	// db.openDB();
	// for(int i=0;i<100;i++)
	// {
	// System.out.println("start:"+OBDateTime.now());
	// list = snmp.getVServerConns(oid, "a_new_vs", db);
	// System.out.println("end:"+OBDateTime.now());
	// System.out.println(list);
	//
	// OBDateTime.Sleep(5000);
	// }
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	public OBDtoConnectionData getVServerConns(int adcType, String swVersion, String vsName) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcType:%d, swVersion:%s, vsName:%s", adcType, swVersion, vsName));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		DtoOidStatVirtServer oid;
		try {
			oid = oidDB.getStatVirtServer(adcType, swVersion);
			return getVServerConns(vsName, oid);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}

	}

	private String parseVServerPostOid(String targetOid, String baseOid) {
		int tmpIndex = targetOid.indexOf(baseOid);
		return targetOid.substring(tmpIndex + baseOid.length());
	}

	private OBDtoConnectionData getVServerConns(String vsName, DtoOidStatVirtServer oid) throws OBException {
		OBDtoConnectionData result = new OBDtoConnectionData();
		List<VariableBinding> tmpList;
		Snmp snmp;
		snmp = openSnmp();
		String postOid = "";
		result.setOccurTime(OBDateTime.toTimestamp(OBDateTime.now()));
		try {
			tmpList = walk(oid.getCurConns());

			result.setCurConns(new Long(0));
			for (int i = 0; i < tmpList.size(); i++) {
				VariableBinding var = tmpList.get(i);
				String name = parseOidName(var.getOid().toString(), oid.getCurConns());
				if ((name != null) && (!name.isEmpty())) {
					if (name.compareToIgnoreCase(vsName) == 0) {
						result.setCurConns(var.getVariable().toLong());
						postOid = parseVServerPostOid(var.getOid().toString(), oid.getCurConns());
					}
				}
			}

			// result.setMaxConns(new Long(0));
			if (postOid.isEmpty()) {
				tmpList = walk(oid.getMaxConns());

				for (int i = 0; i < tmpList.size(); i++) {
					VariableBinding var = tmpList.get(i);
					String name = parseOidName(var.getOid().toString(), oid.getMaxConns());
					if ((name != null) && (!name.isEmpty())) {
						if (name.compareToIgnoreCase(vsName) == 0) {
							result.setMaxConns(var.getVariable().toLong());
						}
					}
				}
			} else {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "postOid = " + postOid);

				String reqOid = oid.getMaxConns() + postOid;
				Object obj = getSnmp(snmp, reqOid);
				result.setMaxConns(Long.parseLong(obj.toString()));
			}

			// result.setTotConns(new Long(0));
			if (postOid.isEmpty()) {
				tmpList = walk(oid.getTotConns());
				for (int i = 0; i < tmpList.size(); i++) {
					VariableBinding var = tmpList.get(i);
					String name = parseOidName(var.getOid().toString(), oid.getMaxConns());
					if ((name != null) && (!name.isEmpty())) {
						if (name.compareToIgnoreCase(vsName) == 0) {
							result.setTotConns(var.getVariable().toLong());
						}
					}
				}
			} else {
				String reqOid = oid.getTotConns() + postOid;
				Object obj = getSnmp(snmp, reqOid);
				result.setTotConns(Long.parseLong(obj.toString()));
			}
			closeSnmp(snmp);
			return result;
		} catch (OBException e) {
			closeSnmp(snmp);
			throw e;
		} catch (Exception e) {
			closeSnmp(snmp);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	// public static void main(String[] args)
	// {
	// OBSnmpF5 snmp = new OBSnmpF5(OBSnmp.VERSION_2C, "192.168.200.11");
	// OBDtoAdcInfo adcInfo=new OBDtoAdcInfo();
	// adcInfo.setAdcType(OBDefine.ADC_TYPE_F5);
	// adcInfo.setSwVersion("");
	// adcInfo.setIndex(13);
	//
	// OBDatabase db=new OBDatabase();
	// try
	// {
	// db.openDB();
	// ArrayList<OBDtoMonTrafficPoolMembersF5> list =
	// snmp.getTrafficPoolMembers(adcInfo, db);
	// System.out.println(list);
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public ArrayList<OBDtoMonTrafficPoolMembers> getTrafficPoolMembers(int adcType, String swVersion,
			ArrayList<OBDtoAdcVServerF5> vsList) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		DtoOidStatPoolMembers oid;
		try {
			oid = oidDB.getStatPoolMembers(adcType, swVersion);

			HashMap<String, DtoSnmpStatPoolMembers> poolMembersTraffic = getTrafficPoolMembers(oid);
			// ArrayList<OBDtoAdcVServerF5> vsList = new
			// OBVServerDB().getVServerListAllF5(adcInfo.getIndex());
			ArrayList<OBDtoMonTrafficPoolMembers> result = new ArrayList<OBDtoMonTrafficPoolMembers>();
			for (OBDtoAdcVServerF5 vsInfo : vsList) {
				OBDtoAdcPoolF5 pool = vsInfo.getPool();
				if (pool == null)
					continue;

				ArrayList<OBDtoAdcPoolMemberF5> memberList = pool.getMemberList();
				for (OBDtoAdcPoolMemberF5 member : memberList) {
					String name = pool.getName() + "_" + member.getIpAddress() + "_" + member.getPort();
					DtoSnmpStatPoolMembers stat = poolMembersTraffic.get(name);

					if (stat == null)
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
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}
	}

	private ArrayList<String> parseOidPoolMembers(String targetOid, String baseOid) {
		int tmpIndex = targetOid.indexOf(baseOid);
		String sLine = targetOid.substring(tmpIndex + baseOid.length());
		String[] data = sLine.split("\\.");

		int iIndex = 0;

		for (; iIndex < data.length; iIndex++) {
			if (!data[iIndex].isEmpty())
				break;
			;
		}
		// pool name 추출.
		iIndex++;
		String poolName = "";
		for (; iIndex < data.length; iIndex++) {
			// if(data[iIndex].isEmpty())
			// break;;
			byte[] bytes = new byte[1];
			bytes[0] = (byte) Integer.parseInt(data[iIndex]);
			if ((bytes[0] >= '!') && (bytes[0] <= '~'))
				poolName += new String(bytes);
			else
				break;
		}

		if ((data.length <= iIndex + 1) || (data[iIndex + 1].compareTo("4") != 0)) {
			return null;
			// ArrayList<String> result = new ArrayList<String>();
			// result.add(poolName);
			// result.add("");
			// result.add("");
			// return result;
		}
		// node ip 주소 추출.
		iIndex += 2;
		String nodeIPAddress = "";
		for (int i = iIndex; i < iIndex + 4; i++) {
			if (i != iIndex)
				nodeIPAddress += ".";
			nodeIPAddress += data[i];
		}

		// service port 추출.
		iIndex += 4;
		String srvPort = "";
		srvPort = data[iIndex];

		ArrayList<String> result = new ArrayList<String>();
		result.add(poolName);
		result.add(nodeIPAddress);
		result.add(srvPort);
		return result;
	}

	// public static void main(String[] args)
	// {
	// OBSnmpF5 snmp = new OBSnmpF5("192.168.100.10");
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// OBDtoAdcInfo adcInfo = new OBDtoAdcInfo();
	// adcInfo.setIndex(8);
	// adcInfo.setSwVersion("");
	// adcInfo.setAdcType(OBDefine.ADC_TYPE_F5);
	// HashMap<String, DtoStatusTmp> list =
	// snmp.getPoolMemberStatus("1.3.6.1.4.1.3375.2.2.5.6.2.1.5", "", "public");
	// System.out.println(list);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	// public HashMap<String, DtoSnmpStatPoolMembers> getNodeStatus(int adcType,
	// String swVersion, OBDatabase db) throws OBException
	// {
	// OBSnmpOidDB oidDB = new OBSnmpOidDB();
	//
	// DtoOidInfoAdcResc oid;
	// try
	// {
	// oid = oidDB.getSystemResc(adcType, swVersion, db);
	// }
	// catch(OBException e)
	// {
	// throw new OBException(e.getMessage());
	// }
	//
	// HashMap<String, DtoSnmpStatPoolMembers> result =
	// getPoolMemberStatus(oid.getNodeAvaliableState(), oid.getNodeEnabledState());
	// return result;
	// }
	public String makeVsOid(String vsName) {
		byte[] bytes;
		int len = vsName.length();
		String oidString = "." + len;
		try {
			bytes = vsName.getBytes("US-ASCII");
			for (byte by : bytes) {
				oidString += ("." + by);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace(); // TODO
		}
		System.out.println(oidString);
		return oidString;
	}

	private HashMap<String, DtoStatusTmp> getPoolMemberStatusPartial(String availOid, String enabledOid,
			ArrayList<String> vsNameList) throws OBException {
		HashMap<String, DtoStatusTmp> result = new HashMap<String, DtoStatusTmp>();
		String vsOid = "";
		String walkOid = ""; // availOid와 eanbledOid에 vsOid를 붙여서 만듦. Oid를 범위를 좁혀 부분 walk를 vs별로 하게.
		long tt0, tt1, tt2, tt3, tt4;
		List<VariableBinding> tmpList;
		try {
			tt0 = System.currentTimeMillis();
			for (String vsName : vsNameList) {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "pool member status partial read. vsName = " + vsName);
				vsOid = makeVsOid(vsName);
				tt1 = System.currentTimeMillis();// 기준시간
				walkOid = availOid + vsOid;
				tmpList = walk(walkOid);
				for (VariableBinding var : tmpList) {
					// 아래 실행문 주의할 것. walk는 vsOid를 붙여서 했지만 parsing은 원래대로 한다. availOid로. 이렇게 하면
					// 부분업데이트에 대한 별도 파싱을 짜지 않아도 된다.
					ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), availOid);
					if ((nameList != null) && (nameList.size() == 3)) {
						DtoStatusTmp member = new DtoStatusTmp();

						member.setAvail(var.getVariable().toInt());
						member.setEnable(OBDefine.STATUS_DISABLE);
						member.setStatus(caluVsStatus(member.getAvail(), member.getEnable()));

						member.setPoolName(nameList.get(0));
						member.setNodeIP(nameList.get(1));
						member.setSrvPort(Integer.parseInt(nameList.get(2)));

						String name = nameList.get(0) + "_" + nameList.get(1) + "_" + nameList.get(2);

						result.put(name, member);
					}
					// else
					// {
					// OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("Invalid
					// data-name"));
					// // throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid
					// data");
					// }
				}
				tt2 = System.currentTimeMillis();
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "     partial availOid read time: " + (tt2 - tt1) / 1000.0);
				walkOid = enabledOid + vsOid;
				tmpList = walk(walkOid);
				for (VariableBinding var : tmpList) {
					// 아래 실행문 주의할 것. walk는 vsOid를 붙여서 했지만 parsing은 원래대로 한다. enabledOid로. 이렇게 하면
					// 부분업데이트에 대한 별도 파싱을 짜지 않아도 된다.
					ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), enabledOid);
					if ((nameList != null) && (nameList.size() == 3)) {
						String name = nameList.get(0) + "_" + nameList.get(1) + "_" + nameList.get(2);
						DtoStatusTmp member = result.get(name);
						if (member != null) {
							member.setEnable(var.getVariable().toInt());
							member.setStatus(caluVsStatus(member.getAvail(), member.getEnable()));
						}
					}
					// else
					// {
					// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid
					// data");
					// }
				}
				tt3 = System.currentTimeMillis();
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "     partial enabledOid read time: " + (tt3 - tt2) / 1000.0);
			}
			tt4 = System.currentTimeMillis();
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "read time: " + (tt4 - tt0) / 1000.0);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "read size: " + result.size());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}
		return result;
	}

	private HashMap<String, DtoStatusTmp> getPoolMemberStatus(String availOid, String enabledOid) throws OBException {
		HashMap<String, DtoStatusTmp> result = new HashMap<String, DtoStatusTmp>();
		try {
			List<VariableBinding> tmpList;
			tmpList = walk(availOid);
			for (VariableBinding var : tmpList) {
				ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), availOid);
				if ((nameList != null) && (nameList.size() == 3)) {
					DtoStatusTmp member = new DtoStatusTmp();

					member.setAvail(var.getVariable().toInt());
					member.setEnable(OBDefine.STATUS_DISABLE);
					member.setStatus(caluVsStatus(member.getAvail(), member.getEnable()));

					member.setPoolName(nameList.get(0));
					member.setNodeIP(nameList.get(1));
					member.setSrvPort(Integer.parseInt(nameList.get(2)));

					String name = nameList.get(0) + "_" + nameList.get(1) + "_" + nameList.get(2);

					result.put(name, member);
				}
				// else
				// {
				// OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("Invalid
				// data-name"));
				// // throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid
				// data");
				// }
			}

			tmpList = walk(enabledOid);
			for (VariableBinding var : tmpList) {
				ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), enabledOid);
				if ((nameList != null) && (nameList.size() == 3)) {
					String name = nameList.get(0) + "_" + nameList.get(1) + "_" + nameList.get(2);
					DtoStatusTmp member = result.get(name);
					if (member != null) {
						member.setEnable(var.getVariable().toInt());
						member.setStatus(caluVsStatus(member.getAvail(), member.getEnable()));
					}
					// else
					// {
					// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid
					// data");
					// }
				}
				// else
				// {
				// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid
				// data");
				// }
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}
		return result;
	}

	// private ArrayList<DtoSnmpStatPoolMembers>
	// getTrafficPoolMembers(DtoOidStatPoolMembers oid) throws OBException
	private HashMap<String, DtoSnmpStatPoolMembers> getTrafficPoolMembers(DtoOidStatPoolMembers oid)
			throws OBException {
		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. %s.",
		// oid.toString()));

		HashMap<String, DtoSnmpStatPoolMembers> result = new HashMap<String, DtoSnmpStatPoolMembers>();

		List<VariableBinding> tmpList;

		try {
			// bytes in
			tmpList = walk(oid.getBytesIn());
			for (VariableBinding var : tmpList) {
				ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), oid.getBytesIn());
				if ((nameList != null) && (nameList.size() == 3)) {
					DtoSnmpStatPoolMembers member = new DtoSnmpStatPoolMembers();
					member.setPoolName(nameList.get(0));
					member.setMemberIP(nameList.get(1));
					member.setServicePort(Integer.parseInt(nameList.get(2)));
					member.setBytesIn(var.getVariable().toLong());

					String name = member.getPoolName() + "_" + member.getMemberIP() + "_" + member.getServicePort();
					result.put(name, member);
				}
				// else
				// {
				// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid
				// data");
				// }
			}

			// bytes out
			tmpList = walk(oid.getBytesOut());
			for (VariableBinding var : tmpList) {
				ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), oid.getBytesOut());
				if ((nameList != null) && (nameList.size() == 3)) {
					String name = nameList.get(0) + "_" + nameList.get(1) + "_" + nameList.get(2);
					DtoSnmpStatPoolMembers member = result.get(name);
					if (member != null)
						member.setBytesOut(var.getVariable().toLong());
					// else
					// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid
					// data");
				}
				// else
				// {
				// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid
				// data");
				// }
			}

			// pkts in
			tmpList = walk(oid.getPktsIn());
			for (VariableBinding var : tmpList) {
				ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), oid.getPktsIn());
				if ((nameList != null) && (nameList.size() == 3)) {
					String name = nameList.get(0) + "_" + nameList.get(1) + "_" + nameList.get(2);
					DtoSnmpStatPoolMembers member = result.get(name);
					if (member != null)
						member.setPktsIn(var.getVariable().toLong());
					// else
					// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid
					// data");
				}
				// else
				// {
				// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid
				// data");
				// }
			}

			// pkts out
			tmpList = walk(oid.getPktsOut());
			for (VariableBinding var : tmpList) {
				ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), oid.getPktsOut());
				if ((nameList != null) && (nameList.size() == 3)) {
					String name = nameList.get(0) + "_" + nameList.get(1) + "_" + nameList.get(2);
					DtoSnmpStatPoolMembers member = result.get(name);
					if (member != null)
						member.setPktsOut(var.getVariable().toLong());
					// else
					// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid
					// data");
				}
				// else
				// {
				// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid
				// data");
				// }
			}
			// max conns
			tmpList = walk(oid.getMaxConns());
			for (VariableBinding var : tmpList) {
				ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), oid.getMaxConns());
				if ((nameList != null) && (nameList.size() == 3)) {
					String name = nameList.get(0) + "_" + nameList.get(1) + "_" + nameList.get(2);
					DtoSnmpStatPoolMembers member = result.get(name);
					if (member != null)
						member.setMaxConns(var.getVariable().toLong());
					// else
					// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid
					// data");
				}
				// else
				// {
				// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid
				// data");
				// }
			}
			// tot conns
			tmpList = walk(oid.getTotConns());
			for (VariableBinding var : tmpList) {
				ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), oid.getTotConns());
				if ((nameList != null) && (nameList.size() == 3)) {
					String name = nameList.get(0) + "_" + nameList.get(1) + "_" + nameList.get(2);
					DtoSnmpStatPoolMembers member = result.get(name);
					if (member != null)
						member.setTotConns(var.getVariable().toLong());
					// else
					// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid
					// data");
				}
				// else
				// {
				// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid
				// data");
				// }
			}
			// cur conns
			tmpList = walk(oid.getCurConns());
			for (VariableBinding var : tmpList) {
				ArrayList<String> nameList = parseOidPoolMembers(var.getOid().toString(), oid.getCurConns());
				if ((nameList != null) && (nameList.size() == 3)) {
					String name = nameList.get(0) + "_" + nameList.get(1) + "_" + nameList.get(2);
					DtoSnmpStatPoolMembers member = result.get(name);
					if (member != null)
						member.setCurConns(var.getVariable().toLong());
					// else
					// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid
					// data");
				}
				// else
				// {
				// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Invalid
				// data");
				// }
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}

		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s",
		// result));
		return result;
	}

	// public static void main(String[] args)
	// {
	// OBSnmpF5 snmp = new OBSnmpF5(OBSnmp.VERSION_2C, "192.168.200.10");
	// OBDtoAdcInfo oid=new OBDtoAdcInfo();
	// oid.setAdcType(OBDefine.ADC_TYPE_F5);
	// oid.setSwVersion("");
	//
	// OBDatabase db=new OBDatabase();
	// ArrayList<OBDtoMonTrafficVServer> list;
	// try
	// {
	// db.openDB();
	// list = snmp.getTrafficVServer(oid, db);
	// for(OBDtoMonTrafficVServer traffic:list)
	// System.out.println(traffic);
	// // System.out.println(list);
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	public ArrayList<OBDtoMonTrafficVServer> getTrafficVServer(int adcType, String swVersion) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		DtoOidStatVirtServer oid;
		try {
			oid = oidDB.getStatVirtServer(adcType, swVersion);

			ArrayList<OBDtoMonTrafficVServer> list = getTrafficVServer(oid);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result-size:%d", list.size()));
			return list;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}
	}

	public OBDtoMonTrafficVServer getTrafficVServerInfo(int adcType, String swVersion, String vsName)
			throws OBException {
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		DtoOidStatVirtServer oid;
		try {
			oid = oidDB.getStatVirtServer(adcType, swVersion);

			OBDtoMonTrafficVServer retVal = getTrafficVServerInfo(oid, vsName);
			return retVal;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}
	}

	private String convertIPAddress(OctetString ip) {
		String result = (ip.get(0) & 0xff) + "." + (ip.get(1) & 0xff) + "." + (ip.get(2) & 0xff) + "."
				+ (ip.get(3) & 0xff);
		return result;
	}

	private ArrayList<OBDtoMonTrafficVServer> getTrafficVServer(DtoOidStatVirtServer oid) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. %s.", oid));

		ArrayList<OBDtoMonTrafficVServer> result = new ArrayList<OBDtoMonTrafficVServer>();

		List<VariableBinding> tmpList;

		try {
			// virtual server name
			tmpList = walk(oid.getVsName());
			for (VariableBinding var : tmpList) {
				String name = parseOidName(var.getOid().toString(), oid.getVsName());
				if ((name != null) && (!name.isEmpty())) {
					OBDtoMonTrafficVServer info = new OBDtoMonTrafficVServer();

					info.setName(var.getVariable().toString().replace("\"", ""));

					result.add(info);
				}
			}

			// ip address
			tmpList = walk(oid.getVsIPAddress());
			for (int i = 0; i < tmpList.size(); i++) {
				VariableBinding var = tmpList.get(i);
				String name = parseOidName(var.getOid().toString(), oid.getVsIPAddress());
				if ((name != null) && (!name.isEmpty())) {
					if (result.get(i).getName().compareToIgnoreCase(name) == 0) {
						result.get(i).setIpaddress(convertIPAddress((OctetString) var.getVariable()));
					}
				}
			}

			tmpList = walk(oid.getBytesIn());
			for (int i = 0; i < tmpList.size(); i++) {
				VariableBinding var = tmpList.get(i);
				String name = parseOidName(var.getOid().toString(), oid.getBytesIn());
				if ((name != null) && (!name.isEmpty())) {
					if (result.get(i).getName().compareToIgnoreCase(name) == 0) {
						result.get(i).setBytesIn(var.getVariable().toLong());
					}
				}
			}

			tmpList = walk(oid.getBytesOut());
			for (int i = 0; i < tmpList.size(); i++) {
				VariableBinding var = tmpList.get(i);
				String name = parseOidName(var.getOid().toString(), oid.getBytesOut());
				if ((name != null) && (!name.isEmpty())) {
					if (result.get(i).getName().compareToIgnoreCase(name) == 0) {
						result.get(i).setBytesOut(var.getVariable().toLong());
					}
				}
			}

			tmpList = walk(oid.getCurConns());
			for (int i = 0; i < tmpList.size(); i++) {
				VariableBinding var = tmpList.get(i);
				String name = parseOidName(var.getOid().toString(), oid.getCurConns());
				if ((name != null) && (!name.isEmpty())) {
					if (result.get(i).getName().compareToIgnoreCase(name) == 0) {
						result.get(i).setCurConns(var.getVariable().toLong());
					}
				}
			}

			tmpList = walk(oid.getMaxConns());
			for (int i = 0; i < tmpList.size(); i++) {
				VariableBinding var = tmpList.get(i);
				String name = parseOidName(var.getOid().toString(), oid.getMaxConns());
				if ((name != null) && (!name.isEmpty())) {
					if (result.get(i).getName().compareToIgnoreCase(name) == 0) {
						result.get(i).setMaxConns(var.getVariable().toLong());
					}
				}
			}

			tmpList = walk(oid.getPktsIn());
			for (int i = 0; i < tmpList.size(); i++) {
				VariableBinding var = tmpList.get(i);
				String name = parseOidName(var.getOid().toString(), oid.getPktsIn());
				if ((name != null) && (!name.isEmpty())) {
					if (result.get(i).getName().compareToIgnoreCase(name) == 0) {
						result.get(i).setPktsIn(var.getVariable().toLong());
					}
				}
			}

			tmpList = walk(oid.getPktsOut());
			for (int i = 0; i < tmpList.size(); i++) {
				VariableBinding var = tmpList.get(i);
				String name = parseOidName(var.getOid().toString(), oid.getPktsOut());
				if ((name != null) && (!name.isEmpty())) {
					if (result.get(i).getName().compareToIgnoreCase(name) == 0) {
						result.get(i).setPktsOut(var.getVariable().toLong());
					}
				}
			}

			tmpList = walk(oid.getTotConns());
			for (int i = 0; i < tmpList.size(); i++) {
				VariableBinding var = tmpList.get(i);
				String name = parseOidName(var.getOid().toString(), oid.getTotConns());
				if ((name != null) && (!name.isEmpty())) {
					if (result.get(i).getName().compareToIgnoreCase(name) == 0) {
						result.get(i).setTotConns(var.getVariable().toLong());
					}
				}
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result-size:%d", result.size()));
		return result;
	}

	private String makeOidName(String name) throws Exception {
		String retVal = "";
		if (name.isEmpty())
			return retVal;
		retVal += "." + name.length();
		byte[] bytes = name.getBytes("US-ASCII");
		for (int i = 0; i < name.length(); i++) {
			retVal += "." + bytes[i];
		}
		return retVal;
	}

	private OBDtoMonTrafficVServer getTrafficVServerInfo(DtoOidStatVirtServer oid, String vsName) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. %s.", oid));

		OBDtoMonTrafficVServer retVal = new OBDtoMonTrafficVServer();

		// List<VariableBinding> tmpList;
		String oidInfo = "";
		Snmp snmp = openSnmp();

		try {
			String nameString = makeOidName(vsName);

			// virtual server name
			oidInfo = oid.getVsName() + nameString;
			if (oidInfo != null) {
				Object obj = getSnmp(snmp, oidInfo);
				if (checkObject(obj)) {
					retVal.setName(obj.toString().replace("\"", ""));
				}
			}
			// tmpList = walk(oidInfo);
			// for(VariableBinding var:tmpList)
			// {// 데이터는 한개만 리턴된다.
			// String name = parseOidName(var.getOid().toString(), oid.getVsName());
			// if((name!=null) && (!name.isEmpty()))
			// {
			// retVal.setName(var.getVariable().toString().replace("\"", ""));
			// }
			// }

			// ip address
			oidInfo = oid.getVsIPAddress() + nameString;
			if (oidInfo != null) {
				Object obj = getSnmp(snmp, oidInfo);
				if (checkObject(obj)) {
					retVal.setIpaddress(convertIPAddress((OctetString) obj));
				}
			}

			oidInfo = oid.getBytesIn() + nameString;
			if (oidInfo != null) {
				Counter64 obj = (Counter64) getSnmp(snmp, oidInfo);
				if (checkObject(obj)) {
					retVal.setBytesIn((Long) obj.toLong());
				}
			}

			oidInfo = oid.getBytesOut() + nameString;
			if (oidInfo != null) {
				Counter64 obj = (Counter64) getSnmp(snmp, oidInfo);
				if (checkObject(obj)) {
					retVal.setBytesOut((Long) obj.toLong());
				}
			}

			oidInfo = oid.getCurConns() + nameString;
			if (oidInfo != null) {
				Counter64 obj = (Counter64) getSnmp(snmp, oidInfo);
				if (checkObject(obj)) {
					retVal.setCurConns((Long) obj.toLong());
				}
			}

			oidInfo = oid.getMaxConns() + nameString;
			if (oidInfo != null) {
				Counter64 obj = (Counter64) getSnmp(snmp, oidInfo);
				if (checkObject(obj)) {
					retVal.setMaxConns((Long) obj.toLong());
				}
			}

			oidInfo = oid.getPktsIn() + nameString;
			if (oidInfo != null) {
				Counter64 obj = (Counter64) getSnmp(snmp, oidInfo);
				if (checkObject(obj)) {
					retVal.setPktsIn((Long) obj.toLong());
				}
			}

			oidInfo = oid.getPktsOut() + nameString;
			if (oidInfo != null) {
				Counter64 obj = (Counter64) getSnmp(snmp, oidInfo);
				if (checkObject(obj)) {
					retVal.setPktsOut((Long) obj.toLong());
				}
			}

			oidInfo = oid.getTotConns() + nameString;
			if (oidInfo != null) {
				Counter64 obj = (Counter64) getSnmp(snmp, oidInfo);
				if (checkObject(obj)) {
					retVal.setTotConns((Long) obj.toLong());
				}
			}
		} catch (OBException e) {
			closeSnmp(snmp);
			throw e;
		} catch (Exception e) {
			closeSnmp(snmp);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		closeSnmp(snmp);
		return retVal;
	}

	// public static void main(String[] args)
	// {
	// OBSnmpF5 snmp = new OBSnmpF5(OBSnmp.VERSION_2C, "192.168.200.11");
	// OBDtoAdcInfo adcInfo=new OBDtoAdcInfo();
	// adcInfo.setAdcType(OBDefine.ADC_TYPE_F5);
	// adcInfo.setSwVersion("");
	// adcInfo.setIndex(3);
	// adcInfo.setSnmpRComm("anwkddo6619");
	//
	// OBDatabase db=new OBDatabase();
	// try
	// {
	// db.openDB();
	// ArrayList<OBDtoAdcVServerF5> vsList = snmp.getSlbStatus(adcInfo, db);
	// for(OBDtoAdcVServerF5 vs:vsList)
	// System.out.println(vs);
	// db.closeDB();
	// // new OBAdcMonitorDB().writeSlbStatusF5(26, vsList, db);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }
	public ArrayList<OBDtoAdcVServerF5> getSlbStatusPartial(int adcType, String swVersion,
			ArrayList<OBDtoAdcVServerF5> vsList) throws OBException {
		return getSlbStatusCore(adcType, swVersion, vsList, OBDefine.RANGE.PARTIAL_VS); // 부분업데이트
	}

	public ArrayList<OBDtoAdcVServerF5> getSlbStatusAll(int adcType, String swVersion,
			ArrayList<OBDtoAdcVServerF5> vsList) throws OBException {
		return getSlbStatusCore(adcType, swVersion, vsList, OBDefine.RANGE.ALL); // 전체 읽기
	}

	private ArrayList<OBDtoAdcVServerF5> getSlbStatusCore(int adcType, String swVersion,
			ArrayList<OBDtoAdcVServerF5> vsList, int range) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcType:%d, swVersion:%s", adcType, swVersion));
		// ArrayList<OBDtoAdcVServerF5> vsList = new
		// OBVServerDB().getVServerListAllF5(adcInfo.getIndex());

		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		DtoOidStatVirtServer oidVSStat = oidDB.getStatVirtServer(adcType, swVersion);
		DtoOidInfoAdcResc oidSysResc = oidDB.getAdcResource(adcType, swVersion);
		HashMap<String, DtoStatusTmp> poolMemberMap;
		HashMap<String, DtoStatusTmp> vsMap = getVSStatus(oidVSStat);
		if (range == OBDefine.RANGE.ALL) {
			poolMemberMap = getPoolMemberStatus(oidSysResc.getNodeAvaliableState(), oidSysResc.getNodeEnabledState());
		} else // range==OBDefine.RANGE.PARTIAL_VS, 부분 읽기. 특정 vs
		{
			ArrayList<String> vsNameList = new ArrayList<String>();
			for (OBDtoAdcVServerF5 vs : vsList) {
				vsNameList.add(vs.getName());
			}
			poolMemberMap = getPoolMemberStatusPartial(oidSysResc.getNodeAvaliableState(),
					oidSysResc.getNodeEnabledState(), vsNameList);
		}

		for (OBDtoAdcVServerF5 vs : vsList) {
			DtoStatusTmp vsStatus = vsMap.get(vs.getName());
			if (vsStatus == null) {
				vs.setStatus(OBDefine.STATUS_DISABLE);
			} else
				vs.setStatus(vsStatus.getStatus());
			OBDtoAdcPoolF5 pool = vs.getPool();
			if (pool == null) {
				continue;
			}
			ArrayList<OBDtoAdcPoolMemberF5> memberList = pool.getMemberList();
			if (memberList == null || memberList.size() == 0) {
				continue;
			}
			for (OBDtoAdcPoolMemberF5 member : memberList) {
				String name = pool.getName() + "_" + member.getIpAddress() + "_" + member.getPort();
				DtoStatusTmp memberStatus = poolMemberMap.get(name);
				if (memberStatus == null) {
					member.setStatus(OBDefine.STATUS_DISABLE);
				} else
					member.setStatus(memberStatus.getStatus());
			}
		}
		return vsList;
	}

	private HashMap<String, DtoStatusTmp> getVSStatus(DtoOidStatVirtServer oid) throws OBException {
		HashMap<String, DtoStatusTmp> result = new HashMap<String, DtoStatusTmp>();

		List<VariableBinding> tmpList;

		// virtual server name
		tmpList = walk(oid.getVsName());
		for (VariableBinding var : tmpList) {
			// String name = parseOidName(var.getOid().toString(), oid.getVsName());
			String name = var.getVariable().toString();
			if ((name != null) && (!name.isEmpty())) {
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
		for (VariableBinding var : tmpList) {
			String name = parseOidName(var.getOid().toString(), oid.getStatus());
			if ((name != null) && (!name.isEmpty())) {
				DtoStatusTmp info = result.get(name);
				if (info != null) {
					info.setAvail(var.getVariable().toInt());
					info.setStatus(caluVsStatus(info.getAvail(), info.getEnable()));
				}
			}
		}
		tmpList = walk(oid.getEnabled());
		for (VariableBinding var : tmpList) {
			String name = parseOidName(var.getOid().toString(), oid.getEnabled());
			if ((name != null) && (!name.isEmpty())) {
				DtoStatusTmp info = result.get(name);
				if (info != null) {
					info.setEnable(var.getVariable().toInt());
					info.setStatus(caluVsStatus(info.getAvail(), info.getEnable()));
				}
			}
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result-size:%d", result.size()));
		return result;
	}

	private HashMap<String, DtoStatusTmp> getVSStatus(String vsIPAddrOid, String availOid, String enabledOid)
			throws OBException {
		HashMap<String, DtoStatusTmp> result = new HashMap<String, DtoStatusTmp>();

		List<VariableBinding> tmpList;

		// // virtual server name
		// tmpList = walk(oid.getVsName());
		// for(VariableBinding var:tmpList)
		// {
		// String name = parseOidName(var.getOid().toString(), oid.getVsName());
		// if((name!=null) && (!name.isEmpty()))
		// {
		// DtoStatusTmp info = new DtoStatusTmp();
		// info.setAvail(OBDefine.STATUS_DISABLE);
		// info.setEnable(OBDefine.STATUS_DISABLE);
		// info.setStatus(caluVsStatus(info.getAvail(), info.getEnable()));
		// result.put(name, info);
		// }
		// }
		String reqOid = "";
		// VS 이름별로 VS 주소를 추출하여 hashmap에 저장한다.
		HashMap<String, String> vsIPMap = new HashMap<String, String>();
		reqOid = vsIPAddrOid;
		if (reqOid != null) {
			tmpList = walk(reqOid);
			for (VariableBinding var : tmpList) {
				String ipAddr = convertIPAddress((OctetString) var.getVariable());
				String vsName = parseOidName(var.getOid().toString(), reqOid);
				vsIPMap.put(vsName, ipAddr);
			}
		}

		// vs status 추출.
		tmpList = walk(availOid);
		for (VariableBinding var : tmpList) {
			String name = parseOidName(var.getOid().toString(), availOid);
			if ((name != null) && (!name.isEmpty())) {
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
		for (VariableBinding var : tmpList) {
			String name = parseOidName(var.getOid().toString(), enabledOid);
			if ((name != null) && (!name.isEmpty())) {
				DtoStatusTmp info = result.get(name);
				if (info != null) {
					info.setEnable(var.getVariable().toInt());
					info.setStatus(caluVsStatus(info.getAvail(), info.getEnable()));
				}
			}
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result-size:%d", result.size()));
		return result;
	}

	public String getAdcHostname(int adcType, String swVersion, OBDatabase db) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start."));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		DtoOidInfoAdcResc oidInfo;

		try {
			oidInfo = oidDB.getAdcResource(OBDefine.ADC_TYPE_F5, "");
			String name = getAdcHostname(oidInfo.getAdcName());
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", name));
			return name;
		} catch (OBException e) {
			throw new OBException(e.getMessage());
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}
	}

	private String getAdcHostname(String oid) throws OBException {
		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. %s.",
		// oid.toString()));

		String result = "";
		String tempOid;
		Snmp snmp = openSnmp();

		try {
			tempOid = oid;
			if ((tempOid != null) && (!tempOid.isEmpty())) {
				Object obj = getSnmp(snmp, tempOid);
				if (checkObject(obj))
					result = obj.toString();
			}
			closeSnmp(snmp);
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s.",
			// info.toString()));
			return result;
		} catch (OBException e) {
			closeSnmp(snmp);
			throw e;
		} catch (Exception e) {
			closeSnmp(snmp);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public String getAdcSWVersion(int adcType, String swVersion, OBDatabase db) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start."));
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		DtoOidInfoAdcResc oidInfo;

		try {
			oidInfo = oidDB.getAdcResource(adcType, swVersion);
			String name = getAdcSWVersion(oidInfo.getAdcSwVersion());
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", name));
			return name;
		} catch (OBException e) {
			throw new OBException(e.getMessage());
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}
	}

	private String getAdcSWVersion(String oid) throws OBException {
		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. %s.",
		// oid.toString()));

		String result = "";
		String tempOid;
		Snmp snmp = openSnmp();

		try {
			tempOid = oid;
			if ((tempOid != null) && (!tempOid.isEmpty())) {
				Object obj = getSnmp(snmp, tempOid);
				if (checkObject(obj))
					result = obj.toString();
			}
			closeSnmp(snmp);
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s.",
			// info.toString()));
			return result;
		} catch (OBException e) {
			closeSnmp(snmp);
			throw e;
		} catch (Exception e) {
			closeSnmp(snmp);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	// public OBSnmpF5(int version, String host)
	// {
	// setSnmpVersion(version);
	// setHost(host);
	// }
	// public OBSnmpF5(String host)
	// {
	// setSnmpVersion(OBSnmp.VERSION_2C);
	// setHost(host);
	// }
	public OBSnmpF5(String host, OBDtoAdcSnmpInfo authInfo) {
		super(host, authInfo);
	}

	public boolean isAvaliableSnmp(String oid) throws OBException {
		Snmp snmp = openSnmp();
		boolean retVal = true;
		try {
			Object obj = getSnmp(snmp, oid);
			if (true != checkObject(obj)) {
				retVal = false;
			}
		} catch (OBException e) {
			retVal = false;
		} catch (Exception e) {
			retVal = false;
		}

		closeSnmp(snmp);
		return retVal;
	}

	public ArrayList<Integer> getSystemFanStatus(int adcType, String swVersion) throws OBException {
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		DtoOidFaultCheckHW oid;
		try {
			oid = oidDB.getFaultCheckHWInfo(adcType, swVersion);
			ArrayList<Integer> retVal = getSystemFanStatus(oid.getHwFanStatus());
			return retVal;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}
	}

	private static final int STATUS_NORMAL = 1; //
	private static final int STATUS_ABNORMAL = 2; //
	private static final int STATUS_NA = 3; //

	private ArrayList<Integer> getSystemFanStatus(String oid) throws OBException {
		ArrayList<Integer> retVal = new ArrayList<Integer>();
		if (oid == null || oid.isEmpty())
			return retVal;

		try {
			List<VariableBinding> tmpList;

			tmpList = walk(oid);
			for (VariableBinding var : tmpList) {
				int value = var.getVariable().toInt();
				if (value == 0)// 0:bad, 1:good, 2:not present. example:
								// SNMPv2-SMI::enterprises.3375.2.1.3.2.1.2.1.2.101 = INTEGER: 1,
								// SNMPv2-SMI::enterprises.3375.2.1.3.2.1.2.1.2.102 = INTEGER: 1
					retVal.add(STATUS_ABNORMAL);
				else if (value == 1)
					retVal.add(STATUS_NORMAL);
				else
					retVal.add(STATUS_NA);
			}
			return retVal;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}
	}

	public ArrayList<Integer> getSystemFanSpeed(int adcType, String swVersion) throws OBException {
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		DtoOidFaultCheckHW oid;
		try {
			oid = oidDB.getFaultCheckHWInfo(adcType, swVersion);
			ArrayList<Integer> retVal = getSystemFanSpeed(oid.getHwFanSpeed());
			return retVal;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}
	}

	private ArrayList<Integer> getSystemFanSpeed(String oid) throws OBException {
		ArrayList<Integer> retVal = new ArrayList<Integer>();
		if (oid == null || oid.isEmpty())
			return retVal;

		try {
			List<VariableBinding> tmpList;

			tmpList = walk(oid);
			for (VariableBinding var : tmpList) {
				retVal.add(var.getVariable().toInt());
			}
			return retVal;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}
	}

	public ArrayList<Integer> getSystemPowerSupplyStatus(int adcType, String swVersion) throws OBException {
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		DtoOidFaultCheckHW oid;
		try {
			oid = oidDB.getFaultCheckHWInfo(adcType, swVersion);
			ArrayList<Integer> retVal = getSystemPowerSupplyStatus(oid.getPowerSupply());
			return retVal;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}
	}

	private ArrayList<Integer> getSystemPowerSupplyStatus(String oid) throws OBException {
		ArrayList<Integer> retVal = new ArrayList<Integer>();
		if (oid == null || oid.isEmpty())
			return retVal;

		try {
			List<VariableBinding> tmpList;

			tmpList = walk(oid);
			for (VariableBinding var : tmpList) {
				int value = var.getVariable().toInt();
				if (value == 0)// 0:bad, 1:good, 2:notpresent.
					retVal.add(STATUS_ABNORMAL);
				else if (value == 1)
					retVal.add(STATUS_NORMAL);
				else
					retVal.add(STATUS_NA);
			}
			return retVal;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}
	}

	// public static void main(String[] args)
	// {
	// OBDatabase db = new OBDatabase();
	// try
	// {
	// OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(3);
	// OBSnmpF5 snmp = new OBSnmpF5(OBSnmp.VERSION_2C, adcInfo.getAdcIpAddress());
	//
	// db.openDB();
	// OBDtoHddInfo info = snmp.getSystemHddtatus(adcInfo, db);
	// System.out.println(info);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }
	//

	public OBDtoHddInfo getSystemHddtatus(int adcType, String swVersion) throws OBException {
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		DtoOidInfoSystem oidSystemInfo;
		try {
			oidSystemInfo = oidDB.getInfoSystem(adcType, swVersion);
			OBDtoHddInfo retVal = getSystemHddtatus(oidSystemInfo);
			return retVal;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}
	}

	private OBDtoHddInfo getSystemHddtatus(DtoOidInfoSystem oidInfo) throws OBException {
		OBDtoHddInfo retVal = new OBDtoHddInfo();

		if (oidInfo == null)
			return retVal;

		try {
			List<VariableBinding> tmpList;
			Long totalLength = 0L;
			Long usedLength = 0L;

			String oid = "";
			oid = oidInfo.getSysHddTotal();
			if (oid != null) {
				tmpList = walk(oid);
				for (VariableBinding var : tmpList) {
					totalLength += var.getVariable().toLong();
				}
			}
			oid = oidInfo.getSysHddUsed();
			if (oid != null) {
				tmpList = walk(oid);
				for (VariableBinding var : tmpList) {
					usedLength += var.getVariable().toLong();
				}
			}
			if (totalLength > 0) {
				retVal.setHddTotal(totalLength);
				retVal.setHddUsed(usedLength);
				retVal.setHddFree(retVal.getHddTotal() - retVal.getHddUsed());
				Long hddUsage = retVal.getHddUsed() * 100L / retVal.getHddTotal();
				retVal.setHddUsage(hddUsage);
			}
			return retVal;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());

		}
	}

//	public static void main(String[] args) {
//		try {
//			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(4);// 4: 200.11, 5: 200.12
//			OBDtoAdcSnmpInfo authInfo = adcInfo.getSnmpInfo();
//			OBSnmpF5 snmp = new OBSnmpF5(adcInfo.getAdcIpAddress(), authInfo);
//
//			OBDtoRptInspectionSnmpF5 info = snmp.getRptInspection(adcInfo.getAdcType(), adcInfo.getSwVersion());
//			System.out.println(info);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * a. 정기점검 보고서용 Header 정보를 추출한다. 리턴되는 DTO는 재활용한다.
	 * 
	 * @param adcType
	 * @param swVersion
	 * @return
	 * @throws OBException
	 */
	public OBDtoRptInspectionSnmpF5 getRptInspectionHeader(int adcType, String swVersion) throws OBException {
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		try {
			OBDtoOidRptInspection oid = oidDB.getRptInspection(adcType, swVersion);
			OBDtoRptInspectionSnmpF5 retVal = getRptInspectionHeader(oid);

			return retVal;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "general: " + e.getMessage());

		}
	}

	private OBDtoRptInspectionSnmpF5 getRptInspectionHeader(OBDtoOidRptInspection oidInfo) throws OBException {
		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. %s.",
		// oid.toString()));
		OBDtoRptInspectionSnmpF5 retVal = new OBDtoRptInspectionSnmpF5();

		Snmp snmp = null;
		String queryOid = "";
		try {
			snmp = openSnmp();

			// "rptHostName"
			retVal.setHostName("");
			try {
				queryOid = oidInfo.getHostName();
				if ((queryOid != null) && (!queryOid.isEmpty())) {
					Object obj = getSnmp(snmp, queryOid);
					if (checkObject(obj))
						retVal.setHostName(obj.toString());
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}

			// "rptModelName"
			retVal.setModel("");
			queryOid = oidInfo.getModel();
			try {
				if ((queryOid != null) && (!queryOid.isEmpty())) {
					Object obj = getSnmp(snmp, queryOid);
					if (checkObject(obj))
						retVal.setModel(obj.toString());
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}

			// "rptOsVersion"
			retVal.setOsVersion("");
			queryOid = oidInfo.getOsVersion();
			try {
				if ((queryOid != null) && (!queryOid.isEmpty())) {
					Object obj = getSnmp(snmp, queryOid);
					if (checkObject(obj))
						retVal.setOsVersion(obj.toString());
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}

			// "rptSerialNum"
			retVal.setSerialNo("");
			queryOid = oidInfo.getSerialNo();
			try {
				if ((queryOid != null) && (!queryOid.isEmpty())) {
					Object obj = getSnmp(snmp, queryOid);
					if (checkObject(obj))
						retVal.setSerialNo(obj.toString());
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}

			// "rptHotFix"
			retVal.setHotFix("");
			queryOid = oidInfo.getHotFix();
			try {
				if ((queryOid != null) && (!queryOid.isEmpty())) {
					Object obj = getSnmp(snmp, queryOid);
					if (checkObject(obj))
						retVal.setHotFix(obj.toString());
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}

			// "rptUpTime"
			TimeTicks upTime = new TimeTicks(0);
			retVal.setUpTime(upTime);
			try {
				queryOid = oidInfo.getUpTime();
				if ((queryOid != null) && (!queryOid.isEmpty())) {
					Object obj = getSnmp(snmp, queryOid);
					if (checkObject(obj) && obj instanceof TimeTicks)
						retVal.setUpTime((TimeTicks) obj);
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "general: " + e.getMessage());
		} finally {
			if (snmp != null)
				closeSnmp(snmp);
		}

		return retVal;
	}

	/**
	 * 정기정검 보고서를 작성한다. snmp로 작성 가능한 항목만 추출한다.
	 * 
	 * @param adcType
	 * @param swVersion
	 * @return
	 * @throws OBException
	 */
	public OBDtoRptInspectionSnmpF5 getRptInspection(int adcType, String swVersion) throws OBException {
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		try {
			OBDtoOidRptInspection oid = oidDB.getRptInspection(adcType, swVersion);
			OBDtoRptInspectionSnmpF5 retVal = getRptInspection(oid);

			// link 정보를 추출 후 리턴 DTO에 설정. 분리한 이유는 link 관련된 기능의 함수 재사용을 위해.
			OBDtoRptInspectionSnmpF5 linkInfo = getRptInspectionLinkInfo(oid);
			retVal.setLinkCollsInfoList(linkInfo.getLinkCollsInfoList());
			retVal.setLinkDuplexInfoList(linkInfo.getLinkDuplexInfoList());
			retVal.setLinkErrorInList(linkInfo.getLinkErrorInList());
			retVal.setLinkErrorOutInfoList(linkInfo.getLinkErrorOutInfoList());
			retVal.setLinkSpeedInfoList(linkInfo.getLinkSpeedInfoList());
			retVal.setLinkUpInfoList(linkInfo.getLinkUpInfoList());
			return retVal;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "general: " + e.getMessage());

		}
	}

	public int getHAStatus(int adcType, String swVersion) throws OBException {
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		try {
			OBDtoOidRptInspection oid = oidDB.getRptInspection(adcType, swVersion);
			int retVal = getHAStatus(oid.getHaStatus());

			return retVal;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "general: " + e.getMessage());

		}
	}

	/**
	 * HA status를 조회함.
	 * 
	 * @param oid
	 * @return
	 * @throws OBException
	 */
	private int getHAStatus(String oid) throws OBException {
		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. %s.",
		// oid.toString()));
		int retVal = -1;

		Snmp snmp = null;
		String queryOid = "";
		try {
			snmp = openSnmp();

			// "rptHAStatus"
			try {
				queryOid = oid;
				if ((queryOid != null) && (!queryOid.isEmpty())) {
					Object obj = getSnmp(snmp, queryOid);
					if (checkObject(obj))
						retVal = (((Integer32) obj).toInt());// 0: stand-by, 그외는 active로 간주. Deprecated!Refer to
																// sysCmTrafficGroupStatus, sysCmFailoverStatus, and
																// sysCmFailoverStatusDetails. This data indicates
																// whether the machine is active or standby. The value
																// for this data could be 0, 1, 2, or 3. The values of 1
																// and 2 are only defined for an active-active
																// installation. If two boxes are both active, value for
																// unit 1 will be 1 and value for unit 2 will be 2.
																// Otherwise, for active unit, this value is 3;for
																// stand-by unit, this value is 0.
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}
			// a.위에서 추출한 값을 이용해서 active/standby 값으로 변환한다.
			if (retVal == 0)
				retVal = OBDtoAdcInfo.ACTIVE_STANDBY_STATE_STANDBY;
			else if (retVal >= 1)
				retVal = OBDtoAdcInfo.ACTIVE_STANDBY_STATE_ACTIVE;
			else
				retVal = OBDtoAdcInfo.ACTIVE_STANDBY_STATE_UNKNOWN;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "general: " + e.getMessage());
		} finally {
			if (snmp != null)
				closeSnmp(snmp);
		}

		return retVal;
	}

	protected String parsePoolMemberIPName(String targetOid, String baseOid) throws OBException {
		String result = "";
		int tmpIndex = targetOid.indexOf(baseOid);
		String sLine = targetOid.substring(tmpIndex + baseOid.length() + 1);
		String[] data = sLine.split("\\.");

		int length = Integer.parseInt(data[0]);
		if (data.length < (length + 2))
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "invalid snmp data. oid: " + targetOid);
		int ipLength = Integer.parseInt(data[length + 2]);
		for (int i = length + 3; i < length + 3 + ipLength; i++) {
			if (!result.isEmpty())
				result += ".";
			result += data[i];
		}
		return result;
	}

	protected String parsePoolMemberPortName(String targetOid, String baseOid) throws OBException {
		String result = "";
		int tmpIndex = targetOid.indexOf(baseOid);
		String sLine = targetOid.substring(tmpIndex + baseOid.length() + 1);
		String[] data = sLine.split("\\.");

		int length = Integer.parseInt(data[0]);
		if (data.length < (length + 2))
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "invalid snmp data. oid: " + targetOid);
		int ipLength = Integer.parseInt(data[length + 2]);

		if (data.length < (length + 3 + ipLength))
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "invalid snmp data. oid: " + targetOid);

		result += data[length + 3 + ipLength];
		return result;
	}

	private OBDtoRptInspectionSnmpF5 getRptInspection(OBDtoOidRptInspection oidInfo) throws OBException {
		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. %s.",
		// oid.toString()));
		OBDtoRptInspectionSnmpF5 retVal = new OBDtoRptInspectionSnmpF5();

		Snmp snmp = null;
		String queryOid = "";
		List<VariableBinding> tmpList;
		try {
			snmp = openSnmp();

			// "rptHostName"
			retVal.setHostName("");
			try {
				queryOid = oidInfo.getHostName();
				if ((queryOid != null) && (!queryOid.isEmpty())) {
					Object obj = getSnmp(snmp, queryOid);
					if (checkObject(obj))
						retVal.setHostName(obj.toString());
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}

			// "rptModelName"
			retVal.setModel("");
			queryOid = oidInfo.getModel();
			try {
				if ((queryOid != null) && (!queryOid.isEmpty())) {
					Object obj = getSnmp(snmp, queryOid);
					if (checkObject(obj))
						retVal.setModel(obj.toString());
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}

			// "rptOsVersion"
			retVal.setOsVersion("");
			queryOid = oidInfo.getOsVersion();
			try {
				if ((queryOid != null) && (!queryOid.isEmpty())) {
					Object obj = getSnmp(snmp, queryOid);
					if (checkObject(obj))
						retVal.setOsVersion(obj.toString());
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}

			// "rptSerialNum"
			retVal.setSerialNo("");
			queryOid = oidInfo.getSerialNo();
			try {
				if ((queryOid != null) && (!queryOid.isEmpty())) {
					Object obj = getSnmp(snmp, queryOid);
					if (checkObject(obj))
						retVal.setSerialNo(obj.toString());
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}

			// "rptHotFix"
			retVal.setHotFix("");
			queryOid = oidInfo.getHotFix();
			try {
				if ((queryOid != null) && (!queryOid.isEmpty())) {
					Object obj = getSnmp(snmp, queryOid);
					if (checkObject(obj))
						retVal.setHotFix(obj.toString());
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}

			// "rptUpTime"
			TimeTicks upTime = new TimeTicks(0);
			retVal.setUpTime(upTime);
			try {
				queryOid = oidInfo.getUpTime();
				if ((queryOid != null) && (!queryOid.isEmpty())) {
					Object obj = getSnmp(snmp, queryOid);
					if (checkObject(obj) && obj instanceof TimeTicks)
						retVal.setUpTime((TimeTicks) obj);
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}

			// "rptCpuMPUtil64"
			ArrayList<OBNameValue> cpuLoadStatusList = new ArrayList<OBNameValue>();
			retVal.setCpuLoadStatusList(cpuLoadStatusList);
			queryOid = oidInfo.getCupLoad();
			try {
				tmpList = walk(queryOid);
				for (VariableBinding var : tmpList) {
					String name = parseOidName(var.getOid().toString(), queryOid);
					if ((name != null) && (!name.isEmpty())) {
						OBNameValue value = new OBNameValue();
						value.setName(name);
						value.setValue(var.getVariable().toString());
						cpuLoadStatusList.add(value);
					}
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}

			// "rptFanStatus"
			ArrayList<OBNameValue> fanStatusList = new ArrayList<OBNameValue>();
			retVal.setFanStatusList(fanStatusList);
			queryOid = oidInfo.getFanStatus();
			try {
				tmpList = walk(queryOid);
				for (VariableBinding var : tmpList) {
					ArrayList<Integer> indexName = parseOid(var.getOid().toString(), queryOid);
					if ((indexName != null) && indexName.size() > 0) {
						OBNameValue value = new OBNameValue();
						value.setName(indexName.get(0).toString());
						value.setValue(var.getVariable().toString());
						fanStatusList.add(value);
					}
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}

			// "rptPoolMemberStatusAvail"
			ArrayList<OBNameValue> poolMemberStatusList = new ArrayList<OBNameValue>();
			retVal.setPoolMemberStatusList(poolMemberStatusList);
			queryOid = oidInfo.getPoolMemberStatus();
			try {
				tmpList = walk(queryOid);
				for (VariableBinding var : tmpList) {
					String poolName = parseOidName(var.getOid().toString(), queryOid);
					String ipAddress = parsePoolMemberIPName(var.getOid().toString(), queryOid);
					String svcPort = parsePoolMemberPortName(var.getOid().toString(), queryOid);
					String poolMemberName = String.format("%s:%s(%s)", ipAddress, svcPort, poolName);
					if (poolMemberName != null && !poolMemberName.isEmpty()) {
						OBNameValue value = new OBNameValue();
						value.setName(poolMemberName);
						value.setValue(var.getVariable().toString());
						poolMemberStatusList.add(value);
					}
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}

			// "rptVSStatusAvail"
			ArrayList<OBNameValue> virtualServerStatusList = new ArrayList<OBNameValue>();
			retVal.setVirtualServerStatusList(virtualServerStatusList);
			queryOid = oidInfo.getVirtualServerStatus();
			try {
				tmpList = walk(queryOid);
				for (VariableBinding var : tmpList) {
					String name = parseOidName(var.getOid().toString(), queryOid);
					if ((name != null) && (!name.isEmpty())) {
						OBNameValue value = new OBNameValue();
						value.setName(name);
						value.setValue(var.getVariable().toString());
						virtualServerStatusList.add(value);
					}
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}

			// "rptPowerSupplyStatus"
			ArrayList<OBNameValue> powerStatusList = new ArrayList<OBNameValue>();
			retVal.setPowerStatusList(powerStatusList);
			queryOid = oidInfo.getPowerStatus();
			try {
				tmpList = walk(queryOid);
				for (VariableBinding var : tmpList) {
					ArrayList<Integer> indexName = parseOid(var.getOid().toString(), queryOid);
					if ((indexName != null) && indexName.size() > 0) {
						OBNameValue value = new OBNameValue();
						value.setName(indexName.get(0).toString());
						value.setValue(var.getVariable().toString());
						powerStatusList.add(value);
					}
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}

			// "rptVlanName"
			ArrayList<OBNameValue> vlanInfoList = new ArrayList<OBNameValue>();
			retVal.setVlanInfoList(vlanInfoList);
			queryOid = oidInfo.getVlanInfo();
			try {
				tmpList = walk(queryOid);

				for (VariableBinding var : tmpList) {
					String name = parseOidName(var.getOid().toString(), queryOid);
					if ((name != null) && (!name.isEmpty())) {
						OBNameValue value = new OBNameValue();
						value.setName(name);
						value.setValue(var.getVariable().toString());
						vlanInfoList.add(value);
					}
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}

			// "rptHAStatus"
			retVal.setHaStatus(0);
			try {
				queryOid = oidInfo.getHaStatus();
				if ((queryOid != null) && (!queryOid.isEmpty())) {
					Object obj = getSnmp(snmp, queryOid);
					if (checkObject(obj))
						retVal.setHaStatus(((Integer32) obj).toInt());
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}

			// "rptGateway"
			ArrayList<OBNameValue> gatewayList = new ArrayList<OBNameValue>();
			retVal.setGatewayList(gatewayList);
			queryOid = oidInfo.getGateway();
			try {
				tmpList = walk(queryOid);

				for (VariableBinding var : tmpList) {
//                    String name = parseOidName(var.getOid().toString(), queryOid);
//                    if((name != null) && (!name.isEmpty()))
					{
						OBNameValue value = new OBNameValue();
						String ipAddress = convertIPAddress((OctetString) var.getVariable());
						value.setName(ipAddress);
						value.setValue(ipAddress);
						gatewayList.add(value);
					}
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}
			// "rptConnMax"
			retVal.setConnMax(0);
			try {
				queryOid = oidInfo.getConnMax();
				if ((queryOid != null) && (!queryOid.isEmpty())) {
					Object obj = getSnmp(snmp, queryOid);
					if (checkObject(obj))
						retVal.setConnMax(((Counter64) obj).toLong());
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get rptConnMax snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}
			// "rptConnCurr"
			retVal.setConnCurr(0);
			try {
				queryOid = oidInfo.getConnCurr();
				if ((queryOid != null) && (!queryOid.isEmpty())) {
					Object obj = getSnmp(snmp, queryOid);
					if (checkObject(obj))
						retVal.setConnCurr(Long.parseLong((obj.toString())));
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get rptConnCurr snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}
			// "rptMemTotal"
			retVal.setMemTotal(0);
			try {
				queryOid = oidInfo.getMemTotal();
				if ((queryOid != null) && (!queryOid.isEmpty())) {
					Object obj = getSnmp(snmp, queryOid);
					if (checkObject(obj))
						retVal.setMemTotal(((Counter64) obj).toLong());
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}
			// "rptMemUsed"
			retVal.setMemUsed(0);
			try {
				queryOid = oidInfo.getMemUsed();
				if ((queryOid != null) && (!queryOid.isEmpty())) {
					Object obj = getSnmp(snmp, queryOid);
					if (checkObject(obj))
						retVal.setMemUsed(((Counter64) obj).toLong());
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}
			// "hwTemperatureStatus"
			ArrayList<OBNameValue> chassisTempStatusList = new ArrayList<OBNameValue>();
			retVal.setChassisTempStatusList(chassisTempStatusList);
			queryOid = oidInfo.getChassisTempStatus();
			try {
				tmpList = walk(queryOid);

				for (VariableBinding var : tmpList) {
					OBNameValue value = new OBNameValue();
					value.setValue(var.getVariable().toString());
					chassisTempStatusList.add(value);
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "general: " + e.getMessage());
		} finally {
			if (snmp != null)
				closeSnmp(snmp);
		}

		return retVal;
	}

	/**
	 * a. 정기점검 보고서에서 사용하기 위한 Link 정보만 추출한다. 보고서 작성시에 Link 정보는 10초 주기로 3회 수집 후 데이터
	 * 변화가 있는 경우에 실패로 간주하기 때문이다. b. 리턴값으로 별도의 DTO를 생성하지 않고, 이전것을 활용한다. Link 관련된 정보에만
	 * 데이터가 저장된다. 나머지는 null로 채워진다.
	 * 
	 * @param adcType
	 * @param swVersion
	 * @return
	 * @throws OBException
	 */
	public OBDtoRptInspectionSnmpF5 getRptInspectionLinkInfo(int adcType, String swVersion) throws OBException {
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		try {
			OBDtoOidRptInspection oid = oidDB.getRptInspection(adcType, swVersion);
			OBDtoRptInspectionSnmpF5 retVal = getRptInspectionLinkInfo(oid);
			return retVal;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "general: " + e.getMessage());

		}
	}

	private OBDtoRptInspectionSnmpF5 getRptInspectionLinkInfo(OBDtoOidRptInspection oidInfo) throws OBException {
		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. %s.",
		// oid.toString()));
		OBDtoRptInspectionSnmpF5 retVal = new OBDtoRptInspectionSnmpF5();

		Snmp snmp = null;
		String queryOid = "";
		List<VariableBinding> tmpList;
		try {
			snmp = openSnmp();

			// "rptLinkUpInfo"
			ArrayList<OBNameValue> linkUpInfoList = new ArrayList<OBNameValue>();
			retVal.setLinkUpInfoList(linkUpInfoList);
			queryOid = oidInfo.getLinkupInfo();
			try {
				tmpList = walk(queryOid);
				for (VariableBinding var : tmpList) {
					String name = parseOidName(var.getOid().toString(), queryOid);
					if ((name != null) && (!name.isEmpty())) {
						OBNameValue value = new OBNameValue();
						value.setName(name);
						value.setValue(var.getVariable().toString());
						linkUpInfoList.add(value);
					}
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get rptLinkUpInfo snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}

			// "rptStatsPhyIfCollisions"
			ArrayList<OBNameValue> linkCollsInfoList = new ArrayList<OBNameValue>();
			retVal.setLinkCollsInfoList(linkCollsInfoList);
			queryOid = oidInfo.getPortColls();
			try {
				tmpList = walk(queryOid);
				for (VariableBinding var : tmpList) {
					String name = parseOidName(var.getOid().toString(), queryOid);
					if ((name != null) && (!name.isEmpty())) {
						OBNameValue value = new OBNameValue();
						value.setName(name);
						value.setValue(var.getVariable().toString());
						linkCollsInfoList.add(value);
					}
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format(
						"failed to get rptStatsPhyIfCollisions snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}

			// "rptPortInfoMode"
			ArrayList<OBNameValue> linkDuplexInfoList = new ArrayList<OBNameValue>();
			retVal.setLinkDuplexInfoList(linkDuplexInfoList);
			queryOid = oidInfo.getPortDuplex();
			try {
				tmpList = walk(queryOid);

				for (VariableBinding var : tmpList) {
					String name = parseOidName(var.getOid().toString(), queryOid);
					if ((name != null) && (!name.isEmpty())) {
						OBNameValue value = new OBNameValue();
						value.setName(name);
						value.setValue(var.getVariable().toString());
						linkDuplexInfoList.add(value);
					}
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get rptPortInfoMode snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}

			// "rptStatsPhyIfErrorsIn"
			ArrayList<OBNameValue> linkErrorInList = new ArrayList<OBNameValue>();
			retVal.setLinkErrorInList(linkErrorInList);
			queryOid = oidInfo.getPortErrorIn();
			try {
				tmpList = walk(queryOid);
				for (VariableBinding var : tmpList) {
					String name = parseOidName(var.getOid().toString(), queryOid);
					if ((name != null) && (!name.isEmpty())) {
						OBNameValue value = new OBNameValue();
						value.setName(name);
						value.setValue(var.getVariable().toString());
						linkErrorInList.add(value);
					}
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String
						.format("failed to get rptStatsPhyIfErrorsIn snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}

			// "rptStatsPhyIfErrorsOut"
			ArrayList<OBNameValue> linkErrorOutInfoList = new ArrayList<OBNameValue>();
			retVal.setLinkErrorOutInfoList(linkErrorOutInfoList);
			queryOid = oidInfo.getPortErrorOut();
			try {
				tmpList = walk(queryOid);
				for (VariableBinding var : tmpList) {
					String name = parseOidName(var.getOid().toString(), queryOid);
					if ((name != null) && (!name.isEmpty())) {
						OBNameValue value = new OBNameValue();
						value.setName(name);
						value.setValue(var.getVariable().toString());
						linkErrorOutInfoList.add(value);
					}
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format(
						"failed to get  rptStatsPhyIfErrorsOut snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}

			// "rptPortInfoSpeed"
			ArrayList<OBNameValue> linkSpeedInfoList = new ArrayList<OBNameValue>();
			retVal.setLinkSpeedInfoList(linkSpeedInfoList);
			queryOid = oidInfo.getPortSpeedInfo();
			try {
				tmpList = walk(queryOid);
				for (VariableBinding var : tmpList) {
					String name = parseOidName(var.getOid().toString(), queryOid);
					if ((name != null) && (!name.isEmpty())) {
						OBNameValue value = new OBNameValue();
						value.setName(name);
						value.setValue(var.getVariable().toString());
						linkSpeedInfoList.add(value);
					}
				}
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String
						.format("failed to get rptPortInfoSpeed snmp data(host:%s, oid:%s)", getHost(), queryOid));
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get snmp data(host:%s, oid:%s)", getHost(), queryOid));
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "general: " + e.getMessage());
		} finally {
			if (snmp != null)
				closeSnmp(snmp);
		}

		return retVal;
	}
}
