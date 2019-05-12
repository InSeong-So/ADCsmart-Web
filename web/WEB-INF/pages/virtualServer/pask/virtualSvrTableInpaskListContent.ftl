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
	<col width="6%"/>
	<col width="18%"/>
	<col width="16%"/>
	<col width="29%"/>	
	<col width="21%"/>
	<col width="5%"/>
</colgroup>
<thead>
  	<tr class="StartLine">
  		<td colspan="7"></td>
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
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_VSPASK_TABLESTATUS"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">15</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 15>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_VSPASK_TABLESTATUS"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">15</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_VSPASK_TABLESTATUS"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">15</span>
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
			<#if orderDir == 2 && orderType == 18>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_VSPASK_TABLEPORT"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">18</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 18>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_VSPASK_TABLEPORT"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">18</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_VSPASK_TABLEPORT"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">18</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>			
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 1>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_VSPASK_TABLENAME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">1</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 1>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_VSPASK_TABLENAME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">1</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_VSPASK_TABLENAME"]!}
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
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_VSPASK_TABLELAST_SET_TIME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 11>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_VSPASK_TABLELAST_SET_TIME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_VSPASK_TABLELAST_SET_TIME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>			
		</th>
		<th>Action</th>
	</tr>
	<tr class="StartLine1">
		<td colspan="7"></td>
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
				<img src="imgs/icon/icon_2d_3.png" alt="${LANGCODEMAP["MSG_VSPASK_TABLEACTION"]!}" />
			<#elseif (theVirtualServer.status!'') == 'disable'>
				<img src="imgs/icon/icon_vs_disabled.png"  alt="${LANGCODEMAP["MSG_VSPASK_TABLEACTION"]!}" />
			<#elseif (theVirtualServer.status!'') == 'available'>
				<img src="imgs/icon/icon_vs_conn.png" alt="${LANGCODEMAP["MSG_VSPASK_TABLEACTION"]!}" />
			<#else>
				<img src="imgs/icon/icon_vs_disconn.png" alt="${LANGCODEMAP["MSG_VSPASK_TABLEACTION"]!}" />
			</#if>
		</td>
		<td class="align_left_P10">
			<a id="mar_lft5" class="modifyVirtualServerLnk" href="#">${theVirtualServer.virtualIp}</a>
		</td>
		<td class="align_center">${theVirtualServer.servicePort}</td>
		<td class="align_left_P10 textOver" title="${theVirtualServer.name}">
			<a class="modifyVirtualServerLnk , align_left_P20"  id="mar_lft5" href="#" target="_self">${theVirtualServer.name}</a>
		</td>		
		<td class="align_center textOver">${(theVirtualServer.lastUpdateTime?string("yyyy-MM-dd HH:mm:ss"))!'-'}</td>
		<td class="align_center">
			<span class="position_R5L5">
				<a href="#" class="onGoToMonitoring">
					<img src="imgs/btn/btn_monitoring_on.png" alt="${LANGCODEMAP["MSG_VSALTEON_TABLEMONITOR"]!}"/>
				</a>
		</span>	
		</td>
	</tr>
	<tr class="DivideLine">
		<td colspan="7"></td>
	</tr>
</#list>
<tr class="EndLine">
	<td colspan="7"></td>
</tr>
