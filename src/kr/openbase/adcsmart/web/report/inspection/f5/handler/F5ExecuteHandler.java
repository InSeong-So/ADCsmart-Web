package kr.openbase.adcsmart.web.report.inspection.f5.handler;

import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.impl.f5.handler.OBAdcF5Handler;
import kr.openbase.adcsmart.service.impl.f5.handler.OBCLIParserF5;
import kr.openbase.adcsmart.service.impl.f5.handler.dto.OBDtoDaemStatus;
import kr.openbase.adcsmart.service.impl.f5.handler.dto.OBDtoNtpInfoF5;
import kr.openbase.adcsmart.service.snmp.f5.OBSnmpF5;
import kr.openbase.adcsmart.service.snmp.f5.dto.OBDtoRptInspectionSnmpF5;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;

public class F5ExecuteHandler
{
    private OBDtoAdcInfo          adcInfo;
    private OBSnmpF5              snmp;
    private OBDtoNtpInfoF5        ntpInfo;
    private List<OBDtoDaemStatus> daemonList;
    private String                daemonString;
    private String                ntpString;

    public F5ExecuteHandler(OBDtoAdcInfo adcInfo)
    {
        this.adcInfo = adcInfo;

        OBAdcF5Handler handler;
        try
        {
            snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
            handler = OBCommon.getValidF5Handler(adcInfo.getSwVersion());

            // cli를 이용해서 데이터를 추출한다.
            handler = OBCommon.getValidF5Handler(adcInfo.getSwVersion());
            handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcCliAccount(), adcInfo.getAdcCliPasswordDecrypt(), adcInfo.getConnPort());
            handler.sshLogin();
            daemonString = handler.cmndDaemonStatus();
            ntpString = handler.cmndNtpInfo();
            handler.disconnect();

            OBCLIParserF5 cliParser = OBCommon.getValidF5CLIParser(adcInfo.getSwVersion());
            daemonList = cliParser.parseDaemonStatus(daemonString);
            ntpInfo = cliParser.parseNtpInfo(ntpString);
        }
        catch(OBException e)
        {
            // TODO
        }
        catch(OBExceptionUnreachable e)
        {
            // TODO
        }
        catch(OBExceptionLogin e)
        {
            // TODO
        }
    }

    /**
     * 10초 간격으로 3번 데이터를 추출한다.
     * 
     * @return
     */
    // public List<OBDtoRptInspectionSnmpF5> getLinkInfo()
    // {
    // List<OBDtoRptInspectionSnmpF5> linkInfo = new ArrayList<OBDtoRptInspectionSnmpF5>();
    //
    // // snmp를 이용해서 데이터를 가져온다.
    // try
    // {
    // // snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
    //
    // linkInfo.add(snmp.getRptInspection(adcInfo.getAdcType(), adcInfo.getSwVersion()));
    // OBDateTime.Sleep(INSPECTION_INTERVAL_LINK_INFO);
    // linkInfo.add(snmp.getRptInspectionLinkInfo(adcInfo.getAdcType(), adcInfo.getSwVersion()));
    // OBDateTime.Sleep(INSPECTION_INTERVAL_LINK_INFO);
    // linkInfo.add(snmp.getRptInspectionLinkInfo(adcInfo.getAdcType(), adcInfo.getSwVersion()));
    // }
    // catch(OBException e)
    // {
    // // TODO
    // }
    //
    // return linkInfo;
    // }

    public OBDtoRptInspectionSnmpF5 getInspectionData()
    {
        try
        {
            return snmp.getRptInspection(adcInfo.getAdcType(), adcInfo.getSwVersion());
        }
        catch(OBException e)
        {
            // TODO
        }

        return null;
    }

    public OBDtoRptInspectionSnmpF5 getLinkInfo()
    {
        try
        {
            return snmp.getRptInspectionLinkInfo(adcInfo.getAdcType(), adcInfo.getSwVersion());
        }
        catch(OBException e)
        {
            // TODO
        }

        return null;
    }
    
    public List<OBDtoDaemStatus> getDaemonList()
    {
        return daemonList;
    }

    public OBDtoNtpInfoF5 getNTPInfo()
    {
        return ntpInfo;
    }
}
