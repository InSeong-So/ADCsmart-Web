package kr.openbase.adcsmart.web.report.inspection.f5.row;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBNameValue;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.f5.handler.F5DataHandler;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;
import kr.openbase.adcsmart.web.report.inspection.utility.Common;

public class GatewayStatusRow extends AbstractRow
{
    private final F5DataHandler dataHandler;

     private final String swVersion;

    public GatewayStatusRow(final String swVersion, final F5DataHandler dataHandler)
    {
        this.dataHandler = dataHandler;
         this.swVersion = swVersion;
    }

    @Override
    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        OBDtoInspectionReportRow gatewayInfo = new OBDtoInspectionReportRow();

        try
        {
            if(Common.f5SwVersion(swVersion))
            {
                gatewayInfo.setCheckMethod("#tmsh show net route");
            }
            else
                gatewayInfo.setCheckMethod("#b route");
            gatewayInfo.setColumn(col.getThridColumn());
            gatewayInfo.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_L3_GATEWAY_STATE));
            gatewayInfo.setChecklist(checkGatewayInfo(dataHandler.getGatewayList()));
            gatewayInfo.setResult(NOTHING);
        }
        catch(Exception e)
        {
            throw new OBException(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, gatewayInfo.toString());
        return gatewayInfo;
    }

    private String checkGatewayInfo(List<OBNameValue> gatewayList) throws Exception
    {
        if(gatewayList == null)
        {
            throw new OBException(OBDefine.LOGFILE_SYSTEM, "failed to get gateway info");
        }

        final List<String> parsedString = new ArrayList<String>();
        for(OBNameValue obj : gatewayList)
        {
            parsedString.add(obj.getValue());
        }

        return formatter(parsedString);
    }
}
