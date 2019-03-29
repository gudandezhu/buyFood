		//页面提示名字插入到wellcome后
		$(".wellcome").before(decodeURIComponent(location.href.split("?")[1].split("&")[0].split("=")[1]));
		var userId = location.href.split("?")[1].split("&")[1].split("=")[1];
		//菜品(方块版)
		function initUI(){
			//展开菜品方块版面板
			if(!$('#tabs').tabs('exists', '菜品')){
				//重新添加新选项面板
				reAddTabs();
			}else{
				$('#tabs').tabs('select', '菜品');//启动选项板
			}
			//显示所有菜品信息
			$.ajax({
				type: "POST",
				dataType:'json',
				url: "/MyShopping/MenuList",
				//data:p,
				async:false,//true:异步    false:同步
				success: function(msg){
					if(!msg.error){
						var codes = getMenusCodesByMenu(msg.body);
						if(codes!=""){
							$(".menus").html(codes);
						}else{
							$(".menus").html("暂无更多菜品信息，请继续关注······");
						}
					}
					
				}
			});
		}
		//菜品(表格版)
		function datagridinitUI(){
			//展开操作面板
			if(!$('#tabs').tabs('exists', '菜品(data-grid)')){
				//重新添加新选项面板
				$('#tabs').tabs('add',{
					title:'菜品(data-grid)',
					closable:true,
					tabWidth:150,
					selected:true,
					href:'datagridmenu.html',
					onLoad:function(){
						easyShopping.js.datagridmenujs.init();
					}
				}); 
			}else{
				$('#tabs').tabs('select', '菜品(data-grid)');//启动选项板
			}
		}
		//我的订单第一版按钮
		function myOrder(){
			if(!$('#tabs').tabs('exists', '订单列表')){
				//重新添加新选项面板
				reAddTabsOrder();
			}else{
				$('#tabs').tabs('select', '订单列表');//启动选项板
				init() ;
			}
		}
		//我的订单表格版按钮
		function userOrder(){
			if(!$('#tabs').tabs('exists', '订单列表(表格)')){
				//重新添加新选项面板
				$('#tabs').tabs('add',{
					title:'订单列表(表格)',
					closable:true,
					tabWidth:150,
					selected:true,
					href:'userOrder.html',
					onLoad:function(){
						easyShopping.js.userOrderjs.init();
					}
				}); 
			}else{
				$('#tabs').tabs('select', '订单列表(表格)');//启动选项板
			}
		}
		//购物车按钮
		function shopInit(){
				//展开购物车面板
			if(!$('#tabs').tabs('exists', '购物车')){
				//重新添加新选项面板
				$('#tabs').tabs('add',{
					title:'购物车',
					closable:true,
					tabWidth:120,
					selected:true,
					href:'shopcar.html',
					onLoad:function(){
						easyShopping.js.shopcarjs.init();
					}
				}); 
			}else{
				$('#tabs').tabs('select', '购物车');//启动选项板
			}
		}
		//个人信息
		function userInfo(){
			if(!$('#tabs').tabs('exists', '个人信息')){
				//重新添加新选项面板
				$('#tabs').tabs('add',{
					title:'个人信息',
					closable:true,
					tabWidth:120,
					selected:true,
					href:'myInfo.html',
					onLoad:function(){
						easyShopping.js.myInfojs.init(userId);
					}
				}); 
			}else{
				$('#tabs').tabs('select', '个人信息');//启动选项板
			}
		}
		//删除购物车信息按钮
		function deleteCar(carId,t){
			if(confirm("确定要删除此信息？")){
				$(t).parent().parent().parent().parent().remove();
				var p = {carId}; 
				$.ajax({
					type: "POST",
					dataType:'text',
					url: "/MyShopping/DeleteCar",
					data:p,
					async:false,//true:异步    false:同步
					success: function(msg){
						myShoppingCar();
					}
				});
			}
		}
		
	
	function getMenusCodesByMenu( menus ){
		var codes=[];
		codes.push('<div style="text-align:center;font-size:20px;"><p>菜品信息</p></div>');
		
		for(var i =0 ; i < menus.length ; i++){
			//每一个菜的html代码
			var menu = menus[i];
			if(menu.state!=1){
				continue;
			}
			codes.push('<div class="menuDiv" style="text-align: center;width: 320px; height: 185px; float: left;" id='+menu.id+'>');
				codes.push('<input type="hidden" id = "sort"  value="'+menu.id+'"/>')
				codes.push('<p id = "name" >'+menu.name+'</p>');
				codes.push('<div class="p_price"><p>价格：<span id="price">'+menu.price+'</span></p><p style="color:red">折扣：<span id="money">'+menu.money+'</span></p></div>');
				codes.push('<p class="p_volume" id="volume">销量：'+menu.volume+'</p>');
				codes.push('<p>');
					codes.push('<button onclick="lessen('+menu.id+')">-</button>');
					codes.push('<input type="text" name="count" class="count"  id="count_'+menu.id+'" value="0" style="width:20px;text-align:center"/>');
					codes.push('<button onclick="add1('+menu.id+')">+</button>');
				codes.push('</p>');
			codes.push('</div>');
		}
		
		return codes.join("");
	}
	function reAddTabs(){
		$('#tabs').tabs('add',{
			title:'菜品',    
			content:'<div class="menus" id="menuDetail"></div>',    
			closable:true,  
			tabWidth:100,
			selected:true,
			onSelect:function(title,index){}
		}); 
	}
	function reAddTabsOrder(){
		$('#tabs').tabs('add',{
			title:'订单列表',
			closable:true,
			tabWidth:100,
			selected:true,
			href:'myOrder.html',
			tools:[{
				iconCls:'icon-mini-refresh',    
				handler:function(){
					myOrder();
				}
			}],
			onLoad:function(panel){
				init();
			}
		}); 
	}
	
	function init() {
		var userId = location.href.split("?")[1].split("&")[1].split("=")[1];
		var p = {state:0,userId:userId}   //0查看全部   1:待下单，2：待生产，3：待收货，4：完成，5：已取消 默认：1
		$.ajax({
	 	   type: "POST",
	 	   dataType:'json',
	 	   url: "/MyShopping/QueryOrderInfoByUserIdServlet",   
	 	   data:p,
	 	   async:false,//true:异步    false:同步
	 	   success: function(msg){
	 		   if (!msg.error) {
	 			  msg.body = msg.body || [];
	 			  if (msg.body.length != 0) {
	 				  var codes = getOrdersCodesByOrder(msg.body);
	 				  $("#orderList").html(codes);
	 			  }else{
	 				 $("#orderList").html("未查询到任何订单信息");
	 			  }
	 		   }
	 	   }
	 	});
	}
	
	$("#searchBtn").click(function () {
		var state = $("input[name=state]:checked").val();
		var userId = location.href.split("?")[1].split("&")[1].split("=")[1];
		var p = {state,userId}; //0查看全部   1:待下单，2：待生产，3：待收货，4：完成，5：已取消 默认：1
		$.ajax({
	 	   type: "POST",
	 	   dataType:'json',
	 	   url: "/MyShopping/QueryOrderInfoByUserIdServlet",
	 	   data:p,
	 	  async:false,//true:异步    false:同步
	 	   success: function(msg){
	 		   if (!msg.error) {
	 			  msg.body = msg.body || [];
	 			  if (msg.body.length != 0) {
	 				  var codes = getOrdersCodesByOrder(msg.body);
	 				  $("#orderList").html(codes);
	 			  }  else {
	  				 $("#orderList").html("未查询到任何订单信息");
	 			  }
	 		   }
	 	   }
	 	});
	});
	
	/**
	 * 获取全部订单信息
	 */
	function getOrdersCodesByOrder(orders) {
		var codes = [];
		for (var i = 0; i < orders.length; i++) {
			var code = getOrderCodesByOrder(orders[i]);
			codes.push(code);
		}
		return codes.join("");
	}
	
	
	/**
	 * 获取单个订单下的食品列表展示代码
	 */
	function getOrderCodesByOrder(order) {
		
		var codes = [];
		
		var menusCode = getMenusCode(order);
		
		codes.push('<div class="orderDiv" id="order_'+order.id+'" orderId="'+order.id+'">');
			codes.push('<table>');
				codes.push('<tr class="head">');
					codes.push('<th colspan="4" class="text-a-l">');
						codes.push('<span class="mr30 floatl">订单号：'+order.id+'</span>');
						codes.push('<span class="mr30 floatl">下单时间：'+order.create_time+'</span>');
						if(order.state==5){
							codes.push('<span style="text-align:center">状态：<span  style="color:red">'+order.stateName+'</span></span>');
						}else{
							codes.push('<span style="text-align:center">状态：'+order.stateName+'</span>');
						}
						if(order.state==1){
							codes.push('<span class="fr span-a f-s-13 cancelOrder" onclick="cancelOrder('+order.id+')">取消订单</span>');
						}
					codes.push('</th>');
				codes.push('</tr>');
				
				//还得加载菜品的代码
				codes.push(menusCode);
				
			codes.push('</table>');
		codes.push('</div>');
		
		return codes.join("");
	}
	
	
	
	/**
	 * 获取订单下菜品列表展示代码
	 */
	function getMenusCode(order) {
		var codes = [];
		var menus = order.menus || [];
		var userInfo = order.userInfo;
		var totalMoney = getTotalMoney(menus);
		var totalPreferentialPrice = getTotalPreferentialPrice(menus);
		
		for (var i = 0; i < menus.length; i++) {
			var menu = menus[i];
			
			if (i == 0) {//拼接第一个菜品代码
				codes.push('<tr>');
					codes.push('<td width="40%"><span class="mr30">'+menu.name+'</span> <span>*'+menu.count+'</span></td>');
					codes.push('<td rowspan="'+menus.length+'" style="font-size: 13px;color: #737373;" width="20%">');
						codes.push('<div>总金额：'+totalMoney+'</div>');
						codes.push('<div>应收：'+(totalMoney-totalPreferentialPrice)+'(-'+totalPreferentialPrice+')</div>');
					codes.push('</td>');
					codes.push('<td rowspan="'+menus.length+'" style="font-size: 12px;color: #A8A8A8;" width="30%">');
						codes.push('<div>'+userInfo.name+'</div>');
						codes.push('<div>'+(userInfo.phone || "")+'</div>');
						codes.push('<div>'+(userInfo.addess || "")+'</div>');
					codes.push('</td>');
					codes.push('<td rowspan="'+menus.length+'"  width="10%">');
						if (order.state == 3) {
							codes.push('<span class="span-a ireceive"   onclick="ireceive('+order.id+',3,)">确认收货</span>');
						}else if(order.state==5){
							codes.push('<span style="font-size:8px">取消原因：<br>'+order.cancel_reason+'</span>');
						}else{
							codes.push('<span class="span-a "></span>');
						}
					codes.push('</td>');
				codes.push('</tr>');
			} else {
				codes.push('<tr>');
					codes.push('<td><span class="mr30">'+menu.name+'</span> <span>*'+menu.count+'</span></td>');
				codes.push('</tr>');
			}
		}
		
		return codes.join("");
		
	}
	
	/*
	 * 获取订单总金额
	 */
	function getTotalMoney(menus) {
		var totalMoney = 0;
		for (var i = 0; i < menus.length; i++) {
			var menu = menus[i];
			menu.price = menu.price || 0;
			menu.count = menu.count || 1;
			
			totalMoney += (menu.price*menu.count);
		}
		return totalMoney;
	}
	
	/*
	 * 获取订单优惠总金额
	 */
	function getTotalPreferentialPrice(menus) {
		var totalMoney = 0;
		for (var i = 0; i < menus.length; i++) {
			var menu = menus[i];
			menu.money = menu.money || 0;
			menu.count = menu.count || 1;
			
			totalMoney += (menu.money*menu.count);
		}
		return totalMoney;
	}
	
	
	//提交取消订单按钮
	$("#submitCanselOrder").on("click",function(){
		if(!confirm("确定要取消订单吗？")){
			return;
		}
		var reason = $("#cancelReason").val();
		var id = $("#orderId").val();
		var p = {reason,id};
		$.ajax({
			type: "POST",
			dataType:'text',
			url: "/MyShopping/CancelOrder",
			data:p,
			async:false,//true:异步    false:同步
			success: function(msg){
				alert("取消订单成功");
				search();
				$("#cancelOrderDiv").fadeOut(500);
			}
		});
	})
	//收货按钮
	function ireceive(id,state){
		
		var p = {id,state}; //0查看全部   1:待下单，2：待生产，3：待收货，4：完成，5：已取消 默认：1
		$.ajax({
			type: "POST",
			dataType:'json',
			url: "/MyShopping/ReceiveAndProductionOrder",
			data:p,
			async:false,//true:异步    false:同步
			success: function(msg){
				alert("成功");
				search();
			}
		});
	}
	//点击首页的图像，显示导航面板，隐藏动图
	function deleteImgAndGetUrl(){
		$("#dt2").css("display","none");
		$("#fp").css("display","block");
	}
	//首页面的四个点击按钮
	function fp1(){datagridinitUI()}
	function fp2(){shopInit()}
	function fp3(){userOrder()}
	function fp4(){userInfo()}
	