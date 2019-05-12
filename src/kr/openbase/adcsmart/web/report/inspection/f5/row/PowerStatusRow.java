package kr.openbase.adcsmart.web.report.inspection.f5.row;

import java.util.ArrayList;

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

public class PowerStatusRow extends AbstractRow
{
    private final F5DataHandler dataHandler;
     private final String swVersion;

    public PowerStatusRow(final String swVersion, final F5DataHandler dataHandler)
    {
         this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    @Override
    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        OBDtoInspectionReportRow powerStatus = new OBDtoInspectionReportRow();

        try
        {
            if(Common.f5SwVersion(swVersion))
            {
                powerStatus.setCheckMethod("#tmsh show sys hardware");
            }
            else
                powerStatus.setCheckMethod("#b platform");
            powerStatus.setColumn(col.getFirstColumn());
            powerStatus.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_POWER_STATE));
            powerStatus.setChecklist(checkPowerStatus(dataHandler.getPowerStatusList()));
            powerStatus.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_NORMAL));

            if(!powerStatus.getChecklist().isEmpty())
                powerStatus.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_ANORMAL));
        }
        catch(Exception e)
        {
            throw new OBException(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, powerStatus.toString());
        return powerStatus;
    }

    private String checkPowerStatus(ArrayList<OBNameValue> powerStatusList) throws Exception
    {
        if(powerStatusList == null || powerStatusList.size() == 0)
        {
            return ERROR;
        }

        String retVal = "";
        for(OBNameValue obj : powerStatusList)
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
            }
        }
        return retVal;
    }

}
