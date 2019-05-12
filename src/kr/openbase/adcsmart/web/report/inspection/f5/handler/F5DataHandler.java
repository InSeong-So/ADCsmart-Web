package kr.openbase.adcsmart.web.report.inspection.f5.handler;

import java.util.ArrayList;
import java.util.List;

import org.snmp4j.smi.TimeTicks;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBNameValue;
import kr.openbase.adcsmart.service.impl.f5.handler.dto.OBDtoDaemStatus;
import kr.openbase.adcsmart.service.impl.f5.handler.dto.OBDtoNtpInfoF5;
import kr.openbase.adcsmart.service.snmp.f5.dto.OBDtoRptInspectionSnmpF5;
import kr.openbase.adcsmart.service.utility.OBDateTime;

public class F5DataHandler
{
    private static final int         INSPECTION_INTERVAL_LINK_INFO = 10000; // 10sec

    private F5ExecuteHandler         handler;

    private OBDtoRptInspectionSnmpF5 inspectionData;
    private OBDtoRptInspectionSnmpF5 inspectionDto2nd;                     // Port 상태를 판별하기 위한 필요 정보만 담긴다.
    private OBDtoRptInspectionSnmpF5 inspectionDto3rd;                     // Port 상태를 판별하기 위한 필요 정보만 담긴다.

    private OBDtoNtpInfoF5           ntpInfo;
    private List<OBDtoDaemStatus>    daemonList;

    public F5DataHandler(OBDtoAdcInfo adcInfo)
    {
        this.handler = new F5ExecuteHandler(adcInfo);
    }

    public void load()
    {
        inspectionData = handler.getInspectionData();
        ntpInfo = handler.getNTPInfo();
        daemonList = handler.getDaemonList();

        // 10초 간격으로 데이터를 수집한다. 간격 간의 데이터를 비교하여 정상/비정상 여부를 판단할 수 있다.
        // inspectionDto1st = handler.getLinkInfo();
        OBDateTime.Sleep(INSPECTION_INTERVAL_LINK_INFO);
        inspectionDto2nd = handler.getLinkInfo();
        OBDateTime.Sleep(INSPECTION_INTERVAL_LINK_INFO);
        inspectionDto3rd = handler.getLinkInfo();
    }

    /**
     * 시간에 따른 변화량을 측정하기 위해서 3번 측정한다.
     * 
     * @return OBDtoRptInspectionSnmpF5 첫 번째 수집한 링크(포트) 정보들이 포함 된 보고서 전체 정보
     */
    public OBDtoRptInspectionSnmpF5 getFirstInspectionData()
    {
        return inspectionData;
    }

    /**
     * 시간에 따른 변화량을 측정하기 위해서 3번 측정한다.
     * 
     * @return OBDtoRptInspectionSnmpF5 두 번째 수집한 링크(포트) 정보들이 포함 된 보고서 전체 정보
     */
    public OBDtoRptInspectionSnmpF5 getSecondInspectionData()
    {
        return inspectionDto2nd;
    }

    /**
     * 시간에 따른 변화량을 측정하기 위해서 3번 측정한다.
     * 
     * @return OBDtoRptInspectionSnmpF5 세 번째수집한 링크(포트) 정보들이 포함 된 보고서 전체 정보
     */
    public OBDtoRptInspectionSnmpF5 getThirdInspectionData()
    {
        return inspectionDto3rd;
    }

    public int getHaStatus()
    {
        return inspectionData.getHaStatus();
    }

    public ArrayList<OBNameValue> getPowerStatusList()
    {
        return inspectionData.getPowerStatusList();
    }

    public OBDtoNtpInfoF5 getNTPInfo()
    {
        return ntpInfo;
    }

    public long getConnCurr()
    {
        return inspectionData.getConnCurr();
    }

    public long getConnMax()
    {
        return inspectionData.getConnMax();
    }

    public List<OBNameValue> getVirtualServerStatusList()
    {
        return inspectionData.getVirtualServerStatusList();
    }

    public List<OBNameValue> getPoolMemberStatusList()
    {
        return inspectionData.getPoolMemberStatusList();
    }

    public List<OBNameValue> getGatewayList()
    {
        return inspectionData.getGatewayList();
    }

    public List<OBNameValue> getVlanInfoList()
    {
        return inspectionData.getVlanInfoList();
    }

    public List<OBNameValue> getLinkUpInfoList()
    {
        return inspectionData.getLinkUpInfoList();
    }

    public TimeTicks getUpTime()
    {
        return inspectionData.getUpTime();
    }

    public long getMemTotal()
    {
        return inspectionData.getMemTotal();
    }

    public long getMemUsed()
    {
        return inspectionData.getMemUsed();
    }

    public List<OBNameValue> getCpuLoadStatusList()
    {
        return inspectionData.getCpuLoadStatusList();
    }

    public List<OBNameValue> getFanStatusList()
    {
        return inspectionData.getFanStatusList();
    }

    public List<OBDtoDaemStatus> getDaemonList()
    {
        return daemonList;
    }
    
    public ArrayList<OBNameValue> getChasisTempStatusList()
    {
        return inspectionData.getChassisTempStatusList();
    }
}
