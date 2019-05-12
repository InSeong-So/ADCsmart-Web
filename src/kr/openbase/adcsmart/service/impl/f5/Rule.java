package kr.openbase.adcsmart.service.impl.f5;

public class Rule {
	private iControl.Interfaces _interfaces = null;
	private iControl.LocalLBRulePortType _LocalLBRule = null;
	private String _name = null;

	// constructor
	public Rule(iControl.Interfaces interfaces, String name) throws Exception {
		_interfaces = interfaces;
		_name = name;
		_LocalLBRule = _interfaces.getLocalLBRule();
	}

	// member - interfaces
	public iControl.Interfaces getInterfaces() {
		return _interfaces;
	}

	// member - name
	public void setInterfaces(iControl.Interfaces interfaces) {
		_interfaces = interfaces;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public iControl.LocalLBRulePortType getRuleStub() {
		return _LocalLBRule;
	}

	// member - VirtualServer stub
	public void setRuleStub(iControl.Interfaces interfaces) throws Exception {
		_LocalLBRule = _interfaces.getLocalLBRule();
	}

	// base value validate
	protected void validateBaseValues() throws Exception {
		if (_interfaces == null) {
			throw new Exception("Rule: F5 Interfaces error(null)");
		} else if (_name == null) {
			throw new Exception("Rule: virtual server name error(null)");
		} else if (_LocalLBRule == null) {
			throw new Exception("Rule: F5 API Stub error(null)");
		}
	}

	protected static void validateBaseValues(iControl.Interfaces interfaces) throws Exception {
		if (interfaces == null) {
			throw new Exception("Rule: F5 interface error(null)");
		}
	}

	public static String[] getList(iControl.Interfaces interfaces) throws Exception {
		String[] rule_list = null;

		validateBaseValues(interfaces);

		rule_list = interfaces.getLocalLBRule().get_list();
		return rule_list;
	}

//	public void create(...) throws Exception
//	{
//		validateBaseValues();
//	}
//
//	public void create(...) throws Exception
//	{
//		validateBaseValues();
//	}
//	
//	public void delete() throws Exception
//	{
//		validateBaseValues();
//	}
//	public static void delete(...) throws Exception
//	{
//		validateBaseValues(interfaces);
//	}

	// test functions
	// --------------------------------------------------------------------------------------
	/***********
	 * W A R N I N G !!!!! *********** adcsmart가 F5 BIGIP를 제어하는데 필요한 기능을 손쉽게 테스트하고,
	 * 주요 설정 값을 즉시 확인하도록 갖춰 놓은 테스트 함수 및 main()입니다. 가급적 바꾸지 말아 주세요. 특히 코드를 제거하면 안
	 * 됩니다. 필요한 경우 comment로 코드 실행을 건너뛰거나 테스트 함수를 추가하는 것은 좋습니다.
	 *********************************************/
//	public static void main(String[] args)
//	{
//		iControl.Interfaces intf = new iControl.Interfaces();
//		
//		intf.initialize("192.168.200.14", "admin", "admin");
//		try
//		{
//			String [] list = Rule.getList(intf);
//			for(int i=0; i<list.length; i++)
//			{
//				System.out.println(String.format(" rule[%d]: %s", i, list[i]));
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
}
