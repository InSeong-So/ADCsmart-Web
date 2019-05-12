package kr.openbase.adcsmart.web.report.inspection.dto;

/**
 * 언어 변경을 위해 필요한 DTO 이다. 현재 사용하지 않고 있다.
 * 
 * @author 최영조
 */
public class OBDtoInspectionReporHeader
{
    // public static final int ADC_TYPE_F5 = 1;
    // public static final int ADC_TYPE_ALTEON = 2;

    private int             adcType;            // ADC 장비 종류 (F5 / Alteon)
    private String          adcName;            // ADC 이름
    private String          modelName;          // 모델명
    private String          osVersion;          // OS Version
    private String          hostName;           // Host 명, F5 정기점검 보고서에서만 쓰인다.
    private String          serialNumber;       // Serial No.
    private String          ipAddr;             // IP Address
    private String          macAddr;            // MAC Address, Alteon 정기점검 보고서에서만 쓰인다.

    public int getAdcType()
    {
        return adcType;
    }

    public void setAdcType(int adcType)
    {
        this.adcType = adcType;
    }

    public String getAdcName()
    {
        return adcName;
    }

    public void setAdcName(String adcName)
    {
        this.adcName = adcName;
    }

    public String getModelName()
    {
        return modelName;
    }

    public void setModelName(String modelName)
    {
        this.modelName = modelName;
    }

    public String getOsVersion()
    {
        return osVersion;
    }

    public void setOsVersion(String osVersion)
    {
        this.osVersion = osVersion;
    }

    public String getHostName()
    {
        return hostName;
    }

    public void setHostName(String hostName)
    {
        this.hostName = hostName;
    }

    public String getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    public String getIpAddr()
    {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr)
    {
        this.ipAddr = ipAddr;
    }

    public String getMacAddr()
    {
        return macAddr;
    }

    public void setMacAddr(String macAddr)
    {
        this.macAddr = macAddr;
    }
}
