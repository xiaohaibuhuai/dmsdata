MainApp.controller('ClubChartCtrls', [ '$scope', function($scope) {
	
	var initInc = false;

	 $.post(PATH+'/stat/clubchart/sum',function(data){
			
		    $('#main').highcharts({
		        title: {
		            text: '每日俱乐部总数及活跃俱乐部曲线图',
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
	         
	          if(title=='俱乐部增量统计'){
	
	        	  $scope.loadInc();
	          }
	        
			
	          
	      }   
	 });
	 
	 
 $scope.loadInc = function(){
	 if(!initInc){
		 $.post(PATH+'/stat/clubchart/inc',function(data){
				
			    $('#inc').highcharts({
			        title: {
			            text: '每日新增俱乐部曲线图',
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

