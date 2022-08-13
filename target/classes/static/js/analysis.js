$(function(){
	"user strict";

  let rankMap = {1: 'ランク5', 2: 'ランク4', 3:'ランク3', 4: 'ランク2', 5: 'ランク1'};

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
          map.x = Math.floor(365 - Number(diff));
        } else {
          map.x = 1;
        }
        map.y = dataMap.totalAmount;
        let dateScore = getDateScore(targetDate);
        let dateRank = rankMap[dateScore];
        let frequentScore = getFrequentScore(dataMap.frequentPurchase);
        let frequentRank = rankMap[frequentScore];
        let amountScore = getAmountScore(dataMap.totalAmount);
        let amountRank = rankMap[amountScore];
        let totalScore = dateScore + frequentScore + amountScore;
        map.dateScore = dateScore;
        map.dateRank = dateRank;
        map.frequentScore = frequentScore;
        map.frequentRank = frequentRank;
        map.amountScore = amountScore;
        map.amountRank = amountRank;
        map.frequentPurchase = dataMap.frequentPurchase;
        map.z = totalScore;
        map.name = dataMap.clientName;
        map.orderDate = targetDate;
        map.orderDateTooltip = formatDate(targetDate);
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
            valueDescriptionFormat: '{index}. {point.name}, 最新購買日: {point.orderDateTooltip}, 購買金額: {point.y}万円, 購買頻度: {point.frequentPurchase}回'
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
            let diff = 365 - Number(this.value);
            let date = new Date();
            date.setDate(date.getDate() - Number(diff));
            return formatDate(date);
          }
        },
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
            '<tr><th>最新購買日:</th><td>{point.orderDateTooltip}</td>' +
            '<th>{point.dateRank}</th></tr>' +
            '<tr><th>購買金額:</th><td>{point.y}万円</td>' +
            '<th>{point.amountRank}</th></tr>' +
            '<tr><th>購買頻度:</th><td>{point.frequentPurchase}回</td>' +
            '<th>{point.frequentRank}</th></tr>',
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
      maxColor: '#ffa0c0',
      minColor: '#FFF0F5',
    }]

  });

  function getDateScore(date) {

    let today = new Date();
    let todayTime = today.getTime();
    let dateTime = date.getTime();

    let diff = todayTime - dateTime;
    if (diff <= 0) {
      return 5;
    }

    let weekDiff = Math.abs(diff) / (7 * 24 * 60 * 60 * 1000);
    let monthDiff = (today.getFullYear() * 12 + today.getMonth()) - (date.getFullYear() * 12 - today.getMonth());

    if (weekDiff <= 3) {
      return 5;
    } else if (monthDiff <= 3) {
      return 4;
    } else if (monthDiff <= 6) {
      return 3;
    } else if (monthDiff <= 12) {
      return 2;
    } else {
      return 1;
    }
  }

  function getFrequentScore(value) {

    if (value >= 15) {
      return 5;
    } else if (value >= 10) {
      return 4;
    } else if (value >= 6) {
      return 3;
    } else if (value >= 3) {
      return 2;
    } else {
      return 1;
    }
  }

  function getAmountScore(value) {

    if (value >= 2000) {
      return 5;
    } else if (value >= 1000) {
      return 4;
    } else if (value >= 500) {
      return 3;
    } else if (value >= 100) {
      return 2;
    } else {
      return 1;
    }
  }

  function formatDate(dt) {
    let y = dt.getFullYear();
    let m = ('00' + (dt.getMonth()+1)).slice(-2);
    let d = ('00' + dt.getDate()).slice(-2);
    return (y + '/' + m + '/' + d);
  }

})