<#setting number_format="0.####"> 

<#if orderType??>
	<#if 1 == orderType></#if><!-- 상태 -->	
	<#if 2 == orderType></#if><!-- VSIP -->
	<#if 3 == orderType></#if><!-- 포트 -->
	<#if 4 == orderType></#if><!-- VS이름 -->
	<#if 5 == orderType></#if><!-- bpsIn -->
	<#if 6 == orderType></#if><!-- bpsOut -->
	<#if 7 == orderType></#if><!-- bpsTotal -->
	<#if 8 == orderType></#if><!-- Concurrent Sessions -->
	<#if 9 == orderType></#if><!-- ADC이름 -->
	<#if 10 == orderType></#if><!-- 종류 -->
	<#if 11 == orderType></#if><!-- ADC IP -->
	<#if 12 == orderType></#if><!-- Member -->
	<#if 13 == orderType></#if><!-- 그룹 이름 -->
	<#if 14 == orderType></#if><!-- LoadBalancing -->
	<#if 15 == orderType></#if><!-- Health Check -->
	<#if 16 == orderType></#if><!-- Persistence -->
	<#if 17 == orderType></#if><!-- Notice Group 여부 -->
	<#if 18 == orderType></#if><!-- 최근 업데이트  -->
	<#if 19 == orderType></#if><!-- 설정이력 -->
</#if>
<#if orderDir??>
	<#if 1 == orderDir></#if><!-- Asc -->
	<#if 2 == orderDir></#if><!-- Desc -->
</#if>	

<colgroup>			
	<col width="60px" data-colidx="-1" />
	<col width="60px" data-colidx="0" />
	<col width="140px" data-colidx="1" />
	<col width="60px" data-colidx="1" />
	<col width="200px" data-colidx="1" />
	<col width="80px"  data-colidx="2"/>
	<col width="80px"  data-colidx="2"/>
	<col width="80px" data-colidx="2"/>
	<col width="120px" data-colidx="3"/>
	<col width="200px" data-colidx="4"/>
	<col width="80px" data-colidx="5"/>
	<!--<col width="150px" data-colidx="6"/>-->
	<!--<col width="150px" data-colidx="7"/>-->
	<!--<col width="150px" data-colidx="8"/>-->
	<!--<col width="130px" data-colidx="9"/>-->
	<!--<col width="110px" data-colidx="10"/>-->
	<!--<col width="130px" data-colidx="11"/>-->
	<!--<col width="140px" data-colidx="12"/>-->
	<!--<col width="130px" data-colidx="13"/>-->
	<!--<col width="130px" data-colidx="14"/>-->
