package kr.openbase.adcsmart.service.dto;

import java.sql.Timestamp;

public class OBDtoSyncSystemTime
{   
    private Integer     useNTP_YN = 0;   // NTP 사용 여부 확인 (Data get일때만 사용) 0: 미사용, 1: 사용
    private Integer     timeSyncType;    // 0 : 사용안함,   1: manually use,   2: NTP use
    private Timestamp   manuallyTime;
    private String      primary_NTP;
    private String      secondary_NTP;
    private Integer     intervalNTPSync;
     
    public Integer getUseNTP_YN()
    {
        return useNTP_YN;
    }
    public void setUseNTP_YN(Integer useNTP_YN)
    {
        this.useNTP_YN = useNTP_YN;
    }
    public Integer getTimeSyncType()
    {
        return timeSyncType;
    }
    public void setTimeSyncType(Integer timeSyncType)
    {
        this.timeSyncType = timeSyncType;
    }
    public Timestamp getManuallyTime()
    {
        return manuallyTime;
    }
    public void setManuallyTime(Timestamp manuallyTime)
    {
        this.manuallyTime = manuallyTime;
    }
    public String getPrimary_NTP()
    {
        return primary_NTP;
    }
    public void setPrimary_NTP(String primary_NTP)
    {
        this.primary_NTP = primary_NTP;
    }
    public String getSecondary_NTP()
    {
        return secondary_NTP;
    }
    public void setSecondary_NTP(String secondary_NTP)
    {
        this.secondary_NTP = secondary_NTP;
    }
    public Integer getIntervalNTPSync()
    {
        return intervalNTPSync;
    }
    public void setIntervalNTPSync(Integer intervalNTPSync)
    {
        this.intervalNTPSync = intervalNTPSync;
    }
    @Override
    public String toString()
    {
        return "OBDtoSyncSystemTime [useNTP_YN=" + useNTP_YN + ", timeSyncType=" + timeSyncType + ", manuallyTime=" + manuallyTime + ", primary_NTP=" + primary_NTP + ", secondary_NTP=" + secondary_NTP + ", intervalNTPSync=" + intervalNTPSync + "]";
    }
}