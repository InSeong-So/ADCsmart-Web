<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area" >
	<div>
		<img src="imgs/title${img_lang!""}/h3_userModify.gif" class="title_h3" />		
	</div>
	<form id="userAddFrm" class="setting" method="post">
		<input name="account.index" class="none" value="${(account.index)!}" />
		<!-- 1 --> 
		<div class="title_h4">
			<li>${LANGCODEMAP["MSG_USERMODIFY_DEFAULT_SET"]!}</li>
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
					<li>${LANGCODEMAP["MSG_USERMODIFY_ID"]!}</li>
				</th>
				<td class="Lth0">
					<#if !(account.id)?? || (account.id)! == ''>
						<input name="account.id" type="text"  class="inputText width130"  value="${(account.id)!}"/>
					<#else>
						<input disabled="disabled" type="text"  class="inputText width130"  value="${(account.id)!}"/>
						<input name="account.id" type="hidden"  value="${(account.id)!}"/>
					</#if>
				</td>
  			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr>
			  	<th class="Lth2">
			  		<li>${LANGCODEMAP["MSG_USERMODIFY_PASSWD"]!}</li>
			  	</th>
			  	<td class="Lth0">
				<input type="button" class="popUpChangePasswdWndLnk pwchange_width Btn_white" value="${LANGCODEMAP["MSG_USERMODIFY_PASSWD_CHANGE"]!}"/>  			  								
				</td>
     		</tr>						
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th class="Lth2">
			  		<li>${LANGCODEMAP["MSG_USERMODIFY_NAME"]!}</li>
			  	</th>	
				<td class="Lth0">
					<input name="account.name" type="text" class="inputText width130" value="${(account.name)!''}" />
				</td>
			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_USERMODIFY_ROLE"]!}</li>
				</th>
				<td class="Lth0">
					<select name="account.roleId" class="inputSelect width152">
						<#list roles as theRole>
							<#if accountRole == 'system'>
					  			<option value="${theRole.id}" ${(theRole.id == (account.roleId)!3)?string('selected="selected"', '')}>${theRole.name}</option>
					  		<#else>
					  			<#if theRole.id == (account.roleId)!3>
					  				<option value="${theRole.id}" selected="selected">${theRole.name}</option>
					  			</#if>
					  		</#if>
						</#list>
					</select>
				</td>
			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th class="Lth1">
					<li>${LANGCODEMAP["MSG_USERMODIFY_MAIL"]!}</li>
				</th>
				<td class="Lth0">
					<input name="account.emailBeforeDomain" type="text" id="textfield3" class="inputText width130" value="${(account.emailBeforeDomain)!''}">
				 	@
			    	<input type="text" name="account.emailDomain" id="textfield" class="inputText width130" value="${(account.emailDomain)!''}" />&nbsp;
			        <select class="selectableEmailDomain width130">
				        <option value="">${LANGCODEMAP["MSG_USERMODIFY_DIRECT_INPUT"]!}</option>
						<option value="naver.com">naver.com</option>                     
						<option value="nate.com">nate.com</option>                     
						<option value="dreamwiz.com">dreamwiz.com</option>
						<option value="yahoo.co.kr">yahoo.co.kr</option>
						<option value="empal.com">empal.com</option>
						<option value="unitel.co.kr">unitel.co.kr</option>
						<option value="gmail.com">gmail.com</option>
						<option value="korea.com">korea.com</option>
						<option value="chol.com">chol.com</option>
						<option value="paran.com">paran.com</option>
						<option value="freechal.com">freechal.com</option>
						<option value="hanmail.net">hanmail.net</option>
						<option value="hotmail.com">hotmail.com</option>
      				</select>
		        </td>
			</tr>      
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th class="Lth1">
					<li>${LANGCODEMAP["MSG_USERMODIFY_PHONE_NUMBER"]!}</li>
				</th>
				<td class="Lth0">
					<input type="text" name="account.phone" id="textfield2" class="inputText width130" value="${(account.phone)!''}">
					<span class="txt_gray2">${LANGCODEMAP["MSG_USERADD_PHONE_NOTICE"]!}</span>
				</td>
			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th class="Lth1">
					<li>${LANGCODEMAP["MSG_USERMODIFY_DESCRIPTION"]!}</li>
				</th>
				<td class="Lth0">
					<textarea name="account.description" class="desc valmoddescription" cols="90%">${(account.description)!''}</textarea>
					<!--
					<input name="account.description" class="desc">
					<span>${(account.description)!''}</span>
					-->	
					<!--<input name="account.description" class="desc" value="${(account.description)!''}">		-->					
				</td>
			</tr>
			<tr style="display:none">
                <input name="account.history" class="none" value="${(account.history)!}" />
            </tr>
			<#if accountRole == 'system'>
				<tr class="DivideLine">
					<td colspan="2"></td>
				</tr>
				<tr>
				<th class="Lth1">
					<li>${LANGCODEMAP["MSG_USERMODIFY_USE_PERIOD"]!}</li>
				</th>
					<td class="Lth0">
					<#if (account.accountMode)! == "unLimitedMode">
						<input type="radio" name="account.accountMode" id="unLimitedMode" checked="checked" value="unLimitedMode" />
					<#else>
						<input type="radio" name="account.accountMode" id="unLimitedMode" value="unLimitedMode" />
					</#if>					
						<label for="unLimitedMode">${LANGCODEMAP["MSG_USERMODIFY_PERMANENT_USE"]!}</label>	&nbsp;&nbsp;&nbsp;				
					<#if (account.accountMode)! == "LimitedMode">
						<input type="radio" name="account.accountMode" id="LimitedMode" checked="checked" value="LimitedMode" />
					<#else>
						<input type="radio" name="account.accountMode" id="LimitedMode" value="LimitedMode" />
					</#if>					
							<label for="LimitedMode">${LANGCODEMAP["MSG_USERMODIFY_PERIOD_SET"]!}
								<input name="account.startTime" class="inputText_calendar2" type="text" value="${(account.startTime?string("yyyy-MM-dd"))!}" title="${LANGCODEMAP["MSG_USERADD_ACCO_PERI_SEL"]!}" readonly/>
									&nbsp;~&nbsp;
								<input name="account.endTime" class="inputText_calendar2" type="text" value="${(account.endTime?string("yyyy-MM-dd"))!}" title="${LANGCODEMAP["MSG_USERADD_ACCO_PERI_SEL"]!}" readonly/>
							</label>
					</td>
				</tr>
				<tr class="DivideLine">
					<td colspan="2"></td>
				</tr>
			<#else>				
				<div style="display: none;">
					<#if (account.accountMode)! == "unLimitedMode">
						<input type="hidden" name="account.accountMode" id="unLimitedMode" value="unLimitedMode" />
					<#else>
						<input type="hidden" name="account.accountMode" id="LimitedMode" value="LimitedMode" />
					</#if>
					<input name="account.startTime" class="" type="text" value="${(account.startTime?string("yyyy-MM-dd"))!}" title="${LANGCODEMAP["MSG_USERMODIFY_ACCO_PERI_SEL"]!}" />
					<input name="account.endTime" class="" type="text" value="${(account.endTime?string("yyyy-MM-dd"))!}" title="${LANGCODEMAP["MSG_USERMODIFY_ACCO_PERI_SEL"]!}" />
				</div>
				<tr class="DivideLine">
					<td colspan="2"></td>
				</tr>
			</#if>
			<#if accountRole == 'system'>
			<tr>
				<th class="Lth1">	
					<li>${LANGCODEMAP["MSG_USERADD_IPFILTER"]!}</li>
				</th>
				<td class="Lth0">
					<input type="text" name="account.ipFilter" value="${(account.ipFilter)!""}" class="inputText width130" /> ${LANGCODEMAP["MSG_USERADD_IPFILTER_FORMAT"]!}
				</td>
			</tr>
			<tr class="EndLine2">
				<td colspan="2"></td>
			</tr>
			</#if>
		</table>
		<!-- 2 --> 
		<div class="title_h4_1">
			<li>${LANGCODEMAP["MSG_USERMODIFY_ALERT_SET"]!}</li>
		</div>
	<table class="Board" cellpadding="0" cellspacing="0">
		<colgroup>
			<col width="200px"/>
			<col >			
		</colgroup>		
		<tr class="StartLine">
			<td colspan="2"></td>
		</tr>
		<tr>
			<th class="Lth1" rowspan="5">
				<li>${LANGCODEMAP["MSG_USERADD_ALERT_TYPE"]!}</li>
			</th>
			<td class="Lth0-25" >
			<#if (account.usesAlertWnd) == 0 >
				<input name="account.usesAlertWnd" class="alert-type-btn" type="radio" id="AlertWnd1" checked="checked" value="0"/>
			<#else>
				<input name="account.usesAlertWnd" class="alert-type-btn" type="radio" id="AlertWnd1" value="0"/>
			</#if>
				<label for="AlertWnd1">${LANGCODEMAP["MSG_USERADD_NOT_USED"]!}</label>				
			</td>	
		</tr>
		<tr class="DivideLine">
			<td></td>
		</tr>		
		<tr>
			<td class="Lth0-25" >
			<#if (account.usesAlertWnd) == 1 >
				<input name="account.usesAlertWnd" class="alert-type-btn" type="radio" id="AlertWnd2" checked="checked" value="1"/>
			<#else>
				<input name="account.usesAlertWnd" class="alert-type-btn" type="radio" id="AlertWnd2" value="1"/>
			</#if>
				<label for="AlertWnd2">${LANGCODEMAP["MSG_USERADD_POPUP_WINDOW"]!}</label> &nbsp;
				<#if (account.usesAlertWnd) == 1 >
	 				<#if (account.usesAlertBeep)!true>
	 					<input name="account.usesAlertBeep" type="checkbox" checked="checked" id="usesAlertBeep" value="true"/>
					<#else>
						<input name="account.usesAlertBeep" type="checkbox" id="usesAlertBeep" value="true"/>
					</#if>
				<#else>
						<input name="account.usesAlertBeep" type="checkbox" id="usesAlertBeep" value="true" disabled="disabled"/>
				</#if>
				<label for="usesAlertBeep">${LANGCODEMAP["MSG_USERADD_BEEP"]!}</label>				
			</td>			
		</tr>
		<tr class="DivideLine">
			<td></td>
		</tr>			
		<tr>
			<td class="Lth0-25" colspan="2">
			<#if (account.usesAlertWnd) == 2>
				<input name="account.usesAlertWnd" class="alert-type-btn" type="radio" checked="checked" id="AlertWnd3" value="2"/>
			<#else>
				<input name="account.usesAlertWnd" class="alert-type-btn" type="radio" id="AlertWnd3" value="2"/>
			</#if>
				<label for="AlertWnd3">${LANGCODEMAP["MSG_USERADD_TICKER"]!}</label> 					
			</td>								
		</tr>
		<tr class="EndLine2">
			<td colspan="32"></td>
		</tr>
	</table>		   
		<!-- 3 --> 
		<#if accountRole == 'system'>
		<div class="adcAssignment">
			<div class="title_h4_1">
				<li>${LANGCODEMAP["MSG_USERMODIFY_ADD_SET"]!}</li>
			</div>			
			<table class="Board">
				<colgroup>
					<col width="200px"/>
					<col >
				</colgroup>
				<tr class="StartLine">
					<td colspan="2"></td>
				</tr>
				<tr> 
					<th class="Lth1">${LANGCODEMAP["MSG_USERMODIFY_ADC_SEL"]!}</th>
		            <td class="Lth0 align_top">			
                    	<table  width="97%" border="0" cellpadding="0" cellspacing="0">
							<colgroup>
								<col width="48%" />
								<col width="4%"  >
								<col width="48%" />
							</colgroup>
                           	<tr> 
					  			<td>
					  				<span class="usrselected_th">${LANGCODEMAP["MSG_USERMODIFY_SELECT"]!}</span><span class="usrselected_txt"><span class="selectedAdcsCount txt_blue">${(registeredAdcMap!{})?values?size}</span>&nbsp;${LANGCODEMAP["MSG_USERADD_COUNT_SEL"]!}</span>
					  			</td>							
								<td></td>
								<td>
									<span class="userlist_txt">${LANGCODEMAP["MSG_USERMODIFY_LIST"]!}</span>
								</td>
					       	</tr>  			
							<tr>
								<td>			
						   			<select name="account.adcVsList.adcIndex" size="7" multiple="multiple" class="adcsSelectedSel usrselecte">
									<#list (registeredAdcMap!{})?values as theAdc>
										<option value="${theAdc.index}">${theAdc.name}</option>
									</#list>
									</select>
								</td>
								<td>
								<div class="position_arrow">
								  <a class="toAdcSelectionLnk" href="#">
								  	<img src="imgs/meun/btn_mov_lft.png" alt="${LANGCODEMAP["MSG_USERMODIFY_MOVE_TO_SEL"]!}" />
								  </a>
								 </div>
								 <div class="position_arrow">
								  <a class="toAdcDeselectionLnk" href="#">
								  	<img src="imgs/meun/btn_mov_rgt.png" alt="${LANGCODEMAP["MSG_USERMODIFY_MOVE_TO_SEL"]!}" />
								  </a></div>
								</td>
								<td>
									<!--
									<span class="inputTextposition1">
										<input type="text" class="adcSearchTxt inputText_search"  />
									</span>
									<span class="btn1">
										<a href="#" class="adcSearchLnk">
											<img src="imgs/meun/btn_search_1.png" alt="검색" />
										</a>
									</span>
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
					<td colspan="4"></td>
				</tr>
			</table>
		</div>
		<div class="adcVsAssignment">
			<div class="title_h4_1">
				<li>${LANGCODEMAP["MSG_USERMODIFY_ADD_SET"]!}</li>
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
					<th class="Lth1">${LANGCODEMAP["MSG_USERMODIFY_VS_SEL"]!}</th>
		            <td class="Lth0 align_top">
                       	<table  width="97%" border="0" cellpadding="0" cellspacing="0">
							<colgroup>
								<col width="48%" />
								<col width="4%"  >
								<col width="48%" />
							</colgroup>
							<tr> 
						  		<td>
									<span class="usrselected_th">${LANGCODEMAP["MSG_USERMODIFY_SELECT"]!}</span><span class="usrselected_txt"><span class="selectedAdcsCount txt_blue">${(registeredAdcMap!{})?values?size}</span>&nbsp;${LANGCODEMAP["MSG_USERMODIFY_COUNT_SEL"]!}</span>
						  		</td>
								<td></td>
								<td>
									<span class="userlist_txt">${LANGCODEMAP["MSG_USERMODIFY_LIST"]!}</span>
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
										<img src="imgs/meun/btn_mov_lft.png" alt="${LANGCODEMAP["MSG_USERMODIFY_MOVE_TO_SEL"]!}" />
									</a>
									</div>
									<div class="position_arrow">
									<a class="toVsDeselectionLnk" id="toVsDeselectionLnk" href="#">
										<img src="imgs/meun/btn_mov_rgt.png" alt="${LANGCODEMAP["MSG_USERMODIFY_MOVE_TO_SEL"]!}" />
									</a></div>								
								</td>
								<td>
									<!--
									<span class="inputTextposition1">
										<input type="text" class="adcSearchTxt  inputText_search"  />
									</span>
									<span class="btn1">
										<a href="#" class="adcSearchLnk">
											<img src="imgs/meun/btn_search_1.png" alt="검색" />
										</a>
									</span>									
									<br class="clearfix" />									
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
			    	<td colspan="4"></td>
		        </tr>
			</table>
		</div>
		<br>
		<div class="adcRsAssignment">			
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
                       	<table  width="97%" border="0" cellpadding="0" cellspacing="0">
							<colgroup>
								<col width="48%" />
								<col width="4%"  >
								<col width="48%" />
							</colgroup>
							<tr> 
						  		<td>
									<span class="usrselected_th">${LANGCODEMAP["MSG_USERMODIFY_SELECT"]!}</span><span class="usrselected_txt"><span class="selectedAdcRsCount txt_blue">${(registeredAdcMap!{})?values?size}</span>&nbsp;${LANGCODEMAP["MSG_USERMODIFY_COUNT_SEL"]!}</span>
						  		</td>
								<td></td>
								<td>
									<span class="userlist_txt">${LANGCODEMAP["MSG_USERMODIFY_LIST"]!}</span>
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
										<img src="imgs/meun/btn_mov_lft.png" alt="${LANGCODEMAP["MSG_USERMODIFY_MOVE_TO_SEL"]!}" />
									</a>
									</div>
									<div class="position_arrow">
									<a class="toRsDeselectionLnk" id="toRsDeselectionLnk" href="#">
										<img src="imgs/meun/btn_mov_rgt.png" alt="${LANGCODEMAP["MSG_USERMODIFY_MOVE_TO_SEL"]!}" />
									</a></div>								
								</td>
								<td>
									<!--
									<span class="inputTextposition1">
										<input type="text" class="adcSearchTxt  inputText_search"  />
									</span>
									<span class="btn1">
										<a href="#" class="adcSearchLnk">
											<img src="imgs/meun/btn_search_1.png" alt="검색" />
										</a>
									</span>									
									<br class="clearfix" />									
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
			    	<td colspan="4"></td>
		        </tr>
			</table>
		</div>
		</#if>
	</form>
	<tr> 
		<td colspan="4">                            
			<div class="position_cT10">	
				<input type="button" class="userAddOkLnk Btn_red" value="${LANGCODEMAP["MSG_USERMODIFY_COMPLETE"]!}"/>  			
			<#if accountRole == 'system'>
				<input type="button" class="userAddCancelLnk Btn_white" value="${LANGCODEMAP["MSG_USERMODIFY_CANCEL"]!}"/> 			
			</#if>
			</div> 
		</td>
	</tr>
 </div>   
