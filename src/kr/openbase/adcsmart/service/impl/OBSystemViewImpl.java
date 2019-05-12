package kr.openbase.adcsmart.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.OBSystemView;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoCpu;
import kr.openbase.adcsmart.service.dto.OBDtoDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoHddInfo;
import kr.openbase.adcsmart.service.dto.OBDtoMemory;
import kr.openbase.adcsmart.service.dto.OBDtoUsageCpu;
import kr.openbase.adcsmart.service.dto.OBDtoUsageMem;
import kr.openbase.adcsmart.service.sysmon.OBSysmon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBSystemViewImpl implements OBSystemView {
//	public static void main(String[] args)
//	{
//		try
//		{
//			System.out.println(new OBSystemViewImpl().getUsageCpuList(null, null));
//		}
//		catch(OBException e)
//		{
//			e.printStackTrace();
//		}
//	}
	@Override
	public OBDtoCpu getUsageCpuList(Date beginTime, Date endTime) throws OBException {
		OBDtoCpu result = new OBDtoCpu();
		OBDatabase db = new OBDatabase();

		String sqlText = "";

		try {
			db.openDB();

			ArrayList<OBDtoUsageCpu> data = new ArrayList<OBDtoUsageCpu>();

			sqlText = String.format(" SELECT " + " OCCUR_TIME, CPU_USAGE " + " FROM LOG_SYSTEM_RESOURCES ");
			String sqlTime = "";
			if (endTime == null)
				endTime = new Date();
			sqlTime = String.format(" WHERE OCCUR_TIME <= %s ",
					OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));

			if (beginTime == null) {
				beginTime = new Date(endTime.getTime() - 7 * 24 * 60 * 60 * 1000);// 7일전 시간.
			}

			sqlTime += String.format(" AND OCCUR_TIME >= %s ",
					OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));

			sqlText += sqlTime;
			sqlText += " ORDER BY OCCUR_TIME ASC ";

			ResultSet rs = db.executeQuery(sqlText);

			long minValue = 0x7FFFFFFFL;
			long maxValue = 0;
			long logCnt = 0;
			long totValue = 0;

			if (rs.next() == false) {// 데이터 없는 경우.
				result.setAvgValue(new Long(0));
				result.setMaxValue(new Long(0));
				result.setMinValue(new Long(0));
			} else {
				do {
					OBDtoUsageCpu info = new OBDtoUsageCpu();
					info.setUsage(db.getInteger(rs, "CPU_USAGE"));
					info.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
					data.add(info);
					if (minValue >= info.getUsage()) {
						minValue = info.getUsage();
					}
					if (maxValue <= info.getUsage()) {
						maxValue = info.getUsage();
					}
					totValue += info.getUsage();
					logCnt++;
				} while (rs.next());

				if (totValue > 0)
					result.setAvgValue(totValue / logCnt);
				else
					result.setAvgValue(totValue);
				result.setMaxValue(maxValue);
				result.setMinValue(minValue);
			}

			result.setConfEventList(null); // adcsmart시스템 자체 정보를 뽑는 것이어서 EventList는 의미가 없으므로 뽑지 않는다.
			result.setCpuList(data);

			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			System.out.println(new OBSystemViewImpl().getUsageMemList(null, null));
//		}
//		catch(OBException e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public OBDtoMemory getUsageMemList(Date beginTime, Date endTime) throws OBException {
		OBDtoMemory result = new OBDtoMemory();
		OBDatabase db = new OBDatabase();

		String sqlText = "";

		try {
			db.openDB();
			ArrayList<OBDtoUsageMem> data = new ArrayList<OBDtoUsageMem>();

			sqlText = String.format(
					" SELECT " + " OCCUR_TIME, MEM_TOTAL, MEM_USED, MEM_USAGE " + " FROM LOG_SYSTEM_RESOURCES ");
			String sqlTime = "";
			if (endTime == null)
				endTime = new Date();
			sqlTime = String.format(" WHERE OCCUR_TIME <= %s ",
					OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));

			if (beginTime == null) {
				beginTime = new Date(endTime.getTime() - 7 * 24 * 60 * 60 * 1000);// 7일전 시간.
			}

			sqlTime += String.format(" AND OCCUR_TIME >= %s ",
					OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));

			sqlText += sqlTime;
			sqlText += " ORDER BY OCCUR_TIME ASC ";

			ResultSet rs = db.executeQuery(sqlText);

			long minValue = 0x7FFFFFFFL;
			long maxValue = 0;
			long totValue = 0;

			long minUsage = 0x7FFFFFFFL;
			long maxUsage = 0;
			long totUsage = 0;
			long logCnt = 0;

			if (rs.next() == false) {// 데이터 없는 경우.
				result.setAvgValue(new Long(0));
				result.setMaxValue(new Long(0));
				result.setMinValue(new Long(0));
			} else {
				do {
					OBDtoUsageMem info = new OBDtoUsageMem();
					info.setUsage(db.getInteger(rs, "MEM_USAGE"));
					info.setTotal(db.getLong(rs, "MEM_TOTAL") * 1024);
					info.setUsed(db.getLong(rs, "MEM_USED") * 1024);
					info.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
					data.add(info);

					if (minValue >= info.getUsed()) {
						minValue = info.getUsed();
					}
					if (maxValue <= info.getUsed()) {
						maxValue = info.getUsed();
					}

					if (minUsage >= info.getUsage()) {
						minUsage = info.getUsage();
					}
					if (maxUsage <= info.getUsage()) {
						maxUsage = info.getUsage();
					}

					totValue += info.getUsed();
					totUsage += info.getUsage();
					logCnt++;
				} while (rs.next());

				if (totValue > 0) {
					result.setAvgValue(totValue / logCnt);
				} else {
					result.setAvgValue(totValue);
				}
				result.setMaxValue(maxValue);
				result.setMinValue(minValue);

				if (totUsage > 0) {
					result.setAvgUsage(totUsage / logCnt);
				} else {
					result.setAvgUsage(totUsage);
				}
				result.setMinUsage(minUsage);
				result.setMaxUsage(maxUsage);
			}

			result.setConfEventList(null); // adcsmart시스템 자체 정보를 뽑는 것이어서 EventList는 의미가 없으므로 뽑지 않는다.
			result.setMemList(data);

			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			System.out.println(new OBSystemViewImpl().getUsageHdd());
