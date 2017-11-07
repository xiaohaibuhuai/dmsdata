MainApp.controller('DataIndexCtrls',  [ '$scope', function($scope) {
		//1 获取时间
		$("span.time1").html("3:00");
		$("span.time2").html("5:00"); 
		//2 获取数据
		   //2.1 钻石变化和日志
		$.ajax({
			type: "POST",
			url: "/data/index/diamondChange",
			dataType:"json",
			//data: "name=John&location=Boston",
			success: function(data) {
				$("#d_left_data_data").html(data.task.num);
				$("#d_left_data_log").html(data.log.num);
			},
			statusCode: {500: function() {
             $("#d_left_data_data").html("1000");
				$("#d_left_data_log").html("2000");
				
             }
		}
		});
		
		
		//2 德州币
		$.ajax({
			type: "POST",
			url: "/data/index/coinChange",
			dataType: "json",
			success: function(data) {
				$("#c_left_data_1").html(data.task.num);
				$("#c_left_data_2").html(data.log.num);
			},
			statusCode: {500: function() {
             $("#c_left_data_1").html("500");
				$("#c_left_data_2").html("800");
             }
		}
		});
		
		
		
		//3  MTT门票
		$.ajax({
			type: "POST",
			url: "some.php",
			data: "name=John&location=Boston",
			success: function(msg) {
				alert("Data Saved: " + msg);
			},
			statusCode: {500: function() {
             $("#m_left_data_1").html("200");
				$("#m_left_data_2").html("-20");
             }
		}
		});


} ]);

