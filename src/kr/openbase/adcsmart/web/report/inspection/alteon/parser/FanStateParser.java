package kr.openbase.adcsmart.web.report.inspection.alteon.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import kr.openbase.adcsmart.web.report.inspection.parser.AbstractAlteonParser;

/**
 * 2, 3 시리즈에서는 접속이 되면 정상이다. 4 시리즈에서는 OK인 경우에만 정상이다. 4 시리즈 이상에서 파워가 하나만 연결됐을 경우에도
 * 비정상이지만 이럴 경우 확인사항에 하나만 연결됐음을 적시해준다.
 * 
 * @author 최영조
 */
public class FanStateParser extends AbstractAlteonParser {
	private static final int REFERENCE_MODEL_NUMBER = 4;

	private SeriesModelParser seriesParser;

	public FanStateParser(String swVersion, int modelNumber) {
		super(swVersion);

		// 4 시리즈 아래 모델
		if (modelNumber < REFERENCE_MODEL_NUMBER) {
			seriesParser = new LowerSeriesParser();
		} else {
			seriesParser = new UpperSeriesParser();
		}

	}

	@Override
	public List<String> parse(String input) {
		return seriesParser.parse(input);
	}

	interface SeriesModelParser {
		List<String> parse(String input);
	}

	// 2, 3 시리즈 모델
	class LowerSeriesParser implements SeriesModelParser {
		@Override
		public List<String> parse(String input) {
			final List<String> FanState = new ArrayList<String>();

			Matcher match = getMatcher(".+OK.*|(Operational.+)", input);
			if (match.find()) {
				FanState.add(match.group());
			}

			return FanState;
		}
	}

	// 4 시리즈 이상 모델
	class UpperSeriesParser implements SeriesModelParser {
		@Override
		public List<String> parse(String input) {
			final List<String> FanState = new ArrayList<String>();

//            input = "Fans table: \n" +
//                        "Fan-Number Type                Status           Speed \n" +  
//                        "---------- ------------      -----------      ----------- \n" + 
//                        "7          Critical           Failed               99%  \n" + 
//                        "9          Non-critical       Operational          59%  \n" + 
//                        "12         Critical           Failed          29% ";
			Matcher match = getMatcher(".+Failed.*|(not operational.+)", input);

			// 23.x
			/*
			 * Fans OK.
			 */
			// 29.x
			/*
			 * Fans table: Fan-Number Type Status Speed ---------- ------------ -----------
			 * ----------- 7 Critical Operational 49% 9 Critical Operational 49% 12 Critical
			 * Operational 49%
			 */
			while (match.find()) {
				// 3가지 상태를 나타내기 위해서 리스트에 2개의 개체를 담는다.
				// 첫 번째 값이 null, 두 번째 값이 null: 비정상 상태
				// 첫 번째 값이 있고, 두 번째 값이 null: 정상 상태
				// 첫 번째 값이 있고, 두 번째 값이 있다: 비정상 상태, 하지만 해당 메시지를 보고서에 출력해야 한다.
				// 위 방법은 좋지 않은 방법이기에 차후 Row 클래스와 Parser 클래스를 병합하거나 다른 방법을 찾아서 해결해야 한다.
//                FanState.add(match.group());
//                FanState.add(match.group(1));

				String[] tempTxt = match.group().split(" ");
				String tempVal = tempTxt[0];
//                System.out.println(tempTxt);
//                System.out.println(tempVal);

				FanState.add(tempVal);
			}

			return FanState;
		}
	}

//    public static void main(String[] args)
//    {
//        System.out.println(Pattern.matches(".+\\d\\d\\d\\d", "Alteon Application Switch 2408"));
//        // 22.X
//        System.out.println(new FanStateParser("test", 2).parse(">> 192.168.100.11_2424 - Main# /info/sys/fan\r\n" + "Error: unknown command \"ps\"\r\n" + "------------------------------------------------------------\r\n" + "[System Menu]\r\n" + "     snmpv3   - SNMPv3 Information Menu\r\n" + "     general  - Show general system information\r\n" + "     log      - Show last 30 syslog messages\r\n" + "     slog     - Display syslog messages saved in flash\r\n" + "     mgmt     - Show Management Port information\r\n" + "     sonmp    - Show SONMP topology table information\r\n" + "     capacity - Show switch capacity information\r\n" + "     fan      - Show switch fan status\r\n" + "     temp     - Show switch temperature sensor status\r\n" + "     dump     - Dump all system information\r\n" + "\r\n" + ">> 192.168.100.11_2424 - System# "));
//        // 25.X
//        System.out.println(new FanStateParser("test", 3).parse(">> scard_alt_slb1 - Main# /info/sys/fan\r\n" + "Error: unknown command \"ps\"\r\n" + "------------------------------------------------------------\r\n" + "[System Menu]\r\n" + "     snmpv3   - SNMPv3 Information Menu\r\n" + "     general  - Show general system information\r\n" + "     time     - Show date and time\r\n" + "     log      - Show last 64 syslog messages\r\n" + "     slog     - Show last 64 syslog messages saved in FLASH\r\n" + "     mgmt     - Show management port information\r\n" + "     sonmp    - Show SONMP topology table information\r\n" + "     capacity - Show switch capacity information\r\n" + "     fan      - Show switch fan status\r\n" + "     temp     - Show switch temperature sensor status\r\n" + "     encrypt  - Show switch encryption licenses\r\n" + "     user     - Show current user status\r\n" + "     dump     - Dump all system information\r\n" + "\r\n" + ">> scard_alt_slb1 - System# "));
//        // 28.X
//        System.out.println(new FanStateParser("test", 4).parse(">> 192.168.100.201_4408 - Standalone ADC - Main# /info/sys/fan\r\n" + "Fans OK\r\n" + "\r\n" + ">> 192.168.100.201_4408 - Standalone ADC - System# "));
//        System.out.println(new FanStateParser("test", 5).parse("Operational\r\n" + "\r\n" + ">> 192.168.100.14_5412 - Standalone ADC - System# "));
//    }
}
