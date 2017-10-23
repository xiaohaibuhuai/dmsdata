 
MainApp.controller('NoticeCtrls', function($scope,TabService) {

	var  dg = $('#dg').datagrid({
	    url:PATH+'/msg/notice/list',
	    fit:true,
	    fitColumns : true,
		idField : 'id',
		sortName : 'id',
		sortOrder : 'desc',
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
			title : 'ID',
			width : 50,
			sortable : true
		}] ],
	     columns:[[
	            {field:'content',title:'公告内容',width:500},
	   	        {field:'status', title:'是否发布',width:100,
	   	        	formatter:function(value){
	   					if(value==1)return '已发布';
	   					if(value==2)return '未发布';
	   			
	   				}
	   	        },
	        {field:'action',title:'操作',width:100,
	         formatter:function(value, row, index) {
	     		var str = '';

	    	 if($scope.so_edit)	str += $.formatString('<img ng-show="so_edit"  style="float:left;" onclick="$(this).scope().editFun(\'{0}\');" src="{1}" title="编辑"/>',row.id,PATH+'/static/js/ext/style/images/extjs_icons/pencil.png');
	    	 if($scope.so_delete)	str += $.formatString('<img ng-show="so_delete" style="float:left;" onclick="$(this).scope().deleteFun(\'{0}\');" src="{1}" title="删除"/>',row.id,PATH+'/static/js/ext/style/images/extjs_icons/cancel.png');
	    		return str + '';
	    	}
	        }
	      ]],
	    onLoadSuccess : function() {
			//$('#searchForm table').show();
			parent.$.messager.progress('close');
			$(this).datagrid('tooltip');
			
	 
		},
		onRowContextMenu : function(e, rowIndex, rowData) {
			e.preventDefault();
			$(this).datagrid('unselectAll').datagrid('uncheckAll');
			$(this).datagrid('selectRow', rowIndex);
			$('#menu').menu('show', {
				left : e.pageX,
				top : e.pageY
			});
		}
	});
	
	


$scope.deleteFun=function(id) {
	if (id == undefined) {//点击右键菜单才会触发这个
		var rows = dg.datagrid('getSelections');
		id = rows[0].id;
	} else {//点击操作里面的删除图标会触发这个
		dg.datagrid('unselectAll').datagrid('uncheckAll');
	}
	if (id != undefined)dg.datagrid('selectRecord', id);
	var node = dg.datagrid('getSelected');
	parent.$.messager.confirm('询问', '您是否要删除此公告？', function(b) {
		if (b) {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				$.post(PATH+'/msg/notice/delete', {
					id : id
				}, function(result) {

	                if(result.code==200){
	                   dg.datagrid('reload');
	                }
	                else {
	                  $.messager.alert('提示',result.msg);
	                }
					parent.$.messager.progress('close');
				}, 'JSON');
				
			
		}
	});
};



$scope.editFun=function(id) {
	
	if (id != undefined)dg.datagrid('selectRecord', id);
	var node = dg.datagrid('getSelected');
	if (node) {
		loadFrom('#fm',node);
		var content = node.content;
//		alert(content);
		var reg=new RegExp("<br>","g");
		var reg1=new RegExp("&nbsp;","g");
		content = content.replace(reg,"");
		content = content.replace(reg1," ");
//		alert(content);
		$('#content').val(content);
		showDialog('#dlg','编辑公告');
		url=PATH+'/msg/notice/edit';
	}
};



$scope.addFun=function() {
	  $('#fm').form('clear');
	  showDialog('#dlg','添加公告');
	  url=PATH+'/msg/notice/add';
};


$scope.submit=function(fm,dlg){
	  if(!$(fm).form('validate')) return ; 
	 
	  var content = $('#content').val();
//	  alert(content);
	  var reg=new RegExp("\n","g");
	  var reg1=new RegExp(" ","g");
	  content = content.replace(reg,"<br>");
	  content = content.replace(reg1,"&nbsp;");
//	  alert(content);
	  $('#content').val(content);
      $(fm).form('submit',{
                url: url,
                onSubmit: function ()
                {
            		//$('#des').val('111');
                },
                success: function(result){
                	result= $.parseJSON(result);
                    if(result.code==200){
                      $(dlg).dialog('close'); 
                      dg.datagrid('reload');
                   }
                   else {
                     $.messager.alert('提示',result.msg);
                   }
             }
       });		
};





} );

