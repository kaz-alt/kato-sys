$(function(){
	"user strict";

  $('#register-button').on('click', function() {
    $('#employee-form').submit();
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
        required: "*氏名を入力してください"
    },
    department: {
        required: "*担当部署を入力してください"
    }
  };

	validate($("#employee-form"));

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