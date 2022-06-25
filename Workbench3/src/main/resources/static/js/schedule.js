$(function(){
	"user strict";

  /**
	 * 担当者名取得
	 */
  window.main.createEmployeeOptionWithId($('#schedule-form select[name="employeeIdList"]'));

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
    // contentHeight: 'auto',
    nowIndicator: true,
    dayMaxEvents: true,
    // 日付をクリック、または範囲を選択したイベント
    selectable: true,
    select: function (info) {
      // 入力ダイアログ
      let start = info.start;
      const allDay = info.allDay;
      let $form = $('#schedule-form');
      $form.find('input[name="startTime"]').val(formatDate(start, false));
      $form.find('input[name="endTime"]').val(formatDate(start, true));
      if (allDay) {
        $form.find('input[name="isAllDay"]').prop('checked', true);
        $form.find('.time-body').hide();
      } else {
        $form.find('input[name="isAllDay"]').prop('checked', false);
        $form.find('.time-body').show();
      }
      $('#schedule-create-modal').modal('show');

      $(document).on('click', '#schedule-form input[name="isAllDay"]', function (e) {
        let isAllDay = $(this).prop('checked');
        if (isAllDay) {
          $('#schedule-form input[name="startTime"]').val(formatDate(start, false));
          $('#schedule-form input[name="endTime"]').val(formatDate(start, true));
        }
      })
    },
    events: function (info, successCallback, failureCallback){

      let url = $('#ref').data('initial-ref');

      $.ajax({
        type : "GET",
        url : url,
      }).done(function(response){
        successCallback(response);
      }).fail(function(){
        alert("fail...");
      })
    },
    eventClick: function (info) {
      let id = info.event.id;
      let start = info.event.start;
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
        $('#schedule-edit-form input[name="isAllDay"], #schedule-edit-form .all-day-label').on('click', function(e) {
          let isChecked = $('#schedule-edit-form input[name="isAllDay"]').prop('checked');

          if (isChecked) {
            $('#schedule-edit-form input[name="isAllDay"]').prop('checked', false);
            $('#schedule-edit-form input[name="startTime"]').val(formatDate(start, false));
            $('#schedule-edit-form input[name="endTime"]').val(formatDate(start, true));
            $('#schedule-edit-form .time-body').slideToggle();
          } else {
            $('#schedule-edit-form input[name="isAllDay"]').prop('checked', true);
            $('#schedule-edit-form .time-body').slideToggle();
          }
        })
      }).fail(function(){
        alert("fail...");
      })
    }
  });

  calendar.render();

  setAllDay();

  $('#edit-schedule-button').click(function(){

    let $form = $('#schedule-edit-modal').find('form');

    $form.submit();

	});

  function formatDate(dt, isEnd) {
    var y = dt.getFullYear();
    var m = ('00' + (dt.getMonth()+1)).slice(-2);
    var d = ('00' + dt.getDate()).slice(-2);
    var h = isEnd ? ('00' + (dt.getHours() + 1)).slice(-2) : ('00' + (dt.getHours())).slice(-2);
    var mm = ('00' + (dt.getMinutes())).slice(-2);
    return (y + '-' + m + '-' + d + ' ' + h + ':' + mm);
  }

  function setAllDay() {
    $(document).on('click', '#schedule-form input[name="isAllDay"]', function(e) {
      $('#schedule-form .time-body').slideToggle();
    })
  }

  $.extend($.validator.messages, {
    required: '*入力必須です',
    datetime: "*正しい日時形式で入力してください"
  });

	//追加ルールの定義
  let methods = {
    dateTimeFormat: function(value, element){
      return this.optional(element) || /^\d{4}-\d{2}-\d{2} \d{2}:\d{2}$/.test(value);
    }
  };

	//メソッドの追加
  $.each(methods, function(key) {
    $.validator.addMethod(key, this);
  });

  	//入力項目の検証ルール定義
  let rules = {
    title: {required: true},
    employeeIdList: {required: true},
    startTime: {required: true, dateTimeFormat: true},
    endTime: {required: true, dateTimeFormat: true},
  };

  //入力項目ごとのエラーメッセージ定義
  let messages = {
    projetitlectName: {
      required: "*タイトルを入力してください"
    },
    employeeIdList: {
      required: "*担当者を入力してください",
    },
    startTime: {
      required: "*開始時刻を入力してください",
      dateTimeFormat: "*正しい日時形式で入力してください"
    },
    endTime: {
      required: "*終了時刻を入力してください",
      dateTimeFormat: "*正しい日時形式で入力してください"
    },
  };

validate($("#schedule-form"));

validate($("#schedule-edit-form"));

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
        } else {
        error.insertAfter(element);
        }
      }
    }
  });
}

})