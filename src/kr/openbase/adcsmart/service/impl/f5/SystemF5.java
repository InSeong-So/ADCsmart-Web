package kr.openbase.adcsmart.service.impl.f5;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.rpc.holders.LongHolder;

import iControl.CommonEnabledState;
import iControl.ManagementSNMPConfigurationClientAccess;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcSystemInfo;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class SystemF5 {

	private iControl.Interfaces _interfaces = null;

	public SystemF5(iControl.Interfaces interfaces) {
		_interfaces = interfaces;
	}

	private void validateBaseValues() throws Exception {
		if (_interfaces == null) {
			throw new Exception("SystemF5: F5 Interfaces error(null)");
		}
	}

	private static void validateBaseValues(iControl.Interfaces interfaces) throws Exception {
		if (interfaces == null) {
			throw new Exception("SystemF5: F5 interface error(null)");
		}
	}

	static void saveHighConfig(iControl.Interfaces interfaces) throws Exception {
		validateBaseValues(interfaces);

		interfaces.getSystemConfigSync().save_configuration("",
				iControl.SystemConfigSyncSaveMode.SAVE_HIGH_LEVEL_CONFIG);
	}

	static void synchronizeFullConfig(iControl.Interfaces interfaces) throws Exception {
		validateBaseValues(interfaces);

		interfaces.getSystemConfigSync().synchronize_configuration(iControl.SystemConfigSyncSyncMode.CONFIGSYNC_ALL);
	}

	private void makeConfigUCSBackup(String ucsFileName) throws Exception // filename 확장자 .ucs로 할 것
	{
		validateBaseValues();
		_interfaces.getSystemConfigSync().save_configuration(ucsFileName,
				iControl.SystemConfigSyncSaveMode.SAVE_FULL);/**
																 * 참고 - SAVE_FULL: Saves a complete configuration that
																 * can be used to set up a device from scratch. This
																 * mode is used to save a configuration that can be used
																 * in a configsync process. The filename specified when
																 * used with this mode should NOT have any path
																 * information, since the file will be restricted to a
																 * specific directory used in configsync. If the
																 * specified file does not end with the ".ucs" suffix, a
																 * ".ucs" will be automatically appended to the file.
																 */
	}

	private long downloadFile(String orgFile, String downloadFile) throws Exception {
		File file = null;
		iControl.SystemConfigSyncFileTransferContext context = new iControl.SystemConfigSyncFileTransferContext();

		long chunk_size = 1024 * 1024 * 10L; // 10MB chunk
		javax.xml.rpc.holders.LongHolder offset = new LongHolder(0);
		FileOutputStream fos = null;
//
//		try {
//			fos = new FileOutputStream(downloadFile);
//		} catch (FileNotFoundException e) {
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("FileNotFoundException : %s", e.getMessage()));
//			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
//					String.format("FileNotFoundException : %s", e.getMessage()));
//		} catch (SecurityException e) {
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("SecurityException : %s", e.getMessage()));
//			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
//					String.format("SecurityException : %s", e.getMessage()));
//		}

		try {
			fos = new FileOutputStream(downloadFile);
			int i = 0;
			while (context.getChain_type() != iControl.CommonFileChainType.FILE_FIRST_AND_LAST
					&& context.getChain_type() != iControl.CommonFileChainType.FILE_LAST) {
				try {
					context = _interfaces.getSystemConfigSync().download_file(orgFile, chunk_size, offset);
//				} catch (RemoteException e) {
//					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
//							String.format("data download RemoteException : %s", e.getMessage()));
//					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
//							String.format("data download Exception : %s", e.getMessage()));
				} catch (Exception e) {
					fos.close();
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("data download Exception : %s", e.getMessage()));
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
							String.format("data download Exception : %s", e.getMessage()));
				}

				fos.write(context.getFile_data());
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
						"File download chunk(" + i++ + "): " + context.getFile_data().length);
			}

