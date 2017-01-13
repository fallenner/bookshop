<#--
表格列标签：展示数据列。
-->
<#macro column name title width=60 hidden=false formatter="" buttons="" align="center">
<#if title="">
colNameList.push("${name}");
<#else>
colNameList.push("${title}");
</#if>
<#if buttons!="">
colModelList.push({name:'${name}',index:'', width:${width}, fixed:true, align:"${align}",sortable:false, resize:false,formatter:${buttons}});
<#else>
colModelList.push({name:'${name}',index:'${name}', width:${width}, sortable:false, align:"${align}",editable: false<#if hidden>,hidden:true</#if><#if formatter!="">,formatter:${formatter}</#if>});
</#if>
</#macro>
