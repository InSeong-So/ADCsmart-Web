<#setting number_format="0.####">
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div> 
		<img src="imgs/title${img_lang!""}/h3_historyList.gif" class="title_h3"/>		
	</div>
	<!-- 설정 이력 비교 -->
	<div class="title_h4">
		<li>${LANGCODEMAP["MSG_HISTORY_COMPARE_SETTING_HISTORY"]!}</li>
	</div>
	<table class="Board" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
		<colgroup>
			<col width="200px"/>
			<col >
		</colgroup>
		<tr class="StartLine">
			<td colspan="2"></td>
		</tr>
		<tr>
			<tbody>
				<th class="Lth1">
					<li>${LANGCODEMAP["MSG_HISTORY_VIRTUALSERVER_NAME"]!}</li>
				</th>
				<td class="Lth0 textOver" title="${(historyDetailAlteon.currentAdcSetting.virtualSvr.name)!'${(historyDetailAlteon.pastAdcSetting.virtualSvr.name)!}'}">${(historyDetailAlteon.currentAdcSetting.virtualSvr.name)!'${(historyDetailAlteon.pastAdcSetting.virtualSvr.name)!}'}</td>
			</tbody>
		</tr>
		<tr class="StartLine">
			<td colspan="2"></td>
		</tr>
	</table>
	<!-- 설정 이력 비교 -->
	<table class="Board" cellpadding="0" cellspacing="0">
		<colgroup>
			<col width="200px"/>
			<col width="39%"/>
			<col >                        
		</colgroup>
		<tbody>
		<tr height="5px">
			<td colspan="3"></td> 
		</tr>
		<tr>
			<td></td> 
			<td class="StartLine" colspan="2"></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<th class="Lth2-1 Rcolor">
				${LANGCODEMAP["MSG_HISTORY_FINAL_SETTING"]!} ${(historyDetailAlteon.currentAdcSetting.setTime?string("yyyy-MM-dd HH:mm:ss"))!}
			</th>
			<th class="Lth2-1">
				<span class="txt_blue">${LANGCODEMAP["MSG_HISTORY_BEFORE_SETTING"]!}</span> 
				${(historyDetailAlteon.pastAdcSetting.setTime?string("yyyy-MM-dd HH:mm:ss"))!}
			</th>
       	</tr>
		<tr class="DivideLine1">
			<td colspan="3"></td>
		</tr>         
		<tr>
			<th class="Lth1">
				<li>${LANGCODEMAP["MSG_HISTORY_VIRTUALIP_ADDRESS"]!}</li>
			</th>
			<td class="Lth0 Rcolor">
				${(historyDetailAlteon.currentAdcSetting.virtualSvr.virtualIp)!}
			</td>
			<td class="Lth0">
				${(historyDetailAlteon.pastAdcSetting.virtualSvr.virtualIp)!}
			</td>
		</tr>
		<tr class="DivideLine1">
			<td colspan="3"></td>
		</tr>   
		<tr>
			<th class="Lth1">
				<li>Virtual Server ${LANGCODEMAP["MSG_HISTORY_NAME"]!}</li>
			</th>
			<td class="Lth0 Rcolor">
				${(historyDetailAlteon.currentAdcSetting.virtualSvr.name)!}
			</td>
			<td class="Lth0">
				${(historyDetailAlteon.pastAdcSetting.virtualSvr.name)!}
			</td>
		</tr>
		<#if (((historyDetailAlteon.currentAdcSetting.virtualSvr.virtualSvcs)![])?size > ((historyDetailAlteon.pastAdcSetting.virtualSvr.virtualSvcs)![])?size)>
			<#assign loopCount = ((historyDetailAlteon.currentAdcSetting.virtualSvr.virtualSvcs)![])?size>
		<#else>
			<#assign loopCount = ((historyDetailAlteon.pastAdcSetting.virtualSvr.virtualSvcs)![])?size>
		</#if>
		<#if loopCount == 0>
			<#assign loopCount = 1>
		</#if>
		<#list 0..(loopCount-1) as i>
		<tr class="DivideLine">
			<td colspan="3"></td>
		</tr>
		<tr>
			<th class="Lth1">
				<li>${LANGCODEMAP["MSG_HISTORY_GROUP_NAME"]!}</li>
			</th>
			<#if (historyDetailAlteon.currentAdcSetting.virtualSvr.virtualSvcs[i].pool.name)! == (historyDetailAlteon.pastAdcSetting.virtualSvr.virtualSvcs[i].pool.name)!>
				<td class="Lth0 Rcolor textOver">
					${(historyDetailAlteon.currentAdcSetting.virtualSvr.virtualSvcs[i].pool.name)!}
				</td>
				<td class="Lth0 textOver">
					${(historyDetailAlteon.pastAdcSetting.virtualSvr.virtualSvcs[i].pool.name)!}
				</td>
			<#else>
				<td class="Lth0 txt_red Rcolor textOver">
					${(historyDetailAlteon.currentAdcSetting.virtualSvr.virtualSvcs[i].pool.name)!}
				</td>
				<td class="Lth0 txt_red textOver">
					${(historyDetailAlteon.pastAdcSetting.virtualSvr.virtualSvcs[i].pool.name)!}
				</td>
			</#if>
       	</tr>
		<tr class="DivideLine">
			<td colspan="3"></td>
		</tr>
		<tr>
			<th class="Lth1">
				<li>${LANGCODEMAP["MSG_HISTORY_PORT"]!}</li>
			</th>
			<#if ((historyDetailAlteon.currentAdcSetting.virtualSvr.virtualSvcs[i].svcPort)!-1) == ((historyDetailAlteon.pastAdcSetting.virtualSvr.virtualSvcs[i].svcPort)!-1)>
				<td class="Lth0 Rcolor">
					${(historyDetailAlteon.currentAdcSetting.virtualSvr.virtualSvcs[i].svcPort)!}
				</td>
				<td class="Lth0">
					${(historyDetailAlteon.pastAdcSetting.virtualSvr.virtualSvcs[i].svcPort)!}
				</td>
			<#else>
				<td class="Lth0 txt_red Rcolor">
					${(historyDetailAlteon.currentAdcSetting.virtualSvr.virtualSvcs[i].svcPort)!}
				</td>
				<td class="Lth0 txt_red">
					${(historyDetailAlteon.pastAdcSetting.virtualSvr.virtualSvcs[i].svcPort)!}
				</td>
			</#if>
       	</tr>
		<tr class="DivideLine">
			<td colspan="3"></td>
		</tr>
		<tr>
			<th class="Lth1">
				<li>State</li>
			</th>
			<#if ((historyDetailAlteon.currentAdcSetting.virtualSvr.state)!-1) == ((historyDetailAlteon.pastAdcSetting.virtualSvr.state)!-1)>
				<td class="Lth0 Rcolor">
					${(historyDetailAlteon.currentAdcSetting.virtualSvr.state)!}
				</td>
				<td class="Lth0">
					${(historyDetailAlteon.pastAdcSetting.virtualSvr.state)!}
				</td>
			<#else>
				<td class="Lth0 txt_red Rcolor">
					${(historyDetailAlteon.currentAdcSetting.virtualSvr.state)!}
				</td>
				<td class="Lth0 txt_red">
					${(historyDetailAlteon.pastAdcSetting.virtualSvr.state)!}
				</td>
			</#if>
       	</tr>
		<tr class="DivideLine">
			<td colspan="3"></td>
		</tr>
		<tr>
			<th class="Lth1">
				<li>${LANGCODEMAP["MSG_HISTORY_GROUP_MEMBER_LIST"]!}</li>
			</th>
			<#if ((historyDetailAlteon.currentAdcSetting.virtualSvr.virtualSvcs[i].pool.members?size)!-1) == ((historyDetailAlteon.pastAdcSetting.virtualSvr.virtualSvcs[i].pool.members?size)!-1)>
				<td class="Lth0 Rcolor">
					<span class="txt_bold">Member</span> <span class="txt_blue">(${((historyDetailAlteon.currentAdcSetting.virtualSvr.virtualSvcs[i].pool.members)![])?size})</span>
				</td>
			<#else>
				<td class="Lth0 Rcolor">
					<span class="txt_red">Member (${((historyDetailAlteon.currentAdcSetting.virtualSvr.virtualSvcs[i].pool.members)![])?size})</span>
				</td>
			</#if>
			
			<#if ((historyDetailAlteon.currentAdcSetting.virtualSvr.virtualSvcs[i].pool.members?size)!-1) == ((historyDetailAlteon.pastAdcSetting.virtualSvr.virtualSvcs[i].pool.members?size)!-1)>
				<td class="Lth0">
					<span class="txt_bold">Member </span><span class="txt_blue">(${((historyDetailAlteon.pastAdcSetting.virtualSvr.virtualSvcs[i].pool.members)![])?size})</span>
				</td>
			<#else>
				<td class="Lth0">
					<span class="txt_red">Member (${((historyDetailAlteon.pastAdcSetting.virtualSvr.virtualSvcs[i].pool.members)![])?size})</span>
				</td>
			</#if>
    	</tr>
		<tr>
			<th class="Lth1">&nbsp;</th>
			<td class="Lth0 Rcolor align_top">
					<table class="width_96Border" cellpadding="0" cellspacing="0">
						<colgroup>
							<col width="220px"/>
							<col width="auto"/>	
						</colgroup>
						 <tr>
			            	<th class="align_center">IP</th>
			            	<th class="align_center">State</th>
						</tr>
					</table>
					<div class="table-wapper96">
						<table class="table_type11" cellpadding="0" cellspacing="0" >
							<colgroup>
								<col width="220px"/>
								<col width="auto"/>	
							</colgroup>
							<tbody>
								<#list (historyDetailAlteon.currentAdcSetting.virtualSvr.virtualSvcs[i].pool.members)![] as theMember>
									<tr>
										<td class="table_type11-boder1 align_left_P10">${theMember.ip}</td>
										<td class="table_type11-boder2 align_center">${(theMember.isEnabled?string('Enabled', 'Disabled'))!''}</td>
									</tr>
								</#list>
							</tbody>
		 				</table>
 				</div>
			</td>
			<td class="Lth0 align_top">
					<table class="width_96Border" cellpadding="0" cellspacing="0">
						<colgroup>
							<col width="220px"/>
							<col width="auto"/>
						</colgroup>
					    <tr>
					    	<th class="align_center">IP</th>
					        <th class="align_center">State</th>
					   	</tr>
					</table>
					<div class="table-wapper96">
						<table class="table_type11" cellpadding="0" cellspacing="0" >
							<colgroup>
								<col width="220px"/>
								<col width="auto"/>	
							</colgroup>
							<tbody>
								<#list (historyDetailAlteon.pastAdcSetting.virtualSvr.virtualSvcs[i].pool.members)![] as theMember>
								<tr>
									<td class="table_type11-boder1 align_left_P10">${theMember.ip}</td>
									<td class="table_type11-boder2 align_center">${(theMember.isEnabled?string('Enabled', 'Disabled'))!''}</td>
								</tr>
								</#list>
							</tbody>
						</table>
					</div>

    		</td>
		</tr>
		<tr class="DivideLine">
			<td colspan="3"></td>
		</tr>         
		<tr>
			<th class="Lth1">
				<li>Health Check</li>
			</th>
			<#if (historyDetailAlteon.currentAdcSetting.virtualSvr.virtualSvcs[i].pool.adcALTEONHealthCheck.extra)! == (historyDetailAlteon.pastAdcSetting.virtualSvr.virtualSvcs[i].pool.adcALTEONHealthCheck.extra)!>
				<td class="Lth0 Rcolor">
					${(historyDetailAlteon.currentAdcSetting.virtualSvr.virtualSvcs[i].pool.adcALTEONHealthCheck.extra)!}
				</td>
				<td class="Lth0">
					${(historyDetailAlteon.pastAdcSetting.virtualSvr.virtualSvcs[i].pool.adcALTEONHealthCheck.extra)!}
				</td>
			<#else>	
				<td class="Lth0 txt_red Rcolor">
					${(historyDetailAlteon.currentAdcSetting.virtualSvr.virtualSvcs[i].pool.adcALTEONHealthCheck.extra)!}
				</td>
				<td class="Lth0 txt_red">
					${(historyDetailAlteon.pastAdcSetting.virtualSvr.virtualSvcs[i].pool.adcALTEONHealthCheck.extra)!}
				</td>
			</#if>	
		</tr>
		<tr class="DivideLine">
			<td colspan="3"></td>
		</tr>
		<tr>
			<th class="Lth1">
				<li>Load Balancing</li>
			</th>
			<#if (historyDetailAlteon.currentAdcSetting.virtualSvr.virtualSvcs[i].pool.loadBalancingType)! == (historyDetailAlteon.pastAdcSetting.virtualSvr.virtualSvcs[i].pool.loadBalancingType)!>
				<td class="Lth0 Rcolor">
					${(historyDetailAlteon.currentAdcSetting.virtualSvr.virtualSvcs[i].pool.loadBalancingType)!}
				</td>
				<td class="Lth0">
					${(historyDetailAlteon.pastAdcSetting.virtualSvr.virtualSvcs[i].pool.loadBalancingType)!}
				</td>
			<#else>	
				<td class="Lth0 txt_red Rcolor">
					${(historyDetailAlteon.currentAdcSetting.virtualSvr.virtualSvcs[i].pool.loadBalancingType)!}
				</td>
				<td class="Lth0 txt_red">
					${(historyDetailAlteon.pastAdcSetting.virtualSvr.virtualSvcs[i].pool.loadBalancingType)!}
				</td>
			</#if>
		</tr>
		<tr class="EndLine">
			<td colspan="3"></td>
		</tr>  
		</#list> 
		<tr class="EndLine">
			<td colspan="3"></td>
		</tr> 		
		</tbody>   		
		<tr class="ContentsLine3">
			<td colspan="9">
			 <#if ((historyDetailAlteon.revertKey)! == 'On')>
			    <input type="button" class="revertLnk Btn_white_small" value="${LANGCODEMAP["MSG_HISTORY_RESTORE"]!}"/>   		
			<#elseif ((historyDetailAlteon.revertKey)! == 'Off')>
			    <input type="button" class="revertOffLnk Btn_white_small" disabled="disabled" value="${LANGCODEMAP["MSG_HISTORY_RESTORE"]!}" title="${LANGCODEMAP["MSG_HISTORY_RESTORE_IMPOSSIBLE"]!}"/> 			
			</#if>		       
		    </td>
		</tr>
	</table>
	<!-- 설정 변경 이력  -->		
	<div class="title_h4_1">
		<li>${LANGCODEMAP["MSG_HISTORY_HISTORY_SETTING_CHANGE"]!} 
			<span class="txt_blue">
				<#if "ko_KR" == langCode>
					(${((historyDetailAlteon.historyList)![])?size}${LANGCODEMAP["MSG_HISTORY_COUNT"]!})
				<#else>
					(${LANGCODEMAP["MSG_HISTORY_COUNT"]!}: ${((historyDetailAlteon.historyList)![])?size})
				</#if>
			</span>
		</li>
	</div>
	<table class="Board" cellpadding="0" cellspacing="0" id="history">
		<colgroup>
			<col width="150px"/>
			<col width="70px"/>
			<col width="auto"/>				
		</colgroup>
		<thead>
			<tr class="StartLine">
				<td colspan="3"></td>
			</tr>
			<tr class="ContentsHeadLine">
				<th>${LANGCODEMAP["MSG_HISTORY_OCCURTIME"]!}</th>
				<th>${LANGCODEMAP["MSG_HISTORY_USER"]!}</th>
				<th>${LANGCODEMAP["MSG_HISTORY_CHANGE_SUMMARY"]!}</th>
			</tr>
			<tr class="StartLine1">
				<td colspan="3"></td>
			</tr>
		</thead>
		<tbody>
		<#list (historyDetailAlteon.historyList)![] as theHistory>
			<tr class="ContentsLine1">
				<td class="BoardLink align_center">${(theHistory.lastChangeTime?string("yyyy-MM-dd HH:mm:ss"))!}<span class="none virtualSvrIndex">${theHistory.virtualSvrIndex!}</span><span class="none logSeq">${theHistory.logSeq!}</span></td>	
				<td class="align_center">${theHistory.accountName!}</td>
				<td class="align_left_P20">${theHistory.changeSummary!}</td>
			</tr>
			<tr class="DivideLine">
				<td colspan="3"></td>
			</tr>			
		</#list>		
		</tbody>
			<tr class="EndLine">
				<td colspan="3"></td>
			</tr>
			<tr height="5px">	
				<td colspan="3"> </td>
			</tr>			
	</table>
	<table class="Board">
		<tr class="ContentsLine3">
			<td colspan="3">
				<input type="button" class="adcHistoryLnk Btn_blue" value="${LANGCODEMAP["MSG_HISTORY_HISTORY_LIST"]!}"/> 
	    	</td>	
		</tr>
	</table>
	<!-- 설정 변경 이력  -->	
</div>