$(function(){
	"user strict";

	let path = location.pathname;

	/**
	 * 検索用ページネーション
	 */
	window.main.setPagenationForSearch($('#inquirySearchForm'), path);

	$('.search-btn').on('click', function(){
		$('#inquirySearchForm').find('#pageNumber').val(1);
		$('#inquirySearchForm').submit();
	});

	/**
	 * 顧客名取得
	 */
	window.main.createClientOptionWithId($('#inquirySearchForm select[name="clientIdList"]'));
	window.main.createClientOptionWithId($('#inquiry-form select[name="clientId"]'));
	/**
	 * 担当者名取得
	 */
    window.main.createEmployeeOptionWithId($('#inquiry-form select[name="responsibleStaffId"]'));
	window.main.createEmployeeOptionWithId($('#inquirySearchForm select[name="targetResponsibleStaffIdList"]'));

	$('#inquiry-table').dataTable({
		bDestroy: true,
		lengthChange: false,
    	searching: false,
    	ordering: false,
    	info: false,
    	paging: false,
		language: window.main.getLanguageForDataTable()
	});

	window.main.sortTableColumn('#inquiry-table', '#inquirySearchForm');
	window.main.resetForm('#inquirySearchForm');

	$('#inquiry-form .solved-body').hide();
	$('#inquiry-form').on('click', '.hasSolved', function(){
		if($(this).prop('checked')){
			$('.solved-body').show();
		} else {
			$('.solved-body').hide();
		}
	})

	$('.inquiry-edit-button').on('click', function(event){
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
 			$('#inquiry-edit-form').html(data);
		    window.main.createEmployeeOptionWithId($('#inquiry-edit-form select[name="responsibleStaffId"]'));
		    window.main.createClientOptionWithId($('#inquiry-edit-form select[name="clientId"]'));
			$('.datepicker').datepicker(window.main.datepicker());
			$('#inquiry-edit-form').find('.solved-body').hide();
			if($('#inquiry-edit-form').find('.hasSolved:checked').val()){
				$('#inquiry-edit-form').find('.solved-body').show();
			}
			$('#inquiry-edit-form label.label-solved').on('click', function() {
				let isChecked = $('#inquiry-edit-form').find('input.hasSolved').prop('checked');
				if (isChecked) {
					$('#inquiry-edit-form').find('input.hasSolved').prop('checked', false);
					$('#inquiry-edit-form').find('.solved-body').hide();
				} else {
					$('#inquiry-edit-form').find('input.hasSolved').prop('checked', true);
					$('#inquiry-edit-form').find('.solved-body').show();
				}
			})
			$('#inquiry-edit-form').on('click', '.hasSolved', function(){
				if($(this).prop('checked')){
					$('.solved-body').show();
				} else {
					$('.solved-body').hide();
				}
			})
		}).fail(function(){
			alert("データ取得に失敗しました");
		})
	})

	$('#edit-inquiry-button').click(function(){

		let $form = $('#inquiry-edit-modal').find('form');

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
			$('#inquiry-edit-form').submit();
		}
	})

	$('.inquiry-del-button').click(function(){

		let id = $(this).data('id');
		let url = $(this).data('href');

		let _csrf = $('meta[name="_csrf"]').attr('content');

		let confirm = window.confirm('データを削除します。よろしいですか？');

		if(confirm){

			let $form = $('<form/>', {action: url, method: 'post'})
  				.append($('<input/>', {type: 'hidden', name: 'inquiryId', value: id}))
  				.append($('<input/>', {type: 'hidden', name: '_csrf', value: _csrf}))
  				.appendTo(document.body);

		console.log($form.serialize());

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
    	contentType: {required: true},
		clientId: {required: true, min: 1},
		content: {required: true},
    	occurredDate: {required: true, date: true, dateFormat: true},
		responsibleStaffId: {required: true, min: 1},
		solvedDate: {date: true, dateFormat: true},
		startOccurredDate: {date: true, dateFormat: true},
		endOccurredDate: {date: true, dateFormat: true},
		startSolvedDate: {date: true, dateFormat: true},
		endSolvedDate: {date: true, dateFormat: true}
  	};

  	//入力項目ごとのエラーメッセージ定義
  	let messages = {
    	contentType: {
      		required: "*内容を入力してください"
    	},
    	clientId: {
      		required: "*顧客を選択してください",
			min: "*顧客を選択してください"
    	},
		responsibleStaffId: {
			required: "*担当責任者を選択してください",
			min: "*担当責任者を選択してください"
		}
  	};

	validate($("#inquiry-form"));

	validate($("#inquiry-edit-form"));

	validate($("#inquirySearchForm"));

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