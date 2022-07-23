$(function(){
	"user strict";

  $(document).on('click', 'span.todo-span', function() {
    let target = $(this).prev('div').find('input');
    target.trigger('click');
  });

  $(document).on('change', '', function() {
    let isChecked = $(this).prop('checked');

    $.ajax({
      type : "POST",
      url : $form.attr('action'),
      data: $form.serialize(),
    }).done(function(){
      location.reload();
		}).fail(function(){
			alert("ToDoの追加に失敗しました");
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
      location.reload();
		}).fail(function(){
			alert("ToDoの追加に失敗しました");
		})
  });

})