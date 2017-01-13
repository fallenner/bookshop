function escapeJquery(srcString){
    // 转义之后的结果
    var escapseResult = srcString;
    // javascript正则表达式中的特殊字符
    var jsSpecialChars = ["\\", "^", "$", "*", "?", ".", "+", "(", ")", "[", "]", "|", "{", "}"];
    // jquery中的特殊字符,不是正则表达式中的特殊字符
    var jquerySpecialChars = ["~", "`", "@", "#", "%", "&", "=", "'", "\"",":", ";", "<", ">", ",", "/"];
    for (var i = 0; i < jsSpecialChars.length; i++) {
        escapseResult = escapseResult.replace(new RegExp("\\"+ jsSpecialChars[i], "g"), "\\"+ jsSpecialChars[i]);
    }
    for (var i = 0; i < jquerySpecialChars.length; i++) {
        escapseResult = escapseResult.replace(new RegExp(jquerySpecialChars[i], "g"), "\\" + jquerySpecialChars[i]);
    }
    return escapseResult;
}

/**
 * 页面上的input封装成json
 */
function getWhere(id){
    var param = {};
    if(id!=null){
        $("#"+id+" > input:hidden,#"+id+" > input:text,#"+id+" > select").each(function () {
            var name = $(this).attr("name");//获取name属性
            var value=$(this).val();
            if(null!=name && null != value){
                //param[name]=value;
                setKey(param, name, value);
            }
        });
        $("#"+id+" > input:radio,#"+id+" > input:checkbox").filter(':checked').each(function () {
            var name = $(this).attr("name");//获取name属性
            var value=$(this).val();
            if(null!=name && null != value){
                //param[name]=value;
                setKey(param, name, value);
            }
        });
    }else{
        $("input:hidden,input:text,select").each(function () {
            var name = $(this).attr("name");//获取name属性
            var value=$(this).val();
            if(null!=name && null != value){
                //param[name]=value;
                setKey(param, name, value);
            }
        });
        $('input:radio,input:checkbox').filter(':checked').each(function () {
            var name = $(this).attr("name");//获取name属性
            var value=$(this).val();
            if(null!=name && null != value){
                //param[name]=value;
                setKey(param, name, value);
            }
        });
    }
    return param;
}
function setKey(obj, key, value){
    if(obj[key]!=undefined){
        obj[key]=obj[key]+","+value;
    }
    else{
        obj[key]=value;
    }
}