package kr.openbase.adcsmart.web.report.inspection.dto;

import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcName;

public class OBDtoRptTitle
{
    private String                  index;
    private Date                    occurTime;             // 보고서 생성 시각.
    private Date                    beginTime;             // 보고서 기간. 시작.
    private Date                    endTime;               // 보고서 종료 시각.
    private String                  userID         = "";
    private Integer                 userIndex      = 0;
    private ArrayList<OBDtoAdcName> adcList;               // null일 경우에는 전체.
    private OBDtoADCObject          targetAdcObjec = null;
    private String                  extraInfo;

    public OBDtoADCObject getTargetAdcObjec()
    {
        return targetAdcObjec;
    }

    public void setTargetAdcObjec(OBDtoADCObject targetAdcObjec)
    {
        this.targetAdcObjec = targetAdcObjec;
    }

    public String getExtraInfo()
    {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo)
    {
        this.extraInfo = extraInfo;
    }

    public String getIndex()
    {
        return index;
    }

    public void setIndex(String index)
    {
        this.index = index;
    }

    public ArrayList<OBDtoAdcName> getAdcList()
    {
        return adcList;
    }

    public void setAdcList(ArrayList<OBDtoAdcName> adcList)
    {
        this.adcList = adcList;
    }

    @Override
    public String toString()
    {
        return "OBDtoRptTitle [index=" + index + ", occurTime=" + occurTime + ", beginTime=" + beginTime + ", endTime=" + endTime + ", userID=" + userID + ", userIndex=" + userIndex + ", adcList=" + adcList + ", targetAdcObjec=" + targetAdcObjec + "]";
    }

    public Date getOccurTime()
    {
        return occurTime;
    }

    public String getUserID()
    {
        return userID;
    }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }

    public void setOccurTime(Date occurTime)
    {
        this.occurTime = occurTime;
    }

    public Date getBeginTime()
    {
        return beginTime;
    }

    public void setBeginTime(Date beginTime)
    {
        this.beginTime = beginTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Integer getUserIndex()
    {
        return userIndex;
    }

    public void setUserIndex(Integer userIndex)
    {
        this.userIndex = userIndex;
    }
}
