<#setting number_format="0.####">
<#if orderType??>
	<#if 15 == orderType></#if><!-- status -->
	<#if 34 == orderType></#if><!-- vsIpaddress -->
	<#if 35 == orderType></#if><!-- servicePort -->
	<#if 36 == orderType></#if><!-- vsName -->
	<#if 38 == orderType></#if><!-- bps In -->
	<#if 39 == orderType></#if><!-- bps Out -->
	<#if 40 == orderType></#if><!-- bps Total -->
	<#if 46 == orderType></#if><!-- session -->
	<#if 11 == orderType></#if><!-- 최근 설정시간 -->
	<#if 14 == orderType></#if><!-- ADC 이름 -->
</#if>
<#if orderDir??>
	<#if 1 == orderDir></#if><!-- Asc -->
	<#if 2 == orderDir></#if><!-- Desc -->
</#if>	
<colgroup>
	<col width="60px"/>
	<col width="60px"/>
	<!--<col width="80px"/>-->
	<col width="140px"/>
	<col width="60px"/>	
	<col width="200px"/>
	<col width="80px"/>
	<col width="80px"/>
	<col width="80px"/>
	<col width="120px"/>
	<col width="150px"/>
	<col width="200px"/>
	<col width="60px"/>
</colgroup>
<thead>
	<tr class="ContentsHeadLine ContentsHeadLineOrder">
		<th rowspan="3">
			<input class="allServersChk" type="checkbox" />
		</th>
		<th rowspan="3">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 15>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_VSALTEON_TABLESTATUS"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">15</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 15>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_VSALTEON_TABLESTATUS"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">15</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_VSALTEON_TABLESTATUS"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">15</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>	
		</th> 
		<!--
		<th rowspan="3">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 15>		
					<a class="orderDir_Desc">State
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">15</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 15>	
					<a class="orderDir_Asc">State
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">15</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">State 
						<img src="imgs/none.gif"/>
						<span class="none orderType">15</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>	
		</th> 		
		-->
		<th colspan="3" class="ContentsHeadLine Rcolor">Virtual Server</th>
		<th colspan="3" class="ContentsHeadLine Rcolor">bps</th>		
		<th rowspan="3">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 46>		
					<a class="orderDir_Desc">Concurrent
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">46</span>
						<span class="none orderDir">1</span>
						<br>Sessions(${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!})&nbsp;&nbsp;
					</a>						
				<#elseif orderDir == 1 && orderType == 46>	
					<a class="orderDir_Asc">Concurrent
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">46</span>
						<span class="none orderDir">2</span>
						<br>Sessions(${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!})&nbsp;&nbsp;					
					</a>
				<#else>
					<a class="orderDir_None">Concurrent
						<img src="imgs/none.gif"/>
						<span class="none orderType">46</span>
						<span class="none orderDir">2</span>
						<br>Sessions(${LANGCODEMAP["MSG_APPLIANCE_COUNT"]!})&nbsp;&nbsp;				
					</a>
				</#if>
			</span>						
		</th>
		<th rowspan="3">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 11>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_VSALTEON_TABLELAST_SET_TIME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 11>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_VSALTEON_TABLELAST_SET_TIME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_VSALTEON_TABLELAST_SET_TIME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>			
		</th>
		<th rowspan="3">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 14>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_GROUPMONITOR_ADCNAME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">14</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 14>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_GROUPMONITOR_ADCNAME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">14</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_GROUPMONITOR_ADCNAME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">14</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>			
		</th>		
		<th rowspan="3">Action</th>		
	</tr>
	<tr class="ContentsHeadLine1 ContentsHeadLineOrder">
	 		<th class="" > 
			<span class="css_textCursor"> 
 				<#if orderDir == 2 && orderType == 34>		 
 					<a class="orderDir_Desc">IP 
 						<img src="imgs/Desc.gif"/> 
 						<span class="none orderType">34</span> 
 						<span class="none orderDir">1</span>					
 					</a>						
 				<#elseif orderDir == 1 && orderType == 34>	 
 					<a class="orderDir_Asc">IP 
 						<img src="imgs/Asc.gif"/> 
 						<span class="none orderType">34</span> 
 						<span class="none orderDir">2</span>		
 					</a> 
 				<#else>
 					<a class="orderDir_None">IP 
 						<img src="imgs/none.gif"/> 
 						<span class="none orderType">34</span> 
 						<span class="none orderDir">2</span>		
 					</a> 
 				</#if>
 			</span>	
 		</th>	
		<th class="">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 35>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_VSALTEON_TABLEPORT"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">35</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 35>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_VSALTEON_TABLEPORT"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">35</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_VSALTEON_TABLEPORT"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">35</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>			
		</th>
		<th class="Rcolor">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 36>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_VSALTEON_TABLENAME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 36>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_VSALTEON_TABLENAME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_VSALTEON_TABLENAME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">36</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>			
		</th>
    	<th class="">
	  		<span class="css_textCursor ">
				<#if orderDir == 2 && orderType == 38>		
					<a class="orderDir_Desc">In
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">38</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 38>	
					<a class="orderDir_Asc">In
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">38</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">In
						<img src="imgs/none.gif"/>
						<span class="none orderType">38</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>
	  	</th>
	  	<th class="">
	  		<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 39>		
					<a class="orderDir_Desc">Out
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">39</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 39>	
					<a class="orderDir_Asc">Out
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">39</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">Out
						<img src="imgs/none.gif"/>
						<span class="none orderType">39</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>
	  	</th>
	  	<th class="Rcolor">
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 40>		
					<a class="orderDir_Desc">Total
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">40</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 40>	
					<a class="orderDir_Asc">Total
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">40</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">Total
						<img src="imgs/none.gif"/>
						<span class="none orderType">40</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>			  	
	  	</th>
    </tr>	
