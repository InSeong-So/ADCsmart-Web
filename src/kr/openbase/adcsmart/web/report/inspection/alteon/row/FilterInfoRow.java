package kr.openbase.adcsmart.web.report.inspection.alteon.row;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.snmp.alteon.dto.OBDtoRptFilterInfo;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.alteon.handler.AlteonDataHandler;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;

/**
 * SNMP 를 이용해서 데이터를 가져와야 한다.
 * 
 * @author 최영조
 */
public class FilterInfoRow extends AbstractRow
{
    private final AlteonDataHandler dataHandler;
    // private final String swVersion;

    public FilterInfoRow(final String swVersion, final AlteonDataHandler dataHandler)
    {
        // this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    @Override
    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        final OBDtoInspectionReportRow reportRowDto = new OBDtoInspectionReportRow();

        // null 값을 주면 바로 윗 항목의 제목을 같이 공유한다.
        reportRowDto.setColumn(col.getForthColumn());
        reportRowDto.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_L4_FILTER_INFO));
        reportRowDto.setCheckMethod("/info/slb/dump");

        try
        {
            // 결과 포맷) 포트번호: 필터리스트(space로 구분), ...
            // 예시) 1: 20 21 22, 5: empty, ...

            final List<OBDtoRptFilterInfo> commandResult = dataHandler.getFilterInfo();

            List<String> parsedResultList = new ArrayList<String>();

            if(commandResult != null)
            {
                for(OBDtoRptFilterInfo result : commandResult)
                {
                    if(result != null)
                    {
                        String parsedResult = "";

                        // 포트번호
                        parsedResult += result.getPortIndex();
                        parsedResult += ": ";
                        parsedResult += result.getFiltList() != null && !result.getFiltList().isEmpty() ? result.getFiltList().toString().replaceAll("[\\[\\]]", "").replace(",", "") : "empty";
                        parsedResultList.add(parsedResult);
                    }
                }
            }

            reportRowDto.setChecklist(formatter(parsedResultList));
            reportRowDto.setResult(NOTHING);
        }
        catch(Exception e)
        {
            // 값이 없는 정상 상태다.
            // 차후 다른 방법으로 제외해야 한다.
            // throw new OBException(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, reportRowDto.toString());
        return reportRowDto;
    }

}
