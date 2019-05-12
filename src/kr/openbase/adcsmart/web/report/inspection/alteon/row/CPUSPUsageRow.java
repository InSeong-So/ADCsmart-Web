package kr.openbase.adcsmart.web.report.inspection.alteon.row;

import java.util.List;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.alteon.handler.AlteonDataHandler;
import kr.openbase.adcsmart.web.report.inspection.alteon.parser.CPUSPUsageParser;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;

public class CPUSPUsageRow extends AbstractRow
{
    private final AlteonDataHandler dataHandler;
    private final String            swVersion;

    public CPUSPUsageRow(final String swVersion, final AlteonDataHandler dataHandler)
    {
        this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }
    
    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        final OBDtoInspectionReportRow reportRowDto = new OBDtoInspectionReportRow();

        reportRowDto.setColumn(col.getFirstColumn());
        reportRowDto.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_CPU_SP_USAGE));
        reportRowDto.setCheckMethod("/stat/sp #/cpu");
        
        try
        {
            final String commandResult = dataHandler.getStatCPUSP();
            final List<String> parsedResult = new CPUSPUsageParser(swVersion).parse(commandResult);

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
        if(parsedString != null)
        {
            int totalUsage = 0;
            int avrUsage = 0;

            for(String str : parsedString)
            {
                try
                {
                    final int spUsage = Integer.parseInt(str);
                    totalUsage = totalUsage + spUsage;
                }
                catch(Exception e)
                {

                }
            }

            int spCount = parsedString.size();
            if(spCount == 0)
            {
                spCount = 1;
            }

            avrUsage = totalUsage / spCount;
            return String.valueOf(avrUsage) + "%";
        }

        return ERROR;
    }

    private String validator(final List<String> parsedString) throws Exception
    {
        for(String str : parsedString)
        {
            final int cpuUseRatio = Integer.parseInt(str);
            if(cpuUseRatio >= 50)
            {
                return OBMessages.getMessage(OBMessages.MSG_RPT_WARNING);
            }
            else
            {
                return OBMessages.getMessage(OBMessages.MSG_RPT_NORMAL);
            }
        }

        return NOTHING;
    }
}
