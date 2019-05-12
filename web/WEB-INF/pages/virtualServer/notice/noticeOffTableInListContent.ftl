<#setting number_format="0.####">
<#if orderTypeOff??>
	<#if 15 == orderTypeOff></#if><!-- vsStatus -->
	<#if  1 == orderTypeOff></#if><!-- vsName -->
	<#if  2 == orderTypeOff></#if><!-- vsIpaddress -->
	<#if 18 == orderTypeOff></#if><!-- servicePort -->
	<#if 33 == orderTypeOff></#if><!-- servicePoolName -->
	<#if 34 == orderTypeOff></#if><!-- noticePoolName -->
</#if>
<#if orderDirOff??>
	<#if 1 == orderDirOff></#if><!-- Asc -->
	<#if 2 == orderDirOff></#if><!-- Desc -->
</#if>
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<colgroup>
<#if adc.type == "Alteon">
	<col width="5%"/>
	<col width="6%"/>		
	<col width="6%"/>	
	<col width="15%"/>
	<col width="12%"/>
	<col width="8%"/>
	<col width="24%"/>
	<col width="24%"/>
<#else>
	<col width="5%"/>
	<col width="6%"/>	
	<col width="21%"/>
	<col width="12%"/>
	<col width="8%"/>
	<col width="24%"/>
	<col width="24%"/>
