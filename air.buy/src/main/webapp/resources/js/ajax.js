var stat = "0";
var month1 = [];
var month2 = [];
var date1 = ["01","02","03","04","05","06","07","08","09","10","11","12","13","14","15"];
var date1_1 =["16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"];
var date2 =["01","02","03","04","05","06","07","08","09","10","11","12","13","14","15"];
var date2_1 =["16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"];
var chartData1 = [];
var chartData2 = [];
var chartData1_1 = [];
var chartData2_1 = [];
var namefrom="" ;
var nameto ="";
var structureD ="";
var currency ="";
var outboundD ="";
var returnD="";
function search(outboundDate, returnDate, from, to, structure, name1, name2) {
	
	namefrom=name1;
	nameto=name2;
	structureD=structure;
	outboundD = outboundDate;
	returnD =  returnDate;

	$("#fromAndTo").text(name1 + "->" + name2);
	$('#money').show();
	$('#name').html(name1 + "->" + name2);
	if (structureD == "RoundTrip") {
		$('#name1').html(name2 + "->" + name1);
	}

	createCal(from, to);
	ajax(outboundD, returnD, from, to, structure, name1, name2,0);
	

}

function createCal(from, to){
	$('#calendar').fullCalendar( 'destroy' );
	$('#calendar1').fullCalendar( 'destroy' );
	$('#calendar').fullCalendar(
			{
				header : {
					left : 'title',
					right : 'prev,next',

				},

				defaultDate : outboundD,
				navLinks : false, // can click day/week names to navigate
				// views
				editable : false,
				eventLimit : true, // allow "more" link when too many events
				
				dayRender : function(date, cell) {
					var szformatDate = $.datepicker.formatDate('yy/mm/dd',
							date.toDate());
					
					if (stat == "0") {
					

						if (szformatDate == outboundD) {
							cell.css("background-color", "#FFAA33");
						}
						
					}
				},
				
				viewRender : function(view) {
					if (stat == "1") {
						
						if (checkDate(view.intervalStart.format(), returnD)) {
							outboundD = view.intervalStart.format();
							ajax(view.intervalStart.format(), returnD, from, to,
									structureD,namefrom.substring(namefrom.length - 4,
											namefrom.length - 1), nameto.substring(nameto.length - 4,
													nameto.length - 1),0);
							$('#calendar').fullCalendar('removeEvents');
							$('#calendar').fullCalendar('addEventSource', month1);
							
						} 

					}
					
					var dtToday = new Date();
					var viewdate = new Date(view.start);

					// PREV - force minimum display month to current month
					if (new Date(viewdate.getFullYear(),
							viewdate.getMonth() + 1, 1).getTime() <= new Date(
							dtToday.getFullYear(), dtToday.getMonth(), 1)
							.getTime()) {
						$('#calendar .fc-prev-button').prop('disabled', true);
						$('#calendar .fc-prev-button').css('opacity', 0.5);
					} else {
						$('#calendar .fc-prev-button').prop('disabled', false);
						$('#calendar .fc-prev-button').css('opacity', 1);
					}

					// NEXT - force max display month to a year from current
					// month
				/*	if (new Date(viewdate.getFullYear(),
							viewdate.getMonth() + 1).getTime() >= new Date(
							dtToday.getFullYear() + 1, dtToday.getMonth() + 1)
							.getTime()) {
						$('#calendar .fc-next-button').prop('disabled', true);
						$('#calendar .fc-next-button').css('opacity', 0.5);
					} else {
						$('#calendar .fc-next-button').prop('disabled', false);
						$('#calendar .fc-next-button').css('opacity', 1);
					}
				*/

				}, eventClick: function(calEvent, jsEvent, view) {

			      

			        // change the border color just for fun
			      //  $(this).css('border-color', 'red');
			        $("#airImg").html('<img src="resources/img/'+calEvent.title.split(" ")[0]+'.png" border="0" height="50" width="150" />');
			        $("#airDept").val($.datepicker.formatDate('yy/mm/dd',new Date(calEvent.start)));
			        $("#airNameHidden").val(calEvent.title.split(" ")[0]);

			    }
			});
	
	if (structureD == "RoundTrip") {
		
		$('#calendar1').fullCalendar(
				{
					header : {
						left : 'title',
						right : 'prev,next',

					},

					defaultDate : returnD,
					navLinks : false, // can click day/week names to navigate
					// views
					editable : false,
					eventLimit : true, // allow "more" link when too many

					viewRender : function(view) {
						
						if (stat == "1") {
							
							
							if (checkDate(outboundD, view.intervalStart.format())) {
								returnD = view.intervalStart.format();
								ajax(outboundD, view.intervalStart.format(),
									from, to, structureD, namefrom.substring(namefrom.length - 4,
											namefrom.length - 1), nameto.substring(nameto.length - 4,
													nameto.length - 1),0);
								$('#calendar1').fullCalendar('removeEvents');
								$('#calendar1').fullCalendar('addEventSource', month2);
							}
						}
				
						var dtToday = new Date();
						var viewdate = new Date(view.start);
					
						// PREV - force minimum display month to current month
						if (new Date(viewdate.getFullYear(), viewdate
								.getMonth() + 1, 1).getTime() <= new Date(
								dtToday.getFullYear(), dtToday.getMonth(), 1)
								.getTime()) {
							$('#calendar1 .fc-prev-button').prop('disabled', true);
							$('#calendar1 .fc-prev-button').css('opacity', 0.5);
						} else {
							$('#calendar1 .fc-prev-button').prop('disabled', false);
							$('#calendar1 .fc-prev-button').css('opacity', 1);
						}

						// NEXT - force max display month to a year from current
						// month
						if (new Date(viewdate.getFullYear(), viewdate
								.getMonth() + 1).getTime() >= new Date(dtToday
								.getFullYear() + 1, dtToday.getMonth() + 1)
								.getTime()) {
							$('#calendar1 .fc-next-button').prop('disabled', true);
							$('#calendar1 .fc-next-button').css('opacity', 0.5);
						} else {
							$('#calendar1 .fc-next-button').prop('disabled', false);
							$('#calendar1 .fc-next-button').css('opacity', 1);
						}

					},	
					dayRender : function(date, cell) {
					
						var szformatDate = $.datepicker.formatDate(
								'yy/mm/dd', date.toDate());
				
						if (stat == "0") {
							if (szformatDate == returnD) {
								cell.css("background-color", "#FFAA33");
							}
						}
					} // events
					, eventClick: function(calEvent, jsEvent, view) {

					      

				        // change the border color just for fun
				      //  $(this).css('border-color', 'red');
				        $("#air1Img").html('<img src="resources/img/'+calEvent.title.split(" ")[0]+'.png" border="0" height="50" width="150" />');
				        $("#airRet").val($.datepicker.formatDate('yy/mm/dd',new Date(calEvent.start)));
				        $("#airName1Hidden").val(calEvent.title.split(" ")[0]);
				      
				    }
				});

	}
}


