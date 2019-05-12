package kr.openbase.adcsmart.web.report.inspection.alteon.row;

import java.util.List;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.alteon.handler.AlteonDataHandler;
import kr.openbase.adcsmart.web.report.inspection.alteon.parser.SLBSessionStatsParser;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;

public class SLBSessionStatsRow extends AbstractRow
{
    private final AlteonDataHandler dataHandler;
    private final String            swVersion;

    public SLBSessionStatsRow(final String swVersion, final AlteonDataHandler dataHandler)
    {
        this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        final OBDtoInspectionReportRow reportRowDto = new OBDtoInspectionReportRow();

        // null 값을 주면 바로 윗 항목의 제목을 같이 공유한다.
        reportRowDto.setColumn(col.getForthColumn());
        reportRowDto.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_L4_SLB_SESSION_STATS));
        reportRowDto.setCheckMethod("/stat/slb/maint");

        try
        {
            final String commandResult = dataHandler.getStatMaint();
            final List<String> parsedResult = new SLBSessionStatsParser(swVersion).parse(commandResult);

            reportRowDto.setChecklist(formatter(parsedResult));
            reportRowDto.setResult(validator(parsedResult));
        }
        catch(Exception e)
        {
            throw new OBException(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, reportRowDto.toString());
        return reportRowDto;
    }

    @Override
    protected String formatter(final List<String> parsedString) throws Exception
    {
        String maxSession = String.format("%,d", Integer.parseInt(parsedString.get(0)));
        String curSession = String.format("%,d", Integer.parseInt(parsedString.get(1)));

        return "Curr: " + curSession + ", " + "Max: " + maxSession;
    }

    private String validator(final List<String> parsedString) throws Exception
    {
        return NOTHING;
    }
}
