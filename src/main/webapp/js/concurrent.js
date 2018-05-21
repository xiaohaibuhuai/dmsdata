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



(function(J,w){
    w.pageScope={
        init:init,
        myChart:echarts.init(document.getElementById('dms-chart')),
        option :{
            tooltip: {
                trigger: 'axis'
            },//5-10修改
            grid: {
                left: '0%',
                right: '3%',
                bottom: '3%',
                top:'10%',
                containLabel: true
            },//5-10修改end
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
        }
    }
    //w.myChart = echarts.init(document.getElementById('dms-chart'));
    w.onload =function () {
            //    国内国外点击样式切换
            //frame.btnActive('.dms-btn-regional');
            //国内国外点击样式切换
            frame.btnActive('.dms-btn-regional');
            //   充值详情汇总统计切换
            frame.btn2Active('.dms-btn-date');
            pageScope.myChart.setOption(pageScope.option);
            pageScope.init();
    }
    var Ajax = function (options) {
            this.param = options;
            this.execute = function () {
                if (!this.param.hasOwnProperty('url')) {
                    throw TypeError("异步请求地址没有设置");
                }
                var fn = this.param.successfn;
                $.ajax({
                    url: this.param.url,
                    type: this.param.type || 'post',
                    data: this.param.data || {},
                    dataType: this.param.dataType || 'json',
                    async: this.param.async === false ? false : true,
                    success: this.param.success || function (response) {
                        console.info(response)
                        if (fn)
                            fn();
                    },
                    error: this.param.error || function (jqXHR, textStatus, errorThrow) {
                        console.info(jqXHR);
                        console.info(textStatus);
                        console.info(errorThrow);
                    }

                });
            }
            this.download=function(){
               /* if (!this.param.hasOwnProperty('url')) {
                                    throw TypeError("异步请求地址没有设置");
                }
                var fileName = "ddd.csv";
                var xhr = new XMLHttpRequest();
                xhr.open("post",this.param.url,true)//异步
                xhr.responseType = "blob" //返回类型blob
                xhr.setRequestHeader("Content-type","application/x-www-form-urlencoded");
                xhr.onload=function(){
                    if(this.status === 200){
                        var blob  = this.response;
                        var reader = new FileReader();
                        reader.readAsDataURL(blob);
                        reader.onload=function(e){
                            var a = w.document.createElement("a");
                            a.setAttribute("id","dowload")
                            a.download=fileName;
                            a.href =  e.target.result;
                            J("body").append(a);
                            J("#dowload").click();
                            J("#dowload").remove();
                        }
                    }

                }
                var data = "filename="+this.param.filename+"&data="+JSON.stringify(param)
                xhr.send(data)*/
                 var form=   '<form action="'+options.url+'" method="post" id="excel" style="display:none">'+
                                '<input type="text" name="fileName" value="'+options.filename+'">'+
                                '<textarea name="data">'+JSON.stringify(options.data)+'</textarea>'+
                             '</form>';

                J('body').append(form);
                J("#excel").submit();
                J("#excel").remove();
            };
    };

    var chart={
            title:[["并发趋势"]],
            index:0,
            getIndex:function(){return this.index},
            setIndex:function(index){this.index=index},
            urls:["/statistic/dmsconcurrenthourview/user/chartdata","/statistic/dmsconcurrentdayview/user/chartdata"],
            getUrl:function(){return this.urls[this.index]},
            div_class_dms_chart_time:'.dms-chart-time',
            mdata:{},
            div_class_dms_chart_time:'.dms-chart-time',
            chartOptions:{},
            getRemoteData:function(param){
                var option = {url:this.getUrl(),data:param,success:this.initChar.bind(this)}
                new Ajax(option
                ).execute()
            },
            mergeChartOptions:function(){
                  this.chartOptions.xAxis = this.reconstitutionxAxis()
                  this.chartOptions.series =  this.reconstitutionSeries()
                  return this.chartOptions;
            },
            xAxis: {
                type: 'category',
                data: ['03-28','03-29','03-30','03-31','04-01','04-02','04-03']
            },
            reconstitutionxAxis:function(){
                this.xAxis.data = this.mdata['xAxis.data']?this.mdata['xAxis.data']:[]
                return  this.xAxis
            },
            series: [


            ],
            reconstitutionSeries:function(){
                this.series = []
                if(this.index == 0 ){
                    var data1 = {

                          name:'活跃人数',
                          type:'line',
                          symbol:'circle',//拐点样式
                          symbolSize: 6,//拐点大小
                          data:this.mdata['series.data']?this.mdata['series.data']:[],
                          itemStyle : {
                              normal : {
                                  color:'#82c95b',
                                  lineStyle:{
                                      color:'#82c95b'
                                  }
                              }
                          }
                    }

                    this.series[0]=data1
                }else{
                     var data1 = {
                          name:'活跃人数',
                          type:'line',
                          symbol:'circle',//拐点样式
                          symbolSize: 6,//拐点大小
                          data:this.mdata['series.data']?this.mdata['series.data']:[],
                          itemStyle : {
                              normal : {
                                  color:'#82c95b',
                                  lineStyle:{
                                      color:'#82c95b'
                                  }
                              }
                          }
                     }
                    this.series[0]=data1
                }
                return this.series

            },
            initChar:function(data){
            if(data){
                this.chartOptions = pageScope.myChart.getOption()
                if(!this.chartOptions){
                    return
                }
                this.mdata = data
                this.redrawchar()
            }else{
                //TODO 处理提示信息
            }

            },
            redrawchar:function(){
                pageScope.myChart.setOption(this.mergeChartOptions())
                if(this.getIndex()==0){
                    J(this.div_class_dms_chart_time).html("<span>"+new Date(this.mdata['date'][0]).formatterDate({},"yyyy.MM.dd")+"</span>")
                }else{
                    J(this.div_class_dms_chart_time).html("<span>"+new Date(this.mdata['xAxis.data'][0]).formatterDate({},"yyyy.MM.dd")+"</span>-<span>"+new Date(this.mdata['xAxis.data'][this.mdata['xAxis.data'].length-1]).formatterDate({},"yyyy.MM.dd")+"</span>")
                }

            },
            showHour:function(){this.index=0;this.getRemoteData(index.getQueryParam())},
            showDay:function(){this.index=1;this.getRemoteData(index.getQueryParam())},
            bind:function(echarts){
                this.getRemoteData = this.getRemoteData.bind(this)
            }
     }
     chart.bind(w.myChart)
     var list={
              index:0,
              getIndex:function(){return this.index},
              setIndex:function(index){this.index=index;this.initTable()},
              columns:[
                       [{name:"date",text:"日期"},{name:"times_01",text:"峰值1"},{name:"times_02",text:"峰值2"},{name:"times_03",text:"峰值3"},{name:"times_04",text:"峰值4"},
                       {name:"times_05",text:"峰值5"},{name:"times_06",text:"峰值6"},{name:"times_07",text:"峰值7"},{name:"times_08",text:"峰值8"},{name:"times_09",text:"峰值9"},
                       {name:"times_10",text:"峰值10"},{name:"times_11",text:"峰值11"},{name:"times_12",text:"峰值12"},{name:"times_13",text:"峰值13"},{name:"times_14",text:"峰值14"},
                       {name:"times_15",text:"峰值15"},{name:"times_16",text:"峰值16"},{name:"times_17",text:"峰值17"},{name:"times_18",text:"峰值18"},{name:"times_19",text:"峰值19"},
                       {name:"times_20",text:"峰值20"},{name:"times_21",text:"峰值21"},{name:"times_22",text:"峰值22"},{name:"times_23",text:"峰值23"},{name:"times_24",text:"峰值24"}],
                       [{name:"date",text:"日期"},{name:"day",text:"峰值"}]
                      ],
              titles:["并发明细／小时","并发明细／日"],
              getTitle:function(){return this.titles[this.index]},
              getColumns:function(){return this.columns[this.index]},
              urls:["/statistic/dmsconcurrenthourview/user/list","/statistic/dmsconcurrentdayview/user/list"],
              getUrl:function(){return this.urls[this.index]},
              data:{},
              getDataOfTotal:function(){return this.data.total||0},
              getDataOfRows:function(){return this.data.rows||[]},
              table_class_dms_table:'.dms-table',
              h2_class_dms_title:'.dms-title',
              getRemoteData:function(param){
                    var option = {url:this.getUrl(),async:false,data:param,success:this.initList.bind(this)}
                    new Ajax(option).execute()
              },
               setASC:function(obj,_index){
                  J(obj).attr("onclick",'action.orderASC(this,'+_index+')')
               },
              setDESC:function(obj,_index){J(obj).attr("onclick",'action.orderDESC(this,'+_index+')')},
              initList:function(data){
                    if(data){
                        J(this.table_class_dms_table).find("tbody").empty()
                        html =""
                        try{
                            for(var i =0 ;i < data.rows.length ;i ++){
                                var row = data.rows[i]
                                html=html+  "<tr>";
                                             for(var j =0 ;j<this.getColumns().length;j++){
                                                var col =  this.getColumns()[j]
                                                html = html + "<td>"+(row[col.name]||"-")+"</td>";
                                             }
                                html = html + "</tr>";
                            }
                        }catch(err){
                            console.error(err)
                        }

                        J(this.table_class_dms_table).find("tbody").append(html)
                        this.data=data||{}
                    }else{
                        console.info("无数据")
                    }
                    return this.data
              },
              initTable:function(){
                    J(this.table_class_dms_table).find("thead").find("tr").empty()
                    html   ="";
                        for(var i =0 ; i< this.getColumns().length;i++){
                                var column = this.getColumns()[i]
                                if(i==0){
                                    html= html+ '<th onclick="action.orderASC(this,'+i+')">'+column.text+'</th>';
                                }else{
                                    html= html+ '<th onclick="action.orderDESC(this,'+i+')">'+column.text+'</th>';
                                }

                        }
                    J(this.table_class_dms_table).find("thead").find("tr").append(html);
                    J(this.h2_class_dms_title).text(this.getTitle())
              },
              getData:function(){
                return this.data
              },
              bind:function(echarts){
                this.getRemoteData = this.getRemoteData.bind(this)
              }
        }
        list.bind()


    w.action={
        button_class_dms_btn_regional:'.dms-btn-regional',
        button_class_dms_btn_regional_active:'.dms-btn-regional.activate',
        all:function(){

           index.setQueryParam({_type:"all"})
           chart.getRemoteData(index.getQueryParam());
           index.setReDrawPage(true)
           index.setConfig({tablePage:0})
           index.loadData()
       },
       china:function(obj){

           if(obj.classList.length>2){
               console.info("全部")
               this.all()
           }else{
               console.info("国内")

               index.setQueryParam({_type:"china"})
               chart.getRemoteData(index.getQueryParam());
               index.setReDrawPage(true)
               index.setConfig({tablePage:0})
               index.loadData()//list.getRemoteData({_type:"all",startDate:"",endDate:""});
           }

       },
        orderDESC:function(obj,_index){
            index.setQueryParam({sort:list.getColumns()[_index].name,by:"desc"})
            list.setASC(obj,_index)
            index.loadData()
        },
        orderASC:function(obj,_index){
                    index.setQueryParam({sort:list.getColumns()[_index].name,by:"asc"})
                    list.setDESC(obj,_index)
                    index.loadData()
        },
        abroad:function(obj){
             if(obj.classList.length>2){
                 console.info("全部")
                 this.all()
             }else{
                  console.info("国外")
                  index.setQueryParam({_type:"abroad"})
                  chart.getRemoteData({_type:"abroad"})
                  index.setReDrawPage(true)
                  index.setConfig({tablePage:0})
                  index.loadData()
             }
        },
        export:function(obj){
            param={
                url:list.getUrl(),
                total:list.getData().total||0,
                column:list.getColumns(),
                queryParams:index.getQueryParam()||{}

            }
            w.setTimeout(function(){console.info("-----------------------------")},5000)
            var CONF={
                url:"/export/csv",
                filename:"并发统计",
                data:param
            }
            new Ajax(CONF).download();
        },
        setIndex:function(index){
            list.setIndex(index)
            chart.setIndex(index)
        },
        hour:function(obj){
            this.setIndex(0)
            index.setQueryParam({})
            index.loadData()
            chart.showHour()
            console.info("小时统计")
        },
        day:function(obj){
            this.setIndex(1)
            index.setQueryParam({})
            chart.showDay()
            index.loadData()
            console.info("天统计")
        }
    }
    var index = {

        select_id_perPageItems:'#perPageItems',
        pageItems:[10,20,30,50,100],
        startDate:null, //开始时间
        endDate:null, //结束时间
        tablePage:null, //table 分页当前页
        perPageItem:null, //每页显示条数
        totalPage:20, //总页数
        isInit:false,
        isReDrawPage:false,
        getDefaultPageItems:function(){return this.pageItems[1]},
        getReDrawPage:function(){return this.isReDrawPage},
        setReDrawPage:function(isReDrawPage){this.isReDrawPage=isReDrawPage},
        queryParam:{},
        sort:"",
        loadData:function(option){
            this.queryParam.rows=this.perPageItem||this.getDefaultPageItems()
            this.queryParam.page=this.tablePage+1
            this.queryParam.startDate=this.startDate||new Date().formatterDate({day:-7},"yyyy-MM-dd")
            this.queryParam.endDate=this.endDate||new Date().formatterDate({},"yyyy-MM-dd")
            list.getRemoteData(this.queryParam)
            if(this.getReDrawPage()){
                this.init({data:list.getData()})
            }
        },
        config:{},
        setQueryParam:function(param){
            if(param){
                for( ele in param){
                    this.queryParam[ele] =  param[ele]
                }
            }
        },
        getQueryParam:function(){
                   return this.queryParam
                },
        clearQueryParam:function(){
             this.queryParam={}
        },
        _initConfig:function(){
            if(this.config.hasOwnProperty("data")){
                var total= this.config.data.total
                if(this.perPageItem){
                    this.totalPage =  Math.ceil(total/this.perPageItem)
                }else{
                    this.totalPage =  Math.ceil(total/this.getDefaultPageItems())
                }

                var page =""
                for( var i = 0 ;i < this.pageItems.length;i++){
                        page = page + '<option '+((this.perPageItem||this.getDefaultPageItems())==this.pageItems[i]?'selected="selected"':"")+' value="'+this.pageItems[i]+'">'+this.pageItems[i]+'</optio>';
                }
                if(page!=""){
                    J(this.select_id_perPageItems).empty()
                    J(this.select_id_perPageItems).append(page)
                }



            }else{
                console.info("页面未加载到远程配置，初始化失败")
            }
        },
        init:function (conf) {
            this.config=conf
            this._initConfig()


            this.currentPage(this.tablePage||0,this.totalPage); //1是变量 this.totalPage 后台需要返回;
            if(!this.isInit){
                this.dmsDate();
                this.PageItem()
                this.pageJump();
            }
            this.isInit=true;
            this.setReDrawPage(false)

        },
        setConfig:function(option){
               for(ele in option){
                    this[ele]= option[ele]
               }
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
                    "startDate": new Date().formatterDate({day:-7},"MM/dd/yyyy"),
                    "endDate": new Date().formatterDate({},"MM/dd/yyyy")
                }, function(start, end, label) {
                    _this.startDate = start.format('YYYY-MM-DD');
                    _this.endDate = end.format('YYYY-MM-DD');
                    _this.setReDrawPage(true)
                    _this.setConfig({tablePage:0})
                    _this.loadData()
                    console.log( start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') );

                });
            }
        },
        //分页插件
        currentPage:function (current,tableTotalPage) {
            var _this = this;
            J('#pagination').pagination(tableTotalPage,{
                prev_text:'上一页', //上一页
                next_text:'下一页', //下一页
                current_page:current,  //当前页
                items_per_page:1,
                callback: function (page_index,jq) { //获取数据后的徽调函数
                    _this.tablePage = page_index;
                    J('#currentPage').text(page_index + 1);
                    J('.totalPage').text(tableTotalPage);
                    //跳到首页
                    if(page_index ==0 ){
                        J('#pageFirst').css('color','#999').addClass('disabled');
                    }else {
                        J('#pageFirst').css('color','#12a0ff').removeClass('disabled');
                    }
                    //跳到尾页
                    if(page_index == (tableTotalPage-1) ){
                        J('#pageLast').css('color','#999').addClass('disabled');
                    }else {
                        J('#pageLast').css('color','#12a0ff').removeClass('disabled');
                    }
                    if(_this.isInit&&!_this.isReDrawPage){
                        _this.loadData();
                    }
                    console.info(page_index)
                    _this.setReDrawPage(false)
                }
            });
        },
        //分页跳转
        pageJump:function () {
            var _this = this;
            // 页面跳转到第几页
            J('#jumpPageBtn').click(function () {
                var jumpPage = J('#jumpPageInput').val();
                if( jumpPage <= _this.totalPage&& /(^[1-9]\d*$)/.test(jumpPage) ){
                    _this.currentPage(jumpPage-1,_this.totalPage);
                }else {
                    alert('页码超出范围');
                }
            });
            //跳到首页
            J('#pageFirst').click(function () {
                _this.currentPage(0,_this.totalPage)
                console.info("首页")
            });
            //跳到尾页
            J('#pageLast').click(function () {
                _this.currentPage(_this.totalPage-1,_this.totalPage)
                console.info("尾页")
            });
        },
        //每页显示条数设置
        PageItem:function () {
            var _this = this;
            J('#perPageItems').change(function () {
                _this.perPageItem = J('#perPageItems').val();
                _this.setReDrawPage(true)
                _this.setConfig({tablePage:0})
                _this.loadData()
            });
        }
    };
    var init =function(){
        list.initTable();
        w.action.all()
    }
    w.pageScope.init=init
})($,window)