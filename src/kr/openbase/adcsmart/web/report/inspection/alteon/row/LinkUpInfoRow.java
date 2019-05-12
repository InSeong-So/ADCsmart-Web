package kr.openbase.adcsmart.web.report.inspection.alteon.row;

import java.util.List;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.alteon.handler.AlteonDataHandler;
import kr.openbase.adcsmart.web.report.inspection.alteon.parser.LinkUpInfoParser;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;

public class LinkUpInfoRow extends AbstractRow
{
    private final AlteonDataHandler dataHandler;
    private final String            swVersion;

    public LinkUpInfoRow(final String swVersion, final AlteonDataHandler dataHandler)
    {
        this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        final OBDtoInspectionReportRow reportRowDto = new OBDtoInspectionReportRow();

        // null 값을 주면 바로 윗 항목의 제목을 같이 공유한다.
        reportRowDto.setColumn(col.getSecondColumn());
        reportRowDto.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_L2_LINK_UP_STATE));
        reportRowDto.setCheckMethod("/info/link");

        try
        {
            final String commandResult = dataHandler.getInfoLink();
            final List<String> parsedResult = new LinkUpInfoParser(swVersion).parse(commandResult);

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

    private String validator(final List<String> parsedString) throws Exception
    {
        return NOTHING;
    }
}
