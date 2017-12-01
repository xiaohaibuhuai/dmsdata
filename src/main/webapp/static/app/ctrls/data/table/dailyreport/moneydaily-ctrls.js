MainApp.controller('MoneyDailyCtrls',  function($scope,TabService) {
    parent.$.messager.progress('close');
	
    
    
    
    $("#btn").click(function(){
    	  alert("ss");
    	  
    	  var dateStart=$("#dateStart").val();
    	  var dateEnd=$("#dateEnd").val();
    	  var type=$("#type").val();
    	  if(dateStart==""||dateEnd==""){
    		  alert("请选择时间");
    		  return;
    	  }else{
    		  var start = new Date(dateStart.replace("-", "/").replace("-", "/")); 
    		  var end= new Date(dateEnd.replace("-", "/").replace("-", "/")); 
    		  if(end<start){  
    			  alert("开始时间大于结束时间")
    		        return; 
    		    }  
    	  }
    	  
    	  //1 
    	  $.ajax({
  		    url : PATH+'/data/table/dailyreport/moneydaily/moneyChart',
  		    method : 'POST',
  		    dataType : "json",
  		    data:"dateStart="+dateStart+"&dateEnd="+dateEnd+"&type="+type,
  		    beforeSend : function() {
  		    },
  		    success : function(data) {
  		    	if(data=="0"){
  		    		alert("搜索无结果");
  		    		return;
  		    	}
  		    	
  		    	//设置隐藏域值
  		    	$("#h_dateStart").val(dateStart)
  		    	$("#h_dateEnd").val(dateEnd)
  		    $("#h_type").val(type)
  		    
  		    	$('#chart1').highcharts({
      			  chart: {
      		            type: 'column'
      		        },
      		        title: {
      		            text: '德扑币消耗堆叠图'
      		        },
      		        xAxis: {
      		          	categories: data.categories
      		        },
      		        yAxis: {
      		          //  min: 0,
      		            title: {
      		                text: '门票存量(张)'
      		            },
      		            stackLabels: {
      		                enabled: false,
      		                style: {
      		                    fontWeight: 'bold',
      		                    color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
      		                }
      		            }
      		        },
      		      legend: {
  		            align: 'right',
  		            x: +8,
  		            verticalAlign: 'top',
  		            y: 25,
  		            floating: true,
  		            backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
  		            borderColor: '#CCC',
  		            borderWidth: 1,
  		            itemStyle : {
  		                'fontSize' : '11px'
  		            },
  		            shadow: false
  		        },
      		        tooltip: {
      		          formatter: function () {
  		                return '<b>' + this.x + '</b><br/>' +
  		                    this.series.name + ': ' + this.y +'('+Highcharts.numberFormat(this.percentage,2)+"%"+')'+ '<br/>' +
  		                    '总量: ' + this.point.stackTotal;
  		            }
      		        },
      		        plotOptions: {
      		            column: {
      		                stacking: 'normal',
      		                dataLabels: {
      		                    enabled: false,
      		                    color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white',
      		                    style: {
      		                        textShadow: '0 0 3px black'
      		                    }
      		                }
      		            }
      		        },
      		        series: data.series
      		    });
  		    },
  		    error : function() {
  		      $("#loadModal").css("display", "none");
  		      alert("服务出错请联系管理员");
  		    }
  		  })
    	  
    	  
    	  
    	 
    	  
    	  
    	  //2
    	  $.post(PATH+'/data/table/dailyreport/moneydaily/diamondChart?'+"dateStart="+dateStart+"&dateEnd="+dateEnd+"&type="+type,function(data){
    			
    		  $('#chart2').highcharts({
    			  chart: {
    		            type: 'column'
    		        },
    		        title: {
    		            text: '钻石消耗堆叠图'
    		        },
    		        xAxis: {
    		          	categories: data.categories
    		        },
    		        yAxis: {
    		            //min: -33,
    		            title: {
    		                text: '门票存量(张)'
    		            },
    		            stackLabels: {
    		                enabled: false,
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
      		                    this.series.name + ': ' + this.y +'('+Highcharts.numberFormat(this.percentage,2)+"%"+')'+ '<br/>' +
      		                    '总量: ' + this.point.stackTotal;
      		            }
    		        },
    		        plotOptions: {
    		            column: {
    		                stacking: 'normal',
    		                dataLabels: {
    		                    enabled: false,
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
    	  
    	  
    	  
    	  //数据表格
    	  var  dg =$('#dg1').datagrid({
    			url : PATH+'/data/table/dailyreport/moneydaily/moneyDataGrid?'+"dateStart="+dateStart+"&dateEnd="+dateEnd+"&type="+type,
    			fit : false,
    			title:"每日德普币消耗汇总表",
    			border : true,
    			singleSelect : true,
    		    columns:[[
    		    	    {field:'targetdate',title:'日期',width:100,align:'left',formatter:function(val,rec){
    	                return jsonYearMonthDay(val);
    	            }},
    		        {field : 'service',title : '服务费',width : 120,align:'left',},
    		        {field:'Interprops',title:'互动道具',width:120,align:'left'},
    		        {field:'magic',title:'魔法表情',width:120,align:'left'},
    		        {field:'lookover',title:'翻翻看',width:120,align:'left'},
    		        {field:'barrage',title:'弹幕',width:120,align:'left'},
    		        {field:'pokermachine',title:'扑克机',width:120,align:'left'},
    		        {field:'caribbean',title:'加勒比',width:120,align:'left'},
    		        {field:'cow',title:'牛牛_一粒大米',width:120,align:'left'},
    		        {field:'eight',title:'八八碰_一粒大米',width:120,align:'left'},
    		        {field:'rewardpoker',title:'打赏牌谱',width:120,align:'left'},
    		        {field:'mttsign',title:'德扑币报名MTT',width:120,align:'left'},
    		        {field:'sum',title:'汇总',width:120,align:'left'}
    		      ]],
    		});
    	    
    	  //数据表格
    	    var  dg =$('#dg2').datagrid({
    			url : PATH+'/data/table/dailyreport/moneydaily/diamondDataGrid?'+"dateStart="+dateStart+"&dateEnd="+dateEnd+"&type="+type,
    			fit : false,
    			title:"每日钻石消耗汇总表",
    			border : true,
    			singleSelect : true,
    		    columns:[[
    		    	    {field:'targetdate',title:'日期',width:100,align:'left',formatter:function(val,rec){
    	                return jsonYearMonthDay(val);
    	            }},
    		        {field : 'alliance',title : '联盟局',width : 120,align:'left',},
    		        {field:'changename',title:'修改昵称',width:120,align:'left'},
    		        {field:'delayprops',title:'延时道具',width:120,align:'left'},
    		        {field:'ticketmtt',title:'MTT门票',width:120,align:'left'},
    		        {field:'clubpush',title:'俱乐部推送',width:120,align:'left'},
    		        {field:'changecname',title:'俱乐部改名',width:120,align:'left'},
    		        {field:'sum',title:'汇总',width:120,align:'left'}
    		      ]],
    		});
    })
    
    
    
    
    
    
    function jsonYearMonthDay(milliseconds) {
	    var datetime = new Date();
	    datetime.setTime(milliseconds);
	    var year = datetime.getFullYear();
	    var month = datetime.getMonth() + 1 < 10 ? "0"
	            + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
	    var date = datetime.getDate() < 10 ? "0" + datetime.getDate()
	            : datetime.getDate();
	    return year + "-" + month + "-" + date;
	 
	}
    
    $("#down").click(function(){
    	if($("#h_dateStart").val()==""||$("#h_dateEnd").val()==""||$("#h_type").val()==""){
   		 alert("当前没有数据");
   		 return;
   	 }
    	
    	$('#exportForm').form('submit',{url:PATH+'/data/table/dailyreport/moneydaily/download'});
    })

});

