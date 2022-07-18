$(function(){
	"user strict";

  $('#load-timeline').on("click", function(e) {
    e.preventDefault();

    let pageNumber = $(this).attr('data-page-number');
    let href = $(this).data('href');
    let date = $(this).attr('data-oldest-date');

    $('#load-timeline').attr('data-page-number', Number(pageNumber) + 1).change();

    let totalPages = $('#load-timeline').attr('data-total-pages');

    if (Number(pageNumber) >= Number(totalPages)) {
      $('#load-timeline').addClass('d-none');
    }

    $.ajax({
      type : "GET",
      url : href,
      data: {pageNumber : pageNumber, date : date},
      dataType : "html"
    }).done(function(data){
      $('#timeline-content').append(data);
		}).fail(function(){
			alert("読み込みに失敗しました");
		})
  });

	$('.timeline-del-button').click(function(){

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
	});

  $(document).on('keyup', '#create-timeline-form textarea.timeline', function (e) {
    checkValue();
  });

  $(document).on('keyup', '#comment-timeline-form textarea.timeline', function () {
    checkValueInComment();
  });

  $(document).on('change', '#create-timeline-form input.timeline-input', function(e) {
    checkValue();
  });

  $(document).on('change', '#create-timeline-form input.timeline-input', function(e) {
    checkValueInComment();
  });

  $(document).on('click', '.tm-comment-btn', function() {

    let timelineId = $(this).data('timeline-id');
    $('#comment-timeline-form').find('input[name="timelineId"]').val(timelineId);

  });


  function checkValue() {

    let value = $('#create-timeline-form textarea.timeline').val();
    let res = $.trim(value);

    let hasFile = $('#create-timeline-form input.timeline-input').val().length;

    if (res != null && res != '' || hasFile) {
      $('button#create-post').prop('disabled', false);
    } else {
      $('button#create-post').prop('disabled', true);
    }
  }

  function checkValueInComment() {

    let value = $('#comment-timeline-form textarea.timeline').val();
    let res = $.trim(value);

    let hasFile = $('#comment-timeline-form input.timeline-input').val().length;

    if (res != null && res != '' || hasFile) {
      $('button#comment-post').prop('disabled', false);
    } else {
      $('button#comment-post').prop('disabled', true);
    }
  }

  $('button#create-post').on('click', function() {
    let $form = $('#create-timeline-form');
    let formData = new FormData($('#create-timeline-form').get(0));
    $.ajax({
      type : "POST",
      url : $form.attr('action'),
      data: formData,
      contentType: false,
      processData: false
    }).done(function(){
      location.reload();
		}).fail(function(){
			alert("投稿に失敗しました");
		})
  });

  $('button#comment-post').on('click', function() {
    let $form = $('#comment-timeline-form');
    let formData = new FormData($('#comment-timeline-form').get(0));
    $.ajax({
      type : "POST",
      url : $form.attr('action'),
      data: formData,
      contentType: false,
      processData: false
    }).done(function(){
      location.reload();
		}).fail(function(){
			alert("コメントに失敗しました");
		})
  });

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