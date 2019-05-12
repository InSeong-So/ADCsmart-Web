package kr.openbase.adcsmart.web.report.inspection.f5.row;

import java.util.List;

import kr.openbase.adcsmart.service.dto.OBNameValue;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.f5.handler.F5DataHandler;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;
import kr.openbase.adcsmart.web.report.inspection.utility.Common;

public class LinkUpStatusRow extends AbstractRow
{
    private final F5DataHandler dataHandler;
     private final String swVersion;

    public LinkUpStatusRow(final String swVersion, final F5DataHandler dataHandler)
    {
         this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    @Override
    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        OBDtoInspectionReportRow linkUpStatus = new OBDtoInspectionReportRow();
        try
        {
            if(Common.f5SwVersion(swVersion))
            {
                linkUpStatus.setCheckMethod("#tmsh show net interface");
            }
            else
                linkUpStatus.setCheckMethod("#b interface");
            linkUpStatus.setColumn(col.getThridColumn());
            
            linkUpStatus.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_L2_LINK_UP_STATE));
            linkUpStatus.setCheckMethod("#b interface");
            linkUpStatus.setChecklist(checkLinkupStatus(dataHandler.getLinkUpInfoList()));
            linkUpStatus.setResult(NOTHING);
        }
        catch(Exception e)
        {
            throw new OBException(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, linkUpStatus.toString());
        return linkUpStatus;
    }

    private String checkLinkupStatus(List<OBNameValue> linkUpList) throws Exception
    {
        if(linkUpList == null || linkUpList.size() == 0)
        {
            return ERROR;
        }

        String prefix = "";
        String retVal = "";

        for(OBNameValue obj : linkUpList)
        {// TODO. 데이터 축약이 필요함.
            if(obj.getValue().equals("0"))
            {
                retVal = retVal + prefix + obj.getName();
                prefix = ", ";
            }
        }

        if(!retVal.equals(""))
        {
            retVal = OBParser.trimCsv(retVal, CHECKLIST_MAX_STRING, " 외 %d건").replace("\"", "");
        }

        return retVal;
    }
}
