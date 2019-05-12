<!----- Contents List Start ----->
<div class="contents_area">
	<div>
		<#if (langCode) == "ko_KR">
			<img src="imgs/title/h3_monitor_service_adc.gif" class="title_h3" />
		<#elseif (langCode) == "en_US">
			<img src="imgs/title_eng/h3_monitor_service_adc.gif" class="title_h3" />
		<#else>
			<img src="imgs/title/h3_monitor_service_adc.gif" class="title_h3" />
		</#if>		
		
		<div class="title_h2_tab">
			<span class="tab1">
					<a class="slbServiceLnk" href="#">
						<img src="imgs/meun${img_lang!""}/3depth_slb_0.png" />
					</a>
			</span>						
			<span class="tab2">
					<img src="imgs/meun${img_lang!""}/3depth_flb_1.png" />				
			</span>						
		</div>
	</div>
	<div class="control_Board">
		<span class="calendar">
			<input type="text" class="inputText_calendar" id="reservationtime" name="reservation"  value="" readonly="" >
		</span>		
						
			<div class="control_positionR">
			    <input type="button" id="exportCssLnk" class="export_width Btn_white" value="${LANGCODEMAP["MSG_PERFCONT_EXPORT"]!}"/> 
                <input type="button" id="refresh" class="export_width Btn_white" value="${LANGCODEMAP["MSG_PERFCONT_REFRESH"]!}"/>		
            </div>			    												
	</div>
	<div id="">
		<ul class="tabs">	  		
	   		<li>
	   			<a class="adcSummary css_textCursor" id="" style="background-color:#666666; color: #fff;" >
	   				<span class="none status">1</span>${LANGCODEMAP["MSG_PERFCONT_BPSCONN"]!}
	   			</a>
	   		</li>
		</ul>
	</div>	
	<br class="clearfix" />		
	<div class="Board">			
	<div id="GroupPerfChart" style="width: 100%; height: 300px;"></div>			
	</div>
	 <!----- Contents Page Start ----->	
	<div class="title_h4_11">
		<li>${LANGCODEMAP["MSG_PERFCONT_PERF_INFO"]!}
			<span class="vsMonitorAdcName" id="lisetHeaderChange"></span>
		</li>
	</div>
	<div class="vs_trafficInfo">
		<table class="Board GroupPerfInfoList" id="group_table" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
		</table>
	</div>	
	
	<div class="Board">		
		<p class="pageNavigator"></p>
		<div id="select1" class="pageRowCountCbx"></div>
	</div>
	<!----- Contents Page End ----->
</div>
<br class="clearfix" />
