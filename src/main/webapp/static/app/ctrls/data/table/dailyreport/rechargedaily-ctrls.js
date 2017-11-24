MainApp.controller('RechargeDailyCtrls',  function($scope,TabService) {
    parent.$.messager.progress('close');
	
    $("#btn").click(function(){
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
		    url : PATH+'/data/table/dailyreport/moneydaily/money',
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
		    	
		    $("#h_dateStart").val(dateStart)
  		    	$("#h_dateEnd").val(dateEnd)
  		    $("#h_type").val(type)
		    	
		    	
		    	$('#chart1').highcharts({
    			  chart: {
    		            type: 'column'
    		        },
    		        title: {
    		            text: '24小时各门票存量对比图'
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
  	  
 
  	  //数据表格
  	  var  dg =$('#dg1').datagrid({
  			url : PATH+'/data/table/cash/gamestatistic/sum',
  			fit : false,
  			title:"1.各牌局类型14日开局总数统计",
  			border : true,
  			singleSelect : true,
  		    columns:[[
  		    	    {field:'targetdate',title:'日期',width:100,align:'left',formatter:function(val,rec){
  	                return jsonYearMonthDay(val);
  	            }},
  		        {field : 'g_normal',title : '普通局',width : 120,align:'left',},
  		        {field:'g_normalins',title:'普通保险局',width:120,align:'left'},
  		        {field:'g_omaha',title:'奥马哈局',width:120,align:'left'},
  		        {field:'g_omahains',title:'奥马哈保险局',width:120,align:'left'},
  		        {field:'g_six',title:'6+局',width:120,align:'left'},
  		        {field:'g_sng',title:'SNG',width:120,align:'left'}
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
      
       	$('#exportForm').form('submit',{url:PATH+'/data/table/dailyreport/rechargedaily/download'});
    })


});

