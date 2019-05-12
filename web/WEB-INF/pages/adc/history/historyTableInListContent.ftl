<#if orderType??>
	<#if 11 == orderType></#if><!-- occurTime -->
	<#if  1 == orderType></#if><!-- vsName -->
	<#if  2 == orderType></#if><!-- vsIpaddress -->
</#if>
<#if orderDir??>
	<#if 1 == orderDir></#if><!--Asc-->
	<#if 2 == orderDir></#if><!--Desc-->
</#if>
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>		
<colgroup>
	<col width="18%"/>
	<col width="18%"/>
	<col width="15%"/>
	<col width="22%">
	<col width="12%"/>
	<col width="15%"/>
</colgroup>
<thead>
	<tr class="StartLine">
		<td colspan="6"></td>
	</tr>
	<tr class="ContentsHeadLine">	
		<th>	
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 11>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_HISTORY_OCCURTIME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 11>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_HISTORY_OCCURTIME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_HISTORY_OCCURTIME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>						
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 1>			
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_HISTORY_NAME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">1</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 1>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_HISTORY_NAME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">1</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_HISTORY_NAME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">1</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>			
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 2>		
					<a class="orderDir_Desc">IP
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">2</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 2>	
					<a class="orderDir_Asc">IP
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">2</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">IP
						<img src="imgs/none.gif"/>
						<span class="none orderType">2</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>	
		</th>
		<th>${LANGCODEMAP["MSG_HISTORY_CHANGE_SUMMARY"]!}</th>
		<th>${LANGCODEMAP["MSG_HISTORY_USER"]!}</th>
		<th>Action</th>
	</tr>
	<tr class="StartLine1">
		<td colspan="6"></td>
	</tr>		
</thead>
<tbody>
<#list adcHistoryList![] as theHistory>
	<tr class="ContentsLine1 history-tr">
		<td class="align_center textOver" title=">${(theHistory.lastChangeTime?string("yyyy-MM-dd HH:mm:ss"))!}">
			<span class="none virtualSvrIndex">${theHistory.virtualSvrIndex!}</span><span class="none logSeq">${theHistory.logSeq!}</span>
			<a class="historyDiffLnk" href="#">${(theHistory.lastChangeTime?string("yyyy-MM-dd HH:mm:ss"))!}</a>
		</td>
		<td class="align_left_P10 textOver" title="${theHistory.virtualSvrName!}">
			<a class="historyDiffLnk" href="#">${theHistory.virtualSvrName!}</a>
		</td>
		<td class="align_center">
			<a class="historyDiffLnk">${theHistory.virtualSvrIp!}</a>
		</td>
		<td class="align_left_P10 max40bytes" title="${theHistory.changeSummary!}">${theHistory.changeSummary!}</td>
		<td class="align_center textOver">${theHistory.accountName!}</td>
		<td class="align_center">
			<#if theHistory.isVirtualSvrDeleted!false>			
				<span class="position_R5L5">					
					<img src="imgs/btn/btn_setting_off.png" alt="${LANGCODEMAP["MSG_HISTORY_SETTING"]!}" title="${LANGCODEMAP["MSG_HISTORY_SETTING"]!}"/>
				</span>				
				<span class="position_R5L5">
					<img src="imgs/btn/btn_monitoring_off.png" alt="${LANGCODEMAP["MSG_HISTORY_MONITORING"]!}" title="${LANGCODEMAP["MSG_HISTORY_MONITORING"]!}"/>
				</span>
			<#else>					
				<span class="position_R5L5 settingLnk">					
					<img src="imgs/btn/btn_setting_on.png" alt="${LANGCODEMAP["MSG_HISTORY_SETTING"]!}" title="${LANGCODEMAP["MSG_HISTORY_SETTING"]!}"/>
				</span>
				
				<span class="position_R5L5 monitorLnk">
					<img src="imgs/btn/btn_monitoring_on.png" alt="${LANGCODEMAP["MSG_HISTORY_MONITORING"]!}" title="${LANGCODEMAP["MSG_HISTORY_MONITORING"]!}"/>
				</span>
									
			</#if>	
		</td>
	</tr>
	<tr class="DivideLine history-tr">
		<td colspan="6"></td>			
	</tr>
</#list>
</tbody>
<tr class="EndLine">
	<td colspan="6"></td>
</tr>
	
