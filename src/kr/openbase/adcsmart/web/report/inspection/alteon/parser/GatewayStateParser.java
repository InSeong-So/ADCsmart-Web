package kr.openbase.adcsmart.web.report.inspection.alteon.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import kr.openbase.adcsmart.web.report.inspection.parser.AbstractAlteonParser;

/**
 * 게이트웨이 상태 정보를 파싱해준다. 리턴 값이 존재하면 비정상 상태이다.
 * 
 * @author 최영조
 */
public class GatewayStateParser extends AbstractAlteonParser {
	private final static int MAX_BOUNDS = 4;

	public GatewayStateParser(String swVersion) {
		super(swVersion);
	}

	@Override
	public List<String> parse(String input) {
		final List<String> gatewayState = new ArrayList<String>();

		Matcher match = getMatcher("(\\d+):\\s*(\\d+\\.\\d+\\.\\d+\\.\\d+,){1}([^,]+,){1}\\s*([^,|^\n]+)\n", input);

		while (match.find()) {
			if (match.groupCount() == MAX_BOUNDS) {
				gatewayState.add(match.group(1) + "(" + match.group(2).replace(",", "") + "):" + match.group(4).trim());
			}
		}

		return gatewayState;
	}

//    public static void main(String[] args)
//    {
//        System.out.println(new GatewayStateParser("test").parse("IP information:\n  Router ID: 192.168.100.12,  AS number 0\nInterface information:\n  1: IP4 192.168.100.12  255.255.255.0   192.168.100.255, vlan 1, up\n  2: IP4 192.168.101.12  255.255.255.0   192.168.101.255, vlan 2, up\n100: IP4 1.1.1.3         255.255.255.0   1.1.1.255      , vlan 100, DOWN\n200: IP4 192.168.200.13  255.255.255.0   192.168.200.255, vlan 200, up\n\nDefault gateway information: metric strict\n  1: 192.168.100.1,                                 vlan any,  up\n  2: 1.1.1.4,                                       vlan any,  FAILED\n\nCurrent IP forwarding settings: ON, dirbr disabled, noicmprd disabled, rtcache enabled\n\nCurrent local networks:\nNone\n-----------------------------------------------\nCurrent IPv6 local networks:\n\nCurrent IP port settings:\n  All other ports have forwarding ON\n\nCurrent network filter settings:\n  none\n\nCurrent route map settings:\n\nCurrent OSPF settings: ON\n                \n  Default route none\n  Router ID: 192.168.100.12\n  lsdb limit 0\n\nCurrent OSPF area settings:\n  0: 0.0.0.0,         type transit, auth none, metric 1, spf 10, enabled\n\nCurrent OSPF interface settings:\n  1: 192.168.100.12,  area index  0, prio 1, cost 1, enabled\n    hello 10, dead 40, trans 1, retra 5\n    key "));
//        System.out.println(new GatewayStateParser("test").parse(">> scard_alt_slb1 - Syslog# /info/l3/ip\r\n" + 
//        "IP information:\r\n" + 
//        "  Router ID: 192.168.101.12,  AS number 0\r\n" + 
//        "\r\n" + 
//        "Interface information:\r\n" + 
//        "  1: IP4 192.168.100.12  255.255.255.0   192.168.100.255, vlan 1, up\r\n" + 
//        "  2: IP4 192.168.101.12  255.255.255.0   192.168.101.255, vlan 2, up\r\n" + 
//        "100: IP4 1.1.1.3         255.255.255.0   1.1.1.255      , vlan 100, DOWN\r\n" + 
//        "200: IP4 192.168.200.13  255.255.255.0   192.168.200.255, vlan 200, up\r\n" + 
//        "\r\n" + 
//        "Default gateway information: metric strict\r\n" + 
//        "  1: 192.168.100.1,                                 vlan any,  up\r\n" + 
//        "  2: 1.1.1.4,                                       vlan any,  FAILED\r\n" + 
//        "\r\n" + 
//        "Current IP forwarding settings: ON, dirbr disabled, noicmprd disabled, rtcache enabled\r\n" + 
//        "\r\n" + 
//        "Current local networks:\r\n" + 
//        "None\r\n" + 
//        "-----------------------------------------------\r\n" + 
//        "Current IPv6 local networks:\r\n" + 
//        "\r\n" + 
//        "Current IP port settings:\r\n" + 
//        "  All other ports have forwarding ON\r\n" + 
//        "\r\n" + 
//        "Current network filter settings:\r\n" + 
//        "  none\r\n" + 
//        "\r\n" + 
//        "Current route map settings:\r\n" + 
//        "\r\n" + 
//        "Current OSPF settings: ON                 \r\n" + 
//        "  Default route none\r\n" + 
//        "  Router ID: 192.168.101.12\r\n" + 
//        "  lsdb limit 0\r\n" + 
//        "\r\n" + 
//        "Current OSPF area settings:\r\n" + 
//        "  0: 0.0.0.0,         type transit, auth none, metric 1, spf 10, enabled\r\n" + 
//        "\r\n" + 
//        "Current OSPF interface settings:\r\n" + 
//        "  1: 192.168.100.12,  area index  0, prio 1, cost 1, enabled\r\n" + 
//        "    hello 10, dead 40, trans 1, retra 5\r\n" + 
//        "    key \r\n" + 
//        "\r\n" + 
//        ">> scard_alt_slb1 - Layer 3# "));
//    }
}
