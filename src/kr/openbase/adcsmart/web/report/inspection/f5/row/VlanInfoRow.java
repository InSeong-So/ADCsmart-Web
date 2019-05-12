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

public class VlanInfoRow extends AbstractRow
{
    private F5DataHandler dataHandler;

     private String swVersion;

    public VlanInfoRow(final String swVersion, final F5DataHandler dataHandler)
    {
         this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    @Override
    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        // VLAN 정보. 이름만 추출하여 제공한다.
        OBDtoInspectionReportRow vlanInfo = new OBDtoInspectionReportRow();

        try
        {
            if(Common.f5SwVersion(swVersion))
            {
                vlanInfo.setCheckMethod("#tmsh show net vlan");
            }
            else
                vlanInfo.setCheckMethod("#b vlan");
            vlanInfo.setColumn(col.getThridColumn());
            vlanInfo.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_L2_VLAN_STATE));
            vlanInfo.setChecklist(checkVlanInfo(dataHandler.getVlanInfoList()));
            vlanInfo.setResult(NOTHING);
        }
        catch(Exception e)
        {
            throw new OBException(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, vlanInfo.toString());
        return vlanInfo;    
    }

    private String checkVlanInfo(List<OBNameValue> vlanList) throws Exception
    {
        if(vlanList == null)
        {
            throw new OBException(OBDefine.LOGFILE_SYSTEM, "failed to get valn info");
        }

        final List<String> parsedString = new ArrayList<String>();
        for(OBNameValue obj : vlanList)
        {
            String rawVlanInfo = obj.getValue();
            String vlanInfo = rawVlanInfo.substring(rawVlanInfo.lastIndexOf("/") + 1);
            parsedString.add(vlanInfo);
        }

        return formatter(parsedString);
    }
}
