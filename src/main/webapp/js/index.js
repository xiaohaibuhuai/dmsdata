
window.onload =function () {
    //    国内国外点击样式切换
    frame.btnActive('.dms-btn-regional');
    var myChart = echarts.init(document.getElementById('dms-chart'));
    option = {
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data:['注册人数','买入人数']
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
                name:'买入人数',
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
};

