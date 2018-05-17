var territory = {
    initStartDate:new Date(new Date()-7*24*3600*1000), //默认开始时间
    initEndDate:new Date(),  //默认结束时间
    tablePage:null, //table 分页当前页
    perPageItem:null, //每页显示条数
    totalPage:2, //总页数
    init:function () {
        this.dmsDate();
        this.currentPage(0,this.totalPage); //1是变量 this.totalPage 后台需要返回;
        this.pageJump();
        fullView();
        fullTable();
    },
    //    日期插件
    dmsDate:function () {
        var  _this = this;
        if($('#dms_date').length > 0){
            $('#dms_date').daterangepicker({
                "locale": {
                    "direction": "ltr",
                    "format": "MM/DD/YYYY",
                    "separator": " - ",
                    "applyLabel": "确定",
                    "cancelLabel": "取消",
                    "fromLabel": "From",
                    "toLabel": "To",
                    "customRangeLabel": "Custom",
                    "daysOfWeek": [
                        "日",
                        "一",
                        "二",
                        "三",
                        "四",
                        "五",
                        "六"
                    ],
                    "monthNames": [
                        "一月",
                        "二月",
                        "三月",
                        "四月",
                        "五月",
                        "六月",
                        "七月",
                        "八月",
                        "九月",
                        "十月",
                        "十一月",
                        "十二月"
                    ],
                    "firstDay": 1
                },
                "maxDate":new Date(),
                "linkedCalendars": false,
                "startDate": _this.initStartDate,
                "endDate": _this.initEndDate
            }, function(start, end, label) {
                console.log( start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') );
            });
            //添加按钮周月按钮
            $('.range_inputs').prepend('<button id="week" class="week btn btn-sm" type="button">周</button>' +
                '<button id="month" class="month btn btn-sm" type="button">月</button>');
            _this.addDmsDateBtn();
        }
    },
    //添加按钮周月按钮事件
    addDmsDateBtn:function () {
        var _this = this;
        $('#week').click(function () {
            if(_this.initStartDate.getDate() == _this.initEndDate.getDate() || _this.initStartDate.getMonth() != _this.initEndDate.getMonth()){
                _this.initStartDate = new Date(new Date()-7*24*3600*1000);
                $(document).off('mousedown');
                _this.dmsDate(); //初始化日期插件
            }else {
                _this.dmsDate();
            }
        });
        $('#month').click(function () {
            if(_this.initStartDate.getMonth() == _this.initEndDate.getMonth()){
                _this.initStartDate = new Date(new Date()-30*24*3600*1000);
                $(document).off('mousedown');
                _this.dmsDate(); //初始化日期插件
            }else {
                _this.dmsDate();
            }
        });
    },
    //分页插件
    currentPage:function (current,tableTotalPage) {
        var _this = this;
        $('#pagination').pagination(tableTotalPage,{
            prev_text:'上一页', //上一页
            next_text:'下一页', //下一页
            current_page:current,  //当前页
            items_per_page:1,
            callback: function (page_index,jq) { //获取数据后的徽调函数
                _this.tablePage = page_index;
                $('#currentPage').text(page_index + 1);
                $('.totalPage').text(tableTotalPage);
                //跳到首页
                if(page_index ==0 ){
                    $('#pageFirst').css('color','#999').addClass('disabled');
                }else {
                    $('#pageFirst').css('color','#12a0ff').removeClass('disabled');
                }
                //跳到尾页
                if(page_index == (tableTotalPage-1) ){
                    $('#pageLast').css('color','#999').addClass('disabled');
                }else {
                    $('#pageLast').css('color','#12a0ff').removeClass('disabled');
                }
                fullTable();
            }
        });
    },
    //分页跳转
    pageJump:function () {
        var _this = this;
        // 页面跳转到第几页
        $('#jumpPageBtn').click(function () {
            var jumpPage = $('#jumpPageInput').val();
            if( jumpPage <= _this.totalPage&& /(^[1-9]\d*$)/.test(jumpPage)){
                _this.currentPage(jumpPage-1,_this.totalPage);
            }else {
                alert('页码超出范围');
            }

        });
        //跳到首页
        $('#pageFirst').click(function () {
            _this.currentPage(0,_this.totalPage)
            fullTable();
        });
        //跳到尾页
        $('#pageLast').click(function () {
            _this.currentPage(_this.totalPage-1,_this.totalPage)
            fullTable();
        });
    },
    //每页显示条数设置
    PageItem:function () {
        var _this = this;
        $('#perPageItems').change(function () {
            _this.perPageItem = $('#perPageItems').val();
            _this.currentPage(0,_this.totalPage)
        });
    }
};

//输出时间值
$('#dms_date').change(function () {
    $("#currentPage").text(1);
    console.log('开始时间'+netStatus.startDate);
    fullView();
    fullTable();
});

window.onload =function () {
    //    国内国外点击样式切换
    // frame.btnActive('.dms-btn-regional');
    //    新增活跃点击样式切换
    territory.addDmsDateBtn();
    frame.btn2Active('.dms-btn-date');
    // 联网方式和运营商点击table切换
    // $('#networking_table_btn').click(function () {
    //
    //     $('#networking_table').show();
    //     $('#networking_table_title').show();
    //     $('#operators_table').hide();
    //     $('#operators_table_title').hide();
    //     fullView();
    //     fullTable();
    // });
    // $('#operators_table_btn').click(function () {
    //     $('#networking_table').hide();
    //     $('#networking_table_title').hide();
    //     $('#operators_table').show();
    //     $('#operators_table_title').show();
    //     fullView();
    //     fullTable();
    // });
    window.netStatus= {
        myChart : echarts.init(document.getElementById('dms-chart'))
    };

    window.netStatus.option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },//5-10修改
        grid: {
            left: '0%',
            right: '3%',
            bottom: '3%',
            top:'8%',
            containLabel: true
        },//5-10修改end
        xAxis: {
            type: 'value',
            boundaryGap: [0, 0.01]
        },
        yAxis: {
            type: 'category',
            data: ['中国移动','中国联通','中国电信','Verizon','Carrier','KDDI','DiGi']
        },
        series: [
            {
                name: '用户人数',
                type: 'bar',
                data: [23, 89, 34, 70, 44,23, 89]
            }

        ],
        color:['rgb(10,160,245)']
    };
    window.netStatus.myChart.setOption(window.netStatus.option);
    //页面初始化
    territory.init();
};

