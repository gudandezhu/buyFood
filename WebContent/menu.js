function query(){
		//显示菜品列表
		var opt = {
			url:"/MyShopping/MenuList",
			type:"POST",//GET  POST,
			dataType:"json",//指定服务器返回的格式
			 // data:p,
			async:true,//true:异步    false:同步
			success:function (req){//请求成功执行的函数
				if(req.body.length!=0){
					var codes = getMenusCodeByMenus(req.body);
 				  	$("#menus").html(codes);
				}
			}
		};
		$.ajax(opt);
	};
	query();
	
	function getMenusCodeByMenus(menus){
		var codes = [];
		for (var i = 0; i < menus.length; i++) {
			var code = getMenuCodeByMenu(menus[i]);
			codes.push(code);
		}
		return codes.join("");

	};


	/**
	 * 拼接单个菜单html代码
	 */
	function getMenuCodeByMenu(menu) {
		var codes = [];
		var cred="";
		if (menu.state == 3 || menu.state == 2) {
			cred = "cred";
		}
		codes.push('<div class="menu-div">');
			codes.push('<p class="name" name="name">'+menu.name);
			codes.push('<div>');
				codes.push('价格：￥<span name="money">'+menu.price+'</span>');
				codes.push('<span class="fr" name="preferentialPrice">优惠金额：￥<span name="price">'+menu.money+'</span></span>');
			codes.push('</div>');
			codes.push('<div>');
				codes.push('是否置顶：<span name="isTop">'+menu.is_top+'</span>');
				codes.push('<span name="state" class="fr '+cred+'" id="state_'+menu.id+'">状态：<span>'+menu.stateName+'</span></span>');
			codes.push('</div>');
			codes.push('<div>');
				codes.push('上架时间：'+menu.putaway_time);
			codes.push('</div>');
			codes.push('<div class="option">');
				if (menu.state != 3) {
					codes.push('<span onclick="menuDeleteOrDown('+menu.id+',this,\'delete\')">删除</span>');
				}
				if (menu.state != 2) {
					codes.push('<span onclick="menuDeleteOrDown('+menu.id+',this,\'down\')">下架</span>');
				}
				codes.push('<span onclick="updateMenuInfo('+menu.id+',this)">修改</span>');
			codes.push('</div>');
		codes.push('</div>');
		
		return codes.join("");
		
	};
	
	/* 删除或者下架操作 */
	 function menuDeleteOrDown(menuid,t,type){
		$(t).remove();
		var p = {id:menuid,m:1};
		if(type=="delete"){
			//状态改为删除，颜色标红，取消删除按钮
			 $("#state_"+menuid).addClass("cred");
			 $("#state_"+menuid+" span").html("删除");
			 p.m=3;
			console.log("删除成功");			
		}else if(type=="down"){
			//状态改为下架，取消下架按钮
			$("#state_"+menuid).addClass("cred");
			$("#state_"+menuid+" span").html("下架");
			p.m=2;
			console.log("下架成功");
		}
		
		
		var opt = {
			url:"/MyShopping/MenuDeleteOrDown",
			type:"POST",//GET  POST,
			dataType:"json",//指定服务器返回的格式
			data:p,
			async:false,//true:异步    false:同步
			success:function (req){//请求成功执行的函数
				query();
				console.log("...");
			}
		};
		$.ajax(opt);
		
		
	}; 

	
	
	//修改菜品信息

	function updateMenuInfo (menuId,t) {
		$("#updateMenu").fadeIn(1000);
		$("#menuId").val(menuId);
		var $div = $(t).parent().parent("div");
		
		
		$("#name").val($div.find("p[name=name]").text());
		$("#money").val($div.find("span[name=money]").text());
		$("#preferentialPrice").val($div.find("span[name=price]").text());
		$("input[name=isTop][value="+$div.find("span[name=isTop]").text()+"]").attr("checked",true);
		if($div.find("span[name=state]").text()=="状态：删除"){
			$("#state").val(3);
		}else if($div.find("span[name=state]").text()=="状态：下架"){
			$("#state").val(2);
		}else{
			$("#state").val(1);
		}
	}
	
	//修改页面修改和取消按钮
	$("#cancleBtn").click(function () {
		$("#updateMenu").fadeOut(500);
	});
	
	$("#updateSubmit").click(function () {
		
		var name = $("#name").val();
		var price = $("#money").val();
		var money = $("#preferentialPrice").val();
		var is_top = $("input[name=isTop]:checked").val();
		var state = $("#state").val();
		//获取隐藏域的ID
		var id= $("#menuId").val();
		
		var obj = {
				id,name,price,money,is_top,state
		}
		var p = {
				obj:JSON.stringify(obj)
		}
		$.ajax({
    	   type: "POST",
    	   dataType:'text',
    	   url: "/MyShopping/UpdateMenuInfoById",
    	   data: p,
    	   async:false,//true:异步    false:同步
    	   success: function(msg){
    		   
   			  console.log("修改成功！");
   			   //更新页面的数据
   			   query();
   			   
   			   //$("#updateMenu").hide();
   			   $("#cancleBtn").click();
    		   
    	   }
    	});
	});
	