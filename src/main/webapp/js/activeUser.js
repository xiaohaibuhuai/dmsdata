window.onload = function () {
    //    国内国外点击样式切换
    frame.btnActive('.dms-btn-regional');
//   日期按钮击样式切换
    frame.btnActive('.dms-btn-date');
//table说明框定位
    $('.table-title-info').css('top',-$('.table-title-info').height());
    $(".table-icon").hover(function(){
        $(".table-title-info").css("display","block");
    });
    $(".table-icon").mouseleave(function(){
        $(".table-title-info").css("display","none");
    });

    window.user= {
        myChart : echarts.init(document.getElementById('dms-chart'))
    };
    window.user.option = {
        tooltip: {
            trigger: 'axis'
        },
        grid: {
            left: '2%',
            right: '3%',
            bottom: '3%',
            top:'10%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            data: ['03-28','03-29','03-30','03-31','04-01','04-02','04-03']
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name:'活跃人数',
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
            }
        ]
    };

    user.myChart.setOption(option);
};
