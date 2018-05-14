/*
 *实现对象到货性取值 
 * * DEMO
 * * 在初始化的时候添加  columnOption
 * J("#datagrid").datagrid({
		    columnOption:(J.fn.datagrid.defaults.columnOption)("#datagrid"),           添加这一行
   });
 */

$.fn.datagrid.defaults.columnOption=function(datagrid){
			if(datagrid==undefined)
			{
				throw new TypeError("检测到 datagrid 的 ID 为空")
			}
			if(datagrid.indexOf('#')<0)
			{
				throw new TypeError("检测到 datagrid 的 ID 不含 ‘#’ 前缀")
			}
			$.parser.onComplete=function(){
				var columns=$(datagrid).datagrid("options").columns;
				console.info(columns);
				if(columns!=undefined)
				{
					for(var i=0 ; i<columns.length ;i++)
					{
						var fileds=columns[i];
						for(var j=0;j<fileds.length;j++)
						{
							var filed=fileds[j]
							if(!filed.hasOwnProperty("formatter")||filed.formatter==undefined)
							{
								
								filed.formatter=function (value, row) {
									var fieldSplit=this.field.split("\.");
									if(fieldSplit.length==1)
									{
										return value;
									}
									else if(fieldSplit.length==2)
									{
										var parent=fieldSplit[0];
										var child=fieldSplit[1];
										return row[parent]==undefined||row[parent][child]==undefined?"":row[parent][child];
									}
			                    }
							}
							else
							{
								continue;
							}
						}
					}
				}
			}
};
$.fn.datebox.defaults.formatterTimestamp = function(date){
	var y = date.getFullYear();
	var m = date.getMonth()+1;
	var d = date.getDate();
	var H = date.getHours();
	var M = date.getMinutes();
	var s = date.getSeconds();
	return y+"-"+(m>9?m:"0"+m)+"-"+(d>9?d:"0"+d)+" "+(H>9?H:"0"+H)+":"+(M>9?M:"0"+M)+":"+(s>9?s:"0"+s)
};
$.fn.datebox.defaults.formatterDate = function(date,param){
	var ms=[1,2,3,4,5,6,7,8,9,10,11,12];
	var y = date.getFullYear();
	var m = date.getMonth()+1;
	var d = date.getDate();
	var H = date.getHours();
	var M = date.getMinutes();
	var s = date.getSeconds();
	if(param&&param.year)
	{
		//var time=date.getTime();
		y+=param.year;
	}
	if(param&&param.month)
	{
		var subtractYear=param.month<0?Math.floor(((param.month+m==0)?-1:(param.month+m)%ms.length==0?(param.month+m-1):(param.month+m))/ms.length):Math.floor(param.month/ms.length);
		y+=subtractYear;
		var subtractMonth=(param.month+m)%ms.length;
		m=ms[subtractMonth<0?ms.length+subtractMonth-1:subtractMonth==0?ms.length-1:subtractMonth-1];
	}
	if(param&&param.day)
	{
		var time=date.getTime(); 
		time+=(param.day*1000*60*60*24);
		date=new Date(time);
		y = date.getFullYear();
		m = date.getMonth()+1;
		d = date.getDate();
	}
	if(param&&param.time)
	{
		return y+"-"+m+"-"+d + " " +param.time
	}
	return y+"-"+m+"-"+d
};
$.extend($.fn.validatebox.defaults.rules, {
	EndDateGreatThanEuqalToStartDate: {
        validator: function(value, param){
        	console.info(param);
        	var data1=$(param[0]).datebox('getValue')
        	var action=param[1];
        	var data2=$(param[2]).datebox('getValue')
        	var current=param[3];
        	return true;
        	if(data1==""||data2=="")
        	{
        		return true;
        	}
        	if(action=="eq")
        	{
        		
        	}
        	else if (action=="gt&eq")
        	{
        		var endTime=new Date(data1)
        		endTime=endTime.getTime();
        		var startTime=new Date(data2)
        		startTime=startTime.getTime();
        		if(current==param[0])
        		{
        			
        		}
        		else if(current==param[2])
        		{
        			
        		}
        		return endTime-startTime>=0?true:false;
        	}
        	else if (action=="lt&eq")
        	{
        		
        	}
        	return false;
           
        },
        message: '活动结束时间应该大于活动开始时间'
    }
});

function formatDuring(mss, type) {
    if (type == 1) {
        var days = mss / (1000 * 60 * 60 * 24);
        var hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        var minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        var seconds = (mss % (1000 * 60)) / 1000;
        return days + "天" + hours + "时" + minutes + "分" + seconds + "秒";
    } else {
        var hours = parseInt(mss / (1000 * 60 * 60));
        if (hours < 1) {
            var minutes = parseInt(mss / (1000 * 60));
            if (minutes < 1) {
                var seconds = parseInt(mss / 1000);
                return seconds + "秒";
            } else {
                var seconds = parseInt((mss % (1000 * 60)) / 1000);
                return minutes + "分" + seconds + "秒";
            }
        } else {
            var minutes = parseInt((mss % (1000 * 60 * 60)) / (1000 * 60));
            var seconds = parseInt((mss % (1000 * 60)) / 1000);
            return hours + "时" + minutes + "分" + seconds + "秒";
        }

    }
}
window.LiveCheckLog={
    type:{
        '我拍用户':'wo_pai_yong_hu',
        '用户投诉管理':'yong_hu_tou_su',//直播、封面、头像、背景、动态
        '直播':'live',
        '封面':'room',
        '头像':'head',
        '背景':'home',
        '动态':'dynamic',
        '我拍用户':'wo_pai_yong_hu',
        '附近头像':'near_head',
        '直播投诉':'live_complain',
        '直播管理':'live_manage',
        '直播详情':'live_detail',
		'实名认证':'realAuth',
        '':'',
        undefined:'',
        view:{
            'yong_hu_tou_su':'用户投诉管理',//直播、封面、头像、背景、动态
            'live':'直播',
            'room':'封面',
            'head':'头像',
            'home':'背景',
            'dynamic':'动态',
            'near_head':'附近头像',
            'live_complain':'直播投诉',
            'live_manage':'直播管理',
            'live_detail':'直播详情',
			'realAuth':'实名认证',
            '':'',
            undefined:'',
        },
        comboxData:function () {
            var data=[];
            for(item in this.view){
                if(item!=""&&item!=undefined){
                    data.push({value:item,text:this.toView(item)});
                }
            }
            data.push({value:"",text:"请选择",selected:true});
            return data;
        },
        toView:function (value) {
            return this.view[value];
        }

    }
}