//			try {
//				fos.flush();
//				fos.close();
//			} catch (IOException e) {
//				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
//						String.format("file flush/close IOException : %s", e.getMessage()));
//				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
//						String.format("file flush/close IOException : %s", e.getMessage()));
//			}

			file = new File(downloadFile);
			if (file.exists() == true) {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "File Download from F5 success: " + orgFile + "->"
						+ downloadFile + ", size = " + file.length());
				return file.length();
			} else // download error
			{
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
						"File Download from F5 fail: " + orgFile + "->" + downloadFile);
				return -1L;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				fos.flush();
				fos.close();
			} catch (IOException e) {

			}
		}
	}

	private void deleteFile(String filename) throws Exception {
		validateBaseValues();
		_interfaces.getSystemConfigSync().delete_file(filename);
	}

	public long backupConfig(String filename) throws OBException {
		String tempUCSFilename = "temp.ucs";
		String tempUCSFilenameFullPath = "/var/local/ucs/" + tempUCSFilename;
		String localFilename = filename.concat(".ucs");
		long fileSize;

		try {
			makeConfigUCSBackup(tempUCSFilename);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("Config backup error: %s", e.getMessage()));
		}

		try {
			fileSize = downloadFile(tempUCSFilenameFullPath, localFilename); // file download 때는 full path를 준다.
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("Config download error:%s", e.getMessage()));
		}

		if (fileSize < 0) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Config download error: file save error.");
		} else if (fileSize == 0) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					"Config download error: file size error. Nothing saved in file.");
		}

		try {
			deleteFile(tempUCSFilenameFullPath); // F5의 ucs file을 지운다. 테스트 할 때는 이 부분을 막고 다운로드 한 파일과 비교한다.
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("Config tempfile clear error:%s", e.getMessage()));
		}

		return fileSize;
	}

	// from here, HA(High Availability) related Functions
	static String[] getPeerAddress(iControl.Interfaces interfaces) throws Exception {
		String[] ret = {};
		validateBaseValues(interfaces);
		ret = interfaces.getSystemFailover().get_peer_address();
		// _interfaces.getSystemConfigSync().delete_file(filename);

		return ret;
	}

