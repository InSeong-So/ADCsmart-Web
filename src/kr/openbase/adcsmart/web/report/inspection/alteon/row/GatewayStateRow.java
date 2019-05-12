package kr.openbase.adcsmart.web.report.inspection.alteon.row;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.alteon.handler.AlteonDataHandler;
import kr.openbase.adcsmart.web.report.inspection.alteon.parser.GatewayStateParser;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;

public class GatewayStateRow extends AbstractRow
{
    private static final String     UP_KEYWORD = "Up";

    private final AlteonDataHandler dataHandler;
    private final String            swVersion;

    public GatewayStateRow(final String swVersion, final AlteonDataHandler dataHandler)
    {
        this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        final OBDtoInspectionReportRow reportRowDto = new OBDtoInspectionReportRow();

        // null 값을 주면 바로 윗 항목의 제목을 같이 공유한다.
        reportRowDto.setColumn(col.getThridColumn());
        reportRowDto.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_L3_GATEWAY_STATE));
        reportRowDto.setCheckMethod("/info/l3/ip");

        try
        {
            final String commandResult = dataHandler.getInfoL3Ip();
            final List<String> parsedResult = new GatewayStateParser(swVersion).parse(commandResult);
            

            final String formattedResult = formatter(parsedResult);
            final String finalResult = validator(formattedResult);

            reportRowDto.setChecklist(formattedResult);
            reportRowDto.setResult(finalResult);
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
        final String regex = "\\d+:(.+)";
        final Pattern pattern = Pattern.compile(regex);

        String result = "";
        String prefix = "";
        for(String rawStr : parsedString)
        {
            final Matcher match = pattern.matcher(rawStr);

            if(match.find())
            {
                if(!match.group(1).equalsIgnoreCase(UP_KEYWORD))
                {
                    result = result + prefix + "\"" + rawStr + "\"";
                    prefix = ", ";
                }
            }
        }

        
        if(!result.equals(""))
        {
            result = OBParser.trimCsv(result, CHECKLIST_MAX_STRING, " 외 %d건").replace("\"", "");
        }
        
        return result;
    }

    private String validator(final String formattedString) throws Exception
    {
        if(formattedString.equals(""))
        {
            return OBMessages.getMessage(OBMessages.MSG_RPT_NORMAL);
        }
        else
        {
            return OBMessages.getMessage(OBMessages.MSG_RPT_ANORMAL);
        }
    }
}
