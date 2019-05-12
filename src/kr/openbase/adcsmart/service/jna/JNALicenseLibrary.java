package kr.openbase.adcsmart.service.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import kr.openbase.adcsmart.service.jna.dto.STLicenseInfo;

public interface JNALicenseLibrary extends Library {
	public static final String JNA_LIBRARY_NAME = "libOBLicense.so";
	public static final NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance(JNALicenseLibrary.JNA_LIBRARY_NAME);
	public static final JNALicenseLibrary INSTANCE = (JNALicenseLibrary) Native
			.loadLibrary(JNALicenseLibrary.JNA_LIBRARY_NAME, JNALicenseLibrary.class);

	/**
	 * �씪�씠�씠�꽱�뒪 �젙蹂대�� 異붿텧�븳�떎.
	 * 
	 * @param pszFileName
	 * @param stInfo
	 * @return
	 */
	int getLicenseInfo(String pszFileName, STLicenseInfo stInfo);

	/**
	 * �뿉�윭 肄붾뱶�뿉 ���븳 臾몄옄�뿴�쓣 異붿텧�븳�떎.
	 * 
	 * @param iCode
	 * @return
	 */
	String getErrString(int iCode);

	/**
	 * �씪�씠�꽱�뒪 �룷硫㏃쓣 �솗�씤�븳�떎.
	 * 
	 * @param byteBuffer
	 * @return
	 */
	int isValidLicenseFormat(java.nio.ByteBuffer byteBuffer);;
}
