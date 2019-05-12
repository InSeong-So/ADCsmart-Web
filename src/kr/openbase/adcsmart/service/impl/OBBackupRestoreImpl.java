package kr.openbase.adcsmart.service.impl;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import kr.openbase.adcsmart.service.OBBackupRestore;
import kr.openbase.adcsmart.service.OBSystemAudit;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoBackupInfo;
import kr.openbase.adcsmart.service.dto.OBDtoBackupSchedule;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoScheduleBackupInfo;
import kr.openbase.adcsmart.service.impl.alteon.OBAdcSystemInfoAlteon;
import kr.openbase.adcsmart.service.impl.f5.CommonF5;
import kr.openbase.adcsmart.service.impl.f5.SystemF5;
import kr.openbase.adcsmart.service.impl.pas.OBAdcSystemInfoPAS;
import kr.openbase.adcsmart.service.impl.pask.OBAdcSystemInfoPASK;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBNetwork;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBStringFile;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBBackupRestoreImpl implements OBBackupRestore {
//	private static final String [][] TABLE_TYPE_LIST = {{"bigint", "BIGINT"}, {"integer", "INT"}, {"timestamp with time zone", "TIMESTAMP WITH TIME ZONE"}, {"character varying", "VARCHAR"}};

	public String makeLogTalbeSchema(String tbName, long startIndex, OBDatabase db) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. tableName:%s", tbName));
		String result = "";
		String sqlText = "";
		try {
			// primary key 정보를 추출한다.
			HashMap<String, String> primaryKeyMap = getPrimaryKeyInfo(tbName, db);

			if (primaryKeyMap.size() >= 2) {
				OBSystemLog.warn(OBDefine.LOGFILE_DEBUG, String.format("primarykey num:%d", primaryKeyMap.size()));
			}
			String squenceTbName = "";
			sqlText = String.format(
					" SELECT COLUMN_NAME, COLUMN_DEFAULT, IS_NULLABLE, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH "
							+ " FROM information_schema.columns " + " WHERE table_name =%s ",
					OBParser.sqlString(tbName.toLowerCase()));
			ResultSet rs = db.executeQuery(sqlText);

			String column = "";
			int iCnt = 0;
			while (rs.next()) {
				if (iCnt != 0)
					column += ", \n";
				iCnt++;
				// 컬럼 이름 구성.
				String colName = db.getString(rs, "COLUMN_NAME").toUpperCase();
				column += colName + " ";
				// 타입 구성.
				String type = db.getString(rs, "DATA_TYPE");
				String defaultValue = db.getString(rs, "COLUMN_DEFAULT");
				if (defaultValue == null || defaultValue.isEmpty()) {
					column += type + " ";
				} else {
					if (type.equals("bigint") == true) {
						column += "BIGSERIAL ";
						squenceTbName = tbName.toUpperCase() + "_" + colName.toUpperCase() + "_SEQ";
					} else if (type.equals("integer") == true) {
						column += "SERIAL ";
						squenceTbName = tbName.toUpperCase() + "_" + colName.toUpperCase() + "_SEQ";
					} else
						throw new OBException(OBException.ERRCODE_BACKUP_SQLTYPE);// , String.format("Invalid sql
																					// type:%s\n", type));
				}

				// value length 구성.
				int length = db.getInteger(rs, "CHARACTER_MAXIMUM_LENGTH");
				if (length > 0) {
					column += "( " + length + " ) ";
				}

				// 값 초기화 구성.
				if (db.getString(rs, "IS_NULLABLE").equals("NO") == true)
					column += "NOT NULL ";

				// primary key 여부 구성.
				String pKey = primaryKeyMap.get(db.getString(rs, "COLUMN_NAME"));
				if (pKey != null && !pKey.isEmpty()) {
					column += "PRIMARY KEY ";
				}
			}

			if (!squenceTbName.isEmpty())
				result += String.format("DROP SEQUENCE IF EXISTS  %s CASCADE;\n", squenceTbName.toUpperCase());
			result += String.format("DROP TABLE IF EXISTS %s CASCADE;\n", tbName.toUpperCase());
			result += String.format("CREATE TABLE %s ( %s ) WITH ( OIDS=FALSE ) TABLESPACE ADCSMART_LOGS;\n",
					tbName.toUpperCase(), column.toUpperCase());
			result += String.format("ALTER TABLE %s OWNER TO ADCSMARTDBA;\n", tbName.toUpperCase());
			if (!squenceTbName.isEmpty())
				result += String.format("ALTER SEQUENCE %s START WITH %d;\n", squenceTbName.toUpperCase(), startIndex);
		} catch (SQLException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("error:%s, sqlText:%s", e.getMessage(), sqlText));
//			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("error:%s, sqlText:%s", e.getMessage(), sqlText));
//			throw e;
			return null;
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("error:%s, sqlText:%s", e.getMessage(), sqlText));
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			return null;
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	private HashMap<String, String> getPrimaryKeyInfo(String tbName, OBDatabase db) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. tableName:%s", tbName));

		String sqlText = "";
		HashMap<String, String> result = new HashMap<String, String>();
		try {
			sqlText = String.format(
					" SELECT pg_attribute.attname, format_type(pg_attribute.atttypid, pg_attribute.atttypmod) "
							+ " FROM pg_index, pg_class, pg_attribute " + " WHERE pg_class.oid = '%s'::regclass "
							+ " AND indrelid = pg_class.oid " + " AND pg_attribute.attrelid = pg_class.oid "
							+ " AND pg_attribute.attnum = any(pg_index.indkey) " + " AND indisprimary;",
					tbName.toLowerCase());
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				String name = db.getString(rs, "attname");
				result.put(name, name);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	@Override
	public ArrayList<OBDtoBackupInfo> getBackupInfoList(Integer accntIndex, String searchKeys, Date beginTime,
			Date endTime, Integer beginIndex, Integer endIndex) throws OBException {
		ArrayList<OBDtoBackupInfo> result = new ArrayList<OBDtoBackupInfo>();

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			result = getBackupInfoList(accntIndex, searchKeys, beginTime, endTime, beginIndex, endIndex,
					OBDefine.ORDER_TYPE_OCCURTIME, OBDefine.ORDER_DIR_DESCEND, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return result;
	}

	private String getBackupInfoOrderType(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ORDER BY OCCUR_TIME DESC NULLS LAST ";
		switch (orderType) {
		case OBDefine.ORDER_TYPE_OCCURTIME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY OCCUR_TIME ASC NULLS LAST ";
			else
				retVal = " ORDER BY OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_STATUS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY STATUS ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY STATUS DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_NAME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY FILE_NAME ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY FILE_NAME DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
//		case OBDefine.ORDER_TYPE_USERNAME:
//			if(orderDir==OBDefine.ORDER_DIR_ASCEND)
//				retVal = " ORDER BY BEGIN_TIME ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
//			else
//				retVal = " ORDER BY BEGIN_TIME DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
//			break;
		case OBDefine.ORDER_TYPE_OPTION:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY TYPE ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY TYPE DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_SIZE:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY FILE_SIZE ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY FILE_SIZE DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
		}
		return retVal;
	}

	public ArrayList<OBDtoBackupInfo> getBackupInfoList(Integer accntIndex, String searchKeys, Date beginTime,
			Date endTime, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir, OBDatabase db)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format(
						"start. accntIndex:%d, searchKeys:%s, beginTime:%s, endTime:%s, beginIndex:%d, endIndex:%d",
						accntIndex, searchKeys, beginTime, endTime, beginIndex, endIndex));
		String sqlText = "";
		try {
			String sqlTime = "";
			if (beginTime != null) {
				if (endTime == null)
					endTime = new Date();
				sqlTime = String.format(" OCCUR_TIME <= %s AND OCCUR_TIME >= %s ",
						OBParser.sqlString(endTime.toString()), OBParser.sqlString(beginTime.toString()));
			} else {
				if (endTime == null)
					endTime = new Date();
				sqlTime = String.format(" OCCUR_TIME <= %s ", OBParser.sqlString(endTime.toString()));
			}

			String sqlLimit = "";
			if (endIndex != null) {
				int limit = 0;
				int offset = 0;
				if (beginIndex != null) {
					limit = Math.abs(endIndex.intValue() - beginIndex.intValue()) + 1;
					offset = Math.abs(beginIndex.intValue());
				} else {
					limit = endIndex.intValue();
					offset = 0;
				}

				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			} else {
				if (beginIndex != null) {
					sqlLimit = String.format(" LIMIT %d OFFSET %d ", 20, beginIndex.intValue());
				} else {
					sqlLimit = String.format(" LIMIT %d OFFSET %d ", 20, 0);
				}

			}

			String sqlSearch = null;
			if (searchKeys != null && !searchKeys.isEmpty()) {
				String wildcardKey = "%" + searchKeys + "%";
				sqlSearch = String.format(" ( FILE_NAME LIKE %s OR COMMENTS LIKE %s ) ",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			}

			sqlText = String.format(" SELECT "
					+ " INDEX, OCCUR_TIME, STATUS, FILE_NAME, ACCNT_ID, ACCNT_INDEX, COMMENTS, TYPE, FILE_SIZE, STATE "
					+ " FROM MNG_DB_BACKUP " + " WHERE STATE=%d ", OBDefine.STATE_ENABLE);

			if (sqlSearch != null && !sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;

			sqlText += " AND " + sqlTime;
			sqlText += getBackupInfoOrderType(orderType, orderDir);// " ORDER BY OCCUR_TIME DESC ";
			sqlText += sqlLimit;
			sqlText += ";";

			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			ArrayList<OBDtoBackupInfo> result = new ArrayList<OBDtoBackupInfo>();

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoBackupInfo obj = new OBDtoBackupInfo();

				obj.setAccntID(db.getString(rs, "ACCNT_ID"));
				obj.setAccntIndex(db.getInteger(rs, "ACCNT_INDEX"));
				obj.setComments(db.getString(rs, "COMMENTS"));
				obj.setFileName(db.getString(rs, "FILE_NAME"));
				obj.setFileSize(db.getLong(rs, "FILE_SIZE"));
				obj.setIndex(db.getString(rs, "INDEX"));
				if (db.getInteger(rs, "STATE") == 0)
					obj.setLogDelete(true);
				else
					obj.setLogDelete(false);
				obj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				obj.setStatus(db.getInteger(rs, "STATUS"));
				obj.setType(db.getInteger(rs, "TYPE"));
				result.add(obj);
			}
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public Integer getBackupInfoListCount(Integer accntIndex, String searchKeys, Date beginTime, Date endTime)
			throws OBException {
		int result = 0;

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			result = getBackupInfoListCount(accntIndex, searchKeys, beginTime, endTime, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return result;
	}

	public Integer getBackupInfoListCount(Integer accntIndex, String searchKeys, Date beginTime, Date endTime,
			OBDatabase db) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. accntIndex:%d, searchKeys:%s, beginTime:%s, endTime:%s", accntIndex, searchKeys,
						beginTime, endTime));
		String sqlText = "";
		try {
			String sqlTime = "";
			if (beginTime != null) {
				if (endTime == null)
					endTime = new Date();
				sqlTime = String.format(" OCCUR_TIME <= %s AND OCCUR_TIME >= %s ",
						OBParser.sqlString(endTime.toString()), OBParser.sqlString(beginTime.toString()));
			} else {
				if (endTime == null)
					endTime = new Date();
				sqlTime = String.format(" OCCUR_TIME <= %s ", OBParser.sqlString(endTime.toString()));
			}

			String sqlSearch = null;
			if (searchKeys != null && !searchKeys.isEmpty()) {
				String wildcardKey = "%" + searchKeys + "%";
				sqlSearch = String.format(" ( FILE_NAME LIKE %s OR COMMENTS LIKE %s ) ",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			}

			sqlText = String.format(" SELECT COUNT(INDEX) AS CNT " + " FROM MNG_DB_BACKUP " + " WHERE STATE=%d ",
					OBDefine.STATE_ENABLE);

			if (sqlSearch != null && !sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;

			sqlText += " AND " + sqlTime;
//			sqlText += " ORDER BY OCCUR_TIME DESC ";
//			sqlText += " LIMIT 1000 ";
			sqlText += ";";

			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			int result = 0;

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				result = db.getInteger(rs, "CNT");
			}
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public ArrayList<OBDtoBackupSchedule> getBackupScheduleList(Integer accntIndex) throws OBException {
		ArrayList<OBDtoBackupSchedule> backupScheduleList = new ArrayList<OBDtoBackupSchedule>();

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. accntIndex:%d", accntIndex));
		String sql = "SELECT INDEX, STATE, OCCUR_TIME, ACCNT_ID, ACCNT_INDEX, TYPE, LOG_DELETE, \n"
				+ "SCHEDULE_TYPE, SCHEDULE_MINUTE, SCHEDULE_HOUR, SCHEDULE_DAY, SCHEDULE_MONTH, SCHEDULE_DAYWEEK \n"
				+ "FROM MNG_BACKUP_SCHEDULE \n" + "WHERE STATE=" + OBDefine.STATE_ENABLE;

		OBDatabase db = new OBDatabase();
		db.openDB();
		try {
			ResultSet rs = db.executeQuery(sql);
			OBDtoBackupSchedule backupSchedule;
			while (rs.next()) {
				backupSchedule = new OBDtoBackupSchedule();
				backupSchedule.setIndex(rs.getLong("INDEX"));
				backupSchedule.setState(rs.getInt("STATE"));
				backupSchedule.setOccurTime(rs.getTimestamp("OCCUR_TIME"));
				backupSchedule.setAccntId(rs.getString("ACCNT_ID"));
				backupSchedule.setAccntIndex(rs.getInt("ACCNT_INDEX"));
				backupSchedule.setType(rs.getInt("TYPE"));
				int logDelete = rs.getInt("LOG_DELETE");
				backupSchedule.setLogDetete(logDelete == OBDtoBackupSchedule.LOG_DELETE_TRUE ? true : false);
				backupSchedule.setScheduleType(rs.getInt("SCHEDULE_TYPE"));
				backupSchedule.setScheduleMinute(rs.getInt("SCHEDULE_MINUTE"));
				backupSchedule.setScheduleHour(rs.getInt("SCHEDULE_HOUR"));
				backupSchedule.setScheduleDay(rs.getInt("SCHEDULE_DAY"));
				backupSchedule.setScheduleMonth(rs.getInt("SCHEDULE_MONTH"));
				backupSchedule.setScheduleDayweek(rs.getInt("SCHEDULE_DAYWEEK"));
				backupScheduleList.add(backupSchedule);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sql:%s", e.getMessage(), sql));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. backupScheduleList:%s", backupScheduleList));
		return backupScheduleList;
	}

	@Override
	public OBDtoBackupInfo getBackupInfo(String index) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoBackupInfo result;
		try {
			db.openDB();

			result = getBackupInfo(index, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return result;
	}

	public OBDtoBackupInfo getBackupInfo(String index, OBDatabase db) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. index:%s", index));
		String sqlText = "";
		try {
			sqlText = String.format(" SELECT "
					+ " INDEX, OCCUR_TIME, STATUS, FILE_NAME, ACCNT_ID, ACCNT_INDEX, COMMENTS, TYPE, FILE_SIZE, STATE "
					+ " FROM MNG_DB_BACKUP " + " WHERE STATE=%d AND INDEX=%s", OBDefine.STATE_ENABLE,
					OBParser.sqlString(index));

			sqlText += ";";

			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			OBDtoBackupInfo result = new OBDtoBackupInfo();

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				result.setAccntID(db.getString(rs, "ACCNT_ID"));
				result.setAccntIndex(db.getInteger(rs, "ACCNT_INDEX"));
				result.setComments(db.getString(rs, "COMMENTS"));
				result.setFileName(db.getString(rs, "FILE_NAME"));
				result.setFileSize(db.getLong(rs, "FILE_SIZE"));
				result.setIndex(db.getString(rs, "INDEX"));
				if (db.getInteger(rs, "STATE") == 0)
					result.setLogDelete(true);
				else
					result.setLogDelete(false);
				result.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				result.setStatus(db.getInteger(rs, "STATUS"));
				result.setType(db.getInteger(rs, "TYPE"));
				OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
				return result;
			} else {
				OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:null"));
				return null;
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

//	private boolean isExistFileName(String fileName, OBDatabase db) throws OBException
//	{
//		boolean result = false;
//		String sqlText="";
//		try
//		{
//			sqlText = String.format(" SELECT " +
//									 " INDEX " +
//									 " FROM MNG_DB_BACKUP " +
//									 " WHERE STATE=%d AND FILE_NAME=%s", 
//									 OBDefine.STATE_ENABLE,
//									 OBParser.sqlString(fileName));
//			
//			sqlText += ";";
//			
//			ResultSet rs = db.executeQuery(sqlText);
//			if(rs.next()==true)
//			{
//				result = true;
//			}
//		}
//		catch(SQLException e)
//		{
//			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sqlText));
//		}		
//		catch(OBException e)
//		{
//			throw e;
//		}
//		catch(Exception e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//		return result;
//	}

//	private ByteArrayInputStream convertToBinary(Object obj) throws OBException
//	{
//		try
//		{
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			ObjectOutputStream oos = new ObjectOutputStream(bos);
//			oos.writeObject(obj);
//			
//			byte [] bytes = bos.toByteArray();
//			oos.close();
//			bos.close();
//			
//			ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
//			return inputStream;
//		}
//		catch(Exception e)
//		{
//			throw new OBException(e.getMessage());
//		}
//	
//	}

//	private String readFile(String tableName) throws OBException
//	{
//		try 
//		{
//			String result="";
//			String fileName=OBDefine.SQL_DIR+tableName+".sql";
//		    BufferedReader in = new BufferedReader(new FileReader(fileName));
//		    String s;
//
//		    while((s = in.readLine()) != null) 
//		    {
//		    	result += s;//+"\n";
////		        System.out.println(s);
//		    }
//		    in.close();
//		    return result;
//		 } 
//		catch(Exception e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//	}

//	public static void main(String[] args)
//	{
//		OBDatabase db = new OBDatabase();
//		try
//		{
//			db.openDB();
//			String[][] aaaa=OBDefine.LOG_TABLE_LIST;
//			System.out.println(aaaa);
////			new OBBackupRestoreImpl().recreateLogTable(OBDefine.LOG_TALBE_ADC_SYSLOG, db);
//		}
//		catch(Exception e)
//		{
//		}
//	}

	private void recreateLogTable(String tableName, String seqTableName) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. tableName:%s, seqTableName:%s", tableName, seqTableName));
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
//			String sqlSequence="";
			long seqNum = 1;
			if (seqTableName != null && !seqTableName.isEmpty()) {
				sqlText = String.format("SELECT nextval(%s) AS CNT", OBParser.sqlString(seqTableName));
				try {
//					long seqNum=0;
					ResultSet rs = db.executeQuery(sqlText);
					if (rs.next() == true)
						seqNum = db.getLong(rs, "CNT");

//					sqlSequence=String.format("ALTER SEQUENCE %s RESTART WITH %d;", seqTableName, seqNum);
				} catch (Exception e) {
				}
			}
			// create a table.
			sqlText = makeLogTalbeSchema(tableName, seqNum, db);
//			sqlText += sqlSequence;
			if (sqlText != null)
				db.execute(sqlText);
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

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
	}

	// F5 config를 backup을 ADC에서 만들고 download한다.
	private void downloadADCConfigF5(OBDtoAdcInfo adcInfo, String fullPathName) throws OBException {
		// F5 icontrol interface 준비
		iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo);
		SystemF5 system = new SystemF5(interfaces);

		try {
			system.backupConfig(fullPathName);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private void downloadADCConfigPAS(OBDtoAdcInfo adcInfo, String fullPathName) throws OBException {
		try {
			String cfgDump = new OBAdcSystemInfoPAS().getCfgDump(adcInfo.getIndex(), adcInfo.getAdcIpAddress(),
					adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(), adcInfo.getSwVersion(),
					adcInfo.getConnService(), adcInfo.getConnPort());
			OBStringFile.toFile(fullPathName, cfgDump);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private void downloadADCConfigPASK(OBDtoAdcInfo adcInfo, String fullPathName) throws OBException {
		try {
			String cfgDump = new OBAdcSystemInfoPASK().getCfgDump(adcInfo.getIndex(), adcInfo.getAdcIpAddress(),
					adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(), adcInfo.getSwVersion(),
					adcInfo.getConnService(), adcInfo.getConnPort());
			OBStringFile.toFile(fullPathName, cfgDump);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			String cfgDump = new OBAdcSystemInfoAlteon().getCfgDump(3, "192.168.100.11", "admin", "admin", "");
//			OBStringFile.toFile("/opt/aaa.log", cfgDump);
//			System.out.println(cfgDump);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//	
	private void downloadADCConfigAlteon(OBDtoAdcInfo adcInfo, String fullPathName) throws OBException {
		try {
			String cfgDump = new OBAdcSystemInfoAlteon().getCfgDump(adcInfo.getIndex(), adcInfo.getAdcIpAddress(),
					adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(), adcInfo.getSwVersion(),
					adcInfo.getConnService(), adcInfo.getConnPort());
			OBStringFile.toFile(fullPathName, cfgDump);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private void downloadADCConfig(OBDtoAdcInfo adcInfo, String fullPathName, OBDatabase db1, OBDatabase db2)
			throws OBException {
		try {
			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5) {
				downloadADCConfigF5(adcInfo, fullPathName);
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				downloadADCConfigAlteon(adcInfo, fullPathName);
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS) {
				downloadADCConfigPAS(adcInfo, fullPathName);
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK) {
				downloadADCConfigPASK(adcInfo, fullPathName);
			} else {
				throw new OBException(OBException.ERRCODE_NOT_SUPPORT_VENDOR);// , String.format("vendor type:%d",
																				// adcInfo.getAdcType()));
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private void doBackupAdcConfig(OBDtoBackupInfo backupInfo, int percentInterval, OBDtoExtraInfo extraInfo)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. backupInfo:%s", backupInfo));

		OBDatabase db1 = new OBDatabase();
		OBDatabase db2 = new OBDatabase();
		String adcNameIP = new String("");
		boolean isBackupDone = false;
		ArrayList<OBDtoAdcInfo> adcInfoList = new ArrayList<OBDtoAdcInfo>();
		try {
			db1.openDB();
			db2.openDB();

			// 모든 ADC 정보 확인
			adcInfoList = new OBAdcManagementImpl().getAdcInfoList(null, OBDefine.ORDER_TYPE_NAME,
					OBDefine.ORDER_DIR_ASCEND);

			// ADC 하나씩 cfg 백업/download
			for (OBDtoAdcInfo adcInfo : adcInfoList) {
				isBackupDone = true; // exception에 걸리면 false로 바꾼다.

				adcNameIP = adcInfo.getName() + "/" + adcInfo.getAdcIpAddress();
				try {
					String fileName = "/backup/" + backupInfo.getIndex() + "/" + adcInfo.getAdcIpAddress();
					downloadADCConfig(adcInfo, fileName, db1, db2);
				} catch (OBException e) {
					// config 백업 중 한 host에서 오류가 나도 건너뛰고 계속 하려면 exception을 던지지 않는다.
					// db1.closeDB();
					// db2.closeDB();
					// throw e;
					isBackupDone = false; // ADC 각각에 대해 config 백업 성공/실패를 감사로그로 남기려고 체크한다.
				} catch (Exception e) {
					// config 백업 중 한 host에서 오류가 나도 건너뛰고 계속 하려면 exception을 던지지 않는다.
					// db1.closeDB();
					// db2.closeDB();
					// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
					// e.getMessage());
					isBackupDone = false; // ADC 각각에 대해 config 백업 성공/실패를 감사로그로 남기려고 체크한다.
				}

				try {
					// ADC 각각에 대해 config 백업 성공/실패를 감사로그로 남긴다. SDS 넷크루즈 일일 보고서 데이터 중 ADC config 백업
					// 결과를 이 데이터로 확인한다.
					if (isBackupDone == true) {
						new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcInfo.getIndex(),
								"localhost", OBSystemAuditImpl.AUDIT_ENV_ADD_ADC_CONF_BACKUP_SUCCESS, adcNameIP, null);
					} else {
						new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcInfo.getIndex(),
								"localhost", OBSystemAuditImpl.AUDIT_ENV_ADD_ADC_CONF_BACKUP_FAIL, adcNameIP, null);
					}
				} catch (Exception e) {
					// 감사로그 기록에 문제가 생겨도 중지하지는 않는다.
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("Write ADC config backup log failed.(adcInfo:%s", adcNameIP));
				}
			}
		} catch (Exception e) {

		} finally {
			if (db1 != null)
				db1.closeDB();
			if (db2 != null)
				db2.closeDB();
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
	}

	private void doBackupMngTable(OBDtoBackupInfo backupInfo, int percentInterval) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. backupInfo:%s", backupInfo));
		String cmnd = "";
		try {
//			int process=0;
			for (String dbName : OBDefine.MNG_TABLE_LIST) {
				cmnd = String.format(" /opt/adcsmart/scripts/backup.sh %s %s ", dbName, backupInfo.getIndex());
//				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("cmnd:%s", cmnd));
				new OBNetwork().runSystemCommand(cmnd);// throws OBException

				// 진행율 저장.
//				process+=percentInterval;
//				setBackupStatus(backupInfo.getIndex(), STATUS_RUNNING);
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
	}

	private void doBackupLogTable(OBDtoBackupInfo backupInfo, int percentInterval) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. backupInfo:%s", backupInfo));
		String cmnd = "";
		try {
//			int process=0;
			for (String[] dbName : OBDefine.LOG_TABLE_LIST) {
				cmnd = String.format(" /opt/adcsmart/scripts/backup.sh %s %s ", dbName[0], backupInfo.getIndex());
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("cmnd:%s", cmnd));
				new OBNetwork().runSystemCommand(cmnd);// throws OBException

				if (backupInfo.isLogDelete() == true) {
					recreateLogTable(dbName[0].toLowerCase(), dbName[1].toLowerCase());
				}
				// 진행율 저장.
//				process+=percentInterval;
//				setBackupStatus(backupInfo.getIndex(), process);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
	}

//	public static void main(String[] args)
//	{
//		OBDatabase db = new OBDatabase();
//		try
//		{
//			db.openDB();
//			Timestamp nowTime = OBDateTime.toTimestamp(OBDateTime.now());
//			Long time = new Long(nowTime.getTime());
//			
//			OBDtoBackupInfo backupInfo = new OBDtoBackupInfo();
//			backupInfo.setAccntID("admin");
//			backupInfo.setAccntIndex(1);
//			backupInfo.setFileName("test");
//			backupInfo.setIndex(time.toString());
//			backupInfo.setLogDelete(false);
//			backupInfo.setOccurTime(nowTime);
//			backupInfo.setType(1);
//			OBDtoExtraInfo extraInfo = new OBDtoExtraInfo();
//			extraInfo.setAccountIndex(1);
//			new OBBackupRestoreImpl().doBackup(backupInfo, extraInfo);
////			new OBBackupRestoreImpl().getBackupInfoListCount(1, null, null, null, db);
//		}
//		catch(Exception e)
//		{
//		}
//	}

	public static final int STATUS_INIT = 1;
	public static final int STATUS_RUNNING = 2;
	public static final int STATUS_COMPLETE = 3;
	public static final int STATUS_ERROR = 4;

	public static final int BACKUP_ADCSMART_CONFIG_LOG = 0;
	public static final int BACKUP_ADCSMART_CONFIG = 1;
	public static final int BACKUP_ADCSMART_LOG = 2;
	public static final int BACKUP_ADC_CONFIG = 3;

	public void doBackup(OBDtoBackupInfo backupInfo, OBDtoExtraInfo extraInfo) throws OBException {// pg_dump dbname >
																									// outfile
																									// 임시 폴더 생성.
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. backupInfo:%s", backupInfo));
//		String failedHost = new String();
		try {
			setBackupStatus(backupInfo.getIndex(), STATUS_RUNNING);

			new OBNetwork().runSystemCommand(String.format("mkdir /backup/%s", backupInfo.getIndex()));// throws
																										// OBException
			new OBNetwork().runSystemCommand(String.format("rm -rf /backup/%s/*", backupInfo.getIndex()));// throws
																											// OBException
			if (backupInfo.getType().equals(BACKUP_ADCSMART_CONFIG_LOG)) {// ADCSmart 설정&로그 정보
				int percentInterval = 100 / (OBDefine.MNG_TABLE_LIST.length + 1);
				doBackupMngTable(backupInfo, percentInterval);
				doBackupLogTable(backupInfo, percentInterval);
			} else if (backupInfo.getType().equals(BACKUP_ADCSMART_CONFIG)) {// 설정 정보만 백업
				int percentInterval = 100 / (OBDefine.MNG_TABLE_LIST.length + 1);
				doBackupMngTable(backupInfo, percentInterval);
			} else if (backupInfo.getType().equals(BACKUP_ADCSMART_LOG)) {// 로그 정보만 백업.
				int percentInterval = 100 / (OBDefine.LOG_TABLE_LIST.length + 1);
				doBackupLogTable(backupInfo, percentInterval);
			} else if (backupInfo.getType().equals(BACKUP_ADC_CONFIG)) {// 로그 정보만 백업.
				int percentInterval = 100 / (OBDefine.LOG_TABLE_LIST.length + 1);
				doBackupAdcConfig(backupInfo, percentInterval, extraInfo);
			} else {
				throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER);// , String.format("backup option:%d\n",
																			// backupInfo.getType()));
			}

			new OBNetwork().runSystemCommand(String.format("tar -zcvf /backup/%s.tgz -C /backup %s",
					backupInfo.getIndex(), backupInfo.getIndex()));// throws OBException
			new OBNetwork().runSystemCommand(String.format("rm -rf /backup/%s", backupInfo.getIndex()));// throws
																										// OBException

			// 파일 사이즈 계산.
			updateBackupFileSize(backupInfo.getIndex(), backupInfo.getIndex());

			// 감사로그 기록. 이 함수 밖에서 하고 싶지만, 그러면 String을 리턴해야 하기 때문에 여기서 한다.
			if (backupInfo.getType().equals(3)) {
				// 설정정보 백업은 DB 백업이 아니어서 다른 백업들과 메시지가 다르다. 그리고 doBackupAdcConfig() 함수 안에서 각 ADC별로
				// 백업 할 때마다 감사로그를 쓰므로 여기서할 필요가 없다.
			} else {
				String typeMsg = makeDBMsg(backupInfo.getType());
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), "localhost",
						OBSystemAuditImpl.AUDIT_ENV_ADD_DB_BACKUP_SUCCESS, typeMsg);
			}
		} catch (OBException e) {
			setBackupStatus(backupInfo.getIndex(), STATUS_ERROR);
			throw e;
		} catch (Exception e) {
			setBackupStatus(backupInfo.getIndex(), STATUS_ERROR);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		setBackupStatus(backupInfo.getIndex(), STATUS_COMPLETE);

		if (backupInfo.getFtpYN() == OBDtoScheduleBackupInfo.FTP_YES)
			SendFtpFile(backupInfo);

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
		// pg_dump --data-only --table=tablename sourcedb > onetable.pg
		// psql destdb < onetable.pg
		// pg_dump -U myUser myDB | gzip > myDB.sql.gz
		// gzip -cd myDB.sql.gz | pg_restore ...
		// pg_restore -Fc -c -d adcms syslog.pg
		// pg_dump -Ft -C --table=log_adc_syslog adcms > syslog.pg
	}

	private String makeDBMsg(Integer type) {
		String retVal = "";
		switch (type) {
		case 0:
			retVal = OBMessages.getMessage(OBMessages.MSG_SYSTEM_DB_BACKUP_TYPE1);
			break;
		case 1:
			retVal = OBMessages.getMessage(OBMessages.MSG_SYSTEM_DB_BACKUP_TYPE2);
			break;
		case 2:
			retVal = OBMessages.getMessage(OBMessages.MSG_SYSTEM_DB_BACKUP_TYPE3);
			break;
		case 3:
			retVal = OBMessages.getMessage(OBMessages.MSG_SYSTEM_DB_BACKUP_TYPE4);
			break;
		}
		return retVal;
	}

	private void SendFtpFile(OBDtoBackupInfo backupInfo) throws OBException {// 백업 파일을 ftp로 전송한다.
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. backupInfo:%s", backupInfo));
		OBDtoScheduleBackupInfo info;
		info = new OBEnvManagementImpl().getScheduleBackupInfo();
		boolean result = false;
		if (info != null) {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("info:%s", info));
			String fileName = "/backup/" + backupInfo.getFileName() + ".tgz";
			ArrayList<String> fileList = new ArrayList<String>();
			fileList.add(fileName);
			result = new OBFTPTransfer().FtpPut(info.getFtpIPAddress(), info.getFtpPort(), info.getFtpID(),
					info.getFtpPasswd(), "", "", fileList);
			if (result == true) {
				new OBSystemAuditImpl().writeLog(1, "localhost", OBSystemAudit.AUDIT_ENV_SND_SCHBACKUP_SUCCESS,
						backupInfo.getFileName());
			} else {
				new OBSystemAuditImpl().writeLog(1, "localhost", OBSystemAudit.AUDIT_ENV_SND_SCHBACKUP_FAIL,
						backupInfo.getFileName());
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%b", result));
	}

	public void writeBackupInfo(OBDtoBackupInfo info, Thread threadObj, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			String accntID = new OBAccountImpl().getAccountID(info.getAccntIndex());
			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
			sqlText = String.format(" INSERT INTO MNG_DB_BACKUP "
					+ " (INDEX, OCCUR_TIME, STATUS, FILE_NAME, ACCNT_ID, ACCNT_INDEX, COMMENTS, TYPE, FILE_SIZE, STATE) "
					+ " VALUES " + " (%s, %s, %d, %s, %s, %d, %s, %d, %d, %d) ", OBParser.sqlString(info.getIndex()),
					OBParser.sqlString(now), info.getStatus(), OBParser.sqlString(info.getFileName()),
					OBParser.sqlString(accntID), info.getAccntIndex(), OBParser.sqlString(info.getComments()),
					info.getType(), 0, STATUS_INIT);

			sqlText += ";";
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public void addBackup(OBDtoBackupInfo backupInfo, OBDtoExtraInfo extraInfo) throws OBException {// backup 작업은 쓰레드에서
																									// 진행한다.
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. backupInfo:%s", backupInfo));

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			Timestamp nowTime = OBDateTime.toTimestamp(OBDateTime.now());
			Long time = new Long(nowTime.getTime());

			backupInfo.setIndex(time.toString());
			backupInfo.setOccurTime(new Date(nowTime.getTime()));

			writeBackupInfo(backupInfo, null, db);

			Runnable r = new OBBackupWorker(backupInfo, extraInfo);
			Thread t = new Thread(r);

			// Thread 생성...
			t.start();
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
					String.format("starting backup thread. backupInfo:%s", backupInfo));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
	}

	@Override
	public void addBackupSchedule(OBDtoBackupSchedule backupSchedule, OBDtoExtraInfo extraInfo) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. backupSchedule:%s", backupSchedule));
		long beginTime = System.currentTimeMillis();
		OBDatabase db = new OBDatabase();
		db.openDB();
		String sql = String.format(
				"INSERT INTO MNG_BACKUP_SCHEDULE(STATE, OCCUR_TIME, ACCNT_ID, ACCNT_INDEX, TYPE, LOG_DELETE, "
						+ "SCHEDULE_TYPE, SCHEDULE_MINUTE, SCHEDULE_HOUR, SCHEDULE_DAY, SCHEDULE_MONTH, SCHEDULE_DAYWEEK) "
						+ "VALUES(%d, %s, %s, %d, %d, %d, %d, %d, %d, %d, %d, %d)",
				OBDefine.STATE_ENABLE, OBParser.sqlString(OBDateTime.toTimestamp(OBDateTime.now())),
				OBParser.sqlString(new OBAccountImpl().getAccountID(backupSchedule.getAccntIndex())),
				backupSchedule.getAccntIndex(), backupSchedule.getType(),
				backupSchedule.isLogDetete() ? OBDtoBackupSchedule.LOG_DELETE_TRUE
						: OBDtoBackupSchedule.LOG_DELETE_FALSE,
				backupSchedule.getScheduleType(), backupSchedule.getScheduleMinute(), backupSchedule.getScheduleHour(),
				backupSchedule.getScheduleDay(),
				backupSchedule.getScheduleMonth() != null ? backupSchedule.getScheduleMonth() + 1 : null,
				backupSchedule.getScheduleDayweek());
		try {
			db.executeUpdate(sql);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sql:%s", e.getMessage(), sql));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. time:%d", System.currentTimeMillis() - beginTime));
	}

	@Override
	public void delBackup(ArrayList<String> backupList, OBDtoExtraInfo extraInfo) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. backupList:%s", backupList));
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			ArrayList<String> fileList = getBackupFilenames(backupList, db);
			delBackup(backupList, extraInfo, db);
			// 감사로그 생성.
			for (String filename : fileList)
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_REPORT_DELETE_SUCCESS, filename);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
	}

	private ArrayList<String> getBackupFilenames(ArrayList<String> backupList, OBDatabase db) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. backupList:%s", backupList));
		ArrayList<String> result = new ArrayList<String>();
		String sqlText = "";
		try {
			String subSql = "''"; // where-in claus의 empty string 방지
			for (int i = 0; i < backupList.size(); i++) {
				String index = backupList.get(i);
				subSql += ("," + OBParser.sqlString(index));
			}
			sqlText = String.format(
					" SELECT " + " INDEX, FILE_NAME " + " FROM MNG_DB_BACKUP " + " WHERE STATE=%d AND INDEX IN ( %s )", // where-in:empty
																														// string
																														// 불가,
																														// null
																														// 불가,
																														// OK
					OBDefine.STATE_ENABLE, subSql);

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				result.add(db.getString(rs, "FILE_NAME"));
			}
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public void delBackup(ArrayList<String> backupList, OBDtoExtraInfo extraInfo, OBDatabase db) throws OBException {
		if (backupList.size() <= 0)
			return;
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start1. backupList:%s, extraInfo:%s", backupList, extraInfo));
		String sqlText = "";
		try {
			String subSql = "''"; // where-in claus의 empty string 방지
			for (int i = 0; i < backupList.size(); i++) {
				subSql += ("," + OBParser.sqlString(backupList.get(i)));
			}
			sqlText = String.format(" UPDATE MNG_DB_BACKUP SET " + " STATE = %d " + " WHERE INDEX IN (%s) ;", // where-in:empty
																												// string
																												// 불가,
																												// null
																												// 불가,
																												// OK
					OBDefine.STATE_DISABLE, subSql);
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
	}

	@Override
	public void delBackupSchedule(long index, OBDtoExtraInfo extraInfo) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start1. index:%d, extraInfo:%s", index, extraInfo));

		String sql = "UPDATE MNG_BACKUP_SCHEDULE SET STATE = " + OBDefine.STATE_DISABLE + " WHERE INDEX = " + index;
		OBDatabase db = new OBDatabase();
		db.openDB();
		try {
			db.executeUpdate(sql);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sql:%s", e.getMessage(), sql));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "end");
	}

	@Override
	public void restoreBackup(String index, OBDtoExtraInfo extraInfo) throws OBException {
		// TODO Auto-generated method stub
	}

	@Override
	public Integer getBackupStatus(String index) throws OBException {
		OBDatabase db = new OBDatabase();
		int result = 0;
		String sqlText = "";
		try {
			db.openDB();

			sqlText = String.format(" SELECT " + " STATUS " + " FROM MNG_DB_BACKUP " + " WHERE INDEX = %s ;",
					OBParser.sqlString(index));
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true)
				result = db.getInteger(rs, "STATUS");
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
		return result;
	}

	@Override
	public void setBackupStatus(String index, Integer status) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. index:%s, status:%d", index, status));
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		try {
			db.openDB();

			Timestamp nowTime = OBDateTime.toTimestamp(OBDateTime.now());
			if (status.intValue() > 100)
				status = 100;
			sqlText = String.format(
					" UPDATE MNG_DB_BACKUP SET " + " STATUS=%d, UPDATE_TIME=%s " + " WHERE INDEX = %s ;", status,
					OBParser.sqlString(nowTime), OBParser.sqlString(index));
			db.executeUpdate(sqlText);
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
	}

	public void updateBackupFileSize(String index, String fileName) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. index:%s", index));
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			String fullPathName = String.format("/backup/%s.tgz", fileName);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("fileName:%s", fullPathName));
			File oFile = new File(fullPathName);
			long fileSize = 0;
			if (oFile.exists()) {
				fileSize = oFile.length();
			}
			sqlText = String.format(" UPDATE MNG_DB_BACKUP SET " + " FILE_SIZE=%d " + " WHERE INDEX = %s ;", fileSize,
					OBParser.sqlString(index));
			db.executeUpdate(sqlText);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. fileSize:%d", fileSize));
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

	@Override
	public ArrayList<OBDtoBackupInfo> getBackupInfoList(Integer accntIndex, String searchKeys, Date beginTime,
			Date endTime, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir)
			throws OBException {
		ArrayList<OBDtoBackupInfo> result = new ArrayList<OBDtoBackupInfo>();

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			result = getBackupInfoList(accntIndex, searchKeys, beginTime, endTime, beginIndex, endIndex, orderType,
					orderDir, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return result;
	}
}
