var index = {
    startDate:null, //开始时间
    endDate:null, //结束时间
    tablePage:null, //table 分页当前页
    perPageItem:null, //每页显示条数
    totalPage:20, //总页数
    init:function () {
        this.dmsDate();
        this.currentPage(0,this.totalPage); //1是变量 this.totalPage 后台需要返回;
        this.pageJump();
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
                "startDate": "04/19/2018",
                "endDate": "04/25/2018"
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
                console.log(page_index);

            }
        });
    },
    //分页跳转
    pageJump:function () {
        var _this = this;
        // 页面跳转到第几页
        $('#jumpPageBtn').click(function () {
            var jumpPage = $('#jumpPageInput').val();
            if( jumpPage <= _this.totalPage && /(^[1-9]\d*$)/.test(jumpPage)){
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
    }
};

//输出时间值
$('#dms_date').change(function () {
    console.log('开始时间'+index.startDate);
});

window.onload =function () {
    //    国内国外点击样式切换
    frame.btnActive('.dms-btn-regional');

    //注册和注册买入按钮切换
    frame.btn2Active('.dms-btn-date'); //5-16修改 //5-16修改end

    var myChart = echarts.init(document.getElementById('dms-chart'));
    option = {
        tooltip: {
            trigger: 'axis'
        },//5-16修改
        legend: {
            data:['注册人数','注册即买入人数'],
            bottom:'0%',
            right:'10%'
        },
        grid: {
            left: '0%',
            right: '3%',
            bottom: '10%',
            top:'10%',
            containLabel: true
        },//5-16修改end
        xAxis: {
            type: 'category',
            data: ['03-28','03-29','03-30','03-31','04-01','04-02','04-03']
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name:'注册人数',
                type:'line',
                symbol:'circle',//拐点样式
                symbolSize: 6,//拐点大小
                data:[0, 132, 101, 100, 290, 230, 0],
                itemStyle : {
                    normal : {
                        color:'#82c95b',
//                        borderColor:'red',  //拐点边框颜色
                        lineStyle:{
                            color:'#82c95b'
                        }
                    }
                }
            },
            {
                name:'注册即买入人数',
                type:'line',
                symbol:'circle',//拐点样式
                symbolSize: 6,//拐点大小
                data:[20, 50, 191, 234, 90, 330, 20],
                itemStyle : { //折线颜色
                    normal : {
                        color:'#12a0ff', //圆点颜色
                        lineStyle:{
                            color:'#12a0ff'
                        }
                    }
                }
            }

        ]
    };
    myChart.setOption(option);
    //页面初始化
    index.init();
};

