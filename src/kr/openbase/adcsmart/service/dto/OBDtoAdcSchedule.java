package kr.openbase.adcsmart.service.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigChunkAlteon;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigChunkF5;

public class OBDtoAdcSchedule
{
    private Integer index;
    private Timestamp occurTime;
    private Integer originUser;
    private String name;
    private String team;
    private String phone;
    private String description;
    private Integer state;
    private Integer notice;
    private String smsReceive;
    private Integer adcIndex;
    private Integer adcType;
    private String vsIndex;
    private String vsName;
    private String vsIp;
    private Timestamp reservationTime;
    private Date reservedTime;
    private Integer reservedHour;
    private Integer reservedMin;
    private String accntIp;
    private Integer accntIndex;
    private Integer changeType;  // 1 : add, 2 : edit, 3 : delete
    private Integer changeYN; // Only alteon
    private Integer changeObjectType;
    private OBDtoAdcConfigChunkAlteon chunkAlteon;
    private OBDtoAdcConfigChunkF5 chunkF5;
    private ArrayList<String> userInfo;
    private ArrayList<String> userName;
    private ArrayList<String> userPhone;
    
    private String userNmHp;
    private String userNm;
    
    private String summary;
    
    @Override
    public String toString()
    {
        return "OBDtoAdcSchedule [index=" + index + ", occurTime=" + occurTime
                + ", originUser=" + originUser + ", name=" + name + ", team="
                + team + ", phone=" + phone + ", description=" + description
                + ", state=" + state + ", notice=" + notice + ", smsReceive="
                + smsReceive + ", adcIndex=" + adcIndex + ", adcType="
                + adcType + ", vsIndex=" + vsIndex + ", vsName=" + vsName
                + ", vsIp=" + vsIp + ", reservationTime=" + reservationTime
                + ", reservedTime=" + reservedTime + ", reservedHour="
                + reservedHour + ", reservedMin=" + reservedMin + ", accntIp="
                + accntIp + ", accntIndex=" + accntIndex + ", changeType="
                + changeType + ", changeYN=" + changeYN + ", changeObjectType="
                + changeObjectType + ", chunkAlteon=" + chunkAlteon
                + ", chunkF5=" + chunkF5 + ", userName=" + userName
                + ", userPhone=" + userPhone + ", summary=" + summary + "]";
    }
    
    public Integer getIndex()
    {
        return index;
    }
    public void setIndex(Integer index)
    {
        this.index = index;
    }
    public Timestamp getOccurTime()
    {
        return occurTime;
    }
    public void setOccurTime(Timestamp occurTime)
    {
        this.occurTime = occurTime;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getTeam()
    {
        return team;
    }
    public void setTeam(String team)
    {
        this.team = team;
    }
    public String getPhone()
    {
        return phone;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    public Integer getNotice()
    {
        return notice;
    }
    public void setNotice(Integer notice)
    {
        this.notice = notice;
    }
    public String getSmsReceive()
    {
        return smsReceive;
    }
    public void setSmsReceive(String smsReceive)
    {                       
        this.smsReceive = smsReceive;
    }
    public Integer getAdcIndex()
    {
        return adcIndex;
    }

    public void setAdcIndex(Integer adcIndex)
    {
        this.adcIndex = adcIndex;
    }

    public String getVsIndex()
    {
        return vsIndex;
    }

    public void setVsIndex(String vsIndex)
    {
        this.vsIndex = vsIndex;
    }

    public String getVsName()
    {
        return vsName;
    }

    public void setVsName(String vsName)
    {
        this.vsName = vsName;
    }

    public String getVsIp()
    {
        return vsIp;
    }

    public void setVsIp(String vsIp)
    {
        this.vsIp = vsIp;
    }

    public Timestamp getReservationTime()
    {
        return reservationTime;
    }

    public void setReservationTime(Timestamp reservationTime)
    {
        this.reservationTime = reservationTime;
    }

    public OBDtoAdcConfigChunkAlteon getChunkAlteon()
    {
        return chunkAlteon;
    }

    public void setChunkAlteon(OBDtoAdcConfigChunkAlteon chunkAlteon)
    {
        this.chunkAlteon = chunkAlteon;
    }

    public OBDtoAdcConfigChunkF5 getChunkF5()
    {
        return chunkF5;
    }

    public void setChunkF5(OBDtoAdcConfigChunkF5 chunkF5)
    {
        this.chunkF5 = chunkF5;
    }

    public String getSummary()
    {
        return summary;
    }

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public String getAccntIp()
    {
        return accntIp;
    }

    public void setAccntIp(String accntIp)
    {
        this.accntIp = accntIp;
    }

    public Integer getAccntIndex()
    {
        return accntIndex;
    }

    public void setAccntIndex(Integer accntIndex)
    {
        this.accntIndex = accntIndex;
    }

    public Integer getChangeYN()
    {
        return changeYN;
    }

    public void setChangeYN(Integer changeYN)
    {
        this.changeYN = changeYN;
    }

    public Integer getChangeType()
    {
        return changeType;
    }

    public void setChangeType(Integer changeType)
    {
        this.changeType = changeType;
    }

    public Integer getChangeObjectType()
    {
        return changeObjectType;
    }

    public void setChangeObjectType(Integer changeObjectType)
    {
        this.changeObjectType = changeObjectType;
    }

    public Integer getAdcType()
    {
        return adcType;
    }

    public void setAdcType(Integer adcType)
    {
        this.adcType = adcType;
    }

    public Integer getOriginUser()
    {
        return originUser;
    }

    public void setOriginUser(Integer originUser)
    {
        this.originUser = originUser;
    }

    public Date getReservedTime()
    {
        return reservedTime;
    }

    public void setReservedTime(Date reservedTime)
    {
        this.reservedTime = reservedTime;
    }

    public Integer getReservedHour()
    {
        return reservedHour;
    }

    public void setReservedHour(Integer reservedHour)
    {
        this.reservedHour = reservedHour;
    }

    public Integer getReservedMin()
    {
        return reservedMin;
    }

    public void setReservedMin(Integer reservedMin)
    {
        this.reservedMin = reservedMin;
    }

    public ArrayList<String> getUserName()
    {
        return userName;
    }

    public void setUserName(ArrayList<String> userName)
    {
        this.userName = userName;
    }

    public ArrayList<String> getUserPhone()
    {
        return userPhone;
    }

    public void setUserPhone(ArrayList<String> userPhone)
    {
        this.userPhone = userPhone;
    }

    public ArrayList<String> getUserInfo()
    {
        return userInfo;
    }

    public void setUserInfo(ArrayList<String> userInfo)
    {
        this.userInfo = userInfo;
    }

    public String getUserNmHp()
    {
        return userNmHp;
    }

    public void setUserNmHp(String userNmHp)
    {
        this.userNmHp = userNmHp;
    }

    public String getUserNm()
    {
        return userNm;
    }

    public void setUserNm(String userNm)
    {
        this.userNm = userNm;
    }
}
