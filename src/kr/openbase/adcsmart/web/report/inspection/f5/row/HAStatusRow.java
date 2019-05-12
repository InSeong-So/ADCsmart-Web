package kr.openbase.adcsmart.web.report.inspection.f5.row;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.f5.handler.F5DataHandler;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;
import kr.openbase.adcsmart.web.report.inspection.utility.Common;

public class HAStatusRow extends AbstractRow
{
    private final F5DataHandler dataHandler;
    private final String        swVersion;

    public HAStatusRow(final String swVersion, final F5DataHandler dataHandler)
    {
        this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    @Override
    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        OBDtoInspectionReportRow haStatus = new OBDtoInspectionReportRow();
        
        try
        {
            haStatus.setColumn(col.getSecondColumn());
            if(Common.f5SwVersion(swVersion))
            {
                haStatus.setCheckMethod("#tmsh show sys failover");
            }
            else
                haStatus.setCheckMethod("#b failover");
            haStatus.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_HA_STATE));
            haStatus.setChecklist(EMPTY);
            haStatus.setResult(checkHAStatus(dataHandler.getHaStatus()));
        }
        catch(Exception e)
        {
            throw new OBException(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, haStatus.toString());
        return haStatus;
    }

    /**
     * HA 상태는 버전에 따라 다르다. 이를 검사한다. 10.x 이하: 0: stand-by, 그외는 active
     * 
     * @param status
     * @param swVersion
     * @return
     */
    private String checkHAStatus(int status) throws Exception
    {//
        if(swVersion == null || swVersion.isEmpty())
        {
            if(status == 0)
                return "Stand-by";
            else
                return "Active";
        }

        try
        {
            String[] verElements = swVersion.split("\\.", 2); // 버전 요소 두개까지 문자열로 건진다. 지금은 하나만 쓴다.
            if(verElements[0].equals("11"))
            {
                switch(status)
                {
                case 0:
                    return "Unknown";
                case 1:
                    return "Offline";
                case 2:
                    return "ForcedOffline";
                case 3:
                    return "Stand-by";
                default:
                    return "Active";
                }
            }
            else
            {
                if(status == 0)
                    return "Stand-by";
                else
                    return "Active";
            }
        }
        catch(Exception e)
        {
            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        return ERROR;
    }

}
