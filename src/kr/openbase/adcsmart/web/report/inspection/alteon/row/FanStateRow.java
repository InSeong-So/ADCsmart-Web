package kr.openbase.adcsmart.web.report.inspection.alteon.row;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.alteon.handler.AlteonDataHandler;
import kr.openbase.adcsmart.web.report.inspection.alteon.parser.FanStateParser;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;

public class FanStateRow extends AbstractRow
{
    private static final int        REFERENCE_MODEL_NUMBER = 4;
    
    private final AlteonDataHandler dataHandler;
    private final String            swVersion;
    private final int               modelNumber;
    private final SeriesModel       seriesModel;

    public FanStateRow(final String swVersion, final AlteonDataHandler dataHandler, final String model)
    {
        this.swVersion = swVersion;
        this.dataHandler = dataHandler;
        this.modelNumber = convertToModelSeriesNumber(model);
        if(modelNumber < REFERENCE_MODEL_NUMBER)
        {
            seriesModel = new LowerSeriesModel();
        }
        else
        {
            seriesModel = new UpperSeriesModel();
        }
    }

    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        final OBDtoInspectionReportRow reportRowDto = new OBDtoInspectionReportRow();

        // null 값을 주면 바로 윗 항목의 제목을 같이 공유한다.
        reportRowDto.setColumn(col.getFirstColumn());
        reportRowDto.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_FAN_STATE));
        reportRowDto.setCheckMethod("/info/sys/fan");

        try
        {
            final String commandResult = dataHandler.getSysFan();
            final List<String> parsedResult = new FanStateParser(swVersion, modelNumber).parse(commandResult);

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
        return seriesModel.formatter(parsedString);
    }

    private String validator(final List<String> parsedString) throws Exception
    {
        return seriesModel.validator(parsedString);
    }

    private int convertToModelSeriesNumber(String modelStr)
    {
        final String regex = ".+(\\d)\\d\\d\\d";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher match = pattern.matcher(modelStr);

        if(match.find())
        {
            return Integer.parseInt(match.group(1));
        }

        return -1;
    }

    interface SeriesModel
    {
        String formatter(final List<String> parsedString) throws Exception;

        String validator(final List<String> parsedString) throws Exception;
    }

    // 2, 3 시리즈 모델
    class LowerSeriesModel implements SeriesModel
    {
        @Override
        public String formatter(final List<String> parsedString) throws Exception
        {
            // MSG_RPT_BASIC_POWER_STATE_CHECKLIST = 듀얼파워는 4시리즈 이상 지원
//            return OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_POWER_STATE_CHECKLIST);
            
//            System.out.println("parsedString : " + parsedString);
            
            if(parsedString.size() == 1 && parsedString.get(0) != null)
            {
                return parsedString.get(0);
            }

            return EMPTY;
        }

        @Override
        public String validator(final List<String> parsedString) throws Exception
        {
            if(parsedString.size() > 0)
            {
                return OBMessages.getMessage(OBMessages.MSG_RPT_NORMAL);
            }

            return OBMessages.getMessage(OBMessages.MSG_RPT_ANORMAL);
        }
    }

    // 4 시리즈 이상 모델
    class UpperSeriesModel implements SeriesModel
    {
        @Override
        public String formatter(final List<String> parsedString) throws Exception
        {
            List<String> failedFan = new ArrayList<String>();
            for(int i = 0; i < parsedString.size(); i++)
            {
                failedFan.add(parsedString.get(i));
            }
            if(failedFan.size() > 0 && failedFan.get(0) != null)
            {
                return "Failed Fan : " + failedFan;
            }

            return EMPTY;
        }

        @Override
        public String validator(final List<String> parsedString) throws Exception
        {
            if(parsedString.size() > 0 && parsedString.get(0) != null)
            {
                return OBMessages.getMessage(OBMessages.MSG_RPT_ANORMAL);
            }

            return OBMessages.getMessage(OBMessages.MSG_RPT_NORMAL);
        }
    }
}
