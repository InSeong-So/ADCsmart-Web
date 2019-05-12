<#setting number_format="0.####">
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div> 
		<#if !(vsGrpInfo.index)??>
			<img src="imgs/title${img_lang!""}/h3_serviceGroupAdd.gif" class="title_h3" />
		<#else>		
			<img src="imgs/title${img_lang!""}/h3_serviceGroupModify.gif" class="title_h3" />
		</#if>	 
	</div>
	<form id="serviceGrpAddFrm" class="setting" method="post">
	<!-- 1 --> 
	<div class="title_h4">
		<li>${LANGCODEMAP["MSG_SYSSETTING_SVCADD_DEFAULT_SET"]!}</li>
	</div>
	<table class="Board" cellpadding="0" cellspacing="0">
		<colgroup>
			<col width="200px"/>
			<col >
		</colgroup>			
		<tr class="StartLine">
			<td colspan="2" ></td>
		</tr>			
		<tr>
			<th class="Lth2">
				<li>${LANGCODEMAP["MSG_SYSSETTING_SVCGROUPNAME"]!}</li>
			</th>
			<td class="Lth0">
			<#if (vsGrpInfo.index)?? || (vsGrpInfo.index)! == ''>
				<input name="vsGrpInfo.name" type="" class="inputText width250" value="${(vsGrpInfo.name)!}"/> <span class="txt_gray2"> ${LANGCODEMAP["MSG_SYSTOOL_RESPONSE_NAMEVALIDATE"]!}</span>
			<#else>
				<input name="vsGrpInfo.name" type="" class="inputText width160" value="${(vsGrpInfo.index)!}"/> <span class="txt_gray2"> ${LANGCODEMAP["MSG_SYSTOOL_RESPONSE_NAMEVALIDATE"]!}</span>
				<input name="vsGrpInfo.index" type="hidden" class="inputText width130" value="${(vsGrpInfo.index)!}"/>
				<input name="vsGrpInfo.name" type="hidden" class="inputText width130" value="${(vsGrpInfo.name)!}"/>
			</#if>			
			</td>
		</tr>
		<tr class="DivideLine">
			<td colspan="2"></td>
		</tr>  
		<tr> 
			<th class="Lth2">
				<li>${LANGCODEMAP["MSG_SYSSETTING_SVCGROUPSERVICE"]!}</li>
			</th>
            <td class="Lth0 align_top">
                  	<table width="97%" border="0" cellpadding="0" cellspacing="0">
					<colgroup>
						<col width="48%" />
						<col width="4%"  >
						<col width="48%" />
					</colgroup>
					<tr> 
				  		<td>
				  			<span class="usrselected_th">${LANGCODEMAP["MSG_USERADD_SELECT"]!}</span>
				  			<span class="usrselected_txt">
				  				<span class="selectedAdcVsCount txt_blue">${(vsGrpInfo.count)!'0'}/10</span>&nbsp;${LANGCODEMAP["MSG_USERADD_COUNT_SEL"]!}
				  			</span>
				  		</td>
						<td></td>
						<td>
							<span class="userlist_txt">${LANGCODEMAP["MSG_USERADD_LIST"]!}</span>
						</td>
					</tr>
					<tr>
						<td>						
							<div class="member_selecte350">									
								<div class="selectedList" id="selectedList">
								<ul>
									<#if (vsGrpInfo)??>
									<#list vsGrpInfo.adcList as theAdcVs>
									<li class="availableAdc" id="${theAdcVs.index!''}" nm="${theAdcVs.adcName!''}" >										
										<a href="#">${theAdcVs.adcName!''}</a>									
										<ul>
											<#list theAdcVs.vsList![] as theVsInfo>
											<li class="availableVs" id="${theVsInfo.vsIndex!''}" nm="${theVsInfo.vsName!''}" ip="${theVsInfo.vsIP!''}:${theVsInfo.vsPort!''}">
											<#if (theVsInfo.vsName??) && (theVsInfo.vsName != "")>
												<a href="#">${theVsInfo.vsName!''}</a>
											<#else>
												<a href="#">${theVsInfo.vsIP!''}:${theVsInfo.vsPort!''}</a>
											</#if>
											</li>									
											</#list>
										</ul>										
									</li>	
									</#list>	
									</#if>											
								</ul>					
								</div>
							</div>
						</td>
						<td>
							<div class="position_arrow">	
								<a class="toVsSelectionLnk" id="toVsSelectionLnk" href="#">
									<img src="imgs/meun/btn_mov_lft.png" alt="${LANGCODEMAP["MSG_USERADD_MOVE_TO_SEL"]!}" />
								</a>
							</div>
							<div class="position_arrow">
								<a class="toVsDeselectionLnk" id="toVsDeselectionLnk" href="#">
									<img src="imgs/meun/btn_mov_rgt.png" alt="${LANGCODEMAP["MSG_USERADD_MOVE_TO_SEL"]!}" />
								</a>	
							</div>					
						</td>	
						<td>
							<div class="member_selecte350">
								<div class="unSelectedList" id="unSelectedList">
								<ul>
									<#list adcAllGroupList![] as theAdcVs>
									<li class="availableAdc" id="${theAdcVs.index!''}" nm="${theAdcVs.adcName!''}" >										
										<a href="#">${theAdcVs.adcName!''}</a>									
										<ul>
											<#list theAdcVs.vsList![] as theVsInfo>
											<li class="availableVs" id="${theVsInfo.vsIndex!''}" nm="${theVsInfo.vsName!''}" ip="${theVsInfo.vsIP!''}:${theVsInfo.vsPort!''}">
											<#if (theVsInfo.vsName != "")>
												<a href="#">${theVsInfo.vsName!''}</a>
											<#else>
												<a href="#">${theVsInfo.vsIP!''}:${theVsInfo.vsPort!''}</a>
											</#if>
											</li>									
											</#list>
										</ul>										
									</li>	
									</#list>							
								</ul>
								</div>
							</div>
						</td>
					</tr>
                  	</table>
			</td>
		</tr>
		<tr class="EndLine2">
			<td colspan="2"></td>
		</tr>
	</table>

	</form>
	<tr> 
		<td colspan="4">                            
			<div class="position_cT10">
			<#if accountRole == 'system'>
				<input type="button" class="serviceGrpAddOkLnk Btn_red" value="${LANGCODEMAP["MSG_USERADD_COMPLETE"]!}"/> 			
			</#if>
		  		<input type="button" class="serviceGrpAddCancelLnk Btn_white" value="${LANGCODEMAP["MSG_USERADD_CANCEL"]!}"/>   
			</div> 
		</td>
	</tr> 
</div>   
<style type="text/css"> 
  .jstree a .jstree-icon { display:none !important; }
  .jstree ul li ul a .jstree-icon { display:none !important; }  
</style>
