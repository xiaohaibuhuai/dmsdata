MainApp.controller('ClubWeekCtrls',  function($scope,TabService) {
    parent.$.messager.progress('close');
	
    var initInc = false;
    
    
    $("#btn").click(function(){
    	  
    	  var dateStart=$("#dateStart").val();      //开始时间
    	  var dateEnd=$("#dateEnd").val();          //结束时间
    	  if(dateStart==""||dateEnd==""){
    		  alert("请选择时间");
    		  return;
    	  }else{

    	      //判断开始结束时间是否符合标准
    		  var start = new Date(dateStart.replace("-", "/").replace("-", "/")); 
    		  var end= new Date(dateEnd.replace("-", "/").replace("-", "/")); 
    		  if(end<start){  
    			  alert("开始时间大于结束时间")
    		        return; 
    		    }  
    	  }
    	  // 数据表格
    	  //家在俱乐部上桌等级表格
    	  var  dg =$('#dg1').datagrid({
    			url : PATH+'/data/table/weekreport/clubweek/clubDataQuery',
    			fit : false,
//    			pagePosition : bottom,
                pagination : true,
    			queryParams :  {
    			    dateStart: dateStart,
    			    dateEnd: dateEnd,
    			    page: 1,
                    rows: 10
    			},
    			title:"俱乐部上桌等级汇总表",
    			border : true,
    			singleSelect : true,
    		    columns:[[
    		        {field : 'clubId',title : '俱乐部id',width : 120,align:'left'},
    		        {field : 'clubName',title : '俱乐部名字',width : 120,align:'left',},
    		        {field:'roomidNum',title:'总上桌人次',width:120,align:'left'},
    		        {field:'numWeek',title:'总上桌人次环比上周',width:120,align:'left'},
    		        {field:'clubNum',title:'俱乐部局上桌人次',width:120,align:'left'},
    		        {field:'leagNum',title:'联盟局上桌人次',width:120,align:'left'},
                    {field:'maxPerson',title:'现有人数/最大人数',width:120,align:'left',formatter:function(val,row,index){
                        return row.personNum + '/' + val;
                    }},
                    {field:'personChange',title:'现有人数较上周变化值',width:120,align:'left'},
                    {field:'exp',title:'当前经验',width:120,align:'left'},
                    {field:'expLevel',title:'当前等级',width:120,align:'left'},
                    {field:'expChan',title:'经验变化值',width:120,align:'left'},
                    {field:'levelChan',title:'等级变化值',width:120,align:'left'},
                    {field:'Host',title:'主机常用登陆地址',width:120,align:'left'},
                    {field:'leaName',title:'所属联盟名称',width:120,align:'left'}
    		      ]]
    		});   

            //分页代码
        	$('#dg1').datagrid('getPager').pagination({
                pageNumber : 1,
                pageSize : 10,
                pageList : 	[10,20,30,40,50],
                beforePageText: '第',//页数文本框前显示的汉字
                afterPageText: '页    共 {pages} 页',
                displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
            });

            //加载俱乐部伙牌表格
            var dg =$('#dg2').datagrid({
                url : PATH+'/data/table/weekreport/clubweek/clubDataQuery',
                pagination : true,
                queryParams :  {
                    dateStart: dateStart,
                    dateEnd: dateEnd,
                    page: 1,
                    rows: 10
                },
                fit : false,
                title:"俱乐部伙牌汇总表",
                border : true,
                singleSelect : true,
                columns:[[
                    {field : 'clubId',title : '俱乐部id',width : 120,align:'left',},
                    {field : 'clubName',title : '俱乐部名字',width : 120,align:'left',},
                    {field:'roomidNum',title:'总上桌人次',width:120,align:'left'},
                    {field:'numWeek',title:'总上桌人次环比上周',width:120,align:'left'},
                    {field:'cheatTimes',title:'总伙牌人次',width:120,align:'left'},
                    {field:'timesPer',title:'总伙牌人次占比',width:120,align:'left'},
                    {field:'timesWeek',title:'总伙牌人次环比上周',width:120,align:'left'},
                    {field:'clubNum',title:'俱乐部局上桌人次',width:120,align:'left'},
                    {field:'clubTimes',title:'俱乐部局伙牌人次',width:120,align:'left'},
                    {field:'leagNum',title:'联盟局上桌人次',width:120,align:'left'},
                    {field:'leagTimes',title:'联盟局伙牌人次',width:120,align:'left'},
                    {field:'maxPerson',title:'现有人数/最大人数',width:120,align:'left',formatter:function(val,row,index){
                        return row.personNum + '/' + val;
                    }},
                    {field:'Host',title:'主机常用登陆地址',width:120,align:'left'},
                    {field:'leaName',title:'所属联盟名称',width:120,align:'left'}
                ]]
            });

            //分页代码
            $('#dg2').datagrid('getPager').pagination({
                pageNumber : 1,
                pageSize : 10,
                pageList : 	[10,20,30,40,50],
                beforePageText: '第',//页数文本框前显示的汉字
                afterPageText: '页    共 {pages} 页',
                displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
            });
   });

    
    $('#tt').tabs({    
	      onSelect:function(title){
	      }   
	 });

    $("#down").click(function(){
    	if($("#h_dateStart").val()==""||$("#h_dateEnd").val()==""){
   		 alert("请选择时间");
   		 return;
   	    }


    	$('#exportForm').form('submit',{url:PATH+'/data/table/weekreport/clubweek/excelDown'});
    });
});

