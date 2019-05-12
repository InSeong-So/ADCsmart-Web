<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div>
		<img src="imgs/title${img_lang!""}/h3_systemInfo.gif" class="title_h3" />		
	</div>
<!--  리뉴얼 -->
	<div class="syschart">
		<div class="control_positionR" >
			<!-- <input type="radio" name="searchTimeMode" id="hoursMode" checked="checked" value="${LANGCODEMAP["MSG_CONSTANT_HOURS_MODE"]!}" />
					<select class="formtext_wdth115" id="lastHours">
						<option value="1">${LANGCODEMAP["MSG_SYSTEMINFO_LAST_ONE_HOUR"]!}</option>
						<option value="3">${LANGCODEMAP["MSG_SYSTEMINFO_LAST_THREE_HOUR"]!}</option>
						<option value="6">${LANGCODEMAP["MSG_SYSTEMINFO_LAST_SIX_HOUR"]!}</option>
						<option value="12">${LANGCODEMAP["MSG_SYSTEMINFO_LAST_TWELVE_TIME"]!}</option>
						<option value="24">${LANGCODEMAP["MSG_SYSTEMINFO_LAST_TWNTYFOUR_TIME"]!}</option>
					</select>&nbsp;&nbsp;&nbsp;
				<input type="radio" name="searchTimeMode" id="periodMode" value="${LANGCODEMAP["MSG_CONSTANT_PERIOD_MODE"]!}" /> -->
			<input type="text" class="inputText_calendar"  name="reservation" id="reservationtime"  value="" readonly="">								
			<span>
				<input type="button" class="Btn_white" id="refresh"  value="${LANGCODEMAP["MSG_SYSTEMINFO_REFRESH"]!}">
			</span>
		</div>
		<br class="clearfix">	
		
		<div class="info2">
			<div class="title">CPU</div>
			<div class="number">
				<span id="cpuNowUsage">00</span> <span class="unit">%</span>
			</div>
		</div>
    	<div id="SysInfoCpuChart" class="Chart1"></div>    	
   	
		<div class="info2">
			<div class="title">Memory</div>
			<div class="number">
				<span id="memoryNowUsage">00</span> <span class="unit">%</span>
			</div>
		</div>
    	<div id="SysInfoMemoryChart" class="Chart2"></div>  	
		<br class="clearfix">	    	
		<div class="info1">
			<div class="title">
			    <li>${LANGCODEMAP["MSG_SYSTEMINFO_HD"]!}</li>
			</div>
			<div class="legend">
			<table>
				<tr>
					<td><div id="hdd_legend01"></div></td>					
					<th>${LANGCODEMAP["MSG_SYSTEMINFO_USED"]!}</th>
					<td><span id="hdd_used_size" class="number1"></span>
					<span id="hdd_used_usage" class="number2"></span><span class="unit"> %</span></td>					
				</tr>
				<tr>
					<td><div id="hdd_legend02"></div></td>
					<th>${LANGCODEMAP["MSG_SYSTEMINFO_FREE"]!}</th>
					<td><span id="hdd_free_size" class="number1"></span>
					<span id="hdd_free_usage" class="number2"></span><span class="unit"> %</span></td>					
				</tr>			
			</table>					
			</div>
			<div id="SysInfoHDDChart" class="stackChart"></div> 	
		</div>	
			
		<div class="info1">
			<div class="title">
			    <li>${LANGCODEMAP["MSG_SYSTEMINFO_DB"]!}</li>
			</div>
			<div class="legend_db">
			<table>
				<tr>
					<td><div id="db_legend01"></div></td>
					<th>${LANGCODEMAP["MSG_SYSTEMINFO_DB_LOG"]!}</th>
					<td><span id="db_log_size" class="number1"></span>
					<span id="db_log_usage" class="number2"></span><span class="unit"> %</span></td>
				</tr>
				<tr>
					<td><div id="db_legend02"></div></td>
					<th>${LANGCODEMAP["MSG_SYSTEMINFO_DB_INDEX"]!}</th>
					<td><span id="db_index_size" class="number1">></span> 
					<span id="db_index_usage" class="number2" ></span><span class="unit"> %</span></td>					
				</tr>
				<tr>
					<td><div id="db_legend03"></div></td>
					<th>${LANGCODEMAP["MSG_SYSTEMINFO_DB_GENERAL"]!}</th>
					<td><span id="db_general_size" class="number1">></span> 
					<span id="db_general_usage" class="number2"></span><span class="unit"> %</span></td>
				</tr>
				<tr>
					<td><div id="db_legend04"></div></td>
					<th>${LANGCODEMAP["MSG_SYSTEMINFO_DB_ETC"]!}</th>
					<td><span id="db_etc_size" class="number1">></span> 
					<span id="db_etc_usage" class="number2"></span><span class="unit"> %</span></td>
					
				</tr>
			</table>			
			</div>
			<div id="SysInfoDatabaseChart" class="stackChart_db"></div> 				
		</div>		
		  
	</div>				
</div>