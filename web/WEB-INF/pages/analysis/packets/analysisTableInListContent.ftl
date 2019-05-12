<#if orderType??>
	<#if 33 == orderType></#if><!-- occurTime -->	
	<#if 34 == orderType></#if><!-- filename -->
	<#if 35 == orderType></#if><!-- filesize -->
</#if>
<#if orderDir??>
	<#if 1 == orderDirection></#if><!-- Asc -->
	<#if 2 == orderDirection></#if><!-- Desc -->
</#if>
<colgroup>							                            
    <col width="5%"/>
	<col width="20%"/>
	<col width="10%"/>
	<col width="15%"/>
	<col width="auto"/>
	<col width="10%"/>
	<col width="25%"/>
</colgroup>
<thead>
	<tr class="StartLine">
		<td colspan="7"></td>
	</tr>
	<tr class="ContentsHeadLine">
		<th>
			<span class="bg_row2 ">
				<input class="allPktDumpChk" name="pktDumpChk" type="checkbox" />
			</span>
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderObj.orderDirection == 2 && orderObj.orderType == 33>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_DIAG_ANAL_TIME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">33</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderObj.orderDirection == 1 && orderObj.orderType == 33>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_DIAG_ANAL_TIME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">33</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_DIAG_ANAL_TIME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">33</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>					
		</th>
		<th>${LANGCODEMAP["MSG_DIAG_ANAL_STATUS"]!}</th>
		<th>${LANGCODEMAP["MSG_DIAG_ANAL_ADC_NAME"]!}</th>
		<th>
			<span class="css_textCursor">
				<#if orderObj.orderDirection == 2 && orderObj.orderType == 34>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_DIAG_ANAL_FILE_NAME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderObj.orderDirection == 1 && orderObj.orderType == 34>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_DIAG_ANAL_FILE_NAME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_DIAG_ANAL_FILE_NAME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">34</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>					
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderObj.orderDirection == 2 && orderObj.orderType == 35>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_DIAG_ANAL_FILE_SIZE"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">35</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderObj.orderDirection == 1 && orderObj.orderType == 35>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_DIAG_ANAL_FILE_SIZE"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">35</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_DIAG_ANAL_FILE_SIZE"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">35</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>				
		</th>		
		<th>
			<span class="css_textCursor">${LANGCODEMAP["MSG_DIAG_ANAL_TERMS"]!}</span>
		</th>
	</tr>
	<tr class="StartLine1">
		<td colspan="7"></td>
	</tr>		
</thead>
<tbody>
	<#list pktdumpInfoList as thePktDumpInfo>
	<tr class="ContentsLine1 faultAnalysisList">
		<td class="align_center">
			<input class="pktDumpChk" type="checkbox" />
			<span class="none pktDumpIndex">${thePktDumpInfo.logIndex!''}</span>
		</td>
		<td class="align_center">${(thePktDumpInfo.occurTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
		
		<td class="align_center">
			<#if (thePktDumpInfo.status!'') == 100> 
				${LANGCODEMAP["MSG_CONSTANT_COM"]!}
			<#elseif (thePktDumpInfo.status!'') == 101> 
				${LANGCODEMAP["MSG_CONSTANT_FAIL"]!}
			<#elseif (thePktDumpInfo.status!'') == 102> 
				${LANGCODEMAP["MSG_CONSTANT_STOP"]!}
			<#elseif (thePktDumpInfo.status!'') == 103> 
				${LANGCODEMAP["MSG_CONSTANT_CANCEL"]!}
			<#else>
				${LANGCODEMAP["MSG_CONSTANT_PROGING"]!}
			</#if>			
		</td>
		<td class="align_left_P10">${thePktDumpInfo.adcName!}</td>
<!-- 		[GS] #3984-1 #5 14.07.28 sw.jung 패킷 수집 중지시 파일 다운로드 가능 -->
		<#if (thePktDumpInfo.status!"") == 100 || (thePktDumpInfo.status!"") == 102>
        <td class="align_left_P10 textOver">     	
			<a class="downloadPktDumpLnk" href="#" title="${thePktDumpInfo.fileName}">${thePktDumpInfo.fileName!''}</a>
		</td>	
		<#else>
		<td class="align_left_P10 textOver" title="${thePktDumpInfo.fileName}">${thePktDumpInfo.fileName!''}</td>	
		</#if>
		<td class="align_center">${thePktDumpInfo.fileSize!''}</td>
		<td class="align_left_P10 textOver" title="${thePktDumpInfo.strFilter!''}">${thePktDumpInfo.strFilter!''}</td>
	</tr>
	<tr class="DivideLine">
		<td colspan="7"></td>
	</tr>	
	</#list>
</tbody>
	<tr class="EndLine">
		<td colspan="7"></td>
	</tr>