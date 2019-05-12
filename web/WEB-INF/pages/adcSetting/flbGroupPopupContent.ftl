<!-- FLB 그룹 선택 -->
<!-- <div id="groupMgmtWnd" class="pop_type_wrap"> -->
	<h2>${LANGCODEMAP["MSG_FLBGROUP_FLBGROUP"]!}</h2>
	<div class="pop_contents">
		<span class="groupselected_txt"><span class="txt_blue SelectCount"></span>&nbsp;${LANGCODEMAP["MSG_FLBGROUP_COUNT_SEL"]!}</span>					 
		<table class="table_type11-2" summary="">
			<caption></caption>
			<colgroup>
				<col width="30px" />
				<col width="100px" />
				<col width="200px" />										
				<col width="170px"/>
				<col width="auto"/>
			</colgroup>
			<thead>			
				<tr>
					<th>
						<#if accountRole == 'readOnly'>
							<input class="allGroupsChk" type="checkbox" disabled="disabled"/>
						<#else>
							<input class="allGroupsChk" type="checkbox"/>
						</#if>
					</th>
					<th>Group Index</th>
					<th>Filter(Index:${LANGCODEMAP["MSG_FLBGROUP_NAME"]!})</th>
					<th>Real</th>	
					<th>Concurrent Connection</th>						
				</tr>					
			</thead>
		</table>
		<div class="listWrap2">
			<table class="table_flb" summary="" >	
				<colgroup>
				<col width="30px" />
				<col width="100px" />
				<col width="200px" />										
				<col width="170px"/>
				<col width="auto"/>
				</colgroup>				
				<tbody class="flbTbody">
					<#list flbGroupList![] as flbGroupInfo>
					<tr>						
						<td>
							<#if (flbGroupInfo.isMonitoringOn) == 1>
								<#if accountRole == 'readOnly'>
									<input class="flbGroupchk" type="checkbox" checked="checked" value="${flbGroupInfo.dbIndex}" disabled="disabled"/>
								<#else>
									<input class="flbGroupchk" type="checkbox" checked="checked" value="${flbGroupInfo.dbIndex}"/>
								</#if>
							<#else>
								<#if accountRole == 'readOnly'>
									<input class="flbGroupchk" type="checkbox" value="${flbGroupInfo.dbIndex}" disabled="disabled"/>
								<#else>
									<input class="flbGroupchk" type="checkbox" value="${flbGroupInfo.dbIndex}"/>
								</#if>
							</#if>						
						</td>						
						<td>
							${flbGroupInfo.groupId!""}						
						</td>
						<td>						
							<#list flbGroupInfo.filterList as filterinfo>							
								<div class="filter align_left_P20">					
									<span class="index"><#setting number_format="0.####">${filterinfo.filterId!""}</span>:
									<span class="name" >${filterinfo.name!""}</span>
								</div>
							</#list>	
												
						</td>
						<td>
							<#list flbGroupInfo.realList as realInfo>
								<div class="real align_left_P20">	
									<span class="id">${realInfo.alteonId!""}</span>:
									<span class="ip">${realInfo.ipAddress!""}</span>								
								</div>
							</#list>
						</td>																
						<td>${flbGroupInfo.currentConnection!""}</td>						
					</tr>
					</#list>					
				</tbody>
			</table>
		</div>
	<div class="position_cT10">
		<#if accountRole != 'readOnly'>
			<input type="button" class="completeLnk Btn_red" value="${LANGCODEMAP["MSG_FLBGROUP_COMPLETE"]!}"/>
		</#if> 	
		<input type="button" class="closeLnk Btn_white" value="${LANGCODEMAP["MSG_FLBGROUP_CLOSE"]!}"/> 		
	</div>
	</div>
	<p class="close">
		<a class="closeLnk" href="#" title="${LANGCODEMAP["MSG_FLBGROUP_CLOSE"]!}">
			<img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_FLBGROUP_CLOSE"]!}"/>
		</a>
	</p>