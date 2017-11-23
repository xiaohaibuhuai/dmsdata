MainApp.controller('SumDailyCtrls',  function($scope,TabService) {
    parent.$.messager.progress('close');
	
    Highcharts.data({
        csv: document.getElementById('tsv').innerHTML,
        itemDelimiter: '\t',
        parsed: function (columns) {
            var brands = {},
                brandsData = [],
                versions = {},
                drilldownSeries = [];
            // 解析百分比字符串
            columns[1] = $.map(columns[1], function (value) {
                if (value.indexOf('%') === value.length - 1) {
                    value = parseFloat(value);
                }
                return value;
            });
            $.each(columns[0], function (i, name) {
                var brand,
                    version;
                if (i > 0) {
                    // Remove special edition notes
                    name = name.split(' -')[0];
                    // 拆分
                    version = name.match(/([0-9]+[\.0-9x]*)/);
                    if (version) {
                        version = version[0];
                    }
                    brand = name.replace(version, '');
                    //创建主数据
                    if (!brands[brand]) {
                        brands[brand] = columns[1][i];
                    } else {
                        brands[brand] += columns[1][i];
                    }
                    // 创建版本数据
                    if (version !== null) {
                        if (!versions[brand]) {
                            versions[brand] = [];
                        }
                        versions[brand].push(['v' + version, columns[1][i]]);
                    }
                }
            });
            $.each(brands, function (name, y) {
                brandsData.push({
                    name: name,
                    y: y,
                    drilldown: versions[name] ? name : null
                });
            });
            $.each(versions, function (key, value) {
                drilldownSeries.push({
                    name: key,
                    id: key,
                    data: value
                });
            });
            // 创建图例
            $('#container1').highcharts({
                chart: {
                    type: 'pie'
                },
                title: {
                    text: '2013年11月浏览器市场份额'
                },
                subtitle: {
                    text: '单击每个浏览器品牌不同版本的具体信息，数据来源: netmarketshare.com.'
                },
                plotOptions: {
                    series: {
                        dataLabels: {
                            enabled: true,
                            format: '{point.name}: {point.y:.1f}%'
                        }
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                    pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
                },
                series: [{
                    name: '品牌',
                    colorByPoint: true,
                    data: brandsData
                }],
                drilldown: {
                    series: drilldownSeries
                }
            });
            
            //2
            $('#container2').highcharts({
                chart: {
                    type: 'pie'
                },
                title: {
                    text: '2013年11月浏览器市场份额'
                },
                subtitle: {
                    text: '单击每个浏览器品牌不同版本的具体信息，数据来源: netmarketshare.com.'
                },
                plotOptions: {
                    series: {
                        dataLabels: {
                            enabled: true,
                            format: '{point.name}: {point.y:.1f}%'
                        }
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                    pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
                },
                series: [{
                    name: '品牌',
                    colorByPoint: true,
                    data: brandsData
                }],
                drilldown: {
                    series: drilldownSeries
                }
            });
            
            
            //3
            $('#container3').highcharts({
                chart: {
                    type: 'pie'
                },
                title: {
                    text: '2013年11月浏览器市场份额'
                },
                subtitle: {
                    text: '单击每个浏览器品牌不同版本的具体信息，数据来源: netmarketshare.com.'
                },
                plotOptions: {
                    series: {
                        dataLabels: {
                            enabled: true,
                            format: '{point.name}: {point.y:.1f}%'
                        }
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                    pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
                },
                series: [{
                    name: '品牌',
                    colorByPoint: true,
                    data: brandsData
                }],
                drilldown: {
                    series: drilldownSeries
                }
            });
            
            
            //4
            $('#container4').highcharts({
                chart: {
                    type: 'pie'
                },
                title: {
                    text: '2013年11月浏览器市场份额'
                },
                subtitle: {
                    text: '单击每个浏览器品牌不同版本的具体信息，数据来源: netmarketshare.com.'
                },
                plotOptions: {
                    series: {
                        dataLabels: {
                            enabled: true,
                            format: '{point.name}: {point.y:.1f}%'
                        }
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                    pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
                },
                series: [{
                    name: '品牌',
                    colorByPoint: true,
                    data: brandsData
                }],
                drilldown: {
                    series: drilldownSeries
                }
            });
            
            
            //5
            $('#container5').highcharts({
                chart: {
                    type: 'pie'
                },
                title: {
                    text: '2013年11月浏览器市场份额'
                },
                subtitle: {
                    text: '单击每个浏览器品牌不同版本的具体信息，数据来源: netmarketshare.com.'
                },
                plotOptions: {
                    series: {
                        dataLabels: {
                            enabled: true,
                            format: '{point.name}: {point.y:.1f}%'
                        }
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                    pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
                },
                series: [{
                    name: '品牌',
                    colorByPoint: true,
                    data: brandsData
                }],
                drilldown: {
                    series: drilldownSeries
                }
            });
            
            
            //6
            $('#container6').highcharts({
                chart: {
                    type: 'pie'
                },
                title: {
                    text: '2013年11月浏览器市场份额'
                },
                subtitle: {
                    text: '单击每个浏览器品牌不同版本的具体信息，数据来源: netmarketshare.com.'
                },
                plotOptions: {
                    series: {
                        dataLabels: {
                            enabled: true,
                            format: '{point.name}: {point.y:.1f}%'
                        }
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                    pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
                },
                series: [{
                    name: '品牌',
                    colorByPoint: true,
                    data: brandsData
                }],
                drilldown: {
                    series: drilldownSeries
                }
            });
            
            
            //7
            $('#container7').highcharts({
                chart: {
                    type: 'pie'
                },
                title: {
                    text: '2013年11月浏览器市场份额'
                },
                subtitle: {
                    text: '单击每个浏览器品牌不同版本的具体信息，数据来源: netmarketshare.com.'
                },
                plotOptions: {
                    series: {
                        dataLabels: {
                            enabled: true,
                            format: '{point.name}: {point.y:.1f}%'
                        }
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                    pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
                },
                series: [{
                    name: '品牌',
                    colorByPoint: true,
                    data: brandsData
                }],
                drilldown: {
                    series: drilldownSeries
                }
            });
            
            
            //8
            $('#container8').highcharts({
                chart: {
                    type: 'pie'
                },
                title: {
                    text: '2013年11月浏览器市场份额'
                },
                subtitle: {
                    text: '单击每个浏览器品牌不同版本的具体信息，数据来源: netmarketshare.com.'
                },
                plotOptions: {
                    series: {
                        dataLabels: {
                            enabled: true,
                            format: '{point.name}: {point.y:.1f}%'
                        }
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                    pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
                },
                series: [{
                    name: '品牌',
                    colorByPoint: true,
                    data: brandsData
                }],
                drilldown: {
                    series: drilldownSeries
                }
            });
            
            
            //9
            $('#container9').highcharts({
                chart: {
                    type: 'pie'
                },
                title: {
                    text: '2013年11月浏览器市场份额'
                },
                subtitle: {
                    text: '单击每个浏览器品牌不同版本的具体信息，数据来源: netmarketshare.com.'
                },
                plotOptions: {
                    series: {
                        dataLabels: {
                            enabled: true,
                            format: '{point.name}: {point.y:.1f}%'
                        }
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                    pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
                },
                series: [{
                    name: '品牌',
                    colorByPoint: true,
                    data: brandsData
                }],
                drilldown: {
                    series: drilldownSeries
                }
            });
            
            
            
            //10
            $('#container10').highcharts({
                chart: {
                    type: 'pie'
                },
                title: {
                    text: '2013年11月浏览器市场份额'
                },
                subtitle: {
                    text: '单击每个浏览器品牌不同版本的具体信息，数据来源: netmarketshare.com.'
                },
                plotOptions: {
                    series: {
                        dataLabels: {
                            enabled: true,
                            format: '{point.name}: {point.y:.1f}%'
                        }
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                    pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
                },
                series: [{
                    name: '品牌',
                    colorByPoint: true,
                    data: brandsData
                }],
                drilldown: {
                    series: drilldownSeries
                }
            });
            
           
        }
    });
	

});

