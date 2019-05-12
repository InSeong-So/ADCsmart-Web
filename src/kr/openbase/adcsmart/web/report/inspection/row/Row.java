package kr.openbase.adcsmart.web.report.inspection.row;

import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;

public interface Row
{
    int    CHECKLIST_MAX_STRING = 24;
    String NOT_PARSED           = "NOT_PARSED";
    String NOTHING              = "-";
    String EMPTY                = "";
    String ERROR                = "NOT INVEST";

    OBDtoInspectionReportRow getRow(final Column col) throws OBException;
}
