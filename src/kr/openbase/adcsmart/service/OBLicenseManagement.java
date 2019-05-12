package kr.openbase.adcsmart.service;

import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoLicenseInfo;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBFileNotFoundException;
import kr.openbase.adcsmart.service.utility.OBLicenseInvalidFormatException;

public interface OBLicenseManagement
{
	/**
	 * 라이센스 정보를 추출한다.
	 * @return OBDtoLicenseInfo
	 * @throws OBFileNotFoundException
	 * @throws OBLicenseInvalidFormatException
	 * @throws OBException
	 */
	public OBDtoLicenseInfo getLicenseGeneralInfo() throws OBException;
	
	/**
	 * 라이센스 포멧 형식을 검사한다.
	 * @param fullPathName
	 * @return boolean
	 * @throws OBFileNotFoundException
	 * @throws OBLicenseInvalidFormatException
	 * @throws OBException
	 */
	public boolean isValidLicenseFormat(String fullPathName) throws OBException;

	/**
	 * 오픈라인 라이센스 업데이트를 실시한다.
	 * 
	 * @param fileName : 파일을 시스템에 다운로드한 후 시스템에 저장된 파일의 위치. fullpath 포함. null 불가.
	 * @param extraInfo : 부가 정보. null 불가.
	 * @throws OBException
	 */
	public boolean offlineUpdateLicense(String fileName, OBDtoExtraInfo extraInfo) throws OBException;
//	public OBDtoLicense getLicenseInfo() throws OBFileNotFoundException, OBLicenseInvalidFormatException, OBException;

//	public String	makeLicense(OBDtoLicense licenseInfo) throws OBException;
}
