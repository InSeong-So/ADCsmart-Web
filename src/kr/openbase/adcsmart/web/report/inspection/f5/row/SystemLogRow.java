package kr.openbase.adcsmart.web.report.inspection.f5.row;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.f5.handler.F5DataHandler;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;

/**
 * F5 장비의 정기점검 보고서에서 "System Log 확인" 항목에 대응하는 클래스이다.
 * 
 * @author 최영조
 */
public class SystemLogRow extends AbstractRow
{

    public SystemLogRow(final String swVersion, final F5DataHandler dataHandler)
    {
        // do nothing
    }

    @Override
    public OBDtoInspectionReportRow getRow(final Column col)
    {
        OBDtoInspectionReportRow syslogInfo = new OBDtoInspectionReportRow();

        syslogInfo.setColumn(col.getFifthColumn());
        syslogInfo.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_SYSTEM_LOG));
        syslogInfo.setCheckMethod("/var/log 로그 확인");
        syslogInfo.setChecklist(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_SYSTEM_LOG_CHECKLIST));
        syslogInfo.setResult(EMPTY);

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, syslogInfo.toString());
        return syslogInfo;
    }

}
