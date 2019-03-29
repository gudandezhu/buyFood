var userId = location.href.split("?")[1].split("&")[1].split("=")[1];
//创建命名空间
namespace.ns('easyShopping.js.shopcarjs');
/*方法定义*/
easyShopping.js.shopcarjs = {
    init:function(){
		$('#shopcar').datagrid({
			border:false,
			toolbar:'#cartb',
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
			url:'/MyShopping/QueryShopCarInfo?userId='+userId,
			idField:'id',
			//ctrl键复选
			ctrlSelect:true,
			frozenColumns:[[
				{title:'勾选',field:'',align:'center',checkbox:true},
				{title:'购物车编码',field:'id',width:'10%',align:'center'},
				{title:'菜品名',field:'menu_name',width:'30%',align:'center'}
			]],
			columns:[[
				{title:'价格',field:'menu_price',width:'15%',halign:'center',align:'center',formatter:function(value,row,index){
					return '<span>'+row.count*value+'</span>';
				}},
				{title:'优惠金额',field:'menu_money',width:'15%',halign:'center',align:'center',sortable:true,formatter:function(value,row,index){
					return '<span>'+row.count*value+'</span>';
				}},
				{title:'实际金额',field:'totolprice',width:'20%',halign:'center',align:'center',sortable:true,formatter:function(value,row,index){
					return '<span style="color:red">'+row.count*(row.menu_price-row.menu_money)+'</span>';
				}},
				{title:'菜品数量',field:'count',width:'9%',halign:'center',align:'center',editor:'numberbox',sortable:true}
			]],
			onClickCell:function(rowIndex,field,value){
				if(field=='count'){
					$('#shopcar').datagrid('beginEdit',rowIndex);
				}else{
					$('#shopcar').datagrid('endEdit',rowIndex);
				}
			}
		});
	
		//刷新按钮
		$("#shopcarrefresh").click(function(){
			//$('#shopcar').datagrid('clearSelections')
			$('#shopcar').datagrid('load',{});
		})
		//下单按钮
		$("#createOrder").click(function(){
			var rows = $("#shopcar").datagrid('getSelections');
			if(rows.length==0){
				$.messager.alert('提示','请先选择一行或多行','info');  
				return;
			}
			$("#cartb").append('<div id="createOrderWin"></div>');
			$("#createOrderWin").window({
				width:600,
				height:400,
				modal:true,
				title:'扫码支付',
				href:'createOderWin.html'
			})
		})
	},
}
function deleteShopCarInfo(){
	var rows = $("#shopcar").datagrid("getSelections");
	if(rows.length==0){
		$.messager.alert('注意','请先选中一行'); 
		return;
	}
	$.messager.confirm('确认','您确认想要删除记录吗？',function(r){
		if (r){
			var idlist = [];
			$.messager.progress();
			for (var i = 0; i < rows.length; i++) {
				$.ajax({
					type:'get',
					url:'DeleteCar?carId='+rows[i].id,
					async:true
				})
			}
			$.messager.alert('提示','删除成功!','info',function(){
				$.messager.progress("close");
				$("#shopcarrefresh").click();
			});
		}
	});  
}