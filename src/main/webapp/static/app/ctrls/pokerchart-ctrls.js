MainApp.controller('PokerChartCtrls', [ '$scope', function($scope) {
	
	 $.post(PATH+'/stat/pokerchart/common',function(data){
			
		    $('#commonChart').highcharts({
		        title: {
		            text: '近30天每日牌局总数及牌局类型分布曲线图',
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

	 









} ]);












 

