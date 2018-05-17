var version =  {
    startDate:null, //开始时间
    endDate:null, //结束时间
    tablePage:null, //table 分页当前页
    perPageItem:null, //每页显示条数
    totalPage:20, //总页数
    init:function () {
        this.dmsDate();
        this.currentPage(0,this.totalPage); //1是变量 this.totalPage 后台需要返回;
        this.pageJump();
        this.getAllVersion();
        this.InitData();
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
                "linkedCalendars": false,
                // "startDate":"01/01/2017",
                // "endDate":"01/01/2019"
                "startDate": new Date().formatterDate({day:-7},"MM/dd/yyyy"),
                "endDate": new Date().formatterDate({},"MM/dd/yyyy")
            }, function(start, end, label) {
                _this.startDate = start.format('YYYY-MM-DD');
                _this.endDate = end.format('YYYY-MM-DD');
                console.log( start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') );
            });
        }
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
        });
        //跳到尾页
        $('#pageLast').click(function () {
            _this.currentPage(_this.totalPage-1,_this.totalPage)
        });
    },
    //每页显示条数设置
    PageItem:function () {
        var _this = this;
        $('#perPageItems').change(function () {
            _this.perPageItem = $('#perPageItems').val();
            _this.currentPage(0,_this.totalPage)
        });
    },
    // 加载数据 根据参数
    InitData:function () {
        setTotalPage();
        fullTable();
    },
    // 页面加载获得版本信息
    getAllVersion:function () {
    var uri = "/user/version/getVersion";
    $.get(uri,function (result) {
        for(var i in result){
            $('#id_select').append("<option>"+result[i].app_version+"</option>");
            $('#id_select').selectpicker('refresh');
            $('#id_select').selectpicker('render');
        }
    });
}

};

//输出时间值
$('#dms_date').change(function () {
    $("#currentPage").text(1);
    setTotalPage();
    fullTable();
});

window.onload = function () {
    //版本搜索框 添加placeHolder
    $('input[aria-label = "Search"]').attr('placeHolder','搜索 版本');
    //    国内国外点击样式切换
    // frame.btnActive('.dms-btn-regional');
    //   日期按钮击样式切换
    frame.btn2Active('.dms-btn-date');
    //表格上的按钮切换
    $('#totalVersion_btn').click(function () {
        $('#newVersion').hide();
        $('#totalVersion').show();
        fullTable();
    });
    $('#newVersion_btn').click(function () {
        $('#newVersion').show();
        $('#totalVersion').hide();
        fullTable();
    });

    //初始化下拉框
    $('#id_select').selectpicker('refresh');
    //$('#name').selectpicker('render'); //重绘
    //监听下拉框的值变化下拉框
    $('#id_select').change(function () {
        $("#currentPage").text(1);
        setTotalPage();
        console.log('app的版本值'+ $('#id_select').selectpicker('val'));
        fullTable();
    });

    //页面初始化
    version.init()
};

function fullTable(sortField,order) {

    var myVersion = $(".dms-btn.active.dms-btn-date").val();
    var uri;
    if (myVersion == "newVersion"){
        uri = "/user/version/newVersion"+getPageParamter(getHeardParamter(),sortField,order);
    } else {
        uri = "/user/version/totalVersion"+getPageParamter(getHeardParamter(),sortField,order);
    }
    $.get(uri,function (result) {
        var rows = result.rows;
        if (myVersion == "newVersion"){
            $("#addVersion").empty();
            for (var i in rows){
                $("#addVersion").append(
                    "<tr>" +
                    "<td>"+rows[i].date+"</td>" +
                    "<td>"+rows[i].app_version+"</td>" +
                    "<td>"+rows[i].is_new+"</td>" +
                    "<td>"+rows[i].isupdate+"</td>" +
                    "<td>"+rows[i].sum_two+"</td>" +
                    "<td>"+rows[i].token_num+"</td>" +
                    "</tr>");
            }
        } else {
            $("#totalVersionBody").empty();
            for (var i in rows){
                $("#totalVersionBody").append(
                    "<tr>" +
                    "<td>"+rows[i].app_version+"</td>" +
                    "<td>"+rows[i].token_num+"</td>" +
                    "<td>"+rows[i].user_num+"</td>" +
                    "</tr>");
            }
        }
    });
}

function getHeardParamter() {
    // 时间控件的值
    var dataArr = $("#dms_date").val().split(" - ")
    // 开始时间和结束时间
    var start_time = new Date((new Date(dataArr[0])).getTime()).Format("yyyy-MM-dd");
    var end_time = new Date((new Date(dataArr[1])).getTime()).Format("yyyy-MM-dd");
    // 获得 国内外的值
    var is_abroad = $(".dms-btn.active.dms-btn-regional").val()

    var param = "?start_time="+start_time+"&end_time="+end_time;
    if (is_abroad!=null&&is_abroad!=undefined&&is_abroad!=""){
        param += "&is_abroad="+is_abroad;
    }
    // 版本
    var app_version = $('#id_select').selectpicker('val');
    if (app_version!=("App版本")&&app_version!=null&&app_version!=undefined&&app_version!=""){
        param += "&app_version="+app_version;
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

function getDownLoadParam() {
    // 时间控件的值
    var dataArr = $("#dms_date").val().split(" - ")
    // 开始时间和结束时间
    var start_time = new Date((new Date(dataArr[0])).getTime()).Format("yyyy-MM-dd");
    var end_time = new Date((new Date(dataArr[1])).getTime()).Format("yyyy-MM-dd");
    var is_abroad = $(".dms-btn.active.dms-btn-regional").val();
    var app_version = $('#id_select').selectpicker('val');
    var param;
    if (is_abroad==null||is_abroad==undefined){
        param = "?start_time="+start_time+"&end_time="+end_time;
    } else {
        param = "?start_time="+start_time+"&end_time="+end_time+"&is_abroad="+is_abroad;
    }
    // 版本
    if (app_version!=("App版本")&&app_version!=null&&app_version!=undefined&&app_version!=""){
        param += "&app_version="+app_version;
    }
    return param;
}

$("#export").click(function () {
    var uri ;
    var myVersion = $(".dms-btn.active.dms-btn-date").val();
    if (myVersion == "newVersion"){
        uri = "/user/version/versionDownload"+getDownLoadParam();
    } else {
        uri = "/user/version/personDownload"+getDownLoadParam();
    }
    window.location.href=uri;
});


$(".dms-btn-regional").click(function () {
    // alert("aaaaa")
    $("#currentPage").text(1);
    setTotalPage();
    $(this).toggleClass('active').siblings(".dms-btn-regional").removeClass('active');
    fullTable();
})


$(".version").click(function () {
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

function setTotalPage() {
    var uri = "/user/version/newVersion"+getPageParamter(getHeardParamter(),null,null);
    $.get(uri,function (result) {
        var total = result.total;
        var perPageItems = $('#perPageItems').val();
        var totalPage = Math.ceil(total/perPageItems);
        version.totalPage=totalPage;
        version.currentPage(0,totalPage);
    });
}
