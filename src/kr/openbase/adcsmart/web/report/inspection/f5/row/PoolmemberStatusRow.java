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

public class PoolmemberStatusRow extends AbstractRow
{
    private F5DataHandler dataHandler;
    // private String swVersion;

    public PoolmemberStatusRow(final String swVersion, final F5DataHandler dataHandler)
    {
        this.dataHandler = dataHandler;
        // this.swVersion = swVersion;
    }

    @Override
    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        OBDtoInspectionReportRow poolMemberStatus = new OBDtoInspectionReportRow();
        try
        {
            poolMemberStatus.setColumn(col.getForthColumn());
            poolMemberStatus.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_L4_POOL_MEMBER_STATE));
            poolMemberStatus.setCheckMethod("GUI→Statistics→Pools");
            poolMemberStatus.setChecklist(checkPoolMemberInfo(dataHandler.getPoolMemberStatusList()));
            poolMemberStatus.setResult(NOTHING);
        }
        catch(Exception e)
        {
            throw new OBException(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, poolMemberStatus.toString());
        return poolMemberStatus;
    }

    /**
     * PoolMember를 순회하며 각 상태에 속한 멤버들의 수를 출력한다.
     * 
     * @param poolMemberList
     * @return
     * @throws OBException
     */
    private String checkPoolMemberInfo(List<OBNameValue> poolMemberList) throws Exception
    {
        if(poolMemberList == null)
        {
            return ERROR;
        }
        String retVal = "";

        int disable = 0;
        int up = 0;
        int failed = 0;
        int unknown = 0;

        for(OBNameValue value : poolMemberList)
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
