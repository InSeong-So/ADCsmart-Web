<#setting number_format="0.####">
<br class="clearfix">
	<!-- 1 장애 경보-->
	<div class="title_h4_1">
		<li>${LANGCODEMAP["MSG_ALERT_FAULT"]!}</li>
	</div>
	<table class="Board_conn" cellpadding="0" cellspacing="0">
		<colgroup>
            <col width="50%" />
            <col width="10%" />
            <col width="10%" />
            <col width="10%" />
            <col width="10%" />
            <col width="10%" />
		</colgroup>		
		<thead>	
		<tr style="display:none">
			<td colspan="4">
				<input type="hidden" class="configLevelHidden" value="${(alarmConfig.configLevel)!''}" />
				<input type="hidden" name="alarmConfigs.adcType" value="${(alarmConfig.adcType)}" />
				<input type="hidden" name="alarmConfigs.object.category" value="${(alarmConfig.object.category)}" />
				<input type="hidden" name="alarmConfigs.object.index" value="${(alarmConfig.object.index)}" />
				<input type="hidden" name="alarmConfigs.object.vendor" value="${(alarmConfig.object.vendor)}" />		
			</td>
		</tr>			
		<tr>
			<th>${LANGCODEMAP["MSG_ALERT_ARTICLE"]!}</th>
			<th><input class="allEnableChk" type="checkbox" name="checkbox" id="checkbox">&nbsp;${LANGCODEMAP["MSG_ALERT_SAVE"]!}</th>	
			<th>${LANGCODEMAP["MSG_ALERT_PERIOD"]!}</th>	
			<th><input class="allSyslogChk" type="checkbox" name="checkbox" id="checkbox" disabled="disabled">&nbsp;${LANGCODEMAP["MSG_ALERT_ACTION_SYSLOG"]!}</th>
			<th><input class="allSnmpTrapChk" type="checkbox" name="checkbox" id="checkbox" disabled="disabled">&nbsp;${LANGCODEMAP["MSG_ALERT_ACTION_SNMPTRAP"]!}</th>			
            <th><input class="allSMSChk" type="checkbox" name="checkbox" id="checkbox" disabled="disabled">&nbsp;SMS전송</th>         
		</tr>
		</thead>		
		<tbody>		
		<tr>
			<th>${LANGCODEMAP["MSG_ALERT_ADC_DISCONNECT"]!}</th>
			<td>
				<#if (alarmConfig.adcDisconnectAction.enable) == 1>
					<input class="enableChk" id="alarmConfigs.adcDisconnectAction.enable" id_h="adcDisconnectAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="enableChk" id="alarmConfigs.adcDisconnectAction.enable" id_h="adcDisconnectAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
					<input type="hidden" id="adcDisconnectAction_enable_h" name="alarmConfigs.adcDisconnectAction.enable" value="${(alarmConfig.adcDisconnectAction.enable)}" />
			</td>
			<td>
				-
			</td>	
			<td>
				<#if (alarmConfig.adcDisconnectAction.syslog) == 1>
					<input class="syslogChk" id="alarmConfigs.adcDisconnectAction.syslog" id_h="adcDisconnectAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="syslogChk" id="alarmConfigs.adcDisconnectAction.syslog" id_h="adcDisconnectAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
					<input type="hidden" id="adcDisconnectAction_syslog_h" name="alarmConfigs.adcDisconnectAction.syslog" value="${(alarmConfig.adcDisconnectAction.syslog)}" />
			</td>
			<td>
				<#if (alarmConfig.adcDisconnectAction.snmptrap) == 1>
					<input class="snmptrapChk" id="alarmConfigs.adcDisconnectAction.snmptrap" id_h="adcDisconnectAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="snmptrapChk" id="alarmConfigs.adcDisconnectAction.snmptrap" id_h="adcDisconnectAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
					<input type="hidden" id="adcDisconnectAction_snmptrap_h" name="alarmConfigs.adcDisconnectAction.snmptrap" value="${(alarmConfig.adcDisconnectAction.snmptrap)}" />
			</td>			
            <td>
                <#if (alarmConfig.adcDisconnectAction.sms) == 1>
                    <input class="smsChk" id="alarmConfigs.adcDisconnectAction.sms" id_h="adcDisconnectAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.adcDisconnectAction.sms" id_h="adcDisconnectAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                </#if>
                    <input type="hidden" id="adcDisconnectAction_sms" name="alarmConfigs.adcDisconnectAction.sms" value="${(alarmConfig.adcDisconnectAction.sms)}" />
            </td>           
		</tr>
		<tr>
			<th>${LANGCODEMAP["MSG_ALERT_ADC_RESTART"]!}</th>
			<td>
				<#if (alarmConfig.adcBootAction.enable) == 1>
					<input class="enableChk" id="alarmConfigs.adcBootAction.enable" id_h="adcBootAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="enableChk" id="alarmConfigs.adcBootAction.enable" id_h="adcBootAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="adcBootAction_enable_h" name="alarmConfigs.adcBootAction.enable" value="${(alarmConfig.adcBootAction.enable)}" />
			</td>	
			<td>
				-
			</td>	
			<td>
				<#if (alarmConfig.adcBootAction.syslog) == 1>
					<input class="syslogChk" id="alarmConfigs.adcBootAction.syslog" id_h="adcBootAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="syslogChk" id="alarmConfigs.adcBootAction.syslog" id_h="adcBootAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="adcBootAction_syslog_h" name="alarmConfigs.adcBootAction.syslog" value="${(alarmConfig.adcBootAction.syslog)}" />
			</td>		
			<td>
				<#if (alarmConfig.adcBootAction.snmptrap) == 1>
					<input class="snmptrapChk" id="alarmConfigs.adcBootAction.snmptrap" id_h="adcBootAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="snmptrapChk" id="alarmConfigs.adcBootAction.snmptrap" id_h="adcBootAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="adcBootAction_snmptrap_h" name="alarmConfigs.adcBootAction.snmptrap" value="${(alarmConfig.adcBootAction.snmptrap)}" />
			</td>	
            <td>
                <#if (alarmConfig.adcBootAction.sms) == 1>
                    <input class="smsChk" id="alarmConfigs.adcBootAction.sms" id_h="adcBootAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.adcBootAction.sms" id_h="adcBootAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                </#if>
                <input type="hidden" id="adcBootAction_sms_h" name="alarmConfigs.adcBootAction.sms" value="${(alarmConfig.adcBootAction.sms)}" />
            </td>   
		</tr>
		<tr>
			<th>${LANGCODEMAP["MSG_ALERT_SWITCH_STANDBY"]!}</th>
			<td>
				<#if (alarmConfig.adcStandbyAction.enable) == 1>
					<input class="enableChk" id="alarmConfigs.adcStandbyAction.enable" id_h="adcStandbyAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="enableChk" id="alarmConfigs.adcStandbyAction.enable" id_h="adcStandbyAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="adcStandbyAction_enable_h" name="alarmConfigs.adcStandbyAction.enable" value="${(alarmConfig.adcStandbyAction.enable)}" />
			
			</td>	
			<td>
				-
			</td>	
			<td>
				<#if (alarmConfig.adcStandbyAction.syslog) == 1>
					<input class="syslogChk" id="alarmConfigs.adcStandbyAction.syslog" id_h="adcStandbyAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="syslogChk" id="alarmConfigs.adcStandbyAction.syslog" id_h="adcStandbyAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="adcStandbyAction_syslog_h" name="alarmConfigs.adcStandbyAction.syslog" value="${(alarmConfig.adcStandbyAction.syslog)}" />
			</td>	
			<td>
				<#if (alarmConfig.adcStandbyAction.snmptrap) == 1>
					<input class="snmptrapChk" id="alarmConfigs.adcStandbyAction.snmptrap" id_h="adcStandbyAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="snmptrapChk" id="alarmConfigs.adcStandbyAction.snmptrap" id_h="adcStandbyAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="adcStandbyAction_snmptrap_h" name="alarmConfigs.adcStandbyAction.snmptrap" value="${(alarmConfig.adcStandbyAction.snmptrap)}" />
			</td>			
            <td>
                <#if (alarmConfig.adcStandbyAction.sms) == 1>
                    <input class="smsChk" id="alarmConfigs.adcStandbyAction.snmptrap" id_h="adcStandbyAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.adcStandbyAction.snmptrap" id_h="adcStandbyAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                </#if>
                <input type="hidden" id="adcStandbyAction_sms_h" name="alarmConfigs.adcStandbyAction.sms" value="${(alarmConfig.adcStandbyAction.sms)}" />
            </td>           
		</tr>
		<tr>
			<th>${LANGCODEMAP["MSG_ALERT_SWITCH_ACTIVE"]!}</th>
			<td>
				<#if (alarmConfig.adcActiveAction.enable) == 1>
					<input class="enableChk"  id="alarmConfigs.adcActiveAction.enable" id_h="adcActiveAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="enableChk"  id="alarmConfigs.adcActiveAction.enable" id_h="adcActiveAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>	
				<input type="hidden" id="adcActiveAction_enable_h" name="alarmConfigs.adcActiveAction.enable" value="${(alarmConfig.adcActiveAction.enable)}" />
			</td>	
			<td>
				-
			</td>	
			<td>
				<#if (alarmConfig.adcActiveAction.syslog) == 1>
					<input class="syslogChk"  id="alarmConfigs.adcActiveAction.syslog" id_h="adcActiveAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="syslogChk"  id="alarmConfigs.adcActiveAction.syslog" id_h="adcActiveAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>	
				<input type="hidden" id="adcActiveAction_syslog_h" name="alarmConfigs.adcActiveAction.syslog" value="${(alarmConfig.adcActiveAction.syslog)}" />
			</td>	
			<td>
				<#if (alarmConfig.adcActiveAction.snmptrap) == 1>
					<input class="snmptrapChk"  id="alarmConfigs.adcActiveAction.snmptrap" id_h="adcActiveAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="snmptrapChk"  id="alarmConfigs.adcActiveAction.snmptrap" id_h="adcActiveAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>	
				<input type="hidden" id="adcActiveAction_snmptrap_h" name="alarmConfigs.adcActiveAction.snmptrap" value="${(alarmConfig.adcActiveAction.snmptrap)}" />
			</td>		
            <td>
                <#if (alarmConfig.adcActiveAction.sms) == 1>
                    <input class="smsChk"  id="alarmConfigs.adcActiveAction.snmptrap" id_h="adcActiveAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk"  id="alarmConfigs.adcActiveAction.snmptrap" id_h="adcActiveAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                </#if>  
                <input type="hidden" id="adcActiveAction_sms_h" name="alarmConfigs.adcActiveAction.sms" value="${(alarmConfig.adcActiveAction.sms)}" />
            </td>       
		</tr>
		<tr>
			<th>${LANGCODEMAP["MSG_ALERT_VS_DISCONNECT"]!}</th>
			<td>
				<#if (alarmConfig.virtualServerDownAction.enable) == 1>
					<input class="enableChk" id="alarmConfigs.virtualServerDownAction.enable" id_h="virtualServerDownAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="enableChk" id="alarmConfigs.virtualServerDownAction.enable" id_h="virtualServerDownAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="virtualServerDownAction_enable_h" name="alarmConfigs.virtualServerDownAction.enable" value="${(alarmConfig.virtualServerDownAction.enable)}" />
			</td>	
			<td>
				-
			</td>	
			<td>
				<#if (alarmConfig.virtualServerDownAction.syslog) == 1>
					<input class="syslogChk" id="alarmConfigs.virtualServerDownAction.syslog" id_h="virtualServerDownAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="syslogChk" id="alarmConfigs.virtualServerDownAction.syslog" id_h="virtualServerDownAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="virtualServerDownAction_syslog_h" name="alarmConfigs.virtualServerDownAction.syslog" value="${(alarmConfig.virtualServerDownAction.syslog)}" />
			</td>
			<td>
				<#if (alarmConfig.virtualServerDownAction.snmptrap) == 1>
					<input class="snmptrapChk" id="alarmConfigs.virtualServerDownAction.snmptrap" id_h="virtualServerDownAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="snmptrapChk" id="alarmConfigs.virtualServerDownAction.snmptrap" id_h="virtualServerDownAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="virtualServerDownAction_snmptrap_h" name="alarmConfigs.virtualServerDownAction.snmptrap" value="${(alarmConfig.virtualServerDownAction.snmptrap)}" />
			</td>			
            <td>
                <#if (alarmConfig.virtualServerDownAction.sms) == 1>
                    <input class="smsChk" id="alarmConfigs.virtualServerDownAction.sms" id_h="virtualServerDownAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.virtualServerDownAction.sms" id_h="virtualServerDownAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                </#if>
                <input type="hidden" id="virtualServerDownAction_sms_h" name="alarmConfigs.virtualServerDownAction.sms" value="${(alarmConfig.virtualServerDownAction.sms)}" />
            </td>           
		</tr>
		<tr>
			<th>${LANGCODEMAP["MSG_ALERT_GROUPMEMBER_DISCONNECT"]!}</th>
			<td>
				<#if (alarmConfig.poolMemberDownAction.enable) == 1>
					<input class="enableChk" id="alarmConfigs.poolMemberDownAction.enable" id_h="poolMemberDownAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="enableChk" id="alarmConfigs.poolMemberDownAction.enable" id_h="poolMemberDownAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="poolMemberDownAction_enable_h" name="alarmConfigs.poolMemberDownAction.enable" value="${(alarmConfig.poolMemberDownAction.enable)}" />
			</td>	
			<td>
				-
			</td>	
			<td>
				<#if (alarmConfig.poolMemberDownAction.syslog) == 1>
					<input class="syslogChk" id="alarmConfigs.poolMemberDownAction.syslog" id_h="poolMemberDownAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="syslogChk" id="alarmConfigs.poolMemberDownAction.syslog" id_h="poolMemberDownAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="poolMemberDownAction_syslog_h" name="alarmConfigs.poolMemberDownAction.syslog" value="${(alarmConfig.poolMemberDownAction.syslog)}" />
			</td>
			<td>
				<#if (alarmConfig.poolMemberDownAction.snmptrap) == 1>
					<input class="snmptrapChk" id="alarmConfigs.poolMemberDownAction.snmptrap" id_h="poolMemberDownAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="snmptrapChk" id="alarmConfigs.poolMemberDownAction.snmptrap" id_h="poolMemberDownAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="poolMemberDownAction_snmptrap_h" name="alarmConfigs.poolMemberDownAction.snmptrap" value="${(alarmConfig.poolMemberDownAction.snmptrap)}" />
			</td>			
            <td>
                <#if (alarmConfig.poolMemberDownAction.sms) == 1>
                    <input class="smsChk" id="alarmConfigs.poolMemberDownAction.sms" id_h="poolMemberDownAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.poolMemberDownAction.sms" id_h="poolMemberDownAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                </#if>
                <input type="hidden" id="poolMemberDownAction_sms_h" name="alarmConfigs.poolMemberDownAction.sms" value="${(alarmConfig.poolMemberDownAction.sms)}" />
            </td>           
		</tr>
		<tr>
			<th>${LANGCODEMAP["MSG_ALERT_LINK_DOWN"]!}</th>
			<td>
				<#if (alarmConfig.linkDownAction.enable) == 1>
					<input class="enableChk" id="alarmConfigs.linkDownAction.enable" id_h="linkDownAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="enableChk" id="alarmConfigs.linkDownAction.enable" id_h="linkDownAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="linkDownAction_enable_h" name="alarmConfigs.linkDownAction.enable" value="${(alarmConfig.linkDownAction.enable)}" />
			</td>	
			<td>
				-
			</td>	
			<td>
				<#if (alarmConfig.linkDownAction.syslog) == 1>
					<input class="syslogChk" id="alarmConfigs.linkDownAction.syslog" id_h="linkDownAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="syslogChk" id="alarmConfigs.linkDownAction.syslog" id_h="linkDownAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="linkDownAction_syslog_h" name="alarmConfigs.linkDownAction.syslog" value="${(alarmConfig.linkDownAction.syslog)}" />
			</td>	
			<td>
				<#if (alarmConfig.linkDownAction.snmptrap) == 1>
					<input class="snmptrapChk" id="alarmConfigs.linkDownAction.snmptrap" id_h="linkDownAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="snmptrapChk" id="alarmConfigs.linkDownAction.snmptrap" id_h="linkDownAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="linkDownAction_snmptrap_h" name="alarmConfigs.linkDownAction.snmptrap" value="${(alarmConfig.linkDownAction.snmptrap)}" />
			</td>			
            <td>
                <#if (alarmConfig.linkDownAction.sms) == 1>
                    <input class="smsChk" id="alarmConfigs.linkDownAction.sms" id_h="linkDownAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.linkDownAction.sms" id_h="linkDownAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                </#if>
                <input type="hidden" id="linkDownAction_sms_h" name="alarmConfigs.linkDownAction.sms" value="${(alarmConfig.linkDownAction.sms)}" />
            </td>           
		</tr>				
		<tr>
			<th>${LANGCODEMAP["MSG_ALERT_CONTINUOUS_FAULT"]!}</th>
			<td>
				<#if (alarmConfig.redundancyCheckAction.enable) == 1>
					<input class="enableChk" id="alarmConfigs.redundancyCheckAction.enable" id_h="redundancyCheckAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="enableChk" id="alarmConfigs.redundancyCheckAction.enable" id_h="redundancyCheckAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="redundancyCheckAction_enable_h" name="alarmConfigs.redundancyCheckAction.enable" value="${(alarmConfig.redundancyCheckAction.enable)}" />
			</td>	
			<td>
				-
			</td>	
			<td>
				<#if (alarmConfig.redundancyCheckAction.syslog) == 1>
					<input class="syslogChk" id="alarmConfigs.redundancyCheckAction.syslog" id_h="redundancyCheckAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="syslogChk" id="alarmConfigs.redundancyCheckAction.syslog" id_h="redundancyCheckAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="redundancyCheckAction_syslog_h" name="alarmConfigs.redundancyCheckAction.syslog" value="${(alarmConfig.redundancyCheckAction.syslog)}" />
			</td>
			<td>
				<#if (alarmConfig.redundancyCheckAction.snmptrap) == 1>
					<input class="snmptrapChk" id="alarmConfigs.redundancyCheckAction.snmptrap" id_h="redundancyCheckAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="snmptrapChk" id="alarmConfigs.redundancyCheckAction.snmptrap" id_h="redundancyCheckAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="redundancyCheckAction_snmptrap_h" name="alarmConfigs.redundancyCheckAction.snmptrap" value="${(alarmConfig.redundancyCheckAction.snmptrap)}" />
			</td>			
            <td>
                <#if (alarmConfig.redundancyCheckAction.sms) == 1>
                    <input class="smsChk" id="alarmConfigs.redundancyCheckAction.sms" id_h="redundancyCheckAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.redundancyCheckAction.sms" id_h="redundancyCheckAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                </#if>
                <input type="hidden" id="redundancyCheckAction_sms_h" name="alarmConfigs.redundancyCheckAction.sms" value="${(alarmConfig.redundancyCheckAction.sms)}" />
            </td>           
		</tr>				
	</tbody>									
	</table>
	
	<!-- 2  ADC 성능 정보-->
	<div class="title_h4_1">
		<li>${LANGCODEMAP["MSG_ALERT_ADC_PERF_INFO"]!}</li>
	</div>
	<table class="Board_conn" cellpadding="0" cellspacing="0">
		<colgroup>
            <col width="50%" />
            <col width="10%" />
            <col width="10%" />
            <col width="10%" />
            <col width="10%" />
            <col width="10%" />
		</colgroup>
		<thead>	
		<tr>
			<th>${LANGCODEMAP["MSG_ALERT_ARTICLE"]!}</th>
			<th><input class="allEnableChk1" type="checkbox" name="checkbox" id="checkbox">&nbsp;${LANGCODEMAP["MSG_ALERT_SAVE"]!}</th>	
			<th>${LANGCODEMAP["MSG_ALERT_PERIOD"]!}</th>	
			<th><input class="allSyslogChk1" type="checkbox" name="checkbox" id="checkbox" disabled="disabled">&nbsp;${LANGCODEMAP["MSG_ALERT_ACTION_SYSLOG"]!}</th>
			<th><input class="allSnmpTrapChk1" type="checkbox" name="checkbox" id="checkbox" disabled="disabled">&nbsp;${LANGCODEMAP["MSG_ALERT_ACTION_SNMPTRAP"]!}</th>			
            <th><input class="allSMSChk1" type="checkbox" name="checkbox" id="checkbox" disabled="disabled">&nbsp;SMS대응</th>            
		</tr>
		</thead>		
		<tbody>
		<tr>
			<th>
				<span class="Alertset_w123">${LANGCODEMAP["MSG_ALERT_CPU_USAGE"]!}</span>
				<input name="alarmConfigs.adcCpuValue" type="text" class="inputText width40 align_center paddingR4" value="${(alarmConfig.adcCpuValue)!''}" maxlength="3">
				&nbsp;${LANGCODEMAP["MSG_ALERT_OVER_PERCENT"]!}
			</th>
			<td>
				<#if (alarmConfig.adcCpuAction.enable) == 1>
					<input class="enableChk" id="alarmConfigs.adcCpuAction.enable" id_h="adcCpuAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="enableChk" id="alarmConfigs.aadcCpuActionenable" id_h="adcCpuAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>			
				<input type="hidden" id="adcCpuAction_enable_h" name="alarmConfigs.adcCpuAction.enable" value="${(alarmConfig.adcCpuAction.enable)}" />						
			</td>	
			<td>
				<input name="alarmConfigs.adcCpuAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.adcCpuAction.intervalValue)!''}" maxlength="3" > 
				<select name="alarmConfigs.adcCpuAction.intervalUnit" class="inputSelect timeType">
					<#if (alarmConfig.adcCpuAction.intervalUnit) == "M">
						<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
						
					<#elseif (alarmConfig.adcCpuAction.intervalUnit) == "H">					
						<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
					<#else>
					</#if>
				</select>
			</td>	
			<td>
				<#if (alarmConfig.adcCpuAction.syslog) == 1>
					<input class="syslogChk" id="alarmConfigs.adcCpuAction.syslog" id_h="adcCpuAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="syslogChk" id="alarmConfigs.adcCpuAction.syslog" id_h="adcCpuAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>			
				<input type="hidden" id="adcCpuAction_syslog_h" name="alarmConfigs.adcCpuAction.syslog" value="${(alarmConfig.adcCpuAction.syslog)}" />						
			</td>
			<td>
				<#if (alarmConfig.adcCpuAction.snmptrap) == 1>
					<input class="snmptrapChk" id="alarmConfigs.adcCpuAction.snmptrap" id_h="adcCpuAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="snmptrapChk" id="alarmConfigs.adcCpuAction.snmptrap" id_h="adcCpuAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>			
				<input type="hidden" id="adcCpuAction_snmptrap_h" name="alarmConfigs.adcCpuAction.snmptrap" value="${(alarmConfig.adcCpuAction.snmptrap)}" />						
			</td>			
            <td>
                <#if (alarmConfig.adcCpuAction.sms) == 1>
                    <input class="smsChk" id="alarmConfigs.adcCpuAction.snmptrap" id_h="adcCpuAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.adcCpuAction.snmptrap" id_h="adcCpuAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                </#if>          
                <input type="hidden" id="adcCpuAction_sms_h" name="alarmConfigs.adcCpuAction.snmptrap" value="${(alarmConfig.adcCpuAction.snmptrap)}" />                       
            </td>           
		</tr>			
		<tr>
			<th>
				<span class="Alertset_w123">${LANGCODEMAP["MSG_ALERT_MEM_USAGE"]!}</span>
				<input name="alarmConfigs.adcMemValue" type="text" class="inputText width40 align_center paddingR4" value="${(alarmConfig.adcMemValue)!''}" maxlength="3">
				&nbsp;${LANGCODEMAP["MSG_ALERT_OVER_PERCENT"]!}
			</th>
			<td>
				<#if (alarmConfig.adcMemAction.enable) == 1>
					<input class="enableChk" id="alarmConfigs.adcMemAction.enable" id_h="adcMemAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="enableChk" id="alarmConfigs.adcMemAction.enable" id_h="adcMemAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="adcMemAction_enable_h" name="alarmConfigs.adcMemAction.enable" value="${(alarmConfig.adcMemAction.enable)}" />
			</td>	
			<td>
				<input name="alarmConfigs.adcMemAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.adcMemAction.intervalValue)!''}" maxlength="3" > 
				<select name="alarmConfigs.adcMemAction.intervalUnit" class="inputSelect timeType">
					<#if (alarmConfig.adcMemAction.intervalUnit) == "M">
						<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
						
					<#elseif (alarmConfig.adcMemAction.intervalUnit) == "H">					
						<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
					<#else>
					</#if>
				</select>
			</td>	
			<td>
				<#if (alarmConfig.adcMemAction.syslog) == 1>
					<input class="syslogChk" id="alarmConfigs.adcMemAction.syslog" id_h="adcMemAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="syslogChk" id="alarmConfigs.adcMemAction.syslog" id_h="adcMemAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="adcMemAction_syslog_h" name="alarmConfigs.adcMemAction.syslog" value="${(alarmConfig.adcMemAction.syslog)}" />
			</td>	
			<td>
				<#if (alarmConfig.adcMemAction.snmptrap) == 1>
					<input class="snmptrapChk" id="alarmConfigs.adcMemAction.snmptrap" id_h="adcMemAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="snmptrapChk" id="alarmConfigs.adcMemAction.snmptrap" id_h="adcMemAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="adcMemAction_snmptrap_h" name="alarmConfigs.adcMemAction.snmptrap" value="${(alarmConfig.adcMemAction.snmptrap)}" />
			</td>			
            <td>
                <#if (alarmConfig.adcMemAction.sms) == 1>
                    <input class="smsChk" id="alarmConfigs.adcMemAction.snmptrap" id_h="adcMemAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.adcMemAction.snmptrap" id_h="adcMemAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                </#if>
                <input type="hidden" id="adcMemAction_sms_h" name="alarmConfigs.adcMemAction.sms" value="${(alarmConfig.adcMemAction.sms)}" />
            </td>           
		</tr>		
		<tr>
			<th>
				${LANGCODEMAP["MSG_ALERT_SESSIONS"]!}&nbsp;
				<input name="alarmConfigs.adcConnHighValue" type="text" class="inputText align_center width110 paddingR4" value="${(alarmConfig.adcConnHighValue)!''}">
				&nbsp;${LANGCODEMAP["MSG_ALERT_OVER_COUNT_SEC"]!}
			</th>
			<td>
				<#if (alarmConfig.connectionLimitHighAction.enable) == 1>
					<input class="enableChk" id="alarmConfigs.connectionLimitHighAction.enable" id_h="connectionLimitHighAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="enableChk" id="alarmConfigs.connectionLimitHighAction.enable" id_h="connectionLimitHighAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="connectionLimitHighAction_enable_h" name="alarmConfigs.connectionLimitHighAction.enable" value="${(alarmConfig.connectionLimitHighAction.enable)}" />
			</td>	
			<td>
				<input name="alarmConfigs.connectionLimitHighAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.connectionLimitHighAction.intervalValue)!''}" maxlength="3" > 
				<select name="alarmConfigs.connectionLimitHighAction.intervalUnit" class="inputSelect timeType">
					<#if (alarmConfig.connectionLimitHighAction.intervalUnit) == "M">
						<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
						
					<#elseif (alarmConfig.connectionLimitHighAction.intervalUnit) == "H">					
						<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
					<#else>
					</#if>
				</select>
			</td>	
			<td>
				<#if (alarmConfig.connectionLimitHighAction.syslog) == 1>
					<input class="syslogChk" id="alarmConfigs.connectionLimitHighAction.syslog" id_h="connectionLimitHighAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="syslogChk" id="alarmConfigs.connectionLimitHighAction.syslog" id_h="connectionLimitHighAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="connectionLimitHighAction_syslog_h" name="alarmConfigs.connectionLimitHighAction.syslog" value="${(alarmConfig.connectionLimitHighAction.syslog)}" />
			</td>
			<td>
				<#if (alarmConfig.connectionLimitHighAction.snmptrap) == 1>
					<input class="snmptrapChk" id="alarmConfigs.connectionLimitHighAction.snmptrap" id_h="connectionLimitHighAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="snmptrapChk" id="alarmConfigs.connectionLimitHighAction.snmptrap" id_h="connectionLimitHighAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="connectionLimitHighAction_snmptrap_h" name="alarmConfigs.connectionLimitHighAction.snmptrap" value="${(alarmConfig.connectionLimitHighAction.snmptrap)}" />
			</td>			
            <td>
                <#if (alarmConfig.connectionLimitHighAction.sms) == 1>
                    <input class="smsChk" id="alarmConfigs.connectionLimitHighAction.sms" id_h="connectionLimitHighAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.connectionLimitHighAction.sms" id_h="connectionLimitHighAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                </#if>
                <input type="hidden" id="connectionLimitHighAction_sms_h" name="alarmConfigs.connectionLimitHighAction.sms" value="${(alarmConfig.connectionLimitHighAction.sms)}" />
            </td>           
		</tr>
		<tr>
			<th>
				${LANGCODEMAP["MSG_ALERT_SESSIONS"]!}&nbsp;
				<input name="alarmConfigs.adcConnLowValue" type="text" class="inputText align_center width110 paddingR4" value="${(alarmConfig.adcConnLowValue)!''}">
				&nbsp;${LANGCODEMAP["MSG_ALERT_UNDER_COUNT_SEC"]!}
			</th>
			<td>
				<#if (alarmConfig.connectionLimitLowAction.enable) == 1>
					<input class="enableChk" id="alarmConfigs.connectionLimitLowAction.enable" id_h="connectionLimitLowAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="enableChk" id="alarmConfigs.connectionLimitLowAction.enable" id_h="connectionLimitLowAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="connectionLimitLowAction_enable_h" name="alarmConfigs.connectionLimitLowAction.enable" value="${(alarmConfig.connectionLimitLowAction.enable)}" />
			</td>	
			<td>
				<input name="alarmConfigs.connectionLimitLowAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.connectionLimitLowAction.intervalValue)!''}" maxlength="3" > 
				<select name="alarmConfigs.connectionLimitLowAction.intervalUnit" class="inputSelect timeType">
					<#if (alarmConfig.connectionLimitLowAction.intervalUnit) == "M">
						<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
						
					<#elseif (alarmConfig.connectionLimitLowAction.intervalUnit) == "H">					
						<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
					<#else>
					</#if>
				</select>
			</td>	
			<td>
				<#if (alarmConfig.connectionLimitLowAction.syslog) == 1>
					<input class="syslogChk" id="alarmConfigs.connectionLimitLowAction.syslog" id_h="connectionLimitLowAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="syslogChk" id="alarmConfigs.connectionLimitLowAction.syslog" id_h="connectionLimitLowAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="connectionLimitLowAction_syslog_h" name="alarmConfigs.connectionLimitLowAction.syslog" value="${(alarmConfig.connectionLimitLowAction.syslog)}" />
			</td>	
			<td>
				<#if (alarmConfig.connectionLimitLowAction.snmptrap) == 1>
					<input class="snmptrapChk" id="alarmConfigs.connectionLimitLowAction.snmptrap" id_h="connectionLimitLowAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="snmptrapChk" id="alarmConfigs.connectionLimitLowAction.snmptrap" id_h="connectionLimitLowAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="connectionLimitLowAction_snmptrap_h" name="alarmConfigs.connectionLimitLowAction.snmptrap" value="${(alarmConfig.connectionLimitLowAction.snmptrap)}" />
			</td>		
            <td>
                <#if (alarmConfig.connectionLimitLowAction.sms) == 1>
                    <input class="smsChk" id="alarmConfigs.connectionLimitLowAction.snmptrap" id_h="connectionLimitLowAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.connectionLimitLowAction.snmptrap" id_h="connectionLimitLowAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                </#if>
                <input type="hidden" id="connectionLimitLowAction_sms_h" name="alarmConfigs.connectionLimitLowAction.sms" value="${(alarmConfig.connectionLimitLowAction.sms)}" />
            </td>       
		</tr>
		<tr>
			<th>
				<span class="Alertset_w123">${LANGCODEMAP["MSG_ALERT_SSL_TRANSACTION"]!}</span>
				<input name="alarmConfigs.adcSslTransValue" type="text" class="inputText align_center width110 paddingR4" value="${(alarmConfig.adcSslTransValue)!''}">
				&nbsp;${LANGCODEMAP["MSG_ALERT_OVER_COUNT_SEC_SSL"]!}
			</th>
			<td>
				<#if (alarmConfig.adcSslTransAction.enable) == 1>
					<input class="enableChk" id="alarmConfigs.adcSslTransAction.enable" id_h="adcSslTransAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="enableChk" id="alarmConfigs.adcSslTransAction.enable" id_h="adcSslTransAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="adcSslTransAction_enable_h" name="alarmConfigs.adcSslTransAction.enable" value="${(alarmConfig.adcSslTransAction.enable)}" />
			</td>	
			<td>
				<input name="alarmConfigs.adcSslTransAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.adcSslTransAction.intervalValue)!''}" maxlength="3" > 
				<select name="alarmConfigs.adcSslTransAction.intervalUnit" class="inputSelect timeType">
					<#if (alarmConfig.adcSslTransAction.intervalUnit) == "M">
						<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
						
					<#elseif (alarmConfig.adcSslTransAction.intervalUnit) == "H">
						<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
					<#else>
					</#if>
				</select>
			</td>	
			<td>
				<#if (alarmConfig.adcSslTransAction.syslog) == 1>
					<input class="syslogChk" id="alarmConfigs.adcSslTransAction.syslog" id_h="adcSslTransAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="syslogChk" id="alarmConfigs.adcSslTransAction.syslog" id_h="adcSslTransAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="adcSslTransAction_syslog_h" name="alarmConfigs.adcSslTransAction.syslog" value="${(alarmConfig.adcSslTransAction.syslog)}" />
			</td>
			<td>
				<#if (alarmConfig.adcSslTransAction.snmptrap) == 1>
					<input class="snmptrapChk" id="alarmConfigs.adcSslTransAction.snmptrap" id_h="adcSslTransAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="snmptrapChk" id="alarmConfigs.adcSslTransAction.snmptrap" id_h="adcSslTransAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="adcSslTransAction_snmptrap_h" name="alarmConfigs.adcSslTransAction.snmptrap" value="${(alarmConfig.adcSslTransAction.snmptrap)}" />
			</td>			
            <td>
                <#if (alarmConfig.adcSslTransAction.sms) == 1>
                    <input class="smsChk" id="alarmConfigs.adcSslTransAction.sms" id_h="adcSslTransAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.adcSslTransAction.sms" id_h="adcSslTransAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                </#if>
                <input type="hidden" id="adcSslTransAction_sms_h" name="alarmConfigs.adcSslTransAction.sms" value="${(alarmConfig.adcSslTransAction.sms)}" />
            </td>           
		</tr>
		<tr>
			<th>
				<span class="Alertset_w123">${LANGCODEMAP["MSG_ALERT_RESPONSE_TIME"]!}</span>
				<input name="alarmConfigs.responseTimeValue" type="text" class="inputText width50 align_center paddingR4" id="respTmValue" value="${(alarmConfig.responseTimeValue)!''}" maxlength="6">
				&nbsp;msec
			</th>
			<td>
				<#if (alarmConfig.responseTimeAction.enable) == 1>
					<input class="enableChk" id="alarmConfigs.responseTimeAction.enable" id_h="responseTimeAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="enableChk" id="alarmConfigs.responseTimeAction.enable" id_h="responseTimeAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>			
				<input type="hidden" id="responseTimeAction_enable_h" name="alarmConfigs.responseTimeAction.enable" value="${(alarmConfig.responseTimeAction.enable)}" />						
			</td>	
			<td>
				<input name="alarmConfigs.responseTimeAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.responseTimeAction.intervalValue)!''}" maxlength="3" > 
				<select name="alarmConfigs.responseTimeAction.intervalUnit" class="inputSelect timeType">
					<#if (alarmConfig.responseTimeAction.intervalUnit) == "M">
						<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
						
					<#elseif (alarmConfig.responseTimeAction.intervalUnit) == "H">					
						<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
					<#else>
					</#if>
				</select>
			</td>	
			<td>
				<#if (alarmConfig.responseTimeAction.syslog) == 1>
					<input class="syslogChk" id="alarmConfigs.responseTimeAction.syslog" id_h="responseTimeAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="syslogChk" id="alarmConfigs.responseTimeAction.syslog" id_h="responseTimeAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>			
				<input type="hidden" id="responseTimeAction_syslog_h" name="alarmConfigs.responseTimeAction.syslog" value="${(alarmConfig.responseTimeAction.syslog)}" />						
			</td>	
			<td>
				<#if (alarmConfig.responseTimeAction.snmptrap) == 1>
					<input class="snmptrapChk" id="alarmConfigs.responseTimeAction.snmptrap" id_h="responseTimeAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="snmptrapChk" id="alarmConfigs.responseTimeAction.snmptrap" id_h="responseTimeAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>			
				<input type="hidden" id="responseTimeAction_snmptrap_h" name="alarmConfigs.responseTimeAction.snmptrap" value="${(alarmConfig.responseTimeAction.snmptrap)}" />						
			</td>		
            <td>
                <#if (alarmConfig.responseTimeAction.sms) == 1>
                    <input class="smsChk" id="alarmConfigs.responseTimeAction.sms" id_h="responseTimeAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.responseTimeAction.sms" id_h="responseTimeAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                </#if>          
                <input type="hidden" id="responseTimeAction_sms_h" name="alarmConfigs.responseTimeAction.sms" value="${(alarmConfig.responseTimeAction.sms)}" />                     
            </td>       
		</tr>
	</tbody>									
	</table>
	
	<!-- 3  기간 정보-->
	<div class="title_h4_1">
		<li>${LANGCODEMAP["MSG_ALERT_DATE_INFO"]!}</li>
	</div>
	<table class="Board_conn" cellpadding="0" cellspacing="0">
		<colgroup>
            <col width="50%" />
            <col width="10%" />
            <col width="10%" />
            <col width="10%" />
            <col width="10%" />
            <col width="10%" />
		</colgroup>
		<thead>		
		<tr>
			<th>${LANGCODEMAP["MSG_ALERT_ARTICLE"]!}</th>
			<th><input class="allEnableChk2" type="checkbox" name="checkbox" id="checkbox">&nbsp;${LANGCODEMAP["MSG_ALERT_SAVE"]!}</th>	
			<th>${LANGCODEMAP["MSG_ALERT_PERIOD"]!}</th>	
			<th><input class="allSyslogChk2" type="checkbox" name="checkbox" id="checkbox" disabled="disabled">&nbsp;${LANGCODEMAP["MSG_ALERT_ACTION_SYSLOG"]!}</th>
			<th><input class="allSnmpTrapChk2" type="checkbox" name="checkbox" id="checkbox" disabled="disabled">&nbsp;${LANGCODEMAP["MSG_ALERT_ACTION_SNMPTRAP"]!}</th>				
            <th><input class="allSMSChk2" type="checkbox" name="checkbox" id="checkbox" disabled="disabled">&nbsp;SMS대응</th>                
		</tr>
		</thead>		
		<tbody>
		<tr>
			<th>
				<span class="Alertset_w123">${LANGCODEMAP["MSG_ALERT_UPTIME"]!}</span>
				<input name="alarmConfigs.adcUptimeValue" type="text" class="inputText width40 align_center paddingR4" value="${(alarmConfig.adcUptimeValue)!''}" maxlength="3">
				&nbsp;${LANGCODEMAP["MSG_ALERT_OVER_MONTH"]!} 
			</th>
			<td>
				<#if (alarmConfig.adcUptimeAction.enable) == 1>
					<input class="enableChk" id="alarmConfigs.adcUptimeAction.enable" id_h="adcUptimeAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="enableChk" id="alarmConfigs.adcUptimeAction.enable" id_h="adcUptimeAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="adcUptimeAction_enable_h" name="alarmConfigs.adcUptimeAction.enable" value="${(alarmConfig.adcUptimeAction.enable)}" />
			</td>	
			<td>
				<input name="alarmConfigs.adcUptimeAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.adcUptimeAction.intervalValue)!''}" maxlength="3" > 
				<select name="alarmConfigs.adcUptimeAction.intervalUnit" class="inputSelect timeType">
					<#if (alarmConfig.adcUptimeAction.intervalUnit) == "M">
						<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
						
					<#elseif (alarmConfig.adcUptimeAction.intervalUnit) == "H">					
						<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
						
					<#else>
					</#if>
				</select>
			</td>	
			<td>
				<#if (alarmConfig.adcUptimeAction.syslog) == 1>
					<input class="syslogChk" id="alarmConfigs.adcUptimeAction.syslog" id_h="adcUptimeAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="syslogChk" id="alarmConfigs.adcUptimeAction.syslog" id_h="adcUptimeAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="adcUptimeAction_syslog_h" name="alarmConfigs.adcUptimeAction.syslog" value="${(alarmConfig.adcUptimeAction.syslog)}" />
			</td>
			<td>
				<#if (alarmConfig.adcUptimeAction.snmptrap) == 1>
					<input class="snmptrapChk" id="alarmConfigs.adcUptimeAction.snmptrap" id_h="adcUptimeAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="snmptrapChk" id="alarmConfigs.adcUptimeAction.snmptrap" id_h="adcUptimeAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="adcUptimeAction_snmptrap_h" name="alarmConfigs.adcUptimeAction.snmptrap" value="${(alarmConfig.adcUptimeAction.snmptrap)}" />
			</td>			
            <td>
                <#if (alarmConfig.adcUptimeAction.sms) == 1>
                    <input class="smsChk" id="alarmConfigs.adcUptimeAction.sms" id_h="adcUptimeAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.adcUptimeAction.sms" id_h="adcUptimeAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                </#if>
                <input type="hidden" id="adcUptimeAction_sms_h" name="alarmConfigs.adcUptimeAction.sms" value="${(alarmConfig.adcUptimeAction.sms)}" />
            </td>           
		</tr>		
		<tr>
			<th>
				<span class="Alertset_w123">${LANGCODEMAP["MSG_ALERT_HAVE_DATE"]!}</span>
				<input name="alarmConfigs.adcPurchaseValue" type="text" class="inputText width40 align_center paddingR4" value="${(alarmConfig.adcPurchaseValue)!''}" maxlength="3">
				&nbsp;${LANGCODEMAP["MSG_ALERT_OVER_MONTH"]!} 
			</th>
			<td>
				<#if (alarmConfig.adcPurchaseAction.enable) == 1>
					<input class="enableChk" id="alarmConfigs.adcPurchaseAction.enable" id_h="adcPurchaseAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="enableChk" id="alarmConfigs.adcPurchaseAction.enable" id_h="adcPurchaseAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="adcPurchaseAction_enable_h" name="alarmConfigs.adcPurchaseAction.enable" value="${(alarmConfig.adcPurchaseAction.enable)}" />
			</td>	
			<td>
				<input name="alarmConfigs.adcPurchaseAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.adcPurchaseAction.intervalValue)!''}" maxlength="3" > 
				<select name="alarmConfigs.adcPurchaseAction.intervalUnit" class="inputSelect timeType">
					<#if (alarmConfig.adcPurchaseAction.intervalUnit) == "M">
						<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
						
					<#elseif (alarmConfig.adcPurchaseAction.intervalUnit) == "H">					
						<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
						
					<#else>
					</#if>
				</select>
			</td>	
			<td>
				<#if (alarmConfig.adcPurchaseAction.syslog) == 1>
					<input class="syslogChk" id="alarmConfigs.adcPurchaseAction.syslog" id_h="adcPurchaseAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="syslogChk" id="alarmConfigs.adcPurchaseAction.syslog" id_h="adcPurchaseAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="adcPurchaseAction_syslog_h" name="alarmConfigs.adcPurchaseAction.syslog" value="${(alarmConfig.adcPurchaseAction.syslog)}" />
			</td>
			<td>
				<#if (alarmConfig.adcPurchaseAction.snmptrap) == 1>
					<input class="snmptrapChk" id="alarmConfigs.adcPurchaseAction.snmptrap" id_h="adcPurchaseAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="snmptrapChk" id="alarmConfigs.adcPurchaseAction.snmptrap" id_h="adcPurchaseAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="adcPurchaseAction_snmptrap_h" name="alarmConfigs.adcPurchaseAction.snmptrap" value="${(alarmConfig.adcPurchaseAction.snmptrap)}" />
			</td>			
            <td>
                <#if (alarmConfig.adcPurchaseAction.sms) == 1>
                    <input class="smsChk" id="alarmConfigs.adcPurchaseAction.sms" id_h="adcPurchaseAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.adcPurchaseAction.sms" id_h="adcPurchaseAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                </#if>
                <input type="hidden" id="adcPurchaseAction_sms_h" name="alarmConfigs.adcPurchaseAction.sms" value="${(alarmConfig.adcPurchaseAction.sms)}" />
            </td>           
		</tr>
		<tr>
			<th>
				<span class="Alertset_w123">${LANGCODEMAP["MSG_ALERT_BIGIP_SSL_AUTH"]!}</span>
				<input name="alarmConfigs.adcSslcertValue" type="text" class="inputText width40 align_center paddingR4" value="${(alarmConfig.adcSslcertValue)!''}" maxlength="3">
				&nbsp;${LANGCODEMAP["MSG_ALERT_DAY_BEFORE"]!}
			</th>
			<td>
				<#if (alarmConfig.adcSslcertAction.enable) == 1>
					<input class="enableChk" id="alarmConfigs.adcSslcertAction.enable" id_h="adcSslcertAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="enableChk" id="alarmConfigs.adcSslcertAction.enable" id_h="adcSslcertAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="adcSslcertAction_enable_h" name="alarmConfigs.adcSslcertAction.enable" value="${(alarmConfig.adcSslcertAction.enable)}" />
			</td>	
			<td>
				<input name="alarmConfigs.adcSslcertAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.adcSslcertAction.intervalValue)!''}" maxlength="3" > 
				<select name="alarmConfigs.adcSslcertAction.intervalUnit" class="inputSelect timeType">
					<#if (alarmConfig.adcSslcertAction.intervalUnit) == "M">
						<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
						
					<#elseif (alarmConfig.adcSslcertAction.intervalUnit) == "H">					
						<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
						
					<#else>
					</#if>
				</select>
			</td>	
			<td>
				<#if (alarmConfig.adcSslcertAction.syslog) == 1>
					<input class="syslogChk" id="alarmConfigs.adcSslcertAction.syslog" id_h="adcSslcertAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="syslogChk" id="alarmConfigs.adcSslcertAction.syslog" id_h="adcSslcertAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="adcSslcertAction_syslog_h" name="alarmConfigs.adcSslcertAction.syslog" value="${(alarmConfig.adcSslcertAction.syslog)}" />
			</td>	
			<td>
				<#if (alarmConfig.adcSslcertAction.snmptrap) == 1>
					<input class="snmptrapChk" id="alarmConfigs.adcSslcertAction.snmptrap" id_h="adcSslcertAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="snmptrapChk" id="alarmConfigs.adcSslcertAction.snmptrap" id_h="adcSslcertAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="adcSslcertAction_snmptrap_h" name="alarmConfigs.adcSslcertAction.snmptrap" value="${(alarmConfig.adcSslcertAction.snmptrap)}" />
			</td>		
            <td>
                <#if (alarmConfig.adcSslcertAction.sms) == 1>
                    <input class="smsChk" id="alarmConfigs.adcSslcertAction.sms" id_h="adcSslcertAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.adcSslcertAction.sms" id_h="adcSslcertAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                </#if>
                <input type="hidden" id="adcSslcertAction_sms_h" name="alarmConfigs.adcSslcertAction.sms" value="${(alarmConfig.adcSslcertAction.sms)}" />
            </td>       
		</tr>		
	</tbody>									
	</table>
	
	<!-- 4  ADC 시스템 정보-->
	<div class="title_h4_1">
		<li>${LANGCODEMAP["MSG_ALERT_ADC_SYS_INFO"]!}</li>
	</div>
	<table class="Board_conn" cellpadding="0" cellspacing="0">
		<colgroup>
            <col width="50%" />
            <col width="10%" />
            <col width="10%" />
            <col width="10%" />
            <col width="10%" />
            <col width="10%" />
		</colgroup>
		<thead>		
		<tr>
			<th>${LANGCODEMAP["MSG_ALERT_ARTICLE"]!}</th>
			<th><input class="allEnableChk3" type="checkbox" name="checkbox" id="checkbox">&nbsp;${LANGCODEMAP["MSG_ALERT_SAVE"]!}</th>	
			<th>${LANGCODEMAP["MSG_ALERT_PERIOD"]!}</th>	
			<th><input class="allSyslogChk3" type="checkbox" name="checkbox" id="checkbox" disabled="disabled">&nbsp;${LANGCODEMAP["MSG_ALERT_ACTION_SYSLOG"]!}</th>
			<th><input class="allSnmpTrapChk3" type="checkbox" name="checkbox" id="checkbox" disabled="disabled">&nbsp;${LANGCODEMAP["MSG_ALERT_ACTION_SNMPTRAP"]!}</th>			
            <th><input class="allSMSChk3" type="checkbox" name="checkbox" id="checkbox" disabled="disabled">&nbsp;SMS대응</th>            
		</tr>
		</thead>		
		<tbody>
			<tr>
				<th>
					<span class="Alertset_w123">${LANGCODEMAP["MSG_ALERT_INTERFACE_ERROR"]!}</span>
					<input name="alarmConfigs.interfaceErrorValue" type="text" class="inputText width40 align_center paddingR4" value="${(alarmConfig.interfaceErrorValue)!''}" maxlength="3">
					&nbsp;${LANGCODEMAP["MSG_ALERT_COUNT_CONSECUTIVELY_OCCUR"]!}
				</th>
				<td>
					<#if (alarmConfig.interfaceErrorAction.enable) == 1>
						<input class="enableChk" id="alarmConfigs.interfaceErrorAction.enable" id_h="interfaceErrorAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="enableChk" id="alarmConfigs.interfaceErrorAction.enable" id_h="interfaceErrorAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="interfaceErrorAction_enable_h" name="alarmConfigs.interfaceErrorAction.enable" value="${(alarmConfig.interfaceErrorAction.enable)}" />
				</td>	
				<td>
					<input name="alarmConfigs.interfaceErrorAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.interfaceErrorAction.intervalValue)!''}" maxlength="3" > 
					<select name="alarmConfigs.interfaceErrorAction.intervalUnit" class="inputSelect timeType">
						<#if (alarmConfig.interfaceErrorAction.intervalUnit) == "M">
							<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
							
						<#elseif (alarmConfig.interfaceErrorAction.intervalUnit) == "H">					
							<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
							
						<#else>
						</#if>
					</select>
				</td>	
				<td>
					<#if (alarmConfig.interfaceErrorAction.syslog) == 1>
						<input class="syslogChk" id="alarmConfigs.interfaceErrorAction.syslog" id_h="interfaceErrorAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="syslogChk" id="alarmConfigs.interfaceErrorAction.syslog" id_h="interfaceErrorAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="interfaceErrorAction_syslog_h" name="alarmConfigs.interfaceErrorAction.syslog" value="${(alarmConfig.interfaceErrorAction.syslog)}" />
				</td>			
				<td>
					<#if (alarmConfig.interfaceErrorAction.snmptrap) == 1>
						<input class="snmptrapChk" id="alarmConfigs.interfaceErrorAction.snmptrap" id_h="interfaceErrorAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="snmptrapChk" id="alarmConfigs.interfaceErrorAction.snmptrap" id_h="interfaceErrorAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="interfaceErrorAction_snmptrap_h" name="alarmConfigs.interfaceErrorAction.snmptrap" value="${(alarmConfig.interfaceErrorAction.snmptrap)}" />
				</td>
                <td>
                    <#if (alarmConfig.interfaceErrorAction.sms) == 1>
                        <input class="smsChk" id="alarmConfigs.interfaceErrorAction.sms" id_h="interfaceErrorAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                    <#else>
                        <input class="smsChk" id="alarmConfigs.interfaceErrorAction.sms" id_h="interfaceErrorAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                    </#if>
                    <input type="hidden" id="interfaceErrorAction_sms_h" name="alarmConfigs.interfaceErrorAction.sms" value="${(alarmConfig.interfaceErrorAction.sms)}" />
                </td>
			</tr>		
			<tr>
				<th>
					<span class="Alertset_w123">${LANGCODEMAP["MSG_ALERT_INTERFACE_USAGE"]!}</span>
					<input name="alarmConfigs.interfaceUsageValue" type="text" class="inputText width40 align_center paddingR4" value="${(alarmConfig.interfaceUsageValue)!''}" maxlength="3">
					&nbsp;${LANGCODEMAP["MSG_ALERT_OVER_PERCENT"]!}
				</th>
				<td>
					<#if (alarmConfig.interfaceUsageLimitAction.enable) == 1>
						<input class="enableChk" id="alarmConfigs.interfaceUsageLimitAction.enable" id_h="interfaceUsageLimitAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="enableChk" id="alarmConfigs.interfaceUsageLimitAction.enable" id_h="interfaceUsageLimitAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="interfaceUsageLimitAction_enable_h" name="alarmConfigs.interfaceUsageLimitAction.enable" value="${(alarmConfig.interfaceUsageLimitAction.enable)}" />
				</td>	
				<td>
					<input name="alarmConfigs.interfaceUsageLimitAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.interfaceUsageLimitAction.intervalValue)!''}" maxlength="3" > 
					<select name="alarmConfigs.interfaceUsageLimitAction.intervalUnit" class="inputSelect timeType">
						<#if (alarmConfig.interfaceUsageLimitAction.intervalUnit) == "M">
							<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
							
						<#elseif (alarmConfig.interfaceUsageLimitAction.intervalUnit) == "H">
							<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
						<#else>
						</#if>
					</select>
				</td>	
				<td>
					<#if (alarmConfig.interfaceUsageLimitAction.syslog) == 1>
						<input class="syslogChk" id="alarmConfigs.interfaceUsageLimitAction.syslog" id_h="interfaceUsageLimitAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="syslogChk" id="alarmConfigs.interfaceUsageLimitAction.syslog" id_h="interfaceUsageLimitAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="interfaceUsageLimitAction_syslog_h" name="alarmConfigs.interfaceUsageLimitAction.syslog" value="${(alarmConfig.interfaceUsageLimitAction.syslog)}" />
				</td>
				<td>
					<#if (alarmConfig.interfaceUsageLimitAction.snmptrap) == 1>
						<input class="snmptrapChk" id="alarmConfigs.interfaceUsageLimitAction.snmptrap" id_h="interfaceUsageLimitAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="snmptrapChk" id="alarmConfigs.interfaceUsageLimitAction.snmptrap" id_h="interfaceUsageLimitAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="interfaceUsageLimitAction_snmptrap_h" name="alarmConfigs.interfaceUsageLimitAction.snmptrap" value="${(alarmConfig.interfaceUsageLimitAction.syslog)}" />
				</td>			
                <td>
                    <#if (alarmConfig.interfaceUsageLimitAction.sms) == 1>
                        <input class="smsChk" id="alarmConfigs.interfaceUsageLimitAction.sms" id_h="interfaceUsageLimitAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                    <#else>
                        <input class="smsChk" id="alarmConfigs.interfaceUsageLimitAction.sms" id_h="interfaceUsageLimitAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                    </#if>
                    <input type="hidden" id="interfaceUsageLimitAction_sms_h" name="alarmConfigs.interfaceUsageLimitAction.sms" value="${(alarmConfig.interfaceUsageLimitAction.sms)}" />
                </td>           
			</tr>			
			<tr>
				<th>
					${LANGCODEMAP["MSG_ALERT_SWITCH_INTERFACE_DUPLEX"]!}&nbsp;${LANGCODEMAP["MSG_ALERT_SWITCH_HALF_FULL"]!}</th>
				<td class="align_center Rcolor">
					<#if (alarmConfig.interfaceDuplexChangeAction.enable) == 1>
						<input class="enableChk" id="alarmConfigs.interfaceDuplexChangeAction.enable" id_h="interfaceDuplexChangeAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="enableChk" id="alarmConfigs.interfaceDuplexChangeAction.enable" id_h="interfaceDuplexChangeAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="interfaceDuplexChangeAction_enable_h" name="alarmConfigs.interfaceDuplexChangeAction.enable" value="${(alarmConfig.interfaceDuplexChangeAction.enable)}" />
				</td>	
				<td class="align_center Rcolor">
					-
				</td>	
				<td class="align_center">
					<#if (alarmConfig.interfaceDuplexChangeAction.syslog) == 1>
						<input class="syslogChk" id="alarmConfigs.interfaceDuplexChangeAction.syslog" id_h="interfaceDuplexChangeAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="syslogChk" id="alarmConfigs.interfaceDuplexChangeAction.syslog" id_h="interfaceDuplexChangeAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="interfaceDuplexChangeAction_syslog_h" name="alarmConfigs.interfaceDuplexChangeAction.syslog" value="${(alarmConfig.interfaceDuplexChangeAction.syslog)}" />
				</td>	
				<td class="align_center">
					<#if (alarmConfig.interfaceDuplexChangeAction.snmptrap) == 1>
						<input class="snmptrapChk" id="alarmConfigs.interfaceDuplexChangeAction.snmptrap" id_h="interfaceDuplexChangeAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="snmptrapChk" id="alarmConfigs.interfaceDuplexChangeAction.snmptrap" id_h="interfaceDuplexChangeAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="interfaceDuplexChangeAction_snmptrap_h" name="alarmConfigs.interfaceDuplexChangeAction.snmptrap" value="${(alarmConfig.interfaceDuplexChangeAction.snmptrap)}" />
				</td>			
                <td class="align_center">
                    <#if (alarmConfig.interfaceDuplexChangeAction.sms) == 1>
                        <input class="smsChk" id="alarmConfigs.interfaceDuplexChangeAction.sms" id_h="interfaceDuplexChangeAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                    <#else>
                        <input class="smsChk" id="alarmConfigs.interfaceDuplexChangeAction.sms" id_h="interfaceDuplexChangeAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                    </#if>
                    <input type="hidden" id="interfaceDuplexChangeAction_sms_h" name="alarmConfigs.interfaceDuplexChangeAction.sms" value="${(alarmConfig.interfaceDuplexChangeAction.sms)}" />
                </td>           
			</tr>		
			<tr>
				<th>
					${LANGCODEMAP["MSG_ALERT_SWITCH_INTERFACE_SPEED"]!}&nbsp;${LANGCODEMAP["MSG_ALERT_SWITCH_10_100_1000"]!}
				</th>
				<td>
					<#if (alarmConfig.interfaceSpeedChangeAction.enable) == 1>
						<input class="enableChk" id="alarmConfigs.interfaceSpeedChangeAction.enable" id_h="interfaceSpeedChangeAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="enableChk" id="alarmConfigs.interfaceSpeedChangeAction.enable" id_h="interfaceSpeedChangeAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="interfaceSpeedChangeAction_enable_h" name="alarmConfigs.interfaceSpeedChangeAction.enable" value="${(alarmConfig.interfaceSpeedChangeAction.enable)}" />
				
				</td>	
				<td>
					-
				</td>	
				<td>
					<#if (alarmConfig.interfaceSpeedChangeAction.syslog) == 1>
						<input class="syslogChk" id="alarmConfigs.interfaceSpeedChangeAction.syslog" id_h="interfaceSpeedChangeAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="syslogChk" id="alarmConfigs.interfaceSpeedChangeAction.syslog" id_h="interfaceSpeedChangeAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="interfaceSpeedChangeAction_syslog_h" name="alarmConfigs.interfaceSpeedChangeAction.syslog" value="${(alarmConfig.interfaceSpeedChangeAction.syslog)}" />
				</td>
				<td>
					<#if (alarmConfig.interfaceSpeedChangeAction.snmptrap) == 1>
						<input class="snmptrapChk" id="alarmConfigs.interfaceSpeedChangeAction.snmptrap" id_h="interfaceSpeedChangeAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="snmptrapChk" id="alarmConfigs.interfaceSpeedChangeAction.snmptrap" id_h="interfaceSpeedChangeAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="interfaceSpeedChangeAction_snmptrap_h" name="alarmConfigs.interfaceSpeedChangeAction.snmptrap" value="${(alarmConfig.interfaceSpeedChangeAction.snmptrap)}" />
				</td>			
                <td>
                    <#if (alarmConfig.interfaceSpeedChangeAction.sms) == 1>
                        <input class="smsChk" id="alarmConfigs.interfaceSpeedChangeAction.sms" id_h="interfaceSpeedChangeAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                    <#else>
                        <input class="smsChk" id="alarmConfigs.interfaceSpeedChangeAction.sms" id_h="interfaceSpeedChangeAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                    </#if>
                    <input type="hidden" id="interfaceSpeedChangeAction_sms_h" name="alarmConfigs.interfaceSpeedChangeAction.sms" value="${(alarmConfig.interfaceSpeedChangeAction.sms)}" />
                </td>           
			</tr>		
			<tr>
				<th>
					${LANGCODEMAP["MSG_ALERT_ADC_SETTING_BACKUP_FAIL"]!}&nbsp;${LANGCODEMAP["MSG_ALERT_FAIL"]!}</th>
				<td>
					<#if (alarmConfig.adcConfBackupFailureAction.enable) == 1>
						<input class="enableChk" id="alarmConfigs.adcConfBackupFailureAction.enable" id_h="adcConfBackupFailureAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="enableChk" id="alarmConfigs.adcConfBackupFailureAction.enable" id_h="adcConfBackupFailureAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="adcConfBackupFailureAction_enable_h" name="alarmConfigs.adcConfBackupFailureAction.enable" value="${(alarmConfig.adcConfBackupFailureAction.enable)}" />
				</td>	
				<td>
					-
				</td>	
				<td>
					<#if (alarmConfig.adcConfBackupFailureAction.syslog) == 1>
						<input class="syslogChk" id="alarmConfigs.adcConfBackupFailureAction.syslog" id_h="adcConfBackupFailureAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="syslogChk" id="alarmConfigs.adcConfBackupFailureAction.syslog" id_h="adcConfBackupFailureAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="adcConfBackupFailureAction_syslog_h" name="alarmConfigs.adcConfBackupFailureAction.syslog" value="${(alarmConfig.adcConfBackupFailureAction.syslog)}" />
				</td>
				<td>
					<#if (alarmConfig.adcConfBackupFailureAction.snmptrap) == 1>
						<input class="snmptrapChk" id="alarmConfigs.adcConfBackupFailureAction.snmptrap" id_h="adcConfBackupFailureAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="snmptrapChk" id="alarmConfigs.adcConfBackupFailureAction.snmptrap" id_h="adcConfBackupFailureAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="adcConfBackupFailureAction_snmptrap_h" name="alarmConfigs.adcConfBackupFailureAction.snmptrap" value="${(alarmConfig.adcConfBackupFailureAction.snmptrap)}" />
				</td>			
                <td>
                    <#if (alarmConfig.adcConfBackupFailureAction.sms) == 1>
                        <input class="smsChk" id="alarmConfigs.adcConfBackupFailureAction.snmptrap" id_h="adcConfBackupFailureAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                    <#else>
                        <input class="smsChk" id="alarmConfigs.adcConfBackupFailureAction.snmptrap" id_h="adcConfBackupFailureAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                    </#if>
                    <input type="hidden" id="adcConfBackupFailureAction_sms_h" name="alarmConfigs.adcConfBackupFailureAction.sms" value="${(alarmConfig.adcConfBackupFailureAction.sms)}" />
                </td>           
			</tr>		
			<tr>
				<th>
					${LANGCODEMAP["MSG_ALERT_ADC_SETTING_SYN"]!}</th>
				<td>
					<#if (alarmConfig.adcConfSyncFailureAction.enable) == 1>
						<input class="enableChk" id="alarmConfigs.adcConfSyncFailureAction.enable" id_h="adcConfSyncFailureAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="enableChk" id="alarmConfigs.adcConfSyncFailureAction.enable" id_h="adcConfSyncFailureAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="adcConfSyncFailureAction_enable_h" name="alarmConfigs.adcConfSyncFailureAction.enable" value="${(alarmConfig.adcConfSyncFailureAction.enable)}" />
				</td>	
				<td>
					-
				</td>	
				<td>
					<#if (alarmConfig.adcConfSyncFailureAction.syslog) == 1>
						<input class="syslogChk" id="alarmConfigs.adcConfSyncFailureAction.syslog" id_h="adcConfSyncFailureAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="syslogChk" id="alarmConfigs.adcConfSyncFailureAction.syslog" id_h="adcConfSyncFailureAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="adcConfSyncFailureAction_syslog_h" name="alarmConfigs.adcConfSyncFailureAction.syslog" value="${(alarmConfig.adcConfSyncFailureAction.syslog)}" />
				</td>
				<td>
					<#if (alarmConfig.adcConfSyncFailureAction.snmptrap) == 1>
						<input class="snmptrapChk" id="alarmConfigs.adcConfSyncFailureAction.snmptrap" id_h="adcConfSyncFailureAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="snmptrapChk" id="alarmConfigs.adcConfSyncFailureAction.snmptrap" id_h="adcConfSyncFailureAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="adcConfSyncFailureAction_snmptrap_h" name="alarmConfigs.adcConfSyncFailureAction.snmptrap" value="${(alarmConfig.adcConfSyncFailureAction.snmptrap)}" />
				</td>			
                <td>
                    <#if (alarmConfig.adcConfSyncFailureAction.sms) == 1>
                        <input class="smsChk" id="alarmConfigs.adcConfSyncFailureAction.sms" id_h="adcConfSyncFailureAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                    <#else>
                        <input class="smsChk" id="alarmConfigs.adcConfSyncFailureAction.sms" id_h="adcConfSyncFailureAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                    </#if>
                    <input type="hidden" id="adcConfSyncFailureAction_sms_h" name="alarmConfigs.adcConfSyncFailureAction.sms" value="${(alarmConfig.adcConfSyncFailureAction.sms)}" />
                </td>           
			</tr>		
			<tr>
				<th>
					${LANGCODEMAP["MSG_ALERT_CPU_TEMPERATURE"]!}</th>
				<td>
					<#if (alarmConfig.cpuTempTooHighAction.enable) == 1>
						<input class="enableChk" id="alarmConfigs.cpuTempTooHighAction.enable" id_h="cpuTempTooHighAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="enableChk" id="alarmConfigs.acpuTempTooHighAction.enable" id_h="cpuTempTooHighAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="cpuTempTooHighAction_enable_h" name="alarmConfigs.cpuTempTooHighAction.enable" value="${(alarmConfig.cpuTempTooHighAction.enable)}" />
				</td>	
				<td>
					<input name="alarmConfigs.cpuTempTooHighAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.cpuTempTooHighAction.intervalValue)!''}" maxlength="3" > 
					<select name="alarmConfigs.cpuTempTooHighAction.intervalUnit" class="inputSelect timeType">
						<#if (alarmConfig.cpuTempTooHighAction.intervalUnit) == "M">
							<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
							
						<#elseif (alarmConfig.cpuTempTooHighAction.intervalUnit) == "H">					
							<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
							
						<#else>
						</#if>
					</select>
				</td>	
				<td>				
					<#if (alarmConfig.cpuTempTooHighAction.syslog) == 1>
						<input class="syslogChk" id="alarmConfigs.cpuTempTooHighAction.syslog" id_h="cpuTempTooHighAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="syslogChk" id="alarmConfigs.cpuTempTooHighAction.syslog" id_h="cpuTempTooHighAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="cpuTempTooHighAction_syslog_h" name="alarmConfigs.cpuTempTooHighAction.syslog" value="${(alarmConfig.cpuTempTooHighAction.syslog)}" />
				</td>
				<td>				
					<#if (alarmConfig.cpuTempTooHighAction.snmptrap) == 1>
						<input class="snmptrapChk" id="alarmConfigs.cpuTempTooHighAction.snmptrap" id_h="cpuTempTooHighAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="snmptrapChk" id="alarmConfigs.cpuTempTooHighAction.snmptrap" id_h="cpuTempTooHighAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="cpuTempTooHighAction_snmptrap_h" name="alarmConfigs.cpuTempTooHighAction.snmptrap" value="${(alarmConfig.cpuTempTooHighAction.snmptrap)}" />
				</td>			
                <td>                
                    <#if (alarmConfig.cpuTempTooHighAction.sms) == 1>
                        <input class="smsChk" id="alarmConfigs.cpuTempTooHighAction.sms" id_h="cpuTempTooHighAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                    <#else>
                        <input class="smsChk" id="alarmConfigs.cpuTempTooHighAction.sms" id_h="cpuTempTooHighAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                    </#if>
                    <input type="hidden" id="cpuTempTooHighAction_sms_h" name="alarmConfigs.cpuTempTooHighAction.sms" value="${(alarmConfig.cpuTempTooHighAction.sms)}" />
                </td>           
			</tr>		
			<tr>
				<th>
					${LANGCODEMAP["MSG_ALERT_CPU_FAN_MOVEMENT"]!}
				</th>
				<td>
					<#if (alarmConfig.cpuFanTooSlowAction.enable) == 1>
						<input class="enableChk" id="alarmConfigs.cpuFanTooSlowAction.enable" id_h="cpuFanTooSlowAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="enableChk" id="alarmConfigs.cpuFanTooSlowAction.enable" id_h="cpuFanTooSlowAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="cpuFanTooSlowAction_enable_h" name="alarmConfigs.cpuFanTooSlowAction.enable" value="${(alarmConfig.cpuFanTooSlowAction.enable)}" />
				</td>	
				<td>
					<input name="alarmConfigs.cpuFanTooSlowAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.cpuFanTooSlowAction.intervalValue)!''}" maxlength="3" > 
					<select name="alarmConfigs.cpuFanTooSlowAction.intervalUnit" class="inputSelect timeType">
						<#if (alarmConfig.cpuFanTooSlowAction.intervalUnit) == "M">
							<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
							
						<#elseif (alarmConfig.cpuFanTooSlowAction.intervalUnit) == "H">					
							<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
							
						<#else>
						</#if>
					</select>
				</td>	
				<td>
					<#if (alarmConfig.cpuFanTooSlowAction.syslog) == 1>
						<input class="syslogChk" id="alarmConfigs.cpuFanTooSlowAction.syslog" id_h="cpuFanTooSlowAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="syslogChk" id="alarmConfigs.cpuFanTooSlowAction.syslog" id_h="cpuFanTooSlowAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="cpuFanTooSlowAction_syslog_h" name="alarmConfigs.cpuFanTooSlowAction.syslog" value="${(alarmConfig.cpuFanTooSlowAction.syslog)}" />
				</td>
				<td>
					<#if (alarmConfig.cpuFanTooSlowAction.snmptrap) == 1>
						<input class="snmptrapChk" id="alarmConfigs.cpuFanTooSlowAction.snmptrap" id_h="cpuFanTooSlowAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="snmptrapChk" id="alarmConfigs.cpuFanTooSlowAction.snmptrap" id_h="cpuFanTooSlowAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="cpuFanTooSlowAction_snmptrap_h" name="alarmConfigs.cpuFanTooSlowAction.snmptrap" value="${(alarmConfig.cpuFanTooSlowAction.snmptrap)}" />
				</td>			
                <td>
                    <#if (alarmConfig.cpuFanTooSlowAction.sms) == 1>
                        <input class="smsChk" id="alarmConfigs.cpuFanTooSlowAction.sms" id_h="cpuFanTooSlowAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                    <#else>
                        <input class="smsChk" id="alarmConfigs.cpuFanTooSlowAction.sms" id_h="cpuFanTooSlowAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                    </#if>
                    <input type="hidden" id="cpuFanTooSlowAction_sms_h" name="alarmConfigs.cpuFanTooSlowAction.sms" value="${(alarmConfig.cpuFanTooSlowAction.sms)}" />
                </td>           
			</tr>
			<tr>
				<th>
					${LANGCODEMAP["MSG_ALERT_CPU_FAN_STATUS_FAULTY"]!}
				</th>
				<td>
					<#if (alarmConfig.cpuFanBadAction.enable) == 1>
						<input class="enableChk" id="alarmConfigs.cpuFanBadAction.enable" id_h="cpuFanBadAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="enableChk" id="alarmConfigs.cpuFanBadAction.enable" id_h="cpuFanBadAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="cpuFanBadAction_enable_h" name="alarmConfigs.cpuFanBadAction.enable" value="${(alarmConfig.cpuFanBadAction.enable)}" />
				</td>	
				<td>
					<input name="alarmConfigs.cpuFanBadAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.cpuFanBadAction.intervalValue)!''}" maxlength="3" > 
					<select name="alarmConfigs.cpuFanBadAction.intervalUnit" class="inputSelect timeType">
						<#if (alarmConfig.cpuFanBadAction.intervalUnit) == "M">
							<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
							
						<#elseif (alarmConfig.cpuFanBadAction.intervalUnit) == "H">					
							<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
							
						<#else>
						</#if>
					</select>
				</td>	
				<td>
					<#if (alarmConfig.cpuFanBadAction.syslog) == 1>
						<input class="syslogChk" id="alarmConfigs.cpuFanBadAction.syslog" id_h="cpuFanBadAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="syslogChk" id="alarmConfigs.cpuFanBadAction.syslog" id_h="cpuFanBadAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="cpuFanBadAction_syslog_h" name="alarmConfigs.cpuFanBadAction.syslog" value="${(alarmConfig.cpuFanBadAction.syslog)}" />
				</td>
				<td>
					<#if (alarmConfig.cpuFanBadAction.snmptrap) == 1>
						<input class="snmptrapChk" id="alarmConfigs.cpuFanBadAction.snmptrap" id_h="cpuFanBadAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="snmptrapChk" id="alarmConfigs.cpuFanBadAction.snmptrap" id_h="cpuFanBadAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="cpuFanBadAction_snmptrap_h" name="alarmConfigs.cpuFanBadAction.snmptrap" value="${(alarmConfig.cpuFanBadAction.snmptrap)}" />
				</td>			
                <td>
                    <#if (alarmConfig.cpuFanBadAction.sms) == 1>
                        <input class="smsChk" id="alarmConfigs.cpuFanBadAction.sms" id_h="cpuFanBadAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                    <#else>
                        <input class="smsChk" id="alarmConfigs.cpuFanBadAction.sms" id_h="cpuFanBadAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                    </#if>
                    <input type="hidden" id="cpuFanBadAction_sms_h" name="alarmConfigs.cpuFanBadAction.sms" value="${(alarmConfig.cpuFanBadAction.sms)}" />
                </td>           
			</tr>
			<tr>
				<th>
					${LANGCODEMAP["MSG_ALERT_CHASSIS_TEMPERATURE"]!}
				</th>
				<td>
					<#if (alarmConfig.chassisTempTooHighAction.enable) == 1>
						<input class="enableChk" id="alarmConfigs.chassisTempTooHighAction.enable" id_h="chassisTempTooHighAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="enableChk" id="alarmConfigs.chassisTempTooHighAction.enable" id_h="chassisTempTooHighAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="chassisTempTooHighAction_enable_h" name="alarmConfigs.chassisTempTooHighAction.enable" value="${(alarmConfig.chassisTempTooHighAction.enable)}" />
				</td>	
				<td>
					<input name="alarmConfigs.chassisTempTooHighAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.chassisTempTooHighAction.intervalValue)!''}" maxlength="3" > 
					<select name="alarmConfigs.chassisTempTooHighAction.intervalUnit" class="inputSelect timeType">
						<#if (alarmConfig.chassisTempTooHighAction.intervalUnit) == "M">
							<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
							
						<#elseif (alarmConfig.chassisTempTooHighAction.intervalUnit) == "H">					
							<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
							
						<#else>
						</#if>
					</select>
				</td>	
				<td>
					<#if (alarmConfig.chassisTempTooHighAction.syslog) == 1>
						<input class="syslogChk" id="alarmConfigs.chassisTempTooHighAction.syslog" id_h="chassisTempTooHighAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="syslogChk" id="alarmConfigs.chassisTempTooHighAction.syslog" id_h="chassisTempTooHighAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="chassisTempTooHighAction_syslog_h" name="alarmConfigs.chassisTempTooHighAction.syslog" value="${(alarmConfig.chassisTempTooHighAction.syslog)}" />
				</td>			
				<td>
					<#if (alarmConfig.chassisTempTooHighAction.snmptrap) == 1>
						<input class="snmptrapChk" id="alarmConfigs.chassisTempTooHighAction.snmptrap" id_h="chassisTempTooHighAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="snmptrapChk" id="alarmConfigs.chassisTempTooHighAction.snmptrap" id_h="chassisTempTooHighAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="chassisTempTooHighAction_snmptrap_h" name="alarmConfigs.chassisTempTooHighAction.snmptrap" value="${(alarmConfig.chassisTempTooHighAction.snmptrap)}" />
				</td>
                <td>
                    <#if (alarmConfig.chassisTempTooHighAction.sms) == 1>
                        <input class="smsChk" id="alarmConfigs.chassisTempTooHighAction.sms" id_h="chassisTempTooHighAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                    <#else>
                        <input class="smsChk" id="alarmConfigs.chassisTempTooHighAction.sms" id_h="chassisTempTooHighAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                    </#if>
                    <input type="hidden" id="chassisTempTooHighAction_ssms_h" name="alarmConfigs.chassisTempTooHighAction.sms" value="${(alarmConfig.chassisTempTooHighAction.sms)}" />
                </td>
			</tr>
			<tr>
				<th>
					${LANGCODEMAP["MSG_ALERT_CHASSIS_FAN_STATUS"]!}
				</th>
				<td>
					<#if (alarmConfig.chassisFanBadAction.enable) == 1>
						<input class="enableChk" id="alarmConfigs.chassisFanBadAction.enable" id_h="chassisFanBadAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="enableChk" id="alarmConfigs.chassisFanBadAction.enable" id_h="chassisFanBadAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="chassisFanBadAction_enable_h" name="alarmConfigs.chassisFanBadAction.enable" value="${(alarmConfig.chassisFanBadAction.enable)}" />
				</td>	
				<td>
					<input name="alarmConfigs.chassisFanBadAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.chassisFanBadAction.intervalValue)!''}" maxlength="3" > 
					<select name="alarmConfigs.chassisFanBadAction.intervalUnit" class="inputSelect timeType">
						<#if (alarmConfig.chassisFanBadAction.intervalUnit) == "M">
							<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
							
						<#elseif (alarmConfig.chassisFanBadAction.intervalUnit) == "H">					
							<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
							
						<#else>
						</#if>
					</select>
				</td>	
				<td>
					<#if (alarmConfig.chassisFanBadAction.syslog) == 1>
						<input class="syslogChk" id="alarmConfigs.chassisFanBadAction.syslog" id_h="chassisFanBadAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="syslogChk" id="alarmConfigs.chassisFanBadAction.syslog" id_h="chassisFanBadAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="chassisFanBadAction_syslog_h" name="alarmConfigs.chassisFanBadAction.syslog" value="${(alarmConfig.chassisFanBadAction.syslog)}" />
				</td>
				<td>
					<#if (alarmConfig.chassisFanBadAction.snmptrap) == 1>
						<input class="snmptrapChk" id="alarmConfigs.chassisFanBadAction.snmptrap" id_h="chassisFanBadAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="snmptrapChk" id="alarmConfigs.chassisFanBadAction.snmptrap" id_h="chassisFanBadAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="chassisFanBadAction_snmptrap_h" name="alarmConfigs.chassisFanBadAction.snmptrap" value="${(alarmConfig.chassisFanBadAction.snmptrap)}" />
				</td>			
                <td>
                    <#if (alarmConfig.chassisFanBadAction.sms) == 1>
                        <input class="smsChk" id="alarmConfigs.chassisFanBadAction.sms" id_h="chassisFanBadAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                    <#else>
                        <input class="smsChk" id="alarmConfigs.chassisFanBadAction.sms" id_h="chassisFanBadAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                    </#if>
                    <input type="hidden" id="chassisFanBadAction_sms_h" name="alarmConfigs.chassisFanBadAction.sms" value="${(alarmConfig.chassisFanBadAction.sms)}" />
                </td>           
			</tr>
			<tr>
				<th>
					${LANGCODEMAP["MSG_ALERT_CHASSIS_POWER_STATUS"]!}
				</th>
				<td>
					<#if (alarmConfig.chassisPowerSupplyBadAction.enable) == 1>
						<input class="enableChk" id="alarmConfigs.chassisPowerSupplyBadAction.enable" id_h="chassisPowerSupplyBadAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="enableChk" id="alarmConfigs.chassisPowerSupplyBadAction.enable" id_h="chassisPowerSupplyBadAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="chassisPowerSupplyBadAction_enable_h" name="alarmConfigs.chassisPowerSupplyBadAction.enable" value="${(alarmConfig.chassisPowerSupplyBadAction.enable)}" />
				</td>	
				<td>
					<input name="alarmConfigs.chassisPowerSupplyBadAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.chassisPowerSupplyBadAction.intervalValue)!''}" maxlength="3" > 
					<select name="alarmConfigs.chassisPowerSupplyBadAction.intervalUnit" class="inputSelect timeType">
						<#if (alarmConfig.chassisPowerSupplyBadAction.intervalUnit) == "M">
							<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
							
						<#elseif (alarmConfig.chassisPowerSupplyBadAction.intervalUnit) == "H">					
							<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
							
						<#else>
						</#if>
					</select>
				</td>	
				<td>
					<#if (alarmConfig.chassisPowerSupplyBadAction.syslog) == 1>
						<input class="syslogChk" id="alarmConfigs.chassisPowerSupplyBadAction.syslog" id_h="chassisPowerSupplyBadAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="syslogChk" id="alarmConfigs.chassisPowerSupplyBadAction.syslog" id_h="chassisPowerSupplyBadAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="chassisPowerSupplyBadAction_syslog_h" name="alarmConfigs.chassisPowerSupplyBadAction.syslog" value="${(alarmConfig.chassisPowerSupplyBadAction.syslog)}" />
				</td>
				<td>
					<#if (alarmConfig.chassisPowerSupplyBadAction.snmptrap) == 1>
						<input class="snmptrapChk" id="alarmConfigs.chassisPowerSupplyBadAction.snmptrap" id_h="chassisPowerSupplyBadAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="snmptrapChk" id="alarmConfigs.chassisPowerSupplyBadAction.snmptrap" id_h="chassisPowerSupplyBadAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="chassisPowerSupplyBadAction_snmptrap_h" name="alarmConfigs.chassisPowerSupplyBadAction.snmptrap" value="${(alarmConfig.chassisPowerSupplyBadAction.snmptrap)}" />
				</td>			
                <td>
                    <#if (alarmConfig.chassisPowerSupplyBadAction.sms) == 1>
                        <input class="smsChk" id="alarmConfigs.chassisPowerSupplyBadAction.sms" id_h="chassisPowerSupplyBadAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                    <#else>
                        <input class="smsChk" id="alarmConfigs.chassisPowerSupplyBadAction.sms" id_h="chassisPowerSupplyBadAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                    </#if>
                    <input type="hidden" id="chassisPowerSupplyBadAction_sms_h" name="alarmConfigs.chassisPowerSupplyBadAction.sms" value="${(alarmConfig.chassisPowerSupplyBadAction.sms)}" />
                </td>           
			</tr>
			<tr>
				<th>
					${LANGCODEMAP["MSG_ALERT_POWER_HIGH"]!}
				</th>
				<td>
					<#if (alarmConfig.voltageTooHighAction.enable) == 1>
						<input class="enableChk" id="alarmConfigs.voltageTooHighAction.enable" id_h="voltageTooHighAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="enableChk" id="alarmConfigs.voltageTooHighAction.enable" id_h="voltageTooHighAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="voltageTooHighAction_enable_h" name="alarmConfigs.voltageTooHighAction.enable" value="${(alarmConfig.voltageTooHighAction.enable)}" />
				</td>	
				<td>
					<input name="alarmConfigs.voltageTooHighAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.voltageTooHighAction.intervalValue)!''}" maxlength="3" > 
					<select name="alarmConfigs.voltageTooHighAction.intervalUnit" class="inputSelect timeType">
						<#if (alarmConfig.voltageTooHighAction.intervalUnit) == "M">
							<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
							
						<#elseif (alarmConfig.voltageTooHighAction.intervalUnit) == "H">					
							<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
							
						<#else>
						</#if>
					</select>
				</td>	
				<td>
					<#if (alarmConfig.voltageTooHighAction.syslog) == 1>
						<input class="syslogChk" id="alarmConfigs.voltageTooHighAction.syslog" id_h="voltageTooHighAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="syslogChk" id="alarmConfigs.voltageTooHighAction.syslog" id_h="voltageTooHighAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="voltageTooHighAction_syslog_h" name="alarmConfigs.voltageTooHighAction.syslog" value="${(alarmConfig.voltageTooHighAction.syslog)}" />
				</td>
				<td>
					<#if (alarmConfig.voltageTooHighAction.snmptrap) == 1>
						<input class="snmptrapChk" id="alarmConfigs.voltageTooHighAction.snmptrap" id_h="voltageTooHighAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="snmptrapChk" id="alarmConfigs.voltageTooHighAction.snmptrap" id_h="voltageTooHighAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="voltageTooHighAction_snmptrap_h" name="alarmConfigs.voltageTooHighAction.snmptrap" value="${(alarmConfig.voltageTooHighAction.snmptrap)}" />
				</td>			
                <td>
                    <#if (alarmConfig.voltageTooHighAction.sms) == 1>
                        <input class="smsChk" id="alarmConfigs.voltageTooHighAction.sms" id_h="voltageTooHighAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                    <#else>
                        <input class="smsChk" id="alarmConfigs.voltageTooHighAction.sms" id_h="voltageTooHighAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                    </#if>
                    <input type="hidden" id="voltageTooHighAction_sms_h" name="alarmConfigs.voltageTooHighAction.sms" value="${(alarmConfig.voltageTooHighAction.sms)}" />
                </td>           
			</tr>
			<tr>
				<th>
					${LANGCODEMAP["MSG_ALERT_CHASSIS_FAN_MOVEMENT"]!}
				</th>
				<td>
					<#if (alarmConfig.chassisFanTooSlowAction.enable) == 1>
						<input class="enableChk" id="alarmConfigs.chassisFanTooSlowAction.enable" id_h="chassisFanTooSlowAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="enableChk" id="alarmConfigs.chassisFanTooSlowAction.enable" id_h="chassisFanTooSlowAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="chassisFanTooSlowAction_enable_h" name="alarmConfigs.chassisFanTooSlowAction.enable" value="${(alarmConfig.chassisFanTooSlowAction.enable)}" />
				</td>	
				<td>
					<input name="alarmConfigs.chassisFanTooSlowAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.chassisFanTooSlowAction.intervalValue)!''}" maxlength="3" > 
					<select name="alarmConfigs.chassisFanTooSlowAction.intervalUnit" class="inputSelect timeType">
						<#if (alarmConfig.chassisFanTooSlowAction.intervalUnit) == "M">
							<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
							
						<#elseif (alarmConfig.chassisFanTooSlowAction.intervalUnit) == "H">					
							<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
							
						<#else>
						</#if>
					</select>
				</td>	
				<td>
					<#if (alarmConfig.chassisFanTooSlowAction.syslog) == 1>
						<input class="syslogChk" id="alarmConfigs.chassisFanTooSlowAction.syslog" id_h="chassisFanTooSlowAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="syslogChk" id="alarmConfigs.chassisFanTooSlowAction.syslog" id_h="chassisFanTooSlowAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="chassisFanTooSlowAction_syslog_h" name="alarmConfigs.chassisFanTooSlowAction.syslog" value="${(alarmConfig.chassisFanTooSlowAction.syslog)}" />
				</td>
				<td>
					<#if (alarmConfig.chassisFanTooSlowAction.snmptrap) == 1>
						<input class="snmptrapChk" id="alarmConfigs.chassisFanTooSlowAction.snmptrap" id_h="chassisFanTooSlowAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="snmptrapChk" id="alarmConfigs.chassisFanTooSlowAction.snmptrap" id_h="chassisFanTooSlowAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="chassisFanTooSlowAction_snmptrap_h" name="alarmConfigs.chassisFanTooSlowAction.snmptrap" value="${(alarmConfig.chassisFanTooSlowAction.snmptrap)}" />
				</td>			
                <td>
                    <#if (alarmConfig.chassisFanTooSlowAction.sms) == 1>
                        <input class="smsChk" id="alarmConfigs.chassisFanTooSlowAction.sms" id_h="chassisFanTooSlowAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                    <#else>
                        <input class="smsChk" id="alarmConfigs.chassisFanTooSlowAction.sms" id_h="chassisFanTooSlowAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                    </#if>
                    <input type="hidden" id="chassisFanTooSlowAction_sms_h" name="alarmConfigs.chassisFanTooSlowAction.sms" value="${(alarmConfig.chassisFanTooSlowAction.sms)}" />
                </td>           
			</tr>
			<tr>
				<th>
					${LANGCODEMAP["MSG_ALERT_DDOS"]!}
				</th>
				<td>
					<#if (alarmConfig.blockDDoSAction.enable) == 1>
						<input class="enableChk" id="alarmConfigs.blockDDoSAction.enable" id_h="blockDDoSAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="enableChk" id="alarmConfigs.blockDDoSAction.enable" id_h="blockDDoSAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="blockDDoSAction_enable_h" name="alarmConfigs.blockDDoSAction.enable" value="${(alarmConfig.blockDDoSAction.enable)}" />
				</td>	
				<td>
					<input name="alarmConfigs.blockDDoSAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.blockDDoSAction.intervalValue)!''}" maxlength="3" > 
					<select name="alarmConfigs.blockDDoSAction.intervalUnit" class="inputSelect timeType">
						<#if (alarmConfig.blockDDoSAction.intervalUnit) == "M">
							<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
							
						<#elseif (alarmConfig.blockDDoSAction.intervalUnit) == "H">					
							<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
							
						<#else>
						</#if>
					</select>
				</td>	
				<td>
					<#if (alarmConfig.blockDDoSAction.syslog) == 1>
						<input class="syslogChk" id="alarmConfigs.blockDDoSAction.syslog" id_h="blockDDoSAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="syslogChk" id="alarmConfigs.blockDDoSAction.syslog" id_h="blockDDoSAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="blockDDoSAction_syslog_h" name="alarmConfigs.blockDDoSAction.syslog" value="${(alarmConfig.blockDDoSAction.syslog)}" />
				</td>
				<td>
					<#if (alarmConfig.blockDDoSAction.snmptrap) == 1>
						<input class="snmptrapChk" id="alarmConfigs.blockDDoSAction.snmptrap" id_h="blockDDoSAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="snmptrapChk" id="alarmConfigs.blockDDoSAction.snmptrap" id_h="blockDDoSAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="blockDDoSAction_snmptrap_h" name="alarmConfigs.blockDDoSAction.snmptrap" value="${(alarmConfig.blockDDoSAction.snmptrap)}" />
				</td>			
                <td>
                    <#if (alarmConfig.blockDDoSAction.sms) == 1>
                        <input class="smsChk" id="alarmConfigs.blockDDoSAction.sms" id_h="blockDDoSAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                    <#else>
                        <input class="smsChk" id="alarmConfigs.blockDDoSAction.sms" id_h="blockDDoSAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                    </#if>
                    <input type="hidden" id="blockDDoSAction_sms_h" name="alarmConfigs.blockDDoSAction.sms" value="${(alarmConfig.blockDDoSAction.sms)}" />
                </td>           
			</tr>
		</tbody>
	</table>