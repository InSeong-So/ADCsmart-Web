package kr.openbase.adcsmart.web.report.inspection.alteon.row;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.alteon.handler.AlteonDataHandler;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;

public class SystemLogRow extends AbstractRow
{
    // private final AlteonDataHandler dataHandler;
    // private final String swVersion;

    public SystemLogRow(final String swVersion, final AlteonDataHandler dataHandler)
    {
        // this.swVersion = swVersion;
        // this.dataHandler = dataHandler;
    }

    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        final OBDtoInspectionReportRow reportRowDto = new OBDtoInspectionReportRow();

        // null 값을 주면 바로 윗 항목의 제목을 같이 공유한다.
        reportRowDto.setColumn(col.getFifthColumn());
        reportRowDto.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_SYSTEM_LOG));
        reportRowDto.setCheckMethod("/info/sys/log");
        reportRowDto.setChecklist(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_SYSTEM_LOG_CHECKLIST));
        reportRowDto.setResult(EMPTY);

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, reportRowDto.toString());
        return reportRowDto;
    }
}
