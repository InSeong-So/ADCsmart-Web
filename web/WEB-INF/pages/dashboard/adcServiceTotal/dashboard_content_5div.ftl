<div class="col-md-5div">
	<#list dashboardServiceData.adcs?sort_by('name') as dashSvcData>
		<#if (dashSvcData_index+1) % 5 == 1>
			<#if dashSvcData.status == 0>
				<#include "dashboard_status_off.ftl">
			<#else>
				<#include "dashboard_status_on.ftl">
			</#if>
		</#if>
	</#list>
</div>
<div class="col-md-5div">
	<#list dashboardServiceData.adcs?sort_by('name') as dashSvcData>
		<#if (dashSvcData_index+1) % 5 == 2>
			<#if dashSvcData.status == 0>
				<#include "dashboard_status_off.ftl">
			<#else>
				<#include "dashboard_status_on.ftl">
			</#if>
		</#if>
	</#list>
</div>
<div class="col-md-5div">
	<#list dashboardServiceData.adcs?sort_by('name') as dashSvcData>
		<#if (dashSvcData_index+1) % 5 == 3>
			<#if dashSvcData.status == 0>
				<#include "dashboard_status_off.ftl">
			<#else>
				<#include "dashboard_status_on.ftl">
			</#if>
		</#if>
	</#list>
</div>
<div class="col-md-5div">
	<#list dashboardServiceData.adcs?sort_by('name') as dashSvcData>
		<#if (dashSvcData_index+1) % 5 == 4>
			<#if dashSvcData.status == 0>
				<#include "dashboard_status_off.ftl">
			<#else>
				<#include "dashboard_status_on.ftl">
			</#if>
		</#if>
	</#list>
</div>
<div class="col-md-5div">
	<#list dashboardServiceData.adcs?sort_by('name') as dashSvcData>
		<#if (dashSvcData_index+1) % 5 == 0>
			<#if dashSvcData.status == 0>
				<#include "dashboard_status_off.ftl">
			<#else>
				<#include "dashboard_status_on.ftl">
			</#if>
		</#if>
	</#list>
</div>




