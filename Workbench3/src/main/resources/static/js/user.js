$(function(){
	"user strict";

  $('#register-button').on('click', function() {
    $('#employee-form').submit();
  });

  $('#change-password').on('click', function() {
    let $form = $('#change-password-form');
    $.ajax({
      type : $form.attr('method'),
      url : $form.attr('action'),
      data: $form.serialize()
    }).done(function(){
      alert('パスワードを変更しました。');
      location.reload();
    }).fail(function(){
      alert("パスワードの変更に失敗しました");
    })
  })

	$.extend($.validator.messages, {
    required: '*入力必須です',
		email: '*正しいメールアドレスの形式で入力して下さい',
		tel: "*正しい電話番号の形式で入力してください",
    katakana: "*全角カタカナで入力してください",
    minlength: "*5文字以上で入力してください",
    maxlength: "*10文字以内で入力してください",
    alphabet: "*半角英数字記号で入力してください",
    equalTo: "*パスワードが異なります"
  });

	//追加ルールの定義
  let methods = {
    tel: function(value, element){
      return this.optional(element) || /^\d{11}$|^\d{2}-\d{4}-\d{4}$|^\d{3}-\d{4}-\d{4}$/.test(value);
    },
    katakana: function(value, element){
      return this.optional(element) || /^([ァ-ヶー]+)$/.test(value);
    },
    alphabet: function(value, element){
      return this.optional(element) || /^[a-zA-Z0-9!-/:-@¥[-`{-~]*$/.test(value);
    }
  };

	//メソッドの追加
  $.each(methods, function(key) {
    $.validator.addMethod(key, this);
  });

  //入力項目の検証ルール定義
  let rules = {
    "lastName": {required: true},
    "firstName": {required: true},
    "lastNameKana": {required: true, katakana: true},
    "firstNameKana": {required: true, katakana: true},
    "department": {required: true},
    "tel": {required: true, tel: true},
    "email": {required: true, email: true},
    "userForm.password": {required: true, alphabet: true, minlength: 5, maxlength: 10},
    "userForm.confirmPassword": {required: true, alphabet: true, minlength: 5, maxlength: 10, equalTo: '[name="userForm.password"]'},
    "password": {required: true, alphabet: true, minlength: 5, maxlength: 10},
    "confirmPassword": {required: true, alphabet: true, minlength: 5, maxlength: 10, equalTo: '[name="password"]'}
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
  validate($("#change-password-form"));

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