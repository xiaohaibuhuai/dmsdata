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
		        {field : 'g_b2',title : '1/2',width : 90,align:'left'},
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
	

	
	
	
	$.post(PATH+'/data/table/cash/blindstatistic/sum',function(s){
		var data = s;
		var points = [],
		region_p,
		region_val,
		region_i,
		country_p,
		country_i,
		cause_p,
		cause_i,
		cause_name = [];
	cause_name['1/2'] = '1/2';
	cause_name['2/4'] = '2/4';
	cause_name['5/10'] = '5/10';
	cause_name['10/20'] = '10/20';
	cause_name['20/40'] = '20/40';
	cause_name['25/50'] = '25/50';
	cause_name['50/100'] = '50/100';
	cause_name['100/200'] = '100/200';
	cause_name['200/400'] = '200/400';
	cause_name['300/600'] = '300/600';
	cause_name['500/1000'] = '500/1000';
	cause_name['1000/2000'] = '1000/2000';
	
	
	region_i = 0;
	for (var region in data) {
		region_val = 0;
		region_p = {
			id: "id_" + region_i,
			name: region,
			color: Highcharts.getOptions().colors[region_i]
		};
		country_i = 0;
		for (var country in data[region]) {
			country_p = {
				id: region_p.id + "_" + country_i,
				name: country,
				parent: region_p.id
			};
			points.push(country_p);
			cause_i = 0;
			for (var cause in data[region][country]) {
				cause_p = {
					id: country_p.id + "_" + cause_i,
					name: cause_name[cause],
					parent: country_p.id,
					value: Math.round(+data[region][country][cause])
				};
				region_val += cause_p.value;   //最里层的值
				points.push(cause_p);
				cause_i++;
			}
			country_i++;
		}
		//region_p.value = Math.round(region_val / country_i);   平均值
		region_p.value = region_val;  // 总值
		points.push(region_p);
		region_i++;
	}
		
		
	var chart = new Highcharts.Chart({
		chart: {
			renderTo: 'main'
		},
		series: [{
			type: "treemap",
			layoutAlgorithm: 'squarified',
			allowDrillToNode: true,
			dataLabels: {
				enabled: false
			},
			levelIsConstant: false,
			levels: [{
				level: 1,
				dataLabels: {
					enabled: true
				},
				borderWidth: 3
			}],
			data: points
		}],
//		subtitle: {
//			text: '点击下钻. 数据来源: <a href="https://apps.who.int/gho/data/node.main.12?lang=en">WHO</a>.'
//		},
		title: {
			text: '14日各盲注牌局统计'
		}
	});
	} ,'json');


	
	
	
	

});

