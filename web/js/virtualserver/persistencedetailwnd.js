var PersistenceDetailWnd =  Class.create({
	initialize : function() 
	{
		this.adc;
		this.index;
	},
	setAdc : function(adc) 
	{
		this.adc = adc;
	},
	setIndex : function(index) 
	{
		this.index = index;
	},
	popUp : function() 
	{
		with (this) 
		{
			showPopup({
				"id" : "#persistenceDetail",
				"width" : "494px"
			});
			_loadDetail();
			_registerEvents();
		}
	},
	_loadDetail : function(index) 
	{
		with (this) 
		{
			ajaxManager.runJsonExt({
				url : "profile/retrieveProfileAdd.action",
				data : 
				{
					"adc.index" : adcSetting.getAdc().index,
					"profileAdd.index" : index
				},
				successFn : function(data) 
				{
					FlowitUtil.log('profileAdd: ' + Object.toJSON(data.profileAdd));
					_fillDetail(data.profileAdd);
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_VPPERSI_PROEXTFAIL, jqXhr);
//					exceptionEvent();
				}	
			});
		}
	},
	_registerEvents : function() 
	{
		with (this) 
		{
			$('.cloneDiv .closeWndLnk').click(function(e) 
			{
				e.preventDefault();
				$('.popup_type1').remove();
				$('.cloneDiv').remove();
			});
		}
	},
	_fillDetail : function(profileAdd) 
	{
		if (profileAdd == undefined || profileAdd == null)
			return;
		
		$('.cloneDiv span[name="profileAdd.name"]').text(profileAdd.name);
		$('.cloneDiv span[name="profileAdd.persistenceType"]').text(profileAdd.persistenceType == "SourceAddressAffinity" ? "Source Address Affinity" : "None");
		$('.cloneDiv span[name="profileAdd.parentProfileName"]').text(profileAdd.parentProfileName == "SourceAddr" ? "source_addr" : "None");
		$('.cloneDiv span[name="profileAdd.isMatchAcrossServices"]').text(String(profileAdd.isMatchAcrossServices) === 'true' ? "Enabled" : "Disabled");
		$('.cloneDiv span[name="profileAdd.timeOutInSec"]').text(profileAdd.timeOutInSec);
	}
});