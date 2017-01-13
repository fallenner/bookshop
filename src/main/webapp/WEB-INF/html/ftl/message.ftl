<#--
表格列标签：展示数据列。
-->
<#macro msg content status="">
    <#if (status!="" && status="success")>
    <div class="alert alert-success">
    <#elseif (status!="" && status="error")>
    <div class="alert alert-danger">
    <#elseif (status!="" && status="warning")>
    <div class="alert alert-warning">
    <#else>
    <div class="alert alert-info">
    </#if>
    <button type="button" class="close" data-dismiss="alert">
        <i class="ace-icon fa fa-times"></i>
    </button>
    <strong>
    ${content!}
    </strong>
</div>
</#macro>
