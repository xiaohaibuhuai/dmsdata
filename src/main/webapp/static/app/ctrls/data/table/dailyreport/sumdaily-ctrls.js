MainApp.controller('SumDailyCtrls',  function($scope,TabService) {
    parent.$.messager.progress('close');
    
    $.ajax({
		    url : PATH+'/data/table/dailyreport/sumdaily/chart',
		    method : 'POST',
		    dataType : "json",
		  //  data:"dateStart="+dateStart+"&dateEnd="+dateEnd+"&type="+type,
		    beforeSend : function() {
		    },
		    success : function(data) {
		    	if(data=="0"){
  		    		alert("搜索无结果");
  		    		return;
  		    	}
		    	
		    	//1 昨日钻石总增加
		    	 $('#container1').highcharts({
		    	        chart: {
		    	            type: 'pie'
		    	        },
		    	        title: {
		    	            text:"昨日钻石总增加:"+data[0].desc
		    	        },
		    	        subtitle: {
		    	            text: ''
		    	        },
		    	        plotOptions: {
		    	            series: {
		    	                dataLabels: {
		    	                    enabled: true,
		    	                    format: '{point.name}: {point.percentage:.1f}%'
		    	                }
		    	            }
		    	        },
		    	        tooltip: {
		    	            headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
		    	            pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y} ({point.percentage:.2f}%)</b> of total<br/>'
		    	        },
		    	        series: data[0].series,
		    	        drilldown: {
		    	            series:data[0].drilldown
		    	        }
		    	    });
	    	 
//		    	 //2 昨日钻石总消耗
		    	 $('#container2').highcharts({
		    	        chart: {
		    	            type: 'pie'
		    	        },
		    	        title: {
		    	            text:"昨日钻石总消耗:"+data[1].desc
		    	        },
		    	        subtitle: {
		    	            text: ''
		    	        },
		    	        plotOptions: {
		    	            series: {
		    	                dataLabels: {
		    	                    enabled: true,
		    	                    format: '{point.name}: {point.percentage:.1f}%'
		    	                }
		    	            }
		    	        },
		    	        tooltip: {
		    	            headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
		    	            pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y} ({point.percentage:.2f}%)</b> of total<br/>'
		    	        },
		    	        series: data[1].series,
		    	        drilldown: {
		    	            series:data[1].drilldown
		    	        }
		    	    });
//		    	 //3 昨日德扑币总增加
//		    	 $('#container3').highcharts({
//		    	        chart: {
//		    	            type: 'pie'
//		    	        },
//		    	        title: {
//		    	            text:"昨日德扑币总增加:"+data[2].desc
//		    	        },
//		    	        subtitle: {
//		    	            text: '单击每个浏览器品牌不同版本的具体信息，数据来源: netmarketshare.com.'
//		    	        },
//		    	        plotOptions: {
//		    	            series: {
//		    	                dataLabels: {
//		    	                    enabled: true,
//		    	                    format: '{point.name}: {point.percentage:.1f}%'
//		    	                }
//		    	            }
//		    	        },
//		    	        tooltip: {
//		    	            headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
//		    	            pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y} ({point.percentage:.2f}%)</b> of total<br/>'
//		    	        },
//		    	        series: data[2].series,
//		    	        drilldown: {
//		    	            series:data[2].drilldown
//		    	        }
//		    	    });
		    	 
//		    	//4 昨日德扑币总消耗
		    	 $('#container4').highcharts({
		    	        chart: {
		    	            type: 'pie'
		    	        },
		    	        title: {
		    	            text:"昨日德扑币总消耗:"+data[3].desc
		    	        },
		    	        subtitle: {
		    	            text: ''
		    	        },
		    	        plotOptions: {
		    	            series: {
		    	                dataLabels: {
		    	                    enabled: true,
		    	                    format: '{point.name}: {point.percentage:.1f}%'
		    	                }
		    	            }
		    	        },
		    	        tooltip: {
		    	            headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
		    	            pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y} ({point.percentage:.2f}%)</b> of total<br/>'
		    	        },
		    	        series: data[3].series,
		    	        drilldown: {
		    	            series:data[3].drilldown
		    	        }
		    	    });
		    	 
		    	 
		    	 
		    		//5 昨日开局数
		    	 $('#container5').highcharts({
		    	        chart: {
		    	            type: 'pie'
		    	        },
		    	        title: {
		    	            text:"昨日开局数:"+data[4].desc
		    	        },
		    	        subtitle: {
		    	            text: ''
		    	        },
		    	        plotOptions: {
		    	            series: {
		    	                dataLabels: {
		    	                    enabled: true,
		    	                    format: '{point.name}: {point.percentage:.1f}%'
		    	                }
		    	            }
		    	        },
		    	        tooltip: {
		    	            headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
		    	            pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y} ({point.percentage:.2f}%)</b> of total<br/>'
		    	        },
		    	        series: data[4].series,
		    	        drilldown: {
		    	            series:data[4].drilldown
		    	        }
		    	    });
		    	 
		    	
	    		 //6 活跃人数
		    	 $('#container6').highcharts({
		    	        chart: {
		    	            type: 'pie'
		    	        },
		    	        title: {
		    	            text:"昨日活跃人数:"+data[5].desc
		    	        },
		    	        subtitle: {
		    	            text: ''
		    	        },
		    	        plotOptions: {
		    	            series: {
		    	                dataLabels: {
		    	                    enabled: true,
		    	                    format: '{point.name}: {point.percentage:.1f}%'
		    	                }
		    	            }
		    	        },
		    	        tooltip: {
		    	            headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
		    	            pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y} ({point.percentage:.2f}%)</b> of total<br/>'
		    	        },
		    	        series: data[5].series,
		    	        drilldown: {
		    	            series:data[5].drilldown
		    	        }
		    	    });
		    	    
		    	 //7 服务费
		    	 $('#container7').highcharts({
		    	        chart: {
		    	            type: 'pie'
		    	        },
		    	        title: {
		    	            text:"昨日总服务费:"+data[6].desc
		    	        },
		    	        subtitle: {
		    	            text: ''
		    	        },
		    	        plotOptions: {
		    	            series: {
		    	                dataLabels: {
		    	                    enabled: true,
		    	                    format: '{point.name}: {point.percentage:.1f}%'
		    	                }
		    	            }
		    	        },
		    	        tooltip: {
		    	            headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
		    	            pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y} ({point.percentage:.2f}%)</b> of total<br/>'
		    	        },
		    	        series: data[6].series,
		    	        drilldown: {
		    	            series:data[6].drilldown
		    	        }
		    	    });
		    	 //8 总手数
		    	 $('#container8').highcharts({
		    	        chart: {
		    	            type: 'pie'
		    	        },
		    	        title: {
		    	            text:"昨日总手数:"+data[7].desc
		    	        },
		    	        subtitle: {
		    	            text: ''
		    	        },
		    	        plotOptions: {
		    	            series: {
		    	                dataLabels: {
		    	                    enabled: true,
		    	                    format: '{point.name}: {point.percentage:.1f}%'
		    	                }
		    	            }
		    	        },
		    	        tooltip: {
		    	            headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
		    	            pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y} ({point.percentage:.2f}%)</b> of total<br/>'
		    	        },
		    	        series: data[7].series,
		    	        drilldown: {
		    	            series:data[7].drilldown
		    	        }
		    	    });	
		    }
    })
    
    
   
           
	

});

