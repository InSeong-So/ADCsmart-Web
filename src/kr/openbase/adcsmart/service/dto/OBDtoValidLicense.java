package kr.openbase.adcsmart.service.dto;

public class OBDtoValidLicense
{
    private Integer msgKey;
    private String deviceModel;
    private Integer maxAdc;    
    
    public Integer getMsgKey()
    {
        return msgKey;
    }
    
    public void setMsgKey(Integer msgKey)
    {
        this.msgKey = msgKey;
    }
    
    public String getDeviceModel()
    {
        return deviceModel;
    }
    
    public void setDeviceModel(String deviceModel)
    {
        this.deviceModel = deviceModel;
    }

    public Integer getMaxAdc()
    {
        return maxAdc;
    }

    public void setMaxAdc(Integer maxAdc)
    {
        this.maxAdc = maxAdc;
    }

    @Override
    public String toString()
    {
        return "OBDtoValidLicense [msgKey=" + msgKey + ", deviceModel=" + deviceModel + ", maxAdc=" + maxAdc + "]";
    }
    
    
}
