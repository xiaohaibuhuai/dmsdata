MainApp.controller('PlayerStatisticCtrls',  function($scope,TabService) {
	var initInc = false;
    parent.$.messager.progress('close');
    
    
    var  dg =$('#dg1').datagrid({
		url : PATH+'/data/table/cash/playerstatistic/getGamePlayer',
		fit : false,
		title:"1.各牌局类型14日活跃玩家统计",
		border : true,
		singleSelect : true,
	    columns:[[
	    	{field:'targetdate',title:'日期',width:100,align:'left',formatter:function(val,rec){
                return jsonYearMonthDay(val);
            }},
	        {field : 'p_normal',title : '普通局',width : 130,align:'left',},
	        {field:'p_normalins',title:'普通保险局',width:110,align:'left'},
	        {field:'p_omaha',title:'奥马哈局',width:110,align:'left'},
	        {field:'p_omahains',title:'奥马哈保险局',width:150,align:'left'},
	        {field:'p_six',title:'6+局',width:150,align:'left'},
	        {field:'p_sng',title:'SNG',width:150,align:'left'}
	      ]],
	});
    
    
    
    
	var  dg =$('#dg2').datagrid({
		url : PATH+'/data/table/cash/playerstatistic/getBlindPlayer4Normal',
		fit : false,
		title:"2.普通局14日各盲注活跃人数统计",
		border : true,
		singleSelect : true,
	    columns:[[
	    	{field:'targetdate',title:'普通局',width:90,align:'left',formatter:function(val,rec){
                return jsonYearMonthDay(val);
            }},
		        {field : 'p_b2',title : '1/2',width : 90,align:'left',},
		        {field:'p_b4',title:'2/4',width:90,align:'left'},
		        {field:'p_10',title:'5/10',width:90,align:'left'},
		        {field:'p_b20',title:'10/20',width:90,align:'left'},
		        {field:'p_b40',title:'20/40',width:90,align:'left'},
		        {field:'p_b50',title:'25/50',width:90,align:'left'},
		        {field:'p_b100',title:'50/100',width:90,align:'left'},
		        {field:'p_b200',title:'100/200',width:90,align:'left'},
		        {field:'p_b400',title:'200/400',width:90,align:'left'},
		        {field:'p_b600',title:'300/600',width:90,align:'left'},
		        {field:'p_b1000',title:'500/1k',width:90,align:'left'},
		        {field:'p_b2000',title:'1k/2k',width:90,align:'left'},
		        {field:'p_pre',title:'盲注指数',width:90,align:'left'}
	      ]],
	});
	
	
	var  dg =$('#dg3').datagrid({
		url : PATH+'/data/table/cash/playerstatistic/getBlindPlayer4Normalins',
		fit : false,
		title:"3.普通局保险局14日各盲注活跃人数统计",
		border : true,
		singleSelect : true,
	     columns:[[
	    	 {field:'targetdate',title:'普通保险局',width:90,align:'left',formatter:function(val,rec){
                 return jsonYearMonthDay(val);
             }},
		        {field : 'p_b2',title : '1/2',width : 90,align:'left',},
		        {field:'p_b4',title:'2/4',width:90,align:'left'},
		        {field:'p_10',title:'5/10',width:90,align:'left'},
		        {field:'p_b20',title:'10/20',width:90,align:'left'},
		        {field:'p_b40',title:'20/40',width:90,align:'left'},
		        {field:'p_b50',title:'25/50',width:90,align:'left'},
		        {field:'p_b100',title:'50/100',width:90,align:'left'},
		        {field:'p_b200',title:'100/200',width:90,align:'left'},
		        {field:'p_b400',title:'200/400',width:90,align:'left'},
		        {field:'p_b600',title:'300/600',width:90,align:'left'},
		        {field:'p_b1000',title:'500/1k',width:90,align:'left'},
		        {field:'p_b2000',title:'1k/2k',width:90,align:'left'},
		        {field:'p_pre',title:'盲注指数',width:90,align:'left'}
	      ]],
	});
	
	var  dg =$('#dg4').datagrid({
		url : PATH+'/data/table/cash/playerstatistic/getBlindPlayer4Omaha',
		fit : false,
		title:"4.奥马哈局14日各盲注活跃人数统计",
		border : true,
		singleSelect : true,
	     columns:[[
	    	 {field:'targetdate',title:'奥马哈局',width:90,align:'left',formatter:function(val,rec){
                 return jsonYearMonthDay(val);
             }},
		        {field : 'p_b2',title : '1/2',width : 90,align:'left',},
		        {field:'p_b4',title:'2/4',width:90,align:'left'},
		        {field:'p_10',title:'5/10',width:90,align:'left'},
		        {field:'p_b20',title:'10/20',width:90,align:'left'},
		        {field:'p_b40',title:'20/40',width:90,align:'left'},
		        {field:'p_b50',title:'25/50',width:90,align:'left'},
		        {field:'p_b100',title:'50/100',width:90,align:'left'},
		        {field:'p_b200',title:'100/200',width:90,align:'left'},
		        {field:'p_b400',title:'200/400',width:90,align:'left'},
		        {field:'p_b600',title:'300/600',width:90,align:'left'},
		        {field:'p_b1000',title:'500/1k',width:90,align:'left'},
		        {field:'p_b2000',title:'1k/2k',width:90,align:'left'},
		        {field:'p_pre',title:'盲注指数',width:90,align:'left'}
	      ]],
	});
	
	
	
	var  dg =$('#dg5').datagrid({
		url : PATH+'/data/table/cash/playerstatistic/getBlindPlayer4Omahains',
		fit : false,
		title:"5. 奥马哈保险局14日各盲注开局数统计",
		border : true,
		singleSelect : true,
	     columns:[[
	    	 {field:'targetdate',title:'奥马哈保险局',width:90,align:'left',formatter:function(val,rec){
                 return jsonYearMonthDay(val);
             }},
		        {field : 'p_b2',title : '1/2',width : 90,align:'left',},
		        {field:'p_b4',title:'2/4',width:90,align:'left'},
		        {field:'p_10',title:'5/10',width:90,align:'left'},
		        {field:'p_b20',title:'10/20',width:90,align:'left'},
		        {field:'p_b40',title:'20/40',width:90,align:'left'},
		        {field:'p_b50',title:'25/50',width:90,align:'left'},
		        {field:'p_b100',title:'50/100',width:90,align:'left'},
		        {field:'p_b200',title:'100/200',width:90,align:'left'},
		        {field:'p_b400',title:'200/400',width:90,align:'left'},
		        {field:'p_b600',title:'300/600',width:90,align:'left'},
		        {field:'p_b1000',title:'500/1k',width:90,align:'left'},
		        {field:'p_b2000',title:'1k/2k',width:90,align:'left'},
		        {field:'p_pre',title:'盲注指数',width:90,align:'left'}
	      ]],
	});
	
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
	
	

});

