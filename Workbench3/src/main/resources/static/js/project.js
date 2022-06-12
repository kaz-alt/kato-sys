$(function(){
	"user strict";

	let path = location.pathname;

	/**
	 * 検索用ページネーション
	 */
	window.main.setPagenationForSearch($('#projectSearchForm'), path);

	$('.search-btn').on('click', function(){
		$('#projectSearchForm').find('#pageNumber').val(1);
		$('#projectSearchForm').submit();
	});

	/**
	 * ソート
	 */
	window.main.sortTableColumn('#project-table', '#projectSearchForm');
	window.main.controlResetSearchFormButton('#projectSearchForm');

	/**
	 * 顧客名取得
	 */
	window.main.createClientOptionWithId($('#projectSearchForm select[name="clientIdList"]'));
	window.main.createClientOptionWithId($('#project-form select[name="clientId"]'));
	/**
	 * 担当者名取得
	 */
	$('#project-form select[name="clientId"]').on('change', function() {
		window.main.createEmployeeOptionByClientId($('#project-form select[name="clientId"]'), $('#project-form select[name="clientStaffIdList"]'));
	})
	window.main.createEmployeeOptionWithId($('#projectSearchForm select[name="ourEmployeeIdList"]'));

	window.main.sortTableColumn('#product-table', '#productSearchForm');
	window.main.controlResetSearchFormButton('#productSearchForm');

	$('#project-table').dataTable({
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

	$.fn.dataTable.ext.order['percent'] = function (settings, col){
    return this.api().column(col, {order:'index'}).nodes().map(function (td, i) {
    if (! $(td).html()) return 0;
    let num = $(td).find('span').html().replace('％', '');
    return Number(num);
    });
	}

	$.fn.dataTable.ext.order['salesAmount'] = function (settings, col){
    return this.api().column(col, {order:'index'}).nodes().map(function (td, i) {
    if (! $(td).html()) return 0;
    let num = $(td).find('span').html().replace('¥', '').replace(',', '');
    return Number(num);
    });
	}

	function setColumnDefines() {
		if(path.indexOf('/detail') > 0){
			return [
				{targets: 1, orderable: false},
				{targets: 3, orderable: true, orderDataType: 'percent', type: 'num'},
				{targets: 4, orderable: true, orderDataType: 'salesAmount', type: 'num'},
				{targets: 9, orderable: false},
				{targets: 10, orderable: false}
			];
		}
		return [];
	}

	$('.project-edit-button').on('click', function(event){
		event.preventDefault();

    let paramUrl = $(this).data("href");

    // fragment(html)取得
    $.ajax({
      type : "GET",
      url : paramUrl,
      dataType : "html"
    }).done(function(data){
    $('#project-edit-form').html(data);
      $('.datepicker').datepicker(window.main.datepicker());
    $('#project-edit-form .select2').select2({
      placeholder: "※ 入力または選択（複数）が可能です。",
        allowClear: false,
        language: "ja"
      });
      window.main.createClientOptionWithId($('#project-edit-form select[name="clientId"]'));
			$('#project-edit-form select[name="clientId"]').on('change', function() {
				$('#project-edit-form select[name="clientStaffIdList"]').find('option').remove();
				window.main.createEmployeeOptionByClientId($('#project-edit-form select[name="clientId"]'), $('#project-edit-form select[name="clientStaffIdList"]'));
			})
		}).fail(function(){
			alert("データ取得に失敗しました");
		})
	})

	$('#edit-project-button').click(function(){

		let $form = $('#project-edit-modal').find('form');

		if(path.indexOf('detail') > -1){
			$.ajax({
        type : $form.attr('method'),
        url : $form.attr('action'),
				data: $form.serialize()
      }).done(function(){
				alert('データを更新しました。');
				location.reload();
			}).fail(function(){
				alert("データの更新に失敗しました");
			})
		} else{
			$('#project-edit-form').submit();
		}
	})

	$('.project-del-button').click(function(){

		let id = $(this).data('id');
		let url = $(this).data('href');
    let projectUrl = $(this).data('project');

		let _csrf = $('meta[name="_csrf"]').attr('content');

		let confirm = window.confirm('データを削除します。よろしいですか？');

		if(confirm){

			$form = $('<form/>', {action: url, method: 'post'})
        .append($('<input/>', {type: 'hidden', name: 'projectId', value: id}))
        .append($('<input/>', {type: 'hidden', name: '_csrf', value: _csrf}))
        .appendTo(document.body)
        .submit();

			if(path.indexOf('detail') > -1){
					$.ajax({
            type : 'POST',
            url : url,
						data: $form.serialize()
          }).done(function(){
						alert('データを削除しました。');
						location.href= projectUrl;
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
    projectName: {required: true},
  clientId: {required: true, min: 1},
  clientStaffIdList: {required: true},
    estimatedOrderAmount: {min: 0, number: true},
  expectedOrderDate: {date: true, dateFormat: true},
  orderProbability: {min: 0, max: 100, number: true},
  startEstimatedOrderAmount: {min: 0, number: true},
  endEstimatedOrderAmount: {min: 0, number: true},
  startOrderProbability: {min: 0, max: 100, number: true},
  endOrderProbability: {min: 0, max: 100, number: true},
  startExpectedOrderDate: {date: true, dateFormat: true},
  endExpectedOrderDate: {date: true, dateFormat: true}
  };

  //入力項目ごとのエラーメッセージ定義
  let messages = {
    projectName: {
        required: "*案件名を入力してください"
    },
    clientId: {
        required: "*顧客を選択してください",
    min: "*顧客を選択してください"
    },
  clientStaffIdList: {
    required: "*当社担当者を入力してください",
  },
  expectedOrderDate: {
        required: "*受注日を選択してください",
    dateFormat: "*正しい日付形式で入力してください"
    },
      estimatedOrderAmount: {
        min: "*0以上で入力してください",
          number: "*数値を入力してください"
      },
      orderProbability: {
        min: "*0以上で入力してください",
    max: "*100以下で入力してください",
          number: "*数値を入力してください"
      },
      startEstimatedOrderAmount: {
        min: "*0以上で入力してください",
          number: "*数値を入力してください"
      },
      endEstimatedOrderAmount: {
        min: "*0以上で入力してください",
          number: "*数値を入力してください"
      },
      startOrderProbability: {
        min: "*0以上で入力してください",
    max: "*100以下で入力してください",
          number: "*数値を入力してください"
      },
      endOrderProbability: {
        min: "*0以上で入力してください",
    max: "*100以下で入力してください",
          number: "*数値を入力してください"
      }
  };

validate($("#project-form"));

validate($("#project-edit-form"));

validate($('#projectSearchForm'));

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