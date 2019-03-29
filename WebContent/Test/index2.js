$(function(){
// 	$("#tt").tree({
// 			url:'CityTreeServlet',
// 			method:'get',
// 			dnd:true,             //定义是否启用拖拽功能。
// 			cascadeCheck:false,   //定义是否层叠选中状态。
// 			checkbox:function(node){  //定义是否在每一个借点之前都显示复选框。
// 				if(node.state=='closed' || !$("#tt").tree('isLeaf',node.target)){
// 					return true;
// 				}
// 				
// 			},		
// 			//onlyLeafCheck:true,   //定义是否只在末级节点名称前面显示复选框。
// 			onDblClick:function(node){
// 				$("#tt").tree('beginEdit',node.target);
// 			},
// 			onAfterEdit:function(node){     //编辑后事件
// 				//保存到数据库
// 				saveToSQL(node.id,node.text);
// 			},
// 			onBeforeDrop:function(target, source, point){   //拖动前事件
// 				if(point=='append' || point=='bottom'){
// 					return false;
// 				}
// 				var parent1 = $("#tt").tree('getParent',source.target);
// 				var parent2 = $("#tt").tree('getParent',target);
// 				return parent1==parent2;
// 			},
// 			onSelect:function(node){      //选中节点触发事件
// 				/* var parent;
// 				while(parent = $("#tt").tree('getParent',node.target)){
// 					node = parent;
// 				} */
// 			//	console.log(node.text);
// 			/*  var parent;
// 				while(parent = $("#tt").tree('getParent',node.target)){
// 						node = parent;
// 				}
// 			*/
// 				console.log($("#tt").tree('getRoot',node.target));
// 			},
// 			onLoadSuccess:function(node, data){     //加载数据成功触发事件
// 				for (var i = 0; i < data.length; i++) {
// 					var node = $('#tt').tree('find', data[i].id);
// 				}
// 			},
// 			 onCheck:function(node,checked){    //点击复选框 触发事件
// 				if(checked==true){
// 					getAllNodes();
// 				}
// 			} 
// 			
// 			
// 		})
		function saveToSQL(id,text){
			var p = {id,text};
			$.ajax({
				type:'GET',
				data:p,
				url:'CityInfoUpdateServlet?',
				async:true
			})
		}
	$("#tb").textbox({
		buttonText:'Search',
		iconCls:'icon-search',
		iconAlign:'left'    
	})	
	$("#tb").textbox('button').click(function(){
		var name = $("#tb").textbox('getText');
		$.ajax({
			type:'get',
			url:'CityTreeServlet2?name='+name,
			async:true,
			dataType:'json',
			success:function(msg){
				if(msg.length!=0){
					path=[];
					index=0;
					msg.reverse()
					path = msg;
					$("#tt2").tree('reload');
				}else{
					$.messager.alert('警告','没有该地方');  
				}
			}
		})
	})
	
	$("#tt2").tree({
		url:'../CityTreeServlet2',
		method:'get',
		dnd:true,
		dnd:true, 
		checkbox:true,
		onDblClick:function(node){
			$("#tt2").tree('beginEdit',node.target);
		}, 
		onAfterEdit:function(node){
			//保存到数据库
			saveToSQL(node.id,node.text);
		},
		onLoadSuccess:function(node,data){
			if(index<path.length){
				var nodes = $('#tt2').tree('find', path[index]);
				index++;
				if(index==path.length){
					$("#tt2").tree('check',nodes.target);
					
				}else{
					$('#tt2').tree('expand',nodes.target);
				}
			}
		}
	})
	
	
})
var path=[];
var index = 0;
function getAllNodes(){
	var nodes = $('#tt').tree('getChecked');
	console.log(nodes);
}

