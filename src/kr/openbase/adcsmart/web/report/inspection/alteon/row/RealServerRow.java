package kr.openbase.adcsmart.web.report.inspection.alteon.row;

import java.util.List;

import kr.openbase.adcsmart.service.dto.OBNameValue;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.alteon.handler.AlteonDataHandler;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;

public class RealServerRow extends AbstractRow
{
    private final AlteonDataHandler dataHandler;
    // private final String swVersion;

    public RealServerRow(final String swVersion, final AlteonDataHandler dataHandler)
    {
        // this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    @Override
    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        final OBDtoInspectionReportRow reportRowDto = new OBDtoInspectionReportRow();

        reportRowDto.setColumn(col.getForthColumn());
        reportRowDto.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_L4_REALSERVER_STATE));
        reportRowDto.setCheckMethod("/info/slb/dump");

        try
        {
            final List<OBNameValue> commandResult = dataHandler.getRealServer();

            int disable = 0;
            int up = 0;
            int failed = 0;

            for(OBNameValue value : commandResult)
            {
                switch(Integer.parseInt(value.getValue()))
                {
                case 2: // running
                    up++;
                    break;
                case 3: // failed
                    failed++;
                    break;
                case 4: // disabled
                    disable++;
                    break;
                default:
                }
            }

            reportRowDto.setChecklist(String.format("Disable(%d개)\nUp(%d개)\nFailed(%d개)", disable, up, failed));
            reportRowDto.setResult(up > 0 ? OBMessages.getMessage(OBMessages.MSG_RPT_NORMAL) : OBMessages.getMessage(OBMessages.MSG_RPT_ANORMAL));

        }
        catch(Exception e)
        {
            throw new OBException(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, reportRowDto.toString());
        return reportRowDto;
    }

}
