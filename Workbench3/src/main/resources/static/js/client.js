$(function(){
	"user strict";

	$('#client-table').dataTable({
		bDestroy: true,
		lengthChange: false,
    	searching: false,
    	ordering: false,
    	info: false,
    	paging: false,
		language: window.main.getLanguageForDataTable()
	});

	$(document).on('click', '.contact-header', function() {
		if (!$(this).hasClass('shown')) {
			$(this).find('span').text('非表示');
			$(this).addClass('shown');
		} else {
			$(this).find('span').text('表示');
			$(this).removeClass('shown');
		}
	});

	window.main.sortTableColumn('#client-table', '#clientSearchForm');
	window.main.resetForm('#clientSearchModal');

	/**
	 * 顧客名取得
	 */
	window.main.createClientOption($('#clientSearchForm select[name="targetClientNameList"]'));
	/**
	 * 担当者名取得
	 */
    window.main.createEmployeeOptionWithId($('#client-form select[name="employeeIdList"]'));
	window.main.createEmployeeOptionWithId($('#clientSearchForm select[name="ourEmployeeIdList"]'));

	let path = location.pathname;
    /**
	 * 検索用ページネーション
	 */
	window.main.setPagenationForSearch($('#clientSearchForm'), path);


	$('.search-btn').on('click', function(){
		$('#clientSearchForm').find('#pageNumber').val(1);
		$('#clientSearchForm').submit();
	})

	$('#del-clientEmployee-button').hide();

	let clientEmployeeCount = 1;

	$('#add-clientEmployee-button').on('click', function(){

		let clientEmployeeContent = $('#clientEmployeeContent-1').clone();
		clientEmployeeContent.find('input,select,textarea').each(function(i,e){
			let $element= $(this);
			let replacedId = $element.attr('id').replace('0', clientEmployeeCount);
			let replacedName = $element.attr('name').replace('0', clientEmployeeCount);
			if($element[0].localName=='input' || $element[0].localName=='textarea'){
				$element.val("");
			} else if($element[0].localName=='select'){
				$element.val(1);
			}
			$element.attr('id', replacedId).attr('name', replacedName);

			$element.next('label').remove();
		})

		clientEmployeeContent
		  .attr('id', 'clientEmployeeContent-' + parseInt(clientEmployeeCount + 1))
		  .find('#clientEmployeeCount').text('（' + parseInt(clientEmployeeCount + 1) + '人目）');

		$('#clientEmployeeContent-' + clientEmployeeCount).after(clientEmployeeContent);

		$('#del-clientEmployee-button').show();
		clientEmployeeCount++;

		if(clientEmployeeCount > 2){
			$('#add-clientEmployee-button').hide();
		}
		validateClientEmployee();
	})

	$('#del-clientEmployee-button').on('click', function(){

		$('#parent-div').prev('div').remove();
		clientEmployeeCount--;

		if(clientEmployeeCount < 3){
			$('#add-clientEmployee-button').show();
			if(clientEmployeeCount == 1){
				$('#del-clientEmployee-button').hide();
			}
		}
	})


	$('.client-edit-button').on('click', function(event){
		event.preventDefault();

　　		let paramUrl = $(this).data("href") + '?id=' + $(this).data("id");

　　		// fragment(html)取得
　　		$.ajax({
	 				type : "GET",
　　　　		url : paramUrl,
　　　　		dataType : "html"
　　		}).done(function(data){
 			$('#client-edit-form').html(data);
		    window.main.createEmployeeOptionWithId($('#client-edit-form select[name="employeeIdList"]'));
			clientEmployeeContents();
			validateClientEmployee();
			$('#client-edit-modal').modal('show');
			$('.datepicker').datepicker(window.main.datepicker());
		}).fail(function(){
			alert("データ取得に失敗しました");
		})
	})

	function clientEmployeeContents(){

		let clientEmployeeSize = parseInt($("#clientEmployeeSize").val());

		let clientEmployeeEditCount = clientEmployeeSize;

		if(clientEmployeeSize < 2){
			$('#del-clientEmployeeEdit-button').hide();
		}

		$('#add-clientEmployeeEdit-button').on('click', function(){

			let clientEmployeeEditContent = $('#clientEmployeeEditContent-1').clone();
			clientEmployeeEditContent.find('input,select,textarea').each(function(){
				let $element= $(this);
				let replacedName = $element.attr('name').replace('0', clientEmployeeEditCount);
				if($element[0].localName=='input' || $element[0].localName=='textarea'){
					$element.val("");
				} else if($element[0].localName=='select'){
					$element.val(1);
				}
				$element.attr('name', replacedName);

				$element.next('label').remove();
			})

			clientEmployeeEditCount++;

			clientEmployeeEditContent
		  		.attr('id', 'clientEmployeeEditContent-' + parseInt(clientEmployeeEditCount))
		  		.find('#clientEmployeeEditCount').text('（' + parseInt(clientEmployeeEditCount) + '人目）');

			$('#clientEmployeeEditContent-' + parseInt(clientEmployeeEditCount - 1)).after(clientEmployeeEditContent);

			$('#del-clientEmployeeEdit-button').show();

			if(clientEmployeeEditCount > 4){
				$('#add-clientEmployeeEdit-button').hide();
			}
			validateClientEmployee();
		})

	  	$('#del-clientEmployeeEdit-button').on('click', function(){

			$('#parentEdit-div').prev('div').remove();
			clientEmployeeEditCount--;

			if(clientEmployeeEditCount < 5){
				$('#add-clientEmployeeEdit-button').show();
				if(clientEmployeeEditCount == 1){
					$('#del-clientEmployeeEdit-button').hide();
				}
			}
		})
	}

	$('#edit-client-button').click(function(){

		let $form = $('#client-edit-modal').find('form');

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
			$('#client-edit-form').submit();
		}
	});

	$('.client-del-button').click(function(){

		let id = $(this).data('id');
		let url = $(this).data('href');

		let _csrf = $('meta[name="_csrf"]').attr('content');

		let confirm = window.confirm('データを削除します。よろしいですか？');

		if(confirm){

			$('<form/>', {action: url, method: 'post'})
  				.append($('<input/>', {type: 'hidden', name: 'clientId', value: id}))
  				.append($('<input/>', {type: 'hidden', name: '_csrf', value: _csrf}))
  				.appendTo(document.body)
  				.submit();
		}
	})

	$.extend($.validator.messages, {
    	email: '*正しいメールアドレスの形式で入力して下さい',
   	 	required: '*入力必須です',
    	tel: "*正しい電話番号の形式で入力してください",
		date: "*正しい日付形式で入力してください"
  	});

	//追加ルールの定義
  	let methods = {
    	tel: function(value, element){
      	return this.optional(element) || /^\d{11}$|^\d{2}-\d{4}-\d{4}$|^\d{3}-\d{4}-\d{4}$/.test(value);
    	},
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
    	clientName: {required: true},
		address: {required: true},
		url: {url: true},
    	employeeIdList: {required: true},
		interviewDate: {required: true, date: true, dateFormat: true}
  	};

  	//入力項目ごとのエラーメッセージ定義
  	let messages = {
    	clientName: {
      		required: "*顧客名を入力してください"
    	},
    	address: {
      		required: "*所在地を入力してください"
    	},
		url: {
			url: "*正しいURLを入力してください"
		},
    	employeeIdList: {
      		required: "*当社担当者を選択してください"
    	},
		interviewDate: {
      		required: "*初回面談日を選択してください",
			dateFormat: "*正しい日付形式で入力してください"
    	}
  	};

	validate($("#client-form"));

	validate($("#client-edit-form"));


	validateClientEmployee();

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
          			error.insertAfter(element);
        		}
      		}
    	});
	}

	function validateClientEmployee(){
		$("#clientEmployeeBody, #clientEmployeeEditBody").find('input').each(function(){
			if($(this).attr('name').indexOf('tel') > 0){
				$(this).rules("add", {
            		required: true,
					tel: true,
    			});
			} else if($(this).attr('name').indexOf('email') > 0){
				$(this).rules("add", {
            		required: true,
					email: true,
    			});
			} else if($(this).attr('name').indexOf('position') > 0){
				;
			} else{
				$(this).rules("add", {
            		required: true
    			});
			}
   	 	});
	}

})