function fullTable(sortField,order) {

    // var type = $(".dms-btn.active.dms-btn-date").val();
    var uri;
    // if (type == "net"){
    //     uri = "/terminal/network/getNetList"+getPageParamter(getHeardParamter(),sortField,order);
    // } else {
        uri = "/terminal/network/getOperatorList"+getPageParamter(getHeardParamter(),sortField,order);
    // }
    $.get(uri,function (result) {
        var total = result.total;
        var perPageItems = $('#perPageItems').val();
        var totalPage = Math.ceil(total/perPageItems);
        territory.totalPage = totalPage;
        $('.totalPage').text(totalPage);
        var rows = result.rows;
        // if (type == "net"){
        //     $("#netTerminal").empty();
        //     for (var i in rows){
        //         $("#netTerminal").append(
        //             "<tr>" +
        //             "<td>"+rows[i].date+"</td>" +
        //             "<td>"+rows[i].net_type+"</td>" +
        //             "<td>"+rows[i].regist_user_num+"</td>" +
        //             "<td>"+rows[i].active_user_num+"</td>" +
        //             "<td>"+rows[i].total_user_num+"</td>" +
        //             "</tr>");
        //     }
        // } else {
            $("#netWorkTerminal").empty();
            for (var i in rows){
                $("#netWorkTerminal").append(
                    "<tr>" +
                    "<td>"+rows[i].date+"</td>" +
                    "<td>"+rows[i].operator+"</td>" +
                    "<td>"+rows[i].total_user_num+"</td>" +
                    "<td>"+rows[i].active_user_num+"</td>" +
                    "</tr>");
            }
        // }
        if ($("#currentPage").text()==totalPage){
            $(".next").replaceWith("<span class=\"current next\">下一页</span>");
        }else {
            $(".next").replaceWith("<a href=\"#\" class=\"next\">下一页</a>");
        }
    });

}

function getHeardParamter() {
    // 时间控件的值
    var dataArr = $("#dms_date").val().split(" - ")
    // 开始时间和结束时间
    var start_time = new Date((new Date(dataArr[0])).getTime()).Format("yyyy-MM-dd");
    var end_time = new Date((new Date(dataArr[1])).getTime()).Format("yyyy-MM-dd");
    $("#start").text(start_time.replace("-",".").replace("-","."));
    $("#end").text(end_time.replace("-",".").replace("-","."));
    // 获得 国内外的值
    var is_abroad = $(".dms-btn.active.dms-btn-regional").val()
    var param = "?start_time="+start_time+"&end_time="+end_time;
    if (is_abroad!=null&&is_abroad!=undefined&&is_abroad!=""){
        param += "&is_abroad="+is_abroad;
    }
    return param;
}


