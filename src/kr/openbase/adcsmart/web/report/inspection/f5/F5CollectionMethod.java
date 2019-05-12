package kr.openbase.adcsmart.web.report.inspection.f5;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.service.impl.OBTelnetCmndExecV2;
import kr.openbase.adcsmart.service.snmp.f5.OBSnmpF5;
import kr.openbase.adcsmart.service.snmp.f5.dto.OBDtoRptInspectionSnmpF5;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReporHeader;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.f5.handler.F5DataHandler;
import kr.openbase.adcsmart.web.report.inspection.f5.row.CPULoadRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.ConnectionInfoRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.FailoverLogRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.FanStatusRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.GatewayStatusRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.HAStatusRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.LinkLogRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.LinkUpStatusRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.MemoryStatusRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.NTPStatusRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.PoolmemberStatusRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.PortStatusRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.PowerStatusRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.ServiceDaemonStatusRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.SystemLogRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.TempStatusRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.UptimeStatusRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.VirtualServerStatusRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.VlanInfoRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.column.F5Column;
import kr.openbase.adcsmart.web.report.inspection.manager.collection.CollectionMethod;

/**
 * 미완성이다. 차후 구조를 변경하여 적용해야 한다.
 * 
 * @author 최영조
 */
public class F5CollectionMethod implements CollectionMethod
{
    private OBDtoAdcInfo adcInfo;

    public F5CollectionMethod(OBDtoAdcInfo adcInfo)
    {
        this.adcInfo = adcInfo;
    }

    @Override
    public OBDtoInspectionReporHeader getAdcInfo()
    {
        final OBDtoInspectionReporHeader retVal = new OBDtoInspectionReporHeader();
        try
        {
            // snmp를 이용해서 데이터를 가져온다.
            OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());// new OBSnmpF5(adcInfo.getAdcIpAddress());

            OBDtoRptInspectionSnmpF5 snmpInfo = snmp.getRptInspection(adcInfo.getAdcType(), adcInfo.getSwVersion());

            retVal.setAdcType(adcInfo.getAdcType());
            retVal.setAdcName(adcInfo.getName());

            String hostName = (snmpInfo.getHostName() == null) ? "" : snmpInfo.getHostName();
            retVal.setHostName(hostName);
            
            retVal.setIpAddr(adcInfo.getAdcIpAddress());
            retVal.setMacAddr("");

            // OSVersion + Hotfix
            String osVersion = adcInfo.getSwVersion();
            final String hotfixNum = snmpInfo.getHotFix();
            if(hotfixNum != null && !hotfixNum.isEmpty())
                osVersion = osVersion + " " + String.format("(%s)", hotfixNum);

            retVal.setOsVersion(osVersion);
            
            String modelName = (snmpInfo.getModel() == null) ? "" : snmpInfo.getModel();
            retVal.setModelName(modelName);
            
            String serialNumber = (snmpInfo.getSerialNo() == null) ? "" : snmpInfo.getSerialNo();
            retVal.setSerialNumber(serialNumber);
        }
        catch(Exception e)
        {
            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        return retVal;
    }

    @Override
    public List<OBDtoInspectionReportRow> getSystemInfo(OBReportInfo rptInfo)
    {
        final List<OBDtoInspectionReportRow> report = new ArrayList<OBDtoInspectionReportRow>();
        final F5Column f5Column = new F5Column();

        // 장비가 살아있는지 확인한다.
        if(!isStandby())
        {
            return report;
        }

        final F5DataHandler dataHandler = new F5DataHandler(adcInfo);
        dataHandler.load();

        String swVersion = adcInfo.getSwVersion();

        try
        {
            report.add(new UptimeStatusRow(swVersion, dataHandler).getRow(f5Column));
            report.add(new PowerStatusRow(swVersion, dataHandler).getRow(f5Column));
            report.add(new FanStatusRow(swVersion, dataHandler).getRow(f5Column));
            report.add(new CPULoadRow(swVersion, dataHandler).getRow(f5Column));
            report.add(new MemoryStatusRow(swVersion, dataHandler).getRow(f5Column));
            report.add(new TempStatusRow(swVersion, dataHandler).getRow(f5Column));
            report.add(new ServiceDaemonStatusRow(swVersion, dataHandler).getRow(f5Column));
            
            report.add(new HAStatusRow(swVersion, dataHandler).getRow(f5Column));
            report.add(new FailoverLogRow(swVersion, dataHandler, adcInfo.getIndex()).getRow(f5Column));

            report.add(new LinkUpStatusRow(swVersion, dataHandler).getRow(f5Column));
            report.add(new LinkLogRow(swVersion, dataHandler, adcInfo.getIndex()).getRow(f5Column));
            report.add(new PortStatusRow(swVersion, dataHandler).getRow(f5Column));
            report.add(new VlanInfoRow(swVersion, dataHandler).getRow(f5Column));
            report.add(new GatewayStatusRow(swVersion, dataHandler).getRow(f5Column));
            
            
            report.add(new PoolmemberStatusRow(swVersion, dataHandler).getRow(f5Column));
            report.add(new VirtualServerStatusRow(swVersion, dataHandler).getRow(f5Column));
            report.add(new ConnectionInfoRow(swVersion, dataHandler).getRow(f5Column));
            
            
            report.add(new NTPStatusRow(swVersion, dataHandler).getRow(f5Column));
            report.add(new SystemLogRow(swVersion, dataHandler).getRow(f5Column));
            
        }
        catch(Exception e)
        {
            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, e.getMessage());
            report.clear(); // 하나라도 실패 시 내용을 전부 삭제한다.
        }

        return report;
    }

    private boolean isStandby()
    {
        boolean isStandby = false;
        try
        {
            isStandby = new OBTelnetCmndExecV2().isReachable(adcInfo.getAdcIpAddress(), adcInfo.getConnPort());
        }
        catch(OBException e)
        {
            // do nothing
        }

        return isStandby;
    }
}
