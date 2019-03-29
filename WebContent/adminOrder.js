//创建命名空间
namespace.ns('easyShopping.js.adminorderjs');
/*方法定义*/
easyShopping.js.adminorderjs = {
    init:function(){
		$("#adminOrder").datagrid({
			// title:"订单列表", 
			toolbar:"#admintb",
			pagination:true,	    //如果为true，则在DataGrid控件底部显示分页工具栏。
			pageList:[5,10,15,20], //多少个分一页
			pageSize:20,			   //分页下拉框默认选择
			pageNumber:1,          //分页时候默认显示的页数
			fit:true,
			striped:true,		    //是否显示斑马线效果。
			rownumbers:true,       //如果为true，则显示一个行号列。
			//ctrl键复选
			ctrlSelect:true, 
			//列排序(定义哪些列 可以 进行排序,设置此属性后会向后台发送Sort:totalMoney  order:asc。    与sortable不同。 sortable列属性表示此列排序)
			//sortName:"totalMoney",
			url:"/MyShopping/DatagridPaginationQueryOrder",
			queryParams:{
				userId:1,
				state:-1
			},
			columns:[
				[
					{title:"订单号",field:"id",width:'5%',align:"center"},
					{title:'勾选',field:'s',align:'center',checkbox:true},
					//{title:"订单用户",width:'13%',field:"userName",align:"center"},
					{title:"用户",field:"userName",width:'5%',align:"center"},
					{title:"收货地址",width:'13%',field:"address",align:"center"},
					{title:"手机号码",width:'10%',field:"userPhone",align:"center"},
					{title:"总金额",width:'10%',field:"totalMoney",align:"center",sortable:true},
					{title:"状态",width:'10%',field:"state",align:"center",formatter:function(value,row,index){
						if(row.state==1){
							return "<b>待接单</b>"
						}else if(row.state==2){
							return "<b>待生产</b>"
						}else if(row.state==3){
							return "<b>待收货</b>"
						}else if(row.state==4){
							return "<b>完成</b>"
						}else if(row.state==5){
							return "<span style='color:#ccc'>已取消</span>"
						}
					},styler:function(value,row,index){
						if(row.state==1){
							return "background-color:#ffee00"
						}
					}},
					//editor：字符串，该编辑类型可以使用的类型有：text,textarea,checkbox,numberbox,validatebox,datebox,combobox,combotree。
					{title:"下单时间",width:'20%',field:"create_time",align:"center",editor:"datetimebox"},
					{title:"取消原因",width:'14%',field:"remark",align:"center",editor:"text",styler:function(index,row){
						if(row.state==5){
							return 'color:#ccc';
						}
					}},
					{title:'操作',field:'del',width:'10%',align:"center",formatter:function(value,row,index){
						if(row.state==1 ){
							return 	'<div><a href="javascript:void(0)" onclick="easyShopping.js.adminorderjs.getOrder()" id='+row.id+' class="c-b" style="text-decoration:none">接单</a></div>' +
							'<a href="javascript:void(0)" onclick="easyShopping.js.adminorderjs.cancelOrder1()" id='+row.id+' class="c-b" style="text-decoration:none">取消订单</a>';
						}else if(row.state==2){
							return '<a href="javascript:void(0)" onclick="easyShopping.js.adminorderjs.produceOrder()" id='+row.id+' style="text-decoration:none">生产</a>';
						}
						
					}}
				]
			], 
			view: detailview, 
			detailFormatter: function(rowIndex, rowData){
				//menus是数组形式   "menus":[{'name':"大菜",'count':"10",'price':'30','money':'5'},{'name':"大菜",'count':"10",'price':'30','money':'5'}]
				var htmlstr = "";
				var totalprice = 0;
				var totalmoney=0;
				for (var i = 0; i < rowData.menus.length; i++) {
					var menuinfo = rowData.menus[i];
					htmlstr += '<div ><span style="color:#0081C2">'+menuinfo.name+'<span style="margin-left:15px">(单价:'+menuinfo.price+" 优惠:"+menuinfo.money+')</span>: *</span><span style="color:red">' + menuinfo.count + '</span><span style="margin-left:20%">￥:'+(menuinfo.price-menuinfo.money)*menuinfo.count+"("+menuinfo.price*menuinfo.count+"-"+menuinfo.money*menuinfo.count+')</span></div>';
					if(i<rowData.menus.length-1){
						htmlstr +='<hr>';
					}
					totalprice +=menuinfo.price*menuinfo.count;
					totalmoney +=menuinfo.money*menuinfo.count;
				}
				
				return '<table style="width:100%"><tr>' + 
				'<td rowspan='+rowData.menuCount+' style="border:0;width:10%"><img src="static/菜品图.png" style="width:100px;height:100px"></td>' + 
				'<td style="border:0">' + htmlstr +'</td>' + 
				'<td rowspan=2 ></td>' + 
				'<td rowspan='+rowData.menuCount+'  style="color:chocolate"><p>总金额：￥<span>'+totalprice+'</span></p><p>应收：￥'+(totalprice-totalmoney)+'(-'+totalmoney+')</p></td>' + 
				'</tr></table>'; 
		    },
		});
		$('#adminOrderWin').window({
			width:600,
			height:400,
			modal:true,
			closed:true,
			title:'login',
			href:'cancelOrderhtml.html',
			onBeforeOpen:function(){
				$("#cancelResoninput").textbox('setText',"");
			}
		});
		$("#cc").combobox({
			valueField:'id', 
			textField:'name',
			editable:false,
			value:"请选择用户组",
			url:'queryAllUser',
			onSelect:function(record){
				$("#adminOrder").datagrid('load',{userId:record.id,state:-1});
			}  
		})
	
	},
	getOrder:function(){
		var row =$("#adminOrder").datagrid('getSelected');
		if(!row){
			$.messager.alert('提示','请先选择一行','info');  
			return;
		}
		if(row.state!=1){
			$.messager.alert('提示','只有待接单订单才能接单','info');
			return;
		}
		var p = {id:row.id,state:row.state};
		$.ajax({
			type:'post',
			data:p,
			url:'ReceiveAndProductionOrder',
			async:true,
			dataType:'json',
			success:function(msg){
				$.messager.alert('提示','成功','info',function(){
					$("#adminOrder").datagrid('reload');
				});  
			}
		})
	},
	cancelOrder1:function(){
		var row =$("#adminOrder").datagrid('getSelected')
		if(!row){
			$.messager.alert('提示','请先选择一行','info');  
			return;
		}
		if(row.state!=1){
			$.messager.alert('提示','只有待接单订单才能取消订单','info');
			return;
		}
		$('#adminOrderWin').window('open');
	},
	produceOrder:function(){
		var row =$("#adminOrder").datagrid('getSelected');
		if(!row){
			$.messager.alert('提示','请先选择一行','info');  
			return;
		}
		if(row.state!=2){
			$.messager.alert('提示','只有待生产订单才能生产订单','info');
			return;
		}
		var p = {id:row.id,state:row.state};
		$.ajax({
			type:'post',
			data:p,
			url:'ReceiveAndProductionOrder',
			async:true,
			dataType:'json',
			success:function(msg){
				$.messager.alert('提示','成功','info',function(){
					$("#adminOrder").datagrid('reload');
				});  
			}
		})
	},
}
//接单
function admingetOrder(){
	easyShopping.js.adminorderjs.getOrder()
}
//生产订单
function adminProduceOrder(){
	easyShopping.js.adminorderjs.produceOrder()
}
//取消订单
function admincancelOrder(){
	easyShopping.js.adminorderjs.cancelOrder1();
}
//刷新页面
function adminOrderrefresh(){
	$("#adminOrder").datagrid('reload');
}
//打印
function adminprint(){
	$('#p').css('display','block');
	var value = $('#p').progressbar('getValue'); 
	if (value < 100){
		value += Math.floor(Math.random() * 10); 
		$('#p').progressbar('setValue', value); 
		setTimeout(arguments.callee, 200);
	}else{
		$.messager.alert('提示','打印完毕！','info',function(){
			$('#p').progressbar('setValue', 0); 
			$('#p').css('display','none');
		});  
	}
	
}
//查询订单
function adminOrdersearch(){
	var content = $("#orderText").textbox("getValue");
	debugger;
}
//多个按钮点击查询订单，不同状态
function datagridload(state){
	$("#adminOrder").datagrid('load',{userId:1,state:state});
}