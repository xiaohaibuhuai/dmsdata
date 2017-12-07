MainApp.controller('CoinChartCtrls',  function($scope,TabService) {
	
	var initInc = false;
	var  dg =$('#dg2').datagrid({
		url : PATH+'/data/monitoring/moneysystem/coinchart/reduceRank',
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
		url : PATH+'/data/monitoring/moneysystem/coinchart/increaseRank',
		fit : false,
		title:"24小时德扑币增加排名",
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
	        {field:'change',title:'德扑币增加',width:150,align:'left'}
	      ]],
	});
	
	$scope.playerView = function(id){
		var title = "玩家"+id;
		var url = PATH+'/stat/player/playerInfo?id='+id;
		TabService.addTab(title,url);
	}
	
	
	
	 $.post(PATH+'/data/monitoring/moneysystem/coinchart/sum',function(data){
			
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








//	 function JSONToCSVConvertor(JSONData, ReportTitle, ShowLabel) {  
//		    //If JSONData is not an object then JSON.parse will parse the JSON string in an Object  
//		    var arrData = typeof JSONData != 'object' ? JSON.parse(JSONData)  
//		            : JSONData;  
//
//		    var CSV = '';  
//		    //Set Report title in first row or line  
//
//		    CSV += ReportTitle + '\r\n\n';  
//
//		    //This condition will generate the Label/Header  
//		    if (ShowLabel) {  
//		        var row = "";  
//
//		        //This loop will extract the label from 1st index of on array  
//		        for ( var index in arrData[0]) {  
//
//		            //Now convert each value to string and comma-seprated  
//		            row += index + ',';  
//		        }  
//
//		        row = row.slice(0, -1);  
//
//		        //append Label row with line break  
//		        CSV += row + '\r\n';  
//		    }  
//
//		    //1st loop is to extract each row  
//		    for (var i = 0; i < arrData.length; i++) {  
//		        var row = "";  
//
//		        //2nd loop will extract each column and convert it in string comma-seprated  
//		        for ( var index in arrData[i]) {  
//		            row += '"' + arrData[i][index] + '",';  
//		        }  
//
//		        row.slice(0, row.length - 1);  
//
//		        //add a line break after each row  
//		        CSV += row + '\r\n';  
//		    }  
//
//		    if (CSV == '') {  
//		        alert("Invalid data");  
//		        return;  
//		    }  
//
//		    //Generate a file name  
//		    var fileName = "MyReport_";  
//		    //this will remove the blank-spaces from the title and replace it with an underscore  
//		    fileName += ReportTitle.replace(/ /g, "_");  
//
//		    //Initialize file format you want csv or xls  
//		    var uri = 'data:text/csv;charset=utf-8,' + escape(CSV);  
//
//		    // Now the little tricky part.  
//		    // you can use either>> window.open(uri);  
//		    // but this will not work in some browsers  
//		    // or you will not get the correct file extension      
//
//		    //this trick will generate a temp <a /> tag  
//		    var link = document.createElement("a");  
//		    link.href = uri;  
//
//		    //set the visibility hidden so it will not effect on your web-layout  
//		    link.style = "visibility:hidden";  
//		    link.download = fileName + ".csv";  
//
//		    //this part will append the anchor tag and remove it after automatic click  
//		    document.body.appendChild(link);  
//		    link.click();  
//		    document.body.removeChild(link);  
//		}  
//
//		$("#btnExport").click(function() {  
//		    var data = JSON.stringify($('#dg1').datagrid('getData').rows);  
//		    alert(data);  
//		    if (data == '')  
//		        return;  
//
//		    JSONToCSVConvertor(data, "Download", true);  
//		});  

	 
//	 $("#btnExport").click(function() {  
//		 //获取Datagride的列  
//         var rows = $('#dg1').datagrid('getRows');  
//         var columns = $("#dg2").datagrid("options").columns[0];  
//         var oXL = new ActiveXObject("Excel.Application"); //创建AX对象excel   
//         var oWB = oXL.Workbooks.Add(); //获取workbook对象   
//         var oSheet = oWB.ActiveSheet; //激活当前sheet  
//         //设置工作薄名称  
//         oSheet.name = "导出Excel报表";  
//         //设置表头  
//         for (var i = 0; i < columns.length; i++) {  
//             oSheet.Cells(1, i+1).value = columns[i].title;  
//         }  
//         //设置内容部分  
//         for (var i = 0; i < rows.length; i++) {  
//             //动态获取每一行每一列的数据值  
//             for (var j = 0; j < columns.length; j++) {                 
//                 oSheet.Cells(i + 2, j+1).value = rows[i][columns[j].field];  
//             }     
//         }                
//         oXL.Visible = true; //设置excel可见属性  
//		});  


   

});

