package kr.openbase.adcsmart.web.report.inspection.alteon;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.alteon.handler.AlteonDataHandler;
import kr.openbase.adcsmart.web.report.inspection.alteon.parser.MacAddrParser;
import kr.openbase.adcsmart.web.report.inspection.alteon.parser.SerialNumberParser;
import kr.openbase.adcsmart.web.report.inspection.alteon.parser.SoftwareVersionParser;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.CPUMPUsageRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.CPUSPUsageRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.DirectFuncRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.FanStateRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.FilterInfoRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.GatewayStateRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.InterfaceStateRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.L4TrafficPacketLossRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.LastBootTimeRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.LinkUpInfoRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.LinkUpLogRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.NTPInfoRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.PortStateRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.PowerStateRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.RealServerRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.SLBSessionStatsRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.STPSessionRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.SyslogUseStateRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.SystemLogRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.TemperatureInfoRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.TemperatureLogRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.TrunkUseStateRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.VirtualServerStateRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.VlanInfoRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.VrrpLogRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.VrrpStateRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.column.AlteonColumn;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReporHeader;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.manager.collection.CollectionMethod;

/**
 * SNMP method 클래스와 병합해야 한다.
 * 
 * @author 최영조
 */
public class AlteonCollectionMethod implements CollectionMethod
{
    private String            swVersion;
    private AlteonDataHandler handler;
    private OBDtoAdcInfo      adcInfo;

    public AlteonCollectionMethod(OBDtoAdcInfo adcInfo)
    {
        swVersion = adcInfo.getSwVersion();

        handler = new AlteonDataHandler(adcInfo);
        handler.load(); // 데이터를 한 번에 텔넷 로그인을 한 후에 모두 가져온다.

        this.adcInfo = adcInfo;
    }

    /**
     * 순서가 중요하다.
     */
    @Override
    public List<OBDtoInspectionReportRow> getSystemInfo(OBReportInfo rptInfo)
    {
        final List<OBDtoInspectionReportRow> inspectionReportRowDTOList = new ArrayList<OBDtoInspectionReportRow>();
        final AlteonColumn alteonColumn = new AlteonColumn();

        try
        {
            inspectionReportRowDTOList.add(new LastBootTimeRow(swVersion, handler).getRow(alteonColumn));
            inspectionReportRowDTOList.add(new PowerStateRow(swVersion, handler, adcInfo.getModel()).getRow(alteonColumn)); // 모델명이 필요하다.
            inspectionReportRowDTOList.add(new FanStateRow(swVersion, handler, adcInfo.getModel()).getRow(alteonColumn));
            inspectionReportRowDTOList.add(new TemperatureInfoRow(swVersion, handler, adcInfo.getModel()).getRow(alteonColumn));
            inspectionReportRowDTOList.add(new TemperatureLogRow(swVersion, handler, adcInfo.getIndex()).getRow(alteonColumn));
            inspectionReportRowDTOList.add(new SyslogUseStateRow(swVersion, handler).getRow(alteonColumn));
            inspectionReportRowDTOList.add(new CPUSPUsageRow(swVersion, handler).getRow(alteonColumn));
            inspectionReportRowDTOList.add(new CPUMPUsageRow(swVersion, handler).getRow(alteonColumn));
            inspectionReportRowDTOList.add(new LinkUpInfoRow(swVersion, handler).getRow(alteonColumn));
            inspectionReportRowDTOList.add(new LinkUpLogRow(swVersion, handler, adcInfo.getIndex()).getRow(alteonColumn));
            inspectionReportRowDTOList.add(new VlanInfoRow(swVersion, handler).getRow(alteonColumn));
            inspectionReportRowDTOList.add(new STPSessionRow(swVersion, handler).getRow(alteonColumn));
            inspectionReportRowDTOList.add(new TrunkUseStateRow(swVersion, handler).getRow(alteonColumn));
            inspectionReportRowDTOList.add(new PortStateRow(swVersion, handler).getRow(alteonColumn));
            inspectionReportRowDTOList.add(new InterfaceStateRow(swVersion, handler).getRow(alteonColumn));
            inspectionReportRowDTOList.add(new GatewayStateRow(swVersion, handler).getRow(alteonColumn));
            inspectionReportRowDTOList.add(new VrrpStateRow(swVersion, handler).getRow(alteonColumn));            
            inspectionReportRowDTOList.add(new VrrpLogRow(swVersion, handler, adcInfo.getIndex()).getRow(alteonColumn));
            inspectionReportRowDTOList.add(new RealServerRow(swVersion, handler).getRow(alteonColumn));
            inspectionReportRowDTOList.add(new DirectFuncRow(swVersion, handler).getRow(alteonColumn));
            inspectionReportRowDTOList.add(new VirtualServerStateRow(swVersion, handler).getRow(alteonColumn));
            inspectionReportRowDTOList.add(new SLBSessionStatsRow(swVersion, handler).getRow(alteonColumn));
            inspectionReportRowDTOList.add(new L4TrafficPacketLossRow(swVersion, handler).getRow(alteonColumn));            
            inspectionReportRowDTOList.add(new FilterInfoRow(swVersion, handler).getRow(alteonColumn));
            inspectionReportRowDTOList.add(new SystemLogRow(swVersion, handler).getRow(alteonColumn));
            inspectionReportRowDTOList.add(new NTPInfoRow(swVersion, handler).getRow(alteonColumn));
        }
        catch(Exception e)
        {
            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, e.getMessage());
            inspectionReportRowDTOList.clear(); // 하나라도 실패 시 내용을 전부 삭제한다.
        }

        return inspectionReportRowDTOList;
    }

    @Override
    public OBDtoInspectionReporHeader getAdcInfo()
    {
        OBDtoInspectionReporHeader retVal = new OBDtoInspectionReporHeader();

        retVal.setAdcType(adcInfo.getAdcType());
        
        final String adcName = (adcInfo.getName() == null) ? "" : adcInfo.getName();
        retVal.setAdcName(adcName);
        
        final String ipAddr = (adcInfo.getAdcIpAddress() == null) ? "" : adcInfo.getAdcIpAddress();
        retVal.setIpAddr(ipAddr);
        
        final String model = (adcInfo.getModel() == null) ? "" : adcInfo.getModel();
        retVal.setModelName(model);

        String osVersion = "";
        String macAddr = "";
        String serialNumber = "";
        try
        {
            // general info 추출.
            final String cmdSysGeneralResult = handler.getSysGeneral();

            osVersion = new SoftwareVersionParser().parse(cmdSysGeneralResult).get(0);
            macAddr = new MacAddrParser().parse(cmdSysGeneralResult).get(0);
            serialNumber = new SerialNumberParser().parse(cmdSysGeneralResult).get(0);
        }
        catch(Exception e)
        {
            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        retVal.setOsVersion(osVersion);
        retVal.setMacAddr(macAddr);
        retVal.setSerialNumber(serialNumber);

        return retVal;
    }
}
