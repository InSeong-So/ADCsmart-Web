<#setting number_format="0.####">
<#if orderType??>
	<#if 15 == orderType></#if><!-- vsStatus -->
	<#if  1 == orderType></#if><!-- vsName -->
	<#if  2 == orderType></#if><!-- vsIpaddress -->
	<#if 18 == orderType></#if><!-- servicePort -->
	<#if 11 == orderType></#if><!-- occurTime -->
</#if>
<#if orderDir??>
	<#if 1 == orderDir></#if><!-- Asc -->
	<#if 2 == orderDir></#if><!-- Desc -->
</#if>
<colgroup>							                            
	<col width="5%"/>
	<col width="15%"/>
	<col width="10%"/>
	<col width="25%"/>
	<col width="15%"/>
	<col width="10%"/>
	<col width="20%"/>
</colgroup>
<thead>	
	<tr class="ContentsHeadLine">
		<#if accountRole == 'readOnly'>
			<th><input class="allServersChk" type="checkbox" disabled="disabled"/></th>
		<#else>
			<th><input class="allServersChk" type="checkbox"/></th>
		</#if>
		<th>
		<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 17>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_SLBSCHEDULE_TABLETIME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">17</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 17>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_SLBSCHEDULE_TABLETIME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">17</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_SLBSCHEDULE_TABLETIME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">17</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>	
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 15>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_SLBSCHEDULE_TABLESTATE"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">15</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 15>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_SLBSCHEDULE_TABLESTATE"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">15</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_SLBSCHEDULE_TABLESTATE"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">15</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>			
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 1>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_SLBSCHEDULE_TABLENAME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">1</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 1>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_SLBSCHEDULE_TABLENAME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">1</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_SLBSCHEDULE_TABLENAME"]!}
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
 		<th>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 11>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_SLBSCHEDULE_TABLEREQUEST"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 11>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_SLBSCHEDULE_TABLEREQUEST"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_SLBSCHEDULE_TABLEREQUEST"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>			
		</th>
		<th>
			<span class="css_textCursor">${LANGCODEMAP["MSG_SLBSCHEDULE_TABLEUSER"]!}</span>			
		</th>					
	</tr>
</thead>
<tbody>
<#list scheduleList![] as theSchedule>
	<tr class="ContentsLine1 virtualServerList">
		<td class="align_center">
			<input class="serverChk" type="checkbox" value="${theSchedule.index!''}">
			<input class="scheduleVsIndex" type="hidden" value="${theSchedule.vsIndex!''}">
			<input class="scheduleHour" type="hidden" value="${theSchedule.reservedHour!''}">
			<input class="scheduleMin" type="hidden" value="${theSchedule.reservedMin!''}">
			<input class="vsIpAddress" type="hidden" value="${theSchedule.vsIp!''}">
		</td>
		<td class="align_center">
			${(theSchedule.reservationTime?string("yyyy-MM-dd HH:mm"))!}
		</td>
		<td class="align_center txt_green_nobold">
			<#if (theSchedule.state == 0)>
				<a class="modifySlbScheduleLnk" id="mar_lft5" href="#" target="_self">${LANGCODEMAP["MSG_SLBSCHEDULE_INCOMPLETE"]!}</a>
			<#elseif (theSchedule.state == 1)>
				<a class="historyDiffLnk" id="mar_lft5" href="#" target="_self">${LANGCODEMAP["MSG_SLBSCHEDULE_SUCCESS"]!}</a>
			<#elseif (theSchedule.state == 2)>
				<a class="historyDiffLnk" id="mar_lft5" href="#" target="_self">${LANGCODEMAP["MSG_SLBSCHEDULE_FAIL"]!}</a>
			<#else>
				<a class="modifySlbScheduleLnk" id="mar_lft5" href="#" target="_self">${LANGCODEMAP["MSG_SLBSCHEDULE_INCOMPLETE"]!}</a>
			</#if>
		</td>
		<#if (theSchedule.state == 0)>
		<td class="textOver" title="${theSchedule.vsName!''}">
			<a class="modifySlbScheduleLnk" id="mar_lft5" href="#" target="_self">${theSchedule.vsName!''}</a>
		</td>
		<td>
			<a class="modifySlbScheduleLnk" id="mar_lft5" href="#" target="_self">${theSchedule.vsIp!''}</a>
		</td>
		<#else>
		<td class="textOver" title="${theSchedule.vsName!''}">${theSchedule.vsName!''}</td>
		<td>${theSchedule.vsIp!''}</td>
		</#if>
		<td class="textOver" title="${theSchedule.team!''} / ${theSchedule.phone!''}">
			${theSchedule.name!''}
		</td>
		<td class="scheuleReceiveUser textOver" title="${theSchedule.userNmHp!''}">
			${theSchedule.userNm!''}	
		</td>
	</tr>
</#list>				
</tbody>


