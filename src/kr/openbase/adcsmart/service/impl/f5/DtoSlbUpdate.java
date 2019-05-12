package kr.openbase.adcsmart.service.impl.f5;

public class DtoSlbUpdate
{
    private boolean nodeInfo;
    private boolean poolmemberInfo; //pool과 member 정보 
    private boolean vsInfo;
    private Integer status; // member 상태와  vsStatus, vs 상태는 시간이 많이 걸리지 않으므로 포함시켰다. 실제로 전체 상태를 가져오는 작업이 많다.

    //constant - status 업데이트 범위
    final Integer STATUS_UPDATE_NONE = 0;
    final Integer STATUS_UPDATE_ALL = 1;
    final Integer STATUS_UPDATE_VS = 2;

    public boolean isDoNothing()
    {
        if((this.nodeInfo || this.poolmemberInfo || this.vsInfo)==false && this.status.equals(STATUS_UPDATE_NONE))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public DtoSlbUpdate() //boolean은 만들어지면 false지만 확인사살  
    {
        this.nodeInfo = false;
        this.poolmemberInfo = false;
        this.vsInfo = false;
        this.status = STATUS_UPDATE_NONE;
    }

    @Override
    public String toString()
    {
        return "DtoSlbUpdate [nodeInfo=" + nodeInfo + ", poolmemberInfo=" + poolmemberInfo + ", vsInfo=" + vsInfo + ", status=" + status + "]";
    }
    public boolean isNodeInfo()
    {
        return nodeInfo;
    }
    public void setNodeInfo(boolean nodeInfo)
    {
        this.nodeInfo = nodeInfo;
    }
    public boolean isPoolmemberInfo()
    {
        return poolmemberInfo;
    }
    public void setPoolmemberInfo(boolean poolmemberInfo)
    {
        this.poolmemberInfo = poolmemberInfo;
    }
    public boolean isVsInfo()
    {
        return vsInfo;
    }
    public void setVsInfo(boolean vsInfo)
    {
        this.vsInfo = vsInfo;
    }
    public Integer getStatus()
    {
        return status;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }
}
