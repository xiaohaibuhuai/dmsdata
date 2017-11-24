MainApp.controller('SinglePokerCtrls',  function($scope,$http) {
	
	parent.$.messager.progress('close');
	　　$("#btn").click(function(){ 
		var roomid= $("#roomid").val();
		var handid= $("#handid").val();
		
		
		if(roomid==null||roomid.indexOf(" ")!=-1||handid==null||handid.indexOf(" ")!=-1){
			alert("不能为空,请重新输入");
			return;
		}
		
		
		
		
		$.ajax({
		    url : PATH + "/data/query/cash/singlepoker/execute?roomid="+roomid+"&handid="+handid,
		    method : 'POST',
		    dataType : "json",
		    beforeSend : function() {
		    },
		    success : function(callback) {
		      if (callback == "0") {
		        alert("搜索无结果");
		        $("#result").html("搜索无结果");
		      } else if(callback =="1"){
		    	    alert("ELK错误");
		      }else if (callback == "2") {
		        alert("服务出错请联系管理员");
		      } else {
		    	  var sign="============";
		    	  
		    	  
		    	  var head =sign+"牌局:"+callback.roomname+",牌局ID:"+callback.roomid+",手数ID:"+callback.handid+sign+"<br/>"
		    	  $.each(callback.before,function(n,value) {   
		    		  var item = ""; 
		    		  
		    		  var v=""
			    		  if(value.value!=null){
			    			  v=value.value;
			    		  }
		          item+=value.time+": "+value.nickName+"(ID:"+value.uuid+")"+" "+value.key+": "+v+"<br/>"
		          head +=item;  
		    	  });
		    	  
		    	  
		    	  $("#result").html(head);
		    	  
		    	  
		    	  
		    	  //flop发牌
		    	  if(callback.flopPokerDes!=null){
		    		  var result = sign+"Flop发牌:"+callback.flopPokerDes+sign+"<br/>";
		    		  if(callback.flop!=null){
		    			  var insresult="0";
		    		  $.each(callback.flop,function(n,value) {   
			    		  var item = ""; 
			    		  var v="";
			    		  if(value.value!=null){
			    			  v=value.value;
			    		  }
//			    		  item+=value.time+": "+value.nickName+"(ID:"+value.uuid+")"+" "+value.key+": "+v+"<br/>";
//			    		  result +=item; 
			          if(value.key=="碎保险"){
			        	  insresult+=value.time+": "+value.nickName+"(ID:"+value.uuid+")"+" "+value.key+": "+v+"<br/>";
			          }else{
			          
			          item+=value.time+": "+value.nickName+"(ID:"+value.uuid+")"+" "+value.key+": "+v+"<br/>";
			          if(value.key=="买保险"&&insresult!="0"){
			            	        	 item+=insresult;
			           }
			          result +=item; 
			          }
			           
			    	  });  
		    		  }
		    		  $("#result").append(result);
		    	  }
		    	  
		    	  //turn 发牌
		    	  if(callback.turnPokerDes!=null){
		    		 
		    		  var result = sign+"Turn发牌:"+callback.turnPokerDes+sign+"<br/>";
		    		  if(callback.turn!=null){
		    		  $.each(callback.turn,function(n,value) {   
			    		  var item = ""; 
			    		  var v=""
				    		  if(value.value!=null){
				    			  v=value.value;
				    		  }
			    		  var insresult="0";
			    		  if(value.key=="碎保险"){
				        	  insresult+=value.time+": "+value.nickName+"(ID:"+value.uuid+")"+" "+value.key+": "+v+"<br/>";
				          }else{
				          
				          item+=value.time+": "+value.nickName+"(ID:"+value.uuid+")"+" "+value.key+": "+v+"<br/>";
				          if(value.key=="买保险"&&insresult!="0"){
				        	
				            	        	 item+=insresult;
				           }
				          result +=item; 
				          }
			    		  
//			          item+=value.time+": "+value.nickName+"(ID:"+value.uuid+")"+" "+value.key+": "+v+"<br/>"
//			          result +=item;  
			    	  });  
		    		  }
		    		  $("#result").append(result);
		    	  }
		    	   
		    	  
		    	  //river发牌
		    	  
		    	  if(callback.riverPokerDes!=null){
		    		  var result = sign+"River发牌:"+callback.riverPokerDes+sign+"<br/>";
		    		  if(callback.river!=null){
		    		  $.each(callback.river,function(n,value) {   
			    		  var item = ""; 
			    		  var v=""
				    		  if(value.value!=null){
				    			  v=value.value;
				    		  }
			    		  
			    		  var insresult="0";
			    		  if(value.key=="碎保险"){
				        	  insresult+=value.time+": "+value.nickName+"(ID:"+value.uuid+")"+" "+value.key+": "+v+"<br/>";
				          }else{
				          
				          item+=value.time+": "+value.nickName+"(ID:"+value.uuid+")"+" "+value.key+": "+v+"<br/>";
				          if(value.key=="买保险"&&insresult!="0"){
				        	
				            	        	 item+=insresult;
				           }
				          result +=item; 
				          }
//			          item+=value.time+": "+value.nickName+"(ID:"+value.uuid+")"+" "+value.key+": "+v+"<br/>"
//			          result +=item;  
			    	  });   
		    		  }
		    		  $("#result").append(result); 
		    	  }
		    	 
		    	  //结果
		    	  if(callback.showDownDes!=null){
		    	  var result = sign+"结算"+sign+"<br/>";
		    	  $.each(callback.result,function(n,value) {   
		    		  var item = ""; 
		    		  var v=""
			    		  if(value.value!=null){
			    			  v=value.value;
			    		  }
		          item+=value.time+": "+value.nickName+"(ID:"+value.uuid+")"+" "+value.key+": "+v+"<br/>"
		          result +=item; 
		    		  
		    	  }); 
		    	  $("#result").append(result); 
		    	  
		    	  }
		    	  
		    	  //手牌
		    	  if(callback.handPokerDes!=null){
		    	  var result = sign+callback.handPokerDes+sign+"<br/>";
		    	  $.each(callback.handPoker,function(n,value) {   		              
		              var item = ""; 
		              if(value.key=="手牌"){
		            	  item+=value.time+": "+value.nickName+"(ID:"+value.uuid+")"+" "+value.key+": "+value.value+"<br/>";
			              result +=item;  
		              }    
		              });  
		      $("#result").append(result); 
		      
		    	  }
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


