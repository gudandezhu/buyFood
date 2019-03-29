$(function(){
	$('#dd').draggable({
		handle:'#title' 
	}); 
	$('#dd1').droppable({
		accept:'#dd' ,
		onDragOver:function(e,source){
			$(source).css({'width':'50px','height':'50px','overflow':'auto'})
			console.log(1);
		},
		onDrop:function(e,source){
			$(source).remove();
		},
		onDragLeave:function(e,source){
			$(source).css({'width':'100%','height':'100%','overflow':'none'})
		}
	}); 
	$("#cc1").combobox({
		editable:false,
		valueField:'id',    
		textField:'name',
		url:'/MyShopping/City?state=1',
		onSelect: function(rec){
			$('#cc2').combobox('clear');
			$('#cc3').combobox('clear');
			$('#cc3').combobox('loadData',[]);
            $('#cc2').combobox('reload','/MyShopping/City?state=2&province_id='+rec.id);    
        }
	})
	$("#cc2").combobox({
		editable:false,
		valueField:'id',
		textField:'name',
		
		onSelect:function(rec){
			$('#cc3').combobox('clear');
			 $('#cc3').combobox('reload','/MyShopping/City?state=3&city_id='+rec.id);    
		}
	})
	$("#cc3").combobox({
		editable:false,
		valueField:'id',    
		textField:'name',
	})
	
	
	
	
	
	
})


