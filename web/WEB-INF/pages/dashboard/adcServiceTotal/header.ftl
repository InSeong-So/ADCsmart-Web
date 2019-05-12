<#setting number_format="0.####">
<#if langCode??>
  <#if "ko_KR" == langCode><#assign img_lang = ""></#if>
  <#if "en_US" == langCode><#assign img_lang = "_eng"></#if>
</#if>
<nav class="navbar navbar-inverse navbar-fixed-top">
     <#include "dashboard_top_navi.ftl">
</nav>
<#include "content.ftl"/>
<#include "dashboard_modal.ftl"/>
<#include "dashboard_modal_js.ftl"/>
