package kr.openbase.adcsmart.web.report.inspection.alteon.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import kr.openbase.adcsmart.web.report.inspection.parser.AbstractAlteonParser;

/**
 * 기존의 버전과 테스트용 버전이 달라서 차후 수정이 필요하다.
 * 
 * @author 최영조
 */
public class VlanInfoParser extends AbstractAlteonParser {
	private static final String EMPTY_KEYWORD = "empty";
	private final int MAX_BOUNDS = 2;

	public VlanInfoParser(String swVersion) {
		super(swVersion);
	}

	@Override
	public List<String> parse(String input) {
		final List<String> vlanInfo = new ArrayList<String>();

		Matcher match = getMatcher(
				"(\\d+)\\s\\s+[a-zA-Z0-9 ]+\\s\\s+[a-zA-Z0-9 ]+\\s\\s+[a-zA-Z0-9 ]+\\s\\s+[a-zA-Z0-9 ]+\\s\\s+[a-zA-Z0-9 ]+\\s\\s+([\\d- ]+)",
				input);
		while (match.find()) {
			if (match.groupCount() == MAX_BOUNDS) {
				String portNumber = "";
				if (match.group(2).equals(" ")) {
					portNumber = EMPTY_KEYWORD;
				} else {
					portNumber = match.group(2).replace(" ", ", ");
				}

				vlanInfo.add(match.group(1) + "(" + portNumber + ")");
			}
		}

		return vlanInfo;
	}

//    public static void main(String[] args)
//    {
//        System.out.println(new VlanInfoParser("test").parse("VLAN                Name               Status Jumbo BWC  Learn Ports\n----  -------------------------------- ------ ----- ---- ----- ----------------\n1     Default VLAN                       ena    n  1024   ena  1 5-10\n2     VLAN 2                             ena    n  1024   ena  2\n100   VLAN 100                           ena    n  1024   ena  1\n200   VLAN 200                           ena    n  1024   ena  3 4\n"));
//        System.out.println(new VlanInfoParser("test").parse("VLAN                Name               Status Jumbo BWC  Learn Ports\n----  -------------------------------- ------ ----- ---- ----- ----------------\n1     Default VLAN                       ena    n   256   ena  1 3 7-28\n5     3                                  dis    n   256   ena  empty\n100   VLAN 100                           ena    n   256   ena  2\n101   VLAN 101                           ena    n   256   ena  4\n200   VLAN 200                           ena    n   256   ena  5 6\n\n"));
//        System.out.println(new VlanInfoParser("test").parse("VLAN                Name               Status Jumbo BWC  Learn Ports\n" + "----  -------------------------------- ------ ----- ---- ----- ----------------\n" + "1     Default VLAN                       ena    n   256   ena  1 3 7-28\n" + "5     3                                  dis    n   256   ena  empty\n" + "100   VLAN 100                           ena    n   256   ena  2\n" + "101   VLAN 101                           ena    n   256   ena  4\n" + "200   VLAN 200                           ena    n   256   ena  5 6\n" + "\r\n" + ">> ADC_2424_22.0.4 - Layer 2# "));
//        System.out.println(new VlanInfoParser("test").parse(">> scard_alt_slb1 - Main# /info/l2/vlan\n" + "VLAN                Name               Status Jumbo BWC  Learn Ports\n" + "----  -------------------------------- ------ ----- ---- ----- ----------------\n" + "1     Default VLAN                       ena    n  1024   ena  1 5-10\r\n" + "2     VLAN 2                             ena    n  1024   ena  2\n" + "100   VLAN 100                           ena    n  1024   ena  1\n" + "200   VLAN 200                           ena    n  1024   ena  3 4\n" + "\n" + ">> scard_alt_slb1 - Layer 2# "));
//    }
}