</thead>
<tbody>
	<#list virtualServerAllList![] as theVirtualServer>
	<tr class="ContentsLine1 virtualServerList">
		<td class="align_center">
			<#if accountRole == 'readOnly'>
				<input class="serverChk" type="checkbox" value="${theVirtualServer.index}" disabled="disabled"/>
			<#else>
				<input class="serverChk" type="checkbox" value="${theVirtualServer.index}"/>
			</#if>
		</td>
		<td class="align_center vsStatus">
			<#if (theVirtualServer.status!'') == 0>
				<img class="imgOn status_imgon" src="imgs/icon/icon_vs_disabled.png" alt="${LANGCODEMAP["MSG_VSALTEON_TABLEOPERATION"]!}" />
			<#elseif (theVirtualServer.status!'') == 1>
				<img class="imgOn status_imgon" src="imgs/icon/icon_vs_conn.png" alt="${LANGCODEMAP["MSG_VSALTEON_TABLEOPERATION"]!}" />
			<#elseif (theVirtualServer.status!'') == 2>
				<img class="imgOn status_imgon" src="imgs/icon/icon_vs_disconn.png" alt="${LANGCODEMAP["MSG_VSALTEON_TABLEOPERATION"]!}" />
			<#else>
				<img class="imgOn status_imgon" src="imgs/icon/icon_2d_3.png" alt="${LANGCODEMAP["MSG_VSALTEON_TABLEOPERATION"]!}" />

			</#if>	
		</td>	
		
		<!--<td class="align_center state">-->
		<!--	${theVirtualServer.status}-->
		<!--</td>-->
		<td class="align_left_P10 nborder vsIp">
			<a id="mar_lft5" class="modifyVirtualServerLnk" href="#">${theVirtualServer.ip}</a>
		</td>	
		<td class="align_center nborder">${theVirtualServer.port}</td>						
		<td class="textOver" title="${theVirtualServer.name}">
			<a class="modifyVirtualServerLnk align_left_P10" id="mar_lft5" href="#" target="_self">${theVirtualServer.name}</a>
		</td>		
		<td class="align_center nborder">
			<#if (theVirtualServer.bpsIn > -1)>
				${theVirtualServer.bpsInUnit!"-"}
			<#else>
				-
			</#if>
		</td>
		<td class="align_center nborder">
			<#if (theVirtualServer.bpsOut > -1)>
				${theVirtualServer.bpsOutUnit!"-"}
			<#else>
				-
			</#if>			
		</td>
		<td class="align_center">
			<#if (theVirtualServer.bpsTotal > -1)>
				${theVirtualServer.bpsTotalUnit!"-"}
			<#else>
				-
			</#if>
		</td>		
		<td class="align_center session">
			<span class="none">${theVirtualServer.adcType!""}</span>
			<#if (theVirtualServer.concurrentSession > -1)>
				${theVirtualServer.concurrentSessionUnit!"-"}
			<#else>
				-
			</#if>
		</td>
		<td class="align_center textOver updateTime"><span class="adcStatus none">${theVirtualServer.adcStatus!""}</span>${(theVirtualServer.updateTime?string("yyyy-MM-dd HH:mm:ss"))!'-'}</td>
		<td class="align_left_P10 adcName textOver" title="${theVirtualServer.adcName}">
			<span class="adcIndex none">${theVirtualServer.adcIndex!""}</span>
			<#if (theVirtualServer.adcType == 2)>
				<img src="imgs/icon/adc/icon_alteon_s.png" />
			<#elseif (theVirtualServer.adcType == 1)>
				<img src="imgs/icon/adc/icon_f5_s.png" />
			<#else>
				<img src="imgs/icon/adc/icon_piolink_s.png" />
			</#if>
			<span class="name">${theVirtualServer.adcName!""}</span>
		</td>		
		<td class="align_center">
			<span class="position_R5L5">
				<a href="#" class="onGoToMonitoring">
					<img src="imgs/btn/btn_monitoring_on.png" alt="${LANGCODEMAP["MSG_VSALTEON_TABLEMONITOR"]!}"/>
				</a>
			</span>	
		</td>
	</tr>	
	</#list>
</tbody>