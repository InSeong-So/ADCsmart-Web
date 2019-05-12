package kr.openbase.adcsmart.web.report.inspection.alteon.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import kr.openbase.adcsmart.web.report.inspection.parser.AbstractAlteonParser;

public class L4TrafficPacketLossParser extends AbstractAlteonParser {
	private final static String SUFFIX_L4TRAFFIC_PACKET_LOSS = "Allocation failures:";
//    private final static String SUFFIX_STATSLB_64SEC_AVG_SESSION = "64 second average:";
	private final static int MAX_BOUNDS = 1;

	public L4TrafficPacketLossParser(String swVersion) {
		super(swVersion);
	}

	@Override
	public List<String> parse(String input) {
		final List<String> trafficPacketLoss = new ArrayList<String>();

		Matcher match = getMatcher(SUFFIX_L4TRAFFIC_PACKET_LOSS + "\\s*(\\d+)", input);
		if (match.find()) {
			if (match.groupCount() == MAX_BOUNDS) {
				trafficPacketLoss.add(match.group(1));
			}
		}

//        match = getMatcher(SUFFIX_STATSLB_64SEC_AVG_SESSION + "\\s*(\\d+)", input);
//        if(match.find())
//        {
//            if(match.groupCount() == MAX_BOUNDS)
//            {
//                trafficPacketLoss.add(match.group(1));
//            }
//        }

		return trafficPacketLoss;
	}

//    public static void main(String[] args)
//    {
//        System.out.println(new L4TrafficPacketLossParser("test").parse("------------------------------------------------------------------\nSLB Maintenance stats:\nMaximum sessions:                  614374\nCurrent sessions:                       8\n  4 second average:                     8\n 64 second average:                     7\nTerminated sessions:                    1\nAllocation failures:                    8\nUDP datagrams:                          0\nNon TCP/IP frames:                      0\nIncorrect VIPs:                         0\nIncorrect Vports:                    4900\nPackets drops: vip is not up            0\nNo available real server:               0\nBackup server activations:              0\nOverflow server activations:            0\nFiltered (denied) frames:               0\nLAND attacks:                           0\nNo TCP control bits:                    0\nInvalid reset packet drops:           114\nOut of State FIN Pkt drops:            14\nTotal IP fragment sessions:             0\n\nCurrent IP4 fragment sessions           0\n\nIP4 fragment discards:                  0\nCurrent IP6 fragment sessions           0\nIP6 fragment discards:                  0\nIP fragment table full:                 0\nCurrent IPF buffer sessions:            0\nHighest IPF buffer sessions:            0\nIPF buffer alloc fails:                 0\nIPF SP buffer alloc fails:              0\nSP buffer too low:                      0\nExceeded 50 OOO packets:                0\nIPF invalid lengths:                    0\nIPF Null Payloads:                      0\nFragment Overlaps:                      0\nDuplicate fragments:                    0\n"));
//        System.out.println(new L4TrafficPacketLossParser("test").parse(">> ADC_2424_22.0.4 - Main# /stat/slb/maint\n" + 
//                "------------------------------------------------------------------\n" + 
//                "SLB Maintenance stats:\n" + 
//                "Maximum sessions:                 2097100\n" + 
//                "Current sessions:                       5\n" + 
//                "  4 second average:                     5\n" + 
//                " 64 second average:                     5\n" + 
//                "Terminated sessions:                    0\n" + 
//                "Allocation failures:                   10\n" + 
//                "UDP datagrams:                          0\n" + 
//                "Non TCP/IP frames:                      0\n" + 
//                "Incorrect VIPs:                         0\n" + 
//                "Incorrect Vports:                    4576\n" + 
//                "No available real server:              51\n" + 
//                "Backup server activations:              0\n" + 
//                "Overflow server activations:            0\n" + 
//                "Filtered (denied) frames:               0\n" + 
//                "LAND attacks:                           0\n" + 
//                "No TCP control bits:                    0\n" + 
//                "Invalid reset packet drops:             0\n" + 
//                "Out of State FIN Pkt drops:             0\n" + 
//                "Total IP fragment sessions:             0\n" + 
//                "Current IP fragment sessions            0\n" + 
//                "IP fragment discards:                   0\n" + 
//                "IP fragment table full:                 0\n" + 
//                "Current IPF buffer sessions:            0\n" + 
//                "Highest IPF buffer sessions:            0\n" + 
//                "IPF buffer alloc fails:                 0\n" + 
//                "IPF SP buffer alloc fails:              0\n" + 
//                "SP buffer too low:                      0\n" + 
//                "Exceeded 16 OOO packets:                0\n" + 
//                "Current real service stats:             3\n" + 
//                "Real service stats failures:            0\n" + 
//                "Free Service pool entries:           8176\n" + 
//                "\n" + 
//                ">> ADC_2424_22.0.4 - Server Load Balancing Statistics# "));
//        
//        System.out.println(new L4TrafficPacketLossParser("test").parse(
//        ">> scard_alt_slb1 - Server Load Balancing Statistics# /stat/slb/maint\r\n" + 
//        "------------------------------------------------------------------\r\n" + 
//        "SLB Maintenance stats:\r\n" + 
//        "Maximum sessions:                  614374\r\n" + 
//        "Current sessions:                       0\r\n" + 
//        "  4 second average:                     0\r\n" + 
//        " 64 second average:                     0\r\n" + 
//        "Terminated sessions:                    0\r\n" + 
//        "Allocation failures:                   20\r\n" + 
//        "UDP datagrams:                          0\r\n" + 
//        "Non TCP/IP frames:                      0\r\n" + 
//        "Incorrect VIPs:                         0\r\n" + 
//        "Incorrect Vports:                       0\r\n" + 
//        "Packets drops: vip is not up            0\r\n" + 
//        "No available real server:               0\r\n" + 
//        "Backup server activations:              0\r\n" + 
//        "Overflow server activations:            0\r\n" + 
//        "Filtered (denied) frames:               0\r\n" + 
//        "LAND attacks:                           0\r\n" + 
//        "No TCP control bits:                    0\r\n" + 
//        "Invalid reset packet drops:             0\r\n" + 
//        "Out of State FIN Pkt drops:             0\r\n" + 
//        "Total IP fragment sessions:             0 \r\n" + 
//        "Current IP4 fragment sessions           0\r\n" + 
//        "IP4 fragment discards:                  0\r\n" + 
//        "Current IP6 fragment sessions           0\r\n" + 
//        "IP6 fragment discards:                  0\r\n" + 
//        "IP fragment table full:                 0\r\n" + 
//        "Current IPF buffer sessions:            0\r\n" + 
//        "Highest IPF buffer sessions:            0\r\n" + 
//        "IPF buffer alloc fails:                 0\r\n" + 
//        "IPF SP buffer alloc fails:              0\r\n" + 
//        "SP buffer too low:                      0\r\n" + 
//        "Exceeded 50 OOO packets:                0\r\n" + 
//        "IPF invalid lengths:                    0\r\n" + 
//        "IPF Null Payloads:                      0\r\n" + 
//        "Fragment Overlaps:                      0\r\n" + 
//        "Duplicate fragments:                    0\r\n" + 
//        "Current real service stats:             0\r\n" + 
//        "Real service stats failures:            0\r\n" + 
//        "Free Service pool entries:           8187\r\n" + 
//        "Current IP6 sessions:                   0\r\n" + 
//        "Incorrect IP6 VIPs:                     0\r\n" + 
//        "Incorrect IP6 Vports:                   0\r\n" + 
//        "Unrecognized IP6 next header:           0\r\n" + 
//        "Unsupported IP6 ext header:             0\r\n" + 
//        "No route to forward IP6 packet:         0\r\n" + 
//        "IP6 packets drops:                      0\r\n" + 
//        "                                          \r\n" + 
//        "SYMANTEC MAINT STATISTICS:\r\n" + 
//        "Symantec streams total:                  0\r\n" + 
//        "Symantec streams current:                0\r\n" + 
//        "Segments inspected:                      0\r\n" + 
//        "IPFragment sessions inspected:           0\r\n" + 
//        "Symantec Denied frames:                  0\r\n" + 
//        "Segment allocation failures:             0\r\n" + 
//        "Buffer allocation failures:              0\r\n" + 
//        "Connection allocation failures:          0\r\n" + 
//        "Segment reallocation failures:            0\r\n" + 
//        "Invalid buffers:                         0\r\n" + 
//        "\r\n" + 
//        "SYMANTEC INSPECTION STATISTICS\r\n" + 
//        "Packets in:                              0\r\n" + 
//        "Packets with no data:                    0\r\n" + 
//        "TCP packets:                             0\r\n" + 
//        "UDP packets:                             0\r\n" + 
//        "packets not TCP,UDP:                     0\r\n" + 
//        "Symantec Match count:                    0\r\n" + 
//        "Match result Fetch errors:               0\r\n" + 
//        "Truncated match info to MP:              0\r\n" + 
//        "Packets in fast path:                    0\r\n" + 
//        "\r\n" + 
//        ">> scard_alt_slb1 - Server Load Balancing Statistics# "));
//    }
}
