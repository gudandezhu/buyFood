$(function(){
	$('#dd').droppable({
		accept:'#d1,#d3',
		iconCls:'icon-search'
	}); 
	$('#tt').tree({    
	   /*data: [{
		   'id':1,
			'text': 'Item1',
			'state': 'closed',
			'children': [
				{'id':2,'text': 'Item11'},
				{'id':3,'text': 'Item12'},
			]},
			{'id':4,'text': 'Item2'}
		] */
		 data:[{    
			"id": 1,    
			"text": "Node 1",    
			"state": "closed",    
			"children": [{    
				"id": 11,    
				"text": "Node 11"   
			},{    
				"id": 12,    
				"text": "Node 12"   
			}]    
		},{
			"id": 2,    
			"text": "Node 2",    
			"state": "closed"   
		}],
		  lines:true,
		  dnd:true
	});
})