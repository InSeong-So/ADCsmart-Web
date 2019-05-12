package kr.openbase.adcsmart.service.snmp.alteon.dto;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBNameValue;

public class OBDtoRptInspectionSnmpAlteon
{
    private ArrayList<OBNameValue> linkUpInfoList;
    private ArrayList<OBNameValue> linkSpeedInfoList;
    private ArrayList<OBNameValue> linkDuplexInfoList;
    private ArrayList<OBNameValue> linkErrorInList;
    private ArrayList<OBNameValue> linkErrorOutInfoList;
    private ArrayList<OBNameValue> linkDiscardsInList;
    private ArrayList<OBNameValue> linkDiscardsOutList;
    
    private ArrayList<OBDtoRptFilterInfo> filterInfo;
    private ArrayList<OBNameValue> realServerStatusList;
    private ArrayList<OBNameValue> virtualServerStatusList;
    @Override
    public String toString()
    {
        return "OBDtoRptInspectionSnmpAlteon [linkUpInfoList=" + linkUpInfoList + ", linkSpeedInfoList=" + linkSpeedInfoList + ", linkDuplexInfoList=" + linkDuplexInfoList + ", linkErrorInList=" + linkErrorInList + ", linkErrorOutInfoList=" + linkErrorOutInfoList + ", linkDiscardsInList=" + linkDiscardsInList + ", linkDiscardsOutList=" + linkDiscardsOutList + ", filterInfo=" + filterInfo + ", realServerStatusList=" + realServerStatusList + ", virtualServerStatusList=" + virtualServerStatusList + "]";
    }
    public ArrayList<OBNameValue> getLinkUpInfoList()
    {
        return linkUpInfoList;
    }
    public void setLinkUpInfoList(ArrayList<OBNameValue> linkUpInfoList)
    {
        this.linkUpInfoList = linkUpInfoList;
    }
    public ArrayList<OBNameValue> getLinkSpeedInfoList()
    {
        return linkSpeedInfoList;
    }
    public void setLinkSpeedInfoList(ArrayList<OBNameValue> linkSpeedInfoList)
    {
        this.linkSpeedInfoList = linkSpeedInfoList;
    }
    public ArrayList<OBNameValue> getLinkDuplexInfoList()
    {
        return linkDuplexInfoList;
    }
    public void setLinkDuplexInfoList(ArrayList<OBNameValue> linkDuplexInfoList)
    {
        this.linkDuplexInfoList = linkDuplexInfoList;
    }
    public ArrayList<OBNameValue> getLinkErrorInList()
    {
        return linkErrorInList;
    }
    public void setLinkErrorInList(ArrayList<OBNameValue> linkErrorInList)
    {
        this.linkErrorInList = linkErrorInList;
    }
    public ArrayList<OBNameValue> getLinkErrorOutInfoList()
    {
        return linkErrorOutInfoList;
    }
    public void setLinkErrorOutInfoList(ArrayList<OBNameValue> linkErrorOutInfoList)
    {
        this.linkErrorOutInfoList = linkErrorOutInfoList;
    }
    public ArrayList<OBNameValue> getLinkDiscardsInList()
    {
        return linkDiscardsInList;
    }
    public void setLinkDiscardsInList(ArrayList<OBNameValue> linkDiscardsInList)
    {
        this.linkDiscardsInList = linkDiscardsInList;
    }
    public ArrayList<OBNameValue> getLinkDiscardsOutList()
    {
        return linkDiscardsOutList;
    }
    public void setLinkDiscardsOutList(ArrayList<OBNameValue> linkDiscardsOutList)
    {
        this.linkDiscardsOutList = linkDiscardsOutList;
    }
    public ArrayList<OBDtoRptFilterInfo> getFilterInfo()
    {
        return filterInfo;
    }
    public void setFilterInfo(ArrayList<OBDtoRptFilterInfo> filterInfo)
    {
        this.filterInfo = filterInfo;
    }
    public ArrayList<OBNameValue> getRealServerStatusList()
    {
        return realServerStatusList;
    }
    public void setRealServerStatusList(ArrayList<OBNameValue> realServerStatusList)
    {
        this.realServerStatusList = realServerStatusList;
    }
    public ArrayList<OBNameValue> getVirtualServerStatusList()
    {
        return virtualServerStatusList;
    }
    public void setVirtualServerStatusList(ArrayList<OBNameValue> virtualServerStatusList)
    {
        this.virtualServerStatusList = virtualServerStatusList;
    }    
}
