package kr.openbase.adcsmart.web.report.inspection.alteon.row;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBNameValue;
import kr.openbase.adcsmart.service.snmp.alteon.dto.OBDtoRptInspectionSnmpAlteon;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.alteon.handler.AlteonDataHandler;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;

public class PortStateRow extends AbstractRow
{
    private final AlteonDataHandler dataHandler;
    // private final String swVersion;

    public PortStateRow(final String swVersion, final AlteonDataHandler dataHandler)
    {
        // this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    @Override
    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        final OBDtoInspectionReportRow reportRowDto = new OBDtoInspectionReportRow();

        reportRowDto.setColumn(col.getSecondColumn());
        reportRowDto.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_L2_PORT_USE_STATE));
        reportRowDto.setCheckMethod("/stat/port #/if");

        String prefix = "";
        String validCheckStr = "";

        try
        {
            final OBDtoRptInspectionSnmpAlteon rptInspection1st = dataHandler.getRptInspection1st();
            final OBDtoRptInspectionSnmpAlteon rptInspection2nd = dataHandler.getRptInspection2nd();
            final OBDtoRptInspectionSnmpAlteon rptInspection3rd = dataHandler.getRptInspection3rd();

            if(rptInspection1st == null || rptInspection2nd == null || rptInspection3rd == null)
            {
                throw new OBException(OBDefine.LOGFILE_SYSTEM, "failed to get port state");
            }

            ArrayList<OBNameValue> firstInfo = rptInspection1st.getLinkSpeedInfoList();
            ArrayList<OBNameValue> secondInfo = rptInspection2nd.getLinkSpeedInfoList();
            ArrayList<OBNameValue> thirdInfo = rptInspection3rd.getLinkSpeedInfoList();

            if(firstInfo.size() != secondInfo.size())
            {
                validCheckStr = validCheckStr + prefix + "data mismatching";
                prefix = ", ";
            }

            if(thirdInfo.size() != secondInfo.size())
            {
                validCheckStr = validCheckStr + prefix + "data mismatching";
                prefix = ", ";
            }

            String changedSpeed = "";
            for(int i = 0; i < firstInfo.size(); i++)
            {
                OBNameValue firstObj = firstInfo.get(i);
                OBNameValue secondObj = secondInfo.get(i);
                OBNameValue thirdObj = thirdInfo.get(i);
                if(firstObj.getValue().equals(secondObj.getValue()) && secondObj.getValue().equals(thirdObj.getValue()))
                {
                    continue;
                }
                if(!changedSpeed.isEmpty())
                    changedSpeed += ", ";
                changedSpeed += firstObj.getName();
            }
            if(!changedSpeed.isEmpty())
            {
                if(!validCheckStr.isEmpty())
                    validCheckStr += ", ";
                validCheckStr += String.format("Speed: %s", changedSpeed);
            }

            // duplex 변경 감지
            firstInfo = rptInspection1st.getLinkDuplexInfoList();
            secondInfo = rptInspection2nd.getLinkDuplexInfoList();
            thirdInfo = rptInspection3rd.getLinkDuplexInfoList();

            if(firstInfo.size() != secondInfo.size())
            {
                validCheckStr = validCheckStr + prefix + "data mismatching";
                prefix = ", ";
            }

            if(thirdInfo.size() != secondInfo.size())
            {
                validCheckStr = validCheckStr + prefix + "data mismatching";
                prefix = ", ";
            }

            String changedDuplex = "";
            for(int i = 0; i < firstInfo.size(); i++)
            {
                OBNameValue firstObj = firstInfo.get(i);
                OBNameValue secondObj = secondInfo.get(i);
                OBNameValue thirdObj = thirdInfo.get(i);
                if(firstObj.getValue().equals(secondObj.getValue()) && secondObj.getValue().equals(thirdObj.getValue()))
                {
                    continue;
                }
                if(!changedDuplex.isEmpty())
                    changedDuplex += ", ";
                changedDuplex += firstObj.getName();
            }
            if(!changedDuplex.isEmpty())
            {
                if(!validCheckStr.isEmpty())
                    validCheckStr += ", ";
                validCheckStr += String.format("Duplex: %s", changedDuplex);
            }

            // errorin+out 변경 감지
            ArrayList<OBNameValue> firstInfoIn = rptInspection1st.getLinkErrorInList();
            ArrayList<OBNameValue> secondInfoIn = rptInspection2nd.getLinkErrorInList();
            ArrayList<OBNameValue> thirdInfoIn = rptInspection3rd.getLinkErrorInList();
            ArrayList<OBNameValue> firstInfoOut = rptInspection1st.getLinkErrorOutInfoList();
            ArrayList<OBNameValue> secondInfoOut = rptInspection2nd.getLinkErrorOutInfoList();
            ArrayList<OBNameValue> thirdInfoOut = rptInspection3rd.getLinkErrorOutInfoList();

            if(firstInfoIn.size() != secondInfoIn.size())
            {
                validCheckStr = validCheckStr + prefix + "data mismatching";
                prefix = ", ";
            }
            if(secondInfoIn.size() != thirdInfoIn.size())
            {
                validCheckStr = validCheckStr + prefix + "data mismatching";
                prefix = ", ";
            }
            if(thirdInfoIn.size() != firstInfoOut.size())
            {
                validCheckStr = validCheckStr + prefix + "data mismatching";
                prefix = ", ";
            }
            if(firstInfoOut.size() != secondInfoOut.size())
            {
                validCheckStr = validCheckStr + prefix + "data mismatching";
                prefix = ", ";
            }
            if(secondInfoOut.size() != thirdInfoOut.size())
            {
                validCheckStr = validCheckStr + prefix + "data mismatching";
                prefix = ", ";
            }

            String changedError = "";
            for(int i = 0; i < firstInfo.size(); i++)
            {
                long first = Long.parseLong(firstInfoIn.get(i).getValue()) + Long.parseLong(firstInfoOut.get(i).getValue());
                long second = Long.parseLong(secondInfoIn.get(i).getValue()) + Long.parseLong(secondInfoOut.get(i).getValue());
                long third = Long.parseLong(thirdInfoIn.get(i).getValue()) + Long.parseLong(thirdInfoOut.get(i).getValue());
                if(first == second && second == third)
                {
                    continue;
                }
                if(!changedError.isEmpty())
                    changedError += ", ";
                changedError += firstInfo.get(i).getName();
            }
            if(!changedError.isEmpty())
            {
                if(!validCheckStr.isEmpty())
                    validCheckStr += ", ";
                validCheckStr += String.format("Erros: %s", changedError);
            }

            reportRowDto.setChecklist(validCheckStr);
            reportRowDto.setResult(validCheckStr.isEmpty() ? OBMessages.getMessage(OBMessages.MSG_RPT_NORMAL) : OBMessages.getMessage(OBMessages.MSG_RPT_ANORMAL));
        }
        catch(Exception e)
        {
            throw new OBException(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, reportRowDto.toString());
        return reportRowDto;
    }
}
