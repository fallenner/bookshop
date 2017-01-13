<#--
表格标签：用于显示列表数据。
-->
<#macro table id url sumUrl="" multiselect=false refresh=false defaultCondition=""
    viewfunc="" addfunc="" editfunc="" delfunc="" searchfunc=""
    otherButton="" width="" height=155 footerrow=false>
<div class="form-group" >

</div>
<table id="grid-table-${id}"></table>
<div id="grid-pager-${id}"></div>
<script type="text/javascript">
jQuery(function($) {
    var grid_selector_${id} = "#grid-table-${id}";
    var pager_selector_${id} = "#grid-pager-${id}";

    //resize to fit page size
    $(window).on('resize.jqGrid', function () {
        <#if width!="">
            $(grid_selector_${id}).jqGrid( 'setGridWidth', "${width}");
        <#else>
            $(grid_selector_${id}).jqGrid( 'setGridWidth', $(".page-content").width());
        </#if>

        var windowHeight = $(window).height();
        var footHeight = $("#page-footer").height();
        if(footHeight==null){footHeight=0;}
        var navbarHeight = $("#navbar").height();
        if(navbarHeight==null){navbarHeight=0;}
        var breadcrumbsHeight = $("#breadcrumbs").height();
        if(breadcrumbsHeight==null){breadcrumbsHeight=0;}
        $(grid_selector_${id}).jqGrid( 'setGridHeight', windowHeight-navbarHeight-breadcrumbsHeight-footHeight-${height} );
    });

    //resize on sidebar collapse/expand
    var parent_column = $(grid_selector_${id}).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid' , function(ev, event_name, collapsed) {
        if( event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed' ) {
            setTimeout(function() {
                $(grid_selector_${id}).jqGrid( 'setGridWidth', parent_column.width() );
            }, 0);
        }
    });
    var colNameList = new Array();
    var colModelList = new Array();
    var groupHeaderList = new Array();
    <#nested/>
    jQuery(grid_selector_${id}).jqGrid({
        url: "${baseUrl}${url}",
        datatype: "json",
        mtype: 'POST',
        colNames:colNameList,
        colModel:colModelList,
        viewrecords : true,
        rowNum:10,
        rowList:[10,20,50,100,200,500],
        pager : pager_selector_${id},
        altRows: true,
        <#if defaultCondition!="">
            postData:${defaultCondition},
        </#if>
        <#if multiselect>
            multiselect: true,
            multiboxonly: true,
        </#if>
        loadComplete : function() {
            var table = this;
            setTimeout(function(){
                styleCheckbox(table);
                updateActionIcons(table);
                updatePagerIcons(table);
                enableTooltips(table);
            }, 0);
        }
        <#if footerrow>
            ,footerrow: true,
            gridComplete: function() {
                var rowNum = parseInt($(this).getGridParam('records'),10);
                if(rowNum > 0){
                    jQuery.ajax({
                        type:"POST",
                        url:"${baseUrl}${sumUrl}",
                        data:getWhere(),
                        cache:false,
                        async:false,
                        dataType:"json",
                        success: function(dataResult){
                            jQuery(".ui-jqgrid-sdiv").show();
                            jQuery(grid_selector_${id}).footerData("set",dataResult);
                        }
                    });
                }else{
                    jQuery(".ui-jqgrid-sdiv").hide();
                }
            }
        </#if>
    });
    if(groupHeaderList.length>0){
        jQuery(grid_selector_${id}).jqGrid('setGroupHeaders', {
            useColSpanStyle: true,
            groupHeaders:groupHeaderList
        });
    }

    $(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size

    //navButtons
    jQuery(grid_selector_${id}).jqGrid('navGrid',pager_selector_${id}, {add:false,edit:false,del:false,view:false,search:false,refresh:true,refreshicon : 'ace-icon fa fa-refresh green'});
    <#if addfunc!="">
        jQuery(grid_selector_${id}).jqGrid('navButtonAdd',pager_selector_${id},{caption:'',buttonicon:'ace-icon fa fa-plus-circle purple',onClickButton:${addfunc},title:'添加新纪录'});
    </#if>
    <#--<#if editfunc!="">-->
        <#--jQuery(grid_selector_${id}).jqGrid('navButtonAdd',pager_selector_${id},{caption:'',buttonicon:'ace-icon fa fa-pencil blue',title:'编辑选中的纪录',onClickButton:function(){-->
            <#--var selectedId = $(grid_selector_${id}).jqGrid("getGridParam", "selrow");-->
                <#--if(selectedId){-->
                    <#--var rowData = $(grid_selector_${id}).jqGrid("getRowData", selectedId);-->
                    <#--${editfunc}(rowData);-->
            <#--}-->
            <#--else{-->
                <#--$.alert("请选择需要操作的数据行");-->
            <#--}-->
        <#--}});-->
    <#--</#if>-->
    <#--jQuery("#btn-del-${id}").click(function () {-->
        <#--var selectedId = $(grid_selector_${id}).jqGrid("getGridParam", "selrow");-->
        <#--if(selectedId){-->
            <#--var rowData = $(grid_selector_${id}).jqGrid("getRowData", selectedId);-->
                <#--<#if delfunc!="">${delfunc}(rowData);</#if>-->
        <#--}-->
        <#--else{-->
            <#--$.alert("请选择需要操作的数据行");-->
        <#--}-->
    <#--});-->
    <#--jQuery("#btn-view-${id}").click(function () {-->
        <#--var selectedId = $(grid_selector_${id}).jqGrid("getGridParam", "selrow");-->
        <#--if(selectedId){-->
            <#--var rowData = $(grid_selector_${id}).jqGrid("getRowData", selectedId);-->
            <#--${viewfunc}(rowData);-->
        <#--}-->
        <#--else{-->
            <#--$.alert("请选择需要操作的数据行");-->
        <#--}-->
    <#--});-->
    <#--jQuery("#btn-search-${id}").click(function () {-->
        <#--<#if searchfunc!="">${searchfunc}();</#if>-->
    <#--});-->
    <#--jQuery("#btn-refresh-${id}").click(function () {-->
        <#--$(grid_selector_${id}).jqGrid("setGridParam", { page:1 }).trigger("reloadGrid");-->
    <#--});-->

    <#--<#if otherButton!="">-->
        <#--jQuery("button[id^='btn-other']").click(function () {-->
            <#--var func = this.getAttribute("func");-->
            <#--var selectedId = $(grid_selector_${id}).jqGrid("getGridParam", "selrow");-->
            <#--if(selectedId){-->
                <#--var rowData = $(grid_selector_${id}).jqGrid("getRowData", selectedId);-->
                <#--eval(func+"(rowData)");-->
            <#--}-->
            <#--else{-->
                <#--$.alert("请选择需要操作的数据行");-->
            <#--}-->
        <#--});-->
    <#--</#if>-->
    //it causes some flicker when reloading or navigating grid
    //it may be possible to have some custom formatter to do this as the grid is being created to prevent this
    //or go back to default browser checkbox styles for the grid
    function styleCheckbox(table) {
    }
    //unlike navButtons icons, action icons in rows seem to be hard-coded
    //you can change them like this in here if you want
    function updateActionIcons(table) {
    }
    //replace icons with FontAwesome icons like above
    function updatePagerIcons(table) {
        var replacement =
        {
            'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
            'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
            'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
            'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

            if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
        })
    }
    function enableTooltips(table) {
        $('.navtable .ui-pg-button').tooltip({container:'body'});
        $(table).find('.ui-pg-div').tooltip({container:'body'});
    }
    $(document).one('ajaxloadstart.page', function(e) {
        $(grid_selector_${id}).jqGrid('GridUnload');
        $('.ui-jqdialog').remove();
    });
});
function optReloadGrid(){
    var postData = getWhere();
    <!--var page = jQuery("#grid-table-${id}").jqGrid('getGridParam','page');-->
    jQuery("#grid-table-${id}").jqGrid('setGridParam',{page:1,postData:postData}).trigger("reloadGrid");
}

function optReloadGridByIndex(tabIndex){
       var postData = getWhere();
        var arr = $("table[id^='grid-table-']");
        if(!tabIndex || tabIndex>=arr.length){
            tabIndex = 0;
        }
       jQuery("#"+arr[tabIndex].id).jqGrid('setGridParam',{page:1,postData:postData}).trigger("reloadGrid");
}
</script>
</#macro>