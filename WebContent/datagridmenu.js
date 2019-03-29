//创建命名空间
namespace.ns('easyShopping.js.datagridmenujs');
var user_id = location.href.split("?")[1].split("&")[1].split("=")[1];
/*方法定义*/
easyShopping.js.datagridmenujs = {
    init:function(){
		$("#textbname").textbox({});
		$('#dg').datagrid({
			border:false,
			toolbar:'#tb',
			striped:true,            //是否显示斑马线效果。
			pagination:true,        //如果为true，则在DataGrid控件底部显示分页工具栏。
			resizeHandle:'both',    //调整列的位置，可用的值有：'left','right','both'。在使用'right'的时候用户可以通过拖动右侧边缘的列标题调整列
			rownumbers:true,       //如果为true，则显示一个行号列。
			fit:true,
		//	title:'菜品列表',         //标题
			pageList:[5,10,15,20], //多少个分一页
			pageSize:10,               //分页下拉框默认选择
			pageNumber:1,          //分页时候默认显示的页数
			method:'get',
			url:'/MyShopping/ClientMenuList',
			idField:'name',
			//ctrl键复选
			ctrlSelect:true,
			frozenColumns:[[
				{title:'勾选',field:'',align:'center',checkbox:true},
				{title:'菜名',field:'name',width:'30%',align:'center',sortable:true}
			]],
			columns:[[
				{title:'价格',field:'price',width:'20%',halign:'center',align:'center',sortable:true},
				{title:'优惠金额',field:'money',width:'20%',halign:'center',align:'center',sortable:true},
				{title:'销量',field:'volume',width:'20%',halign:'center',align:'center',sortable:true,styler:
					 function(value,row,index){
						if (value >= 2){
							return 'background-color:yellow;color:red;';
						}	
					}
				},
				{title:'购买数量',field:'buyCount',width:'9%',halign:'center',align:'center',editor:'numberbox',formatter:function(value,row,index){
					return "<span class='buyCountSpan' id=numberspinner"+row.id+"></span>"
				}}
			]],
			queryParams:{},
			onLoadSuccess:function(){
				$(".buyCountSpan").numberspinner({
					width:120,
					min: 0,
					max: 100,
					editable: true
				});  
			}
		});
		
		
		//刷新按钮
		$("#refresh").click(function(){
			//$('#dg').datagrid('clearSelections');
			$('#dg').datagrid('load',{});
		})
		//加入购物车
		$("#addToShopCar").click(function(){
				var rows = $("#dg").datagrid('getSelections');
				if(rows.length==0){
					$.messager.alert('注意','请至少先选中一行'); 
					return;
				}
				var shopcars = [];
				for (var i = 0; i < rows.length; i++) {
					var row = {};
					row.menu_id = rows[i].id;
					row.user_id = user_id;
					row.menu_name =  rows[i].name;
					row.menu_price =  rows[i].price;
					row.menu_money = rows[i].money;
					row.count = $("#numberspinner"+rows[i].id).numberspinner("getValue");
					if(row.count==0 || row.count==""){
						$.messager.alert('注意','请选中正确的行！且数量不能为0'); 
						return ;
					}
					shopcars.push(row);
				}
				var p = {
					obj : JSON.stringify(shopcars)
				}
				$.messager.confirm('确认','加入到购物车吗？',function(r){
					if (r){
						$.ajax({
							type: "POST",
							dataType:'text',
							url: "/MyShopping/AddMyshopCar",
							data:p,
							async:false,//true:异步    false:同步
							success: function(msg){
								$.messager.alert('提醒','添加成功','info',function(){
									//刷新本页面
									$("#refresh").click();
									//如果购物车面板存在 刷新购物车
									refreshShopcar();
								}); 
							}
						});
					}    
				}); 
			})
		
			
		},
	}
	//用户搜索按钮
	function usersearchmenu(){
		var name = $("#textbname").textbox('getValue');
		$('#dg').datagrid('load',{name:name});
	}
	//如果购物车面板存在 刷新购物车
	function refreshShopcar(){
		if($('#tabs').tabs('exists', '购物车')){
			$('#tabs').tabs('getTab','购物车').panel('refresh');
		}
	}