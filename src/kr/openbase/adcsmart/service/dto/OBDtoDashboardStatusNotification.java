package kr.openbase.adcsmart.service.dto;

public class OBDtoDashboardStatusNotification
{
    private String targetIndex;
    private String targetName;
    private String targetIp;
    private int adcIndex;
    private String poolIndex;
    private int status;
    
    public String getTargetIndex()
    {
        return targetIndex;
    }
    public void setTargetIndex(String targetIndex)
    {
        this.targetIndex = targetIndex;
    }
    public String getPoolIndex()
    {
        return poolIndex;
    }
    public void setPoolIndex(String poolIndex)
    {
        this.poolIndex = poolIndex;
    }
    public String getTargetName()
    {
        return targetName;
    }
    public void setTargetName(String targetName)
    {
        this.targetName = targetName;
    }
    public String getTargetIp()
    {
        return targetIp;
    }
    public void setTargetIp(String targetIp)
    {
        this.targetIp = targetIp;
    }
    public int getStatus()
    {
        return status;
    }
    public void setStatus(int status)
    {
        this.status = status;
    }
    public int getAdcIndex()
    {
        return adcIndex;
    }
    public void setAdcIndex(int adcIndex)
    {
        this.adcIndex = adcIndex;
    }
    @Override
    public String toString()
    {
        return "OBDtoDashboardStatusNotification [targetIndex=" + targetIndex
                + ", targetName=" + targetName + ", targetIp=" + targetIp
                + ", adcIndex=" + adcIndex + ", poolIndex=" + poolIndex
                + ", status=" + status + "]";
    }  
}