<#if orderOption.orderType??>
	<#if 11 ==  orderOption.orderType></#if><!-- occurTime -->
	<#if 19 ==  orderOption.orderType></#if><!-- adcName -->
	<#if 16 ==  orderOption.orderType></#if><!-- type -->
	<#if 21 ==  orderOption.orderType></#if><!-- severity -->
</#if>
<#if orderOption.orderDirection??>
	<#if 1 == orderOption.orderDirection></#if><!-- Asc -->
	<#if 2 == orderOption.orderDirection></#if><!-- Desc -->
</#if>	
<colgroup>
	<col width="160px"/>
	<col width="140px"/>		
	<col width="120px"/>
	<col width="auto"/>
</colgroup>
<thead>
	<tr class="StartLine">
		<td colspan="4"></td>
	</tr>
	<tr class="ContentsHeadLine">
		<th>	
			<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 11>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_ADCLOG_OCCUR_TIME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 11>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_ADCLOG_OCCUR_TIME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_ADCLOG_OCCUR_TIME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>						
		</th>		
		<th>
			<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 19>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_ADCLOG_ADCNAME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">19</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 19>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_ADCLOG_ADCNAME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">19</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_ADCLOG_ADCNAME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">19</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>				
		</th>		
		<th>
			<span class="css_textCursor">
				<#if orderOption.orderDirection == 2 && orderOption.orderType == 21>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_ADCLOG_TYPE"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">21</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 21>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_ADCLOG_TYPE"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">21</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_ADCLOG_TYPE"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">21</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>			
		</th>
		<th>${LANGCODEMAP["MSG_ADCLOG_CONTENT"]!}
		</th>	
	</tr>
	<tr class="StartLine1">
		<td colspan="4"></td>
	</tr>			
	<tr class="ContentsHeadLineFilter">	
		<td></td>	
		<td></td>	
		<!--<td class="align_center">
			 <select name="logtype-selecte" class="inputSelect logtype-selecte">
				<#if (selectOption.logType) == 0>
		  			<option value="0" selected="selected">전체</option>
		  			<option value="1">ADC</option>
		  			<option value="2">ADCsmart</option>
		  		<#elseif (selectOption.logType) == 1>
		  			<option value="0">전체</option>
		  			<option value="1" selected="selected">ADC</option>
		  			<option value="2">ADCsmart</option>
		  		<#elseif (selectOption.logType) == 2>
		  			<option value="0">전체</option>
		  			<option value="1">ADC</option>
		  			<option value="2" selected="selected">ADCsmart</option>
		  		<#else>
		  		</#if>
			</select> <img src="imgs/icon/icon_filter.gif">	 
		</td>-->
		<td class="align_center">
			<select name="loglevel-selecte" class="inputSelect loglevel-selecte">
		  		<#if (selectOption.logLevel) == "all">
		  			<option value="all" selected="selected">${LANGCODEMAP["MSG_ADCLOG_ALL"]!}</option>
		  			<option value="EMERG">EMERG</option>
		  			<option value="ALERT">ALERT</option>
		  			<option value="CRIT">CRIT</option>
		  			<option value="ERR">ERR</option>
		  			<option value="WARNING">WARNING</option>
		  			<option value="NOTICE">NOTICE</option>
		  			<option value="INFO">INFO</option>
		  			<option value="DEBUG">DEBUG</option>
		  		<#elseif (selectOption.logLevel) == "EMERG">
		  			<option value="all">${LANGCODEMAP["MSG_ADCLOG_ALL"]!}</option>
		  			<option value="EMERG" selected="selected">EMERG</option>
		  			<option value="ALERT">ALERT</option>
		  			<option value="CRIT">CRIT</option>
		  			<option value="ERR">ERR</option>
		  			<option value="WARNING">WARNING</option>
		  			<option value="NOTICE">NOTICE</option>
		  			<option value="INFO">INFO</option>
		  			<option value="DEBUG">DEBUG</option>
		  		<#elseif (selectOption.logLevel) == "ALERT">
		  			<option value="all">${LANGCODEMAP["MSG_ADCLOG_ALL"]!}</option>
		  			<option value="EMERG">EMERG</option>
		  			<option value="ALERT" selected="selected">ALERT</option>
		  			<option value="CRIT">CRIT</option>
		  			<option value="ERR">ERR</option>
		  			<option value="WARNING">WARNING</option>
		  			<option value="NOTICE">NOTICE</option>
		  			<option value="INFO">INFO</option>
		  			<option value="DEBUG">DEBUG</option>
		  		<#elseif (selectOption.logLevel) == "CRIT">
		  			<option value="all">${LANGCODEMAP["MSG_ADCLOG_ALL"]!}</option>
		  			<option value="EMERG">EMERG</option>
		  			<option value="ALERT">ALERT</option>
		  			<option value="CRIT" selected="selected">CRIT</option>
		  			<option value="ERR">ERR</option>
		  			<option value="WARNING">WARNING</option>
		  			<option value="NOTICE">NOTICE</option>
		  			<option value="INFO">INFO</option>
		  			<option value="DEBUG">DEBUG</option>
		  		<#elseif (selectOption.logLevel) == "ERR">
		  			<option value="all">${LANGCODEMAP["MSG_ADCLOG_ALL"]!}</option>
		  			<option value="EMERG">EMERG</option>
		  			<option value="ALERT">ALERT</option>
		  			<option value="CRIT">CRIT</option>
		  			<option value="ERR" selected="selected">ERR</option>
		  			<option value="WARNING">WARNING</option>
		  			<option value="NOTICE">NOTICE</option>
		  			<option value="INFO">INFO</option>
		  			<option value="DEBUG">DEBUG</option>
		  		<#elseif (selectOption.logLevel) == "WARNING">
		  			<option value="all">${LANGCODEMAP["MSG_ADCLOG_ALL"]!}</option>
		  			<option value="EMERG">EMERG</option>
		  			<option value="ALERT">ALERT</option>
		  			<option value="CRIT">CRIT</option>
		  			<option value="ERR">ERR</option>
		  			<option value="WARNING" selected="selected">WARNING</option>
		  			<option value="NOTICE">NOTICE</option>
		  			<option value="INFO">INFO</option>
		  			<option value="DEBUG">DEBUG</option>
		  		<#elseif (selectOption.logLevel) == "NOTICE">
		  			<option value="all">${LANGCODEMAP["MSG_ADCLOG_ALL"]!}</option>
		  			<option value="EMERG">EMERG</option>
		  			<option value="ALERT">ALERT</option>
		  			<option value="CRIT">CRIT</option>
		  			<option value="ERR">ERR</option>
		  			<option value="WARNING">WARNING</option>
		  			<option value="NOTICE" selected="selected">NOTICE</option>
		  			<option value="INFO">INFO</option>
		  			<option value="DEBUG">DEBUG</option>
		  		<#elseif (selectOption.logLevel) == "INFO">
		  			<option value="all">${LANGCODEMAP["MSG_ADCLOG_ALL"]!}</option>
		  			<option value="EMERG">EMERG</option>
		  			<option value="ALERT">ALERT</option>
		  			<option value="CRIT">CRIT</option>
		  			<option value="ERR">ERR</option>
		  			<option value="WARNING">WARNING</option>
		  			<option value="NOTICE">NOTICE</option>
		  			<option value="INFO" selected="selected">INFO</option>
		  			<option value="DEBUG">DEBUG</option>
		  		<#elseif (selectOption.logLevel) == "DEBUG">
		  			<option value="all">${LANGCODEMAP["MSG_ADCLOG_ALL"]!}</option>
		  			<option value="EMERG">EMERG</option>
		  			<option value="ALERT">ALERT</option>
		  			<option value="CRIT">CRIT</option>
		  			<option value="ERR">ERR</option>
		  			<option value="WARNING">WARNING</option>
		  			<option value="NOTICE">NOTICE</option>
		  			<option value="INFO">INFO</option>
		  			<option value="DEBUG" selected="selected">DEBUG</option>
		  		<#else>
		  		</#if>
			</select> <img src="imgs/icon/icon_filter.gif">					
		</td>
		<td></td>	
	</tr>
	<tr class="StartLine1">
		<td colspan="4"></td>
	</tr>				
