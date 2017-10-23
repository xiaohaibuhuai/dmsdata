MainApp.controller('UserChartCtrls', [ '$scope', function($scope) {
	
	var initInc = false;

	 $.post(PATH+'/stat/userchart/sum',function(data){
			
		    $('#main').highcharts({
		        title: {
		            text: '每日玩家总数及活跃玩家曲线图',
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
			} ,'json');
	 
	
	
	
	
	 $('#tt').tabs({    
	      onSelect:function(title){   
	         
	          if(title=='玩家注册统计'){
	
	        	  $scope.loadInc();
	          }
	        
			
	          
	      }   
	 });
	 
	 
 $scope.loadInc = function(){
	 if(!initInc){
		 $.post(PATH+'/stat/userchart/inc',function(data){
				
			    $('#inc').highcharts({
			        title: {
			            text: '每日注册玩家曲线图',
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
			    
			    
			    initInc = true;
			    
				} ,'json');
		 
	 }

}












   

} ]);

