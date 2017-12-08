MainApp.controller('ClubWeekCtrls',  function($scope,TabService) {
    parent.$.messager.progress('close');
	
    var initInc = false;
    
    
    $("#btn").click(function(){
    	  alert("ss");
    	  
    	  var dateStart=$("#dateStart").val();
    	  var dateEnd=$("#dateEnd").val();
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
    	  alert("bbbb")
    	  // 数据表格
    	  var  dg =$('#dg1').datagrid({
    			url : PATH+'/data/table/weekreport/clubweek/clubLevel?'+"dateStart="+dateStart+"&dateEnd="+dateEnd,
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
    	  
      	$("#h_dateStart").val(dateStart);
	    	$("#h_dateEnd").val(dateEnd);
   });
    
    
    
    
    $('#tt').tabs({    
	      onSelect:function(title){   
	         
	          if(title=='俱乐部增量统计'){
	
	        	  $scope.loadInc();
	          }      
	      }   
	 });
    
    
    
    $scope.loadInc = function(){
      	 if(!initInc){
      		 // 数据表格
      		 
      		if($("#h_dateStart").val()==""||$("#h_dateEnd").val()==""){
	      		 alert("当前没有数据");
	      		 return;
	      	 }
    	    var  dg =$('#dg2').datagrid({
    			url : PATH+'/data/table/weekreport/clubweek/clubCheat?'+"dateStart="+$("#h_dateStart").val()+"&dateEnd="+$("#h_dateEnd").val(),
    			fit : false,
    			title:"俱乐部伙牌",
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
    }	 
      	 }
    
    
    $("#down").click(function(){
    	if($("#h_dateStart").val()==""||$("#h_dateEnd").val()==""||$("#h_type").val()==""){
   		 alert("当前没有数据");
   		 return;
   	 }
    	
    	$('#exportForm').form('submit',{url:PATH+'/data/table/weekreport/clubweek/excleDown'});
    })
    
    
    

});

