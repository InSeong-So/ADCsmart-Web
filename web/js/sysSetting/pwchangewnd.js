var PwChangeWnd =  Class.create({
	initialize : function() 
	{
	},
	popUp : function() 
	{
		with (this) 
		{
			showPopup
			({
				"id" : "#pwChange",
				"width" : "494px"
			});
			// 함수를 두번 호출하여 삭제함.
			_registerEvents();
			
		}
	},
	passwordChange : function(userId, password, index) 
	{
		with (this) 
		{
			ajaxManager.runJsonExt({
				url: 'sysSetting/changePassword.action',
				target: "#pwChange",
				data : 
				{
					"userId" : userId == undefined ? undefined : userId,
					"password" : password == undefined ? undefined : password,
					"index" : index == undefined ? "" : index
				}, 
				successFn : function(data) 
				{
					if(password == undefined)
					{
						return true;
					}
					
					if(data.isSuccessful == false){
						$.obAlertNotice(VAR_COMMON_PASSWDHISTORY);
						return false;
					}
					
					$('.onCancel').click();
					$.obAlertNotice(VAR_PW_CHANGESUCCESS);
					initialize();
				},
				errorFn : function(jqXhr)
				{
					$.obAlertAjaxError(VAR_PW_CHANGEFAIL, jqXhr);
				}	
			});
			
		}
	},
	
	validateUserAdd : function(data, oldPasswdList) 
	{
		var password = data.userPassword;
		if (password == '') 
		{
			$.obAlertNotice(VAR_COMMON_PASSWDINPUT);
			return false;
		}
		
		if ($('.cloneDiv .confirmPassword').val() == '') 
		{
			$.obAlertNotice(VAR_COMMON_PASSWDCONFIRM);
			return false;
		}
		
		if($('.cloneDiv .confirmPassword').val() != $('.cloneDiv .password').val())
		{
			$.obAlertNotice(VAR_PW_RULWRONG);
			return false;
		}
		
//		if ($('.cloneDiv .password').val() != $('.cloneDiv .confirmPassword').val()) 
//		{
//			$.obAlertNotice(VAR_PW_INPUTWRONG);
//			return false;
//		}
	
		// 이전 패스워드 목록울 가져온다.
		if(getValidatePasswd(data, oldPasswdList) == false)
		{
			$.obAlertNotice(VAR_PW_RULWRONG);
			return false;
		}
		
		return true;
	},
	_registerEvents : function() 
	{
		with (this) 
		{
			$('.onOk').click(function(event) 
			{
				event.preventDefault();
				
				var data = {
						"userId" : $('input[name="account.id"]').val(),
						"userName" : $('input[name="account.name"]').val(),
						"userPhoneNumb" : $('input[name="account.phone"]').val(),
						"userPassword" : $('.cloneDiv .password').val()
				};
				// 사용자 추가 시 프론트엔드에서의 암호화 작업에 이슈가 있어 서버쪽으로 변경함. 차후 수정 필요.
				// 해당 패스워드는 validate용으로 암호화를 시킴.
				var password = passwordEncode($('.cloneDiv .password').val());
//				var password = $('.cloneDiv .password').val();
				var confirmPassword = $('.cloneDiv .confirmPassword').val();
				var index = $('input[name="account.index"]').val();
				var accountHistory = $('input[name="account.history"]').val();
			
				if(!validateUserAdd(data, accountHistory))
				{
						return false;
				}
				passwordChange(data.userId, password, index);
				
			});
			
			$('.onCancel').click(function(event) 
			{
				event.preventDefault();
				$('.close').click();
			});
		}
	}
});

