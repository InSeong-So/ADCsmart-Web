package kr.openbase.adcsmart.web.report.inspection.alteon.row;

import java.util.List;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.alteon.handler.AlteonDataHandler;
import kr.openbase.adcsmart.web.report.inspection.alteon.parser.NTPInfoParser;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;

public class NTPInfoRow extends AbstractRow
{
    private static final String DISABLED_KEYWORD = "disabled";
    
    private final AlteonDataHandler dataHandler;
    private final String            swVersion;

    public NTPInfoRow(final String swVersion, final AlteonDataHandler dataHandler)
    {
        this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        final OBDtoInspectionReportRow reportRowDto = new OBDtoInspectionReportRow();

        // null 값을 주면 바로 윗 항목의 제목을 같이 공유한다.
        reportRowDto.setColumn(col.getFifthColumn());
        reportRowDto.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTP_INFO));
        reportRowDto.setCheckMethod("/cfg/sys/ntp/cur");
        
        try
        {
            final String commandResult = dataHandler.getCfgNtpCur();
            final List<String> parsedResult = new NTPInfoParser(swVersion).parse(commandResult);

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
        String result = "";
        String prefix = "";
        for(String str : parsedString)
        {
            result = result + prefix + str;
            prefix = "\n";

        }
        return result;
    }

    private String validator(final List<String> parsedString) throws Exception
    {
        if(parsedString.size() > 0)
        {
            if(parsedString.get(0).equalsIgnoreCase(DISABLED_KEYWORD))
            {
                return OBMessages.getMessage(OBMessages.MSG_RPT_NOT_INTERLOCK);
            }
        }

        return OBMessages.getMessage(OBMessages.MSG_RPT_INTERLOCK);
    }
}
