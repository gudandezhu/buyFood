//创建命名空间
easyShopping.formatting('easyShopping.js.adminManagementJs');
/*方法定义*/
easyShopping.js.adminManagementJs = {
    init:function(){
        /*渲染界面begion*/
        $('#leftOption').panel({
            href:'web/adminOption.html',
            fit:true,
            border:false
        });
        // $('#leftOption').panel('href',"")
        $('#allGoods').linkbutton({
            text:'商品管理',
            group:'option',
            height:35
        })
        $('#allOrder').linkbutton({
            text:'订单管理',
            group:'option',
            height:35
        })
        $('#myInfo').linkbutton({
            text:'商家信息',
            group:'option',
            height:35
        })
        $('#showView').tabs('add',{
            title:'商家信息',
            href:'web/adminInfo.html',
        })
        /*渲染界面end*/
        /*赋值事件begin*/
        //全部菜单按钮
        $('#allGoods').click(function(){
            if ($('#showView').tabs('getTab','菜单管理')) {
                $('#showView').tabs('select','菜单管理')
            } else {
                easyShopping.js.adminManagementJs.createTabPage('菜单管理','web/adminMenu.html');
            }
        })
        //全部订单
        $('#allOrder').click(function(){
            if ($('#showView').tabs('getTab','订单管理')) {
                $('#showView').tabs('select','订单管理')
            } else {
                easyShopping.js.adminManagementJs.createTabPage('订单管理','web/adminOrder.html');
            }
        })
        //商家信息
        $('#myInfo').click(function(){
            $('#showView').tabs('select','商家信息')
        })
        /*赋值事件end*/
    },/*创建tabs页*/
    createTabPage:function(title,url){
        $('#showView').tabs('add',{
            title:title,
            href:url,
            closable:true
        });
    }
};
$(function(){
    //初始化数据
    easyShopping.js.adminManagementJs.init();
})