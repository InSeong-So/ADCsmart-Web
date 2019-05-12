package kr.openbase.adcsmart.service.impl.f5.handler.dto;

public class OBDtoNtpInfoF5
{
    private String remoteIPAddress;
    private String referenceID;
    private int     reachTime;
    @Override
    public String toString()
    {
        return "OBDtoNtpInfoF5 [remoteIPAddress=" + remoteIPAddress + ", referenceID=" + referenceID + ", reachTime=" + reachTime + "]";
    }
    public String getRemoteIPAddress()
    {
        return remoteIPAddress;
    }
    public void setRemoteIPAddress(String remoteIPAddress)
    {
        this.remoteIPAddress = remoteIPAddress;
    }
    public String getReferenceID()
    {
        return referenceID;
    }
    public void setReferenceID(String referenceID)
    {
        this.referenceID = referenceID;
    }
    public int getReachTime()
    {
        return reachTime;
    }
    public void setReachTime(int reachTime)
    {
        this.reachTime = reachTime;
    }
}
