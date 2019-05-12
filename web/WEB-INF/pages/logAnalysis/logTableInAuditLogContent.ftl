<#if orderType??>
	<#if 11 == orderType></#if><!-- occurTime -->
	<#if 16 == orderType></#if><!-- type -->
	<#if 19 == orderType></#if><!-- username -->
	<#if 20 == orderType></#if><!-- ipaddress -->
	<#if 21 == orderType></#if><!-- severity -->
</#if>
<#if orderDir??>
	<#if 1 == orderDir></#if><!--Asc-->
	<#if 2 == orderDir></#if><!--Desc-->
</#if>	
<colgroup>
	<col width="140px"/>
	<col width="100px"/>
	<col width="120px"/>
	<col width="120px"/>
	<col width="80px"/>
	<col width="auto"/ >
</colgroup>
<thead>
	<tr class="StartLine">
		<td colspan="6"></td>
	</tr>
	<tr class="ContentsHeadLine">
		<input class ="none" value="2" name="orderDir_Desc"/>
		<input class ="none" value="1" name="orderDir_Asc"/>
		<th>	
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 11>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_ADCLOG_OCCUR_TIME"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 11>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_ADCLOG_OCCUR_TIME"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_ADCLOG_OCCUR_TIME"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">11</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>							
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 19>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_ADCLOG_USER"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">19</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 19>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_ADCLOG_USER"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">19</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_ADCLOG_USER"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">19</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>			
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 20>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_ADCLOG_CONNECTIP"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">20</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 20>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_ADCLOG_CONNECTIP"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">20</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_ADCLOG_CONNECTIP"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">20</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>			
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 16>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_ADCLOG_TYPE"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">16</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 16>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_ADCLOG_TYPE"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">16</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_ADCLOG_TYPE"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">16</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>	
		</th>
		<th>
			<span class="css_textCursor">
				<#if orderDir == 2 && orderType == 21>		
					<a class="orderDir_Desc">${LANGCODEMAP["MSG_ADCLOG_IMPORTANCE"]!}
						<img src="imgs/Desc.gif"/>
						<span class="none orderType">21</span>
						<span class="none orderDir">1</span>					
					</a>						
				<#elseif orderDir == 1 && orderType == 21>	
					<a class="orderDir_Asc">${LANGCODEMAP["MSG_ADCLOG_IMPORTANCE"]!}
						<img src="imgs/Asc.gif"/>
						<span class="none orderType">21</span>
						<span class="none orderDir">2</span>		
					</a>
				<#else>
					<a class="orderDir_None">${LANGCODEMAP["MSG_ADCLOG_IMPORTANCE"]!}
						<img src="imgs/none.gif"/>
						<span class="none orderType">21</span>
						<span class="none orderDir">2</span>		
					</a>
				</#if>
			</span>			
		</th>
		<th>${LANGCODEMAP["MSG_ADCLOG_CONTENT"]!}</th>	
	</tr>
	<tr class="StartLine1">
		<td colspan="6"></td>
	</tr>	
</thead>
<tbody class="auditLogData">
	<#list auditLogs![] as theAuditLog>
	<tr class="ContentsLine3 auditLogList">		
		<td class="align_center textOver" title="${theAuditLog.occur_time?string("yyyy-MM-dd HH:mm:ss")}">${theAuditLog.occur_time?string("yyyy-MM-dd HH:mm:ss")}
			<input type="hidden" class="audit-occurtime" value='${theAuditLog.occur_time?string("yyyy-MM-dd HH:mm:ss")}' />
			<input type="hidden" class="audit-generator" value="${theAuditLog.generator!''}" />
			<input type="hidden" class="audit-ip" value="${theAuditLog.client_ip!''}" />
			<input type="hidden" class="audit-type" value="${theAuditLog.type!''}" />			
			<input type="hidden" class="audit-level" value="${theAuditLog.level!''}" />
			<input type="hidden" class="audit-content" value="${theAuditLog.content!''}" />
		</td>
		<td class="align_center textOver">${theAuditLog.generator!''}</td>
		<td class="align_center textOver">${theAuditLog.client_ip!''}</td>
		<td class="align_center">${theAuditLog.type!''}</td>
		<td class="align_center">${theAuditLog.level!''}</td>
		<td class="align_left_P10 textOver">
			<a class="popuplink" href="#">${theAuditLog.content!''}</a>
		</td>
	</tr>
	<tr class="DivideLine">
		<td colspan="6"></td>
	</tr>
	</#list>
</tbody>
<tr class="EndLine">
	<td colspan="6"></td>
</tr>