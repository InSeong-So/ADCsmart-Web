package kr.openbase.adcsmart.web.report.inspection.f5.row;

import java.sql.ResultSet;
import java.sql.SQLException;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.f5.handler.F5DataHandler;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;
import kr.openbase.adcsmart.web.report.inspection.utility.Common;

public class LinkLogRow extends AbstractRow {
//    private final F5DataHandler dataHandler;
	private final String swVersion;
	private final Integer adcIndex;

	public LinkLogRow(final String swVersion, final F5DataHandler dataHandler, final Integer adcIndex) {
		this.swVersion = swVersion;
//        this.dataHandler = dataHandler;
		this.adcIndex = adcIndex;
	}

	@Override
	public OBDtoInspectionReportRow getRow(final Column col) throws OBException {
		OBDtoInspectionReportRow LinkLog = new OBDtoInspectionReportRow();
		try {
			if (Common.f5SwVersion(swVersion)) {
				LinkLog.setCheckMethod("/var/log/ltm");
			} else
				LinkLog.setCheckMethod("/var/log/ltm");
			LinkLog.setColumn(col.getThridColumn());

			LinkLog.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_L2_LINK_DOWN_LOG));
			LinkLog.setCheckMethod("/var/log/ltm");
			LinkLog.setChecklist(checkLinkDownLog());
			LinkLog.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_NORMAL));

			if (!LinkLog.getChecklist().isEmpty())
				LinkLog.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_ANORMAL));
		} catch (Exception e) {
			throw new OBException(OBDefine.LOGFILE_SYSTEM, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, LinkLog.toString());
		return LinkLog;
	}

	private String checkLinkDownLog() throws Exception {
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		String retVal = "";
		try {
			db.openDB();

			sqlText = String.format(
					" SELECT * FROM LOG_ADC_SYSLOG WHERE ADC_INDEX=%d       \n"
							+ " AND EVENT LIKE %s                                                 \n"
							+ " AND OCCUR_TIME > (CURRENT_DATE - INTERVAL '30 days') LIMIT 1;     \n",
					adcIndex, OBParser.sqlString(OBDefine.LINK_DOWN_LOG.LINK_DOWN1));
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal = "Link down occurred";
				return retVal;
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}
}
