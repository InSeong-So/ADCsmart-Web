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
			<td colspan="4" >
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
                    <input class="smsChk" id="alarmConfigs.adcStandbyAction.sms" id_h="adcStandbyAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.adcStandbyAction.sms" id_h="adcStandbyAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
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
                    <input class="smsChk"  id="alarmConfigs.adcActiveAction.sms" id_h="adcActiveAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk"  id="alarmConfigs.adcActiveAction.sms" id_h="adcActiveAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
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
                    <input class="smsChk" id="alarmConfigs.poolMemberDownAction.snmptrap" id_h="poolMemberDownAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="ssmsChk" id="alarmConfigs.poolMemberDownAction.snmptrap" id_h="poolMemberDownAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
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
		<tr>
			<th>${LANGCODEMAP["MSG_ALERT_VRRP_VRID_CONFLICT"]!}</th>
			<td>
				<#if (alarmConfig.vrrpCollisionAction.enable) == 1>
					<input class="enableChk" id="alarmConfigs.vrrpCollisionAction.enable" id_h="vrrpCollisionAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="enableChk" id="alarmConfigs.vrrpCollisionAction.enable" id_h="vrrpCollisionAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="vrrpCollisionAction_enable_h" name="alarmConfigs.vrrpCollisionAction.enable" value="${(alarmConfig.vrrpCollisionAction.enable)}" />
			</td>	
			<td>
				-
			</td>	
			<td>
				<#if (alarmConfig.vrrpCollisionAction.syslog) == 1>
					<input class="syslogChk" id="alarmConfigs.vrrpCollisionAction.syslog" id_h="vrrpCollisionAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="syslogChk" id="alarmConfigs.vrrpCollisionAction.syslog" id_h="vrrpCollisionAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="vrrpCollisionAction_syslog_h" name="alarmConfigs.vrrpCollisionAction.syslog" value="${(alarmConfig.vrrpCollisionAction.syslog)}" />
			</td>
			<td>
				<#if (alarmConfig.vrrpCollisionAction.snmptrap) == 1>
					<input class="snmptrapChk" id="alarmConfigs.vrrpCollisionAction.snmptrap" id_h="vrrpCollisionAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="snmptrapChk" id="alarmConfigs.vrrpCollisionAction.snmptrap" id_h="vrrpCollisionAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="vrrpCollisionAction_snmptrap_h" name="alarmConfigs.vrrpCollisionAction.snmptrap" value="${(alarmConfig.vrrpCollisionAction.snmptrap)}" />
			</td>			
            <td>
                <#if (alarmConfig.vrrpCollisionAction.sms) == 1>
                    <input class="smsChk" id="alarmConfigs.vrrpCollisionAction.sms" id_h="vrrpCollisionAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.vrrpCollisionAction.sms" id_h="vrrpCollisionAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                </#if>
                <input type="hidden" id="vrrpCollisionAction_sms_h" name="alarmConfigs.vrrpCollisionAction.sms" value="${(alarmConfig.vrrpCollisionAction.sms)}" />
            </td>           
		</tr>
		<tr>
			<th>${LANGCODEMAP["MSG_ALERT_GATEWAY_DOWN"]!}</th>
			<td>
				<#if (alarmConfig.gatewayDownAction.enable) == 1>
					<input class="enableChk" id="alarmConfigs.gatewayDownAction.enable" id_h="gatewayDownAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="enableChk" id="alarmConfigs.gatewayDownAction.enable" id_h="gatewayDownAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="gatewayDownAction_enable_h" name="alarmConfigs.gatewayDownAction.enable" value="${(alarmConfig.gatewayDownAction.enable)}" />
			</td>	
			<td>
				-
			</td>	
			<td>
				<#if (alarmConfig.gatewayDownAction.syslog) == 1>
					<input class="syslogChk" id="alarmConfigs.gatewayDownAction.syslog" id_h="gatewayDownAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="syslogChk" id="alarmConfigs.gatewayDownAction.syslog" id_h="gatewayDownAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="gatewayDownAction_syslog_h" name="alarmConfigs.gatewayDownAction.syslog" value="${(alarmConfig.gatewayDownAction.syslog)}" />
			</td>
			<td>
				<#if (alarmConfig.gatewayDownAction.snmptrap) == 1>
					<input class="snmptrapChk" id="alarmConfigs.gatewayDownAction.snmptrap" id_h="gatewayDownAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="snmptrapChk" id="alarmConfigs.gatewayDownAction.snmptrap" id_h="gatewayDownAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="gatewayDownAction_snmptrap_h" name="alarmConfigs.gatewayDownAction.snmptrap" value="${(alarmConfig.gatewayDownAction.snmptrap)}" />
			</td>			
            <td>
                <#if (alarmConfig.gatewayDownAction.sms) == 1>
                    <input class="smsChk" id="alarmConfigs.gatewayDownAction.sms" id_h="gatewayDownAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.gatewayDownAction.sms" id_h="gatewayDownAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                </#if>
                <input type="hidden" id="gatewayDownAction_sms_h" name="alarmConfigs.gatewayDownAction.sms" value="${(alarmConfig.gatewayDownAction.sms)}" />
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
            <th><input class="allSMSChk1" type="checkbox" name="checkbox" id="checkbox" disabled="disabled">&nbsp;SMS전송</th>            
		</tr>
		</thead>			
		<tbody>
		<tr>
			<th>
				<span class="Alertset_w123">${LANGCODEMAP["MSG_ALERT_MP_USAGE"]!}</span>
				<input name="alarmConfigs.adcMPValue" type="text" class="inputText width40 align_center paddingR4" value="${(alarmConfig.adcMPValue)!''}" maxlength="3">
				&nbsp;${LANGCODEMAP["MSG_ALERT_OVER_PERCENT"]!}
			</th>
			<td>
				<#if (alarmConfig.adcMPAction.enable) == 1>
					<input class="enableChk" id="alarmConfigs.adcMPAction.enable" id_h="adcMPAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="enableChk" id="alarmConfigs.adcMPAction.enable" id_h="adcMPAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>			
				<input type="hidden" id="adcMPAction_enable_h" name="alarmConfigs.adcMPAction.enable" value="${(alarmConfig.adcMPAction.enable)}" />						
			</td>	
			<td>
				<input name="alarmConfigs.adcMPAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.adcMPAction.intervalValue)!''}" maxlength="3" > 
				<select name="alarmConfigs.adcMPAction.intervalUnit" class="inputSelect timeType">
					<#if (alarmConfig.adcMPAction.intervalUnit) == "M">
						<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
					<#elseif (alarmConfig.adcMPAction.intervalUnit) == "H">
						<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
					<#else>
					</#if>
				</select>
			</td>	
			<td>
				<#if (alarmConfig.adcMPAction.syslog) == 1>
					<input class="syslogChk" id="alarmConfigs.adcMPAction.syslog" id_h="adcMPAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="syslogChk" id="alarmConfigs.adcMPAction.syslog" id_h="adcMPAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>			
				<input type="hidden" id="adcMPAction_syslog_h" name="alarmConfigs.adcMPAction.syslog" value="${(alarmConfig.adcMPAction.syslog)}" />						
			</td>
			<td>
				<#if (alarmConfig.adcMPAction.snmptrap) == 1>
					<input class="snmptrapChk" id="alarmConfigs.adcMPAction.snmptrap" id_h="adcMPAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="snmptrapChk" id="alarmConfigs.adcMPAction.snmptrap" id_h="adcMPAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>			
				<input type="hidden" id="adcMPAction_snmptrap_h" name="alarmConfigs.adcMPAction.snmptrap" value="${(alarmConfig.adcMPAction.snmptrap)}" />						
			</td>			
            <td>
                <#if (alarmConfig.adcMPAction.sms) == 1>
                    <input class="smsChk" id="alarmConfigs.adcMPAction.sms" id_h="adcMPAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.adcMPAction.sms" id_h="adcMPAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                </#if>          
                <input type="hidden" id="adcMPAction_sms_h" name="alarmConfigs.adcMPAction.sms" value="${(alarmConfig.adcMPAction.sms)}" />                      
            </td>           
		</tr>		
		<tr>
			<th>
				<span class="Alertset_w123">${LANGCODEMAP["MSG_ALERT_SP_USAGE"]!}</span>
				<input name="alarmConfigs.adcSPValue" type="text" class="inputText width40 align_center paddingR4" value="${(alarmConfig.adcSPValue)!''}" maxlength="3">
				&nbsp;${LANGCODEMAP["MSG_ALERT_OVER_PERCENT"]!}
			</th>
			<td>
				<#if (alarmConfig.adcSPAction.enable) == 1>
					<input class="enableChk" id="alarmConfigs.adcSPAction.enable" id_h="adcSPAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="enableChk" id="alarmConfigs.adcSPAction.enable" id_h="adcSPAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>			
				<input type="hidden" id="adcSPAction_enable_h" name="alarmConfigs.adcSPAction.enable" value="${(alarmConfig.adcSPAction.enable)}" />						
			</td>	
			<td>
				<input name="alarmConfigs.adcSPAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.adcSPAction.intervalValue)!''}" maxlength="3" > 
				<select name="alarmConfigs.adcSPAction.intervalUnit" class="inputSelect timeType">
					<#if (alarmConfig.adcSPAction.intervalUnit) == "M">
						<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
					<#elseif (alarmConfig.adcSPAction.intervalUnit) == "H">
						<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
					<#else>
					</#if>
				</select>
			</td>	
			<td>
				<#if (alarmConfig.adcSPAction.syslog) == 1>
					<input class="syslogChk" id="alarmConfigs.adcSPAction.syslog" id_h="adcSPAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="syslogChk" id="alarmConfigs.adcSPAction.syslog" id_h="adcSPAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>			
				<input type="hidden" id="adcSPAction_syslog_h" name="alarmConfigs.adcSPAction.syslog" value="${(alarmConfig.adcSPAction.syslog)}" />						
			</td>
			<td>
				<#if (alarmConfig.adcSPAction.snmptrap) == 1>
					<input class="snmptrapChk" id="alarmConfigs.adcSPAction.snmptrap" id_h="adcSPAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="snmptrapChk" id="alarmConfigs.adcSPAction.snmptrap" id_h="adcSPAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>			
				<input type="hidden" id="adcSPAction_snmptrap_h" name="alarmConfigs.adcSPAction.snmptrap" value="${(alarmConfig.adcSPAction.snmptrap)}" />						
			</td>			
            <td>
                <#if (alarmConfig.adcSPAction.sms) == 1>
                    <input class="smsChk" id="alarmConfigs.adcSPAction.sms" id_h="adcSPAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.adcSPAction.sms" id_h="adcSPAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                </#if>          
                <input type="hidden" id="adcSPAction_sms_h" name="alarmConfigs.adcSPAction.sms" value="${(alarmConfig.adcSPAction.sms)}" />                      
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
                    <input class="smsChk" id="alarmConfigs.adcMemAction.sms" id_h="adcMemAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.adcMemAction.sms" id_h="adcMemAction_ssms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
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
                    <input class="smsChk" id="alarmConfigs.connectionLimitLowAction.sms" id_h="connectionLimitLowAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.connectionLimitLowAction.sms" id_h="connectionLimitLowAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                </#if>
                <input type="hidden" id="connectionLimitLowAction_sms_h" name="alarmConfigs.connectionLimitLowAction.sms" value="${(alarmConfig.connectionLimitLowAction.sms)}" />
            </td>           
		</tr>
		<tr>
			<th>
				${LANGCODEMAP["MSG_ALERT_FILTERSESSIONS"]!}&nbsp;
				<input name="alarmConfigs.filterSessionHighValue" type="text" class="inputText align_center width110 paddingR4" value="${(alarmConfig.filterSessionHighValue)!''}">
				&nbsp;${LANGCODEMAP["MSG_ALERT_OVER_COUNT_SEC"]!}
			</th>
			<td>
				<#if (alarmConfig.filterSessionLimitHighAction.enable) == 1>
					<input class="enableChk" id="alarmConfigs.filterSessionLimitHighAction.enable" id_h="filterSessionLimitHighAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="enableChk" id="alarmConfigs.filterSessionLimitHighAction.enable" id_h="filterSessionLimitHighAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="filterSessionLimitHighAction_enable_h" name="alarmConfigs.filterSessionLimitHighAction.enable" value="${(alarmConfig.filterSessionLimitHighAction.enable)}" />
			</td>	
			<td>
				<input name="alarmConfigs.filterSessionLimitHighAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.filterSessionLimitHighAction.intervalValue)!''}" maxlength="3" > 
				<select name="alarmConfigs.filterSessionLimitHighAction.intervalUnit" class="inputSelect timeType">
					<#if (alarmConfig.filterSessionLimitHighAction.intervalUnit) == "M">
						<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
					<#elseif (alarmConfig.filterSessionLimitHighAction.intervalUnit) == "H">					
						<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
						<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
					<#else>
					</#if>
				</select>
			</td>	
			<td>
				<#if (alarmConfig.filterSessionLimitHighAction.syslog) == 1>
					<input class="syslogChk" id="alarmConfigs.filterSessionLimitHighAction.syslog" id_h="filterSessionLimitHighAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="syslogChk" id="alarmConfigs.filterSessionLimitHighAction.syslog" id_h="filterSessionLimitHighAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="filterSessionLimitHighAction_syslog_h" name="alarmConfigs.filterSessionLimitHighAction.syslog" value="${(alarmConfig.filterSessionLimitHighAction.syslog)!''}" />
			</td>	
			<td>
				<#if (alarmConfig.filterSessionLimitHighAction.snmptrap) == 1>
					<input class="snmptrapChk" id="alarmConfigs.filterSessionLimitHighAction.snmptrap" id_h="filterSessionLimitHighAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
				<#else>
					<input class="snmptrapChk" id="alarmConfigs.filterSessionLimitHighAction.snmptrap" id_h="filterSessionLimitHighAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
				</#if>
				<input type="hidden" id="filterSessionLimitHighAction_snmptrap_h" name="alarmConfigs.filterSessionLimitHighAction.snmptrap" value="${(alarmConfig.filterSessionLimitHighAction.snmptrap)!''}" />
			</td>		
            <td>
                <#if (alarmConfig.filterSessionLimitHighAction.sms) == 1>
                    <input class="smsChk" id="alarmConfigs.filterSessionLimitHighAction.sms" id_h="filterSessionLimitHighAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                <#else>
                    <input class="smsChk" id="alarmConfigs.filterSessionLimitHighAction.sms" id_h="filterSessionLimitHighAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                </#if>
                <input type="hidden" id="filterSessionLimitHighAction_sms_h" name="alarmConfigs.filterSessionLimitHighAction.sms" value="${(alarmConfig.filterSessionLimitHighAction.sms)!''}" />
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
                    <input class="smspChk" id="alarmConfigs.responseTimeAction.sms" id_h="responseTimeAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
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
					<input type="hidden" id="interfaceUsageLimitAction_snmptrap_h" name="alarmConfigs.interfaceUsageLimitAction.snmptrap" value="${(alarmConfig.interfaceUsageLimitAction.snmptrap)}" />
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
				<td>
					<#if (alarmConfig.interfaceDuplexChangeAction.enable) == 1>
						<input class="enableChk" id="alarmConfigs.interfaceDuplexChangeAction.enable" id_h="interfaceDuplexChangeAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="enableChk" id="alarmConfigs.interfaceDuplexChangeAction.enable" id_h="interfaceDuplexChangeAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="interfaceDuplexChangeAction_enable_h" name="alarmConfigs.interfaceDuplexChangeAction.enable" value="${(alarmConfig.interfaceDuplexChangeAction.enable)}" />
				</td>	
				<td>
					-
				</td>	
				<td>
					<#if (alarmConfig.interfaceDuplexChangeAction.syslog) == 1>
						<input class="syslogChk" id="alarmConfigs.interfaceDuplexChangeAction.syslog" id_h="interfaceDuplexChangeAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="syslogChk" id="alarmConfigs.interfaceDuplexChangeAction.syslog" id_h="interfaceDuplexChangeAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="interfaceDuplexChangeAction_syslog_h" name="alarmConfigs.interfaceDuplexChangeAction.syslog" value="${(alarmConfig.interfaceDuplexChangeAction.syslog)}" />
				</td>
				<td>
					<#if (alarmConfig.interfaceDuplexChangeAction.snmptrap) == 1>
						<input class="snmptrapChk" id="alarmConfigs.interfaceDuplexChangeAction.snmptrap" id_h="interfaceDuplexChangeAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="snmptrapChk" id="alarmConfigs.interfaceDuplexChangeAction.snmptrap" id_h="interfaceDuplexChangeAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="interfaceDuplexChangeAction_snmptrap_h" name="alarmConfigs.interfaceDuplexChangeAction.snmptrap" value="${(alarmConfig.interfaceDuplexChangeAction.snmptrap)}" />
				</td>				
                <td>
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
                        <input class="smsChk" id="alarmConfigs.adcConfBackupFailureAction.sms" id_h="adcConfBackupFailureAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                    <#else>
                        <input class="smsChk" id="alarmConfigs.adcConfBackupFailureAction.sms" id_h="adcConfBackupFailureAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                    </#if>
                    <input type="hidden" id="adcConfBackupFailureAction_sms_h" name="alarmConfigs.adcConfBackupFailureAction.sms" value="${(alarmConfig.adcConfBackupFailureAction.sms)}" />
                </td>               
			</tr>		
			<tr>
				<th>
					${LANGCODEMAP["MSG_ALERT_CPU_TEMPERATURE"]!}&nbsp;${LANGCODEMAP["MSG_ALERT_HIGH"]!}</th>
				<td>
					<#if (alarmConfig.temperatureTooHighAction.enable) == 1>
						<input class="enableChk" id="alarmConfigs.temperatureTooHighAction.enable" id_h="temperatureTooHighAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="enableChk" id="alarmConfigs.temperatureTooHighAction.enable" id_h="temperatureTooHighAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="temperatureTooHighAction_enable_h" name="alarmConfigs.temperatureTooHighAction.enable" value="${(alarmConfig.temperatureTooHighAction.enable)}" />
				</td>	
				<td>
					<input name="alarmConfigs.temperatureTooHighAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.temperatureTooHighAction.intervalValue)!''}" maxlength="3" > 
					<select name="alarmConfigs.temperatureTooHighAction.intervalUnit" class="inputSelect timeType">
						<#if (alarmConfig.temperatureTooHighAction.intervalUnit) == "M">
							<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
						<#elseif (alarmConfig.temperatureTooHighAction.intervalUnit) == "H">
							<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
						<#else>
						</#if>
					</select>
				</td>	
				<td>
					<#if (alarmConfig.temperatureTooHighAction.syslog) == 1>
						<input class="syslogChk" id="alarmConfigs.temperatureTooHighAction.syslog" id_h="temperatureTooHighAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="syslogChk" id="alarmConfigs.temperatureTooHighAction.syslog" id_h="temperatureTooHighAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="temperatureTooHighAction_syslog_h" name="alarmConfigs.temperatureTooHighAction.syslog" value="${(alarmConfig.temperatureTooHighAction.syslog)}" />
				</td>	
				<td>
					<#if (alarmConfig.temperatureTooHighAction.snmptrap) == 1>
						<input class="snmptrapChk" id="alarmConfigs.temperatureTooHighAction.snmptrap" id_h="temperatureTooHighAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="snmptrapChk" id="alarmConfigs.temperatureTooHighAction.snmptrap" id_h="temperatureTooHighAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="temperatureTooHighAction_snmptrap_h" name="alarmConfigs.temperatureTooHighAction.snmptrap" value="${(alarmConfig.temperatureTooHighAction.snmptrap)}" />
				</td>			
                <td>
                    <#if (alarmConfig.temperatureTooHighAction.sms) == 1>
                        <input class="smsChk" id="alarmConfigs.temperatureTooHighAction.sms" id_h="temperatureTooHighAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                    <#else>
                        <input class="smsChk" id="alarmConfigs.temperatureTooHighAction.sms" id_h="temperatureTooHighAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                    </#if>
                    <input type="hidden" id="temperatureTooHighAction_sms_h" name="alarmConfigs.temperatureTooHighAction.sms" value="${(alarmConfig.temperatureTooHighAction.sms)}" />
                </td>           
			</tr>		
			<tr>
				<th>
					${LANGCODEMAP["MSG_ALERT_FAN_STATUS"]!}&nbsp;${LANGCODEMAP["MSG_ALERT_FAULTY"]!}</th>
				<td>
					<#if (alarmConfig.fanNotOperationalAction.enable) == 1>
						<input class="enableChk" id="alarmConfigs.fanNotOperationalAction.enable" id_h="fanNotOperationalAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="enableChk" id="alarmConfigs.afanNotOperationalAction.enable" id_h="fanNotOperationalAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="fanNotOperationalAction_enable_h" name="alarmConfigs.fanNotOperationalAction.enable" value="${(alarmConfig.fanNotOperationalAction.enable)}" />
				</td>	
				<td>
					<input name="alarmConfigs.fanNotOperationalAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.fanNotOperationalAction.intervalValue)!''}" maxlength="3" > 
					<select name="alarmConfigs.fanNotOperationalAction.intervalUnit" class="inputSelect timeType">
						<#if (alarmConfig.fanNotOperationalAction.intervalUnit) == "M">
							<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
						<#elseif (alarmConfig.fanNotOperationalAction.intervalUnit) == "H">
							<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
						<#else>
						</#if>
					</select>
				</td>	
				<td>				
					<#if (alarmConfig.fanNotOperationalAction.syslog) == 1>
						<input class="syslogChk" id="alarmConfigs.fanNotOperationalAction.syslog" id_h="fanNotOperationalAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="syslogChk" id="alarmConfigs.fanNotOperationalAction.syslog" id_h="fanNotOperationalAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="fanNotOperationalAction_syslog_h" name="alarmConfigs.fanNotOperationalAction.syslog" value="${(alarmConfig.fanNotOperationalAction.syslog)}" />
				</td>
				<td>				
					<#if (alarmConfig.fanNotOperationalAction.snmptrap) == 1>
						<input class="snmptrapChk" id="alarmConfigs.fanNotOperationalAction.snmptrap" id_h="fanNotOperationalAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="snmptrapChk" id="alarmConfigs.fanNotOperationalAction.snmptrap" id_h="fanNotOperationalAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="fanNotOperationalAction_snmptrap_h" name="alarmConfigs.fanNotOperationalAction.snmptrap" value="${(alarmConfig.fanNotOperationalAction.snmptrap)}" />
				</td>			
                <td>                
                    <#if (alarmConfig.fanNotOperationalAction.sms) == 1>
                        <input class="smsChk" id="alarmConfigs.fanNotOperationalAction.sms" id_h="fanNotOperationalAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                    <#else>
                        <input class="smsChk" id="alarmConfigs.fanNotOperationalAction.sms" id_h="fanNotOperationalAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                    </#if>
                    <input type="hidden" id="fanNotOperationalAction_sms_h" name="alarmConfigs.fanNotOperationalAction.sms" value="${(alarmConfig.fanNotOperationalAction.sms)}" />
                </td>           
			</tr>		
			<tr>
				<th>
					${LANGCODEMAP["MSG_ALERT_POWER_ONE_USE"]!}
				</th>
				<td>
					<#if (alarmConfig.onlyOnePowerSupplyAction.enable) == 1>
						<input class="enableChk" id="alarmConfigs.onlyOnePowerSupplyAction.enable" id_h="onlyOnePowerSupplyAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="enableChk" id="alarmConfigs.onlyOnePowerSupplyAction.enable" id_h="onlyOnePowerSupplyAction_enable_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="onlyOnePowerSupplyAction_enable_h" name="alarmConfigs.onlyOnePowerSupplyAction.enable" value="${(alarmConfig.onlyOnePowerSupplyAction.enable)}" />
				</td>	
				<td>
					<input name="alarmConfigs.onlyOnePowerSupplyAction.intervalValue" type="text" class="inputText width40 align_center paddingR4 timeValue" value="${(alarmConfig.onlyOnePowerSupplyAction.intervalValue)!''}" maxlength="3" > 
					<select name="alarmConfigs.onlyOnePowerSupplyAction.intervalUnit" class="inputSelect timeType">
						<#if (alarmConfig.onlyOnePowerSupplyAction.intervalUnit) == "M">
							<option value="M" selected="selected">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
						<#elseif (alarmConfig.onlyOnePowerSupplyAction.intervalUnit) == "H">
							<option value="M">${LANGCODEMAP["MSG_ALERT_MINUTE"]!}</option>
							<option value="H" selected="selected">${LANGCODEMAP["MSG_ALERT_HOUR"]!}</option>
						<#else>
						</#if>
					</select>
				</td>	
				<td>
					<#if (alarmConfig.onlyOnePowerSupplyAction.syslog) == 1>
						<input class="syslogChk" id="alarmConfigs.onlyOnePowerSupplyAction.syslog" id_h="onlyOnePowerSupplyAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="syslogChk" id="alarmConfigs.onlyOnePowerSupplyAction.syslog" id_h="onlyOnePowerSupplyAction_syslog_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="onlyOnePowerSupplyAction_syslog_h" name="alarmConfigs.onlyOnePowerSupplyAction.syslog" value="${(alarmConfig.onlyOnePowerSupplyAction.syslog)}" />
				</td>	
				<td>
					<#if (alarmConfig.onlyOnePowerSupplyAction.snmptrap) == 1>
						<input class="snmptrapChk" id="alarmConfigs.onlyOnePowerSupplyAction.snmptrap" id_h="onlyOnePowerSupplyAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
					<#else>
						<input class="snmptrapChk" id="alarmConfigs.onlyOnePowerSupplyAction.snmptrap" id_h="onlyOnePowerSupplyAction_snmptrap_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
					</#if>
					<input type="hidden" id="onlyOnePowerSupplyAction_snmptrap_h" name="alarmConfigs.onlyOnePowerSupplyAction.snmptrap" value="${(alarmConfig.onlyOnePowerSupplyAction.snmptrap)}" />
				</td>			
                <td>
                    <#if (alarmConfig.onlyOnePowerSupplyAction.sms) == 1>
                        <input class="smsChk" id="alarmConfigs.onlyOnePowerSupplyAction.sms" id_h="onlyOnePowerSupplyAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" checked="checked" value="1"/> 
                    <#else>
                        <input class="smshk" id="alarmConfigs.onlyOnePowerSupplyAction.sms" id_h="onlyOnePowerSupplyAction_sms_h" type="checkbox" onchange="changeCheckbox(this);" value="0"/> 
                    </#if>
                    <input type="hidden" id="onlyOnePowerSupplyAction_sms_h" name="alarmConfigs.onlyOnePowerSupplyAction.sms" value="${(alarmConfig.onlyOnePowerSupplyAction.sms)}" />
                </td>           
			</tr>
		</tbody>
	</table>