<!-- 비밀번호 변경 팝업 -->
<div id="pwChange" class="pop_type_wrap">
	<h2>${LANGCODEMAP["MSG_USERMODIFY_PASSWD_CHANGE"]!}</h2>
	<div class="pop_contents">
		<p class="setpwd">
			<span>${LANGCODEMAP["MSG_USERMODIFY_PASSWD"]!}</span><input name="password" class="password" type="password" value="${(password)!}"/>
		</p>
		<p class="setpwd_btm">
			<span>${LANGCODEMAP["MSG_USERADD_PASSWD_CONM"]!}</span><input name="confirmPassword" class="confirmPassword" type="password" value="${(confirmPassword)!}"/>
		</p>
		<div class="center mar_top10">
			<input type="button" class="onOk Btn_red" value="${LANGCODEMAP["MSG_USERMODIFY_COMPLETE"]!}"/>   					
			<input type="button" class="onCancel Btn_white" value="${LANGCODEMAP["MSG_USERMODIFY_CANCEL"]!}"/>  		
		</div> 		
	</div>
	<p class="close">
		<a href="#" title="${LANGCODEMAP["MSG_USERADD_CLOSE"]!}">
			<img src="imgs/popup/btn_clse.gif" alt="${LANGCODEMAP["MSG_USERADD_CLOSE"]!}" />
		</a>
	</p>
</div>
<!-- 비밀번호 변경 팝업 -->
<script type="text/javascript">
	$(function()
	{
		// input text width always 100%
		input100('input.sch');
	});
</script>
<style type="text/css"> 
  .jstree a .jstree-icon { display:none !important; }
  .jstree ul li ul a .jstree-icon { display:none !important; }  
</style>
