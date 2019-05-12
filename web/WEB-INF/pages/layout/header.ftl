<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>

<div class="top">
	<span class="logo">
		<#if (varIsSDSSite??) && (varIsSDSSite) == true>
			<a id="header_logo" title="${LANGCODEMAP["MSG_LAYOUT_SDS"]!}"; class="css_textCursor">
				<img src="imgs/customize_sds/header_logo.png">
			</a>
		<#else>
			<a id="header_logo" title="ADCsmart"; class="css_textCursor">
				<img src="imgs/customize/header_logo.png">
			</a>
		</#if>
	</span>	
	
	<div class="menu1">  
	<#if (accountRole! == 'system')>  
    	<span>
    		<a href="javascript:;" class="dashboardMnu" id="dashboardMnu"><img style="display:none" src="imgs/meun${img_lang!""}/1M_dashboard_0.png" title="${LANGCODEMAP["MSG_LAYOUT_DASHBOARD"]!}"><div id="dashboardimg" title="대시보드"></div></a>
    	</span>
    </#if>
    <#if (accountRole! == 'system') || (accountRole! == 'config')>	
        <span>
        	<a href="javascript:;" class="faultMgmtMnu" id="faultMgmtMnu"><img style="display:none" src="imgs/meun${img_lang!""}/1M_Analysis_0.png"><div id="faultMgmtimg" title="${LANGCODEMAP["MSG_LAYOUT_DIAG"]!}"></div></a>
        </span>
    </#if>
        <span>
        	<a href="javascript:;" class="monitorMnu" id="monitorMnu"><img style="display:none" src="imgs/meun${img_lang!""}/1M_Monitor_0.png" title="${LANGCODEMAP["MSG_LAYOUT_MONITORING"]!}"><div id="monitorimg" title="${LANGCODEMAP["MSG_LAYOUT_MONITORING"]!}"></div></a>        	
        </span>

        <span>
        	<a href="javascript:;" class="logMnu" id="logMnu"><img style="display:none" src="imgs/meun${img_lang!""}/1M_Log_0.png" title="${LANGCODEMAP["MSG_LAYOUT_LOG"]!}"><div id="logimg" title="${LANGCODEMAP["MSG_LAYOUT_LOG"]!}"></div></a>
        </span>   
        <span>
        	<a href="javascript:;" class="slbMgmtMnu" id="slbMgmtMnu"><img style="display:none" src="imgs/meun${img_lang!""}/1M_Configuration_0.png" title="${LANGCODEMAP["MSG_LAYOUT_SETTING"]!}"><div id="slbMgmtimg" title="${LANGCODEMAP["MSG_LAYOUT_SETTING"]!}"></div></a>        	
        </span>
        <span><a href="javascript:;" class="reportMnu" id="reportMnu"><img style="display:none" src="imgs/meun${img_lang!""}/1M_Report_0.png" title="${LANGCODEMAP["MSG_LAYOUT_REPORT"]!}"><div id="reportimg" title="${LANGCODEMAP["MSG_LAYOUT_REPORT"]!}"></div></a></span>

        <span>
        	<a href="javascript:;" class="sysSettingMnu" id="sysSettingMnu"><img style="display:none" src="imgs/meun${img_lang!""}/1M_Sysadmin_0.png" title="${LANGCODEMAP["MSG_LAYOUT_SYS_MAN"]!}"><div id="sysSettingimg" title="${LANGCODEMAP["MSG_LAYOUT_SYS_MAN"]!}"></div></a>
        </span>                            
        <span>
        	<a href="javascript:;" class="toolMnu" id="toolMnu"><img style="display:none" src="imgs/meun${img_lang!""}/1M_Systools_0.png" title="${LANGCODEMAP["MSG_LAYOUT_TOOL"]!}"><div id="systoolsimg" title="${LANGCODEMAP["MSG_LAYOUT_TOOL"]!}"></div></a>
        </span>
	</div>
	<!--end of navigation 1depht-->
	
	<span class="left_admin">		
    	<span class="userID">
        	<img src="imgs/icon/bullet_user01.png" />
            	<a class="loginIdLnk" title="${LANGCODEMAP["MSG_LAYOUT_USER_ID"]!}"; href="javascript:;">
                	<span style="display:none;">${account.index}</span>
                	<span style="display:none;">${account.name!''}</span>
                	<span style="display:none;">${account.phone!''}</span>
                	${account.id}
                </a>
		</span>    
        <span class="logout">
        	<a id="logoutLnk" title="${LANGCODEMAP["MSG_LAYOUT_LOGOUT"]!}"; href="javascript:;">
        		<img src="imgs/meun${img_lang!""}/btn_logout.png">                         
        	</a>
		</span>
	</span>
	
	<span class="menu2 dashSub none">
	    <ul>			
	      	<li class="dashDynamicMnu"><a href="javascript:;" class="dashDynamicMnu">Dynamic</a></li>
	      	<li class="dashServiceMnu"><a href="javascript:;" class="dashServiceMnu">ServiceTotal</a></li>	      	          
		</ul>
	</span>
	
	<span class="menu2 faultSub none">
    	<ul>
    		<li><a class="faultHistoryMnu" href="javascript:;">${LANGCODEMAP["MSG_DIAG_SET_DIAGNOSISS"]!}</a></li>
			<li><a class="faultAnalysisMnu" href="javascript:;">${LANGCODEMAP["MSG_LAYOUT_PACKET_DIAG"]!}</a></li>
			<li><a class="monitorL2Mnu" href="javascript:;">${LANGCODEMAP["MSG_LAYOUT_L2_SEARCH"]!}</a></li>
			<li><a class="sessionMonitoringMnu" href="javascript:;">${LANGCODEMAP["MSG_LAYOUT_SESSION_SEARCH"]!}</a></li>
		</ul>
	</span>

	<span class="menu2 systemSub none">
	    <ul>
			<li class="sysSettingUserMnu"><a href="javascript:;" class="userMgmtPgh">사용자 관리</a></li>
			<#if accountRole == 'system'>
            <li class="sysSettingVsFilterMnu"><a href="javascript:;" class="vsFilterPgh">대시보드VS필터</a></li>
			<li class="sysSettingLogicalGroupMnu"><a href="javascript:;" class="logicalGrpPgh">${LANGCODEMAP["MSG_LAYOUT_LOGICAL_GROUP"]!}</a></li>
			<li class="sysSettingBackupMnu"><a href="javascript:;" class="sysBackupPgh">${LANGCODEMAP["MSG_LAYOUT_SYS_BAKUP"]!}</a></li>
	      	<li class="sysSettingSysInfoMnu"><a href="javascript:;" class="systemInfoPgh">${LANGCODEMAP["MSG_LAYOUT_SYS_INFO"]!}</a></li>
			<li class="sysSettingLicMnu"><a href="javascript:;" class="licensePgh">${LANGCODEMAP["MSG_LAYOUT_LICENCE"]!}</a></li>
	      	<li class="sysSettingAuditMnu"><a href="javascript:;" class="auditLogPgh">${LANGCODEMAP["MSG_LAYOUT_AUDIT_LOG"]!}</a></li>  
	      	<li class="sysSettingLogContentMnu"><a href="javascript:;" class="logContentPgh">${LANGCODEMAP["MSG_LAYOUT_ADMIN_LOG"]!}</a></li>
	      	<li class="sysSettingConfigMnu"><a href="javascript:;" class="configPgh">${LANGCODEMAP["MSG_LAYOUT_SYS_SETTING"]!}</a></li> 
	      	</#if>                     
		</ul>
	</span>
	
	<span class="menu2 toolSub none">
	    <ul>	      	
	      	<li class="sysSettingResponseTimeMnu"><a href="javascript:;" class="responseTimePgh">${LANGCODEMAP["MSG_LAYOUT_RESPONSE_TIME"]!}</a></li>
	      	<li class="sysSettingSysViewDumpMnu"><a href="javascript:;" class="sysViewDumpPgh">${LANGCODEMAP["MSG_LAYOUT_SYSVIEW_DUMP"]!}</a></li>
		</ul>
	</span>
	
	<span class="menu2 slbSettingSub none">
	    <ul class="2depth">
			<li class="adcSettingMnu">
				<a href="javascript:;" class="">${LANGCODEMAP["MSG_LAYOUT_ADC_SETTING"]!}</a>
				<span class="menu3 adcSettingSubSub none">
				<ul class="3depth">
					<li class="adcModify"><a href="javascript:;" class="adcModify" id="adcModify">&#8226; ${LANGCODEMAP["MSG_LAYOUT_ADCMODIFY"]!}</a></li>
	              	<li class="adcConnect none"><a href="javascript:;" class="adcConnect" id="adcConnect">&#8226; ${LANGCODEMAP["MSG_LAYOUT_ADCCONNECTINFO"]!}</a></li>
	            </ul>
	            </span>
			</li>
			<li class="slbSettingMnu">
				<a href="javascript:;" class="">${LANGCODEMAP["MSG_LAYOUT_SLB_SETTING"]!}</a>
				<span class="menu3 slbSettingSubSub none">
	            <ul class="3depth">
					<li class="slbVs"><a href="javascript:;" class="" id="slbVs">&#8226; Virtual Server</a></li>					
	              	<li class="slbProfile none"><a href="javascript:;" class="slbProfile" id="slbProfile">&#8226; Profile</a></li>
	              	<li class="slbRs"><a href="javascript:;" class="" id="slbRs">&#8226; Real Server</a></li>
	              	<li class="slbNotice none"><a href="javascript:;" class="slbNotice" id="slbNotice">&#8226; Notice</a></li>
	            </ul>
				</span> 
			</li>
			<li class="slbScheduleMnu">
				<a href="javascript:;" class="">${LANGCODEMAP["MSG_LAYOUT_SLB_SCHEDULING"]!}</a>
			</li>
			<li class="slbHistoryMnu"><a href="javascript:;" class="">${LANGCODEMAP["MSG_LAYOUT_SLB_HISTORY"]!}</a></li>
			<#if accountRole! == 'system'>
			<li class="adcAlertMnu"><a href="javascript:;" class="">${LANGCODEMAP["MSG_LAYOUT_ADC_ALERT"]!}</a></li>
			</#if>
		</ul>
	</span>
	
	<span class="menu2 monitorSub none">
	    <ul>
			<li class="monitorApplianceMnu"><a href="javascript:;" class="monitorApplianceMnu">ADC</a></li>
			<li class="monitorServicePerfomanceMnu"><a href="javascript:;" class="monitorServicePerfomanceMnu">${LANGCODEMAP["MSG_LAYOUT_SERVICE"]!}</a></li>
			<li class="monitorNetworkMnu"><a href="javascript:;" class="monitorNetworkMnu">${LANGCODEMAP["MSG_LAYOUT_SERVICEMAP"]!}</a></li>
	      	<li class="monitorGroupMnu"><a href="javascript:;" class="monitorGroupMnu">Group</a></li>
			<li class="monitorRealServerMnu"><a href="javascript:;" class="monitorRealServerMnu">Real Server</a></li>
	      	<#if (accountRole! != 'vsAdmin') || (accountRole! != 'rsAdmin')>   
	      	<li class="flbInfoMnu"><a href="javascript:;" class="flbInfoMnu">${LANGCODEMAP["MSG_LAYOUT_FILTERINFO"]!}</a></li>
	      	</#if>
		</ul>
	</span>
	
	<span class="menu2 logSub none">
	    <ul>			
	      	<li class="monitorFaultMnu"><a href="javascript:;" class="monitorFaultMnu">${LANGCODEMAP["MSG_LAYOUT_FAULT"]!}</a></li>
	      	<li class="monitorAlertMnu"><a href="javascript:;" class="monitorAlertMnu">${LANGCODEMAP["MSG_LAYOUT_ALERT"]!}</a></li>
	      	<li class="adcLogMnu"><a href="javascript:;" class="adcLogMnu">${LANGCODEMAP["MSG_LAYOUT_ADCLOG"]!}</a></li>          
		</ul>
	</span>

	<!--end of navigation 2~3depht-->
	<div class="LocationNavi"></div>	
</div>
<!-- AlertPopup Area -->
<div class="alert-popup-area">
</div>
<div id="alarmMessage"></div>
