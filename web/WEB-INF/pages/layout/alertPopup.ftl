<#list alertData.alertList![] as AlertLog>
	<#if AlertLog.type ??>
		<#if 0 == AlertLog.type><#assign faultType=1></#if>
		<#if 1 == AlertLog.type><#assign warningType=1></#if>
	</#if>
</#list>

<div id="alert-popup" class="pop_type_wrap_alert">
    <div class="alert-header-area">
    <h2>${LANGCODEMAP["MSG_ALERT_POP_REAL"]!} <span class="alertCount txt_yellow">${alertData.alertCount!""}</span>
	     <#if alertData.alertCount < 101>     	
	     <#else>
	      ${LANGCODEMAP["MSG_ALERT_POP_COUNT_ING"]!} <span class="txt_yellow">100</span>
	     </#if>
	     ${LANGCODEMAP["MSG_ALERT_POP_COUNT_ALERT"]!}
	     <span class="txt_alertTime_margin">
	     	<img src="imgs/icon/iocn_alert_time.png" />
	     		<span class="txt_alertTime"> ${(nowTime?string("yyyy-MM-dd HH:mm:ss"))!}</span>
	     </span>
    </h2>
    </div>
    <div class="pop2_contents">        
        <div class="description condition">
	        <div>
				<ul class="tabs_3">
					<li>
					    <a class="css_textCursor alert-all" style="background-color:#666666; color: #fff;">
					        <span class="">${LANGCODEMAP["MSG_ALERT_POP_ALL"]!}</span>
					    </a>
					</li>
					<#if faultType?default(0) == 1>
					<li>
					    <a class="css_textCursor alert-fault">
					        <span class="">${LANGCODEMAP["MSG_ALERT_POP_FALUT"]!}</span>
					    </a>
					</li>
					</#if>
					<#if warningType?default(0) == 1>
					<li>
					    <a class="css_textCursor alert-warning">
					        <span class="">${LANGCODEMAP["MSG_ALERT_POP_WARNING"]!}</span>
					    </a>
					</li>
					</#if>				                   
				</ul>
	        </div>
	        <input type="hidden" class="alert-countset" value="${alertData.alertCount}">
	        <div class="alert-table-area">
	        	<#include "alertPopupTableIn.ftl"/>
	        </div>                               
		</div>        
		<div class="position_cT10">
			<input type="button" class="moveWndLnk Btn_white" value="${LANGCODEMAP["MSG_ALERT_POP_MOVE_ALERT"]!}"/>
			<input type="button" class="closeWndLnk Btn_white" value="${LANGCODEMAP["MSG_ALERT_POP_CLOSE"]!}"/>   			
			<span class="none alertBeep"></span> 
		</div>
	</div>
</div>