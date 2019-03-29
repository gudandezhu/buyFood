var namespace = namespace || {};

(function(){
	namespace.ns = function(strs){
		var arrs = strs.split(".");
		obj = window;
		for (var i = 0; i < arrs.length; i++) {
			if(obj[arrs[i]] == undefined){
				obj[arrs[i]]={};
			}
			obj = obj[arrs[i]];
		}
		return obj;
	}
})();