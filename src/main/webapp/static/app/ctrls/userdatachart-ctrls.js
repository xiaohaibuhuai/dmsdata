MainApp.controller('UserDataChartCtrls', function($scope,TabService) {
	$scope.init = function() {
		$(dateStart).val(getYesterdayFormatDate());
		$scope.searchFun();
	}
	$scope.searchFun=function() {
		parent.$.messager.progress('close');
		$.post(PATH+'/stat/userdatachart/sum',$(searchForm).serialize(),function(getdata){
			    var colors = Highcharts.getOptions().colors;
			    categories = getdata.categories;
			    data = getdata.data;
		        browserData = [],
		        versionsData = [];
		        var i;
		        var j;
		        dataLen = data.length;
		        var drillDataLen;
		        var brightness;
		        var datePara = getdata.datePara;
			    var totalNum = getdata.totalNum;
			    var countryNum = getdata.countryNum;
		        

		        // Build the data arrays
		    for (i = 0; i < dataLen; i += 1) {
		        // add browser data
		        browserData.push({
		            name: categories[i],
		            y: data[i].y,
		            color: colors[data[i].color]
		        });

		        // add version data
		        var drillDataLen = data[i].drilldown.data.length;
		        for (j = 0; j < drillDataLen; j += 1) {
		            brightness = 0.2 - (j / drillDataLen) / 5;
		            versionsData.push({
		                name: data[i].drilldown.categories[j],
		                y: data[i].drilldown.data[j],
		                color: Highcharts.Color(colors[data[i].color]).brighten(brightness).get()
		            });
		        }
		    }

		    // Create the chart
		    Highcharts.chart('container', {
		        chart: {
		            type: 'pie'
		        },
		        title: {
		            text: datePara+'玩家地区分析'
		        },
		        subtitle: {
		            text: '在线总人数'+ totalNum+"/条件内总人数"+countryNum
		        },
		        yAxis: {
		            title: {
		                text: 'Total percent market share'
		            }
		        },
		        plotOptions: {
		            pie: {
		                shadow: false,
		                center: ['50%', '50%']
		            }
		        },
		        tooltip: {
		            valueSuffix: ' '
		        },
		        series: [{
		            name: '省份',
		            data: browserData,
		            size: '60%',
		            dataLabels: {
		                formatter: function () {
		                    return this.y > 0 ? this.point.name : null;
		                },
		                color: '#ffffff',
		                distance: -30
		            }
		        }, {
		            name: '城市',
		            data: versionsData,
		            size: '80%',
		            innerSize: '60%',
		            dataLabels: {
		                formatter: function () {
		                    // display only if larger than 1
		                    return this.y > 1 ? '<b>' + this.point.name + ':</b> ' + this.y + ' ' : null;
		                }
		            }
		        }]
		    });
		 } ,'json');
	};
	$scope.cleanFun=function() {
		$('#searchForm input').val('');
		document.getElementById("container").innerHTML = "";
	};
	// 获取昨天时间，格式YYYY-MM-DD
    function getYesterdayFormatDate() {
    	var newdate = new Date();
    	var newtimems = newdate.getTime()-(24*60*60*1000);
    	var date = new Date(newtimems);
        
        var seperator1 = "-";
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = year + seperator1 + month + seperator1 + strDate;
        return currentdate;
    }
    
	 $('#init').tabs({
	      onSelect:function(title){
	          if(title=='省份地区人数统计'){
	        	  $scope.loadProvinceTrend();
	          }else if(title=='城市地区人数统计'){
	        	  $scope.loadCityTrend();
	          }
	      }   
	 });
    
    $scope.loadProvinceTrend = function(){
            $.post(PATH+'/stat/userdatachart/provinceTrend',function(data){
				    $('#provinceTrend').highcharts({
				        title: {
				            text: '省份地区在线人数趋势图',
				            x: -20 // center
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
    }
    
    $scope.loadCityTrend = function(){
        $.post(PATH+'/stat/userdatachart/cityTrend',function(data){
			    $('#cityTrend').highcharts({
			        title: {
			            text: '城市地区在线人数趋势图',
			            x: -20 // center
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
}
    
    
    
});

