MainApp.controller('PokerCtrls', function($scope,TabService) {
	$('#dataGrid').datagrid({
		url : PATH+'/stat/poker/list',
		fit : true,
		fitColumns : true,
		border : false,
		pagination : true,
		idField : 'roomid',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		sortName : 'roomid',
		sortOrder : 'desc',
		checkOnSelect : false,
		selectOnCheck : false,
		nowrap : false,
		striped : true,
		rownumbers : false,
		singleSelect : true,
		toolbar:'#toolbar',
	    frozenColumns : [ [ {
			field : 'roomid',
			title : '牌局ID',
			width : 100,
			sortable : true,
	         formatter:function(value, row, index) {
		     	var str = row.roomid;
		    	 if($scope.so_view)	str= $.formatString('<a ng-show="so_view" href="#"  onclick="$(this).scope().viewFun(\'{0}\');"  >'+row.roomid+'</a>',row.roomid);
		    	return str + '';
		    }
			
		}, {
			field : 'roomname',
			title : '牌局名称',
			width : 120
		} ] ],
	     columns:[[
		           	{field : 'blind',title : '盲注',width : 100},
		            {field : 'createuser',title : '创建者ID',width : 100,
			            formatter:function(value, row, index) {
					     	var str = row.createuser;
					    	 if($scope.so_player_view)	str= $.formatString('<a ng-show="so_player_view"  href="#" onclick="$(this).scope().playerView(\'{0}\');"  >'+row.createuser+'</a>',row.createuser);
					    	return str + '';
					    }
			        },
			        {field : 'showid',title : '创建者showid',width : 150},
			        {field : 'nickname',title : '创建者昵称',width : 150},
					{field : 'ctime',title : '开始/结束时间',width : 100,sortable : true},
					{field:'overtag', title:'是否结束',width : 100,
			        	formatter:function(value){
							if(value==0)return '否';
							if(value==1)return '是';
						}
			        },
					{field : 'gtime',title : '牌局时间',width : 100},
					{field : 'hands',title : '总手数',width : 100},
					{field:'gameroomtype', title:'牌局类型',width : 100,
			        	formatter:function(value){
							if(value==1)return '普通局';
							if(value==2)return 'SNG局';
							if(value==3)return '普通保险局';
							if(value==4)return '6＋局';
							if(value==5)return '奥马哈局';
							if(value==6)return '奥马哈保险局';
						}
			        },
			        {field:'leagueid', title:'联盟局',width : 100,
			        	formatter:function(value){
							if(value==0)return '否';
							if(value!=0)return value;
						}
			        }
	      ]],
		onLoadSuccess : function() {
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
	
		var title = "牌局"+id;
		var url = PATH+'/stat/poker/pokerInfo?id='+id;
		TabService.addTab(title,url);
		
	};
	
	$scope.playerView=function(id) {
	
		var title = "玩家"+id;
		var url = PATH+'/stat/player/playerInfo?id='+id;
		TabService.addTab(title,url);
		
	};
	
	
	$scope.searchFun=function() {
		$('#dataGrid').datagrid('load', $.serializeObject($('#searchForm')));
	};
	$scope.cleanFun=function() {
		$('#searchForm input').val('');
		$('#dataGrid').datagrid('load', {});
	};

} );