function ajax(outboundDate, returnDate, from, to, structure, name1, name2,view) {
	month1 = [];
	month2 = [];
	chartData1 = [];
	chartData2 = [];
	chartData1_1 = [];
	chartData2_1 = [];
	var data = {
		"outboundDate" : outboundDate,
		"returnDate" : returnDate,
		"from" : from,
		"to" : to,
		"structure" : structure
	};
	$body = $("body");

	var defer = $.Deferred();

	$.ajax({
		type : "GET",
		contentType : "application/json",
		url : "getMoney",
		data : data,
		dataType : 'json',
		// async: false,

		success : function(data) {
			defer.resolve(data);
		//	console.log("SUCCESS: ", data);
		//	console.log("SUCCESS: ", JSON.stringify(data));
			$.each(data, function(k, v) {
				var data1 = [];
				var data2 = [];
				var data1_1 = [];
				var data2_1 = [];
				$.each(v.moneyList, function(s) {
	
					$.each(v.moneyList[s], function(k, t) {
						if (k != "name") {
							var monthSource = new Object();
							monthSource.title = v.airportName + " " + t;
							monthSource.start = k;
							
							var index = k.split('-')[2];
							if (s == 0) {
								month1.push(monthSource);
								if(index <= 15){
									data1[index-1] = parseInt(t.replace(/[a-zA-Z]/g,''));
								}else{
									data1_1[index-16] = parseInt(t.replace(/[a-zA-Z]/g,''));
								}
								currency = t.replace(/[0-9]/g,'');
							} else {
								month2.push(monthSource);
								if(index <= 15){
									data2[index-1] =parseInt(t.replace(/[a-zA-Z]/g,''));
								}else{
									data2_1[index-16] = parseInt(t.replace(/[a-zA-Z]/g,''));
								}
							}
						} 
					});
				
					
						
					
				});
				var chartSource1 = new Object();
				var chartSource2 = new Object();
				var chartSource1_1 = new Object();
				var chartSource2_1 = new Object();
				if (!jQuery.isEmptyObject(data1)) {
					chartSource1.name = v.airportName.trim();
					chartSource1.data = data1;
					chartData1.push(chartSource1);
				}
				if (!jQuery.isEmptyObject(data1_1)) {
					chartSource1_1.name = v.airportName.trim();
					chartSource1_1.data = data1_1;
					chartData1_1.push(chartSource1_1);
				}
				if (!jQuery.isEmptyObject(data2)) {
					chartSource2.name = v.airportName.trim();
					chartSource2.data = data2;
					chartData2.push(chartSource2);
				}
				if (!jQuery.isEmptyObject(data2_1)) {
					chartSource2_1.name = v.airportName.trim();
					chartSource2_1.data = data2_1;
					chartData2_1.push(chartSource2_1);
				}
			});

			$('#calendar').fullCalendar('removeEvents');
			$('#calendar').fullCalendar('addEventSource', month1);
			if (structure == "RoundTrip") {
				$('#calendar1').fullCalendar('removeEvents');
				$('#calendar1').fullCalendar('addEventSource', month2);
				if ( new Date($.datepicker.formatDate('yy/mm',new Date(returnDate))).getTime() <=  new Date($.datepicker.formatDate('yy/mm',new Date(outboundDate))).getTime()){
					$('#calendar1 .fc-prev-button').prop('disabled', true);
					$('#calendar1 .fc-prev-button').css('opacity', 0.5);
					$('#calendar .fc-next-button').attr('disabled', true);
					$('#calendar .fc-next-button').css('opacity', 0.5);
					
				}else{
					$('#calendar1 .fc-prev-button').attr('disabled', false);
					$('#calendar1 .fc-prev-button').css('opacity', 1);
					$('#calendar .fc-next-button').attr('disabled', false);
					$('#calendar .fc-next-button').css('opacity', 1);
				}
			}
			
			if(view == 0){
				browserCal();
				
				
			}else {
				browserChart();
			}
			$('#moneyFind').hide();
			$("#selectTicket").show();
			$("fromAndTo").text(name1+"->"+name2);
			
			window.location.href = '#services';
			stat = "1";
			return true;
		},
		error : function(e) {

			console.log("ERROR: ", e);
			alert("查詢失敗");
			
			
			return false;
		},
		done : function(e) {
			console.log("DONE");

		}
	});

	return false;
}

