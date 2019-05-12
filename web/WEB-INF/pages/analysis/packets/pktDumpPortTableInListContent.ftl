<select name="interfaceName" id="interfaceName" class="inputSelect width134">
	<option value="">${LANGCODEMAP["MSG_DIAG_ANAL_SELECTE_PORT"]!}</option>
<#list portInterfaceNameList as thePortInterfaceName>
	<option value="${thePortInterfaceName.portName!''}">${thePortInterfaceName.portName!''}</option>
</#list>
</select>