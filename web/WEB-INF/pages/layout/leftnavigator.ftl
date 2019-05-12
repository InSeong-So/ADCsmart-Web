<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="nav">
	<ul class="topSnb" id="menu" width="160px">
		<#if (accountRole! == 'system')>
		<li>
			<a class="dashboardMnu" href="#">				
				<img src="imgs/meun${img_lang!""}/M_Dashboard_0.gif" alt="Dashboard">					
			</a>			
		</li>
		</#if>
		<#if (accountRole! == 'system') || (accountRole! == 'config')>	
		<li>
			<a class="faultMgmtMnu" href="#">
				<img src="imgs/meun${img_lang!""}/M_Analysis_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_FAULT_DIAG"]!}">								
			</a>
			<ul>			
				<li>
					<a class="faultHistoryMnu" href="#">
						<img src="imgs/meun${img_lang!""}/M_Analysis_faultHistory_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_HISTORY"]!}">												
					</a>
				</li>
				<li>
					<a class="faultAnalysisMnu" href="#">
						<img src="imgs/meun${img_lang!""}/M_Analysis_collectionList_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_PACKET_DIAG"]!}">												
					</a>
				</li>
				<li>
					<a class="monitorL2Mnu" href="#">						
						<img src="imgs/meun${img_lang!""}/M_Analysis_L2Search_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_L2_SEARCH"]!}">						
					</a>
				</li>
				<li>
					<a class="sessionMonitoringMnu" href="#">						
						<img src="imgs/meun${img_lang!""}/M_Analysis_sessionSearch_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_SESSION_SEARCH"]!}">												
					</a>
				</li>
			</ul>
		</li>
		</#if>
		<li>
			<a id="monitor" class="monitorMnu" href="#">				
				<img src="imgs/meun${img_lang!""}/M_Monitor_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_MONITORING"]!}">								
			</a>
			<ul>		
				<li>
					<a class="monitorApplianceMnu" href="#">						
						<img src="imgs/meun${img_lang!""}/M_Monitor_adcdevice_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_APPL_MONITORING"]!}">						
					</a>
				</li>		
				<li>
					<a class="monitorServicePerfomanceMnu" href="#">						
						<img src="imgs/meun${img_lang!""}/M_Monitor_servicePerformance_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_SERVICE_PERF_MONITOR"]!}">												
					</a>
				</li>
				<li>
					<a class="monitorNetworkMnu" href="#">						
						<img src="imgs/meun${img_lang!""}/M_Monitor_vsMonitor_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_VS_SUMMARY"]!}">												
					</a>
				</li>	
				<li>
					<a class="monitorGroupMnu" href="#">
						<img src="imgs/meun${img_lang!""}/M_Monitor_group_0.gif" alt="Group">
					</a>
				</li>
				<li>
					<a class="monitorRealServerMnu" href="#">
						<img src="imgs/meun${img_lang!""}/M_Monitor_realserver_0.gif" alt="Realserver">
					</a>
				</li>						
 				<li>
 					<a class="monitorStatisticsMnu" href="#"> 						
						<img src="imgs/meun${img_lang!""}/M_Monitor_interface_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_INTERFACE"]!}">							 						
 					</a>
 				</li>
 				<#if (accountRole! != 'vsAdmin') || (accountRole! != 'rsAdmin')> 				
	 				<li>
						<a class="flbInfoMnu" href="#">
							<img src="imgs/meun${img_lang!""}/M_Configuration_FLBinfo_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_SLB_SETTING"]!}">						
						</a>
					</li>
				</#if>
				<li>
					<a class="monitorFaultMnu" href="#">
						<img src="imgs/meun${img_lang!""}/M_Monitor_faultlog_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_FAULT"]!}">												
					</a>
				</li>
				<li>
					<a class="monitorAlertMnu" href="#">
						<img src="imgs/meun${img_lang!""}/M_Monitor_alert_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_FAULT"]!}">												
					</a>
				</li>		
				<li>
					<a class="adcLogMnu" href="#">
						<img src="imgs/meun${img_lang!""}/M_Monitor_adclog_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_LOG"]!}">						
					</a>
				</li>			
			</ul>
		</li>
		<li>
			<a class="slbMgmtMnu" href="#">				
				<img src="imgs/meun${img_lang!""}/M_Configuration_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_SLB_SETTING"]!}">								
			</a>
			<#if accountRole! == 'system'>			
			<ul class="m2">
				<li>
					<a class="adcSettingMnu" href="#">
						<img src="imgs/meun${img_lang!""}/M_Configuration_adcSet_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_ADC_SETTING"]!}">												
					</a>
				</li>				
				<li>
					<a class="slbSettingMnu" href="#">
						<img src="imgs/meun${img_lang!""}/M_Configuration_slbSet_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_SLB_SETTING"]!}">						
					</a>
				</li>				
				<li>
					<a class="slbHistoryMnu" href="#">
						<img src="imgs/meun${img_lang!""}/M_Configuration_setHistory_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_SLB_HISTORY"]!}">												
					</a>
				</li>	
				<li>
					<a class="adcAlertMnu" href="#">
						<img src="imgs/meun${img_lang!""}/M_Configuration_loadadcalert_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_ADC_ALERT"]!}">							
					</a>
				</li>			
			</ul>
			</#if>
			<ul class="m3">
				<li>
					<a class="adcSettingMnu" href="#">
						<img src="imgs/meun${img_lang!""}/M_Configuration_adcSet_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_ADC_SETTING"]!}">												
					</a>
				</li>
				<li>
					<a class="slbSettingMnu" href="#">
						<img src="imgs/meun${img_lang!""}/M_Configuration_slbSet_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_SLB_SETTING"]!}">												
					</a>
				</li>
				<li>
					<a class="slbHistoryMnu" href="#">
						<img src="imgs/meun${img_lang!""}/M_Configuration_setHistory_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_SLB_HISTORY"]!}">											
					</a>
				</li>				
			</ul>
		</li>
		<li>
			<a class="reportMnu" href="#">
				<img src="imgs/meun${img_lang!""}/M_Report_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_REPORT"]!}">				
			</a>		
			<ul></ul>	
		</li>		
		<li>
			<a class="sysSettingMnu" href="#">
				<img src="imgs/meun${img_lang!""}/M_Sysadmin_0.gif" alt="${LANGCODEMAP["MSG_LAYOUT_SYS_MAN"]!}">								
			</a>					
		</li>		
	</ul>
	<span class="bottom">
<!-- <span class="bottom" title="버전: 3.0.3 &nbsp;
     2014.03.25 &nbsp;
     &nbsp;
     (c)2014 OPENBASE &nbsp;
              이 프로그램의 저작권은 (주) 오픈베이스에 있습니다."> -->
		<img src="imgs/meun${img_lang!""}/menu_bottom_obLogo.png" alt="${LANGCODEMAP["MSG_LAYOUT_OB_LOGO"]!}" class="bottom">				
	</span>   
</div>