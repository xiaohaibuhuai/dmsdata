MainApp.controller('ClubCtrls',  function($scope,TabService) {
	var init =true;
	var  dg =$('#dataGrid').datagrid({
		url : PATH+'/stat/club/list',
		fit : true,
		fitColumns : true,
		border : false,
		pagination : true,
		idField : 'clubid',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		sortName : 'clubid',
		sortOrder : 'desc',
		checkOnSelect : false,
		selectOnCheck : false,
		nowrap : false,
		striped : true,
		rownumbers : false,
		singleSelect : true,
		toolbar:'#toolbar',
	    frozenColumns : [ [ {
			field : 'clubid',
			title : '俱乐部ID',
			width : 100,
			checkbox : false,
			sortable : true,
			align:'left',
	         formatter:function(value, row, index) {
		     var str = row.clubid;
		    	 if($scope.so_view)	str= $.formatString('<a ng-show="so_view" href="#"  onclick="$(this).scope().viewFun(\'{0}\');"  >'+row.clubid+'</a>',row.clubid);
		    	return str + '';
		    }
			
		}, {
			field : 'clubname',
			title : '俱乐部名称',
			width : 120,
			align:'left'
		} ] ],
	     columns:[[
	        {field : 'createuser',title : '创建者ID',width : 80,align:'left',
	            formatter:function(value, row, index) {
			     	var str = row.createuser;
			    	 if($scope.so_player_view)	str= $.formatString('<a ng-show="so_player_view" href="#"  onclick="$(this).scope().playerView(\'{0}\');"  >'+row.createuser+'</a>',row.createuser);
			    	return str + '';
			    }
	        },
	        {field:'fund',title:'基金',width:80,align:'left'},
	        {field:'clublevel',title:'等级',width:80,align:'left'},
	        {field:'clubexperience',title:'总经验',width:80,align:'left'},
	        {field:'clubchangeexp',title:'昨日增经验',width:80,align:'left'},
	        {field:'clubbuylevel',title:'购买等级',width:80,align:'left'},
	        {field:'buylevellimittime',title:'到期时间',width:80,align:'left'},
	        {field:'maxmembers',title:'最大人数',width:80,align:'left'},
	        {field:'maxmanagers',title:'最大管理',width:80,align:'left'},
	        {field:'currentmembers',title:'当前人数',width:80,align:'left'}
	      ]],
		onLoadSuccess : function() {
			init=false
			$('#searchForm table').show();
			parent.$.messager.progress('close');
			$(this).datagrid('tooltip');
			
		},
		onRowContextMenu : function(e, rowIndex, rowData) {
			e.preventDefault();
			$(this).datagrid('unselectAll');
			$(this).datagrid('selectRow', rowIndex);
			$('#menu').menu('show', {
				left : e.pageX,
				top : e.pageY
			});
		}
	});

	
	$scope.viewFun = function(id){
		var title = "俱乐部"+id;
		var url = PATH+'/stat/club/clubInfo?id='+id;
		TabService.addTab(title,url);

	}

	$scope.playerView = function(id){
		var title = "玩家"+id;
		var url = PATH+'/stat/player/playerInfo?id='+id;
		TabService.addTab(title,url);
		
	}

$scope.searchFun=function() {
	$('#dataGrid').datagrid('load', $.serializeObject($('#searchForm')));
};
$scope.cleanFun=function() {
	$('#searchForm input').val('');
	$('#dataGrid').datagrid('load', {});
};


$scope.addFun=function() {
	  $('#fm').form('clear');
	  showDialog('#dlg','添加俱乐部靓号');
	  url=PATH+'/stat/club/add';
};

$scope.submit=function(fm,dlg){
   if(!$(fm).form('validate')) return ; 
   $.messager.confirm("提示", "确定添加此靓号", function(flag) {
	   if(flag){
		   TabService.load();
		   $(fm).form('submit',{
	             url: url,
	             onSubmit: function ()
	             { 
	             },
	             success: function(result){
	            	    parent.$.messager.progress('close');
		                if(result==0){
		                	$.messager.alert('提示',"添加靓号俱乐部失败");
		                }else if(result==2){
		                	$.messager.alert('提示',"该俱乐部ID已存在");
		                }
		                else if(result==3){
		                	$.messager.alert('提示',"俱乐部创建者与输入用户不匹配");
		                }
		                else if(result==4){
		                	$.messager.alert('提示',"添加俱乐部靓号异常！");
		                }
		                else if(result==5){
		                	$.messager.alert('提示',"俱乐部创建失败，请联系管理员！");
		                }
		                else if(result == 1){
		                   $.messager.alert('提示',"靓号俱乐部创建成功！");
		                   $(dlg).dialog('close'); 
		                   dg.datagrid('reload');
		                }else{
		                	result= $.parseJSON(result);
		                	$.messager.alert('提示',result.msg);
		                }
	          }
	    });
	   }
   });
};

$scope.moveFun=function() {
	  $('#ffm').form('clear');
	  showDialog('#ddlg','俱乐部成员迁移');
	  url=PATH+'/stat/club/moveClubUser';
};
$scope.submit_move=function(fm,dlg){
	   if(!$(fm).form('validate')) return ;
	   $.messager.confirm("提示", "确定进行俱乐部成员迁移", function(flag) {
		   if(flag){
			   TabService.load();
			   $(fm).form('submit',{
		             url: url,
		             onSubmit: function ()
		             {
		             },
		             success: function(result){
		            	    parent.$.messager.progress('close');
			                if(result==1){
			                	$.messager.alert('提示',"俱乐部成员迁移失败");
			                }else if(result==2){
			                	$.messager.alert('提示',"该俱乐部ID不存在");
			                }else if(result==3){
			                	$.messager.alert('提示',"服务器错误");
			                }else if(result == 0){
			                   $.messager.alert('提示',"俱乐部成员迁移成功！");
			                   $(dlg).dialog('close'); 
			                   dg.datagrid('reload');
			                }else{
			                	result= $.parseJSON(result);
			                	$.messager.alert('提示',result.msg);
			                }
		          }
		      });	
		   }
	   });
	};
});