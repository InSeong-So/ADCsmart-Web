package kr.openbase.adcsmart.web.report.inspection.f5.row.column;

import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;

public class F5Column implements Column
{
    private String basicColumn;
    private String haColumn;
    private String l2L3Column;
    private String l4Column;
    private String etcColumn;

    public F5Column()
    {
        basicColumn = OBMessages.getMessage(OBMessages.MSG_RPT_F5_COLUMN_BASIC);
        haColumn = OBMessages.getMessage(OBMessages.MSG_RPT_F5_COLUMN_HA);
        l2L3Column = OBMessages.getMessage(OBMessages.MSG_RPT_F5_COLUMN_L2L3);
        l4Column = OBMessages.getMessage(OBMessages.MSG_RPT_F5_COLUMN_L4);
        etcColumn = OBMessages.getMessage(OBMessages.MSG_RPT_F5_COLUMN_ETC);
    }

    @Override
    public String getFirstColumn()
    {
        String result = basicColumn;
        basicColumn = null; // 두 번째부턴 null 값이 들어가도록 의도했다.
        return result;
    }

    @Override
    public String getSecondColumn()
    {
        String result = haColumn;
        haColumn = null; // 두 번째부턴 null 값이 들어가도록 의도했다.
        return result;
    }

    @Override
    public String getThridColumn()
    {
        String result = l2L3Column;
        l2L3Column = null; // 두 번째부턴 null 값이 들어가도록 의도했다.

        return result;
    }

    @Override
    public String getForthColumn()
    {
        String result = l4Column;
        l4Column = null; // 두 번째부턴 null 값이 들어가도록 의도했다.
        return result;
    }

    @Override
    public String getFifthColumn()
    {
        String result = etcColumn;
        etcColumn = null; // 두 번째부턴 null 값이 들어가도록 의도했다.
        return result;
    }
}
