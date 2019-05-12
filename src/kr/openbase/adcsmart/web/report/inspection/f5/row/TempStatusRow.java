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

public class TempStatusRow extends AbstractRow
{
    private final F5DataHandler dataHandler;

     private final String swVersion;

    public TempStatusRow(final String swVersion, final F5DataHandler dataHandler)
    {
         this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    @Override
    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        OBDtoInspectionReportRow tempStatus = new OBDtoInspectionReportRow();

        try
        {
            tempStatus.setColumn(col.getFirstColumn());
            if(Common.f5SwVersion(swVersion))
            {
                tempStatus.setCheckMethod("#tmsh show sys hardware");
            }
            else
                tempStatus.setCheckMethod("#b platform");
            tempStatus.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_CASSIS_TEMP_STATE));
            tempStatus.setChecklist(checkTempStatus(dataHandler.getChasisTempStatusList()));

            if(!tempStatus.getChecklist().isEmpty())
            {
                tempStatus.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_ANORMAL));
            }
            else
            {
                tempStatus.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_NORMAL));
            }
        }
        catch(Exception e)
        {
            throw new OBException(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, tempStatus.toString());
        return tempStatus;
    }

    private String checkTempStatus(List<OBNameValue> chassisTempStatusList) throws Exception
    {
        if(chassisTempStatusList == null || chassisTempStatusList.size() == 0)
        {
            return ERROR;
        }

        String retVal = "";
        for(OBNameValue obj : chassisTempStatusList)
        {
            if(Integer.parseInt(obj.getValue()) >= OBDefine.CHASSIS_TEMPERATURE.HIGH)
            {
                retVal = "Temperature High";
                break;
            }
        }
        return retVal;
    }
}
