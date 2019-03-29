	$(function(){
		admininit();
	})
	
	//init操作
	function admininit(){
		$('#tabs').tabs({
			border:false
		});
		$('#tabs').tabs('add',{
			title:'首页',    
			content:'首页',
			href:'firstPage.html'
		}); 
	}
	
	
	
	//显示菜品列表
	function menulist(){
		//展开菜品面板
		if(!$('#tabs').tabs('exists', '菜品')){
			//重新添加新选项面板
			reAddTabs();
		}else{
			$('#tabs').tabs('select', '菜品');//启动选项板
		}
	}
		//展开订单列表面板
	function orderList(){
		if(!$('#tabs').tabs('exists', '订单列表')){
			//重新添加新选项面板
			reAddTabsOrder();
		}else{
			$('#tabs').tabs('select', '订单列表');//启动选项板
		}
	}
		//展开订单列表（datagrid）面板
	function datagridOrderList(){
		if(!$('#tabs').tabs('exists', '订单列表(data-grid)')){
			$('#tabs').tabs('add',{
				title:'订单列表(data-grid)',
				closable:true,
				tabWidth:200,
				selected:true,
				href:'adminOrder.html',
				onLoad:function(panel){
					easyShopping.js.adminorderjs.init();
				}
			}); 
		}else{
			$('#tabs').tabs('select', '订单列表(data-grid)');//启动选项板
		}
	}
	//增加菜品
	function addMenuMethod(){
		if(!$('#tabs').tabs('exists', '增加菜品')){
			$('#tabs').tabs('add',{
				title:'增加菜品',
				closable:true,
				tabWidth:100,
				selected:true,
				href:'add.html',
				onLoad:function(panel){
					$("#addname").textbox("setValue","");
					$("#addprice").textbox("setValue","");
					$("#addmoney").textbox("setValue",""); 
				}
			}); 
		}else{
			$('#tabs').tabs('select', '增加菜品');//启动选项板
		}
	}
	
	function reAddTabs(){
		// add a new tab panel   
		$('#tabs').tabs('add',{
			title:'菜品',
			closable:true,  
			tabWidth:100,
			href:'data-grid2.html',
			selected:true,
			onLoad:function(panel){
				init();
			}
		}); 
	}	
	function reAddTabsOrder(){
		$('#tabs').tabs('add',{
			title:'订单列表',
			closable:true,
			tabWidth:100,
			selected:true,
			href:'order.html',
			onLoad:function(panel){
				obj();
			}
		}); 
	}
	//安全退出
	function backAdmin(){
		location.href="index.html";
	}
	
	