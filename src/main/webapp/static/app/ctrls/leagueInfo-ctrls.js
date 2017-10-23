MainApp.controller('LeagueInfoCtrls',  function($scope,TabService) {
	var init =true;
	var initMember = false;
	var initPoker = false;
	var initToplist = false;
	 $('#clubTab').tabs({    
	      onSelect:function(title){   
	          if(title=='成员信息'){
	        	  $scope.loadMember();
	          }
	          else if(title=='牌局信息'){
	        	  $scope.loadPoker();
	          }
			  else if(title=='战绩排行'){
				  $scope.loadToplist();     	  
			  }
	      }   
	 });
$scope.loadMember = function(){
	if(!initMember){
		//$scope.load()
		$("#memberDiv").html($("#base").html());
		$("#memberDiv").append('<div style=" width:1500px;"><div id="memberchart" style=" width: 750px;  float:right;"></div><div   style=" width: 750px;  "> <table id="memberDG" title="成员列表"  ></table></div></div>');
		//加载成员列表
		var  dg = $('#memberDG').datagrid({
		    url:PATH+'/stat/league/memberlist?leagueid='+$("#leagueid").val(),
		    fit:false,
		    fitColumns : false,
			idField : 'clubid',
			striped: true, 
			border : false,
			nowrap:false,
			rownumbers:false,
			singleSelect:true,
		    pagination : true,
		    checkOnSelect : false,
			selectOnCheck : false,	
		    frozenColumns : [ [ {
				field : 'clubid',
				title : '成员ID',
				width : 80,
				checkbox : false,
				align:'left',
		         formatter:function(value, row, index) {
			     	var str = row.memberuuid;
			    	 if($scope.so_player_view)	str= $.formatString('<a ng-show="so_club_view"  href="#"  onclick="$(this).scope().clubView(\'{0}\');"  >'+row.clubid+'</a>',row.clubid);
			    	return str + '';
			    }
			}, {
				field : 'clubname',
				title : '俱乐部名称',
				width : 120,
				align:'left'
			} ] ],
		     columns:[[
						{
							field : 'fund',
							title : '俱乐部基金',
							width : 100,
							sortable : false
						},{
							field : 'members',
							title : '人数',
							width : 100,
							sortable : false
						},
						{
							field : 'maxmembers',
							title : '上限',
							width : 100,
							sortable : false
						},{
							field : 'createUser',
							title : '创建者UUID',
							width : 100,
							sortable : false
						}
		      ]],
		      onLoadSuccess : function() {
			}
		});
		initMember =  true;
		//parent.$.messager.progress('close');
	}
}


$scope.loadToplist = function(){
	if(!initToplist){
		//$scope.load();

		var  dg = $('#toplistDG').datagrid({
		    url:PATH+'/stat/club/toplist?clubid='+$("#clubid").val(),
		    fit:false,
		    fitColumns : false,
			idField : 'uuid',
			striped: true, 
			border : false,
			nowrap:false,
			rownumbers:false,
			singleSelect:true,
		    pagination : false,
			pageSize : 10,
			pageList : [ 10, 20, 30, 40, 50 ],
			sortName : 'bonus',
			sortOrder : 'desc',
		    checkOnSelect : false,
			selectOnCheck : false,	
		    frozenColumns : [ [ {
				field : 'uuid',
				title : '玩家ID',
				width : 100,
				align:'center',
		         formatter:function(value, row, index) {
			     	var str = row.uuid;
			    	 if($scope.so_player_view)	str= $.formatString('<a ng-show="so_player_view"  href="#"  onclick="$(this).scope().playerView(\'{0}\');"  >'+row.uuid+'</a>',row.uuid);
			    	return str + '';
			    }
				
			}, {
				field : 'nickname',
				title : '玩家昵称',
				width : 100,
				align:'center'
			} ] ],
		     columns:[[
					{
						field : 'bonus',
						title : '玩家盈亏',
						width : 100,
						sortable : true
					
					}
		     
		      ]],
		    onLoadSuccess : function() {
		    	initToplist = true;
		    	

			}
		});
		//parent.$.messager.progress('close');
	}
}

$scope.loadPoker = function(){
	if(!initPoker){
		//$scope.load();
		var  dg = $('#pokerDG').datagrid({
		    url:PATH+'/stat/league/pokerlist?leagueid='+$("#leagueid").val(),
		    fit:false,
		    fitColumns : false,
			idField : 'roomid',
			striped: true, 
			border : false,
			nowrap:false,
			rownumbers:false,
			singleSelect:true,
		    pagination : true,
			pageSize : 10,
			pageList : [ 10, 20, 30, 40, 50 ],
			sortName : 'ctime',
			sortOrder : 'desc',
		    checkOnSelect : false,
			selectOnCheck : false,	
		    frozenColumns : [ [ {
				field : 'roomid',
				title : '牌局ID',
				width : 80,
		         formatter:function(value, row, index) {
			     	var str = row.roomid;
			    	 if($scope.so_poker_view)	str= $.formatString('<a ng-show="so_poker_view" href="#"   onclick="$(this).scope().pokerView(\'{0}\');"  >'+row.roomid+'</a>',row.roomid);
			    	return str + '';
			    }
				
			}, {
				field : 'roomname',
				title : '牌局名子',
				width : 120
			} ] ],
		     columns:[[
		           	{field : 'blind',title : '盲注',width : 50},
		            {field : 'createuser',title : '创建者ID',width : 60,
			            formatter:function(value, row, index) {
					     	var str = row.createuser;
					    	 if($scope.so_player_view)	str= $.formatString('<a ng-show="so_player_view" href="#"   onclick="$(this).scope().playerView(\'{0}\');"  >'+row.createuser+'</a>',row.createuser);
					    	return str + '';
					    }
			        },
			        {field : 'nickname',title : '创建者昵称',width : 100},
			        {field : 'fromroomid',title : '俱乐部ID',width : 100},
			        {field : 'leagueid',title : '联盟ID',width : 100},
					{field : 'ctime',title : '结束时间',width : 120,sortable : true},
					{field : 'gtime',title : '牌局时间',width : 60},
					{field : 'hands',title : '总手数',width : 60},
					{field : 'players',title : '玩家数',width : 60}
		      ]],
		    onLoadSuccess : function() {
			}
		});

		//加载俱乐部每日盲注分布图
		$scope.loadBlindChart();
		//加载俱乐部每日牌局时长分布图
		$scope.loadGtimeChart();
		initPoker = true;
	    //parent.$.messager.progress('close');
	}
	
}


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
$scope.pokerView = function(id){
	var title = "牌局"+id;
	var url = PATH+'/stat/poker/pokerInfo?id='+id;
	TabService.addTab(title,url);
}




$scope.searchPokerFun=function() {
	$scope.load();
	$('#pokerDG').datagrid('load', $.serializeObject($('#searchPokerForm')));
	$scope.loadBlindChart();
	$scope.loadGtimeChart();
	
};
$scope.cleanPokerFun=function() {
	$scope.load();
	$('#searchPokerForm input').val('');
	$('#pokerDG').datagrid('load', {});
	$scope.loadBlindChart();
	$scope.loadGtimeChart();
};
$scope.searchToplistFun=function() {
	$('#toplistDG').datagrid('load', $.serializeObject($('#searchForm')));

};
$scope.cleanToplistFun=function() {
	$('#searchForm input').val('');
	$('#toplistDG').datagrid('load', {});

};

$scope.loadBlindChart =  function(){

	 $.getJSON(PATH+'/stat/league/blindchart',{
			leagueid : $("#leagueid").val(),
			dateStart:$("#dateStart").val(),
			dateEnd:$("#dateEnd").val()
		},function(data){
			$('#blindchart').highcharts({
		        title: {
		            text: '联盟每天盲注分布图',
		            x: -20 //center
		        },
		        credits : {
					enabled : false
				},
		        xAxis: {
		            categories: data.categories
		        },
		        yAxis: {
		            plotLines: [{
		                value: 0,
		                width: 1,
		                color: '#808080'
		            }]
		        },
		        legend: {
		            layout: 'vertical',
		            align: 'right',
		            verticalAlign: 'middle',
		            borderWidth: 0
		        },
		        series: data.series
		    });
		} );
}

$scope.loadGtimeChart =  function(){
	 $.getJSON(PATH+'/stat/league/gtimechart',{
			leagueid : $("#leagueid").val(),
			dateStart:$("#dateStart").val(),
			dateEnd:$("#dateEnd").val()
		},function(data){
			$('#gtimechart').highcharts({
		        title: {
		            text: '联盟牌局时长分布图',
		            x: -20 //center
		        },
		        credits : {
					enabled : false
				},
		        xAxis: {
		            categories: data.categories
		        },
		        yAxis: {
		            plotLines: [{
		                value: 0,
		                width: 1,
		                color: '#808080'
		            }]
		        },
		        legend: {
		            layout: 'vertical',
		            align: 'right',
		            verticalAlign: 'middle',
		            borderWidth: 0
		        },
		        series: data.series
		    });
			parent.$.messager.progress('close');
		} );
}

$scope.start=function(id){
    var msg = '确认关注此俱乐部？';
	parent.$.messager.confirm('询问', msg, function(b) {
		if (b) {
			   parent.$.messager.progress({
				  title : '提示',
				  text : '数据处理中，请稍后....'
			    });
				$.post(PATH+'/stat/clubtrace/start', {
					id : id
				}, function(result) {
					window.location.reload();
					parent.$.messager.progress('close');
				}, 'JSON');			
		}
	});		
};

$scope.end=function(id){
	var msg = '确认取消关注此俱乐部？';
	parent.$.messager.confirm('询问', msg, function(b) {
		if (b) {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				$.post(PATH+'/stat/clubtrace/end', {
					id : id
				}, function(result) {
					window.location.reload();
					parent.$.messager.progress('close');
				}, 'JSON');			
		}
	});		
};

$scope.load = function(){
	parent.$.messager.progress({
		title : '提示',
		text : '数据处理中，请稍后....'
		});
}
} );