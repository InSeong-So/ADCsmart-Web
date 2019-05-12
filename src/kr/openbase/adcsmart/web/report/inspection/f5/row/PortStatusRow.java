package kr.openbase.adcsmart.web.report.inspection.f5.row;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBNameValue;
import kr.openbase.adcsmart.service.snmp.f5.dto.OBDtoRptInspectionSnmpF5;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.f5.handler.F5DataHandler;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;
import kr.openbase.adcsmart.web.report.inspection.utility.Common;

public class PortStatusRow extends AbstractRow
{
    private final F5DataHandler dataHandler;
     private final String swVersion;

    public PortStatusRow(final String swVersion, final F5DataHandler dataHandler)
    {
         this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    @Override
    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        OBDtoInspectionReportRow portStatus = new OBDtoInspectionReportRow();

        try
        {
            if(Common.f5SwVersion(swVersion))
            {
                portStatus.setCheckMethod("#tmsh show net interface");
            }
            else
                portStatus.setCheckMethod("#b interface");
            portStatus.setColumn(col.getThridColumn());
            portStatus.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_L2_PORT_USE_STATE));
            portStatus.setChecklist(checkPortStatus(dataHandler.getFirstInspectionData(), dataHandler.getSecondInspectionData(), dataHandler.getThirdInspectionData()));
            portStatus.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_NORMAL));
            if(!portStatus.getChecklist().isEmpty())
                portStatus.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_ANORMAL));
        }
        catch(Exception e)
        {
            throw new OBException(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, portStatus.toString());
        return portStatus;
    }

    /**
     * 포트의 상태를 판별해준다. 에러 문자가 리턴 된다면 어떤 이유에서인지 수집한 데이터 간의 형식이나 개수가 달라진 것이다.
     * 
     * @param linkInfo1st
     * @param linkInfo2nd
     * @param linkInfo3rd
     * @return
     */
    private String checkPortStatus(OBDtoRptInspectionSnmpF5 linkInfo1st, OBDtoRptInspectionSnmpF5 linkInfo2nd, OBDtoRptInspectionSnmpF5 linkInfo3rd) throws Exception
    {
        String retVal = "";

        // speed 변경.
        ArrayList<OBNameValue> firstInfo = linkInfo1st.getLinkSpeedInfoList();
        ArrayList<OBNameValue> secondInfo = linkInfo2nd.getLinkSpeedInfoList();
        ArrayList<OBNameValue> thirdInfo = linkInfo3rd.getLinkSpeedInfoList();

        if(firstInfo.size() != secondInfo.size())
            return ERROR;
        if(thirdInfo.size() != secondInfo.size())
            return ERROR;

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
            if(!retVal.isEmpty())
                retVal += ", ";
            retVal += String.format("Speed: %s", changedSpeed);
        }

        // duplex 변경 감지
        firstInfo = linkInfo1st.getLinkDuplexInfoList();
        secondInfo = linkInfo2nd.getLinkDuplexInfoList();
        thirdInfo = linkInfo3rd.getLinkDuplexInfoList();

        if(firstInfo.size() != secondInfo.size())
            return ERROR;
        if(thirdInfo.size() != secondInfo.size())
            return ERROR;

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
            if(!retVal.isEmpty())
                retVal += ", ";
            retVal += String.format("Duplex: %s", changedDuplex);
        }

        // errorin+out 변경 감지
        ArrayList<OBNameValue> firstInfoIn = linkInfo1st.getLinkErrorInList();
        ArrayList<OBNameValue> secondInfoIn = linkInfo2nd.getLinkErrorInList();
        ArrayList<OBNameValue> thirdInfoIn = linkInfo3rd.getLinkErrorInList();
        ArrayList<OBNameValue> firstInfoOut = linkInfo1st.getLinkErrorOutInfoList();
        ArrayList<OBNameValue> secondInfoOut = linkInfo2nd.getLinkErrorOutInfoList();
        ArrayList<OBNameValue> thirdInfoOut = linkInfo3rd.getLinkErrorOutInfoList();

        if(firstInfoIn.size() != secondInfoIn.size())
            return ERROR;
        if(secondInfoIn.size() != thirdInfoIn.size())
            return ERROR;
        if(thirdInfoIn.size() != firstInfoOut.size())
            return ERROR;
        if(firstInfoOut.size() != secondInfoOut.size())
            return ERROR;
        if(secondInfoOut.size() != thirdInfoOut.size())
            return ERROR;

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
            if(!retVal.isEmpty())
                retVal += ", ";
            retVal += String.format("Erros: %s", changedError);
        }

        // colls 변경 감지
        firstInfo = linkInfo1st.getLinkCollsInfoList();
        secondInfo = linkInfo2nd.getLinkCollsInfoList();
        thirdInfo = linkInfo3rd.getLinkCollsInfoList();

        if(firstInfo.size() != secondInfo.size())
            return ERROR;
        if(thirdInfo.size() != secondInfo.size())
            return ERROR;

        String changedColls = "";
        for(int i = 0; i < firstInfo.size(); i++)
        {
            OBNameValue firstObj = firstInfo.get(i);
            OBNameValue secondObj = secondInfo.get(i);
            OBNameValue thirdObj = thirdInfo.get(i);
            if(firstObj.getValue().equals(secondObj.getValue()) && secondObj.getValue().equals(thirdObj.getValue()))
            {
                continue;
            }
            if(!changedColls.isEmpty())
                changedDuplex += ", ";
            changedColls += firstObj.getName();
        }
        if(!changedColls.isEmpty())
        {
            if(!retVal.isEmpty())
                retVal += ", ";
            retVal += String.format("Colls: %s", changedColls);
        }

        return retVal;
    }

}
