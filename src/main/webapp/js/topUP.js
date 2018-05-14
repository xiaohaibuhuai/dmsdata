/*var index = {
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
                alert(page_index);
            }
        });
    },
    //分页跳转
    pageJump:function () {
        var _this = this;
        // 页面跳转到第几页
        $('#jumpPageBtn').click(function () {
            var jumpPage = $('#jumpPageInput').val();
            if( jumpPage <= _this.totalPage){
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
    //   充值详情汇总统计切换
    frame.btn2Active('.dms-btn-date');
    // 充值详情汇总统计点击table切换
    $('#topUP_detail_btn').click(function () {
        $('#topUP_detail').show();
        $('#topUP_detail_title').show();
        $('#topUP_total').hide();
        $('#topUP_total_title').hide();
    });
    $('#topUP_total_btn').click(function () {
        $('#topUP_detail').hide();
        $('#topUP_detail_title').hide();
        $('#topUP_total').show();
        $('#topUP_total_title').show();
    });

    var myChart = echarts.init(document.getElementById('dms-chart'));
    option = {
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        series : [
            {
                name: '充值',
                type: 'pie',
                radius : '80%',
                center: ['50%', '50%'],
                label: {
                    normal: {
                        formatter: '{b} : {d}%'
                    }
                },
                data:[
                    {value:335, name:'直接访问'},
                    {value:310, name:'邮件营销'},
                    {value:234, name:'联盟广告'},
                    {value:234, name:'联盟广告1'},
                    {value:300, name:'联盟广告2'},
                    {value:234, name:'联盟广告3'},
                    {value:135, name:'视频广告4'},
                    {value:135, name:'视频广告1'},
                    {value:500, name:'视频广告2'},
                    {value:135, name:'视频广告3'},
                    {value:1548, name:'搜索引擎1'},
                    {value:1548, name:'搜索引擎2'},
                    {value:548, name:'搜索引擎3'},
                    {value:1548, name:'搜索引擎4'}
                ]

            }
        ],
        color: ['rgb(18,160,255)','rgb(131,175,155)','rgb(200,200,169)','rgb(54,64,74)','rgb(254,164,64)','rgb(252,157,154)']
    };
    myChart.setOption(option);
    //页面初始化
    index.init();
};*/






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
        option:{
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    series : [
                        {
                            name: '充值',
                            type: 'pie',
                            radius : '80%',
                            center: ['50%', '50%'],
                            label: {
                                normal: {
                                    formatter: '{b} : {d}%'
                                }
                            },
                            data:[
                                {value:335, name:'直接访问'},
                                {value:310, name:'邮件营销'},
                                {value:234, name:'联盟广告'},
                                {value:234, name:'联盟广告1'},
                                {value:300, name:'联盟广告2'},
                                {value:234, name:'联盟广告3'},
                                {value:135, name:'视频广告4'},
                                {value:135, name:'视频广告1'},
                                {value:500, name:'视频广告2'},
                                {value:135, name:'视频广告3'},
                                {value:1548, name:'搜索引擎1'},
                                {value:1548, name:'搜索引擎2'},
                                {value:548, name:'搜索引擎3'},
                                {value:1548, name:'搜索引擎4'}
                            ]

                        }
                    ],
                    color: ['rgb(18,160,255)','rgb(131,175,155)','rgb(200,200,169)','rgb(54,64,74)','rgb(254,164,64)','rgb(252,157,154)']
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
                 var form=   '<form action="'+CONF.url+'" method="post" id="excel" style="display:none">'+
                                '<input type="text" name="fileName" value="'+CONF.filename+'">'+
                                '<textarea name="data">'+JSON.stringify(CONF.data)+'</textarea>'+
                             '</form>';

                J('body').append(form);
                J("#excel").submit();
                J("#excel").remove();
            };
    };


    var chart={
          //echarts:{},
          url:"/statistic/dmsuserview/user/chartdata",
          div_class_dms_chart_time:'.dms-chart-time',
          chartOptions:{},
          getRemoteData:function(param){
                var option = {url:this.url,data:param,success:this.initChar.bind(this)}
                new Ajax(option
                ).execute()
          },
          initChar:function(data){
            if(data){
                this.chartOptions = pageScope.myChart.getOption()
                if(!this.chartOptions){
                    return
                }
                this.chartOptions.xAxis[0].data = data['xAxis.data']?data['xAxis.data']:[]
                this.chartOptions.series[0].data = data['series.register.data']?data['series.register.data']:[]
                this.chartOptions.series[1].data = data['series.buyin.data']?data['series.buyin.data']:[]
                pageScope.myChart.setOption(this.chartOptions)
                J(this.div_class_dms_chart_time).html("<span>"+new Date(data['xAxis.data'][0]).formatterDate({},"yyyy.MM.dd")+"</span>-<span>"+new Date(data['xAxis.data'][data['xAxis.data'].length-1]).formatterDate({},"yyyy.MM.dd")+"</span>")
            }else{
                //TODO 处理提示信息
            }

          },
          bind:function(echarts){
            //this.echarts=echarts;
            this.getRemoteData = this.getRemoteData.bind(this)
          }
    }
    chart.bind(w.myChart)





     var list={
              index:0,
              getIndex:function(){return this.index},
              setIndex:function(index){this.index=index;this.initTable()},
              columns:[
                       [{name:"date",text:"安卓微信充值"},{name:"regist_user_num",text:"苹果充值"},{name:"regist_buyin_user_num",text:"谷歌充值"},{name:"total_buyin_user_num",text:"公众号微信支付"},{name:"total_user_num",text:"公众号支付宝支付"},{name:"total_buyin_user_num",text:"步步德扑"},{name:"total_buyin_user_num",text:"九格创想"},{name:"total_buyin_user_num",text:"Cms支付宝"},{name:"total_buyin_user_num",text:"钻石基金"},{name:"total_buyin_user_num",text:"个人大额充值"}],
                       [{name:"date",text:"充值类型"},{name:"regist_user_num",text:"充值人数"},{name:"regist_buyin_user_num",text:"充值次数"},{name:"regist_buyin_user_num",text:"充值金额"}]
                      ],
              titles:["充值渠道明细","充值类型汇总"],
              getTitle:function(){return this.titles[this.index]},
              getColumns:function(){return this.columns[this.index]},
              urls:["/statistic/dmsuserview/user/list","/statistic/dmsuserview/user/list"],
              getUrl:function(){return this.urls[this.index]},
              data:{},
              getDataOfTotal:function(){return this.data.total||0},
              getDataOfRows:function(){return this.data.rows||[]},
              table_class_dms_table:'.dms-table',
              getRemoteData:function(param){
                    var option = {url:this.getUrl(),async:false,data:param,success:this.initList.bind(this)}
                    new Ajax(option).execute()
              },
               setASC:function(obj,_index){
                  J(obj).attr("onclick",'action.orderASC(this,'+_index+')')
                  //obj.onclick=orderDESC.bind(obj)
               },
              setDESC:function(obj,_index){J(obj).attr("onclick",'action.orderDESC(this,'+_index+')')},
              initList:function(data){
                    if(data){
                        J(this.table_class_dms_table).find("tbody").empty()
                        html =""
                        for(var i =0 ;i < data.rows.length ;i ++){
                            var row = data.rows[i]
                            html=html+  "<tr>";
                                         for(var j =0 ;j<this.getColumns().length;i++){
                                            var col =  this.getColumns()[j]
                                            html = html + "<td>"+(row[col.name]||"")+"</td>";
                                         }
                                        html = html + "</tr>";
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
                                html= html+ '<th onclick="action.orderDESC(this,'+i+')">'+column.text+'</th>';
                                //html= html+ '<th onclick="action.order(this,'+i+')">'+column.text+'</th>';
                                //(this.table_class_dms_table).find("thead").find("td")
                        }
                    J(this.table_class_dms_table).find("thead").find("tr").append(html);
                    //dms_pay_chanel_view
                    /*for(var i =0 ; i< this.columns.length;i++){
                            var column = this.columns[i]
                            //html= html+ '<td onclick="action.order(this,'+i+')">'+column.text+'</td>';
                            J(this.table_class_dms_table).find("th")[0].innerHTML=column.text
                            J(J(this.table_class_dms_table).find("th").text()[0]).attr("onclick",'action.order(this,'+i+')')
                    }*/
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
        /*div_class_range_inputs:"range_inputs",
        addEvent:function(){
            w.document.getElementsByClassName("range_inputs")[0].addEventListener("click",function(){
                index.loadData();
            })
        },*/
        all:function(){

           index.setQueryParam({_type:"all"})
           chart.getRemoteData(index.getQueryParam());
           index.setReDrawPage(true)
           index.loadData()//list.getRemoteData({_type:"all",startDate:"",endDate:""});
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
               index.loadData()//list.getRemoteData({_type:"all",startDate:"",endDate:""});
           }

       },
        orderDESC:function(obj,_index){
            index.setQueryParam({sort:list.columns[_index].name,by:"desc"})
            list.setASC(obj,_index)
            index.loadData()
        },
        orderASC:function(obj,_index){
                    index.setQueryParam({sort:list.columns[_index].name,by:"asc"})
                    list.setDESC(obj,_index)
                    index.loadData()
        },
        orderDESC:function(obj,_index){
            index.setQueryParam({sort:list.columns[_index].name,by:"desc"})
            list.setASC(obj,_index)
            index.loadData()
        },
        orderASC:function(obj,_index){
                    index.setQueryParam({sort:list.columns[_index].name,by:"asc"})
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
                  index.loadData()//list.getRemoteData({_type:"all",startDate:"",endDate:""});
             }
        },
        export:function(obj){// onclick="action.export(this)"  //action.changeDate(this)
            param={
                url:list.getUrl(),
                total:list.getData().total||0,
                column:list.getColumns(),
                queryParams:index.getQueryParam()||{}

            }

            //J(obj).linkbutton('disable');
            w.setTimeout(function(){console.info("-----------------------------")},5000)
            var CONF={
                url:"/export/csv",
                filename:"充值统计",
                data:param
            }


            /*var form=       '<form action="'+CONF.url+'" method="post" id="excel" style="display:none">'+
                                '<input type="text" name="fileName" value="'+CONF.filename+'">'+
                                '<textarea name="data">'+JSON.stringify(CONF.data)+'</textarea>'+
                            '</form>';

            J('body').append(form);
            J("#excel").submit();
            J("#excel").remove();
            console.info("导出")*/
            new Ajax(CONF).download();
        },
        topUpDetail:function(obj){
            list.setIndex(0)
            index.setQueryParam({})
            index.loadData()
            console.info("充值详情")
        },
        topUpCollect:function(obj){
            list.setIndex(1)
            index.setQueryParam({})
            index.loadData()
            console.info("汇总统计")
        }
    }
    var index = {

        select_id_perPageItems:'#perPageItems',
        pageItems:[10,20,30,40,50],
        startDate:null, //开始时间
        endDate:null, //结束时间
        tablePage:null, //table 分页当前页
        perPageItem:null, //每页显示条数
        totalPage:20, //总页数
        isInit:false,
        isReDrawPage:false,
        getReDrawPage:function(){return this.isReDrawPage},
        setReDrawPage:function(isReDrawPage){this.isReDrawPage=isReDrawPage},
        queryParam:{},
        sort:"",
        loadData:function(option){
            this.queryParam.rows=this.perPageItem||this.pageItems[0]
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
                var rows= this.config.data.total
                if(this.perPageItem){
                    this.totalPage =  Math.ceil(rows/this.perPageItem)
                }else{
                    this.totalPage =  Math.ceil(rows/this.pageItems[0])
                }

                var page =""
                for( var i = 0 ;i < this.pageItems.length;i++){
                        page = page + '<option select='+((this.perPageItems||this.pageItems[0])==this.pageItems[i]?"selected":"")+' value="'+this.pageItems[i]+'">'+this.pageItems[i]+'</optio>';
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
            this.dmsDate();

            this.currentPage(this.tablePage||0,this.totalPage); //1是变量 this.totalPage 后台需要返回;
            this.PageItem()
            this.pageJump();
            this.isInit=true;
            this.setReDrawPage(false)

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
                if( jumpPage <= _this.totalPage){
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
                _this.currentPage(0,_this.totalPage)
            });
        }
    };
    var init =function(){
        list.initTable();
        w.action.all()
        //w.action.addEvent()
        index.init({data:list.getData(),datestart:"",dateend:""})

    }
    w.pageScope.init=init
})($,window)