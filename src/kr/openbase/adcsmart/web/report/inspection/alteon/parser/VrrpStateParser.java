package kr.openbase.adcsmart.web.report.inspection.alteon.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.openbase.adcsmart.web.report.inspection.parser.AbstractAlteonParser;

public class VrrpStateParser extends AbstractAlteonParser {
	public VrrpStateParser(String swVersion) {
		super(swVersion);
	}

	@Override
	public List<String> parse(String input) {
		final List<String> vrrpState = new ArrayList<String>();

		/*
		 * input = ">> scard_alt_slb1 - Standalone ADC - Layer 3# /info/l3/vrrp\r\n" +
		 * "------------------------------------------------------------------\r\n" +
		 * "------------------------------------------------------------------\n" +
		 * "VRRP information:                                                          \r\n"
		 * +
		 * " 131: vrid  131, 192.168.100.131, if  1, renter, prio 100, master, server  \r\n"
		 * +
		 * " 132: vrid  132, 192.168.100.132, if  1, renter, prio 100, master, server  \r\n"
		 * +
		 * " 133: vrid  133, 192.168.100.133, if  1, renter, prio 100, backup, server  \r\n"
		 * +
		 * " 134: vrid  134, 192.168.100.134, if  1, renter, prio 100, master, server  \r\n"
		 * + "\n" + ">> scard_alt_slb1 - Server Load Balancing Statistics# ";
		 */
//        System.out.println("input : " + input);
		Pattern masterCnt = Pattern.compile("master");
		Matcher m = masterCnt.matcher(input);
		int masterCount = 0;
		for (int i = 0; m.find(i); i = m.end())
			masterCount++;

//        System.out.println(masterCount); //특정문자열(Pattern)의 갯수

		Pattern backupCnt = Pattern.compile("backup");
		Matcher mm = backupCnt.matcher(input);
		int backupCount = 0;
		for (int i = 0; mm.find(i); i = mm.end())
			backupCount++;

//        System.out.println(backupCount); //특정문자열(Pattern)의 갯수

		vrrpState.add("masterCount:" + masterCount);
		vrrpState.add("backupCount:" + backupCount);

//        Matcher match = getMatcher(".+master.*", input);
//        if(match.find())
//        {
//            // 3가지 상태를 나타내기 위해서 리스트에 2개의 개체를 담는다.
//            // 첫 번째 값이 null, 두 번째 값이 null: 비정상 상태
//            // 첫 번째 값이 있고, 두 번째 값이 null: 정상 상태
//            // 첫 번째 값이 있고, 두 번째 값이 있다: 비정상 상태, 하지만 해당 메시지를 보고서에 출력해야 한다.
//            // 위 방법은 좋지 않은 방법이기에 차후 Row 클래스와 Parser 클래스를 병합하거나 다른 방법을 찾아서 해결해야 한다.
//            vrrpState.add(match.group(0));
//        }

		return vrrpState;
	}

//    public static void main(String[] args)
//    {
//        System.out.println(new VrrpStateParser("test").parse("------------------------------------------------------------------\nVRRP information:\n131: vrid  131, 192.168.100.131, if  1, renter, prio 100, master, server\n"));
//        System.out.println(new VrrpStateParser("test").parse(">> ADC_2424_22.0.4 - Main# /info/l3/vrrp\n" + 
//                "------------------------------------------------------------------\n" + 
//                "VRRP information:                                                          \r\n" +
//                " 131: vrid  131, 192.168.100.131, if  1, renter, prio 100, master, server  \r\n" + 
//                "\n" + 
//                ">> ADC_2424_22.0.4 - Standalone ADC - Layer 3# "));
//        
//        System.out.println(new VrrpStateParser("test").parse(
//        ">> scard_alt_slb1 - Standalone ADC - Layer 3# /info/l3/vrrp\r\n" + 
//        "------------------------------------------------------------------\r\n" + 
//        "------------------------------------------------------------------\n" + 
//        "VRRP information:                                                          \r\n" +
//        " 131: vrid  131, 192.168.100.131, if  1, renter, prio 100, master, server  \r\n" + 
//        " 132: vrid  132, 192.168.100.132, if  1, renter, prio 100, master, server  \r\n" +
//        " 133: vrid  133, 192.168.100.133, if  1, renter, prio 100, backup, server  \r\n" +
//        " 134: vrid  134, 192.168.100.134, if  1, renter, prio 100, master, server  \r\n" +
//        "\n" + 
//        ">> scard_alt_slb1 - Server Load Balancing Statistics# "));
//        
//        System.out.println(new VrrpStateParser("test").parse(
//        ">> scard_alt_slb1 - Standalone ADC - Layer 3# /info/l3/vrrp\r\n" + 
//        "------------------------------------------------------------------\r\n" + 
//        "------------------------------------------------------------------\n" + 
//        "VRRP information:                                                          \r\n" +
//        " 131: vrid  131, 192.168.100.131, if  1, renter, prio 100, master, server  \r\n" + 
//        " 132: vrid  132, 192.168.100.132, if  1, renter, prio 100, backup, server  \r\n" +
//        " 133: vrid  133, 192.168.100.133, if  1, renter, prio 100, backup, server  \r\n" +
//        " 134: vrid  134, 192.168.100.134, if  1, renter, prio 100, backup, server  \r\n" +
//        "\n" + 
//        ">> scard_alt_slb1 - Server Load Balancing Statistics# "));
//    }
}
