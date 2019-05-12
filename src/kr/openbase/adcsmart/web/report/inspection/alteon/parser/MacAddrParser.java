package kr.openbase.adcsmart.web.report.inspection.alteon.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import kr.openbase.adcsmart.web.report.inspection.parser.AbstractAlteonParser;

public class MacAddrParser extends AbstractAlteonParser {

	public MacAddrParser() {
		super(null); // do not use
	}

	@Override
	public List<String> parse(String input) {
		final List<String> macAddr = new ArrayList<String>();

		Matcher match = getMatcher("MAC Address\\s*:\\s+([a-fA-F\\d:]+)\\s+", input);

		if (match.find()) {
			macAddr.add(match.group(1));
		}

		return macAddr;
	}

//    public static void main(String[] args)
//    {
//        // 22.0.4 버전
//        System.out.println(new MacAddrParser().parse(">> ADC_2424_22.0.4 - System# /info/sys/general     \r\n" + "System Information at 15:44:00 Wed Jan 14, 2015\r\n" + "\r\n" + "Alteon Application Switch 2424\r\n" + "\r\n" + "Switch is up 21 days, 2 hours, 35 minutes and 1 second.\r\n" + "Last boot: 13:08:21 Wed Dec 24, 2014 (hard reset from Telnet)\r\n" + "Last apply: 18:50:48 Mon Jan 12, 2015\r\n" + "Last save: 11:05:54 Wed Jan 14, 2015\r\n" + "\r\n" + "MAC Address: 00:0c:f8:d5:71:00    IP (If 100) Address: 192.168.100.11\r\n" + "Hardware Order No:   EB1412013     Serial No: Not Available Rev: Not Available\r\n" + "Mainboard Hardware:                  Part No: P314090-A  Rev: 03\r\n" + "Management Processor Board Hardware: Part No: P314080-A  Rev: 02\r\n" + "Fast Ethernet Board Hardware:        Part No: P314091-A  Rev: 03\r\n" + "\r\n" + "Software Version 22.0.4 (FLASH image2), active configuration.\r\n" + "\r\n" + "\r\n" + ">> ADC_2424_22.0.4 - System# "));
//        // 25.3.6.40 버전
//        System.out.println(new MacAddrParser().parse(">> scard_alt_slb1 - Main# /info/sys/general \r\n" + "System Information at 15:44:41 Wed Jan 14, 2015\r\n" + "Time zone: No timezone configured (GMT offset +9:00)\r\n" + "\r\n" + "\r\n" + "Memory profile is Default\r\n" + "\r\n" + "Symantec feature is globally Disabled\r\n" + "\r\n" + "Alteon Application Switch 2208\r\n" + "\r\n" + "Switch is up 2 days, 1 hour, 44 minutes and 45 seconds.\r\n" + "Last boot: 13:59:51 Mon Jan 12, 2015 (power cycle)\r\n" + "Last apply:  9:29:47 Tue Jan 13, 2015\r\n" + "Last save: 14:45:26 Mon Jan 12, 2015\r\n" + "\r\n" + "MAC Address: 00:15:e8:cb:60:00    IP (If 1) Address: 192.168.100.12\r\n" + "Hardware Order No:   EB1412010     Serial No: SSCPB80546 Rev: 12\r\n" + "Mainboard Hardware:                  Part No: P316677-B  Rev: 05\r\n" + "Management Processor Board Hardware: Part No: P314080-A  Rev: 02\r\n" + "Fast Ethernet Board Hardware:        Part No: P316676-A  Rev: 03\r\n" + "\r\n" + "Note - When the measured temperature inside the switch EXCEEDs\r\n" + "       the high threshold at 62 degree Celsius a syslog message\r\n" + "       will be generated.\r\n" + "\r\n" + "Software Version 25.3.6.40 (FLASH image2), active configuration.\r\n" + "\r\n" + "\r\n" + ">> scard_alt_slb1 - System# "));
//        // 29.5.2.0 버전
//        System.out.println(new MacAddrParser().parse(">> Standalone ADC - Main# /info/sys/general \r\n" + "System Information at 15:06:19 Wed Jan 14, 2015\r\n" + "Time zone: No timezone configured (GMT offset -8:00)\r\n" + "\r\n" + "\r\n" + "Memory profile is Default\r\n" + "\r\n" + "Alteon Application Switch 4408\r\n" + "\r\n" + "Switch is up 8 days, 23 hours, 42 minutes and 7 seconds.\r\n" + "Last boot: 15:24:13 Mon Jan  5, 2015 (hard reset from console)\r\n" + "Last apply: 14:46:38 Wed Jan 14, 2015\r\n" + "Last save: 14:46:58 Wed Jan 14, 2015\r\n" + "\r\n" + "MAC Address                :    00:03:b2:ce:64:c0\r\n" + "Hardware MainBoard No|Rev  :    NSA5110-E2SFP | A.04\r\n" + "Hardware DB No             :    Not Available\r\n" + "Hardware Serial Number     :    31305661 \r\n" + "\r\n" + "Note - When the measured temperature inside the switch EXCEEDs\r\n" + "       the anomaly threshold at 74 degree Celsius or the critical\r\n" + "       temperature at 81 degree Celsius different syslog messages\r\n" + "       will be generated.\r\n" + "\r\n" + "Software Version 29.5.2.0 (FLASH image2), active configuration.\r\n" + "HA State:    ACTIVE*\r\n" + "Mode of operation: by Number\r\n" + "\r\n" + "\r\n" + ">> Standalone ADC - System# "));
//    }
}
