package kr.openbase.adcsmart.web.report.inspection.alteon.parser;

import java.util.Arrays;
import java.util.List;

import kr.openbase.adcsmart.web.report.inspection.parser.AbstractAlteonParser;

/**
 * SNMP를 사용하여 추출하기 때문에 현재는 사용하지 않는다.
 * @author 최영조
 */
public class VirtualServerStateParser extends AbstractAlteonParser
{

    public VirtualServerStateParser(String swVersion)
    {
        super(swVersion);
    }

    @Override
    public List<String> parse(String input)
    {
        return Arrays.asList(input.split(", "));
    }

}
