package kr.openbase.adcsmart.service.impl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketException;

import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionTimeout;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.telnet.TelnetClient;

public class OBTelnetCmndExecV2 {
	private TelnetClient telnetClient = null;// = new TelnetClient();
	private BufferedInputStream inStream = null;
	private PrintStream outStream = null;

	private String SUFFIX_LOGIN_PROMPT = "login: ";
	private String SUFFIX_PASSWD_ROMPT = "Password: ";
	private String SUFFIX_ROMPT_OK = "Main# ";
	private String SUFFIX_CONTINUE = "[0m";// "[7mPress q to quit, any other key to continue[0m"
	private String SUFFIX_CONTINUE2 = "[K";// "[7mPress q to quit, any other key to continue[0m"
	private String SUFFIX_MORE = "--More--";// "More--[m, PASK 1.7에서는 "[7m--More--[m\x0D", 포함관계
	private String SUFFIX_CONFIRM = "note [y]: ";// "Confirm seeing above note [y]: "
	private String SUFFIX_CONFIRM_YN = "[y/n]: ";// "Confirm saving to FLASH [y/n]: "
	private String SUFFIX_CONFIRM_12 = "Confirm Option [1/2]: ";// "Confirm Option [1/2]: "

	private String userName = "";
	private String password = "";
	private String serverName = "";

	private String command = "";
	private String retMssage = "";
	private Integer portNum = 23; // default, changeable
	final static int DEFULT_TIMEOUT_COUNT = 20;
	final static int DEFAULT_SOCKET_TIMEOUT = 100;

	class BufferInfo {
		Integer lineCount;
		String data;

		@Override
		public String toString() {
			return "BufferInfo [lineCount=" + lineCount + ", data=" + data + "]";
		}

		public Integer getLineCount() {
			return lineCount;
		}

