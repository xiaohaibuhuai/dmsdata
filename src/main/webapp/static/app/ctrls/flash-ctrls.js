MainApp.controller('FlashBatchInfoCtrls', function($scope,TabService) {
	var  dg = $('#dg').datagrid({
	    url:PATH+'/banner/flash/list',
	    fit:true,
	    fitColumns : true,
		idField : 'id',
		striped: true, 
		border : false,
		nowrap:false,
		rownumbers:true,
		singleSelect:true,
	    pagination : true,
	    checkOnSelect : false,
		selectOnCheck : false,
		pageSize : 20,
	    pageList : [ 20, 30, 40, 50],
	    toolbar : '#toolbar',
	    columns:[[
			{field:'id', title:'ID',width:30},
			{field:'title', title:'标题',width : 80},
			{field:'ctime', title:'创建时间',width:80},
			{field:'uuid', title:'创建人',width:50},
			{field:'languagetype', title:'语言',width:30,
	        	formatter:function(value){
					if(value==1)return '中文版';
					if(value==2)return '国际版';
					if(value==3)return '繁体版';
				}
	        },
	        {field:'jumpUrl', title:'跳转URL',width : 100},
	        {field:'jumpTargetUrl', title:'统计URL',width : 100},
	        {field:'startTimes', title:'启动次数',width:50},
	        {field:'beginTime', title:'生效时间',width:80},
	        {field:'endTime', title:'过期时间',width:80},
	        {field:'action',title:'操作',width:50,
	         formatter:function(value, row, index) {
	     		var str = '';
	    	 if($scope.so_edit)	str += $.formatString('<img ng-show="so_edit"  style="float:left;" onclick="$(this).scope().editFun(\'{0}\');" src="{1}" title="编辑"/>',row.id,PATH+'/static/js/ext/style/images/extjs_icons/pencil.png');
	    	 if($scope.so_del)str += $.formatString('<img ng-show="so_del" style="float:left;margin-left: 4px;" onclick="$(this).scope().del(\'{0}\');" src="{1}" title="删除"/>',row.id,PATH+'/static/js/ext/style/images/extjs_icons/cancel.png');
	    	 if($scope.so_reset)str += $.formatString('<img ng-show="so_reset" style="float:left;margin-left: 8px;" onclick="$(this).scope().reset(\'{0}\');" src="{1}" title="重置为原URL"/>',row.id,PATH+'/static/js/ext/style/images/extjs_icons/pencil_go.png');
	    	 if($scope.so_set) str += $.formatString('<img ng-show="so_reset" style="float:left;margin-left: 4px;" onclick="$(this).scope().set(\'{0}\');" src="{1}" title="设置可记录次数"/>',row.id,PATH+'/static/js/ext/style/images/extjs_icons/pencil_go.png');
	    		return str + '';
	    	}
	        }
	      ]],
		onLoadSuccess : function() {
			$('#searchForm table').show();
			parent.$.messager.progress('close');
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
	
	$scope.searchFun=function() {
		dg.datagrid('load', $.serializeObject($('#searchForm')));
	};
	$scope.cleanFun=function() {
		$('#searchForm input').val('');
		dg.datagrid('load', {});
	};

	$scope.excel=function(){
		url=PATH+'/banner/flash/excel';
		$('#searchForm').form('submit',{url:url});
	};
	
	$scope.editFun=function(id) {
		if (id != undefined) dg.datagrid('selectRecord', id);
		var node = dg.datagrid('getSelected');
		if (node) {
			$('#id').val(node.id);
			loadFrom('#fm',node);
			showDialog('#dlg','编辑闪屏设置');
			url=PATH+'/banner/flash/edit';
		}
	};
	$scope.addFun=function() {
		  $('#fm').form('clear');
	 	  $('#id').val("");
	 	  showDialog('#dlg','添加闪屏设置');
	 	  url=PATH+'/banner/flash/add';
	};
	$scope.submit=function(fm,dlg){
		 if(!$("#fm").form('validate')) return ;
		 //对路径进行编码
		 $("#encodeJumpUrl").val(encodeURI(escape($("#jumpUrl").val())));
		 var msg = "";
	     if(url == "/banner/flash/add"){
	    	 msg = "确定添加标题为<font color=red> "+ $('#title').val() +"</font>语言类型为<font color=red>"+ $('#languagetype').combobox('getText')+"</font>的闪屏设置";
		 }else{
			 msg = "确定修改标题为<font color=red> "+ $('#title').val() +"</font>语言类型为<font color=red>"+ $('#languagetype').combobox('getText')+"</font>的闪屏设置";
		 }
		 $.messager.confirm("提示", msg, function(flag) {
			  if(flag){
				  $scope.load();
				  $(fm).form('submit',{
		                url: url,
		                onSubmit: function ()
		                {
		                },
		                success: function(result){
		                parent.$.messager.progress('close');
		                data= $.parseJSON(result);
						if (data.state == 0) {
							$.messager.alert('提示','设置闪屏成功');
							$(dlg).dialog('close'); 
			                dg.datagrid('reload');
						} else {
							if(data.state == 1){
								$.messager.alert('提示','闪屏设置时间段有交叉');
							}else if(data.state == 2){
								$.messager.alert('提示','闪屏入库失败');
							}else if(data.state == 3){
								$.messager.alert('提示','闪屏设置失败');
							}else if(data.state == 4){
								$.messager.alert('提示','闪屏设置异常');
							}else{
								$.messager.alert('提示',data.msg);
							}
						}
		             }
		        });		
			  }
		});
	};

	$scope.del =function(id){
		if (id != undefined)dg.datagrid('selectRecord', id);
		var node = dg.datagrid('getSelected');
		if (node) {
	        var msg = '确认删除该闪屏';
			parent.$.messager.confirm('询问', msg, function(b) {
				if (b) {
					    $scope.load();
						$.post(PATH+'/banner/flash/del', {
							id : id
						}, function(result) {
							parent.$.messager.progress('close');
							if (result.code==200) {
								dg.datagrid('reload');
							}
						}, 'JSON');
				}
			});
	   }
	};

	$scope.reset=function(id){
		if (id != undefined)dg.datagrid('selectRecord', id);
		var node = dg.datagrid('getSelected');
		if (node) {
	        var msg = '是否恢复原始的跳转地址？';
			parent.$.messager.confirm('询问', msg, function(b) {
				if (b) {
					    $scope.load();
						$.post(PATH+'/banner/flash/reset', {
							id : id
						}, function(result) {
							if (result.code==200) {
								dg.datagrid('reload');
							}
							parent.$.messager.progress('close');
						}, 'JSON');
				}
			});
	  }
	};
	
	$scope.set=function(id){
		if (id != undefined)dg.datagrid('selectRecord', id);
		var node = dg.datagrid('getSelected');
		if (node) {
	        var msg = '是否设置跳转URL可记录次数？';
			parent.$.messager.confirm('询问', msg, function(b) {
				if (b) {
					    $scope.load();
						$.post(PATH+'/banner/flash/set', {
							id : id
						}, function(result) {
							if (result.code==200) {
								dg.datagrid('reload');
							}
							parent.$.messager.progress('close');
						}, 'JSON');
				}
			});
	  }
	};
	
	
	
	$scope.uploadImg = function(imgFile,input,imgUrl){
		fileName = input.value;
		$scope.load();
		$.ajaxFileUpload({
			url : '/file/upload/imageUpload?imgFile='+imgFile,   //提交的路径
			secureuri : false, // 是否启用安全提交，默认为false
			fileElementId : imgFile, // file控件id
			dataType : 'json',
			data : {
				fileName : fileName //传递参数，用于解析出文件名
			}, // 键:值，传递文件名
			success : function(data, status) {
				parent.$.messager.progress('close');
				if (data.error == 0) {
					var src = data.src;
					$('#'+ imgUrl).val(src);   
				} else {
					alert(data.message);
				}
			},
			error : function(data, status) {
				alert(data.message);
			}
		});
	}
	//预览图片
	$scope.ViewImg=function(src,title) {
		  if(src == null || src == ""){
			  alert("图片为空");
			  return;
		  }
		  $('#show').html("<img style='width:auto,height:auto' src='" + src + "'>");
	 	  showDialog('#ImgView',title);
	};
	
	$scope.load = function(){
		parent.$.messager.progress({
			title : '提示',
			text : '数据处理中，请稍后....'
			});
	}

});