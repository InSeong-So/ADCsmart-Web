package kr.openbase.adcsmart.web.report.inspection.alteon.row.column;

import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;

public class AlteonColumn implements Column
{
    private String basicColumn;
    private String l2Column;
    private String l3Column;
    private String l4Column;
    private String etcColumn;

    public AlteonColumn()
    {
        basicColumn = OBMessages.getMessage(OBMessages.MSG_RPT_ALTEON_COLUMN_BASIC);
        l2Column = OBMessages.getMessage(OBMessages.MSG_RPT_ALTEON_COLUMN_L2);
        l3Column = OBMessages.getMessage(OBMessages.MSG_RPT_ALTEON_COLUMN_L3);
        l4Column = OBMessages.getMessage(OBMessages.MSG_RPT_ALTEON_COLUMN_L4);
        etcColumn = OBMessages.getMessage(OBMessages.MSG_RPT_ALTEON_COLUMN_ETC);
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
        String result = l2Column;
        l2Column = null; // 두 번째부턴 null 값이 들어가도록 의도했다.
        return result;
    }

    @Override
    public String getThridColumn()
    {
        String result = l3Column;
        l3Column = null; // 두 번째부턴 null 값이 들어가도록 의도했다.
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
