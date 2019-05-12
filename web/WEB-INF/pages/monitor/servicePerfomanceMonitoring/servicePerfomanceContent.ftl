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
		
		<#if adcObject.desciption == 'Alteon'>
			<#if adc.isFlb == 1>
				<div class="title_h2_tab">
					<span class="tab1">
							<img src="imgs/meun${img_lang!""}/3depth_slb_1.png"/>					
					</span>
					<span class="tab2">
							<a class="flbGroupLnk" href="#">
								<img src="imgs/meun${img_lang!""}/3depth_flb_0.png"/>					
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
	</div>	
	<div class="nulldata none"></div>
	<div class="successdata">
	<div class="control_Board">
		<span class="calendar">		
			<input type="button" class="inputText_realtime_off" id="realtime_btn" name="reservation" value="realTimeMode"/ readonly=""> 	
			<input type="text" class="inputText_calendar" id="reservationtime" name="reservation"  value="periodMode" readonly="" >
		</span>
		<span class="inputTextposition">
			<input name="searchKey" type="text" class="searchTxt inputText_search" id="svcPerfSearchKey" value="" placeholder="VS IP, VS NAME">
		</span>
		<span class="btn"> 
			<a class="searchLnk" href="#" id="svcPerfSearchBtn">
				<img src="imgs/meun/btn_search_1.png" alt="${LANGCODEMAP["MSG_PERFAVGMAX_SEARCH"]!}">
			</a>
			</span>
		<span class="total01"> 
			${LANGCODEMAP["MSG_APPLIANCE_TOTAL"]!} <span class="txt_bold noticePageCountInfo">00</span>${LANGCODEMAP["MSG_HISTORY_COUNT"]!} 
			<span class="txt_bold noticePageInfo">(2/4 ${LANGCODEMAP["MSG_APPLIANCE_PAGE"]!})</span>
		</span>					
			<div class="control_positionR">
			    <input type="button" id="exportCssLnk" class="export_width Btn_white" value="${LANGCODEMAP["MSG_PERFCONT_EXPORT"]!}"/> 
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
			<li>							
				<input type="checkbox" id="responseChk" name="responseValChk"  />
				<label for="responseChk">${LANGCODEMAP["MSG_PERFAVGMAX_RESPONSETIME"]!}</label>		
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
					<input name="startTime" id="compareDateTime" class="inputText_calendar2" type="text" title="${LANGCODEMAP["MSG_PERFCONT_QUERY_TERM_SEL"]!}" readonly/>
				</label>
			</li>	
		</ul>																						
	</div>
	<div class="item3 bpsChart">	
		<div class="chart">
			<div class="stackChart" id="SvcPerfBpsChart"></div>
	    </div>	
		<div class="legend">
			<div class="title defaultItem none"><span id="itemChange">bps Total</span></div>
			<table class="chartvalue" style="table-layout: fixed;">				
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
			<div class="stackChart" id="SvcPerfConncurrChart"></div>
	    </div>	
		<div  class="legend" >
			<div class="title defaultItem none">Concurrent Sessions</div>	
			<table class="chartvalue" style="table-layout: fixed;">
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
	<div class="item3 responseChart none">	
		<div class="chart">
			<div class="stackChart" id="SvcPerfResponseChart"></div>
	    </div>	
		<div  class="legend" >
			<div class="title defaultItem none">${LANGCODEMAP["MSG_PERFAVGMAX_RESPONSETIME"]!}</div>
			<table class="chartvalue responseAvgMax" style="table-layout: fixed;">	
			
			</table>
		</div>		
	</div>	
</div>
	 <!----- Contents Page Start ----->
	<div class="title_h4_1">
		<li>${LANGCODEMAP["MSG_PERFCONT_PERF_INFO"]!}
			<span class="vsMonitorAdcName" id="lisetHeaderChange"></span>
		</li>	 
	</div>
	<div class="vs_trafficInfo">
		<table class="Board svcTrafficInfoList" id="svc_table" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
		</table>
	</div>
	<div class="dataNotExistMsg none">${LANGCODEMAP["MSG_DATA_NOT_EXIST"]!}</div>
	<div class="nulldataMsg none">${LANGCODEMAP["MSG_COMMON_DATA_NULL"]!}</div>	
	<div class="searchNotMsg none">${LANGCODEMAP["MSG_SEARCH_NOT_RESULT"]!}</div>
	<div class="Board">		
		<p class="pageNavigator"></p>
		<div id="select1" class="pageRowCountCbx"></div>
	</div>
	</div>
	<!----- Contents Page End ----->
</div>
<br class="clearfix" />

<!-- 트래픽 상세정보 -->
<div id="trafficDesc" class="pop_type_wrap">
</div>
<!-- virtual server action 팝업 -->
<div id="vs_action" class="pop_type_wrap">
	<h2>
		Virtual Server Name &nbsp; <span class="color_e5d9a1">Vs_Name_1</span> 
		<img src="imgs/monitoring/icon_greenDot.png" alt="${LANGCODEMAP["MSG_PERFCONT_NORMAL"]!}"/>
	</h2>
	<div class="pop_contents">
		<table class="form_type1 none_mb mar_top10" summary="${LANGCODEMAP["MSG_PERFCONT_VS_ACTION_INFO"]!}">
			<caption>${LANGCODEMAP["MSG_PERFCONT_VS_ACTION_INFO"]!}</caption>
			<colgroup>
				<col width="35%" />
				<col width="55%" />
			</colgroup>
			<tbody>
				<tr>
					<th scope="row" class="bor_top">${LANGCODEMAP["MSG_PERFCONT_STATUS"]!}</th>
					<td class="bor_top">
						<input type="radio" checked="checked" /> Enabled <input type="radio" class="mar_lft10" /> Disabled
					</td>
				</tr>
				<tr>
					<th scope="row" class="bor_btm">Action</th>
					<td class="bor_btm">
						<a href="#">
							<img src="imgs/popup/btn_chngeStting.gif" alt="${LANGCODEMAP["MSG_PERFCONT_VS_SET_CHNAGE"]!}" />
						</a>
					</td>
				</tr>
			</tbody>
		</table>
		<p class="right mar_top5">
			<a title="${LANGCODEMAP["MSG_PERFCONT_CLOSE"]!}" onclick="closeLPopup()" href="#">
				<img alt="${LANGCODEMAP["MSG_PERFCONT_CLOSE"]!}" src="imgs/popup/btn_close.gif" />
			</a>
		</p>
	</div>
	<p class="close">
		<a href="#" title="${LANGCODEMAP["MSG_PERFCONT_CLOSE"]!}">
			<img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_PERFCONT_CLOSE"]!}" />
		</a>
	</p>
</div>
<!-- //virtual server action 팝업 -->