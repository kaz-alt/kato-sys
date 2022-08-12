$(function(){
	"user strict";

  $(document).on("click", "input#entire-schedule-check", function() {

    let url = $(this).data('url');
    let isAll = $(this).prop('checked');

    // fragment(html)取得
    $.ajax({
      type : "GET",
      url : url,
      data : {isAll : isAll},
      dataType : "html"
    }).done(function(data){
      $('#home-schedule-body').html(data);
    }).fail(function(){
      alert("データ取得に失敗しました");
    })
  });

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

	$(document).on('click', 'span.todo-del-span', function(){

    let id = $(this).data('id');
    let url = $(this).data('href');

    let _csrf = $('meta[name="_csrf"]').attr('content');

    let confirm = window.confirm('ToDoを削除します。よろしいですか？');

    if(confirm){

      let $form = $('<form/>', {action: url, method: 'POST'})
                    .append($('<input/>', {type: 'hidden', name: 'id', value: id}))
                    .append($('<input/>', {type: 'hidden', name: '_csrf', value: _csrf}))
                    .appendTo(document.body);

      $.ajax({
        type : "POST",
        url : $form.attr('action'),
        data: $form.serialize(),
      }).done(function(){
        getFragment();
      }).fail(function(){
        alert("ToDoの削除に失敗しました");
      })
    }
	});

  $(document).on('keyup', '#create-todo-form textarea.todo', function () {
    checkValue();
  });

  $(document).on('change', '#create-todo-form input[name="deadline"]', function () {
    checkValue();
  });

  $(document).on('keyup', '#create-contact-form textarea.contact', function () {
    checkContactValue();
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

  function checkContactValue() {

    let value = $('#create-contact-form textarea.contact').val();
    let res = $.trim(value);

    if (res.length > 100) {
      $('#create-contact-form p.err-msg').removeClass('d-none');
    } else {
      $('#create-contact-form p.err-msg').addClass('d-none');
    }

    if (res != null && res != '' && res.length <= 100) {
      $('button#create-contact').prop('disabled', false);
    } else {
      $('button#create-contact').prop('disabled', true);
    }
  }

  $(document).on('hide.bs.modal', '#create-todo-modal', function() {
    $('#create-todo-form')[0].reset();
    $('button#create-todo').prop('disabled', true);
  });

  $(document).on('hide.bs.modal', '#create-contact-modal', function() {
    $('#create-contact-form')[0].reset();
    $('button#create-contact').prop('disabled', true);
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

  $('button#create-contact').on('click', function() {
    let $form = $('#create-contact-form');
    $.ajax({
      type : "POST",
      url : $form.attr('action'),
      data: $form.serialize(),
    }).done(function(){
      $('#create-contact-modal').modal('hide');
      alert("頂いた内容で報告致しました。");
      getFragment();
		}).fail(function(){
			alert("報告に失敗しました");
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