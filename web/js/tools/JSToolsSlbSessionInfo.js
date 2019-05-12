var JSToolsSlbSessionInfo = Class.create({
	initialize : function() {
		this.isInitialized = false;
		this.adcIp = undefined;
		this.adcType = undefined;
		this.Id = undefined;
		this.password = undefined;
		this.ipType = undefined;
		this.ip = undefined;
		this.connService = undefined;
		this.connPort = undefined;
		
	},

	loadContent : function() {
		with (this) {
			ajaxManager.runHtmlExt({
				url: 'sysTools/slbSessionLoadContent.action',
				target: "#wrap .contents",
				data : 
				{
					"adcIPAddress" : this.adcIp,
					"adcType" : this.adcType,
					"adcUserID" : this.Id,
					"adcPasswd" : this.password,
					"searchIPType" :  this.ipType,
					"searchIPAddress" : this.ip,
					"connService"  : this.connService,
					"connPort"  : this.connPort
				}, 
				successFn : function(params) {
					registerAdcAddContentEvents();
					header.setActiveMenu('JSToolsSessionInfo');
				},
				errorFn : function(a,b,c){
					exceptionEvent();
				}	
			});
			
		}
	},
	validateUserAdd : function() {
		if ($('input[name="adcIPAddress"]').val() == '') 
		{
			$.obAlertNotice('IP를 입력하세요.');
			return false;
		}
		else if ($('input[name="adcUserID"]').val() == '') 
		{
			$.obAlertNotice('ID를 입력하세요.');
			return false;
		}
		else if ($('input[name="adcPasswd"]').val() == '') 
		{
			$.obAlertNotice('비밀번호를 입력하세요.');
			return false;
		}
		else if($('select[name="searchIPType"]').val() == '1' && $('input[name="searchIPAddress"]').val() == '')
		{
			$.obAlertNotice('Source IP를 입력하세요.');
			return false;
		}
		else if($('select[name="searchIPType"]').val() == '2' && $('input[name="searchIPAddress"]').val() == '')	
		{
			$.obAlertNotice('Destination IP를 입력하세요.');
			return false;
		}
		return true;
	},
	registerAdcAddContentEvents : function() 
	{
		with (this) 
		{
			$('.searchLnk').click(function(event) 
			{
				event.preventDefault();
				adcIp = $('input[name="adcIPAddress"]').val();
				adcType = $('select[name="adcType"]').val();
				Id = $('input[name="adcUserID"]').val();
				password = $('input[name="adcPasswd"]').val();  
				ipType = $('select[name="searchIPType"]').val();     
				ip = $('input[name="searchIPAddress"]').val(); 	
				if ($('input[name="connService"]:checked').val() != null)
				{
					var TestConnectionAdcConnService = $('input[name="connService"]:checked').val();
					if(TestConnectionAdcConnService == '23')
					{//선택된 통신포트가 telnet 인 경우 
						var Port = $('#telnet').val();
					}
					else if(TestConnectionAdcConnService == '22')
					{//선택된 통신포트가 ssl 인 경우 
						Port = $('#ssh').val();
					}
				}
				connService = TestConnectionAdcConnService;
				connPort= Port;
				if(!validateUserAdd())
				{
					return false;
				}
				loadContent();
				/*$('#uploadform').submit();*/
			});
			$('.exportCssLnk').click(function(event) 
			{
				event.preventDefault();  
				exportCss();
			});
			$('.connServiceSelect').change(function()
			{//ADC 통신 포트 변경
				var selectServiceType = $('input[name="connService"]:checked').val();
				if (selectServiceType == '23')
				{
					$('#telnet').removeAttr("disabled");
					$('#ssh').attr("disabled","disabled");
				}
				else if (selectServiceType == '22')
				{
					$('#ssh').removeAttr("disabled");
					$('#telnet').attr("disabled","disabled");
				}
				
			});
		}
	},
	exportCss : function() {
		with (this) {
			var params = "contents=" + $('textarea[name="contents"]').val().split(/\n/g).join("<br>");
			if(params == "contents=")
			{
				$.obAlertNotice("내보내기 데이터가 없습니다.");
				return;
			}
			var url = "sysTools/slbSessionDownloadCsv.action?" + params;
			$('#downloadFrame').attr('src', url);
		}
	},
});
