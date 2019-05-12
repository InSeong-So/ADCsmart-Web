package kr.openbase.adcsmart.service.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;

import kr.openbase.adcsmart.service.OBDynamicDashboard;
import kr.openbase.adcsmart.service.dashboard.dto.OBDtoDashboardInfo;
import kr.openbase.adcsmart.service.dashboard.dto.OBDtoWidgetInfo;
import kr.openbase.adcsmart.service.dashboard.dto.OBDtoWidgetItemInfo;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardStatusNotification;
import kr.openbase.adcsmart.service.dto.OBDtoDataObj;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoGroupHistory;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultBpsConnInfo;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBSystemAuditImpl;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBParser;

public class OBDynamicDashboardImpl implements OBDynamicDashboard // NO_UCD
{
	@Override
	public OBDtoDashboardInfo getDashboardInfo(String indexKey) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDatabase db2 = new OBDatabase();
		OBDatabase db3 = new OBDatabase();

		try {
			db.openDB();
			db2.openDB();
			db3.openDB();

			OBDtoDashboardInfo retVal = getDashboardInfo(indexKey, db, db2, db3);

			return retVal;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
			if (db2 != null)
				db2.closeDB();
			if (db3 != null)
				db3.closeDB();
		}
	}

	private OBDtoDashboardInfo getDashboardInfo(String index, OBDatabase db, OBDatabase db2, OBDatabase db3)
			throws OBException {
		String sqlText = "";
		try {
			Timestamp nowTime = OBDateTime.toTimestamp(OBDateTime.now());
			sqlText = String.format(
					" UPDATE %s                         \n" + " SET ACCESS_TIME=%s               \n"
							+ " WHERE INDEX_KEY = %s             \n",
					OBCommon.makeProperTableName("MNG_DASHBOARD"), OBParser.sqlString(nowTime),
					OBParser.sqlString(index));
			db.executeUpdate(sqlText);

			sqlText = String.format(
					" SELECT INDEX_KEY, OCCUR_TIME, NAME, DESCRIPTION \n"
							+ " FROM %s                                         \n"
							+ " WHERE INDEX_KEY = %s                            \n"
							+ " ORDER BY OCCUR_TIME LIMIT 1                     \n",
					OBCommon.makeProperTableName("MNG_DASHBOARD"), OBParser.sqlString(index));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				OBDtoDashboardInfo retVal = new OBDtoDashboardInfo();
				retVal.setIndexKey(db.getString(rs, "INDEX_KEY"));
				retVal.setName(db.getString(rs, "NAME"));
				retVal.setWidgetList(getWidgetInfoList(retVal.getIndexKey(), db2, db3));
				return retVal;
			}
			return null;
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
	public void addDashboardInfo(OBDtoDashboardInfo info, OBDtoExtraInfo extraInfo) throws OBException {
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
		} catch (OBException e) {
			throw e;
		}

		try {
			addDashboardInfo(info, db);
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_DYNAMIC_DASHBOARD_ADD_SUCCESS, info.getName());
		} catch (OBException e) {
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_DYNAMIC_DASHBOARD_ADD_FAIL, info.getName(), e.getErrorMessage());
			throw e;
		} catch (Exception e) {
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_DYNAMIC_DASHBOARD_ADD_FAIL, info.getName(), e.getMessage());
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	private void addDashboardInfo(OBDtoDashboardInfo info, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			Timestamp occurTime = OBDateTime.toTimestamp(OBDateTime.now());

			Random random = new Random();
			Long base = random.nextLong();
			String dashboardIndex = makeDashboardKey(base.toString(), occurTime);

			sqlText = String.format(
					" INSERT INTO %s                                 \n"
							+ " (INDEX_KEY, OCCUR_TIME, NAME, DESCRIPTION)     \n"
							+ " VALUES (%s, %s, %s, %s)                        \n",
					OBCommon.makeProperTableName("MNG_DASHBOARD"), OBParser.sqlString(dashboardIndex),
					OBParser.sqlString(occurTime), OBParser.sqlString(info.getName()), OBParser.sqlString(""));
			db.executeUpdate(sqlText);

			addWidgetInfoList(dashboardIndex, info.getWidgetList(), db);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

	}

	@Override
	public void delDashboardInfo(String indexKey, OBDtoExtraInfo extraInfo) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDatabase db2 = new OBDatabase();
		OBDatabase db3 = new OBDatabase();
		try {
			db.openDB();
			db2.openDB();
			db3.openDB();

			OBDtoDashboardInfo dashboardInfo = getDashboardInfo(indexKey, db, db2, db3);
			try {
				delDashboardInfo(indexKey, db);
			} catch (OBException e) {
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAuditImpl.AUDIT_DYNAMIC_DASHBOARD_DEL_FAIL, dashboardInfo.getName(),
						e.getErrorMessage());
				throw e;
			}
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_DYNAMIC_DASHBOARD_DEL_SUCCESS, dashboardInfo.getName());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
			if (db2 != null)
				db2.closeDB();
			if (db3 != null)
				db3.closeDB();
		}
	}

	private void delDashboardInfo(String indexKey, OBDatabase db) throws OBException {
		String sqlText = "";
		try {// 테이블 삭제
			sqlText = String.format(" DELETE FROM %s             \n" + " WHERE INDEX_KEY = %s       \n",
					OBCommon.makeProperTableName("MNG_DASHBOARD"), OBParser.sqlString(indexKey));

			db.executeUpdate(sqlText);

			// 위젯 테이블 삭제
			sqlText = String.format(" DELETE FROM %s                   \n" + " WHERE DASHBOARD_INDEX = %s       \n",
					OBCommon.makeProperTableName("MNG_DASHBOARD_WIDGET"), OBParser.sqlString(indexKey));

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public void setDashboardInfo(String indexKey, OBDtoDashboardInfo dashboardInfo, OBDtoExtraInfo extraInfo)
			throws OBException {
		OBDatabase db = new OBDatabase();

		try {
			db.openDB();
			setDashboardInfo(indexKey, dashboardInfo, db);
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_DYNAMIC_DASHBOARD_SET_SUCCESS, dashboardInfo.getName());
		} catch (OBException e) {
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_DYNAMIC_DASHBOARD_SET_FAIL, dashboardInfo.getName(), e.getErrorMessage());
			throw e;
		} catch (Exception e) {
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_DYNAMIC_DASHBOARD_SET_FAIL, dashboardInfo.getName(), e.getMessage());
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	private void setDashboardInfo(String indexKey, OBDtoDashboardInfo dashboardInfo, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			String setText = "";
			if ((dashboardInfo.getName() != null) && (!dashboardInfo.getName().isEmpty())) {
				if (!setText.isEmpty())
					setText += ", ";
				setText += String.format("NAME=%s", OBParser.sqlString(dashboardInfo.getName()));
			}

			// 대시보드 테이블 구성.
			sqlText = String.format(
					" UPDATE %s                         \n" + " SET %s                            \n"
							+ " WHERE INDEX_KEY = %s              \n",
					OBCommon.makeProperTableName("MNG_DASHBOARD"), setText, OBParser.sqlString(indexKey));

			db.executeUpdate(sqlText);

			// 위젯 테이블 구성.
			sqlText = String.format(" DELETE FROM %s                   \n" + " WHERE DASHBOARD_INDEX = %s       \n",
					OBCommon.makeProperTableName("MNG_DASHBOARD_WIDGET"), OBParser.sqlString(indexKey));

			db.executeUpdate(sqlText);

			addWidgetInfoList(indexKey, dashboardInfo.getWidgetList(), db);
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
	public ArrayList<OBDtoDashboardInfo> getDashboardInfoList() throws OBException {
		OBDatabase db = new OBDatabase();
		OBDatabase db2 = new OBDatabase();
		OBDatabase db3 = new OBDatabase();

		ArrayList<OBDtoDashboardInfo> retVal = new ArrayList<OBDtoDashboardInfo>();
		try {
			db.openDB();
			db2.openDB();
			db3.openDB();

			retVal = getDashboardInfoList(0, db, db2, db3);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
			if (db2 != null)
				db2.closeDB();
			if (db3 != null)
				db3.closeDB();
		}
		return retVal;
	}

	private ArrayList<OBDtoDashboardInfo> getDashboardInfoList(Integer count, OBDatabase db, OBDatabase db2,
			OBDatabase db3) throws OBException {
		ArrayList<OBDtoDashboardInfo> retVal = new ArrayList<OBDtoDashboardInfo>();
		String sqlText = "";
		try {
			sqlText = String.format(
					" SELECT INDEX_KEY, OCCUR_TIME, NAME, DESCRIPTION \n"
							+ " FROM %s     								    \n"
							+ " WHERE INDEX >= %d               		        \n"
							+ " ORDER BY NAME ASC                               \n",
					OBCommon.makeProperTableName("MNG_DASHBOARD"), OBDefine.DEFAULT_DASHBOARD_KEY);
			if (count != null && count.intValue() > 0)
				sqlText += String.format(" LIMIT %d ", count);

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoDashboardInfo info = new OBDtoDashboardInfo();

				info.setIndexKey(db.getString(rs, "INDEX_KEY"));
				info.setName(db.getString(rs, "NAME"));
				info.setWidgetList(getWidgetInfoList(info.getIndexKey(), db2, db3));

				retVal.add(info);
			}

			sqlText = String.format(
					" SELECT INDEX_KEY, OCCUR_TIME, NAME, DESCRIPTION \n"
							+ " FROM %s                                         \n"
							+ " WHERE INDEX < %d								\n"
							+ "ORDER BY OCCUR_TIME                              \n",
					OBCommon.makeProperTableName("MNG_DASHBOARD"), OBDefine.DEFAULT_DASHBOARD_KEY);
			if (count != null && count.intValue() > 0)
				sqlText += String.format(" LIMIT %d ", count);

			ResultSet rs2 = db.executeQuery(sqlText);
			while (rs2.next()) {
				OBDtoDashboardInfo info = new OBDtoDashboardInfo();

				info.setIndexKey(db.getString(rs2, "INDEX_KEY"));
				info.setName(db.getString(rs2, "NAME"));
				info.setWidgetList(getWidgetInfoList(info.getIndexKey(), db2, db3));

				retVal.add(info);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private String getTargetObjName(Integer category, Integer index, String strIndex, OBDatabase db)
			throws OBException {
		String retVal = "";

		String sqlText = "";
		try {
			if (category == OBDtoADCObject.CATEGORY_ALL) {
				return OBMessages.getMessage(OBMessages.MSG_SYSTEM_ALL_ADC);
			} else if (category == OBDtoADCObject.CATEGORY_GROUP) {
				sqlText = String.format(" SELECT NAME AS NAME          \n" + " FROM MNG_ADC_GROUP           \n"
						+ " WHERE INDEX = %d             \n", index);
			} else if (category == OBDtoADCObject.CATEGORY_ADC) {
				sqlText = String.format(" SELECT NAME AS NAME          \n" + " FROM MNG_ADC                 \n"
						+ " WHERE INDEX = %d             \n", index);
			} else if (category == OBDtoADCObject.CATEGORY_VS) {
				sqlText = String.format(" SELECT VIRTUAL_IP AS NAME    \n" + " FROM TMP_SLB_VSERVER         \n"
						+ " WHERE INDEX = %s             \n", OBParser.sqlString(strIndex));
			} else if (category == OBDtoADCObject.CATEGORY_VSVC) {
				sqlText = String.format(" SELECT B.VIRTUAL_IP AS NAME               \n"
						+ " FROM TMP_SLB_VS_SERVICE  A                \n"
						+ " INNER JOIN TMP_SLB_VSERVER B              \n"
						+ " ON B.INDEX = A.VS_INDEX AND A.INDEX = %s  \n", OBParser.sqlString(strIndex));
			} else if (category == OBDtoADCObject.CATEGORY_SERVICEGROUP) {
				sqlText = String.format(" SELECT NAME AS NAME          \n" + " FROM MNG_VSSERVICE_GROUP           \n"
						+ " WHERE INDEX = %d             \n", index);
			} else if (category == OBDtoADCObject.CATEGORY_VS) {
				return retVal;
			}

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal = db.getString(rs, "NAME");
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private ArrayList<OBDtoWidgetInfo> getWidgetInfoList(String dashboardIndex, OBDatabase db, OBDatabase db2)
			throws OBException {
		ArrayList<OBDtoWidgetInfo> retVal = new ArrayList<OBDtoWidgetInfo>();

		String sqlText = "";
		try {
			sqlText = String.format(
					" SELECT INDEX_KEY, DASHBOARD_INDEX, NAME, DESCRIPTION,                     \n"
							+ " TARGET_TYPE, TARGET_INDEX,                                                \n"
							+ " WIDGET_TYPE, WIDGET_WIDTH, WIDGET_HEIGHT,                                 \n"
							+ " WIDGET_MIN_WIDTH, WIDGET_MAX_WIDTH, WIDGET_MIN_HEIGHT, WIDGET_MAX_HEIGHT, \n"
							+ " POS_X_AXIS, POS_Y_AXIS, MORE_INFO_INDEX, EXTRA_INFO                       \n"
							+ " FROM %s                                                                   \n"
							+ " WHERE DASHBOARD_INDEX = %s                                                \n",
					OBCommon.makeProperTableName("MNG_DASHBOARD_WIDGET"), OBParser.sqlString(dashboardIndex));
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoWidgetInfo info = new OBDtoWidgetInfo();
				info.setIndex(db.getString(rs, "INDEX_KEY"));
				info.setName(db.getString(rs, "NAME"));
				info.setType(db.getInteger(rs, "WIDGET_TYPE"));
				info.setWidth(db.getInteger(rs, "WIDGET_WIDTH"));
				info.setHeight(db.getInteger(rs, "WIDGET_HEIGHT"));
				info.setxPosition(db.getInteger(rs, "POS_X_AXIS"));
				info.setyPosition(db.getInteger(rs, "POS_Y_AXIS"));
				info.setHeightMax(db.getInteger(rs, "WIDGET_MAX_HEIGHT"));
				info.setHeightMin(db.getInteger(rs, "WIDGET_MIN_HEIGHT"));
				info.setWidthMax(db.getInteger(rs, "WIDGET_MAX_WIDTH"));
				info.setWidthMin(db.getInteger(rs, "WIDGET_MIN_WIDTH"));
				info.setMoreInfoIndex(db.getInteger(rs, "MORE_INFO_INDEX"));
				OBDtoADCObject object = new OBDtoADCObject();
				object.setCategory(db.getInteger(rs, "TARGET_TYPE"));
				String objIndex = db.getString(rs, "TARGET_INDEX");
//				if(object.getCategory()<=OBDtoADCObject.CATEGORY_ADC)
				if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL
						|| object.getCategory() == OBDtoADCObject.CATEGORY_GROUP
						|| object.getCategory() == OBDtoADCObject.CATEGORY_ADC
						|| object.getCategory() == OBDtoADCObject.CATEGORY_SERVICEGROUP)

					object.setIndex(Integer.parseInt(objIndex));
				else
					object.setIndex(0);
				object.setStrIndex(objIndex);
				object.setName(getTargetObjName(object.getCategory(), object.getIndex(), object.getStrIndex(), db2));
				object.setDesciption(db.getString(rs, "EXTRA_INFO"));
				info.setTargetObj(object);

				retVal.add(info);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

//	private long makeWidgetkKey(String dashboardIndexStr, Timestamp occurTime) throws OBException
//	{
//		long currentTime = occurTime.getTime();
//		long retVal = 0L;
//		try
//		{
//			long dashboardIndex = Long.parseLong(dashboardIndexStr);
//			currentTime = currentTime & 0xffffffffffL;
//			long temp = (long)dashboardIndex*0x10000000000L;
//			retVal = temp|currentTime;
//		}
//		catch(Exception e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}	
//		return retVal;
//	}

	private String makeDashboardKey(String baseIndexStr, Timestamp occurTime) throws OBException {
		long currentTime = occurTime.getTime();
		String retVal = "0";
		try {
			long baseIndex = Long.parseLong(baseIndexStr);
			currentTime = currentTime & 0xffffffffffL;
			long temp = (long) baseIndex * 0x10000000000L;
			Long tempRetval = temp | currentTime;
			retVal = tempRetval.toString();
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private void addWidgetInfoList(String dashboardIndex, ArrayList<OBDtoWidgetInfo> widgetList, OBDatabase db)
			throws OBException {
		String sqlText = "";
		try {
			String subText = "";

			Integer index = 1;
			for (OBDtoWidgetInfo widget : widgetList) {
				if (!subText.isEmpty())
					subText += ", ";
				Timestamp occurTime = new Timestamp(index++);

				String widgetKey = makeDashboardKey(dashboardIndex, occurTime);

//				if(widget.getTargetObj().getCategory()<=OBDtoADCObject.CATEGORY_ADC)
				if (widget.getTargetObj().getCategory() == OBDtoADCObject.CATEGORY_ALL
						|| widget.getTargetObj().getCategory() == OBDtoADCObject.CATEGORY_GROUP
						|| widget.getTargetObj().getCategory() == OBDtoADCObject.CATEGORY_ADC
						|| widget.getTargetObj().getCategory() == OBDtoADCObject.CATEGORY_SERVICEGROUP) {
					subText += String.format(" (%s, %s, %s, %s, %d, %s, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %s) ",
							OBParser.sqlString(widgetKey), OBParser.sqlString(dashboardIndex),
							OBParser.sqlString(widget.getName()), OBParser.sqlString(""),
							widget.getTargetObj().getCategory(),
							OBParser.sqlString(widget.getTargetObj().getIndex().toString()), widget.getType(),
							widget.getWidth(), widget.getHeight(), widget.getWidthMin(), widget.getWidthMax(),
							widget.getHeightMin(), widget.getHeightMax(), widget.getxPosition(), widget.getyPosition(),
							widget.getMoreInfoIndex(), OBParser.sqlString(widget.getTargetObj().getDesciption()));
				} else {
					subText += String.format(" (%s, %s, %s, %s, %d, %s, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %s) ",
							OBParser.sqlString(widgetKey), OBParser.sqlString(dashboardIndex),
							OBParser.sqlString(widget.getName()), OBParser.sqlString(""),
							widget.getTargetObj().getCategory(),
							OBParser.sqlString(widget.getTargetObj().getStrIndex()), widget.getType(),
							widget.getWidth(), widget.getHeight(), widget.getWidthMin(), widget.getWidthMax(),
							widget.getHeightMin(), widget.getHeightMax(), widget.getxPosition(), widget.getyPosition(),
							widget.getMoreInfoIndex(), OBParser.sqlString(widget.getTargetObj().getDesciption()));
				}
			}
			if (subText.isEmpty())
				return;

			sqlText = String.format(
					" INSERT INTO %s                                                            \n"
							+ " (INDEX_KEY, DASHBOARD_INDEX, NAME, DESCRIPTION,                           \n"
							+ " TARGET_TYPE, TARGET_INDEX,                                                \n"
							+ " WIDGET_TYPE, WIDGET_WIDTH, WIDGET_HEIGHT,                                 \n"
							+ " WIDGET_MIN_WIDTH, WIDGET_MAX_WIDTH, WIDGET_MIN_HEIGHT, WIDGET_MAX_HEIGHT, \n"
							+ " POS_X_AXIS, POS_Y_AXIS, MORE_INFO_INDEX, EXTRA_INFO)                      \n"
							+ " VALUES %s                                                                 \n",
					OBCommon.makeProperTableName("MNG_DASHBOARD_WIDGET"), subText);
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private final static Integer LAST_MAX_COUNT = 3;

	@Override
	public ArrayList<OBDtoDashboardInfo> getDashboardInfoLastList() throws OBException {
		OBDatabase db = new OBDatabase();
		OBDatabase db2 = new OBDatabase();
		OBDatabase db3 = new OBDatabase();

		ArrayList<OBDtoDashboardInfo> retVal = new ArrayList<OBDtoDashboardInfo>();
		try {
			db.openDB();
			db2.openDB();
			db3.openDB();
			retVal = getDashboardInfoLastList(LAST_MAX_COUNT, db, db2, db3);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
			if (db2 != null)
				db2.closeDB();
			if (db3 != null)
				db3.closeDB();
		}
		return retVal;
	}

	private ArrayList<OBDtoDashboardInfo> getDashboardInfoLastList(Integer count, OBDatabase db, OBDatabase db2,
			OBDatabase db3) throws OBException {
		ArrayList<OBDtoDashboardInfo> retVal = new ArrayList<OBDtoDashboardInfo>();

		String sqlText = "";
		try {
			sqlText = String.format(
					" SELECT INDEX_KEY, ACCESS_TIME, NAME, DESCRIPTION \n"
							+ " FROM %s                                          \n"
							+ " ORDER BY ACCESS_TIME DESC                        \n",
					OBCommon.makeProperTableName("MNG_DASHBOARD"));
			if (count != null && count.intValue() > 0)
				sqlText += String.format(" LIMIT %d ", count);

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoDashboardInfo info = new OBDtoDashboardInfo();

				info.setIndexKey(db.getString(rs, "INDEX_KEY"));
				info.setName(db.getString(rs, "NAME"));
				info.setWidgetList(getWidgetInfoList(info.getIndexKey(), db2, db3));

				retVal.add(info);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDtoADCObject target = new OBDtoADCObject();
////			target.setCategory(OBDtoADCObject.CATEGORY_ALL);
////			target.setCategory(OBDtoADCObject.CATEGORY_GROUP);
//			target.setCategory(OBDtoADCObject.CATEGORY_ADC);
//			target.setIndex(1);
//			target.setVendor(OBDefine.ADC_TYPE_ALTEON);
//			ArrayList<OBDtoADCObject> retVal = new OBDynamicDashboardImpl().getInvolvedObjectList(target, null);
//			for(OBDtoADCObject object: retVal)
//				System.out.println(object);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public ArrayList<OBDtoADCObject> getInvolvedObjectList(OBDtoADCObject target, OBDtoExtraInfo extraInfo)
			throws OBException {
		ArrayList<OBDtoADCObject> retVal = new ArrayList<OBDtoADCObject>();

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();
			retVal = getInvolvedObjectList(target, extraInfo, db);
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

	@Override
	public ArrayList<OBDtoADCObject> getInvolvedVsList(OBDtoADCObject target, OBDtoExtraInfo extraInfo)
			throws OBException {
		ArrayList<OBDtoADCObject> retVal = new ArrayList<OBDtoADCObject>();

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();
			retVal = getInvolvedObjectList(target, extraInfo, db);
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

	@Override
	public ArrayList<OBDtoADCObject> getInvolvedMemberList(OBDtoADCObject target, OBDtoExtraInfo extraInfo)
			throws OBException {
		ArrayList<OBDtoADCObject> retVal = new ArrayList<OBDtoADCObject>();

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();
			retVal = getInvolvedObjectList(target, extraInfo, db);
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

	private ArrayList<OBDtoADCObject> getInvolvedObjectList(OBDtoADCObject target, OBDtoExtraInfo extraInfo,
			OBDatabase db) throws OBException {
		ArrayList<OBDtoADCObject> retVal = new ArrayList<OBDtoADCObject>();

		if (target.getCategory() == OBDtoADCObject.CATEGORY_ALL) {// 그룹 목록을 제공한다.
			retVal = getInvolvedObjectListAllGroup(target, extraInfo, db);
		} else if (target.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {// 지정된 그룹에 포함된 ADC 목록을 제공한다.
			retVal = getInvolvedObjectListGroup(target, extraInfo, db);
		} else if (target.getCategory() == OBDtoADCObject.CATEGORY_ADC) {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(target.getIndex());// .getAdcType(adcIndex, db);
			target.setVendor(adcInfo.getAdcType());
			retVal = getInvolvedObjectListADC(target, extraInfo, db);
		} else if (target.getCategory() == OBDtoADCObject.CATEGORY_MEMBER) {
			retVal = new OBDynamicDashboardDB().getADCVSMemberList(target, db);
		} else if (target.getCategory() == OBDtoADCObject.CATEGORY_SERVICEGROUP) {
			retVal = getInvolvedObjectSvcListGroup(target, extraInfo, db);
//		    retVal = getInvolvedObjectListSvcGroup(target, extraInfo, db);
		} else if (target.getCategory() == OBDtoADCObject.CATEGORY_SERVICEGROUPVSLIST) {
			retVal = new OBDynamicDashboardDB().getServiceGroupVSList(target, db);
		} else if (target.getCategory() == OBDtoADCObject.CATEGORY_SERVICEGROUPMEMBERLIST) {
			retVal = new OBDynamicDashboardDB().getServiceGroupMemberList(target, db);
		} else {// 하위 객체는 없다.
//		    retVal = getInvolvedObjectSvcListGroup(target, extraInfo, db);
//            retVal = getInvolvedObjectListSvcGroup(target, extraInfo, db);
		}
		return retVal;
	}

	private ArrayList<OBDtoADCObject> getInvolvedObjectListAllGroup(OBDtoADCObject target, OBDtoExtraInfo extraInfo,
			OBDatabase db) throws OBException {
		ArrayList<OBDtoADCObject> retVal = new ArrayList<OBDtoADCObject>();
		String sqlText = "";
		try {
			sqlText = String.format(
					" SELECT INDEX, NAME, DESCRIPTION   \n" + " FROM MNG_ADC_GROUP                \n"
							+ " WHERE AVAILABLE = %d              \n" + " ORDER BY NAME ASC                 \n",
					OBDefine.DATA_AVAILABLE);
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoADCObject log = new OBDtoADCObject();

				log.setCategory(OBDtoADCObject.CATEGORY_GROUP);
				log.setDesciption(db.getString(rs, "DESCRIPTION"));
				log.setIndex(db.getInteger(rs, "INDEX"));
				log.setName(db.getString(rs, "NAME"));

				retVal.add(log);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private ArrayList<OBDtoADCObject> getInvolvedObjectSvcListGroup(OBDtoADCObject target, OBDtoExtraInfo extraInfo,
			OBDatabase db) throws OBException {
		ArrayList<OBDtoADCObject> retVal = new ArrayList<OBDtoADCObject>();
		String sqlText = "";
		try {
			sqlText = String.format(" SELECT INDEX, NAME FROM MNG_VSSERVICE_GROUP ");
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoADCObject log = new OBDtoADCObject();

				log.setCategory(OBDtoADCObject.CATEGORY_SERVICEGROUP);
//              log.setDesciption(db.getString(rs, "DESCRIPTION"));
				log.setIndex(db.getInteger(rs, "INDEX"));
				log.setName(db.getString(rs, "NAME"));
//              log.setVendor(db.getInteger(rs, "TYPE"));

				retVal.add(log);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

//	private ArrayList<OBDtoADCObject> getInvolvedObjectListSvcGroup(OBDtoADCObject target, OBDtoExtraInfo extraInfo,
//			OBDatabase db) throws OBException {
//		ArrayList<OBDtoADCObject> retVal = new ArrayList<OBDtoADCObject>();
//		String sqlText = "";
//		try {
////			sqlText = String.format(" SELECT INDEX, NAME, VS_INDEX FROM MNG_VSSERVICE_GROUP          \n"+
////			                        " WHERE NAME = %d              \n" +
////			                        " ORDER BY NAME ASC                                      \n",
////			                         target.getIndex(), 
////			                         OBDefine.DATA_AVAILABLE);
////			ResultSet rs = db.executeQuery(sqlText);
////			while(rs.next())
////			{
////				OBDtoADCObject log = new OBDtoADCObject();
////				
////				log.setCategory(OBDtoADCObject.CATEGORY_GROUP);
//////				log.setDesciption(db.getString(rs, "DESCRIPTION"));
//////				log.setIndex(db.getInteger(rs, "INDEX"));
////				log.setName(db.getString(rs, "NAME"));
//////				log.setVendor(db.getInteger(rs, "TYPE"));
////				
////				retVal.add(log);
////			}
//
//			sqlText = String.format(" SELECT A.INDEX AS INDEX, A.NAME AS NAME, A.VS_INDEX AS VS_INDEX,          \n"
//					+ " B.INDEX AS VSS_INDEX, B.NAME AS VS_NAME                                                   \n"
//					+ " FROM (SELECT INDEX, NAME, VS_INDEX FROM MNG_VSSERVICE_GROUP WHERE NAME = %s ) A           \n"
//					+ " LEFT JOIN (SELECT * FROM TMP_SLB_VSERVER) B                                               \n"
//					+ " ON A.VS_INDEX = B.INDEX;                                                                  ",
//					"testA");
//			ResultSet rs = db.executeQuery(sqlText);
//
//			while (rs.next()) {
//				OBDtoADCObject log = new OBDtoADCObject();
//
//				log.setCategory(OBDtoADCObject.CATEGORY_GROUP);
////	                log.setDesciption(db.getString(rs, "DESCRIPTION"));
////	                log.setIndex(db.getInteger(rs, "INDEX"));
//				log.setName(db.getString(rs, "NAME"));
////	                log.setVendor(db.getInteger(rs, "TYPE"));
//
//				retVal.add(log);
//			}
//		} catch (SQLException e) {
//			throw new OBException(OBException.ERRCODE_DB_QEURY,
//					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
//		} catch (Exception e) {
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//		return retVal;
//	}

	private ArrayList<OBDtoADCObject> getInvolvedObjectListGroup(OBDtoADCObject target, OBDtoExtraInfo extraInfo,
			OBDatabase db) throws OBException {
		ArrayList<OBDtoADCObject> retVal = new ArrayList<OBDtoADCObject>();
		String sqlText = "";
		try {
			sqlText = String.format(
					" SELECT INDEX, NAME, DESCRIPTION, TYPE                  \n"
							+ " FROM MNG_ADC                                           \n"
							+ " WHERE GROUP_INDEX = %d AND AVAILABLE = %d              \n"
							+ " ORDER BY NAME ASC                                      \n",
					target.getIndex(), OBDefine.DATA_AVAILABLE);
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoADCObject log = new OBDtoADCObject();

				log.setCategory(OBDtoADCObject.CATEGORY_GROUP);
				log.setDesciption(db.getString(rs, "DESCRIPTION"));
				log.setIndex(db.getInteger(rs, "INDEX"));
				log.setName(db.getString(rs, "NAME"));
				log.setVendor(db.getInteger(rs, "TYPE"));

				retVal.add(log);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	@Override
	public ArrayList<OBDtoADCObject> getInvolvedObjectListAlteon(OBDtoADCObject target, OBDtoExtraInfo extraInfo)
			throws OBException {
		ArrayList<OBDtoADCObject> retVal = new ArrayList<OBDtoADCObject>();

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
		} catch (OBException e) {
			throw e;
		}

		try {
			retVal = getInvolvedObjectListAlteon(target, extraInfo, db);
		} catch (OBException e) {
			db.closeDB();
			throw e;
		} catch (Exception e) {
			db.closeDB();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private ArrayList<OBDtoADCObject> getInvolvedObjectListAlteon(OBDtoADCObject target, OBDtoExtraInfo extraInfo,
			OBDatabase db) throws OBException {
		ArrayList<OBDtoADCObject> retVal = new ArrayList<OBDtoADCObject>();
		String sqlText = "";
		try {
			sqlText = String.format(
					" SELECT INDEX, NAME, DESCRIPTION, TYPE                  \n"
							+ " FROM MNG_ADC                                           \n"
							+ " WHERE TYPE = %d AND AVAILABLE = %d              \n",
					target.getIndex(), OBDefine.DATA_AVAILABLE);
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoADCObject log = new OBDtoADCObject();

				log.setCategory(OBDtoADCObject.CATEGORY_GROUP);
				log.setDesciption(db.getString(rs, "DESCRIPTION"));
				log.setIndex(db.getInteger(rs, "INDEX"));
				log.setName(db.getString(rs, "NAME"));
				log.setVendor(db.getInteger(rs, "TYPE"));

				retVal.add(log);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private ArrayList<OBDtoADCObject> getInvolvedObjectListADC(OBDtoADCObject target, OBDtoExtraInfo extraInfo,
			OBDatabase db) throws OBException {// ADC에 포함된 Virtual server, service 목록을 제공한다.
		ArrayList<OBDtoADCObject> retVal = new ArrayList<OBDtoADCObject>();
		String sqlText = "";
		try {
			if (target.getVendor() == OBDefine.ADC_TYPE_ALTEON) {// service 목록을 제공한다.
				sqlText = String.format(
						" SELECT A.INDEX AS INDEX, B.NAME, B.VIRTUAL_IP, A.VIRTUAL_PORT, B.STATUS, B.ALTEON_ID  \n"
								+ " FROM TMP_SLB_VS_SERVICE  A                                                  \n"
								+ " INNER JOIN TMP_SLB_VSERVER  B                                               \n"
								+ " ON A.VS_INDEX = B.INDEX                                                     \n"
								+ " WHERE A.ADC_INDEX = %d                                                      \n"
								+ " ORDER BY B.VIRTUAL_IP ASC   			  									  \n",
						target.getIndex());
			} else {// server 목록을 제공한다.
				sqlText = String.format(
						" SELECT INDEX, NAME, VIRTUAL_IP, VIRTUAL_PORT, STATUS, ALTEON_ID   \n"
								+ " FROM TMP_SLB_VSERVER                                              \n"
								+ " WHERE ADC_INDEX = %d                                              \n"
								+ " ORDER BY VIRTUAL_IP ASC											\n",
						target.getIndex());
			}
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoADCObject log = new OBDtoADCObject();

				log.setCategory(OBDtoADCObject.CATEGORY_GROUP);
				log.setDesciption(db.getString(rs, "NAME"));
//				log.setIndex(db.getInteger(rs, "ALTEON_ID"));
				log.setName(db.getString(rs, "VIRTUAL_IP"));
				log.setStatus(db.getInteger(rs, "STATUS"));
				log.setStrIndex(db.getString(rs, "INDEX"));
				log.setVendor(target.getVendor());
				log.setExtraInfo(db.getInteger(rs, "VIRTUAL_PORT"));
				log.setAlteonId(db.getString(rs, "ALTEON_ID"));
				retVal.add(log);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	@Override
	public ArrayList<OBDtoWidgetItemInfo> getWidgetItemList() throws OBException {
		ArrayList<OBDtoWidgetItemInfo> retVal = new ArrayList<OBDtoWidgetItemInfo>();

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();
			retVal = getWidgetItemList(db);
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

	private ArrayList<OBDtoWidgetItemInfo> getWidgetItemList(OBDatabase db) throws OBException {
		ArrayList<OBDtoWidgetItemInfo> retVal = new ArrayList<OBDtoWidgetItemInfo>();
		String sqlText = "";
		try {

			sqlText = String.format(
					" SELECT INDEX, NAME, DESCRIPTION, TARGET_AREA, WIDTH_MIN, WIDTH_MAX, HEIGHT_MIN, HEIGHT_MAX \n"
							+ " FROM %s                                                                                    \n"
							+ " ORDER BY TYPE ASC, NAME ASC																		 	 \n",
					OBCommon.makeProperTableName("MNG_WIDGET_INFO"));

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoWidgetItemInfo obj = new OBDtoWidgetItemInfo();

				obj.setIndex(db.getInteger(rs, "INDEX"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setDescription(db.getString(rs, "DESCRIPTION"));
				obj.setTargetArea(db.getInteger(rs, "TARGET_AREA"));
				obj.setWidthMinSize(db.getInteger(rs, "WIDTH_MIN"));
				obj.setWidthMaxSize(db.getInteger(rs, "WIDTH_MAX"));
				obj.setHeightMinSize(db.getInteger(rs, "HEIGHT_MIN"));
				obj.setHeightMaxSize(db.getInteger(rs, "HEIGHT_MAX"));
				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	@Override
	public ArrayList<OBDtoGroupHistory> getResponseTimeHistoryByIndexGroup(OBDtoADCObject target,
			OBDtoSearch searchOption) throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoGroupHistory> retVal = new ArrayList<OBDtoGroupHistory>();
		try {
			db.openDB();
			retVal = new OBDynamicDashboardDB().getResponseTimeHistoryByIndexGroup(target, searchOption, db);
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

	@Override
	public ArrayList<OBDtoDataObj> getResponseTimeHistoryByIndex(OBDtoADCObject target, OBDtoSearch searchOption)
			throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoDataObj> retVal = new ArrayList<OBDtoDataObj>();
		try {
			db.openDB();
			if (target.getCategory() == OBDtoADCObject.CATEGORY_ALL) {

			} else if (target.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {

			} else {
				retVal = new OBDynamicDashboardDB().getResponseTimeHistoryByIndex(target, searchOption, db);
			}
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

	@Override
	public ArrayList<OBDtoGroupHistory> getVsConcurrentSessionHistoryByIndexGroup(OBDtoADCObject target,
			OBDtoSearch searchOption) throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoGroupHistory> retVal = new ArrayList<OBDtoGroupHistory>();
		try {
			db.openDB();
			retVal = new OBDynamicDashboardDB().getVsBpsConcurrentSessionHistoryByIndexGroup(target, searchOption, db);
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

	public ArrayList<OBDtoFaultBpsConnInfo> getVsConcurrentSessionHistoryByIndex(OBDtoADCObject target,
			OBDtoSearch searchOption) throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoFaultBpsConnInfo> retVal = new ArrayList<OBDtoFaultBpsConnInfo>();
		try {
			db.openDB();
			if (target.getCategory() == OBDtoADCObject.CATEGORY_ALL) {

			} else if (target.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {

			} else {
				retVal = new OBDynamicDashboardDB().getVsBpsConcurrentSessionHistoryByIndex(target, searchOption, db);
			}
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

	@Override
	public ArrayList<OBDtoGroupHistory> getVsThroughputHistoryByIndexGroup(OBDtoADCObject target,
			OBDtoSearch searchOption) throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoGroupHistory> retVal = new ArrayList<OBDtoGroupHistory>();
		try {
			db.openDB();
			retVal = new OBDynamicDashboardDB().getVsThroughputHistoryByIndex(target, searchOption, db);
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

	@Override
	public ArrayList<OBDtoFaultBpsConnInfo> getVsThroughputHistoryByIndex(OBDtoADCObject target,
			OBDtoSearch searchOption) throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoFaultBpsConnInfo> retVal = new ArrayList<OBDtoFaultBpsConnInfo>();
		try {
			db.openDB();

			if (target.getCategory() == OBDtoADCObject.CATEGORY_ALL) {

			} else if (target.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {

			} else {
				retVal = new OBDynamicDashboardDB().getVsBpsConcurrentSessionHistoryByIndex(target, searchOption, db);
			}
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

	@Override
	public ArrayList<OBDtoADCObject> getFlbGroupList(OBDtoADCObject target, OBDtoExtraInfo extraInfo)
			throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoADCObject> retVal = new ArrayList<OBDtoADCObject>();
		try {
			db.openDB();
			retVal = new OBDynamicDashboardDB().getFlbGroupList(target, extraInfo, db);
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

	@Override
	public ArrayList<OBDtoDashboardStatusNotification> getAdcStatusNotification(OBDtoADCObject adcObject)
			throws Exception {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoDashboardStatusNotification> retVal = new ArrayList<OBDtoDashboardStatusNotification>();
		try {
			db.openDB();
			retVal = new OBDynamicDashboardDB().getAdcNotificationList(adcObject, db);
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

	@Override
	public ArrayList<OBDtoDashboardStatusNotification> getVsStatusNotification(OBDtoADCObject adcObject)
			throws Exception {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoDashboardStatusNotification> retVal = new ArrayList<OBDtoDashboardStatusNotification>();
		try {
			db.openDB();
			if (adcObject.getIndex() == OBDefine.DATA_UNAVAILABLE) {
				String[] index = adcObject.getStrIndex().split("_");
				Integer adcIndex = Integer.parseInt(index[0]);
				OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
				adcObject.setVendor(adcInfo.getAdcType());
			}

			retVal = new OBDynamicDashboardDB().getVsNotificationList(adcObject, db);
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

	@Override
	public ArrayList<OBDtoDashboardStatusNotification> getMemberStatusNotification(OBDtoADCObject adcObject)
			throws Exception {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoDashboardStatusNotification> retVal = new ArrayList<OBDtoDashboardStatusNotification>();
		try {
			db.openDB();
			retVal = new OBDynamicDashboardDB().getMemberNotificationList(adcObject, db);
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
}
