MainApp.controller('LeagueCtrls',  function($scope,TabService) {
	var init =true;
	var  dg =$('#dataGrid').datagrid({
		url : PATH+'/stat/league/list',
		fit : true,
		fitColumns : true,
		border : false,
		pagination : true,
		idField : 'leagueid',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		sortName : 'members',
		sortOrder : 'desc',
		checkOnSelect : false,
		selectOnCheck : false,
		nowrap : false,
		striped : true,
		rownumbers : false,
		singleSelect : true,
		toolbar:'#toolbar',
	    frozenColumns : [ [ {
			field : 'leagueid',
			title : '联盟ID',
			width : 100,
			checkbox : false,
			sortable : true,
			align:'left',
	         formatter:function(value, row, index) {
		     	var str = row.leagueid;
		    	if($scope.so_view)	str= $.formatString('<a ng-show="so_view"  href="#"  onclick="$(this).scope().viewFun(\'{0}\');"  >'+row.leagueid+'</a>',row.leagueid);
		    	return str + '';
		    }
			
		}, {
			field : 'leaguename',
			title : '联盟名称',
			width : 120,
			align:'left'
		} ] ],
	     columns:[[
	        {field : 'leaguelord',title : '创建俱乐部ID',width : 80,align:'left',
	            formatter:function(value, row, index) {
			     	var str = row.leaguelord;
			    	 if($scope.so_club_view)	str= $.formatString('<a ng-show="so_club_view"  href="#" onclick="$(this).scope().clubView(\'{0}\');"  >'+row.leaguelord+'</a>',row.leaguelord);
			    	return str + '';
			    }
	        },
	        {field :'maxmembers',title : '联盟上限',width:80,align:'left'},
	        {field:'members',title:'成员数',width:80,align:'left',sortable : true},
	        {field:'ctime',title:'创建时间',width:80,align:'left'},
	        {field:'billNum',title:'7天开局数',width:80,align:'left'}
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
		var title = "联盟"+id;
		var url = PATH+'/stat/league/leagueInfo?id='+id;
		TabService.addTab(title,url);
	}

	$scope.clubView = function(id){
		var title = "俱乐部"+id;
		var url = PATH+'/stat/club/clubInfo?id='+id;
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
   $(fm).form('submit',{
             url: url,
             onSubmit: function ()
             {
             },
             success: function(result){
             
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
};

} );












 

