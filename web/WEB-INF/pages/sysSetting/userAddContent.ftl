<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<div class="contents_area">
	<div>
		<img src="imgs/title${img_lang!""}/h3_userAdd.gif" class="title_h3" />				 
	</div>
	<form id="userAddFrm" class="setting" method="post">
		<!-- 1 --> 
		<div class="title_h4">
			<li>${LANGCODEMAP["MSG_USERADD_DEFAULT_SET"]!}</li>
		</div>
		<table class="Board" cellpadding="0" cellspacing="0">
			<colgroup>
				<col width="200px"/>
				<col >
			</colgroup>			
			<tr class="DivideLine">
				<td colspan="2" class="StartLine"></td>
			</tr>			
			<tr>
				<th class="Lth2">
					<li >${LANGCODEMAP["MSG_USERADD_ID"]!}</li>
				</th>
				<td class="Lth0">
					<#if !(account.id)?? || (account.id)! == ''>
						<input name="account.id" type="text" class="inputText width130" value="${(account.id)!}"/>
					<#else>
						<input disabled="disabled" type="text" class="inputText width130" value="${(account.id)!}"/>
						<input name="account.id" type="hidden" class="inputText width130" value="${(account.id)!}"/>
					</#if>
				</td>
			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_USERADD_PASSWD"]!}</li>
				</th>
				<td class="Lth0">
					<input name="account.password" type="password" class="inputPassword inputText width130" />
					<#if (account.id)??>
						<a class="resetPassword" href="#">
							<img src="imgs/common/btn_init.gif" alt="${LANGCODEMAP["MSG_USERADD_PASSWD_RESET"]!}"/>
						</a>
					</#if>
				</td>
			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_USERADD_PASSWD_CONM"]!}</li>
				</th>
				<td class="Lth0">
					<input name="account.confirmPassword" type="password" class="inputPassword inputText width130">
				</td>
			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_USERADD_NAME"]!}</li>
				</th>
				<td class="Lth0">
					<input name="account.name" type="text" value="${(account.name)!''}" class="inputText width130" />
				</td>
			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th class="Lth2">
					<li>${LANGCODEMAP["MSG_USERADD_ROLE"]!}</li>
				</th>
				<td class="Lth0" >
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
			<#if accountRole == 'system'>
			<tr>
				<th class="Lth1">
					<li>${LANGCODEMAP["MSG_USERLIST_CLONE"]!}</li>
				</th>
				<td class="Lth0" >  
					<select name="accountClone" class="accntClone inputSelect width152">
						<option value="0" selected="selected">선택하세요</option>
					<#list accounts as theAccount>	  				
						<option value="${theAccount.index!''}" >${theAccount.name!''}</option>                 	
					</#list>					
					</select>
					<#if (account.id)??>
					<input type="text" class="roleId" value="${account.roleId!''}"></span>
					</#if>					
				</td>
			</tr>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			</#if>
			<tr>
				<th class="Lth1">
					<li>${LANGCODEMAP["MSG_USERADD_MAIL"]!}</li>
				</th>
				<td class="Lth0" >
					<input name="account.emailBeforeDomain" type="text" id="textfield3" class="inputText width130" value="${(account.emailBeforeDomain)!''}">
						  @
					<input type="text" name="account.emailDomain" id="textfield" class="inputText width130" value="${(account.emailDomain)!''}" />&nbsp;
					<select class="selectableEmailDomain inputSelect width152">
						<option value="">${LANGCODEMAP["MSG_USERADD_DIRECT_INPUT"]!}</option>
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
					<li>${LANGCODEMAP["MSG_USERADD_PHONE_NUMBER"]!}</li>
				</th>
				<td class="Lth0">
					<input type="text" name="account.phone" id="textfield2" class="inputText width130" value="${(account.phone)!''}"><!--&nbsp;<span class="example">(010-0000-0000)</span>-->
					<span class="txt_gray2">${LANGCODEMAP["MSG_USERADD_PHONE_NOTICE"]!}</span>
				</td>
			<tr class="DivideLine">
				<td colspan="2"></td>
			</tr>
			<tr>
				<th class="Lth1">
					<li>${LANGCODEMAP["MSG_USERADD_DESCRIPTION"]!}</li>
				</th>
				<td class="Lth0">
					<textarea name="account.description" class="desc valdescription" cols="90%">${(account.description)!''}</textarea>
					<!--<input name="account.description" class="desc" value="${(account.description)!''}">	-->
				</td>
			</tr>
			<#if accountRole == 'system'>
				<tr class="DivideLine">
					<td colspan="2"></td>
				</tr>
				<tr>
				<th class="Lth1">
					<li>${LANGCODEMAP["MSG_USERADD_USE_PERIOD"]!}</li>
				</th>
					<td class="Lth0">
						<input type="radio" name="account.accountMode" id="unLimitedMode" checked="checked" value="unLimitedMode" />								
							<label for="unLimitedMode">${LANGCODEMAP["MSG_USERADD_PERMANENT_USE"]!}</label>	&nbsp;&nbsp;&nbsp;
						<input type="radio" name="account.accountMode" id="LimitedMode" value="LimitedMode" />								
							<label for="LimitedMode">${LANGCODEMAP["MSG_USERADD_PERIOD_SET"]!}
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
				<tr class="DivideLine">
					<td colspan="2"></td>
				</tr>
			</#if>
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
		</table>	   
		<!-- 2 --> 
		<div class="title_h4_1">
			<li>${LANGCODEMAP["MSG_USERADD_ALERT_SET"]!}</li>
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
				<td class="Lth0-25">
					<input name="account.usesAlertWnd" class="alert-type-btn" type="radio" id="AlertWnd1" value="0"/> 
					<label for="AlertWnd1">${LANGCODEMAP["MSG_USERADD_NOT_USED"]!}</label>				
				</td>	
			</tr>
			<tr class="DivideLine">
				<td ></td>
			</tr>		
			<tr>
				<td class="Lth0-25" >
					<input name="account.usesAlertWnd" class="alert-type-btn" type="radio" id="AlertWnd2" checked="checked" value="1"/> 
					<label for="AlertWnd2">${LANGCODEMAP["MSG_USERADD_POPUP_WINDOW"]!}</label>	&nbsp;	
					<input name="account.usesAlertBeep" type="checkbox" id="usesAlertBeep" checked="checked" value="true"/>
					<label for="usesAlertBeep">${LANGCODEMAP["MSG_USERADD_BEEP"]!}</label>							
				</td>		
			</tr>
			<tr class="DivideLine">
				<td ></td>
			</tr>
			<tr>
				<td class="Lth0-25">
					<input name="account.usesAlertWnd" class="alert-type-btn" type="radio" id="AlertWnd3" value="2"/> 
					<label for="AlertWnd3">${LANGCODEMAP["MSG_USERADD_TICKER"]!}</label> 					
				</td>							
			</tr>
			<tr class="EndLine2">
				<td colspan="2"></td>
			</tr>
		</table>
		
		<div>
			<#include "userAddRoleContent.ftl"/>	
		</div>
	</form>
	<tr> 
		<td colspan="4">                            
			<div class="position_cT10">
			<#if accountRole == 'system'>
				<input type="button" class="userAddOkLnk Btn_red" value="${LANGCODEMAP["MSG_USERADD_COMPLETE"]!}"/> 			
			</#if>
		  		<input type="button" class="userAddCancelLnk Btn_white" value="${LANGCODEMAP["MSG_USERADD_CANCEL"]!}"/>   
			</div> 
		</td>
	</tr> 
</div>   
<style type="text/css"> 
  .jstree a .jstree-icon { display:none !important; }
  .jstree ul li ul a .jstree-icon { display:none !important; }  
</style>
