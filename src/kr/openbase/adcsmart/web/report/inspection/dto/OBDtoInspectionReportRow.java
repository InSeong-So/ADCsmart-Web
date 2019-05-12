package kr.openbase.adcsmart.web.report.inspection.dto;

public class OBDtoInspectionReportRow
{
    private String column;        // 항목
    private String inspectionlist; // 점검사항
    private String checkMethod;   // 점검방법
    private String checklist;     // 확인사항
    private String result;        // 결과

    public String getColumn()
    {
        return column;
    }

    public void setColumn(String column)
    {
        this.column = column;
    }

    public String getCheckMethod()
    {
        return checkMethod;
    }

    public void setCheckMethod(String checkMehod)
    {
        this.checkMethod = checkMehod;
    }

    public String getChecklist()
    {
        return checklist;
    }

    public void setChecklist(String checklist)
    {
        this.checklist = checklist;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public String getInspectionlist()
    {
        return inspectionlist;
    }

    public void setInspectionlist(String inspectionlist)
    {
        this.inspectionlist = inspectionlist;
    }

    @Override
    public String toString()
    {
        return "OBDtoInspectionReportRow [column=" + column + ", inspectionlist=" + inspectionlist + ", checkMethod=" + checkMethod + ", checklist=" + checklist + ", result=" + result + "]";
    }
}
