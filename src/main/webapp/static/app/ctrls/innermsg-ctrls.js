 MainApp.controller('InnerMsgCtrls', function($scope,$interval,TabService) {
	var  dg = $('#dg').datagrid({
	    url:PATH+'/inner/msg/list',
	    fit:true,
	    fitColumns : true,
		idField : 'id',
		sortName : 'id',
		sortOrder : 'desc',
		striped: true, 
		border : false,
		nowrap:false,
		rownumbers:true,
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
			width : 30,
			sortable : true
		}, {
			field : 'sendTime',
			title : '发送时间',
			width : 140,
			sortable : false
		} ] ],
	     columns:[[
	        {field:'zhmsg',title:'中文内容',width:100,
	        	formatter:function(value, row, index) {
	        		if(value!=null){
	        			var str = ""+value;
		        		if(str.length>15)
		        			str = str.substring(0,15)+"...";
		        		return str;
	        		}else{
	        			return "";
	        		}
	        	}
	      },
	        {field:'enmsg',title:'英文内容',width:100,
	        	formatter:function(value, row, index) {
	        		if(value!=null){
	        			var str = ""+value;
		        		if(str.length>15)
		        			str = str.substring(0,15)+"...";
		        		return str;
	        		}else{
	        			return "";
	        		}
	        	}
	      },
	        {field:'zhtmsg',title:'繁体内容',width:100,
	        	formatter:function(value, row, index) {
	        		if(value!=null){
	        			var str = ""+value;
		        		if(str.length>15)
		        			str = str.substring(0,15)+"...";
		        		return str;
	        		}else{
	        			return "";
	        		}
	        	}
	      },
	      {field:'status', title:'消息状态',width :30,
	        	formatter:function(value){
					if(value==0)return '待执行';
					if(value==1)return '已发送';
					if(value==2)return '已失效';
		
				}
	        },
	        {field:'uuid', title:'操作人UUID',width :50
	        },
	        {field:'action',title:'操作',width:30,
	         formatter:function(value, row, index) {
	     		var str = '';

	    	 if($scope.so_edit && row.status==0)	str += $.formatString('<img ng-show="so_edit"  style="float:left;" onclick="$(this).scope().editFun(\'{0}\');" src="{1}" title="编辑"/>',row.id,PATH+'/static/js/ext/style/images/extjs_icons/pencil.png');
	    	 if($scope.so_delete && row.status==0)	str += $.formatString('<img ng-show="so_delete" style="float:left;" onclick="$(this).scope().deleteFun(\'{0}\');" src="{1}" title="删除"/>',row.id,PATH+'/static/js/ext/style/images/extjs_icons/cancel.png');
	    		return str + '';
	    	}
	        }
	      ]],
	    onLoadSuccess : function() {

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
	parent.$.messager.confirm('询问', '您是否删除此记录？', function(b) {
		if (b) {
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				$.post(PATH+'/inner/msg/delete', {
					id : id
				}, function(result) {
					if (result.code==200) {
						dg.datagrid('reload');
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
		$.ajax({
		    type: "post",
		    dataType: 'json',
		    url: PATH+'/inner/msg/toAddorEdit',
		    data: {},
		    success:function(res){
		  	    loadFrom('#fm',node);
		  	    $('#zhmsg').val(node.zhmsg);
			    $('#enmsg').val(node.enmsg);
			    $('#zhtmsg').val(node.zhtmsg);
		  	    $('#dualToken').val(res.dualToken);
				showDialog('#dlg','编辑消息');
				url=PATH+'/inner/msg/edit';
		    }
		});
	}
};

$scope.addFun=function() {
	$.ajax({
	    type: "post",
	    dataType: 'json',
	    url: PATH+'/inner/msg/toAddorEdit',
	    data: {},
	    success:function(res){
	  	    $('#fm').form('clear');
	  	    $('#dualToken').val(res.dualToken);
	  	    showDialog('#dlg','添加消息');
	  	    url=PATH+'/inner/msg/add';
	    }
	});
};


$scope.submit=function(fm,dlg){
	  if($('#zhmsg').val()=='' && $('#enmsg').val()=='' && $('#zhmtsg').val()==''){
		  alert("消息不能为空");
		  return;
	  }
	  $.messager.confirm("提示", "确定发送牌局内消息", function(flag) {
		  if(flag){
			  TabService.load();
			  $(fm).form('submit',{
	                url: url,
	                onSubmit: function ()
	                {
	                },
	                success: function(result){
	                	parent.$.messager.progress('close');
	                	result= $.parseJSON(result);
	                	if(result.code==200){
	                		$(dlg).dialog('close'); 
	                		dg.datagrid('reload');
	                	}else{
	                	    $.messager.alert('提示',result.msg);
	                	    $(dlg).dialog('close'); 
	                		dg.datagrid('reload');
	                	}
	             }
	       });
		  }
	  });
};

//$interval( function(){
//	dg.datagrid('reload');
//}, 60000);

});