//	// 안 쓰는 함수, 우선 함수 만들어 놓음. 쓸 때 Integer를 return하게 바꿀 것
//	private static iControl.SystemFailoverFailoverState getFailOverState(iControl.Interfaces interfaces)
//			throws Exception {
//		validateBaseValues(interfaces);
//		iControl.SystemFailoverFailoverState ret = interfaces.getSystemFailover().get_failover_state();
//
//		// return values:FAILOVER_STATE_STANDBY=0, FAILOVER_STATE_ACTIVE=1,
//		// FAILOVER_STATE_FORCED_OFFLINE=2,
//		// FAILOVER_STATE_OFFLINE=3, FAILOVER_STATE_UNKNOWN=4
//		return ret;
//	}
	// end, HA(High Availability) related Functions

	// -------------------------------------------------
	/*
	 * public static OBDtoAdcSystemInfo summary(Interfaces interfaces) throws
	 * Exception { OBDtoAdcSystemInfo sysInfo = new OBDtoAdcSystemInfo();
	 * 
	 * SystemSystemInfoPortType _SystemInfo = interfaces.getSystemSystemInfo(); }
	 */
	static OBDtoAdcSystemInfo getSummary(iControl.Interfaces interfaces) throws Exception {
		OBDtoAdcSystemInfo info = new OBDtoAdcSystemInfo();
		validateBaseValues(interfaces);

//		int i;
//		iControl.SystemSystemInfoPortType _SystemInfo = interfaces.getSystemSystemInfo();
//		iControl.SystemHardwareInformation [] cpu = _SystemInfo.get_hardware_information();
//		iControl.SystemVersionInformation [] versions = cpu[0].getVersions();
//		iControl.SystemProductInformation product = _SystemInfo.get_product_information();
//		iControl.SystemSystemInformation system = _SystemInfo.get_system_information();

//		for(i=0; i<versions.length; i++)
//		{
//			if(versions[i].getName().equals("cpu MHz"))
//			{
//				info.setCpuSize((int)(Double.parseDouble(versions[i].getValue())*1000.0));
//				break;
//			}
//		}
//		try
//		{
//			info.setModel(_SystemInfo.get_marketing_name());
//		}
//		catch(Exception e)
//		{
//			info.setModel("-");
//		}
//		info.setModel(_SystemInfo.get_marketing_name());
//		info.setSwVersion(product.getProduct_version());
		info.setLastSaveTime(ManagementDBVariable.getLocalConfigTime(interfaces)); // last save time
		info.setLastApplyTime(info.getLastSaveTime());
//		info.setHostName(system.getHost_name());	//hostname

		return info;
	}

	public static String checkInterface(iControl.Interfaces interfaces) throws Exception {
		validateBaseValues(interfaces);

		return interfaces.getSystemSystemInfo().get_version();
	}

	public static String getProductVersion(iControl.Interfaces interfaces) throws Exception {
		validateBaseValues(interfaces);

		return interfaces.getSystemSystemInfo().get_product_information().getProduct_version();
	}

	public static ArrayList<String> getSnmpReadCommunity(iControl.Interfaces interfaces) throws Exception {
		validateBaseValues(interfaces);
		iControl.ManagementSNMPConfigurationWrapperSecurityInformation[] readComm = interfaces
				.getManagementSNMPConfiguration().get_readonly_community();
		iControl.ManagementSNMPConfigurationWrapperSecurityInformation[] readWriteComm = interfaces
				.getManagementSNMPConfiguration().get_readwrite_community();
		ArrayList<String> retVal = new ArrayList<String>();
		for (int i = 0; i < readComm.length; i++) {
			retVal.add(readComm[i].getCommunity());
		}
		for (int i = 0; i < readWriteComm.length; i++) {
			retVal.add(readWriteComm[i].getCommunity());
		}
		return retVal;
	}

	public static ArrayList<String> getSnmpAllowList(iControl.Interfaces interfaces) throws Exception {
		validateBaseValues(interfaces);
		ManagementSNMPConfigurationClientAccess[] allowedList = interfaces.getManagementSNMPConfiguration()
				.get_client_access();
		ArrayList<String> AllowList = new ArrayList<String>();

		// loop 처리 변수
		String tempClient = null;
		int i = 0;

		for (i = 0; i < allowedList.length; i++) {
			tempClient = null;
			if (allowedList[i].getAddress() != null && allowedList[i].getAddress().isEmpty() == false) // 주소가 있으면 주소문자열을
																										// 만든다. 없으면 에러다.
			{
				tempClient = allowedList[i].getAddress();
				if (allowedList[i].getNetmask() != null && allowedList[i].getNetmask().isEmpty() == false) {
					tempClient += ("/" + allowedList[i].getNetmask());
				}
				if (tempClient != null) {
					AllowList.add(tempClient);
				}
			}
		}
		return AllowList;
	}

	public static void setSnmpAllowList(iControl.Interfaces interfaces, String adcsmartIP) throws Exception {
		ManagementSNMPConfigurationClientAccess[] client_access_info = null;
		interfaces.getManagementSNMPConfiguration().set_client_access(client_access_info);
	}

	public static void setSnmpState(iControl.Interfaces interfaces) throws Exception {
		CommonEnabledState STATE_ENABLED = null;
		interfaces.getManagementSNMPConfiguration().set_agent_trap_state(STATE_ENABLED);
	}

//	public static void main(String[] args) {
//		iControl.Interfaces interfaces = new iControl.Interfaces();
//		interfaces.initialize("172.172.1.152", "admin", "admin");
//		// interfaces.initialize("192.168.200.14", "admin", "admin");
//
////		sync(이중화) test ---------------------------
////		try
////		{
////			//SystemF5.synchronizeBasicConfig(interfaces);
////			SystemF5.synchronizeFullConfig(interfaces);
////		}
////		catch(Exception e)
////		{
////			//CommonF5.Exception(null, e.getMessage());
////			e.printStackTrace();
////		}
//
////		try
////		{
////			System.out.println("SNMP allowlist = " + SystemF5.getSnmpAllowList(interfaces));
////		}
////		catch(Exception e1)
////		{
////			e1.printStackTrace();
////		}
//
////		test - get peer address ---------------------
////		try
////		{
////			int i, n;
////			SystemF5.getSummary(interfaces);
////			String [] peerList = SystemF5.getPeerAddress(interfaces);
////			for(i=0,n=peerList.length; i<n; i++)
////			{
////				System.out.println(String.format("Peer[%d] = %s", i, peerList[i]));
////			}
////		}
////		catch(Exception e)
////		{
////			e.printStackTrace();
////		}
//
//		// test - print detail
//		try {
//			// config 테스트 백업
//			// SystemF5 systemf5 = new SystemF5(interfaces);
//			// systemf5.BackupConfig("x:\\work\\ykkim.f5");
//
//			// 아래는 테스트 전용 call. 실제 쓰이는 코드는 아니지만, 각종 기능을 추가할 때 값을 추출해 보는 테스터로 꼭 필요하다.
//			SystemF5.printSystemInfo(interfaces);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

