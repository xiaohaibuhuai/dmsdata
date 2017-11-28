MainApp.controller('CheatFinderCtrls',  function($scope,$http) {
	
	parent.$.messager.progress('close');
	　　$("#btn").click(function(){ 
		var ids= $("#ids").val();
		var num=ids.split(",");
		if (num.length < 2) {
           alert("查询伙牌最少需要两名玩家/输入格式有误(检查是否为英文逗号)");
           return;
         }
		if(num.length >=9){
			 alert("查询伙牌不得大于九名玩家");
			 return;
		}
		
		for(var i=0;i<num.length;i++){
			 for(var j=i+1; j<num.length;j++){
				 if(num[i]==num[j]){
					 alert("uuid有重复请重新输入");
					 return;
				 }
			 }
			 
		}
		
		
		$.ajax({
		    url : PATH + "/data/cheatfinder/findCheatByIds?ids="+ids,
		    method : 'POST',
		    dataType : "json",
		    beforeSend : function() {
		      $('#roundModal').hide()
		    },
		    success : function(callback) {
		      $("#loadModal").css("display", "none");
		      if (callback == "1") {
		        alert("ID 不能为空");
		      } else if (callback == "2") {
		        alert("服务出错请联系管理员");
		      } else {
		        // 描述
		        $('#desc').html(callback.desc);

		        // 单人
		        var singleList = callback.single;

		        if (singleList.length == 0) {
		          $('#singleLi').css("display", "none");
		        } else {
		          var scoreList = [];
		          var singleHtml = '';

		          // 单人
		          for (var i = 0; i < singleList.length; i++) {
		            var self = singleList[i].self;
		            // 描述
		            singleHtml += '<div class="result_li_text" style="margin-top:10px;">' + singleList[i].desc + '</div>';
		            var partnerList = singleList[i].partnerList;
		            for (var j = 0; j < partnerList.length; j++) {
		              var partner = partnerList[j];
		              var id = "c" + self.uuid + "_" + partner.uuid;
		              singleHtml += '<div class="username_bg user_bg_line">'
		              singleHtml += '<div class="clearfix">'
		              singleHtml += '<span class="username1">' + self.nickname + '(' + self.uuid + ')</span>'
		              singleHtml += '</div>'
		              singleHtml += '<div class="user_progress_border">'
		              singleHtml += '<div class="user_progress_bgc">'
		              singleHtml += '<div class="user_progress_len" id="' + id + '">'
		              singleHtml += '<img src="' + PATH + '/static/images/transition.png" alt="">'
		              singleHtml += '</div>'
		              singleHtml += '</div>'
		              singleHtml += '</div>'
		              singleHtml += '<div class="clearfix">'
		              singleHtml += '<span class="username2">' + partner.nickname + '(' + partner.uuid + ',同桌数:'
		                  + partner.sameTime + ')</span>'
		              singleHtml += '</div>'
		              singleHtml += '</div>'
		              scoreList.push({
		                id : id,
		                score : partner.score
		              })
		            }
		          }

		          $('#single').html(singleHtml);

		          // 多人
		          var multipleList = callback.multiple;
		          var multipleHtml = "";
		          if (multipleList.length == 0) {
		            $('#multipleLi').css("display", "none");
		          } else {
		            for (var i = 0; i < multipleList.length; i++) {
		              // 描述
		              multipleHtml += '<div class="result_li_text">' + multipleList[i].desc + '</div>'

		              // 作弊信息
		              var coupleList = multipleList[i].coupleList;
		              for (var j = 0; j < coupleList.length; j++) {
		            	       
		                var id = "m" + coupleList[j].a.uuid + "_" + coupleList[j].b.uuid;
		                
		
		                
		                multipleHtml += '<div class="username_bg user_bg_line">';
		                multipleHtml += '<div class="clearfix">';
		                multipleHtml += '<span class="username1">' + coupleList[j].a.nickname + '(' + coupleList[j].a.uuid
		                    + ')</span>';
		                multipleHtml += '</div>';
		                multipleHtml += '<div class="user_progress_border">';
		                multipleHtml += '<div class="user_progress_bgc">';
		                multipleHtml += '<div class="user_progress_len" id="' + id + '">';
		                multipleHtml += '<img src="' + PATH + '/static/images/transition.png" alt="">';
		                multipleHtml += '</div>';
		                multipleHtml += '</div>';
		                multipleHtml += '</div>';
		                multipleHtml += '<div class="clearfix">';
		                multipleHtml += '<span class="username2">' + coupleList[j].b.nickname + '(' + coupleList[j].b.uuid
		                    + ')</span>';
		                multipleHtml += '</div>';
		                multipleHtml += '</div>';
		                scoreList.push({
		                  id : id,
		                  score : coupleList[j].score
		                })
		              }
		            }
		          }
		          $('#multiple').html(multipleHtml);

		          // 设置bar
		          for (var i = 0; i < scoreList.length; i++) {
		            progresslen($('#' + scoreList[i].id), scoreList[i].score);
		          }
		        }

		        $("#selectModal").css("display", "none");
		        $("#resultModal").css("display", "block");
		      }
		    },
		    error : function() {
		      $("#loadModal").css("display", "none");
		      alert("服务出错请联系管理员");
		    }
		  })
		
		
		
		
		
		function blindCountEvent() {
           $("[name = targetUuid]:checkbox").bind("click", function() {
           if ($('input[type=checkbox]:checked').length > 9) {
            $(this).attr("checked", false);
            alert("最多只能查询9名玩家");
            }
          $('#currentSelected').html($('input[type=checkbox]:checked').length);
         })
         }
		
		
		
	　　}); 
});	
		
		
		
		
//	　　　　
//		$('#ff').form('submit',{
//		    url:"/data/cheatfinder/findCheatByIds",
//		    dataType:"json",
//		    onSubmit: function(){
//		       	alert("sss");
//		    },
//		    success:function(result){
//		    	
//		    	alert(result);
//		    	var json = jQuery.parseJSON(result);
//		    	 var single_ = eval(json.single)
//		    	 $scope.single = single_
//		
//	
//		   // 	$("#dg").datagrid('loadData', a);
//		   
//		    	//重载
//		    	$("#dg").datagrid("reload");
//		    }
//		});