</colgroup>
<thead>
	<tr class="ContentsHeadLine ContentsHeadLineOrder">
		<th rowspan="3" class="default selChk" data-colidx="-1">
			<input class="allVsChk" type="checkbox" disabled="disabled" />
		</th>
		<th rowspan="3" class="default" data-colidx="0">
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
		<th colspan="3" class="default " data-colidx="1">
			Virtual Server
		</th>
		
		<th colspan="3" class="default " data-colidx="2">
			bps
		</th>
		
		<th rowspan="3" class="default " data-colidx="3">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 8>
					<a class="orderDir_Desc orderHeader">Concurrent<img src="imgs/Desc.gif"/><span class="none orderType">8</span><span class="none orderDir">1</span><br>Sessions(${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!})</a>
				<#elseif orderDir == 1 && orderType == 8>
					<a class="orderDir_Asc orderHeader">Concurrent<img src="imgs/Asc.gif"/><span class="none orderType">8</span><span class="none orderDir">2</span><br>Sessions(${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!})</a>
				<#else>
					<a class="orderDir_None orderHeader">Concurrent<img src="imgs/none.gif"/><span class="none orderType">8</span><span class="none orderDir">2</span><br>Sessions(${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!})</a>
				</#if>
			</span>				 
		</th>
	<#if montotalSvc.condition.adcName.isSelect()>
		<th rowspan="3" class="" data-colidx="4">
	<#else>			
		<th rowspan="3" class=" none" data-colidx="4">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 9>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_ADCNAME"]!}<img src="imgs/Desc.gif"/><span class="none orderType">9</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 9>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_ADCNAME"]!}<img src="imgs/Asc.gif"/><span class="none orderType">9</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_ADCNAME"]!}<img src="imgs/none.gif"/><span class="none orderType">9</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		</th>
	<#if montotalSvc.condition.adcType.isSelect()>
		<th rowspan="3" class="" data-colidx="5">
	<#else>			
		<th rowspan="3" class=" none" data-colidx="5">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 10>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_TYPE"]!}<img src="imgs/Desc.gif"/><span class="none orderType">10</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 10>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_TYPE"]!}<img src="imgs/Asc.gif"/><span class="none orderType">10</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_TYPE"]!}<img src="imgs/none.gif"/><span class="none orderType">10</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		</th>
	<#if montotalSvc.condition.adcIp.isSelect()>
		<th rowspan="3" class="" data-colidx="6">
	<#else>			
		<th rowspan="3" class=" none" data-colidx="6">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 11>
					<a class="orderDir_Desc orderHeader">ADC IP<img src="imgs/Desc.gif"/><span class="none orderType">11</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 11>
					<a class="orderDir_Asc orderHeader">ADC IP<img src="imgs/Asc.gif"/><span class="none orderType">11</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">ADC IP<img src="imgs/none.gif"/><span class="none orderType">1</span><span class="none orderDir">2</span></a>
				</#if>
			</span>			
		</th>
	<#if montotalSvc.condition.member.isSelect()>
		<th rowspan="3" data-colidx="7">
	<#else>			
		<th rowspan="3" class=" none" data-colidx="7">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 12>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_MEMBERINFO"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Desc.gif"/><span class="none orderType">12</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 12>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_MEMBERINFO"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Asc.gif"/><span class="none orderType">12</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_MEMBERINFO"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/none.gif"/><span class="none orderType">12</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		</th>	
	<#if montotalSvc.condition.group.isSelect()>
		<th rowspan="3" data-colidx="8">
	<#else>			
		<th rowspan="3" class=" none" data-colidx="8">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 13>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_GROUPNAMES"]!}<img src="imgs/Desc.gif"/><span class="none orderType">13</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 13>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_GROUPNAMES"]!}<img src="imgs/Asc.gif"/><span class="none orderType">13</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_GROUPNAMES"]!}<img src="imgs/none.gif"/><span class="none orderType">13</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		</th>
	<#if montotalSvc.condition.loadbalancing.isSelect()>
		<th rowspan="3" data-colidx="9">
	<#else>			
		<th rowspan="3" class=" none" data-colidx="9">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 14>
					<a class="orderDir_Desc orderHeader">Load Balancing<img src="imgs/Desc.gif"/><span class="none orderType">14</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 14>
					<a class="orderDir_Asc orderHeader">Load Balancing<img src="imgs/Asc.gif"/><span class="none orderType">14</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">Load Balancing<img src="imgs/none.gif"/><span class="none orderType">14</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		</th>
	<#if montotalSvc.condition.healthCheck.isSelect()>
		<th rowspan="3" data-colidx="10">
	<#else>			
		<th rowspan="3" class=" none" data-colidx="10">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 15>
					<a class="orderDir_Desc orderHeader">Health Check<img src="imgs/Desc.gif"/><span class="none orderType">15</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 15>
					<a class="orderDir_Asc orderHeader">Health Check<img src="imgs/Asc.gif"/><span class="none orderType">15</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">Health Check<img src="imgs/none.gif"/><span class="none orderType">15</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		</th>
	<#if montotalSvc.condition.persistence.isSelect()>
		<th rowspan="3" data-colidx="11">
	<#else>			
		<th rowspan="3" class=" none" data-colidx="11">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 16>
					<a class="orderDir_Desc orderHeader">Persistence<img src="imgs/Desc.gif"/><span class="none orderType">16</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 16>
					<a class="orderDir_Asc orderHeader">Persistence<img src="imgs/Asc.gif"/><span class="none orderType">16</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">Persistence<img src="imgs/none.gif"/><span class="none orderType">16</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		</th>
	<#if montotalSvc.condition.noticeGroup.isSelect()>
		<th rowspan="3" data-colidx="12">
	<#else>			
		<th rowspan="3" class=" none" data-colidx="12">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 17>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_NOTICEGROUPFLAG"]!}<img src="imgs/Desc.gif"/><span class="none orderType">17</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 17>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_NOTICEGROUPFLAG"]!}<img src="imgs/Asc.gif"/><span class="none orderType">17</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_NOTICEGROUPFLAG"]!}<img src="imgs/none.gif"/><span class="none orderType">17</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		</th>
	<#if montotalSvc.condition.updateTime.isSelect()>
		<th rowspan="3" data-colidx="13">
	<#else>			
		<th rowspan="3" class=" none" data-colidx="13">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 18>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_LASTUPDATE"]!}<img src="imgs/Desc.gif"/><span class="none orderType">18</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 18>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_LASTUPDATE"]!}<img src="imgs/Asc.gif"/><span class="none orderType">18</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_LASTUPDATE"]!}<img src="imgs/none.gif"/><span class="none orderType">18</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		 </th>
	<#if montotalSvc.condition.configHistory.isSelect()>
		<th rowspan="3" data-colidx="14">
	<#else>			
		<th rowspan="3" class=" none" data-colidx="14">
	</#if>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 19>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_SETTTING_HISTORY"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!})<img src="imgs/Desc.gif"/><span class="none orderType">19</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 19>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_SETTTING_HISTORY"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/Asc.gif"/><span class="none orderType">19</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_GROUPMONITOR_SETTTING_HISTORY"]!}${LANGCODEMAP["MSG_GROUPMONITOR_EA"]!}<img src="imgs/none.gif"/><span class="none orderType">19</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		</th>
	</tr>
	<tr class="ContentsHeadLine1 ContentsHeadLineOrder">	
		<th class="default" data-colidx="1">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 2>
					<a class="orderDir_Desc orderHeader">IP<img src="imgs/Desc.gif"/><span class="none orderType">2</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 2>
					<a class="orderDir_Asc orderHeader">IP<img src="imgs/Asc.gif"/><span class="none orderType">2</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">IP<img src="imgs/none.gif"/><span class="none orderType">2</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
			<span class="vsIp none">VS IP</span>
		</th>
		<th class="default" data-colidx="1">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 3>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_PERFALTEON_PORT"]!}<img src="imgs/Desc.gif"/><span class="none orderType">3</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 3>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_PERFALTEON_PORT"]!}<img src="imgs/Asc.gif"/><span class="none orderType">3</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_PERFALTEON_PORT"]!}<img src="imgs/none.gif"/><span class="none orderType">3</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		</th>
		<th class="default Rcolor" data-colidx="1">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 4>
					<a class="orderDir_Desc orderHeader">${LANGCODEMAP["MSG_PERFALTEON_NAME"]!}<img src="imgs/Desc.gif"/><span class="none orderType">4</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 4>
					<a class="orderDir_Asc orderHeader">${LANGCODEMAP["MSG_PERFALTEON_NAME"]!}<img src="imgs/Asc.gif"/><span class="none orderType">4</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">${LANGCODEMAP["MSG_PERFALTEON_NAME"]!}<img src="imgs/none.gif"/><span class="none orderType">4</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		</th>
		<th class="default" data-colidx="2">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 5>
					<a class="orderDir_Desc orderHeader">In<img src="imgs/Desc.gif"/><span class="none orderType">5</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 5>
					<a class="orderDir_Asc orderHeader">In<img src="imgs/Asc.gif"/><span class="none orderType">5</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">In<img src="imgs/none.gif"/><span class="none orderType">5</span><span class="none orderDir">2</span></a>
				</#if>
			</span>			
		</th>
		<th class="default" data-colidx="2">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 6>
					<a class="orderDir_Desc orderHeader">Out<img src="imgs/Desc.gif"/><span class="none orderType">6</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 6>
					<a class="orderDir_Asc orderHeader">Out<img src="imgs/Asc.gif"/><span class="none orderType">6</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">Out<img src="imgs/none.gif"/><span class="none orderType">6</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		</th>
		<th class="default" data-colidx="2">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 7>
					<a class="orderDir_Desc orderHeader">Total<img src="imgs/Desc.gif"/><span class="none orderType">7</span><span class="none orderDir">1</span></a>
				<#elseif orderDir == 1 && orderType == 7>
					<a class="orderDir_Asc orderHeader">Total<img src="imgs/Asc.gif"/><span class="none orderType">7</span><span class="none orderDir">2</span></a>
				<#else>
					<a class="orderDir_None orderHeader">Total<img src="imgs/none.gif"/><span class="none orderType">7</span><span class="none orderDir">2</span></a>
				</#if>
			</span>
		</th>			
	</tr>
	