//	private static void printSystemInfo(iControl.Interfaces interfaces) throws Exception {
//		int i, n;
//		validateBaseValues(interfaces);
//
//		iControl.SystemSystemInfoPortType _SystemInfo = interfaces.getSystemSystemInfo();
//		iControl.SystemHardwareInformation[] cpu = _SystemInfo.get_hardware_information(); // v10.1.0 부터 지원
//		iControl.SystemDiskUsage[] disk = _SystemInfo.get_disk_usage_information().getUsages();
//		iControl.SystemProductInformation product = _SystemInfo.get_product_information();
//
//		String cpuName = cpu[0].getName();
//		String cpuType = cpu[0].getType().getValue();
//		long cpuSlot = cpu[0].getSlot();
//		String cpuModel = cpu[0].getModel();
//		iControl.SystemVersionInformation[] versions = cpu[0].getVersions();
//
//		String cpuCacheSize = "";
//		String cpuCores = "";
//		int cpuKHz = -1;
//
//		for (i = 0; i < versions.length; i++) {
//			if (versions[i].getName().equals("cache size")) {
//				cpuCacheSize = versions[i].getValue();
//			} else if (versions[i].getName().equals("cores")) {
//				cpuCores = versions[i].getValue();
//			} else if (versions[i].getName().equals("cpu MHz")) {
//				cpuKHz = (int) (Double.parseDouble(versions[i].getValue()) * 1000.0);
//			}
//		}
//
//		long diskTotal = 0L;
//		long diskFree = 0L;
//
//		for (i = 0, n = disk.length; i < n; i++) {
//			diskTotal += CommonF5.buildLong(disk[i].getBlock_size()) * CommonF5.buildLong(disk[i].getTotal_blocks());
//			diskFree += CommonF5.buildLong(disk[i].getBlock_size()) * CommonF5.buildLong(disk[i].getFree_blocks());
//		}
//
//		System.out.println("cpu name                = " + cpuName);
//		System.out.println("cpu type                = " + cpuType);
//		System.out.println("cpu slot                = " + cpuSlot);
//		System.out.println("cpu model               = " + cpuModel);
//		System.out.println("cpu cache size          = " + cpuCacheSize);
//		System.out.println("cpu cores               = " + cpuCores);
//		System.out.println("cpu KHz                 = " + cpuKHz);
//
//		System.out.println("total_blocks            = " + diskTotal);
//		System.out.println("free_blocks             = " + diskFree);
//		System.out.println("guid                    = " + _SystemInfo.get_globally_unique_identifier());
//		System.out.println("marketing name          = " + _SystemInfo.get_marketing_name()); // model
//		System.out.println("memory                  = " + _SystemInfo.get_memory_usage_information()); // MemoryUsageInformation
//		System.out.println("product code            = " + product.getProduct_code());
//		System.out.println("product version         = " + SystemF5.getProductVersion(interfaces));
//		System.out.println("product package version = " + product.getPackage_version());
//		System.out.println("product package edition = " + product.getPackage_edition());
//
//		String[] peers = interfaces.getSystemFailover().get_peer_address();
//		for (i = 0, n = peers.length; i < n; i++) {
//			System.out.println(String.format("peer[%d]                 = %s", i, peers[i]));
//		}
//		if (interfaces.getSystemFailover().is_redundant() == true) {
//			System.out.println("is_redundant            = true");
//		} else {
//			System.out.println("is_redundant            = false");
//		}
//		System.out.println("failover status         = " + SystemF5.getFailOverState(interfaces));
//		// String [] product_features = product.getProduct_features();
//		// for(i=0; i<product_features.length; i++)
//		// {
//		// System.out.println("product features = " + product_features[i]);
//		// }
//	}
}