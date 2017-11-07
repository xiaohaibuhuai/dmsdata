MainApp.controller('CoinChartCtrls',  function($scope,TabService) {
	
	var initInc = false;
	var  dg =$('#dg2').datagrid({
		url : PATH+'/data/coinchart/reduceRank',
		fit : false,
		title:"24小时德扑币减少排名",
		border : true,
		singleSelect : true,
	    columns:[[
	    	    {field:'rank',title:'排名',width:100,align:'left'},
	        {field : 'uuid',title : '创建者ID',width : 130,align:'left',
	            formatter:function(value, row, index) {
			     	var str = row.uuid;
			    	 if($scope.so_player_view)	str= $.formatString('<a ng-show="so_player_view" href="#"  onclick="$(this).scope().playerView(\'{0}\');"  >'+row.uuid+'</a>',row.uuid);
			    	return str + '';
			    }
	        },
	        {field:'showid',title:'showId',width:110,align:'left'},
	        {field:'nickname',title:'昵称',width:110,align:'left'},
	        {field:'change',title:'德扑币减少',width:150,align:'left'}
	      ]],
	});
	
	
	var  dg =$('#dg1').datagrid({
		url : PATH+'/data/coinchart/increaseRank',
		fit : false,
		title:"24小时德扑币减少排名",
		border : true,
		singleSelect : true,
	     columns:[[
	    	    {field:'rank',title:'排名',width:100,align:'left'},
	        {field : 'uuid',title : '创建者ID',width : 130,align:'left',
	            formatter:function(value, row, index) {
			     	var str = row.uuid;
			    	 if($scope.so_player_view)	str= $.formatString('<a ng-show="so_player_view" href="#"  onclick="$(this).scope().playerView(\'{0}\');"  >'+row.uuid+'</a>',row.uuid);
			    	return str + '';
			    }
	        },
	        {field:'showid',title:'showId',width:110,align:'left'},
	        {field:'nickname',title:'昵称',width:110,align:'left'},
	        {field:'change',title:'德扑币减少',width:150,align:'left'}
	      ]],
	});
	
	$scope.playerView = function(id){
		var title = "玩家"+id;
		var url = PATH+'/stat/player/playerInfo?id='+id;
		TabService.addTab(title,url);
	}
	
	
	
	 $.post(PATH+'/data/coinchart/sum',function(data){
			
		  $('#main').highcharts({
			  chart: {
		            type: 'column'
		        },
		        title: {
		            text: '24小时德州币变化对比图',
		 
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
	
	
//	 $('#tt').tabs({    
//	      onSelect:function(title){   
//	         
//	          if(title=='俱乐部增量统计'){
//	
//	        	  $scope.loadInc();
//	          }
//	        
//			
//	          
//	      }   
//	 });
	 
	 
// $scope.loadInc = function(){
//	 if(!initInc){
//		 $.post(PATH+'/stat/clubchart/inc',function(data){
//				
//			    $('#inc').highcharts({
//			        title: {
//			            text: '每日新增俱乐部曲线图',
//			            x: -20 //center
//			        },
//			        credits : {
//						enabled : false
//					},
//			        xAxis: {
//			            categories: data.categories
//			        },
//			        yAxis: {
//			            plotLines: [{
//			                value: 0,
//			                width: 1,
//			                color: '#808080'
//			            }]
//			        },
//			        legend: {
//			            layout: 'vertical',
//			            align: 'right',
//			            verticalAlign: 'middle',
//			            borderWidth: 0
//			        },
//			        series: data.series
//			    });
//			    
//			    
//			    initInc = true;
//			    
//				} ,'json');
//		 
//	 }

//}












   

});

