package kr.openbase.adcsmart.web.report.inspection.alteon.row;

import java.util.List;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.alteon.handler.AlteonDataHandler;
import kr.openbase.adcsmart.web.report.inspection.alteon.parser.CPUMPUsageParser;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;

/**
 * CPU MP load 상태 행을 만들어준다.
 * 
 * @author 최영조
 */
public class CPUMPUsageRow extends AbstractRow
{
    private final AlteonDataHandler dataHandler;
    private final String            swVersion;

    public CPUMPUsageRow(final String swVersion, final AlteonDataHandler dataHandler)
    {
        this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }
    
    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        final OBDtoInspectionReportRow reportRowDto = new OBDtoInspectionReportRow();

        reportRowDto.setColumn(col.getFirstColumn());
        reportRowDto.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_CPU_MP_USAGE));
        reportRowDto.setCheckMethod("/stat/mp/cpu");
        
        try
        {            
            final String commandResult = dataHandler.getStatCPUMP();
            final List<String> parsedResult = new CPUMPUsageParser(swVersion).parse(commandResult);

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
    protected String formatter(final List<String> parsedString)
    {
        for(String str : parsedString)
        {
            return str + "%";
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
