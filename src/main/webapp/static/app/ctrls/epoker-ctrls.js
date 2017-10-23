MainApp.controller('EpokerCtrls',  function($scope,TabService) {
	$.getJSON(PATH+'/stat/epoker/count', {
		dateStart:$("#dateStart").val(),
		dateEnd:$("#dateEnd").val()
	}, 
	function(result) {
		if (result.code==200) {
			 $("#total").html(result.obj.total);
			 $("#etotal").html(result.obj.etotal);
		}else{
			$.messager.alert('提示',result.msg);
		}
	});	
	$('#dataGrid').datagrid({
		url : PATH+'/stat/epoker/list',
		title : '错误牌局列表',
		fit : true,
		fitColumns : true,
		border : false,
		pagination : true,
		idField : 'roomid',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		sortName : 'roomid',
		sortOrder : 'desc',
		checkOnSelect : false,
		selectOnCheck : false,
		nowrap : false,
		striped : true,
		rownumbers : false,
		singleSelect : true,
		toolbar:'#toolbar',
	    frozenColumns : [ [ {
			field : 'roomid',
			title : '牌局ID',
			width : 100,
			sortable : true,
	         formatter:function(value, row, index) {
		     	var str = row.roomid;
		    	 if($scope.so_view)	str= $.formatString('<a ng-show="so_view" href="#"   onclick="$(this).scope().viewFun(\'{0}\');"  >'+row.roomid+'</a>',row.roomid);
		    	return str + '';
		    }
		}] ],
	     columns:[[
					{field:'roomname',title:'牌局名称',width:200}, 
					{field:'gameroomtype', title:'牌局类型',width : 100,
						formatter:function(value){
							if(value==1)return '普通局';
							if(value==2)return 'SNG局';
							if(value==3)return '普通保险局';
							if(value==4)return '6＋局';
							if(value==5)return '奥马哈局';
							if(value==6)return '奥马哈保险局';
						}
					},
					{field : 'bonus',title : '总结果',width : 100},
					{field : 'bonus1',title : '战绩不平',width : 100},
					{field : 'bonus2',title : '保险不平',width : 100}
	    
	      ]],
		onLoadSuccess : function() {
			$('#searchForm table').show();
			parent.$.messager.progress('close');
			$(this).datagrid('tooltip');
		},
		onRowContextMenu : function(e, rowIndex, rowData) {
			e.preventDefault();
			$(this).datagrid('selectRow', rowIndex);
			$('#menu').menu('show', {
				left : e.pageX,
				top : e.pageY
			});
		}
	});

$scope.count = function(){
	$.getJSON(PATH+'/stat/epoker/count', {
		dateStart:$("#dateStart").val(),
		dateEnd:$("#dateEnd").val()
	}, 
	function(result) {
		if (result.code==200) {
			 $("#total").html(result.obj.total);
			 $("#etotal").html(result.obj.etotal);
		}else{
			$.messager.alert('提示',result.msg);
		}
		
		
	});
}
$scope.viewFun=function(id) {
	var title = "牌局"+id;
	var url = PATH+'/stat/poker/pokerInfo?id='+id;
	TabService.addTab(title,url);
};

$scope.searchFun=function() {
	$scope.load();
	$scope.count();
	$('#dataGrid').datagrid('load', $.serializeObject($('#searchForm')));
};

$scope.cleanFun=function() {
	$scope.load();
	$('#searchForm input').val('');
	$scope.count();
	$('#dataGrid').datagrid('load', {});
};

$scope.load = function(){
	parent.$.messager.progress({
		title : '提示',
		text : '数据处理中，请稍后....'
	});
}

} );