package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;
import java.util.Date;

public class OBReportInfo
{
    public static final int RPT_TYPE_SYSTEM_OP = 1;// 시스템 운영 보고서
    public static final int RPT_TYPE_SYSTEM_FAULT = 2;// 시스템 장애 보고서
    public static final int RPT_TYPE_L4_DAILY = 3;// L4 운영(일간)보고서
    public static final int RPT_TYPE_L4_WEEKLY = 4;// L4 운영(주간)보고서
    public static final int RPT_TYPE_L4_MONTHLY = 5;// L4 운영(주간)보고서
    public static final int RPT_TYPE_L4_OPERATION = 6;// L4운영보고서
    public static final int RPT_TYPE_ADC_DIAGNOSIS = 7;// ADC진단 보고서
    public static final int RPT_TYPE_SYSTEM_OP_TOTAL = 8;// 전체 운영 보고서

    public static final int FILE_TYPE_PDF = 1;
    public static final int FILE_TYPE_DOC = 2;
    public static final int FILE_TYPE_XLS = 3;
    public static final int FILE_TYPE_PPT = 4;
    public static final int FILE_TYPE_RTF = 5;

    public static final int STATUS_INIT = 1;
    public static final int STATUS_RUNNING = 2;
    public static final int STATUS_COMPLETE = 3;
    public static final int STATUS_ERROR = 4;

    private String index;
    private Integer groupIndex;
    private Date occurTime;
    private Integer status;
    private String name;
    private Date beginTime;
    private Date endTime;
    private Integer rptType;// 1: 시스템 운영 보고서, 2: 시스템 장애 보고서
    private Integer fileType;// 1: pdf, 2:doc, 3:xls, 4:ppt
    private ArrayList<OBDtoAdcName> adcList;

    private OBDtoADCObject targetObject;

    private Integer accountIndex;
    private String accountID;
    private String fileName;

    private String extraInfo;

    @Override
    public String toString()
    {
        return "OBReportInfo [index=" + index + ", groupIndex=" + groupIndex
                + ", occurTime=" + occurTime + ", status=" + status + ", name="
                + name + ", beginTime=" + beginTime + ", endTime=" + endTime
                + ", rptType=" + rptType + ", fileType=" + fileType
                + ", adcList=" + adcList + ", targetObject=" + targetObject
                + ", accountIndex=" + accountIndex + ", accountID=" + accountID
                + ", fileName=" + fileName + ", extraInfo=" + extraInfo + "]";
    }

    public String getExtraInfo()
    {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo)
    {
        this.extraInfo = extraInfo;
    }

    public OBDtoADCObject getTargetObject()
    {
        return targetObject;
    }

    public void setTargetObject(OBDtoADCObject targetObject)
    {
        this.targetObject = targetObject;
    }

    public Integer getRptType()
    {
        return rptType;
    }

    public void setRptType(Integer rptType)
    {
        this.rptType = rptType;
    }

    public Integer getFileType()
    {
        return fileType;
    }

    public void setFileType(Integer fileType)
    {
        this.fileType = fileType;
    }

    public String getIndex()
    {
        return index;
    }

    public void setIndex(String index)
    {
        this.index = index;
    }

    public Integer getGroupIndex()
    {
        return groupIndex;
    }

    public void setGroupIndex(Integer groupIndex)
    {
        this.groupIndex = groupIndex;
    }

    public Date getOccurTime()
    {
        return occurTime;
    }

    public void setOccurTime(Date occurTime)
    {
        this.occurTime = occurTime;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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

    public ArrayList<OBDtoAdcName> getAdcList()
    {
        return adcList;
    }

    public void setAdcList(ArrayList<OBDtoAdcName> adcList)
    {
        this.adcList = adcList;
    }

    public Integer getAccountIndex()
    {
        return accountIndex;
    }

    public void setAccountIndex(Integer accountIndex)
    {
        this.accountIndex = accountIndex;
    }

    public String getAccountID()
    {
        return accountID;
    }

    public void setAccountID(String accountID)
    {
        this.accountID = accountID;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
}