//		}
//		catch(OBException e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public OBDtoHddInfo getUsageHdd() throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start."));
		OBDtoHddInfo result = new OBDtoHddInfo();
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		String sqlText2 = "";

		try {
			db.openDB();

			sqlText = String.format(" SELECT " + " OCCUR_TIME, HDD_TOTAL, HDD_USED, HDD_USAGE "
					+ " FROM LOG_SYSTEM_RESOURCES " + " ORDER BY OCCUR_TIME DESC " + " LIMIT 1");

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {// 데이터 존재하는 경우.
				result.setHddTotal(db.getLong(rs, "HDD_TOTAL") * 1024);
				result.setHddUsed(db.getLong(rs, "HDD_USED") * 1024);
				result.setHddUsage(db.getLong(rs, "HDD_USAGE"));
				result.setHddFree(result.getHddTotal() - result.getHddUsed());
			} else {
				new OBSysmon().processSystemResc();
				rs = db.executeQuery(sqlText);
				if (rs.next() == true) {
					result.setHddTotal(db.getLong(rs, "HDD_TOTAL") * 1024);
					result.setHddUsed(db.getLong(rs, "HDD_USED") * 1024);
					result.setHddUsage(db.getLong(rs, "HDD_USAGE"));
					result.setHddFree(result.getHddTotal() - result.getHddUsed());
				}
			}

			sqlText2 = String.format(" SELECT " + " HDD_TOTAL, HDD_USED, OCCUR_TIME FROM LOG_SYSTEM_RESOURCES "
					+ " WHERE OCCUR_TIME BETWEEN (current_timestamp - interval '30' day) "
					+ " AND (current_timestamp - interval '30' day  + interval '1' hour) order by occur_time asc limit 1;");
			ResultSet rs2 = db.executeQuery(sqlText2);

			if (rs2.next() == true) {// 데이터 존재하는 경우.
				result.setHddTotal30daysBefore(db.getLong(rs2, "HDD_TOTAL") * 1024);
				result.setHddUsed30daysBefore(db.getLong(rs2, "HDD_USED") * 1024);
			}
			/*
			 * else { new OBSysmon().processSystemResc(); rs2 = db.executeQuery(sqlText2);
			 * if(rs2.next()==true) { result.setHddUsed30days(db.getLong(rs2,
			 * "HDD_TOTAL")*1024); } }
			 */

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

//	private OBDtoHdd getUsageHdd(OBDatabase db) throws OBException
//	{
//		OBDtoHdd result = new OBDtoHdd();
//		
//		String sqlText="";
//
//		try
//		{
//			sqlText = String.format(" SELECT " +
//									" OCCUR_TIME, HDD_TOTAL, HDD_USED, HDD_USAGE " +
//									" FROM LOG_SYSTEM_RESOURCES " +
//									" ORDER BY OCCUR_TIME ASC " +
//									" LIMIT 1");
//			
//			ResultSet rs = db.executeQuery(sqlText);
//			
//			if(rs.next()==true)
//			{// 데이터 없는 경우.
//				result.setHddTotal(db.getLong(rs, "HDD_TOTAL")*1024);
//				result.setHddUsed(db.getLong(rs, "HDD_USED")*1024);
//				result.setHddUsage(db.getLong(rs, "HDD_USAGE"));
//				result.setHddFree(result.getHddTotal()-result.getHddUsed());
//			}
//			return result;
//		}
//		catch(SQLException e)
//		{
//			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sqlText));
//		}		
//		catch(Exception e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			System.out.println(new OBSystemViewImpl().getUsageDatabase());
//		}
//		catch(OBException e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public OBDtoDatabase getUsageDatabase() throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start."));
		OBDtoDatabase result = new OBDtoDatabase();
		OBDatabase db = new OBDatabase();

		String sqlText = "";

		try {
			db.openDB();

			sqlText = String.format(
					" SELECT " + " OCCUR_TIME, HDD_TOTAL, HDD_USED, DB_LOG_USED, DB_GENERAL_USED, DB_INDEX_USED "
							+ " FROM LOG_SYSTEM_RESOURCES " + " ORDER BY OCCUR_TIME DESC " + " LIMIT 1");

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {// 데이터 존재하는 경우.
				result.setTotalDiskSize(db.getLong(rs, "HDD_TOTAL") * 1024);
				result.setTotalUsedSized(db.getLong(rs, "HDD_USED") * 1024);
				result.setGeneralUsed(db.getLong(rs, "DB_GENERAL_USED") * 1024);
				result.setIndexUsed(db.getLong(rs, "DB_INDEX_USED") * 1024);
				result.setLogUsed(db.getLong(rs, "DB_LOG_USED") * 1024);
			}

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}
}
