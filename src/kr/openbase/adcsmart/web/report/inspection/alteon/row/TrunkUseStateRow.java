package kr.openbase.adcsmart.web.report.inspection.alteon.row;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.alteon.handler.AlteonDataHandler;
import kr.openbase.adcsmart.web.report.inspection.alteon.parser.TrunkUseStateParser;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;

public class TrunkUseStateRow extends AbstractRow
{
    private static final String UP_KEYWORD       = "up";
    private static final String DISABLED_KEYWORD = "disabled";

    private final AlteonDataHandler dataHandler;
    private final String            swVersion;

    public TrunkUseStateRow(final String swVersion, final AlteonDataHandler dataHandler)
    {
        this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        final OBDtoInspectionReportRow reportRowDto = new OBDtoInspectionReportRow();

        // null 값을 주면 바로 윗 항목의 제목을 같이 공유한다.
        reportRowDto.setColumn(col.getSecondColumn());
        reportRowDto.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_L2_TRUNK_USE_STATE));
        reportRowDto.setCheckMethod("/info/l2/trunk");

        try
        {
            final String commandResult = dataHandler.getInfoTrunk();
            final List<String> parsedResult = new TrunkUseStateParser(swVersion).parse(commandResult);

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
        for(String str : parsedString)
        {
            if(str.equalsIgnoreCase(DISABLED_KEYWORD))
            {
                return(OBMessages.getMessage(OBMessages.MSG_RPT_NOT_USED));
            }

            final String regex = ".+:(.+)";
            final Pattern pattern = Pattern.compile(regex);
            final Matcher match = pattern.matcher(str);

            if(match.find())
            {
                // 내용 중 하나라도 UP 상태라면 정상 상태
                if(match.groupCount() > 0 && match.group(1).equalsIgnoreCase(UP_KEYWORD))
                {
                    return OBMessages.getMessage(OBMessages.MSG_RPT_NORMAL);
                }
            }
        }

        return OBMessages.getMessage(OBMessages.MSG_RPT_ANORMAL);
    }
}
