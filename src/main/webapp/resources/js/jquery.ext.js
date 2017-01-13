/**
 * Created by liwj on 2015-1-9.
 */

$.extend({
    alert: function(msg, status) {
        if(status!=null && status=="true"){
            layer.alert(msg,{icon:1, title:'提示'});
        }
        else{
            layer.alert(msg,{icon:0, title:'提示'});
        }

    },
    confirm: function(msg,callback) {
        layer.confirm(msg,{icon: 3, title:'提示'},function(index){
            layer.close(index);
            callback();
        });
    },
    msg:function(msg){
        layer.msg(msg,{icon:2});
    },
    edit:function(url){
        $("form[id^='hiddenForm_']").remove();
        var timestamp = (new Date()).getTime();
        var hiddenFormId = "hiddenForm_"+timestamp;
        var formStr = "<form id='"+hiddenFormId+"' action='"+url+"' method='post'>";
        $("input:hidden,input:text,select").each(function () {
            var name = $(this).attr("name");//获取name属性
            var value=$(this).val();
            if(null!=name && null != value){
                formStr += "<input type='hidden' name='"+name+"' value='"+value+"'/>";
            }
        });
        $('input:radio,input:checkbox').filter(':checked').each(function () {
            var name = $(this).attr("name");//获取name属性
            var value=$(this).val();
            if(null!=name && null != value){
                formStr += "<input type='hidden' name='"+name+"' value='"+value+"'/>";
            }
        });
        formStr += "</form>";
        $("BODY").append(formStr);
        $("#"+hiddenFormId).submit();
    },
    remove:function(url, callback){
        $.ajax({
            type:"POST",
            url:url,
            data:getWhereStr(),
            cache:false,
            async:false,
            dataType:"json",
            success: function(dataResult){
                //layer.msg(dataResult.msgText, 2, {type:1,shade:false});
                $.alert(dataResult.msgText);
                callback();
            }
        });
    },

    dialog: function (url,title,width,height) {
        if(title==null){title="";}
        if(width==null||width==""){width="800px";}
        if(height==null||height==""){height="600px";}
        layer.open({
            type: 2,
            title: title,
            shade: [0.2, '#000'],
            //border: [0],
            area: [width , height],
            content: url
        });
    },
    close:function(){
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    },
    divDialog: function(domId,title){
        if(title==null){title="";}
        $.layer({
            type: 1,
            title: [
                title,
                'border:none; background:#909ebb; color:#fff;'
            ],
            shade: [0.2, '#000'],
            area: ['auto', 'auto'],
            border: [0],
            page: {dom : domId},
            close: function(index){
                $('body').css("overflow","auto");
            }
        });
        $('body').css("overflow","hidden");
    },
    divClose:function(){
        setBodyAuto();
        layer.closeAll();
    }
});
