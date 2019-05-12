package kr.openbase.adcsmart.service.dto.fault;

import java.util.Date;

public class OBDtoFaultBpsConnData
{
    private Date    occurTime=null;
    private Long    bpsInValue=-1L; // BPS IN
    private Long    preBpsInValue=-1L; // 전 BPS IN
    private Long    bpsOutValue=-1L; // BPS OUT
    private Long    preBpsOutValue=-1L; // 전 BPS OUT
    private Long    bpsTotValue=-1L; // BPS TOT
    private Long    preBpsTotValue=-1L; // 전 BPS TOT
    private Long    connCurrValue=-1L; // Conn Curr
    private Long    preConnCurrValue=-1L; // 전 Conn Curr
    private Integer respTimeValue=-1; // response time
    private Integer preRespTimeValue=-1; // 전 response time
    
    @Override
    public String toString()
    {
        return "OBDtoFaultBpsConnData [occurTime=" + occurTime
                + ", bpsInValue=" + bpsInValue + ", preBpsInValue="
                + preBpsInValue + ", bpsOutValue=" + bpsOutValue
                + ", preBpsOutValue=" + preBpsOutValue + ", bpsTotValue="
                + bpsTotValue + ", preBpsTotValue=" + preBpsTotValue
                + ", connCurrValue=" + connCurrValue + ", preConnCurrValue="
                + preConnCurrValue + ", respTimeValue=" + respTimeValue
                + ", preRespTimeValue=" + preRespTimeValue + "]";
    }
    public Date getOccurTime()
    {
        return occurTime;
    }
    public void setOccurTime(Date occurTime)
    {
        this.occurTime = occurTime;
    }
    public Long getBpsInValue()
    {
        return bpsInValue;
    }
    public void setBpsInValue(Long bpsInValue)
    {
        this.bpsInValue = bpsInValue;
    }
    public Long getPreBpsInValue()
    {
        return preBpsInValue;
    }
    public void setPreBpsInValue(Long preBpsInValue)
    {
        this.preBpsInValue = preBpsInValue;
    }
    public Long getBpsOutValue()
    {
        return bpsOutValue;
    }
    public void setBpsOutValue(Long bpsOutValue)
    {
        this.bpsOutValue = bpsOutValue;
    }
    public Long getPreBpsOutValue()
    {
        return preBpsOutValue;
    }
    public void setPreBpsOutValue(Long preBpsOutValue)
    {
        this.preBpsOutValue = preBpsOutValue;
    }
    public Long getBpsTotValue()
    {
        return bpsTotValue;
    }
    public void setBpsTotValue(Long bpsTotValue)
    {
        this.bpsTotValue = bpsTotValue;
    }
    public Long getPreBpsTotValue()
    {
        return preBpsTotValue;
    }
    public void setPreBpsTotValue(Long preBpsTotValue)
    {
        this.preBpsTotValue = preBpsTotValue;
    }
    public Long getConnCurrValue()
    {
        return connCurrValue;
    }
    public void setConnCurrValue(Long connCurrValue)
    {
        this.connCurrValue = connCurrValue;
    }
    public Long getPreConnCurrValue()
    {
        return preConnCurrValue;
    }
    public void setPreConnCurrValue(Long preConnCurrValue)
    {
        this.preConnCurrValue = preConnCurrValue;
    }
    public Integer getRespTimeValue()
    {
        return respTimeValue;
    }
    public void setRespTimeValue(Integer respTimeValue)
    {
        this.respTimeValue = respTimeValue;
    }
    public Integer getPreRespTimeValue()
    {
        return preRespTimeValue;
    }
    public void setPreRespTimeValue(Integer preRespTimeValue)
    {
        this.preRespTimeValue = preRespTimeValue;
    }
}
