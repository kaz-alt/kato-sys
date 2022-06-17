$(function(){
	"user strict";

	const calendarEl = document.querySelector('#calendar');
  const calendar = new FullCalendar.Calendar(calendarEl, {
    headerToolbar: {
        left: 'prev,next today',
        center: 'title',
        right: 'dayGridMonth,timeGridWeek,timeGridDay,listMonth'
    },
    navLinks: true,
    businessHours: true,
    editable: true,
    locale: 'ja',
    // 日付をクリック、または範囲を選択したイベント
    selectable: true,
    select: function (info) {
      // 入力ダイアログ
      const eventName = prompt("イベントを入力してください");
      if (eventName) {

        let url = $('#ref').data('ref');
        let $form = $('<form/>')
          .append($('<input/>', {type: 'hidden', name: 'startTime', value: '2022-06-17 23:20'}))
          .append($('<input/>', {type: 'hidden', name: 'endTime', value: '2022-06-17 23:50'}))
          .append($('<input/>', {type: 'hidden', name: 'title', value: eventName}))
          .append($('<input/>', {type: 'hidden', name: '_csrf', value: $('meta[name="_csrf"]').attr('content')}))
          .appendTo(document.body);

        $.ajax({
          type : "POST",
          url : url,
          data: $form.serialize(),
        }).done(function(){
          alert('success!');
          location.reload();
        }).fail(function(){
          alert("fail...");
        })
      }
    },
  });

  calendar.render();

})