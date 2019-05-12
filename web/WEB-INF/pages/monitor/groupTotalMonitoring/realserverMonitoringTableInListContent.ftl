<#setting number_format="0.####"> 

<#if orderType??>
	<#if 1 == orderType></#if><!-- 상태 -->
	<#if 2 == orderType></#if><!-- State -->
	<#if 3 == orderType></#if><!-- Real Server 이름 -->
	<#if 4 == orderType></#if><!-- Real Server IP-->
	<#if 5 == orderType></#if><!-- 사용 여부-->
	<#if 6 == orderType></#if><!-- 그룹 개수-->
	<#if 7 == orderType></#if><!-- ADC이름 -->
	<#if 8 == orderType></#if><!-- 종류-->
	<#if 9 == orderType></#if><!-- ADC IP -->
	<#if 10 == orderType></#if><!-- Ratio -->
	<#if 11 == orderType></#if><!-- AlteonId -->
</#if>
<#if orderDir??>
	<#if 1 == orderDir></#if><!-- Asc -->
	<#if 2 == orderDir></#if><!-- Desc -->
</#if>	

<colgroup>			
	<col width="60px" />
	<col width="130px" />
	<col width="180px" />
	<col width="130px" />
	<col width="130px" />
	<col width="100px" />
	<col width="100px" />
	<col width="120px" />			
	<col width="120px" />
	<col width="120px" />
	<col width="100px" />
</colgroup>
<thead>
	<tr class="ContentsHeadLine selColHead">
		<th class="bg_row2 default">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 1>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_ADCSETTING_STATUS"]!}<img src="imgs/Desc.gif"/><span class="none orderType">1</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 1>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_ADCSETTING_STATUS"]!}<img src="imgs/Asc.gif"/><span class="none orderType">1</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_ADCSETTING_STATUS"]!}<img src="imgs/none.gif"/><span class="none orderType">1</span><span class="none orderDir">2</span></a>
				</#if>
			</span>			
		</th>
		<th class="default">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 2>
					<a class="orderDir_Desc orderHeader">State<img src="imgs/Desc.gif"/><span class="none orderType">2</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 2>
					<a class="orderDir_Asc orderHeader">State<img src="imgs/Asc.gif"/><span class="none orderType">2</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">State<img src="imgs/none.gif"/><span class="none orderType">2</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		</th>
		<th class="default">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 11>
					<a class="orderDir_Desc orderHeader">Real Server Index<img src="imgs/Desc.gif"/><span class="none orderType">11</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 11>
					<a class="orderDir_Asc orderHeader">Real Server Index<img src="imgs/Asc.gif"/><span class="none orderType">11</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">Real Server Index<img src="imgs/none.gif"/><span class="none orderType">11</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		</th>
		<th class="default">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 3>
					<a class="orderDir_Desc orderHeader">Real Server IP<img src="imgs/Desc.gif"/><span class="none orderType">3</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 3>
					<a class="orderDir_Asc orderHeader">Real Server IP<img src="imgs/Asc.gif"/><span class="none orderType">3</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">Real Server IP<img src="imgs/none.gif"/><span class="none orderType">3</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		</th>
		<th class="default">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 4>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_REALNAME"]!}<img src="imgs/Desc.gif"/><span class="none orderType">4</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 4>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_REALNAME"]!}<img src="imgs/Asc.gif"/><span class="none orderType">4</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_REALNAME"]!}<img src="imgs/none.gif"/><span class="none orderType">4</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		</th>	
	<#if montotalRs.condition.used.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>		
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 5>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_USEFLAG"]!}<img src="imgs/Desc.gif"/><span class="none orderType">5</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 5>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_USEFLAG"]!}<img src="imgs/Asc.gif"/><span class="none orderType">5</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_USEFLAG"]!}<img src="imgs/none.gif"/><span class="none orderType">5</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		</th>
	<#if montotalRs.condition.group.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 6>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_GROUPCOUNT"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Desc.gif"/><span class="none orderType">6</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 6>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_GROUPCOUNT"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Asc.gif"/><span class="none orderType">6</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_GROUPCOUNT"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/none.gif"/><span class="none orderType">6</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		</th>	
	<#if montotalRs.condition.adcName.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>	
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 7>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_ADCNAME"]!}<img src="imgs/Desc.gif"/><span class="none orderType">7</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 7>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_ADCNAME"]!}<img src="imgs/Asc.gif"/><span class="none orderType">7</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_ADCNAME"]!}<img src="imgs/none.gif"/><span class="none orderType">7</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		</th>
	<#if montotalRs.condition.adcType.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>	
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 8>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_TYPE"]!}<img src="imgs/Desc.gif"/><span class="none orderType">8</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 8>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_TYPE"]!}<img src="imgs/Asc.gif"/><span class="none orderType">8</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_TYPE"]!}<img src="imgs/none.gif"/><span class="none orderType">8</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		</th>
	<#if montotalRs.condition.adcIp.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 9>
					<a class="orderDir_Desc orderHeader">ADC IP<img src="imgs/Desc.gif"/><span class="none orderType">9</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 9>
					<a class="orderDir_Asc orderHeader">ADC IP<img src="imgs/Asc.gif"/><span class="none orderType">9</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">ADC IP<img src="imgs/none.gif"/><span class="none orderType">9</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		</th>
	<#if montotalRs.condition.ratio.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 10>
					<a class="orderDir_Desc orderHeader">Ratio<img src="imgs/Desc.gif"/><span class="none orderType">10</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 10>
					<a class="orderDir_Asc orderHeader">Ratio<img src="imgs/Asc.gif"/><span class="none orderType">10</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">Ratio<img src="imgs/none.gif"/><span class="none orderType">10</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		</th>
	</tr>
