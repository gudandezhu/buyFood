var userId = location.href.split("?")[1].split("&")[1].split("=")[1];
//创建命名空间
namespace.ns('easyShopping.js.userOrderjs');
/*方法定义*/
easyShopping.js.userOrderjs = {
    init:function(){
		
		
		
		$("#userOrder").datagrid({
			// title:"订单列表", 
			toolbar:"#userOrdertb",
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
				userId:userId,
				state:-1
			},
			columns:[
				[
					{title:"订单号",field:"id",width:'5%',align:"center"},
					{title:'勾选',field:'s',align:'center',checkbox:true},
					//{title:"订单用户",width:'13%',field:"userName",align:"center"},
					{title:"收货地址",width:'15%',field:"address",align:"center"},
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
							return '<a href="javascript:void(0)" onclick="easyShopping.js.userOrderjs.cancelOrder1()" id='+row.id+'  class="c-b"  style="text-decoration:none">取消订单</a>';
						}else if(row.state==3){
							return '<a href="javascript:void(0)" onclick="easyShopping.js.userOrderjs.acceptFoodOrder()" id='+row.id+'  class="c-b"  style="text-decoration:none">收货</a>';
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
		    } 
		});
		//收货
		$("#useracceptFood").click(function(){
			easyShopping.js.userOrderjs.acceptFoodOrder();
		});
		//取消订单
		$("#usercancelOrder").on('click',function(){
			easyShopping.js.userOrderjs.cancelOrder1();
		});
		//刷新
		$("#userOrderrefresh").click(function(){
			$("#userOrder").datagrid('reload');
		})
		//取消订单窗口
		$('#userOrderWin').window({
			width:600,    
			height:400,    
			modal:true,
			closed:true,
			title:'login',
			href:'canceluserOrderhtml.html',
			onBeforeOpen:function(){
				$("#cancelResoninput").textbox('setText',"");
			}
		});
	},
	cancelOrder1:function(){
		var row =$("#userOrder").datagrid('getSelected')
		if(!row){
			$.messager.alert('提示','请先选择一行','info');  
			return;
		}
		if(row.state!=1){
			$.messager.alert('提示','只有待接单订单才能取消订单','info');
			return;
		}
		$('#userOrderWin').window('open');
	},
	acceptFoodOrder:function(){
		var row = $("#userOrder").datagrid('getSelected');
		if(!row){
			$.messager.alert('提示','请先选择一行','info');
			return;
		}
		if(row.state!=3){
			$.messager.alert('提示','只有待收货订单才能确认收货','info');
			return;
		}
		$.messager.confirm('确认','您确认收货吗 ？',function(r){
			if (!r){
				return;
			}
			var p = {id:row.id,state:3};
			$.ajax({
				type:'get',
				data:p,
				url:'ReceiveAndProductionOrder',
				async:true,
				dataType:'json',
				success:function(msg){
					$.messager.alert('提示','收货成功！','info',function(){
						$("#userOrder").datagrid('reload');
					});
				}
			})
		}); 	
	}
}

function userdatagridload(state){
	$("#userOrder").datagrid('load',{userId:userId,state:state});
}