package kr.openbase.adcsmart.service.dto;

import java.sql.Timestamp;

public class OBDtoSlbUser
{
    private Integer index;
    private String name;
    private String team;
    private String phone;
    private Integer type; //1. 요청자, 2. 수신자
    private Timestamp updateTime;
    
    @Override
    public String toString()
    {
        return "OBDtoSlbUser [index=" + index + ", name=" + name + ", team="
                + team + ", phone=" + phone + ", type=" + type
                + ", updateTime=" + updateTime + "]";
    }
    public Integer getIndex()
    {
        return index;
    }
    public void setIndex(Integer index)
    {
        this.index = index;
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
    public Integer getType()
    {
        return type;
    }
    public void setType(Integer type)
    {
        this.type = type;
    }
    public Timestamp getUpdateTime()
    {
        return updateTime;
    }
    public void setUpdateTime(Timestamp updateTime)
    {
        this.updateTime = updateTime;
    }
}
