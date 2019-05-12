package kr.openbase.adcsmart.web.report.inspection.f5.row;

import java.text.DecimalFormat;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.f5.handler.F5DataHandler;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;
import kr.openbase.adcsmart.web.report.inspection.utility.Common;

public class ConnectionInfoRow extends AbstractRow
{
    private final F5DataHandler dataHandler;
     private final String swVersion;

    public ConnectionInfoRow(final String swVersion, final F5DataHandler dataHandler)
    {
        this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    @Override
    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        OBDtoInspectionReportRow connectionInfo = new OBDtoInspectionReportRow();

        try
        {
            connectionInfo.setColumn(col.getForthColumn());
            if(Common.f5SwVersion(swVersion))
            {
                connectionInfo.setCheckMethod("#tmsh show sys traffic");
            }
            else
                connectionInfo.setCheckMethod("#b global");
            connectionInfo.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_L4_CONNECTION_COUNT));
            connectionInfo.setChecklist(checkConnectionInfo(dataHandler.getConnCurr(), dataHandler.getConnMax()));
            connectionInfo.setResult(NOTHING);
        }
        catch(Exception e)
        {
            throw new OBException(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, connectionInfo.toString());
        return connectionInfo;
    }

    private String checkConnectionInfo(long curr, long max) throws Exception
    {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return String.format("Curr: %s, Max:%s", formatter.format(curr), formatter.format(max));
    }

}