//去程 chart
function chart(status){

	var data = "" ;
	var dateOut = "";
	if("0" == status){
		data = JSON.stringify(chartData1).replace(/null/g,'0');
		dateOut = date1;
	}else{
		data = JSON.stringify(chartData1_1).replace(/null/g,'0');
		dateOut = date1_1;
	}

	 var options =  {
	        chart: {
	        	renderTo: 'container',
	            type: 'column'
	        },
	        title: {
	            text: namefrom + "->" + nameto
	        },
	        subtitle: {
	            text: $.datepicker.formatDate('yy/mm',new Date(outboundD))
	        },
	        tooltip: {
	        	useHTML: true,
	            headerFormat: '<img src="resources/img/{series.name}.png" title="" alt="" border="0" height="50" width="150"/><p>'+$.datepicker.formatDate('yy/mm',new Date(outboundD))+'/{point.key}<table>',
	            pointFormat: '<tr><td>{series.name}: {point.y}</td>',
	            footerFormat: '</table>'
	         
	        },
	        xAxis: {
	            categories: dateOut
	        },
	        yAxis: {
	            min: 0,
	            title: {
	            	text: '最低票價(' + currency +')' 
	            },
	            labels: {
	                formatter: function(){
	                	return Highcharts.numberFormat(this.value,0);
	                }
	            }
	        },
	        credits: {
	            enabled: false
	        },
	        series: JSON.parse(data)
	
	       
	  };

	         new Highcharts.Chart(options);
}
//回程CHART
function returnChart(status){
	var data = "" ;
	var dateOut = "";
	if("0" == status){
		data = JSON.stringify(chartData2).replace(/null/g,'0');
		dateOut = date2;
	}else{
		data = JSON.stringify(chartData2_1).replace(/null/g,'0');
		dateOut = date2_1;
	}

	 var options =  {
	        chart: {
	        	renderTo: 'container1',
	            type: 'column'
	        },
	        title: {
	            text: nameto + "->" + namefrom
	        },
	        subtitle: {
	            text:  $.datepicker.formatDate('yy/mm',new Date(returnD))
	        },
	        tooltip: {
	        	useHTML: true,
	            headerFormat: '<img src="resources/img/{series.name}.png" title="" alt="" border="0" height="50" width="150"/><p>'+$.datepicker.formatDate('yy/mm',new Date(returnD))+'/{point.key}<table>',
	            pointFormat: '<tr><td>{series.name}: {point.y}</td>',
	            footerFormat: '</table>'
	         
	        },
	        xAxis: {
	            categories: dateOut
	        },
	        yAxis: {
	            min: 0,
	            title: {
	                text: '最低票價(' + currency +')' 
	            },
	            labels: {
	                formatter: function(){
	                	return Highcharts.numberFormat(this.value,0);
	                }
	            }
	        },
	        credits: {
	            enabled: false
	        },
	        series: JSON.parse(data)
	
	       
	  };

	         new Highcharts.Chart(options);
}
//去程下15天
function nextChart(){
	chart("1");
	$('#next').hide();
	$('#before').show();

}
//去程前15天
function beforeChart(){
	chart("0");
	$('#next').show();
	$('#before').hide();
	
}

