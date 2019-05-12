//console.log("aaaaaaaaaaaaaaa");
var MyApp=angular.module('MyApp',[]);
//아래 컨트롤러는 각 페이지,html,div 에 사용하도록 한다.
MyApp.controller('SysViewController', function($scope,$http) {
//	console.log("bbbbbbbbbbbbbbbb");
	//서버로부터 데이터를 호출 한다.
	
	/*$scope.startDump = function(){
		$http({
			method : 'GET',
			url : 'sysTools/isDumpDownloadFileExist.action'
		}).success(function(data, status, headers, config) {
			
		}).error(function(data, status, headers, config) {
			console.log("호출 에러 or 서버에러..");	
		});
	}*/
	
/*	$scope.runDumpDownload = function(){
		
		$http({
			method : 'GET',
			url : 'sysTools/isDumpDownloadFileExist.action'
		}).success(function(data, status, headers, config) {
//			var url = "sysSetting/runDumpConfig.action";
//			$('#downloadFrame').attr('src', url);
//			console.log("PERSON_data: ",data);
//			console.log("PERSON_status: ",status);
//			console.log("PERSON_headers: ",headers);
//			console.log("PERSON_config: ",config);
//			$scope.accounts = data;
//			console.log("socpe_data: ",$scope.persons);	
			runDumpDownload();
		}).error(function(data, status, headers, config) {
			console.log("호출 에러 or 서버에러..");	
		});
		
	}*/
	
	$scope.startDump = function(){
		
		$http({
			method : 'GET',
			url : 'sysTools/dumpFileCreate.action'
		}).success(function(data, status, headers, config) {
			jsToolsMain.loadSysViewContent();
			$('.startDump').attr("flag", "true")
		}).error(function(data, status, headers, config) {
			console.log("호출 에러 or 서버에러..");	
		});
		
	}
	
	$scope.runDumpDownload = function() 
	{
		var url = "sysTools/runDumpDownload.action";
		$('#downloadFrame').attr('src', url);	
	}
	
//	target: "#downloadLnk",
//	successFn : function(params) 
//	{
//		var url = "sysSetting/backup/downloadBackupfile.action?backupIndex=" + index;
//		$('#downloadFrame').attr('src', url);
//	},
});