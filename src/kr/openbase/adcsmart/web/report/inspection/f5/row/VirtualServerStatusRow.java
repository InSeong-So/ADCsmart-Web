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

public class VirtualServerStatusRow extends AbstractRow
{
    private final F5DataHandler dataHandler;
    // private final String swVersion;

    public VirtualServerStatusRow(final String swVersion, final F5DataHandler dataHandler)
    {
        // this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    @Override
    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        // Virtual Server 상태 . green이 아닌 항목이 있을 경우 xxx외 00건 으로 표시한다.
        OBDtoInspectionReportRow virtualServerStatus = new OBDtoInspectionReportRow();

        try
        {
            virtualServerStatus.setColumn(col.getForthColumn());
            virtualServerStatus.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_L4_VIRTUALSERVER_STATE));
            virtualServerStatus.setCheckMethod("GUI→Statistics→Virtual Servers");
            virtualServerStatus.setChecklist(checkVirtualServerInfo(dataHandler.getVirtualServerStatusList()));
            virtualServerStatus.setResult(NOTHING);
        }
        catch(Exception e)
        {
            throw new OBException(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, virtualServerStatus.toString());
        return virtualServerStatus;
    }

    /**
     * Virtual Server를 순회하며 각 상태에 속한 VS 수를 산출한다.
     * 
     * @param virtualServerList
     * @return
     * @throws OBException
     */
    private String checkVirtualServerInfo(List<OBNameValue> virtualServerList) throws Exception
    {
        if(virtualServerList == null)
        {
            return ERROR;
        }

        String retVal = "";

        int disable = 0;
        int up = 0;
        int failed = 0;
        int unknown = 0;

        for(OBNameValue value : virtualServerList)
        {
            switch(Integer.parseInt(value.getValue()))
            {
            case 0: // none - error;
                failed++;
                break;
            case 1: // green - available in some capacity;
                up++;
                break;
            case 2: // not currently available;
                disable++;
                break;
            case 3: // red - not available;
                failed++;
                break;
            case 4: // blue - availability is unknown;
                unknown++;
                break;
            case 5: // gray - unlicensed.
                disable++;
                break;
            default:
            }
        }

        retVal = String.format("Disable(%d개)\nUp(%d개)\nFailed(%d개)\nUnknown(%d개)", disable, up, failed, unknown);

        return retVal;
    }

}