//回程下15天
function nextReturnChart(){
	returnChart("1");
	$('#next1').hide();
	$('#before1').show();
	
}
//回程stat =="1" ;
function beforeReturnChart(){
	returnChart("0");
	$('#next1').show();
	$('#before1').hide();
	
}

// 預覽柱狀圖
function browserChart(){
	var dtToday = new Date();
	var today = dtToday.getFullYear()+"/"+(dtToday.getMonth()+1+"/01");
	$('#money').hide();
	$('#chart').show();

	
	if(new Date(today).getTime() >=  new Date($.datepicker.formatDate('yy/mm',new Date(outboundD))+"/01").getTime()){
		$('#beforeMonth').hide();
	}else{
		$('#beforeMonth').show();
	}

	if (jQuery.isEmptyObject(chartData1)) {
		nextChart();
		$('#before').hide();
	}else{
		beforeChart();
	}
	if(structureD == "RoundTrip"){
		if(new Date(today).getTime() >=  new Date($.datepicker.formatDate('yy/mm',new Date(returnD))+"/01").getTime()){
			$('#before1Month').hide();
		}else{
			$('#before1Month').show();
		}
		if(new Date($.datepicker.formatDate('yy/mm',new Date(returnD))+"/01").getTime() ==  new Date($.datepicker.formatDate('yy/mm',new Date(outboundD))+"/01").getTime()){
			$('#before1Month').hide();
			$('#nextMonth').hide();
		}else{
			$('#before1Month').show();
			$('#nextMonth').show();
		}

		if (jQuery.isEmptyObject(chartData2)) {
			nextReturnChart();
			$('#before1').hide();
		}else {
			beforeReturnChart();
		}
		

	} else {
		$('#next1').hide();
		$('#before1').hide();
		$('#before1Month').hide();
		$('#next1Month').hide();
	}
}

//預覽行事曆
function browserCal(){
	
	$('#money').show();
	$('#chart').hide();
}

