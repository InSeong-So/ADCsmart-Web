package kr.openbase.adcsmart.web.report.inspection.alteon.row;

import java.util.List;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.alteon.handler.AlteonDataHandler;
import kr.openbase.adcsmart.web.report.inspection.alteon.parser.VrrpStateParser;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;

public class VrrpStateRow extends AbstractRow
{
    private final AlteonDataHandler dataHandler;
    private final String            swVersion;

    public VrrpStateRow(final String swVersion, final AlteonDataHandler dataHandler)
    {
        this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        final OBDtoInspectionReportRow reportRowDto = new OBDtoInspectionReportRow();

        // null 값을 주면 바로 윗 항목의 제목을 같이 공유한다.
        reportRowDto.setColumn(col.getForthColumn());
        reportRowDto.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_L3_VRRP_STATE));
        reportRowDto.setCheckMethod("/info/l3/vrrp");

        try
        {
            final String commandResult = dataHandler.getInfoL3Vrrp();
            final List<String> parsedResult = new VrrpStateParser(swVersion).parse(commandResult);

//            System.out.println("parsedResult : " + parsedResult);
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
        // [masterCount:3, backupCount:1]
        String vrrpState = "";
        int masterCnt = Integer.parseInt(parsedString.get(0).split(":")[1]);
        int backupCnt = Integer.parseInt(parsedString.get(1).split(":")[1]);
        
        if(masterCnt > backupCnt)
            vrrpState = "master";
        else if(backupCnt > masterCnt)
            vrrpState = "backup";
        else
            vrrpState = "-";
            
        return vrrpState;
    }

    public String validator(final List<String> parsedString)
    {
        int masterCnt = Integer.parseInt(parsedString.get(0).split(":")[1]);
        int backupCnt = Integer.parseInt(parsedString.get(1).split(":")[1]);
        
        if(masterCnt > 0 && backupCnt > 0)
        {
            return OBMessages.getMessage(OBMessages.MSG_RPT_ANORMAL);
        }
        else
        {
            return OBMessages.getMessage(OBMessages.MSG_RPT_NORMAL);
        }
    }
}
