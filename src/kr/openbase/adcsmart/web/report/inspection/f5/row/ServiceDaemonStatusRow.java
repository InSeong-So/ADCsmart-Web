package kr.openbase.adcsmart.web.report.inspection.f5.row;

import java.util.List;

import kr.openbase.adcsmart.service.impl.f5.handler.dto.OBDtoDaemStatus;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.f5.handler.F5DataHandler;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;
import kr.openbase.adcsmart.web.report.inspection.utility.Common;

public class ServiceDaemonStatusRow extends AbstractRow
{
    private final F5DataHandler dataHandler;
     private final String swVersion;

    public ServiceDaemonStatusRow(final String swVersion, final F5DataHandler dataHandler)
    {
         this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    @Override
    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        OBDtoInspectionReportRow daemonStatus = new OBDtoInspectionReportRow();

        try
        {
            if(Common.f5SwVersion(swVersion))
            {
                daemonStatus.setCheckMethod("#tmsh show sys service");
            }
            else
                daemonStatus.setCheckMethod("#bigstart status");
            daemonStatus.setColumn(col.getFirstColumn());
            daemonStatus.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_L4_DAEMON_STATE));
            daemonStatus.setChecklist(checkDaemonStatus(dataHandler.getDaemonList()));
            daemonStatus.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_NORMAL));
            if(!daemonStatus.getChecklist().isEmpty())
                daemonStatus.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_ANORMAL));
        }
        catch(Exception e)
        {
            throw new OBException(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, daemonStatus.toString());
        return daemonStatus;
    }

    private String checkDaemonStatus(List<OBDtoDaemStatus> daemonList) throws Exception
    {//
        if(daemonList == null)
        {
            return ERROR;
        }

        String retVal = "";
        for(OBDtoDaemStatus obj : daemonList)
        {
            if(obj.getStatus() == OBDtoDaemStatus.STATUS_DOWN)
            {
                if(!retVal.isEmpty())
                    retVal += ", ";
                retVal += obj.getProcessName();
            }
        }

        if(!retVal.isEmpty())
            retVal = "Stop: " + retVal;

        return retVal;
    }

}
