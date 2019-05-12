package kr.openbase.adcsmart.web.report.inspection.alteon.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import kr.openbase.adcsmart.web.report.inspection.parser.AbstractAlteonParser;

public class NTPInfoParser extends AbstractAlteonParser {
	private final static String CURRENT_NTP_STATE = "Current NTP state:";
	private final static String CURRENT_PRIMARY_NTP = "Current primary NTP server:";
	private final static String CURRENT_SECONDARY_NTP = "Current secondary NTP server:";
	private final static String PRIMARY = "primary";
	private final static String SECONDARY = "secondary";

	private final static int MAX_BOUNDS = 1;

	public NTPInfoParser(String swVersion) {
		super(swVersion);
	}

	@Override
	public List<String> parse(String input) {
		final List<String> ntpInfo = new ArrayList<String>();

		String state = getInfo(CURRENT_NTP_STATE, input);
		String primaryServer = getInfo(CURRENT_PRIMARY_NTP, input);
		String secondaryServer = getInfo(CURRENT_SECONDARY_NTP, input);

		if (!state.equals("")) {
			ntpInfo.add(state);
		}

		if (!primaryServer.equals("")) {
			ntpInfo.add(PRIMARY + ": " + primaryServer);
		}

		if (!secondaryServer.equals("")) {
			ntpInfo.add(SECONDARY + ": " + secondaryServer);
		}

		return ntpInfo;
	}

	private String getInfo(final String keyword, final String input) {
		Matcher match = getMatcher(keyword + "\\s+(.+)\n", input);
		if (match.find()) {
			if (match.groupCount() == MAX_BOUNDS) {
				return match.group(1);
			}
		}

		return "";
	}

//    public static void main(String[] args)
//    {
//        System.out.println(new NTPInfoParser("test").parse("Current NTP state: disabled\nCurrent primary NTP server: 141.223.182.106\nCurrent secondary NTP server: 192.168.101.50\nCurrent resync interval: 1 minutes\nCurrent GMT timezone offset: +9:00\n"));
//    }
}