</#if>
</colgroup>
<thead>
	<tr class="StartLine">
	<#if adc.type == "Alteon">
		<td colspan="8"></td>
	<#else>
		<td colspan="7"></td>		
	</#if>			
	</tr>
	<tr class="ContentsHeadLine">
		<#if accountRole == 'readOnly'>
			<th><input class="allNoitceGrpChk" type="checkbox" disabled="disabled"/></th>
		<#else>	
			<th><input class="allNoticeGrpChk" type="checkbox"/></th>
		</#if>		
		<th>
		<span class="css_textCursor">
				<#if orderDirOff == 2 && orderTypeOff == 15>		
					<a class="orderDirOff_Desc">${LANGCODEMAP["MSG_VSF5_TABLESTATUS"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderTypeOff">15</span>
						<span class="none orderDirOff">1</span>					
					</a>						
				<#elseif orderDirOff == 1 && orderTypeOff == 15>	
					<a class="orderDirOff_Asc">${LANGCODEMAP["MSG_VSF5_TABLESTATUS"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderTypeOff">15</span>
						<span class="none orderDirOff">2</span>		
					</a>
				<#else>
					<a class="orderDirOff_None">${LANGCODEMAP["MSG_VSF5_TABLESTATUS"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderTypeOff">15</span>
						<span class="none orderDirOff">2</span>		
					</a>
				</#if>
			</span>	
		</th>
		<#if adc.type == "Alteon">
		<th>
			<span class="css_textCursor">
				<#if orderDirOff == 2 && orderTypeOff == 1>		
					<a class="orderDirOff_Desc">Index
						<img src="imgs/Desc.gif"/>
						<span class="none orderTypeOff">1</span>
						<span class="none orderDirOff">1</span>					
					</a>						
				<#elseif orderDirOff == 1 && orderTypeOff == 1>	
					<a class="orderDirOff_Asc">Index
						<img src="imgs/Asc.gif"/>
						<span class="none orderTypeOff">1</span>
						<span class="none orderDirOff">2</span>		
					</a>
				<#else>
					<a class="orderDirOff_None">Index
						<img src="imgs/none.gif"/>
						<span class="none orderTypeOff">1</span>
						<span class="none orderDirOff">2</span>		
					</a>
				</#if>
			</span>			
		</th>	
		</#if>
		<th>
			<span class="css_textCursor">
				<#if orderDirOff == 2 && orderTypeOff == 1>		
					<a class="orderDirOff_Desc">${LANGCODEMAP["MSG_VSF5_TABLENAME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderTypeOff">1</span>
						<span class="none orderDirOff">1</span>					
					</a>						
				<#elseif orderDirOff == 1 && orderTypeOff == 1>	
					<a class="orderDirOff_Asc">${LANGCODEMAP["MSG_VSF5_TABLENAME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderTypeOff">1</span>
						<span class="none orderDirOff">2</span>		
					</a>
				<#else>
					<a class="orderDirOff_None">${LANGCODEMAP["MSG_VSF5_TABLENAME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderTypeOff">1</span>
						<span class="none orderDirOff">2</span>		
					</a>
				</#if>
			</span>			
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderDirOff == 2 && orderTypeOff == 2>		
					<a class="orderDirOff_Desc">IP
						<img src="imgs/Desc.gif"/>
						<span class="none orderTypeOff">2</span>
						<span class="none orderDirOff">1</span>					
					</a>						
				<#elseif orderDirOff == 1 && orderTypeOff == 2>	
					<a class="orderDirOff_Asc">IP
						<img src="imgs/Asc.gif"/>
						<span class="none orderTypeOff">2</span>
						<span class="none orderDirOff">2</span>		
					</a>
				<#else>
					<a class="orderDirOff_None">IP
						<img src="imgs/none.gif"/>
						<span class="none orderTypeOff">2</span>
						<span class="none orderDirOff">2</span>		
					</a>
				</#if>
			</span>		
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderDirOff == 2 && orderTypeOff == 18>		
					<a class="orderDirOff_Desc">${LANGCODEMAP["MSG_VSF5_TABLEPORT"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderTypeOff">18</span>
						<span class="none orderDirOff">1</span>					
					</a>						
				<#elseif orderDirOff == 1 && orderTypeOff == 18>	
					<a class="orderDirOff_Asc">${LANGCODEMAP["MSG_VSF5_TABLEPORT"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderTypeOff">18</span>
						<span class="none orderDirOff">2</span>		
					</a>
				<#else>
					<a class="orderDirOff_None">${LANGCODEMAP["MSG_VSF5_TABLEPORT"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderTypeOff">18</span>
						<span class="none orderDirOff">2</span>		
					</a>
				</#if>
			</span>			
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderDirOff == 2 && orderTypeOff == 33>		
					<a class="orderDirOff_Desc">${LANGCODEMAP["MSG_VSNOTICE_SERVICE_GROUP"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderTypeOff">33</span>
						<span class="none orderDirOff">1</span>					
					</a>						
				<#elseif orderDirOff == 1 && orderTypeOff == 33>	
					<a class="orderDirOff_Asc">${LANGCODEMAP["MSG_VSNOTICE_SERVICE_GROUP"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderTypeOff">33</span>
						<span class="none orderDirOff">2</span>		
					</a>
				<#else>
					<a class="orderDirOff_None">${LANGCODEMAP["MSG_VSNOTICE_SERVICE_GROUP"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderTypeOff">33</span>
						<span class="none orderDirOff">2</span>		
					</a>
				</#if>
			</span>			
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderDirOff == 2 && orderTypeOff == 34>		
					<a class="orderDirOff_Desc">${LANGCODEMAP["MSG_VSNOTICE_GROUP"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderTypeOff">34</span>
						<span class="none orderDirOff">1</span>					
					</a>						
				<#elseif orderDirOff == 1 && orderTypeOff == 34>	
					<a class="orderDirOff_Asc">${LANGCODEMAP["MSG_VSNOTICE_GROUP"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderTypeOff">34</span>
						<span class="none orderDirOff">2</span>		
					</a>
				<#else>
					<a class="orderDirOff_None">${LANGCODEMAP["MSG_VSNOTICE_GROUP"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderTypeOff">34</span>
						<span class="none orderDirOff">2</span>		
					</a>
				</#if>
			</span>		
		</th>
	</tr>
	<tr class="StartLine1">
	<#if adc.type == "Alteon">
		<td colspan="8"></td>
	<#else>
		<td colspan="7"></td>
	</#if>		
	</tr>
</thead>
<tbody id="noticeChangeCount">
	<#if VServerNoticeOff??>
		<#list VServerNoticeOff as theNotice>
			<#if 0 == (theNotice.vsStatus!'')>
				<#assign imageFileName = "icon_vs_disabled.png">
				<#assign statusText = "Disabled">
			<#elseif 1 == (theNotice.vsStatus!'')>
					<#assign imageFileName = "icon_vs_conn.png">
					<#assign statusText = "${LANGCODEMAP['MSG_PERFALTEON_NORMAL']!}">
			<#elseif 2 == (theNotice.vsStatus!'')>
					<#assign imageFileName = "icon_vs_disconn.png">
					<#assign statusText = "${LANGCODEMAP['MSG_PERFALTEON_DISCONN']!}">
			<#elseif 3 == (theNotice.vsStatus!'')>
					<#assign imageFileName = "icon_yellowDot.png">
					<#assign statusText = "${LANGCODEMAP['MSG_PERFALTEON_ABNORMAL']!}">
			</#if>
			
			<tr class="ContentsLine1 vsServerChangeList">
				<td class="align_center">					
					<#if accountRole == 'readOnly'>
						<input class="noticeGrpChk" id="noticeChangeGrp" ischk="0" type="checkbox" value="${theNotice.index}" disabled="disabled"/>
					<#else>	
						<input class="noticeGrpChk" id="noticeChangeGrp" ischk="0" type="checkbox" value="${theNotice.index}" />
					</#if>
				</td>				
				<td class="align_center">
					<span class="vsStatus none">${(theNotice.vsStatus)!''}</span>
					<span class="align_center">
						<img src="imgs/icon/${imageFileName}" alt="${statusText}" />
					</span>
				</td>
				<#if adc.type == "Alteon">
				<td class="align_center alteonID">${theNotice.alteonID}</td>	
				</#if>
				<td class="textOver vsName" title="${theNotice.vsName}">${(theNotice.vsName)!''}</td>
				<td class="align_left_P10 vsIp">${(theNotice.virtualIp)!''}</td>
				<td class="align_center vsPort">${(theNotice.servicePort)!''}</td>
				<td class="textOver">
					<#if adc.type == "Alteon">	
						<span class="sPoolIndex none">${(theNotice.servicePoolIndex)!''}</span>
						<span class="sPoolAlteonID none">${(theNotice.servicePoolAlteonID)!''}</span>
						<span class="sPoolName">${(theNotice.servicePoolAlteonID)!''}(${(theNotice.servicePoolName)!''})</span>
					<#elseif adc.type = "F5">
						<span class="sPoolIndex none">${(theNotice.servicePoolIndex)!''}</span>
						<span class="sPoolName">${(theNotice.servicePoolName)!''}</span>
					</#if>					
				</td>							
				<td class="textOver txt_gray1">
				<#if (noticeGrpList?size > 0)>
					<#list noticeGrpList as theNoticeGrp>	
					<#if accntIndex == (theNoticeGrp.accntIndex)>
					<#if adc.type == "Alteon">						
						<span class="nPoolIndex none">${(theNoticeGrp.poolIndex)!''}</span>
						<span class="nPoolName noticePool" title="${(theNoticeGrp.poolName)!''}">${(theNoticeGrp.alteonID)!''}(${(theNoticeGrp.poolName)!''})</span>				
					<#elseif adc.type = "F5">
						<span class="nPoolIndex none">${(theNoticeGrp.poolIndex)!''}</span>
						<span class="nPoolName noticePool" title="${(theNoticeGrp.poolName)!''}">${(theNoticeGrp.poolName)!''}</span>
						
					</#if>	
					</#if>						
					</#list>
				<#else>
					<span class="noticePool">-</span>
				</#if>																										
				</td>															
			</tr>
			<tr>
			<#if adc.type == "Alteon">
				<td colspan="8" class="DivideLine1"></td>
			<#else>
				<td colspan="7" class="DivideLine1"></td>	
			</#if>					
			</tr>
		</#list>
	</#if>
	<tr class="EndLine">
	<#if adc.type == "Alteon">
		<td colspan="8"></td>
	<#else>
		<td colspan="7"></td>
	</#if>
	</tr>
</tbody>