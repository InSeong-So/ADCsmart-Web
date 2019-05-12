package kr.openbase.adcsmart.web.report.inspection.alteon.parser;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.web.report.inspection.parser.AbstractAlteonParser;
import kr.openbase.adcsmart.web.report.inspection.utility.Common;

/**
 * 미완성이다.
 * 
 * @author 최영조
 */
public class TrunkUseStateParser extends AbstractAlteonParser {
	private final static String SUFFIX_INFOTRUNK_ENABLED = "Trunk group ";
	private final static String SUFFIX_INFOTRUNK_DISABLED = "disabled";
	private final static String PORT_UP = "forwarding";
	private final static String PORT_DOWN = "DOWN";

	public TrunkUseStateParser(String swVersion) {
		super(swVersion);
	}

	@Override
	public List<String> parse(String input) {
		final List<String> trunkUseState = new ArrayList<String>();
		if (input.contains(SUFFIX_INFOTRUNK_DISABLED)) {
			trunkUseState.add(SUFFIX_INFOTRUNK_DISABLED);
			return trunkUseState;
		}
		String portState = "";
		String groupNumber = "";
		String portNumber = "";
		String[] trunkText = input.split("\\n");
		for (String line : trunkText) {
			if (line.contains(SUFFIX_INFOTRUNK_ENABLED)) {
				int length = line.indexOf(",");
				groupNumber = line.substring(SUFFIX_INFOTRUNK_ENABLED.length(), length);
				continue;
			}
			line = Common.removeMoreText(line);
			String[] trunk = line.trim().split("\\s+");
			if (trunk[0].contains(":")) {
				portNumber = trunk[0];
				if (trunk[trunk.length - 1].toLowerCase().contains(PORT_UP)) {

					portState = PORT_UP;
				} else {
					portState = PORT_DOWN;
				}

				trunkUseState.add(groupNumber + " " + portNumber + portState);
			}
		}
		return trunkUseState;
	}

//    public static void main(String[] args)
//    {
//        System.out.println(new TrunkUseStateParser("test").parse("Trunk group 2, bw contract 1024, port state:\n    15: STG  1 DOWN\n    16: STG  1 forwarding\n\nTrunk group 3, bw contract 1024, port state:\n    3: STG  1 DOWN\n"));
//        System.out.println(new TrunkUseStateParser("test").parse("All Trunk groups are disabled.\n"));
//    }
}
