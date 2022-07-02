$(function(){
	"user strict";

	let path = location.pathname;

	/**
	 * 検索用ページネーション
	 */
	window.main.setPagenationForSearch($('#activitySearchForm'), path);

	$('.search-btn').on('click', function(){
		$('#activitySearchForm').find('#pageNumber').val(1);
		$('#activitySearchForm').submit();
	});

  /**
	 * 担当者名取得
	 */
  window.main.createEmployeeOptionWithId($('#activity-form select[name="staffId"]'));
  window.main.createEmployeeOptionWithId($('#activitySearchForm select[name="staffIdList"]'));

	$('#activity-table').dataTable({
		bDestroy: true,
		lengthChange: false,
    searching: false,
    ordering: path.indexOf('/detail') > 0,
		columnDefs: setColumnDefines(),
    info: false,
    paging: false,
    lengthMenu: [5],
		language: window.main.getLanguageForDataTable()
	});

	$.fn.dataTable.ext.order['count'] = function (settings, col){
    return this.api().column(col, {order:'index'}).nodes().map(function (td, i) {
    if (! $(td).html()) return 0;
    let num = $(td).html().replace('個', '');
    return Number(num);
    });
	}

	$.fn.dataTable.ext.order['sales'] = function (settings, col){
    return this.api().column(col, {order:'index'}).nodes().map(function (td, i) {
    if (! $(td).html()) return 0;
    let num = $(td).html().replace('万円', '').replace(',', '');
    return Number(num);
    });
	}

	function setColumnDefines() {
		if(path.indexOf('/detail') > 0){
			return [
				{targets: 2, orderable: true, orderDataType: 'count', type: 'num'},
				{targets: 3, orderable: true, orderDataType: 'sales', type: 'num'},
				{targets: 4, orderable: false},
				{targets: 5, orderable: false}
			];
		}
		return [];
	}

	window.main.sortTableColumn('#activity-table', '#activitySearchForm');
	window.main.resetForm('#activitySearchForm');

	$('.activity-edit-button').on('click', function(event){
		event.preventDefault();

    let paramUrl = $(this).data("href") + '?id=' + $(this).data("id");

    // fragment(html)取得
    $.ajax({
      type : "GET",
      url : paramUrl,
      dataType : "html"
    }).done(function(data){
      $('#activity-edit-form').html(data);
      $('.datepicker').datepicker(window.main.datepicker());
      window.main.createClientOptionWithId($('#activity-edit-form select[name="clientId"]'));
    }).fail(function(){
      alert("データ取得に失敗しました");
    })
  })

	$('#edit-activity-button').click(function(){

		let $form = $('#activity-edit-modal').find('form');

		if(path.indexOf('detail') > 0){
			$.ajax({
	 			type : $form.attr('method'),
　　　　			url : $form.attr('action'),
				data: $form.serialize(),
　　			}).done(function(){
				alert('データを更新しました。');
				location.reload();
			}).fail(function(){
				alert("データの更新に失敗しました");
			})
		} else{
			$('#product-edit-form').submit();
		}
	})

	$('.product-del-button').click(function(){

		let id = $(this).data('id');
		let url = $(this).data('href');

		let _csrf = $('meta[name="_csrf"]').attr('content');

		let confirm = window.confirm('データを削除します。よろしいですか？');

		if(confirm){

			let $form = $('<form/>', {action: url, method: 'POST'})
	  				.append($('<input/>', {type: 'hidden', name: 'productId', value: id}))
	  				.append($('<input/>', {type: 'hidden', name: '_csrf', value: _csrf}))
	  				.appendTo(document.body);

			if(path.indexOf('detail') > 0){
				$.ajax({
		 			type : 'POST',
					url : url,
					data: $form.serialize()
	　　			}).done(function(){
					alert('データを削除しました。');
					location.reload();
				}).fail(function(){
					alert("データの削除に失敗しました");
				})

		    } else{

	  				$form.submit();

		    }
		}
	})

	$.extend($.validator.messages, {
   	 	required: '*入力必須です',
		date: "*正しい日付形式で入力してください"
  	});

	//追加ルールの定義
  	let methods = {
		dateFormat: function(value, element){
      	return this.optional(element) || /^\d{4}-\d{2}-\d{2}$/.test(value);
    	}
  	};

	//メソッドの追加
  	$.each(methods, function(key) {
    	$.validator.addMethod(key, this);
  	});

  	//入力項目の検証ルール定義
  	let rules = {
    	productName: {required: true},
		clientId: {required: true, min: 1},
		productQuantity: {required: true, min: 1, number: true},
    	productSales: {required: true, min: 1, number: true},
		purchasedDate: {required: true, date: true, dateFormat: true},
		startProductSales: {min: 0, number: true},
		endProductSales: {min: 0, number: true},
		startPurchasedDate: {date: true, dateFormat: true},
		endPurchasedDate: {date: true, dateFormat: true}
  	};

  	//入力項目ごとのエラーメッセージ定義
  	let messages = {
    	productName: {
      		required: "*製品・サービス名を入力してください"
    	},
    	clientId: {
      		required: "*顧客を選択してください",
			min: "*顧客を選択してください"
    	},
		productQuantity: {
			required: "*個数を入力してください",
			min: "*１以上で入力してください",
			number: "*数値を入力してください"
		},
    	productSales: {
      		required: "*金額を選択してください",
			min: "*１以上で入力してください",
			number: "*数値を入力してください"
    	},
		interviewDate: {
      		required: "*受注日を選択してください",
			dateFormat: "*正しい日付形式で入力してください"
    	},
        startProductSales: {
	        min: "*0以上で入力してください",
            number: "*数値を入力してください"
        },
        endProductSales: {
	        min: "*0以上で入力してください",
            number: "*数値を入力してください"
        }
  	};

	validate($("#product-form"));

	validate($("#product-edit-form"));

	validate($('#productSearchForm'));

	function validate($form){

		$form.validate({
      		rules: rules,
      		messages: messages,

      		//エラーメッセージ出力箇所調整
      		errorPlacement: function(error, element){
 				error.addClass('text-danger');
				if (element.is('select')) {
          			error.appendTo(element.parent());
        		}else {
					if(element.hasClass('group') || element.hasClass('search')){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
        		}
      		}
    	});
	}

})