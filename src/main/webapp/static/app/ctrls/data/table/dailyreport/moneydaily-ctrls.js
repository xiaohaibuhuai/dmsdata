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
  		    },
  		    error : function() {
  		      $("#loadModal").css("display", "none");
  		      alert("服务出错请联系管理员");
  		    }
  		  })
    	  
    	  
    	  
    	 
    	  
    	  
    	  //2
    	  $.post(PATH+'/data/table/dailyreport/moneydaily/diamond',function(data){
    			
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
    		            min: -33,
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
    	    
    	  //数据表格
    	    var  dg =$('#dg2').datagrid({
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
    
	
    
//    //excel表格导出
//     $("#down").click(function(){
//    	 
//    	 if($("#h_dateStart").val()==""||$("#h_dateEnd").val()==""||$("#h_type").val()==""){
//    		 alert("当前没有数据");
//    		 return;
//    	 }
//    	 	//alert($("#h_dateStart").val()+$("#h_dateEnd").val()+$("#h_type").val());
//    	  var url = '/data/table/dailyreport/rechargedaily/download';
//    	  var xhr = new XMLHttpRequest();
//    	  xhr.open('GET', url, true);    // 也可以使用POST方式，根据接口
//    	  xhr.responseType = "blob";  // 返回类型blob
//    	  // 定义请求完成的处理函数，请求前也可以增加加载框/禁用下载按钮逻辑
//    	  xhr.onload = function () {
//    	    // 请求完成
//    	    if (this.status === 200) {
//    	      // 返回200
//    	      var blob = this.response;
//    	      var reader = new FileReader();
//    	      reader.readAsDataURL(blob);  // 转换为base64，可以直接放入a表情href
//    	      reader.onload = function (e) {
//    	        // 转换完成，创建一个a标签用于下载
//    	        var a = document.createElement('a');
//    	       // a.download = 'data.xlsx';
//    	        a.href = e.target.result;
//    	        $("#link").append(a);  // 修复firefox中无法触发click
//    	        a.click();
//    	        $(a).remove();
//    	      }
//    	    }
//    	  };
//    	  // 发送ajax请求
//    	  xhr.send()
//    	})
    
    $("#down").click(function(){
    	if($("#h_dateStart").val()==""||$("#h_dateEnd").val()==""||$("#h_type").val()==""){
   		 alert("当前没有数据");
   		 return;
   	 }
    	
    	$('#exportForm').form('submit',{url:PATH+'/data/table/dailyreport/moneydaily/download'});
    
    	
    	
    	
    })

});

