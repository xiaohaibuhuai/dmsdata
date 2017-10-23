MainApp.controller('PlayerInfoCtrls',  function($scope,TabService) {
	
	var init =true;
	var initClub = false;
	var initFriend = false;
	var initCommon = false;
	var initSng = false;
	var initTicket = false;
	var ticketDg;
	
	 $('#tt').tabs({    
	      onSelect:function(title){   
	         
	          if(title=='他的俱乐部'){  
	        	  $scope.loadClub();
	          }
	          else if(title=='他的好友'){
	        	  $scope.loadFriend();
	          }
			  else if(title=='他的普通局'){
				  $scope.loadCommon();     	  
			  }
			  else if(title=='他的SNG'){
				  $scope.loadSng();
			  }
			  else if(title=='他的门票'){
				  $scope.loadTicket();
			  }
			
	          
	      }   
	 });
	 
	
$scope.loadClub = function(){
	if(!initClub){
		//$scope.load()

		$("#clubDiv").html($("#base").html());
		$("#clubDiv").append('<table id="clubDG" title="俱乐部列表"  ></table>');
		var  dg = $('#clubDG').datagrid({
		    url:PATH+'/stat/player/clublist?uuid='+$("#uuid").val(),
		    fit:false,
		    fitColumns : false,
			idField : 'clubid',
			striped: true, 
			border : false,
			nowrap:false,
			rownumbers:false,
			singleSelect:true,
		    pagination : false,
		    checkOnSelect : false,
			selectOnCheck : false,	
		    frozenColumns : [ [ {
				field : 'clubid',
				title : '俱乐部ID',
				width : 100,
		         formatter:function(value, row, index) {
			     	var str = row.clubid;
			    	 if($scope.so_club_view)	str= $.formatString('<a ng-show="so_club_view"  href="#" onclick="$(this).scope().clubView(\'{0}\');"  >'+row.clubid+'</a>',row.clubid);
			    	return str + '';
			    }
				
			}, {
				field : 'clubname',
				title : '俱乐部名称',
				width : 150
			} ] ],
		     columns:[[
		        //{field : 'clublocation',title : '俱乐部地点',width : 100},
		        {field : 'createuser',title : '创建者ID',width : 80,
		            formatter:function(value, row, index) {
				     	var str = row.createuser;
				    	 if($scope.so_player_view)	str= $.formatString('<a ng-show="so_player_view" href="#"  onclick="$(this).scope().playerView(\'{0}\');"  >'+row.createuser+'</a>',row.createuser);
				    	return str + '';
				    }
		        },
		        {field : 'nickname',title : '创建者昵称',width : 120},
		        {field : 'showid',title : '创建者showid',width : 120},
		        {field:'fund',title:'基金',width:80},
		        {field:'maxmembers',title:'最大人数',width:80},
		        {field:'curNum',title:'现有人数',width:80}
		     
		      ]],
		    onLoadSuccess : function() {
		    	initClub = true;
				//parent.$.messager.progress('close');

			}
		});
	}
}




$scope.loadFriend = function(){
	if(!initFriend){
		//$scope.load();
		$("#friendDiv").html($("#base").html());
		$("#friendDiv").append('<table id="friendDG"  title="好友列表"   ></table>');
		var  dg = $('#friendDG').datagrid({
		    url:PATH+'/stat/player/friendlist?uuid='+$("#uuid").val(),
		    fit:false,
		    fitColumns : false,
			idField : 'uuid',
			sortName : 'friendtype',
			sortOrder : 'asc',
			striped: true, 
			border : false,
			nowrap:false,
			rownumbers:false,
			singleSelect:true,
		    pagination : true,
			pageSize : 10,
			pageList : [ 10, 20, 30, 40, 50 ],
			
		    checkOnSelect : false,
			selectOnCheck : false,	
		    frozenColumns : [ [ {
				field : 'uuid',
				title : '好友UUID',
				width : 100,
		         formatter:function(value, row, index) {
			     	var str = row.clubid;
			    	 if($scope.so_club_view)	str= $.formatString('<a ng-show="so_player_view"  href="#" onclick="$(this).scope().playerView(\'{0}\');"  >'+row.uuid+'</a>',row.uuid);
			    	return str + '';
			    }
				
			}, {
				field : 'nickname',
				title : '好友昵称',
				width : 150
			}, {
				field : 'showid',
				title : '好友showid',
				width : 150
			} ] ],
		     columns:[[
					{
						field : 'viptype',
						title : 'VIP',
						width : 100,
						formatter:function(value){
							if(value=='BLUE_CARD')return '蓝卡';
							if(value=='GOLD_CARD')return '黄金卡';
							if(value=='BLACK_CARD')return '黑卡';
							if(value=='PLATINUM_CARD')return '白金卡';
						}
					},{
						field : 'gender',
						title : '性别',
						width : 100,
						formatter:function(value){
							if(value=='0')return '女';
							if(value=='1')return '男';
						}
					},
					{
						field : 'diamond',
						title : '钻石',
						width : 100
					},{
						field : 'depu_coin',
						title : '德扑币',
						width : 100
					},{
						field : 'friendtype',
						title : '好友标志',
						width : 100,
						sortable : true
					}
					/*,
					{
						field : 'android_diamond',
						title : '钻石（安卓）',
						width : 100
					},{
						field : 'android_depu_coin',
						title : '德扑币（安卓）',
						width : 100
					}*/
		      ]],
		    onLoadSuccess : function() {
		    	initFriend = true;
				//parent.$.messager.progress('close');

			}
		});
		
	}
}

$scope.loadCommon = function(){
	if(!initCommon){
		//$scope.load();

		var  dg = $('#commonDG').datagrid({
		    url:PATH+'/stat/player/commonlist?uuid='+$("#uuid").val(),
		    fit:false,
		    fitColumns : false,
			idField : 'roomid',
			sortName : 'ctime',
			sortOrder : 'desc',
			striped: true, 
			border : false,
			nowrap:false,
			rownumbers:false,
			singleSelect:true,
		    pagination : true,
			pageSize : 10,
			pageList : [ 10, 20, 30, 40, 50 ],
		    checkOnSelect : false,
			selectOnCheck : false,	
		    frozenColumns : [ [ {
				field : 'roomid',
				title : '牌局ID',
				width : 100,
				checkbox : false,
		         formatter:function(value, row, index) {
			     	var str = row.roomid;
			    	 if($scope.so_poker_view)	str= $.formatString('<a ng-show="so_poker_view" href="#"  onclick="$(this).scope().pokerView(\'{0}\');"  >'+row.roomid+'</a>',row.roomid);
			    	return str + '';
			    }
				
			}, {
				field : 'roomname',
				title : '牌局名词',
				width : 150
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
			        {field : 'nickname',title : '创建者昵称',width : 120},
					{field : 'ctime',title : '结束时间',width : 100},
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
					{field : 'players',title : '玩家数',width : 100},
					{field : 'buystacks',title : '买入',width : 100},
					{field : 'bonus',title : '盈亏',width : 100}
		     
		      ]],
		    onLoadSuccess : function() {
		    	

			}
		});
		//加载统计
		$scope.countCommon();

		//加载玩家每日盈亏图
		$scope.loadBonusChart();
		
		//加载玩家每日牌局类型图
		$scope.loadPokerChart();
		
		initCommon = true;
	    //parent.$.messager.progress('close');
	}
	
}

$scope.loadSng = function(){
	if(!initSng){
		$("#sngDiv").html($("#base").html());
		initSng = true;
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

$scope.searchFun=function() {
	$scope.load();
	$('#commonDG').datagrid('load', $.serializeObject($('#searchForm')));
	$scope.countCommon();
	$scope.loadBonusChart();
	$scope.loadPokerChart();
	
};
$scope.cleanFun=function() {
	$scope.load();
	$('#searchForm input').val('');
	$('#commonDG').datagrid('load', {});
	$scope.countCommon();
	$scope.loadBonusChart();
	$scope.loadPokerChart();
	
};
$scope.countCommon = function(){
	$.getJSON(PATH+'/stat/player/countCommon', {
		uuid : $("#uuid").val(),
		dateStart:$("#dateStart").val(),
		dateEnd:$("#dateEnd").val()
	}, 
	function(result) {
		if (result.code==200) {
			 $("#totalBonus").html(result.obj.totalBonus);
			 $("#totalFast").html(result.obj.totalFast);
			 $("#totalGroup").html(result.obj.totalGroup);
			 $("#totalClub").html(result.obj.totalClub);
			 parent.$.messager.progress('close');
		}else{
			$.messager.alert('提示',result.msg);
		}
	});
}
$scope.loadBonusChart=function(){
	//加载玩家月盈亏图标
	 $.getJSON(PATH+'/stat/player/bonuschart',{
			uuid : $("#uuid").val(),
			dateStart:$("#dateStart").val(),
			dateEnd:$("#dateEnd").val()
		},function(data){

		$('#bonuschart').highcharts({
		        title: {
		            text: '玩家每日盈亏曲线图',
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

$scope.loadPokerChart =  function(){

	 $.getJSON(PATH+'/stat/player/pokerchart',{
			uuid : $("#uuid").val(),
			dateStart:$("#dateStart").val(),
			dateEnd:$("#dateEnd").val()
		},function(data){

			$('#pokerchart').highcharts({
		        title: {
		            text: '玩家每日牌局类型对比图',
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
    var msg = '确认关注此用户？';
	parent.$.messager.confirm('询问', msg, function(b) {
		if (b) {
			   parent.$.messager.progress({
				  title : '提示',
				  text : '数据处理中，请稍后....'
			    });
				$.post(PATH+'/stat/playertrace/start', {
					id : id
				}, function(result) {
					window.location.reload();
					parent.$.messager.progress('close');
				}, 'JSON');			
		}
	});		
};

$scope.end=function(id){
	var msg = '确认取消关注此用户？';
	parent.$.messager.confirm('询问', msg, function(b) {
		if (b) {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				$.post(PATH+'/stat/playertrace/end', {
					id : id
				}, function(result) {
					window.location.reload();
					parent.$.messager.progress('close');
				}, 'JSON');			
		}
	});		
};


$scope.loadTicket = function(){
	if(!initTicket){
		$("#ticketDiv").html($("#base").html());
		$("#ticketDiv").append('<table id="ticketDG"  title="门票列表"   ></table>');
		ticketDg = $('#ticketDG').datagrid({
		    url:PATH+'/stat/player/ticketlist?uuid='+$("#uuid").val(),
		    fit:false,
		    fitColumns : false,
			idField : 'typeid',
			striped: true, 
			border : false,
			nowrap:false,
			rownumbers:false,
			singleSelect:true,
		    pagination : true,
			pageSize : 10,
			pageList : [ 10, 20, 30, 40, 50 ],
			
		    checkOnSelect : false,
			selectOnCheck : false,	
		    frozenColumns : [ [
				    {
						field : 'typeid',
						title : '门票ID',
						width : 80
					}, {
						field : 'zhname',
						title : '中文名称',
						width : 250
					} ] ],
		     columns:[[
                    {field : 'belonguuid',title :'uuid',width : 80,hidden:'true'},
                    {field : 'remainnum',title :'剩余数量',width : 80},
                    {field : 'remark',title : '门票注释',width : 150},
                    {field : 'prizepoolfee',title : '计入奖池金额',width : 100},
    		        {field : 'servicefee',title : '服务费',width : 100},
    	   	        {field : 'circulationnums', title:'门票类型',width : 100,
    		        	formatter:function(value){
    						if(value==1)return '通用门票';
    						if(value==0)return '特殊门票';
    					}
    		        },
					{field:'expirationtime', title:'有效期',width : 120,
		        	    formatter:function(value){
							if(value==0)return '无';
							if(value!=0)return value;
					     }
		            },
		            {field:'action',title:'操作',width:100,
			              formatter:function(value, row, index) {
				     		     var str = '';
				     		     if($scope.so_delTicket) str += $.formatString('<img ng-show="so_export" style="float:left;margin-left: 4px;" onclick="$(this).scope().delTicketFun(\'{0}\');" src="{1}" title="删除门票"/>',row.typeid,PATH+'/static/js/ext/style/images/extjs_icons/cancel.png');
				    	         return str + '';
				    	     }
				    }
		      ]],
		    onLoadSuccess : function() {
		    	initTicket = true;
			}
		});
	}
}


//删除门票
$scope.delTicketFun=function(id) {
	if (id != undefined)ticketDg.datagrid('selectRecord', id);
	var node = ticketDg.datagrid('getSelected');
	if (node) {
		$('#rfm').form('clear');
		loadFrom('#rfm',node);
	  	$('#typeid').val(node.typeid);
	  	$('#zhname').val(node.zhname);
	  	$('#belonguuid').val(node.belonguuid);
	  	$('#remainnum').numberbox('setValue',node.remainnum);
		showDialog('#ddlg','删除门票');
		url=PATH+'/race/giveticket/del';
	}
};

$scope.setpwdSubmit=function(rfm,ddlg){
	if(!$("#rfm").form('validate')) return;
	if($('#delNum').numberbox('getValue') > $('#remainnum').numberbox('getValue')){
		alert('删除数量不能大于当前门票数量');
		return;
	}
	var msg = "将删除该被封玩家该类门票，请确认！";
	$.messager.confirm("提示", msg, function(flag) {
		if(flag){
			$scope.load();
			$(rfm).form('submit',{
                url: url,
                onSubmit: function ()
                 { },
                 success: function(result){
                	 data= $.parseJSON(result);
					 parent.$.messager.progress('close');
					 if (data.state == 0) {
						$.messager.alert('提示','删除成功');
					 } else {
						if(data.state == -1){
							$.messager.alert('提示','请检查门票类型和备注');
						}else if(data.state == -3){
							$.messager.alert('提示','服务器异常');
						}else if(data.state == -4){
							$.messager.alert('提示','请检查UUID合法性且删除门票数量最小值为1');
						}
					 }
	                 $(ddlg).dialog('close');
	                 ticketDg.datagrid('reload');
                 }
            });		
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