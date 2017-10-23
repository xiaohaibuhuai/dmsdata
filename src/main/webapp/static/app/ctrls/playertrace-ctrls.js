MainApp.controller('PlayerTraceCtrls', function($scope, TabService) {
	var init =true;

	$('#dataGrid').datagrid({
		url : PATH+'/stat/playertrace/list',
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
			    	str= $.formatString('<a ng-show="so_view" href="#"  onclick="$(this).scope().viewFun(\'{0}\');"  >'+row.uuid+'</a>',row.uuid);
				return str+'';
	         }
		} ,
		{
			field : 'nickname',
			title : '昵称',
			width : 120,
			sortable : false
		} ] ],
		columns : [ [ 
			{
				field : 'phonenumber',
				title : '手机',
				width : 100,
				sortable : false
			}, {
				field : 'viptype',
				title : 'VIP',
				width : 80,
				sortable : false,
				formatter:function(value){
					if(value=='BLUE_CARD')return '蓝卡';
					if(value=='GOLD_CARD')return '黄金卡';
					if(value=='BLACK_CARD')return '黑卡';
					if(value=='PLATINUM_CARD')return '白金卡';
				}
			},{
				field : 'gender',
				title : '性别',
				width : 50,
				sortable : false,
				formatter:function(value){
					if(value=='0')return '女';
					if(value=='1')return '男';
				}
			},
			{
				field : 'diamond',
				title : '钻石',
				width : 80,
				sortable : false
			},{
				field : 'depu_coin',
				title : '德扑币',
				width : 80,
				sortable : false
			},
//			{
//				field : 'android_diamond',
//				title : '钻石（安卓）',
//				width : 80,
//				sortable : false
//			},{
//				field : 'android_depu_coin',
//				title : '德扑币（安卓）',
//				width : 80,
//				sortable : false
//			},
			{
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
			] ],
		onLoadSuccess : function() {
			
			init=false
			$('#searchForm table').show();
			parent.$.messager.progress('close');
			$(this).datagrid('tooltip');
			
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






$scope.searchFun=function() {
	$('#dataGrid').datagrid('load', $.serializeObject($('#searchForm')));
};
$scope.cleanFun=function() {
	$('#searchForm input').val('');
	$('#dataGrid').datagrid('load', {});
};



});