</thead>		
<tbody>
	<tr class="ContentsLine1 serviceList none">		
		<td class="align_center index vsChk" data-colidx="-1"></td>
		<td class="align_center status" data-colidx="0"></td>
		<td class="align_left_P10 ip nborder" data-colidx="1">IP</td>
		<td class="align_center port nborder" data-colidx="1">${LANGCODEMAP["MSG_PERFALTEON_PORT"]!}</td>
		<td class="align_left_P10 textOver name" data-colidx="1">${LANGCODEMAP["MSG_GROUPMONITOR_NAME"]!}</td>		
		<td class="align_center bpsIn nborder" data-colidx="2">In</td>
		<td class="align_center bpsOut nborder" data-colidx="2">OUt</td>
		<td class="align_center bpsTotal" data-colidx="2">${LANGCODEMAP["MSG_GROUPMONITOR_TOTAL"]!}</td>
	<#if montotalSvc.condition.concurrentSession.isSelect()>
		<td class="align_center concurrentSession" data-colidx="3"></td>
	<#else>			
		<td class="align_center concurrentSession none" data-colidx=3"></td>
	</#if>
	<#if montotalSvc.condition.adcName.isSelect()>
		<td class="align_left_P10 textOver adcName" data-colidx="4"></td>
	<#else>			
		<td class="align_left_P10 textOver adcName none" data-colidx="4"></td>
	</#if>
	<#if montotalSvc.condition.adcType.isSelect()>
		<td class="align_center adcType" data-colidx="5"></td>
	<#else>			
		<td class="align_center adcType none" data-colidx="5"></td>
	</#if>
	<#if montotalSvc.condition.adcIp.isSelect()>
		<td class="align_left_P10 adcIp" data-colidx="6"></td>
	<#else>			
		<td class="align_left_P10 adcIp none" data-colidx="6"></td>
	</#if>
	<#if montotalSvc.condition.member.isSelect()>
		<td class="align_center member" data-colidx="7"></td>
	<#else>			
		<td class="align_center member none" data-colidx="7"></td>
	</#if>	
	<#if montotalSvc.condition.group.isSelect()>
		<td class="align_left_P10 textOver groupName" data-colidx="8"></td>
	<#else>			
		<td class="align_left_P10 textOver groupName none" data-colidx="8"></td>
	</#if>
	<#if montotalSvc.condition.loadbalancing.isSelect()>
		<td class="align_center loadbalancing" data-colidx="9"></td>
	<#else>			
		<td class="align_center loadbalancing none" data-colidx="9"></td>
	</#if>
	<#if montotalSvc.condition.healthCheck.isSelect()>
		<td class="align_center healthCheck" data-colidx="10"></td>
	<#else>			
		<td class="align_center healthCheck none" data-colidx="10"></td>
	</#if>
	<#if montotalSvc.condition.persistence.isSelect()>
		<td class="align_center persistence" data-colidx="11"></td>
	<#else>			
		<td class="align_center persistence none" data-colidx="11"></td>
	</#if>
	<#if montotalSvc.condition.noticeGroup.isSelect()>
		<td class="align_center textOver noticeGroup " data-colidx="12"></td>
	<#else>			
		<td class="align_center textOver noticeGroup none" data-colidx="12"></td>
	</#if>
	<#if montotalSvc.condition.updateTime.isSelect()>
		<td class="align_center updateTime" data-colidx="13"></td>
	<#else>			
		<td class="align_center updateTime none" data-colidx="13"></td>
	</#if>
	<#if montotalSvc.condition.configHistory.isSelect()>
		<td class="align_center slbConfig24Hour" data-colidx="14"></td>
	<#else>			
		<td class="align_center slbConfig24Hour none" data-colidx="14"></td>
	</#if>	  
	</tr>
	<#list montotalSvc.serviceList![] as theSvcList>
	<tr class="ContentsLine1 serviceList">		
		<td class="align_center vsChk" data-colidx="-1" data-index="${theSvcList_index}">
			<input class="vsColChk" id="node" type="checkbox" value="${theSvcList.index}"/>
		</td>
		<td class="align_center status" data-colidx="0">
			<#if theSvcList.status == -1>
				<img src="imgs/icon/icon_yellowDot.png" />
			<#elseif theSvcList.status == 0>
				<img src="imgs/icon/icon_vs_disabled.png" alt="disable" />
			<#elseif theSvcList.status == 1>
				<img src="imgs/icon/icon_vs_conn.png" alt="available" />				
			<#elseif theSvcList.status == 2>
				<img src="imgs/icon/icon_vs_disconn.png" alt="unavailable" />					
			<#else>
				<img src="imgs/icon/icon_yellowDot.png" />					
			</#if>
		</td>	                   
		<td class="align_left_P10 ip nborder" data-colidx="1">
			<a href="javascript:;" class="serviceMonitoringLnk"><span style="display:none;">${(theSvcList.adcIndex)!''}</span><span style="display:none;">2</span><span class="vsIp">${(theSvcList.ip)!''}</span></a>
		</td>            
		<td class="align_center port nborder" data-colidx="1">${(theSvcList.port?replace("999", ", "))!''}</td>
		<td class="align_left_P10 textOver name" title="${(theSvcList.name)!''}" data-colidx="1">${(theSvcList.name)!''}</td>	
		<td class="align_center bpsIn nborder" data-colidx="2">
			<#if (theSvcList.bpsIn) == -1>
				-
			<#else>
				${(theSvcList.bpsInUnit)!''}
			</#if>
		</td>  
		<td class="align_center bpsOut nborder" data-colidx="2">
			<#if (theSvcList.bpsOut) == -1>
				-
			<#else>
				${(theSvcList.bpsOutUnit)!''}
			</#if>
		</td>  
		<td class="align_center bpsTotal" data-colidx="2">   					
			<#if (theSvcList.bpsTotal) == -1>
				-
			<#else>
				${(theSvcList.bpsTotalUnit)!''}
			</#if>		
		</td>  		   
	<#if montotalSvc.condition.concurrentSession.isSelect()>
		<td class="align_center concurrentSession" data-colidx="3">
	<#else>			
		<td class="align_center concurrentSession none" data-colidx="3">
	</#if>  
			<#if (theSvcList.concurrentSession) == -1>
				-
			<#else>
				${(theSvcList.concurrentSessionUnit)!''}
			</#if>		
		</td>
	<#if montotalSvc.condition.adcName.isSelect()>
		<td class="align_left_P10 textOver adcName" title="${(theSvcList.adcName)!}" data-colidx="4">
	<#else>			
		<td class="align_left_P10 textOver adcName none" title="${(theSvcList.adcName)!}" data-colidx="4">
	</#if>    
			<a href="javascript:;" class="adcMonitoringLnk"><span style="display:none;">${(theSvcList.adcIndex)!''}</span>${(theSvcList.adcName)!''}</a>
		</td> 
	<#if montotalSvc.condition.adcType.isSelect()>
		<td class="align_center adcType" data-colidx="5">
	<#else>			
		<td class="align_center adcType none" data-colidx="5">
	</#if>   
			<span style="display:none;">${(theSvcList.adcType)!''}</span>
			<#if (theSvcList.adcType) == 1>
				<img src="imgs/icon/adc/icon_f5_s.png" alt="f5" />	
			<#elseif (theSvcList.adcType) == 2>
				<img src="imgs/icon/adc/icon_alteon_s.png" alt="alteon" />
			<#elseif (theSvcList.adcType) == 3>
				<img src="imgs/icon/adc/icon_piolink_s.png" alt="PiolinkUnknown" />	
			<#elseif (theSvcList.adcType) == 4>
				<img src="imgs/icon/adc/icon_piolink_s.png" alt="PiolinkUnknown" />	
			<#else>
				Unknown
			</#if>
		</td>
	<#if montotalSvc.condition.adcIp.isSelect()>
		<td class="align_left_P10 adcIp" data-colidx="6">
	<#else>			
		<td class="align_left_P10 adcIp none" data-colidx="6">
	</#if>     
		${(theSvcList.adcIp)!''}
		</td> 
	<#if montotalSvc.condition.member.isSelect()>
		<td class="align_center member" data-colidx="7">
	<#else>			
		<td class="align_center member none" data-colidx="7">
	</#if>  
			<#if (theSvcList.member) == -1>
				-
			<#else>
				${(theSvcList.member)!''}
			</#if>		
		</td>    
	<#if montotalSvc.condition.group.isSelect()>
		<td class="align_left_P10 textOver groupName" title="${(theSvcList.groupName)!''}" data-colidx="8">
	<#else>			
		<td class="align_left_P10 textOver groupName none" title="${(theSvcList.groupName)!''}" data-colidx="8">
	</#if>       
			<#if (theSvcList.groupName)??>				
				${(theSvcList.groupName)!''}			
			<#else>
				-
			</#if>
		</td>	        
	<#if montotalSvc.condition.loadbalancing.isSelect()>
		<td class="align_center loadbalancing" data-colidx="9">
	<#else>			
		<td class="align_center loadbalancing none" data-colidx="9">
	</#if>    
			<#if (theSvcList.loadbalancing)??>				
				${(theSvcList.loadbalancing)!''}			
			<#else>
				-
			</#if>
		</td>
	<#if montotalSvc.condition.healthCheck.isSelect()>
		<td class="align_center healthCheck" data-colidx="10">
	<#else>			
		<td class="align_center healthCheck none" data-colidx="10">
	</#if>
			<#if (theSvcList.healthCheck)??>				
				${(theSvcList.healthCheck)!''}			
			<#else>
				-
			</#if>	
		</td>
	<#if montotalSvc.condition.persistence.isSelect()>
		<td class="align_center persistence" data-colidx="11">
	<#else>			
		<td class="align_center persistence none" data-colidx="11">
	</#if>		
			<#if (theSvcList.adcType) == 1>
				<#if (theSvcList.persistence)??>
					${(theSvcList.persistence)!''}
				<#else>
					-
				</#if>
			<#else>
				-
			</#if>
		</td>
	<#if montotalSvc.condition.noticeGroup.isSelect()>
		<td class="align_center noticeGroup" data-colidx="12">
	<#else>			
		<td class="align_center noticeGroup none" data-colidx="12">
	</#if>
			<#if (theSvcList.noticeGroup)??>
				${(theSvcList.noticeGroup)!''}
			<#else>
				-
			</#if>
		</td>
	<#if montotalSvc.condition.updateTime.isSelect()>
		<td class="align_center updateTime" title="${(theSvcList.updateTime)!''}" data-colidx="13">
	<#else>			
		<td class="align_center updateTime none" title="${(theSvcList.updateTime)!''}" data-colidx="13">
	</#if>
			<#if (theSvcList.updateTime)??>
				${(theSvcList.updateTime?string("yyyy-MM-dd HH:mm:ss"))!}
			<#else>
				-
			</#if>
		</td>
	<#if montotalSvc.condition.configHistory.isSelect()>
		<td class="align_center slbConfig24Hour" data-colidx="14">
	<#else>			
		<td class="align_center slbConfig24Hour none" data-colidx="14">
	</#if>
			${(theSvcList.slbConfig24Hour)!''}
		</td>        
	</tr>
	</#list>	
</tbody>
		
