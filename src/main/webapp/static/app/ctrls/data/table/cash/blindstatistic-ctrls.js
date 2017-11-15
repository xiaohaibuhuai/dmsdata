MainApp.controller('BlindStatisticCtrls',  function($scope,TabService) {
	var initInc = false;
    parent.$.messager.progress('close');
	var  dg =$('#dg1').datagrid({
		url : PATH+'/data/table/cash/blindstatistic/getBlindNormal',
		fit : false,
		title:"1.普通局14日各盲注开局数统计",
		border : true,
		singleSelect : true,
		columns:[[
	    	        {field:'targetdate',title:'普通局',width:90,align:'left',formatter:function(val,rec){
                return jsonYearMonthDay(val);
                 }},
		        {field : 'g_b2',title : '1/2',width : 90,align:'left',},
		        {field:'g_b4',title:'2/4',width:90,align:'left'},
		        {field:'g_b10',title:'5/10',width:90,align:'left'},
		        {field:'g_b20',title:'10/20',width:90,align:'left'},
		        {field:'g_b40',title:'20/40',width:90,align:'left'},
		        {field:'g_b50',title:'25/50',width:90,align:'left'},
		        {field:'g_b100',title:'50/100',width:90,align:'left'},
		        {field:'g_b200',title:'100/200',width:90,align:'left'},
		        {field:'g_b400',title:'200/400',width:90,align:'left'},
		        {field:'g_b600',title:'300/600',width:90,align:'left'},
		        {field:'g_b1000',title:'500/1k',width:90,align:'left'},
		        {field:'g_b2000',title:'1k/2k',width:90,align:'left'},
		        {field:'g_pre',title:'盲注指数',width:90,align:'left'}
	      ]],
	});
	
	
	var  dg =$('#dg2').datagrid({
		url : PATH+'/data/table/cash/blindstatistic/getBlindNormalins',
		fit : false,
		title:"2.普通保险局14日各盲注开局数统计",
		border : true,
		singleSelect : true,
	     columns:[[
	    	 {field:'targetdate',title:'普通保险局',width:90,align:'left',formatter:function(val,rec){
                 return jsonYearMonthDay(val);
             }},
		        {field : 'g_b2',title : '1/2',width : 90,align:'left',},
		        {field:'g_b4',title:'2/4',width:90,align:'left'},
		        {field:'g_b10',title:'5/10',width:90,align:'left'},
		        {field:'g_b20',title:'10/20',width:90,align:'left'},
		        {field:'g_b40',title:'20/40',width:90,align:'left'},
		        {field:'g_b50',title:'25/50',width:90,align:'left'},
		        {field:'g_b100',title:'50/100',width:90,align:'left'},
		        {field:'g_b200',title:'100/200',width:90,align:'left'},
		        {field:'g_b400',title:'200/400',width:90,align:'left'},
		        {field:'g_b600',title:'300/600',width:90,align:'left'},
		        {field:'g_b1000',title:'500/1k',width:90,align:'left'},
		        {field:'g_b2000',title:'1k/2k',width:90,align:'left'},
		        {field:'g_pre',title:'盲注指数',width:90,align:'left'}
	      ]],
	});
	
	var  dg =$('#dg3').datagrid({
		url : PATH+'/data/table/cash/blindstatistic/getBlindOmaha',
		fit : false,
		title:"3.奥马哈局14日各盲注开局数统计",
		border : true,
		singleSelect : true,
	     columns:[[
	    	 {field:'targetdate',title:'奥马哈局',width:90,align:'left',formatter:function(val,rec){
                 return jsonYearMonthDay(val);
             }},
		        {field : 'g_b2',title : '1/2',width : 90,align:'left',},
		        {field:'g_b4',title:'2/4',width:90,align:'left'},
		        {field:'g_b10',title:'5/10',width:90,align:'left'},
		        {field:'g_b20',title:'10/20',width:90,align:'left'},
		        {field:'g_b40',title:'20/40',width:90,align:'left'},
		        {field:'g_b50',title:'25/50',width:90,align:'left'},
		        {field:'g_b100',title:'50/100',width:90,align:'left'},
		        {field:'g_b200',title:'100/200',width:90,align:'left'},
		        {field:'g_b400',title:'200/400',width:90,align:'left'},
		        {field:'g_b600',title:'300/600',width:90,align:'left'},
		        {field:'g_b1000',title:'500/1k',width:90,align:'left'},
		        {field:'g_b2000',title:'1k/2k',width:90,align:'left'},
		        {field:'g_pre',title:'盲注指数',width:90,align:'left'}
	      ]],
	});
	
	
	
	var  dg =$('#dg4').datagrid({
		url : PATH+'/data/table/cash/blindstatistic/getBlindOmahains',
		fit : false,
		title:"4. 奥马哈保险局14日各盲注开局数统计",
		border : true,
		singleSelect : true,
	     columns:[[
	    	 {field:'targetdate',title:'奥马哈保险局',width:90,align:'left',formatter:function(val,rec){
                 return jsonYearMonthDay(val);
             }},
		        {field : 'g_b2',title : '1/2',width : 90,align:'left',},
		        {field:'g_b4',title:'2/4',width:90,align:'left'},
		        {field:'g_b10',title:'5/10',width:90,align:'left'},
		        {field:'g_b20',title:'10/20',width:90,align:'left'},
		        {field:'g_b40',title:'20/40',width:90,align:'left'},
		        {field:'g_b50',title:'25/50',width:90,align:'left'},
		        {field:'g_b100',title:'50/100',width:90,align:'left'},
		        {field:'g_b200',title:'100/200',width:90,align:'left'},
		        {field:'g_b400',title:'200/400',width:90,align:'left'},
		        {field:'g_b600',title:'300/600',width:90,align:'left'},
		        {field:'g_b1000',title:'500/1k',width:90,align:'left'},
		        {field:'g_b2000',title:'1k/2k',width:90,align:'left'},
		        {field:'g_pre',title:'盲注指数',width:90,align:'left'}
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
	
//	 $.post(PATH+'/data/table/cash/gamestatistic/chart',function(d){
//		  $('#main').highcharts({
//			  chart: {
//		            type: 'column'
//		        },
//		        title: {
//		            text: '14天开局数变化柱图',
//		 
//		        },
//		        yAxis: [{
//		            min: 0,
//		            title: {
//		                text: '数量'
//		            }
//		        }, {
//		            title: {
//		                text: '百分比 (%)'
//		            },
//		            opposite: true
//		        }],
//		        
//		        legend: {
//		            shadow: false
//		        },
//		        tooltip: {
//		            shared: true
//		        },
//		        plotOptions: {
//		            column: {
//		                grouping: false,
//		                shadow: false,
//		                borderWidth: 0
//		            }
//		        },
//		        
//		        xAxis: {
//		            categories: d.categories
//		        },        
//		        series: [{
//		            name: '每日开局总数',
//		            color: 'rgba(165,170,217,1)',
//		            data: d.series[0].sum,
//		            pointPadding: 0.3,
//		            pointPlacement: -0.2
//		        }, {
//		            name: '每日有效开局总数',
//		            color: 'rgba(126,86,134,.9)',
//		            data: d.series[0].valid,
//		            pointPadding: 0.4,
//		            pointPlacement: -0.2
//		        }, {
//		            name: '百分比',
//		            color: 'rgba(216,191,216,1)',
//		            data: d.series[0].per,
//		            tooltip: {
//		                valuePrefix: '%',
//		            },
//		            visible: false,
//		            pointPadding: 0.4,
//		            pointPlacement: 0.2,
//		            yAxis: 1
//		        }]
//		    });
//		    parent.$.messager.progress('close');
//			} ,'json');
});