</thead>	
<tbody>
	<#list adcLogs![] as theAdcLog>
		<tr class="ContentsLine1 adcLogList">
			<td class="align_center textOver">${theAdcLog.occur_time?string("yyyy-MM-dd HH:mm:ss")}
				<input type="hidden" class="adclog-occurtime" value="${theAdcLog.occur_time?string("yyyy-MM-dd HH:mm:ss")}" />
				<input type="hidden" class="adclog-adcname" value="${theAdcLog.adc_name!}" />
				<input type="hidden" class="adclog-type" value="${theAdcLog.type!}" />
				<input type="hidden" class="adclog-loglevel" value="${theAdcLog.log_level!}" />
				<input type="hidden" class="adclog-content" value='${theAdcLog.content!}' />
				<input type="hidden" class="adclog-extra-content" value="${theAdcLog.content_extra!}" />
			</td>
			<td class="align_left_P10  textOver">
				<a class="popuplink" href="#">${theAdcLog.adc_name!}</a>				
			</td>			
			<td class="align_left_P10 textOver">${theAdcLog.log_level!}</td>
			<td class="align_left_P10  textOver">
				<a class="popuplink" href="#">${theAdcLog.content_view!}</a>
			</td>	
		</tr>
		<tr class="DivideLine">
			<td colspan="4"></td>
		</tr>
	</#list>
	<tr class="EndLine">
		<td colspan="4"></td>
	</tr>						
</tbody>