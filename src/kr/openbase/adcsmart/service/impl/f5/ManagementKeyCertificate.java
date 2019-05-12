package kr.openbase.adcsmart.service.impl.f5;

import java.sql.Timestamp;
import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcSslCertificate;

class ManagementKeyCertificate {
	private static void validateBaseValues(iControl.Interfaces interfaces) throws Exception {
		if (interfaces == null) {
			throw new Exception("ManagementKeyCertificate: F5 interface error(null)");
		}
	}

//	public Timestamp getLocalConfigTime() throws Exception
//	{
//		validateBaseValues();
//		
//		String [] name = { "ConfigSync.LocalConfigTime" };
//		iControl.ManagementDBVariableVariableNameValue [] NameValue = _ManagementDBVariablePortType.query(name);
//		Timestamp ts = new Timestamp(Long.parseLong(NameValue[0].getValue())*1000);
//		return ts;
//	}

	private static iControl.ManagementKeyCertificateCertificateInformation[] getList(iControl.Interfaces interfaces)
			throws Exception {
		validateBaseValues(interfaces);

		iControl.ManagementKeyCertificateManagementModeType mode = iControl.ManagementKeyCertificateManagementModeType.MANAGEMENT_MODE_DEFAULT;

		iControl.ManagementKeyCertificateCertificateInformation[] certInfoList = interfaces
				.getManagementKeyCertificate().get_certificate_list(mode);

		return certInfoList;
	}

	static ArrayList<OBDtoAdcSslCertificate> getAll(iControl.Interfaces interfaces) throws Exception {
		ArrayList<OBDtoAdcSslCertificate> sslCertSet = new ArrayList<OBDtoAdcSslCertificate>();

		validateBaseValues(interfaces);

		// SSL certificate 리스트 구함
		iControl.ManagementKeyCertificateCertificateInformation[] sslCerts = ManagementKeyCertificate
				.getList(interfaces);

		int num = sslCerts.length;
		int i = 0;

		for (i = 0; i < num; i++) {
			OBDtoAdcSslCertificate sslCert = new OBDtoAdcSslCertificate();
			// sslCert.setAdcIndex(0);
			sslCert.setCertificateName(sslCerts[i].getCertificate().getCert_info().getId());
			sslCert.setCommonName(sslCerts[i].getCertificate().getSubject().getCommon_name());
			sslCert.setOrganizationName(sslCerts[i].getCertificate().getSubject().getOrganization_name());
			sslCert.setExpirationDate(new Timestamp(sslCerts[i].getCertificate().getExpiration_date() * 1000L));

			sslCertSet.add(sslCert);
		}
		return sslCertSet;
	}

// test functions ------------------------------------------------------------------------------------------
//	public static void main(String[] args)
//	{
//		iControl.Interfaces interfaces = new iControl.Interfaces();
//		
//		interfaces.initialize("192.168.200.11", "admin", "admin");
//		
//		try
//		{
//			iControl.ManagementKeyCertificateCertificateInformation [] certList = ManagementKeyCertificate.getList(interfaces);
//			for(ManagementKeyCertificateCertificateInformation cert : certList)
//			{
//				System.out.println("---------------------------------");
//				System.out.println("cert exp date   = " + cert.getCertificate().getExpiration_date());
//				//Timestamp ts = new Timestamp(cert.getCertificate().getExpiration_date());
//				//String tm = OBDateTime.toString(ts);
//				System.out.println("cert common name = " + cert.getCertificate().getSubject().getCommon_name());
//				System.out.println("cert exp string  = " + cert.getCertificate().getExpiration_string());
//				System.out.println("cert serial      = " + cert.getCertificate().getSerial_number());
//				System.out.println("cert id          = " + cert.getCertificate().getCert_info().getId());
//				System.out.println("cert org name    = " + cert.getCertificate().getSubject().getOrganization_name());
//
//				if(cert.isIs_bundled()==true)
//					System.out.println("is bundled?   = true");
//				else
//					System.out.println("is bundled?   = false");
//				
////				System.out.println("");
////				System.out.println("");
//			}
//		}
//		catch(Exception e)
//		{
//			System.out.println("Unreachable - " + e.getMessage());
//		}
//		
//		try
//		{
//			ArrayList<OBDtoAdcSslCertificate> results = ManagementKeyCertificate.getAll(interfaces);
//			for(OBDtoAdcSslCertificate rec:results)
//			{
//				System.out.println("---------------------------------");
//				System.out.println("cert id       = " + rec.getCertificateName());
//				System.out.println("cert exp date = " + (rec.getExpirationDate().toString()));
//			}
//		}
//		catch(Exception e)
//		{
//			System.out.println("Unreachable - " + e.getMessage());
//		}
//	}
}