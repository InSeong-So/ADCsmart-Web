package kr.openbase.adcsmart.service.snmp.system;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.snmp4j.Snmp;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.VariableBinding;

import kr.openbase.adcsmart.service.dto.OBDtoAdcSnmpInfo;
import kr.openbase.adcsmart.service.snmp.OBSnmp;
import kr.openbase.adcsmart.service.snmp.OBSnmpOidDB;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidInfoSystem;
import kr.openbase.adcsmart.service.snmp.dto.DtoSnmpSystemInfo;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBSnmpSystem extends OBSnmp {
//	public static void main(String[] args)
//	{
//		OBSnmpSystem snmp = new OBSnmpSystem(OBSnmp.VERSION_2C, "localhost", "openbase");
//		try
//		{
//			DtoSnmpSystemInfo info = snmp.getSystemInfo();
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//	
	public DtoSnmpSystemInfo getSystemInfo() throws OBException {
		OBSnmpOidDB oidDB = new OBSnmpOidDB();

		DtoOidInfoSystem oidSystemInfo;

		try {
			oidSystemInfo = oidDB.getInfoSystem(OBDefine.ADC_TYPE_F5, "");
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}

		DtoSnmpSystemInfo info = getSystemInfo(oidSystemInfo);
		return info;
	}

	private int getDiskPath(String oid) throws OBException {
		List<VariableBinding> tmpList;
		try {
			// virtual server name
			tmpList = walk(oid);
			for (VariableBinding var : tmpList) {
				ArrayList<Integer> oidList = parseOid(var.getOid().toString(), oid);

				String diskPath = var.getVariable().toString();
				if ((diskPath != null) && (!diskPath.isEmpty())) {
					if (diskPath.compareToIgnoreCase("/var") == 0) {
						return oidList.get(0);
					}
				}
			}
			return 1;
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
	}

	public Long getSysUptimeElapsed() throws OBException {
		Snmp snmp = openSnmp();
		Long ret = 0L;
		try {
			Object obj = getSnmp(snmp, "1.3.6.1.2.1.1.3.0");// uptime oid는 고정 define이다.
			if (obj != null) {
				org.snmp4j.smi.TimeTicks elapseTime = (TimeTicks) obj;
				ret = elapseTime.toLong();
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			closeSnmp(snmp);
		}
		return ret;
	}

	/**
	 * system 기본 정보 추출.
	 * 
	 * @param oid
	 * @return
	 * @throws OBException
	 */
	private DtoSnmpSystemInfo getSystemInfo(DtoOidInfoSystem oid) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. oid:%s.", oid));

		DtoSnmpSystemInfo info = new DtoSnmpSystemInfo();

		String tempOid;
		Snmp snmp = openSnmp();

		try {

			// sysDescr
			tempOid = oid.getSysDescr();
			if ((tempOid != null) && (!tempOid.isEmpty())) {
				Object obj = getSnmp(snmp, tempOid);
				if ((obj != null) && (obj.toString().equalsIgnoreCase("noSuchInstance") == false))
					info.setSysDescr(obj.toString());
			}

			// sysObjectID
			tempOid = oid.getSysObjectID();
			if ((tempOid != null) && (!tempOid.isEmpty())) {
				Object obj = getSnmp(snmp, tempOid);
				if ((obj != null) && (obj.toString().equalsIgnoreCase("noSuchInstance") == false))
					info.setSysObjectID(obj.toString());
			}

			// up time
			tempOid = oid.getSysUpTime();
			if ((tempOid != null) && (!tempOid.isEmpty())) {
				Object obj = getSnmp(snmp, tempOid);
				if (obj != null) {
					org.snmp4j.smi.TimeTicks elapseTime = (TimeTicks) obj;
					Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
					Timestamp upTime = new Timestamp(now.getTime() - elapseTime.toLong());
					info.setUpTime(upTime);
				}
			}

			// cpu used
			tempOid = oid.getSysCpuIdle();
			if ((tempOid != null) && (!tempOid.isEmpty())) {
				Object obj = getSnmp(snmp, tempOid);
				if ((obj != null) && (obj.toString().equalsIgnoreCase("noSuchInstance") == false)) {
					info.setCpuIdle(Integer.parseInt(obj.toString()));
				}
			}

			// mem total
			tempOid = oid.getSysMemTotal();
			if ((tempOid != null) && (!tempOid.isEmpty())) {
				Object obj = getSnmp(snmp, tempOid);
				if ((obj != null) && (obj.toString().equalsIgnoreCase("noSuchInstance") == false))
					info.setMemTotal(Integer.parseInt(obj.toString()));
			}

			// mem avail
			tempOid = oid.getSysMemAvail();
			if ((tempOid != null) && (!tempOid.isEmpty())) {
				Object obj = getSnmp(snmp, tempOid);
				if ((obj != null) && (obj.toString().equalsIgnoreCase("noSuchInstance") == false)) {
					info.setMemAvail(Integer.parseInt(obj.toString()));
				}
			}

			// mem buffered
			tempOid = oid.getSysMemBuffered();
			if ((tempOid != null) && (!tempOid.isEmpty())) {
				Object obj = getSnmp(snmp, tempOid);
				if ((obj != null) && (obj.toString().equalsIgnoreCase("noSuchInstance") == false)) {
					info.setMemBuffered(Integer.parseInt(obj.toString()));
				}
			}

			// mem cached
			tempOid = oid.getSysMemCached();
			if ((tempOid != null) && (!tempOid.isEmpty())) {
				Object obj = getSnmp(snmp, tempOid);
				if ((obj != null) && (obj.toString().equalsIgnoreCase("noSuchInstance") == false)) {
					info.setMemCached(Integer.parseInt(obj.toString()));
				}
			}

			Integer usedMem = info.getMemTotal() - (info.getMemAvail() + info.getMemBuffered() + info.getMemCached());
			info.setMemUsed(usedMem);

			// system model 추출.
			tempOid = oid.getSysModel();
			if ((tempOid != null) && (!tempOid.isEmpty())) {
				Object obj = getSnmp(snmp, tempOid);
				if ((obj != null) && (obj.toString().equalsIgnoreCase("noSuchInstance") == false))
					info.setSysModel(obj.toString());
			}

			int diskIndex = getDiskPath(oid.getSysDiskPath());

			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("diskIndex:%d", diskIndex));

			// system hdd total
			tempOid = oid.getSysHddTotal() + "." + diskIndex;
			if ((tempOid != null) && (!tempOid.isEmpty())) {
				Object obj = getSnmp(snmp, tempOid);
				if ((obj != null) && (obj.toString().equalsIgnoreCase("noSuchInstance") == false))
					info.setHddTotal(Long.parseLong(obj.toString()));
			}

			// system hdd total
			tempOid = oid.getSysHddUsed() + "." + diskIndex;
			if ((tempOid != null) && (!tempOid.isEmpty())) {
				Object obj = getSnmp(snmp, tempOid);
				if ((obj != null) && (obj.toString().equalsIgnoreCase("noSuchInstance") == false))
					info.setHddUsed(Long.parseLong(obj.toString()));
			}
		} catch (Exception e) {
			closeSnmp(snmp);
			throw new OBException(e.getMessage());
		}
		closeSnmp(snmp);
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", info));
		return info;
	}

	public OBSnmpSystem(int version, String host, String rcomm) {
		setSnmpVersion(version);
		setHost(host);
		OBDtoAdcSnmpInfo authInfo = new OBDtoAdcSnmpInfo();
		authInfo.setVersion(version);
		authInfo.setRcomm(rcomm);
		this.setAuthInfo(authInfo);
	}

	public OBSnmpSystem(String host) {
		setSnmpVersion(OBSnmp.VERSION_2C);
		setHost(host);
		OBDtoAdcSnmpInfo authInfo = new OBDtoAdcSnmpInfo();
		authInfo.setVersion(OBSnmp.VERSION_2C);
		authInfo.setRcomm("public");
		this.setAuthInfo(authInfo);
	}
}
