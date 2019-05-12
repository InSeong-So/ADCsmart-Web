<#setting number_format="0.####">
<#if orderType??>
	<#if 15 == orderType></#if><!-- status -->
	<#if 17 == orderType></#if><!-- index -->
	<#if  1 == orderType></#if><!-- vsName -->
	<#if  2 == orderType></#if><!-- vsIpaddress -->
	<!-- <#if 18 == orderType></#if> servicePort -->
	<#if 11 == orderType></#if><!-- occurTime -->
</#if>
<#if orderDir??>
	<#if 1 == orderDir></#if><!-- Asc -->
	<#if 2 == orderDir></#if><!-- Desc -->
</#if>	
<colgroup>
	<col width="5%"/>
	<col width="6%"/>
	<col width="7%"/>
	<col width="13%"/>
	<col width="14%"/>
	<col width="16%"/>	
	<col width="17%"/>
	<col width="4%"/>
	<col width="13%"/>
	<col width="5%"/>
</colgroup>
<thead>
	<tr class="StartLine">
		<td colspan="10"></td>
	</tr>
	<tr class="ContentsHeadLine">
		<#if accountRole == 'readOnly'>
			<th><input class="allServersChk" type="checkbox" disabled="disabled"/></th>
		<#else>
			<th><input class="allServersChk" type="checkbox"/></th>
		</#if>
		<th>
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
		<th>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 17>		
					<a class="orderDir_Desc">Index
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">17</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 17>	
					<a class="orderDir_Asc">Index
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">17</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">Index
						<img src="imgs/none.gif"/>
						<span class="none orderType">17</span>
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
		<th>${LANGCODEMAP["MSG_VSALTEON_TABLEPORT"]!}</th>
		<th>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 1>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_VSALTEON_TABLENAME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">1</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 1>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_VSALTEON_TABLENAME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">1</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_VSALTEON_TABLENAME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">1</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>			
		</th>		
		<th>
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
		<th>VRRP</th>
		<th>Network Class</th>
		<th>Action</th>
	</tr>
	<tr class="StartLine1">
		<td colspan="10"></td>
	</tr>	
</thead>
<#list virtualServers![] as theVirtualServer>
	<tr class="ContentsLine1 virtualServerList">
		<td class="align_center">
			<#if accountRole == 'readOnly'>
				<input class="serverChk" type="checkbox" value="${theVirtualServer.index}" disabled="disabled"/>
			<#else>
				<input class="serverChk" type="checkbox" value="${theVirtualServer.index}"/>
			</#if>
		</td>
		<td class="align_center">
			<#if (theVirtualServer.status!'') == 'block'>
				<img class="imgOn status_imgon" src="imgs/icon/icon_2d_3.png" alt="${LANGCODEMAP["MSG_VSALTEON_TABLEOPERATION"]!}" />
			<#elseif (theVirtualServer.status!'') == 'disable'>
				<img class="imgOn status_imgon" src="imgs/icon/icon_vs_disabled.png" alt="${LANGCODEMAP["MSG_VSALTEON_TABLEOPERATION"]!}" />
			<#elseif (theVirtualServer.status!'') == 'available'>
				<img class="imgOn status_imgon" src="imgs/icon/icon_vs_conn.png" alt="${LANGCODEMAP["MSG_VSALTEON_TABLEOPERATION"]!}" />
			<#else>
				<img class="imgOn status_imgon" src="imgs/icon/icon_vs_disconn.png" alt="${LANGCODEMAP["MSG_VSALTEON_TABLEOPERATION"]!}" />
			</#if>
			<img class="imgOff status_imgoff none" src="imgs/icon/icon_vs_disabled.png" alt="${LANGCODEMAP["MSG_VSALTEON_TABLEOPERATION"]!}" />
		</td>
		<td class="align_center">${theVirtualServer.alteonId}</td>
		<td class="align_left_P10">
			<a id="mar_lft5" class="modifyVirtualServerLnk" href="#">${theVirtualServer.virtualIp}</a>
		</td>
		<td class="align_center">${virtualServerFacade.retrieveAlteonVsPortsString(adc.index, theVirtualServer.index)!}</td>
		<td class="align_left_P10 textOver" title="${theVirtualServer.name}">
			<a class="modifyVirtualServerLnk align_left_P10" id="mar_lft5" href="#" target="_self">${theVirtualServer.name}</a>
		</td>		
		<td class="align_center textOver">${(theVirtualServer.lastUpdateTime?string("yyyy-MM-dd HH:mm:ss"))!'-'}</td>
		<td class="align_center">
			<#if (theVirtualServer.vrrpState) == -1>
				<img src="imgs/icon/bu_conn_off.png"/>
			<#elseif (theVirtualServer.vrrpState) == 0>
				<img src="imgs/icon/bu_conn_dis.png"/>
			<#elseif (theVirtualServer.vrrpState) == 1>
				<img src="imgs/icon/bu_conn_on.png"/>
			</#if>
		</td>
		<td class="align_center">
			<#if !(theVirtualServer.nwclassId)??>
				<img src="imgs/icon/bu_conn_off.png"/>
			<#else>
				<#if (theVirtualServer.nwclassId) == 0>
					<img src="imgs/icon/bu_conn_off.png"/>
				<#else>	
					<img src="imgs/icon/bu_conn_on.png"/>
				</#if>	
			</#if>
		</td>
		<td class="align_center">
			<span class="position_R5L5">
				<a href="#" class="onGoToMonitoring">
					<img src="imgs/btn/btn_monitoring_on.png" alt="${LANGCODEMAP["MSG_VSALTEON_TABLEMONITOR"]!}"/>
				</a>
		</span>	
		</td>
	</tr>
	<tr class="DivideLine virtualServerList">
		<td colspan="10"></td>
	</tr>
</#list>
<tr class="EndLine">
	<td colspan="10"></td>
</tr>
