$(function(){
	"user strict";

  let path = location.pathname;
    /**
	 * 検索用ページネーション
	 */
	window.main.setPagenationForSearch($('#employeeSearchForm'), path);

	$('.employee-edit-button').on('click', function(event){
		event.preventDefault();

    let paramUrl = $(this).data("href") + '?id=' + $(this).data("id");

    // fragment(html)取得
    $.ajax({
      type : "GET",
      url : paramUrl,
      dataType : "html"
    }).done(function(data){
      $('#employee-edit-form').html(data);
		}).fail(function(){
			alert("データ取得に失敗しました");
		})
	});

  window.main.resetForm('#employeeSearchForm');

	$('.search-btn').on('click', function(){
		$('#employeeForm').find('#pageNumber').val(1);
		$('#employeeSearchForm').submit();
	});

	$('#edit-employee-button').click(function(){

		$('#employee-edit-form').submit();

	})

	$('.employee-del-button').click(function(){

		let id = $(this).data('id');
		let url = $(this).data('href');

		let _csrf = $('meta[name="_csrf"]').attr('content');

		let confirm = window.confirm('データを削除します。よろしいですか？');

		if(confirm){

			$('<form/>', {action: url, method: 'POST'})
        .append($('<input/>', {type: 'hidden', name: 'employeeId', value: id}))
        .append($('<input/>', {type: 'hidden', name: '_csrf', value: _csrf}))
        .appendTo(document.body)
        .submit();
		}
	})

	$.extend($.validator.messages, {
    required: '*入力必須です',
		email: '*正しいメールアドレスの形式で入力して下さい',
		tel: "*正しい電話番号の形式で入力してください",
    katakana: "*全角カタカナで入力してください"
  });

	//追加ルールの定義
  let methods = {
    tel: function(value, element){
      return this.optional(element) || /^\d{11}$|^\d{2}-\d{4}-\d{4}$|^\d{3}-\d{4}-\d{4}$/.test(value);
    },
    katakana: function(value, element){
      return this.optional(element) || /^([ァ-ヶー]+)$/.test(value);
    }
  };

	//メソッドの追加
  $.each(methods, function(key) {
    $.validator.addMethod(key, this);
  });

  //入力項目の検証ルール定義
  let rules = {
    lastName: {required: true},
    firstName: {required: true},
    lastNameKana: {required: true, katakana: true},
    firstNameKana: {required: true, katakana: true},
    department: {required: true},
    tel: {required: true, tel: true},
    email: {required: true, email: true}
  };

  //入力項目ごとのエラーメッセージ定義
  let messages = {
    name: {
        required: "*社員の氏名を入力してください"
    },
    department: {
        required: "*担当部署を入力してください"
    }
  };

	validate($("#employee-form"));

	validate($("#employee-edit-form"));

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
          if(element.hasClass('group')){
            error.appendTo(element.parent().parent());
          } else{
            error.insertAfter(element);
          }
        }
      }
    });
	}

})