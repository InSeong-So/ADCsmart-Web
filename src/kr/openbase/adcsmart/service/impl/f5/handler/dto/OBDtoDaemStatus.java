package kr.openbase.adcsmart.service.impl.f5.handler.dto;

public class OBDtoDaemStatus
{
    public final static int STATUS_RUN  = 1;//
    public final static int STATUS_DOWN = 2;//
    private String processName = "";
    private int    status   = STATUS_DOWN;
    
    @Override
    public String toString()
    {
        return "OBDtoDaemStatus [processName=" + processName + ", status=" + status + "]";
    }
    public String getProcessName()
    {
        return processName;
    }
    public void setProcessName(String processName)
    {
        this.processName = processName;
    }
    public int getStatus()
    {
        return status;
    }
    public void setStatus(int status)
    {
        this.status = status;
    }
}
