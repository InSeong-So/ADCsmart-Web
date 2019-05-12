package kr.openbase.adcsmart.web.report.inspection.dto;

import java.util.List;

public class OBDtoInspectionReport
{
    private OBDtoInspectionReporHeader     adcInfo;
    private List<OBDtoInspectionReportRow> contentsList;

    public OBDtoInspectionReporHeader getAdcInfo()
    {
        return adcInfo;
    }

    public void setAdcInfo(OBDtoInspectionReporHeader adcInfo)
    {
        this.adcInfo = adcInfo;
    }

    public List<OBDtoInspectionReportRow> getContentsList()
    {
        return contentsList;
    }

    public void setContentsList(List<OBDtoInspectionReportRow> contentsList)
    {
        this.contentsList = contentsList;
    }

    @Override
    public String toString()
    {
        return "SystemAdminSummaryDto [adc=" + adcInfo + ", contentsList=" + contentsList + "]";
    }
}
