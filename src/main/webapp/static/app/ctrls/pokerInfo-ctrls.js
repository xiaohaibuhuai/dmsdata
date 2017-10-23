MainApp.controller('PokerInfoCtrls', function($scope,TabService) {
		//加载成员列表
		var  dg = $('#memberDG').datagrid({
		    url:PATH+'/stat/poker/memberlist?roomid='+$("#roomid").val(),
		    fit:false,
		    fitColumns : false,
			idField : 'uuid',
			striped: true, 
			border : false,
			nowrap:false,
			rownumbers:false,
			singleSelect:true,
		    pagination : true,
		    pageList : [ 10, 20, 30, 40, 50 ],
			sortName : 'bonus',
			sortOrder : 'desc',
		    checkOnSelect : false,
			selectOnCheck : false,	
		    frozenColumns : [ [ {
				field : 'uuid',
				title : 'UUID',
				width : 100,
				checkbox : false,
		         formatter:function(value, row, index) {
			     	var str = row.uuid;
			    	 if($scope.so_player_view)	str= $.formatString('<a ng-show="so_player_view" href="#"  onclick="$(this).scope().playerView(\'{0}\');"  >'+row.uuid+'</a>',row.uuid);
			    	return str + '';
			    }
				
			},{
				field : 'showid',
				title : 'showid',
				width : 150
			},{
				field : 'nickname',
				title : '昵称',
				width : 150
			}]],
		     columns:[[
						{field : 'buystacks',title : '买入',width : 100},
						{field : 'remainstacks',title : '带出',width : 100},
						{field : 'insurancebuystacks',title : '保险买入',width : 100},
						{field : 'insurancegetstacks',title : '保险收入',width : 100},
						{field : 'bonus',title : '结算',width : 100,sortable : true},
						{field : 'clubid',title : '俱乐部ID',width : 100},
						{field : 'clubname',title : '俱乐部名字',width : 100}
		      ]],
		    onLoadSuccess : function() {
			}
		});

		$scope.clubView = function(id){
			var title = "俱乐部"+id;
			var url = PATH+'/stat/club/clubInfo?id='+id;
			TabService.addTab(title,url);
		}
		
		$scope.playerView = function(id){
			var title = "玩家"+id;
			var url = PATH+'/stat/player/playerInfo?id='+id;
			TabService.addTab(title,url);
		}

		$scope.load = function(){
			parent.$.messager.progress({
				title : '提示',
				text : '数据处理中，请稍后....'
				});
		}
} );












 

