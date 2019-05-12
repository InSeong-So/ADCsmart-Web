package kr.openbase.adcsmart.web.report.inspection.alteon.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import kr.openbase.adcsmart.web.report.inspection.parser.AbstractAlteonParser;

public class STPUseStateParser extends AbstractAlteonParser {
	private final static String SUFFIX_INFOSTG_ENABLED = "Spanning Tree Group";
	private final static int MAX_BOUNDS = 2;

	public STPUseStateParser(String swVersion) {
		super(swVersion);
	}

	@Override
	public List<String> parse(String input) {
		final List<String> stpUseState = new ArrayList<String>();

		Matcher match = getMatcher(SUFFIX_INFOSTG_ENABLED + "\\s+(\\d+):\\s+(.+?),*\\s+", input);
		while (match.find()) {
			if (match.groupCount() == MAX_BOUNDS) {
				stpUseState.add(match.group(1) + "(" + match.group(2) + ")");
			}
		}

		return stpUseState;
	}

//    public static void main(String[] args)
//    {
//        System.out.println(new STPUseStateParser("test").parse("\n------------------------------------------------------------------\nSpanning Tree Group 1: On (STP/PVST), FDB aging timer 300\nVLANs:  1 2\n\nPort    Priority   Cost      State       Designated Bridge     Des Port\n------  --------   ----    ----------  ----------------------  --------\n1             0      0      DISABLED  *\n2             0      0     FORWARDING *\n3             0      0     FORWARDING *\n4             0      0      DISABLED  *\n5             0      0      DISABLED  *\n6             0      0      DISABLED  *\n7             0      0      DISABLED  *\n8             0      0      DISABLED  *\n9             0      0      DISABLED  *\n10            0      0      DISABLED  *\n11            0      0      DISABLED  *\n12            0      0      DISABLED  *\n* = STP turned off for this port.\nTransmission of PVST frames on untagged ports: Disabled\n\nNumber of topology changes - 0\nTime since last topology change - N/A\n"));
//        System.out.println(new STPUseStateParser("test").parse(">> scard_alt_slb1 - Layer 3# /info/l2/stg\r\n" + "\r\n" + "------------------------------------------------------------------\r\n" + "Spanning Tree Group 1: Off (RSTP), FDB aging timer 300\r\n" + "VLANs:  1 2 100 200\r\n" + "\r\n" + "1         0         0   DSB *\r\n" + "2         0         0   FWD *\r\n" + "3         0         0   DSB *\r\n" + "4         0         0   FWD *\r\n" + "5         0         0   FWD *\r\n" + "6         0         0   DSB *\r\n" + "7         0         0   DSB *\r\n" + "8         0         0   DSB *\r\n" + "9         0         0   DSB *\r\n" + "10        0         0   DSB *\r\n" + "* = STP turned off for this port.\r\n" + "Transmission of PVST frames on untagged ports: Disabled\r\n" + "\r\n" + "Number of topology changes - 0\r\n" + "Time since last topology change - N/A\r\n" + "\r\n" + ">> scard_alt_slb1 - Layer 2# " + "\r\n" + "------------------------------------------------------------------\r\n" + "Spanning Tree Group 2: On (RSTP), FDB aging timer 300\r\n" + "VLANs:  1 2 100 200\r\n" + "\r\n" + "1         0         0   DSB *\r\n" + "2         0         0   FWD *\r\n" + "3         0         0   DSB *\r\n" + "4         0         0   FWD *\r\n" + "5         0         0   FWD *\r\n" + "6         0         0   DSB *\r\n" + "7         0         0   DSB *\r\n" + "8         0         0   DSB *\r\n" + "9         0         0   DSB *\r\n" + "10        0         0   DSB *\r\n" + "* = STP turned off for this port.\r\n" + "Transmission of PVST frames on untagged ports: Disabled"));
//        System.out.println(new STPUseStateParser("test").parse(">> ADC_2424_22.0.4 - Main# /info/l2/stg\r\n" + 
//        "------------------------------------------------------------------\r\n" + 
//        "Spanning Tree Group 1: Off, FDB aging timer 300\r\n" + 
//        "\r\n" + 
//        "Port   Priority   Cost      State       Designated Bridge     Des Port\r\n" + 
//        "-----  --------   ----    ----------  ----------------------  --------\r\n" + 
//        "  1          0      0      DISABLED  *\r\n" + 
//        "  2          0      0     FORWARDING *\r\n" + 
//        "  3          0      0      DISABLED  *\r\n" + 
//        "  4          0      0     FORWARDING *\r\n" + 
//        "  7          0      0      DISABLED  *\r\n" + 
//        "  8          0      0      DISABLED  *\r\n" + 
//        "  9          0      0      DISABLED  *\r\n" + 
//        " 10          0      0      DISABLED  *\r\n" + 
//        " 11          0      0      DISABLED  *\r\n" + 
//        " 12          0      0      DISABLED  *\r\n" + 
//        " 13          0      0      DISABLED  *\r\n" + 
//        " 14          0      0      DISABLED  *\r\n" + 
//        " 15          0      0      DISABLED  *\r\n" + 
//        " 16          0      0      DISABLED  *\r\n" + 
//        " 17          0      0      DISABLED  *\r\n" + 
//        " 18          0      0      DISABLED  *\r\n" + 
//        " 19          0      0      DISABLED  *\r\n" + 
//        " 20          0      0      DISABLED  *    \r\n" + 
//        " 21          0      0      DISABLED  *\r\n" + 
//        " 22          0      0      DISABLED  *\r\n" + 
//        " 23          0      0      DISABLED  *\r\n" + 
//        " 24          0      0      DISABLED  *\r\n" + 
//        " 25          0      0      DISABLED  *\r\n" + 
//        " 26          0      0      DISABLED  *\r\n" + 
//        " 27          0      0      DISABLED  *\r\n" + 
//        " 28          0      0      DISABLED  *\r\n" + 
//        "* = STP turned off for this port.\r\n" + 
//        "------------------------------------------------------------------\r\n" + 
//        "Spanning Tree Group 2: On\r\n" + 
//        "\r\n" + 
//        "Current Root:            Path-Cost  Port Hello MaxAge FwdDel Aging\r\n" + 
//        " 8000 00:0c:f8:d5:71:00        0       0    2     20     15    300\r\n" + 
//        "\r\n" + 
//        "Parameters:  Priority  Hello  MaxAge  FwdDel  Aging\r\n" + 
//        "              32768      2      20      15     300\r\n" + 
//        "\r\n" + 
//        "Port   Priority   Cost      State       Designated Bridge     Des Port\r\n" + 
//        "-----  --------   ----    ----------  ----------------------  --------\r\n" + 
//        "  5        128      0      DISABLED   \r\n" + 
//        "  6        128      5     FORWARDING   8000-00:0c:f8:d5:71:00    32774\r\n" + 
//        "\r\n" + 
//        ">> ADC_2424_22.0.4 - Layer 2# "));
//
//    }
}