function beforeMonth(status){
	stat = "0";
	if(status == 0){ //去程
		//var current = $.datepicker.formatDate('yy/mm/dd',new Date($('#calendar').fullCalendar('getView').intervalStart.format()));
		  var myDate = new Date(outboundD);
		  myDate.setMonth(myDate.getMonth()-1); 
		  outboundD = $.datepicker.formatDate('yy/mm/dd',myDate);
		  $('#money').show();
			createCal( namefrom.substring(namefrom.length - 4,namefrom.length-1), nameto.substring(nameto.length - 4,nameto.length-1));
			ajax(outboundD, returnD, namefrom.substring(namefrom.length - 4,namefrom.length-1), nameto.substring(nameto.length - 4,nameto.length-1), structureD, namefrom, nameto,1);
	
		 var dtToday = new Date();
		 var today = dtToday.getFullYear()+"/"+(dtToday.getMonth()+1+"/01");
			alert("today"+":"+$.datepicker.formatDate('yy/mm',new Date(outboundD)));
			if(new Date($.datepicker.formatDate('yy/mm',new Date(returnD))+"/01").getTime() ==  new Date($.datepicker.formatDate('yy/mm',new Date(outboundD))+"/01").getTime()){
				$('#before1Month').hide();
				$('#nextMonth').hide();
			}else{
				$('#before1Month').show();
				$('#nextMonth').show();
			}
			if(new Date(today).getTime() >=  new Date($.datepicker.formatDate('yy/mm',new Date(outboundD))+"/01").getTime()){
				
				$('#beforeMonth').hide();
			}else {
				$('#beforeMonth').show();
			}
	} else {
		if (structureD == "RoundTrip") {
			$('#calendar1').fullCalendar( 'refetchEvents' );
			$('#calendar1').fullCalendar( 'gotoDate', returnD );
		}
		var myDate = new Date(returnD);
		  myDate.setMonth(myDate.getMonth()-1); 
		  returnD = $.datepicker.formatDate('yy/mm/dd',myDate);
		  $('#money').show();
			createCal( namefrom.substring(namefrom.length - 4,namefrom.length-1), nameto.substring(nameto.length - 4,nameto.length-1));
		ajax(outboundD, returnD, namefrom.substring(namefrom.length - 4,namefrom.length-1), nameto.substring(nameto.length - 4,nameto.length-1), structureD, namefrom, nameto,1);
		var dtToday = new Date();
		var today = dtToday.getFullYear()+"/"+(dtToday.getMonth()+1+"/01");
		if(new Date(today).getTime() >=  new Date($.datepicker.formatDate('yy/mm',new Date(returnD))+"/01").getTime()){
			$('#before1Month').hide();
		}else {
			$('#before1Month').show();
		}
		if(new Date($.datepicker.formatDate('yy/mm',new Date(returnD))+"/01").getTime() ==  new Date($.datepicker.formatDate('yy/mm',new Date(outboundD))+"/01").getTime()){
			$('#before1Month').hide();
			$('#nextMonth').hide();
		}else{
			$('#before1Month').show();
			$('#nextMonth').show();
		}
		
	}
	
	
}
function checkDate(outboundD,returnD){
	 var fromDate = $.datepicker.formatDate('yy/mm',new Date(outboundD));
	 var fromto = $.datepicker.formatDate('yy/mm',new Date(returnD));

	 if(fromDate > fromto) {
		 alert("出發日不能小於回程日");
		 return false ;
	 }
	 return true ;
	
}
//下個月
function nextMonth(status){
	
	stat = "0";
	if (status == 0) { // 去程

		var myDate = new Date(outboundD);
		myDate.setMonth(myDate.getMonth() + 1);
		if (checkDate(myDate, returnD)) {
			outboundD = $.datepicker.formatDate('yy/mm/dd', myDate);
			$('#money').show();
			createCal( namefrom.substring(namefrom.length - 4,namefrom.length-1), nameto.substring(nameto.length - 4,nameto.length-1));
			ajax(outboundD, returnD, namefrom.substring(namefrom.length - 4,
					namefrom.length - 1), nameto.substring(nameto.length - 4,
					nameto.length - 1), structureD, namefrom, nameto, 1);
			
		
		}
		
		var dtToday = new Date();
		var today = dtToday.getFullYear()+"/"+(dtToday.getMonth()+1+"/01");
	
		if(new Date($.datepicker.formatDate('yy/mm',new Date(returnD))+"/01").getTime() ==  new Date($.datepicker.formatDate('yy/mm',new Date(outboundD))+"/01").getTime()){
			$('#before1Month').hide();
			$('#nextMonth').hide();
		}else{
			$('#before1Month').show();
			$('#nextMonth').show();
		}
		if(new Date(today).getTime() >=  new Date($.datepicker.formatDate('yy/mm',new Date(outboundD))+"/01").getTime()){
			
			$('#beforeMonth').hide();
		}else {
			$('#beforeMonth').show();
		}
	} else { //回程
		if (structureD == "RoundTrip") {

			$('#calendar1').fullCalendar('refetchEvents');
			$('#calendar1').fullCalendar('gotoDate', returnD);
		}
		var myDate = new Date(returnD);
		  myDate.setMonth(myDate.getMonth()+1); 
		  returnD = $.datepicker.formatDate('yy/mm/dd',myDate);
		  $('#money').show();
			createCal( namefrom.substring(namefrom.length - 4,namefrom.length-1), nameto.substring(nameto.length - 4,nameto.length-1));
		ajax(outboundD, returnD, namefrom.substring(namefrom.length - 4,namefrom.length-1), nameto.substring(nameto.length - 4,nameto.length-1), structureD, namefrom, nameto,1);
		var dtToday = new Date();
		var today = dtToday.getFullYear()+"/"+(dtToday.getMonth()+1+"/01");
		if(new Date(today).getTime() >=  new Date($.datepicker.formatDate('yy/mm',new Date(returnD))+"/01").getTime()){
			$('#before1Month').hide();
		}else {
			$('#before1Month').show();
		}
		if(new Date($.datepicker.formatDate('yy/mm',new Date(returnD))+"/01").getTime() ==  new Date($.datepicker.formatDate('yy/mm',new Date(outboundD))+"/01").getTime()){
			$('#before1Month').hide();
			$('#nextMonth').hide();
		}else{
			$('#before1Month').show();
			$('#nextMonth').show();
		}
		
	}
	

	
}


