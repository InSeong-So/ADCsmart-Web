package kr.openbase.adcsmart.web.report.inspection.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractAlteonParser implements Parser
{
    protected String swVersion;

    public AbstractAlteonParser(String swVersion)
    {
        this.swVersion = swVersion;
    }

    protected Matcher getMatcher(final String regex, final String content)
    {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(content);
    }

}
