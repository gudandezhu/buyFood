$(function(){
		//登录界面窗口
		$("#win").window({
			width:600,
			height:400,
			collapsible:false,
			minimizable:false,
			maximizable:false,
			closable:false,
			closed:false,
			title:'login'
		})
		
		$("#login").click(function(){
			if(!$('#name').textbox('isValid') || !$('#password').textbox('isValid')){
				$.messager.alert('提示','请详细填写')
				return;
			}
			var name = $("#name").textbox("getText");
			var password = $("#password").textbox("getText")
			var p = {
				name,password
			};
			var opt = {
				url:"/MyShopping//UserRegister",
				type:"POST",//GET  POST,
				dataType:"json",//指定服务器返回的格式
				data:p,
				async:true,//true:异步    false:同步
				success:function (req){//请求成功执行的函数
					if(!req.error){
						$.messager.alert('提示',req.msg,'info',function(){
							return;
						});
						return;
					}
					//登录成功，返回type
					if(req.stic==1){
						//管理员页面
						$.messager.alert('提示',"登录成功，跳转管理员页面",'info',function(){
							location.href="admin.html";
						});  
						
					}else{
						//用户页面
						$.messager.alert('提示',"登录成功，跳转用户页面",'info',function(){
							location.href="user.html?name="+name+"&id="+Number(req.body);
						});  
						
					}
				}
			};
			$.ajax(opt);
		});

		$("#register").click(function(){
			location.href="register.html";
		});
	});