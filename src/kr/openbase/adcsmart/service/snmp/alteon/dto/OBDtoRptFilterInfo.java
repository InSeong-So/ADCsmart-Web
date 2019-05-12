package kr.openbase.adcsmart.service.snmp.alteon.dto;

import java.util.ArrayList;

public class OBDtoRptFilterInfo
{
    private int portIndex;
    private ArrayList<Integer> filtList;
    
    @Override
    public String toString()
    {
        return "OBDtoRptFilterInfo [portIndex=" + portIndex + ", filtList=" + filtList + "]";
    }
    public int getPortIndex()
    {
        return portIndex;
    }
    public void setPortIndex(int portIndex)
    {
        this.portIndex = portIndex;
    }
    public ArrayList<Integer> getFiltList()
    {
        return filtList;
    }
    public void setFiltList(ArrayList<Integer> filtList)
    {
        this.filtList = filtList;
    }
}
