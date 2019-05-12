<div class="col-md-8div">
	<#list dashboardServiceData.adcs?sort_by('name') as dashSvcData>
		<#if (dashSvcData_index+1) % 8 == 1>
			<#if dashSvcData.status == 0>
				<#include "dashboard_status_off.ftl">
			<#else>
				<#include "dashboard_status_on.ftl">
			</#if>
		</#if>
	</#list>
</div>
<div class="col-md-8div">
	<#list dashboardServiceData.adcs?sort_by('name') as dashSvcData>
		<#if (dashSvcData_index+1) % 8 == 2>
			<#if dashSvcData.status == 0>
				<#include "dashboard_status_off.ftl">
			<#else>
				<#include "dashboard_status_on.ftl">
			</#if>
		</#if>
	</#list>
</div>
<div class="col-md-8div">
	<#list dashboardServiceData.adcs?sort_by('name') as dashSvcData>
		<#if (dashSvcData_index+1) % 8 == 3>
			<#if dashSvcData.status == 0>
				<#include "dashboard_status_off.ftl">
			<#else>
				<#include "dashboard_status_on.ftl">
			</#if>
		</#if>
	</#list>
</div>
<div class="col-md-8div">
	<#list dashboardServiceData.adcs?sort_by('name') as dashSvcData>
		<#if (dashSvcData_index+1) % 8 == 4>
			<#if dashSvcData.status == 0>
				<#include "dashboard_status_off.ftl">
			<#else>
				<#include "dashboard_status_on.ftl">
			</#if>
		</#if>
	</#list>
</div>
<div class="col-md-8div">
	<#list dashboardServiceData.adcs?sort_by('name') as dashSvcData>
		<#if (dashSvcData_index+1) % 8 == 5>
			<#if dashSvcData.status == 0>
				<#include "dashboard_status_off.ftl">
			<#else>
				<#include "dashboard_status_on.ftl">
			</#if>
		</#if>
	</#list>
</div>
<div class="col-md-8div">
	<#list dashboardServiceData.adcs?sort_by('name') as dashSvcData>
		<#if (dashSvcData_index+1) % 8 == 6>
			<#if dashSvcData.status == 0>
				<#include "dashboard_status_off.ftl">
			<#else>
				<#include "dashboard_status_on.ftl">
			</#if>
		</#if>
	</#list>
</div>
<div class="col-md-8div">
	<#list dashboardServiceData.adcs?sort_by('name') as dashSvcData>
		<#if (dashSvcData_index+1) % 8 == 7>
			<#if dashSvcData.status == 0>
				<#include "dashboard_status_off.ftl">
			<#else>
				<#include "dashboard_status_on.ftl">
			</#if>
		</#if>
	</#list>
</div>
<div class="col-md-8div">
	<#list dashboardServiceData.adcs?sort_by('name') as dashSvcData>
		<#if (dashSvcData_index+1) % 8 == 0>
			<#if dashSvcData.status == 0>
				<#include "dashboard_status_off.ftl">
			<#else>
				<#include "dashboard_status_on.ftl">
			</#if>
		</#if>
	</#list>
</div>




