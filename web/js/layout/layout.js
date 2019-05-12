/*
 * 로그인 후 화면을 구성한다.  header, firstmenu, secondenu, maincontent 네가지 형태로 구성된다.
 */
var ClassLayout = Class.create(
{
	initialize : function() 
	{
		this.refreshIntervalSeconds = 10;	//  시간조절 초단위
		this.refreshTimer = undefined;
		this.OBajaxManager = new OBAjax();
		this.CFirstmenu = new ClassMenu1st();
		this.CHeader = new ClassLayoutHeader(this.CFirstmenu);
		this.accountRole = undefined;
	},	
	loadContent : function() 
	{
		with (this) 
		{
			this.CHeader.loadContent();// header loading
			this.CFirstmenu.loadContent();// left navigator loading 
			_loadPickView();
			_loadAccountRole();
			_loadAdcSearchBar();
		}
	},	
	getAccountRole : function() 
	{
		with (this) 
		{
			return accountRole;
		}
	},
	_setAccountRole : function(accountRole1) 
	{
		with (this) 
		{
			this.accountRole = accountRole1;
		}
	},
	_loadPickView : function()
	{	
		with(this)
		{
			ajaxManager.runHtml({
				url : "layout/loadPickView.action",
				target : "#wrap .pickView",
				successFn : function(params)
				{
					
				}
			});
		}			
	},
	_loadAccountRole : function()
	{
		with(this)
		{
			ajaxManager.runJsonExt({
				url : "base.action",
				successFn : function(data) 
				{
					_setAccountRole(data.accountRole);
				},
				errorFn : function(a, b, c)
	            {
	            }
			});
		}
	},
	_loadAdcSearchBar : function()
	{
		with(this)
		{
			ajaxManager.runHtml({
				url : "layout/loadAdcSearchBar.action",
				target : "#wrap .adcSearch",
				successFn : function(params)
				{					
				}
			});
		}		
	},
});
