$(function(){
	"user strict";

  $(document).on('click', 'img.timeline-image', function() {
		let element = $(this).clone();
		$('#timeline-image-zoom-modal').find('#zoom').html($(element));
		$('#timeline-image-zoom-modal').modal('show');
	})

  $(document).on("click", "a#layout-timeline", function(e) {
    e.preventDefault();

    let href = $(this).attr("href");
    let url = $(this).data("url");

    $.ajax({
      type : "GET",
      url : url,
    }).done(function(){
      location.href = href;
		}).fail(function(){
			alert("更新に失敗しました");
		})
  });

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

	$(document).on('click', '.timeline-del-button', function(){

		let id = $(this).data('id');
		let url = $(this).data('href');

		let _csrf = $('meta[name="_csrf"]').attr('content');

		let confirm = window.confirm('投稿を削除します。よろしいですか？');

		if(confirm){

			$('<form/>', {action: url, method: 'POST'})
        .append($('<input/>', {type: 'hidden', name: 'id', value: id}))
        .append($('<input/>', {type: 'hidden', name: '_csrf', value: _csrf}))
        .appendTo(document.body)
        .submit();
		}
	});

  $('#create-timeline-form input[name="image"]').on('change', function () {
    let file = $(this).prop('files')[0];
    $('#create-timeline-form span').text(file.name);
  });

  $('#comment-timeline-form input[name="image"]').on('change', function () {
    let file = $(this).prop('files')[0];
    $('#comment-timeline-form span').text(file.name);
  });

  $(document).on('keyup', '#create-timeline-form textarea.timeline', function () {
    checkValue();
  });

  $(document).on('keyup', '#comment-timeline-form textarea.timeline', function () {
    checkValueInComment();
  });

  $(document).on('change', '#create-timeline-form input.timeline-input', function() {
    checkValue();
  });

  $(document).on('change', '#comment-timeline-form input.timeline-input', function() {
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

    if (res.length > 400) {
      $('#create-timeline-form p.err-msg').removeClass('d-none');
    } else {
      $('#create-timeline-form p.err-msg').addClass('d-none');
    }

    if (res != null && res != '' && res.length <= 400 || hasFile) {
      $('button#create-post').prop('disabled', false);
    } else {
      $('button#create-post').prop('disabled', true);
    }
  }

  function checkValueInComment() {

    let value = $('#comment-timeline-form textarea.timeline').val();
    let res = $.trim(value);

    let hasFile = $('#comment-timeline-form input.timeline-input').val().length;

    if (res.length > 400) {
      $('#comment-timeline-form p.comment-err-msg').removeClass('d-none');
    } else {
      $('#comment-timeline-form p.commen-terr-msg').addClass('d-none');
    }

    if (res != null && res != '' && res.length <= 400 || hasFile) {
      $('button#comment-post').prop('disabled', false);
    } else {
      $('button#comment-post').prop('disabled', true);
    }
  }

  $(document).on('hide.bs.modal', '#create-timeline-modal', function() {
    $('#create-timeline-form')[0].reset();
    $('#create-timeline-form span.timeline-span').text('選択されていません');
    $('button#create-post').prop('disabled', true);
  });

  $(document).on('hide.bs.modal', '#comment-timeline-modal', function() {
    $('#comment-timeline-form')[0].reset();
    $('#comment-timeline-form span.timeline-span').text('選択されていません');
    $('button#comment-post').prop('disabled', true);
  });

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
    let href = $(this).data('href');
    let $form = $('#comment-timeline-form');
    let formData = new FormData($('#comment-timeline-form').get(0));
    $.ajax({
      type : "POST",
      url : $form.attr('action'),
      data: formData,
      contentType: false,
      processData: false
    }).done(function(){
      getCommentFragment($form.find('input[name="timelineId"]').val(), href);
      $('#comment-timeline-modal').modal('hide');
		}).fail(function(){
			alert("コメントに失敗しました");
		})
  });

  function getCommentFragment(id, href) {

    $.ajax({
      type : "GET",
      url : href,
      data: {id : id},
      dataType : "html"
    }).done(function(data){
      $('.target-comment-' + id).remove();
      $('#target-timeline-' + id).after(data);
		}).fail(function(){
			alert("読み込みに失敗しました");
		})
  }

})