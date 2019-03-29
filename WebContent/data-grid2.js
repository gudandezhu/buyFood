function init(){
	$("#textb").textbox({
		//buttonText:'Search',    
		iconCls:'icon-search', 
		iconAlign:'right'
	});
	$('#addmenuhtml').window({
		width:600,
		height:400,
		modal:true,
		closed:true,
		href:'add.html',
		title:'增加菜品',
		onClose:function(){
			$("#addname").textbox("setValue","");
			$("#addprice").textbox("setValue","");
			$("#addmoney").textbox("setValue",""); 
		}
	});

	$("#dg").datagrid({
		border:false,
		toolbar:"#tb",
		striped:true,		    //是否显示斑马线效果。
		pagination:true,	    //如果为true，则在DataGrid控件底部显示分页工具栏。
		resizeHandle:"both",    //调整列的位置，可用的值有：'left','right','both'。在使用'right'的时候用户可以通过拖动右侧边缘的列标题调整列
		rownumbers:true,       //如果为true，则显示一个行号列。
		//title:"菜品列表",  	   //标题
		//width:'100%',			   //
		//height:'100%',  		   //
		fit:true,
		pageList:[5,10,15,20], //多少个分一页
		pageSize:10,			   //分页下拉框默认选择
		pageNumber:1,          //分页时候默认显示的页数
		method:"get",
		url:"/MyShopping/DatagridQuerySearch",
		idField:"name",
		//ctrl键复选
		ctrlSelect:true,
		frozenColumns:[[
			{title:'勾选',field:'s',align:'center',checkbox:true},
			{title:'菜名',field:'name',width:'14.5%',sortable:true,align:"center"},
			{title:"操作",field:"del",width:'15%',formatter:function(value,row,index){
				return "<a href='javascript:void(0)' onclick='deleteE("+row.id+","+index+")'>删除</a> "+
				"<a href='javascript:void(0)' onclick='updateMenu("+index+")'>修改</a> "+
				"<a href='javascript:void(0)' onclick='saveMenu("+index+")'>保存</a> "+
				"<a href='javascript:void(0)' onclick='cancelMenu("+index+")'>取消</a>"
			}}
		]],
		columns:[
			[
				{title:"状态",field:"state",width:'10%',align:"center",sortable:true,formatter:function(value,row,index){
					if(value==1){
						return "<span>上架</span>";
					}else if(value==2){
						return "<span>下架</span>";
					}else if(value==3){
						return "<span>删除</span>";
					}
				},styler:function(value,row,index){
					if(row.state==2){
						return "background-color:yellow;color:red";
					}else if(row.state==3){
						return "color:red";
					}
				}},
				//editor    可以是如下属性
						//                text,textarea,checkbox,numberbox,validatebox,datebox,combobox,combotree。
				{title:"价格",field:"price",width:'10%',halign:"center",align:"center",sortable:true},
				{title:"优惠金额",field:"money",width:'10%',halign:"center",align:"center",editor:"text",sortable:true},
				{title:"创建时间",field:"putaway_time",width:'30%',halign:"center",align:"center",hidden:false,sortable:true,editor:"datetimebox"},
				{title:"是否置顶",field:"is_top",width:'10%',halign:"center",align:"center",sortable:true,formatter:function(value,row,index){
					if(value=="Y"){
						return "<b style='color:red'>是</b>"
					}else{
						return "<b>否</b>"
					}		
				}}
			]
		],
		onDblClickCell:function(rowIndex,field,value){
			if(field=="name"){
				var row = $("#dg").datagrid('getRows')[rowIndex];
				$('#textb').textbox('setValue',row.name);	
				$('#cc1').combobox('select',row.is_top);
				$('#cc2').combobox('select',row.state==1);
			}
		},
		queryParams:{
			name: $("#textb").textbox('getValue'),
			isTop:$("input[name=checkIsTop]").val(),
			state:$("input[name=checkIsState]").val()
		}
	});
	//绑定事件
	bindAll();
}
function bindAll(){
		$("#deleteData").on('click',function(){
			var row = $("#dg").datagrid("getSelected");
			if(row==null){
				slide("注意，请先选中一行");
				return;
			}
			if(row.state=="3"){
				slide('该菜品已经删除过')
				return;
			}
				
			$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
				if (r){
					deleteAjax(row.id,$("#dg").datagrid("getRowIndex",row));    
				} else{
					return;
				}
			});  
		
		})
		//搜索功能
		$('#search').on('click',function(){
			$("#dg").datagrid('load',{
				name: $("#textb").textbox('getValue'),
				isTop:  $("#cc1").textbox('getValue'),
				state:  $("#cc2").textbox('getValue')
			});
		})
		//刷新功能
		$("#refresh").on('click',function(){
			
			$("#dg").datagrid('reload');
		})
}
//修改菜品
function adminupdateData(){
	var row = $("#dg").datagrid("getSelected");
	if(!row){
		slide("注意，请先选中一行");
		return;
	}
	$("#dg").append('<div id="updateMenu" style="padding: 10px;"></div>');
	$("#updateMenu").window({
		width:600,    
		height:400,    
		modal:true,
		href:'update.html',
		title:'修改菜品',
		onLoad:function(){
			gender(row);
		},
		onClose:function(){
			$("#cancleUpdateBtn").click();
		}
	});
}
function slide(msg){
	$.messager.show({
		title:'提示',
		msg:msg,
		timeout:1000,
		showType:'slide',
		style:{
			top:'10%',
			left:'40%'
		}
	});
}


function deleteAjax(id,rowIndex){
	$.ajax({
		type:"get",
		url:"/MyShopping/MenuDeleteOrDown?id="+id+"&m=3",
		async:true,
		dataType:'json',
		success:function(msg){
			$("#dg").datagrid("reload");
			$.messager.alert('好消息！',"删除成功···"); 
		}
	});
}
function gender(row){
	$("#name").textbox('setValue',row.name);
	$("#price").textbox('setValue',row.price);
	$("#money").textbox('setValue',row.money);
	$("#updateMenu p input[name=isTop][value='"+row.is_top+"']").attr("checked",true);
	$("#state").combobox('setValue',row.state);
}
function updateMenu(index){
		$("#dg").datagrid('beginEdit',index);
	}
function saveMenu(index){
		$("#dg").datagrid('endEdit',index);
	}
function cancelMenu(index){
		$("#dg").datagrid('cancelEdit',index);
	}
function deleteE(id,index){
		alert(id+":"+index);
	}
	
function addDataMenu(){
	$('#addmenuhtml').window("open");
}

