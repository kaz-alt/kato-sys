$(function(){
	"user strict";

  /**
	 * 担当者名取得
	 */
  window.main.createEmployeeOptionWithId($('#schedule-form select[name="employeeIdList"]'));

  function formatDate(dt, isEnd) {
    var y = dt.getFullYear();
    var m = ('00' + (dt.getMonth()+1)).slice(-2);
    var d = ('00' + dt.getDate()).slice(-2);
    var h = isEnd ? ('00' + (dt.getHours() + 1)).slice(-2) : ('00' + (dt.getHours())).slice(-2);
    var mm = ('00' + (dt.getMinutes())).slice(-2);
    return (y + '-' + m + '-' + d + ' ' + h + ':' + mm);
  }

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
      const start = info.start;
      let $form = $('#schedule-form');
      $form.find('input[name="startTime"]').val(formatDate(start, false));
      $form.find('input[name="endTime"]').val(formatDate(start, true));
      $('#schedule-create-modal').modal('show');

      const eventName = $form.find('input[name="title"]').val();

      if (eventName) {

        let url = $('#ref').data('ref');

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
    events: function (info, successCallback, failureCallback){

      let url = $('#ref').data('initial-ref');

      $.ajax({
        type : "GET",
        url : url,
      }).done(function(response){
        calendar.removeAllEvents();
        successCallback(response);
      }).fail(function(){
        alert("fail...");
      })
    },
    eventClick: function (info) {
      let id = info.event.id;
      let url = $('#ref').data('detail-ref');

      $.ajax({
        type : "GET",
        url : url,
        data: {id: id},
        dataType : "html"
      }).done(function(data){
        $('#schedule-edit-form').html(data);
        $('#schedule-edit-form .select2').select2({
          placeholder: "※ 入力または選択（複数）が可能です。",
          allowClear: false,
          language: "ja"
        });
        window.main.createEmployeeOptionWithId($('#schedule-edit-form select[name="employeeIdList"]'));
        $('.datetimepicker').datetimepicker({
          format: 'Y-m-d H:i',
          minDate: 0,
          step:15,
        });
        $('#schedule-edit-modal').modal('show');
      }).fail(function(){
        alert("fail...");
      })
    }
  });

  calendar.render();

})