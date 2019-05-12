/*
 * 시스템 자원 상태를 추출하여 DB에 저장한다.
 */
package kr.openbase.adcsmart.service.sysmon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvAdditional;
import kr.openbase.adcsmart.service.impl.OBEnvManagementImpl;
import kr.openbase.adcsmart.service.snmp.OBSnmp;
import kr.openbase.adcsmart.service.snmp.dto.DtoSnmpSystemInfo;
import kr.openbase.adcsmart.service.snmp.system.OBSnmpSystem;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBSysmon// implements SimpleDaemon
{
	public void writeSystemResc(Timestamp now, DtoSnmpSystemInfo sysInfo, OBDtoDatabase dbUsage) throws OBException
	{
		String sqlText;
		int memUsage=0;
		int hddUsage=0;

		final OBDatabase db = new OBDatabase();
        try
        {
            db.openDB();
			memUsage = (int)((float)sysInfo.getMemUsed()/sysInfo.getMemTotal()*100);
			hddUsage = (int)((float)sysInfo.getHddUsed()/sysInfo.getHddTotal()*100);

			sqlText = String.format(" INSERT INTO " +
									" LOG_SYSTEM_RESOURCES " +
									" (OCCUR_TIME, CPU_USAGE, MEM_TOTAL, MEM_USED, MEM_USAGE, HDD_TOTAL, HDD_USED, HDD_USAGE, DB_LOG_USED, DB_GENERAL_USED, DB_INDEX_USED) " +
									" VALUES " +
									" (%s, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d); ", 
									OBParser.sqlString(now),
									100-sysInfo.getCpuIdle(),
									sysInfo.getMemTotal(),
									sysInfo.getMemUsed(),
									memUsage,
									sysInfo.getHddTotal(), 
									sysInfo.getHddUsed(),
									hddUsage, 
									dbUsage.getLogUsed(),
									dbUsage.getGeneralUsed(),
									dbUsage.getIndexUsed());
			
			OBSystemLog.sql(OBDefine.LOGFILE_ADCMON, String.format("sqlText:%s", sqlText));

			db.executeUpdate(sqlText);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new OBException(e.getMessage());
		}
        finally
        {
            if(db != null) db.closeDB();
        }
	}

	

	public void processSystemResc()
	{
		OBSystemLog.info(OBDefine.LOGFILE_SYSMON, String.format("start."));
		
		try
	    {
		    
		    OBDtoSystemEnvAdditional rCommunity = new OBEnvManagementImpl().getAdditionalConfig();
		
		    OBSnmpSystem snmp = new OBSnmpSystem(OBSnmp.VERSION_2C, "localhost", rCommunity.getSnmpCommunity().getCommunity());
		
		    DtoSnmpSystemInfo sysInfo;
		
			sysInfo = snmp.getSystemInfo();
			// write data into db.
			Timestamp now=OBDateTime.toTimestamp(OBDateTime.now());
			OBDtoDatabase dbUsage = getUsageDatabase();
			writeSystemResc(now, sysInfo, dbUsage);
		}
		catch(Exception e)
		{
			OBSystemLog.error(OBDefine.LOGFILE_SYSMON, String.format("failure to write data into db(%s)", e.getMessage()));
		}
		OBSystemLog.info(OBDefine.LOGFILE_SYSMON, String.format("end."));
	}
	
	private OBDtoDatabase getUsageDatabase()// throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start."));

		OBDtoDatabase retVal = new OBDtoDatabase();
		
		String sqlText="";
		final OBDatabase db = new OBDatabase();
        try
        {
            db.openDB();
			sqlText = String.format(" SELECT PG_TABLESPACE_SIZE(\'adcsmart_logs\') AS LOGS, PG_TABLESPACE_SIZE(\'adcsmart_generals\') AS GENERALS, PG_TABLESPACE_SIZE(\'adcsmart_indexes\') AS INDEXES ");
			
			ResultSet rs = db.executeQuery(sqlText);
			if(rs.next())
			{
				retVal.setLogUsed(db.getLong(rs, "LOGS"));
				if(retVal.getLogUsed()!=0)
					retVal.setLogUsed(retVal.getLogUsed()/1024L);//K 단위로 변환.
				retVal.setGeneralUsed(db.getLong(rs, "GENERALS"));
				if(retVal.getGeneralUsed()!=0)
					retVal.setGeneralUsed(retVal.getGeneralUsed()/1024L);//K 단위로 변환.
				retVal.setIndexUsed(db.getLong(rs, "INDEXES"));
				if(retVal.getIndexUsed()!=0)
					retVal.setIndexUsed(retVal.getIndexUsed()/1024L);//K 단위로 변환.
			}
			
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", retVal));
			return retVal;
		}
		catch(SQLException e)
		{
			OBSystemLog.error(OBDefine.LOGFILE_SYSMON, String.format("failure to get database usage(%s).", e.getMessage()));
		}		
		catch(Exception e)
		{
			OBSystemLog.error(OBDefine.LOGFILE_SYSMON, String.format("failure to get database usage(%s)", e.getMessage()));
		}
        finally
        {
            if(db != null) db.closeDB();
        }
		return retVal;
	}
}
