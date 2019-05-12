package kr.openbase.adcsmart.service.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeF5;

public class OBParser {
//	public static void main(String[] args)
//	{
///*
//		String out = escapeSql("'\\");
//		System.out.println(out);
//		System.out.println(java.util.regex.Pattern.quote("aa\\daaa"));
//*/
//
//		ArrayList<Integer> numbers = new ArrayList<Integer>();
//		numbers.add(1);
//		numbers.add(2);
//		numbers.add(3);
//		System.out.println("from: " + numbers);
//		System.out.println("to  : " + convertList2CommaString(numbers));
//		System.out.println("no space: " + removeSpaces(convertList2CommaString(numbers)));
//
///*
// 		//TEST : integerList2CommaString();
// 		String in1 = null;
//		in1.add(1);
//		in1.add(2);
//		System.out.println("Integer list = " + in1.toString());
//		String res = integerList2CommaString(in1);
//		System.out.println("String list = " + res);
//*/
///*
//		String in2 = null, out = null;
//		//TEEST: convertMultipleSapces2SingleSpace()
//		in2 = String.format("%s\n%s\n%s\n", "aaaa  b  c ddd   eee", "a b  c   ddd", "kkk ddd c");
//		out = convertMultipleSpaces2SingleSpace(in2); //line은 유지하고 space만 바꾸는 함수 테스트
//		System.out.println(String.format("result of convertMultipleSapces2SingleSpace(): [%s] --> [%s]", in2, out));
//*/
//
//		Set<String> in3 = new HashSet<String>();
//		in3.add("aaa");
//		in3.add("bbb");
//		in3.add("ccc");
//		System.out.println("result1 = " + convertList2CommaString(in3));
//		System.out.println("result2 = " + convertList2SingleQuotedCommaString(in3));
//	}
	public static boolean moveFile(String source, String dest) throws OBException {
		boolean result = false;

		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;

		try {
			inputStream = new FileInputStream(source);
			outputStream = new FileOutputStream(dest);

			FileChannel fcin = inputStream.getChannel();
			FileChannel fcout = outputStream.getChannel();

			long size = 0;

			size = fcin.size();
			fcin.transferTo(0, size, fcout);

			fcout.close();
			fcin.close();
			outputStream.close();
			inputStream.close();

			result = true;

			File f = new File(source);
			if (f.delete()) {
				result = true;
			}
			return result;
		} catch (Exception e) {
			result = false;
			throw new OBException(OBException.ERRCODE_SYSTEM_FILE_MOVE);
		}
	}

//	public static void main(String [] args) throws OBException
//	{
//		propertyToString("adcsmart.properties","site.isSDSSite");
//	}

	/**
	 * 지정된 파일내에서 지정된 key에 대한 value를 찾아 리턴한다.
	 * 
	 * @param fileName
	 * @param key
	 * @return 지정된 key에 대한 value가 없는 경우에는 ""을 리턴한다.
	 * @throws OBException
	 */
	public static String propertyToString(String fileName, String key) throws OBException// adcsmart.properties 파일 읽어와서
																							// key값의 true, false를 문자열로
																							// 반환
	{
		String retVal = "";
		try {
			String fileContents = OBStringFile.filePathtoString(fileName);
			retVal = parseKeywordValue(fileContents, key, "=", 0, 5);
			if (retVal == null)
				return "";

			retVal = retVal.trim();

			if (retVal == null) {
				return "";
			}
		} catch (Exception e) {
			return "";
//			throw new OBException(e.getMessage());
		}
		return retVal;
	}

	public static String escapeSql(String body) {
		return StringEscapeUtils.escapeSql(body);
	}

//	public static void main(String[] args)
//	{
//			String test = "bwpark kkk'aaa'";
//			String out = replaceString(test, "kkk", "ss");
//			System.out.println(out);
//	}