</thead>		
<tbody>
	<tr class="ContentsLine1 realserverList none">
		<td class="align_center status"></td>
		<td class="align_center state"></td>
		<td class="align_left_P10 alteonId"></td>
		<td class="align_left_P10 ip"></td>		
		<td class="align_left_P10 textOver name"></td> 
	<#if montotalRs.condition.used.isSelect()>
		<td class="align_center used"></td>
	<#else>			
		<td class="align_center used none"></td>
	</#if>    		                  
	<#if montotalRs.condition.group.isSelect()>
		<td class="align_center group"></td>
	<#else>			
		<td class="align_center group none"></td>
	</#if>    
	<#if montotalRs.condition.adcName.isSelect()>
		<td class="align_left_P10 textOver adcName"></td> 
	<#else>			
		<td class="align_left_P10 textOver adcName none"></td> 
	</#if>  
	<#if montotalRs.condition.adcType.isSelect()>
		<td class="align_center adcType"></td>
	<#else>			
		<td class="align_center adcType none"></td>
	</#if>  		
	<#if montotalRs.condition.adcIp.isSelect()>
		<td class="align_left_P10 adcIp"></td>
	<#else>			
		<td class="align_left_P10 adcIp none"></td>
	</#if>    
	<#if montotalRs.condition.ratio.isSelect()>
		<td class="align_center ratio"></td>
	<#else>			
		<td class="align_center ratio none"></td>
	</#if>   		      
	</tr>
	<#list montotalRs.realList![] as theRsList>
	<tr class="ContentsLine1 realserverList">
		<#if montotalRs.condition.status.isSelect()>				
		<td class="align_center status">
			<#if theRsList.status == -1>
				<img src="imgs/icon/icon_2d_0.png" />
			<#elseif theRsList.status == 1>
				<img src="imgs/icon/icon_2d_1.png" alt="available" />
			<#elseif theRsList.status == 0>
				<img src="imgs/icon/icon_2d_2.png" alt="unavailable" />	
			<#elseif theRsList.status == 2>
				<img src="imgs/icon/icon_2d_0.png" alt="disable" />					
			<#else>
				<img src="imgs/icon/icon_2d_0.png" />					
			</#if>
		</td>
		</#if>
		<td class="align_center state">
			<#if theRsList.state == 1>
				Enabled
			<#elseif theRsList.state == 0>
				Disabled
			<#elseif theRsList.state == 2>
				Forced Offline			
			<#else>						
			</#if>	
		</td>       
		<td class="align_left_P10 ip">${(theRsList.alteonId)!''}</td>
		<td class="align_left_P10 ip">${(theRsList.ip)!''}</td>
		<td class="align_left_P10 textOver name" title="${(theRsList.name)!''}">
			<#if (theRsList.name)??>
				${(theRsList.name)!'-'}
			<#else>
				-
			</#if>
		</td>
		<td class="align_center used">
			<#if (theRsList.used) == 1>
				${LANGCODEMAP["MSG_SYSSETTING_USE"]!}
			<#else>
				${LANGCODEMAP["MSG_CONSTANT_NOUSE"]!}
			</#if>
		</td>       
	<#if montotalRs.condition.group.isSelect()>
		<td class="align_center group">
	<#else>			
		<td class="align_center group none">
	</#if>  
			<#if (theRsList.group) == -1>			
				-
			<#else>
				${(theRsList.group)!''}
			</#if>
		</td>
	<#if montotalRs.condition.adcName.isSelect()>
		<td class="align_left_P10 textOver adcName" title="${(theRsList.adcName)!''}">
	<#else>			
		<td class="align_left_P10 textOver adcName none" title="${(theRsList.adcName)!''}">
	</#if>
			<a href="javascript:;" class="adcMonitoringLnk"><span style="display:none;">${(theRsList.adcIndex)!''}</span>${(theRsList.adcName)!''}</a>
		</td> 
	<#if montotalRs.condition.adcType.isSelect()>
		<td class="align_center adcType">
	<#else>			
		<td class="align_center adcType none">
	</#if>
			<#if (theRsList.adcType) == 1>
				<img src="imgs/icon/adc/icon_f5_s.png" alt="f5" />	
			<#elseif (theRsList.adcType) == 2>
				<img src="imgs/icon/adc/icon_alteon_s.png" alt="alteon" />
			<#elseif (theRsList.adcType) == 3>
				<img src="imgs/icon/adc/icon_piolink_s.png" alt="PiolinkUnknown" />	
			<#elseif (theRsList.adcType) == 4>
				<img src="imgs/icon/adc/icon_piolink_s.png" alt="PiolinkUnknown" />	
			<#else>
				Unknown
			</#if>
		</td> 
	<#if montotalRs.condition.adcIp.isSelect()>
		<td class="align_left_P10 adcIp">
	<#else>			
		<td class="align_left_P10 adcIp none">
	</#if>		
			<!--<a href="javascript:;" class="adcMonitoringLnk"><span style="display:none;">${(theRsList.adcIndex)!''}</span>${(theRsList.adcIp)!''}</a>-->
			${(theRsList.adcIp)!''}
		</td> 
	<#if montotalRs.condition.ratio.isSelect()>
		<td class="align_center ratio">
	<#else>			
		<td class="align_center ratio none">
	</#if>
			<#if (theRsList.ratio) == -1>			
				-
			<#else>
				${(theRsList.ratio)!''}
			</#if>		
		</td>  
	</tr>
	</#list>	
</tbody>
