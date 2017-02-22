	(function () {
		var previousVal = "";
		$('#fromDate').on('focus', function (){
			previousVal = $('#fromDate').val();
		}
		).on('change', function(){
			var fromDate = $('#fromDate').getAsDate();
			var toDate = $('#toDate').getAsDate();
			if(fromDate.valueOf() > toDate.valueOf()) {
				$('.dateerror').fadeIn(1000).fadeOut(10000);
				$('#fromDate').val(previousVal);
				//$('#toDate').focus();
			}
		});
	}());
	
	(function () {
		var previousVal = "";
	
		$('#toDate').on('focus', function (){
			previousVal = $('#toDate').val();
		}
		).on('change', function(){
			var fromDate = $('#fromDate').getAsDate();
			var toDate = $('#toDate').getAsDate();
			//console.info(previousVal);
			if (fromDate.valueOf() > toDate.valueOf()){
				$('.dateerror').fadeIn(1000).fadeOut(10000);
				$('#toDate').val(previousVal);
				//$('#fromDate').focus();
			}
		});
	})();

	
	/* Update datepicker plugin so that MM/DD/YYYY format is used. */
    $.extend($.fn.datepicker.defaults, {
      parse: function (string) {
        var matches;
        if ((matches = string.match(/^(\d{2,2})\/(\d{2,2})\/(\d{4,4})$/))) {
          return new Date(matches[3], matches[1] - 1, matches[2]);
        } else {
          return null;
        }
      },
      format: function (date) {
        var
          month = (date.getMonth() + 1).toString(),
          dom = date.getDate().toString();
        if (month.length === 1) {
          month = "0" + month;
        }
        if (dom.length === 1) {
          dom = "0" + dom;
        }
        return month + "/" + dom + "/" + date.getFullYear();
      }
    }); 
