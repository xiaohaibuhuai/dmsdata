var frame = {
   init: function () {
        this.elHeight();
        // this.dmsDate();
    },
    //左侧菜单元素渲染
    leftMenu:function () {
        var leftStr ='';
        for(var i = 0; i<leftBar.length; i++){
            leftStr += ' <li>' +
                '<div class="menu-item-til">' +
                '<span>'+leftBar[i].item+'</span>' +
                '<img class="fr menu-default" src="../../imgs/left_menu_default.png">' +
                '</div>';
            if(leftBar[i].children.length > 0){
                leftStr += '<ul class="menu-item-list">';
                for (var j = 0; j < leftBar[i].children.length; j++){
                    leftStr += '<li data-url="'+leftBar[i].children[j].urlName+'">'+ leftBar[i].children[j].itemList +'</li>'
                }
                leftStr +='</ul>'
            }
            leftStr += '</li>'
        }

        $(' #dms_left>ul ').html(leftStr);
        this.leftMenuEffect()
    },
    //国内国外筛选条件按钮交互效果
    btnActive:function (el) {
        $(el).click(function () {
            $(this).toggleClass('active').siblings(el).removeClass('active');
        })
    },
    //图表表格筛选条件按钮交互效果
    btn2Active:function (el) {
        $(el).click(function () {
            $(this).addClass('active').siblings(el).removeClass('active');
        })
    },
    //左侧菜单事件
    leftMenuEffect:function () {

        var dUrlPathArr =window.location.pathname.split('/');
        var dUrlPath =dUrlPathArr[dUrlPathArr.length-1];
        var dUrlName = dUrlPath.split('.')[0];
        var dataUrlList = $('.menu-item-list li');
        //  til 交互效果
        $('.menu-item-til').click(function () {
            var menu = $(this);
            //icon切换
            menu.children('img').toggleClass('menu-click').toggleClass('menu-default');
            menu.children('img.menu-click').attr('src','../../imgs/left_menu_click.png');
            menu.children('img.menu-default').attr('src','../../imgs/left_menu_default.png');
            //子类目展开或者隐藏
            menu.toggleClass('menu-active').siblings('.menu-item-list').toggleClass('expansion');

        });
        //子类目交互设置
        dataUrlList.click(function () {
            // console.log(this);
            location.href = $(this).attr('data-url') +'.html';
            $('.menu-item-list li.menu-active').removeClass('menu-active');
            $(this).toggleClass('menu-active');
        });

        //页面加载完成初始化leftbar
        dataUrlList.each(function (i,val) {
            if( $(val).attr('data-url') == dUrlName){
                $('.menu-item-list li[data-url='+dUrlName+']').addClass('menu-active').parent().addClass('expansion').siblings('.menu-item-til').addClass('menu-active');
                var menu = $('.menu-item-list li[data-url='+dUrlName+']').parent().siblings('.menu-item-til');
                menu.children('img').removeClass('menu-default').addClass('menu-click');
                menu.children('img.menu-click').attr('src','../../imgs/left_menu_click.png');
                menu.children('img.menu-default').attr('src','../../imgs/left_menu_default.png');
            }
        })
        //页面加载完成初始化leftbar end

    },
    elHeight:function () {
        $('body').css('min-height',this.winH());
        $('.dms-left').css('min-height',this.winH());
    },
    winH:function () {
        var winHeight;
        winHeight = $(window).height();
        return winHeight;
    }
//    日期插件
//     dmsDate:function () {
//         if($('#dms_date').length > 0){
//             $('#dms_date').daterangepicker({
//                 "locale": {
//                     "direction": "ltr",
//                     "format": "MM/DD/YYYY",
//                     "separator": " - ",
//                     "applyLabel": "确定",
//                     "cancelLabel": "取消",
//                     "fromLabel": "From",
//                     "toLabel": "To",
//                     "customRangeLabel": "Custom",
//                     "daysOfWeek": [
//                         "日",
//                         "一",
//                         "二",
//                         "三",
//                         "四",
//                         "五",
//                         "六"
//                     ],
//                     "monthNames": [
//                         "一月",
//                         "二月",
//                         "三月",
//                         "四月",
//                         "五月",
//                         "六月",
//                         "七月",
//                         "八月",
//                         "九月",
//                         "十月",
//                         "十一月",
//                         "十二月"
//                     ],
//                     "firstDay": 1
//                 },
//                 "linkedCalendars": false,
//                 "startDate": "04/19/2018",
//                 "endDate": "04/25/2018"
//             }, function(start, end, label) {
//                 console.log( start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') );
//             });
//         }
//     }
};
frame.init();
$(document).ready(function () {
    frame.leftMenu();
});
//leftBar 数据格式
var leftBar = [
    {
        'item':'用户统计',
        'children':[{
            itemList:'用户数据汇总',
            urlName:'index'
        }, {
            itemList:'活跃用户统计',
            urlName:'activeUser'
        }, {
            itemList:'留存用户统计',
            urlName:'retain'
        }, {
            itemList:'版本',
            urlName:'version'
        }
        ]
    } ,
    {
        'item':'终端属性统计',
        'children':[{
            itemList:'地域统计',
            urlName:'netStatus'
        }, {
            itemList:'设备终端统计',
            urlName:'TerminalEqu'
        }, {
            itemList:'网络状态统计',
            urlName:'network'
        }
        ]
    },
    {
        'item':'并发统计',
        'children':[{
            itemList:'并发统计',
            urlName:'concurrent'
        }
        ]
    },
    {
        'item':'收入统计',
        'children':[{
            itemList:'充值统计',
            urlName:'topUp'
        }, {
            itemList:'德扑币消耗',
            urlName:'consumption'
        }, {
            itemList:'德扑币消耗',
            urlName:'dpConsumption'
        }, {
            itemList:'钻石消耗',
            urlName:'zsConsumption'
        }
        ]
    },
    {
        'item':'牌局统计',
        'children':[{
            itemList:'汇总统计',
            urlName:'summary'
        }, {
            itemList:'各牌局类型数据详表',
            urlName:'dataDetail'
        }, {
            itemList:'MTT牌局统计',
            urlName:'mtt'
        }, {
            itemList:'盲注级别统计',
            urlName:'blinds'
        }
        ]
    },
    {
        'item':'联盟、俱乐部数据统计',
        'children':[{
            itemList:'汇总统计表',
            urlName:'total'
        }
        ]
    },
    {
        'item':'道具数据统计',
        'children':[{
            itemList:'各牌局局内道具消耗统计',
            urlName:'props'
        }, {
            itemList:'道具消耗汇总统计',
            urlName:'propsTotal'
        }
        ]
    },
    {
        'item':'小游戏统计',
        'children':[{
            itemList:'小游戏统计',
            urlName:'littleGame'
        }
        ]
    },
    {
        'item':'权限设置',
        'children':[{
            itemList:'角色设置',
            urlName:'characterSet'
        }
        ]
    }
];