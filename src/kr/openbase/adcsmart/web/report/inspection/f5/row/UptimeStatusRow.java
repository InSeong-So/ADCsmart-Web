package kr.openbase.adcsmart.web.report.inspection.f5.row;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.f5.handler.F5DataHandler;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;

import org.snmp4j.smi.TimeTicks;

public class UptimeStatusRow extends AbstractRow
{
    private final F5DataHandler dataHandler;
    // private final String swVersion;

    public UptimeStatusRow(final String swVersion, final F5DataHandler dataHandler)
    {
        // this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    @Override
    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        OBDtoInspectionReportRow upTimeStatus = new OBDtoInspectionReportRow();

        try
        {
            upTimeStatus.setColumn(col.getFirstColumn());
            upTimeStatus.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_UPTIME));
            upTimeStatus.setCheckMethod("#uptime");
            upTimeStatus.setChecklist(checkUptimeStatus(dataHandler.getUpTime()));
            upTimeStatus.setResult(NOTHING);
        }
        catch(Exception e)
        {
            throw new OBException(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, upTimeStatus.toString());
        return upTimeStatus;
    }

    private String checkUptimeStatus(TimeTicks timeTicks) throws Exception
    {
        return String.format("%s", timeTicks.toString());
    }

}
