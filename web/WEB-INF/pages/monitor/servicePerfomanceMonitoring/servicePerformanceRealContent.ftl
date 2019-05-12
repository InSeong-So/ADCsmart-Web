<!-----Real Server Info Content Page ----->
<div class="contents_area">
	<span>
		<#if (langCode) == "ko_KR">
			<img src="imgs/title/h3_monitor_memberPerformance.gif" class="title_h3" />
		<#elseif (langCode) == "en_US">
			<img src="imgs/title_eng/h3_monitor_memberPerformance.gif" class="title_h3" />
		<#else>
			<img src="imgs/title/h3_monitor_memberPerformance.gif" class="title_h3" />
		</#if>
	</span>	
	<#if adcObject.desciption == 'Alteon'>
		<#if adc.isFlb == 1>
			<div class="title_h2_tab">
				<span class="tab1">
					<img src="imgs/meun${img_lang!""}/3depth_slb_1.png" />						
				</span>
				<span class="tab2">
					<a class="flbGroupLnk" href="#">
						<img src="imgs/meun${img_lang!""}/3depth_flb_0.png" />							
					</a>
				</span>					
			</div>
		<#elseif (accountRole == 'vsAdmin') || (accountRole == 'rsAdmin')>
			<div class="title_h2_tab">
				<span class="tab1">
					<img src="imgs/meun${img_lang!""}/3depth_slb_1.png" />						
				</span>							
			</div>
		<#else>
			<div class="title_h2_tab">
				<span class="tab1">
					<img src="imgs/meun${img_lang!""}/3depth_slb_1.png" />						
				</span>
				<span class="tab2">
					<a class="">
						<img src="imgs/meun${img_lang!""}/3depth_flb_2.png" />							
					</a>
				</span>					
			</div>
		</#if>
	<#else>
	</#if>
	<span class="title_h3txt">
		<span class="vsNotice">
			Virtual Server ${LANGCODEMAP["MSG_PERFLIST_NAME"]!} (IP : 10.10.10.112  ${LANGCODEMAP["MSG_PERFLIST_PORT"]!} :80)
		</span>
	</span>	
	<div class="control_Board">
		<span class="calendar">		
			<input type="text" class="inputText_calendar" id="reservationtime" name="reservation"  value="" readonly="" >			
		</span>		
		<span class="total01"> 
			${LANGCODEMAP["MSG_APPLIANCE_TOTAL"]!} <span class="txt_bold noticePageCountInfo">00</span>${LANGCODEMAP["MSG_HISTORY_COUNT"]!} 
			<span class="txt_bold noticePageInfo">(2/4 ${LANGCODEMAP["MSG_APPLIANCE_PAGE"]!})</span>
		</span>					
			<div class="control_positionR">
			    <input type="button" id="exportMember" class="export_width Btn_white" value="${LANGCODEMAP["MSG_PERFCONT_EXPORT"]!}"/> 
                <input type="button" id="refresh" class="export_width Btn_white" value="${LANGCODEMAP["MSG_PERFCONT_REFRESH"]!}"/>		
                </div>			    												
	</div>
	
	<br class="clearfix" />
	<div class="servicechart">
	<div class="item1">
	<div class="title">${LANGCODEMAP["MSG_PERFAVGMAX_ITEM"]!}</div>	
		<ul>
			<li>				
				<input type="checkbox" class="bpsAllChk" id="bpsChk" name="bpsValChk"  checked="checked"/>
				<label for="bpsChk">bps</label>
				<ul>
					<li>							
						<input type="radio" class="selectBps" id="selectBpsIn" name="bps" value="0"/>
						<label for="selectBpsIn">bps In</label>	
					</li>	
					<li>										
						<input type="radio" class="selectBps" id="selectBpsOut" name="bps" value="1"/>
						<label for="selectBpsOut">bps Out</label>	
					</li>	
					<li>									
						<input type="radio" class="selectBps" id="selectBpsTotal" name="bps" checked="checked" value="2"/>
						<label for="selectBpsTotal">bps Total</label>	
					</li>	
				</ul>									
			</li>	
			<li>																					
				<input type="checkbox" id="concurrSessionChk" name="concurrSessionValChk" />
				<label for="concurrSessionChk">Concurrent Sessions</label>
			</li>
		</ul>	
	</div>														
	<div class="item2">
	<div class="title">${LANGCODEMAP["MSG_PERFAVGMAX_COMPAREAREA"]!}</div>
		<ul>	
			<li>
				<input type="radio" class="preCompare" id="preNot" name="preCompare" value="-1"  checked="checked" />
				<label for="preNot">${LANGCODEMAP["MSG_PERFAVGMAX_NO"]!}</label>	
			</li>					
			<li>
				<input type="radio" class="preCompare" id="preDay" name="preCompare" value="0" />
				<label for="preDay">${LANGCODEMAP["MSG_PERFAVGMAX_PREDAY"]!}</label>	
			</li>	
			<li>							
				<input type="radio" class="preCompare" id="preWeek" name="preCompare" value="1" />
				<label for="preWeek">${LANGCODEMAP["MSG_PERFAVGMAX_PREWEEK"]!}</label>	
			</li>	
			<li>					
				<input type="radio" class="preCompare" id="preMonth" name="preCompare" value="2" />
				<label for="preMonth">${LANGCODEMAP["MSG_PERFAVGMAX_PREMONTH"]!}</label>	
			</li>	
			<li>				
				<input type="radio" class="preCompare" id="selDate" name="preCompare" value="3" />
				<label for="selDate">	
					<input name="startTime" id="compareDateTime" class="inputText_calendar" type="text" title="${LANGCODEMAP["MSG_PERFCONT_QUERY_TERM_SEL"]!}" readonly/>
				</label>
			</li>	
		</ul>																						
	</div>
	<div class="item3 bpsChart">	
		<div class="chart">
			<div class="stackChart" id="MemberPerfChart"></div>
	    </div>	
		<div class="legend" >
			<div class="title defaultItem none"><span id="itemChange">bps Total</span></div>
			<table class="chartvalue">	
				<colgroup>
					<col width="auto">
					<col width="20%"/>
					<col width="20%"/>
					<col width="20%"/>	
				</colgroup>
				<thead>
					<tr>
						<th></th>
						<th>Current</th>
						<th>Avg</th>
						<th>Max</th>						
					</tr>
				</thead>
				<tbody class="bpsAvgMax">
				</tbody>
			</table>
		</div>		
	</div>
	<div class="item3 connCurrChart none">	
		<div class="chart">
			<div class="stackChart" id="MemberPerfConnChart"></div>
	    </div>	
		<div  class="legend" >
			<div class="title defaultItem none">Concurrent Sessions</div>	
			<table class="chartvalue">	
				<colgroup>
					<col width="auto">
					<col width="20%"/>
					<col width="20%"/>
					<col width="20%"/>	
				</colgroup>
				<thead>
					<tr>
						<th></th>
						<th>Current</th>
						<th>Avg</th>
						<th>Max</th>						
					</tr>
				</thead>
				<tbody class="connAvgMax">
				</tbody>
			</table>
		</div>		
	</div>		
</div>	
	<!----- Contents Page Start ----->
	<div class="title_h4_11">
		<li>${LANGCODEMAP["MSG_PERFCONT_PERF_INFO"]!}
			<span class="vsMonitorAdcName" id="lisetHeaderChange"></span>
		</li>
	</div>
	<div class="vs_trafficInfo">
		<table class="Board svcTrafficInfoList" id="svc_table" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
		</table>
	</div>
		
	<div class="Board">		
		<p class="pageNavigator"></p>
		<div id="select1" class="pageRowCountCbx"></div>
		<input type="button" id="serviceListLnk" class="Btn_white" value="${LANGCODEMAP["MSG_PERFCONT_MOVE_BACK"]!}"/>  
	</div>
	</div>
	<!----- Contents Page End ----->
</div>
<br class="clearfix" />