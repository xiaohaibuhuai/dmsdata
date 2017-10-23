MainApp.controller('ClubInfoCtrls',  function($scope,TabService) {
	
	var init =true;
	var initMember = false;
	var initPoker = false;
	var initToplist = false;
	var initLeague = false;
	
	 $('#clubTab').tabs({    
	      onSelect:function(title){   
	          if(title=='成员信息'){
	        	  $scope.loadMember();
	          }
	          if(title=='他的联盟'){  
	        	  $scope.loadLeague();
	          }
	          else if(title=='牌局信息'){
	        	  $scope.loadPoker();
	          }
			  else if(title=='战绩排行'){
				  $scope.loadToplist();     	  
			  }
	      }   
	 });
	 
	 $scope.loadLeague = function(){
			if(!initLeague){
				$("#leagueDiv").html($("#base").html());
				$("#leagueDiv").append('<table id="leagueDG" title="联盟列表"  ></table>');
				var  dg = $('#leagueDG').datagrid({
				    url:PATH+'/stat/club/leaguelist?clubid='+$("#clubid").val(),
				    fit:false,
				    fitColumns : false,
					idField : 'leagueid',
					striped: true, 
					border : false,
					nowrap:false,
					rownumbers:false,
					singleSelect:true,
				    pagination : false,
				    checkOnSelect : false,
					selectOnCheck : false,	
				    frozenColumns : [ [ {
						field : 'leagueid',
						title : '联盟ID',
						width : 100,
				         formatter:function(value, row, index) {
					     	var str = row.leagueid;
					    	 if($scope.so_league_view)	str= $.formatString('<a ng-show="so_league_view"  href="#" onclick="$(this).scope().leagueView(\'{0}\');"  >'+row.leagueid+'</a>',row.leagueid);
					    	return str + '';
					    }
						
					}, {
						field : 'leaguename',
						title : '联盟名称',
						width : 150
					} ] ],
				     columns:[[
				        {field : 'ctime',title : '创建时间',width : 100},
				        {field : 'leaguelord',title : '创建俱乐部ID',width : 80,
				            formatter:function(value, row, index) {
						     	var str = row.leaguelord;
						    	 if($scope.so_club_view)	str= $.formatString('<a ng-show="so_club_view" href="#"  onclick="$(this).scope().clubView(\'{0}\');"  >'+row.leaguelord+'</a>',row.leaguelord);
						    	return str + '';
						    }
				        },
				        {field : 'clubname',title : '创建者俱乐部昵称',width : 120},
				        {field:'maxmembers',title:'联盟上限',width:80},
				        {field:'members',title:'成员数',width:80}
				      ]],
				    onLoadSuccess : function() {
				    	initLeague = true;
					}
				});
			}
		}
	
$scope.loadMember = function(){
	if(!initMember){
		//$scope.load()
		$("#memberDiv").html($("#base").html());
		$("#memberDiv").append('<div style=" width:1500px;"><div id="memberchart" style=" width: 750px;  float:right;"></div><div   style=" width: 750px;  "> <table id="memberDG" title="成员列表"  ></table></div></div>');
		//加载成员vip分布图
		$scope.loadMemberChart();
		//加载成员列表
		var  dg = $('#memberDG').datagrid({
		    url:PATH+'/stat/club/memberlist?clubid='+$("#clubid").val(),
		    fit:false,
		    fitColumns : false,
			idField : 'memberuuid',
			sortName : 'cmi.memberlevel',
			sortOrder : 'asc',
			striped: true, 
			border : false,
			nowrap:false,
			rownumbers:false,
			singleSelect:true,
		    pagination : true,
		    checkOnSelect : false,
			selectOnCheck : false,	
		    frozenColumns : [ [ {
				field : 'memberuuid',
				title : '成员ID',
				width : 80,
				checkbox : false,
				align:'left',
		         formatter:function(value, row, index) {
			     	var str = row.memberuuid;
			    	 if($scope.so_player_view)	str= $.formatString('<a ng-show="so_player_view"  href="#" onclick="$(this).scope().playerView(\'{0}\');"  >'+row.memberuuid+'</a>',row.memberuuid);
			    	return str + '';
			    }
				
			}, {
				field : 'showid',
				title : 'showid',
				width : 100,
				align:'left'
			}, {
				field : 'nickname',
				title : '成员昵称',
				width : 120,
				align:'left'
			} ] ],
		     columns:[[
						{
							field : 'memberlevel',
							title : '成员级别',
							width : 60,
							sortable : false,
							formatter:function(value){
								if(value=='1')return '创建者';
								if(value=='2')return '管理员';
								if(value=='3')return '管理员';
								if(value=='4')return '普通成员';
							}
						},{
							field : 'viptype',
							title : 'VIP',
							width : 60,
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
							width : 70,
							sortable : false
						},{
							field : 'depu_coin',
							title : '德扑币',
							width : 70,
							sortable : false
						},{
							field : 'address',
							title : '最近所在地（参考）',
							width : 120,
							sortable : false
						},{
							field : 'ip',
							title : '最近登录ip',
							width : 120,
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
			    	 if($scope.so_player_view)	str= $.formatString('<a ng-show="so_player_view" href="#"  onclick="$(this).scope().playerView(\'{0}\');"  >'+row.uuid+'</a>',row.uuid);
			    	return str + '';
			    }
				
			}, {
				field : 'showid',
				title : 'showid',
				width : 100,
				align:'center'
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
		    url:PATH+'/stat/club/pokerlist?clubid='+$("#clubid").val(),
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
				width : 100,
		         formatter:function(value, row, index) {
			     	var str = row.roomid;
			    	 if($scope.so_poker_view)	str= $.formatString('<a ng-show="so_poker_view"  href="#" onclick="$(this).scope().pokerView(\'{0}\');"  >'+row.roomid+'</a>',row.roomid);
			    	return str + '';
			    }
				
			}, {
				field : 'roomname',
				title : '牌局名词',
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
					{field : 'ctime',title : '结束时间',width : 100,sortable : true},
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
			        {field:'createroomtype', title:'牌局来自',width : 100,
			        	formatter:function(value){
							if(value==1)return '圈子局';
							if(value==2)return '俱乐部局';
							if(value==3)return '快速局';
						}
			        },
			        {field:'leagueid', title:'联盟局',width : 100,
			        	formatter:function(value){
							if(value==0)return '否';
							if(value!=0)return value;
						}
			        },
					{field : 'gtime',title : '牌局时间',width : 100},
					{field : 'hands',title : '总手数',width : 100},
					{field : 'players',title : '玩家数',width : 100}
		     
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

$scope.leagueView = function(id){
	var title = "联盟"+id;
	var url = PATH+'/stat/league/leagueInfo?id='+id;
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

$scope.loadMemberChart=function(){
	//加载成员vip分布
	 $.getJSON(PATH+'/stat/club/memberchart',{
			clubid : $("#clubid").val(),
		},function(data){
        //alert(JSON.stringify(data.series));
		$('#memberchart').highcharts({
			    plotBackgroundColor: null,
		        plotBorderWidth: null,
		        plotShadow: false,
		        title: {
		            text: '成员VIP类型分布图',
		            x: -20 //center
		        },
		        tooltip:{
		        	 pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
		        },
		        plotOptions :{
		            pie: {
		                allowPointSelect: true,
		                cursor: 'pointer',
		                dataLabels: {
		                   enabled: true,
		                   format: '<b>{point.name}%</b>: {point.percentage:.1f} %',
		                   style: {
		                      color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
		                   }
		                }
		             }
		        },

		        //[{name:"Member VIP  share",data:[['蓝卡',50],['黄金卡',8.3],['白金卡',41.7]],type:"pie"}]  正确
		        //[{"name":"Member VIP  share","data":[["蓝卡","50"],["黄金卡","8.3"],["白金卡","41.7"],["黑卡","0"]],"type":"pie"}] 错误，数据类型不对，比如“50”是字符串，所以不起作用
		        series: data.series
		        
		        
		    });
			
			});
}

$scope.loadBlindChart =  function(){

	 $.getJSON(PATH+'/stat/club/blindchart',{
			clubid : $("#clubid").val(),
			dateStart:$("#dateStart").val(),
			dateEnd:$("#dateEnd").val()
		},function(data){

			$('#blindchart').highcharts({
		        title: {
		            text: '俱乐部每天盲注分布图',
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

	 $.getJSON(PATH+'/stat/club/gtimechart',{
			clubid : $("#clubid").val(),
			dateStart:$("#dateStart").val(),
			dateEnd:$("#dateEnd").val()
		},function(data){

			$('#gtimechart').highcharts({
		        title: {
		            text: '俱乐部牌局时长分布图',
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












 

