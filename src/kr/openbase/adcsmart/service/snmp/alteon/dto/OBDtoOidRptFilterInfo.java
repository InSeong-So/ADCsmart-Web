package kr.openbase.adcsmart.service.snmp.alteon.dto;

public class OBDtoOidRptFilterInfo
{
    private String portState;
    private String portFiltBmap;
    
    @Override
    public String toString()
    {
        return "OBDtoOidRptFilterInfo [portState=" + portState + ", portFiltBmap=" + portFiltBmap + "]";
    }
    public String getPortState()
    {
        return portState;
    }
    public void setPortState(String portState)
    {
        this.portState = portState;
    }
    public String getPortFiltBmap()
    {
        return portFiltBmap;
    }
    public void setPortFiltBmap(String portFiltBmap)
    {
        this.portFiltBmap = portFiltBmap;
    }
}
