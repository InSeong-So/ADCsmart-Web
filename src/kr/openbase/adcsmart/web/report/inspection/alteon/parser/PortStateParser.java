package kr.openbase.adcsmart.web.report.inspection.alteon.parser;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.web.report.inspection.parser.AbstractAlteonParser;

/**
 * 포트 상태에 관련 된 문자열을 파싱해준다. SNMP로 가져오고 파싱을 해놨기 때문에 따로 문자열을 파싱하지 않는다.
 * 
 * @author 최영조
 */
public class PortStateParser extends AbstractAlteonParser {
	// private final static String INTERFACE_STATS = "Interface statistics for
	// port";
	//
	// private final static int MAX_BOUNDS = 2;

	public PortStateParser(String swVersion) {
		super(swVersion);
	}

	@Override
	public List<String> parse(String input) {
		final List<String> portState = new ArrayList<String>();

		// 하나의 인터페이스 정보 구문을 전부 가져온다.
		// Matcher match = getMatcher(INTERFACE_STATS +
		// "\\s+(\\d+):\n\\s+.+\\s+.+\n([^:]+:[^:]+\n){6}", input);
		// while(match.find())
		// {
		// if(match.groupCount() == MAX_BOUNDS)
		// {
		// portState.add(match.group(1));
		// }
		// }

		return portState;
	}

//    public static void main(String[] args)
//    {
//        System.out.println(new PortStateParser("test").parse("------------------------------------------------------------------\nInterface statistics for port 1:\n                     ifHCIn Counters       ifHCOut Counters\nOctets:                            0                      0\nUcastPkts:                         0               55562184\nBroadcastPkts:                     0                    222\nMulticastPkts:                     0                3351904\nDiscards:                          0                      0\nErrors:                            0                      0\n"));
//    }
}
