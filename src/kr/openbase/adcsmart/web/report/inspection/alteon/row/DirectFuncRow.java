package kr.openbase.adcsmart.web.report.inspection.alteon.row;

import java.util.List;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.alteon.handler.AlteonDataHandler;
import kr.openbase.adcsmart.web.report.inspection.alteon.parser.DirectFuncParser;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;

/**
 * Direct 기능 항목의 내용을 출력한다.
 * 
 * @author 최영조
 */
public class DirectFuncRow extends AbstractRow
{
    private static final String     ENABLED  = "direct enabled";
    private static final String     DISABLED = "direct disabled";

    private final AlteonDataHandler dataHandler;
    private final String            swVersion;

    public DirectFuncRow(final String swVersion, final AlteonDataHandler dataHandler)
    {
        this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        final OBDtoInspectionReportRow reportRowDto = new OBDtoInspectionReportRow();

        reportRowDto.setColumn(col.getForthColumn());
        reportRowDto.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_L4_DIRECT_FUNC));
        reportRowDto.setCheckMethod("/cfg/slb/adv/cur");

        try
        {
            final String commandResult = dataHandler.getCfgSlbVstatCur();
            final List<String> parsedResult = new DirectFuncParser(swVersion).parse(commandResult);

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

    public List<String> parser(final String cmdResult)
    {
        return new DirectFuncParser(swVersion).parse(cmdResult);
    }

    public String formatter(final List<String> parsedString)
    {
        if(parsedString.size() > 0)
        {
            return ENABLED;
        }
        else
        {
            return DISABLED;
        }
    }

    public String validator(final List<String> parsedString)
    {
        if(parsedString.size() > 0)
        {
            return OBMessages.getMessage(OBMessages.MSG_RPT_USED);
        }
        else
        {
            return OBMessages.getMessage(OBMessages.MSG_RPT_NOT_USED);
        }
    }
}
