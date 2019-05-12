package kr.openbase.adcsmart.service.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionTimeout;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBParser;

public class OBSShCmndExec {
	private Session session = null;
	// private Channel channel =null;
	private String host = "";
	private String user = "";
	private String password = "";
	private JSch jsch = null;
	private java.util.Properties config = null;
	private Integer port = 22;

	// private InputStream inStream;// = channel.getInputStream();
	// private OutputStream outStream;// = channel.getOutputStream();

	private BufferedInputStream inStream = null;
	private PrintStream outStream = null;
	private Channel channel = null;

	private Integer sshMode = SSL_MODE_SHELL;

	private String command = "";
	private String retMssage = "";

	private String SUFFIX_LOGIN_PROMPT = "login: ";
	private String SUFFIX_PASSWD_ROMPT = "Password: ";
	private String SUFFIX_ROMPT_OK = "# ";
	private String SUFFIX_CONTINUE = "[0m"; // "[7mPress q to quit, any other key to continue[0m"
	private String SUFFIX_CONTINUE2 = "[K"; // "[7mPress q to quit, any other key to continue[0m"
	private String SUFFIX_MORE = "--More--"; // "More--[m, PASK 1.7에서는 "[7m--More--[m\x0D", 포함관계
	private String SUFFIX_CONFIRM = "note [y]: "; // "Confirm seeing above note [y]: "
	private String SUFFIX_CONFIRM_YN = "[y/n]: "; // "Confirm saving to FLASH [y/n]: "
	private String SUFFIX_CONFIRM_12 = "Confirm Option [1/2]: "; // "Confirm Option [1/2]: "

	final static int DEFULT_TIMEOUT_COUNT = 20; // ykk___
	final static int DEFAULT_SOCKET_TIMEOUT = 100;

	public static final int SSL_MODE_SHELL = 1;
	public static final int SSL_MODE_EXEC = 2;

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

