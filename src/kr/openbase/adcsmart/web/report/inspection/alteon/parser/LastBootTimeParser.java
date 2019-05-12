package kr.openbase.adcsmart.web.report.inspection.alteon.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import kr.openbase.adcsmart.web.report.inspection.parser.AbstractAlteonParser;

public class LastBootTimeParser extends AbstractAlteonParser {
	private final static String SUFFIX_GENERAL_LASTBOOT = "Last boot"; // 16:35:47 Mon Apr 22, 2013 (hard reset from
																		// console)
	private final static int MAX_BOUNDS = 1;

	public LastBootTimeParser(String swVersion) {
		super(swVersion);
	}

	@Override
	public List<String> parse(String input) {
		List<String> lastBootTime = new ArrayList<String>();

		Matcher match = getMatcher(SUFFIX_GENERAL_LASTBOOT + ":\\s*(.+)", input);
		if (match.find()) {
			if (match.groupCount() == MAX_BOUNDS) {
				lastBootTime.add(match.group(1).trim());
			}
		}

		return lastBootTime;
	}

//    public static void main(String[] args)
//    {
//        // 22.0.4 버전
//        System.out.println(new LastBootTimeParser("test").parse(">> ADC_2424_22.0.4 - System# /info/sys/general     \n" + "System Information at 15:44:00 Wed Jan 14, 2015\n" + "\n" + "Alteon Application Switch 2424\n" + "\n" + "Switch is up 21 days, 2 hours, 35 minutes and 1 second.\n" + "Last boot: 13:08:21 Wed Dec 24, 2014 (hard reset from Telnet)\n" + "Last apply: 18:50:48 Mon Jan 12, 2015\n" + "Last save: 11:05:54 Wed Jan 14, 2015\n" + "\n" + "MAC Address: 00:0c:f8:d5:71:00    IP (If 100) Address: 192.168.100.11\n" + "Hardware Order No:   EB1412013     Serial No: Not Available Rev: Not Available\n" + "Mainboard Hardware:                  Part No: P314090-A  Rev: 03\n" + "Management Processor Board Hardware: Part No: P314080-A  Rev: 02\n" + "Fast Ethernet Board Hardware:        Part No: P314091-A  Rev: 03\n" + "\n" + "Software Version 22.0.4 (FLASH image2), active configuration.\n" + "\n" + "\n" + ">> ADC_2424_22.0.4 - System# "));
//        // 25.3.6.40 버전
//        System.out.println(new LastBootTimeParser("test").parse(">> scard_alt_slb1 - Main# /info/sys/general \n" + "System Information at 15:44:41 Wed Jan 14, 2015\n" + "Time zone: No timezone configured (GMT offset +9:00)\n" + "\n" + "\n" + "Memory profile is Default\n" + "\n" + "Symantec feature is globally Disabled\n" + "\n" + "Alteon Application Switch 2208\n" + "\n" + "Switch is up 2 days, 1 hour, 44 minutes and 45 seconds.\n" + "Last boot: 13:59:51 Mon Jan 12, 2015 (power cycle)\n" + "Last apply:  9:29:47 Tue Jan 13, 2015\n" + "Last save: 14:45:26 Mon Jan 12, 2015\n" + "\n" + "MAC Address: 00:15:e8:cb:60:00    IP (If 1) Address: 192.168.100.12\n" + "Hardware Order No:   EB1412010     Serial No: SSCPB80546 Rev: 12\n" + "Mainboard Hardware:                  Part No: P316677-B  Rev: 05\n" + "Management Processor Board Hardware: Part No: P314080-A  Rev: 02\n" + "Fast Ethernet Board Hardware:        Part No: P316676-A  Rev: 03\n" + "\n" + "Note - When the measured temperature inside the switch EXCEEDs\n" + "       the high threshold at 62 degree Celsius a syslog message\n" + "       will be generated.\n" + "\n" + "Software Version 25.3.6.40 (FLASH image2), active configuration.\n" + "\n" + "\n" + ">> scard_alt_slb1 - System# "));
//        // 29.5.2.0 버전
//        System.out.println(new LastBootTimeParser("test").parse(">> Standalone ADC - Main# /info/sys/general \n" + "System Information at 15:06:19 Wed Jan 14, 2015\n" + "Time zone: No timezone configured (GMT offset -8:00)\n" + "\n" + "\n" + "Memory profile is Default\n" + "\n" + "Alteon Application Switch 4408\n" + "\n" + "Switch is up 8 days, 23 hours, 42 minutes and 7 seconds.\n" + "Last boot: 15:24:13 Mon Jan  5, 2015 (hard reset from console)\n" + "Last apply: 14:46:38 Wed Jan 14, 2015\n" + "Last save: 14:46:58 Wed Jan 14, 2015\n" + "\n" + "MAC Address                :    00:03:b2:ce:64:c0\n" + "Hardware MainBoard No|Rev  :    NSA5110-E2SFP | A.04\n" + "Hardware DB No             :    Not Available\n" + "Hardware Serial Number     :    31305661 \n" + "\n" + "Note - When the measured temperature inside the switch EXCEEDs\n" + "       the anomaly threshold at 74 degree Celsius or the critical\n" + "       temperature at 81 degree Celsius different syslog messages\n" + "       will be generated.\n" + "\n" + "Software Version 29.5.2.0 (FLASH image2), active configuration.\n" + "HA State:    ACTIVE*\n" + "Mode of operation: by Number\n" + "\n" + "\n" + ">> Standalone ADC - System# "));
//    }
}
