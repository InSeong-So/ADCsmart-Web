package kr.openbase.adcsmart.web.report.inspection.alteon.parser;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.web.report.inspection.parser.AbstractAlteonParser;

/**
 * Filter 정보를 가져온다. SNMP를 통해서 가져온다.
 * 
 * @author 최영조
 */
public class FilterInfoParser extends AbstractAlteonParser
{

    public FilterInfoParser(String swVersion)
    {
        super(swVersion);
    }

    @Override
    public List<String> parse(String input)
    {   
        List<String> filterInfo = new ArrayList<String> ();
        filterInfo.add(input);
        return filterInfo;
    }

}
