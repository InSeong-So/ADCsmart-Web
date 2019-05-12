var DashboardLoader = Class.create({
	initialize : function() {
	},
	/*loadSystemOpertaion : function() 
	{
		var option = "width=1280,height=800,toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizeable=yes,left=0,top=0";
		window.open("dashboard/loadSystemAdmin.action", "systemOperation", option);
	},*/
	loadAdcSummaryDashboard : function() 
	{
		var option = "width=1280,height=760,toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,left=0,top=0";
		window.open("dashboard/sds/main.action", "adcSummaryDashboard", option);
	},	
	loadAdcSdsDashboard : function() 
	{
		var option = "width=1280,height=760,toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,left=0,top=0";
		window.open("dashboard/adcMon/adcMonMain.action", "adcSdsDashboard", option);
	},
	loadDashboard : function()
	{
		var option = "width=1280,height=760,toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,left=0,top=0";
		var DashboardPopup = window.open("dashboard/dashBoardMain.action", "Dashboard", option);
		return DashboardPopup;
	}
});