function  getPageParamter(para,sortField,order){
    // 第几页
    var page = $('#currentPage').text();
    // 每页显示条数
    var perPageItems = $('#perPageItems').val();
    if(page!=null&&page!=undefined){
        para += "&page="+page;
    };
    if(perPageItems!=null&&perPageItems!=undefined){
        para += "&total="+perPageItems;
    };
    if(sortField!=null){
        para = para +"&sortField="+sortField+"&order="+order;
    }else {
        // 默认按时间排序
        para = para +"&sortField=date&order=desc";
    }
    return para;
}


// 日期格式化
Date.prototype.Format = function(fmt) {
    var o = {
        "M+" : this.getMonth() + 1, // 月份
        "d+" : this.getDate(), // 日
        "h+" : this.getHours(), // 小时
        "m+" : this.getMinutes(), // 分
        "s+" : this.getSeconds(), // 秒
        "q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
        "S" : this.getMilliseconds()
        // 毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
            .substr(4 - RegExp.$1.length));
    for ( var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
                : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
// 日期格式化
Date.prototype.formatterDate = function(param,parrent){
    var parrent =  parrent|| "yyyy-MM-dd HH:mm:ss"
    var ms=[1,2,3,4,5,6,7,8,9,10,11,12];
    var y = this.getFullYear();
    var m = this.getMonth()+1;
    var d = this.getDate();
    var H = this.getHours();
    var M = this.getMinutes();
    var s = this.getSeconds();
    if(param&&param.year)
    {
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
        var time=this.getTime();
        time +=(param.day*1000*60*60*24);
        var date=new Date(time);
        y = date.getFullYear();
        m = date.getMonth()+1;
        d = date.getDate();
    }
    if(param&&param.time)
    {
        //return y+"-"+m+"-"+d + " " +param.time
        parrent.replace(/yyyy/g,y).replace(/MM/g,m).replace(/dd/g,d)+" "+time

    }
    return parrent.replace(/yyyy/g,y).replace(/MM/g,m).replace(/dd/g,d)
};
// 获得下载的参数
function getDownLoadParam() {
    // 时间控件的值
    var dataArr = $("#dms_date").val().split(" - ")
    // 开始时间和结束时间
    var start_time = new Date((new Date(dataArr[0])).getTime()).Format("yyyy-MM-dd");
    var end_time = new Date((new Date(dataArr[1])).getTime()).Format("yyyy-MM-dd");
    $("#start").text(start_time.replace("-",".").replace("-","."));
    $("#end").text(end_time.replace("-",".").replace("-","."));
    var is_abroad = $(".dms-btn.active.dms-btn-regional").val();
    var param;
    if (is_abroad==null||is_abroad==undefined){
        param = "?start_time="+start_time+"&end_time="+end_time;
    } else {
        param = "?start_time="+start_time+"&end_time="+end_time+"&is_abroad="+is_abroad;
    }
    return param;
}
// 导出
$("#export").click(function () {
    var uri ;
    // var type = $(".dms-btn.active.dms-btn-date").val();
    // if (type == "net"){
    //     uri = "/terminal/network/networkDownLoad"+getDownLoadParam();
    // } else {
        uri = "/terminal/network/operatorDownLoad"+getDownLoadParam();
    // }
    window.location.href=uri;
});

// 国内外
$(".dms-btn-regional").click(function () {
    $("#currentPage").text(1);
    $(this).toggleClass('active').siblings(".dms-btn-regional").removeClass('active');
    fullTable();
    fullView();
});

// 排序
$(".rank").click(function () {
    var sortField = $(this).attr("name");
    if ($(this).attr("value")==null||$(this).attr("value")==undefined){
        $(this).attr("value","desc");
    }else if ($(this).attr("value")=="desc"){
        $(this).attr("value","asc");
    } else if ($(this).attr("value")=="asc"){
        $(this).attr("value","desc");
    }
    var order = $(this).attr("value");
    fullTable(sortField,order);
});

// 填充折线图
function fullView(period,is_abroad) {
    var uri;
    var type = $(".dms-btn.active.dms-btn-date").val();
    // if (type == "net"){
    //     uri = "/terminal/network/netWorkView"+getDownLoadParam();
    // } else {
        uri = "/terminal/network/operatorView"+getDownLoadParam();
    // }
    var type = $(".dms-btn.active.dms-btn-date").val();

    $.get(uri,function (result) {
        var dataArr = new Array();
        var numArr = new Array();
        for (var i in result ){
            dataArr[i] = result[i].operator;
            numArr[i] = result[i].total_user_num;
        }

        window.netStatus.option.yAxis.data = dataArr;
        window.netStatus.option.series[0].data = numArr;
        window.netStatus.myChart.setOption(window.netStatus.option);

    });
}

