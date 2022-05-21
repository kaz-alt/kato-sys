$(function(){
	"user strict";

	$('img.card').on('click', function() {
		let element = $(this).clone();
		$('#business-card-zoom-modal').find('.zoom-card').html($(element));
		$('#business-card-zoom-modal').modal('show');
	})

	/**
	 * 顧客名取得
	 */
	window.main.createClientOption($('#business-card-form select[name="companyName"]'));
	window.main.createClientOption($('#businessCardSearchForm select[name="companyNameList"]'));

    let path = location.pathname;
    /**
	 * 検索用ページネーション
	 */
	window.main.setPagenationForSearch($('#businessCardSearchForm'), path);

	$(document).on('hide.bs.modal', '#business-card-edit-modal', function() {
		window.main.createClientOption($('#business-card-form select[name="companyName"]'));
	})

	$('.business-card-show-btn').on('click', function(event){
		event.preventDefault();

　　		let paramUrl = $(this).data("href");
        let data = $(this).data("id");

　　		// fragment(html)取得
　　		$.ajax({
	 		type : "GET",
　　　　		url : paramUrl,
			data : {id : data},
　　　　		dataType : "html"
　　		}).done(function(data){
 			$('#business-card-edit-form').html(data);
		    window.main.createClientOption($('#business-card-edit-form select[name="companyName"]'));
            $('#business-card-edit-form #exchangeDateForEdit').datepicker(window.main.datepicker());
		    let downloadBtn = $('#img-download-btn');
			if (downloadBtn.length) {
				$('#business-card-edit-form input[name="image"]').prop('disabled', true);
				$('#changeImgFile').on('change', function() {
					if ($(this).prop('checked')) {
						$('#business-card-edit-form input[name="image"]').prop('disabled', false);
					} else {
						$('#business-card-edit-form input[name="image"]').prop('disabled', true);
					}
				})
				$('#img-download-btn').on('click', function() {
					downloadImage($(this));
				})
			}
		}).fail(function(){
			alert("データ取得に失敗しました");
		})
	})

	$('#business-card-edit-button').click(function(){

		let $form = $('#business-card-edit-form');

		$form.submit();
	})

	$('.del-business-card').click(function(){

		let id = $(this).data('id');
		let url = $(this).data('href');

		let _csrf = $('meta[name="_csrf"]').attr('content');

		let confirm = window.confirm('データを削除します。よろしいですか？');

		if(confirm){

			$('<form/>', {action: url, method: 'post'})
  				.append($('<input/>', {type: 'hidden', name: 'id', value: id}))
  				.append($('<input/>', {type: 'hidden', name: '_csrf', value: _csrf}))
  				.appendTo(document.body)
  				.submit();
		}
	})

   window.main.controlResetSearchFormButton('#businessCardSearchForm');

	$('.search-btn').on('click', function(){
		$('#businessCardSearchForm').find('#pageNumber').val(1);
		$('#businessCardSearchForm').submit();
	});

	function downloadImage($button) {
		let url = $button.data('href');
		let id = $button.data('id');
		$('<form/>', {action: url, method: 'get'})
		.append($('<input/>', {type: 'hidden', name: 'id', value: id}))
		.appendTo(document.body)
		.submit();
	}

	$.extend($.validator.messages, {
   	 	required: '*入力必須です',
		date: "*正しい日付形式で入力してください",
		dateFormat: "*正しい日付形式で入力してください"
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
    	companyName: {required: true},
		name: {required: true},
		department: {required: true},
		exchangeDate: {date: true, dateFormat: true},
		startExchangeDate: {date: true, dateFormat: true},
		endExchangeDate: {date: true, dateFormat: true}
  	};

  	//入力項目ごとのエラーメッセージ定義
  	let messages = {
    	companyName: {
      		required: "*会社名を入力してください"
    	},
    	name: {
      		required: "*氏名を選択してください"
    	}
  	};

	validate($("#business-card-form"));

	validate($("#business-card-edit-form"));

	validate($("#businessCardSearchForm"));

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
					} else{
						error.insertAfter(element);
					}
        		}
      		}
    	});
	}
})