	// 0f --> 00001111 이렇게 바꾼다. 앞에 0으로 시작해도 바이트가 빠지지 않게 했다.
	// 주의 한 바이트씩 처리한다.
	// 이 함수는 Alteon SNMP에서 bitmap 형식으로 들어오는 값을 parsing할 때 필요하다.
	public static String hexByteToBinary(String hex) {
		Integer i = Integer.parseInt(hex, 16);
		String bin = Integer.toBinaryString(i);
		int leadingZeroCount = (8 - bin.length());
		for (i = 0; i < leadingZeroCount; i++) {
			bin = "0" + bin;
		}
		return bin;
	}

	// 16진수 문자열 A03F5EFF09 ... 를 byte 배열로 바꾼다.
	// Alteon snmp hex string을 byte로 바꿀 때 쓰고 있다.
	public static byte[] hexStringToBytes(String value) {
		int i = 0;
		byte[] ba = new byte[value.length() / 2];
		for (i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer.parseInt(value.substring(i * 2, (i * 2) + 2), 16);
		}
		return ba;
	}

	// utf-8 바이트를 String으로 바꾼다.
	// Alteon snmp 한글 식별에 쓴다.
	public static String utf8ByteToStringA(byte[] bytes) {
		try {
			return new String(bytes, "utf-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	// MS949 바이트를 String으로 바꾼다.
	// Alteon snmp 한글 식별에 쓴다.
	public static String ms949ByteToString(byte[] bytes) {
		try {
			return new String(bytes, "MS949");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public static String replaceString(String body, String target, String replacement) {
		body = body.replace(target, replacement);
		return body;
	}

	public static String getRandomString(int length) {
		StringBuffer buffer = new StringBuffer();
		Random random = new Random();

		String chars[] = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");

		for (int i = 0; i < length; i++) {
			buffer.append(chars[random.nextInt(chars.length)]);
		}
		return buffer.toString();
	}

	public static String makeIndexString(Integer adcIndex, String name) {
		return adcIndex + "_" + name;
	}

//	public final static void main(String[] args) throws OBException //trimAndSimplifySpaces() test 
//	{
//		System.out.println(sqlString("aaaa\\saa"));
//		System.out.println(ms949ByteToString(hexStringToBytes("c7d1b1dbc0ccb8a7")));
//	}
	public static String convertCommaSqlInList(String list) throws OBException {
		String retVal = "";
		try {
			if (list == null)
				return retVal;
			String[] arg = list.split(",");

			for (String inList : arg) {
				if (!retVal.isEmpty())
					retVal += ", ";
				retVal += OBParser.sqlString(inList);
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	public static String convertSqlRealServerList(ArrayList<OBDtoAdcNodeF5> realList) throws OBException {
		String retVal = "";
		try {
			if (realList == null || realList.size() == 0)
				return retVal;
			for (OBDtoAdcNodeF5 rsIndex : realList) {
				if (!retVal.isEmpty())
					retVal += ", ";
				retVal += OBParser.sqlString(rsIndex.getIndex());
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	public static String convertSqlGrpIndexList(ArrayList<Integer> grpList) throws OBException {
		String retVal = "";
		try {
			if (grpList == null || grpList.size() == 0)
				return retVal;
			for (Integer rsIndex : grpList) {
				if (!retVal.isEmpty())
					retVal += ", ";
				retVal += rsIndex;
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	public static String sqlString(String text) throws OBException {
		if (text == null) {
			return null;
		}
		// postgresql string escape 규칙
		// - query의 문자열 안에 --, ", ;은 문제가 없다.
		// - query의 문자열 안에 \이나 '을 쓰려면 escape해야 한다. \\, \'
		// 버퍼가 여러줄일 때 매칭하려면 M과 S modifier를 조함해서 써야한다. m:multiline, s:DOTALL,
		// singleline이라고도 함.
		if (text.matches("(?ms).*['\\\\].*")) // '나 \이 있으면 escape처리에 들어간다. \\\\은 정규식으로는 \\이고, 정규식 밖으로 뺀 문자로는 \이다.
		{
			// look ahead를 써야한다.
			// 추가할 문자가 \이기 때문에 '과 \을 따로 처리할 수 없고, 정규식으로 처리하려면 lookahead로 '이나 \앞에 \을 하나 더 넣어야
			// 한다. 다른 처리는 부정확하다.
			text = text.replaceAll("(?ms)(?=['\\\\])", "\\\\"); // 참고: 인자 둘 다 정규식이다.
		}
		return "'" + text + "'";
	}

	// sqlString 테스트 - 중요:주석쳤더라도 지우지 말것. ykkim
	// 아래 함수의 테스트 결과
	// input[aaa\;--'bbb], result['aaa\\;--\'bbb']
	// input[aaa\bbb], result['aaa\\bbb']
//	public final static void main(String[] args)
//	{
//		try
//		{
//			String input = "aaa\\;--'bbb"; //실제로 column에 넣고 싶은 문자는 "aaa\;--'bbb"
//			String result = sqlString(input);
//			System.out.println(String.format("input[%s], result[%s]", input, result));
//			input = "aaa\\bbb"; //실제로 column에 넣고 싶은 문자는 "aaa\bbb"
//			result = sqlString(input);
//			System.out.println(String.format("input[%s], result[%s]", input, result));
//		}
//		catch(OBException e)
//		{
//			e.printStackTrace();
//		}
//	}

	public static String sqlString(Timestamp time) {
		if (time == null)
			return null;
		return "'" + time + "'";
	}

	public static String removeWhitespace(String body) {
		String[] data = body.split(" ");
		String out = "";
		for (int i = 0; i < data.length; i++) {
			if (data[i].isEmpty())
				continue;
			out += data[i];
		}
		return out;
	}

	// 머리의 space를 지우는 것 만이 아니라, 중간 space를 축약하기도 한다. 오류는 안 나지만 기존 space 축약 기능과 중복으로
	// 쓰고 잇는지 파악 필요. 2013.10.29 ykkim.
	public static String removeFirstWhitespace(String body) {
		String[] data = body.split(" ");
		String out = "";
		for (int i = 0; i < data.length; i++) {
			if (data[i].isEmpty())
				continue;
			if (!out.isEmpty())
				out += " ";
			out += data[i];
		}
		return out;
	}

	public static String removeString(String body, String delimeter) {
		if (body == null)
			return null;

		String[] data = body.split(delimeter);
		String out = "";
		for (int i = 0; i < data.length; i++) {
			if (data[i].isEmpty())
				continue;
			out += data[i];
		}
		return out;
	}

	// #3984-1 #4: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
	public static String removeWildcard(String text) {
		return text.replaceAll("(?ms)(?=[%_])", "\\\\");
	}

	/**
	 * 주어진 조건에 맞도록 파싱 후 파싱된 데이터를 문자열 형태로 반환한다. 여러개의 라인형태로 구성된 문자열에서 keyword로 우선 해당
	 * 라인을 찾은 후에 해당 라인에서 delimeter로 파싱 후 position에 해당되는 값을 count만큼 문자열로 구성하여 반한다.
	 * 
	 * @param body      -- 파싱 대상이 되는 데이터.
	 *
	 * @param keyword   -- 찾고자 하는 키.
	 *
	 * @param delimeter -- 파싱이 사용되는 구분값.
	 *
	 * @param position  -- 리턴하고자 하는 문자열의 시작 위치.
	 *
	 * @param count     -- 리턴하고자 하는 문자열의 개수.
	 *
	 * @return 원하는 데이터. 데이터가 없을 경우에는 null을 반환함.
	 * 
	 */
	public static String parseKeywordValue(String body, String keyword, String delimeter, int position, int count) {
		int firstIndex = body.indexOf(keyword);// 키워드 문자열 찾기.
		if (firstIndex < 0) {
			return null;
		}

		String secondSearch = body.substring(firstIndex + keyword.length());

		String[] lines = secondSearch.split("\r\n");

		// 첫번째 라인만 이용해서 파싱 작업 실시.
		if (delimeter.isEmpty())
			return lines[0];

		String[] data = lines[0].split(delimeter);// , count+1);

		int iStartPos = 0;
		int storeCnt = 0;
		String out = "";
		for (int i = 0; i < data.length; i++) {
			if (data[i].isEmpty())
				continue;
			if (iStartPos >= position) {
				if (out.isEmpty())
					out = data[i];
				else
					out += (" " + data[i]);
				storeCnt++;
				if (storeCnt >= count)
					break;
			}
			iStartPos++;
		}

		return out;
	}

	/**
	 * 입력된 데이터가 숫자인지 판단한다.
	 * 
	 * @param str -- 판별하고자 하는 데이
	 *
	 * @return true: 입력데이터가 숫자일 경우. false: 입력데이터가 숫자가 아닐 경우.
	 * 
	 */
	public static boolean isNumeric(String str) {
		return StringUtils.isNumeric(str);
	}

	/**
	 * arrayList<String>의 각 멤버를 ''으로 묶고 comma로 구별하여 String으로 만든다. SQL query IN 구절에
	 * 유용하다.
	 * 
	 * @param list -- String ArrayList
	 * @return -- 각 item을 ''으로 묶고 comma로 구별한 String -- 예: "item1", "item2", "item3"을
	 *         멤버로 가지는 ArrayList의 결과 = "'item1','item2','item3'"
	 */
	public static String convertList2SingleQuotedString(ArrayList<String> list) {
		if (list == null) {
			return "";
		}
		return list.toString().replace("[", "'").replace("]", "'").replace(", ", "','");
	}

	/**
	 * Set<String>의 각 멤버를 comma로 구별하여 String으로 만든다.
	 * 
	 * @param list -- String Set
	 * @return -- 각 item을 comma로 구별한 String -- 예: "item1", "item2", "item3"을 멤버로 가지는
	 *         HashSet 결과 = "item1,item2,item3"
	 */
	public static String convertList2CommaString(Set<String> list) {
		if (list == null) {
			return "";
		}
		return list.toString().replace("[", "").replace("]", "").replace(", ", ",");
	}

	/**
	 * ArrayList<String>의 각 멤버를 comma로 구별하여 String으로 만든다.
	 * 
	 * @param list -- String ArrayList
	 * @return -- 각 item을 comma로 구별한 String -- 예: "item1", "item2", "item3"을 멤버로 가지는
	 *         HashSet 결과 = "item1,item2,item3"
	 */
	public static String convertArrayList2CommaString(ArrayList<String> list) {
		if (list == null) {
			return "";
		}
		return list.toString().replace("[", "").replace("]", "").replace(", ", ",");
	}

	/**
	 * Set<String>의 각 멤버를 comma로 구별하여 String으로 만든다. 멤버에 '를 붙인다.
	 * 
	 * @param list -- String ArrayList
	 * @return -- 각 item을 comma로 구별한 String -- 예: "item1", "item2", "item3"을 멤버로 가지는
	 *         HashSet 결과 = "'item1','item2','item3'"
	 */
	public static String convertList2SingleQuotedCommaString(Set<String> list) {
		if (list == null) {
			return "";
		}
		return list.toString().replace("[", "'").replace("]", "'").replace(", ", "','");
	}

	/**
	 * arrayList<Integer>의 각 멤버를 comma로 구별하여 String으로 만든다. SQL query IN 구절에 유용하다.
	 * 
	 * @param list -- Integer ArrayList
	 * @return -- 각 item을 comma로 구별한 String -- 예: 10, 20, 30을 멤버로 가지는 ArrayList의 결과
	 *         = "10,20,30"
	 */
	public static String convertList2CommaString(ArrayList<Integer> list) {
		if (list == null) {
			return "";
		}
		return list.toString().replace("[", "").replace("]", "");
	}

	public static String removeSpaces(String buffer) {
		if (buffer == null) {
			return null;
		}
		return buffer.replace(" ", "");
	}

	/**
	 * 문자열에서 붙어 있는 space 여러개를 1개로 바꾼다. space에 해당하는 문자는 다음과 같다 : space(\x20), tab
	 * 
	 * @param buffer
	 * @return
	 */
	public static String convertMultipleSpaces2SingleSpace(String buffer) // 이 함수 쓰는 곳은 테스트해서 모두 아래 함수로 바꿀 것
	{
		if (buffer == null) {
			return null;
		}
		return buffer.replaceAll("\\t+|\\x20+", " ");
	}

	/**
	 * 라인 머리와 꼬리의 space를 trim, 중간의 복수 space는 1개로 축약, 차차
	 * convertMultipleWhiteSpaces2SingleSpace()를 대체할 것
	 * 
	 * @param buffer
	 * @return
	 */
	public static String trimAndSimplifySpaces(String buffer) {
		if (buffer == null) {
			return null;
		}
		int i, j;
		String[] lines = buffer.split("\n"); // 라인으로 분리
		String out = "";
		String[] temp;
		for (i = 0; i < lines.length; i++) {
			if (lines[i].isEmpty() == false) {
				temp = lines[i].trim().split("\\x20+");
				for (j = 0; j < temp.length; j++) {
					out += temp[j];
					if (j < temp.length - 1) {
						out += " "; // 중간에만 space 붙임
					}
				}
			}
			if (i < lines.length - 1) {
				out += "\n";
			}
		}
		return out;
	}
//	public final static void main(String[] args) //trimAndSimplifySpaces() test 
//	{
//		String input = "   aaa   bbb   ccc   \n   ddd  eee  ff \n\n\n";
//		String result = trimAndSimplifySpaces(input);
//		System.out.println(String.format("input[%s], result[%s]", input, result));
//	}

	/**
	 * <pre>
	 * 단일행 csv에서 길이를 초과한 분량만큼 잘라 외 n건으로 표기한다.
	 * 해석하는 csv 스펙은 다음과 같다.
	 * withDelimiter(','): 콤마로 구분한 값
	 * withQuote('"'): 값에 구분자 포함시 쌍따옴표로 감싼다.
	 * </pre>
	 * 
	 * @param csv: 단일행의 쉼표로 구분된 값
	 * @param length: 제한하고자 하는 길이
	 * @param remainMessage: 잔여 항목 표시 메세지, 잔여항목 수는 %d로 표기(ex: 외 %d건)
	 * @return 커스텀 csv형태: ', '로 구분된 값. "" 없음
	 * @throws OBException
	 */
	public static String trimCsv(String csv, int limit, String remainMessage) throws OBException {
		// CSV 파싱을 통해 record 셋으로 만듬
		String resultString = "";
		StringReader reader = new StringReader(csv);
		try {
			CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
			// 첫행에 대해서만 해석을 수행한다. 나머지 행은 무시한다.
			CSVRecord record = parser.getRecords().get(0);
			parser.close();

			// CSV 포맷 설정
			String delimeter;
			String value;
			int printSize = 0;
			for (int i = 0; i < record.size(); i++) {
				delimeter = i > 0 ? ", " : "";
				value = record.get(i).trim();
				if ((resultString + delimeter + value).length() > limit) // is Overflow
				{
					// 말줄임 처리가 가능하다면 마지막 값을 말줄임 처리해 삽입
					if (value.length() > (delimeter + "...").length()) {
						resultString += delimeter + value;
						resultString = resultString.substring(0, limit - "...".length());
						printSize++;
					}
					break;
				}

				// 결과가 초과되지 않았다면 연장 후 계속
				resultString += delimeter + value;
				printSize++;
			}
			int remain = record.size() - printSize;
			if (remain > 0)
				resultString = resultString + String.format(remainMessage, remain);
		} catch (IOException e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "csv: " + csv);
		}

		return resultString;
	}

	/**
	 * Java Regexp에 사용되는 예약어들을 이스케이프 처리한다.
	 * 
	 * @param regex 이스케이프할 대상 문자열
	 * @return 이스케이프된 문자열
	 * @see java.util.regex.Pattern
	 */
	public static String escapeRegex(String regex) {
		return regex.replaceAll("([\\\\\\.\\[\\{\\(\\*\\+\\?\\^\\$\\|])", "\\\\$1");
	}

	public static String oidAlteonEnh(String poolID) {
		String retVal = "";
		ArrayList<Integer> poolIndex = OBCommon.stringToascii(poolID);
		int poolLength = poolIndex.size();
		retVal = "." + poolIndex.size();
		for (int i = 0; i < poolLength; i++) {
			retVal += ".";
			retVal += poolIndex.get(i);
		}
		return retVal;
	}

	public static String extraAddPort29V(String addPort) {
		String retVal = "";
		if (addPort == null) {
			return retVal;
		}
		String arg[] = addPort.split(",");
		retVal = arg[0].replace("port:", "");
		return retVal;
	}

	public static String extraAddPort(String addPort) {
		String retVal = "";
		if (addPort == null) {
			return retVal;
		}
		retVal = addPort.replace("[", "").replace("]", "").replace(",", "");
		return retVal;
	}

//	public final static void main(String[] args) //getAdcsmartIP test
//    {
////        String input = "w\n"
////+ "\n"
////+"USERTERMLOGIN TIMEFROM IPLAST CMD\n"
////+"=================================\n"
////+"admin Telnet13:56:13172.172.2.41w\n"
////+"SNMPSNMP17:17:15172.172.1.163Get Request\n"
////+"SNMPSNMP17:25:17172.172.1.163Get Request\n"
////+"SNMPSNMP17:28:48172.172.1.163Get Request\n"
////+"SNMPSNMP17:36:50172.172.1.163Get Next Request\n"
////+"SNMPSNMP19:09:48172.172.1.163Get Request\n"
////+"\n"
////+">> 192.168.100.11_[29.0.4_4024] - Standalone ADC - Main#";
////        String result = trimAndSimplifySpaces(input);
////        String special ="";
////        Pattern p = 
////                Pattern.compile("((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})w");
////             Matcher m = p.matcher(input);
////             
////             while(m.find()){
////                 special = m.group();
////                 System.out.println(special.substring(0, special.length() -1));
////             }
//              
////        System.out.println(input);
//    }

	public static ArrayList<String> getAdcsmartIP(String input) {
		ArrayList<String> result = new ArrayList<String>();
		String special = "";
		Pattern p = Pattern.compile(
				"((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})w");
		Matcher m = p.matcher(input);

		while (m.find()) {
			special = m.group();
			result.add(special.substring(0, special.length() - 1));
		}
		return result;
	}

	public static String getstate(Integer state) {
		String result = "";
		if (state == OBDefine.MEMBER_STATE.ENABLE) {
			result = "ENBALE";
		} else if (state == OBDefine.MEMBER_STATE.DISABLE) {
			result = "DISABLE";
		} else if (state == OBDefine.MEMBER_STATE.FORCED_OFFLINE) {
			result = "FORCEOFFLINE";
		}
		return result;
	}

	public static String getServiceType(Integer type) {
		String result = "";
		if (type == OBDefine.PROTOCOL.TELNET) {
			result = "TELNET";
		} else if (type == OBDefine.PROTOCOL.SSH) {
			result = "SSH";
		}
		return result;
	}

	public static String getSnmpVersion(Integer snmpversion) {
		String result = "";
		if (snmpversion == OBDefine.SNMPVERSION.SNMPV2) {
			result = "VersionTwo";
		} else if (snmpversion == OBDefine.SNMPVERSION.SNMPV3) {
			result = "VersionThree";
		}
		return result;
	}
}
