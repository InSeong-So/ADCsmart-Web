package kr.openbase.adcsmart.web.report.inspection.alteon.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import kr.openbase.adcsmart.web.report.inspection.parser.AbstractAlteonParser;

public class SyslogUseStateParser extends AbstractAlteonParser {
	private final static int MAX_BOUNDS = 2;

	public SyslogUseStateParser(String swVersion) {
		super(swVersion);
	}

	@Override
	public List<String> parse(String input) {
		final List<String> syslogUseState = new ArrayList<String>();
		Matcher match = getMatcher("(ho*st\\d*)\\s+(\\d+\\.\\d+\\.\\d+\\.\\d+),\\s+[^,]+,\\s+.+", input);
		while (match.find()) {
			if (match.groupCount() == MAX_BOUNDS) {
				syslogUseState.add(match.group(1) + ":" + match.group(2));
			}
		}

		return syslogUseState;
	}

//    public static void main(String[] args)
//    {
//        System.out.println(new SyslogUseStateParser("test").parse(">> scard_alt_slb1 - Main# /cfg/sys/syslog/cur\n" + "Current syslog configuration:\n" + "hst1 211.189.47.58, severity 7, facility 0\n" + "hst2 172.172.2.209, severity 7, facility 0\n" + "hst3 172.172.2.222, severity 7, facility 0\n" + "hst4 172.172.2.13, severity 7, facility 0\n" + "hst5 172.172.2.56, severity 7, facility 0, console enabled\n" + "syslogging all features)\n"));
//        System.out.println(new SyslogUseStateParser("test").parse("Current syslog configuration:\n            host 172.172.1.163, severity 7, facility 0\n            host2 172.172.2.220, severity2 7, facility2 0, console enabled\n            syslogging all features\n"));
//        System.out.println(new SyslogUseStateParser("test").parse(">> scard_alt_slb1 - Main# /cfg/sys/syslog/cur\n" + "Current syslog configuration:\n" + "  hst1 172.172.1.163, severity 7, facility 0\n" + "  hst2 172.172.2.220, severity 7, facility 0\n" + "  hst3 172.172.2.62, severity 7, facility 0\n" + "  hst4 172.172.2.161, severity 7, facility 0\n" + "  hst5 172.172.2.222, severity 7, facility 0, console enabled\n" + "  syslogging all features\n" + "\n" + ">> scard_alt_slb1 - Syslog# \n" + ""));
//    }
}
