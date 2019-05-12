package kr.openbase.adcsmart.web.report;

import kr.openbase.adcsmart.service.utility.OBException;

public interface Report 
{
	void generate() throws OBException;
	void generate(String index) throws OBException;
}