function airUrl(){
	if($("#airNameHidden").val() == ""){
		alert("請選擇出發日期");
		return false;
	}
	if(structureD == "RoundTrip"){
		if($("#airName1Hidden").val() == ""){
			alert("請選擇回程日期");
			return false;
		}
		
		
	}
	
	alert("最後價格以官網價格為主");
	
	var airName = [];
	airName.push($("#airNameHidden").val());

	if ($("#airNameHidden").val() != $("#airName1Hidden").val()) {
		airName.push($("#airName1Hidden").val());
	}

	var data = {
			"outboundDate" : $("#airDept").val(),
			"returnDate" : $("#airRet").val(),
			"from" :  namefrom.substring(namefrom.length - 4,namefrom.length-1),
			"to" : nameto.substring(nameto.length - 4,nameto.length-1),
			"structure" : structureD,
			"airName" : airName
		};
	var peach = {
	   "flight_search_parameter[0][departure_date]":$("#airDept").val(),
	"flight_search_parameter[0][departure_airport_code]":namefrom.substring(namefrom.length - 4,namefrom.length-1),
	"flight_search_parameter[0][arrival_airport_code]": nameto.substring(nameto.length - 4,nameto.length-1),
	"flight_search_parameter[0][is_return]":"true",
	"flight_search_parameter[0][return_date]":$("#airRet").val(),
	"adult_count":"1",
	"child_count":"0",
	"infant_count":"0"
	};
	
	$.ajax({
		type : "GET",
		contentType : "application/json",
		url : "getUrl",
		data : data,
		dataType : 'json',
	
		success : function(data) {
			$.each(data, function(k, v) {
				var html = "";
				if(v.indexOf("air.buy") >= 0){
					html = v.split("air.buy")[1];
					/*var OpenWindow = window.open(v.split("air.buy")[0],'_blank');
					var text = document.createTextNode('hi');
					OpenWindow.document.body.appendChild(html);*/
					
					/*var w = window.open("sample.html",'_blank');
					  var html = v.split("air.buy")[0];
					  localStorage.setItem('AdminId', idvalue);
					  localStorage.setItem('AdminId', idvalue);
					  localStorage.setItem('AdminId', idvalue);
					  localStorage.setItem('AdminId', idvalue);
					    $(w.document.body).html(html);*/
					var w = window.open( v.split("air.buy")[0], '_blank');
					w.document.localStorage.setItem('access_uuid', "19b4deb2-c5c7-47a7-a200-8c20214cb90b");
					w.document.localStorage.setItem('app_session_uuid', "11d69982-88c2-4165-a28e-a0470467646c");
					w.document.localStorage.setItem('client_reqid', "32cbef3d-4e07-417f-bbb8-ca5bfecc1479");
					w.document.localStorage.setItem('_session_id', "285af8eb1560640602f3efa7e86e3d74");
					w.document.body.innerHTML =  v.split("air.buy")[1];
				} else {
					window.open(v, '_blank');
				}
			
				
			});
			
			/*$.post("https://booking.flypeach.com/tw",peach,function(result){
				window.open(result, '_blank');
			  });*/
		},
		error : function(e) {

			console.log("ERROR: ", e);
			alert("開啟失敗");
			
			
			return false;
		},
		done : function(e) {
			console.log("DONE");

		}
	});
	
	
//	https://booking.airasia.com/Flight/Select?c=true&s=false&r=true&o1=TPE&d1=SYD&dd1=2017-01-10&dd2=2017-02-10
}