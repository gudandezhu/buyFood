	function obj() {
		var p = {state:0}   //0查看全部   1:待下单，2：待生产，3：待收货，4：完成，5：已取消 默认：1
		$.ajax({
	 	   type: "POST",
	 	   dataType:'json',
	 	   url: "/MyShopping/QueryOrderInfoServlet",   
	 	   data:p,
	 	   async:false,//true:异步    false:同步
	 	   success: function(msg){
	 		   if (!msg.error) {
	 			  if (msg.body.length != 0) {
	 				  var codes = getOrdersCodesByOrder(msg.body);
	 				  $("#orderList").html(codes);
					  bindSearchBtn();
	 			  }else{
	 				 $("#orderList").html("未查询到任何订单信息");
	 			  }
	 		   }
	 	   }
	 	});
	}
//	initOrderInfoList();
	
	/**
	 * 搜索
	 */
	function bindSearchBtn(){
		$("#searchBtn").on('click',function(){
			var state = $("input[name=state]:checked").val();
			var p = {state:state}; //0查看全部   1:待下单，2：待生产，3：待收货，4：完成，5：已取消 默认：1
			$.ajax({
			   type: "POST",
			   dataType:'json',
			   url: "/MyShopping/QueryOrderInfoServlet",
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
		})
	}
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
		
		codes.push('<div id="order_'+order.id+'" orderId="'+order.id+'">');
			codes.push('<table>');
				codes.push('<tr class="head">');
					codes.push('<th colspan="4" class="text-a-l">');
						codes.push('<span class="mr30 floatl">订单号：'+order.id+'</span>');
						codes.push('<span class="mr30 floatl">下单时间：'+order.create_time+'</span>');
						if(order.state==5){
							codes.push('<span class="floatl">状态：<span  style="color:red">'+order.stateName+'</span></span>');
						}else{
							codes.push('<span class="floatl">状态：'+order.stateName+'</span>');
						}
						if(order.state==1 || order.state==2){
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
						if (order.state == 1) {
							codes.push( '<span class="span-a ireceive"  onclick="ireceive('+order.id+',1)">接单</span>');
						} else if (order.state == 2) {
							codes.push('<span class="span-a ireceive"   onclick="ireceive('+order.id+',2)">生产</span>');
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
	
	
	
	
	//取消按钮
	$("#cancleBtn").click(function(){
		$("#cancelOrderDiv").fadeOut(500);
	});
		
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
	

//接单和生产按钮
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
			obj();
		}
	});
}
/**
 * 取消订单按钮
 */
function cancelOrder (id) {
	$("#cancelOrderDiv").fadeIn(500);
	$("#orderId").val(id);
}

$("#submitCansel").click(function(){
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
			//ireceive
			obj();
			$("#cancelOrderDiv").fadeOut(500);
		}
	});
});