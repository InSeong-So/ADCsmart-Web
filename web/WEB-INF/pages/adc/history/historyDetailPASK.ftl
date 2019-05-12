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
	<table class="Board" cellpadding="0" cellspacing="0"  style="table-layout: fixed;">
		<colgroup>
			<col width="200px"/>
			<col >
		</colgroup>
		<tr class="DivideLine">
			<td colspan="2" class="StartLine"></td>
		</tr>
		<tr>
			<th class="Lth1">
				<li>${LANGCODEMAP["MSG_HISTORY_VIRTUALSERVER_NAME"]!}</li>
			</th>
			<td class="Lth0 textOver" title="${(historyDetailPASK.currentAdcSetting.virtualSvr.name)!'${(historyDetailPASK.pastAdcSetting.virtualSvr.name)!}'}">${(historyDetailPASK.currentAdcSetting.virtualSvr.name)!'${(historyDetailPASK.pastAdcSetting.virtualSvr.name)!}'}</td>
		</tr>
		<tr class="EndLine">
			<td colspan="2"></td>
		</tr>
	</table>
	<!-- 설정 이력 비교 -->
	<table class="Board" cellpadding="0" cellspacing="0" >
		<colgroup>
			<col width="200px"/>
			<col width="39%"/>
			<col >                        
		</colgroup>
		<tbody>
		<tr height="5px">
			<td></td> 
			<td></td>
		</tr>		
		<tr>
			<td></td> 
			<td class="StartLine" colspan="2"></td>
		</tr>
		<tr>
			<td >&nbsp;</td>
			<th class="Lth2-1 Rcolor">
				${LANGCODEMAP["MSG_HISTORY_FINAL_SETTING"]!} ${(historyDetailPASK.currentAdcSetting.setTime?string("yyyy-MM-dd HH:mm:ss"))!}
			</th>
			<th class="Lth2-1">
				<span class="txt_blue">${LANGCODEMAP["MSG_HISTORY_BEFORE_SETTING"]!} </span> 
				${(historyDetailPASK.pastAdcSetting.setTime?string("yyyy-MM-dd HH:mm:ss"))!}
			</th>
       	</tr>
		<tr class="DivideLine1">
			<td colspan="3"></td>
		</tr>         
		<tr>
			<th class="Lth1" >
				<li>${LANGCODEMAP["MSG_HISTORY_VIRTUALIP_ADDRESS"]!}</li>
			</th>
			<td class="Lth0 Rcolor">${(historyDetailPASK.currentAdcSetting.virtualSvr.virtualIp)!}</td>
			<td class="Lth0">${(historyDetailPASK.pastAdcSetting.virtualSvr.virtualIp)!}</td>
		</tr>
		<tr class="DivideLine">
			<td colspan="3"></td>
		</tr>
		<tr>
			<th class="Lth1">
				<li>${LANGCODEMAP["MSG_HISTORY_GROUP_NAME"]!}</li>
			</th>
			<#if (historyDetailPASK.currentAdcSetting.virtualSvr.pool.name)! == (historyDetailPASK.pastAdcSetting.virtualSvr.pool.name)!>
				<td class="Lth0 , Rcolor textOver" >${(historyDetailPASK.currentAdcSetting.virtualSvr.pool.name)!}</td>
				<td class="Lth0 textOver" >${(historyDetailPASK.pastAdcSetting.virtualSvr.pool.name)!}</td>
			<#else>
				<td class="Lth0 , txt_red Rcolor textOver" >${(historyDetailPASK.currentAdcSetting.virtualSvr.pool.name)!}</td>
				<td class="Lth0 , txt_red textOver" >${(historyDetailPASK.pastAdcSetting.virtualSvr.pool.name)!}</td>
			</#if>
       	</tr>
		<tr class="DivideLine">
			<td colspan="3"></td>
		</tr>
		<tr>
			<th class="Lth1">
				<li>${LANGCODEMAP["MSG_HISTORY_PORT"]!}</li>
			</th>
			<#if ((historyDetailPASK.currentAdcSetting.virtualSvr.servicePort)!-1) == ((historyDetailPASK.pastAdcSetting.virtualSvr.servicePort)!-1)>
				<td class="Lth0 Rcolor">${(historyDetailPASK.currentAdcSetting.virtualSvr.servicePort)!}</td>
				<td class="Lth0">${(historyDetailPASK.pastAdcSetting.virtualSvr.servicePort)!}</td>
			<#else>
				<td class="Lth0 txt_red Rcolor">${(historyDetailPASK.currentAdcSetting.virtualSvr.servicePort)!}</td>
				<td class="Lth0 txt_red" >${(historyDetailPASK.pastAdcSetting.virtualSvr.servicePort)!}</td>
			</#if>
       	</tr>
		<tr class="DivideLine">
			<td colspan="3"></td>
		</tr>
		<tr>
			<th class="Lth1" >
				<li>${LANGCODEMAP["MSG_HISTORY_GROUP_MEMBER_LIST"]!}</li>
			</th>
			<#if ((historyDetailPASK.currentAdcSetting.virtualSvr.pool.members?size)!-1) == ((historyDetailPASK.pastAdcSetting.virtualSvr.pool.members?size)!-1)>
				<td class="Lth0 Rcolor" >
				<span class="txt_bold">Member</span> <span class="txt_blue">(${(historyDetailPASK.currentAdcSetting.virtualSvr.pool.members?size)!0})</span></td>
			<#else>
				<td class="Lth0 Rcolor" >
				<span class="txt_red">Member (${(historyDetailPASK.currentAdcSetting.virtualSvr.pool.members?size)!0})</span></td>
			</#if>
			
			<#if ((historyDetailPASK.currentAdcSetting.virtualSvr.pool.members?size)!-1) == ((historyDetailPASK.pastAdcSetting.virtualSvr.pool.members?size)!-1)>
				<td class="Lth0" >
				<span class="txt_bold">Member</span> <span class="txt_blue">(${(historyDetailPASK.pastAdcSetting.virtualSvr.pool.members?size)!0})</span></td>
			<#else>
				<td class="Lth0" >
				<span class="txt_red">Member (${(historyDetailPASK.pastAdcSetting.virtualSvr.pool.members?size)!0})</span></td>
			</#if>
    	</tr>
		<tr>
			<th class="Lth1">&nbsp;</th>
			<td class="Lth0 Rcolor align_top">
					<table class="width_96Border" cellpadding="0" cellspacing="0">
							<colgroup>
								<col width="150px"/>
								<col width="70px"/>
								<col width="auto"/>	
							</colgroup>
							<tr>
				            	<th class="align_center">IP</th>
				            	<th class="align_center">${LANGCODEMAP["MSG_HISTORY_PORT"]!}</th>
				            	<th class="align_center">State</th>
							</tr>
					</table>
					<div class="table-wapper96">
						<table class="table_type11" cellpadding="0" cellspacing="0" >
							<colgroup>
								<col width="150px"/>
								<col width="70px"/>
								<col width="auto"/>	
							</colgroup>
							<tbody>
								<#list (historyDetailPASK.currentAdcSetting.virtualSvr.pool.members)![] as theMember>
									<tr>
										<td class="table_type11-boder1 , align_left_P10">${theMember.ip}</td>
										<td class="table_type11-boder1 , align_center">${theMember.port}</td>
										<td class="table_type11-boder2 , align_center">${(theMember.isEnabled?string('Enabled', 'Disabled'))!''}</td>
									</tr>
								</#list>
							</tbody>
		 				</table>
 				</div>
			</td>
			<td class="Lth0 align_top">
					<table class="width_96Border" cellpadding="0" cellspacing="0">
							<colgroup>
								<col width="150px"/>
								<col width="70px"/>
								<col width="auto"/>	
							</colgroup>
					          <tr>
					            <th class="align_center">IP</th>
					            <th class="align_center">${LANGCODEMAP["MSG_HISTORY_PORT"]!}</th>
					            <th class="align_center">State</th>
					          </tr>
						</table>
					<div class="table-wapper96">
						<table class="table_type11" cellpadding="0" cellspacing="0" >
							<colgroup>
								<col width="150px"/>
								<col width="70px"/>
								<col width="auto"/>	
							</colgroup>
							<tbody>
								<#list (historyDetailPASK.pastAdcSetting.virtualSvr.pool.members)![] as theMember>
									<tr>
										<td class="table_type11-boder1 align_left_P10">${theMember.ip}</td>
										<td class="table_type11-boder1 align_center">${theMember.port}</td>
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
			<#if (historyDetailPASK.currentAdcSetting.virtualSvr.pool.adcPASKHealthCheck.name)! == (historyDetailPASK.pastAdcSetting.virtualSvr.pool.adcPASKHealthCheck.name)!>
				<td class="Lth0 Rcolor">${(historyDetailPASK.currentAdcSetting.virtualSvr.pool.adcPASKHealthCheck.name)!}</td>
				<td class="Lth0">${(historyDetailPASK.pastAdcSetting.virtualSvr.pool.adcPASKHealthCheck.name)!}</td>
			<#else>	
				<td class="Lth0 txt_red Rcolor">${(historyDetailPASK.currentAdcSetting.virtualSvr.pool.adcPASKHealthCheck.name)!}</td>
				<td class="Lth0 txt_red">${(historyDetailPASK.pastAdcSetting.virtualSvr.pool.adcPASKHealthCheck.name)!}</td>
			</#if>	
		</tr>
		<tr class="DivideLine">
			<td colspan="3"></td>
		</tr>
		<tr>
			<th class="Lth1">
				<li>Load Balancing</li>
			</th>
			<#if (historyDetailPASK.currentAdcSetting.virtualSvr.pool.loadBalancingType)! == (historyDetailPASK.pastAdcSetting.virtualSvr.pool.loadBalancingType)!>
				<td class="Lth0 Rcolor">${(historyDetailPASK.currentAdcSetting.virtualSvr.pool.loadBalancingType)!}</td>
				<td class="Lth0">${(historyDetailPASK.pastAdcSetting.virtualSvr.pool.loadBalancingType)!}</td>
			<#else>	
				<td class="Lth0 txt_red Rcolor">${(historyDetailPASK.currentAdcSetting.virtualSvr.pool.loadBalancingType)!}</td>
				<td class="Lth0 txt_red">${(historyDetailPASK.pastAdcSetting.virtualSvr.pool.loadBalancingType)!}</td>
			</#if>
		</tr> 
		<tr class="DivideLine">
			<td colspan="3"></td>
		</tr>
		<tr>
			<th class="Lth1">
				<li>Virtual IP Info</li>
			</th>
			<#if (historyDetailPASK.currentAdcSetting.vipInfo)! == (historyDetailPASK.pastAdcSetting.vipInfo)!>
				<td class="Lth0 Rcolor">${(historyDetailPASK.currentAdcSetting.vipInfo)!}</td>
				<td class="Lth0">${(historyDetailPASK.currentAdcSetting.vipInfo)!}</td>
			<#else>	
				<td class="Lth0 txt_red Rcolor">${(historyDetailPASK.currentAdcSetting.vipInfo)!}</td>
				<td class="Lth0 txt_red">${(historyDetailPASK.currentAdcSetting.vipInfo)!}</td>
			</#if>
		</tr> 
		<tr class="EndLine">
			<td colspan="3"></td>
		</tr>
	  	<tr class="ContentsLine3">
	  		<td colspan="9">
	        	<#if ((historyDetailPASK.revertKey)! == 'On')>	
	        		<input type="button" class="imgOn revertLnk Btn_white_small" value="${LANGCODEMAP["MSG_HISTORY_RESTORE"]!}"/>	  
        			<input type="button" class="imgOffrevertOffLnk Btn_white_small" disabled="disabled" value="${LANGCODEMAP["MSG_HISTORY_RESTORE"]!}" title="${LANGCODEMAP["MSG_HISTORY_RESTORE_IMPOSSIBLE"]!}"/>
	       		<#elseif ((historyDetailPASK.revertKey)! == 'Off')>
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
					(${((historyDetailPASK.historyList)![])?size}${LANGCODEMAP["MSG_HISTORY_COUNT"]!})
				<#else>
					(${LANGCODEMAP["MSG_HISTORY_COUNT"]!}: ${((historyDetailPASK.historyList)![])?size})
				</#if>				
			</span>
		</li>
	</div>
	<table class="Board" cellpadding="0" cellspacing="0"  id="history">
		<colgroup>
			<col width="20%"/>
			<col width="20%"/>
			<col width="60%"/>
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
		<#list (historyDetailPASK.historyList)![] as theHistory>
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