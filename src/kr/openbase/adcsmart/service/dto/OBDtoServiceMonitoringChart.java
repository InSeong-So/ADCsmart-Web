package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;

public class OBDtoServiceMonitoringChart
{
    private ArrayList<OBDtoBpsConnData> bpsConn;
    private ArrayList<OBDtoNameCurAvgMax> bpsInfo;
    private ArrayList<OBDtoNameCurAvgMax> connInfo;    
    
    @Override
    public String toString()
    {
        return "OBDtoServiceMonitoringChart [bpsConn=" + bpsConn + ", bpsInfo="
                + bpsInfo + ", connInfo=" + connInfo + "]";
    }

    public ArrayList<OBDtoBpsConnData> getBpsConn()
    {
        return bpsConn;
    }

    public void setBpsConn(ArrayList<OBDtoBpsConnData> bpsConn)
    {
        this.bpsConn = bpsConn;
    }

    public ArrayList<OBDtoNameCurAvgMax> getBpsInfo()
    {
        return bpsInfo;
    }

    public void setBpsInfo(ArrayList<OBDtoNameCurAvgMax> bpsInfo)
    {
        this.bpsInfo = bpsInfo;
    }

    public ArrayList<OBDtoNameCurAvgMax> getConnInfo()
    {
        return connInfo;
    }

    public void setConnInfo(ArrayList<OBDtoNameCurAvgMax> connInfo)
    {
        this.connInfo = connInfo;
    }
}
