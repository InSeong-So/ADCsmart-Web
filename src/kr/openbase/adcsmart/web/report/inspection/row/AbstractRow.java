package kr.openbase.adcsmart.web.report.inspection.row;

import java.util.List;

import kr.openbase.adcsmart.service.utility.OBParser;

public abstract class AbstractRow implements Row
{
    protected String formatter(final List<String> parsedString) throws Exception
    {
        String result = "";
        String prefix = "";
        for(String rawStr : parsedString)
        {
            result = result + prefix + "\"" + rawStr + "\"";
            prefix = ", ";
        }
        
        if(!result.equals(""))
        {
            result = OBParser.trimCsv(result, CHECKLIST_MAX_STRING, " 외 %d건").replace("\"", "");
        }
        return result;
    }
}
