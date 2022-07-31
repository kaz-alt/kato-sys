$(function(){
	"user strict";

  setUpData();

  function setUpData() {

    $.ajax({
      type : "GET",
      url : '/demo/analysis/get_data',
      catch: false,
    }).done(function(data){
      let today = new Date();
      for (let i in data) {
        let map = {};
        let dataMap = data[i];
        let targetDate = new Date(dataMap.latestOrderDate);
        let diff = (today - targetDate) / 86400000;
        if (diff < 366) {
          map.x = Math.floor(366 - Number(diff));
        } else {
          map.x = 1;
        }
        map.y = dataMap.totalAmount;
        map.z = dataMap.frequentPurchase;
        map.name = dataMap.clientName;
        map.orderDate = dataMap.latestOrderDate;
        chart.series[0].addPoint(map, true, false);
      }
    }).fail(function(){
      alert("データの取得に失敗しました");
    })
  }

  let chart = Highcharts.chart('analysis-bubble', {

    chart: {
        type: 'bubble',
        plotBorderWidth: 1,
        zoomType: 'xy'
    },

    legend: {
        enabled: false
    },

    title: {
        text: 'RFM分析バブルチャート'
    },

    accessibility: {
        point: {
            valueDescriptionFormat: '{index}. {point.name}, 最新購買日: {point.orderDate}, 購買金額: ¥{point.y}, 購買頻度: {point.z}回'
        }
    },

    xAxis: {
        startOnTick: true,
        endOnTick: false,
        title: {
            text: '最新購買日'
        },
        labels: {
          formatter: function () {
            return 365 - Number(this.value) + '日前';
        }
        },
        accessibility: {
            rangeDescription: 'Range: 60 to 100 grams.'
        }
    },

    yAxis: {
        startOnTick: false,
        endOnTick: true,
        title: {
            text: '購買金額'
        },
        labels: {
            format: '{value}万円'
        },
    },

    tooltip: {
        useHTML: true,
        headerFormat: '<table>',
        pointFormat: '<tr><th colspan="2"><h3>{point.name}</h3></th></tr>' +
            '<tr><th>最新購買日:</th><td>{point.orderDate}</td></tr>' +
            '<tr><th>購買金額:</th><td class="mike">¥{point.y}</td></tr>' +
            '<tr><th>購買頻度:</th><td>{point.z}回</td></tr>',
        footerFormat: '</table>',
        followPointer: true
    },

    plotOptions: {
        series: {
            dataLabels: {
                enabled: true,
                format: '{point.name}'
            }
        }
    },

    credits: false,

    series: [{
      data: []
    }],

    colorAxis: [{
      maxColor: '#000fb0',
      minColor: '#e3e5ff',
  }]

});


})