	public OBSShCmndExec() {
		try {
			String propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME,
					OBDefine.PROPERTY_KEY_TELNET_CONNECT_TIMEOUT);
			if (propertyValue != null && !propertyValue.isEmpty())
				this.TELNET_CONNECT_TIMEOUT = Integer.parseInt(propertyValue);
		} catch (Exception e) {
		}
	}

	public void setPromptInfo(String login, String passwd, String prompt) {
		this.SUFFIX_LOGIN_PROMPT = login;
		this.SUFFIX_PASSWD_ROMPT = passwd;
		this.SUFFIX_ROMPT_OK = prompt;
	}

	public void setSSHMode(Integer mode) {
		this.sshMode = mode;
	}

	private void initializeSession() throws OBExceptionUnreachable, OBExceptionLogin {
		try {
			this.config = new java.util.Properties();
			this.config.put("StrictHostKeyChecking", "no");
			this.jsch = new JSch();
			this.session = jsch.getSession(this.user, this.host, this.port);
			this.session.setPassword(this.password);
			this.session.setConfig(this.config);
			this.session.connect(TELNET_CONNECT_TIMEOUT);// .connect();
			// return this.session.openChannel("exec");
		} catch (Exception e) {
			if (e.getMessage().contains("No route to host")) {
				throw new OBExceptionUnreachable(e.getMessage());
			}
			if (e.getMessage().contains("socket is not established")) {
				throw new OBExceptionUnreachable(e.getMessage());
			}
			// "Connection refused"
			throw new OBExceptionLogin(e.getMessage());
		}
	}

	// 특수 문자를 제거 & 라인 카운트
	private BufferInfo normalize(byte[] byteArray, int offset, int length) {
		StringBuilder sb = new StringBuilder(length);
		Integer lineCount = 0;
		BufferInfo ret = new BufferInfo();
		ret.setLineCount(0);
		for (int i = offset; i < offset + length; i++) {
			byte one = byteArray[i];
			// if((one>=32 && one<=126)|| one=='\r' || one=='\n')
			if ((one >= 32 && one <= 126) || one == '\n')// '\r'은 특수 문자로 인식한다.
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

	private synchronized final boolean waitLoginPrompt(String loginSuffix, String loginMsg, String passwdSuffix,
			String passwdMsg, String promptOK) throws OBExceptionTimeout, OBException {
		byte[] b = new byte[5000];
		int cnt = 0;
		int off = 0;
		int len = b.length;

		int iReadTimeoutCnt = 0;
		String retVal = "";
		BufferInfo buffer;
		String line;
		do {
			try {
				cnt = this.inStream.read(b, off, len);
				if (cnt < 0)
					break;
				if (cnt == 0)
					continue;
				iReadTimeoutCnt = 0;
			} catch (IOException e) {
				// System.err.println("Exception while reading socket1:" + e.getMessage()+",
				// cont:"+iReadTimeoutCnt);
				if (e.getMessage().indexOf("Read timed out") >= 0) {
					// System.err.println("Exception while reading socket1:" + e.getMessage()+",
					// cont:"+iReadTimeoutCnt);
					iReadTimeoutCnt++;
					if (iReadTimeoutCnt > OBSShCmndExec.DEFULT_TIMEOUT_COUNT) {
						this.retMssage = retVal;
						throw new OBExceptionTimeout(String.format("faild to receive data(%s). server:%s, rcvData:%s",
								e.getMessage(), this.host, retVal));
					}
				} else {
					// System.err.println("Exception while reading socket:" + e.getMessage()+",
					// cont:"+iReadTimeoutCnt);
				}
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

			if (retVal.endsWith(promptOK)) {
				this.retMssage = retVal;
				return true;// break;
			}

			if (retVal.endsWith(loginSuffix)) {
				retVal = "";
				OBDateTime.Sleep(100);
				writeShellModeMsg(loginMsg);
				OBDateTime.Sleep(100);
				continue;
			}

			if (retVal.endsWith(passwdSuffix)) {
				retVal = "";
				OBDateTime.Sleep(100);
				writeShellModeMsg(passwdMsg);
				OBDateTime.Sleep(100);
				continue;
			}

			if (retVal.endsWith(this.SUFFIX_CONTINUE)) {
				OBDateTime.Sleep(100);
				writeShellModeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (retVal.endsWith(this.SUFFIX_CONFIRM)) {
				writeShellModeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (retVal.endsWith(this.SUFFIX_CONFIRM_YN)) {
				OBDateTime.Sleep(100);
				writeShellModeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (line.endsWith(this.SUFFIX_CONFIRM_12)) {
				OBDateTime.Sleep(100);
				writeShellModeMsg("2");
				OBDateTime.Sleep(100);
				continue;
			}
			if (retVal.trim().contains(this.SUFFIX_MORE)) {
				OBDateTime.Sleep(100);
				writeShellModeMsg("  ");
				OBDateTime.Sleep(100);
				continue;
			}
		} while (cnt >= 0);
		this.retMssage = retVal;
		return false;
	}

	public String getCommandString() throws OBException {
		return this.command;
	}

	public String getCmndRetString() throws OBException {
		return this.retMssage;
	}

	public void setCommandString(String cmnd) throws OBException {
		this.command = cmnd;
	}

	public void setCmndRetString(String retMssage) throws OBException {
		this.retMssage = retMssage;
	}

	public void setConnectionInfo(String server, String user, String password, int connPort) {
		this.host = server;
		this.user = user;
		this.password = password;
		this.port = connPort;
	}

	public void setConnectionInfo(String server, String user, String password, int connPort, int mode) {
		this.host = server;
		this.user = user;
		this.password = password;
		this.port = connPort;
		this.sshMode = mode;
	}

	public void sshLogin(String server, String user, String passwd, int port)
			throws OBExceptionUnreachable, OBExceptionLogin {
		setConnectionInfo(server, user, passwd, port);
		initializeSession();
	}

	public void sshLogin() throws OBExceptionUnreachable, OBExceptionLogin {
		try {
			initializeSession();
			if (this.sshMode == SSL_MODE_SHELL) {
				procShellModeLogin();
			}
		} catch (OBExceptionLogin e) {
			throw e;
		}
	}

	private void procShellModeLogin() throws OBExceptionLogin {
		try {
			if (this.sshMode == SSL_MODE_SHELL) {
				if (this.password == null || this.password.isEmpty())
					throw new OBExceptionLogin("invalid id or password");

				this.channel = (Channel) this.session.openChannel("shell");

				this.inStream = new BufferedInputStream(this.channel.getInputStream());
				this.outStream = new PrintStream(this.channel.getOutputStream());

				this.channel.connect();

				boolean retVal = waitLoginPrompt(this.SUFFIX_LOGIN_PROMPT, this.host, this.SUFFIX_PASSWD_ROMPT,
						this.password, this.SUFFIX_ROMPT_OK);
				if (retVal == false)
					throw new OBExceptionLogin("invalid id or password");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new OBExceptionLogin("failed to login");
		}
	}

	public void sshDisconnect() {
		try {
			if (this.sshMode == SSL_MODE_SHELL) {
				if (this.inStream != null)
					this.inStream.close();
				if (this.outStream != null)
					this.outStream.close();
				if (this.channel != null)
					this.channel.disconnect();
			}

			if (null != this.session)
				this.session.disconnect();
		} catch (IOException e) {
		}
	}

	public synchronized final String sendCommand(String command) throws OBException {
		String retVal = "";
		try {
			if (this.sshMode == SSL_MODE_EXEC) {
				ChannelExec channel = (ChannelExec) this.session.openChannel("exec");
				this.command = command;
				if (channel != null) {
					channel.setCommand(command);
					channel.setInputStream(null);
					// ((ChannelExec)channel).set.setErrStream(System.err);
					InputStream in = channel.getInputStream();
					channel.connect();

					retVal = readServerOutput(channel, in);
				}

				channel.disconnect();

				this.retMssage = retVal;
				return retVal;
			} else {// shell mode
				retVal = sendShellModeCommandForLimitedResult(command, this.SUFFIX_ROMPT_OK, -1); // resultLimit = -1,
																									// 결과수 제한 없는 실행

				this.retMssage = retVal;
				return retVal;
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_COMM_READ);
		}
	}

	public synchronized final String sendCommandForLimitedResult(String command, String suffix, int resultLimit)
			throws OBException { // 결과수를 제한하는 ssh 명령 실행, 결과수를 제한하는 방식은 쉘모드만 콜한다.
		String retVal = "";
		try {
			retVal = sendShellModeCommandForLimitedResult(command, suffix, resultLimit);

			this.retMssage = retVal;
			return retVal;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_COMM_READ);
		}
	}

	public synchronized final String sendCommand(String command, String suffix) throws OBException {
		String retVal = "";
		try {
			if (this.sshMode == SSL_MODE_EXEC) {
				ChannelExec channel = (ChannelExec) this.session.openChannel("exec");
				this.command = command;
				if (channel != null) {
					channel.setCommand(command);
					channel.setInputStream(null);
					// ((ChannelExec)channel).set.setErrStream(System.err);
					InputStream in = channel.getInputStream();
					channel.connect();

					retVal = readServerOutput(channel, in);
					// in.close();
				}

				channel.disconnect();

				this.retMssage = retVal;
				return retVal;
			} else {// shell mode
				retVal = sendShellModeCommandForLimitedResult(command, suffix, -1); // resultLimit = -1, 결과수 제한 없는 실행

				this.retMssage = retVal;
				return retVal;
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_COMM_READ);
		}
	}

	// Alteon에서 exit은 save하지 않은 게 있을 경우 종료할지 질문이 나온다. 안 나올 수도 있다.
	// 1) save된 상태라면 즉시 종료해야 한다. suffix 대기 구조를 쓰면 안된다.
	// 2) save없이 종료를 묻는 prompt가 나올 수 있으므로 대기도 필요하다.
	// 위 두 상황이 상충되지만 즉시 종료는 필요하기 때문에 suffix 없이 즉시 종료하고, 중간에 prompt를 대비해 'y'를 보낸다.
	// 필요없을때 보내도 부작용은 없다.
	public synchronized final String sendCommandExit() throws OBException {
		String cmnd = "exit";
		this.command = cmnd;
		this.retMssage = "";
		String retVal = "";
		if (this.sshMode == SSL_MODE_EXEC) {
			ChannelExec channel;
			try {
				channel = (ChannelExec) this.session.openChannel("exec");
				if (channel != null) {
					channel.setCommand(command);
					channel.setInputStream(null);
					// ((ChannelExec)channel).set.setErrStream(System.err);
					InputStream in = channel.getInputStream();
					channel.connect();

					retVal = readServerOutput(channel, in);
					// in.close();
				}

				channel.disconnect();

				this.retMssage = retVal;
				return null;
			} catch (Exception e) {
				throw new OBException(OBException.ERRCODE_SYSTEM_COMM_READ);
			}
		} else {
			writeShellModeMsg(cmnd);
			OBDateTime.Sleep(500);
			writeShellModeMsg("y"); // save confirm이 있으면 y로 응답
			return null;
		}
	}

	public synchronized final String sendCommandOnce(String command, String suffix) throws OBException {
		String retVal = "";
		try {
			if (this.sshMode == SSL_MODE_EXEC) {
				ChannelExec channel = (ChannelExec) this.session.openChannel("exec");
				this.command = command;
				if (channel != null) {
					channel.setCommand(command);
					channel.setInputStream(null);
					// ((ChannelExec)channel).set.setErrStream(System.err);
					InputStream in = channel.getInputStream();
					channel.connect();

					retVal = readServerOutput(channel, in);
					// in.close();
				}

				channel.disconnect();

				this.retMssage = retVal;
				return retVal;
			} else {// shell mode
				retVal = sendShellModeCommandOnce(command, suffix);

				this.retMssage = retVal;
				return retVal;
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_COMM_READ);
		}
	}

	public synchronized final String sendCommandTimeout(String command, String suffix, int timeOut) throws OBException {
		String retVal = "";
		try {
			if (this.sshMode == SSL_MODE_EXEC) {
				ChannelExec channel = (ChannelExec) this.session.openChannel("exec");
				this.command = command;
				if (channel != null) {
					channel.setCommand(command);
					channel.setInputStream(null);
					InputStream in = channel.getInputStream();
					channel.connect();

					retVal = readServerOutput(channel, in);
				}

				channel.disconnect();

				this.retMssage = retVal;
				return retVal;
			} else {// shell mode
				retVal = sendShellModeCommandTimeout(command, suffix, timeOut);

				this.retMssage = retVal;
				return retVal;
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_COMM_READ);
		}
	}

	private synchronized final void clearInStream() {
		byte[] contents = new byte[1024];

		try {
			OBDateTime.Sleep(100);
			while (this.inStream.available() > 0) {
				this.inStream.read(contents);
			}
		} catch (IOException e) {
		}
	}

	private synchronized final String sendShellModeCommandForLimitedResult(String cmnd, String suffix, int resultLimit)
			throws OBException {
		byte[] b = new byte[128];
		int cnt = 0;
		int off = 0;
		// int len = b.length;

		if (cmnd != null) {
			clearInStream();// input stream 버퍼 초기화.
			writeShellModeMsg(cmnd);
			this.command = cmnd;
			this.retMssage = "";
			OBDateTime.Sleep(100);
		}

		int iReadTimeoutCnt = 0;
		String retVal = "";
		BufferInfo buffer;
		String line;
		int lineCount = 0;
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
				// System.err.println("Exception while reading socket1:" + e.getMessage()+",
				// cont:"+iReadTimeoutCnt+", cmnd:"+cmnd);
				if (e.getMessage().indexOf("Read timed out") >= 0) {
					iReadTimeoutCnt++;
					if (iReadTimeoutCnt > OBSShCmndExec.DEFULT_TIMEOUT_COUNT) {
						this.retMssage = retVal;
						throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
								String.format("failed to recv data(%s), server:%s", e.getMessage(), this.host));
					}
				}
				continue;
			}

			buffer = normalize(b, off, cnt);
			line = buffer.getData();

			if (line.isEmpty()) {
				continue;
			}

			retVal += line;
			lineCount += buffer.getLineCount(); // 라인수 누적계산

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
				writeShellModeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (retVal.endsWith(this.SUFFIX_CONFIRM)) {
				writeShellModeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (retVal.endsWith(this.SUFFIX_CONFIRM_YN)) {
				writeShellModeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (line.endsWith(this.SUFFIX_CONFIRM_12)) {
				writeShellModeMsg("2");
				OBDateTime.Sleep(100);
				continue;
			}
			if (retVal.contains(this.SUFFIX_MORE)) {
				writeShellModeMsg("  ");
				OBDateTime.Sleep(100);
				continue;
			}
		} while (cnt >= 0);
		this.retMssage = retVal;
		return null;
	}

	public synchronized final String sendShellModeCommandOnce(String cmnd, String suffix) throws OBException {
		byte[] b = new byte[5000];
		int cnt = 0;
		int off = 0;
		// int len = b.length;

		if (cmnd != null) {
			clearInStream();// input stream 버퍼 초기화.
			writeShellModeMsg(cmnd);
			this.command = cmnd;
			this.retMssage = "";
			OBDateTime.Sleep(100);
		}

		int iReadTimeoutCnt = 0;
		String retVal = "";
		BufferInfo buffer;
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
				// System.err.println("Exception while reading socket1:" + e.getMessage()+",
				// cont:"+iReadTimeoutCnt+", cmnd:"+cmnd);
				if (e.getMessage().indexOf("Read timed out") >= 0) {
					iReadTimeoutCnt++;
					if (iReadTimeoutCnt > OBSShCmndExec.DEFULT_TIMEOUT_COUNT) {
						this.retMssage = retVal;
						throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
								String.format("failed to recv data(%s), server:%s", e.getMessage(), this.host));
					}
				}
				continue;
			}
			// String line22 = new String(b, 0, cnt);
			// System.err.println(line22);
			// String line = new String(b);
			buffer = normalize(b, off, cnt);
			line = buffer.getData();
			cnt = 0;
			off = 0;
			// len = b.length;

			if (line.isEmpty()) {
				continue;
			}

			retVal += line;
			if (retVal.endsWith(suffix)) {
				this.retMssage = retVal;
				return retVal;// break;
			}

			if (retVal.endsWith(this.SUFFIX_CONTINUE)) {
				writeShellModeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (retVal.endsWith(this.SUFFIX_CONFIRM)) {
				writeShellModeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (retVal.endsWith(this.SUFFIX_CONFIRM_YN)) {
				writeShellModeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (line.endsWith(this.SUFFIX_CONFIRM_12)) {
				writeShellModeMsg("2");
				OBDateTime.Sleep(100);
				continue;
			}
			if (retVal.contains(this.SUFFIX_MORE)) {
				writeShellModeMsg("q ");
				OBDateTime.Sleep(100);
				continue;
			}
			if (retVal.endsWith(this.SUFFIX_CONTINUE2)) {
				writeShellModeMsg("q ");
				OBDateTime.Sleep(100);
				continue;
			}
		} while (cnt >= 0);
		this.retMssage = retVal;
		return null;
	}

	// timeout: second 단위.
	private synchronized final String sendShellModeCommandTimeout(String cmnd, String suffix, int timeOut)
			throws OBException {
		byte[] b = new byte[5000];
		int cnt = 0;
		int off = 0;
		int len = b.length;

		if (cmnd != null) {
			clearInStream();// input stream 버퍼 초기화.
			this.command = cmnd;
			this.retMssage = "";
			writeShellModeMsg(cmnd);
			OBDateTime.Sleep(100);
		}

		int iReadTimeoutCnt = 0;
		String retVal = "";
		BufferInfo buffer;
		String line;
		do {
			try {
				while (this.inStream.available() > 0) {
					cnt = this.inStream.read(b, off, len);
					break;
				}

//                cnt = this.inStream.read(b, off, len);
				if (cnt < 0)
					break;
				if (cnt == 0) {
					iReadTimeoutCnt++;
					OBDateTime.Sleep(100);
					if (iReadTimeoutCnt > timeOut) {
						this.retMssage = retVal;
						abortCommand();// Ctrl+C 명령 전송함.
						OBDateTime.Sleep(100);
						return retVal;
//                        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to recv data(%s), server:%s", "timeout", this.host));
					}
					continue;
				}

				iReadTimeoutCnt = 0;
			} catch (IOException e) {
				if (e.getMessage().indexOf("Read timed out") >= 0) {
					iReadTimeoutCnt++;
					if (iReadTimeoutCnt > timeOut) {
						this.retMssage = retVal;
						throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
								String.format("failed to recv data(%s), server:%s", e.getMessage(), this.host));
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

			if (retVal.endsWith(suffix)) {
				this.retMssage = retVal;
				return retVal;// break;
			}

			if (retVal.endsWith(this.SUFFIX_CONTINUE)) {
				writeShellModeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (retVal.endsWith(this.SUFFIX_CONFIRM)) {
				writeShellModeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (retVal.endsWith(this.SUFFIX_CONFIRM_YN)) {
				writeShellModeMsg("y");
				OBDateTime.Sleep(100);
				continue;
			}
			if (retVal.endsWith(this.SUFFIX_CONFIRM_12)) {
				writeShellModeMsg("2");
				OBDateTime.Sleep(100);
				continue;
			}

			if (retVal.contains(this.SUFFIX_MORE)) {
				writeShellModeMsg("  ");
				OBDateTime.Sleep(100);
				continue;
			}
		} while (cnt >= 0);
		this.retMssage = retVal;
		return null;
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

	/**
	 * 서버측에 명령을 전달한다.
	 * 
	 * @param msg
	 * @throws OBException
	 */
	private synchronized void writeShellModeMsg(String msg) throws OBException {
		this.outStream.println(msg);
		this.outStream.flush();
	}

	public boolean executeScpFromCommand(String remoteFileName, String localFileName) throws OBException {
		FileOutputStream fos = null;
		// if(this.channel == null)
		// {
		// throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER[1]);
		// }

		try {
			String prefix = null;
			if (new File(localFileName).isDirectory()) {
				prefix = localFileName + File.separator;
			}

			ChannelExec channel = (ChannelExec) this.session.openChannel("exec");

			String cmnd = "scp -f " + remoteFileName;
			this.command = cmnd;
			channel.setCommand(cmnd);
			channel.setInputStream(null);
			// ((ChannelExec)channel).set.setErrStream(System.err);
			InputStream in = channel.getInputStream();
			OutputStream out = channel.getOutputStream();
			channel.connect();

			byte[] buf = new byte[1024];

			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();

			// 파일 크기를 읽어 들인다.
			int c = checkAck(in);
			if (c != 'C') {
				in.close();
				out.close();
				throw new OBException(OBException.ERRCODE_SYSTEM_COMM_ACK);
			}

			// read '0644 '
			in.read(buf, 0, 5);

			long filesize = 0L;
			while (true) {
				if (in.read(buf, 0, 1) < 0) {
					in.close();
					out.close();
					channel.disconnect();
					throw new OBException(OBException.ERRCODE_SYSTEM_COMM_READ);
				}
				if (buf[0] == ' ')
					break;
				filesize = filesize * 10L + (long) (buf[0] - '0');
			}

			String file = null;
			for (int i = 0;; i++) {
				in.read(buf, i, 1);
				if (buf[i] == (byte) 0x0a) {
					file = new String(buf, 0, i);
					break;
				}
			}

			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();

			// read a content of lfile
			try {
				fos = new FileOutputStream(prefix == null ? localFileName : prefix + file);
			} catch (FileNotFoundException e) {
				throw new OBException(OBException.ERRCODE_SYSTEM_FILENOTFOUND);
			}
			int foo;
			while (true) {
				if (buf.length < filesize)
					foo = buf.length;
				else
					foo = (int) filesize;
				foo = in.read(buf, 0, foo);
				if (foo < 0) {
					in.close();
					out.close();
					channel.disconnect();
					throw new OBException(OBException.ERRCODE_SYSTEM_COMM_READ);
				}
				fos.write(buf, 0, foo);
				filesize -= foo;
				if (filesize == 0L)
					break;
			}
			fos.close();
			fos = null;
			if (checkAck(in) != 0) {
				in.close();
				out.close();
				channel.disconnect();
				throw new OBException(OBException.ERRCODE_SYSTEM_COMM_ACK);
			}

			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();
			in.close();
			out.close();

			channel.disconnect();
			return true;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_COMM_READ);
		}
	}

	private String readServerOutput(Channel channel, InputStream in) {
		StringBuffer sb = new StringBuffer();
		byte[] tmp = new byte[1024];
		try {
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0) {
						break;
					}
					sb.append(new String(tmp, 0, i));
					// System.out.print(new String(tmp, 0, i));
				}
				if (channel.isClosed()) {
					break;
				}
				OBDateTime.Sleep(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// throw new OBException(OBException.ERRCODE_SYSTEM_COMM_READ[1]);
		}
		return sb.toString();
	}

	private int checkAck(InputStream in) throws OBException {
		try {
			int b = in.read();
			// b may be 0 for success,
			// 1 for error,
			// 2 for fatal error,
			// -1
			if (b == 0)
				return b;
			if (b == -1)
				return b;

			if (b == 1 || b == 2) {
				StringBuffer sb = new StringBuffer();
				int c;
				do {
					c = in.read();
					sb.append((char) c);
				} while (c != '\n');

				if (b == 1) { // error
					if (sb.indexOf("No such file or directory") >= 0)
						throw new OBException(OBException.ERRCODE_SYSTEM_FILENOTFOUND);
					else
						throw new OBException(sb.toString());
				}
				if (b == 2) { // fatal error
					throw new OBException(sb.toString());
					// System.out.print(sb.toString());
				}
			}
			return b;
		} catch (IOException e) {
			// if(e.getMessage().indexOf("Read timed out")>=0)
			{
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to recv data(%s), server:%s", e.getMessage(), this.host));
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	// private String readServerOutput(Channel channel, InputStream in) throws
	// IOException, JSchException, OBException
	// {
	// byte[] buf=new byte[1024];
	// channel.connect();
	// OutputStream out=channel.getOutputStream();
	// // send '\0'
	// buf[0]=0; out.write(buf, 0, 1); out.flush();
	// StringBuffer sb = new StringBuffer();
	//
	// // while(true)
	// // {
	// // file length 추출.
	// int c=checkAck(in);
	// if(c!='C')
	// {
	// // break;
	// }
	//
	// // read '0644 '
	// in.read(buf, 0, 5);
	//
	// long filesize=0L;
	// while(true)
	// {
	// if(in.read(buf, 0, 1)<0)
	// {
	// // error
	// break;
	// }
	// if(buf[0]==' ')break;
	// filesize=filesize*10L+(long)(buf[0]-'0');
	// }
	//
	// String file=null;
	// for(int i=0;;i++)
	// {
	// in.read(buf, i, 1);
	// if(buf[i]==(byte)0x0a)
	// {
	// file=new String(buf, 0, i);
	// break;
	// }
	// }
	// sb.append(file);
	// // }
	// return sb.toString();
	// //System.out.println("filesize="+filesize+", file="+file);
	// //
	// // // send '\0'
	// // buf[0]=0; out.write(buf, 0, 1); out.flush();
	// //
	// // // read a content of lfile
	// // fos=new FileOutputStream(prefix==null ? lfile : prefix+file);
	// // int foo;
	// // while(true){
	// // if(buf.length<filesize) foo=buf.length;
	// // else foo=(int)filesize;
	// // foo=in.read(buf, 0, foo);
	// // if(foo<0){
	// // // error
	// // break;
	// // }
	// // fos.write(buf, 0, foo);
	// // filesize-=foo;
	// // if(filesize==0L) break;
	// // }
	// // fos.close();
	// // fos=null;
	// //
	// // if(checkAck(in)!=0){
	// // System.exit(0);
	// // }
	// //
	// // // send '\0'
	// // buf[0]=0; out.write(buf, 0, 1); out.flush();
	// // }
	// }
	//
	//
	//
	// StringBuffer sb = new StringBuffer();
	// byte[] tmp=new byte[1024];
	// try
	// {
	// while(true)
	// {
	// while(in.available()>0)
	// {
	// int i=in.read(tmp, 0, 1024);
	// if(i<0)
	// {
	// break;
	// }
	// sb.append(new String(tmp, 0, i));
	// // System.out.print(new String(tmp, 0, i));
	// }
	// if(channel.isClosed())
	// {
	// // System.out.println("exit-status: "+channel.getExitStatus());
	// break;
	// }
	// OBDateTime.Sleep(1000);
	// }
	// }
	// catch(Exception e)
	// {
	// // throw new OBException(OBException.ERRCODE_SYSTEM_COMM_READ[1]);
	// }
	// return sb.toString();
	// }

//    public static void main(String args[])
//    {
//        OBSShCmndExec sce = new OBSShCmndExec();
//        // sce.setConnectionInfo("61.82.88.79", "admin", "netoptics", OBSShCmndExec.SSL_MODE_EXEC);
//        // sce.setConnectionInfo("61.82.88.79", "admin", "netoptics", OBSShCmndExec.SSL_MODE_SHELL);
//        sce.setConnectionInfo("192.168.200.12", "admin", "openbase", 1);
//        try
//        {
//            for(int i = 0; i < 10; i++)
//            {
//                sce.sshLogin();
//                String retVal;
//                retVal = sce.sendCommand("?");
//                System.out.println(retVal);
//                retVal = sce.sendCommand("/info/slb/dump");
//                System.out.println(retVal);
//                retVal = sce.sendCommand("?");
//                System.out.println(retVal);
//                sce.sshDisconnect();
//            }
//            // retVal = sce.executeCommand("ps -A");
//            // System.out.println(retVal);
//            // retVal = sce.executeCommand("ps -A");
//            // System.out.println(retVal);
//        }
//        catch(OBException e)
//        {
//            e.printStackTrace();
//        }
//        catch(OBExceptionUnreachable e)
//        {
//            e.printStackTrace();
//        }
//        catch(OBExceptionLogin e)
//        {
//            e.printStackTrace();
//        }
//        sce.sshDisconnect();
//        return;
//    }
}
