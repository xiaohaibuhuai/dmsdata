MainApp.controller('DiamondChartCtrls', function($scope,TabService) {
	
	var initInc = false;

//	   初始化图表
	 $.post(PATH+'/data/diamondchart/diamondChange',function(data){
			
		  $('#main').highcharts({
			  chart: {
		            type: 'column'
		        },
		        title: {
		            text: '24小时钻石变化对比图',
		 
		        },
		        credits : {
					enabled : false
				},
		        xAxis: {
		            categories: data.categories
		        },
//		        plotOptions: {
//		        	    column: {
//						dataLabels: {
//							enabled: true
//						},
//						enableMouseTracking: true
//					}
//				},
		        
		        series: data.series
		    });
		    parent.$.messager.progress('close');
			} ,'json');
	 
	 $.post(PATH+'/data/diamondchart/recharge',function(data){
			
		  $('#main2').highcharts({
			  chart: {
		            type: 'column'
		        },
		        title: {
		            text: '24小时各渠道充值对比图'
		        },
		        xAxis: {
		          	categories: data.categories
		        },
		        yAxis: {
		            min: 0,
		            title: {
		                text: '充值数(元)'
		            },
		            stackLabels: {
		                enabled: true,
		                style: {
		                    fontWeight: 'bold',
		                    color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
		                }
		            }
		        },
		        legend: {
		            align: 'right',
		            x: -30,
		            verticalAlign: 'top',
		            y: 25,
		            floating: true,
		            backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
		            borderColor: '#CCC',
		            borderWidth: 1,
		            shadow: false
		        },
		        tooltip: {
		            formatter: function () {
		                return '<b>' + this.x + '</b><br/>' +
		                    this.series.name + ': ' + this.y + '<br/>' +
		                    '总量: ' + this.point.stackTotal;
		            }
		        },
		        plotOptions: {
		            column: {
		                stacking: 'normal',
		                dataLabels: {
		                    enabled: true,
		                    color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',
		                    style: {
		                        textShadow: '0 0 3px black'
		                    }
		                }
		            }
		        },
		        series: data.series
		    });
		    parent.$.messager.progress('close');
			} ,'json');
	
	
	
//初始化数据表格
	 var  dg =$('#dg').datagrid({
			url : PATH+'/data/coinchart/increaseRank',
			fit : false,
			title:"24小时钻石增加排名",
		//	fitColumns : true,
			border : true,
			singleSelect : true,
		    columns:[[
		    	    {field:'rank',title:'排名',width:50,align:'left'},
		        {field : 'uuid',title : '创建者ID',width : 100,align:'left',
		            formatter:function(value, row, index) {
				     	var str = row.uuid;
				    	 if($scope.so_player_view)	str= $.formatString('<a ng-show="so_player_view" href="#"  onclick="$(this).scope().playerView(\'{0}\');"  >'+row.uuid+'</a>',row.uuid);
				    	return str + '';
				    }
		        },
		        {field:'showid',title:'showId',width:100,align:'left'},
		        {field:'nickname',title:'昵称',width:100,align:'left'},
		        {field:'change',title:'钻石增加',width:100,align:'left'}
		      ]],
		});
	 
	 var  dg2 =$('#dg2').datagrid({
			url : PATH+'/data/coinchart/reduceRank',
			fit : false,
			title:"24小时钻石增加排名",
		//	fitColumns : true,
			border : true,
			singleSelect : true,
		    columns:[[
		    	    {field:'rank',title:'排名',width:50,align:'left'},
		        {field : 'uuid',title : '创建者ID',width : 100,align:'left',
		            formatter:function(value, row, index) {
				     	var str = row.uuid;
				    	 if($scope.so_player_view)	str= $.formatString('<a ng-show="so_player_view" href="#"  onclick="$(this).scope().playerView(\'{0}\');"  >'+row.uuid+'</a>',row.uuid);
				    	return str + '';
				    }
		        },
		        {field:'showid',title:'showId',width:100,align:'left'},
		        {field:'nickname',title:'昵称',width:100,align:'left'},
		        {field:'change',title:'钻石减少',width:100,align:'left'}
		      ]],
		});

	 var  dg3 =$('#dg3').datagrid({
			url : PATH+'/data/coinchart/increaseRank',
			fit : false,
			title:"24小时充值次数排名",
		//	fitColumns : true,
			border : true,
			singleSelect : true,
		    columns:[[
		    	    {field:'rank',title:'排名',width:50,align:'left'},
		        {field : 'uuid',title : '创建者ID',width : 100,align:'left',
		            formatter:function(value, row, index) {
				     	var str = row.uuid;
				    	 if($scope.so_player_view)	str= $.formatString('<a ng-show="so_player_view" href="#"  onclick="$(this).scope().playerView(\'{0}\');"  >'+row.uuid+'</a>',row.uuid);
				    	return str + '';
				    }
		        },
		        {field:'showid',title:'showId',width:100,align:'left'},
		        {field:'nickname',title:'昵称',width:100,align:'left'},
		        {field:'change',title:'充值次数',width:100,align:'left'}
		      ]],
		}); 
	 
	 var  dg4 =$('#dg4').datagrid({
			url : PATH+'/data/coinchart/increaseRank',
			fit : false,
			title:"24小时充值额度排名",
		//	fitColumns : true,
			border : true,
			singleSelect : true,
		    columns:[[
		    	    {field:'rank',title:'排名',width:50,align:'left'},
		        {field : 'uuid',title : '创建者ID',width : 100,align:'left',
		            formatter:function(value, row, index) {
				     	var str = row.uuid;
				    	 if($scope.so_player_view)	str= $.formatString('<a ng-show="so_player_view" href="#"  onclick="$(this).scope().playerView(\'{0}\');"  >'+row.uuid+'</a>',row.uuid);
				    	return str + '';
				    }
		        },
		        {field:'showid',title:'showId',width:100,align:'left'},
		        {field:'nickname',title:'昵称',width:100,align:'left'},
		        {field:'change',title:'钻石增加',width:100,align:'left'}
		      ]],
		});




	 $scope.playerView = function(id){
			var title = "玩家"+id;
			var url = PATH+'/stat/player/playerInfo?id='+id;
			TabService.addTab(title,url);
		}





   

} );

