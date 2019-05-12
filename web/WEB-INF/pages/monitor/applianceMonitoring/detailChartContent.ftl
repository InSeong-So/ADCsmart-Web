<div class="Board CpuOnlyNone">
		<div class="position_R10B5">
			<!-- <input type="radio" name="searchTimeMode" id="hoursMode" class="spCpuSub" checked="checked" value="${LANGCODEMAP["MSG_CONSTANT_HOURS_MODE"]!}" />
			<select class="formtext_wdth115 spCpuSub" id="lastHours">
				<option value="1">${LANGCODEMAP["MSG_APPLIANCE_ONE_TIME"]!}</option>
				<option value="3">${LANGCODEMAP["MSG_APPLIANCE_THREE_TIME"]!}</option>
				<option value="6">${LANGCODEMAP["MSG_APPLIANCE_SIX_TIME"]!}</option>
				<option value="12">${LANGCODEMAP["MSG_APPLIANCE_TWELVE_TIME"]!}</option>
				<option value="24">${LANGCODEMAP["MSG_APPLIANCE_TWNTYFOUR_TIME"]!}</option>
			</select>
			&nbsp;&nbsp;&nbsp;			
			<input type="radio" name="searchTimeMode" id="periodMode" class="spCpuSub" value="${LANGCODEMAP["MSG_CONSTANT_PERIOD_MODE"]!}" />
			<label for="periodMode" class="spCpuSub">
				<input name="startTime" class="inputText_calendar spCpuSub" type="text" title="${LANGCODEMAP["MSG_APPLIANCE_SELECTE_SEARCH_DATE"]!}" readonly/> &nbsp;~&nbsp;
				<input name="endTime" class="inputText_calendar spCpuSub" type="text" title="${LANGCODEMAP["MSG_APPLIANCE_SELECTE_SEARCH_DATE"]!}" readonly/>
			</label>&nbsp; -->
			<input type="text" class="inputText_calendar"  name="reservation" id="reservationtime"  value="" readonly="">
			<input type="button" id="refresh" class="Btn_white spCpuSub" value="${LANGCODEMAP["MSG_APPLIANCE_REFRESH"]!}"/>  	
 		</div>
</div>
<br class="clearfix" />		
<div class="device">
<!--
	<div class="info">
		<div class="title"></div>
		<div class="number" id="DetailCurr">
			<span class="unit"></span>
		</div>
		<div class="average" id="DetailPrev"></div>
		<div class="avgTitle"></div>
		<div class="number" id="DetailAvgCurr">
			<span class="unit"></span>
		</div>
	</div>	
-->	
	<div class="info">
		<div class="title"></div>
		<div class="number" id="DetailCurr">
			<span class="unit"></span>
		</div>
		<div class="average" id="DetailPrev"></div>
		<div class="averageM" id="DetailMaximun"></div>
		
		<div class="sessionTitle"></div>
		<div class="number sessionCurr" id="DetailSessionCurr">
			<span class="unit"></span>
		</div>
		<div class="average" id="DetailSessionPrev"></div>
	</div>	
	<div class="position_R10B5">
		<input type="button" id="exportCssLnk" class="exportCssLnk export_width Btn_white" value="${LANGCODEMAP["MSG_STATISTICS_OUT_FILE"]!}"/>
	</div>
		
	<div class="chart">
		<div id="detailChart" style="width: 100%; height: 300px;">
		</div>
	</div>		
<input type="button" id="" class="Btn_white spCPUSessionMain none" value="${LANGCODEMAP["MSG_APPLIANCE_HISTORYBACK"]!}">
</div>