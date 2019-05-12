package kr.openbase.adcsmart.service.impl.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.OBReport;
import kr.openbase.adcsmart.service.OBSystemAudit;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcName;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.service.impl.OBAccountImpl;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBSystemAuditImpl;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBReportImpl implements OBReport {
	private ArrayList<OBDtoAdcName> getAdcList(String index) throws OBException {
		ArrayList<OBDtoAdcName> retVal = new ArrayList<OBDtoAdcName>();
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT ADC_INDEX_LIST, ADC_NAME_LIST 	\n"
					+ " FROM LOG_REPORT 						\n" + " WHERE INDEX IN (%s)					\n",
					OBParser.sqlString(index));

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				ArrayList<Integer> adcIndexList = convertAdcIndexList(db.getString(rs, "ADC_INDEX_LIST"));
				ArrayList<OBDtoAdcInfo> adcInfoList = new OBAdcManagementImpl()
						.getBasicAdcInfoListForReport(adcIndexList);
				for (OBDtoAdcInfo adcInfo : adcInfoList) {
					OBDtoAdcName obj = new OBDtoAdcName();
					obj.setIndex(adcInfo.getIndex());
					obj.setIpaddress(adcInfo.getAdcIpAddress());
					obj.setName(adcInfo.getName());
					retVal.add(obj);
				}
//				ArrayList<String> adcNameList = convertAdcNameList(db.getString(rs, "ADC_NAME_LIST"));
//				if(adcIndexList.size() == adcNameList.size())
//				{
//					for(int i=0;i<adcIndexList.size();i++)
//					{
//						OBDtoAdcName adc = new OBDtoAdcName();
//						adc.setIndex(adcIndexList.get(i));
//						adc.setName(adcNameList.get(i));
//						result.add(adc);
//					}
//				}
			}
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
		return retVal;
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			ArrayList<OBReportInfo> list = new OBReportImpl().getReportInfoList(1, 29, null, null, null, null, null);
//			for(OBReportInfo info: list)
//				System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public ArrayList<OBReportInfo> getReportInfoList(Integer adccountIndex, Integer adcIndex, Integer groupIndex,
			String searchKeys, Date beginTime, Date endTime, Integer beginIndex, Integer endIndex) throws OBException {
		return getReportInfoList(adccountIndex, adcIndex, groupIndex, searchKeys, beginTime, endTime, beginIndex,
				endIndex, OBDefine.ORDER_TYPE_OCCURTIME, OBDefine.ORDER_DIR_DESCEND);
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			OBReportInfo list = new OBReportImpl().getReportInfo("12345");
//			System.out.println(list);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public OBReportInfo getReportInfo(String rptIndex) throws OBException {
		OBReportInfo result = new OBReportInfo();
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT INDEX, OCCUR_TIME, STATUS, NAME, BEGIN_TIME, END_TIME, TYPE, ADC_INDEX_LIST, 	\n"
							+ " ADC_NAME_LIST, ACCNT_ID, FILE_NAME, ACCNT_INDEX, FILE_TYPE, EXTRA_INFO  				\n"
							+ " FROM LOG_REPORT 																		\n"
							+ " WHERE INDEX=%s AND AVAILABLE = %d 													\n",
					OBParser.sqlString(rptIndex), OBDefine.STATE_ENABLE);

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				result.setIndex(db.getString(rs, "INDEX"));
				result.setAccountID(db.getString(rs, "ACCNT_ID"));
				result.setAccountIndex(db.getInteger(rs, "ACCNT_INDEX"));
				result.setAdcList(getAdcList(result.getIndex()));
				result.setBeginTime(OBDateTime.toDate(db.getTimestamp(rs, "BEGIN_TIME")));
				result.setEndTime(OBDateTime.toDate(db.getTimestamp(rs, "END_TIME")));
				result.setName(db.getString(rs, "NAME"));
				result.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				result.setStatus(db.getInteger(rs, "STATUS"));
				result.setRptType(db.getInteger(rs, "TYPE"));
				result.setFileName(db.getString(rs, "FILE_NAME"));
				result.setFileType(db.getInteger(rs, "FILE_TYPE"));
				result.setExtraInfo(db.getString(rs, "EXTRA_INFO"));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}
		return result;
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			ArrayList<Integer> list = new OBReportImpl().convertAdcIndexList("'111', '222', '333'");
//			System.out.println(list);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	private ArrayList<Integer> convertAdcIndexList(String indexList) {
		ArrayList<Integer> result = new ArrayList<Integer>();

		if (indexList == null)
			return result;

		String[] indexs = indexList.split(",");
		for (String index : indexs) {
			index = index.replace("\"", "").trim();
			result.add(Integer.parseInt(index));
		}
		return result;
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			ArrayList<String> list = new OBReportImpl().convertAdcNameList("'aaa', 'bbb', 'ccc'");
//			System.out.println(list);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//	private ArrayList<String> convertAdcNameList(String nameList)
//	{//형태: 'aaa', 'bbb', 'ccc'
//		ArrayList<String> result = new ArrayList<String>();
//		
//		if(nameList==null)
//			return result;
//		
//		String [] names = nameList.split(", ");
//		for(String name: names)
//		{
//			name = name.replace("\"", "");
//			result.add(name);
//		}
//		return result;
//	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			System.out.println(new OBReportImpl().getReportInfoListCount(null, 29, null, null, null));
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public Integer getReportInfoListCount(Integer adccountIndex, Integer adcIndex, Integer groupIndex,
			String searchKeys, Date beginTime, Date endTime) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format(
						"start. adccountIndex:%d, adcIndex:%d, groupIndex:%d, searchKeys:%s, beginTime:%s, endTime:%s",
						adccountIndex, adcIndex, groupIndex, searchKeys, beginTime, endTime));

		OBDatabase db = new OBDatabase();

		int result = 0;
		String sqlText = "";
		try {
			db.openDB();

			String sqlSearch = "";
			if (searchKeys != null && !searchKeys.isEmpty()) {
				// #3984-2 #14: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKeys) + "%";
				sqlSearch = String.format(" ( NAME LIKE %s OR ACCNT_ID LIKE %s ) ",

						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			}

			String sqlTime = "";
			if (endTime == null)
				sqlTime = String.format(" OCCUR_TIME <= %s ", OBParser.sqlString(OBDateTime.now()));
			else
				sqlTime = String.format(" OCCUR_TIME <= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));

			if (beginTime != null)
				sqlTime += String.format(" AND OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));

			String sqlAdcKey = null;
			if (groupIndex != null) {
				sqlAdcKey = String.format(" GROUP_INDEX = %s ", groupIndex);
			} else if (adcIndex != null) {
				String adcName = new OBAdcManagementImpl().getAdcName(adcIndex, db);
				sqlAdcKey = String.format(
						" (GROUP_INDEX IS NULL AND ( ADC_INDEX_LIST LIKE '%s' OR ADC_NAME_LIST LIKE '%s' ))", adcIndex,
						adcName);
			}
			sqlText = String.format(" SELECT COUNT(INDEX) AS CNT 	\n" + " FROM LOG_REPORT     			\n"
					+ " WHERE %s AND AVAILABLE=%d 	\n", sqlAdcKey, OBDefine.STATE_ENABLE);

			if (sqlTime != null && !sqlTime.isEmpty())
				sqlText += " AND" + sqlTime;

			if (sqlSearch != null && !sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				result = db.getInteger(rs, "CNT");
				if (result > 10000)
					result = 10000;
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", result));
		return result;
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			ArrayList<OBReportInfo> list = new OBReportImpl().getReportInfoList(1, 29, null, null, null, null, null);
//			for(OBReportInfo info: list)
//				System.out.println(info);
//			
//			new OBReportImpl().setStatus(list.get(0).getIndex(), 20);
//			System.out.println(new OBReportImpl().getStatus(list.get(0).getIndex()));
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public Integer getReportStatus(String index) throws OBException {
		OBDatabase db = new OBDatabase();

		int result = 0;
		String sqlText = "";
		try {
			db.openDB();

			sqlText = String.format("SELECT STATUS FROM LOG_REPORT WHERE INDEX=%s ", OBParser.sqlString(index));

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				result = db.getInteger(rs, "STATUS");
			} else {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("data not found"));
			}
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
	public void updateReportStatus(String index, Integer status) throws OBException {
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		try {
			db.openDB();

			int statusValue = status;
			if (statusValue > 100)
				statusValue = 100;
			if (statusValue < 0)
				statusValue = 0;

			sqlText = String.format(" UPDATE LOG_REPORT " + " SET STATUS=%d " + " WHERE INDEX=%s", statusValue,
					OBParser.sqlString(index));

			sqlText += ";";

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
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			ArrayList<OBReportInfo> list = new OBReportImpl().getReportInfoList(1, 29, null, null, null, null, null);
//			for(OBReportInfo info: list)
//				System.out.println(info);
//			
//			ArrayList<String> indexList = new ArrayList<String>();
//			indexList.add(list.get(0).getIndex());
//			indexList.add(list.get(1).getIndex());
//			OBDtoExtraInfo extraInfo = new OBDtoExtraInfo();
//			extraInfo.setAccountIndex(1);
//			extraInfo.setClientIPAddress("172.172.2.2");			
//			new OBReportImpl().delReport(indexList, extraInfo);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public void delReport(ArrayList<String> indexList, OBDtoExtraInfo extraInfo) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. indexList:%s, extraInfo:%s", indexList, extraInfo));
		OBDatabase db = new OBDatabase();

		String sqlText = "";

		String names = "";
		try {
			db.openDB();

			String sqlIndexs = "''"; // where-in empty 방지
			for (int i = 0; i < indexList.size(); i++) {
				String index = indexList.get(i);
				sqlIndexs += ", ";
				sqlIndexs += OBParser.sqlString(index);
			}

			// 이름 추출
			sqlText = String.format(" SELECT NAME " + " FROM LOG_REPORT " + " WHERE INDEX IN ( %s ) ;", // where-in:empty
																										// string 불가,
																										// null 불가, OK
					sqlIndexs);

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				if (!names.isEmpty())
					names += ", ";
				names += db.getString(rs, "NAME");
			}

			// 삭제
			sqlText = String.format(" UPDATE LOG_REPORT " + " SET AVAILABLE=%d " + " WHERE INDEX IN ( %s ) ;", // where-in:empty
																												// string
																												// 불가,
																												// null
																												// 불가,
																												// OK
					OBDefine.STATE_DISABLE, sqlIndexs);

			db.executeUpdate(sqlText);
			// 감사로그 저장.
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_REPORT_DELETE_SUCCESS, names);
		} catch (SQLException e) {
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_REPORT_DELETE_FAIL, names);
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_REPORT_DELETE_FAIL, names);
			throw e;
		} catch (Exception e) {
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_REPORT_DELETE_FAIL, names);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			OBReportInfo conf = new OBReportInfo();
//			
//			conf.setAccountID("admin01");
//			conf.setAccountIndex(1);
//			ArrayList<OBDtoAdcName> adcList = new ArrayList<OBDtoAdcName>();
//			OBDtoAdcName adcInfo = new OBDtoAdcName();
//			adcInfo.setIndex(29);
//			adcInfo.setName("192.168.100.11");
//			adcList.add(adcInfo);
//			OBDtoAdcName adcInfo2 = new OBDtoAdcName();
//			adcInfo2.setIndex(30);
//			adcInfo2.setName("192.168.200.14");
//			adcList.add(adcInfo2);
//
//			conf.setAdcList(adcList);
//			conf.setBeginTime(new Timestamp(new Date().getTime()-1000000));
//			conf.setEndTime(new Date());
//			conf.setName("test");
//			conf.setType(OBReportInfo.RPT_TYPE_SYSTEM_FAULT);
//			OBDtoExtraInfo extraInfo = new OBDtoExtraInfo();
//			extraInfo.setAccountIndex(1);
//			extraInfo.setClientIPAddress("172.172.2.2");
//			new OBReportImpl().addReport(conf, extraInfo);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	private String makeFileName(OBReportInfo conf, Timestamp time) {
		String prefix = conf.getName(); // 보고서 다운로드시 이름 수정. junhyun.ok_GS

		switch (conf.getFileType()) {
		case OBReportInfo.FILE_TYPE_DOC:
			return OBDefine.REPORT_DIR + "/" + prefix + "_" + time.getTime() + ".doc";
		case OBReportInfo.FILE_TYPE_PDF:
			return OBDefine.REPORT_DIR + "/" + prefix + "_" + time.getTime() + ".pdf";
		case OBReportInfo.FILE_TYPE_XLS:
			return OBDefine.REPORT_DIR + "/" + prefix + "_" + time.getTime() + ".xls";
		case OBReportInfo.FILE_TYPE_PPT:
			return OBDefine.REPORT_DIR + "/" + prefix + "_" + time.getTime() + ".ppt";
		case OBReportInfo.FILE_TYPE_RTF:
			return OBDefine.REPORT_DIR + "/" + prefix + "_" + time.getTime() + ".rtf";
		default:
			return OBDefine.REPORT_DIR + "/" + prefix + "_" + time.getTime() + ".pdf";
		}
	}

//	2012-11-15 09:56:13.191::call::kr.openbase.adcms.service.impl.OBReportImpl::addReport::start. 
//	conf:OBReportInfo [index=null, occurTime=Thu Nov 15 09:56:13 KST 2012, status=null, name=시스템운영보고서_1115095611, beginTime=Wed Nov 14 00:00:00 KST 2012, endTime=Wed Nov 14 23:59:59 KST 2012, 
//	rptType=1, fileType=1, adcList=[OBDtoAdcName [Index=5, name=null]], accountIndex=1, accountID=null, fileName=null], 
//	extraInfo:OBDtoExtraInfo [accountIndex=1, clientIPAddress=172.172.2.2, extraMsg1=시스템운영보고서_1115095611, extraMsg2=null]

//	public static void main(String[] args)
//	{
//		try
//		{
//			OBReportInfo conf= new OBReportInfo();
//			Date date =  new Date();
//			conf.setOccurTime(date);
//			conf.setName("시스템운영보고서_1115095611");
//			SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd"); 
//			Date beginDate = ft.parse("2012-11-14");
//			Date endDate = ft.parse("2012-11-22");			
//			conf.setBeginTime(beginDate);
//			conf.setEndTime(endDate);
//			conf.setRptType(1);
//			conf.setFileType(1);
//			ArrayList<OBDtoAdcName> adcList = new ArrayList<OBDtoAdcName>();
//			
//			OBDtoAdcName adcName = new OBDtoAdcName();
//			adcName.setIndex(5);
//			adcList.add(adcName);
//			conf.setAdcList(adcList);
//			conf.setAccountIndex(1);
//
//			OBDtoExtraInfo extraInfo = new OBDtoExtraInfo();
//			extraInfo.setAccountIndex(1);
//			extraInfo.setClientIPAddress("172.172.2.2");
//			extraInfo.setExtraMsg1("시스템운영보고서_1115095611");
//			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"));
//			String list = new OBReportImpl().addReport(conf, extraInfo);
//			System.out.println(list);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public String addReport(OBReportInfo conf, OBDtoExtraInfo extraInfo) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("11start. conf:%s, extraInfo:%s", conf, extraInfo));
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		String index = "";
		try {
			db.openDB();

			String accntID = new OBAccountImpl().getAccountID(conf.getAccountIndex());

			Timestamp time = OBDateTime.toTimestamp(OBDateTime.now());
			index = "" + time.getTime();

			// 이름 추출.
			String fileName = makeFileName(conf, time);// OBDefine.REPORT_DIR+"/"+time.getTime();
			String adcIndexList = "";
			String adcNameList = "";

			Integer groupIndex = conf.getGroupIndex();
			if (groupIndex != null) {
				ArrayList<OBDtoAdcInfo> adcList = groupIndex == 0
						? new OBAdcManagementImpl().getAdcInfoList(conf.getAccountIndex())
						: new OBAdcManagementImpl().getAdcInfoListInGroup(groupIndex, conf.getAccountIndex());
				for (OBDtoAdcInfo adcInfo : adcList) {
					if (adcInfo.getStatus().equals(0)) {
						continue;
					}

					adcIndexList += adcInfo.getIndex() + ",";
					adcNameList += adcInfo.getName() + ",";
				}
				adcIndexList = adcIndexList.substring(0, adcIndexList.lastIndexOf(","));
				adcNameList = adcNameList.substring(0, adcNameList.lastIndexOf(","));
			} else {
				String indexs = "";
				ArrayList<OBDtoAdcName> adcList = conf.getAdcList();

				for (OBDtoAdcName adcIndex : adcList) {
					indexs += adcIndex.getIndex();
				}
				ArrayList<String> indexsAndNames = new OBAdcManagementImpl().getAdcStatusAndNameList(indexs, db);
				if (indexsAndNames == null || indexsAndNames.isEmpty()) {
					throw new OBException(OBException.ERRCODE_SYSTEM_DATA_NOT_EXIST,
							String.format("report index data is null"));
				}
				adcIndexList = indexsAndNames.get(0);
				adcNameList = indexsAndNames.get(1);
			}

			sqlText = String.format(
					" INSERT INTO  LOG_REPORT 																				\n"
							+ " (INDEX, OCCUR_TIME, STATUS, NAME, BEGIN_TIME, END_TIME, TYPE, 										\n"
							+ " ADC_INDEX_LIST, ADC_NAME_LIST, GROUP_INDEX, ACCNT_ID, ACCNT_INDEX, FILE_NAME, AVAILABLE, FILE_TYPE, EXTRA_INFO ) 	\n"
							+ " VALUES " + " (%s, %s, %d, %s, %s, %s, %d, '%s', '%s', %d, %s, %d, %s, %d, %d, %s) ",
					OBParser.sqlString(index), OBParser.sqlString(time), OBReportInfo.STATUS_INIT,
					OBParser.sqlString(conf.getName()),
					OBParser.sqlString(new Timestamp(conf.getBeginTime().getTime())),
					OBParser.sqlString(new Timestamp(conf.getEndTime().getTime())), conf.getRptType(), adcIndexList,
					adcNameList, groupIndex, OBParser.sqlString(accntID), conf.getAccountIndex(),
					OBParser.sqlString(fileName), OBDefine.STATE_ENABLE, conf.getFileType(),
					OBParser.sqlString(conf.getExtraInfo()));

			sqlText += ";";

			db.executeUpdate(sqlText);
			// 감사로그 생성함.
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_REPORT_ADD_SUCCESS, conf.getName());
		} catch (SQLException e) {
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_REPORT_ADD_FAIL, conf.getName());
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_REPORT_ADD_FAIL, conf.getName());
			throw e;
		} catch (Exception e) {
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_REPORT_ADD_FAIL, conf.getName());
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
		return index;
	}

	@Override
	public ArrayList<OBReportInfo> getReportInfoList(Integer Status) throws OBException {
		OBDatabase db = new OBDatabase();

		ArrayList<OBReportInfo> result = new ArrayList<OBReportInfo>();
		String sqlText = "";
		try {
			db.openDB();

			sqlText = String.format(
					" SELECT A.INDEX, A.OCCUR_TIME, A.STATUS, A.NAME, A.BEGIN_TIME, A.END_TIME, A.TYPE, A.ADC_INDEX_LIST, 	\n"
							+ " A.ADC_NAME_LIST, A.ACCNT_ID, A.FILE_NAME, A.ACCNT_INDEX, A.FILE_TYPE 									\n"
							+ " FROM LOG_REPORT A 																					\n"
							+ " WHERE  A.STATUS=%d AND A.AVAILABLE = %d 																\n",
					Status, OBDefine.STATE_ENABLE);
			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBReportInfo log = new OBReportInfo();

				log.setIndex(db.getString(rs, "INDEX"));
				log.setAccountID(db.getString(rs, "ACCNT_ID"));
				log.setAccountIndex(db.getInteger(rs, "ACCNT_INDEX"));
				log.setAdcList(getAdcList(log.getIndex()));
				log.setBeginTime(OBDateTime.toDate(db.getTimestamp(rs, "BEGIN_TIME")));
				log.setEndTime(OBDateTime.toDate(db.getTimestamp(rs, "END_TIME")));
				log.setName(db.getString(rs, "NAME"));
				log.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				log.setStatus(db.getInteger(rs, "STATUS"));
				log.setRptType(db.getInteger(rs, "TYPE"));
				log.setFileName(db.getString(rs, "FILE_NAME"));
				log.setFileType(db.getInteger(rs, "FILE_TYPE"));
				result.add(log);
			}
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	private String getReportInfoOrderType(Integer orderType, Integer orderDir) throws OBException {
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
		case OBDefine.ORDER_TYPE_PERIOD:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY BEGIN_TIME ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY BEGIN_TIME DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_TYPE:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY TYPE ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY TYPE DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
//		case OBDefine.ORDER_TYPE_ADCNAME:
//			if(orderDir==OBDefine.ORDER_DIR_ASCEND)
//				retVal = " ORDER BY CONTENT ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
//			else
//				retVal = " ORDER BY CONTENT DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
//			break;
//		case OBDefine.ORDER_TYPE_ACCNTNAME:
//			if(orderDir==OBDefine.ORDER_DIR_ASCEND)
//				retVal = " ORDER BY CONTENT ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
//			else
//				retVal = " ORDER BY CONTENT DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
//			break;
		}
		return retVal;
	}

	@Override
	public ArrayList<OBReportInfo> getReportInfoList(Integer adccountIndex, Integer adcIndex, Integer groupIndex,
			String searchKeys, Date beginTime, Date endTime, Integer beginIndex, Integer endIndex, Integer orderType,
			Integer orderDir) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format(
				"start. adccountIndex:%d, adcIndex:%d, groupIndex:%d, searchKeys:%s, beginTime:%s, endTime:%s, beginIndex:%d, endIndex:%d",
				adccountIndex, adcIndex, groupIndex, searchKeys, beginTime, endTime, beginIndex, endIndex));
		OBDatabase db = new OBDatabase();
		ArrayList<OBReportInfo> result = new ArrayList<OBReportInfo>();
		String sqlText = "";
		try {
			db.openDB();

			int offset = 0;
			if (beginIndex != null)
				offset = beginIndex.intValue();

			String sqlLimit = "";
			if (endIndex != null) {
				int limit = Math.abs(endIndex.intValue() - offset) + 1;
				if (limit > 10000)
					limit = 10000;
				sqlLimit = String.format(" LIMIT %d OFFSET %d", limit, offset);
			} else {
				sqlLimit = String.format(" LIMIT %d OFFSET %d", 10000, offset);
			}

			String sqlSearch = "";
			if (searchKeys != null && !searchKeys.isEmpty()) {
				// #3984-2 #14: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKeys) + "%";
				sqlSearch = String.format(" ( A.NAME LIKE %s OR A.ACCNT_ID LIKE %s ) ", OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey));
			}

			String sqlTime = "";
			if (endTime == null)
				sqlTime = String.format(" A.OCCUR_TIME <= %s ", OBParser.sqlString(OBDateTime.now()));
			else
				sqlTime = String.format(" A.OCCUR_TIME <= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));

			if (beginTime != null)
				sqlTime += String.format(" AND A.OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));

			String sqlAdcKey = null;
			if (groupIndex != null) {
				sqlAdcKey = String.format(" A.GROUP_INDEX = %s ", groupIndex);
			} else if (adcIndex != null) {
				String adcName = new OBAdcManagementImpl().getAdcName(adcIndex, db);
				sqlAdcKey = String.format(
						" (A.GROUP_INDEX IS NULL AND ( A.ADC_INDEX_LIST LIKE '%s' OR A.ADC_NAME_LIST LIKE '%s' ))",
						adcIndex, adcName);
			}
			sqlText = String.format(
					" SELECT A.INDEX, A.OCCUR_TIME, A.STATUS, A.NAME, A.BEGIN_TIME, A.END_TIME, A.TYPE, A.ADC_INDEX_LIST, 	\n"
							+ " A.ADC_NAME_LIST, A.ACCNT_ID, A.FILE_NAME, A.ACCNT_INDEX, A.FILE_TYPE 									\n"
							+ " FROM LOG_REPORT A 																					\n"
							+ " WHERE %s AND A.AVAILABLE = %d 																		\n",
					sqlAdcKey, OBDefine.STATE_ENABLE);

			if (sqlTime != null && !sqlTime.isEmpty())
				sqlText += " AND" + sqlTime;

			if (sqlSearch != null && !sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;

			sqlText += getReportInfoOrderType(orderType, orderDir);// " ORDER BY A.OCCUR_TIME DESC ";

			sqlText += sqlLimit;

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBReportInfo log = new OBReportInfo();

				log.setIndex(db.getString(rs, "INDEX"));
				log.setAccountID(db.getString(rs, "ACCNT_ID"));
				log.setAccountIndex(db.getInteger(rs, "ACCNT_INDEX"));
				log.setAdcList(getAdcList(log.getIndex()));
				log.setBeginTime(OBDateTime.toDate(db.getTimestamp(rs, "BEGIN_TIME")));
				log.setEndTime(OBDateTime.toDate(db.getTimestamp(rs, "END_TIME")));
				log.setName(db.getString(rs, "NAME"));
				log.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				log.setStatus(db.getInteger(rs, "STATUS"));
				log.setRptType(db.getInteger(rs, "TYPE"));
				log.setFileName(db.getString(rs, "FILE_NAME"));
				log.setFileType(db.getInteger(rs, "FILE_TYPE"));
				result.add(log);
			}
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}
}
