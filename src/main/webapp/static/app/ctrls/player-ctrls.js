MainApp.controller('PlayerCtrls', function($scope, TabService) {
	var init =true;
	var dg = $('#dataGrid').datagrid({
		url : PATH+'/stat/player/list',
		fit : true,
		fitColumns : true,
		border : false,
		pagination : true,
		idField : 'uuid',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		sortName : 'uuid',
		sortOrder : 'desc',
		checkOnSelect : false,
		selectOnCheck : false,
		nowrap : false,
		striped : true,
		rownumbers : false,
		singleSelect : true,
		toolbar:'#toolbar',
		frozenColumns : [ [ 
		{
			field : 'uuid',
			title : 'UUID',
			width : 80,
			sortable : true,
			formatter : function(value, row, index) {
				var str = row.uuid;
			    if($scope.so_view) 
			    	str= $.formatString('<a ng-show="so_view"  href="#" onclick="$(this).scope().viewFun(\'{0}\');"  >'+row.uuid+'</a>',row.uuid);
				return str+'';
	         }
		} ,
		{
			field : 'showid',
			title : 'showid',
			width : 80,
			sortable : false
		} ,
		{
			field : 'nickname',
			title : '昵称',
			width : 120,
			sortable : false
		} ] ],
		columns : [[
			{
				field : 'idtype',
				title : '注册类型',
				width : 50,
				sortable : false,
				formatter:function(value){
					if(value=='ID_TYPE_PHONE')return '手机号';
					if(value=='ID_TYPE_FACEBOOK')return 'FaceBook';
				}
			},{
				field : 'countrycode',
				title : '国家区号',
				width : 40,
				sortable : false
			},{
				field : 'phonenumber',
				title : '手机',
				width : 80,
				sortable : false
			},{
				field : 'state',
				title : '状态',
				width : 30,
				sortable : false,
				formatter:function(value){
					if(value=='0')return '正常';
					if(value=='1')return '已封';
				}
			}, {
				field : 'pwd',
				title : '密码',
				width : 50,
				sortable : false
			},{
				field : 'viptype',
				title : 'VIP',
				width : 50,
				sortable : false,
				formatter:function(value){
					if(value=='BLUE_CARD')return '蓝卡';
					if(value=='GOLD_CARD')return '黄金卡';
					if(value=='BLACK_CARD')return '黑卡';
					if(value=='PLATINUM_CARD')return '白金卡';
				}
			},{
				field : 'viplimittime',
				title : '到期时间',
				width : 70,
				sortable : false
			},{
				field : 'gender',
				title : '性别',
				width : 30,
				sortable : false,
				formatter:function(value){
					if(value=='0')return '女';
					if(value=='1')return '男';
				}
			},
			{
				field : 'diamond',
				title : '钻石',
				width : 50,
				sortable : true
			},{
				field : 'depu_coin',
				title : '德扑币',
				width : 50,
				sortable : true
			},{
				field : 'languagetype',
				title : '语言',
				width : 30,
				sortable : false,
				formatter:function(value){
					if(value=='1')return '简体';
					if(value=='2')return '英文';
					if(value=='3')return '繁体';
				}
			},{
				field : 'devicetype',
				title : '设备',
				width : 30,
				sortable : false,
				formatter:function(value){
					if(value=='1')return '苹果';
					if(value=='2')return '安卓';
				}
			},{
				field : 'address',
				title : '最近所在地（参考）',
				width : 80,
				sortable : false
			},{
				field : 'ip',
				title : '最近登录ip',
				width : 80,
				sortable : false
			}
//			,{
//				field:'action',
//				title:'操作',
//				width:100,
//		        formatter:function(value, row, index) {
//			     		var str = '';
//			    	    if($scope.so_setpwd && row.state==1)str += $.formatString('<img ng-show="so_setpwd" style="float:left;margin-left: 4px;" onclick="$(this).scope().setPwdFun(\'{0}\');" src="{1}" title="设置密码"/>',row.uuid,PATH+'/static/js/ext/style/images/extjs_icons/bricks.png');
//			    	    if($scope.so_givevip) str += $.formatString('<img ng-show="so_givevip" style="float:left;margin-left: 4px;" onclick="$(this).scope().giveVipFun(\'{0}\');" src="{1}" title="赠送会员"/>',row.uuid,PATH+'/static/js/ext/style/images/extjs_icons/shield_go.png');
//			    	    return str + '';
//			    	}
//			    }
			] ],
		onLoadSuccess : function() {
			init=false
			$('#searchForm table').show();
			parent.$.messager.progress('close');
//			$(this).datagrid('tooltip');
		},
		onRowContextMenu : function(e, rowIndex, rowData) {
			e.preventDefault();
			$(this).datagrid('unselectAll');
			$(this).datagrid('selectRow', rowIndex);
			$('#menu').menu('show', {
				left : e.pageX,
				top : e.pageY
			});
		}
	});

$scope.viewFun=function(id) {
	 url = PATH+'/stat/player/playerInfo?id='+id;
	 title = "玩家"+id;
	 TabService.addTab(title,url);
};


//标记发放状态
$scope.setPwdFun=function(id) {
	if (id != undefined)dg.datagrid('selectRecord', id);
	var node = dg.datagrid('getSelected');
	if(node.state != 1){
		alert("只有被封的用户可以设置密码");
		return;
	}
	if (node) {
		$('#rfm').form('clear');
		loadFrom('#rfm',node);
	  	$('#uuid').val(node.uuid);
	  	$('#nickname').val(node.nickname);
		showDialog('#ddlg','设置被封用户密码');
		url=PATH+'/stat/player/setPwd';
	}
};

$scope.setpwdSubmit=function(rfm,ddlg){
	if(!$("#rfm").form('validate')) return;
	var msg = "将解封该被封玩家，请确保密码密文正确！";
	$.messager.confirm("提示", msg, function(flag) {
		if(flag){
			$scope.load();
			$(rfm).form('submit',{
                url: url,
                onSubmit: function ()
                 { },
                 success: function(result){
                	parent.$.messager.progress('close');
                	result = $.parseJSON(result);
	                if(result.code==0){
	                   $.messager.alert('提示','密码设置成功');
	                }else if(result.code==1){
	                  $.messager.alert('提示','该用户不是被封用户，无法设置密码');
	                }else if(result.code==2){
	                  $.messager.alert('提示','保存失败，请联系管理员');
	                }else if(result.code==3){
	                  $.messager.alert('提示','服务器异常');
	                }
	                $(ddlg).dialog('close');
	                dg.datagrid('reload');
                 }
            });		
	     }
    }); 
};

//赠送vip
$scope.giveVipFun=function(id) {
	if (id != undefined)dg.datagrid('selectRecord', id);
	var node = dg.datagrid('getSelected');
	if (node) {
		$('#rfm1').form('clear');
		loadFrom('#rfm1',node);
	  	$('#uuid1').val(node.uuid);
	  	$('#nickname1').val(node.nickname);
		showDialog('#ddlg1','赠送vip');
		url1=PATH+'/stat/player/giveVip';
	}
};

$scope.giveVipSubmit=function(rfm,ddlg){
	if(!$("#rfm1").form('validate')) return;
	var msg = "将赠送玩家VIP特权，请确认";
	$.messager.confirm("提示", msg, function(flag) {
		if(flag){
			$scope.load();
			$(rfm).form('submit',{
                url: url1,
                onSubmit: function ()
                 { },
                 success: function(result){
                	parent.$.messager.progress('close');
                	result = $.parseJSON(result);
	                if(result.code==0){
	                   $.messager.alert('提示','赠送VIP特权成功');
	                }else{
	                  $.messager.alert('提示','服务器异常');
	                }
	                $(ddlg).dialog('close');
	                dg.datagrid('reload');
                 }
            });		
	     }
    }); 
};


$scope.searchFun=function() {
	$('#dataGrid').datagrid('load', $.serializeObject($('#searchForm')));
};
$scope.cleanFun=function() {
	$('#searchForm input').val('');
	$('#dataGrid').datagrid('load', {});
};

$scope.load = function(){
	parent.$.messager.progress({
		title : '提示',
		text : '数据处理中，请稍后....'
		});
}
});