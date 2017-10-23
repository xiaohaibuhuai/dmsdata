 
MainApp.controller('ReferCtrls', function($scope,TabService) {

	var  dg = $('#dg').datagrid({
	    url:PATH+'/partner/refer/list?id='+$('#id').val(),
	    fit:true,
	    fitColumns : true,
		idField : 'id',
		sortName : 'id',
		sortOrder : 'asc',
		striped: true, 
		border : false,
		nowrap:false,
		rownumbers:false,
		singleSelect:true,
	    pagination : true,
	    checkOnSelect : false,
		selectOnCheck : false,
		pageSize : 15,
	    pageList : [15, 20, 30, 40, 50],
	    toolbar : '#toolbar',
	    frozenColumns : [ [ {
			field : 'id',
			title : '推广码',
			width : 50,
			sortable : false
		}, {
			field : 'rfsrc',
			title : '渠道号',
			width : 200,
			sortable : false
		} ] ],
	     columns:[[
	        {field:'phone', title:'推广员手机',width : 100},
	        {field:'rank',title:'推广员等级',width:100}
	  
	      ]],
	    onLoadSuccess : function() {
			parent.$.messager.progress('close');
			
	 
		},
		onRowContextMenu : function(e, rowIndex, rowData) {
			e.preventDefault();
			$(this).datagrid('unselectAll').datagrid('uncheckAll');
			$(this).datagrid('selectRow', rowIndex);
	
		}
	});
	
	










} );

