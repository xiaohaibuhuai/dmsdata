MainApp.controller('GameStatisticCtrls',  function($scope,TabService) {
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
			{field:'g_short',title:'短排局',width:120,align:'left'},
			{field:'g_shortins',title:'短排保险局',width:120,align:'left'},
	        {field:'g_sng',title:'SNG',width:120,align:'left'},
	        {field:'g_sum',title:'统计',width:120,align:'left'}
	      ]],
	});
	
	
	var  dg =$('#dg2').datagrid({
		url : PATH+'/data/table/cash/gamestatistic/valid',
		fit : false,
		title:"2.各牌局类型14日有效开局总数统计",
		border : true,
		singleSelect : true,
	     columns:[[
	    	        {field:'targetdate',title:'日期',width:100,align:'left',formatter:function(val,rec){
                              return jsonYearMonthDay(val);
                    }},
		        {field : 'g_normal',title : '普通局',width : 120,align:'left'},
		        {field:'g_normalins',title:'普通保险局',width:120,align:'left'},
		        {field:'g_omaha',title:'奥马哈局',width:120,align:'left'},
		        {field:'g_omahains',title:'奥马哈保险局',width:120,align:'left'},
		        {field:'g_six',title:'6+局',width:120,align:'left'},
			    {field:'g_short',title:'短排局',width:120,align:'left'},
			    {field:'g_shortins',title:'短排保险局',width:120,align:'left'},
		        {field:'g_sng',title:'SNG',width:120,align:'left'},
		        {field:'g_sum',title:'统计',width:120,align:'left'}
	      ]],
	});
	
	//将时间戳转换为yyyy-MM-dd
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
	
	
	 $.post(PATH+'/data/table/cash/gamestatistic/chart',function(d){
		  $('#main').highcharts({
			  chart: {
		            type: 'column'
		        },
		        title: {
		            text: '14天开局数变化柱图',
		 
		        },
		        yAxis: [{
		            min: 0,
		            title: {
		                text: '数量'
		            }
		        }, ],
		        
		        legend: {
		            shadow: false
		        },
		        tooltip: {
		            shared: true,
		            formatter : function (){ // 提示框格式化字符串
		                 var s = '<b>'+this.x+'</b>';
		                 $.each(this.points ,function(index,value){
		                	     if(this.series.name=='每日有效开局总数'){
		                	    	 if(this.y!=null){
		                	    	 s+='<br />' + this.series.name + ':' + this.y+" ("+this.point.ext+"%)";
		                	    	 }
		                	    	 }else{
		                	    	 s += '<br />' + this.series.name + ':' + this.y;
		                	     }
		                    
		                 });
		                 return s;
		             },
		            
		        },
		        plotOptions: {
		            column: {
		                grouping: false,
		                shadow: false,
		                borderWidth: 0
		            }
		        },
		        
		        xAxis: {
		            categories: d.categories
		        },        
		        series: [{
		            name: '每日开局总数',
		            color: 'rgba(165,170,217,1)',
		            data: d.series[0],
		            pointPadding: 0.3,
		            pointPlacement: -0.2
		        }, {
		            name: '每日有效开局总数',
		            color: 'rgba(126,86,134,.9)',
		            data: d.series[1],
		            pointPadding: 0.4,
		            pointPlacement: -0.2,
		        }, 
//		        {
//		            name: '百分比',
//		            color: 'rgba(216,191,216,1)',
//		            data: d.series[2],
//		            tooltip: {
//		                valueSuffix: '%',
//		            },
//		            visible: false,
//		            pointPadding: 0.4,
//		            pointPlacement: 0.2,
//		            yAxis: 1
//		        }
		        ]
		    });
		    parent.$.messager.progress('close');
			} ,'json');
});

