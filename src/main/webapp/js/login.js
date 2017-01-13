// JavaScript Document
//支持Enter键登录
		document.onkeydown = function(e){
			if($(".bac").length==0)
			{
				if(!e) e = window.event;
				if((e.keyCode || e.which) == 13){
					var obtnLogin=document.getElementById("submit_btn")
					obtnLogin.focus();
				}
			}
		}
		function changeValidateCode(baseUrl){
			$("#captcha_img").attr("src",baseUrl+"/validate_code/get?time="+ new Date().getMilliseconds());
		}
    	$(function(){
			//提交表单
			$('#submit_btn').click(function(){
				show_loading();
				//var myReg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/; //邮件正则
				if($('#username').val() == ''){
					show_err_msg('用户名还没填呢！');	
					$('#username').focus();
				}else if($('#password').val() == ''){
					show_err_msg('密码还没填呢！');
					$('#password').focus();
				}else if($('#j_captcha').val() == ''){
					show_err_msg('验证码还没填呢！');
					$('#j_captcha').focus();
				}else{
					//ajax提交表单，#login_form为表单的ID。 如：$('#login_form').ajaxSubmit(function(data) { ... });
					$('#login_form').submit();
					show_msg('登录成功咯！  正在为您跳转...','/');	
				}
			});
		});