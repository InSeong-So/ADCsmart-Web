package kr.openbase.adcsmart.web.report.inspection.f5.row;

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

public class FanStatusRow extends AbstractRow
{
    private final F5DataHandler dataHandler;

     private final String swVersion;

    public FanStatusRow(final String swVersion, final F5DataHandler dataHandler)
    {
         this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    @Override
    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        OBDtoInspectionReportRow fanStatus = new OBDtoInspectionReportRow();

        try
        {
            fanStatus.setColumn(col.getFirstColumn());
            if(Common.f5SwVersion(swVersion))
            {
                fanStatus.setCheckMethod("#tmsh show sys hardware");
            }
            else
                fanStatus.setCheckMethod("#b platform");
            fanStatus.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_FAN_STATE));
            fanStatus.setChecklist(checkFanStatus(dataHandler.getFanStatusList()));

            if(!fanStatus.getChecklist().isEmpty())
            {
                fanStatus.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_ANORMAL));
            }
            else
            {
                fanStatus.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_NORMAL));
            }
        }
        catch(Exception e)
        {
            throw new OBException(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, fanStatus.toString());
        return fanStatus;
    }

    private String checkFanStatus(List<OBNameValue> fanStatusList) throws Exception
    {
        if(fanStatusList == null || fanStatusList.size() == 0)
        {
            return ERROR;
        }

        String retVal = "";
        for(OBNameValue obj : fanStatusList)
        {
            switch(Integer.parseInt(obj.getValue()))
            {
            case 0: // bad
                if(!retVal.isEmpty())
                    retVal += ", ";
                retVal += String.format("(%s) bad", obj.getName());
                break;
            case 1: // good
                break;
            case 2: // not present
                if(!retVal.isEmpty())
                    retVal += ", ";
                retVal += String.format("(%s) not present", obj.getName());
                break;
            default:// good
                break;//
            }
        }
        return retVal;
    }
}
