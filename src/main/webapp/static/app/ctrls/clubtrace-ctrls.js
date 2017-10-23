MainApp.controller('ClubTraceCtrls',  function($scope,TabService) {
	
	var init =true;

	$('#dataGrid').datagrid({
		url : PATH+'/stat/clubtrace/list',
		fit : true,
		fitColumns : true,
		border : false,
		pagination : true,
		idField : 'clubid',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		sortName : 'clubid',
		sortOrder : 'desc',
		checkOnSelect : false,
		selectOnCheck : false,
		nowrap : false,
		striped : true,
		rownumbers : false,
		singleSelect : true,
		toolbar:'#toolbar',
	    frozenColumns : [ [ {
			field : 'clubid',
			title : '俱乐部ID',
			width : 100,
			checkbox : false,
			sortable : true,
			align:'left',
	         formatter:function(value, row, index) {
		     	var str = row.clubid;
		    	 if($scope.so_view)	str= $.formatString('<a ng-show="so_view"  href="#" onclick="$(this).scope().viewFun(\'{0}\');"  >'+row.clubid+'</a>',row.clubid);
		    	return str + '';
		    }
			
		}, {
			field : 'clubname',
			title : '俱乐部名称',
			width : 120,
			align:'left'
		} ] ],
	     columns:[[
	        //{field : 'clublocation',title : '俱乐部地点',width : 80,align:'left'},
	        {field : 'createuser',title : '创建者ID',width : 80,align:'left',
	            formatter:function(value, row, index) {
			     	var str = row.createuser;
			    	 if($scope.so_player_view)	str= $.formatString('<a ng-show="so_player_view" href="#"  onclick="$(this).scope().playerView(\'{0}\');"  >'+row.createuser+'</a>',row.createuser);
			    	return str + '';
			    }
	        },
	        {field:'fund',title:'基金',width:80,align:'left'},
	        {field:'maxmembers',title:'最大人数',width:80,align:'left'},
	        {field:'weekNum',title:'近7天牌局数',width:80,align:'left'},
	        {field:'monthNum',title:'近30天牌局数',width:80,align:'left'}
	      ]],
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

	
	$scope.viewFun = function(id){
		var title = "俱乐部"+id;
		var url = PATH+'/stat/club/clubInfo?id='+id;
		TabService.addTab(title,url);

	}

	$scope.playerView = function(id){
		var title = "玩家"+id;
		var url = PATH+'/stat/player/playerInfo?id='+id;
		TabService.addTab(title,url);
		
	}

$scope.searchFun=function() {
	$('#dataGrid').datagrid('load', $.serializeObject($('#searchForm')));
};
$scope.cleanFun=function() {
	$('#searchForm input').val('');
	$('#dataGrid').datagrid('load', {});
};



 


} );












 

