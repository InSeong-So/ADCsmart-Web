package kr.openbase.adcsmart.web.report.inspection.alteon.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import kr.openbase.adcsmart.web.report.inspection.parser.AbstractAlteonParser;

public class InterfaceStateParser extends AbstractAlteonParser {
	private final int MAX_BOUNDS = 4;

	public InterfaceStateParser(String swVersion) {
		super(swVersion);
	}

	@Override
	public List<String> parse(String input) {
		final List<String> interfaceState = new ArrayList<String>();

		Matcher match = getMatcher("(\\d+):\\s*.*\\s+(\\d+\\.\\d+\\.\\d+\\.\\d+\\s*){3},([^,]+,){1}\\s*([^,|^\n]+)+",
				input);

		while (match.find()) {
			if (match.groupCount() == MAX_BOUNDS) {
				interfaceState.add(match.group(1) + ":" + match.group(4).trim());
			}
		}

		return interfaceState;
	}

//    public static void main(String[] args)
//    {
//        System.out.println(new InterfaceStateParser("test").parse("IP information:\n  Router ID: 192.168.100.12,  AS number 0\nInterface information:\n  1: IP4 192.168.100.12  255.255.255.0   192.168.100.255, vlan 1, up\n  2: IP4 192.168.101.12  255.255.255.0   192.168.101.255, vlan 2, up\n100: IP4 1.1.1.3         255.255.255.0   1.1.1.255      , vlan 100, DOWN\n200: IP4 192.168.200.13  255.255.255.0   192.168.200.255, vlan 200, up\n\nDefault gateway information: metric strict\n  1: 192.168.100.1,                                 vlan any,  up\n  2: 1.1.1.4,                                       vlan any,  FAILED\n\nCurrent IP forwarding settings: ON, dirbr disabled, noicmprd disabled, rtcache enabled\n\nCurrent local networks:\nNone\n-----------------------------------------------\nCurrent IPv6 local networks:\n\nCurrent IP port settings:\n  All other ports have forwarding ON\n\nCurrent network filter settings:\n  none\n\nCurrent route map settings:\n\nCurrent OSPF settings: ON\n                \n  Default route none\n  Router ID: 192.168.100.12\n  lsdb limit 0\n\nCurrent OSPF area settings:\n  0: 0.0.0.0,         type transit, auth none, metric 1, spf 10, enabled\n\nCurrent OSPF interface settings:\n  1: 192.168.100.12,  area index  0, prio 1, cost 1, enabled\n    hello 10, dead 40, trans 1, retra 5\n    key "));
//        System.out.println(new InterfaceStateParser("test").parse("Interface information:\n100: 192.168.100.11  255.255.255.0   192.168.100.255,  vlan 100, up\n101: 192.168.101.11  255.255.255.0   192.168.101.255,  vlan 101, up\n200: 192.168.200.12  255.255.255.0   192.168.200.255,  vlan 200, up\n"));
//        System.out.println(new InterfaceStateParser("test").parse(">> scard_alt_slb1 - Syslog# /info/l3/ip\n" + 
//                "IP information:\n" + 
//                "  Router ID: 192.168.101.12,  AS number 0\n" + 
//                "\n" + 
//                "Interface information:\n" + 
//                "  1: IP4 192.168.100.12  255.255.255.0   192.168.100.255, vlan 1, up\n" + 
//                "  2: IP4 192.168.101.12  255.255.255.0   192.168.101.255, vlan 2, up\n" + 
//                "100: IP4 1.1.1.3         255.255.255.0   1.1.1.255      , vlan 100, DOWN\n" + 
//                "200: IP4 192.168.200.13  255.255.255.0   192.168.200.255, vlan 200, up\n" + 
//                "\n" + 
//                "Default gateway information: metric strict\n" + 
//                "  1: 192.168.100.1,                                 vlan any,  up\n" + 
//                "  2: 1.1.1.4,                                       vlan any,  FAILED\n" + 
//                "\n" + 
//                "Current IP forwarding settings: ON, dirbr disabled, noicmprd disabled, rtcache enabled\n" + 
//                "\n" + 
//                "Current local networks:\n" + 
//                "None\n" + 
//                "-----------------------------------------------\n" + 
//                "Current IPv6 local networks:\n" + 
//                "\n" + 
//                "Current IP port settings:\n" + 
//                "  All other ports have forwarding ON\n" + 
//                "\n" + 
//                "Current network filter settings:\n" + 
//                "  none\n" + 
//                "\n" + 
//                "Current route map settings:\n" + 
//                "\n" + 
//                "Current OSPF settings: ON                 \n" + 
//                "  Default route none\n" + 
//                "  Router ID: 192.168.101.12\n" + 
//                "  lsdb limit 0\n" + 
//                "\n" + 
//                "Current OSPF area settings:\n" + 
//                "  0: 0.0.0.0,         type transit, auth none, metric 1, spf 10, enabled\n" + 
//                "\n" + 
//                "Current OSPF interface settings:\n" + 
//                "  1: 192.168.100.12,  area index  0, prio 1, cost 1, enabled\n" + 
//                "    hello 10, dead 40, trans 1, retra 5\n" + 
//                "    key \n" + 
//                "\n" + 
//                ">> scard_alt_slb1 - Layer 3# "));
//    }
}
