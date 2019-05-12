var License = Class.create({
	initialize : function() 
	{
		this.isInitialized = false;
	},
	loadContent : function() 
	{
		with (this) 
		{
			ajaxManager.runHtmlExt({
				url : 'sysSetting/loadLicenseContent.action',
				target : "#wrap .contents", 
				successFn : function(params) 
				{
					registerLicenseContents();
					header.setActiveMenu('SysSettingLic');
				},
				errorFn: function(jqXhr)
				{
					$.obAlertAjaxError(VAR_LICENSE_LOAD, jqXhr);
				}
			});			
		}
	},
	loadLicenseContent : function() 
	{
		with (this) 
		{
			ajaxManager.runHtmlExt({
				url : 'sysSetting/loadLicenseContent.action',
				target : "#wrap .contents", 
				successFn : function(params) 
				{
					registerLicenseContents();
					header.setActiveMenu('SysSettingLic');
				},
				errorFn: function(jqXhr)
				{
					$.obAlertAjaxError(VAR_LICENSE_LOAD, jqXhr);
				}
			});			
		}
	},
	
	registerLicenseContents : function() 
	{
		with (this) 
		{
			$('.licenseUploadOkLnk').click(function() 
			{		
				$('#uploadform').submit();
			});
			
			$('#uploadform').submit(function() 
			{
//			$('#uploadform').ajaxForm(function() {	
//				alert("test");
				if (!validateSystemConfigAdd())
					return false;
				$(this).ajaxSubmit({
					dataType : 'json',
					url : 'sysSetting/uploadFileContent.action',					
					success : function(data) 
					{
						//FlowitUtill.log(Object.toJSON(data));
						if (data.isSuccessful) 
						{
							$.obAlertNotice(VAR_COMMON_REGISUCCESS);
							if (data.accountRole == 'system')
								loadLicenseContent();
						} 
						else 
							$.obAlertNotice(data.message);
					},
					error : function(jqXhr)
					{
						$.obAlertAjaxError(VAR_LICENSE_FILEUPLOAD, jqXhr);					
					}
				});

				return false;
			});	
		}
	},
	
	validateSystemConfigAdd : function() 
	{
		//화면표시 설정
		var data = $('input[name="filepath"]').val();
					
//		var data2 = data.substring(data.length-3, data.length);
		
//		alert(data + "  :  " + data2);
		
		if ($('input[name="filepath"]').val() == null) 
		{
			$.obAlertNotice(VAR_LICENSE_NOFILE);
			return false;
		} 
		
		if (data != null)
		{
			var data2 = data.substring(data.length-3, data.length);
			
			if (data != 'license.lic') 
			{
				$.obAlertNotice(VAR_LICENSE_FILENAMEDIFFER);
				return false;
			} 
			else if (data2 != "lic") 
			{ //alert("1");
				$.obAlertNotice(VAR_LICENSE_NOTLICFILE);
				return false;
			} 
		}
		
		return true;
	}
});