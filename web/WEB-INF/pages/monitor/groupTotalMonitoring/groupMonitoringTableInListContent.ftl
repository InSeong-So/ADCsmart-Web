<#setting number_format="0.####"> 

<#if orderType??>
	<#if 1 == orderType></#if><!-- Group이름 -->
	<#if 2 == orderType></#if><!-- Group 인덱스(alteonID) -->
	<#if 3 == orderType></#if><!-- Backup -->
	<#if 4 == orderType></#if><!-- Member -->
	<#if 5 == orderType></#if><!-- Filter정보 -->
	<#if 6 == orderType></#if><!-- Virtual Server -->
	<#if 7 == orderType></#if><!-- ADC이름 -->
	<#if 8 == orderType></#if><!-- 종류-->
	<#if 9 == orderType></#if><!-- ADC IP -->
</#if>
<#if orderDir??>
	<#if 1 == orderDir></#if><!-- Asc -->
	<#if 2 == orderDir></#if><!-- Desc -->
</#if>	

<colgroup>			
	<col width="200px">
	<col width="110px">
	<col width="110px">
	<col width="110px">
	<col width="120px">
	<col width="140px">
	<col width="200px">
	<col width="110">
	<col width="150px">
</colgroup>
<thead>	
	<tr class="ContentsHeadLine selColHead">	
		<th class="bg_row2 default">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 1>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_GROUPNAME"]!}<img src="imgs/Desc.gif"/><span class="none orderType">1</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 1>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_GROUPNAME"]!}<img src="imgs/Asc.gif"/><span class="none orderType">1</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_GROUPNAME"]!}<img src="imgs/none.gif"/><span class="none orderType">1</span><span class="none orderDir">2</span></a>
				</#if>
			</span>			
		</th>
		<th class="default">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 2>
					<a class="orderDir_Desc orderHeader">Group Index<img src="imgs/Desc.gif"/><span class="none orderType">2</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 2>
					<a class="orderDir_Asc orderHeader">Group Index<img src="imgs/Asc.gif"/><span class="none orderType">2</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">Group Index<img src="imgs/none.gif"/><span class="none orderType">2</span><span class="none orderDir">2</span></a>
				</#if>
			</span>					
		</th>
	<#if montotalGrp.condition.backup.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>		
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 3>
					<a class="orderDir_Desc orderHeader">Backup<img src="imgs/Desc.gif"/><span class="none orderType">3</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 3>
					<a class="orderDir_Asc orderHeader">Backup<img src="imgs/Asc.gif"/><span class="none orderType">3</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">Backup<img src="imgs/none.gif"/><span class="none orderType">3</span><span class="none orderDir">2</span></a>
				</#if>
			</span>	
		</th>		
	<#if montotalGrp.condition.member.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 4>
					<a class="orderDir_Desc orderHeader">Member${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Desc.gif"/><span class="none orderType">4</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 4>
					<a class="orderDir_Asc orderHeader">Member${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Asc.gif"/><span class="none orderType">4</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">Member${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/none.gif"/><span class="none orderType">4</span><span class="none orderDir">2</span></a>
				</#if>
			</span>	
		</th>		
	<#if montotalGrp.condition.filter.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 5>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_FILTERINFO"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Desc.gif"/><span class="none orderType">5</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 5>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_FILTERINFO"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Asc.gif"/><span class="none orderType">5</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_FILTERINFO"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/none.gif"/><span class="none orderType">5</span><span class="none orderDir">2</span></a>
				</#if>
			</span>	
		</th>		
	<#if montotalGrp.condition.vsAssigned.isSelect()>
		<th>
	<#else>			
		<th class="none">
	</#if>				
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 6>
					<a class="orderDir_Desc orderHeader">Virtual Server${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Desc.gif"/><span class="none orderType">6</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 6>
					<a class="orderDir_Asc orderHeader">Virtual Server${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Asc.gif"/><span class="none orderType">6</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">Virtual Server${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/none.gif"/><span class="none orderType">6</span><span class="none orderDir">2</span></a>
				</#if>
			</span>	
		</th>			
	<#if montotalGrp.condition.adcName.isSelect()>
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
	<#if montotalGrp.condition.adcType.isSelect()>	
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
	<#if montotalGrp.condition.adcIp.isSelect()>	
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
	</tr>
</thead>		
<tbody>
	<tr class="ContentsLine1 groupList none">
		<td class="align_left_P10 textOver name"></td>
		<td class="align_center id"></td>     
	<#if montotalGrp.condition.backup.isSelect()>
		<td class="align_center backup"></td>
	<#else>
		<td class="align_center backup none"></td>
	</#if>  
	<#if montotalGrp.condition.member.isSelect()>
		<td class="align_center member"></td>
	<#else>
		<td class="align_center member none"></td>
	</#if>
	<#if montotalGrp.condition.filter.isSelect()>
		<td class="align_center filter"></td>
	<#else>
		<td class="align_center filter none"></td>
	</#if>	 
	<#if montotalGrp.condition.vsAssigned.isSelect()>
		<td class="align_center vsAssigned"></td>
	<#else>
		<td class="align_center vsAssigned none"></td>
	</#if>
	<#if montotalGrp.condition.adcName.isSelect()>
		<td class="align_left_P10 textOver adcName"></td>
	<#else>
		<td class="align_left_P10 textOver adcName none"></td>
	</#if>
	<#if montotalGrp.condition.adcType.isSelect()>	
		<td class="align_center adcType"></td>
	<#else>
		<td class="align_center adcType none"></td>
	</#if>
	<#if montotalGrp.condition.adcIp.isSelect()>	
		<td class="align_left_P10 adcIp"></td>
	<#else>
		<td class="align_left_P10 adcIp none"></td>
	</#if>  
	</tr>
	<#list montotalGrp.groupList![] as theGroupList>
	<tr class="ContentsLine1 groupList">		
		<td class="align_left_P10 textOver name" title="${(theGroupList.name)!''}">${(theGroupList.name)!''}</td>
		<td class="align_center id">
			<#if (theGroupList.id)??>			
				${(theGroupList.id)!''}
			<#else>
				-
			</#if>
		</td>                      
		<td class="align_center backup">
			<#if (theGroupList.backup)??>
				${(theGroupList.backup)!''}
			<#else>
				-
			</#if>
		</td>		            
	<#if montotalGrp.condition.member.isSelect()>
		<td class="align_center member">
	<#else>
		<td class="align_center member none">
	</#if>
			<#if (theGroupList.member) == -1>
				-
			<#else>
				${(theGroupList.member)!''}
			</#if>		
		</td>
	<#if montotalGrp.condition.filter.isSelect()>
		<td class="align_center filter">
	<#else>
		<td class="align_center filter none">
	</#if> 
			<#if (theGroupList.filter) == -1>
				-
			<#else>
				${(theGroupList.filter)!''}
			</#if>		
		</td>
	<#if montotalGrp.condition.vsAssigned.isSelect()>
		<td class="align_center vsAssigned">
	<#else>
		<td class="align_center vsAssigned none">
	</#if>           
			<#if (theGroupList.vsAssigned) == -1>
				-
			<#else>
				${(theGroupList.vsAssigned)!''}
			</#if> 
		</td>          
	<#if montotalGrp.condition.adcName.isSelect()>
		<td class="align_left_P10 textOver adcName" title="${(theGroupList.adcName)!''}">
	<#else>
		<td class="align_left_P10 textOver adcName none" title="${(theGroupList.adcName)!''}">
	</#if>
			<a href="javascript:;" class="adcMonitoringLnk"><span style="display:none;">${(theGroupList.adcIndex)!''}</span>${(theGroupList.adcName)!''}</a>
		</td>
	<#if montotalGrp.condition.adcType.isSelect()>
		<td class="align_center adcType">
	<#else>
		<td class="align_center adcType none">
	</#if>
			<#if (theGroupList.adcType) == 1>
				<img src="imgs/icon/adc/icon_f5_s.png" alt="f5" />	
			<#elseif (theGroupList.adcType) == 2>
				<img src="imgs/icon/adc/icon_alteon_s.png" alt="alteon" />
			<#elseif (theGroupList.adcType) == 3>
				<img src="imgs/icon/adc/icon_piolink_s.png" alt="PiolinkUnknown" />	
			<#elseif (theGroupList.adcType) == 4>
				<img src="imgs/icon/adc/icon_piolink_s.png" alt="PiolinkUnknown" />	
			<#else>
				Unknown
			</#if>
		</td>
	<#if montotalGrp.condition.adcIp.isSelect()>
		<td class="align_left_P10 adcIp">
	<#else>
		<td class="align_left_P10 adcIp none">
	</#if>
		${(theGroupList.adcIp)!''}
		</td>      
	</tr>
	</#list>	
</tbody>
			
