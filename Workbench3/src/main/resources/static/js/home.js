$(function(){
	"user strict";

  $(document).on('click', 'span.todo-span', function() {
    let target = $(this).prev('div').find('input');
    target.trigger('click');
  });

  $(document).on('change', 'input.todo-input', function() {

    let isDone = $(this).prop('checked');
    let id = $(this).data('id');
    let url = $(this).data('url');
    let _csrf = $('meta[name="_csrf"]').attr('content');

    $.ajax({
      type : "POST",
      url : url,
      data: {id : id, isDone : isDone, _csrf : _csrf},
    }).done(function(){
      getFragment();
		}).fail(function(){
			alert("ToDoの更新に失敗しました");
		})
  });

	$(document).on('click', 'button.todo-del-button', function(){

		let id = $(this).data('id');
		let url = $(this).data('href');

		let _csrf = $('meta[name="_csrf"]').attr('content');

		let confirm = window.confirm('ToDoを削除します。よろしいですか？');

		if(confirm){

			$('<form/>', {action: url, method: 'POST'})
        .append($('<input/>', {type: 'hidden', name: 'id', value: id}))
        .append($('<input/>', {type: 'hidden', name: '_csrf', value: _csrf}))
        .appendTo(document.body)
        .submit();
		}
	});

  $(document).on('keyup', '#create-todo-form textarea.todo', function () {
    checkValue();
  });

  $(document).on('change', '#create-todo-form input[name="deadline"]', function () {
    checkValue();
  });

  function checkValue() {

    let value = $('#create-todo-form textarea.todo').val();
    let res = $.trim(value);

    if (res.length > 100) {
      $('#create-todo-form p.err-msg').removeClass('d-none');
    } else {
      $('#create-todo-form p.err-msg').addClass('d-none');
    }

    let deadline = $('#create-todo-form input[name="deadline"]').val();

    if (res != null && res != '' && res.length <= 100 && deadline != null && deadline != '') {
      $('button#create-todo').prop('disabled', false);
    } else {
      $('button#create-todo').prop('disabled', true);
    }
  }

  $(document).on('hide.bs.modal', '#create-todo-modal', function() {
    $('#create-todo-form')[0].reset();
    $('button#create-todo').prop('disabled', true);
  });

  $('button#create-todo').on('click', function() {
    let $form = $('#create-todo-form');
    $.ajax({
      type : "POST",
      url : $form.attr('action'),
      data: $form.serialize(),
    }).done(function(){
      $('#create-todo-modal').modal('hide');
      getFragment();
		}).fail(function(){
			alert("ToDoの追加に失敗しました");
		})
  });

  function getFragment() {

    let url = $('div#todo-body').data('url');

    // fragment(html)取得
    $.ajax({
      type : "GET",
      url : url,
      dataType : "html"
    }).done(function(data){
      $('#todo-body').html(data);
      $('input.todo-input').each(function() {
        if ($(this).prop('checked')) {
          $(this).parent().parent().addClass('done');
        } else {
          $(this).parent().parent().removeClass('done');
        }
      });
    }).fail(function(){
      alert("データ取得に失敗しました");
    })
  }

})