package kr.openbase.adcsmart.web.report.inspection.alteon.parser;

import java.util.Arrays;
import java.util.List;

import kr.openbase.adcsmart.web.report.inspection.parser.AbstractAlteonParser;

// SNMP로 구현한다. 구현 우선순위가 낮다.
public class RealServerStateParser extends AbstractAlteonParser
{

    public RealServerStateParser(String swVersion)
    {
        super(swVersion);
    }

    @Override
    public List<String> parse(String input)
    {
        return Arrays.asList(input.split(", "));
    }
    
}
