<#if orderOption.orderType??>
	<#if 33 == orderOption.orderType></#if><!-- OCCUR_TIME -->
	<#if 34 == orderOption.orderType></#if><!-- ADC_NAME -->
	<#if 35 == orderOption.orderType></#if><!-- TYPE -->
</#if>
<#if orderOption.orderDirection??>
	<#if 1 == orderOption.orderDirection></#if><!--Asc-->
	<#if 2 == orderOption.orderDirection></#if><!--Desc-->
</#if>
<table class="Board_100" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
              <colgroup>
              	  <col width="40px" />
                  <col width="140px" />
                  <col width="140px" />
                  <col width="100px" />
                  <col width="auto" />
              </colgroup>
              <tr class="StartLine">
                  <td colspan="5"></td>
              </tr>
              <tr class="ContentsHeadLine">
                  <th>No.</th>
                  <th>
                  	<span class="css_textCursor">
							<#if orderOption.orderDirection == 2 && orderOption.orderType == 33>		
								<a class="orderDir_Desc">${LANGCODEMAP["MSG_ALERT_POP_OCCURTIME"]!}
									<img src="imgs/Desc.gif"/>
									<span class="none orderType">33</span>
									<span class="none orderDir">1</span>					
								</a>
							<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 33>	
								<a class="orderDir_Asc">${LANGCODEMAP["MSG_ALERT_POP_OCCURTIME"]!}
									<img src="imgs/Asc.gif"/>
									<span class="none orderType">33</span>
									<span class="none orderDir">2</span>
								</a>
							<#else>
								<a class="orderDir_None">${LANGCODEMAP["MSG_ALERT_POP_OCCURTIME"]!}
									<img src="imgs/none.gif"/>
									<span class="none orderType">33</span>
									<span class="none orderDir">2</span>
								</a>
							</#if>
						</span>
					</th>
                  <th>	
                  	<span class="css_textCursor">
							<#if orderOption.orderDirection == 2 && orderOption.orderType == 34>		
								<a class="orderDir_Desc">${LANGCODEMAP["MSG_ALERT_POP_ADCNAME"]!}
									<img src="imgs/Desc.gif"/>
									<span class="none orderType">34</span>
									<span class="none orderDir">1</span>					
								</a>						
							<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 34>	
								<a class="orderDir_Asc">${LANGCODEMAP["MSG_ALERT_POP_ADCNAME"]!}
									<img src="imgs/Asc.gif"/>
									<span class="none orderType">34</span>
									<span class="none orderDir">2</span>		
								</a>
							<#else>
								<a class="orderDir_None">${LANGCODEMAP["MSG_ALERT_POP_ADCNAME"]!}
									<img src="imgs/none.gif"/>
									<span class="none orderType">34</span>
									<span class="none orderDir">2</span>		
								</a>
							</#if>
						</span>
					</th>
                  <th>
                  	<span class="css_textCursor">
							<#if orderOption.orderDirection == 2 && orderOption.orderType == 35>		
								<a class="orderDir_Desc">${LANGCODEMAP["MSG_ALERT_POP_TYPE"]!}
									<img src="imgs/Desc.gif"/>
									<span class="none orderType">35</span>
									<span class="none orderDir">1</span>					
								</a>						
							<#elseif orderOption.orderDirection == 1 && orderOption.orderType == 35>	
								<a class="orderDir_Asc">${LANGCODEMAP["MSG_ALERT_POP_TYPE"]!}
									<img src="imgs/Asc.gif"/>
									<span class="none orderType">35</span>
									<span class="none orderDir">2</span>		
								</a>
							<#else>
								<a class="orderDir_None">${LANGCODEMAP["MSG_ALERT_POP_TYPE"]!}
									<img src="imgs/none.gif"/>
									<span class="none orderType">35</span>
									<span class="none orderDir">2</span>		
								</a>
							</#if>
						</span>                    
                  </th>                
                  <th>${LANGCODEMAP["MSG_ALERT_POP_CONTENT"]!}</th>                                        
              </tr>
              <tr class="StartLine1">
                  <td colspan="5"></td>
              </tr>
          </table>
      <div class="listHeight200">
          <table class="Board_100" id="alertDataTable" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
              <colgroup>
             	  <col width="40px" />
                  <col width="130px" />
                  <col width="140px" />
                  <col width="100px" />                
                  <col width="auto" />                                                                
              </colgroup>
              <#list alertData.alertList![] as AlertLog>
              <tr class="ContentsLine">
              	  <td class="align_center">${AlertLog_index+1}</td>
                  <td class="align_center">${(AlertLog.occurTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
                  <td class="align_left_P10 textOver">${AlertLog.adcName!""}</td>
                  <td class="lign_left_P10 textOver type">
                  	<#if AlertLog.type == 0>
							<#if AlertLog.status == 1>
								<img src="imgs/icon/icon_critical_none.gif" alt="${LANGCODEMAP["MSG_LFAULT_FAULT_RESOLVE"]!}">${LANGCODEMAP["MSG_LFAULT_FAULT_RESOLVE"]!}
							<#elseif AlertLog.status == 0>
								<img src="imgs/icon/icon_critical.gif" alt="${LANGCODEMAP["MSG_LFAULT_FAULT_OCCUR"]!}">${LANGCODEMAP["MSG_LFAULT_FAULT_OCCUR"]!}
							<#else>
							</#if>					
					<#elseif AlertLog.type == 1>
						<img src="imgs/icon/icon_warning.gif" alt="${LANGCODEMAP["MSG_FAULT_WARNING"]!}">${LANGCODEMAP["MSG_FAULT_WARNING"]!}
					<#else>
					</#if>                   
                  <td class="ContentsTxt">
                      ${AlertLog.event!""}      
                  </td>                            
              </tr>
              <tr class="DivideLine">
                  <td colspan="5"></td>
              </tr>
              </#list>
			</table>
			<input type="hidden" class="alert-resetCount" value="${alertData.alertCount}">
		</div>