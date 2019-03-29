$(function(){
	$('#tb').textbox({    
		iconCls:'icon-man', 
		iconAlign:'left'       
	});
	$('#cc').combobox({
		valueField:'id',    
		textField:'gender',
		data:[
			{'id':'男','gender':'男'},
			{'id':'女',"gender":"女"}
		]
	});
	$('#love').combobox({    
		required:true,    
		multiple:true,
		editable:false,
		panelMinHeight:30,
		valueField:'id',    
		textField:'lover',
		data:[
			{'id':0,'lover':'LOL'},
			{'id':1,"lover":"王者荣耀"},
			{'id':2,"lover":"传奇"},
			{'id':3,"lover":"绝地求生"},
		]
	}); 
	$('#ss').numberspinner({
		min: 1,
		max: 200,
		editable: false   
	});
	$('#dt').datetimebox({    
		value: '1995-1-15 00:00',    
		required: true,    
		showSeconds: false   
	});
	$('#nn').numberbox({ 
		required:true,
		precision:0,
		validType:'length[11,11]',
		invalidMessage:'电话号码必须是11位'
	}); 
	$('#btn').linkbutton({
		iconCls: 'icon-save',
	});  
	 $('#btn').bind('click', function(){
		 
        var name =  $('#tb').textbox('getValue');
		//var gender = $('#cc').combobox('getValue');
		var gender = $('input[name=gender]:checked').val();
		
		//var loves = $('#love').combobox('getValues');
		var $love = $('input[name=love]:checked');
		var loves = [];
		$love.each(function (index,temp) {
			var obj = $(this);
			loves.push(obj.val());
		})
		
		
		var year  = $('#ss').numberspinner('getValue');
		var  birthday = $("#dt").datetimebox('getValue');
		var phone  =  $('#nn').numberbox('getValue');
		var p = {name,gender,loves,year,birthday,phone};
		 console.log(p);
    });
})