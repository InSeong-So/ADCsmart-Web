<div class="userRoleArea">
	<!-- 3 -->
	<#if accountRole == 'system'>
	<div class="adcAssignment">
		<div class="title_h4_1">
			<li>${LANGCODEMAP["MSG_USERADD_ADD_SET"]!}</li>
		</div>
		<table class="Board">
			<colgroup>
				<col width="200px" />
				<col >
			</colgroup>
			<tr class="StartLine">
				<td colspan="2"></td>
			</tr>
			<tr> 
				<th class="Lth1">${LANGCODEMAP["MSG_USERADD_ADC_SEL"]!}</th>
		        <td class="Lth0 align_top">
                	<table width="97%" border="0" cellpadding="0" cellspacing="0">	
                		<colgroup>
							<col width="48%" />
							<col width="4%" >
							<col width="48%" />
						</colgroup>									
                        <tr> 
					  		<td><span class="usrselected_th">${LANGCODEMAP["MSG_USERADD_SELECT"]!}</span><span class="usrselected_txt"><span class="selectedAdcsCount txt_blue">${(registeredAdcMap!{})?values?size}</span>&nbsp;${LANGCODEMAP["MSG_USERADD_COUNT_SEL"]!}</span></td>							
							<td> </td>
							<td><span class="userlist_txt">${LANGCODEMAP["MSG_USERADD_LIST"]!}</span></td>
						</tr>  						
						<tr>
					   		<td>						
								<select name="account.adcVsList.adcIndex" size="6"  multiple="multiple" class="adcsSelectedSel usrselecte">
								<#list (registeredAdcMap!{})?values as theAdc>
									<option value="${theAdc.index}">${theAdc.name}</option>
								</#list>
								</select>
							</td>						
	     					<td>
								<div class="position_arrow">
					      			<a class="toAdcSelectionLnk " href="#">
					      				<img src="imgs/meun/btn_mov_lft.png" alt="${LANGCODEMAP["MSG_USERADD_MOVE_TO_SEL"]!}" />
					      			</a>
								</div>
								<div class="position_arrow">
									<a class="toAdcDeselectionLnk" href="#">
				      					<img src="imgs/meun/btn_mov_rgt.png" alt="${LANGCODEMAP["MSG_USERADD_MOVE_TO_SEL"]!}" />
				      				</a>
								</div>
	           				</td>
							<td>
								<!--
								<span class="inputTextposition1">
									<input  type="text" class="adcSearchTxt inputText_search"  />
						    	</span>
								<span class="btn1">
									<a href="#" class="adcSearchLnk">
										<img src="imgs/meun/btn_search_1.png" alt="검색" />
									</a>
								</span>
								<br class="clearfix" />								
								<select size="5" class="userlist adcsDeselectedSel">
								-->
								<select size="6" multiple="multiple" class="usrselecte adcsDeselectedSel">
									<#list availableAdcs![] as theAdc>
										<option value="${theAdc.index}">${theAdc.name}</option>
									</#list>	
								</select>
							</td>
						</tr>
					</table>				
				</td>
			</tr>
			<tr class="EndLine2">
				<td colspan="2"></td>
			</tr>
		</table>
	</div>
	<div class="adcVsAssignment ">
		<div class="title_h4_1">
			<li>${LANGCODEMAP["MSG_USERADD_ADD_SET"]!}</li>
		</div>
		<table class="Board">
			<colgroup>
				<col width="200px" />
				<col >
			</colgroup>
			<tr class="StartLine">
				<td colspan="2"></td>
			</tr>
			<tr> 
				<th class="Lth1">${LANGCODEMAP["MSG_USERADD_VS_SEL"]!}</th>
	            <td class="Lth0 align_top">
                   	<table width="97%" border="0" cellpadding="0" cellspacing="0">
						<colgroup>
							<col width="48%" />
							<col width="4%"  >
							<col width="48%" />
						</colgroup>
						<tr> 
					  		<td>
					  			<span class="usrselected_th">${LANGCODEMAP["MSG_USERADD_SELECT"]!}</span><span class="usrselected_txt"><span class="selectedAdcsCount txt_blue">${(registeredAdcMap!{})?values?size}</span>&nbsp;${LANGCODEMAP["MSG_USERADD_COUNT_SEL"]!}</span>
					  		</td>
							<td></td>
							<td>
								<span class="userlist_txt">${LANGCODEMAP["MSG_USERADD_LIST"]!}</span>
							</td>
						</tr>
						<tr>
							<td>						
								<div class="usrselecte">									
									<div class="selectedList" id="selectedList">
									<ul>
										<#list (registeredAdcVSMap!{})?values as theAdcVs>
										<li class="availableAdc" id="${theAdcVs.adcIndex!''}" nm="${theAdcVs.adcName!''}" >										
											<a href="#">${theAdcVs.adcName!''}</a>									
											<ul>
												<#list theAdcVs.vsInfoList![] as theVsInfo>
												<li class="availableVs" id="${theVsInfo.vsIndex!''}" nm="${theVsInfo.vsName!''}">
													<a href="#">${theVsInfo.vsName!''}</a>
												</li>									
												</#list>
											</ul>										
										</li>	
										</#list>												
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
								<!--
								<span class="inputTextposition1">
									<input  type="text" class="adcSearchTxt inputText_search"  />
								</span>
								<span class="btn1">
									<a href="#" class="adcSearchLnk">
										<img src="imgs/meun/btn_search_1.png" alt="검색" />
									</a>
								</span>								
								<div class="userlist">
								-->	
								<div class="usrselecte">
									<div class="unSelectedList" id="unSelectedList">
									<ul>
										<#list availableAdcVSs![] as theAdcVs>
										<li class="availableAdc" id="${theAdcVs.adcIndex!''}" nm="${theAdcVs.adcName!''}" >										
											<a href="#">${theAdcVs.adcName!''}</a>									
											<ul>
												<#list theAdcVs.vsInfoList![] as theVsInfo>
												<li class="availableVs" id="${theVsInfo.vsIndex!''}" nm="${theVsInfo.vsName!''}">
													<a href="#">${theVsInfo.vsName!''}</a>
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
	</div>
	<br>
	<div class="adcRsAssignment ">		
		<table class="Board">
			<colgroup>
				<col width="200px" />
				<col >
			</colgroup>
			<tr class="StartLine">
				<td colspan="2"></td>
			</tr>
			<tr> 
				<th class="Lth1">${LANGCODEMAP["MSG_USERADD_RS_SEL"]!}</th>
	            <td class="Lth0 align_top">
                   	<table width="97%" border="0" cellpadding="0" cellspacing="0">
						<colgroup>
							<col width="48%" />
							<col width="4%"  >
							<col width="48%" />
						</colgroup>
						<tr> 
					  		<td>
					  			<span class="usrselected_th">${LANGCODEMAP["MSG_USERADD_SELECT"]!}</span><span class="usrselected_txt"><span class="selectedAdcRsCount txt_blue">${(registeredAdcMap!{})?values?size}</span>&nbsp;${LANGCODEMAP["MSG_USERADD_COUNT_SEL"]!}</span>
					  		</td>
							<td></td>
							<td>
								<span class="userlist_txt">${LANGCODEMAP["MSG_USERADD_LIST"]!}</span>
							</td>
						</tr>
						<tr>
							<td>						
								<div class="usrselecte">									
									<div class="selectedList" id="selectedRsList">								
									<ul>
										<#list (registeredAdcRSMap!{})?values as theAdcRs>
										<li class="availableAdc" id="${theAdcRs.adcIndex!''}" nm="${theAdcRs.adcName!''}" >										
											<a href="#">${theAdcRs.adcName!''}</a>									
											<ul>
												<#list theAdcRs.rsInfoList![] as theRsInfo>
												<li class="availableRs" id="${theRsInfo.rsIndex!''}" nm="${theRsInfo.rsIPAddress!''}">
													<a href="#">${theRsInfo.rsIPAddress!''}</a>
												</li>									
												</#list>
											</ul>										
										</li>	
										</#list>												
									</ul>					
									</div>
								</div>
							</td>
							<td>
								<div class="position_arrow">	
									<a class="toRsSelectionLnk" id="toRsSelectionLnk" href="#">
										<img src="imgs/meun/btn_mov_lft.png" alt="${LANGCODEMAP["MSG_USERADD_MOVE_TO_SEL"]!}" />
									</a>
								</div>
								<div class="position_arrow">
									<a class="toRsDeselectionLnk" id="toRsDeselectionLnk" href="#">
										<img src="imgs/meun/btn_mov_rgt.png" alt="${LANGCODEMAP["MSG_USERADD_MOVE_TO_SEL"]!}" />
									</a>	
								</div>					
							</td>	
							<td>
								<!--
								<span class="inputTextposition1">
									<input  type="text" class="adcSearchTxt inputText_search"  />
								</span>
								<span class="btn1">
									<a href="#" class="adcSearchLnk">
										<img src="imgs/meun/btn_search_1.png" alt="검색" />
									</a>
								</span>								
								<div class="userlist">
								-->	
								<div class="usrselecte">
									<div class="unSelectedList" id="unSelectedRsList">
									<ul>
										<#list availableAdcRSs![] as theAdcRs>
										<li class="availableAdc" id="${theAdcRs.adcIndex!''}" nm="${theAdcRs.adcName!''}" >										
											<a href="#">${theAdcRs.adcName!''}</a>									
											<ul>
												<#list theAdcRs.rsInfoList![] as theRsInfo> 
												<li class="availableRs" id="${theRsInfo.rsIndex!''}" nm="${theRsInfo.rsIPAddress!''}">
													<a href="#">${theRsInfo.rsIPAddress!''}</a>
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
	</div>
	</#if>  
</div>		
<style type="text/css"> 
  .jstree a .jstree-icon { display:none !important; }
  .jstree ul li ul a .jstree-icon { display:none !important; }  
</style>