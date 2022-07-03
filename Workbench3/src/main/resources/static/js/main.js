$(function(){

  $('.dataTable').dataTable( {
        paging: false,
        pagingType: "simple_numbers",
        searching: false,
        ordering: false,
        info: false,
        language: getLanguageForDataTable()
  } );

  $('[data-toggle="tooltip"]').tooltip();

// スクロールのドラッグ有効化
jQuery.prototype.mousedragscrollable = function () {
  let target;
  $(this).each(function (i, e) {
    $(e).mousedown(function (event) {
      event.preventDefault();
      target = $(e);
      $(e).data({
        down: true,
        move: false,
        x: event.clientX,
        y: event.clientY,
        scrollleft: $(e).scrollLeft(),
        scrolltop: $(e).scrollTop(),
      });
      return false;
    });
    $(e).click(function (event) {
      if ($(e).data("move")) {
        return false;
      }
    });
  });
  $(document)
    .mousemove(function (event) {
      if ($(target).data("down")) {
        event.preventDefault();
        let move_x = $(target).data("x") - event.clientX;
        let move_y = $(target).data("y") - event.clientY;
        if (move_x !== 0 || move_y !== 0) {
          $(target).data("move", true);
        } else {
          return;
        }
        $(target).scrollLeft($(target).data("scrollleft") + move_x);
        $(target).scrollTop($(target).data("scrolltop") + move_y);
        return false;
      }
    })
    .mouseup(function (event) {
      $(target).data("down", false);
      return false;
    });
};
$(".table-scroll").mousedragscrollable();

  $('.select2').select2({
	placeholder: "※ 入力または選択（複数）が可能です。",
    allowClear: false,
    language: "ja"
  });

  function createClientOption($select) {
	let term = '';
	$select.select2({
		minimumInputLength: 1,
		language: "ja",
		placeholder: "例）〇〇株式会社",
		ajax: {
      url: $select.data('href'),
      data: function(params) {
      let data = {};
        data.name = params.term;
        term = params.term;
        return data;
        },
        processResults: function(data) {
    let path = location.pathname;
        if (data.length) {
      let resultList = [];
          for (let i in data) {
          let map = {};
          map.id = data[i];
              map.text = data[i];
              resultList.push(map);
          }
          return {
              results: resultList
            };
			} else {
				if (path.indexOf('/business_card') >= 0) {
					return {
                      results: [{ id: term, text: term}]
                    };
				} else {
					return {results : [{}]}
				}
			}
          }
        }
	  })
  }
  window.main= window.main|| {};
  window.main.createClientOption = createClientOption;

  function createClientOptionWithId($select) {
	$select.select2({
		minimumInputLength: 1,
		language: "ja",
		placeholder: "例）〇〇株式会社",
		ajax: {
          url: $select.data('href'),
          data: function(params) {
	        let data = {};
            data.name = params.term;
            term = params.term;
           return data;
           },
          processResults: function(data) {
	        let resultList = [];
	        for (let i in data) {
		      let map = {};
		      map.id = data[i].id;
              map.text = data[i].name;
              resultList.push(map);
	        }
	        return {
              results: resultList
            };
          }
        }
	  })
  }
  window.main= window.main|| {};
  window.main.createClientOptionWithId = createClientOptionWithId;

  function createEmployeeOptionWithId($select) {
	$select.select2({
		minimumInputLength: 1,
		language: "ja",
		placeholder: "例）山田太郎",
		ajax: {
          url: $select.data('href'),
          data: function(params) {
	        let data = {};
            data.name = params.term;
            term = params.term;
           return data;
           },
          processResults: function(data) {
	        let resultList = [];
	        for (let i in data) {
		      let map = {};
		      map.id = data[i].id;
              map.text = data[i].name;
              resultList.push(map);
	        }
	        return {
              results: resultList
            };
          }
        }
	  })
  }
  window.main= window.main|| {};
  window.main.createEmployeeOptionWithId = createEmployeeOptionWithId;

  function createEmployeeOptionByClientId($selectClientId, $select) {
	$select.empty();
	let value = $selectClientId.val();
	let url = $select.data('url');
	$.ajax({
	 	type : "GET",
　　　　	url : url,
        data : {clientId : value}
　　	}).done(function(data){
	  for (let i in data) {
		$select
		  .append($("<option></option>")
		  .attr("value", data[i].id)
		  .text(data[i].name));
	  }
	}).fail(function(){
		alert("データ取得に失敗しました");
	})
  }
  window.main= window.main|| {};
  window.main.createEmployeeOptionByClientId = createEmployeeOptionByClientId;

  function createProjectOptionByName($select) {
    $select.select2({
      minimumInputLength: 1,
      language: "ja",
      placeholder: "例）◯◯案件",
      ajax: {
        url: $select.data('href'),
        data: function(params) {
          let data = {};
          data.name = params.term;
          term = params.term;
          return data;
        },
        processResults: function(data) {
          let resultList = [];
          for (let i in data) {
            let map = {};
            map.id = data[i].id;
            map.text = data[i].name;
            resultList.push(map);
          }
          return {
            results: resultList
          };
        }
      }
    })
  }
  window.main= window.main|| {};
  window.main.createProjectOptionByName = createProjectOptionByName;

  $('.datepicker').datepicker(datepicker()).datepicker('setDate', new Date());
  $('.datepicker-search').datepicker(datepicker());
  $('.datetimepicker').datetimepicker({
    format: 'Y-m-d H:i',
    minDate: 0,
    step:15,
  });

  function setPagenationForSearch($form, path) {
	if(path.indexOf('/search') > 0){
		$('.page-item').not('.disabled, .active').click(function(e){
			e.preventDefault();
			$form.find('#pageNumber').val($(this).data('value'));
			$form.submit();
		})
	}
  }
  window.main= window.main|| {};
  window.main.setPagenationForSearch = setPagenationForSearch;

  function sortTableColumn(tableName, formName) {
    $(document).on('click', tableName + ' th.sort-pointer', function(){
		let url = $(formName).attr('action');
		let sortData = $(this).data('sort-data');
		let sortOrder = $(this).data('sort-order');

		let activeSortData = $(tableName).find('th.active').data('sort-data');

		if(sortData != activeSortData){
		  sortOrder = "asc";
		} else {
			if (sortOrder == 'asc') {
				sortOrder = "desc";
			} else {
				sortOrder = "asc";
			}
		}

		let parameter = "";
		let param = location.search;
		param = param.replace('?', '');
		param = param.split('&');
		for (let i in param){
			let splitedParam = param[i].split('=');
			let name = splitedParam[0];
			let value = splitedParam[1] == null ? null : splitedParam[1];
			value = name == 'sortData' ? sortData : name == 'sortOrder' ? sortOrder : value;
			if (name == '' || name == null) continue;
			if (i == 0) {
				parameter += '?' + name + '=' + value;
			} else {
				parameter += '&' + name + '=' + value;
			}
		}
		if (parameter.indexOf('sortData') < 0 && parameter.indexOf('sortOrder') < 0) {
			parameter += parameter.indexOf('?') >= 0 ? '&sortData=' + sortData + '&sortOrder=' + sortOrder :
			  '?sortData=' + sortData + '&sortOrder=' + sortOrder;
		}
		location.href = url + parameter;
	})
  }
  window.main= window.main|| {};
  window.main.sortTableColumn = sortTableColumn;

  function resetForm(form) {
    $(form).find('.reset-search-form-btn').on('click', function() {
      $(form)
        .find("textarea, :text, select").val("").end()
        .find(":not(.staff-radio):checked").prop("checked", false).end()
        .find("select").val(null).trigger("change").end()
        .find('input[type="number"]').val(null).trigger("change").end()
        .find('input[type="email"]').val("").trigger("change");
    });
  }
  window.main= window.main|| {};
  window.main.resetForm = resetForm;

  function getLanguageForDataTable(){
	return {
		 "decimal": ".",
		 "thousands": ",",
		 "sProcessing": "処理中...",
		 "sLengthMenu": "_MENU_ 件表示",
		 "sZeroRecords": "データはありません。",
		 "sInfo": " _TOTAL_ 件中 _START_ から _END_ まで表示",
		 "sInfoEmpty": " 0 件中 0 から 0 まで表示",
		 "sInfoFiltered": "（全 _MAX_ 件より抽出）",
		 "sInfoPostFix": "",
		 "sSearch": "キーワード絞込み:",
		 "sUrl": "",
		 "oPaginate": {
		 "sFirst": "先頭",
		 "sPrevious": "前",
		 "sNext": "次",
		 "sLast": "最終"
		 }
	}
  }
  window.main= window.main|| {};
  window.main.getLanguageForDataTable = getLanguageForDataTable;

  function datepicker(){
	return {
		dateFormat: "yy-mm-dd",
    	closeText: "閉じる",
    	prevText: "&#x3C;前",
    	nextText: "次&#x3E;",
    	currentText: "今月",
    	monthNames: [
	      "1月",
	      "2月",
	      "3月",
	      "4月",
	      "5月",
	      "6月",
	      "7月",
	      "8月",
	      "9月",
	      "10月",
	      "11月",
	      "12月"
	    ],
	    monthNamesShort: [
	      "1月",
	      "2月",
	      "3月",
	      "4月",
	      "5月",
	      "6月",
	      "7月",
	      "8月",
	      "9月",
	      "10月",
	      "11月",
	      "12月"
	    ],
	    dayNames: [
	      "日曜日",
	      "月曜日",
	      "火曜日",
	      "水曜日",
	      "木曜日",
	      "金曜日",
	      "土曜日"
	    ],
	    dayNamesShort: ["日", "月", "火", "水", "木", "金", "土"],
	    dayNamesMin: ["日", "月", "火", "水", "木", "金", "土"],
	    weekHeader: "週",
	    isRTL: false,
	    showMonthAfterYear: true,
	    yearSuffix: "年",
	    firstDay: 1, // 週の初めは月曜
	    showButtonPanel: true // "今日"ボタン, "閉じる"ボタンを表示する
	}
  }
  window.main= window.main|| {};
  window.main.datepicker = datepicker;

  $('#create-button').click(function() {

	$(this).parent().prev().find('form').submit();

  })
})