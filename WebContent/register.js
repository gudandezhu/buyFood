$(function(){
	$.extend($.fn.validatebox.defaults.rules, {
		minLength: {
			validator: function(value, param){    
				return value.length >= param[0];    
			},    
			message: 'Please enter at least {0} characters.'   
		},
		equals: {
			validator: function(value,param){
				return value == $(param[0]).textbox('getValue');    
			},    
        message: '两次密码必须一样'
		}   
	}); 
	$("#ff").form({
		novalidate:false
	});
	//$("#ff").submit();
	
})


function back(){
	location.href="index.html";
}
function submitForm(){
	var name = $("input[name=name]").val();
	var password = $("input[name=password]").val();
	var addess = $("input[name=addess]").val();
	var phone = $("input[name=phone]").val();
	
	var p = {
		name,password,addess,phone
	}
	var obj = JSON.stringify(p);
	
	$.messager.progress();	// 显示进度条

	$("#ff").form('submit',{
		url:"/MyShopping/Register",
		onSubmit: function(param){                 //在提交之前触发，返回false可以终止提交。
			param.obj = obj;
			var isValid = $(this).form('validate');
			if (!isValid){
				$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
			}
			return isValid;	// 返回false终止表单提交

		},    
		success:function(data){
			data = JSON.parse(data);
			$.messager.progress('close');	// 如果提交成功则隐藏进度条
			alert(data.msg);
			if(!data.error){
				//跳转页面
				location.href="index.html";
			}
		}
	});
}