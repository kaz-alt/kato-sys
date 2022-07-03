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

  /**
	 * 案件名取得
	 */
  window.main.createProjectOptionByName($('#activity-form select[name="projectId"]'));
  window.main.createProjectOptionByName($('#activitySearchForm select[name="projectIdList"]'));

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

    let paramUrl = $(this).data("href");
    let id = $(this).data("id");

    // fragment(html)取得
    $.ajax({
      type : "GET",
      url : paramUrl,
      data : {id : id},
      dataType : "html"
    }).done(function(data){
      $('#activity-edit-form').html(data);
      $('.datepicker').datepicker(window.main.datepicker());
      window.main.createEmployeeOptionWithId($('#activity-edit-form select[name="staffId"]'));
      window.main.createProjectOptionByName($('#activity-edit-form select[name="projectId"]'));
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
				data: $form.serialize()
      }).done(function(){
        alert('データを更新しました。');
        location.reload();
			}).fail(function(){
				alert("データの更新に失敗しました");
			})
		} else{
			$('#activity-edit-form').submit();
		}
	})

	$('.activity-del-button').click(function(){

		let id = $(this).data('id');
		let url = $(this).data('href');

		let _csrf = $('meta[name="_csrf"]').attr('content');

		let confirm = window.confirm('データを削除します。よろしいですか？');

		if(confirm){

			let $form = $('<form/>', {action: url, method: 'POST'})
        .append($('<input/>', {type: 'hidden', name: 'id', value: id}))
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
		date: "*正しい日付形式で入力してください",
    maxlength: "*2,000文字以内で入力してください"
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
    staffId: {required: true, min: 1},
    activityDate: {required: true, date: true, dateFormat: true},
    content: {required: true, maxlength: 2000},
    startActivityDate: {date: true, dateFormat: true},
    endActivityDate: {date: true, dateFormat: true}
  };

  //入力項目ごとのエラーメッセージ定義
  let messages = {
    clientsstaffIdtaffIdId: {required: "*社員を選択してください",min: "*社員を選択してください"},
    activityDate: {required: "*活動日を選択してください", dateFormat: "*正しい日付形式で入力してください"},
  };

	validate($("#activity-form"));

	validate($("#activity-edit-form"));

	validate($('#activitySearchForm'));

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