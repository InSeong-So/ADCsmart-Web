<div class="status_area" style="margin-top:45px;">
    <#include "dashboard_top_content.ftl">
</div>    
<div class="container-fluid">
	<div>
		<div class="col-md-10 nopadding">
			<#if dashboardServiceData.adcs?size gt 40>
				<#include "dashboard_content_8div.ftl">
			<#else>			
				<#include "dashboard_content_5div.ftl">
			</#if>
		</div>
		
		<div class="col-md-2" >
			<#include "dashboard_side_content.ftl"> 
		</div>
	</div>
</div> <!-- /* container-fluid -->

<!-- Custom styles for this template -->
  
<link type="text/css" rel="stylesheet" href="/css/font-awesome-4.7.0/css/font-awesome.min.css"/>
<!-- Bootstrap core CSS -->
<link type="text/css" rel="stylesheet" href="/css/bootstrap.min.css"/>
<link type="text/css" rel="stylesheet" href="/css/adcsmart_theme.css"/> 