		public void setLineCount(Integer lineCount) {
			this.lineCount = lineCount;
		}

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}
	}

	private Integer TELNET_CONNECT_TIMEOUT = 5000;

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getRetMssage() {
		return retMssage;
	}

	public void setRetMssage(String retMssage) {
		this.retMssage = retMssage;
	}

	/**
	 * 로그인 접속 정보를 설정한다. 반드시 수행되어야 하는 작업이다.
	 * 
	 * @param server
	 * @param user
	 * @param password
	 */
	public void setConnectionInfo(String server, String user, String password) {
		this.serverName = server;
		this.userName = user;
		this.password = password;
		try {
			String propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME,
					OBDefine.PROPERTY_KEY_TELNET_CONNECT_TIMEOUT);
			if (propertyValue != null && !propertyValue.isEmpty())
				this.TELNET_CONNECT_TIMEOUT = Integer.parseInt(propertyValue);
		} catch (Exception e) {
		}
	}

	public void setConnectionInfo(String server, String user, String password, Integer portNum) {
		this.serverName = server;
		this.userName = user;
		this.password = password;
		this.portNum = portNum;
		try {
			String propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME,
					OBDefine.PROPERTY_KEY_TELNET_CONNECT_TIMEOUT);
			if (propertyValue != null && !propertyValue.isEmpty())
				this.TELNET_CONNECT_TIMEOUT = Integer.parseInt(propertyValue);
		} catch (Exception e) {
		}
	}

	/**
	 * telnet emulation을 위한 prompt을 설정한다. 생략할 경우에는 default 값으로 사용된다.
	 * 
	 * @param login
	 * @param passwd
	 * @param prompt
	 */
	public void setPromptInfo(String login, String passwd, String prompt) {
		this.SUFFIX_LOGIN_PROMPT = login;
		this.SUFFIX_PASSWD_ROMPT = passwd;
		this.SUFFIX_ROMPT_OK = prompt;
	}

	/**
	 * 로그인 과정을 수행한다. 본 함수 호출 전에 setConnectionInfo(..), setPromptInfo(..)_을 통해 초기화를
	 * 수행해야 한다.
	 * 
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
	public synchronized final void login() throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
				"server = " + this.serverName + " / user = " + this.userName + " / pass = " + this.password);
		for (int i = 0; i < 3; i++) {
			try {
				loginProcess();
				return;
			} catch (OBExceptionUnreachable e) {
				throw e;
			} catch (OBExceptionLogin e) {
				break;
			} catch (OBExceptionTimeout e) {
				disconnect();
				OBDateTime.Sleep(500);
				continue;
			}
		}
		throw new OBExceptionLogin(String.format("failed to login. host:%s", this.serverName));
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBTelnetCmndExecV2 handler = new OBTelnetCmndExecV2();
//			boolean retVal = handler.isReachable("192.168.100.11", 23);
//			System.err.println(retVal);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}	

	public synchronized final boolean isReachable(String ipAddress, Integer portNum) throws OBException {
		TelnetClient client = new TelnetClient();

		try {
			client.setConnectTimeout(TELNET_CONNECT_TIMEOUT);
			client.connect(ipAddress, portNum);
		} catch (SocketException e) {
			return false;
		} catch (IOException e) {
			return false;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		try {
			if (client != null)
				client.disconnect();
		} catch (Exception e) {
		}
		return true;
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBTelnetCmndExecV2 handler = new OBTelnetCmndExecV2();
//			handler.setConnectionInfo("192.168.200.111", "root", "default", 443);
//			handler.login();
//			System.err.println("login done");
//			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "login done");
////			String retVal="";
////			retVal = handler.sendCommand("configure", "# ");
////			System.err.println(retVal);
////			retVal = handler.sendCommand("write memory", "# ", 60);
////			System.err.println("write memory done");
////			retVal = handler.sendCommand("show info slb", "# ");
////			System.err.println(retVal);
////			retVal = handler.sendCommand("configure", "# ");
////			retVal = handler.sendCommand("ntp", "# ");
////			retVal = handler.sendCommand("current", "# ");
////			System.err.println(retVal);
////			retVal = handler.sendCommand("exit", "# ");
////			retVal = handler.sendCommand("exit", "# ");
////			retVal = handler.sendCommand("show logging buffer", "# ");
////			System.err.println(retVal);
////			retVal = handler.sendCommand("show running-config slb", "# ");
////			System.err.println(retVal);
//			handler.disconnect();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	// timeout: second 단위.
	public synchronized final String sendCommandTimeout(String cmnd, String suffix, int timeOut) throws OBException {
		byte[] b = new byte[5000];
		int cnt = 0;
		int off = 0;
		int len = b.length;

		if (cmnd != null) {
			clearInStream();// input stream 버퍼 초기화.
			this.command = cmnd;
			this.retMssage = "";
			writeMsg(cmnd);
			OBDateTime.Sleep(100);
		}

		int iReadTimeoutCnt = 0;
		String retVal = "";
		String line;
		BufferInfo buffer;
		do {
			try {
				cnt = this.inStream.read(b, off, len);
				if (cnt < 0)
					break;
				if (cnt == 0)
					continue;
//        		System.out.println("aaaa:"+ new String(b, off, cnt));
				iReadTimeoutCnt = 0;
			} catch (IOException e) {
				if (e.getMessage().indexOf("Read timed out") >= 0) {
					iReadTimeoutCnt++;
					if (iReadTimeoutCnt > timeOut) {
						this.retMssage = retVal;
						throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
								String.format("failed to recv data(%s), server:%s", e.getMessage(), this.serverName));
					}
				}
				continue;
			}

			buffer = normalize(b, off, cnt);
			line = buffer.getData();
			retVal += line;
			cnt = 0;
			off = 0;
			len = b.length;

			if (line.endsWith(suffix)) {
				this.retMssage = retVal;
				return retVal;// break;
			}

			if (line.endsWith(this.SUFFIX_CONTINUE)) {
				writeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (line.endsWith(this.SUFFIX_CONFIRM)) {
				writeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (line.endsWith(this.SUFFIX_CONFIRM_YN)) {
				writeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (line.endsWith(this.SUFFIX_CONFIRM_12)) {
				writeMsg("2");
				OBDateTime.Sleep(100);
				continue;
			}

			if (line.contains(this.SUFFIX_MORE)) {
				writeMsg("  ");
				OBDateTime.Sleep(100);
				continue;
			}
		} while (cnt >= 0);
		this.retMssage = retVal;
		return null;
	}

	/**
	 * 일정 시간 동안만 CLI 명령어를 실행시킨다. OpenSSH 업그레이드 작업을 하면서 성공 판별 문자열이 달라졌기 때문에 내부에서 사용하는
	 * 메소드가 조금 달라졌다. 작업 5535 을 참고한다.
	 * 
	 * @param cmnd
	 * @param suffix
	 * @param timeOut
	 * @return
	 * @throws OBException
	 */
	// timeout: second 단위.
	public synchronized final String sendCommand(String cmnd, String suffix, int timeOut) throws OBException {
		byte[] b = new byte[5000];
		int cnt = 0;
		int off = 0;
		int len = b.length;

		if (cmnd != null) {
			clearInStream();// input stream 버퍼 초기화.
			this.command = cmnd;
			this.retMssage = "";
			writeMsg(cmnd);
			OBDateTime.Sleep(100);
		}

		int iReadTimeoutCnt = 0;
		String retVal = "";
		String line;
		BufferInfo buffer;
		do {
			try {
				cnt = this.inStream.read(b, off, len);
				if (cnt < 0)
					break;
				if (cnt == 0)
					continue;
//              System.out.println("aaaa:"+ new String(b, off, cnt));
				iReadTimeoutCnt = 0;
			} catch (IOException e) {
				if (e.getMessage().indexOf("Read timed out") >= 0) {
					iReadTimeoutCnt++;
					if (iReadTimeoutCnt > timeOut) {
						this.retMssage = retVal;
						throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
								String.format("failed to recv data(%s), server:%s", e.getMessage(), this.serverName));
					}
				}
				continue;
			}

			buffer = normalize(b, off, cnt);
			line = buffer.getData();
			retVal += line;
			cnt = 0;
			off = 0;
			len = b.length;

			// 기존의 endswith 메소드에서 contains 로 변경했다.
			if (line.contains(suffix)) {
				this.retMssage = retVal;
				return retVal;
			}

			if (line.endsWith(this.SUFFIX_CONTINUE)) {
				writeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (line.endsWith(this.SUFFIX_CONFIRM)) {
				writeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (line.endsWith(this.SUFFIX_CONFIRM_YN)) {
				writeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (line.endsWith(this.SUFFIX_CONFIRM_12)) {
				writeMsg("2");
				OBDateTime.Sleep(100);
				continue;
			}

			if (line.contains(this.SUFFIX_MORE)) {
				writeMsg("  ");
				OBDateTime.Sleep(100);
				continue;
			}
		} while (cnt >= 0);
		this.retMssage = retVal;
		return null;
	}

	// 특수 문자를 제거 & 라인 카운트
	private BufferInfo normalize(byte[] byteArray, int offset, int length) {
		StringBuilder sb = new StringBuilder(length);
		Integer lineCount = 0;
		BufferInfo ret = new BufferInfo();
		ret.setLineCount(0);
		for (int i = offset; i < offset + length; i++) {
			byte one = byteArray[i];
//			if((one>=32 && one<=126)|| one=='\r' || one=='\n')
			if ((one >= 32 && one <= 126) || one == '\n')// '\r' 은 특수문자로 인식한다.
			{
				sb.append((char) one);
				if (one == '\n') {
					lineCount++;
				}
			}
		}
		ret.setData(sb.toString());
		ret.setLineCount(lineCount);
		return ret;
	}

	public synchronized final String sendCommand(String cmnd, String suffix) throws OBException // 결과수 제한이 없는
																								// sendCommand이면 결과수를
																								// -1로 한다.
	{
		return sendCommandForLimitedResult(cmnd, suffix, -1);
	}

	private synchronized final void clearInStream() {
		byte[] contents = new byte[1024];

		try {
			while (this.inStream.available() > 0) {
				this.inStream.read(contents);
			}
		} catch (IOException e) {
		}
	}

	public synchronized final String sendCommandForLimitedResult(String cmnd, String suffix, int resultLimit)
			throws OBException {
		byte[] b = new byte[2048];
		int cnt = 0;
		int off = 0;
//	    int len = b.length;

		if (cmnd != null) {
			clearInStream();// input stream 버퍼 초기화.
			writeMsg(cmnd);
			this.command = cmnd;
			this.retMssage = "";
			OBDateTime.Sleep(100);
		}

		int iReadTimeoutCnt = 0;
		int lineCount = 0;
		String retVal = "";
		BufferInfo buffInfo;
		String line;
		do {
			try {
				cnt = this.inStream.read(b, 0, b.length);
				if (cnt < 0)
					break;
				if (cnt == 0) {
					OBDateTime.Sleep(100);
					continue;
				}
				iReadTimeoutCnt = 0;
			} catch (IOException e) {
//        		System.err.println("Exception while reading socket1:" + e.getMessage()+", cont:"+iReadTimeoutCnt+", cmnd:"+cmnd);        		
				if (e.getMessage().indexOf("Read timed out") >= 0) {
					iReadTimeoutCnt++;
					if (iReadTimeoutCnt > OBTelnetCmndExecV2.DEFULT_TIMEOUT_COUNT) {
						this.retMssage = retVal;
						throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
								String.format("failed to recv data(%s), server:%s", e.getMessage(), this.serverName));
					}
				}
				continue;
			}

			buffInfo = normalize(b, off, cnt);
			line = buffInfo.getData();
			if (line.isEmpty()) {
				continue;
			}

			retVal += line;
			lineCount += buffInfo.getLineCount();

			if (resultLimit > 0 && lineCount >= resultLimit) // 결과 수가 제한된 경우 그 개수를 넘으면 그만한다.
			{
				this.retMssage = retVal;
				return retVal; // break;
			}
			if (retVal.endsWith(suffix)) {
				this.retMssage = retVal;
				return retVal;// break;
			}

			if (retVal.endsWith(this.SUFFIX_CONTINUE)) {
				writeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (retVal.endsWith(this.SUFFIX_CONFIRM)) {
				writeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (retVal.endsWith(this.SUFFIX_CONFIRM_YN)) {
				writeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (line.endsWith(this.SUFFIX_CONFIRM_12)) {
				writeMsg("2");
				OBDateTime.Sleep(100);
				continue;
			}
			if (retVal.contains(this.SUFFIX_MORE)) {
				writeMsg("  ");
				OBDateTime.Sleep(100);
				continue;
			}
		} while (cnt >= 0);
		this.retMssage = retVal;
		return null;
	}

	// Alteon에서 exit은 save하지 않은 게 있을 경우 종료할지 질문이 나온다. 안 나올 수도 있다.
	// 1) save된 상태라면 즉시 종료해야 한다. suffix 대기 구조를 쓰면 안된다.
	// 2) save없이 종료를 묻는 prompt가 나올 수 있으므로 대기도 필요하다.
	// 위 두 상황이 상충되지만 즉시 종료는 필요하기 때문에 suffix 없이 즉시 종료하고, 중간에 prompt를 대비해 'y'를 보낸다.
	// 필요없을때 보내도 부작용은 없다.
	public synchronized final String sendCommandExit() throws OBException {
		String cmnd = "exit";
		writeMsg(cmnd);
		this.command = cmnd;
		this.retMssage = "";
		OBDateTime.Sleep(500);
		writeMsg("y"); // 보험

		this.retMssage = "";
		return null;
	}

	public synchronized final String sendCommandOnce(String cmnd, String suffix) throws OBException {
		byte[] b = new byte[5000];
		int cnt = 0;
		int off = 0;
//	    int len = b.length;

		if (cmnd != null) {
			clearInStream();// input stream 버퍼 초기화.
			writeMsg(cmnd);
			this.command = cmnd;
			this.retMssage = "";
			OBDateTime.Sleep(100);
		}

		int iReadTimeoutCnt = 0;
		String retVal = "";
		String line;
		BufferInfo buffer;
		do {
			try {
				cnt = this.inStream.read(b, 0, b.length);
				if (cnt < 0)
					break;
				if (cnt == 0) {
					OBDateTime.Sleep(100);
					continue;
				}
				iReadTimeoutCnt = 0;
			} catch (IOException e) {
//        		System.err.println("Exception while reading socket1:" + e.getMessage()+", cont:"+iReadTimeoutCnt+", cmnd:"+cmnd);        		
				if (e.getMessage().indexOf("Read timed out") >= 0) {
					iReadTimeoutCnt++;
					if (iReadTimeoutCnt > OBTelnetCmndExecV2.DEFULT_TIMEOUT_COUNT) {
						this.retMssage = retVal;
						throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
								String.format("failed to recv data(%s), server:%s", e.getMessage(), this.serverName));
					}
				}
				continue;
			}
//    		String line22 = new String(b, 0, cnt);
//        	System.err.println(line22);
//    		String line = new String(b);
			buffer = normalize(b, off, cnt);
			line = buffer.getData();
			cnt = 0;
			off = 0;
//			len = b.length;

			if (line.isEmpty()) {
				continue;
			}

			retVal += line;
			if (retVal.endsWith(suffix)) {
				this.retMssage = retVal;
				return retVal;// break;
			}

			if (retVal.endsWith(this.SUFFIX_CONTINUE)) {
				writeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (retVal.endsWith(this.SUFFIX_CONFIRM)) {
				writeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (retVal.endsWith(this.SUFFIX_CONFIRM_YN)) {
				writeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (line.endsWith(this.SUFFIX_CONFIRM_12)) {
				writeMsg("2");
				OBDateTime.Sleep(100);
				continue;
			}
			if (retVal.contains(this.SUFFIX_MORE)) {
				writeMsg("q ");
				OBDateTime.Sleep(100);
				continue;
			}
			if (retVal.endsWith(this.SUFFIX_CONTINUE2)) {
				writeMsg("q ");
				OBDateTime.Sleep(100);
				continue;
			}
		} while (cnt >= 0);
		this.retMssage = retVal;
		return null;
	}

	/**
	 * 명령을 전달한 후 리턴값을 수신한다.
	 * 
	 * @param loginSuffix: 로그인 과정에서 사용되는 login suffix
	 * @param loginMsg: 사용자 계정 정보. ID
	 * @param passwdSuffix: password 전달을 위한 suffix
	 * @param passwdMsg: 패스워드 문자열.
	 * @param promptOK: 로그인 완료 메세지.
	 * @return
	 * @throws IOException
	 * @throws OBException
	 */
	private synchronized final boolean sendLoginCmnd(String loginSuffix, String loginMsg, String passwdSuffix,
			String passwdMsg, String promptOK) throws OBExceptionTimeout, OBException {
		byte[] b = new byte[5000];
		int cnt = 0;
		int off = 0;
		int len = b.length;

		int iReadTimeoutCnt = 0;
		String retVal = "";
		String line;
		BufferInfo buffer;
		do {
			try {
				cnt = this.inStream.read(b, off, len);
				if (cnt < 0)
					break;
				if (cnt == 0)
					continue;
				iReadTimeoutCnt = 0;
			} catch (IOException e) {
//              System.err.println("Exception while reading socket1:" + e.getMessage()+", cont:"+iReadTimeoutCnt);        		
				if (e.getMessage().indexOf("Read timed out") >= 0) {
//                  System.err.println("Exception while reading socket1:" + e.getMessage()+", cont:"+iReadTimeoutCnt);        		
					iReadTimeoutCnt++;
					if (iReadTimeoutCnt > OBTelnetCmndExecV2.DEFULT_TIMEOUT_COUNT) {
						this.retMssage = retVal;
						throw new OBExceptionTimeout(String.format("faild to receive data(%s). server:%s, rcvData:%s",
								e.getMessage(), this.serverName, retVal));
					}
				} else {
					System.err
							.println("Exception while reading socket:" + e.getMessage() + ", cont:" + iReadTimeoutCnt);
				}
				OBDateTime.Sleep(1);
				continue;
			}
			buffer = normalize(b, off, cnt);
			line = buffer.getData();
			if (line.isEmpty() == true) {
				off = off + cnt;
				len = len - cnt;
				continue;
			}
			cnt = 0;
			off = 0;
			len = b.length;

			retVal += line;

			if (retVal.contains(promptOK)) {
				this.retMssage = retVal;
				return true;// break;
			}

			if (retVal.endsWith(loginSuffix)) {
				retVal = "";
				OBDateTime.Sleep(100);
				writeMsg(loginMsg);
				OBDateTime.Sleep(100);
				continue;
			}

			if (retVal.endsWith(passwdSuffix)) {
				retVal = "";
				OBDateTime.Sleep(100);
				writeMsg(passwdMsg);
				OBDateTime.Sleep(100);
				continue;
			}

			if (retVal.endsWith(this.SUFFIX_CONTINUE)) {
				OBDateTime.Sleep(100);
				writeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (retVal.endsWith(this.SUFFIX_CONFIRM)) {
				writeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (retVal.endsWith(this.SUFFIX_CONFIRM_YN)) {
				OBDateTime.Sleep(100);
				writeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (line.endsWith(this.SUFFIX_CONFIRM_12)) {
				OBDateTime.Sleep(100);
				writeMsg("2");
				OBDateTime.Sleep(100);
				continue;
			}
			if (retVal.trim().contains(this.SUFFIX_MORE)) {
				OBDateTime.Sleep(100);
				writeMsg("  ");
				OBDateTime.Sleep(100);
				continue;
			}
		} while (cnt >= 0);
		this.retMssage = retVal;
		return false;
	}

	/**
	 * 로그인 과정을 수행한다.
	 * 
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBExceptionTimeout
	 */
	private synchronized void loginProcess()
			throws OBExceptionUnreachable, OBExceptionLogin, OBExceptionTimeout, OBException {
		this.telnetClient = new TelnetClient();

		try {
			this.telnetClient.setConnectTimeout(TELNET_CONNECT_TIMEOUT);
			this.telnetClient.connect(this.serverName, this.portNum);
		} catch (SocketException e) {
			throw new OBExceptionUnreachable(
					String.format("failed to make telnet session(%s). message:%s", this.serverName, e.getMessage()));
		} catch (IOException e) {
			throw new OBExceptionUnreachable(
					String.format("failed to make telnet session(%s). message:%s", this.serverName, e.getMessage()));
		}

		// 타임아웃 설정 : 5초
		try {
			this.telnetClient.setSoTimeout(OBTelnetCmndExecV2.DEFAULT_SOCKET_TIMEOUT);
			this.telnetClient.setTcpNoDelay(true);
			this.telnetClient.setReceiveBufferSize(5000);
			this.telnetClient.setKeepAlive(true);
		} catch (SocketException e) {
			throw new OBExceptionUnreachable(
					String.format("failed to login(%s). message:%s", this.serverName, e.getMessage()));
		}

//		this.inStream = this.telnetClient.getInputStream();
		this.inStream = new BufferedInputStream(this.telnetClient.getInputStream());
		this.outStream = new PrintStream(this.telnetClient.getOutputStream());

		try {
			OBDateTime.Sleep(500);
			if (this.password == null || this.password.isEmpty())
				throw new OBExceptionLogin("invalid id or password");

			boolean retVal = sendLoginCmnd(this.SUFFIX_LOGIN_PROMPT, this.userName, this.SUFFIX_PASSWD_ROMPT,
					this.password, this.SUFFIX_ROMPT_OK);
			if (retVal == false)
				throw new OBExceptionLogin("invalid id or password");
		} catch (OBExceptionLogin e) {
			this.disconnect();
			throw e;
		} catch (OBExceptionTimeout e) {
			this.disconnect();
			throw e;
		} catch (OBException e) {
			this.disconnect();
			throw e;
		}
	}

	public void disconnect() {
		try {
			if (this.inStream != null) {
				this.inStream.close();
			}

			if (this.telnetClient != null) {
				this.telnetClient.disconnect();
			}
		} catch (IOException e) {
		} catch (Exception e) {
		}
		this.inStream = null;
		this.outStream = null;
		this.telnetClient = null;
	}

	/**
	 * 서버측에 명령을 전달한다.
	 * 
	 * @param msg
	 * @throws OBException
	 */
	private synchronized void writeMsg(String msg) throws OBException {
		this.outStream.println(msg);
		this.outStream.flush();
	}

	public synchronized final void abortCommand() throws OBException {
		try {
			this.outStream.write(3);// 3 means Ctrl+C
			this.outStream.flush();
			clearInStream();// input stream 버퍼 초기화.
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Exception: " + e.getMessage());
		}
		return;
	}
}
