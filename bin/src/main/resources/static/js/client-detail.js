$(function(){
	"user strict";

	let beforeUrl = document.referrer;
	if(beforeUrl.indexOf('product') > 0){
		$('#product-tab').click();
	}
	if(beforeUrl.indexOf('inquiry_requirement_complaint') > 0){
		let type = location.search;
		if(type.indexOf('1') > 0){
			$('#inquiry-tab').click();
		} else if(type.indexOf('2') > 0){
			$('#requirement-tab').click();
		} else{
			$('#complaint-tab').click();
		}
	}

	$('#year').change(function(){
		$('#purchasedYearForm').submit();
	});

	$('#return-btn').on('click', function() {
		history.back();
	});

	$('.table th').removeClass('sort-pointer').find('i').hide();

	let dateFlg = 0;
	let nameFlg = 1;
	let numberFlg = 2;
	let salesFlg = 3;

	let dateList = [];
	let salesList = [];
	let nameList = [];
	let numberList = [];

	readyForChart();
	$('#yealy-sales-title').text($('#year').val() + '年度売上推移');
	let total = salesList.reduce((sum, element) => sum + element, 0);
	total = total * 10000;
	$('#yearly-sales-sum').text('¥' + total.toLocaleString());

	createStackedBarChart(nameList, dateList, salesList);
	createPieChart(nameList, numberList);

	function readyForChart(){

		$('#product-table').find('tbody tr td').each(function(i){
			switch(i){
			case dateFlg:
				let month = $(this).text().split('-')[1];
				dateList.push(month);
				dateFlg += 6;
				break;
			case salesFlg:
				let sales = $(this).text();
				salesList.push(parseInt(sales.split('万円')[0].replace(',', '')));
				salesFlg += 6;
				break;
			case nameFlg:
				nameList.push($(this).text());
				nameFlg += 6;
				break;
			case numberFlg:
				numberList.push($(this).text());
				numberFlg += 6;
				break;
			default:
				break;
			}
		})
	}

	function getData(month, sales){
		let dataList = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
		month = parseInt(month);
		let index = 0;

		if(month > 3){
			index = month - 4;
		} else{
			index = month + 8;
		}
		dataList[index] = sales;
		return dataList;
	}

	function createDataSets(nameList, dateList, salesList){

		let datasets = [];

		for(let i in nameList){

			let dataMap = {
          					label : nameList[i],
          					data : getData(dateList[i], salesList[i])
        				};

			datasets.push(dataMap);
		}
		return datasets;
	}

	function createPieLabel(nameList){

		let labelList = [];

		for(let i in nameList){
			if($.inArray(nameList[i], labelList) == -1){
				labelList.push(nameList[i]);
			}
		}
		return labelList;
	}

	function createPieDataSets(nameList, numberList){

		let dataMap = {};
		let labelList = createPieLabel(nameList);
		for(let i in labelList){
			dataMap[labelList[i]] = 0;
		}

		for(let i in nameList){
			let index = $.inArray(nameList[i], labelList);
			dataMap[labelList[index]] += parseInt(numberList[i]);
		}
		let dataList = [];
		Object.keys(dataMap).forEach(key => dataList.push(dataMap[key]));
		return dataList;
	}

	function createStackedBarChart(nameList, dateList, salesList){

		const barChartData = {
	      labels  : ['4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月', '1月', '2月', '3月'],
	      datasets: createDataSets(nameList,dateList,salesList)
	    }

		const ctx = $('#client-stackedBarChart');
	    let stackedBarChartData = $.extend(true, {}, barChartData)

	    let stackedBarChartOptions = {
	      responsive              : true,
	      maintainAspectRatio     : false,
	      scales: {
	        xAxes: [{
	          stacked: true,
	        }],
	        yAxes: [{
	          stacked: true,
	 		  ticks: {
	                    callback: function(value) {
	                        return value + '万円';
	                    }
	                }
	        }]
	      },
		  tooltips:{
	      	callbacks:
	    		{
	            label: function(tooltipItems, data) {
	            		if(tooltipItems.yLabel == "0"){
	                		return "";
	            		}
	            		return data.datasets[tooltipItems.datasetIndex].label + "：" + tooltipItems.yLabel + "万円";
	        		}
	    		}
			},
		    plugins: {
	            colorschemes: {
	                scheme: 'brewer.Paired12'
	            }
	        }
	    }

	    new Chart(ctx, {
	      type: 'bar',
	      data: stackedBarChartData,
	      options: stackedBarChartOptions
	    })
	}

	function createPieChart(nameList, numberList){

		if(!nameList.length){
			$('#product-warning').show();
		} else{
			$('#product-warning').hide();
		}

		const pieChartCanvas = $('#client-pieChart');

		const pieChartData = {
	      labels  : createPieLabel(nameList),
	      datasets: [{
					data: createPieDataSets(nameList, numberList)
					}]
	    }

    	const pieOptions = {
      	  maintainAspectRatio : false,
          responsive : true,
		  tooltips: {
          	callbacks: {
          		label: function (tooltipItem, data) {
					let totalValue = 0;
	                /* データセットの数だけ繰り返し */
	                for (var i = 0; i < data.datasets[0].data.length; i++) {
	                     /* 数値型でデータの数だけ加算してゆくことで合計値を取る */
	                     totalValue += Number(data.datasets[0].data[i]);
	                }
            		return data.labels[tooltipItem.index]
              				+ ": "
							+ (Math.round(data.datasets[0].data[tooltipItem.index] / totalValue * 100 * 10)/10)
							+ " %（個数："
              				+ data.datasets[0].data[tooltipItem.index]
              				+ "個）";
          			}
        		}
      		}
    	}

    	new Chart(pieChartCanvas, {
          type: 'pie',
          data: pieChartData,
          options: pieOptions
        })
	}


})